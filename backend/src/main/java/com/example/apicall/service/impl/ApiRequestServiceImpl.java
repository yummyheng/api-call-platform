package com.example.apicall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.apicall.common.constant.SystemConstants;
import com.example.apicall.dto.ApiRequestDTO;
import com.example.apicall.domain.ApiRequest;
import com.example.apicall.repository.ApiRequestRepository;
import com.example.apicall.service.ApiRequestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApiRequestServiceImpl implements ApiRequestService {

    @Autowired
    private ApiRequestRepository apiRequestRepository;

    @Override
    public List<ApiRequestDTO> listRequestsByProjectId(String projectId) {
        List<ApiRequest> apiRequests = apiRequestRepository.selectList(new LambdaQueryWrapper<ApiRequest>()
                .eq(ApiRequest::getProjectId, projectId));
        return apiRequests.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ApiRequestDTO getRequestById(String id) {
        ApiRequest apiRequest = apiRequestRepository.selectById(id);
        return toDto(apiRequest);
    }

    @Override
    public ApiRequestDTO addRequest(ApiRequestDTO apiRequestDTO) {
        // 基于URL和请求报文进行去重检查
        
        // 查询是否已存在相同URL和请求报文的API请求
        LambdaQueryWrapper<ApiRequest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiRequest::getProjectId, apiRequestDTO.getProjectId())
                   .eq(ApiRequest::getApiUrl, apiRequestDTO.getApiUrl());
        
        // 处理请求体的null和空字符串情况，视为相同
        String requestBody = apiRequestDTO.getRequestBody();
        if (StringUtils.isEmpty(requestBody)) {
            // 如果请求体是空字符串或null，查询条件为requestBody is null or requestBody = ''
            queryWrapper.and(wrapper -> wrapper.isNull(ApiRequest::getRequestBody).or().eq(ApiRequest::getRequestBody, SystemConstants.EMPTY_STRING));
        } else {
            queryWrapper.eq(ApiRequest::getRequestBody, requestBody);
        }
        
        // 查询是否存在重复记录，使用list而不是selectOne，避免TooManyResultsException
        List<ApiRequest> existingRequests = apiRequestRepository.selectList(queryWrapper);
        if (existingRequests != null && !existingRequests.isEmpty()) {
            // 如果有多个重复记录，只返回第一条
            ApiRequest existingRequest = existingRequests.get(0);
            log.info("[API请求-保存] 去重检查: 已存在相同的API请求，项目ID: {}, API URL: {}, 已存在ID: {}", 
                    apiRequestDTO.getProjectId(), apiRequestDTO.getApiUrl(), existingRequest.getId());
            return toDto(existingRequest);
        }
        
        // 如果不存在，则创建新记录
        log.info("[API请求-保存] 去重检查: 不存在相同的API请求，将创建新记录");
        ApiRequest apiRequest = toEntity(apiRequestDTO);
        Date now = new Date();
        apiRequest.setCreatedTime(now);
        apiRequest.setUpdatedTime(now);
        
        apiRequestRepository.insert(apiRequest);
        log.info("[API请求-保存] 数据库插入完成，生成的ID: {}", apiRequest.getId());
        
        return toDto(apiRequest);
    }

    @Override
    public ApiRequestDTO updateRequest(ApiRequestDTO apiRequestDTO) {
        ApiRequest apiRequest = toEntity(apiRequestDTO);
        apiRequest.setUpdatedTime(new Date());
        apiRequestRepository.updateById(apiRequest);
        return toDto(apiRequest);
    }
    
    // 手动实现DTO和Entity之间的转换
    private ApiRequestDTO toDto(ApiRequest apiRequest) {
        if (apiRequest == null) {
            return null;
        }
        ApiRequestDTO apiRequestDTO = new ApiRequestDTO();
        BeanUtils.copyProperties(apiRequest, apiRequestDTO);
        return apiRequestDTO;
    }
    
    private ApiRequest toEntity(ApiRequestDTO apiRequestDTO) {
        if (apiRequestDTO == null) {
            return null;
        }
        ApiRequest apiRequest = new ApiRequest();
        BeanUtils.copyProperties(apiRequestDTO, apiRequest);
        return apiRequest;
    }

    @Override
    public boolean deleteRequest(String id) {
        return apiRequestRepository.deleteById(id) > 0;
    }
}