<template>
  <!-- 执行测试对话框 -->
  <el-dialog
    title="执行测试案例"
    :visible.sync="executeTestDialogVisible"
    width="600px"
    top="10vh"
    :z-index="9999"
  >
    <div class="execute-test-dialog">
      <!-- 请求信息 -->
      <div class="dialog-section">
        <h3>请求信息</h3>
        <div class="info-item">
          <span class="info-label">URL：</span>
          <span class="info-value">{{ testExecutionParams.url }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">Method：</span>
          <el-tag :type="getMethodType(testExecutionParams.method)">
            {{ testExecutionParams.method }}
          </el-tag>
        </div>
        <div class="info-item">
          <span class="info-label">环境：</span>
          <el-tooltip
            :content="getCurrentEnvironmentBaseUrl()"
            placement="top"
            effect="dark"
            :disabled="!testExecutionParams.environmentId"
          >
            <el-select v-model="testExecutionParams.environmentId" placeholder="请选择环境" style="width: 200px;">
              <el-option
                v-for="environment in environments"
                :key="environment.id"
                :label="environment.envName"
                :value="environment.id"
                :disabled="false"
              ></el-option>
            </el-select>
          </el-tooltip>
        </div>
      </div>

      <!-- 请求报文 -->
      <div class="dialog-section">
        <h3>请求报文</h3>
        <el-input
          type="textarea"
          :rows="8"
          v-model="testExecutionParams.requestData"
          placeholder="请输入请求数据（JSON格式）"
        ></el-input>
      </div>

      <!-- 执行结果 -->
      <div v-if="testExecutionResult" class="dialog-section">
        <h3>执行结果</h3>
        <div class="info-item">
          <span class="info-label">状态码：</span>
          <el-tag :type="testExecutionResult.success ? 'success' : 'danger'">
            {{ testExecutionResult.statusCode }}
          </el-tag>
        </div>
        <div class="info-item">
          <span class="info-label">执行时间：</span>
          <span class="info-value">{{ testExecutionResult.duration }}ms</span>
        </div>
        <div class="info-item">
          <div class="info-header">
            <span class="info-label">响应结果：</span>
            <button class="copy-btn" @click="copyResponseResult" title="复制">
              <i class="el-icon-document-copy"></i>
            </button>
          </div>
          <pre class="response-result">{{ testExecutionResult.responseBody }}</pre>
        </div>

        <!-- HTTP客户端调用详情 -->
        <div v-if="testExecutionResult.httpClientCallDetail" class="dialog-section">
          <h3>HTTP客户端调用详情</h3>
          
          <!-- 请求详情 -->
          <div v-if="testExecutionResult.httpClientCallDetail.requestUrl" class="info-item">
            <span class="info-label">请求URL：</span>
            <span class="info-value">{{ testExecutionResult.httpClientCallDetail.requestUrl }}</span>
          </div>
          <div v-if="testExecutionResult.httpClientCallDetail.httpMethod" class="info-item">
            <span class="info-label">请求方法：</span>
            <el-tag :type="getMethodType(testExecutionResult.httpClientCallDetail.httpMethod)">
              {{ testExecutionResult.httpClientCallDetail.httpMethod }}
            </el-tag>
          </div>
          <div v-if="testExecutionResult.httpClientCallDetail.requestHeaders" class="info-item">
            <span class="info-label">请求头：</span>
            <pre class="response-result">{{ formatJson(testExecutionResult.httpClientCallDetail.requestHeaders) }}</pre>
          </div>
          <div v-if="testExecutionResult.httpClientCallDetail.requestParams" class="info-item">
            <span class="info-label">请求参数：</span>
            <pre class="response-result">{{ formatJson(testExecutionResult.httpClientCallDetail.requestParams) }}</pre>
          </div>
          <div v-if="testExecutionResult.httpClientCallDetail.requestBody" class="info-item">
            <span class="info-label">请求体：</span>
            <pre class="response-result">{{ formatJson(testExecutionResult.httpClientCallDetail.requestBody) }}</pre>
          </div>
          
          <!-- 响应详情 -->
          <div v-if="testExecutionResult.httpClientCallDetail.responseStatusCode" class="info-item">
            <span class="info-label">响应状态码：</span>
            <el-tag :type="testExecutionResult.httpClientCallDetail.responseStatusCode >= 200 && testExecutionResult.httpClientCallDetail.responseStatusCode < 300 ? 'success' : 'danger'">
              {{ testExecutionResult.httpClientCallDetail.responseStatusCode }}
            </el-tag>
          </div>
          <div v-if="testExecutionResult.httpClientCallDetail.responseHeaders" class="info-item">
            <span class="info-label">响应头：</span>
            <pre class="response-result">{{ formatJson(testExecutionResult.httpClientCallDetail.responseHeaders) }}</pre>
          </div>
          <div v-if="testExecutionResult.httpClientCallDetail.responseBody" class="info-item">
            <span class="info-label">响应体：</span>
            <pre class="response-result">{{ formatJson(testExecutionResult.httpClientCallDetail.responseBody) }}</pre>
          </div>
          
          <!-- 执行信息 -->
          <div v-if="testExecutionResult.httpClientCallDetail.startTime" class="info-item">
            <span class="info-label">开始时间：</span>
            <span class="info-value">{{ testExecutionResult.httpClientCallDetail.startTime }}</span>
          </div>
          <div v-if="testExecutionResult.httpClientCallDetail.endTime" class="info-item">
            <span class="info-label">结束时间：</span>
            <span class="info-value">{{ testExecutionResult.httpClientCallDetail.endTime }}</span>
          </div>
          <div v-if="testExecutionResult.httpClientCallDetail.executionTimeMs" class="info-item">
            <span class="info-label">执行时间：</span>
            <span class="info-value">{{ testExecutionResult.httpClientCallDetail.executionTimeMs }}ms</span>
          </div>
          <div v-if="testExecutionResult.httpClientCallDetail.executionStatus" class="info-item">
            <span class="info-label">执行状态：</span>
            <el-tag :type="testExecutionResult.httpClientCallDetail.executionStatus === 'COMPLETED' ? 'success' : testExecutionResult.httpClientCallDetail.executionStatus === 'FAILED' ? 'danger' : 'warning'">
              {{ testExecutionResult.httpClientCallDetail.executionStatus }}
            </el-tag>
          </div>
          <div v-if="testExecutionResult.httpClientCallDetail.errorMessage" class="info-item">
            <span class="info-label">错误信息：</span>
            <pre class="response-result error-message">{{ testExecutionResult.httpClientCallDetail.errorMessage }}</pre>
          </div>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">取消</el-button>
      <el-button type="primary" @click="executeTestCaseFromDialog" :loading="isExecuting">
        {{ isExecuting ? '执行中...' : '执行测试' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
// 导入统一的API管理模块
import api from '../api/api';

export default {
  name: 'ApiTestCaseExecutor',
  
  props: {
    apiTreeData: {
      type: Object,
      default: null
    },
    testData: {
      type: String,
      default: ''
    },
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  
  data() {
    return {
      // 执行测试对话框
      executeTestDialogVisible: this.dialogVisible,
      // 测试执行参数
      testExecutionParams: {
        url: '',
        method: '',
        requestData: '',
        environmentId: '' // 添加环境ID参数
      },
      // 环境列表
      environments: [],
      // 测试执行结果
      testExecutionResult: null,
      // 执行中状态
      isExecuting: false
    };
  },
  
  watch: {
    dialogVisible(newVal) {
      this.executeTestDialogVisible = newVal;
      if (newVal) {
        console.log('对话框已打开，开始获取环境列表');
        this.fetchEnvironments();
      }
    },
    executeTestDialogVisible(newVal) {
      this.$emit('update:dialogVisible', newVal);
    },
    apiTreeData() {
      if (this.executeTestDialogVisible) {
        this.initTestParams();
      }
    },
    testData() {
      if (this.executeTestDialogVisible) {
        this.testExecutionParams.requestData = this.testData || this.apiTreeData?.detail?.testCase?.testData || '{}';
      }
    },
    // 监听环境变化，更新URL
    'testExecutionParams.environmentId': {
      handler(newEnvId) {
        this.testExecutionParams.url = this.getFullApiUrl(newEnvId);
      },
      immediate: true
    }
  },
  
  methods: {
    /**
     * 初始化测试参数
     */
    initTestParams() {
      if (!this.apiTreeData || !this.apiTreeData.detail) {
        this._showMessage('warning', '请先选择一个测试用例');
        this.closeDialog();
        return;
      }
      
      // 获取环境ID - 确保环境列表加载完成
      let envId = this.testExecutionParams.environmentId;
      if (!envId && this.environments.length > 0) {
        envId = this.environments.find(env => env.isDefault)?.id || this.environments[0].id;
      }
      
      // 设置测试执行参数
      this.testExecutionParams = {
        url: this.getFullApiUrl(envId),
        method: this.apiTreeData.detail.request?.httpMethod || this.apiTreeData.detail.request?.method || '',
        requestData: this.testData || this.apiTreeData.detail.testCase?.testData || '{}',
        environmentId: envId // 默认选择默认环境
      };
      
      console.log('测试执行参数已初始化:', this.testExecutionParams);
      console.log('当前环境列表:', this.environments);
      
      // 清除之前的执行结果
      this.testExecutionResult = null;
    },
    
    /**
     * 关闭对话框
     */
    closeDialog() {
      this.executeTestDialogVisible = false;
    },
    
    /**
     * 从对话框执行测试用例
     */
    executeTestCaseFromDialog() {
      if (!this.apiTreeData || !this.apiTreeData.detail || !this.apiTreeData.detail.testCase) {
        this._showMessage('warning', '请先选择一个测试用例');
        return;
      }
      
      // 验证请求数据
      const testData = this._validateRequestJson(this.testExecutionParams.requestData);
      if (testData === null) {
        return;
      }
      
      // 执行测试
      this.executeTest();
    },
    
    /**
     * 获取环境列表
     */
    fetchEnvironments() {
      api.getEnvironments().then(environments => {
        this.environments = environments;
        
        // 手动设置默认环境
        if (this.environments.length > 0) {
          const defaultEnv = this.environments.find(env => env.isDefault) || this.environments[0];
          this.testExecutionParams.environmentId = defaultEnv.id;
          
          // 更新URL
          this.testExecutionParams.url = this.getFullApiUrl(defaultEnv.id);
        }
        
        // 在环境数据加载完成后初始化测试参数
        this.initTestParams();
      }).catch(error => {
        console.error('获取环境列表失败:', error);
        // 即使获取环境失败，也初始化测试参数
        this.initTestParams();
      });
    },

    /**
     * 构建测试结果
     * @param {Object} params - 测试结果参数
     * @returns {Object} - 构建好的测试结果
     */
    _buildTestResult(params) {
      const { success, responseData, error, testData } = params;
      
      const baseResult = {
        id: Date.now().toString(),
        timestamp: new Date().toISOString(),
        apiId: this.apiTreeData.detail.request?.id || 'unknown',
        apiName: this.apiTreeData.detail.request?.apiName || this.apiTreeData.label,
        apiUrl: this.getApiUrl(),
        method: this.apiTreeData.detail.request?.httpMethod || this.apiTreeData.detail.request?.method || '',
        requestData: testData,
        success: success,
        httpClientCallDetail: {} // 默认空对象
      };
      
      if (success) {
        // 解析被调用API的响应体
        let calledApiResponseBody = '';
        let calledApiStatusCode = 0;
        
        try {
          // 解析实际调用的API响应
          if (responseData.httpClientCallDetail && responseData.httpClientCallDetail.responseBody) {
            const httpResponse = JSON.parse(responseData.httpClientCallDetail.responseBody);
            calledApiResponseBody = JSON.stringify(httpResponse, null, 2);
            calledApiStatusCode = httpResponse.code || 200;
          } else {
            calledApiResponseBody = JSON.stringify(responseData, null, 2);
            calledApiStatusCode = 200;
          }
        } catch (e) {
          calledApiResponseBody = responseData.httpClientCallDetail?.responseBody || JSON.stringify(responseData, null, 2);
          calledApiStatusCode = 200;
        }
        
        return {
          ...baseResult,
          statusCode: calledApiStatusCode,
          responseBody: calledApiResponseBody,
          duration: responseData.elapsedTime || responseData.httpClientCallDetail?.executionTimeMs || 0,
          httpClientCallDetail: responseData.httpClientCallDetail || {}
        };
      } else {
        // 处理失败情况
        return {
          ...baseResult,
          statusCode: error?.response?.status || 500,
          responseBody: error?.response?.data?.message || error?.message || 'Unknown error',
          duration: 0,
          httpClientCallDetail: error?.response?.data?.httpClientCallDetail || {}
        };
      }
    },

    /**
     * 获取环境ID
     * @returns {string|null} - 环境ID，如果获取失败则返回null
     */
    _getEnvironmentId() {
      // 首先尝试从当前选择的环境ID获取
      let envId = this.testExecutionParams.environmentId;
      if (envId) {
        return envId;
      }
      
      // 尝试从环境列表中获取默认环境ID
      const defaultEnv = this.environments.find(env => env.isDefault) || this.environments[0];
      if (defaultEnv) {
        envId = defaultEnv.id;
        this.testExecutionParams.environmentId = envId;
        return envId;
      }
      
      // 没有找到环境ID
      return null;
    },

    /**
     * 执行测试
     */
    executeTest() {
      if (!this.apiTreeData || !this.apiTreeData.detail || !this.apiTreeData.detail.testCase) {
        this._showMessage('warning', '请先选择一个测试用例');
        return;
      }
      
      try {
        // 获取测试用例ID
        const testCaseId = this.apiTreeData.detail.testCase.id;
        
        if (!testCaseId) {
          this._showMessage('warning', '测试用例ID不能为空');
          return;
        }
        
        // 验证请求数据
        const testData = this._validateRequestJson(this.testExecutionParams.requestData);
        if (testData === null) {
          return;
        }
        
        // 确保环境ID不为空
        const envId = this._getEnvironmentId();
        if (!envId) {
          this._showMessage('warning', '请先选择一个环境');
          return;
        }
        
        // 显示加载中状态
        this._showMessage('info', '正在执行测试...');
        
        // 设置执行中状态
        this.isExecuting = true;
        
        // 调用后端接口执行测试用例，传递环境ID作为请求参数
        api.executeTestCase(testCaseId, testData, envId)
          .then(response => {
            // 处理成功响应
            const responseData = response; // 注意：apiClient的响应拦截器已经直接返回了response.data
            
            // 构建测试结果
            const testResult = this._buildTestResult({
              success: true,
              responseData: responseData,
              testData: testData
            });
            
            // 更新组件中的执行结果
            this.testExecutionResult = testResult;
            
            // 发送测试执行结果给父组件
            this.$emit('test-executed', testResult);
            
            this._showMessage('success', '测试执行成功');
          })
          .catch(error => {
            // 处理错误响应
            console.error('Test execution error:', error);
            
            // 构建失败的测试结果
            const testResult = this._buildTestResult({
              success: false,
              error: error,
              testData: testData
            });
            
            // 更新组件中的执行结果
            this.testExecutionResult = testResult;
            
            // 发送测试执行结果给父组件
            this.$emit('test-executed', testResult);
            
            this._showMessage('error', '测试执行失败');
          })
          .finally(() => {
            // 关闭执行中状态
            this.isExecuting = false;
          });
      } catch (error) {
        // 处理同步错误
        console.error('Test execution error:', error);
        
        // 构建失败的测试结果
        const testResult = this._buildTestResult({
          success: false,
          error: error,
          testData: this._validateRequestJson(this.testExecutionParams.requestData) || {}
        });
        
        // 更新组件中的执行结果
        this.testExecutionResult = testResult;
        
        // 发送测试执行结果给父组件
        this.$emit('test-executed', testResult);
        
        // 关闭执行中状态
        this.isExecuting = false;
        
        this._showMessage('error', '测试执行失败');
      }
    },
    
    /**
     * 获取API URL（仅路径部分）
     */
    getApiUrl() {
      if (!this.apiTreeData) return '';
      
      // 对于测试用例节点，URL在detail.request中
      if (this.apiTreeData.type === 'test_case' && this.apiTreeData.detail && this.apiTreeData.detail.request) {
        const url = this.apiTreeData.detail.request.apiUrl || this.apiTreeData.detail.request.url || '';
        return url;
      }
      
      // 其他节点类型
      const url = this.apiTreeData.apiUrl || this.apiTreeData.url || '';
      return url;
    },
    
    /**
     * 获取完整的API URL（包含baseUrl）
     */
    getFullApiUrl(environmentId) {
      const apiPath = this.getApiUrl();
      if (!apiPath) return '';
      
      const envId = environmentId || this.testExecutionParams.environmentId;
      if (!envId) return apiPath;
      
      const selectedEnv = this.environments.find(env => env.id === envId);
      if (!selectedEnv || !selectedEnv.baseUrl) return apiPath;
      
      // 确保baseUrl不以/结尾，apiPath不以/开头
      const baseUrl = selectedEnv.baseUrl.replace(/\/$/, '');
      const path = apiPath.replace(/^\//, '');
      
      return `${baseUrl}/${path}`;
    },
    
    /**
     * 获取当前选中环境的baseUrl
     */
    getCurrentEnvironmentBaseUrl() {
      if (!this.testExecutionParams.environmentId) return '';
      
      const selectedEnv = this.environments.find(env => env.id === this.testExecutionParams.environmentId);
      return selectedEnv ? selectedEnv.baseUrl : '';
    },
    
    /**
     * 获取HTTP方法类型样式
     */
    getMethodType(method) {
      const methodMap = {
        'GET': 'success',
        'POST': 'primary',
        'PUT': 'warning',
        'DELETE': 'danger',
        'PATCH': 'info'
      };
      return methodMap[method?.toUpperCase()] || 'info';
    },
    
    /**
     * 格式化JSON数据
     */
    formatJson(jsonData) {
      try {
        if (!jsonData) return '{}';
        if (typeof jsonData === 'string') {
          return JSON.stringify(JSON.parse(jsonData), null, 2);
        }
        return JSON.stringify(jsonData, null, 2);
      } catch (e) {
        return jsonData || '{}';
      }
    },
    
    /**
     * 复制响应结果到剪贴板
     */
    copyResponseResult() {
      console.log('copyResponseResult方法被调用，响应体:', this.testExecutionResult.responseBody);
      
      const content = this.testExecutionResult.responseBody;
      if (!content) {
        console.log('没有可复制的响应结果');
        this._showMessage('warning', '没有可复制的响应结果');
        return;
      }
      
      // 检查是否在安全上下文（HTTPS或localhost）
      if (window.isSecureContext) {
        console.log('使用现代化的Clipboard API进行复制');
        // 使用现代化的Clipboard API
        navigator.clipboard.writeText(content)
          .then(() => {
            console.log('复制成功（使用Clipboard API）');
            this._showMessage('success', '已复制到剪贴板');
          })
          .catch(err => {
            console.error('Clipboard API复制失败:', err);
            // 降级到传统方法
            this._fallbackCopyTextToClipboard(content);
          });
      } else {
        console.log('当前不是安全上下文，使用传统的复制方法');
        // 非安全上下文，直接使用传统方法
        this._fallbackCopyTextToClipboard(content);
      }
    },
    
    /**
     * 降级复制方法，使用传统的document.execCommand
     * @param {string} text - 要复制的文本
     */
    _fallbackCopyTextToClipboard(text) {
      console.log('_fallbackCopyTextToClipboard方法被调用，文本:', text);
      
      // 创建临时输入元素
      const textArea = document.createElement('textarea');
      textArea.value = text;
      textArea.style.position = 'fixed';
      textArea.style.opacity = '0';
      textArea.style.left = '-999999px';
      textArea.style.top = '-999999px';
      
      document.body.appendChild(textArea);
      
      // 选择并复制文本
      textArea.select();
      textArea.setSelectionRange(0, 999999); // 兼容移动设备
      
      try {
        const successful = document.execCommand('copy');
        if (successful) {
          console.log('复制成功（使用document.execCommand）');
          this._showMessage('success', '已复制到剪贴板');
        } else {
          console.error('document.execCommand复制失败');
          this._showMessage('error', '复制失败，请手动复制');
        }
      } catch (err) {
        console.error('document.execCommand执行错误:', err);
        this._showMessage('error', `复制失败: ${err.message}`);
      } finally {
        // 清理临时元素
        document.body.removeChild(textArea);
        console.log('复制操作完成，临时元素已清理');
      }
    },
    
    /**
     * 验证请求数据是否为有效JSON
     * @param {string} jsonString - 待验证的JSON字符串
     * @returns {Object|null} - 解析后的对象或null（如果无效）
     */
    _validateRequestJson(jsonString) {
      if (!jsonString) return {};
      
      try {
        return JSON.parse(jsonString);
      } catch (e) {
        this._showMessage('error', '测试数据格式无效，请输入有效的JSON');
        return null;
      }
    },
    
    /**
     * 显示消息
     * @param {string} type - 消息类型
     * @param {string} message - 消息内容
     */
    _showMessage(type, message) {
      try {
        if (this.$message && this.$message[type]) {
          this.$message[type](message);
        } else {
          console[type === 'error' ? 'error' : 'log'](message);
        }
      } catch (e) {
        console[type === 'error' || type === 'warning' ? 'error' : 'log'](message);
      }
    }
  }
};
</script>

<style scoped>
.execute-test-dialog {
  padding: 10px;
}

.dialog-section {
  margin-bottom: 20px;
}

.dialog-section h3 {
  margin-bottom: 10px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.info-item {
  margin-bottom: 10px;
  display: flex;
  align-items: flex-start;
}

.info-label {
  width: 80px;
  font-weight: bold;
  color: #666;
  margin-right: 10px;
}

.info-value {
  flex: 1;
  word-break: break-all;
  color: #333;
}

.response-result {
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  overflow-x: auto;
  overflow-y: auto;
  max-height: 300px;
  font-family: 'Courier New', Courier, monospace;
  font-size: 14px;
  line-height: 1.8;
  color: #333;
  white-space: pre-wrap;
  word-break: break-all;
}



/* 错误信息样式 */
.error-message {
  background-color: #fef0f0;
  border-color: #fbc4c4;
  color: #f56c6c;
}

.dialog-footer {
  text-align: right;
}
</style>