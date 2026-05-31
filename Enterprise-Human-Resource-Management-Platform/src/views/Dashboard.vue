<script setup>
import { onMounted, ref } from 'vue'
import api from '@/services/api'

const loading = ref(true)
const dashboard = ref(null)

onMounted(async () => {
  try {
    dashboard.value = await api.getDashboard()
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div v-loading="loading">
    <template v-if="dashboard">
      <el-row :gutter="16" class="stats-row">
        <el-col :span="6">
          <el-card shadow="hover">
            <el-statistic title="员工总数" :value="dashboard.summary?.employeeCount || 0" />
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <el-statistic title="部门总数" :value="dashboard.summary?.deptCount || 0" />
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <el-statistic title="昨日正常考勤" :value="dashboard.summary?.yesterdayAttendance || 0" />
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <el-statistic title="待审批请假" :value="dashboard.summary?.pendingLeave || 0" />
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="16" style="margin-top: 16px">
        <el-col :span="12">
          <el-card header="本月考勤统计">
            <el-descriptions :column="2" border v-if="dashboard.monthAttendance?.summary">
              <el-descriptions-item label="正常">
                {{ dashboard.monthAttendance.summary.normal || 0 }}
              </el-descriptions-item>
              <el-descriptions-item label="迟到">
                {{ dashboard.monthAttendance.summary.late || 0 }}
              </el-descriptions-item>
              <el-descriptions-item label="早退">
                {{ dashboard.monthAttendance.summary.earlyLeave || 0 }}
              </el-descriptions-item>
              <el-descriptions-item label="缺卡">
                {{ dashboard.monthAttendance.summary.missing || 0 }}
              </el-descriptions-item>
              <el-descriptions-item label="请假">
                {{ dashboard.monthAttendance.summary.leaveCount || 0 }}
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card header="本月请假统计">
            <el-descriptions :column="2" border v-if="dashboard.monthLeave?.summary">
              <el-descriptions-item label="待审批">
                {{ dashboard.monthLeave.summary.pending || 0 }}
              </el-descriptions-item>
              <el-descriptions-item label="已通过">
                {{ dashboard.monthLeave.summary.approved || 0 }}
              </el-descriptions-item>
              <el-descriptions-item label="已拒绝">
                {{ dashboard.monthLeave.summary.rejected || 0 }}
              </el-descriptions-item>
              <el-descriptions-item label="总计">
                {{ dashboard.monthLeave.summary.total || 0 }}
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="16" style="margin-top: 16px">
        <el-col :span="12">
          <el-card header="各部门员工人数">
            <el-table :data="dashboard.deptEmployeeCounts || []" size="small">
              <el-table-column prop="deptName" label="部门" />
              <el-table-column prop="employeeCount" label="人数" width="100" />
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card header="请假类型分布">
            <el-table :data="dashboard.monthLeave?.typeStats || []" size="small">
              <el-table-column prop="type" label="类型" />
              <el-table-column prop="count" label="次数" width="100" />
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </template>
  </div>
</template>

<style scoped>
.stats-row :deep(.el-statistic__number) {
  font-size: 28px;
}
</style>
