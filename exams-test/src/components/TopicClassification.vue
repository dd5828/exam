<script setup>
import { ref, onMounted } from 'vue'
import { categoryList } from '@/api/category'
// import { userRouter } from 'vue-router' //跳转路由
// const router = userRouter()
const fromModel = ref({
  name: '',
  id: ''
})

const category = ref([])

const categoryByList = async () => {
  const res = await categoryList()
  category.value = res.data.data
}
onMounted(categoryByList) //页面刷新调用
// const AddQuestion = () => {
//     router.push('/article/question/add')
// }
</script>

<template>
  <el-card style="height: 100vh">
    <div>
      <label style="margin-right: 5px">题目名称：</label>
      <el-input placeholder="请输入题目名称" style="width: 15%" v-model="fromModel.name"></el-input>
      <label style="margin: 0 5px 0 20px">题目分类：</label>
      <el-select v-model="fromModel.id" style="width: 14%" placeholder="请选择" clearable @clear="init">
        <el-option v-for="cat in category" :key="cat.id" :label="cat.typeName" :value="cat.id" />
      </el-select>

      <div style="float: right">
        <el-button type="primary" @click="AddQuestion('add')">添加题目</el-button>
      </div>
    </div>
  </el-card>
</template>
