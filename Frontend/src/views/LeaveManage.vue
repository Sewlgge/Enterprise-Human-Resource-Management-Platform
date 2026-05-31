<script setup>
import { onMounted, ref } from 'vue'
import api, { withMessage } from '@/services/api'
import { LEAVE_STATUS, leaveTagType } from '@/utils/constants'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = ref({ pageNum: 1, pageSize: 10, status: null })
const remarkDialog = ref(false)
const currentId = ref(null)
const remark = ref('')

async function loadData() {
  loading.value = true
  try {
    const data = await api.leavePage({ ...query.value })
    list.value = data.items || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

async function approve(id, status) {
  if (status === 2) {
    currentId.value = id
    remark.value = ''
    remarkDialog.value = true
    return
  }
  await withMessage(api.approveLeave(id, status), '审批成功')
  loadData()
}

async function confirmReject() {
  await withMessage(api.approveLeave(currentId.value, 2, remark.value), '已拒绝')
  remarkDialog.value = false
  loadData()
}

onMounted(loadData)
</script>

<template>
  <el-card>
    <el-form :inline="true">
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable placeholder="全部" style="width: 120px">
          <el-option v-for="(label, key) in LEAVE_STATUS" :key="key" :label="label" :value="Number(key)" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list" stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="employeeName" label="申请人" />
      <el-table-column prop="type" label="类型" width="90" />
      <el-table-column prop="startDate" label="开始" width="120" />
      <el-table-column prop="endDate" label="结束" width="120" />
      <el-table-column prop="days" label="天数" width="70" />
      <el-table-column prop="reason" label="原因" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="leaveTagType(row.status)">{{ LEAVE_STATUS[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button link type="success" @click="approve(row.id, 1)">通过</el-button>
            <el-button link type="danger" @click="approve(row.id, 2)">拒绝</el-button>
          </template>
          <span v-else class="done">已处理</span>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="query.pageNum"
      v-model:page-size="query.pageSize"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top: 16px; justify-content: flex-end"
      @current-change="loadData"
    />

    <el-dialog v-model="remarkDialog" title="拒绝原因" width="420px">
      <el-input v-model="remark" type="textarea" placeholder="可选填写拒绝备注" />
      <template #footer>
        <el-button @click="remarkDialog = false">取消</el-button>
        <el-button type="danger" @click="confirmReject">确认拒绝</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.done {
  color: #999;
  font-size: 13px;
}
</style>
