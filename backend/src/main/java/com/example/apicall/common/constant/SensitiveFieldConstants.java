package com.example.apicall.common.constant;

/**
 * 敏感字段常量类
 * 用于管理系统中的敏感字段名称，避免使用魔法字符串
 */
public class SensitiveFieldConstants {
    /** 密码字段 */
    public static final String PASSWORD_FIELD = "password";
    
    /** 令牌字段 */
    public static final String TOKEN_FIELD = "token";
    
    /** 密钥字段 */
    public static final String SECRET_FIELD = "secret";
    
    /** 键字段 */
    public static final String KEY_FIELD = "key";
    
    /** 访问ID字段 */
    public static final String ACCESS_ID_FIELD = "accessId";
    
    /** 访问密钥字段 */
    public static final String ACCESS_KEY_FIELD = "accessKey";
    
    /** 敏感值替换字符串 */
    public static final String SENSITIVE_VALUE_REPLACEMENT = "******";
    
    /** 正则表达式分隔符 */
    public static final String REGEX_FIELD_PATTERN = "%s\\s*[:=]\\s*[\\w\\W]*?(?=[,\\}]|\\s+[^\\w\\W])";
}