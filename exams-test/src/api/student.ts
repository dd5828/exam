// 学生相关接口
import request from '@/utils/request'

export const getStudentList=(params: object)=>{
  return request({
    url: '/student/page',
    method: 'get',
    params: params
  })
}