//试卷模块相关接口
import request from '@/utils/request'

/**
 * 新增试卷
 * @param data
 */
export const AddTestPaper = (data: object) => {
  const num = request({
    url: '/test/paper/add',
    method: 'post',
    data: data
  })
  console.log('fangf', num)
  return num
}

/**
 * 试卷新增试题
 * @param data
 */
export const AddTestPaperQuestion = (data: object) => {
  return request({
    url: '/test/paper/question',
    method: 'post',
    data: data
  })
}

/**
 * 根据试卷id查询题型数量
 * @param id
 * @returns
 */
export const countQuestionTypesByPaperId = (id: number) => {
  return request({
    url: `/test/paper/getByIdBatch/${id}`,
    method: 'get'
  })
}

/**
 * 修改试卷
 * @param data
 * @returns
 */
export const updateTestPaper = (data: object) => {
  return request({
    url: '/test/paper/update',
    method: 'put',
    data: data
  })
}

/**
 * 修改题型分数
 * @param data
 * @returns
 */
export const updateTypeScore = (data: object) => {
  return request({
    url: '/test/paper/updateTypeScore',
    method: 'put',
    data: data
  })
}

/**
 *
 * @param data 试卷分页查询
 * @returns
 */
// export const getTestList = (data: object) => {
//   console.log('api', data)
//   return request({
//     url: '/test/paper/page',
//     method: 'get',
//     data: data
//   })
// }

export const getPageList = (params: object) => {
  console.log('api', params)
  return request({
    url: '/test/paper/page',
    method: 'get',
    params: params
  })
}

/**
 *根据id查询试卷详情
 * @param id
 */
export const getTestParticulars=(id:number)=>{
  return request({
    url:`/test/paper/getPaperBy/${id}`,
    method:'get'
  })
}
/**
 * 根据id删除试卷
 * @param id
 */
export const delectById=(id:number)=>{
  return request({
    url:`/test/paper/delete/${id}`,
    method:'delete'
  })
}

export const addQuestionsToPaper = (data:object) => {
  return request({
    url: '/testPaper/addQuestions',
    method: 'post',
    data
  })
}