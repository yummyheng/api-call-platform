package com.example.apicall.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.apicall.domain.ApiTestCase;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiTestCaseRepository extends BaseMapper<ApiTestCase> {
}