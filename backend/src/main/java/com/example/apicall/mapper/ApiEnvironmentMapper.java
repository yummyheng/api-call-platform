package com.example.apicall.mapper;

import com.example.apicall.domain.ApiEnvironment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 环境配置Mapper接口
 */
@Mapper
public interface ApiEnvironmentMapper extends BaseMapper<ApiEnvironment> {
    /**
     * 根据应用ID查询环境配置列表
     * @param appId 应用ID
     * @return 环境配置列表
     */
    List<ApiEnvironment> selectByAppId(@Param("appId") String appId);

    /**
     * 更新默认环境状态
     * @param appId 应用ID
     * @param isDefault 是否默认环境
     * @return 影响行数
     */
    int updateDefaultStatus(@Param("appId") String appId, @Param("isDefault") Boolean isDefault);
}