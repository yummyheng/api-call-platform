package com.example.apicall.controller;

import com.example.apicall.dto.ApiProjectDTO;
import com.example.apicall.service.ApiProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/projects")
@Slf4j
public class ApiProjectController {

    @Autowired
    private ApiProjectService apiProjectService;

    @GetMapping
    public List<ApiProjectDTO> listAllProjects() {
        log.info("listAllProjects() - Request received");
        List<ApiProjectDTO> projects = apiProjectService.listAllProjects();
        log.info("listAllProjects() - Returning {} projects", projects.size());
        return projects;
    }
    

}