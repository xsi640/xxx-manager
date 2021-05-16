import { defineAsyncComponent } from 'vue'
import { createRouter, createWebHashHistory } from 'vue-router';
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import store from '../store';

const login = defineAsyncComponent(() => import('../views/login.vue'))
const main = defineAsyncComponent(() => import('../views/main.vue'))


const routes = [
  {
    path: '/',
    redirect: '/main'
  },
  {
    path: '/login',
    name: 'login',
    component: login
  },
  {
    path: '/:catchAll(.*)',
    name: 'main',
    component: main
  }, {
    path: '/404',
    component: defineAsyncComponent(() => import('../views/404.vue'))
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (to.path === '/login') {
    next()
  } else {
    if ((store.getters as any)['root/TOKEN']) {
      next()
    } else {
      next('/login')
    }
    NProgress.done()
  }
})

router.afterEach(() => {
  NProgress.done()
})

export default router;