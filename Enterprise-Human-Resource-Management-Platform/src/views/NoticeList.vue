<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import api, { withMessage } from '@/services/api'
import { NOTICE_STATUS } from '@/utils/constants'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = ref({ pageNum: 1, pageSize: 10, title: '' })
const dialogVisible = ref(false)
const form = ref({ title: '', content: '', status: 1 })

async function loadData() {
  loading.value = true
  try {
    const data = await api.noticePage({ ...query.value })
    list.value = data.items || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

function openPublish() {
  form.value = { title: '', content: '', status: 1 }
  dialogVisible.value = true
}

async function publish() {
  await withMessage(api.publishNotice(form.value), '发布成功')
  dialogVisible.value = false
  loadData()
}

async function remove(id) {
  await ElMessageBox.confirm('确认删除该公告？', '提示', { type: 'warning' })
  await withMessage(api.deleteNotice(id), '删除成功')
  loadData()
}

onMounted(loadData)
</script>

<template>
  <el-card>
    <div class="toolbar">
      <el-input v-model="query.title" placeholder="标题搜索" clearable style="width: 220px" @keyup.enter="loadData" />
      <el-button type="primary" @click="loadData">查询</el-button>
      <el-button v-if="auth.isAdmin" type="success" @click="openPublish">发布公告</el-button>
    </div>

    <el-table v-loading="loading" :data="list" stripe style="margin-top: 16px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="200">
        <template #default="{ row }">
          <el-link type="primary" @click="router.push(`/notices/${row.id}`)">{{ row.title }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="publisherName" label="发布人" width="120" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">{{ NOTICE_STATUS[row.status] }}</template>
      </el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column v-if="auth.isAdmin" label="操作" width="100">
        <template #default="{ row }">
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

    <el-dialog v-model="dialogVisible" title="发布公告" width="560px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="6" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">已发布</el-radio>
            <el-radio :value="0">草稿</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="publish">发布</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.toolbar {
  display: flex;
  gap: 12px;
}
</style>
