package com.example.apicall.common.enums;

/**
 * 日志类型枚举
 * 用于统一管理系统中的日志类型，避免使用魔法字符串
 */
public enum LogTypeEnum {
    
    /** 请求日志 */
    REQUEST("request"),
    
    /** API调用记录日志 */
    API_CALL_RECORD("apiCallRecord"),
    
    /** 响应日志 */
    RESPONSE("response"),
    
    /** JSON格式日志 */
    JSON("json"),
    
    /** 其他类型日志 */
    OTHER("other"),
    
    /** 请求体日志 */
    REQUEST_BODY("requestBody"),
    
    /** 请求参数完成日志 */
    REQUEST_PARAMS_COMPLETED("requestParamsCompleted"),
    
    /** 包装器中的请求体日志 */
    REQUEST_BODY_FROM_WRAPPER("requestBodyFromWrapper"),
    
    /** 响应内容日志 */
    RESPONSE_CONTENT("responseContent"),
    
    /** 响应内容长度日志 */
    RESPONSE_CONTENT_LENGTH("responseContentLength"),
    
    /** 拦截器入口日志 */
    INTERCEPTOR_ENTRY("interceptorEntry");
    
    private final String value;
    
    LogTypeEnum(String value) {
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
    public static LogTypeEnum fromValue(String value) {
        for (LogTypeEnum type : LogTypeEnum.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}