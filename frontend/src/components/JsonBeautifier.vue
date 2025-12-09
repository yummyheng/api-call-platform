<template>
  <div class="json-beautifier">
    <div class="json-beautifier-header">
      <h4>JSON 美化工具</h4>
      <div class="json-actions">
        <button @click="beautifyJson" class="btn btn-primary">
          <Icon name="format" :size="16" />
          格式化 JSON
        </button>
        <button @click="copyJson" class="btn btn-secondary">
          <Icon name="copy" :size="16" />
          复制结果
        </button>
      </div>
    </div>
    <div class="json-beautifier-content">
      <div class="json-input-section">
        <label for="json-input">输入 JSON:</label>
        <textarea
          id="json-input"
          v-model="inputJson"
          placeholder="请输入需要美化的 JSON 字符串..."
          rows="6"
        ></textarea>
      </div>
      <div class="json-output-section">
        <label>美化结果:</label>
        <pre v-if="beautifiedJson" class="json-output">{{ beautifiedJson }}</pre>
        <div v-else class="json-output-empty">美化后的 JSON 将显示在这里</div>
      </div>
    </div>
    <div v-if="errorMessage" class="json-error">{{ errorMessage }}</div>
  </div>
</template>

<script>
import '../assets/styles/components/_json-beautifier.scss';
import Icon from './Icon.vue';

export default {
  name: 'JsonBeautifier',
  components: {
    Icon
  },
  data() {
    return {
      inputJson: '',
      beautifiedJson: '',
      errorMessage: ''
    };
  },
  methods: {
    beautifyJson() {
      // 重置错误信息
      this.errorMessage = '';
      
      if (!this.inputJson.trim()) {
        this.errorMessage = '请输入 JSON 文本';
        return;
      }
      
      try {
        // 解析 JSON 字符串
        const parsedJson = JSON.parse(this.inputJson);
        // 美化 JSON
        this.beautifiedJson = JSON.stringify(parsedJson, null, 2);
      } catch (error) {
        this.errorMessage = `JSON 解析错误: ${error.message}`;
        this.beautifiedJson = '';
      }
    },
    copyJson() {
      if (!this.beautifiedJson) {
        this.errorMessage = '没有可复制的内容';
        return;
      }
      
      navigator.clipboard.writeText(this.beautifiedJson)
        .then(() => {
          // 简单提示，可以替换为更好的提示方式
          this.errorMessage = '已复制到剪贴板';
          setTimeout(() => {
            this.errorMessage = '';
          }, 2000);
        })
        .catch(err => {
          this.errorMessage = `复制失败: ${err.message}`;
        });
    }
  }
};
</script>

<style scoped>
/* 组件样式已提取到外部文件 */
</style>