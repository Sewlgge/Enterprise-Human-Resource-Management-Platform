<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api, { uploadAvatar, withMessage } from '@/services/api'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const auth = useAuthStore()
const pwdForm = ref({ oldPassword: '', newPassword: '', confirm: '' })
const pwdLoading = ref(false)
const avatarLoading = ref(false)

async function changePassword() {
  if (pwdForm.value.newPassword !== pwdForm.value.confirm) {
    ElMessage.warning('两次输入的新密码不一致')
    return
  }
  pwdLoading.value = true
  try {
    await withMessage(
      api.updatePassword(pwdForm.value.oldPassword, pwdForm.value.newPassword),
      '密码修改成功，即将退出登录',
    )
    await auth.logout()
    router.push('/login')
  } catch {
    /* handled */
  } finally {
    pwdLoading.value = false
  }
}

async function onAvatarChange(file) {
  avatarLoading.value = true
  try {
    const url = await withMessage(uploadAvatar(file), '头像上传成功')
    auth.setAvatar(url)
  } catch {
    /* handled */
  } finally {
    avatarLoading.value = false
  }
  return false
}
</script>

<template>
  <div class="profile-page">
    <section class="profile-hero">
      <el-avatar :size="88" :src="auth.user?.avatar" class="hero-avatar">
        {{ auth.user?.username?.charAt(0)?.toUpperCase() }}
      </el-avatar>
      <div class="hero-info">
        <h2>{{ auth.user?.username }}</h2>
        <p>{{ auth.isAdmin ? '管理员' : '普通员工' }} · ID {{ auth.user?.id }}</p>
      </div>
      <el-upload :show-file-list="false" accept="image/*" :before-upload="onAvatarChange">
        <el-button :loading="avatarLoading" plain>更换头像</el-button>
      </el-upload>
    </section>

    <el-row :gutter="20" class="profile-grid">
      <el-col :xs="24" :md="12">
        <el-card shadow="never" class="profile-card">
          <template #header>
            <span class="card-title">账号信息</span>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="用户名">{{ auth.user?.username }}</el-descriptions-item>
            <el-descriptions-item label="角色">
              {{ auth.isAdmin ? '管理员' : '普通员工' }}
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="auth.user?.status === 1 ? 'success' : 'danger'" size="small">
                {{ auth.user?.status === 1 ? '正常' : '禁用' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="注册时间">{{ auth.user?.createTime }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :xs="24" :md="12">
        <el-card shadow="never" class="profile-card">
          <template #header>
            <span class="card-title">修改密码</span>
          </template>
          <p class="pwd-tip">修改密码后系统将自动退出，需使用新密码重新登录。</p>
          <el-form :model="pwdForm" label-position="top">
            <el-form-item label="旧密码">
              <el-input v-model="pwdForm.oldPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="新密码">
              <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="4-16 位字符" />
            </el-form-item>
            <el-form-item label="确认新密码">
              <el-input v-model="pwdForm.confirm" type="password" show-password />
            </el-form-item>
            <el-button type="primary" :loading="pwdLoading" @click="changePassword">
              保存并退出登录
            </el-button>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.profile-page {
  max-width: 960px;
  margin: 0 auto;
}

.profile-hero {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 28px 32px;
  margin-bottom: 20px;
  background: linear-gradient(135deg, #f0fdfa 0%, #fff 100%);
  border: 1px solid #ccfbf1;
  border-radius: 16px;
}

.hero-avatar {
  border: 3px solid #fff;
  box-shadow: 0 4px 16px rgba(15, 118, 110, 0.15);
}

.hero-info {
  flex: 1;
}

.hero-info h2 {
  margin: 0 0 6px;
  font-size: 22px;
  color: var(--hr-text);
}

.hero-info p {
  margin: 0;
  color: var(--hr-text-secondary);
  font-size: 14px;
}

.profile-card {
  border-radius: 14px;
  border: 1px solid var(--hr-border);
  margin-bottom: 20px;
}

.card-title {
  font-weight: 600;
  color: var(--hr-text);
}

.pwd-tip {
  margin: 0 0 16px;
  padding: 10px 14px;
  background: #fffbeb;
  border-radius: 8px;
  color: #92400e;
  font-size: 13px;
  line-height: 1.5;
}

@media (max-width: 768px) {
  .profile-hero {
    flex-direction: column;
    text-align: center;
  }
}
</style>
