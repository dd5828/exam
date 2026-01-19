//用户请求模块
import request from '@/utils/request'

// export const userRegisterService=({username:string,passwword:string repassword:string})=>
/**
 * 注册接口
 * @param data
 * @returns
 */
export const userRegisterService = (data: object) => {
  console.log('打印', data)
  request({
    url: '/admin/register',
    method: 'post',
    data: data
  })
}

/**
 * 登录
 * @param data
 * @returns
 */
export const userLoginService = (data: object) =>
  request({
    url: '/admin/login-a',
    method: 'post',
    data: data
  })

/**
 * 修改用户信息
 * @param data
 * @returns
 */
export const updateUser = (data: object) =>
  request({
    url: '/admin/update',
    method: 'put',
    data: data
  })

/**
 * 修改密码
 * @param data
 * @returns
 */
export const updateUserPassword = (data: object) =>
  request({
    url: '/admin/upPassword',
    method: 'put',
    data: data
  })

/**
 * 修改头像
 * @param flie
 * @returns
 */
export const userUploadAvatarService = (flie: any) => {
  return request({
    url: '/common/upload',
    method: 'post',
    data: flie
  })
}
