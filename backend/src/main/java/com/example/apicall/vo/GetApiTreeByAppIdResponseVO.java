package com.example.apicall.vo;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * getApiTreeByAppId方法的响应VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetApiTreeByAppIdResponseVO {
    private String id;
    private String label;
    private String type;
    private ApiDetailVO detail;

    /**
     * 确保children字段不为null
     */
    @Builder.Default
    private List<GetApiTreeByAppIdResponseVO> children = Collections.emptyList();
}