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
@TableName("api_test_case")
public class ApiTestCase {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    @TableField("api_id")
    private String apiId;
    @TableField("test_case_name")
    private String testCaseName;
    @TableField("test_data")
    private String testData;
    @TableField("expected_result")
    private String expectedResult;
    @TableField("is_enabled")
    private Boolean isEnabled;
    @TableField("created_by")
    private String createdBy;
    @TableField("created_time")
    private Date createdTime;
    @TableField("updated_by")
    private String updatedBy;
    @TableField("updated_time")
    private Date updatedTime;
}