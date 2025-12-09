package com.example.apicall.vo;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * getApiTree方法的响应VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetApiTreeResponseVO {
    private String id;
    private String label;
    private String type;
    private ApiDetailVO detail;

    /**
     * 确保children字段不为null
     */
    @Builder.Default
    private List<GetApiTreeResponseVO> children = Collections.emptyList();
}