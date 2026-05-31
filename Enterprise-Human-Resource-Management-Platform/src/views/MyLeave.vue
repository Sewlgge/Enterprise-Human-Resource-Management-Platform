<script setup>
import { onMounted, ref } from 'vue'
import api, { withMessage } from '@/services/api'
import { LEAVE_STATUS, LEAVE_TYPES, leaveTagType } from '@/utils/constants'

const loading = ref(false)
const list = ref([])
const dialogVisible = ref(false)
const form = ref({ startDate: '', days: 1, type: '事假', reason: '' })

async function loadData() {
  loading.value = true
  try {
    list.value = await api.myLeave()
  } finally {
    loading.value = false
  }
}

function openApply() {
  form.value = { startDate: '', days: 1, type: '事假', reason: '' }
  dialogVisible.value = true
}

async function submit() {
  await withMessage(api.applyLeave(form.value), '申请已提交')
  dialogVisible.value = false
  loadData()
}

onMounted(loadData)
</script>

<template>
  <el-card>
    <div class="toolbar">
      <el-button type="primary" @click="openApply">申请请假</el-button>
    </div>

    <el-table v-loading="loading" :data="list" stripe style="margin-top: 16px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="type" label="类型" width="90" />
      <el-table-column prop="startDate" label="开始日期" width="120" />
      <el-table-column prop="endDate" label="结束日期" width="120" />
      <el-table-column prop="days" label="天数" width="70" />
      <el-table-column prop="reason" label="原因" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="leaveTagType(row.status)">{{ LEAVE_STATUS[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="approveRemark" label="审批备注" />
      <el-table-column prop="applyTime" label="申请时间" width="180" />
    </el-table>

    <el-dialog v-model="dialogVisible" title="申请请假" width="480px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="开始日期" required>
          <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="请假天数" required>
          <el-input-number v-model="form.days" :min="1" :max="30" />
        </el-form-item>
        <el-form-item label="请假类型" required>
          <el-select v-model="form.type" style="width: 100%">
            <el-option v-for="t in LEAVE_TYPES" :key="t" :label="t" :value="t" />
          </el-select>
        </el-form-item>
        <el-form-item label="请假原因">
          <el-input v-model="form.reason" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">提交</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.toolbar {
  display: flex;
  justify-content: flex-end;
}
</style>
