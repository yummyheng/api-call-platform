package com.example.apicall.common.constant;

/**
 * 日志常量类
 * 用于管理系统中与日志相关的常量值，避免使用魔法字符串
 */
public class LogConstants {
    
    /** API调用记录日志类型 */
    public static final String API_CALL_RECORD = "apiCallRecord";
    
    /** 请求体标识字符串 */
    public static final String REQUEST_BODY_FLAG = "请求体:";
    
    /** 从日志导入的API描述 */
    public static final String IMPORTED_API_DESCRIPTION = "从日志导入的API";
    
    /** 从日志导入的预期结果 */
    public static final String IMPORTED_EXPECTED_RESULT = "从日志导入的预期结果";
    
    /** API名称正则表达式分隔符 */
    public static final String API_NAME_REGEX_SEPARATOR = "[/\\.-]";
    
    /** API名称正则表达式空格匹配 */
    public static final String API_NAME_REGEX_SPACE = "\\s+";
    
    /** 请求体开始偏移量 */
    public static final int REQUEST_BODY_START_OFFSET = 4;
    
    /** 请求体三重花括号正则表达式 */
    public static final String REQUEST_BODY_TRIPLE_BRACES_REGEX = "\\{\\{\\{|\\}\\}\\}";
    
    /** 日志成功前缀 */
    public static final String SUCCESS_LOG_PREFIX = "成功处理日志: ";
    
    /** 日志失败前缀 */
    public static final String ERROR_LOG_PREFIX = "处理失败日志: ";
    
    /** 日志解析失败前缀 */
    public static final String PARSE_ERROR_LOG_PREFIX = "解析失败日志: ";
}
