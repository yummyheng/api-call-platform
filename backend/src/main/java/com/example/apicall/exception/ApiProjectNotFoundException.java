package com.example.apicall.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiProjectNotFoundException extends RuntimeException {
    private String projectId;
    
    @Override
    public String getMessage() {
        return "API项目不存在: " + projectId;
    }
}