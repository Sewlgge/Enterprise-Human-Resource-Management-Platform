<script setup>
import { onMounted, ref } from 'vue'
import { ElMessageBox } from 'element-plus'
import api, { withMessage } from '@/services/api'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const depts = ref([])
const query = ref({ pageNum: 1, pageSize: 10, name: '', deptId: null })
const dialogVisible = ref(false)
const editing = ref(null)
const form = ref({ name: '', description: '', deptId: null })

async function loadDepts() {
  depts.value = await api.deptList()
}

async function loadData() {
  loading.value = true
  try {
    const data = await api.positionPage({ ...query.value })
    list.value = data.items || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editing.value = null
  form.value = { name: '', description: '', deptId: null }
  dialogVisible.value = true
}

function openEdit(row) {
  editing.value = row
  form.value = { name: row.name, description: row.description, deptId: row.deptId }
  dialogVisible.value = true
}

async function save() {
  if (editing.value) {
    await withMessage(
      api.updatePosition({ id: editing.value.id, name: form.value.name, description: form.value.description }),
      '更新成功',
    )
  } else {
    await withMessage(api.addPosition(form.value), '添加成功')
  }
  dialogVisible.value = false
  loadData()
}

async function remove(id) {
  await ElMessageBox.confirm('确认删除该职位？', '提示', { type: 'warning' })
  await withMessage(api.deletePosition(id), '删除成功')
  loadData()
}

onMounted(async () => {
  await loadDepts()
  await loadData()
})
</script>

<template>
  <el-card>
    <div class="toolbar">
      <el-input v-model="query.name" placeholder="职位名称" clearable style="width: 180px" />
      <el-select v-model="query.deptId" clearable placeholder="所属部门" style="width: 160px">
        <el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id" />
      </el-select>
      <el-button type="primary" @click="loadData">查询</el-button>
      <el-button type="success" @click="openCreate">新增职位</el-button>
    </div>

    <el-table v-loading="loading" :data="list" stripe style="margin-top: 16px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="职位名称" />
      <el-table-column prop="deptName" label="所属部门" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row.id)">删除</el-button>
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

    <el-dialog v-model="dialogVisible" :title="editing ? '编辑职位' : '新增职位'" width="480px">
      <el-form :model="form" label-width="80px">
        <el-form-item v-if="!editing" label="部门" required>
          <el-select v-model="form.deptId" placeholder="选择部门" style="width: 100%">
            <el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.toolbar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}
</style>
