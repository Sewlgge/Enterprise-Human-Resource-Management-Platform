import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const MainLayout = () => import('@/layouts/MainLayout.vue')

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { public: true },
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { public: true },
  },
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '', redirect: '/dashboard' },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { admin: true, title: '仪表盘' },
      },
      {
        path: 'employees',
        name: 'EmployeeList',
        component: () => import('@/views/EmployeeList.vue'),
        meta: { title: '员工管理' },
      },
      {
        path: 'employees/create',
        name: 'EmployeeCreate',
        component: () => import('@/views/EmployeeForm.vue'),
        meta: { admin: true, title: '新建员工' },
      },
      {
        path: 'employees/:id',
        name: 'EmployeeDetails',
        component: () => import('@/views/EmployeeDetails.vue'),
        props: true,
        meta: { title: '员工详情' },
      },
      {
        path: 'employees/:id/edit',
        name: 'EmployeeEdit',
        component: () => import('@/views/EmployeeForm.vue'),
        props: true,
        meta: { admin: true, title: '编辑员工' },
      },
      {
        path: 'depts',
        name: 'DeptList',
        component: () => import('@/views/DeptList.vue'),
        meta: { admin: true, title: '部门管理' },
      },
      {
        path: 'positions',
        name: 'PositionList',
        component: () => import('@/views/PositionList.vue'),
        meta: { admin: true, title: '职位管理' },
      },
      {
        path: 'attendance/my',
        name: 'MyAttendance',
        component: () => import('@/views/MyAttendance.vue'),
        meta: { title: '我的考勤' },
      },
      {
        path: 'attendance/manage',
        name: 'AttendanceManage',
        component: () => import('@/views/AttendanceManage.vue'),
        meta: { admin: true, title: '考勤管理' },
      },
      {
        path: 'leave/my',
        name: 'MyLeave',
        component: () => import('@/views/MyLeave.vue'),
        meta: { title: '我的请假' },
      },
      {
        path: 'leave/manage',
        name: 'LeaveManage',
        component: () => import('@/views/LeaveManage.vue'),
        meta: { admin: true, title: '请假审批' },
      },
      {
        path: 'notices',
        name: 'NoticeList',
        component: () => import('@/views/NoticeList.vue'),
        meta: { title: '公告通知' },
      },
      {
        path: 'notices/:id',
        name: 'NoticeDetail',
        component: () => import('@/views/NoticeDetail.vue'),
        props: true,
        meta: { title: '公告详情' },
      },
      {
        path: 'logs',
        name: 'LogList',
        component: () => import('@/views/LogList.vue'),
        meta: { admin: true, title: '操作日志' },
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人中心' },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach(async (to) => {
  const auth = useAuthStore()

  if (to.meta.public) {
    return true
  }

  if (!auth.token) return '/login'

  if (!auth.user) {
    try {
      await auth.fetchUser()
    } catch {
      auth.clearSession()
      return '/login'
    }
  }

  if (to.meta.admin && !auth.isAdmin) {
    return '/notices'
  }

  return true
})

export default router
