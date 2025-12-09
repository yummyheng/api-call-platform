import axios from 'axios'

// 创建axios实例
const apiClient = axios.create({
  baseURL: '/api', // 与前端配置一致
  timeout: 10000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
apiClient.interceptors.request.use(
  config => {
    // 可以在这里添加认证信息等
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
apiClient.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    // 统一错误处理
    console.error('API请求错误:', error)
    return Promise.reject(error)
  }
)

// API接口统一管理
export const api = {
  // API树相关
  getApiTree() {
    return apiClient.get('/tree')
  },
  
  // 环境管理相关
  getEnvironments() {
    return apiClient.get('/environments')
  },
  deleteEnvironment(id) {
    return apiClient.delete(`/environments/${id}`)
  },
  setDefaultEnvironment(id, appId) {
    return apiClient.post(`/environments/${id}/default/${appId}`)
  },
  updateEnvironment(id, data) {
    return apiClient.put(`/environments/${id}`, data)
  },
  createEnvironment(data) {
    return apiClient.post('/environments', data)
  },
  
  // 测试用例执行
  executeTestCase(testCaseId, data, environmentId) {
    return apiClient.post(`/call/execute/test-case/${testCaseId}`, data, {
      params: { environmentId }
    })
  },
  
  // 日志导入相关
  importLogs(data) {
    return apiClient.post('/logs/import', data)
  },
  importLogsByPath(data) {
    return apiClient.post('/logs/import-by-path', data)
  },
  
  // 项目相关
  getProjects() {
    return apiClient.get('/projects')
  }
}

export default api