package com.example.apicall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.apicall.dto.ApiProjectDTO;
import com.example.apicall.domain.ApiProject;
import com.example.apicall.repository.ApiProjectRepository;
import com.example.apicall.service.ApiProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApiProjectServiceImpl implements ApiProjectService {

    @Autowired
    private ApiProjectRepository apiProjectRepository;

    @Override
    public List<ApiProjectDTO> listAllProjects() {
        List<ApiProject> apiProjects = apiProjectRepository.selectList(null);
        return apiProjects.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ApiProjectDTO getProjectById(String id) {
        ApiProject apiProject = apiProjectRepository.selectById(id);
        return toDto(apiProject);
    }

    
    /**
     * 将ApiProject转换为ApiProjectDTO
     */
    private ApiProjectDTO toDto(ApiProject apiProject) {
        if (apiProject == null) {
            return null;
        }
        
        ApiProjectDTO dto = new ApiProjectDTO();
        dto.setId(apiProject.getId());
        dto.setAppId(apiProject.getAppId());
        dto.setProjectName(apiProject.getProjectName());
        dto.setDescription(apiProject.getDescription());
        dto.setCreatedBy(apiProject.getCreatedBy());
        dto.setCreatedTime(apiProject.getCreatedTime());
        dto.setUpdatedBy(apiProject.getUpdatedBy());
        dto.setUpdatedTime(apiProject.getUpdatedTime());
        return dto;
    }
}