<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { withMessage } from '@/services/api'
import AuthShell from '@/components/AuthShell.vue'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const form = ref({ username: '', password: '' })

async function submit() {
  if (!form.value.username || !form.value.password) return
  loading.value = true
  try {
    await withMessage(auth.login(form.value.username, form.value.password), '登录成功')
    router.push('/')
  } catch {
    /* handled */
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <AuthShell title="欢迎回来" subtitle="登录您的账号以继续使用">
    <el-form :model="form" label-position="top" @submit.prevent="submit">
      <el-form-item label="用户名">
        <el-input v-model="form.username" size="large" placeholder="4-16 位字符" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input
          v-model="form.password"
          type="password"
          size="large"
          show-password
          placeholder="4-16 位字符"
        />
      </el-form-item>
      <el-button type="primary" size="large" :loading="loading" native-type="submit" class="submit-btn">
        登录
      </el-button>
      <div class="footer">
        还没有账号？
        <router-link to="/register">立即注册</router-link>
      </div>
    </el-form>
  </AuthShell>
</template>

<style scoped>
.submit-btn {
  width: 100%;
  margin-top: 8px;
}

.footer {
  margin-top: 24px;
  text-align: center;
  color: var(--hr-text-secondary);
  font-size: 14px;
}

.footer a {
  color: var(--hr-primary);
  text-decoration: none;
  font-weight: 500;
}

.footer a:hover {
  text-decoration: underline;
}

:deep(.el-form-item__label) {
  color: #334155;
  font-weight: 500;
}
</style>
