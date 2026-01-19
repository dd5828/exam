import { userUserStore } from '@/stores'
import axios from 'axios'
import router from '@/router'
import { ElMessage } from 'element-plus'

const baseURL = '/api'

const instance = axios.create({
  // TODO 1. 基础地址，超时时间
  baseURL,
  timeout: 2000
})

//请求拦截器
instance.interceptors.request.use(
  (config) => {
    //判断是否上传图片，是则添加请求头
    // if (config.url === '/common/upload') {
    //   config.headers['Content-Type'] = 'multipart/form-data'
    // }
    // TODO 2. 携带token

    const userStore = userUserStore() //判断token是否存在
    if (userStore.token) {
      //给请求头添加token
      config.headers.token = userStore.token
    }
    return config
  },
  (err) => Promise.reject(err)
)

//相应拦截器
instance.interceptors.response.use(
  (res) => {
    // TODO 3. 处理业务失败
    // TODO 4. 摘取核心响应数据
    if (res.data.code === 1) {
      return res
    }

    ElMessage({ message: res.data.msg || '服务异常', type: 'error' })
    return Promise.reject(res.data)
  },
  (err) => {
    // TODO 5. 处理401错误
    ElMessage({ message: err.response.data.message || '登录过期66', type: 'error' })
    console.log(err)
    if (err.response?.status === 401) {
      router.push('/login')
    }
    return Promise.reject(err)
  }
)

export default instance

export { baseURL }
