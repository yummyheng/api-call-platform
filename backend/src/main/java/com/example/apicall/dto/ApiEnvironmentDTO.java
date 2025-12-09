package com.example.apicall.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 环境配置DTO类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiEnvironmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 环境ID
     */
    private String id;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 环境名称
     */
    private String envName;

    /**
     * 环境描述
     */
    private String envDescription;

    /**
     * 基础URL
     */
    private String baseUrl;

    /**
     * 请求头
     */
    private List<KeyPairDTO> headers;

    /**
     * 请求参数
     */
    private List<KeyPairDTO> params;

    /**
     * 是否默认环境
     */
    private Boolean isDefault;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    // getter和setter方法


    /**
     * 键值对DTO类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class KeyPairDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String key;
        private String value;
    }
}