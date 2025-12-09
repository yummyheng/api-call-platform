package com.example.apicall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.example.apicall.common.constant.BooleanValueConstants;
import com.example.apicall.common.constant.SystemConstants;
import com.example.apicall.domain.ApiEnvironment;
import com.example.apicall.dto.ApiEnvironmentDTO;
import com.example.apicall.service.ApiEnvironmentService;
import com.example.apicall.utils.JsonUtils;

/**
 * 环境配置服务实现类
 */
@Service
@Slf4j
public class ApiEnvironmentServiceImpl implements ApiEnvironmentService {

    @Autowired
    private com.example.apicall.mapper.ApiEnvironmentMapper environmentMapper;

    @Override
    public List<ApiEnvironmentDTO> getAllEnvironments() {
        log.info("getAllEnvironments() - Request received");
        List<ApiEnvironment> environments = environmentMapper.selectList(null);
        List<ApiEnvironmentDTO> dtos = new ArrayList<>();
        for (ApiEnvironment env : environments) {
            dtos.add(toDto(env));
        }
        log.info("getAllEnvironments() - Returning {} environments", dtos.size());
        return dtos;
    }

    @Override
    public ApiEnvironmentDTO getEnvironmentById(String id) {
        log.info("getEnvironmentById() - Request received for id: {}", id);
        ApiEnvironment environment = environmentMapper.selectById(id);
        return toDto(environment);
    }



    @Override
    @Transactional
    public ApiEnvironmentDTO addEnvironment(ApiEnvironmentDTO environmentDTO) {
        log.info("addEnvironment() - Request received for environment: {}", environmentDTO);
        
        ApiEnvironment environment = new ApiEnvironment();
        environment.setEnvName(environmentDTO.getEnvName());
        environment.setBaseUrl(environmentDTO.getBaseUrl());
        environment.setAppId(environmentDTO.getAppId());
        environment.setHeaders(JsonUtils.toJsonString(environmentDTO.getHeaders()));
        environment.setParams(JsonUtils.toJsonString(environmentDTO.getParams()));
        environment.setIsDefault(environmentDTO.getIsDefault() ? 1 : 0);
        environment.setCreatedAt(new Date());
        environment.setUpdatedAt(new Date());
        
        // 如果是默认环境，先将其他环境设置为非默认
        if (Boolean.TRUE.equals(environmentDTO.getIsDefault())) {
            environmentMapper.updateDefaultStatus(environment.getAppId(), false);
        }
        
        environmentMapper.insert(environment);
        log.info("addEnvironment() - Environment added successfully with id: {}", environment.getId());
        return toDto(environment);
    }

    @Override
    @Transactional
    public ApiEnvironmentDTO updateEnvironment(String id, ApiEnvironmentDTO environmentDTO) {
        log.info("updateEnvironment() - Request received for id: {} with environment: {}", id, environmentDTO);
        
        ApiEnvironment existingEnvironment = environmentMapper.selectById(id);
        if (existingEnvironment == null) {
            throw new RuntimeException(SystemConstants.ERROR_ENVIRONMENT_NOT_FOUND + id);
        }
        
        existingEnvironment.setEnvName(environmentDTO.getEnvName());
        existingEnvironment.setBaseUrl(environmentDTO.getBaseUrl());
        existingEnvironment.setHeaders(JsonUtils.toJsonString(environmentDTO.getHeaders()));
        existingEnvironment.setParams(JsonUtils.toJsonString(environmentDTO.getParams()));
        existingEnvironment.setIsDefault(environmentDTO.getIsDefault() ? 1 : 0);
        existingEnvironment.setUpdatedAt(new Date());
        
        // 如果是默认环境，先将其他环境设置为非默认
        if (Boolean.TRUE.equals(environmentDTO.getIsDefault())) {
            environmentMapper.updateDefaultStatus(existingEnvironment.getAppId(), false);
        }
        
        environmentMapper.updateById(existingEnvironment);
        log.info("updateEnvironment() - Environment updated successfully with id: {}", id);
        return toDto(existingEnvironment);
    }

    @Override
    public boolean deleteEnvironment(String id) {
        log.info("deleteEnvironment() - Request received for id: {}", id);
        int result = environmentMapper.deleteById(id);
        boolean success = result > 0;
        log.info("deleteEnvironment() - Environment deleted successfully: {}", success);
        return success;
    }

    @Override
    @Transactional
    public boolean setDefaultEnvironment(String id, String appId) {
        log.info("setDefaultEnvironment() - Request received for id: {} and appId: {}", id, appId);
        
        // 先将所有环境设置为非默认
        environmentMapper.updateDefaultStatus(appId, false);
        
        // 将指定环境设置为默认
        ApiEnvironment environment = environmentMapper.selectById(id);
        if (environment == null) {
            throw new RuntimeException(SystemConstants.ERROR_ENVIRONMENT_NOT_FOUND + id);
        }
        
        environment.setIsDefault(BooleanValueConstants.TRUE_VALUE);
        environment.setUpdatedAt(new Date());
        int result = environmentMapper.updateById(environment);
        
        boolean success = result > 0;
        log.info("setDefaultEnvironment() - Environment set as default successfully: {}", success);
        return success;
    }
    
    /**
      * 将ApiEnvironment转换为ApiEnvironmentDTO
      */
     private ApiEnvironmentDTO toDto(ApiEnvironment environment) {
         if (environment == null) {
             return null;
         }
         
         ApiEnvironmentDTO dto = new ApiEnvironmentDTO();
         dto.setId(environment.getId());
         dto.setEnvName(environment.getEnvName());
         dto.setBaseUrl(environment.getBaseUrl());
         dto.setAppId(environment.getAppId());
         dto.setHeaders(JsonUtils.toKeyPairList(environment.getHeaders()));
         dto.setParams(JsonUtils.toKeyPairList(environment.getParams()));
         dto.setIsDefault(environment.getIsDefault() == 1);
         dto.setCreatedAt(environment.getCreatedAt());
         dto.setUpdatedAt(environment.getUpdatedAt());
         return dto;
     }
}