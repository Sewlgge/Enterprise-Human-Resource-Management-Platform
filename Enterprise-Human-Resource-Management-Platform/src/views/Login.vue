<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { withMessage } from '@/services/api'

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
  <div class="auth-page">
    <el-card class="auth-card">
      <h2>企业人事管理平台</h2>
      <p class="subtitle">请登录您的账号</p>
      <el-form :model="form" label-width="70px" @submit.prevent="submit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="4-16 位字符" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="4-16 位字符" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" native-type="submit" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
        <div class="footer">
          还没有账号？
          <router-link to="/register">立即注册</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.auth-card {
  width: 420px;
  padding: 12px;
}

h2 {
  margin: 0 0 8px;
  text-align: center;
}

.subtitle {
  text-align: center;
  color: #999;
  margin: 0 0 24px;
}

.footer {
  text-align: center;
  color: #666;
  font-size: 14px;
}
</style>
