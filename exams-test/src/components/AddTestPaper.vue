<script lang="ts" setup>
import { ref, onMounted, defineEmits } from 'vue'
import { categoryList } from '@/api/category'
import { useTestPaperStore } from '@/stores'

import { userUploadAvatarService } from '@/api/user'

const tsetPaperStore = useTestPaperStore()

const emit = defineEmits(['refresh']) //调用父方法

interface Category {
  //分类
  id: number
  typeName: string
}

interface TestPaperInfo {
  //试卷基本信息
  paperTitle: string //试卷标题
  description: string //试卷描述
  categoryId: number //试卷分类id
  paperPictures: string //试卷图片
}

const testPaperInfo = ref<TestPaperInfo>({
  paperTitle: '',
  description: '你不点进来，怎么知道你很厉害',
  categoryId: 1, // 或者设置一个默认的分类 ID
  paperPictures: tsetPaperStore.testPaperData.paperPictures
})

const category = ref<Category[]>([]) //题目分类下拉列表
const categoryByList = async () => {
  //获取题目分类下拉列表
  const res = await categoryList()
  category.value = res.data.data
}
onMounted(() => {
  categoryByList()
})

//保存，并进入下一步
import { AddTestPaper } from '@/api/testPaper'
const aaaaff = async () => {
  if (expandState.value === 1) {
    console.log(expandState.value)
    onUpdateAvatar()
  } else {
    console.log(expandState.value)
  }
  const res = await AddTestPaper(testPaperInfo.value)
  await tsetPaperStore.setPaperId(res.data.data) //存入新增文章id
  emit('refresh') //调用父方法
}

//展示图片

const imgUrl = ref<string | null>(tsetPaperStore.testPaperData.paperPictures) //展示图片
const img = ref() //上传图片
const uploadRef = ref()
const onUploadFile = (file: any) => {
  console.log(file)
  // img.value = file.raw
  console.log(imgUrl.value)
  const fromData = new FormData()

  fromData.append('file', file.raw)
  img.value = fromData

  const reader = new FileReader()
  reader.readAsDataURL(file.raw)
  reader.onload = () => {
    if (typeof reader.result === 'string') {
      imgUrl.value = reader.result
    }
  }
}
//上传图片
const onUpdateAvatar = async () => {
  // console.log(imgUrl.value)
  const res = await userUploadAvatarService(img.value)
  // console.log(res)
  // await userStore.setTestImg(res.data.data) //
  await tsetPaperStore.setPaperPictures(res.data.data) //存入文章图片

  // ElMessage({ type: 'success', message: '成功' })
}

//展开状态
const expandState = ref<number>(0)
</script>

<template>
  <div class="header-save">
    <div>
      <el-card style="padding: 60px 60px">
        <div class="group-title">
          <span>试卷标题</span>
          <p>
            <el-input
              v-model="testPaperInfo.paperTitle"
              style="width: 60vh"
              maxlength="30"
              size="large"
              placeholder="最多可输入30个字符"
              show-word-limit
              type="text"
            />
          </p>
        </div>
        <br />
        <div class="group-title">
          <span>试卷类别</span>
          <p>
            <el-select
              v-model="testPaperInfo.categoryId"
              style="width: 60vh"
              size="large"
              placeholder="请选择"
              clearable
            >
              <el-option v-for="cat in category" :key="cat.id" :label="cat.typeName" :value="cat.id" />
            </el-select>
          </p>
        </div>

        <!-- 高级设置展开 -->
        <el-collapse>
          <el-collapse-item title="高级设置">
            <template v-slot:title>
              <span class="collapse-title" @click="expandState === 0 ? (expandState = 1) : (expandState = 0)"
                >高级设置</span
              >
            </template>

            <div style="display: flex; flex-direction: column">
              <div class="group-title">
                <span>试卷封面</span>

                <el-row style="margin-top: 20px">
                  <el-col :span="12">
                    <el-upload
                      ref="uploadRef"
                      class="avatar-uploader"
                      :auto-upload="false"
                      :show-file-list="false"
                      :on-change="onUploadFile"
                    >
                      <img v-if="imgUrl" :src="imgUrl" class="avatar" width="200" />
                      <img v-else src="@/assets/avatar.jpg" width="200" />
                    </el-upload>
                    <br />
                    <!-- <el-button @click="uploadRef.$el.querySelector('input').click()" type="primary"
                                        :icon="Plus" size="large">选择图片</el-button>
                                    <el-button type="success" :icon="Upload" size="large" @click="onUpdateAvatar"> 上传头像
                                    </el-button> -->
                  </el-col>
                </el-row>
              </div>

              <div class="group-title">
                <span>描述</span>
                <p>
                  <el-input v-model="testPaperInfo.description" style="width: 60vh" type="textarea" />
                </p>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>

        <el-button size="large" type="primary" @click="aaaaff">保存并进入下一步</el-button>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.header-save {
  /* width: 50%; */
  margin: 24px auto;
  display: flex;
  flex-direction: column;
  /* border-radius: 10px !important; */
}

.group-title {
  /* margin: 50px; */
  position: relative;
  display: inline-block;
  margin-bottom: 24px;
  font-size: 16px;
  /* font-weight: 700; */
  color: #333333;
  text-indent: 12px;
}

.group-title::before {
  content: '';
  position: absolute;
  top: 12px;
  left: 0;
  transform: translateY(-50%);
  width: 4px;
  height: 16px;
  background-color: #36aafd !important;
  border-radius: 2px !important;
}

/* 去掉展开上下边框线 */
.el-collapse {
  /* display: flex; */

  --el-collapse-border-color: transparent !important;
}

::v-deep .collapse-title {
  /* flex: 1 0 90%; */
  order: 1;
  font-size: 16px;
  color: #36aafd;

  .el-collapse-item__header {
    flex: 1 0 auto;
    order: -1;
  }
}
</style>
