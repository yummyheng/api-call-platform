package com.example.apicall.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiTestCaseNotFoundException extends RuntimeException {
    private String testCaseId;
    @Override
    public String getMessage() {
        return "测试用例不存在: " + testCaseId;
    }
}