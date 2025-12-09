package com.example.apicall.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.apicall.domain.ApiCallLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiCallLogRepository extends BaseMapper<ApiCallLog> {
}