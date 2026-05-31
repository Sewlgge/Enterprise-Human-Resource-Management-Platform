<script setup>
import { onMounted, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import api, { withMessage } from '@/services/api'
import { NOTICE_STATUS, isNoticeDraft } from '@/utils/constants'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = ref({ pageNum: 1, pageSize: 10, title: '' })
const dialogVisible = ref(false)
const form = ref({ id: null, title: '', content: '' })
const hasDraftLoaded = ref(false)

const dialogTitle = computed(() => (form.value.id ? '编辑公告' : '发布公告'))

async function loadData() {
  loading.value = true
  try {
    const params = { ...query.value }
    if (auth.isAdmin) {
      params.status = -1
    }
    const data = await api.noticePage(params)
    list.value = data.items || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

async function openPublish(row) {
  hasDraftLoaded.value = false

  if (row && isNoticeDraft(row.status)) {
    form.value = {
      id: row.id,
      title: row.title,
      content: row.content || '',
    }
    hasDraftLoaded.value = true
    dialogVisible.value = true
    return
  }

  form.value = { id: null, title: '', content: '' }
  try {
    const draft = await api.noticeDraft()
    if (draft) {
      form.value = {
        id: draft.id,
        title: draft.title,
        content: draft.content || '',
      }
      hasDraftLoaded.value = true
      ElMessage.info('已加载您的草稿内容')
    }
  } catch {
    /* 无草稿时保持空白表单 */
  }
  dialogVisible.value = true
}

async function submitNotice(status) {
  if (!form.value.title?.trim()) {
    ElMessage.warning('请输入公告标题')
    return
  }
  const payload = {
    id: form.value.id,
    title: form.value.title,
    content: form.value.content,
    status: Number(status),
  }
  const msg = status === 0 ? '草稿已保存' : '发布成功'
  await withMessage(api.publishNotice(payload), msg)
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
      <el-button v-if="auth.isAdmin" type="success" @click="openPublish()">发布公告</el-button>
    </div>

    <el-table v-loading="loading" :data="list" stripe style="margin-top: 16px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="200">
        <template #default="{ row }">
          <el-link
            v-if="!isNoticeDraft(row.status)"
            type="primary"
            @click="router.push(`/notices/${row.id}`)"
          >
            {{ row.title }}
          </el-link>
          <span v-else>{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="publisherName" label="发布人" width="120" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="isNoticeDraft(row.status) ? 'info' : 'success'" size="small">
            {{ NOTICE_STATUS[Number(row.status)] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column v-if="auth.isAdmin" label="操作" width="160">
        <template #default="{ row }">
          <el-button v-if="isNoticeDraft(row.status)" link type="primary" @click="openPublish(row)">
            继续编辑
          </el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px">
      <el-alert
        v-if="hasDraftLoaded"
        type="info"
        :closable="false"
        show-icon
        title="已恢复草稿内容，可继续编辑后保存草稿或正式发布。"
        style="margin-bottom: 16px"
      />
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="6" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button @click="submitNotice(0)">保存草稿</el-button>
        <el-button type="primary" @click="submitNotice(1)">发布</el-button>
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
