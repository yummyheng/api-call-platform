package com.example.apicall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallLogDTO {
    private String id;
    private String apiId;
    private String testCaseId;
    private String requestParams;
    private String actualResult;
    private Integer statusCode;
    private Integer responseTime;
    private Date callTime;
}