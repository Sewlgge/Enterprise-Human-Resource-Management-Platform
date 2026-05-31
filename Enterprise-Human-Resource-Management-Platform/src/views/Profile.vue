<script setup>
import { ref } from 'vue'
import api, { uploadAvatar, withMessage } from '@/services/api'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

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
      '密码修改成功',
    )
    pwdForm.value = { oldPassword: '', newPassword: '', confirm: '' }
  } catch {
    /* handled */
  } finally {
    pwdLoading.value = false
  }
}

async function onAvatarChange(file) {
  avatarLoading.value = true
  try {
    await withMessage(uploadAvatar(file.raw), '头像上传成功')
  } catch {
    /* handled */
  } finally {
    avatarLoading.value = false
  }
  return false
}
</script>

<template>
  <el-row :gutter="16">
    <el-col :span="10">
      <el-card header="账号信息">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="用户名">{{ auth.user?.username }}</el-descriptions-item>
          <el-descriptions-item label="用户 ID">{{ auth.user?.id }}</el-descriptions-item>
          <el-descriptions-item label="角色">
            {{ auth.isAdmin ? '管理员' : '普通员工' }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            {{ auth.user?.status === 1 ? '正常' : '禁用' }}
          </el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ auth.user?.createTime }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card header="上传头像" style="margin-top: 16px">
        <p class="tip">上传后将更新与您账号绑定的员工头像</p>
        <el-upload :show-file-list="false" accept="image/*" :before-upload="onAvatarChange">
          <el-button :loading="avatarLoading">选择图片上传</el-button>
        </el-upload>
      </el-card>
    </el-col>

    <el-col :span="14">
      <el-card header="修改密码">
        <el-form :model="pwdForm" label-width="100px" style="max-width: 420px">
          <el-form-item label="旧密码">
            <el-input v-model="pwdForm.oldPassword" type="password" show-password />
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="pwdForm.newPassword" type="password" show-password />
          </el-form-item>
          <el-form-item label="确认新密码">
            <el-input v-model="pwdForm.confirm" type="password" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="pwdLoading" @click="changePassword">保存</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
  </el-row>
</template>

<style scoped>
.tip {
  color: #999;
  font-size: 13px;
  margin: 0 0 12px;
}
</style>
