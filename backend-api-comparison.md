# 后端API接口与前端文档比较表

| 接口路径 | 请求方法 | 功能描述 | 所在Controller | 前端文档是否包含 | 备注 |
|---------|---------|---------|-------------|-------------|------|
| `/environments` | GET | 获取所有环境配置 | ApiEnvironmentController | 是 | |
| `/environments/app/{appId}` | GET | 根据应用ID获取环境配置列表 | ApiEnvironmentController | 否 | 多余接口 |
| `/environments/{id}` | GET | 根据环境ID获取环境配置详情 | ApiEnvironmentController | 否 | 多余接口 |
| `/environments` | POST | 新增环境配置 | ApiEnvironmentController | 是 | |
| `/environments/{id}` | PUT | 更新环境配置 | ApiEnvironmentController | 是 | |
| `/environments/{id}` | DELETE | 删除环境配置 | ApiEnvironmentController | 是 | |
| `/environments/{id}/default/{appId}` | POST | 设置默认环境 | ApiEnvironmentController | 是 | |
| `/call/execute` | POST | 执行API请求 | ApiCallController | 否 | 多余接口 |
| `/call/execute/test-case/{id}` | POST | 执行测试用例 | ApiCallController | 是 | |
| `/projects` | GET | 获取项目列表 | ApiProjectController | 是 | |
| `/projects/app-ids` | GET | 获取所有应用ID | ApiProjectController | 否 | 多余接口 |
| `/projects/app/{appId}` | GET | 根据应用ID获取项目列表 | ApiProjectController | 否 | 多余接口 |
| `/projects/{id}` | GET | 根据项目ID获取项目详情 | ApiProjectController | 否 | 多余接口 |
| `/projects` | POST | 新增项目 | ApiProjectController | 否 | 多余接口 |
| `/logs/import` | POST | 上传日志文件导入 | LogImportController | 是 | |
| `/logs/import-by-path` | POST | 通过文件路径导入日志 | LogImportController | 是 | |
| `/tree` | GET | 获取API树数据 | ApiTreeController | 是 | |
| `/tree/app/{appId}` | GET | 根据应用ID获取API树数据 | ApiTreeController | 否 | 多余接口 |
| `/tree/test-case/{testCaseId}` | GET | 根据测试用例ID获取API树数据 | ApiTreeController | 否 | 多余接口 |
| `/requests/project/{projectId}` | GET | 根据项目ID获取请求列表 | ApiRequestController | 否 | 多余接口 |
| `/requests/{id}` | GET | 根据请求ID获取请求详情 | ApiRequestController | 否 | 多余接口 |
| `/requests` | POST | 新增请求 | ApiRequestController | 否 | 多余接口 |
| `/test-cases/api/{apiId}` | GET | 根据API ID获取测试用例列表 | ApiTestCaseController | 否 | 多余接口 |
| `/test-cases/{id}` | GET | 根据测试用例ID获取测试用例详情 | ApiTestCaseController | 否 | 多余接口 |
| `/test-cases` | POST | 新增测试用例 | ApiTestCaseController | 否 | 多余接口 |
| `/test-cases/{id}/test-data` | POST | 新增测试数据 | ApiTestCaseController | 否 | 多余接口 |

## 分析总结

### 前端已使用的接口（10个）
这些接口在前端文档中明确列出并被前端组件使用：
- GET `/environments`
- POST `/environments`
- PUT `/environments/{id}`
- DELETE `/environments/{id}`
- POST `/environments/{id}/default/{appId}`
- POST `/call/execute/test-case/{id}`
- GET `/projects`
- POST `/logs/import`
- POST `/logs/import-by-path`
- GET `/tree`

### 后端多余的接口（16个）
这些接口在后端实现中存在，但前端文档中未提及且前端组件未使用：
- GET `/environments/app/{appId}`
- GET `/environments/{id}`
- POST `/call/execute`
- GET `/projects/app-ids`
- GET `/projects/app/{appId}`
- GET `/projects/{id}`
- POST `/projects`
- GET `/tree/app/{appId}`
- GET `/tree/test-case/{testCaseId}`
- GET `/requests/project/{projectId}`
- GET `/requests/{id}`
- POST `/requests`
- GET `/test-cases/api/{apiId}`
- GET `/test-cases/{id}`
- POST `/test-cases`
- POST `/test-cases/{id}/test-data`

### 建议

1. **保留必要接口**：保留前端文档中列出的10个接口，这些是前端正常运行所必需的。

2. **考虑删除或保留多余接口**：
   - 对于功能完整但前端暂未使用的接口（如获取环境详情、项目详情等），可以考虑保留，作为未来功能扩展的基础。
   - 对于可能是测试或遗留的接口（如POST `/call/execute`），可以考虑删除或标记为内部接口。

3. **文档更新**：如果决定保留某些多余接口，建议更新前端接口文档，明确这些接口的用途和使用方式。

4. **接口版本控制**：如果需要删除接口，建议采用版本控制的方式，确保不影响可能使用这些接口的其他系统。

5. **前后端同步**：建立前后端接口变更的沟通机制，确保前端文档与后端实现保持一致。