# 前端调用的后端接口清单

| 接口路径 | 请求方法 | 功能描述 | 所在组件 | 参数说明 |
|---------|---------|---------|---------|---------|
| `/api/tree` | GET | 获取API树数据 | App.vue | 无 |
| `/api/environments` | GET | 获取环境列表 | ApiEnvironmentConfig.vue、ApiLogImporter.vue | 无 |
| `/api/environments` | POST | 新增环境 | ApiEnvironmentConfig.vue | `appId`、`envName`、`envDescription`、`baseUrl`、`isDefault`、`headers`、`params` |
| `/api/environments/{id}` | PUT | 更新环境 | ApiEnvironmentConfig.vue | `id`（路径参数）、`appId`、`envName`、`envDescription`、`baseUrl`、`isDefault`、`headers`、`params` |
| `/api/environments/{id}` | DELETE | 删除环境 | ApiEnvironmentConfig.vue | `id`（路径参数） |
| `/api/environments/{id}/default/{appId}` | POST | 设置默认环境 | ApiEnvironmentConfig.vue | `id`（路径参数）、`appId`（路径参数） |
| `/api/call/execute/test-case/{testCaseId}` | POST | 执行测试用例 | ApiTestCaseExecutor.vue | `testCaseId`（路径参数）、`environmentId`、`requestData` |
| `/api/projects` | GET | 获取项目列表 | ApiLogImporter.vue | 无 |
| `/api/logs/import` | POST | 上传日志文件导入 | ApiLogImporter.vue | `environmentId`、`projectId`、日志文件 |
| `/api/logs/import-by-path` | POST | 通过文件路径导入日志 | ApiLogImporter.vue | `environmentId`、`projectId`、`filePath` |