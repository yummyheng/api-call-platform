<template>
  <div class="test-results-container">
    <el-card style="margin-bottom: 20px;" :border="false">
      <div slot="header" class="clearfix">
        <span>执行结果</span>
        <el-button @click="clearResults" type="danger" size="small" style="float:right;">
          清空记录
        </el-button>
      </div>
      
      <div v-if="testResults && testResults.length > 0">
        <el-table
          :data="testResults"
          style="width: 100%"
          stripe
          @row-click="handleExecutionResultClick"
          :row-class-name="tableRowClassName"
        >
          <el-table-column prop="testCaseId" label="Test Case ID" width="120"></el-table-column>
          <el-table-column prop="timestamp" label="执行时间" width="200">
            <template v-slot="{ row }">
              {{ formatDate(row.timestamp) }}
            </template>
          </el-table-column>
          <el-table-column prop="success" label="状态" width="100">
            <template v-slot="{ row }">
              <el-tag :type="row.success ? 'success' : 'danger'">
                {{ row.success ? '成功' : '失败' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="responseBody" label="Response Body">
            <template v-slot="{ row }">
              <div class="response-body-container">
                {{ formatJson(row.responseBody) }}
              </div>
            </template>
          </el-table-column>
        </el-table>
        

      </div>
      
      <div v-else class="empty-results">
        <el-empty description="暂无测试结果" :image-size="120"></el-empty>
        <p class="empty-tip">请先执行测试用例</p>
      </div>
    </el-card>
  </div>
</template>

<script>
// 导入组件样式
import '../assets/styles/components/_test-results.scss';

export default {
  name: 'TestResults',
  props: {
    testResults: {
      type: Array,
      default: () => []
    },
    selectedExecutionResult: {
      type: Object,
      default: null
    }
  },
  methods: {
    handleExecutionResultClick(row) {
      console.log('Selected execution result:', row);
      console.log('HTTP Client Call Detail:', row.httpClientCallDetail);
      this.$emit('result-click', row);
    },
    formatJson(jsonData) {
      if (!jsonData) return '{}';
      try {
        const parsed = typeof jsonData === 'string' ? JSON.parse(jsonData) : jsonData;
        return JSON.stringify(parsed, null, 2);
      } catch (e) {
        return jsonData.toString();
      }
    },
    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleString();
    },
    
    // 根据HTTP方法获取标签类型
    getMethodType(method) {
      const methodMap = {
        'GET': 'success',
        'POST': 'primary',
        'PUT': 'warning',
        'DELETE': 'danger'
      };
      return methodMap[method] || 'info';
    },
    
    // 清空测试结果
    clearResults() {
      this.$confirm('确定要清空所有测试结果吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$emit('clear-results');
        this.$message.success('测试结果已清空');
      }).catch(() => {
        // 用户取消操作
      });
    },
    
    // 为表格行添加样式
    tableRowClassName({row}) {
      if (this.selectedExecutionResult && this.selectedExecutionResult.testCaseId === row.testCaseId) {
        return 'selected-row';
      }
      return '';
    }
  }
}
</script>

<style scoped>
/* 组件样式已提取到外部文件 */
</style>