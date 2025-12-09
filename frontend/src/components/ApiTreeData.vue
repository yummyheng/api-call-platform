<template>
  <!-- 根容器 -->
  <div class="api-tree-data-wrapper">
    <!-- 中间栏数据展示容器 -->
    <div class="api-tree-data-container" v-if="apiTreeData">
      <!-- 卡片容器 -->
      <el-card class="modern-card">
        <template #header>
          <!-- 顶部操作栏 -->
          <div class="card-header">
            <div class="card-header-actions">
      <!-- 执行测试按钮 -->
        <el-button type="primary" size="large" @click="handleExecuteTest" class="execute-test-button">
          <i class="el-icon-check-circle"></i>执行测试
        </el-button>
        <!-- 日志导入按钮 -->
        <el-button type="primary" size="large" @click="handleLogImport" class="execute-test-button">
          <i class="el-icon-upload"></i>日志导入
        </el-button>
            </div>
          </div>
        </template>
      
      <!-- 节点类型标识 -->
      <div class="node-type-section">
        <el-tag :type="getNodeTypeColor()" effect="dark">{{ getNodeTypeName() }}</el-tag>
        
        <!-- HTTP方法标识（仅API类型显示） -->
        <el-tag v-if="getNodeMethod()" :type="getMethodType(getNodeMethod())" class="method-tag">
          {{ getNodeMethod() }}
        </el-tag>
      </div>
      
      <!-- 节点标题 -->
      <h2 class="node-title">{{ apiTreeData.label }}</h2>
      
      <!-- API详情信息 -->
      <div v-if="!isTestCaseNode" class="detail-section info-card">
        <!-- API URL信息 -->
        <div class="info-item">
          <span class="info-label">URL：</span>
          <span class="info-value">{{ getApiUrl() }}</span>
        </div>
        
        <!-- API描述信息 -->
        <div class="info-item" v-if="getNodeDescription()">
          <span class="info-label">描述：</span>
          <div class="description">{{ getNodeDescription() }}</div>
        </div>
        
        <!-- 详细数据预览 -->
        <div class="info-item detail-data-preview" v-if="formatDetailData()">
          <span class="info-label">详细数据：</span>
          <pre>{{ formatDetailData() }}</pre>
        </div>
      </div>
      
      <!-- 测试用例详情 -->
      <div v-if="isTestCaseNode" class="test-case-section info-card">
        <!-- 测试用例信息 -->
        <div class="info-item">
          <span class="info-label">测试用例：</span>
          <span class="info-value">{{ apiTreeData.label }}</span>
        </div>
        
        <!-- API URL信息（测试用例） -->
        <div class="info-item">
          <span class="info-label">API URL：</span>
          <span class="info-value">{{ getApiUrl() }}</span>
        </div>
        
        <!-- 项目信息 - 可折叠 -->
        <div v-if="apiTreeData.detail && apiTreeData.detail.project" class="info-card project-info">
          <div class="section-header" @click="toggleSection('project')">
            <h3 class="section-title">项目信息</h3>
            <span class="toggle-icon" :class="{ expanded: expandedSections?.project }">▼</span>
          </div>
          <transition name="slide">
            <div v-if="!expandedSections || expandedSections.project" class="info-content">
              <div class="info-item">
                <span class="info-label">项目名称:</span>
                <span class="info-value">{{ apiTreeData.detail.project?.projectName || apiTreeData.detail.project?.name || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">项目描述:</span>
                <span class="info-value">{{ apiTreeData.detail.project?.description || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">应用ID:</span>
                <span class="info-value">{{ apiTreeData.detail.project?.appId || '-' }}</span>
              </div>
            </div>
          </transition>
        </div>
        
        <!-- API请求信息 - 可折叠 -->
        <div v-if="apiTreeData.detail && apiTreeData.detail.request" class="info-card request-info">
          <div class="section-header" @click="toggleSection('request')">
            <h3 class="section-title">API请求信息</h3>
            <span class="toggle-icon" :class="{ expanded: expandedSections?.request }">▼</span>
          </div>
          <transition name="slide">
            <div v-if="!expandedSections || expandedSections.request" class="info-content">
              <div class="info-item">
                <span class="info-label">请求路径:</span>
                <span class="info-value api-path">{{ apiTreeData.detail.request?.apiUrl || apiTreeData.detail.request?.url || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">请求方法:</span>
                <span class="info-value">
                  <span class="method-badge" :class="getMethodClass(apiTreeData.detail.request?.httpMethod || apiTreeData.detail.request?.method || '')">
                    {{ apiTreeData.detail.request?.httpMethod || apiTreeData.detail.request?.method || '-' }}
                  </span>
                </span>
              </div>
              <div class="info-item">
                <span class="info-label">API描述:</span>
                <span class="info-value">{{ apiTreeData.detail.request?.description || apiTreeData.detail.request?.apiName || '-' }}</span>
              </div>
            </div>
          </transition>
        </div>
        
        <!-- 测试用例详细信息 - 可折叠 -->
        <div v-if="apiTreeData.detail && apiTreeData.detail.testCase" class="info-card testcase-info">
          <div class="section-header" @click="toggleSection('testcase')">
            <h3 class="section-title">测试用例配置</h3>
            <span class="toggle-icon" :class="{ expanded: expandedSections?.testcase }">▼</span>
          </div>
          <transition name="slide">
            <div v-if="!expandedSections || expandedSections.testcase" class="info-content">
              <div class="info-item">
                <span class="info-label">测试用例名称:</span>
                <span class="info-value">{{ apiTreeData.detail.testCase?.testCaseName || apiTreeData.detail.testCase?.name || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">预期结果:</span>
                <span class="info-value">{{ apiTreeData.detail.testCase?.expectedResult || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">创建者:</span>
                <span class="info-value">{{ apiTreeData.detail.testCase?.createdBy || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">创建时间:</span>
                <span class="info-value">{{ formatDate(apiTreeData.detail.testCase?.createdTime || apiTreeData.detail.testCase?.createdAt || '') }}</span>
              </div>
            </div>
          </transition>
        </div>
        
        <!-- 原始detail数据预览 - 可折叠 -->
        <div class="info-card raw-detail-preview">
          <div class="section-header" @click="toggleSection('rawData')">
            <h3 class="section-title">原始数据预览</h3>
            <span class="toggle-icon" :class="{ expanded: expandedSections?.rawData }">▼</span>
          </div>
          <transition name="slide">
            <div v-if="!expandedSections || expandedSections.rawData" class="json-preview-container">
              <div class="info-header">
                <span class="info-label">原始数据：</span>
                <button class="copy-btn" @click="copyRawData" title="复制">
<i class="el-icon-document-copy"></i>
                </button>
              </div>
              <pre class="json-preview">{{ formatDetailData() }}</pre>
            </div>
          </transition>
        </div>
      </div>
      
      <!-- 测试数据输入区域 -->
      <div v-if="!isTestCaseNode" class="test-input-section">
        <h4>测试数据</h4>
        <el-input
          type="textarea"
          :rows="8"
          v-model="testData"
          placeholder="请输入测试数据（JSON格式）"
        ></el-input>
        
        <!-- 操作按钮区域 -->
        <div class="action-buttons">
          <el-button @click="saveTestData">保存测试数据</el-button>
        </div>
      </div>
      
      <!-- 相关测试用例列表 -->
      <div v-if="relatedTestCases && relatedTestCases.length > 0" class="test-cases-section">
        <h4>相关测试用例</h4>
        <el-table :data="relatedTestCases" @row-click="handleTestCaseClick">
          <el-table-column prop="name" label="测试用例名称" width="200"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" :formatter="formatDate"></el-table-column>
          <el-table-column prop="description" label="描述"></el-table-column>
        </el-table>
      </div>
    </el-card>
    </div>
    
    <!-- 无数据时的提示信息 -->
    <div v-else class="no-data">
      <div class="empty-state">
        请选择API或测试用例查看详情
      </div>
    </div>

  <!-- 引入执行测试组件 -->
  <api-test-case-executor
    :api-tree-data="apiTreeData"
    :test-data="testData"
    :dialog-visible.sync="executeTestDialogVisible"
    @test-executed="handleTestExecuted"
  />
  
  <!-- 引入日志导入组件 -->
  <api-log-importer
    :dialog-visible.sync="logImportDialogVisible"
    @log-imported="handleLogImported"
  />
  
</div>
</template>

<script>
import '../assets/styles/components/_api-tree-data.scss';
import ApiTestCaseExecutor from './ApiTestCaseExecutor.vue';
import ApiLogImporter from './ApiLogImporter.vue';


export default {
  components: {
    ApiTestCaseExecutor,
    ApiLogImporter
  },
  name: 'ApiTreeData',
  
  /**
   * 组件属性定义
   */
  props: {
    // API树节点数据
    apiTreeData: {
      type: Object,
      default: null
    },
    // 测试用例详情数据
    testCaseDetail: {
      type: Object,
      default: null
    },
    // 测试数据JSON字符串
    testData: {
      type: String,
      default: ''
    },
    // 相关测试用例列表
    relatedTestCases: {
      type: Array,
      default: () => []
    }
  },
  
  /**
   * 计算属性
   */
  computed: {
    // 判断当前节点是否为测试用例类型
    isTestCaseNode() {
      return this.apiTreeData && this.apiTreeData.type === 'test_case';
    },
    

  },
  
  /**
   * 组件数据
   */
  data() {
    return {
      expandedSections: {
        project: true,
        request: true,
        testcase: true,
        rawData: true
      },
      // 主题状态
      isDarkMode: false,
      // 执行测试对话框可见性
      executeTestDialogVisible: false,
      // 日志导入对话框可见性
      logImportDialogVisible: false
    };
  },
  
  /**
   * 生命周期钩子 - 组件挂载后
   */
  mounted() {
    // 初始化主题
    this.initTheme();
    // 监听localStorage中的主题变化
    window.addEventListener('storage', this.handleThemeChange);
  },
  
  /**
   * 生命周期钩子 - 组件销毁前
   */
  beforeDestroy() {
    // 移除localStorage监听
    window.removeEventListener('storage', this.handleThemeChange);
  },

  /**
   * 组件方法
   */
  methods: {
    /**
     * 处理日志导入按钮点击事件
     */
    handleLogImport() {
      this.logImportDialogVisible = true;
    },
    
    /**
     * 处理日志导入完成事件
     * @param {Object} result - 日志导入结果
     */
    handleLogImported(result) {
      console.log('日志导入结果:', result);
      // 可以在这里处理导入完成后的逻辑，如刷新数据等
    },
    
    /**
     * 切换区域展开/折叠状态
     * @param {string} section - 区域名称
     */
    toggleSection(section) {
      this.expandedSections[section] = !this.expandedSections[section];
    },

    /**
     * 获取HTTP方法样式类
     * @param {string} method - HTTP方法
     * @returns {string} 样式类名
     */
    getMethodClass(method) {
      const methodClassMap = {
        'GET': 'method-get',
        'POST': 'method-post',
        'PUT': 'method-put',
        'DELETE': 'method-delete',
        'PATCH': 'method-patch',
        'HEAD': 'method-head',
        'OPTIONS': 'method-options'
      };
      return methodClassMap[method] || 'method-other';
    },


    
    /**
     * 获取节点类型信息
     */
    getNodeTypeInfo() {
      if (!this.apiTreeData) return { name: '', color: '' };
      
      const typeInfoMap = {
        'api': { name: 'API接口', color: 'primary' },
        'folder': { name: '文件夹', color: 'info' },
        'test_case': { name: '测试用例', color: 'success' },
        'module': { name: '模块', color: 'warning' }
      };
      
      return typeInfoMap[this.apiTreeData.type] || { name: '未知类型', color: 'info' };
    },
    
    /**
     * 获取节点类型名称
     * @returns {string} 节点类型的中文名称
     */
    getNodeTypeName() {
      return this.getNodeTypeInfo().name;
    },
    
    /**
     * 获取节点类型对应的颜色标签
     * @returns {string} Element UI标签类型
     */
    getNodeTypeColor() {
      return this.getNodeTypeInfo().color;
    },
    
    /**
     * 获取节点的HTTP方法
     * @returns {string} 大写的HTTP方法名称
     */
    getNodeMethod() {
      if (!this.apiTreeData || !this.apiTreeData.method) return '';
      return this.apiTreeData.method.toUpperCase();
    },
    
    /**
     * 获取节点描述
     * @returns {string} 节点描述文本
     */
    getNodeDescription() {
      return this.apiTreeData?.description || this.apiTreeData?.desc || '';
    },
    

    
    /**
     * 验证JSON格式是否有效
     * @param {string} jsonString - 待验证的JSON字符串
     * @returns {boolean} 是否为有效JSON
     */
    isValidJson(jsonString) {
      if (!jsonString) return true;
      
      try {
        JSON.parse(jsonString);
        return true;
      } catch (e) {
        return false;
      }
    },
    
    /**
     * 格式化JSON预览
     * @param {string} jsonString - 待格式化的JSON字符串
     * @returns {string} 格式化后的JSON字符串
     */
    formatJsonPreview(jsonString) {
      if (!jsonString) return '';
      
      try {
        const parsed = JSON.parse(jsonString);
        return JSON.stringify(parsed, null, 2);
      } catch (e) {
        return jsonString;
      }
    },
    
    /**
     * 格式化详细数据
     * @returns {string} 格式化后的详细数据JSON字符串
     */
    formatDetailData() {
      try {
        const { detail, detailVO } = this.apiTreeData || {};
        const detailData = {};
        
        // 处理detail对象
        if (detail) {
          const { project, request, testCase } = detail;
          if (project) detailData.project = project;
          if (request) detailData.request = request;
          if (testCase) detailData.testCase = testCase;
          
          // 如果没有直接的project/request/testCase字段，则使用整个detail对象
          if (Object.keys(detailData).length === 0) {
            detailData.detail = detail;
          }
        }
        
        // 保留原有的detailVO处理
        if (detailVO) {
          detailData.detailVO = detailVO;
        }
        
        // 只有当有数据时才返回格式化的JSON
        return Object.keys(detailData).length > 0 ? JSON.stringify(detailData, null, 2) : '';
      } catch (e) {
        console.error('格式化详细数据时出错:', e);
        return '无法格式化详细数据';
      }
    },
    
    /**
     * 根据HTTP方法获取标签类型
     * @param {string} method - HTTP方法
     * @returns {string} Element UI标签类型
     */
    getMethodType(method) {
      return {
        'GET': 'success',
        'POST': 'primary',
        'PUT': 'warning',
        'DELETE': 'danger'
      }[method] || 'info';
    },
    
    /**
     * 格式化日期
     * @param {string} dateString - 日期字符串
     * @returns {string} 格式化后的日期
     */
    formatDate(dateString) {
      return dateString ? new Date(dateString).toLocaleString() : '';
    },
    
    /**
     * 保存测试数据
     */
    saveTestData() {
      if (!this.apiTreeData) {
        this._showMessage('warning', '请先选择一个API');
        return;
      }
      
      try {
        // 验证测试数据是否为有效的JSON
        if (this.testData) {
          JSON.parse(this.testData);
        }
        
        // 触发保存事件
        this.$emit('save-test-data', this.testData);
        this._showMessage('success', '测试数据保存成功');
      } catch (e) {
        this._showMessage('error', '测试数据格式无效，请输入有效的JSON');
      }
    },
    
    /**
     * 处理测试用例点击
     * @param {Object} row - 测试用例行数据
     */
    handleTestCaseClick(row) {
      this.$emit('test-case-click', row);
    },
    
    /**
     * 复制内容到剪贴板
     * @param {string} content - 要复制的内容
     */
    copyToClipboard(content) {
      console.log('copyToClipboard方法被调用，内容:', content);
      
      if (!content) {
        console.log('没有可复制的内容');
        this.$message.warning('没有可复制的内容');
        return;
      }
      
      // 检查是否在安全上下文（HTTPS或localhost）
      if (window.isSecureContext) {
        console.log('使用现代化的Clipboard API进行复制');
        // 使用现代化的Clipboard API
        navigator.clipboard.writeText(content)
          .then(() => {
            console.log('复制成功（使用Clipboard API）');
            this.$message.success('已复制到剪贴板');
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
          this.$message.success('已复制到剪贴板');
        } else {
          console.error('document.execCommand复制失败');
          this.$message.error('复制失败，请手动复制');
        }
      } catch (err) {
        console.error('document.execCommand执行错误:', err);
        this.$message.error(`复制失败: ${err.message}`);
      } finally {
        // 清理临时元素
        document.body.removeChild(textArea);
        console.log('复制操作完成，临时元素已清理');
      }
    },
    
    /**
     * 复制原始数据
     */
    copyRawData() {
      this.copyToClipboard(this.formatDetailData());
    },
    
    /**
     * 处理执行测试按钮点击
     */
    handleExecuteTest() {
      if (!this.apiTreeData || !this.apiTreeData.detail) {
        this.$message.warning('请先选择一个测试用例');
        return;
      }
      // 显示执行测试对话框
      this.executeTestDialogVisible = true;
    },
    
    /**
     * 处理测试执行结果
     */
    handleTestExecuted(testResult) {
      // 发送测试执行结果给父组件
      this.$emit('test-executed', testResult);
    },
    
    /**
     * 处理日志导入按钮点击
     */
    handleLogImport() {
      // 显示日志导入对话框
      this.logImportDialogVisible = true;
    },
    
    /**
     * 处理日志导入结果
     */
    handleLogImported(logResult) {
      // 发送日志导入结果给父组件
      this.$emit('log-imported', logResult);
    },
    
    /**
     * 获取API的URL
     * @returns {string} API的URL地址
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
     * 初始化主题
     */
    initTheme() {
      // 从localStorage获取主题设置
      const savedTheme = localStorage.getItem('theme');
      this.isDarkMode = savedTheme === 'dark' || 
        (!savedTheme && window.matchMedia('(prefers-color-scheme: dark)').matches);
      // 应用主题到文档
      this.applyThemeToDocument();
    },
    
    /**
     * 将主题应用到文档元素
     */
    applyThemeToDocument() {
      // 将主题应用到文档元素
      document.documentElement.classList.toggle('dark-theme', this.isDarkMode);
      document.body.classList.toggle('dark-theme', this.isDarkMode);
    },
    
    /**
     * 处理主题变化
     * @param {StorageEvent} event - 存储事件对象
     */
    handleThemeChange(event) {
      // 当localStorage中的theme发生变化时触发
      if (event.key === 'theme') {
        this.isDarkMode = event.newValue === 'dark';
        // 应用主题到文档
        this.applyThemeToDocument();
      }
    }
  }
}
</script>