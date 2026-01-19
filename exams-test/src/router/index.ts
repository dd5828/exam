import { createRouter, createWebHistory, createMemoryHistory } from 'vue-router'
import { userUserStore } from '@/stores/index'
//createRouter  创建路由实例
//配置history 模式
// 1.history模式： createWebHistory  地址栏不带#
// 2.hash模式 ：createMemoryHistory  地址栏带#
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login', component: () => import('@/views/login/loginPage.vue') },
    {
      path: '/',
      component: () => import('@/views/layout/LayoutContainer.vue'),
      redirect: '/user/profile',
      children: [
        {
          path: '/article/chart',
          component: () => import('@/views/chart/index.vue')
        },
        {
          path: '/article/test',
          component: () => import('@/views/test/index.vue')
        },
        {
          path: '/article/test/add',
          component: () => import('@/views/test/addTest.vue')
        },
        {
          path: '/article/test/particulars/:id', // 定义动态参数id
          component: () => import('@/views/test/testParticulars.vue')
        },
        {
          path: '/article/test/add-questions/', // 定义动态参数id
          component: () => import('@/views/test/addPaperQuestions.vue')
        },
        {
          path: '/article/test/update/:id', // 定义动态参数id
          component: () => import('@/views/test/updateTest.vue')
        },
        {
          path: '/article/question',
          component: () => import('@/views/question/index.vue')
        },
        {
          path: '/article/question/add',
          component: () => import('@/views/question/addQuestion.vue')
        },
        {
          path: '/user/profile',
          component: () => import('@/views/user/userProfile.vue')
        },
        {
          path: '/user/avatar',
          component: () => import('@/views/user/userAvatar.vue')
        },
        {
          path: '/user/password',
          component: () => import('@/views/user/userPassword.vue')
        },
        //答题详情
        {
          path: '/answer/preview',
          component: () => import('@/views/AnswerPreview/index.vue')
        }
      ]
    }
  ]
})

// 登录访问拦截
router.beforeEach((to) => {
  const userStore = userUserStore()
  if (!userStore.token && to.path !== '/login') return '/login'
})

export default router
