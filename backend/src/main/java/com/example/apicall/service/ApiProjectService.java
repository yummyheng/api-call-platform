package com.example.apicall.service;

import com.example.apicall.dto.ApiProjectDTO;
import java.util.List;
import java.util.Set;

public interface ApiProjectService {
    /**
     * 获取所有项目列表
     */
    List<ApiProjectDTO> listAllProjects();

    /**
     * 通过ID获取项目
     */
    ApiProjectDTO getProjectById(String id);
}