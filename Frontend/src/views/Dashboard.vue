<script setup>
import { onMounted, onUnmounted, ref, nextTick } from 'vue'
import * as echarts from 'echarts'
import api from '@/services/api'

const loading = ref(true)
const dashboard = ref(null)

const attendancePieRef = ref(null)
const dailyTrendRef = ref(null)
const deptBarRef = ref(null)
const leavePieRef = ref(null)

const charts = []

const CHART_COLORS = ['#0f766e', '#14b8a6', '#5eead4', '#f59e0b', '#ef4444', '#6366f1']

function mountChart(el, option) {
  if (!el) return
  const chart = echarts.init(el)
  chart.setOption(option)
  charts.push(chart)
}

function buildAttendancePie(summary) {
  if (!summary) return
  mountChart(attendancePieRef.value, {
    color: CHART_COLORS,
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { bottom: 0 },
    series: [
      {
        type: 'pie',
        radius: ['42%', '68%'],
        data: [
          { name: '正常', value: summary.normal || 0 },
          { name: '迟到', value: summary.late || 0 },
          { name: '早退', value: summary.earlyLeave || 0 },
          { name: '缺卡', value: summary.missing || 0 },
          { name: '请假', value: summary.leaveCount || 0 },
        ].filter((item) => item.value > 0),
      },
    ],
  })
}

function buildDailyTrend(dailyStats = []) {
  const dates = dailyStats.map((item) => String(item.date).slice(5))
  mountChart(dailyTrendRef.value, {
    color: CHART_COLORS,
    tooltip: { trigger: 'axis' },
    legend: { data: ['正常', '异常'], bottom: 0 },
    grid: { left: 40, right: 20, top: 30, bottom: 50 },
    xAxis: { type: 'category', data: dates, boundaryGap: false },
    yAxis: { type: 'value', minInterval: 1 },
    series: [
      {
        name: '正常',
        type: 'line',
        smooth: true,
        areaStyle: { opacity: 0.15 },
        data: dailyStats.map((item) => item.normal || 0),
      },
      {
        name: '异常',
        type: 'line',
        smooth: true,
        areaStyle: { opacity: 0.1 },
        data: dailyStats.map((item) => item.abnormal || 0),
      },
    ],
  })
}

function buildDeptBar(deptList = []) {
  mountChart(deptBarRef.value, {
    color: [CHART_COLORS[0]],
    tooltip: { trigger: 'axis' },
    grid: { left: 40, right: 20, top: 20, bottom: 40 },
    xAxis: {
      type: 'category',
      data: deptList.map((item) => item.deptName),
      axisLabel: { rotate: deptList.length > 5 ? 25 : 0 },
    },
    yAxis: { type: 'value', minInterval: 1 },
    series: [
      {
        type: 'bar',
        barMaxWidth: 36,
        itemStyle: { borderRadius: [6, 6, 0, 0] },
        data: deptList.map((item) => item.employeeCount || 0),
      },
    ],
  })
}

function buildLeavePie(typeStats = []) {
  mountChart(leavePieRef.value, {
    color: CHART_COLORS,
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { bottom: 0 },
    series: [
      {
        type: 'pie',
        radius: '62%',
        data: typeStats.map((item) => ({ name: item.type, value: item.count || 0 })),
      },
    ],
  })
}

function renderCharts() {
  while (charts.length) {
    charts.pop().dispose()
  }
  if (!dashboard.value) return

  buildAttendancePie(dashboard.value.monthAttendance?.summary)
  buildDailyTrend(dashboard.value.monthAttendance?.dailyStats || [])
  buildDeptBar(dashboard.value.deptEmployeeCounts || [])
  buildLeavePie(dashboard.value.monthLeave?.typeStats || [])
}

function handleResize() {
  charts.forEach((chart) => chart.resize())
}

onMounted(async () => {
  window.addEventListener('resize', handleResize)
  try {
    dashboard.value = await api.getDashboard()
    await nextTick()
    renderCharts()
  } finally {
    loading.value = false
  }
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  charts.forEach((chart) => chart.dispose())
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

      <el-row :gutter="16" class="chart-row">
        <el-col :span="12">
          <el-card header="本月考勤状态分布">
            <div ref="attendancePieRef" class="chart-box" />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card header="本月每日考勤趋势">
            <div ref="dailyTrendRef" class="chart-box" />
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="16" class="chart-row">
        <el-col :span="12">
          <el-card header="各部门员工人数">
            <div ref="deptBarRef" class="chart-box" />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card header="本月请假类型分布">
            <div ref="leavePieRef" class="chart-box" />
          </el-card>
        </el-col>
      </el-row>
    </template>
  </div>
</template>

<style scoped>
.stats-row :deep(.el-statistic__number) {
  font-size: 28px;
  color: var(--hr-primary);
}

.chart-row {
  margin-top: 16px;
}

.chart-box {
  height: 320px;
}
</style>
