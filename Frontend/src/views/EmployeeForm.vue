<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import api, { withMessage } from '@/services/api'

const router = useRouter()
const route = useRoute()
const id = route.params.id
const loading = ref(false)
const depts = ref([])
const positions = ref([])

const form = ref({
  realName: '',
  gender: 1,
  phone: '',
  email: '',
  deptId: null,
  positionId: null,
  age: 25,
  address: '',
  avatar: '',
  hireDate: '',
})

async function loadDepts() {
  depts.value = await api.deptList()
}

async function loadPositions(deptId) {
  positions.value = deptId ? await api.positionList({ deptId }) : []
}

watch(
  () => form.value.deptId,
  (deptId) => {
    form.value.positionId = null
    loadPositions(deptId)
  },
)

onMounted(async () => {
  await loadDepts()
  if (id) {
    loading.value = true
    try {
      const data = await api.getEmployee(id)
      Object.assign(form.value, data)
      if (data.hireDate) {
        form.value.hireDate = String(data.hireDate).slice(0, 19).replace('T', ' ')
      }
      await loadPositions(form.value.deptId)
    } finally {
      loading.value = false
    }
  }
})

async function submit() {
  loading.value = true
  try {
    if (id) {
      await withMessage(api.updateEmployee({ id: Number(id), ...form.value }), '更新成功')
    } else {
      await withMessage(api.addEmployee(form.value), '创建成功')
    }
    router.push('/employees')
  } catch {
    /* handled */
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <el-card v-loading="loading">
    <h3 style="margin-top: 0">{{ id ? '编辑员工' : '新建员工' }}</h3>
    <el-form :model="form" label-width="100px" style="max-width: 640px">
      <el-form-item label="姓名" required>
        <el-input v-model="form.realName" />
      </el-form-item>
      <el-form-item label="性别" required>
        <el-radio-group v-model="form.gender">
          <el-radio :value="1">男</el-radio>
          <el-radio :value="0">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="年龄" required>
        <el-input-number v-model="form.age" :min="18" :max="65" />
      </el-form-item>
      <el-form-item label="手机" required>
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item label="邮箱" required>
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="部门" required>
        <el-select v-model="form.deptId" placeholder="选择部门" style="width: 100%">
          <el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="职位" required>
        <el-select v-model="form.positionId" placeholder="选择职位" style="width: 100%" :disabled="!form.deptId">
          <el-option v-for="p in positions" :key="p.id" :label="p.name" :value="p.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="住址">
        <el-input v-model="form.address" />
      </el-form-item>
      <el-form-item label="入职时间">
        <el-date-picker
          v-model="form.hireDate"
          type="datetime"
          value-format="YYYY-MM-DD HH:mm:ss"
          placeholder="选择入职时间"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="头像 URL">
        <el-input v-model="form.avatar" placeholder="可选，或通过个人中心上传" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit">保存</el-button>
        <el-button @click="router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
