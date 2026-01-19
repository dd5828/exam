<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { page,getSumQuestion } from '@/api/question'
import {getPageList} from '@/api/testPaper'
import {getStudentList} from '@/api/student'

const questionDataDTO = ref({
  //分页查询参数
  categoryId: '', //题目所属分类id
  page: 1, //页码
  pageSize: 10, //每页展示数量
  questionType: '', //题型
  title: '' //题目标题
})
//定义获取试题数量
const totalTest = ref(0)
const selectPage = async () => {
  const res = await page(questionDataDTO.value)
  console.log('打印试题分页查询', res.data.data)
  totalTest.value = res.data.data.total
}
//定义获取试卷数量
const totalPaPer = ref(0)
let paPerStatic=ref(0)
let paPerEnd=ref(0)
const testList = ref({
  paperTitle: '',
  status: '',
  page: 1,
  pageSize: 1000
})
const getList = async () => {
  const res = await getPageList(testList.value)
  console.log(res.data.data)
  totalPaPer.value = res.data.data.total
  console.log(res.data.data.records[1].status)
  for(let i=0;i<res.data.data.records.length;i++){
    if(res.data.data.records[i].status===1){
      paPerStatic.value+=1
    }
    else
      paPerEnd.value+=1
  }
  console.log("开始"+paPerStatic.value)
  console.log("结束"+paPerEnd.value)


}
//定义获取学生信息数量
const totalStudent=ref()
const Student=ref({
  studentName:'',
  studentNumber:'',
  page:1,
  pageSize:3
})
const getStudentListPage=async () =>{
  const res=await getStudentList(Student.value)
  console.log(res.data.data)
  totalStudent.value=res.data.data.total
}

//获取题型数量信息
const getSumQuestionList=async () =>{
  const res=await getSumQuestion()
  console.log(res.data.data)
  console.log("1.dayin",questionTypes.value)
   questionTypes.value=res.data.data
  console.log("dayin",questionTypes.value)
}

onMounted(async () => {
  await selectPage()
  await getList()
  await getStudentListPage()
  await getSumQuestionList()
}) //页面刷新调用

// 先定义接口类型
interface QuestionType {
  questionType: string
  sumQuestion: number
}

// 再使用 ref 并指定泛型
const questionTypes = ref<QuestionType[]>([
  { questionType: '单选题', sumQuestion: 200 },
  { questionType: '多选题', sumQuestion: 10 },
  { questionType: '判断题', sumQuestion: 15 }
])



</script>
<template>
  <div
    style="
      background-color: white;
      width: 100%;
      height: 100%;
      display: flex;
      align-items: stretch;
      flex-direction: column;
    "
  >
    <div style="padding: 20px; width: 97%">
      <span class="border-color">情况概览</span>
      <div style="margin-top: 10px; border-top: 1px solid #efefef">
        <div style="display: flex; margin: 20px 0 20px 0; align-items: center; justify-content: space-between">
          <div class="hhh">
            <div>
              <span class="underline">{{totalPaPer}}/100  <i style="font-size: 30px; margin-left: 10px" class="iconfont icon-chuangjianshijuan"></i></span>
              <span class="txt">创建考试  </span>
            </div>
            <div>

            </div>
          </div>
          <div class="hhh">
            <span class="underline">{{totalTest}}/100  <i style="font-size: 30px; margin-left: 10px" class="iconfont icon-kaowuguanli-kaoshiguanli"></i></span>
            <span class="txt">创建考题</span>
          </div>
          <div class="hhh">
            <span class="underline">{{totalStudent}}/100  <i style="font-size: 30px; margin-left: 10px" class="iconfont icon-gerenxinxi"></i></span>
            <span class="txt">学生账号</span>
          </div>
        </div>
      </div>
    </div>

    <div class="frame">
      <div class="lalala">
        <span class="border-color">考试数量</span>
        <div style="display: flex; flex-direction: column">
          <p class="flood" style="margin-top: 20px;"><span>进行中</span> <span>{{paPerStatic}}场</span></p>
          <p class="flood"><span>已结束</span> <span>{{paPerEnd}}场</span></p>
        </div>
      </div>
      <div class="lalala">
        <span class="border-color">考题数量</span>
        <div style="margin-top: 30px;width: 300px;height: 300px">
          <question-type :questionTypes="questionTypes"></question-type>
        </div>
      </div>
      <div class="lalala">
        <span class="border-color">平台公告</span>
        <h4>暂无公告</h4>
      </div>
    </div>

    <!--    <div style="display: flex; align-items: center; justify-content: center">-->
    <!--      <echarts></echarts>-->
    <!--    </div>-->
  </div>
</template>
<style scoped>
.border-color {
  border-left: 3px solid #009aff;
  padding-left: 10px;
}
.frame {
  display: flex;
  width: 97%;
  margin: 20px;
  align-items: center;
  justify-content: space-between;
}
.hhh {
  width: 300px;
  //height: 100px;
  border: 1px solid #efefef;
  border-radius: 5px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;

  .underline {
    font-size: 25px;
    display: flex;
    align-items: center;
    justify-items: center;
  }
  .txt {
    font-size: 10px;
    color: #666666;
  }
}

.lalala {
  width: 300px;
  height: 300px;
  //border: 1px solid #efefef;
  display: flex;
  flex-direction: column;
}
.flood {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}
</style>
