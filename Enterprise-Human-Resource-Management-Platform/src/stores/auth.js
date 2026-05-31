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

  async function register(username, password) {
    await api.register(username, password)
  }

  async function fetchUser() {
    user.value = await api.getUserInfo()
    return user.value
  }

  async function logout() {
    try {
      await api.logout()
    } finally {
      token.value = null
      user.value = null
      clearToken()
    }
  }

  function initFromStorage() {
    token.value = getToken()
  }

  return { token, user, isLoggedIn, isAdmin, login, register, fetchUser, logout, initFromStorage }
})
