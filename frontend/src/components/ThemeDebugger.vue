<template>
  <div class="theme-switcher">
    <button 
      class="theme-toggle-btn"
      @click="toggleTheme"
      :class="{ active: isDarkMode }"
    >
      {{ isDarkMode ? '🌞 切换到白天模式' : '🌙 切换到黑夜模式' }}
    </button>
  </div>
</template>

<script>
import '../assets/styles/components/_theme-debugger.scss';

export default {
  name: 'ThemeSwitcher',
  computed: {
    isDarkMode() {
      // 获取当前主题模式
      return document.documentElement.classList.contains('dark-theme');
    }
  },
  methods: {
    toggleTheme() {
      const newTheme = !this.isDarkMode;
      
      // 同时应用到html和body
      document.documentElement.classList.toggle('dark-theme', newTheme);
      document.body.classList.toggle('dark-theme', newTheme);
      
      // 保存到本地存储
      localStorage.setItem('theme', newTheme ? 'dark' : 'light');
    }
  }
};
</script>

<style scoped>
/* 组件样式已提取到外部文件 */
</style>