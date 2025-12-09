package com.example.apicall.common.enums;

/**
 * HTTP方法枚举
 * 用于统一管理系统中使用的HTTP方法，避免使用魔法字符串
 */
public enum HttpMethodEnum {
    
    /** GET方法 */
    GET("GET"),
    
    /** POST方法 */
    POST("POST"),
    
    /** PUT方法 */
    PUT("PUT"),
    
    /** DELETE方法 */
    DELETE("DELETE"),
    
    /** PATCH方法 */
    PATCH("PATCH"),
    
    /** OPTIONS方法 */
    OPTIONS("OPTIONS");
    
    private final String value;
    
    HttpMethodEnum(String value) {
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
    public static HttpMethodEnum fromValue(String value) {
        for (HttpMethodEnum method : HttpMethodEnum.values()) {
            if (method.value.equals(value)) {
                return method;
            }
        }
        return null;
    }
}