<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>基本资料</span>
      </div>
    </template>
    <el-row>
      <el-col :span="12">
        <el-form :model="userInfo" :rules="rules" ref="formRef" label-width="100px" size="large">
          <el-form-item label="登录名称">
            <el-input v-model="userInfo.studentId" disabled></el-input>
          </el-form-item>
          <el-form-item label="用户昵称" prop="name">
            <el-input v-model="userInfo.name"></el-input>
          </el-form-item>
          <el-form-item label="用户手机号" prop="phoneNumber">
            <el-input v-model="userInfo.phoneNumber"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm">提交修改</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </el-card>
</template>

<script setup>
import { reactive, ref } from 'vue'

import { updateUser } from '@/api/user'

import { userUserStore } from '@/stores/index'

const formRef = ref(null)

const userStore = userUserStore()

const userInfo = reactive({
  studentId: userStore.studentId,
  name: userStore.name || '',
  phoneNumber: userStore.phone || ''
})

const rules = {
  name: [{ required: true, message: '请输入用户昵称', trigger: 'blur' }],
  phoneNumber: [
    { required: true, message: '请输入用户手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
}

//修改前得预先校验

const submitForm = async () => {
  console.log(userStore.id)
  await formRef.value.validate() //formRef.value.validate()是用于触发表单验证的方法
  //构造登录所需参数
  const id = userStore.id
  const userUpdate = { id: id, name: userInfo.name, phone: userInfo.phoneNumber }
  const res = await updateUser(userUpdate)
  userStore.name = userInfo.name
  userStore.phone = userInfo.phoneNumber
  ElMessage.success('修改成功')
}
// const submitForm = () => {
//     formRef.value.validate((valid) => {
//         if (valid) {
//             // 表单验证通过，执行提交逻辑
//             console.log(userInfo)
//         } else {
//             // 表单验证不通过
//             console.log('表单验证不通过')
//         }
//     })
// }
</script>

<style scoped>
.el-form-item__label {
  font-weight: bold;
}

.el-button {
  margin-top: 20px;
}
</style>
