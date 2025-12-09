package com.example.apicall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiTreeVO {
    private String id;
    private String label;
    private String type;
    private Object children;
    private ApiDetailVO detail;
}