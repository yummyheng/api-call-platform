<template>
  <div>
    <!-- 日志导入对话框 -->
    <el-dialog
      title="日志导入"
      :visible.sync="isVisible"
      width="80%"
      center
      class="api-log-importer-dialog"
      @open="init"
    >
      <el-form :model="formData" label-width="80px">
        <!-- 环境选择 -->
        <el-form-item label="环境选择">
          <el-select v-model="formData.environmentName" placeholder="请选择环境">
            <el-option
              v-for="env in environments"
              :key="env.id"
              :label="env.envName"
              :value="env.envName"
            ></el-option>
          </el-select>
        </el-form-item>

        <!-- 项目选择 -->
        <el-form-item label="Project选择">
          <el-select v-model="formData.projectName" placeholder="请选择Project">
            <el-option
              v-for="project in projects"
              :key="project.id"
              :label="project.projectName"
              :value="project.projectName"
            ></el-option>
          </el-select>
        </el-form-item>

        <!-- 日志文件上传 -->
        <el-form-item label="日志文件">
          <div style="margin-bottom: 10px;">
            <el-upload
              class="upload-demo"
              ref="upload"
              :action="uploadUrl"
              :headers="uploadHeaders"
              :data="formData"
              :auto-upload="false"
              :file-list="fileList"
              :before-upload="beforeUpload"
              :on-change="handleFileChange"
              :on-success="onUploadSuccess"
              :on-error="onUploadError"
              accept=".log"
            >
              <el-button size="small" type="primary" @click.stop="triggerFileInput">选择日志文件</el-button>
              <div slot="tip" class="el-upload__tip">只能上传.log文件</div>
            </el-upload>
          </div>
          <!-- 文件路径输入框 -->
          <div>
            <el-input
              v-model="formData.filePath"
              placeholder="或者直接输入.log文件路径"
              clearable
            >
              <el-button
                slot="append"
                type="primary"
                @click.stop="importByFilePath"
              >
                按路径导入
              </el-button>
            </el-input>
          </div>
        </el-form-item>
      </el-form>

      <!-- 底部操作按钮 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="isVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImport">导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
/**
 * API日志导入组件
 * 提供两种导入方式：文件上传和文件路径导入
 */
import { api } from '../api/api.js';
export default {
  name: 'ApiLogImporter',
  components: {},
  props: {
    /**
     * 控制对话框显示/隐藏的外部属性
     */
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      /**
       * 本地对话框可见状态
       */
      isVisible: this.dialogVisible,
      /**
       * 表单数据
       */
      formData: {
        environmentName: '', // 环境名称
        projectName: '',     // 项目名称
        filePath: '',        // 日志文件路径
        environmentId: '',   // 环境ID
        projectId: ''        // 项目ID
      },
      /**
       * 环境列表
       */
      environments: [],
      /**
       * 项目列表
       */
      projects: [],
      /**
       * 已选择的文件列表
       */
      fileList: [],
      /**
       * 文件上传URL
       */
      uploadUrl: '/api/logs/import',
      /**
       * 文件上传请求头
       */
      uploadHeaders: {}
    };
  },
  
  /**
   * 组件挂载后执行
   */
  mounted() {
    console.log('ApiLogImporter组件挂载完成', this.dialogVisible);
    if (this.dialogVisible) {
      this.init();
    }
  },
  
  watch: {
    /**
     * 监听dialogVisible属性变化
     * @param {boolean} newVal - 新的可见状态
     */
    dialogVisible: function(newVal) {
      console.log('dialogVisible变化:', newVal);
      this.isVisible = newVal;
      if (newVal) {
        this.init(); // 对话框打开时初始化数据
      } else {
        this.resetForm(); // 对话框关闭时重置表单
      }
    }
  },
  methods: {
      /**
       * 处理文件选择变化
       * @param {Object} file - 当前操作的文件对象
       * @param {Array} fileList - 更新后的文件列表
       */
      handleFileChange(file, fileList) {
        this.fileList = fileList;
      },

      /**
       * 触发文件选择对话框
       * 通过操作Element UI上传组件的原生文件输入框实现
       */
      triggerFileInput() {
        // 只使用Element UI上传组件自带的文件选择功能
        const uploadComponent = this.$refs.upload;
        if (uploadComponent) {
          const uploadElement = uploadComponent.$el;
          const fileInput = uploadElement.querySelector('input[type="file"]');
          
          if (fileInput) {
            try {
              fileInput.click();
            } catch (error) {
              this.$message.error('触发文件选择失败');
            }
          } else {
            this.$message.error('未找到文件选择元素');
          }
        }
      },
      
      /**
       * 通过文件路径导入日志文件
       * 向服务器发送文件路径，由服务器直接读取并导入日志
       */
      importByFilePath() {
        // 验证表单数据
        const validation = this.validateForm();
        if (!validation.isValid) {
          this.$message.error(validation.message);
          return;
        }

        const { selectedEnv, selectedProject } = validation;
        
        // 验证文件路径是否以.log结尾
        if (!this.formData.filePath.endsWith('.log')) {
          this.$message.error('文件路径必须指向.log文件');
          return;
        }
        
        // 准备请求参数
        const importParams = {
          environmentId: selectedEnv.id,
          projectId: selectedProject.id,
          filePath: this.formData.filePath
        };
        
        // 发送日志导入请求
        api.importLogsByPath(importParams)
          .then(response => {
            if (response.data.success === true) {
              this.$message.success('日志导入成功');
              this.isVisible = false;
              this.$emit('log-imported', response.data);
            } else {
              this.$message.error('日志导入失败: ' + (response.data.message || '未知错误'));
            }
          })
          .catch(error => {
            console.error('日志导入请求失败:', error);
            this.$message.error('日志导入失败: ' + (error.response?.data?.message || error.message || '网络错误'));
          });
      },

      /**
       * 初始化组件数据
       * 对话框打开时调用，获取环境和项目列表
       */
      init() {
        console.log('ApiLogImporter初始化');
        console.log('调用getEnvironments()前');
        this.getEnvironments();
        console.log('调用getEnvironments()后');
        console.log('调用getProjects()前');
        this.getProjects();
        console.log('调用getProjects()后');
      },

      /**
       * 获取环境列表
       * 从后端API获取所有可用环境数据
       */
      getEnvironments() {
        console.log('开始获取环境列表');
        api.getEnvironments()
          .then(response => {
            console.log('环境列表获取成功:', JSON.stringify(response));
            console.log('环境列表数据类型:', typeof response);
            // 确保获取的是数组数据
            if (response && Array.isArray(response)) {
              console.log('环境列表长度:', response.length);
              if (response.length > 0) {
                console.log('第一个环境数据:', JSON.stringify(response[0]));
              }
              // 使用JSON.parse(JSON.stringify())确保数据是纯数组格式
              this.environments = JSON.parse(JSON.stringify(response));
            } else if (response && response.data && Array.isArray(response.data)) {
              console.log('环境列表长度:', response.data.length);
              if (response.data.length > 0) {
                console.log('第一个环境数据:', JSON.stringify(response.data[0]));
              }
              // 使用JSON.parse(JSON.stringify())确保数据是纯数组格式
              this.environments = JSON.parse(JSON.stringify(response.data));
            } else {
              console.error('环境列表数据格式不正确:', response);
              this.environments = [];
            }
            console.log('环境列表最终数据类型:', Array.isArray(this.environments));
          })
          .catch(error => {
            this.$message.error('获取环境列表失败');
            console.error('获取环境列表失败:', error);
          });
      },

      /**
       * 获取项目列表
       * 从后端API获取所有可用项目数据
       */
      getProjects() {
        console.log('开始获取项目列表');
        api.getProjects()
          .then(response => {
            console.log('项目列表获取成功:', JSON.stringify(response));
            console.log('项目列表数据类型:', typeof response);
            // 确保获取的是数组数据
            if (response && Array.isArray(response)) {
              console.log('项目列表长度:', response.length);
              if (response.length > 0) {
                console.log('第一个项目数据:', JSON.stringify(response[0]));
              }
              // 使用JSON.parse(JSON.stringify())确保数据是纯数组格式
              this.projects = JSON.parse(JSON.stringify(response));
            } else if (response && response.data && Array.isArray(response.data)) {
              console.log('项目列表长度:', response.data.length);
              if (response.data.length > 0) {
                console.log('第一个项目数据:', JSON.stringify(response.data[0]));
              }
              // 使用JSON.parse(JSON.stringify())确保数据是纯数组格式
              this.projects = JSON.parse(JSON.stringify(response.data));
            } else {
              console.error('项目列表数据格式不正确:', response);
              this.projects = [];
            }
            console.log('项目列表最终数据类型:', Array.isArray(this.projects));
          })
          .catch(error => {
            this.$message.error('获取项目列表失败');
            console.error('获取项目列表失败:', error);
          });
      },

      /**
       * 文件上传前的验证
       * @param {Object} file - 待上传的文件对象
       * @returns {boolean} 是否允许上传该文件
       */
      beforeUpload(file) {
        const isLog = file.name.endsWith('.log');
        if (!isLog) {
          this.$message.error('只能上传.log文件!');
          return false;
        }
        return true;
      },

      /**
       * 验证表单数据完整性
       * @returns {Object} 验证结果对象
       * @returns {boolean} return.isValid - 是否验证通过
       * @returns {string} return.message - 验证失败时的错误信息
       * @returns {Object} return.selectedEnv - 选中的环境对象（验证通过时）
       * @returns {Object} return.selectedProject - 选中的项目对象（验证通过时）
       */
      validateForm() {
        const selectedEnv = this.environments.find(env => env.envName === this.formData.environmentName);
        const selectedProject = this.projects.find(proj => proj.projectName === this.formData.projectName);
        
        if (!selectedEnv) {
          return { isValid: false, message: '请选择环境' };
        }
        if (!selectedProject) {
          return { isValid: false, message: '请选择Project' };
        }
        if (this.fileList.length === 0 && !this.formData.filePath) {
          return { isValid: false, message: '请选择日志文件或输入文件路径' };
        }
        
        return { isValid: true, selectedEnv, selectedProject };
      },

      /**
       * 处理导入按钮点击事件
       * 根据用户选择的导入方式（文件上传或路径导入）执行相应操作
       */
      handleImport() {
        // 验证表单数据
        const validation = this.validateForm();
        if (!validation.isValid) {
          this.$message.error(validation.message);
          return;
        }

        const { selectedEnv, selectedProject } = validation;
        
        // 如果同时选择了文件和输入了路径，优先使用文件上传方式
        if (this.fileList.length > 0 && this.formData.filePath) {
          this.$message.warning('同时选择了文件和输入了路径，将优先使用文件上传方式');
        }

        // 根据是否有文件路径选择导入方式
        if (this.formData.filePath && this.fileList.length === 0) {
          // 使用文件路径导入
          this.importByFilePath();
        } else {
          // 使用传统文件上传
          // 更新上传组件的额外参数
          this.formData.environmentId = selectedEnv.id;
          this.formData.projectId = selectedProject.id;
          
          // 执行文件上传
          this.$refs.upload.submit();
        }
      },

      /**
       * 文件上传成功处理
       * @param {Object} response - 服务器返回的上传结果
       * @param {Object} file - 上传成功的文件对象
       * @param {Array} fileList - 更新后的文件列表
       */
      onUploadSuccess(response, file, fileList) {
        if (response.success === true) {
          this.$message.success('日志导入成功');
          this.isVisible = false;
          this.$emit('log-imported', response);
        } else {
          this.$message.error('日志导入失败: ' + (response.message || '未知错误'));
        }
      },

      /**
       * 文件上传失败处理
       * @param {Object} error - 上传失败的错误信息
       * @param {Object} file - 上传失败的文件对象
       * @param {Array} fileList - 更新后的文件列表
       */
      onUploadError(error, file, fileList) {
        console.error('文件上传失败:', error);
        this.$message.error('日志导入失败: ' + error.message);
      },

      /**
       * 重置表单数据
       * 清除所有输入值和已选择的文件
       */
      resetForm() {
        this.formData = {
          environmentName: '',
          projectName: '',
          filePath: '',
          environmentId: '',
          projectId: ''
        };
        this.fileList = [];
      }
    }
};
</script>

<style scoped>
.dialog-content {
  max-height: 60vh;
  overflow-y: auto;
}

.dialog-footer {
  text-align: right;
}
</style>