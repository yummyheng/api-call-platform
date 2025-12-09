<template>
  <svg
    :class="iconClass"
    :width="size || 20"
    :height="size || 20"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    stroke-width="2"
    stroke-linecap="round"
    stroke-linejoin="round"
    xmlns="http://www.w3.org/2000/svg"
  >
    <!-- 刷新图标 -->
    <g v-if="name === 'refresh'">
      <path d="M12 3C14.2091 3 16 4.79086 16 7C16 7.47165 15.9689 7.9367 15.9094 8.39149L18.5 11L17.5 12L15 9.5C14.2848 10.839 12.9751 11.8 11.4727 12.1449V15H8.53333V12.1637C7.06585 11.7843 5.80486 10.8193 5 9.5L2.5 12L1.5 11L4.09064 8.39149C4.03109 7.9367 4 7.47165 4 7C4 4.79086 5.79086 3 8 3H12Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M12 21C9.79086 21 8 19.2091 8 17C8 16.5283 8.03109 16.0633 8.09064 15.6085L5.5 13L6.5 12L9 14.5C9.71523 13.161 11.0249 12.2 12.5273 11.8551V9H15.4667V11.8363C16.9341 12.2157 18.1951 13.1807 19 14.5L21.5 12L22.5 13L19.9094 15.6085C19.9689 16.0633 20 16.5283 20 17C20 19.2091 18.2091 21 16 21H12Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </g>
    
    <!-- 月亮图标 (深色模式) -->
    <g v-else-if="name === 'moon'">
      <path d="M10 16.5C13.5899 16.5 16.5 13.5899 16.5 10C16.5 6.41015 13.5899 3.5 10 3.5C6.41015 3.5 3.5 6.41015 3.5 10C3.5 13.5899 6.41015 16.5 10 16.5Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M18.5 18.5L17 17" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M3 17L1.5 18.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M17 3L18.5 1.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M3 3L1.5 1.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M18.5 10L17 10" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M1.5 10L3 10" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M10 1.5V3" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M10 17V18.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </g>
    
    <!-- 太阳图标 (浅色模式) -->
    <g v-else-if="name === 'sun'">
      <path d="M17.5 10C17.5 14.1421 14.1421 17.5 10 17.5C5.85786 17.5 2.5 14.1421 2.5 10C2.5 5.85786 5.85786 2.5 10 2.5C12.1305 2.5 13.9878 3.28394 15.3432 4.5L16.03 3.813C17.6057 5.38866 18.5 7.49966 18.5 9.79966C18.5 12.0997 17.6057 14.2107 16.03 15.7863L15.3363 15.0927C16.6992 13.7298 17.5 11.9238 17.5 10Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M10 2V0" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M22 10H24" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M10 22V24" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M2 10H0" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M18.364 5.63604L17.657 4.92904" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M6.34315 17.657L5.63614 18.364" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M18.364 18.364L17.657 17.657" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M6.34315 6.34304L5.63614 5.63604" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </g>
    
    <!-- 箭头图标 (折叠/展开) -->
    <g v-else-if="name === 'arrow'">
      <path d="M6 12L10 8L6 4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </g>
    
    <!-- 文档图标 -->
    <g v-else-if="name === 'documentation'">
      <path d="M11 2.253a1 1 0 1 0-2 0h2zm-2 13a1 1 0 1 0 2 0H9zm.447-12.167a1 1 0 1 0 1.107-1.666L9.447 3.086zM1 2.253L.447 1.42A1 1 0 0 0 0 2.253h1zm0 13H0a1 1 0 0 0 1.553.833L1 15.253zm8.447.833a1 1 0 1 0 1.107-1.666l-1.107 1.666zm0-14.666a1 1 0 1 0 1.107 1.666L9.447 1.42zM19 2.253h1a1 1 0 0 0-.447-.833L19 2.253zm0 13l-.553.833A1 1 0 0 0 20 15.253h-1zm-9.553-.833a1 1 0 1 0 1.107 1.666L9.447 14.42zM9 2.253v13h2v-13H9zm1.553-.833C9.203.523 7.42 0 5.5 0v2c1.572 0 2.961.431 3.947 1.086l1.107-1.666zM5.5 0C3.58 0 1.797.523.447 1.42l1.107 1.666C2.539 2.431 3.928 2 5.5 2V0zM0 2.253v13h2v-13H0zm1.553 13.833C2.539 15.431 3.928 15 5.5 15v-2c-1.92 0-3.703.523-5.053 1.42l1.107 1.666zM5.5 15c1.572 0 2.961.431 3.947 1.086l1.107-1.666C9.203 13.523 7.42 13 5.5 13v2zm5.053-11.914C11.539 2.431 12.928 2 14.5 2V0c-1.92 0-3.703.523-5.053 1.42l1.107 1.666zM14.5 2c1.573 0 2.961.431 3.947 1.086l1.107-1.666C18.203.523 16.421 0 14.5 0v2zm3.5.253v13h2v-13h-2zm1.553 12.167C18.203 13.523 16.421 13 14.5 13v2c1.573 0 2.961.431 3.947 1.086l1.107-1.666zM14.5 13c-1.92 0-3.703.523-5.053 1.42l1.107 1.666C11.539 15.431 12.928 15 14.5 15v-2z" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
    </g>
    
    <!-- 支持图标 -->
    <g v-else-if="name === 'support'">
      <path d="M10 3.22l-.61-.6a5.5 5.5 0 0 0-7.666.105 5.5 5.5 0 0 0-.114 7.665L10 18.78l8.39-8.4a5.5 5.5 0 0 0-.114-7.665 5.5 5.5 0 0 0-7.666-.105l-.61.61z" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
    </g>
    
    <!-- 生态图标 -->
    <g v-else-if="name === 'ecosystem'">
      <path d="M11.447 8.894a1 1 0 1 0-.894-1.789l.894 1.789zm-2.894-.789a1 1 0 1 0 .894 1.789l-.894-1.789zm0 1.789a1 1 0 1 0 .894-1.789l-.894 1.789zM7.447 7.106a1 1 0 1 0-.894 1.789l.894-1.789zM10 9a1 1 0 1 0-2 0h2zm-2 2.5a1 1 0 1 0 2 0H8zm9.447-5.606a1 1 0 1 0-.894-1.789l.894 1.789zm-2.894-.789a1 1 0 1 0 .894 1.789l-.894-1.789zm0 1.789a1 1 0 1 0 .894-1.789l-.894 1.789zm-2.894-.789a1 1 0 1 0 .894 1.789l-.894-1.789zm1.092 8.207a1 1 0 1 0-1.681 1.189l.894-.414l.787.425zm-2.681-1.789a1 1 0 1 0 .894 1.789l-.894-1.789zm-1.106 3.578a1 1 0 1 0-1.789-.894l.425.909l-.425.787zm2.425-.894a1 1 0 1 0 .894 1.789l-.894-1.789zm-2.894-4.789a1 1 0 1 0 .894 1.789l-.894-1.789zm-1.106 3.578a1 1 0 1 0-1.789-.894l.425.909l-.425.787zm2.425-.894a1 1 0 1 0 .894 1.789l-.894-1.789zm-3.789-1.789a1 1 0 1 0-.894 1.789l.894-1.789zm-1.106 3.578a1 1 0 1 0-1.789-.894l.425.909l-.425.787z" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
    </g>
    
    <!-- 工具图标 -->
    <g v-else-if="name === 'tooling'">
      <path d="M20 18v-4h-3v1h-2v-1H9v1H7v-1H4v4h16M6.33 8l-1.74 4H7v-1h2v1h6v-1h2v1h2.41l-1.74-4H6.33M9 5v1h6V5H9m12.84 7.61c.1.22.16.48.16.8V18c0 .53-.21 1-.6 1.41c-.4.4-.85.59-1.4.59H4c-.55 0-1-.19-1.4-.59C2.21 19 2 18.53 2 18v-4.59c0-.32.06-.58.16-.8L4.5 7.22C4.84 6.41 5.45 6 6.33 6H7V5c0-.55.18-1 .57-1.41C7.96 3.2 8.44 3 9 3h6c.56 0 1.04.2 1.43.59c.39.41.57.86.57 1.41v1h.67c.88 0 1.49.41 1.83 1.22l2.34 5.39z" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
    </g>
    
    <!-- 社区图标 -->
    <g v-else-if="name === 'community'">
      <path d="M15 4a1 1 0 1 0 0 2V4zm0 11v-1a1 1 0 0 0-1 1h1zm0 4l-.707.707A1 1 0 0 0 16 19h-1zm-4-4l.707-.707A1 1 0 0 0 11 14v1zm-4.707-1.293a1 1 0 0 0-1.414 1.414l1.414-1.414zm-.707.707l-.707-.707.707.707zM9 11v-1a1 1 0 0 0-.707.293L9 11zm-4 0h1a1 1 0 0 0-1-1v1zm0 4H4a1 1 0 0 0 1.707.707L5 15zm10-9h2V4h-2zm-4-2h2V2h-2zm-4 0h2V2h-2zm-4 0h2V2h-2zm14 2v11.83a2.004 2.004 0 0 1-1.205 1.853l-.196.083l-.064.023a2 2 0 0 1-1.364 0l-.064-.023l-.196-.083A2.003 2.003 0 0 1 11 17.83V6a2 2 0 0 1 1.83-2h.17zm-9 2v5h2v-5h-2zm-4 0v5h2v-5H6zm8 0v5h2v-5h-2zm4 0v9.83a2.003 2.003 0 0 1-.83 1.654l-.17.076V6h.17a2 2 0 0 1 1.83 2zm-13 0h2.17a2 2 0 0 1 1.83-2h.17v11.83a2.003 2.003 0 0 1-.83 1.654l-.17.076h-2.17A2.003 2.003 0 0 1 5 17.83V6a2 2 0 0 1 1.83-2h.17z" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
    </g>
    
    <!-- 格式化图标 -->
    <g v-else-if="name === 'format'">
      <path d="M20 8H14V2H12V8H6V12H12V22H14V12H20V8Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </g>
    
    <!-- 复制图标 -->
    <g v-else-if="name === 'copy'">
      <path d="M8 4H6C4.89543 4 4 4.89543 4 6V18C4 19.1046 4.89543 20 6 20H18C19.1046 20 20 19.1046 20 18V16" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <rect x="8" y="4" width="12" height="12" rx="2" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </g>
  </svg>
</template>

<script>
export default {
  name: 'Icon',
  props: {
    name: {
      type: String,
      required: true,
      validator: function(value) {
        return [
          'refresh', 'moon', 'sun', 'arrow',
          'documentation', 'support', 'ecosystem',
          'tooling', 'community', 'format', 'copy'
        ].includes(value);
      }
    },
    size: {
      type: Number,
      default: 20
    },
    className: {
      type: String,
      default: ''
    }
  },
  computed: {
    iconClass() {
      return this.className ? `icon-${this.name} ${this.className}` : `icon-${this.name}`;
    }
  }
};
</script>

<style scoped>
.icon {
  display: inline-block;
  vertical-align: middle;
  fill: currentColor;
}
</style>