package com.example.apicall.service;

import com.example.apicall.dto.ApiResponseDTO;

public interface ApiCallService {
    ApiResponseDTO executeTestCase(String testCaseId, String testData, String environmentId);
}