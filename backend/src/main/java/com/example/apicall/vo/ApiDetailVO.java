package com.example.apicall.vo;

import com.example.apicall.domain.ApiProject;
import com.example.apicall.domain.ApiRequest;
import com.example.apicall.domain.ApiTestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiDetailVO {
    private ApiProject project;
    private ApiRequest request;
    private ApiTestCase testCase;
}