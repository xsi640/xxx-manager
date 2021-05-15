import { defineAsyncComponent } from 'vue'
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import { store } from '../store';

const login = defineAsyncComponent(() => import('../views/login.vue'))
const main = defineAsyncComponent(() => import('../views/main.vue'))

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'main',
    component: main
  },
  {
    path: '/login',
    name: 'login',
    component: login
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    next()
  } else {
    if (store.getters.token != '') {
      next()
    } else {
      next('/login')
    }
  }
})

export default router;