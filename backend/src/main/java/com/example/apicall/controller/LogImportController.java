package com.example.apicall.controller;

import com.example.apicall.service.LogImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志导入控制器
 */
@RestController
@RequestMapping("/logs")
@Slf4j
public class LogImportController {

    @Autowired
    private LogImportService logImportService;

    /**
     * 导入日志文件
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importLogFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("environmentId") String environmentId,
            @RequestParam("projectId") String projectId) {
        try {
            Map<String, Object> result = logImportService.importLogFile(file, environmentId, projectId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", result);
            response.put("message", "日志导入成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("导入日志失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "导入日志失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 通过文件路径导入日志文件
     */
    @PostMapping("/import-by-path")
    public ResponseEntity<Map<String, Object>> importLogByPath(@RequestBody Map<String, String> requestParams) {
        try {
            String filePath = requestParams.get("filePath");
            String environmentId = requestParams.get("environmentId");
            String projectId = requestParams.get("projectId");
            
            Map<String, Object> result = logImportService.importByPath(filePath, environmentId, projectId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", result);
            response.put("message", "日志导入成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("通过文件路径导入日志失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "通过文件路径导入日志失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}