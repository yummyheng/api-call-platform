package com.example.apicall.controller;

import com.example.apicall.dto.ApiResponseDTO;
import com.example.apicall.service.ApiCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/call")
public class ApiCallController {

    @Autowired
    private ApiCallService apiCallService;



    /**
     * 执行测试用例
     * @param testCaseId 测试用例ID
     * @param testData 测试数据
     * @param environmentId 环境ID
     * @return API响应结果
     */
    @PostMapping("/execute/test-case/{id}")
    public ApiResponseDTO executeTestCase(@PathVariable("id") String testCaseId, 
                                         @RequestBody(required = false) String testData,
                                         @RequestParam(required = false) String environmentId) {
        if (testCaseId == null) {
            throw new IllegalArgumentException("测试用例ID不能为空");
        }
        return apiCallService.executeTestCase(testCaseId, testData, environmentId);
    }
}