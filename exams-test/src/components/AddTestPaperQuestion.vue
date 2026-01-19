<script setup>
import { ref, onMounted, watchEffect, defineEmits } from 'vue'
import { categoryList } from '@/api/category'
import { page, getById } from '@/api/question'
import { useRouter } from 'vue-router'
import { useTestPaperStore } from '@/stores'
import { AddTestPaperQuestion } from '@/api/testPaper'
const tsetPaperStore = useTestPaperStore()
const router = useRouter()

const dialogVisible = ref(false) //查看详情弹窗

const category = ref([]) //题目分类下拉列表

const categoryByList = async () => {
  //获取题目分类下拉列表
  const res = await categoryList()
  category.value = res.data.data
}
//获取分页数据
const selectPage = async () => {
  const res = await page(questionDataDTO.value)
  console.log('打印分页查询', res.data.data)
  tableData.value = res.data.data.records
  total.value = res.data.data.total
}
onMounted(async () => {
  await categoryByList()
  selectPage()
}) //页面刷新调用
const addQuestion = () => {
  router.push('/article/question/add')
}

const questionDataDTO = ref({
  //分页查询参数
  categoryId: '', //题目所属分类id
  page: 1, //页码
  pageSize: 5, //每页展示数量
  questionType: '', //题型
  title: '' //题目标题
})
//表格数据
const tableData = ref([])
const onDelete = ref(true) //删除按钮是否显示-----false:显示-----true:不显示
const ids = ref('') //选中时的id字符串
//表格选中时
const handleSelectionChange = (selection) => {
  if (selection.length > 0) {
    ids.value = ''
    for (let i = 0; i < selection.length; i++) {
      ids.value += selection[i].id
      if (i < selection.length - 1) {
        ids.value += ','
      }
    }
    onDelete.value = false
  } else {
    onDelete.value = true
  }
}
const emit = defineEmits(['refresh']) //调用父方法
//批量新增试题
const batchDelete = async () => {
  //构造新增所需参数
  const dto = {
    paperId: tsetPaperStore.testPaperData.paperId,
    ids: ids.value.split(',').map((id) => Number(id.trim())) //字符串转列表
  }
  // console.log('hhhhh', dto)
  await AddTestPaperQuestion(dto)
  ElMessage.success('导入成功')
  emit('refresh') //调用父方法
}

const sss = () => {
  emit('refresh') //调用父方法
}

//修改
// const handleUpdateQuestion = (id) => {
//     console.log(id)
//     router.push({
//         path: '/article/question/add',
//         query: {
//             id: id
//         }
//     })
// }
//定义获取分页总数量
const total = ref(0)

//每页展示数量发生变化时触发
const handleSizeChange = (pageSize) => {
  questionDataDTO.value.pageSize = pageSize
  selectPage()
}
//页码发生变化时触发
const handleCurrentChange = (page) => {
  questionDataDTO.value.page = page
  selectPage()
}

//路由变化时触发-----重新获取分页数据
watchEffect(() => {
  selectPage()
})
//题目详情列表
const subjectDetails = ref({})
//题目答题总次数
const num = ref(0)
//题目答题错误次数
const sum = ref(0)
//点击详情展开
const expand = async (id) => {
  console.log('当前id', id)
  dialogVisible.value = true
  const res = await getById(id)
  subjectDetails.value = res.data.data
  num.value = res.data.data.wrongAnswerCount
  sum.value = res.data.data.totalAnswerCount
}
//选项ABCD
const labels = ref(['A', 'B', 'C', 'D', 'E', 'F', 'G'])
//正确答案选项
const rightLabels = () => {
  const options = subjectDetails.value.questionOptions
  if (options && options.length > 1 && subjectDetails.value.questionType !== '简答题') {
    if (subjectDetails.value.questionType === '多选题') {
      return options
        .filter((option) => option.isCorrect === 1)
        .map((option) => option.optionContent)
        .join(',')
    } else {
      const correctOption = options.find((option) => option.isCorrect === 1)
      return correctOption ? correctOption.optionContent : null
    }
  } else if (options && options.length > 0) {
    return options[0].optionContent
  } else {
    return null
  }
}
</script>

<template>
  <el-card>
    <div>
      <!-- <p>{{ tsetPaperStore.testPaperData.paperId }}</p> -->
      <label style="margin-right: 5px">题目名称：</label>
      <el-input placeholder="请输入题目名称" style="width: 15%" v-model="questionDataDTO.title"></el-input>
      <label style="margin: 0 5px 0 20px">题目分类：</label>
      <el-select v-model="questionDataDTO.categoryId" style="width: 14%" placeholder="请选择" clearable @clear="init">
        <el-option v-for="cat in category" :key="cat.id" :label="cat.typeName" :value="cat.id" />
      </el-select>

      <el-button type="primary" @click="selectPage()" style="margin-left: 20px">查询</el-button>

      <div style="float: right">
        <!-- <el-button type="primary" @click="sss">跳过</el-button> -->

        <el-button type="primary" @click="batchDelete" :disabled="onDelete">添加试题</el-button>
      </div>

      <!-- 查看详情弹窗 -->
      <el-dialog v-model="dialogVisible" title="题目详情" width="800" :before-close="handleClose">
        <div style="display: flex; align-items: center; justify-content: space-between">
          <div>
            <p>
              <span style="color: #009aff">({{ subjectDetails.questionType }})</span> {{ subjectDetails.title }}
            </p>
            <div v-for="(item, i) in subjectDetails.questionOptions" :key="i">
              <p v-if="subjectDetails.questionType != '简答题'">{{ labels[i] }} {{ item.optionContent }}</p>
            </div>
            <p style="color: #009aff">正确答案:{{ rightLabels() }}</p>
            <p>
              <span>分类：{{ subjectDetails.categoryName }} | </span>
              <span>作者：{{ subjectDetails.createUser }} | </span>
              <span>更新于：{{ subjectDetails.updateTime }}</span>
              <br />
              <span>总作答次数：{{ subjectDetails.totalAnswerCount }}| </span>
              <span>错误答题次数：{{ subjectDetails.wrongAnswerCount }}</span>
            </p>
          </div>

          <div>
            <test v-if="subjectDetails.totalAnswerCount > 0" :num="num" :sum="sum"></test>
          </div>
        </div>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
          </div>
        </template>
      </el-dialog>
    </div>

    <!-- 表格 -->
    <el-table ref="multipleTableRef" :data="tableData" class="tableBox" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column property="id" label="id" />
      <el-table-column property="title" label="标题">
        <template v-slot="{ row }">
          <span class="truncate-text">{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column property="categoryName" label="题目分类" />
      <el-table-column property="questionType" label="题型" />
      <el-table-column property="createUser" label="创建者" />
      <el-table-column property="updateUser" label="修改者" />
      <el-table-column label="操作">
        <template v-slot="scope">
          <temlate>
            <!-- <el-button type="success" link plain @click="handleUpdateQuestion(scope.row.id)">修改</el-button> -->
            <el-button type="warning" link plain @click="expand(scope.row.id)">详情</el-button>
          </temlate>
        </template>
      </el-table-column>
    </el-table>

    <!-- <test></test> -->
    <!-- 
    分页 -->
    <div style="display: flex; align-items: center; justify-content: center; margin-top: 30px">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="questionDataDTO.page"
        class="pageList"
        :page-sizes="[5, 10, 20, 40, 50]"
        :page-size="questionDataDTO.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>
  </el-card>
</template>

<style>
.truncate-text {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.tableBox {
  margin-top: 30px;
  /* width: 100%; */
  border: 1px solid #f3f4f7;
  border-bottom: 0;
}

.el-button--success,
.el-button--warning,
.el-table td {
  font-size: 13px !important;
}

.el-table td,
.el-table th {
  padding: 12px 0;
}
</style>
