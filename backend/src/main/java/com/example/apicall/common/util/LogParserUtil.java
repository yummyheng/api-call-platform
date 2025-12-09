package com.example.apicall.common.util;

import com.example.apicall.common.enums.LogTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日志解析工具类
 * 提供日志行解析、元数据提取等通用方法
 */
@Slf4j
public class LogParserUtil {

    /**
     * 解析日志行
     * @param logLine 日志行字符串
     * @return 解析后的日志数据，解析失败返回Optional.empty()
     */
    public static Optional<Map<String, Object>> parseLogLine(String logLine) {
        return Optional.ofNullable(logLine)
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .flatMap(line -> {
                    try {
                        // 提取日志元数据
                        Optional<Map<String, Object>> metadataOpt = extractLogMetadata(line);
                        return metadataOpt.flatMap(metadata -> {
                            String message = (String) metadata.get("message");
                            if (message == null || message.trim().isEmpty()) {
                                log.debug("日志消息为空: {}", line);
                                return Optional.empty();
                            }

                            // 尝试解析为JSON格式
                            Map<String, Object> logMap;
                            String trimmedMessage = message.trim();
                            
                            // 简单检查是否可能是JSON格式（以{开头并以}结尾，或者以[开头并以]结尾）
                            boolean isJsonFormat = (trimmedMessage.startsWith("{") && trimmedMessage.endsWith("}")) || 
                                                  (trimmedMessage.startsWith("[") && trimmedMessage.endsWith("]"));

                            if (isJsonFormat) {
                                try {
                                    Optional<Map<String, Object>> jsonOpt = JsonUtil.parseJson(message);
                                    if (jsonOpt.isPresent()) {
                                        // 如果是JSON格式，添加元数据信息
                                        logMap = jsonOpt.get();
                                        logMap.putAll(metadata);
                                        logMap.remove("message"); // 移除原始消息，保留解析后的JSON结构
                                        logMap.put("logType", "json");
                                    } else {
                                        // 非JSON格式日志，使用策略模式进行文本解析
                                        logMap = metadata;
                                        parseNonJsonLog(message, logMap);
                                    }
                                } catch (Exception e) {
                                    // 预期的JSON解析失败，不记录错误日志
                                    // 非JSON格式日志，使用策略模式进行文本解析
                                    logMap = metadata;
                                    parseNonJsonLog(message, logMap);
                                }
                            } else {
                                // 明显不是JSON格式，直接使用策略模式解析
                                logMap = metadata;
                                parseNonJsonLog(message, logMap);
                            }

                            // 处理敏感信息
                            SensitiveDataUtil.processSensitiveData(logMap);

                            return Optional.of(logMap);
                        });
                    } catch (Exception e) {
                        log.error("解析日志行失败: {}", line, e);
                        return Optional.empty();
                    }
                });
    }

    /**
     * 提取日志元数据
     * 日志格式: 2025-12-03 14:49:29.714 [http-nio-8081-exec-1] DEBUG c.e.l.w.i.LoggingInterceptor - 消息
     * @param logLine 日志行字符串
     * @return 包含元数据的Map，提取失败返回Optional.empty()
     */
    public static Optional<Map<String, Object>> extractLogMetadata(String logLine) {
        try {
            // 日志格式: 2025-12-03 14:49:29.714 [http-nio-8081-exec-1] DEBUG c.e.l.w.i.LoggingInterceptor - 消息
            // 修改正则表达式以适应日志级别和类名之间的多个空格
            Pattern pattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(\\.\\d{3})?) \\[([^\\]]+)\\] ([A-Z]+)\\s+([^\\s]+)\\s+-\\s+(.*)$");
            Matcher matcher = pattern.matcher(logLine);

            Map<String, Object> metadataMap = new HashMap<>();

            if (matcher.find()) {
                metadataMap.put("timestamp", matcher.group(1));
                metadataMap.put("threadId", matcher.group(3));
                metadataMap.put("logLevel", matcher.group(4));
                metadataMap.put("className", matcher.group(5));
                metadataMap.put("message", matcher.group(6));
            } else {
                // 如果元数据提取失败，尝试简单解析
                log.debug("元数据提取失败: {}", logLine);
                metadataMap.put("message", logLine);
            }

            return Optional.of(metadataMap);
        } catch (Exception e) {
            log.error("提取日志元数据失败: {}", logLine, e);
            return Optional.empty();
        }
    }

    /**
     * 判断是否为API调用日志
     * @param logMap 日志数据Map
     * @return 是否为API调用日志
     */
    public static boolean isApiCallLog(Map<String, Object> logMap) {
        // 检查是否包含API调用记录的关键字段
        boolean hasApiFields = logMap != null && logMap.containsKey("apiId") && logMap.containsKey("callTime") && logMap.containsKey("elapsedTime");
        boolean hasLogType = logMap != null && logMap.get("logType") != null && 
                           ("request".equals(logMap.get("logType")) || 
                            "apiCallRecord".equals(logMap.get("logType")) || 
                            "response".equals(logMap.get("logType")) ||
                            "json".equals(logMap.get("logType")));
        boolean result = hasApiFields || hasLogType;

        log.debug("API调用日志检查 - hasApiFields: {}, hasLogType: {}, result: {}", hasApiFields, hasLogType, result);
        return result;
    }

    /**
     * 解析非JSON格式日志
     * @param message 日志消息
     * @param logMap 日志数据Map
     */
    private static void parseNonJsonLog(String message, Map<String, Object> logMap) {
        // 使用策略模式选择合适的解析策略
        parsingStrategies.stream()
                .filter(strategy -> strategy.supports(message))
                .findFirst()
                .ifPresent(strategy -> strategy.parse(message, logMap));
    }

    /**
     * 日志解析策略接口
     */
    private interface LogParsingStrategy {
        boolean supports(String message);
        void parse(String message, Map<String, Object> logMap);
    }

    // 日志解析策略实现
    private static final List<LogParsingStrategy> parsingStrategies = Arrays.asList(
        // new RequestLogParsingStrategy(),
        new ApiCallRecordParsingStrategy(),
        new ResponseContentParsingStrategy(),
        new ResponseContentLengthParsingStrategy(),
        new InterceptorEntryParsingStrategy(),
        new OtherLogParsingStrategy()
    );

    // /**
    //  * 请求日志解析策略
    //  */
    // private static class RequestLogParsingStrategy implements LogParsingStrategy {
    //     private final Pattern pattern = Pattern.compile("请求URL: ([^,]+), 方法: ([^,]+), 客户端IP: ([^,]+)(, 请求体: (.+))?");

    //     @Override
    //     public boolean supports(String message) {
    //         return message.contains("请求URL:") && message.contains("方法:");
    //     }

    //     @Override
    //     public void parse(String message, Map<String, Object> logMap) {
    //         Matcher matcher = pattern.matcher(message);
    //         if (matcher.find()) {
    //             logMap.put("requestUrl", matcher.group(1).trim());
    //             logMap.put("httpMethod", matcher.group(2).trim());
    //             logMap.put("clientIp", matcher.group(3).trim());
                
    //             // 提取请求体
    //             if (matcher.group(5) != null) {
    //                 String requestBody = matcher.group(5).trim();
    //                 logMap.put("requestBody", requestBody);
    //                 logMap.put("requestParams", requestBody); // 兼容旧代码，保留requestParams字段
    //             }
                
    //             logMap.put("logType", LogTypeEnum.REQUEST.getValue());
    //         }
    //     }
    // }

    /**
     * API调用记录解析策略
     */
    private static class ApiCallRecordParsingStrategy implements LogParsingStrategy {
        private final Pattern pattern = Pattern.compile("API调用记录 - URL: ([^,]+), Method: ([^,]+), ClientIP: ([^,]+), 参数: (.*)", Pattern.DOTALL);

        @Override
        public boolean supports(String message) {
            return message.contains("API调用记录");
        }

        @Override
        public void parse(String message, Map<String, Object> logMap) {
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                logMap.put("requestUrl", matcher.group(1).trim());
                logMap.put("httpMethod", matcher.group(2).trim());
                logMap.put("clientIp", matcher.group(3).trim());
                
                // 处理参数和请求体部分
                String params = matcher.group(4).trim();
                
                // 检查是否包含请求体
                int requestBodyIndex = params.indexOf("请求体:");
                if (requestBodyIndex != -1) {
                    // 提取请求体
                    String requestBody = params.substring(requestBodyIndex + 4).trim();
                    logMap.put("requestBody", requestBody);
                    
                    // 提取实际参数（如果有）
                    String actualParams = params.substring(0, requestBodyIndex).trim();
                    if (!actualParams.isEmpty()) {
                        logMap.put("requestParams", actualParams);
                    } else {
                        logMap.put("requestParams", "");
                    }
                } else {
                    // 没有请求体，直接设置参数
                    logMap.put("requestParams", params);
                }
                
                logMap.put("logType", LogTypeEnum.API_CALL_RECORD.getValue());
            }
        }
    }

    /**
     * 响应内容解析策略
     */
    private static class ResponseContentParsingStrategy implements LogParsingStrategy {
        @Override
        public boolean supports(String message) {
            return message.contains("响应内容:");
        }

        @Override
        public void parse(String message, Map<String, Object> logMap) {
            logMap.put("logType", LogTypeEnum.RESPONSE_CONTENT.getValue());
            logMap.put("responseContent", message.replace("响应内容:", "").trim());
        }
    }

    /**
     * 响应内容长度解析策略
     */
    private static class ResponseContentLengthParsingStrategy implements LogParsingStrategy {
        @Override
        public boolean supports(String message) {
            return message.contains("响应内容长度:");
        }

        @Override
        public void parse(String message, Map<String, Object> logMap) {
            String contentLength = message.replace("响应内容长度:", "").trim();
            logMap.put("logType", LogTypeEnum.RESPONSE_CONTENT_LENGTH.getValue());
            logMap.put("contentLength", contentLength);
        }
    }

    /**
     * 拦截器日志解析策略
     */
    private static class InterceptorEntryParsingStrategy implements LogParsingStrategy {
        @Override
        public boolean supports(String message) {
            return message.contains("进入日志拦截器");
        }

        @Override
        public void parse(String message, Map<String, Object> logMap) {
            String interceptorMethod = message.replace("进入日志拦截器 - ", "").trim();
            logMap.put("logType", LogTypeEnum.INTERCEPTOR_ENTRY.getValue());
            logMap.put("interceptorMethod", interceptorMethod);
        }
    }

    /**
     * 其他日志解析策略
     */
    private static class OtherLogParsingStrategy implements LogParsingStrategy {
        @Override
        public boolean supports(String message) {
            return true; // 始终支持，作为默认策略
        }

        @Override
        public void parse(String message, Map<String, Object> logMap) {
            logMap.put("logType", LogTypeEnum.OTHER.getValue());
        }
    }

    /**
     * 主方法，用于调试日志解析功能
     */
    public static void main(String[] args) {
        // 测试API调用记录解析策略
        testApiCallRecordParsing();
        
        // 测试完整的parseLogLine方法
        testParseLogLine();
    }
    
    /**
     * 测试完整的parseLogLine方法
     */
    private static void testParseLogLine() {
        System.out.println("\n=== 测试完整的parseLogLine方法 ===");
        
        // 测试用例1: 带请求体的完整日志行
        System.out.println("\n测试用例1: 带请求体的完整日志行");
        String fullLogLine1 = "2025-12-04 16:11:08.297 [http-nio-8081-exec-1] INFO  c.e.l.w.i.LoggingInterceptor - API调用记录 - URL: /api/call/execute/test-case/1, Method: POST, ClientIP: 0:0:0:0:0:0:0:1, 参数: 请求体: {\"page\":1,\"size\":10,\"environmentId\":\"dev-1\"}";
        System.out.println("原始日志行: " + fullLogLine1);
        
        Optional<Map<String, Object>> result1 = parseLogLine(fullLogLine1);
        if (result1.isPresent()) {
            System.out.println("\n完整日志行（带请求体）解析结果：");
            printResult(result1.get());
        } else {
            System.out.println("\n完整日志行（带请求体）解析失败");
        }

        // 测试用例2: 不带请求体的完整日志行
        System.out.println("\n\n测试用例2: 不带请求体的完整日志行");
        String fullLogLine2 = "2025-12-04 16:11:08.297 [http-nio-8081-exec-1] INFO  c.e.l.w.i.LoggingInterceptor - API调用记录 - URL: /api/call/execute/test-case/1, Method: GET, ClientIP: 0:0:0:0:0:0:0:1, 参数: page=1&size=10&environmentId=dev-1";
        System.out.println("原始日志行: " + fullLogLine2);
        
        Optional<Map<String, Object>> result2 = parseLogLine(fullLogLine2);
        if (result2.isPresent()) {
            System.out.println("\n完整日志行（不带请求体）解析结果：");
            printResult(result2.get());
        } else {
            System.out.println("\n完整日志行（不带请求体）解析失败");
        }
    }
    
    /**
     * 直接测试ApiCallRecordParsingStrategy的解析功能
     */
    private static void testApiCallRecordParsing() {
        ApiCallRecordParsingStrategy strategy = new ApiCallRecordParsingStrategy();
        
        // 测试用例1: 带请求体的API调用记录
        String logLine1 = "API调用记录 - URL: /api/call/execute/test-case/1, Method: POST, ClientIP: 0:0:0:0:0:0:0:1, 参数: 请求体: {\"page\":1,\"size\":10,\"environmentId\":\"dev-1\"}";
        
        // 测试用例2: 不带请求体的API调用记录
        String logLine2 = "API调用记录 - URL: /api/call/execute/test-case/1, Method: GET, ClientIP: 0:0:0:0:0:0:0:1, 参数: page=1&size=10&environmentId=dev-1";
        
        // 测试用例3: 参数部分既有其他参数又有请求体的情况
        String logLine3 = "API调用记录 - URL: /api/call/execute/test-case/1, Method: POST, ClientIP: 0:0:0:0:0:0:0:1, 参数: id=123&name=test 请求体: {\"page\":1,\"size\":10,\"environmentId\":\"dev-1\"}";
        
        System.out.println("=== 测试ApiCallRecordParsingStrategy ===");
        
        // 测试用例1
        System.out.println("\n测试用例1: 带请求体的API调用记录");
        Map<String, Object> result1 = new HashMap<>();
        strategy.parse(logLine1, result1);
        printResult(result1);
        
        // 测试用例2
        System.out.println("\n测试用例2: 不带请求体的API调用记录");
        Map<String, Object> result2 = new HashMap<>();
        strategy.parse(logLine2, result2);
        printResult(result2);
        
        // 测试用例3
        System.out.println("\n测试用例3: 参数部分既有其他参数又有请求体的情况");
        Map<String, Object> result3 = new HashMap<>();
        strategy.parse(logLine3, result3);
        printResult(result3);
    }
    
    /**
     * 打印解析结果
     */
    private static void printResult(Map<String, Object> result) {
        System.out.println("logType: " + result.getOrDefault("logType", "未知"));
        System.out.println("requestUrl: " + result.getOrDefault("requestUrl", "未知"));
        System.out.println("httpMethod: " + result.getOrDefault("httpMethod", "未知"));
        System.out.println("clientIp: " + result.getOrDefault("clientIp", "未知"));
        System.out.println("requestParams: " + result.getOrDefault("requestParams", "未知"));
        
        if (result.containsKey("requestBody")) {
            System.out.println("requestBody: " + result.get("requestBody"));
            System.out.println("✓ 成功解析请求体");
        } else {
            System.out.println("✗ 未找到请求体");
        }
    }
    
    /**
     * 测试日志解析功能的辅助方法
     */
    private static void testLogParsing(String logLine) {
        // 解析日志行
        Optional<Map<String, Object>> resultOpt = parseLogLine(logLine);
        
        if (resultOpt.isPresent()) {
            Map<String, Object> result = resultOpt.get();
            
            // 检查requestBody是否被解析
            if (result.containsKey("requestBody")) {
                System.out.println("✓ requestBody解析成功: " + result.get("requestBody"));
            } else {
                System.out.println("✗ requestBody解析失败，未找到requestBody字段");
            }
            
            // 输出基本信息
            System.out.println("logType: " + result.getOrDefault("logType", "未知"));
            System.out.println("requestUrl: " + result.getOrDefault("requestUrl", "未知"));
            System.out.println("httpMethod: " + result.getOrDefault("httpMethod", "未知"));
        } else {
            System.out.println("解析失败");
        }
    }
}
