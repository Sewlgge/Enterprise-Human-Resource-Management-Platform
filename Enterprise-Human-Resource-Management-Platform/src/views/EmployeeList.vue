<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import api, { withMessage } from '@/services/api'
import { GENDER_MAP } from '@/utils/constants'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = ref({ pageNum: 1, pageSize: 10, realName: '', phone: '', deptId: null })
const depts = ref([])

async function loadDepts() {
  depts.value = await api.deptList()
}

async function loadData() {
  loading.value = true
  try {
    const data = await api.employeePage({ ...query.value })
    list.value = data.items || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

function search() {
  query.value.pageNum = 1
  loadData()
}

function view(id) {
  router.push({ name: 'EmployeeDetails', params: { id } })
}

function edit(id) {
  router.push({ name: 'EmployeeEdit', params: { id } })
}

async function del(id) {
  await ElMessageBox.confirm('确认删除该员工？', '提示', { type: 'warning' })
  await withMessage(api.deleteEmployee(id), '删除成功')
  loadData()
}

onMounted(async () => {
  await loadDepts()
  await loadData()
})
</script>

<template>
  <el-card>
    <el-form :inline="true" :model="query" @submit.prevent="search">
      <el-form-item label="姓名">
        <el-input v-model="query.realName" clearable placeholder="员工姓名" />
      </el-form-item>
      <el-form-item label="手机">
        <el-input v-model="query.phone" clearable placeholder="手机号" />
      </el-form-item>
      <el-form-item label="部门">
        <el-select v-model="query.deptId" clearable placeholder="全部部门" style="width: 160px">
          <el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button v-if="auth.isAdmin" type="success" @click="$router.push('/employees/create')">
          新建员工
        </el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list" stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column label="头像" width="80">
        <template #default="{ row }">
          <el-avatar :size="36" :src="row.avatar">{{ row.realName?.[0] }}</el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="realName" label="姓名" />
      <el-table-column label="性别" width="70">
        <template #default="{ row }">{{ GENDER_MAP[row.gender] }}</template>
      </el-table-column>
      <el-table-column prop="deptName" label="部门" />
      <el-table-column prop="positionName" label="职位" />
      <el-table-column prop="phone" label="手机" width="130" />
      <el-table-column prop="email" label="邮箱" min-width="160" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="view(row.id)">查看</el-button>
          <template v-if="auth.isAdmin">
            <el-button link type="primary" @click="edit(row.id)">编辑</el-button>
            <el-button link type="danger" @click="del(row.id)">删除</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="query.pageNum"
      v-model:page-size="query.pageSize"
      :total="total"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next"
      style="margin-top: 16px; justify-content: flex-end"
      @current-change="loadData"
      @size-change="loadData"
    />
  </el-card>
</template>
