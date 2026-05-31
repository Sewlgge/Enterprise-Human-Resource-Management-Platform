export const TOKEN_KEY = 'hr_token'

export const ROLES = {
  ADMIN: 0,
  EMPLOYEE: 1,
}

export const GENDER_MAP = { 0: '女', 1: '男' }

export const ATTENDANCE_STATUS = {
  0: '正常',
  1: '迟到',
  2: '早退',
  3: '缺卡',
  4: '请假',
}

export const LEAVE_STATUS = {
  0: '待审批',
  1: '已通过',
  2: '已拒绝',
}

export const NOTICE_STATUS = {
  0: '草稿',
  1: '已发布',
}

export const LEAVE_TYPES = ['事假', '病假', '年假', '婚假', '产假', '调休']

export function attendanceTagType(status) {
  const map = { 0: 'success', 1: 'warning', 2: 'warning', 3: 'danger', 4: 'info' }
  return map[status] || 'info'
}

export function leaveTagType(status) {
  const map = { 0: 'warning', 1: 'success', 2: 'danger' }
  return map[status] || 'info'
}
