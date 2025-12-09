package com.example.apicall.service;

import com.example.apicall.dto.ApiRequestDTO;
import java.util.List;

public interface ApiRequestService {
    List<ApiRequestDTO> listRequestsByProjectId(String projectId);
    
    ApiRequestDTO getRequestById(String id);
    
    ApiRequestDTO addRequest(ApiRequestDTO apiRequestDTO);
    ApiRequestDTO updateRequest(ApiRequestDTO apiRequestDTO);
    boolean deleteRequest(String id);
}