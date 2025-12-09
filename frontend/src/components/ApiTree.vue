<template>
  <div class="api-tree-container">
    <el-tree
      :data="processedApiTree"
      :props="defaultProps"
      node-key="id"
      :expand-on-click-node="false"
      :default-expanded-keys="defaultExpandedKeys"
      @node-click="handleNodeClick"
      class="api-tree"
      :highlight-current="true"
      :indent="20"
    >
      <template #default="{ node, data }">
        <div class="tree-node-content">
          <span v-if="data.type === 'request' && data.method && data.method.trim()" class="method-text">{{ data.method }}</span>
          <span class="node-label">{{ data.label }}</span>
        </div>
      </template>
    </el-tree>
  </div>
</template>

<script>
// 导入组件样式
import '../assets/styles/components/_api-tree.scss';

export default {
  name: 'ApiTree',
  props: {
    apiTree: {
      type: Array,
      default: () => []
    }
  },
  computed: {
    // 元素 Plus树组件的默认属性配置
    defaultProps() {
      return {
        children: 'children',
        label: 'label',
        isLeaf: 'isLeaf' // 确保使用节点的isLeaf属性来判断是否为叶子节点
      };
    },
    
    // 默认展开的节点键值
    defaultExpandedKeys() {
      const collectNodeIds = (nodes, ids = []) => {
        if (!nodes || !Array.isArray(nodes)) return ids;
        
        nodes.forEach(node => {
          if (node && node.id) {
            ids.push(node.id);
            if (node.children && node.children.length > 0) {
              collectNodeIds(node.children, ids);
            }
          }
        });
        
        return ids;
      };
      
      return collectNodeIds(this.apiTree);
    },
    
    // 处理树形数据
    processedApiTree() {
      if (!this.apiTree || (Array.isArray(this.apiTree) && this.apiTree.length === 0)) {
        return [];
      }
      
      const processNode = (node) => {
          if (!node) return null;
          
          const newNode = { ...node };
          
          // 映射HTTP方法字段
          if (newNode.type === 'request' && newNode.httpMethod) {
            newNode.method = newNode.httpMethod;
          }
          
          // 确保所有节点都有children属性
          if (!newNode.children) {
            newNode.children = [];
          }
        
        // 递归处理子节点
        if (Array.isArray(newNode.children) && newNode.children.length > 0) {
          newNode.children = newNode.children.map(processNode).filter(child => child !== null);
        }
        
        // 根据节点类型和子节点存在情况设置叶子节点属性
      // 1. 测试用例节点始终是叶子节点，无论是否有children数组
      if (newNode.type === 'test_case') {
        newNode.isLeaf = true;
        // 清空可能存在的children数组，确保不显示展开图标
        newNode.children = [];
      } 
      // 2. 对于request和project节点，只有当有实际子节点时才显示展开图标
      else {
        newNode.isLeaf = !(newNode.children && Array.isArray(newNode.children) && newNode.children.length > 0);
      }
        
        // 处理测试用例节点标签
        if (newNode.type === 'test_case') {
          const nameWithoutUrl = newNode.label.split(/\s+http:/)[0];
          newNode.label = nameWithoutUrl;
        }
        
        return newNode;
      };
      
      // 处理数组或单个对象
      if (Array.isArray(this.apiTree)) {
        return this.apiTree.map(processNode).filter(node => node !== null);
      } else {
        const result = processNode(this.apiTree);
        return result ? [result] : [];
      }
    }
  },
  methods: {
    // 处理节点点击事件
    handleNodeClick(data) {
      if (!data) return;
      
      // 标准化节点数据
      const nodeData = {
        ...data,
        ...(data.type === 'request' && !data.testCase ? {
          testCase: {
            testData: ''
          }
        } : {})
      };
      
      this.$emit('node-click', nodeData);
    }
  }
};
</script>

<style scoped>
/* 组件样式已提取到外部文件 */
</style>