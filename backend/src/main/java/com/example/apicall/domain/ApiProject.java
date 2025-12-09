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
@TableName("api_project")
public class ApiProject {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    @TableField("app_id")
    private String appId;
    @TableField("project_name")
    private String projectName;
    @TableField("description")
    private String description;
    @TableField("created_by")
    private String createdBy;
    @TableField("created_time")
    private Date createdTime;
    @TableField("updated_by")
    private String updatedBy;
    @TableField("updated_time")
    private Date updatedTime;
}