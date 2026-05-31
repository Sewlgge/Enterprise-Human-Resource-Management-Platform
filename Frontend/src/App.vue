<script setup></script>

<template>
  <router-view />
</template>

<style>
:root {
  --hr-primary: #0f766e;
  --hr-primary-hover: #0d9488;
  --hr-primary-light: #5eead4;
  --hr-sidebar-from: #134e4a;
  --hr-sidebar-to: #042f2e;
  --hr-bg: #f8fafc;
  --hr-surface: #fff;
  --hr-border: #eef2f6;
  --hr-text: #0f172a;
  --hr-text-secondary: #64748b;

  --el-color-primary: #0f766e;
  --el-color-primary-light-3: #4da89f;
  --el-color-primary-light-5: #7fc4bc;
  --el-color-primary-light-7: #b3ddd7;
  --el-color-primary-light-8: #cceae6;
  --el-color-primary-light-9: #e6f4f2;
  --el-color-primary-dark-2: #0c5f58;
}

* {
  box-sizing: border-box;
}

body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background: var(--hr-bg);
  color: var(--hr-text);
}

/* 全局卡片与表格微调，与登录页风格一致 */
.el-card {
  border-radius: 12px;
  border-color: var(--hr-border);
}

.el-table th.el-table__cell {
  background-color: #f0fdfa !important;
}
</style>
