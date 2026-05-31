<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const activeMenu = computed(() => route.path)

const menuItems = computed(() => {
  const items = [
    { path: '/notices', title: '公告通知' },
    { path: '/attendance/my', title: '我的考勤' },
    { path: '/leave/my', title: '我的请假' },
    { path: '/employees', title: '员工管理' },
    { path: '/profile', title: '个人中心' },
  ]
  if (auth.isAdmin) {
    items.unshift({ path: '/dashboard', title: '仪表盘' })
    items.splice(4, 0,
      { path: '/depts', title: '部门管理' },
      { path: '/positions', title: '职位管理' },
      { path: '/attendance/manage', title: '考勤管理' },
      { path: '/leave/manage', title: '请假审批' },
      { path: '/logs', title: '操作日志' },
    )
  }
  return items
})

async function handleLogout() {
  await auth.logout()
  router.push('/login')
}
</script>

<template>
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="logo">人事管理平台</div>
      <el-menu :default-active="activeMenu" router class="menu">
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          {{ item.title }}
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span class="page-title">{{ route.meta.title || '企业人事管理' }}</span>
        <div class="user-area">
          <div class="user-profile" @click="router.push('/profile')">
            <el-avatar :size="36" :src="auth.user?.avatar">
              {{ auth.user?.username?.charAt(0)?.toUpperCase() }}
            </el-avatar>
            <span class="username">{{ auth.user?.username }}</span>
          </div>
          <el-tag size="small" :type="auth.isAdmin ? 'danger' : undefined" :class="auth.isAdmin ? '' : 'tag-employee'">
            {{ auth.isAdmin ? '管理员' : '员工' }}
          </el-tag>
          <el-button link type="primary" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout {
  min-height: 100vh;
}

.aside {
  background: linear-gradient(180deg, var(--hr-sidebar-from) 0%, var(--hr-sidebar-to) 100%);
  color: #ecfdf5;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 16px;
  font-weight: 600;
  color: #ecfdf5;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.menu {
  border-right: none;
  background: transparent;
}

.menu :deep(.el-menu-item) {
  color: rgba(236, 253, 245, 0.75);
}

.menu :deep(.el-menu-item.is-active) {
  background: var(--hr-primary) !important;
  color: #fff;
}

.menu :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.08);
  color: #ecfdf5;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--hr-surface);
  border-bottom: 1px solid var(--hr-border);
  height: 60px;
}

.page-title {
  font-size: 18px;
  font-weight: 500;
  color: var(--hr-text);
}

.user-area {
  display: flex;
  align-items: center;
  color: var(--hr-text-secondary);
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background 0.2s;
}

.user-profile:hover {
  background: #f0fdfa;
}

.username {
  font-weight: 500;
  color: var(--hr-text);
}

.tag-employee {
  --el-tag-bg-color: #e6f4f2;
  --el-tag-border-color: #b3ddd7;
  --el-tag-text-color: var(--hr-primary);
}

.main {
  background: var(--hr-bg);
  min-height: calc(100vh - 60px);
}
</style>
