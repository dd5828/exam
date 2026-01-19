import { createApp } from 'vue'

import pinia from '@/stores/index'
import '@/assets/main.scss'

import Echarts from 'vue-echarts'
import * as echarts from 'echarts'

import App from './App.vue'
import router from './router'
import './assets/iconfont/iconfont.css' //// 引入阿里云字体图标css

const app = createApp(App)

app.use(pinia)
app.use(router)

app.component('v-chart', Echarts)

app.config.globalProperties.$echarts = echarts

app.mount('#app')
