<script lang="ts" setup>
import { ref, watch, watchEffect, onMounted, computed } from 'vue'
import { Addquestion, getById, updateQuestion } from '@/api/question'
import { useRouter, useRoute } from 'vue-router'
import { categoryList } from '@/api/category'
import { ElMessage } from 'element-plus'

// 类型定义
interface QuestionType {
  typeName: string
  typeNum: number
}

interface QuestionOption {
  isCorrect: number
  optionContent: string
}

interface QuestionForm {
  categoryId: number
  questionOptions: QuestionOption[]
  questionType: string
  tags: string
  title: string
}

interface Category {
  id: number
  typeName: string
}

// 响应式数据
const router = useRouter()
const route = useRoute()

const questionTypes = ref<QuestionType[]>([
  { typeName: '单选题', typeNum: 4 },
  { typeName: '多选题', typeNum: 5 },
  { typeName: '判断题', typeNum: 2 },
  { typeName: '简答题', typeNum: 1 }
])

const category = ref<Category[]>([])
const labels = ref(['A', 'B', 'C', 'D', 'E', 'F', 'G'])
const num = ref(4)
const right = ref(0)
const checkList = ref<number[]>([])
const radio = ref('单选题')

// 计算属性
const isEditMode = computed(() => {
  const id = route.query.id
  return id !== null && typeof id === 'string' && !isNaN(Number(id))
})

const currentQuestionType = computed(() =>
  isEditMode.value ? questionFrom.value.questionType : radio.value
)

const pageTitle = computed(() =>
  isEditMode.value ? '修改试题' : '新增试题'
)

// 试题表单
const questionFrom = ref<QuestionForm>({
  categoryId: 1,
  questionOptions: Array.from({ length: num.value }, () => ({
    isCorrect: 0,
    optionContent: ''
  })),
  questionType: radio.value,
  tags: '',
  title: ''
})

// 方法
const categoryByList = async () => {
  const res = await categoryList()
  category.value = res.data.data
}

const getByQuestions = async (id: number) => {
  const res = await getById(id)
  questionFrom.value = res.data.data

  // 设置正确答案
  if (questionFrom.value.questionType === '多选题') {
    checkList.value = questionFrom.value.questionOptions
      .map((option, index) => option.isCorrect === 1 ? index : -1)
      .filter(index => index !== -1)
  } else {
    const correctIndex = questionFrom.value.questionOptions.findIndex(
      option => option.isCorrect === 1
    )
    right.value = correctIndex !== -1 ? correctIndex : 0
  }

  // 设置选项数量
  num.value = questionFrom.value.questionOptions.length
}

const resetForm = () => {
  questionFrom.value = {
    categoryId: 1,
    questionOptions: Array.from({ length: num.value }, () => ({
      isCorrect: 0,
      optionContent: ''
    })),
    questionType: radio.value,
    tags: '',
    title: ''
  }
  checkList.value = []
  right.value = 0
}

const setCorrectAnswers = () => {
  // 重置所有选项
  questionFrom.value.questionOptions.forEach(option => {
    option.isCorrect = 0
  })

  // 设置正确答案
  if (currentQuestionType.value === '多选题') {
    checkList.value.forEach(index => {
      if (questionFrom.value.questionOptions[index]) {
        questionFrom.value.questionOptions[index].isCorrect = 1
      }
    })
  } else {
    if (questionFrom.value.questionOptions[right.value]) {
      questionFrom.value.questionOptions[right.value].isCorrect = 1
    }
  }
}

const handleSubmit = async () => {
  try {
    setCorrectAnswers()

    if (isEditMode.value) {
      await updateQuestion(questionFrom.value)
      ElMessage.success('修改成功')
    } else {
      await Addquestion(questionFrom.value)
      ElMessage.success('新增成功')
      resetForm()
    }

    router.push('/article/question')
  } catch (error) {
    ElMessage.error('操作失败，请重试')
  }
}

// 监听器
watch(num, (newValue, oldValue) => {
  if (newValue > oldValue) {
    while (questionFrom.value.questionOptions.length < newValue) {
      questionFrom.value.questionOptions.push({
        isCorrect: 0,
        optionContent: ''
      })
    }
  } else {
    questionFrom.value.questionOptions.length = newValue
  }
})

watch(radio, (newValue) => {
  const type = questionTypes.value.find(t => t.typeName === newValue)
  if (type) {
    num.value = type.typeNum
  }
})

// 路由监听
watchEffect(() => {
  const id = route.query.id
  if (id && typeof id === 'string' && !isNaN(Number(id))) {
    getByQuestions(Number(id))
  } else if (isEditMode.value === false) {
    resetForm()
  }
})

// 生命周期
onMounted(() => {
  categoryByList()
})
</script>

<template>
  <el-card class="question-card">
    <template #header>
      <span class="card-title">{{ pageTitle }}</span>
    </template>

    <div class="content-layout">
      <!-- 左侧题型选择 -->
      <div class="question-type-section">
        <span class="section-title">试题题型</span>

        <!-- 新增模式 -->
        <el-radio-group
          v-if="!isEditMode"
          v-model="radio"
          class="type-radio-group"
        >
          <el-radio
            v-for="item in questionTypes"
            :key="item.typeName"
            :value="item.typeName"
            :label="item.typeName"
            class="type-radio"
          />
        </el-radio-group>

        <!-- 修改模式 -->
        <div v-else class="readonly-type">
          <el-tag type="primary" size="large">
            {{ currentQuestionType }}
          </el-tag>
        </div>
      </div>

      <!-- 右侧内容区域 -->
      <el-scrollbar class="content-scrollbar">
        <!-- 试题题干 -->
        <div class="form-section">
          <div class="section-header">
            {{ currentQuestionType }} - 试题题干
          </div>
          <el-input
            v-model="questionFrom.title"
            placeholder="请输入试题题干"
            type="textarea"
            :rows="3"
            class="title-input"
          />
        </div>

        <!-- 答案选项 -->
        <div class="form-section">
          <div class="section-header">答案选项</div>

          <!-- 选项列表 -->
          <div
            v-for="(item, index) in questionFrom.questionOptions"
            :key="index"
            class="option-item"
          >
            <div class="option-label">选项{{ labels[index] }}</div>
            <el-input
              v-model="item.optionContent"
              :placeholder="`请输入选项${labels[index]}的内容`"
              type="textarea"
              :rows="2"
              class="option-input"
            />
          </div>

          <!-- 题目分类 -->
          <div class="category-section">
            <label class="category-label">题目分类：</label>
            <el-select
              v-model="questionFrom.categoryId"
              placeholder="请选择题目分类"
              clearable
              class="category-select"
            >
              <el-option
                v-for="cat in category"
                :key="cat.id"
                :label="cat.typeName"
                :value="cat.id"
              />
            </el-select>
          </div>

          <!-- 正确答案 -->
          <div class="correct-answer-section">
            <div class="answer-title">正确答案：</div>

            <!-- 多选题 -->
            <el-checkbox-group
              v-if="currentQuestionType === '多选题'"
              v-model="checkList"
              class="checkbox-group"
            >
              <el-checkbox
                v-for="(item, index) in questionFrom.questionOptions"
                :key="index"
                :label="index"
                :disabled="index >= num"
                class="answer-checkbox"
              >
                选项 {{ labels[index] }}
              </el-checkbox>
            </el-checkbox-group>

            <!-- 单选题/判断题 -->
            <el-radio-group
              v-else
              v-model="right"
              class="radio-group"
            >
              <el-radio
                v-for="(item, index) in questionFrom.questionOptions"
                :key="index"
                :label="index"
                :disabled="index >= num"
                class="answer-radio"
              >
                选项 {{ labels[index] }}
              </el-radio>
            </el-radio-group>
          </div>

          <!-- 提交按钮 -->
          <el-button
            type="primary"
            @click="handleSubmit"
            class="submit-button"
          >
            {{ isEditMode ? '修改' : '保存' }}
          </el-button>
        </div>
      </el-scrollbar>
    </div>
  </el-card>
</template>

<style scoped>
.question-card {
  height: 86vh;
}

.card-title {
  font-weight: bold;
  font-size: 20px;
}

.content-layout {
  display: flex;
  justify-content: space-between;
  gap: 40px;
  height: 100%;
}

.question-type-section {
  display: flex;
  flex-direction: column;
  width: 160px;
  flex-shrink: 0;
}

.section-title {
  margin-bottom: 16px;
  font-weight: bold;
  font-size: 16px;
}

.type-radio-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.type-radio {
  width: 100%;
  height: 48px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  border-radius: 6px;
  transition: all 0.3s;
}

.type-radio:hover {
  background-color: #f5f7fa;
}

:deep(.type-radio .el-radio__label) {
  font-size: 14px;
}

.readonly-type {
  margin-top: 8px;
}

.content-scrollbar {
  flex: 1;
  height: 500px;
}

.form-section {
  margin-bottom: 32px;
}

.section-header {
  position: relative;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: 700;
  color: #333;
  padding-left: 12px;
}

.section-header::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  transform: translateY(-50%);
  width: 4px;
  height: 16px;
  background-color: #36aafd;
  border-radius: 2px;
}

.title-input {
  width: 100%;
}

:deep(.title-input .el-textarea__inner) {
  min-height: 80px;
  resize: vertical;
}

.option-item {
  margin-bottom: 16px;
}

.option-label {
  margin-bottom: 8px;
  font-weight: 500;
  color: #606266;
}

.option-input {
  width: 100%;
}

:deep(.option-input .el-textarea__inner) {
  min-height: 60px;
  resize: vertical;
}

.category-section {
  margin: 24px 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.category-label {
  font-weight: 500;
  color: #606266;
  white-space: nowrap;
}

.category-select {
  width: 240px;
}

.correct-answer-section {
  margin: 24px 0;
}

.answer-title {
  margin-bottom: 16px;
  color: #e6a23c;
  font-weight: 500;
}

.checkbox-group,
.radio-group {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.answer-checkbox,
.answer-radio {
  margin-right: 0;
}

.submit-button {
  margin-top: 24px;
  width: 120px;
  height: 40px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .content-layout {
    flex-direction: column;
    gap: 20px;
  }

  .question-type-section {
    width: 100%;
  }

  .type-radio-group {
    flex-direction: row;
    flex-wrap: wrap;
  }

  .type-radio {
    flex: 1;
    min-width: 120px;
  }
}
</style>