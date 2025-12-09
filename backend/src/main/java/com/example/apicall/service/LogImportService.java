package com.example.apicall.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface LogImportService {
    /**
     * 导入日志文件
     *
     * @param file          日志文件
     * @param environmentId 环境ID
     * @param projectId     项目ID
     * @return 导入结果
     */
    Map<String, Object> importLogFile(MultipartFile file, String environmentId, String projectId);
    
    /**
     * 通过文件路径导入日志文件
     *
     * @param filePath      日志文件路径
     * @param environmentId 环境ID
     * @param projectId     项目ID
     * @return 导入结果
     */
    Map<String, Object> importByPath(String filePath, String environmentId, String projectId);
}