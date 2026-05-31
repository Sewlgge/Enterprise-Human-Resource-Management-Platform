import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import App from './App.vue'
import router from './router'
import { setUnauthorizedHandler } from './services/api'
import { useAuthStore } from './stores/auth'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.use(ElementPlus)

setUnauthorizedHandler(() => {
  const auth = useAuthStore()
  auth.clearSession()
  if (!router.currentRoute.value.meta.public) {
    router.push('/login')
  }
})

app.mount('#app')
