package com.example.apicall.vo;

import com.example.apicall.domain.ApiProject;
import com.example.apicall.domain.ApiRequest;
import com.example.apicall.domain.ApiTestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * getApiTreeByTestCaseId方法的响应VO - 扁平化结构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetApiTreeByTestCaseIdResponseVO {
    // 测试用例基本信息
    private String id;
    private String label;
    private String type = "test_case";
    
    // 项目相关信息
    private String projectId;
    private String projectName;
    
    // API请求相关信息
    private String requestId;
    private String apiUrl;
    
    // 测试用例详细信息
    private ApiTestCase testCase;
    private ApiProject project;
    private ApiRequest request;
}