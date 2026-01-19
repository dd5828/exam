<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref, watch } from 'vue'
import { userRegisterService, userLoginService } from '@/api/user'
import { userUserStore } from '@/stores/index'
import { useRouter } from 'vue-router'

const isRegister = ref(false) //登录注册切换

//绑定 form 数据对象
const formModel = ref({
  username: '',
  password: '',
  repassword: ''
})

//表单校验规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 10, message: '用户名必须是5-10位的字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码必须是6-15位的非空字符',
      trigger: 'blur'
    }
  ],
  repassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码必须是6-15的非空字符',
      trigger: 'blur'
    },
    {
      validator: (rule, value, callback) => {
        if (value !== formModel.value.password) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

//注册前的预先校验
const form = ref()

const register = async () => {
  await form.value.validate() //// 等待表单对象的 validate 方法执行完成，校验完成则继续执行
  const userfrom = { studentId: formModel.value.username, password: formModel.value.password } //构造登录数据
  await userRegisterService(userfrom)
  ElMessage.success('注册成功')
  isRegister.value = false //切换登录
}

//监听登录注册切换，切换时重置表单内容
watch(isRegister, () => {
  formModel.value = {
    username: '',
    password: '',
    repassword: ''
  }
})

const userStore = userUserStore() //存储token
const router = useRouter() //路由切换

//登录前的预校验
const login = async () => {
  await form.value.validate() //校验通过则执行以下方法
  const userfrom = { studentId: formModel.value.username, password: formModel.value.password } //构造登录数据
  console.log(userfrom)
  const res = await userLoginService(userfrom)
  console.log(res.data.data.id)
  userStore.setToken(res.data.data.token) //存入token
  userStore.setId(res.data.data.id) //存入id
  userStore.setPhone(res.data.data.phone)
  userStore.setName(res.data.data.name) //存入昵称
  userStore.setStudentId(res.data.data.studentId) //存入工号
  ElMessage.success('登录成功')
  router.push('/')
}
</script>

<template>
  <div class="login-container">
    <el-row class="login-page">
      <!-- 左侧背景区域 -->
      <el-col :span="14" class="left-section">
        <div class="background-overlay"></div>
        <div class="content">
          <div class="logo-area">
            <img src="@/assets/logo2.png" alt="Logo" class="logo" />
          </div>
          <div class="welcome-text">
            <h1>欢迎使用系统</h1>
          </div>
        </div>
      </el-col>

      <!-- 右侧表单区域 -->
      <el-col :span="10" class="right-section">
        <div class="form-container">
          <!-- 注册表单 -->
          <el-form
            ref="form"
            size="large"
            autocomplete="off"
            :model="formModel"
            :rules="rules"
            v-if="isRegister"
            class="auth-form"
          >
            <div class="form-header">
              <h1>创建账户</h1>
              <p>注册新账号，开始使用我们的服务</p>
            </div>

            <el-form-item prop="username">
              <el-input
                :prefix-icon="User"
                v-model="formModel.username"
                placeholder="请输入用户名"
                size="large"
              ></el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                :prefix-icon="Lock"
                v-model="formModel.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                show-password
              ></el-input>
            </el-form-item>

            <el-form-item prop="repassword">
              <el-input
                :prefix-icon="Lock"
                v-model="formModel.repassword"
                type="password"
                placeholder="请再次输入密码"
                size="large"
                show-password
              ></el-input>
            </el-form-item>

            <el-form-item>
              <el-button
                class="submit-button"
                type="primary"
                @click="register"
                auto-insert-space
              >
                注册
              </el-button>
            </el-form-item>

            <div class="form-footer">
              <span>已有账号？</span>
              <el-link
                type="primary"
                :underline="false"
                @click="isRegister = false"
                class="auth-switch"
              >
                立即登录
              </el-link>
            </div>
          </el-form>

          <!-- 登录表单 -->
          <el-form
            ref="form"
            size="large"
            autocomplete="off"
            :model="formModel"
            :rules="rules"
            v-else
            class="auth-form"
          >
            <div class="form-header">
              <h1>欢迎回来</h1>
              <p>请登录您的账户继续使用</p>
            </div>

            <el-form-item prop="username">
              <el-input
                :prefix-icon="User"
                v-model="formModel.username"
                placeholder="请输入用户名"
                size="large"
              ></el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                name="password"
                :prefix-icon="Lock"
                v-model="formModel.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                show-password
              ></el-input>
            </el-form-item>

            <el-form-item class="remember-forgot">
              <div class="flex">
                <el-checkbox>记住我</el-checkbox>
                <el-link type="primary" :underline="false">忘记密码？</el-link>
              </div>
            </el-form-item>

            <el-form-item>
              <el-button
                class="submit-button"
                type="primary"
                @click="login"
                auto-insert-space
              >
                登录
              </el-button>
            </el-form-item>

            <div class="form-footer">
              <span>还没有账号？</span>
              <el-link
                type="primary"
                :underline="false"
                @click="isRegister = true"
                class="auth-switch"
              >
                立即注册
              </el-link>
            </div>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
}

.login-page {
  height: 100%;
  margin: 0;
}

// 左侧背景区域
.left-section {
  position: relative;
  background:
    linear-gradient(135deg, rgba(74, 144, 226, 0.9) 0%, rgba(88, 81, 219, 0.9) 100%),
    url('@/assets/login_bg.jpg') no-repeat center / cover;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;

  .background-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.3);
  }

  .content {
    position: relative;
    z-index: 1;
    text-align: center;
    max-width: 80%;

    .logo-area {
      margin-bottom: 40px;

      .logo {
        max-width: 180px;
        height: auto;
        filter: brightness(0) invert(1);
      }
    }

    .welcome-text {
      h1 {
        font-size: 2.5rem;
        margin-bottom: 16px;
        font-weight: 600;
      }

      p {
        font-size: 1.2rem;
        opacity: 0.9;
      }
    }
  }
}

// 右侧表单区域
.right-section {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8fafc;

  .form-container {
    width: 100%;
    max-width: 400px;
    padding: 0 24px;

    .auth-form {
      background: white;
      padding: 40px 32px;
      border-radius: 12px;
      box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);

      .form-header {
        text-align: center;
        margin-bottom: 32px;

        h1 {
          font-size: 1.8rem;
          font-weight: 600;
          color: #1f2937;
          margin-bottom: 8px;
        }

        p {
          color: #6b7280;
          font-size: 0.95rem;
        }
      }

      :deep(.el-form-item) {
        margin-bottom: 24px;
      }

      :deep(.el-input) {
        .el-input__wrapper {
          border-radius: 8px;
          padding: 12px 16px;
          box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);

          &:hover {
            box-shadow: 0 1px 5px rgba(0, 0, 0, 0.15);
          }

          &.is-focus {
            box-shadow: 0 0 0 1px #409eff, 0 1px 5px rgba(0, 0, 0, 0.1);
          }
        }
      }

      .remember-forgot {
        margin-bottom: 8px;

        .flex {
          width: 100%;
          display: flex;
          justify-content: space-between;
          align-items: center;
        }
      }

      .submit-button {
        width: 100%;
        height: 48px;
        border-radius: 8px;
        font-size: 1rem;
        font-weight: 500;
        background: linear-gradient(135deg, #4a90e2, #5851db);
        border: none;
        margin-top: 8px;

        &:hover {
          transform: translateY(-1px);
          box-shadow: 0 4px 12px rgba(74, 144, 226, 0.3);
        }
      }

      .form-footer {
        text-align: center;
        margin-top: 24px;
        padding-top: 20px;
        border-top: 1px solid #e5e7eb;
        color: #6b7280;
        font-size: 0.9rem;

        .auth-switch {
          margin-left: 6px;
          font-weight: 500;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .left-section {
    display: none;
  }

  .right-section {
    .form-container {
      max-width: 100%;
      padding: 0 16px;

      .auth-form {
        padding: 32px 24px;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
      }
    }
  }
}

// 动画效果
.auth-form {
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>