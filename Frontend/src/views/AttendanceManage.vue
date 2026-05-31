<script setup>
import { onMounted, ref } from 'vue'
import api, { withMessage } from '@/services/api'
import { ATTENDANCE_STATUS, attendanceTagType } from '@/utils/constants'

function todayStr() {
  const d = new Date()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${d.getFullYear()}-${m}-${day}`
}

const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = ref({
  page: 1,
  size: 10,
  attendanceDate: todayStr(),
  employeeId: null,
  status: null,
})
const editVisible = ref(false)
const editForm = ref({ id: null, employeeId: null, signInTime: '', signOutTime: '', status: 0 })

async function loadData() {
  loading.value = true
  try {
    const params = { ...query.value }
    if (!params.employeeId) delete params.employeeId
    if (params.status === null || params.status === '') delete params.status
    const data = await api.attendancePage(params)
    list.value = data.items || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

function onDateChange() {
  query.value.page = 1
  loadData()
}

function resetQuery() {
  query.value = {
    page: 1,
    size: 10,
    attendanceDate: todayStr(),
    employeeId: null,
    status: null,
  }
  loadData()
}

function openEdit(row) {
  editForm.value = {
    id: row.id,
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
    <el-form :inline="true" :model="query" class="toolbar">
      <el-form-item label="考勤日期">
        <el-date-picker
          v-model="query.attendanceDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="选择日期"
          :clearable="false"
          style="width: 160px"
          @change="onDateChange"
        />
      </el-form-item>
      <el-form-item label="员工 ID">
        <el-input-number
          v-model="query.employeeId"
          :min="1"
          controls-position="right"
          placeholder="可选"
          style="width: 140px"
        />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable placeholder="全部" style="width: 120px">
          <el-option v-for="(label, key) in ATTENDANCE_STATUS" :key="key" :label="label" :value="Number(key)" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetQuery">今天</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list" stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="employeeName" label="员工" min-width="100" />
      <el-table-column prop="attendanceDate" label="日期" width="120" />
      <el-table-column prop="signInTime" label="签到" width="180">
        <template #default="{ row }">{{ row.signInTime || '-' }}</template>
      </el-table-column>
      <el-table-column prop="signOutTime" label="签退" width="180">
        <template #default="{ row }">{{ row.signOutTime || '-' }}</template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="attendanceTagType(row.status)">{{ ATTENDANCE_STATUS[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">修正</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="query.page"
      v-model:page-size="query.size"
      :total="total"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next"
      style="margin-top: 16px; justify-content: flex-end"
      @current-change="loadData"
      @size-change="onDateChange"
    />

    <el-dialog v-model="editVisible" title="修正考勤" width="520px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="签到时间">
          <el-date-picker
            v-model="editForm.signInTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="签退时间">
          <el-date-picker
            v-model="editForm.signOutTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
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

<style scoped>
.toolbar {
  display: flex;
  flex-wrap: wrap;
  gap: 4px 0;
}
</style>
