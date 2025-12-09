package com.example.apicall.common.constant;

/**
 * HTTP状态码常量
 * 用于统一管理系统中使用的HTTP状态码，避免使用魔法数值
 */
public class HttpStatusConstants {
    
    /** 成功状态码的起始值 */
    public static final int SUCCESS_START = 200;
    
    /** 成功状态码的结束值（不包含） */
    public static final int SUCCESS_END = 300;
    
    /** 客户端错误状态码的起始值 */
    public static final int CLIENT_ERROR_START = 400;
    
    /** 客户端错误状态码的结束值（不包含） */
    public static final int CLIENT_ERROR_END = 500;
    
    /** 服务器错误状态码的起始值 */
    public static final int SERVER_ERROR_START = 500;
    
    /** 服务器错误状态码的结束值（不包含） */
    public static final int SERVER_ERROR_END = 600;
    
    /** 请求成功 */
    public static final int OK = 200;
    
    /** 创建成功 */
    public static final int CREATED = 201;
    
    /** 无内容 */
    public static final int NO_CONTENT = 204;
    
    /** 重定向 */
    public static final int MOVED_PERMANENTLY = 301;
    
    /** 临时重定向 */
    public static final int FOUND = 302;
    
    /** 错误的请求 */
    public static final int BAD_REQUEST = 400;
    
    /** 未授权 */
    public static final int UNAUTHORIZED = 401;
    
    /** 禁止访问 */
    public static final int FORBIDDEN = 403;
    
    /** 资源不存在 */
    public static final int NOT_FOUND = 404;
    
    /** 方法不允许 */
    public static final int METHOD_NOT_ALLOWED = 405;
    
    /** 服务器内部错误 */
    public static final int INTERNAL_SERVER_ERROR = 500;
    
    /** 服务不可用 */
    public static final int SERVICE_UNAVAILABLE = 503;
    
    /**
     * 判断HTTP状态码是否表示成功
     * @param statusCode HTTP状态码
     * @return 是否成功
     */
    public static boolean isSuccess(int statusCode) {
        return statusCode >= SUCCESS_START && statusCode < SUCCESS_END;
    }
    
    /**
     * 判断HTTP状态码是否表示客户端错误
     * @param statusCode HTTP状态码
     * @return 是否客户端错误
     */
    public static boolean isClientError(int statusCode) {
        return statusCode >= CLIENT_ERROR_START && statusCode < CLIENT_ERROR_END;
    }
    
    /**
     * 判断HTTP状态码是否表示服务器错误
     * @param statusCode HTTP状态码
     * @return 是否服务器错误
     */
    public static boolean isServerError(int statusCode) {
        return statusCode >= SERVER_ERROR_START && statusCode < SERVER_ERROR_END;
    }
}
