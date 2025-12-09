package com.example.apicall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequestDTO {
    private String id;
    private String appId;
    private String projectId;
    private String apiName;
    private String apiUrl;
    private String httpMethod;
    private String requestHeaders;
    private String requestBody;
    private String description;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
}