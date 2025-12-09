package com.example.apicall.service.impl;

import com.example.apicall.dto.ApiResponseDTO;
import com.example.apicall.domain.ApiRequest;
import com.example.apicall.domain.ApiTestCase;
import com.example.apicall.model.HttpClientCallDetail;
import com.example.apicall.repository.ApiRequestRepository;
import com.example.apicall.repository.ApiTestCaseRepository;
import com.example.apicall.service.ApiCallService;
import com.example.apicall.service.ApiEnvironmentService;
import com.example.apicall.dto.ApiEnvironmentDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import com.example.apicall.common.constant.HttpStatusConstants;
import com.example.apicall.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.Arrays;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URISyntaxException;
import com.example.apicall.exception.ApiRequestNotFoundException;
import com.example.apicall.exception.ApiTestCaseNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ApiCallServiceImpl implements ApiCallService {

    // 使用fastjson代替Jackson ObjectMapper
    @Autowired
    private ApiRequestRepository apiRequestRepository;

    @Autowired
    private ApiTestCaseRepository apiTestCaseRepository;
    
    @Autowired
    private ApiEnvironmentService apiEnvironmentService;




    public ApiResponseDTO executeTestCase(String testCaseId, String testData) {
        log.info("执行测试用例，测试用例ID: {}", testCaseId);
        return executeTestCase(testCaseId, testData, null);
    }
    
    @Override
    public ApiResponseDTO executeTestCase(String testCaseId, String testData, String environmentId) {
        log.info("执行测试用例，测试用例ID: {}，测试数据: {}，环境ID: {}", testCaseId, testData, environmentId);
        
        // 1. 根据testCaseId查询测试用例
        // 使用LambdaQueryWrapper确保精确匹配字符串类型的ID，避免UUID被解析为数字类型
        ApiTestCase testCase = apiTestCaseRepository.selectOne(new LambdaQueryWrapper<ApiTestCase>()
            .eq(ApiTestCase::getId, testCaseId)
        );
        if (testCase == null) {
            log.error("测试用例不存在，测试用例ID: {}", testCaseId);
            throw new ApiTestCaseNotFoundException(testCaseId);
        }
        
        // 2. 根据测试用例的apiId查询API请求信息
        // 使用LambdaQueryWrapper确保精确匹配字符串类型的ID，避免UUID被解析为数字类型
        ApiRequest apiRequest = apiRequestRepository.selectOne(new LambdaQueryWrapper<ApiRequest>()
            .eq(ApiRequest::getId, testCase.getApiId())
        );
        if (apiRequest == null) {
            log.error("API请求不存在，API ID: {}", testCase.getApiId());
            throw new ApiRequestNotFoundException(testCase.getApiId());
        }
        
        // 3. 确定实际使用的测试数据
        String actualTestData = testData != null ? testData : testCase.getTestData();
        
        // 4. 尝试从testData中提取environmentId（仅作为备选方案）
        // 优先使用前端直接传递的environmentId参数
        if (environmentId == null && actualTestData != null && !actualTestData.isEmpty()) {
            try {
                Map<String, Object> testDataMap = JSON.parseObject(actualTestData, new TypeReference<Map<String, Object>>() {});
                if (testDataMap.containsKey("environmentId")) {
                    Object envIdObj = testDataMap.get("environmentId");
                    if (envIdObj != null) {
                        String extractedEnvId = envIdObj.toString();
                        if (extractedEnvId != null && !extractedEnvId.isEmpty()) {
                            environmentId = extractedEnvId;
                            log.info("从testData中提取到environmentId: {}", environmentId);
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("解析testData中的environmentId失败: {}", e.getMessage());
                // 解析失败时，继续使用原始的testData和environmentId
            }
        }
        
        // 5. 执行HTTP请求
        return executeHttpRequest(apiRequest, actualTestData, environmentId);
    }

    private ApiResponseDTO executeHttpRequest(ApiRequest apiRequest, String requestBody, String environmentId) {
        long startTime = System.currentTimeMillis();
        
        try {
            // 获取环境配置
            ApiEnvironmentDTO environment = null;
            if (environmentId != null) {
                environment = apiEnvironmentService.getEnvironmentById(environmentId);
                log.info("使用环境配置: {} (ID: {})", environment != null ? environment.getEnvName() : "未找到", environmentId);
            }
            
            // 解析请求头
            Map<String, String> headers = new HashMap<>();
            if (apiRequest.getRequestHeaders() != null && !apiRequest.getRequestHeaders().isEmpty()) {
                try {
                    headers = JSON.parseObject(apiRequest.getRequestHeaders(), new TypeReference<Map<String, String>>() {});
                } catch (Exception e) {
                    headers = new HashMap<>();
                }
            }
            
            // 合并环境请求头（环境配置的请求头优先级高于API请求的请求头）
            if (environment != null && environment.getHeaders() != null) {
                for (ApiEnvironmentDTO.KeyPairDTO envHeader : environment.getHeaders()) {
                    headers.put(envHeader.getKey(), envHeader.getValue());
                }
            }
            
            // 构建完整URL（环境的baseUrl + API的url）
            String fullUrl = apiRequest.getApiUrl();
            if (environment != null && environment.getBaseUrl() != null) {
                // 确保URL格式正确
                String baseUrl = environment.getBaseUrl();
                String apiUrl = apiRequest.getApiUrl();
                
                if (baseUrl.endsWith("/")) {
                    baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
                }
                
                if (apiUrl.startsWith("/")) {
                    apiUrl = apiUrl.substring(1);
                }
                
                fullUrl = baseUrl + "/" + apiUrl;
            }
            
            // 创建HTTP客户端调用详情
            HttpClientCallDetail callDetail = HttpClientCallDetail.builder()
                    .requestUrl(fullUrl)
                    .httpMethod(apiRequest.getHttpMethod())
                    .requestHeaders(headers)
                    .requestBody(requestBody)
                    .build();
            callDetail.startExecution();

            // 执行HTTP请求
            HttpResponse httpResponse = HttpUtils.executeRequest(fullUrl, apiRequest.getHttpMethod(), headers, requestBody);

            // 计算耗时
            long elapsedTime = System.currentTimeMillis() - startTime;

            // 处理响应
            ApiResponseDTO responseDTO = ApiResponseDTO.builder()
                    .apiId(apiRequest.getId())
                    .callTime(new Date())
                    .statusCode(httpResponse.getStatusLine().getStatusCode())
                    .success(HttpStatusConstants.isSuccess(httpResponse.getStatusLine().getStatusCode()))
                    .responseHeaders(HttpUtils.parseResponseHeaders(httpResponse))
                    .responseBody(HttpUtils.readResponseBody(httpResponse))
                    .elapsedTime(elapsedTime)
                    .httpClientCallDetail(callDetail)
                    .build();

            // 更新调用详情的响应信息
            callDetail.setResponseStatusCode(httpResponse.getStatusLine().getStatusCode());
            callDetail.setExecutionTimeMs(elapsedTime);
            callDetail.setResponseBody(responseDTO.getResponseBody());
            callDetail.setResponseHeaders(responseDTO.getResponseHeaders());
            callDetail.endExecution();

            return responseDTO;

        } catch (Exception e) {
            // 处理异常
            log.error("请求失败: {}", e.getMessage(), e);
            long elapsedTime = System.currentTimeMillis() - startTime;
            
            // 创建异常情况下的调用详情
            HttpClientCallDetail callDetail = HttpClientCallDetail.builder()
                    .executionTimeMs(elapsedTime)
                    .build();
            callDetail.failExecution(e.getMessage());
            
            // 创建异常情况下的响应DTO
            ApiResponseDTO responseDTO = ApiResponseDTO.builder()
                    .apiId(apiRequest.getId())
                    .callTime(new Date())
                    .success(false)
                    .responseBody("请求失败: " + e.getMessage())
                    .elapsedTime(elapsedTime)
                    .httpClientCallDetail(callDetail)
                    .build();

            return responseDTO;
        }
    }


}