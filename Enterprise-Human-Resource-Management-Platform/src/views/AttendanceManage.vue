<script setup>
import { onMounted, ref } from 'vue'
import api, { withMessage } from '@/services/api'
import { ATTENDANCE_STATUS, attendanceTagType } from '@/utils/constants'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = ref({ page: 1, size: 10, employeeId: null, status: null })
const editVisible = ref(false)
const editForm = ref({ employeeId: null, signInTime: '', signOutTime: '', status: 0 })

async function loadData() {
  loading.value = true
  try {
    const data = await api.attendancePage({ ...query.value })
    list.value = data.items || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

function openEdit(row) {
  editForm.value = {
    employeeId: row.employeeId,
    signInTime: row.signInTime,
    signOutTime: row.signOutTime,
    status: row.status,
  }
  editVisible.value = true
}

async function saveEdit() {
  await withMessage(api.updateAttendance(editForm.value), '更新成功')
  editVisible.value = false
  loadData()
}

onMounted(loadData)
</script>

<template>
  <el-card>
    <el-form :inline="true" :model="query">
      <el-form-item label="员工 ID">
        <el-input-number v-model="query.employeeId" :min="1" controls-position="right" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable placeholder="全部" style="width: 120px">
          <el-option v-for="(label, key) in ATTENDANCE_STATUS" :key="key" :label="label" :value="Number(key)" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list" stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="employeeName" label="员工" />
      <el-table-column prop="signInTime" label="签到" width="180" />
      <el-table-column prop="signOutTime" label="签退" width="180" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="attendanceTagType(row.status)">{{ ATTENDANCE_STATUS[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">修正</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="query.page"
      v-model:page-size="query.size"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top: 16px; justify-content: flex-end"
      @current-change="loadData"
    />

    <el-dialog v-model="editVisible" title="修正考勤" width="520px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="签到时间">
          <el-date-picker
            v-model="editForm.signInTime"
            type="datetime"
            value-format="YYYY-MM-DDTHH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="签退时间">
          <el-date-picker
            v-model="editForm.signOutTime"
            type="datetime"
            value-format="YYYY-MM-DDTHH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="editForm.status" style="width: 100%">
            <el-option v-for="(label, key) in ATTENDANCE_STATUS" :key="key" :label="label" :value="Number(key)" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>
