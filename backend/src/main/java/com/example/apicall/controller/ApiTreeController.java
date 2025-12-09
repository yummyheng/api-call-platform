package com.example.apicall.controller;

import com.example.apicall.service.ApiTreeService;
import com.example.apicall.vo.GetApiTreeResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tree")
public class ApiTreeController {

    @Autowired
    private ApiTreeService apiTreeService;

    /**
     * 获取所有API项目、请求和测试用例的树形结构
     * @return 树形结构列表
     */
    @GetMapping
    public List<GetApiTreeResponseVO> getApiTree() {
        List<GetApiTreeResponseVO> apiTree = apiTreeService.getApiTree();
        return apiTree;
    }
}