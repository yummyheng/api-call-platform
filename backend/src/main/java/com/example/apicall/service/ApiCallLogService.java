package com.example.apicall.service;

import com.example.apicall.dto.ApiCallLogDTO;
import java.util.List;

public interface ApiCallLogService {
    List<ApiCallLogDTO> listCallLogsByTestCaseId(String testCaseId);
    List<ApiCallLogDTO> listCallLogsByApiId(String apiId);
    ApiCallLogDTO getCallLogById(String id);
    ApiCallLogDTO addCallLog(ApiCallLogDTO apiCallLogDTO);
    boolean deleteCallLog(String id);
}