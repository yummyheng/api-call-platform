package com.example.apicall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.apicall.dto.ApiTestCaseDTO;
import com.example.apicall.domain.ApiTestCase;
import com.example.apicall.repository.ApiTestCaseRepository;
import com.example.apicall.service.ApiTestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiTestCaseServiceImpl implements ApiTestCaseService {

    @Autowired
    private ApiTestCaseRepository apiTestCaseRepository;

    @Override
    public List<ApiTestCaseDTO> listTestCasesByApiId(String apiId) {
        List<ApiTestCase> apiTestCases = apiTestCaseRepository.selectList(new LambdaQueryWrapper<ApiTestCase>()
                .eq(ApiTestCase::getApiId, apiId));
        return apiTestCases.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ApiTestCaseDTO getTestCaseById(String id) {
        ApiTestCase apiTestCase = apiTestCaseRepository.selectById(id);
        return toDto(apiTestCase);
    }

    @Override
    public ApiTestCaseDTO addTestCase(ApiTestCaseDTO apiTestCaseDTO) {
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ApiTestCaseServiceImpl.class);
        logger.debug("[测试用例-保存] 开始保存测试用例，测试用例名称: {}, API ID: {}", apiTestCaseDTO.getTestCaseName(), apiTestCaseDTO.getApiId());
        
        ApiTestCase apiTestCase = toEntity(apiTestCaseDTO);
        Date now = new Date();
        apiTestCase.setCreatedTime(now);
        apiTestCase.setUpdatedTime(now);
        
        logger.debug("[测试用例-保存] 转换为实体对象完成，准备插入数据库");
        apiTestCaseRepository.insert(apiTestCase);
        logger.debug("[测试用例-保存] 数据库插入完成，生成的ID: {}", apiTestCase.getId());
        
        ApiTestCaseDTO result = toDto(apiTestCase);
        logger.debug("[测试用例-保存] 返回DTO对象: {}", result.getTestCaseName());
        return result;
    }

    @Override
    public ApiTestCaseDTO updateTestCase(ApiTestCaseDTO apiTestCaseDTO) {
        ApiTestCase apiTestCase = toEntity(apiTestCaseDTO);
        apiTestCase.setUpdatedTime(new Date());
        apiTestCaseRepository.updateById(apiTestCase);
        return toDto(apiTestCase);
    }
    
    /**
     * 将ApiTestCase转换为ApiTestCaseDTO
     */
    private ApiTestCaseDTO toDto(ApiTestCase apiTestCase) {
        if (apiTestCase == null) {
            return null;
        }
        
        ApiTestCaseDTO dto = new ApiTestCaseDTO();
        dto.setId(apiTestCase.getId());
        dto.setApiId(apiTestCase.getApiId());
        dto.setTestCaseName(apiTestCase.getTestCaseName());
        dto.setTestData(apiTestCase.getTestData());
        dto.setExpectedResult(apiTestCase.getExpectedResult());
        dto.setIsEnabled(apiTestCase.getIsEnabled());
        dto.setCreatedBy(apiTestCase.getCreatedBy());
        dto.setCreatedTime(apiTestCase.getCreatedTime());
        dto.setUpdatedBy(apiTestCase.getUpdatedBy());
        dto.setUpdatedTime(apiTestCase.getUpdatedTime());
        return dto;
    }
    
    /**
     * 将ApiTestCaseDTO转换为ApiTestCase
     */
    private ApiTestCase toEntity(ApiTestCaseDTO apiTestCaseDTO) {
        if (apiTestCaseDTO == null) {
            return null;
        }
        
        ApiTestCase entity = new ApiTestCase();
        entity.setId(apiTestCaseDTO.getId());
        entity.setApiId(apiTestCaseDTO.getApiId());
        entity.setTestCaseName(apiTestCaseDTO.getTestCaseName());
        entity.setTestData(apiTestCaseDTO.getTestData());
        entity.setExpectedResult(apiTestCaseDTO.getExpectedResult());
        entity.setIsEnabled(apiTestCaseDTO.getIsEnabled());
        entity.setCreatedBy(apiTestCaseDTO.getCreatedBy());
        entity.setCreatedTime(apiTestCaseDTO.getCreatedTime());
        entity.setUpdatedBy(apiTestCaseDTO.getUpdatedBy());
        entity.setUpdatedTime(apiTestCaseDTO.getUpdatedTime());
        return entity;
    }

    @Override
    public boolean deleteTestCase(String id) {
        return apiTestCaseRepository.deleteById(id) > 0;
    }

    @Override
    public boolean updateTestCaseTestData(String id, String testData) {
        ApiTestCase apiTestCase = apiTestCaseRepository.selectById(id);
        if (apiTestCase != null) {
            apiTestCase.setTestData(testData);
            apiTestCase.setUpdatedTime(new Date());
            return apiTestCaseRepository.updateById(apiTestCase) > 0;
        }
        return false;
    }
}