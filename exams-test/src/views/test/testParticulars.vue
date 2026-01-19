<template>
  <div class="exam-detail-container" v-loading="loading">
    <!-- 只有当paperData有数据时才显示内容 -->
    <div v-if="paperData.paper && paperData.paper.paperId">
      <!-- 试卷头部信息 -->
      <div class="exam-header">
        <div class="exam-title-section">
          <h1 class="exam-title">{{ paperData.paper.paperTitle }}</h1>
          <el-tag :type="paperData.paper.status === 1 ? 'success' : 'danger'" class="status-tag">
            {{ paperData.paper.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </div>
        <p class="exam-description">{{ paperData.paper.description }}</p>
        <div class="exam-info">
          <div class="exam-info-item">
            <el-icon><Clock /></el-icon>
            <span>考试时长：{{ paperData.paper.duration }}分钟</span>
          </div>
          <div class="exam-info-item">
            <el-icon><Document /></el-icon>
            <span>试卷总分：{{ paperData.paper.totalScore }}分</span>
          </div>
          <div class="exam-info-item">
            <el-icon><Calendar /></el-icon>
            <span>创建时间：{{ formatDate(paperData.paper.createdAt) }}</span>
          </div>
          <div class="exam-info-item">
            <el-icon><Collection /></el-icon>
            <span>题目数量：{{ paperData.questionVOList?.length || 0 }}题</span>
          </div>
          <div class="exam-info-item">
            <el-icon><Timer /></el-icon>
            <span>考试时间：{{ formatDate(paperData.paper.startTime) }} 至 {{ formatDate(paperData.paper.endTime) }}</span>
          </div>
        </div>
      </div>

      <!-- 试卷操作按钮区域 -->
      <div class="exam-actions-section">
        <div class="actions-card">
          <div class="actions-content">
            <div class="actions-info">
              <div class="actions-info-item">
                <el-icon class="info-icon"><Edit /></el-icon>
                <div class="info-text">
                  <div class="info-label">编辑试卷</div>
                  <div class="info-desc">修改试卷信息和题目</div>
                </div>
              </div>
              <div class="actions-info-item">
                <el-icon class="info-icon"><Refresh /></el-icon>
                <div class="info-text">
                  <div class="info-label">刷新数据</div>
                  <div class="info-desc">获取最新试卷信息</div>
                </div>
              </div>
            </div>
            <div class="actions-buttons">
              <el-button
                type="primary"
                @click="editPaper(paperData.paper.paperId)"
                icon="Edit"
                class="action-btn primary-btn"
              >
                编辑试卷
              </el-button>
              <el-button
                @click="backToList"
                icon="Back"
                class="action-btn info-btn"
              >
                返回列表
              </el-button>
              <el-button
                @click="refreshData"
                icon="Refresh"
                class="action-btn refresh-btn"
              >
                刷新数据
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 试卷内容 -->
      <div class="exam-body" v-if="paperData.questionVOList && paperData.questionVOList.length > 0">
        <!-- 动态渲染所有题型 -->
        <div v-for="(section, sectionIndex) in groupedQuestions" :key="section.type">
          <h3 class="section-title">
            {{ getSectionTitle(section.type, sectionIndex, section.questions.length) }}
          </h3>
          <div
            class="question-card"
            v-for="(question, questionIndex) in section.questions"
            :key="question.id"
          >
            <div class="question-header">
              <div class="question-title-section">
                <span class="question-index">第{{ getQuestionNumber(section.type, questionIndex) }}题</span>
                <el-tag class="question-type-tag" :type="getQuestionTypeTagType(question.questionType)" size="small">
                  {{ question.questionType }}
                </el-tag>
                <div class="question-stat">
                  <span class="stat-item">答题次数：{{ question.totalAnswerCount || 0 }}</span>
                  <span class="stat-item">错误率：{{ calculateErrorRate(question) }}%</span>
                  <span class="stat-item">分值：{{ question.score || 0 }}分</span>
                </div>
              </div>
              <div class="question-meta">
                <span class="create-info">创建：{{ question.createUser }} {{ formatDate(question.createTime) }}</span>
                <span class="update-info">更新：{{ question.updateUser }} {{ formatDate(question.updateTime) }}</span>
              </div>
            </div>

            <!-- 题目内容根据题型动态渲染 -->
            <div class="question-content">
              <p class="question-title">{{ question.title }}</p>

              <!-- 单选题/多选题 -->
              <div
                v-if="['单选题', '多选题'].includes(question.questionType) && question.questionOptions"
                class="options-list"
              >
                <div
                  class="option-item"
                  v-for="option in question.questionOptions"
                  :key="option.id"
                  :class="{
                    'correct-option': option.isCorrect === 1,
                    'multi-correct': question.questionType === '多选题' && option.isCorrect === 1
                  }"
                >
                  <span class="option-label">{{ getOptionLabel(question, option) }}</span>
                  <div class="option-content">{{ option.optionContent }}</div>
                  <el-icon v-if="option.isCorrect === 1" class="correct-icon">
                    <CircleCheck />
                  </el-icon>
                </div>
              </div>

              <!-- 判断题 -->
              <div v-if="question.questionType === '判断题' && question.questionOptions" class="judgement-options">
                <div
                  class="judgement-item"
                  v-for="option in question.questionOptions"
                  :key="option.id"
                  :class="{ 'correct-judgement': option.isCorrect === 1 }"
                >
                  <span class="judgement-label">{{ option.optionContent }}</span>
                  <el-icon v-if="option.isCorrect === 1" class="correct-icon">
                    <CircleCheck />
                  </el-icon>
                </div>
              </div>

              <!-- 填空题 -->
              <div v-if="question.questionType === '填空题'" class="fill-blanks">
                <div class="answer-content">
                  <p class="answer-label">正确答案：</p>
                  <div class="answer-text">{{ question.correctAnswer || '无答案' }}</div>
                </div>
              </div>

              <!-- 简答题 -->
              <div v-if="question.questionType === '简答题'" class="short-answer">
                <div class="answer-content">
                  <p class="answer-label">参考答案：</p>
                  <div class="answer-text">{{ question.correctAnswer || '无答案' }}</div>
                </div>
                <div v-if="question.answerAnalysis" class="analysis-content">
                  <p class="analysis-label">解析：</p>
                  <div class="analysis-text">{{ question.answerAnalysis }}</div>
                </div>
              </div>

              <!-- 题目标签 -->
              <div class="question-tags" v-if="question.tags">
                <el-tag
                  v-for="tag in question.tags.split(',')"
                  :key="tag"
                  size="small"
                  class="tag-item"
                >
                  {{ tag }}
                </el-tag>
              </div>

              <!-- 题目解析 -->
              <div v-if="question.answerAnalysis && !['简答题'].includes(question.questionType)" class="analysis-content">
                <p class="analysis-label">解析：</p>
                <div class="analysis-text">{{ question.answerAnalysis }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <div class="empty-content">
          <el-empty description="暂无题目数据" />
          <div class="empty-actions">
            <el-button type="primary" @click="editPaper(paperData.paper.paperId)" icon="Edit">
              添加题目
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else-if="!loading" class="empty-state">
      <el-empty description="暂无试卷数据" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick, computed } from 'vue'
import { ElMessage, ElTag, ElButton, ElIcon } from 'element-plus'
import { Clock, Document, Calendar, Collection, Timer, CircleCheck, Edit, Back, Refresh } from '@element-plus/icons-vue'
import { getTestParticulars } from '@/api/testPaper'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const articleId = ref(Number(route.params.id))
const paperData = ref({
  paper: {},
  questionVOList: []
})
const loading = ref(false)
const dataLoaded = ref(false)

// 计算属性：按题型分组题目
const groupedQuestions = computed(() => {
  if (!paperData.value.questionVOList || paperData.value.questionVOList.length === 0) {
    return []
  }

  // 定义题型顺序
  const typeOrder = ['单选题', '多选题', '判断题', '填空题', '简答题']

  // 按题型分组
  const grouped = {}
  paperData.value.questionVOList.forEach(question => {
    if (!grouped[question.questionType]) {
      grouped[question.questionType] = []
    }
    grouped[question.questionType].push(question)
  })

  // 按预定顺序返回
  return typeOrder
    .filter(type => grouped[type] && grouped[type].length > 0)
    .map(type => ({
      type,
      questions: grouped[type]
    }))
})

// 重置数据
const resetData = () => {
  paperData.value = {
    paper: {},
    questionVOList: []
  }
  dataLoaded.value = false
}

// 获取试卷信息
const fetchPaperDetail = async () => {
  if (!articleId.value) {
    console.error('articleId is null or undefined')
    return
  }

  // 如果已经加载过数据，直接返回
  if (dataLoaded.value && paperData.value.paper.paperId === articleId.value) {
    console.log('数据已加载，跳过API调用')
    return
  }

  loading.value = true
  try {
    console.log("开始获取试卷详情，ID:", articleId.value)
    const res = await getTestParticulars(articleId.value)
    console.log("API响应:", res)

    if (res.data && res.data.code === 1) {
      paperData.value = res.data.data
      dataLoaded.value = true
      console.log('获取试卷数据成功')
    } else {
      ElMessage.error(res.data?.msg || '获取试卷详情失败')
    }
  } catch (error) {
    console.error('获取试卷详情错误:', error)
    ElMessage.error('获取试卷详情失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

// 获取题型标题
const getSectionTitle = (type, index, count) => {
  const sectionNames = ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十']
  const typeNames = {
    '单选题': '单项选择题',
    '多选题': '多项选择题',
    '判断题': '判断题',
    '填空题': '填空题',
    '简答题': '简答题'
  }

  return `${sectionNames[index]}、${typeNames[type] || type}（共${count}题）`
}

// 获取题目序号
const getQuestionNumber = (type, index) => {
  // 计算当前题型之前的题目总数
  let prevCount = 0
  for (let i = 0; i < groupedQuestions.value.length; i++) {
    const section = groupedQuestions.value[i]
    if (section.type === type) break
    prevCount += section.questions.length
  }

  return prevCount + index + 1
}

// 获取题型标签类型
const getQuestionTypeTagType = (type) => {
  const typeMap = {
    '单选题': 'primary',
    '多选题': 'success',
    '判断题': 'warning',
    '填空题': 'info',
    '简答题': 'danger'
  }
  return typeMap[type] || 'info'
}

// 计算错误率
const calculateErrorRate = (question) => {
  if (!question.totalAnswerCount || question.totalAnswerCount === 0) return 0
  return ((question.wrongAnswerCount / question.totalAnswerCount) * 100).toFixed(1)
}

// 获取选项标签 (A, B, C, D...)
const getOptionLabel = (question, option) => {
  if (!question.questionOptions) return ''
  const optionIndex = question.questionOptions.indexOf(option)
  if (optionIndex !== -1) {
    return String.fromCharCode(65 + optionIndex)
  }
  return ''
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  try {
    const date = new Date(dateString)
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
  } catch (e) {
    return dateString
  }
}

// 编辑试卷
const editPaper = (id) => {
  router.push(`/article/test/update/${id}`)
}

// 返回列表
const backToList = () => {
  router.back()
}

// 刷新数据
const refreshData = () => {
  resetData()
  nextTick(() => {
    fetchPaperDetail()
  })
}

// 监听路由参数变化
watch(
  () => route.params.id,
  (newId) => {
    console.log('路由参数变化:', newId)
    if (newId && Number(newId) !== articleId.value) {
      articleId.value = Number(newId)
      resetData()
      nextTick(() => {
        fetchPaperDetail()
      })
    }
  },
  { immediate: true }
)

// 监听paperData的变化
watch(
  () => paperData.value,
  (newData) => {
    console.log('paperData变化:', newData)
  },
  { deep: true }
)

// 监听loading状态
watch(
  () => loading.value,
  (newLoading) => {
    console.log('loading状态:', newLoading)
  }
)

// 组件挂载
onMounted(() => {
  console.log('组件挂载，当前ID:', articleId.value)

  // 确保数据加载
  if (!dataLoaded.value || paperData.value.paper.paperId !== articleId.value) {
    fetchPaperDetail()
  }
})

// 添加一个全局事件监听器，确保路由变化时能正确响应
window.addEventListener('popstate', () => {
  console.log('浏览器导航变化')
  setTimeout(() => {
    if (route.params.id !== articleId.value) {
      articleId.value = Number(route.params.id)
      resetData()
      fetchPaperDetail()
    }
  }, 100)
})

// 添加一个定时检查，确保数据正确加载
setInterval(() => {
  if (route.params.id && Number(route.params.id) !== articleId.value) {
    console.log('定时检查发现ID不匹配，重新加载数据')
    articleId.value = Number(route.params.id)
    resetData()
    fetchPaperDetail()
  }
}, 1000) // 每秒检查一次
</script>

<style scoped>
.exam-detail-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
  width: 100%;
  margin: 0 auto;
}

/* 试卷头部样式 */
.exam-header {
  padding: 25px 30px;
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  color: white;
  position: relative;
  overflow: hidden;
}

.exam-header::before {
  content: '';
  position: absolute;
  top: -50px;
  right: -50px;
  width: 150px;
  height: 150px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
}

.exam-header::after {
  content: '';
  position: absolute;
  bottom: -30px;
  left: -30px;
  width: 100px;
  height: 100px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
}

.exam-title-section {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 12px;
  position: relative;
  z-index: 1;
}

.exam-title {
  font-size: 28px;
  font-weight: 600;
  margin: 0;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
  flex: 1;
  max-width: 80%;
  line-height: 1.3;
}

.status-tag {
  font-weight: bold;
  height: 32px;
  line-height: 30px;
  font-size: 14px;
  padding: 0 12px;
  border: none;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.exam-description {
  font-size: 15px;
  opacity: 0.95;
  margin-bottom: 20px;
  line-height: 1.6;
  position: relative;
  z-index: 1;
  max-width: 90%;
}

.exam-info {
  display: flex;
  flex-wrap: wrap;
  gap: 25px;
  position: relative;
  z-index: 1;
}

.exam-info-item {
  display: flex;
  align-items: center;
  font-size: 14px;
  background: rgba(255, 255, 255, 0.15);
  padding: 8px 15px;
  border-radius: 20px;
  backdrop-filter: blur(5px);
  transition: all 0.3s ease;
}

.exam-info-item:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateY(-2px);
}

.exam-info-item .el-icon {
  margin-right: 8px;
  font-size: 16px;
}

/* 操作按钮区域样式 */
.exam-actions-section {
  padding: 25px 30px;
  background: linear-gradient(to bottom, #f9fafc, #ffffff);
}

.actions-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #ebeef5;
  overflow: hidden;
  transition: all 0.3s ease;
}

.actions-card:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.actions-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25px 30px;
  flex-wrap: wrap;
  gap: 30px;
}

.actions-info {
  display: flex;
  gap: 40px;
  flex: 1;
  min-width: 300px;
}

.actions-info-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px 0;
}

.info-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  color: white;
  border-radius: 12px;
  font-size: 24px;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  transition: all 0.3s ease;
}

.actions-info-item:hover .info-icon {
  transform: rotate(15deg) scale(1.1);
}

.info-text {
  display: flex;
  flex-direction: column;
}

.info-label {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.info-desc {
  font-size: 13px;
  color: #909399;
  line-height: 1.4;
}

.actions-buttons {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.action-btn {
  height: 44px;
  padding: 0 24px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 8px;
  border: none;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.action-btn:active {
  transform: translateY(0);
}

.primary-btn {
  background: linear-gradient(135deg, #409EFF, #2979ff);
  color: white;
}

.primary-btn:hover {
  background: linear-gradient(135deg, #2979ff, #1a5cd8);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
}

.info-btn {
  background: linear-gradient(135deg, #909399, #757575);
  color: white;
}

.info-btn:hover {
  background: linear-gradient(135deg, #7a7c80, #616161);
  box-shadow: 0 4px 12px rgba(144, 147, 153, 0.4);
}

.refresh-btn {
  background: linear-gradient(135deg, #E6A23C, #ff9800);
  color: white;
}

.refresh-btn:hover {
  background: linear-gradient(135deg, #d48e2a, #e68900);
  box-shadow: 0 4px 12px rgba(230, 162, 60, 0.4);
}

/* 试卷内容样式 */
.exam-body {
  padding: 30px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  margin: 35px 0 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #409EFF;
  color: #303133;
  position: relative;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  bottom: -2px;
  width: 80px;
  height: 2px;
  background: linear-gradient(90deg, #409EFF, #67C23A);
}

.question-card {
  margin-bottom: 25px;
  border: 1px solid #e4e7ed;
  border-radius: 10px;
  overflow: hidden;
  transition: all 0.3s;
  background: white;
  position: relative;
}

.question-card:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12);
  transform: translateY(-3px);
  border-color: #409EFF;
}

.question-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(to bottom, #409EFF, #67C23A);
  opacity: 0;
  transition: opacity 0.3s;
}

.question-card:hover::before {
  opacity: 1;
}

.question-header {
  padding: 18px 25px;
  background-color: #f9fafc;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  border-bottom: 1px solid #e4e7ed;
}

.question-title-section {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.question-index {
  font-weight: 700;
  color: #409EFF;
  font-size: 16px;
  min-width: 80px;
}

.question-type-tag {
  font-weight: 500;
  height: 26px;
  line-height: 24px;
  padding: 0 10px;
  border-radius: 4px;
}

.question-stat {
  display: flex;
  gap: 20px;
  margin-left: 20px;
}

.stat-item {
  font-size: 13px;
  color: #606266;
  background: #f0f2f5;
  padding: 4px 10px;
  border-radius: 4px;
  transition: all 0.3s;
}

.stat-item:hover {
  background: #e4e7ed;
  color: #303133;
}

.question-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  font-size: 12px;
  color: #909399;
  min-width: 200px;
}

.create-info, .update-info {
  margin-bottom: 4px;
  line-height: 1.4;
}

.question-content {
  padding: 25px;
  line-height: 1.6;
}

.question-title {
  font-size: 16px;
  margin-bottom: 20px;
  font-weight: 500;
  color: #303133;
  line-height: 1.8;
}

/* 选项样式 */
.options-list {
  margin-top: 15px;
}

.option-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
  padding: 12px 15px;
  border-radius: 6px;
  transition: all 0.3s;
  position: relative;
  border: 1px solid #e4e7ed;
}

.option-item:hover {
  background-color: #f5f7fa;
  transform: translateX(5px);
}

.correct-option {
  background-color: rgba(64, 158, 255, 0.05);
  border: 1px solid #a0cfff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.multi-correct {
  background-color: rgba(103, 194, 58, 0.05);
  border: 1px solid #b3e19d;
  box-shadow: 0 2px 8px rgba(103, 194, 58, 0.1);
}

.option-label {
  display: inline-block;
  width: 32px;
  height: 32px;
  line-height: 32px;
  text-align: center;
  background-color: #e4e7ed;
  border-radius: 50%;
  margin-right: 12px;
  flex-shrink: 0;
  font-weight: 600;
  transition: all 0.3s;
}

.correct-option .option-label {
  background: linear-gradient(135deg, #409EFF, #2979ff);
  color: white;
  box-shadow: 0 2px 4px rgba(64, 158, 255, 0.3);
}

.multi-correct .option-label {
  background: linear-gradient(135deg, #67C23A, #4caf50);
  color: white;
  box-shadow: 0 2px 4px rgba(103, 194, 58, 0.3);
}

.option-content {
  flex: 1;
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
}

.correct-icon {
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #67C23A;
  font-size: 20px;
  opacity: 0;
  transition: opacity 0.3s;
}

.correct-option .correct-icon,
.multi-correct .correct-icon {
  opacity: 1;
}

/* 判断题样式 */
.judgement-options {
  display: flex;
  gap: 25px;
  margin-top: 20px;
}

.judgement-item {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 12px 25px;
  border: 2px solid #e4e7ed;
  border-radius: 8px;
  transition: all 0.3s;
  position: relative;
  flex: 1;
  font-weight: 500;
  min-height: 50px;
}

.judgement-item:hover {
  background-color: #f5f7fa;
  border-color: #409EFF;
  transform: translateY(-2px);
}

.correct-judgement {
  background-color: rgba(64, 158, 255, 0.05);
  border: 2px solid #a0cfff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.judgement-label {
  font-size: 14px;
}

/* 填空题和简答题样式 */
.fill-blanks, .short-answer {
  margin-top: 20px;
}

.answer-content, .analysis-content {
  margin-top: 15px;
}

.answer-label, .analysis-label {
  font-weight: 600;
  color: #409EFF;
  margin-bottom: 8px;
  font-size: 14px;
}

.answer-text, .analysis-text {
  padding: 15px 20px;
  background-color: #f9fafc;
  border-radius: 8px;
  border-left: 4px solid #409EFF;
  font-size: 14px;
  line-height: 1.7;
  color: #606266;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
}

.question-tags {
  margin-top: 20px;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  padding-top: 15px;
  border-top: 1px dashed #e4e7ed;
}

.tag-item {
  font-size: 12px;
  border-radius: 4px;
  padding: 2px 10px;
  transition: all 0.3s;
}

.tag-item:hover {
  transform: translateY(-2px);
}

/* 空状态样式 */
.empty-state {
  padding: 60px 20px;
  text-align: center;
}

.empty-content {
  max-width: 500px;
  margin: 0 auto;
}

.empty-actions {
  margin-top: 20px;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .actions-content {
    flex-direction: column;
    align-items: stretch;
    text-align: center;
  }

  .actions-info {
    justify-content: center;
    margin-bottom: 20px;
  }

  .actions-buttons {
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .exam-header, .exam-actions-section, .exam-body {
    padding: 15px;
  }

  .exam-title {
    font-size: 22px;
  }

  .exam-info {
    flex-direction: column;
    gap: 12px;
  }

  .exam-info-item {
    width: 100%;
    justify-content: flex-start;
  }

  .actions-content {
    padding: 15px;
  }

  .actions-info {
    flex-direction: column;
    gap: 20px;
  }

  .actions-info-item {
    justify-content: center;
    text-align: left;
  }

  .actions-buttons {
    flex-direction: column;
    width: 100%;
  }

  .action-btn {
    width: 100%;
    justify-content: center;
  }

  .question-header {
    flex-direction: column;
    gap: 12px;
  }

  .question-title-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .question-stat {
    margin-left: 0;
    flex-wrap: wrap;
    gap: 10px;
  }

  .question-meta {
    align-items: flex-start;
    width: 100%;
  }

  .question-content {
    padding: 15px;
  }

  .judgement-options {
    flex-direction: column;
    gap: 12px;
  }

  .option-item {
    padding: 10px;
  }
}
</style>