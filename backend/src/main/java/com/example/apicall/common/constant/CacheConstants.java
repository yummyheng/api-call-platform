package com.example.apicall.common.constant;

/**
 * 缓存常量类
 * 用于管理系统中的缓存名称和键名，避免使用魔法字符串
 */
public class CacheConstants {
    
    /** API响应缓存 */
    public static final String API_RESPONSES = "apiResponses";
    
    /** 环境配置缓存 */
    public static final String API_ENVIRONMENTS = "apiEnvironments";
    
    /** API请求缓存 */
    public static final String API_REQUESTS = "apiRequests";
    
    /** 测试用例缓存 */
    public static final String API_TEST_CASES = "apiTestCases";
    
    /** 项目缓存 */
    public static final String API_PROJECTS = "apiProjects";
    
    /** 缓存键分隔符 */
    public static final String CACHE_KEY_SEPARATOR = "_";
    
    /** 空缓存键标识 */
    public static final String EMPTY_CACHE_KEY = "empty";
    
    /** 默认缓存键标识 */
    public static final String DEFAULT_CACHE_KEY = "default";
}