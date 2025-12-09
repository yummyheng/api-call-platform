import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue2 from '@vitejs/plugin-vue2'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue2()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    // 添加代理配置，解决CORS问题
    proxy: {
      '/api': {
        target: 'http://localhost:8082', // Derby后端
        changeOrigin: true
        // 不重写/api前缀，因为Derby后端已配置context-path=/api
      }
    }
  }
})
