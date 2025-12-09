package com.example.apicall.dto;

import com.example.apicall.model.HttpClientCallDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDTO {
    private Long id;
    private String apiId;
    private Integer statusCode;
    private String responseBody;
    private Map<String, String> responseHeaders;
    private Date callTime;
    private Long elapsedTime;
    private boolean success;
    private String message;
    private HttpClientCallDetail httpClientCallDetail;
}