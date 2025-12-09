package com.example.apicall.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.apicall.domain.ApiProject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiProjectRepository extends BaseMapper<ApiProject> {
}