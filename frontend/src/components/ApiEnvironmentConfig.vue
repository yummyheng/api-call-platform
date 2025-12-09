<template>
  <div class="api-environment-config">
    <el-card class="modern-card">
      <template #header>
        <div class="card-header">
          <h3>环境配置管理</h3>
          <div class="card-header-actions">
            <el-button type="primary" @click="handleAddEnvironment">
              <i class="el-icon-plus"></i>新增环境
            </el-button>
          </div>
        </div>
      </template>

      <!-- 环境列表 -->
      <div class="environment-list-section">
        <el-table :data="environments" style="width: 100%" @row-click="handleRowClick">
          <el-table-column prop="id" label="ID" width="80"></el-table-column>
          <el-table-column prop="appId" label="应用ID" width="150"></el-table-column>
          <el-table-column prop="envName" label="环境名称" width="150"></el-table-column>
          <el-table-column prop="envDescription" label="环境描述" min-width="150"></el-table-column>
          <el-table-column prop="baseUrl" label="基础URL" width="200"></el-table-column>
          <el-table-column prop="isDefault" label="是否默认">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.isDefault" disabled></el-switch>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="创建时间" width="180" :formatter="formatDate"></el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleEditEnvironment(scope.row)">
                编辑
              </el-button>
              <el-button type="danger" size="small" @click="handleDeleteEnvironment(scope.row.id)">
                删除
              </el-button>
              <el-button v-if="!scope.row.isDefault" type="success" size="small" @click="handleSetDefault(scope.row)">
                设置默认
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 新增/编辑环境对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      :before-close="handleCloseDialog"
    >
      <el-form ref="environmentForm" :model="formData" :rules="formRules" label-width="120px">
        <el-form-item label="应用ID" prop="appId">
          <el-input v-model="formData.appId" placeholder="请输入应用ID"></el-input>
        </el-form-item>
        <el-form-item label="环境名称" prop="envName">
          <el-input v-model="formData.envName" placeholder="请输入环境名称"></el-input>
        </el-form-item>
        <el-form-item label="环境描述">
          <el-input v-model="formData.envDescription" placeholder="请输入环境描述"></el-input>
        </el-form-item>
        <el-form-item label="基础URL" prop="baseUrl">
          <el-input v-model="formData.baseUrl" placeholder="请输入基础URL"></el-input>
        </el-form-item>
        <el-form-item label="是否默认">
          <el-switch v-model="formData.isDefault"></el-switch>
        </el-form-item>
        <el-form-item label="请求头">
          <el-button type="primary" size="small" @click="handleAddHeader">添加头信息</el-button>
          <el-table :data="formData.headers" border style="margin-top: 10px">
            <el-table-column prop="key" label="Key" width="120"></el-table-column>
            <el-table-column prop="value" label="Value"></el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">
                <el-button type="danger" size="mini" @click="handleDeleteHeader(scope.$index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item label="请求参数">
          <el-button type="primary" size="small" @click="handleAddParam">添加参数</el-button>
          <el-table :data="formData.params" border style="margin-top: 10px">
            <el-table-column prop="key" label="Key" width="120"></el-table-column>
            <el-table-column prop="value" label="Value"></el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">
                <el-button type="danger" size="mini" @click="handleDeleteParam(scope.$index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleCloseDialog">取 消</el-button>
        <el-button type="primary" @click="handleSaveEnvironment">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { api } from '../api/api.js';
import '../assets/styles/components/_api-environment-config.scss';

export default {
  name: 'ApiEnvironmentConfig',
  data() {
    return {
      environments: [],
      dialogVisible: false,
      dialogTitle: '新增环境',
      formData: {
        id: '',
        appId: '',
        envName: '',
        envDescription: '',
        baseUrl: '',
        isDefault: false,
        headers: [],
        params: []
      },
      formRules: {
        appId: [
          { required: true, message: '请输入应用ID', trigger: 'blur' }
        ],
        envName: [
          { required: true, message: '请输入环境名称', trigger: 'blur' }
        ],
        baseUrl: [
          { required: true, message: '请输入基础URL', trigger: 'blur' },
          { type: 'url', message: '请输入有效的URL', trigger: 'blur' }
        ]
      }
    };
  },
  mounted() {
    this.fetchEnvironments();
  },
  methods: {
    // 获取环境列表
    async fetchEnvironments() {
      try {
        const environments = await api.getEnvironments();
        this.environments = environments;
      } catch (error) {
        console.error('获取环境列表失败:', error);
        this.$message.error('获取环境列表失败');
      }
    },
    
    // 格式化日期
    formatDate(row, column, cellValue) {
      if (!cellValue) return '';
      const date = new Date(cellValue);
      return date.toLocaleString();
    },
    
    // 新增环境
    handleAddEnvironment() {
      this.dialogTitle = '新增环境';
      this.formData = {
        id: '',
        appId: '',
        envName: '',
        envDescription: '',
        baseUrl: '',
        isDefault: false,
        headers: [],
        params: []
      };
      this.dialogVisible = true;
    },
    
    // 编辑环境
    handleEditEnvironment(row) {
      this.dialogTitle = '编辑环境';
      this.formData = {
        ...row,
        headers: row.headers || [],
        params: row.params || []
      };
      this.dialogVisible = true;
    },
    
    // 删除环境
    handleDeleteEnvironment(id) {
      this.$confirm('确定要删除这个环境吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await api.deleteEnvironment(id);
          this.$message.success('删除成功');
          this.fetchEnvironments();
        } catch (error) {
          console.error('删除环境失败:', error);
          this.$message.error('删除环境失败');
        }
      }).catch(() => {
        // 取消删除操作
      });
    },
    
    // 设置默认环境
    async handleSetDefault(row) {
      try {
        await api.setDefaultEnvironment(row.id, row.appId);
        this.$message.success('设置默认环境成功');
        this.fetchEnvironments();
      } catch (error) {
        console.error('设置默认环境失败:', error);
        this.$message.error('设置默认环境失败');
      }
    },
    
    // 关闭对话框
    handleCloseDialog() {
      this.dialogVisible = false;
      this.$refs.environmentForm?.resetFields();
    },
    
    // 保存环境
    async handleSaveEnvironment() {
      this.$refs.environmentForm.validate(async (valid) => {
        if (valid) {
          try {
            if (this.formData.id) {
              // 更新环境
              await api.updateEnvironment(this.formData.id, this.formData);
              this.$message.success('更新成功');
            } else {
              // 新增环境
              await api.createEnvironment(this.formData);
              this.$message.success('新增成功');
            }
            this.dialogVisible = false;
            this.fetchEnvironments();
          } catch (error) {
            console.error('保存环境失败:', error);
            this.$message.error('保存环境失败');
          }
        }
      });
    },
    
    // 添加头信息
    handleAddHeader() {
      this.formData.headers.push({ key: '', value: '' });
    },
    
    // 删除头信息
    handleDeleteHeader(index) {
      this.formData.headers.splice(index, 1);
    },
    
    // 添加参数
    handleAddParam() {
      this.formData.params.push({ key: '', value: '' });
    },
    
    // 删除参数
    handleDeleteParam(index) {
      this.formData.params.splice(index, 1);
    },
    
    // 行点击事件
    handleRowClick(row) {
      console.log('行点击:', row);
    }
  }
};
</script>

<style scoped>
/* 组件样式已提取到外部文件 */
</style>