<script setup>
import { reactive, ref } from 'vue'
import { updateUserPassword } from '@/api/user'
import { userUserStore } from '@/stores/index'
const userStore = userUserStore()
const passwordForm = reactive({
  id: userStore.id || '',
  oldPassword: '',
  newPassword: '',
  confirmNewPassword: ''
})

const rules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
  confirmNewPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value === '') {
          callback(new Error('确认新密码不能为空'))
        } else if (value !== passwordForm.newPassword) {
          callback(new Error('新密码和确认新密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

//修改前得预先校验

const form = ref()

const submitForm = async () => {
  await form.value.validate() //// 等待表单对象的 validate 方法执行完成，校验完成则继续执行
  //执行修改
  await updateUserPassword(passwordForm)
  ElMessage.success('修改成功')
}
</script>

<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>重置密码</span>
      </div>
    </template>
    <el-row>
      <el-col :span="12">
        <el-form :model="passwordForm" :rules="rules" ref="form" label-width="100px" size="large">
          <el-form-item label="原密码" prop="oldPassword">
            <el-input v-model="passwordForm.oldPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="passwordForm.newPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="确认新密码" prop="confirmNewPassword">
            <el-input v-model="passwordForm.confirmNewPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm">提交修改</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </el-card>
</template>

<style scoped>
.el-form-item__label {
  font-weight: bold;
}

.el-button {
  margin-top: 20px;
}
</style>
