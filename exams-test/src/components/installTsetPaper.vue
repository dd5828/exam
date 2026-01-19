<script lang="ts" setup>
import { ref, onMounted, watch, nextTick, defineEmits } from 'vue'
import { Edit } from '@element-plus/icons-vue'

const emit = defineEmits(['refresh']) //调用父方法
// import { ElMessage } from 'element-plus'

const totalTime = ref(30) //总时长
const totalScore = ref(100) //总分

const centerDialogVisible = ref(false) //修改时长是否弹窗
const totalScoreDialogVisible = ref(false) //修改总分是否弹窗
//获取当前新增试卷的id
import { useTestPaperStore } from '@/stores/index'
const testPaper = useTestPaperStore()

//获取题型数量
import { countQuestionTypesByPaperId } from '@/api/testPaper'
interface CountQuestionSum {
  questionType: string
  count: number
  typeScore: number
}
const countQuestionSum = ref<CountQuestionSum>({
  questionType: '',
  count: 0,
  typeScore: 0
})
const getCoutnQusetion = async () => {
  const res = await countQuestionTypesByPaperId(testPaper.testPaperData.paperId)
  console.log(res.data.data)
  countQuestionSum.value = res.data.data
  console.log('cesdf', countQuestionSum.value)
}

onMounted(() => {
  getCoutnQusetion()
})

//时间----------------------------------------------------------
import { CaretLeft, CaretRight } from '@element-plus/icons-vue'
import moment from 'moment-timezone'
const value2 = ref<Date[] | string>()
const formattedStartDate = ref('') //开启时间
const formattedEndDate = ref('') //结束时间
const printFormattedDate = () => {
  if (value2.value && value2.value.length === 2) {
    const startDate = value2.value[0]
    const endDate = value2.value[1]
    // 使用 moment-timezone 进行时区转换并格式化
    formattedStartDate.value = startDate ? moment(startDate).tz('Asia/Shanghai').format('YYYY-MM-DD HH:mm:ss') : ''
    formattedEndDate.value = endDate ? moment(endDate).tz('Asia/Shanghai').format('YYYY-MM-DD HH:mm:ss') : ''
    console.log(`开始时间：${formattedStartDate.value}`)
    console.log(`结束时间：${formattedEndDate.value}`)
    // return [formattedStartDate, formattedEndDate]
    nextTick()
    return true
  } else {
    ElMessage({ message: '请选择完整的日期范围', type: 'error' })
    return false
  }
}

//执行修改------------------------------------------------------
interface PaperInfo {
  duration: number //考试时长
  totalScore: number //试卷总分
  paperId: number //试卷id
  startTime: string //开始时间
  endTime: string //结束时间
}

const paperinfo = ref<PaperInfo>({
  paperId: testPaper.testPaperData.paperId,
  duration: totalTime.value,
  totalScore: totalScore.value,
  startTime: formattedStartDate.value,
  endTime: formattedEndDate.value
})

watch([totalTime, totalScore, formattedStartDate, formattedEndDate], (newValues, oldValues) => {
  console.log('以下变量发生了变化：')
  newValues.forEach((newValue, index) => {
    if (newValue !== oldValues[index]) {
      // 更新 paperinfo
      paperinfo.value = {
        paperId: testPaper.testPaperData.paperId,
        duration: totalTime.value,
        totalScore: totalScore.value,
        startTime: formattedStartDate.value,
        endTime: formattedEndDate.value
      }
    }
  })
})

const sumScore = () => {
  let sum = 0
  if (Array.isArray(countQuestionSum.value)) {
    countQuestionSum.value.forEach((item) => {
      sum += item.typeScore
    })
  }
  return sum
}
import { updateTestPaper, updateTypeScore } from '@/api/testPaper'
import { useRouter } from 'vue-router'

const router = useRouter()
const updateInfo = async () => {
  if (printFormattedDate() === true) {
    await nextTick()

    //判定题型分数是否等于总分
    if (sumScore() === totalScore.value) {
      await updateTestPaper(paperinfo.value)
      console.log('题型分数', countQuestionSum.value)
      //构造修改题型分数参数
      const questionTypeScoreDTO = {
        paperId: testPaper.testPaperData.paperId,
        questionTypeCountVOList: countQuestionSum.value
      }
      await updateTypeScore(questionTypeScoreDTO)
      ElMessage({ message: '设置成功', type: 'success' })
      router.push('/article/test')
      emit('refresh') //调用父方法
    } else {
      ElMessage({ message: '分数设置错误', type: 'error' })
    }
  }
}
</script>

<template>
  <div class="header-save">
    <el-card class="main-card">
      <div class="settings-grid">
        <!-- 考试时长 -->
        <div class="setting-group">
          <div class="setting-header">
            <span class="setting-title">考试时长</span>
            <el-button type="primary" :icon="Edit" text @click="centerDialogVisible = true">修改</el-button>
          </div>
          <div class="setting-content">
            <div class="value-display">
              <span class="value">{{ totalTime }}</span>
              <span class="unit">分钟</span>
            </div>
          </div>
        </div>

        <!-- 考试分数 -->
        <div class="setting-group">
          <div class="setting-header">
            <span class="setting-title">考试总分</span>
            <el-button type="primary" :icon="Edit" text @click="totalScoreDialogVisible = true">修改</el-button>
          </div>
          <div class="setting-content">
            <div class="value-display">
              <span class="value score">{{ totalScore }}</span>
              <span class="unit">分</span>
            </div>
          </div>
        </div>

        <!-- 设置题型分数 -->
        <div class="setting-group question-types">
          <div class="setting-header">
            <span class="setting-title">设置题型分数</span>
          </div>
          <div class="setting-content">
            <div class="question-type-grid">
              <div v-for="(item, i) in countQuestionSum" :key="i" class="question-type-item">
                <div class="question-type-info">
                  <span class="type-name">{{ item.questionType }}</span>
                  <span class="type-count">{{ item.count }}题</span>
                </div>
                <div class="score-input">
                  <el-input-number
                    v-model="item.typeScore"
                    :controls="false"
                    size="small"
                    class="score-input-field"
                    :min="0"
                  />
                  <span class="score-unit">分</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 设置起始时间 -->
        <div class="setting-group time-setting">
          <div class="setting-header">
            <span class="setting-title">设置起始时间</span>
          </div>
          <div class="setting-content">
            <div class="date-picker-container">
              <el-date-picker
                v-model="value2"
                type="datetimerange"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                format="YYYY-MM-DD HH:mm:ss"
                date-format="YYYY/MM/DD ddd"
                time-format="A hh:mm:ss"
                unlink-panels
                class="date-range-picker"
              >
                <template #prev-month>
                  <el-icon>
                    <CaretLeft />
                  </el-icon>
                </template>
                <template #next-month>
                  <el-icon>
                    <CaretRight />
                  </el-icon>
                </template>
                <template #prev-year>
                  <el-icon>
                    <svg viewBox="0 0 20 20" version="1.1" xmlns="http://www.w3.org/2000/svg">
                      <g stroke-width="1" fill-rule="evenodd">
                        <g fill="currentColor">
                          <path
                            d="M8.73171,16.7949 C9.03264,17.0795 9.50733,17.0663 9.79196,16.7654 C10.0766,16.4644 10.0634,15.9897 9.76243,15.7051 L4.52339,10.75 L17.2471,10.75 C17.6613,10.75 17.9971,10.4142 17.9971,10 C17.9971,9.58579 17.6613,9.25 17.2471,9.25 L4.52112,9.25 L9.76243,4.29275 C10.0634,4.00812 10.0766,3.53343 9.79196,3.2325 C9.50733,2.93156 9.03264,2.91834 8.73171,3.20297 L2.31449,9.27241 C2.14819,9.4297 2.04819,9.62981 2.01448,9.8386 C2.00308,9.89058 1.99707,9.94459 1.99707,10 C1.99707,10.0576 2.00356,10.1137 2.01585,10.1675 C2.05084,10.3733 2.15039,10.5702 2.31449,10.7254 L8.73171,16.7949 Z"
                          />
                        </g>
                      </g>
                    </svg>
                  </el-icon>
                </template>
                <template #next-year>
                  <el-icon>
                    <svg viewBox="0 0 20 20" version="1.1" xmlns="http://www.w3.org/2000/svg">
                      <g stroke-width="1" fill-rule="evenodd">
                        <g fill="currentColor">
                          <path
                            d="M11.2654,3.20511 C10.9644,2.92049 10.4897,2.93371 10.2051,3.23464 C9.92049,3.53558 9.93371,4.01027 10.2346,4.29489 L15.4737,9.25 L2.75,9.25 C2.33579,9.25 2,9.58579 2,10.0000012 C2,10.4142 2.33579,10.75 2.75,10.75 L15.476,10.75 L10.2346,15.7073 C9.93371,15.9919 9.92049,16.4666 10.2051,16.7675 C10.4897,17.0684 10.9644,17.0817 11.2654,16.797 L17.6826,10.7276 C17.8489,10.5703 17.9489,10.3702 17.9826,10.1614 C17.994,10.1094 18,10.0554 18,10.0000012 C18,9.94241 17.9935,9.88633 17.9812,9.83246 C17.9462,9.62667 17.8467,9.42976 17.6826,9.27455 L11.2654,3.20511 Z"
                          />
                        </g>
                      </g>
                    </svg>
                  </el-icon>
                </template>
              </el-date-picker>
            </div>
          </div>
        </div>
      </div>

      <div class="action-buttons">
        <el-button type="primary" size="large" @click="updateInfo" class="submit-button">完成设置</el-button>
      </div>
    </el-card>
  </div>

  <!-- 时长弹窗 -->
  <el-dialog v-model="centerDialogVisible" title="试卷时间设置" width="500" center draggable>
    <div class="dialog-content">
      <p class="dialog-text">
        限制试卷总时长
        <el-input-number v-model="totalTime" style="width: 100px; margin: 0 8px" :step="1" size="small" />
        分钟
      </p>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="centerDialogVisible = false">确定</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 总分 -->
  <el-dialog v-model="totalScoreDialogVisible" title="试卷总分设置" width="500" center draggable>
    <div class="dialog-content">
      <p class="dialog-text">
        总分
        <el-input-number v-model="totalScore" style="width: 100px; margin: 0 8px" :step="1" size="small" />
        分
      </p>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="totalScoreDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="totalScoreDialogVisible = false">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.header-save {
  margin: 24px auto;
  max-width: 1200px;
}

.main-card {
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.settings-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.setting-group {
  background: #f8fafc;
  border-radius: 8px;
  padding: 20px;
  border-left: 4px solid #36aafd;
  transition: all 0.3s ease;
}

.setting-group:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
  transform: translateY(-2px);
}

.question-types {
  grid-column: span 2;
}

.time-setting {
  grid-column: span 2;
}

.setting-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e8eaed;
}

.setting-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  position: relative;
}

.setting-content {
  display: flex;
  flex-direction: column;
}

.value-display {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.value {
  font-size: 28px;
  font-weight: 700;
  color: #ff962a;
}

.value.score {
  color: #f56c6c;
}

.unit {
  font-size: 16px;
  color: #666;
}

.question-type-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
}

.question-type-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: white;
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
}

.question-type-info {
  display: flex;
  flex-direction: column;
}

.type-name {
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.type-count {
  font-size: 14px;
  color: #666;
}

.score-input {
  display: flex;
  align-items: center;
  gap: 8px;
}

.score-input-field {
  width: 80px;
}

.score-unit {
  color: #666;
  font-size: 14px;
}

.date-picker-container {
  display: flex;
  justify-content: center;
}

.date-range-picker {
  width: 100%;
  max-width: 500px;
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #e8eaed;
}

.submit-button {
  padding: 12px 32px;
  font-size: 16px;
  border-radius: 6px;
}

.dialog-content {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.dialog-text {
  font-size: 16px;
  color: #333;
  display: flex;
  align-items: center;
}

.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 16px;
}

@media (max-width: 768px) {
  .settings-grid {
    grid-template-columns: 1fr;
  }

  .question-types,
  .time-setting {
    grid-column: span 1;
  }

  .question-type-grid {
    grid-template-columns: 1fr;
  }

  .main-card {
    padding: 20px;
  }
}
</style>