package com.example.apicall.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("api_call_log")
public class ApiCallLog {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    @TableField("api_id")
    private String apiId;
    @TableField("test_case_id")
    private String testCaseId;
    @TableField("request_params")
    private String requestParams;
    @TableField("actual_result")
    private String actualResult;
    @TableField("status_code")
    private Integer statusCode;
    @TableField("response_time")
    private Integer responseTime;
    @TableField("call_time")
    private Date callTime;
}