package com.example.apicall.common.enums;

/**
 * 执行状态枚举
 * 用于统一管理HTTP客户端调用的执行状态，避免使用魔法字符串
 */
public enum ExecutionStatusEnum {
    
    /** 待执行 */
    PENDING("PENDING"),
    
    /** 执行中 */
    IN_PROGRESS("IN_PROGRESS"),
    
    /** 执行完成 */
    COMPLETED("COMPLETED"),
    
    /** 执行失败 */
    FAILED("FAILED");
    
    private final String value;
    
    ExecutionStatusEnum(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * 根据字符串值获取枚举
     * @param value 字符串值
     * @return 对应的枚举，如果没有匹配返回null
     */
    public static ExecutionStatusEnum fromValue(String value) {
        for (ExecutionStatusEnum status : ExecutionStatusEnum.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}