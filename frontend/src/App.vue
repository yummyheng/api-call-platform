<template>
  <div class="app-container" :class="{ 'dark-theme': isDarkMode }">
    <div class="header">
      <div class="header-title">
        <h1>API调用平台</h1>
      </div>
      <div class="header-actions">
        <button
          @click="refreshData"
          class="action-btn"
          title="刷新数据"
        >
          <Icon name="refresh" />
        </button>
        <button
          @click="toggleTheme"
          class="action-btn"
          :title="isDarkMode ? '切换到浅色模式' : '切换到深色模式'"
        >
          <Icon :name="isDarkMode ? 'moon' : 'sun'" />
        </button>
      </div>
    </div>
    
    <div class="main-content">

      
      <!-- 左侧API树面板 -->
      <div class="sidebar">
        <ApiTree 
          :api-tree="apiTree" 
          :default-expanded-keys="expandedKeys"
          @node-click="handleNodeClick"
        />
      </div>
      
      <!-- 中间内容区域 -->
      <div class="content">
        <!-- API详情和测试功能 -->
        <ApiTreeData 
          v-if="selectedApiData"
          :api-tree-data="selectedApiData" 
          :selectedTestCaseId="selectedTestCaseId"
          :testCases="testCases"
          :test-results="testResults"
          @test-case-click="handleTestCaseClick"
          @test-executed="handleTestExecuted"
          @save-test-data="handleSaveTestData"
          @result-click="handleResultClick"
          @clear-results="handleClearResults"
        />
        <div v-else class="empty-state">
          <p>请从左侧选择一个API</p>
        </div>
        
        <!-- 测试结果区域 -->
        <div class="test-results-section" v-if="selectedApiData">
          <TestResults 
            :test-results="executionResults"
            :selected-execution-result="selectedExecutionResult"
            @result-click="handleResultClick"
            @clear-results="handleClearResults"
          />
        </div>
      </div>
      
      <!-- 右侧工具面板 -->
      <div 
        class="tools-panel-container"
        :class="{ 'collapsed': isToolsPanelCollapsed }"
        :style="isToolsPanelCollapsed ? { width: '36px' } : { width: toolsPanelWidth + 'px' }"
      >
        <!-- 折叠按钮 -->
        <button 
          class="collapse-btn"
          @click="toggleToolsPanel"
          :title="isToolsPanelCollapsed ? '展开工具面板' : '折叠工具面板'"
        >
          <Icon name="arrow" :size="16" />
        </button>
        
        <!-- 可拖动的工具面板 -->
        <div 
          class="tools-panel"
          v-show="!isToolsPanelCollapsed"
        >
          <!-- 拖动手柄 -->
          <div class="resize-handle" @mousedown="startResizing"></div>
          
          <div class="tools-header">
            <h3>工具</h3>
            <div class="tools-tabs">
              <el-tabs v-model="activeToolTab" type="card">
                <el-tab-pane label="JSON美化" name="0">
                  <JsonBeautifier />
                </el-tab-pane>
                <el-tab-pane label="环境配置" name="1">
                  <ApiEnvironmentConfig />
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// 导入组件
import ApiTree from './components/ApiTree.vue';
import ApiTreeData from './components/ApiTreeData.vue';
import TestResults from './components/TestResults.vue';
import JsonBeautifier from './components/JsonBeautifier.vue';
import ApiEnvironmentConfig from './components/ApiEnvironmentConfig.vue';
import Icon from './components/Icon.vue';
import { api } from './api/api.js';
// 导入统一的基础样式文件
import './assets/base.scss';

export default {
  name: 'App',
  components: {
    ApiTree,
    ApiTreeData,
    TestResults,
    JsonBeautifier,
    ApiEnvironmentConfig,
    Icon
  },
  data() {
    return {
      // API树数据
      apiTree: [],
      expandedKeys: ['allProjects'],
      
      // API和测试数据
      selectedApiData: null,
      selectedTestCaseId: '',
      testCases: [],
      testResults: [],
      
      // 执行结果
      executionResults: [],
      selectedExecutionResult: null,
      isDarkMode: false,
      activeToolTab: '0', // 0: JSON美化, 1: 环境配置
      isToolsPanelCollapsed: true,
      toolsPanelWidth: 350
    };
  },
  
  async created() {
    // 初始化加载数据
    await this.loadInitialData();
  },
  
  methods: {
    // 初始化加载数据
    async loadInitialData() {
      try {
        // 只加载API树数据，不再单独加载测试用例数据
        await this.fetchApiTree();
        // 初始化主题设置
        this.initTheme();
      } catch (error) {
        console.error('Failed to load initial data:', error);
        try {
          this.$message.error('Failed to load application data');
        } catch (e) {
          console.log('Failed to load application data');
        }
      }
    },
    
    initTheme() {
      // 检查系统主题设置
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      // 检查本地存储中的主题设置
      const savedTheme = localStorage.getItem('theme');
      
      if (savedTheme) {
        this.isDarkMode = savedTheme === 'dark';
      } else {
        this.isDarkMode = prefersDark;
      }
      
      // 应用主题
      this.applyTheme();
    },
    
    toggleTheme() {
      this.isDarkMode = !this.isDarkMode;
      // 保存主题设置
      localStorage.setItem('theme', this.isDarkMode ? 'dark' : 'light');
      this.applyTheme();
    },
    
    applyTheme() {
      // 确保主题同时应用到html和body元素
      document.documentElement.classList.toggle('dark-theme', this.isDarkMode);
      document.body.classList.toggle('dark-theme', this.isDarkMode);
      
      // 打印不同区域的样式信息用于调试
      console.log('=== 主题切换调试日志 ===');
      console.log('当前主题模式:', this.isDarkMode ? '深色模式' : '浅色模式');
      
      // 打印不同区域的样式信息用于调试
      console.log('=== 主题切换调试日志 ===');
      console.log('当前主题模式:', this.isDarkMode ? '深色模式' : '浅色模式');
      
      // 打印主要区域的计算样式
      const getComputedStyleForSelector = (selector) => {
        const element = document.querySelector(selector);
        if (element) {
          const style = window.getComputedStyle(element);
          return {
            selector,
            backgroundColor: style.backgroundColor,
            color: style.color,
            borderColor: style.borderColor || 'none',
            fontFamily: style.fontFamily,
            fontSize: style.fontSize
          };
        }
        return { selector, error: '元素未找到' };
      };
      
      // 打印各个主要UI区域的样式
      const regions = [
        '.app-container',
        '.header',
        '.main-content',
        '.sidebar',
        '.api-tree-container',
        '.content-container',
        '.tools-panel-container',
        '.tools-panel-header',
        '.api-tree-data-container',
        '.test-case-info',
        '.api-info',
        '.test-data-preview',
        '.detail-data-preview',
        '.test-case-section.info-card',
        '.btn-primary',
        '.btn-secondary'
      ];
      
      const styles = regions.map(getComputedStyleForSelector);
      console.log('各区域样式信息:', styles);
      
      // 特别打印CSS变量值
      console.log('\n当前CSS变量值:');
      const cssVars = {
        bgPrimary: getComputedStyle(document.documentElement).getPropertyValue('--bg-primary').trim(),
        bgSecondary: getComputedStyle(document.documentElement).getPropertyValue('--bg-secondary').trim(),
        textPrimary: getComputedStyle(document.documentElement).getPropertyValue('--text-primary').trim(),
        textSecondary: getComputedStyle(document.documentElement).getPropertyValue('--text-secondary').trim(),
        borderColor: getComputedStyle(document.documentElement).getPropertyValue('--border-color').trim()
      };
      console.log(cssVars);
      console.log('========================');
    },
    
    toggleToolsPanel() {
      this.isToolsPanelCollapsed = !this.isToolsPanelCollapsed;
    },
    
    startResizing(event) {
      // 阻止默认事件，防止文本选择等
      event.preventDefault();
      
      const startX = event.clientX;
      const startWidth = this.toolsPanelWidth;
      
      // 添加mousemove和mouseup事件监听器
      const handleMouseMove = (e) => {
        // 计算新宽度
        const delta = startX - e.clientX;
        const newWidth = Math.max(200, Math.min(600, startWidth - delta)); // 设置最小和最大宽度
        
        // 更新宽度
        this.toolsPanelWidth = newWidth;
      };
      
      const handleMouseUp = () => {
        // 移除事件监听器
        document.removeEventListener('mousemove', handleMouseMove);
        document.removeEventListener('mouseup', handleMouseUp);
      };
      
      // 添加事件监听器
      document.addEventListener('mousemove', handleMouseMove);
      document.addEventListener('mouseup', handleMouseUp);
    },
    
    // 刷新数据
    async refreshData() {
      try {
        await this.loadInitialData();
        try {
          this.$message.success('Data refreshed successfully');
        } catch (e) {
          console.log('Data refreshed successfully');
        }
      } catch (error) {
        console.error('Failed to refresh data:', error);
        try {
          this.$message.error('Failed to refresh data');
        } catch (e) {
          console.error('Failed to refresh data');
        }
      }
    },
    
    // 获取API树 - 从真实数据库获取数据
  async fetchApiTree() {
    try {
      console.log('从后端API获取真实的项目和API数据');
      
      // 使用统一的API管理获取API树结构
      const treeData = await api.getApiTree();
      
      this.apiTree = treeData;
      
      console.log('获取的API树数据:', this.apiTree);
      
      // 从API树中提取测试用例信息
      this.extractTestCasesFromApiTree();
      
      this.setInitialExpandedKeys();
      
      // 自动选择第一个测试案例
      this.selectFirstTestCase();
    } catch (error) {
      console.error('获取API树数据失败:', error);
      // 只记录错误，不再使用模拟数据
      try {
        this.$message.error('获取API树数据失败，请检查后端服务');
      } catch (e) {
        console.log('获取API树数据失败，请检查后端服务');
      }
    }
  },
  
  // 从API树中提取测试用例信息
  extractTestCasesFromApiTree() {
    try {
      console.log('从API树中提取测试用例信息');
      this.testCases = [];
      
      // 递归遍历API树提取测试用例
      const extractTestCases = (nodes, parentPath = '', parentNode = null) => {
        if (!nodes || !Array.isArray(nodes)) return;
        
        nodes.forEach((node, index) => {
          const currentPath = parentPath ? `${parentPath}-${index}` : `${index}`;
          
          // 检查是否为测试用例节点
          if (node && node.type === 'test_case') {
            // 为测试用例节点添加parent属性，以便在需要时可以追溯到父节点
            const testCase = {
              id: `test_${node.id || currentPath}`,
              name: node.label,
              apiPath: parentPath,
              ...node,
              parent: parentNode
            };
            
            // 合并detail和detailVO数据，优先使用detailVO
            if (node.detail || node.detailVO) {
              // 初始化testCase.detailVO对象
              if (!testCase.detailVO) {
                testCase.detailVO = {};
              }
              
              // 如果detail存在，将其合并到testCase中
              if (node.detail) {
                testCase.detail = { ...node.detail };
                
                // 同时将detail中的数据复制到detailVO中，除非detailVO已经有相应字段
                Object.keys(node.detail).forEach(key => {
                  if (testCase.detailVO[key] === undefined) {
                    testCase.detailVO[key] = node.detail[key];
                  }
                });
              }
              
              // 确保测试数据和期望结果都被正确提取
              testCase.testData = testCase.detailVO?.testData || '{}';
              testCase.expectedResult = testCase.detailVO?.expectedResult || '';
              
              // 处理request和result对象
              if (testCase.detailVO.request) {
                // 确保request对象完整
                testCase.detailVO.request = { ...testCase.detailVO.request };
                
                // 如果request中包含testData，也将其用于testCase.testData
                if (testCase.detailVO.request.testData && !testCase.testData) {
                  testCase.testData = testCase.detailVO.request.testData;
                }
              }
              
              if (testCase.detailVO.result) {
                // 确保result对象完整
                testCase.detailVO.result = { ...testCase.detailVO.result };
              }
            }
            
            // 添加到测试用例列表
            this.testCases.push(testCase);
          }
          
          // 递归处理子节点
          if (node && node.children && node.children.length > 0) {
            extractTestCases(node.children, currentPath, node);
          }
        });
      };
      
      extractTestCases(this.apiTree);
      console.log('从API树中提取的测试用例数据:', this.testCases);
    } catch (error) {
      console.error('提取测试用例数据失败:', error);
    }
  },
  
  // 查找API树中的第一个测试用例节点
  findFirstTestCaseNode(node) {
    if (!node) return null;
    
    // 如果当前节点是测试用例，直接返回
    if (node.type === 'test_case') {
      return node;
    }
    
    // 递归查找子节点
    if (node.children && Array.isArray(node.children)) {
      for (const child of node.children) {
        const found = this.findFirstTestCaseNode(child);
        if (found) {
          return found;
        }
      }
    }
    
    return null;
  },
  
  // 自动选择第一个测试案例
  selectFirstTestCase() {
    console.log('尝试自动选择第一个测试案例');
    
    // 确保apiTree是有效的数组
    if (!this.apiTree || !Array.isArray(this.apiTree) || this.apiTree.length === 0) {
      console.log('API树数据为空或无效');
      return;
    }
    
    // 检查是否有测试案例节点
    let firstTestCaseNode = null;
    
    // 从根节点开始查找
    for (const rootNode of this.apiTree) {
      firstTestCaseNode = this.findFirstTestCaseNode(rootNode);
      if (firstTestCaseNode) {
        break;
      }
    }
    
    // 如果找到了测试案例节点，点击它
    if (firstTestCaseNode) {
      console.log('找到第一个测试案例:', firstTestCaseNode.label);
      this.handleNodeClick(firstTestCaseNode);
    } else {
      console.log('没有找到测试案例节点');
    }
  },
    

    
    // 处理节点点击
    handleNodeClick(nodeData) {
    console.log('App: Node clicked:', nodeData);
    
    // 记录选中的节点
    this.selectedNode = nodeData;
    
    // 创建用于显示的完整数据对象
    const displayData = { ...nodeData };
    
    // 确保detail和detailVO数据都被包含
    if (nodeData.detail) {
      displayData.detail = { ...nodeData.detail };
    }
    
    if (nodeData.detailVO) {
      displayData.detailVO = { ...nodeData.detailVO };
    }
    
    // 根据节点类型处理
    if (nodeData.type === 'project' || (nodeData.children && nodeData.children.length > 0)) {
      this.selectedApiData = null;
      this.selectedTestCaseId = '';
    } else if (nodeData.type === 'test_case') {
      // 如果是测试用例节点，设置selectedApiData和selectedTestCaseId
      this.selectedApiData = displayData;
      this.selectedTestCaseId = nodeData.id || `test_${Date.now()}`;
      
      // 设置方法和API名称信息，避免显示为N/A
      if (nodeData.parent) {
        this.selectedApiData.method = nodeData.parent.httpMethod || nodeData.parent.method || 'N/A';
        this.selectedApiData.apiName = nodeData.parent.label || 'N/A';
      }
      
      console.log('选中测试用例:', nodeData.label);
      console.log('测试用例数据完整性检查:');
      console.log('- 包含detail:', !!nodeData.detail);
      console.log('- 包含detailVO:', !!nodeData.detailVO);
      console.log('- 包含测试数据:', !!nodeData.testData);
    } else {
      // 为选中的API添加测试数据结构
      this.selectedApiData = {
        ...displayData,
        testCase: {
          testData: '{}'
        },
        // 确保method和apiName字段存在
        method: nodeData.httpMethod || nodeData.method || 'N/A',
        apiName: nodeData.label || 'N/A'
      };
      this.selectedTestCaseId = '';
    }
  },
    
    // 处理测试用例点击
    handleTestCaseClick(testCase) {
    console.log('App: Test case clicked:', testCase);
    this.selectedTestCaseId = testCase.id;
    
    // 设置选中的API数据并确保包含method和apiName
    if (testCase.parent) {
      this.selectedApiData = {
        ...testCase,
        method: testCase.parent.httpMethod || testCase.parent.method || 'N/A',
        apiName: testCase.parent.label || 'N/A'
      };
    }
    
    // 触发相应的事件
    this.$emit('test-case-click', testCase);
  },
  
  // 处理测试结果点击事件
  handleResultClick(result) {
    if (!result) return;
    
    console.log('点击测试结果:', result.id);
    this.selectedExecutionResult = result;
    this.$emit('result-click', result);
  },
  
  // 处理清空结果事件
  handleClearResults() {
    console.log('清空测试结果');
    this.executionResults = [];
    this.testResults = [];
    this.$emit('clear-results');
  },
    
    // 处理测试执行结果
    handleTestExecuted(result) {
      console.log('App: Test executed:', result);
      console.log('执行结果详细信息:', { 
        id: result.id,
        status: result.status,
        timestamp: result.timestamp,
        duration: result.duration,
        hasResponse: !!result.response,
        hasRequest: !!result.request,
        hasHttpClientCallDetail: !!result.httpClientCallDetail
      });
      
      // 将新的执行结果添加到列表开头
      this.executionResults.unshift(result);
      this.testResults.unshift(result);
      
      // 将新的执行结果设置为选中状态，以便显示详细信息
      this.selectedExecutionResult = result;
      
      // 限制列表长度
      if (this.executionResults.length > 50) {
        this.executionResults = this.executionResults.slice(0, 50);
      }
      if (this.testResults.length > 50) {
        this.testResults = this.testResults.slice(0, 50);
      }
      
      console.log('更新后的executionResults数量:', this.executionResults.length);
      console.log('更新后的testResults数量:', this.testResults.length);
    },
    
    // 处理保存测试数据
    handleSaveTestData(testData) {
      console.log('App: Saving test data:', testData);
      console.log('保存前selectedApiData:', this.selectedApiData);
      
      if (this.selectedApiData) {
        this.selectedApiData.testCase = {
          ...this.selectedApiData.testCase,
          testData: testData
        };
        console.log('保存后selectedApiData:', this.selectedApiData);
        console.log('保存后testCase:', this.selectedApiData.testCase);
      } else {
        console.warn('没有选中的API数据，无法保存测试数据');
      }
    },
    
    // 设置初始展开的节点
    setInitialExpandedKeys() {
      // 简单实现：展开第一个节点的所有层级
      if (this.apiTree && this.apiTree.length > 0) {
        this.expandedKeys = ['0', '0-0'];
        console.log('初始展开的节点:', this.expandedKeys);
      }
    },
    
    // 移除了模拟数据生成方法，现在完全依赖真实的后端API调用
    // 这样可以确保始终使用最新的真实数据，避免开发环境与生产环境的差异
  }
};
</script>

<style>
/* 全局样式已提取到 global.css */
</style>

<style scoped>
/* 组件特定样式已提取到外部文件 */
/* 如有需要可在此添加少量组件特定样式 */
.action-btn {
  background: none;
  border: none;
  padding: 8px;
  cursor: pointer;
  color: var(--theme-text-primary);
  border-radius: 4px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-btn:hover {
  background-color: var(--theme-bg-hover);
  color: var(--theme-text-primary);
}

.action-btn:active {
  background-color: var(--theme-bg-active);
}
</style>
