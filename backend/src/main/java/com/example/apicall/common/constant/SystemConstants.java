package com.example.apicall.common.constant;

/**
 * 系统常量类
 * 用于管理系统级别的常量值，避免使用魔法字符串
 */
public class SystemConstants {
    
    /** 系统用户标识 */
    public static final String SYSTEM_USER = "system";
    
    /** 默认分页大小 */
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    
    /** 默认页码 */
    public static final Integer DEFAULT_PAGE_NUM = 1;
    
    /** NULL字符串表示 */
    public static final String NULL_STRING = "null";
    
    /** 空字符串 */
    public static final String EMPTY_STRING = "";
    
    /** 下划线分隔符 */
    public static final String UNDERLINE_SEPARATOR = "_";
    
    /** 中划线分隔符 */
    public static final String DASH_SEPARATOR = "-";
    
    /** 逗号分隔符 */
    public static final String COMMA_SEPARATOR = ",";
    
    /** 冒号分隔符 */
    public static final String COLON_SEPARATOR = ":";
    
    /** 分号分隔符 */
    public static final String SEMICOLON_SEPARATOR = ";";
    
    /** 点分隔符 */
    public static final String DOT_SEPARATOR = ".";
    
    /** 斜杠分隔符 */
    public static final String SLASH_SEPARATOR = "/";
    
    /** 反斜杠分隔符 */
    public static final String BACKSLASH_SEPARATOR = "\\";
    
    /** 空格分隔符 */
    public static final String SPACE_SEPARATOR = " ";
    
    /** 空括号 */
    public static final String EMPTY_PARENTHESIS = "()";
    
    /** 空大括号 */
    public static final String EMPTY_BRACES = "{}";
    
    /** 空中括号 */
    public static final String EMPTY_BRACKETS = "[]";
    
    /** 空引号 */
    public static final String EMPTY_QUOTES = "\"\"";
    
    /** 单引号 */
    public static final String SINGLE_QUOTE = "'";
    
    /** 双引号 */
    public static final String DOUBLE_QUOTE = "\"";
    
    /** URL协议分隔符 */
    public static final String URL_PROTOCOL_SEPARATOR = "://";
    
    /** URL参数分隔符 */
    public static final String URL_PARAM_SEPARATOR = "?";
    
    /** 环境或项目不存在错误消息 */
    public static final String ERROR_ENV_PROJECT_NOT_EXIST = "环境或项目不存在";
    
    /** 环境不存在错误消息 */
    public static final String ERROR_ENVIRONMENT_NOT_FOUND = "Environment not found with id: ";
    
    /** 请求失败前缀 */
    public static final String ERROR_REQUEST_FAILED_PREFIX = "请求失败: ";
    
    /** 默认字符串截断长度 */
    public static final int DEFAULT_TRUNCATE_LENGTH = 100;
}