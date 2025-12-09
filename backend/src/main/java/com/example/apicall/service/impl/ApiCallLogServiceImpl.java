package com.example.apicall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.apicall.dto.ApiCallLogDTO;
import com.example.apicall.domain.ApiCallLog;
import com.example.apicall.repository.ApiCallLogRepository;
import com.example.apicall.service.ApiCallLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiCallLogServiceImpl implements ApiCallLogService {

    @Autowired
    private ApiCallLogRepository apiCallLogRepository;

    @Override
    public List<ApiCallLogDTO> listCallLogsByTestCaseId(String testCaseId) {
        List<ApiCallLog> apiCallLogs = apiCallLogRepository.selectList(new LambdaQueryWrapper<ApiCallLog>()
                .eq(ApiCallLog::getTestCaseId, testCaseId));
        return apiCallLogs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApiCallLogDTO> listCallLogsByApiId(String apiId) {
        List<ApiCallLog> apiCallLogs = apiCallLogRepository.selectList(new LambdaQueryWrapper<ApiCallLog>()
                .eq(ApiCallLog::getApiId, apiId));
        return apiCallLogs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ApiCallLogDTO getCallLogById(String id) {
        ApiCallLog apiCallLog = apiCallLogRepository.selectById(id);
        return toDto(apiCallLog);
    }

    @Override
    public ApiCallLogDTO addCallLog(ApiCallLogDTO apiCallLogDTO) {
        ApiCallLog apiCallLog = toEntity(apiCallLogDTO);
        Date now = new Date();
        apiCallLog.setCallTime(now);
        apiCallLogRepository.insert(apiCallLog);
        return toDto(apiCallLog);
    }

    @Override
    public boolean deleteCallLog(String id) {
        return apiCallLogRepository.deleteById(id) > 0;
    }
    
    /**
     * 将ApiCallLog转换为ApiCallLogDTO
     */
    private ApiCallLogDTO toDto(ApiCallLog apiCallLog) {
        if (apiCallLog == null) {
            return null;
        }
        
        ApiCallLogDTO dto = new ApiCallLogDTO();
        dto.setId(apiCallLog.getId());
        dto.setApiId(apiCallLog.getApiId());
        dto.setTestCaseId(apiCallLog.getTestCaseId());
        dto.setRequestParams(apiCallLog.getRequestParams());
        dto.setActualResult(apiCallLog.getActualResult());
        dto.setStatusCode(apiCallLog.getStatusCode());
        dto.setResponseTime(apiCallLog.getResponseTime());
        dto.setCallTime(apiCallLog.getCallTime());
        return dto;
    }
    
    /**
     * 将ApiCallLogDTO转换为ApiCallLog
     */
    private ApiCallLog toEntity(ApiCallLogDTO apiCallLogDTO) {
        if (apiCallLogDTO == null) {
            return null;
        }
        
        ApiCallLog entity = new ApiCallLog();
        entity.setId(apiCallLogDTO.getId());
        entity.setApiId(apiCallLogDTO.getApiId());
        entity.setTestCaseId(apiCallLogDTO.getTestCaseId());
        entity.setRequestParams(apiCallLogDTO.getRequestParams());
        entity.setActualResult(apiCallLogDTO.getActualResult());
        entity.setStatusCode(apiCallLogDTO.getStatusCode());
        entity.setResponseTime(apiCallLogDTO.getResponseTime());
        entity.setCallTime(apiCallLogDTO.getCallTime());
        return entity;
    }
}