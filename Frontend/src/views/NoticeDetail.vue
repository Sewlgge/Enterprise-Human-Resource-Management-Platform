<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/services/api'
import { NOTICE_STATUS } from '@/utils/constants'

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const notice = ref(null)

onMounted(async () => {
  try {
    notice.value = await api.noticeDetail(route.params.id)
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <el-card v-loading="loading">
    <template v-if="notice">
      <h2 style="margin-top: 0">{{ notice.title }}</h2>
      <div class="meta">
        <span>发布人：{{ notice.publisherName || '-' }}</span>
        <span>状态：{{ NOTICE_STATUS[notice.status] }}</span>
        <span>时间：{{ notice.createTime }}</span>
      </div>
      <el-divider />
      <div class="content">{{ notice.content }}</div>
      <el-button style="margin-top: 24px" @click="router.back()">返回</el-button>
    </template>
  </el-card>
</template>

<style scoped>
.meta {
  display: flex;
  gap: 24px;
  color: #999;
  font-size: 14px;
}

.content {
  line-height: 1.8;
  white-space: pre-wrap;
  color: #333;
}
</style>
