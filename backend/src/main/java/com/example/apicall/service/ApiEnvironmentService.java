package com.example.apicall.service;

import com.example.apicall.dto.ApiEnvironmentDTO;

import java.util.List;

/**
 * 环境配置服务接口
 */
public interface ApiEnvironmentService {
    /**
     * 获取所有环境配置
     * @return 环境配置列表
     */
    List<ApiEnvironmentDTO> getAllEnvironments();

    /**
     * 获取环境配置通过ID
     * @param id 环境ID
     * @return 环境配置
     */
    ApiEnvironmentDTO getEnvironmentById(String id);

    /**
     * 新增环境配置
     * @param environmentDTO 环境配置DTO
     * @return 新增的环境配置
     */
    ApiEnvironmentDTO addEnvironment(ApiEnvironmentDTO environmentDTO);

    /**
     * 更新环境配置
     * @param id 环境ID
     * @param environmentDTO 环境配置DTO
     * @return 更新后的环境配置
     */
    ApiEnvironmentDTO updateEnvironment(String id, ApiEnvironmentDTO environmentDTO);

    /**
     * 删除环境配置
     * @param id 环境ID
     * @return 是否删除成功
     */
    boolean deleteEnvironment(String id);

    /**
     * 设置默认环境
     * @param id 环境ID
     * @param appId 应用ID
     * @return 是否设置成功
     */
    boolean setDefaultEnvironment(String id, String appId);
}