//题目分类请求模块
import request from '@/utils/request'

export const categoryList = () => {
  return request({
    url: '/category/list',
    method: 'get'
  })
}
