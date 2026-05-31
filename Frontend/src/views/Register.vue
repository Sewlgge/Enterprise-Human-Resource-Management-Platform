<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import api, { withMessage } from '@/services/api'
import AuthShell from '@/components/AuthShell.vue'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const depts = ref([])
const positions = ref([])

const form = ref({
  username: '',
  password: '',
  confirm: '',
  realName: '',
  phone: '',
  gender: 1,
  email: '',
  deptId: null,
  positionId: null,
  age: 25,
  address: '',
})

async function loadDepts() {
  try {
    depts.value = (await api.deptListPublic()) || []
  } catch (err) {
    ElMessage.error(err.message || '加载部门失败')
  }
}

async function loadPositions(deptId) {
  if (!deptId) {
    positions.value = []
    return
  }
  try {
    positions.value = await api.positionListPublic({ deptId })
  } catch (err) {
    ElMessage.error(err.message || '加载职位失败')
    positions.value = []
  }
}

watch(
  () => form.value.deptId,
  (deptId) => {
    form.value.positionId = null
    loadPositions(deptId)
  },
)

onMounted(loadDepts)

async function submit() {
  if (form.value.password !== form.value.confirm) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }

  loading.value = true
  try {
    await withMessage(
      auth.register({
        username: form.value.username,
        password: form.value.password,
        realName: form.value.realName,
        phone: form.value.phone,
        gender: form.value.gender,
        email: form.value.email,
        deptId: form.value.deptId,
        positionId: form.value.positionId,
        age: form.value.age,
        address: form.value.address,
      }),
      '注册成功，请登录',
    )
    router.push('/login')
  } catch {
    /* handled */
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <AuthShell title="创建账号" subtitle="填写账号与员工信息，一键完成注册">
    <el-form :model="form" label-position="top" @submit.prevent="submit">
      <div class="section-label">账号信息</div>
      <el-form-item label="用户名">
        <el-input v-model="form.username" placeholder="4-16 位字符" />
      </el-form-item>
      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="密码">
            <el-input v-model="form.password" type="password" show-password placeholder="4-16 位" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="确认密码">
            <el-input v-model="form.confirm" type="password" show-password />
          </el-form-item>
        </el-col>
      </el-row>

      <div class="section-label">员工信息</div>
      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="真实姓名">
            <el-input v-model="form.realName" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号">
            <el-input v-model="form.phone" placeholder="11 位手机号" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="性别">
            <el-radio-group v-model="form.gender">
              <el-radio :value="1">男</el-radio>
              <el-radio :value="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年龄">
            <el-input-number v-model="form.age" :min="18" :max="65" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" placeholder="name@company.com" />
      </el-form-item>
      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="部门">
            <el-select v-model="form.deptId" placeholder="选择部门" style="width: 100%">
              <el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职位">
            <el-select
              v-model="form.positionId"
              placeholder="选择职位"
              style="width: 100%"
              :disabled="!form.deptId"
            >
              <el-option v-for="p in positions" :key="p.id" :label="p.name" :value="p.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="住址">
        <el-input v-model="form.address" placeholder="选填" />
      </el-form-item>

      <el-button type="primary" :loading="loading" native-type="submit" class="submit-btn">
        注册
      </el-button>
      <div class="footer">
        已有账号？
        <router-link to="/login">返回登录</router-link>
      </div>
    </el-form>
  </AuthShell>
</template>

<style scoped>
.section-label {
  font-size: 13px;
  font-weight: 600;
  color: var(--hr-primary);
  letter-spacing: 0.04em;
  margin: 8px 0 12px;
  text-transform: uppercase;
}

.submit-btn {
  width: 100%;
  margin-top: 12px;
}

.footer {
  margin-top: 20px;
  text-align: center;
  color: var(--hr-text-secondary);
  font-size: 14px;
}

.footer a {
  color: var(--hr-primary);
  text-decoration: none;
  font-weight: 500;
}

.footer a:hover {
  text-decoration: underline;
}

:deep(.el-form-item__label) {
  color: #334155;
  font-weight: 500;
}
</style>
