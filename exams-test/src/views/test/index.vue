<script lang="ts" setup>
import { ref, watch, onMounted ,nextTick} from 'vue'
import { Search } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { getPageList, updateTestPaper,delectById } from '@/api/testPaper'
import { ElMessage } from 'element-plus'

const router = useRouter()
const status = ref()
const List = ref()
const total = ref(0)
const onAddTest = () => {
  router.push('/article/test/add')
}

interface TestPaperList {
  //试卷分页查询所需
  paperTitle: string //试卷标题
  status: any //试卷状态
  page: number //页码
  pageSize: number //每条记录数
}

const testList = ref<TestPaperList>({
  paperTitle: '',
  status: '',
  page: 1,
  pageSize: 3
})

const getList = async () => {
  const res = await getPageList(testList.value)
  console.log(res.data.data)
  List.value = res.data.data.records
  total.value = res.data.data.total
   // 等待DOM更新完成（列表渲染后）再获取高度
  await nextTick()
  updateMyHeight()  // 调用更新高度的方法
}
const changePage = (page: number) => {
  testList.value.page = page
  getList()  // 直接调用getList，内部已处理高度更新
}
const changePageSize = (pageSize: number) => {
  testList.value.pageSize = pageSize
  console.log("shul"+pageSize)

}

// 定义动态高度变量（响应式）
let myHeight = ref<string>('auto')  // 初始为auto，由内容决定
// 核心：更新my的高度（封装为函数复用）
const updateMyHeight = () => {
  // 用nextTick等待DOM更新（列表重新渲染完成）
  nextTick(() => {
    const myDiv = document.getElementById('my')
    if (myDiv) {
    // 强制刷新元素布局（解决高度计算延迟问题）
    myDiv.offsetHeight  // 触发重排，确保获取最新高度
    const newHeight = myDiv.scrollHeight
    myHeight.value = `${newHeight}px`
    console.log(`当前每页${testList.value.pageSize}条，高度：${newHeight}px`)  // 观察日志
  }
  })
}


watch(
  testList,
  (newVal, oldVal) => {
    getList()
  },
  {
    immediate: true, // 设置immediate为true，让监听器在页面加载时立即执行一次
    deep: true // 添加深度监听选项
  }
)

const updateStatus = async (paperId: number, status: number) => {
  const data = { paperId: paperId, status: status === 1 ? 0 : 1 }
  const res = await updateTestPaper(data)
  ElMessage({ type: 'success', message: '修改成功' })
  getList()
   // 状态修改后列表可能变化，也可触发高度更新
  updateMyHeight()
}

// 初始加载时确保高度正确
onMounted(() => {
  updateMyHeight()
})

const  onParticulars= (id:number) => {
  router.push(`/article/test/particulars/${id}`)
}

const deletePaper=async (paperId:number)=>{
  await delectById(paperId)
  ElMessage.success('删除成功')
}

</script>

<template>
  <div :style="{ width: '100%', height: myHeight, backgroundColor: 'white' }" id="my">
    <div class="tab">
      <el-button type="primary" @click="onAddTest()">新建试卷</el-button>
<!--      <el-button type="primary" @click="onParticulars()">试卷详情</el-button>-->
      <div style="float: right">
        <el-input v-model="testList.paperTitle" style="width: 100px; margin-right: 10px" placeholder="搜索"
          :suffix-icon="Search" clearable />
        <el-select v-model="testList.status" style="width: 120px" placeholder="试卷状态" clearable>
          <el-option value="0" label="未发布" />
          <el-option value="1" label="已发布" />
        </el-select>
      </div>
    </div>

    <div v-for="(item, i) in List" :key="i" class="item">
      <div @click="onParticulars(item.paperId)">
        <img :src="item.paperPictures" alt=""
             style="width: 141px; height: 138px; border: 1px solid rgb(139 115 115); border-radius: 10px" />
      </div>
      <div class="title" @click="onParticulars(item.paperId)">
        <p style="font-weight: 600; font-size: 18px">{{ item.paperTitle }}</p>
        <span style="font-size: 14px; color: gray">{{ item.categoryName }} | 总分 {{ item.totalScore }}分 | 限制{{
            item.totalScore }}分钟</span>
        <p style="font-size: 14px; color: gray">{{ item.createUser }} 创建于{{ item.createdAt }}</p>
      </div>
      <div class="choose">
        <el-button type="primary" style="margin-left: 10px" @click.stop="updateStatus(item.paperId, item.status)"
                   v-if="item.status == 0">启用</el-button>

        <el-button type="info" style="margin-left: 10px" @click.stop="updateStatus(item.paperId, item.status)"
                   v-else>禁用</el-button>
        <el-button type="danger" @click.stop="deletePaper(item.paperId)">删除</el-button>
      </div>
    </div>

    <div style="display: flex; align-items: center; justify-content: center" class="page">
      <div class="jjj">
        <el-pagination @size-change="changePageSize" @current-change="changePage" :current-page="testList.page"
          :page-sizes="[3, 10, 30, 40, 50]" :page-size="testList.pageSize"
          layout="total, sizes, prev, pager, next, jumper" :total="total" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.tab {
  box-shadow: 0px 3px 6px 0px rgba(51, 51, 51, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 52px;
  margin-bottom: 0;
  padding: 0px 16px 0 40px;
  padding: 0 16px;
}

.item {
  margin: 30px;
  height: 138px;
  display: flex;
  position: relative;
}

.title {
  margin-left: 20px;
  /* height: 100%; */
  display: flex;
  justify-content: space-between;
  flex-direction: column;
}

.choose {
  position: absolute;
  display: flex;
  align-items: center;
  width: 200px;
  height: 138px;
  flex-direction: column;
  justify-content: space-evenly;
  right: 10px;
}

.page {
  position: fixed;
  /* position: relative; */
  width: 83.3%;
  bottom: 10px;
  background-color: rgb(255, 255, 255);
}

.jjj {
  /* margin-left: 350px; */
  margin-right: 5%;
}
</style>
