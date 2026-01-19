//题目请求模块
import request from '@/utils/request'
/**
 *
 * @param data 添加题目
 */
export const Addquestion = (data: object) => {
  console.log('打印999', data)
  request({
    url: '/question/add',
    method: 'post',
    data: data
  })
}

/**
 * 题目分页查询
 * @param data
 */
export const page = (params: any) => {
  //通过地址栏传参就要写params
  return request({
    url: '/question/page',
    method: 'get',
    params: params
  })
}

/**
 * 修改题目
 * @param data
 */
export const updateQuestion = (data: object) => {
  request({
    url: '/question/',
    method: 'put',
    data: data
  })
}

/**
 * 根据id获取题目详情
 * @param params
 * @returns
 */
export const getById = (id: number) => {
  return request({
    url: `/question/getById/${id}`,
    method: 'get'
  })
}

/**
 * 删除接口
 * @param ids
 * @returns
 */
export const deleteQuestion = (ids: string) => {
  return request({
    url: '/question',
    method: 'delete',
    params: { ids }
  })
}

export const getSumQuestion = () => {
  return request({
    url: '/question/getSumQuestion',
    method: 'get'
  })
}
