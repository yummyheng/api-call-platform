package com.example.apicall.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 环境配置实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("api_environment")
public class ApiEnvironment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 环境ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 应用ID
     */
    @TableField("app_id")
    private String appId;

    /**
     * 环境名称
     */
    @TableField("env_name")
    private String envName;

    /**
     * 环境描述
     */
    @TableField("env_desc")
    private String envDescription;

    /**
     * 基础URL
     */
    @TableField("base_url")
    private String baseUrl;

    /**
     * 请求头，JSON格式
     */
    @TableField(exist = false)
    private String headers;

    /**
     * 请求参数，JSON格式
     */
    @TableField(exist = false)
    private String params;

    /**
     * 是否为默认环境 0:否 1:是
     */
    @TableField("is_default")
    private Integer isDefault;

    /**
     * 优先级
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedAt;

    // getter和setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getEnvDescription() {
        return envDescription;
    }

    public void setEnvDescription(String envDescription) {
        this.envDescription = envDescription;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
