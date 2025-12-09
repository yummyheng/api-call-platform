package com.example.apicall.controller;

import com.example.apicall.dto.ApiEnvironmentDTO;
import com.example.apicall.service.ApiEnvironmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 环境配置控制器
 */
@RestController
@RequestMapping("/environments")
@Slf4j
public class ApiEnvironmentController {

    @Autowired
    private ApiEnvironmentService apiEnvironmentService;

    /**
     * 获取所有环境配置
     * @return 环境配置列表
     */
    @GetMapping
    public List<ApiEnvironmentDTO> getAllEnvironments() {
        log.info("getAllEnvironments() - Request received");
        List<ApiEnvironmentDTO> environments = apiEnvironmentService.getAllEnvironments();
        log.info("getAllEnvironments() - Returning {} environments", environments.size());
        return environments;
    }



    /**
     * 新增环境配置
     * @param environmentDTO 环境配置DTO
     * @return 新增的环境配置
     */
    @PostMapping
    public ApiEnvironmentDTO addEnvironment(@RequestBody ApiEnvironmentDTO environmentDTO) {
        log.info("addEnvironment() - Request received for environment: {}", environmentDTO);
        ApiEnvironmentDTO addedEnvironment = apiEnvironmentService.addEnvironment(environmentDTO);
        log.info("addEnvironment() - Environment added successfully: {}", addedEnvironment);
        return addedEnvironment;
    }

    /**
     * 更新环境配置
     * @param id 环境ID
     * @param environmentDTO 环境配置DTO
     * @return 更新后的环境配置
     */
    @PutMapping("/{id}")
    public ApiEnvironmentDTO updateEnvironment(@PathVariable String id, @RequestBody ApiEnvironmentDTO environmentDTO) {
        log.info("updateEnvironment() - Request received for id: {} with environment: {}", id, environmentDTO);
        ApiEnvironmentDTO updatedEnvironment = apiEnvironmentService.updateEnvironment(id, environmentDTO);
        log.info("updateEnvironment() - Environment updated successfully: {}", updatedEnvironment);
        return updatedEnvironment;
    }

    /**
     * 删除环境配置
     * @param id 环境ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public boolean deleteEnvironment(@PathVariable String id) {
        log.info("deleteEnvironment() - Request received for id: {}", id);
        boolean success = apiEnvironmentService.deleteEnvironment(id);
        log.info("deleteEnvironment() - Environment deleted successfully: {}", success);
        return success;
    }

    /**
     * 设置默认环境
     * @param id 环境ID
     * @param appId 应用ID
     * @return 是否设置成功
     */
    @PostMapping("/{id}/default/{appId}")
    public boolean setDefaultEnvironment(@PathVariable String id, @PathVariable String appId) {
        log.info("setDefaultEnvironment() - Request received for id: {} and appId: {}", id, appId);
        boolean success = apiEnvironmentService.setDefaultEnvironment(id, appId);
        log.info("setDefaultEnvironment() - Environment set as default successfully: {}", success);
        return success;
    }
}