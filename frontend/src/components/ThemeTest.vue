<template>
  <div>
    <h1>Theme Test</h1>
    <div class="theme-toggle">
      <button @click="toggleDark">Toggle Dark Mode</button>
      <button @click="toggleLight">Toggle Light Mode</button>
      <button @click="toggleAuto">Toggle Auto</button>
    </div>
    <div class="test-section">
      <el-card>
        <div slot="header">Card Header</div>
        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="date" label="Date" width="180"></el-table-column>
          <el-table-column prop="name" label="Name" width="180"></el-table-column>
          <el-table-column prop="address" label="Address"></el-table-column>
        </el-table>
      </el-card>
    </div>
    <div class="test-section">
      <el-button type="primary">Primary Button</el-button>
      <el-button type="success">Success Button</el-button>
      <el-button type="warning">Warning Button</el-button>
      <el-button type="danger">Danger Button</el-button>
    </div>
    <div class="test-section">
      <el-input placeholder="Please input"></el-input>
    </div>
  </div>
</template>

<script>
import '../assets/styles/components/_theme-test.scss';

export default {
  data() {
    return {
      tableData: [
        { date: '2023-05-16', name: 'John Doe', address: 'New York' },
        { date: '2023-05-17', name: 'Jane Doe', address: 'London' },
        { date: '2023-05-18', name: 'Bob Smith', address: 'Paris' }
      ]
    }
  },
  methods: {
    toggleDark() {
      document.documentElement.classList.add('dark');
    },
    toggleLight() {
      document.documentElement.classList.remove('dark');
      document.documentElement.classList.add('light');
    },
    toggleAuto() {
      document.documentElement.classList.remove('dark', 'light');
      // Auto mode will be handled by system preferences
    }
  }
}
</script>

<style scoped>
/* 组件样式已提取到外部文件 */
</style>