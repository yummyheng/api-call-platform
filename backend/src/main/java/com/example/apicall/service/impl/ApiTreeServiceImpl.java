package com.example.apicall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.apicall.domain.ApiProject;
import com.example.apicall.domain.ApiRequest;
import com.example.apicall.domain.ApiTestCase;
import com.example.apicall.common.constant.TreeConstants;
import com.example.apicall.common.enums.TreeNodeTypeEnum;
import com.example.apicall.repository.ApiProjectRepository;
import com.example.apicall.repository.ApiRequestRepository;
import com.example.apicall.repository.ApiTestCaseRepository;
import com.example.apicall.service.ApiTreeService;
import com.example.apicall.vo.ApiDetailVO;

import com.example.apicall.vo.GetApiTreeResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiTreeServiceImpl implements ApiTreeService {

    @Autowired
    private ApiProjectRepository apiProjectRepository;

    @Autowired
    private ApiRequestRepository apiRequestRepository;

    @Autowired
    private ApiTestCaseRepository apiTestCaseRepository;

    @Override
    public List<GetApiTreeResponseVO> getApiTree() {
        // 获取所有项目
        List<ApiProject> projects = apiProjectRepository.selectList(null);
        // 构建项目节点列表
        List<GetApiTreeResponseVO> projectNodes = buildApiTreeForAll(projects);
        
        // 创建母节点
        GetApiTreeResponseVO rootNode = new GetApiTreeResponseVO();
        rootNode.setId(TreeConstants.ROOT_NODE_ID);
        rootNode.setLabel(TreeConstants.ROOT_NODE_NAME);
        rootNode.setType(TreeNodeTypeEnum.ROOT.getValue());
        rootNode.setChildren(projectNodes);
        
        // 返回包含母节点的列表
        return Collections.singletonList(rootNode);
    }

    private List<GetApiTreeResponseVO> buildApiTreeForAll(List<ApiProject> projects) {
        // 构建项目节点
        return projects.stream()
                .map(project -> {
                    // 获取当前项目下的所有API请求
                    List<ApiRequest> requests = apiRequestRepository.selectList(
                            new LambdaQueryWrapper<ApiRequest>().eq(ApiRequest::getProjectId, project.getId())
                    );

                    // 构建请求节点
                    List<GetApiTreeResponseVO> requestNodes = requests.stream()
                            .map(request -> {
                                // 获取当前请求下的所有测试用例
                                List<ApiTestCase> testCases = apiTestCaseRepository.selectList(
                                        new LambdaQueryWrapper<ApiTestCase>().eq(ApiTestCase::getApiId, request.getId())
                                );

                                // 构建测试用例节点
                                List<GetApiTreeResponseVO> testCaseNodes = testCases.stream()
                                        .map(testCase -> {
                                            // 构建详细信息VO
                                            ApiDetailVO detailVO = ApiDetailVO.builder()
                                                    .project(project)
                                                    .request(request)
                                                    .testCase(testCase)
                                                    .build();

                                            return GetApiTreeResponseVO.builder()
                                                .id(testCase.getId().toString())
                                                .label(testCase.getTestCaseName())
                                                .type(TreeNodeTypeEnum.TEST_CASE.getValue())
                                                .children(Collections.emptyList())
                                                .detail(detailVO)
                                                .build();
                                        })
                                        .collect(Collectors.toList());

                                // 构建请求节点
                                return GetApiTreeResponseVO.builder()
                                        .id(request.getId().toString())
                                        .label(request.getApiUrl())
                                        .type(TreeNodeTypeEnum.REQUEST.getValue())
                                        .children(testCaseNodes)
                                        .build();
                            })
                            .collect(Collectors.toList());

                    // 构建项目节点
                    return GetApiTreeResponseVO.builder()
                            .id(project.getId().toString())
                            .label(project.getProjectName())
                            .type(TreeNodeTypeEnum.PROJECT.getValue())
                            .children(requestNodes)
                            .build();
                })
                .collect(Collectors.toList());
    }
}