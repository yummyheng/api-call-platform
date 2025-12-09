package com.example.apicall.service;

import com.example.apicall.vo.GetApiTreeResponseVO;

import java.util.List;

public interface ApiTreeService {
    /**
     * 获取所有API项目、请求和测试用例的树形结构
     * @return 树形结构列表
     */
    List<GetApiTreeResponseVO> getApiTree();
}