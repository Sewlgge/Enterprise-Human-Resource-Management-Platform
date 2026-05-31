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
          <span>{{ auth.user?.username }}</span>
          <el-tag size="small" :type="auth.isAdmin ? 'danger' : 'info'" style="margin: 0 12px">
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
  background: #001529;
  color: #fff;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.menu {
  border-right: none;
  background: #001529;
}

.menu :deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.75);
}

.menu :deep(.el-menu-item.is-active) {
  background: #1890ff !important;
  color: #fff;
}

.menu :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.08);
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid #eee;
  height: 60px;
}

.page-title {
  font-size: 18px;
  font-weight: 500;
}

.user-area {
  display: flex;
  align-items: center;
  color: #666;
}

.main {
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}
</style>
