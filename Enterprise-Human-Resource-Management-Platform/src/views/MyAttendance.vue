<script setup>
import { onMounted, ref } from 'vue'
import api, { withMessage } from '@/services/api'
import { ATTENDANCE_STATUS, attendanceTagType } from '@/utils/constants'

const loading = ref(false)
const list = ref([])
const month = ref(new Date().getMonth() + 1)
const signing = ref(false)

async function loadData() {
  loading.value = true
  try {
    list.value = await api.myAttendance(month.value)
  } finally {
    loading.value = false
  }
}

async function signIn() {
  signing.value = true
  try {
    await withMessage(api.signIn(), '签到成功')
    loadData()
  } catch {
    /* handled */
  } finally {
    signing.value = false
  }
}

async function signOut() {
  signing.value = true
  try {
    await withMessage(api.signOut(), '签退成功')
    loadData()
  } catch {
    /* handled */
  } finally {
    signing.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <el-card>
    <div class="actions">
      <el-select v-model="month" style="width: 120px" @change="loadData">
        <el-option v-for="m in 12" :key="m" :label="`${m} 月`" :value="m" />
      </el-select>
      <el-button type="primary" :loading="signing" @click="signIn">签到</el-button>
      <el-button type="warning" :loading="signing" @click="signOut">签退</el-button>
    </div>

    <el-table v-loading="loading" :data="list" stripe style="margin-top: 16px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="signInTime" label="签到时间" width="180" />
      <el-table-column prop="signOutTime" label="签退时间" width="180" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="attendanceTagType(row.status)">{{ ATTENDANCE_STATUS[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" />
      <el-table-column prop="createTime" label="记录时间" width="180" />
    </el-table>
  </el-card>
</template>

<style scoped>
.actions {
  display: flex;
  gap: 12px;
  align-items: center;
}
</style>
