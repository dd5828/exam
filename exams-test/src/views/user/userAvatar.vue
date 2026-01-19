<script setup>
import { ref } from 'vue'
import { Plus, Upload } from '@element-plus/icons-vue'
import { userUploadAvatarService, updateUser } from '@/api/user'
import { userUserStore } from '@/stores'

const userStore = userUserStore()

const imgUrl = ref(userStore.img) //展示图片
const img = ref() //上传图片
const uploadRef = ref()

const onUploadFile = (file) => {
  console.log(file)
  // img.value = file.raw
  console.log(imgUrl.value)
  const fromData = new FormData()

  fromData.append('file', file.raw)
  img.value = fromData

  const reader = new FileReader()
  reader.readAsDataURL(file.raw)
  reader.onload = () => {
    imgUrl.value = reader.result
  }
}
//上传图片
const onUpdateAvatar = async () => {
  // console.log(imgUrl.value)
  const res = await userUploadAvatarService(img.value)
  console.log(res)
  await userStore.setImg(res.data.data) //存入头像
  await updateUser({ id: userStore.id, img: userStore.img }) //修改用户头像
  ElMessage({ type: 'success', message: '更换头像成功' })
}
</script>
<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>更换头像</span>
      </div>
    </template>
    <el-row>
      <el-col :span="12">
        <div class="avatar-container">
          <el-upload
            ref="uploadRef"
            class="avatar-uploader"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="onUploadFile"
          >
            <div class="avatar-wrapper">
              <img v-if="imgUrl" :src="imgUrl" class="avatar" />
              <img v-else src="@/assets/avatar.jpg" class="avatar" />
            </div>
          </el-upload>
        </div>
        <br />
        <div class="button-group">
          <el-button
            @click="uploadRef.$el.querySelector('input').click()"
            type="primary"
            :icon="Plus"
            size="large"
          >
            选择图片
          </el-button>
          <el-button
            type="success"
            :icon="Upload"
            size="large"
            @click="onUpdateAvatar"
          >
            上传头像
          </el-button>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>

<style scoped>
.card-header {
  font-weight: 600;
  font-size: 16px;
}

.avatar-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.avatar-wrapper {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid #e0e0e0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.avatar-wrapper:hover {
  border-color: #409eff;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .avatar-wrapper {
    width: 150px;
    height: 150px;
  }

  .button-group {
    flex-direction: column;
    align-items: center;
  }

  .button-group .el-button {
    width: 200px;
  }
}
</style>