<script setup>
import { onMounted, ref } from 'vue'
import api from '@/services/api'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = ref({ pageNum: 1, pageSize: 10, keyword: '', userId: null })

async function loadData() {
  loading.value = true
  try {
    const data = await api.logPage({ ...query.value })
    list.value = data.items || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <el-card>
    <el-form :inline="true">
      <el-form-item label="关键字">
        <el-input v-model="query.keyword" clearable placeholder="操作/方法" style="width: 200px" />
      </el-form-item>
      <el-form-item label="用户 ID">
        <el-input-number v-model="query.userId" :min="1" controls-position="right" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list" stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="用户" width="120" />
      <el-table-column prop="operation" label="操作" width="140" />
      <el-table-column prop="method" label="请求方法" min-width="180" />
      <el-table-column prop="params" label="参数" min-width="200" show-overflow-tooltip />
      <el-table-column prop="ip" label="IP" width="130" />
      <el-table-column prop="createTime" label="时间" width="180" />
    </el-table>

    <el-pagination
      v-model:current-page="query.pageNum"
      v-model:page-size="query.pageSize"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top: 16px; justify-content: flex-end"
      @current-change="loadData"
    />
  </el-card>
</template>
