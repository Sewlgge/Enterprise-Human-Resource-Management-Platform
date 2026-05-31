<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import api, { withMessage } from '@/services/api'
import { GENDER_MAP } from '@/utils/constants'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const loading = ref(true)
const data = ref(null)

onMounted(async () => {
  try {
    data.value = await api.getEmployee(route.params.id)
  } finally {
    loading.value = false
  }
})

async function remove() {
  await ElMessageBox.confirm('确认删除该员工？', '提示', { type: 'warning' })
  await withMessage(api.deleteEmployee(route.params.id), '删除成功')
  router.push('/employees')
}
</script>

<template>
  <el-card v-loading="loading">
    <template v-if="data">
      <div class="header">
        <el-avatar :size="72" :src="data.avatar">{{ data.realName?.[0] }}</el-avatar>
        <div>
          <h2 style="margin: 0">{{ data.realName }}</h2>
          <p class="sub">{{ data.deptName }} · {{ data.positionName }}</p>
        </div>
      </div>

      <el-descriptions :column="2" border style="margin-top: 20px">
        <el-descriptions-item label="员工 ID">{{ data.id }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ data.username || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ GENDER_MAP[data.gender] }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ data.age }}</el-descriptions-item>
        <el-descriptions-item label="手机">{{ data.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ data.email }}</el-descriptions-item>
        <el-descriptions-item label="住址" :span="2">{{ data.address || '-' }}</el-descriptions-item>
        <el-descriptions-item label="入职时间">{{ data.hireDate }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ data.createTime }}</el-descriptions-item>
      </el-descriptions>

      <div style="margin-top: 16px">
        <el-button @click="router.back()">返回</el-button>
        <el-button v-if="auth.isAdmin" type="primary" @click="router.push(`/employees/${data.id}/edit`)">
          编辑
        </el-button>
        <el-button v-if="auth.isAdmin" type="danger" @click="remove">删除</el-button>
      </div>
    </template>
  </el-card>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  gap: 16px;
}

.sub {
  margin: 4px 0 0;
  color: #999;
}
</style>
