import { defineAsyncComponent } from 'vue'
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
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
  NProgress.start()
  if (to.path === '/login') {
    next()
  } else {
    console.log((store.getters)['root/token'])
    if ((store.getters)['root/token']) {
      next()
    } else {
      next('/login')
    }
  }
  NProgress.done()
})

router.afterEach(() => {
  NProgress.done()
})

export default router;