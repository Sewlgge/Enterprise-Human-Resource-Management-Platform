import { ElMessage } from 'element-plus'
import { TOKEN_KEY } from '@/utils/constants'

const BASE = import.meta.env.VITE_API_BASE || '/api'

let onUnauthorized = () => {
  localStorage.removeItem(TOKEN_KEY)
  window.location.href = '/login'
}

export function setUnauthorizedHandler(fn) {
  onUnauthorized = fn
}

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

export function clearToken() {
  localStorage.removeItem(TOKEN_KEY)
}

function buildQuery(params = {}) {
  const search = new URLSearchParams()
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      search.append(key, value)
    }
  })
  const qs = search.toString()
  return qs ? `?${qs}` : ''
}

async function request(path, options = {}) {
  const { method = 'GET', params, json, form, skipAuth, raw } = options
  const headers = { ...(options.headers || {}) }

  if (!skipAuth) {
    const token = getToken()
    if (token) headers.Authorization = token
  }

  let url = BASE + path
  let body

  if (params && method === 'GET') {
    url += buildQuery(params)
  } else if (json !== undefined) {
    headers['Content-Type'] = 'application/json'
    body = JSON.stringify(json)
  } else if (form) {
    headers['Content-Type'] = 'application/x-www-form-urlencoded'
    body = new URLSearchParams(form).toString()
  } else if (params && method !== 'GET') {
    url += buildQuery(params)
  }

  const res = await fetch(url, { method, headers, body })

  if (res.status === 401) {
    onUnauthorized()
    throw new Error('登录已过期，请重新登录')
  }

  if (raw) {
    if (!res.ok) throw new Error(await res.text() || res.statusText)
    return res
  }

  const contentType = res.headers.get('content-type') || ''
  const data = contentType.includes('application/json') ? await res.json() : await res.text()

  if (typeof data === 'object' && data !== null && 'code' in data) {
    if (data.code !== 0) {
      throw new Error(data.message || '请求失败')
    }
    return data.data
  }

  if (!res.ok) {
    throw new Error(typeof data === 'string' ? data : res.statusText)
  }

  return data
}

export async function uploadAvatar(file) {
  const formData = new FormData()
  formData.append('file', file)
  const token = getToken()
  const res = await fetch(`${BASE}/employee/upload/avatar`, {
    method: 'POST',
    headers: token ? { Authorization: token } : {},
    body: formData,
  })

  if (res.status === 401) {
    onUnauthorized()
    throw new Error('登录已过期，请重新登录')
  }

  const data = await res.json()
  if (data.code !== 0) throw new Error(data.message || '上传失败')
  return data.data
}

export const api = {
  // 用户
  register(username, password) {
    return request('/user/register', { method: 'POST', form: { username, password }, skipAuth: true })
  },
  login(username, password) {
    return request('/user/login', { method: 'POST', form: { username, password }, skipAuth: true })
  },
  logout() {
    return request('/user/logout', { method: 'POST' })
  },
  getUserInfo() {
    return request('/user/info', { method: 'POST' })
  },
  updatePassword(oldPassword, newPassword) {
    return request('/user/update', { method: 'POST', form: { oldPassword, newPassword } })
  },

  // 仪表盘
  getDashboard() {
    return request('/dashborad')
  },

  // 员工
  employeePage(params) {
    return request('/employee/page', { params })
  },
  getEmployee(id) {
    return request(`/employee/get/${id}`)
  },
  addEmployee(json) {
    return request('/employee/add', { method: 'POST', json })
  },
  updateEmployee(json) {
    return request('/employee/update', { method: 'PUT', json })
  },
  deleteEmployee(id) {
    return request(`/employee/delete/${id}`, { method: 'DELETE' })
  },

  // 部门
  deptPage(params) {
    return request('/dept/page', { params })
  },
  deptList(name) {
    return request('/dept/list', { params: { name } })
  },
  getDept(id) {
    return request(`/dept/get/${id}`)
  },
  addDept(name, description) {
    return request('/dept/add', { method: 'POST', form: { name, description } })
  },
  updateDept(id, name, description) {
    return request('/dept/update', { method: 'PUT', params: { id, name, description } })
  },
  deleteDept(id) {
    return request(`/dept/delete/${id}`, { method: 'DELETE' })
  },

  // 职位
  positionPage(params) {
    return request('/position/page', { params })
  },
  positionList(params) {
    return request('/position/list', { params })
  },
  getPosition(id) {
    return request(`/position/${id}`)
  },
  addPosition(json) {
    return request('/position', { method: 'POST', json })
  },
  updatePosition(json) {
    return request('/position/update', { method: 'PUT', json })
  },
  deletePosition(id) {
    return request(`/position/delete/${id}`, { method: 'DELETE' })
  },

  // 考勤
  signIn() {
    return request('/attendance/signin', { method: 'POST' })
  },
  signOut() {
    return request('/attendance/signout', { method: 'POST' })
  },
  myAttendance(month) {
    return request('/attendance/my', { params: { month } })
  },
  attendancePage(params) {
    return request('/attendance/page', { params })
  },
  updateAttendance(json) {
    return request('/attendance/update', { method: 'PUT', json })
  },

  // 请假
  applyLeave(form) {
    return request('/leave', { method: 'POST', form })
  },
  myLeave() {
    return request('/leave/my')
  },
  leaveDetail(id) {
    return request(`/leave/${id}`)
  },
  leavePage(params) {
    return request('/leave/page', { params })
  },
  approveLeave(id, status, remark) {
    return request(`/leave/approver/${id}`, { method: 'PUT', params: { status, remark } })
  },

  // 公告
  noticePage(params) {
    return request('/notice/page', { params })
  },
  noticeDetail(id) {
    return request(`/notice/${id}`)
  },
  publishNotice(json) {
    return request('/notice/publish', { method: 'POST', json })
  },
  deleteNotice(id) {
    return request(`/notice/${id}`, { method: 'DELETE' })
  },

  // 日志
  logPage(params) {
    return request('/log/page', { params })
  },
}

export async function withMessage(promise, successMsg) {
  try {
    const data = await promise
    if (successMsg) ElMessage.success(successMsg)
    return data
  } catch (err) {
    ElMessage.error(err.message || '操作失败')
    throw err
  }
}

export default api
