package com.example.apicall.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.apicall.domain.ApiRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiRequestRepository extends BaseMapper<ApiRequest> {
}