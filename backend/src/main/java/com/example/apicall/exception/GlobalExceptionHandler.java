package com.example.apicall.exception;

import com.example.apicall.dto.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiRequestNotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleApiRequestNotFoundException(ApiRequestNotFoundException ex) {
        log.error("API请求不存在异常: {}", ex.getMessage());
        ApiResponseDTO response = new ApiResponseDTO();
        response.setSuccess(false);
        response.setResponseBody(ex.getMessage());
        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setCallTime(new Date());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiTestCaseNotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleApiTestCaseNotFoundException(ApiTestCaseNotFoundException ex) {
        log.error("测试用例不存在异常: {}", ex.getMessage());
        ApiResponseDTO response = new ApiResponseDTO();
        response.setSuccess(false);
        response.setResponseBody(ex.getMessage());
        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setCallTime(new Date());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiProjectNotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleApiProjectNotFoundException(ApiProjectNotFoundException ex) {
        log.error("API项目不存在异常: {}", ex.getMessage());
        ApiResponseDTO response = new ApiResponseDTO();
        response.setSuccess(false);
        response.setResponseBody(ex.getMessage());
        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setCallTime(new Date());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("参数类型不匹配异常: {}", ex.getMessage());
        String errorMessage = String.format("参数 '%s' 的类型不正确。预期类型: %s, 实际值: %s", 
            ex.getName(), 
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "未知", 
            ex.getValue() != null ? ex.getValue() : "null");
        ApiResponseDTO response = new ApiResponseDTO();
        response.setSuccess(false);
        response.setResponseBody(errorMessage);
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setCallTime(new Date());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 处理其他异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleGenericException(Exception ex) {
        log.error("系统异常: {}", ex.getMessage(), ex);
        ApiResponseDTO response = new ApiResponseDTO();
        response.setSuccess(false);
        response.setResponseBody("系统内部错误: " + ex.getMessage());
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setCallTime(new Date());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}