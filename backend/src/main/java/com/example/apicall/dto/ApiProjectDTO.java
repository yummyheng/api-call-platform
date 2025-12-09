package com.example.apicall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiProjectDTO {
    private String id;
    private String appId;
    private String projectName;
    private String description;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
}