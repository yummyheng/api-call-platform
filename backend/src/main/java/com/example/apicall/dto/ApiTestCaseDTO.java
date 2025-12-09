package com.example.apicall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiTestCaseDTO {
    private String id;
    private String apiId;
    private String testCaseName;
    private String testData;
    private String expectedResult;
    private Boolean isEnabled;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
}