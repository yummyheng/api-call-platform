package com.example.apicall.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiRequestNotFoundException extends RuntimeException {
    private String apiId;
    @Override
    public String getMessage() {
        return "API请求不存在: " + apiId;
    }
}