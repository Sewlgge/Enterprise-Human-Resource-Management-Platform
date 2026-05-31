import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import api, { getToken, setToken, clearToken } from '@/services/api'
import { ROLES } from '@/utils/constants'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(getToken())
  const user = ref(null)

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === ROLES.ADMIN)

  async function login(username, password) {
    const t = await api.login(username, password)
    token.value = t
    setToken(t)
    await fetchUser()
  }

  async function register(data) {
    await api.register(data)
  }

  async function fetchUser() {
    user.value = await api.getUserInfo()
    return user.value
  }

  function clearSession() {
    token.value = null
    user.value = null
    clearToken()
  }

  async function logout() {
    try {
      if (token.value || getToken()) {
        await api.logout()
      }
    } catch {
      /* token 可能已失效，本地清会话即可 */
    } finally {
      clearSession()
    }
  }

  function initFromStorage() {
    token.value = getToken()
  }

  function setAvatar(avatar) {
    if (user.value) {
      user.value = { ...user.value, avatar }
    }
  }

  return { token, user, isLoggedIn, isAdmin, login, register, fetchUser, logout, clearSession, initFromStorage, setAvatar }
})
