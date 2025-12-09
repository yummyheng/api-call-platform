package com.example.apicall.model;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.example.apicall.common.enums.ExecutionStatusEnum;
import lombok.Builder;
import lombok.Data;

/**
 * HTTP客户端调用详情类，用于记录HTTP请求的完整信息
 */
@Data
@Builder
public class HttpClientCallDetail {
    private String apiId;
    private String requestUrl;
    private String httpMethod;
    @Builder.Default
    private Map<String, String> requestHeaders = new HashMap<>();
    private String requestBody;
    private String requestParams;
    private int responseStatusCode;
    @Builder.Default
    private Map<String, String> responseHeaders = new HashMap<>();
    private String responseBody;
    private long executionTimeMs;
    @Builder.Default
    private String startTime = getCurrentTimestamp();
    private String endTime;
    @Builder.Default
    private String executionStatus = ExecutionStatusEnum.PENDING.getValue();
    private String errorMessage;
    
    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
    
    public void startExecution() {
        this.startTime = getCurrentTimestamp();
        this.executionStatus = ExecutionStatusEnum.IN_PROGRESS.getValue();
    }
    
    public void endExecution() {
        this.endTime = getCurrentTimestamp();
        this.executionStatus = ExecutionStatusEnum.COMPLETED.getValue();
    }
    
    public void failExecution(String errorMessage) {
        this.endTime = getCurrentTimestamp();
        this.executionStatus = ExecutionStatusEnum.FAILED.getValue();
        this.errorMessage = errorMessage;
    }
}