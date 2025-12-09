package com.example.apicall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.apicall.common.constant.*;
import com.example.apicall.common.enums.LogTypeEnum;
import com.example.apicall.common.util.JsonUtil;
import com.example.apicall.common.util.LogParserUtil;
import com.example.apicall.common.util.SensitiveDataUtil;
import com.example.apicall.dto.ApiEnvironmentDTO;
import com.example.apicall.dto.ApiProjectDTO;
import com.example.apicall.dto.ApiRequestDTO;
import com.example.apicall.dto.ApiTestCaseDTO;
import com.example.apicall.service.*;
import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 日志导入服务实现类
 */
@Service
@Slf4j
public class LogImportServiceImpl implements LogImportService {



    @Autowired
    private ApiEnvironmentService apiEnvironmentService;

    @Autowired
    private ApiProjectService apiProjectService;

    @Autowired
    private ApiRequestService apiRequestService;

    @Autowired
    private ApiTestCaseService apiTestCaseService;

    @Autowired
    private ApiCallLogService apiCallLogService;

    @Override
    public Map<String, Object> importLogFile(MultipartFile file, String environmentId, String projectId) {
        log.info("[导入日志-主流程] 开始导入日志文件: {}, 环境ID: {}, 项目ID: {}, 文件大小: {}KB", file.getOriginalFilename(), environmentId, projectId, file.getSize() / 1024);
        Map<String, Object> result = new HashMap<>();
        List<String> successLogs = new ArrayList<>();
        List<String> errorLogs = new ArrayList<>();

        try {
            // 验证环境和项目是否存在
            log.debug("[导入日志-主流程] 开始验证环境和项目，environmentId={}, projectId={}", environmentId, projectId);
            ApiEnvironmentDTO environment = apiEnvironmentService.getEnvironmentById(environmentId);
            log.debug("[导入日志-主流程] 环境验证结果: {}", environment != null ? environment.getEnvName() : "null");
            
            ApiProjectDTO project = apiProjectService.getProjectById(projectId);
            log.debug("[导入日志-主流程] 项目验证结果: {}", project != null ? project.getProjectName() : "null");

            if (environment == null || project == null) {
                result.put(MapKeyConstants.SUCCESS, false);
                result.put(MapKeyConstants.MESSAGE, SystemConstants.ERROR_ENV_PROJECT_NOT_EXIST);
                log.error("[导入日志-主流程] 环境或项目验证失败");
                return result;
            }

            // 读取和解析日志文件
            log.debug("[导入日志-主流程] 开始读取和解析日志文件内容");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            log.debug("[导入日志-主流程] 文件读取流初始化完成");

            // 使用Stream API处理日志文件
            int[] logCount = {0};
            int[] successCount = {0};

            processLogFile(reader, logCount, successCount, successLogs, errorLogs, environmentId, projectId, environment, project);

            log.info("[导入日志-主流程] 日志导入完成，共处理 {} 条，成功 {} 条，失败 {} 条", logCount, successCount, errorLogs.size());
            result.put(MapKeyConstants.SUCCESS, true);
            result.put(MapKeyConstants.MESSAGE, "日志导入完成");
            result.put("totalLogs", logCount);
            result.put("successCount", successCount);
            result.put("errorCount", errorLogs.size());
            result.put("errorDetails", errorLogs);

        } catch (Exception e) {
            log.error("[导入日志-主流程] 导入日志文件失败", e);
            result.put(MapKeyConstants.SUCCESS, false);
            result.put(MapKeyConstants.MESSAGE, "导入日志文件失败: " + e.getMessage());
        }

        return result;
    }
    
    @Override
    public Map<String, Object> importByPath(String filePath, String environmentId, String projectId) {
        log.info("[导入日志-路径] 开始通过文件路径导入日志文件: {}, 环境ID: {}, 项目ID: {}", filePath, environmentId, projectId);
        Map<String, Object> result = new HashMap<>();
        List<String> successLogs = new ArrayList<>();
        List<String> errorLogs = new ArrayList<>();

        try {
            // 验证环境和项目是否存在
            log.debug("[导入日志-路径] 开始验证环境和项目，environmentId={}, projectId={}", environmentId, projectId);
            ApiEnvironmentDTO environment = apiEnvironmentService.getEnvironmentById(environmentId);
            log.debug("[导入日志-路径] 环境验证结果: {}", environment != null ? environment.getEnvName() : "null");
            
            ApiProjectDTO project = apiProjectService.getProjectById(projectId);
            log.debug("[导入日志-路径] 项目验证结果: {}", project != null ? project.getProjectName() : "null");

            if (environment == null || project == null) {
                result.put(MapKeyConstants.SUCCESS, false);
                result.put(MapKeyConstants.MESSAGE, SystemConstants.ERROR_ENV_PROJECT_NOT_EXIST);
                log.error("[导入日志-路径] 环境或项目验证失败");
                return result;
            }

            // 读取和解析日志文件
            log.debug("[导入日志-路径] 开始读取和解析日志文件内容: {}", filePath);
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                result.put(MapKeyConstants.SUCCESS, false);
                result.put(MapKeyConstants.MESSAGE, "指定的日志文件不存在或不是一个有效的文件");
                log.error("[导入日志-路径] 文件不存在或不是有效的文件: {}", filePath);
                return result;
            }
            
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            log.debug("[导入日志-路径] 文件读取流初始化完成");

            // 使用Stream API处理日志文件
            int[] logCount = {0};
            int[] successCount = {0};

            processLogFile(reader, logCount, successCount, successLogs, errorLogs, environmentId, projectId, environment, project);

            log.info("[导入日志-路径] 日志导入完成，共处理 {} 条，成功 {} 条，失败 {} 条", logCount, successCount, errorLogs.size());
            result.put(MapKeyConstants.SUCCESS, true);
            result.put(MapKeyConstants.MESSAGE, "日志导入完成");
            result.put("totalLogs", logCount);
            result.put("successCount", successCount);
            result.put("errorCount", errorLogs.size());
            result.put("errorDetails", errorLogs);

        } catch (Exception e) {
            log.error("[导入日志-路径] 通过文件路径导入日志文件失败", e);
            result.put(MapKeyConstants.SUCCESS, false);
            result.put(MapKeyConstants.MESSAGE, "通过文件路径导入日志文件失败: " + e.getMessage());
        }

        return result;
    }
    
    /**
     * 处理日志文件的通用方法，供文件上传和文件路径导入两种方式复用
     * 按线程号分组并按时间戳排序，解决并行请求的乱序问题
     * 同时实现单个文件内的测试用例去重，同一文件内重复的测试用例只保留一个
     */
    private void processLogFile(BufferedReader reader, int[] logCount, int[] successCount, 
                              List<String> successLogs, List<String> errorLogs, 
                              String environmentId, String projectId, 
                              ApiEnvironmentDTO environment, ApiProjectDTO project) {
        // 创建一个Set来跟踪已经处理过的测试用例（API名称 + 方法 + 请求体的组合）
        Set<String> processedTestCases = new HashSet<>();
        
        // 1. 解析所有日志行，提取线程ID和时间戳信息
        List<LogLine> logLines = reader.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> {
                    try {
                        Optional<Map<String, Object>> logOpt = LogParserUtil.parseLogLine(line);
                        return logOpt.map(logData -> {
                            String threadId = (String) logData.getOrDefault("threadId", "unknown");
                            String timestamp = (String) logData.getOrDefault("timestamp", "1970-01-01 00:00:00.000");
                            return new LogLine(threadId, timestamp, logData, line);
                        }).orElse(null);
                    } catch (Exception e) {
                        log.error("[导入日志-处理] 解析日志行失败: {}", line, e);
                        errorLogs.add("解析失败日志: " + line);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        // 2. 按线程ID分组
        Map<String, List<LogLine>> logLinesByThread = logLines.stream()
                .collect(Collectors.groupingBy(LogLine::getThreadId));
        
        // 3. 对每个线程的日志按时间戳排序并处理
        logLinesByThread.forEach((threadId, threadLogs) -> {
            // 按时间戳排序
            threadLogs.sort(Comparator.comparing(LogLine::getTimestamp));
            
            // 处理排序后的日志
            threadLogs.forEach(logLine -> {
                try {
                    log.trace("[导入日志-处理] 线程 {} 解析日志行: {}", threadId, logLine.getOriginalLine());
                    // 处理日志数据，传入已处理测试用例集合进行去重
                    boolean processed = processLogData(logLine.getLogData(), environmentId, projectId, environment, project, processedTestCases);
                    if (processed) {
                        successCount[0]++;
                        successLogs.add("成功处理日志: " + JSON.toJSONString(logLine.getLogData()));
                        log.debug("[导入日志-处理] 线程 {} 第 {} 条日志处理成功", threadId, logCount[0] + 1);
                    } else {
                        errorLogs.add("处理失败日志: " + JSON.toJSONString(logLine.getLogData()));
                        log.debug("[导入日志-处理] 线程 {} 第 {} 条日志处理失败", threadId, logCount[0] + 1);
                    }
                    logCount[0]++;
                } catch (Exception e) {
                    log.error("[导入日志-处理] 线程 {} 处理日志行失败: {}", threadId, logLine.getOriginalLine(), e);
                    errorLogs.add("处理失败日志: " + logLine.getOriginalLine());
                }
            });
        });
    }
    
    /**
     * 日志行包装类，用于按线程ID和时间戳排序
     */
    private static class LogLine {
        private final String threadId;
        private final String timestamp;
        private final Map<String, Object> logData;
        private final String originalLine;
        
        public LogLine(String threadId, String timestamp, Map<String, Object> logData, String originalLine) {
            this.threadId = threadId;
            this.timestamp = timestamp;
            this.logData = logData;
            this.originalLine = originalLine;
        }
        
        public String getThreadId() {
            return threadId;
        }
        
        public String getTimestamp() {
            return timestamp;
        }
        
        public Map<String, Object> getLogData() {
            return logData;
        }
        
        public String getOriginalLine() {
            return originalLine;
        }
    }



    /**
     * 处理日志数据
     * 实现单个文件内的测试用例去重，同一文件内重复的测试用例只保留一个
     */
    private boolean processLogData(Map<String, Object> logData, String environmentId, String projectId, 
                                  ApiEnvironmentDTO environment, ApiProjectDTO project, 
                                  Set<String> processedTestCases) {
        try {
            // 提取API请求信息，支持新旧两种字段名
            String url = (String) logData.getOrDefault("url", logData.get("requestUrl"));
            String method = (String) logData.getOrDefault("method", logData.get("httpMethod"));
            String requestBody = Optional.ofNullable(logData.get(MapKeyConstants.REQUEST_BODY)).map(Object::toString).orElse(SystemConstants.EMPTY_STRING);
            String requestHeaders = Optional.ofNullable(logData.get(MapKeyConstants.REQUEST_HEADERS)).map(Object::toString).orElse(SystemConstants.EMPTY_BRACES);
            
    
            // 如果是API调用记录类型，尝试从请求参数中提取请求体
            if (StringUtils.isEmpty(requestBody) && LogTypeEnum.API_CALL_RECORD.getValue().equals(logData.get(MapKeyConstants.LOG_TYPE))) {
                String requestParams = (String) logData.get(MapKeyConstants.REQUEST_PARAMS);
                if (requestParams != null && requestParams.contains(LogConstants.REQUEST_BODY_FLAG)) {
                    // 解析 "参数: 请求体: {{{"page":1,"size":10,"environmentId":"dev-1"}}}"
                    int bodyStartIndex = requestParams.indexOf(LogConstants.REQUEST_BODY_FLAG) + LogConstants.REQUEST_BODY_START_OFFSET;
                    if (bodyStartIndex > LogConstants.REQUEST_BODY_START_OFFSET && bodyStartIndex < requestParams.length()) {
                        String bodyStr = requestParams.substring(bodyStartIndex).trim();
                        // 移除可能的三重花括号
                        bodyStr = bodyStr.replaceAll(LogConstants.REQUEST_BODY_TRIPLE_BRACES_REGEX, SystemConstants.EMPTY_STRING);
                        requestBody = bodyStr;
                    }
                }
            }

            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(method)) {
                log.warn("[导入日志-处理] 日志数据缺少URL或方法: {}", JSON.toJSONString(logData));
                return false;
            }

            // 生成API名称和测试用例去重标识
            String apiName = generateApiName(url, method);
            String testCaseIdentifier = generateTestCaseIdentifier(apiName, method, requestBody);
            
            // 检查是否已经处理过相同的测试用例
            if (processedTestCases.contains(testCaseIdentifier)) {
                log.info("[导入日志-处理] 跳过重复测试用例: {}，请求体: {}", 
                        apiName, requestBody.length() > 100 ? requestBody.substring(0, 100) + "..." : requestBody);
                return true; // 跳过重复测试用例，但仍算处理成功
            }

            // 转换为ApiRequestDTO
            ApiRequestDTO apiRequest = convertToApiRequest(url, method, requestHeaders, requestBody, project);
            
            ApiRequestDTO savedApiRequest = apiRequestService.addRequest(apiRequest);
            log.info("[导入日志-处理] API请求保存完成，保存结果: {}, ID: {}", 
                    savedApiRequest != null ? savedApiRequest.getApiName() : "null", 
                    savedApiRequest != null ? savedApiRequest.getId() : "null");

            if (savedApiRequest == null || savedApiRequest.getId() == null) {
                log.warn("[导入日志-处理] 保存API请求失败: {}", JSON.toJSONString(apiRequest));
                return false;
            }

            // 转换为ApiTestCaseDTO
            log.info("[导入日志-处理] API请求保存成功，ID: {}，开始创建测试用例", savedApiRequest.getId());
            ApiTestCaseDTO apiTestCase = convertToApiTestCase(savedApiRequest, logData, environmentId);
            
            apiTestCaseService.addTestCase(apiTestCase);
            log.info("[导入日志-处理] 测试用例保存完成，API ID: {}", savedApiRequest.getId());
            
            // 将测试用例标记为已处理
            processedTestCases.add(testCaseIdentifier);
            log.info("[导入日志-处理] 测试用例已标记为已处理: {}", testCaseIdentifier);

            return true;
        } catch (Exception e) {
            log.error("[导入日志-处理] 处理日志数据失败: {}", JSON.toJSONString(logData), e);
            return false;
        }
    }
    
    /**
     * 生成测试用例的唯一标识，用于单个文件内的测试用例去重
     * 基于API名称、方法和请求体的组合生成标识
     */
    private String generateTestCaseIdentifier(String apiName, String method, String requestBody) {
        // 使用API名称、方法和请求体的组合作为测试用例的唯一标识
        // 对于空请求体，统一使用"EMPTY_BODY"表示
        String bodyIdentifier = StringUtils.isEmpty(requestBody) ? "EMPTY_BODY" : requestBody;
        return apiName + "_" + method + "_" + bodyIdentifier;
    }

    /**
     * 转换为ApiRequestDTO
     */
    private ApiRequestDTO convertToApiRequest(String url, String method, String requestHeaders, 
                                             String requestBody, ApiProjectDTO project) {
        ApiRequestDTO apiRequest = new ApiRequestDTO();
        // 使用project对象的appId和id来正确设置API请求的字段
        apiRequest.setAppId(project.getAppId());
        apiRequest.setProjectId(project.getId());
        apiRequest.setApiName(generateApiName(url, method));
        apiRequest.setApiUrl(url);
        apiRequest.setHttpMethod(method);
        apiRequest.setRequestHeaders(requestHeaders);
        apiRequest.setRequestBody(requestBody);
        apiRequest.setDescription(LogConstants.IMPORTED_API_DESCRIPTION);
        apiRequest.setCreatedBy(SystemConstants.SYSTEM_USER);
        apiRequest.setCreatedTime(new Date());
        apiRequest.setUpdatedBy(SystemConstants.SYSTEM_USER);
        apiRequest.setUpdatedTime(new Date());
        return apiRequest;
    }

    /**
     * 转换为ApiTestCaseDTO
     */
    private ApiTestCaseDTO convertToApiTestCase(ApiRequestDTO apiRequest, Map<String, Object> logData, 
                                               String environmentId) {
        ApiTestCaseDTO apiTestCase = new ApiTestCaseDTO();
        apiTestCase.setApiId(apiRequest.getId()); // 使用getId()代替getApiId()
        apiTestCase.setTestCaseName(generateTestCaseName(apiRequest.getApiName()));
        apiTestCase.setIsEnabled(true);
        
        // 从logData中提取请求体作为测试数据，而不是存储完整日志
        String requestBody = Optional.ofNullable(logData.get(MapKeyConstants.REQUEST_BODY))
                                    .map(Object::toString)
                                    .orElse(SystemConstants.EMPTY_STRING);
        apiTestCase.setTestData(requestBody);
        
        // 从logData中提取响应内容作为预期结果，如果没有则使用默认值
        String expectedResult = Optional.ofNullable(logData.get(MapKeyConstants.RESPONSE_BODY))
                                       .map(Object::toString)
                                       .orElse(LogConstants.IMPORTED_EXPECTED_RESULT);
        apiTestCase.setExpectedResult(expectedResult);
        
        // 环境ID不需要保存到测试用例中
        
        apiTestCase.setCreatedBy(SystemConstants.SYSTEM_USER);
        apiTestCase.setCreatedTime(new Date());
        apiTestCase.setUpdatedBy(SystemConstants.SYSTEM_USER);
        apiTestCase.setUpdatedTime(new Date());
        return apiTestCase;
    }

    /**
     * 生成API名称
     */
    private String generateApiName(String url, String method) {
        // 从URL中提取路径部分
        String path = url;
        if (url.contains(SystemConstants.URL_PROTOCOL_SEPARATOR)) {
            path = url.substring(url.indexOf(SystemConstants.URL_PROTOCOL_SEPARATOR) + 3);
            if (path.contains(SystemConstants.SLASH_SEPARATOR)) {
                path = path.substring(path.indexOf(SystemConstants.SLASH_SEPARATOR) + 1);
            }
        }
        // 移除查询参数
        if (path.contains(SystemConstants.URL_PARAM_SEPARATOR)) {
            path = path.substring(0, path.indexOf(SystemConstants.URL_PARAM_SEPARATOR));
        }
        // 替换特殊字符
        path = path.replaceAll(LogConstants.API_NAME_REGEX_SEPARATOR, SystemConstants.SPACE_SEPARATOR).trim();
        // 转换为驼峰命名
        String[] parts = path.split(LogConstants.API_NAME_REGEX_SPACE);
        StringBuilder apiName = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                apiName.append(Character.toUpperCase(part.charAt(0)))
                      .append(part.substring(1).toLowerCase());
            }
        }
        return apiName.length() > 0 ? apiName.toString() : method + "_API";
    }

    /**
     * 生成测试用例名称
     */
    private String generateTestCaseName(String apiName) {
        return apiName + TestCaseConstants.TEST_CASE_SUFFIX;
    }
}
