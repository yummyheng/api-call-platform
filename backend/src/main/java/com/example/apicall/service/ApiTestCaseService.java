package com.example.apicall.service;

import com.example.apicall.dto.ApiTestCaseDTO;
import java.util.List;

public interface ApiTestCaseService {
    List<ApiTestCaseDTO> listTestCasesByApiId(String apiId);
    ApiTestCaseDTO getTestCaseById(String id);
    ApiTestCaseDTO addTestCase(ApiTestCaseDTO apiTestCaseDTO);
    ApiTestCaseDTO updateTestCase(ApiTestCaseDTO apiTestCaseDTO);
    boolean deleteTestCase(String id);
    boolean updateTestCaseTestData(String id, String testData);
}