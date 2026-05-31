<script setup>
defineProps({
  title: { type: String, required: true },
  subtitle: { type: String, default: '' },
})
</script>

<template>
  <div class="auth-shell">
    <aside class="auth-brand">
      <div class="brand-inner">
        <div class="brand-mark">HR</div>
        <h1>企业人事管理平台</h1>
        <p>考勤 · 请假 · 组织 · 协同</p>
        <ul class="brand-points">
          <li>一站式员工生命周期管理</li>
          <li>注册即创建员工档案</li>
          <li>安全可靠的账号体系</li>
        </ul>
      </div>
    </aside>
    <main class="auth-main">
      <div class="auth-panel">
        <header class="auth-header">
          <h2>{{ title }}</h2>
          <p v-if="subtitle">{{ subtitle }}</p>
        </header>
        <slot />
      </div>
    </main>
  </div>
</template>

<style scoped>
.auth-shell {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 1fr 1.1fr;
  background: var(--hr-bg);
}

.auth-brand {
  background: linear-gradient(145deg, var(--hr-primary) 0%, var(--hr-sidebar-from) 55%, var(--hr-sidebar-to) 100%);
  color: #ecfdf5;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px;
}

.brand-inner {
  max-width: 360px;
}

.brand-mark {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.14);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 20px;
  letter-spacing: 1px;
  margin-bottom: 28px;
}

.brand-inner h1 {
  margin: 0 0 12px;
  font-size: 28px;
  font-weight: 600;
  line-height: 1.3;
}

.brand-inner > p {
  margin: 0 0 32px;
  color: rgba(236, 253, 245, 0.78);
  font-size: 15px;
}

.brand-points {
  margin: 0;
  padding: 0;
  list-style: none;
}

.brand-points li {
  position: relative;
  padding-left: 18px;
  margin-bottom: 14px;
  color: rgba(236, 253, 245, 0.9);
  font-size: 14px;
  line-height: 1.6;
}

.brand-points li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 9px;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--hr-primary-light);
}

.auth-main {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
}

.auth-panel {
  width: 100%;
  max-width: 480px;
  background: #fff;
  border-radius: 20px;
  padding: 36px 40px 32px;
  box-shadow:
    0 1px 2px rgba(15, 23, 42, 0.04),
    0 12px 40px rgba(15, 23, 42, 0.08);
  border: 1px solid var(--hr-border);
}

.auth-header h2 {
  margin: 0 0 8px;
  font-size: 24px;
  color: var(--hr-text);
  font-weight: 600;
}

.auth-header p {
  margin: 0 0 28px;
  color: var(--hr-text-secondary);
  font-size: 14px;
}

@media (max-width: 900px) {
  .auth-shell {
    grid-template-columns: 1fr;
  }

  .auth-brand {
    padding: 32px 24px;
  }

  .brand-points {
    display: none;
  }
}
</style>
