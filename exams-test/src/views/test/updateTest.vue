<template>
  <div class="paper-edit-container" v-loading="loading">
    <!-- 试卷信息编辑区域 -->
    <el-card class="paper-info-card">
      <template #header>
        <div class="card-header">
          <span class="header-title">试卷信息</span>
          <div class="header-actions">
            <el-button type="primary" @click="savePaperInfo" :loading="saving">
              保存试卷信息
            </el-button>
            <el-button @click="cancelEdit">取消</el-button>
          </div>
        </div>
      </template>

      <el-form ref="paperFormRef" :model="paperForm" :rules="paperRules" label-width="100px">
        <!-- 第一行：试卷名称和试卷总分 -->
        <div class="form-row symmetric-row">
          <el-form-item label="试卷名称" prop="paperTitle" class="form-item symmetric-item">
            <el-input
              v-model="paperForm.paperTitle"
              placeholder="请输入试卷名称"
              maxlength="100"
              show-word-limit
              class="symmetric-input"
            />
          </el-form-item>

          <el-form-item
            label="试卷总分"
            prop="totalScore"
            class="form-item symmetric-item"
            :class="{'score-error': isTotalScoreError}"
          >
            <div class="score-input-wrapper">
              <el-input
                v-model="paperForm.totalScore"
                placeholder="请输入总分"
                type="number"
                min="0"
                max="1000"
                @input="handleTotalScoreInput"
                @blur="validateTotalScore"
                class="symmetric-input plain-input"
                :class="{'input-error': isTotalScoreError}"
                ref="totalScoreInputRef"
              />
              <span class="unit-text">分</span>
              <div class="score-validation" v-if="isTotalScoreError">
                <el-icon color="#F56C6C" size="14"><Warning /></el-icon>
                <span class="error-text">题目总分: {{ formatNumber(computedQuestionTotalScore) }}分</span>
              </div>
            </div>
          </el-form-item>
        </div>

        <!-- 第二行：开始时间和结束时间 -->
        <div class="form-row symmetric-row">
          <el-form-item label="开始时间" prop="startTime" class="form-item symmetric-item">
            <el-date-picker
              v-model="paperForm.startTime"
              type="datetime"
              placeholder="选择开始时间"
              value-format="YYYY-MM-DD HH:mm:ss"
              class="symmetric-date-picker"
              :disabled-date="disabledStartDate"
            />
          </el-form-item>

          <el-form-item label="结束时间" prop="endTime" class="form-item symmetric-item">
            <el-date-picker
              v-model="paperForm.endTime"
              type="datetime"
              placeholder="选择结束时间"
              value-format="YYYY-MM-DD HH:mm:ss"
              class="symmetric-date-picker"
              :disabled-date="disabledEndDate"
            />
          </el-form-item>
        </div>

        <!-- 第三行：考试时长和试卷状态 -->
        <div class="form-row symmetric-row">
          <el-form-item label="考试时长" prop="duration" class="form-item symmetric-item">
            <div class="duration-input-wrapper">
              <el-input
                v-model="paperForm.duration"
                placeholder="请输入时长"
                type="number"
                min="1"
                max="600"
                @input="handleDurationInput"
                class="symmetric-input plain-input"
              />
              <span class="unit-text">分钟</span>
            </div>
          </el-form-item>

          <el-form-item label="试卷状态" prop="status" class="form-item symmetric-item">
            <el-select
              v-model="paperForm.status"
              placeholder="请选择状态"
              class="symmetric-select"
            >
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
        </div>

        <!-- 第四行：试卷描述 -->
        <div class="form-row full-row">
          <el-form-item label="试卷描述" prop="description" class="form-item full-item">
            <el-input
              v-model="paperForm.description"
              type="textarea"
              :rows="4"
              placeholder="请输入试卷描述"
              maxlength="500"
              show-word-limit
              class="symmetric-textarea"
            />
          </el-form-item>
        </div>
      </el-form>
    </el-card>

    <!-- 题目管理区域 -->
    <el-card class="questions-card">
      <template #header>
        <div class="card-header">
          <span class="header-title">题目管理</span>
          <div class="header-actions">
            <el-button type="primary" @click="addQuestion" icon="Plus">
              添加题目
            </el-button>
            <el-button type="warning" @click="openTypeScoreDialog" icon="Edit">
              设置题型分数
            </el-button>
            <el-button @click="refreshQuestions" icon="Refresh">
              刷新
            </el-button>
          </div>
        </div>
      </template>

      <!-- 分数校验提示 -->
      <div v-if="isScoreMismatch" class="score-warning-card">
        <el-alert
          title="分数不一致"
          type="warning"
          :closable="false"
          show-icon
        >
          <div class="warning-content">
            <p>试卷总分: {{ formatNumber(paperForm.totalScore) }}分 ≠ 题目总分: {{ formatNumber(computedQuestionTotalScore) }}分</p>
            <p>相差: {{ formatNumber(Math.abs(scoreDifference)) }}分</p>
            <div class="warning-actions">
              <el-button type="warning" size="small" @click="openTypeScoreDialog">
                设置题型分数
              </el-button>
            </div>
          </div>
        </el-alert>
      </div>

      <!-- 题目列表 -->
      <div v-if="questions.length > 0">
        <!-- 按题型分组显示 -->
        <div v-for="(section, sectionIndex) in groupedQuestions" :key="`${section.type}-${sectionIndex}`" class="question-section">
          <div class="section-header">
            <h3 class="section-title">
              {{ getSectionTitle(section.type, sectionIndex, section.questions.length) }}
            </h3>
            <span class="section-count">共{{ section.questions.length }}题</span>
            <span class="section-total-score">总分: {{ formatNumber(getSectionTotalScore(section.type)) }}分</span>
          </div>

          <el-table
            :data="section.questions"
            :border="true"
            :stripe="true"
            class="question-table"
            row-key="id"
          >
            <el-table-column type="index" label="序号" width="80" align="center">
              <template #default="scope">
                <span class="question-index">{{ getQuestionNumber(section.type, scope.$index) }}</span>
              </template>
            </el-table-column>

            <el-table-column prop="questionType" label="题型" width="100" align="center">
              <template #default="scope">
                <el-tag :type="getQuestionTypeTagType(scope.row.questionType)" size="small">
                  {{ scope.row.questionType }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="title" label="题目内容" min-width="300">
              <template #default="scope">
                <div class="question-content">
                  <div class="question-title" v-text="truncateText(scope.row.title, 80)"></div>
                  <div class="question-meta" v-if="scope.row.questionOptions && ['单选题', '多选题', '判断题'].includes(scope.row.questionType)">
                    <span class="meta-item">选项数: {{ scope.row.questionOptions.length }}</span>
                  </div>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="score" label="分值" width="100" align="center">
              <template #default="scope">
                <span class="question-score">{{ formatNumber(scope.row.score) }}分</span>
              </template>
            </el-table-column>

            <el-table-column label="答题统计" width="150" align="center">
              <template #default="scope">
                <div class="question-stats">
                  <div class="stat-item">答题: {{ scope.row.totalAnswerCount || 0 }}</div>
                  <div class="stat-item">错误率: {{ calculateErrorRate(scope.row) }}%</div>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="tags" label="标签" width="150">
              <template #default="scope">
                <div class="question-tags">
                  <el-tag
                    v-for="tag in getQuestionTags(scope.row.tags).slice(0, 2)"
                    :key="tag"
                    size="small"
                    class="tag-item"
                  >
                    {{ tag }}
                  </el-tag>
                  <span v-if="getQuestionTags(scope.row.tags).length > 2" class="more-tags">...</span>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="操作" width="120" align="center" fixed="right">
              <template #default="scope">
                <div class="action-buttons">
                  <el-button
                    type="primary"
                    size="small"
                    @click="editQuestion(scope.row)"
                    icon="Edit"
                  >
                    编辑
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <el-empty description="暂无题目数据">
          <template #description>
            <p class="empty-text">当前试卷还没有题目，请点击"添加题目"按钮添加题目</p>
          </template>
          <el-button type="primary" @click="addQuestion">添加题目</el-button>
        </el-empty>
      </div>
    </el-card>

    <!-- 设置题型分数对话框 -->
    <el-dialog
      v-model="typeScoreDialogVisible"
      title="设置题型分数"
      width="700px"
      :before-close="handleTypeScoreDialogClose"
    >
      <div class="type-score-dialog">
        <el-alert
          type="info"
          :closable="false"
          show-icon
          class="dialog-alert"
        >
          <div class="alert-content">
            <p>设置每种题型每题的分值，系统会自动计算该题型总分</p>
            <p>注意：设置后会统一修改该题型下所有题目的分数</p>
          </div>
        </el-alert>

        <div class="type-score-table">
          <el-table
            :data="typeScoreData"
            :border="true"
            :stripe="true"
            class="score-table"
          >
            <el-table-column prop="questionType" label="题型" width="120" align="center">
              <template #default="scope">
                <el-tag :type="getQuestionTypeTagType(scope.row.questionType)" size="small">
                  {{ scope.row.questionType }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="count" label="题目数量" width="100" align="center">
              <template #default="scope">
                <span class="type-count">{{ scope.row.count }}</span>
              </template>
            </el-table-column>

            <el-table-column label="每题分值" width="150" align="center">
              <template #default="scope">
                <el-input
                  v-model.number="scope.row.typeScore"
                  type="number"
                  placeholder="请输入分数"
                  size="small"
                  :min="0.1"
                  :max="100"
                  :step="0.5"
                  @input="handleTypeScoreChange(scope.row)"
                  @blur="formatTypeScore(scope.row)"
                  style="width: 120px;"
                >
                  <template #append>分</template>
                </el-input>
              </template>
            </el-table-column>

            <el-table-column label="题型总分" width="120" align="center">
              <template #default="scope">
                <span class="type-total-score">{{ formatTypeTotalScore(scope.row) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="dialog-summary">
          <div class="summary-row">
            <div class="summary-item">
              <span class="summary-label">试卷总分:</span>
              <span class="summary-value">{{ formatNumber(paperForm.totalScore) }}分</span>
            </div>
            <div class="summary-item">
              <span class="summary-label">设置后题目总分:</span>
              <span class="summary-value" :class="{'summary-error': typeScoreTotal > paperForm.totalScore}">
                {{ formatNumber(typeScoreTotal) }}分
              </span>
            </div>
            <div class="summary-item" v-if="Math.abs(typeScoreTotal - paperForm.totalScore) > 0.01">
              <span class="summary-label">差异:</span>
              <span class="summary-difference" :class="{'summary-error': typeScoreTotal > paperForm.totalScore}">
                {{ typeScoreTotal > paperForm.totalScore ? '超出' : '缺少' }}
                {{ formatNumber(Math.abs(typeScoreTotal - paperForm.totalScore)) }}分
              </span>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="typeScoreDialogVisible = false">取消</el-button>
          <el-button type="warning" @click="autoDistributeScores">
            智能分配
          </el-button>
          <el-button
            type="primary"
            @click="saveTypeScores"
            :loading="savingTypeScores"
            :disabled="typeScoreTotal - paperForm.totalScore > 0.01"
          >
            保存设置
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { ElMessage, ElMessageBox, ElCard, ElForm, ElFormItem, ElInput, ElSelect,
  ElOption, ElDatePicker, ElButton, ElTable, ElTableColumn, ElTag, ElEmpty, ElAlert, ElDialog, ElIcon } from 'element-plus'
import { Plus, Refresh, Edit, Warning } from '@element-plus/icons-vue'
import { useRoute, useRouter } from 'vue-router'

// 导入API（请确保路径正确）
import { getTestParticulars, updateTestPaper, updateTypeScore } from '@/api/testPaper'

const route = useRoute()
const router = useRouter()
const paperId = ref(Number(route.params.id))

// 加载状态
const loading = ref(false)
const saving = ref(false)
const savingTypeScores = ref(false)

// 试卷表单数据
const paperForm = ref({
  paperId: '',
  paperTitle: '',
  totalScore: 100,
  startTime: '',
  endTime: '',
  duration: 120,
  status: 1,
  description: ''
})

// 自定义验证器
const validateNumber = (rule, value, callback) => {
  // 修复：正确处理0值和空值
  if (value === '' || value === undefined || value === null) {
    if (rule.required) {
      callback(new Error(rule.message || '不能为空'))
    } else {
      callback()
    }
    return
  }

  const numValue = Number(value)

  if (isNaN(numValue)) {
    callback(new Error(rule.message || '请输入有效数字'))
    return
  }

  if (rule.min !== undefined && numValue < rule.min) {
    callback(new Error(`值不能小于${rule.min}`))
    return
  }

  if (rule.max !== undefined && numValue > rule.max) {
    callback(new Error(`值不能大于${rule.max}`))
    return
  }

  if (rule.pattern && !rule.pattern.test(value)) {
    callback(new Error(rule.message || '格式不正确'))
    return
  }

  callback()
}

// 试卷总分验证器 - 修复逻辑冲突：移除强制≥题目总分的校验，改为仅提示
const validateTotalScoreRule = (rule, value, callback) => {
  if (value === '' || value === undefined || value === null) {
    callback(new Error('请输入试卷总分'))
    return
  }

  const numValue = Number(value)

  if (isNaN(numValue)) {
    callback(new Error('请输入有效数字'))
    return
  }

  if (numValue < 1) {
    callback(new Error('试卷总分必须大于0'))
    return
  }

  if (numValue > 1000) {
    callback(new Error('试卷总分不能超过1000分'))
    return
  }

  // 仅提示不阻止保存
  if (questions.value.length > 0 && numValue < computedQuestionTotalScore.value) {
    ElMessage.warning(`试卷总分(${numValue}分)小于题目总分(${formatNumber(computedQuestionTotalScore.value)}分)，建议调整`)
  }

  callback()
}

// 结束时间验证器
const validateEndTime = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请选择结束时间'))
    return
  }

  if (!paperForm.value.startTime) {
    callback()
    return
  }

  const startTime = new Date(paperForm.value.startTime)
  const endTime = new Date(value)

  if (endTime <= startTime) {
    callback(new Error('结束时间必须晚于开始时间'))
    return
  }

  callback()
}

// 试卷表单验证规则
const paperRules = {
  paperTitle: [
    { required: true, message: '请输入试卷名称', trigger: 'blur' },
    { min: 1, max: 100, message: '试卷名称长度在1-100个字符之间', trigger: 'blur' }
  ],
  totalScore: [
    { required: true, validator: validateTotalScoreRule, trigger: ['blur', 'change'] }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, validator: validateEndTime, trigger: 'change' }
  ],
  duration: [
    { required: true, validator: validateNumber, message: '请输入考试时长', min: 1, max: 600, trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择试卷状态', trigger: 'change' }
  ]
}

// 题目列表数据
const questions = ref([])

// 题型分数对话框相关 - 新增totalTypeScore字段（题型总分）
const typeScoreDialogVisible = ref(false)
const typeScoreData = ref([])
const originalTypeScores = ref(new Map())

// DOM引用
const paperFormRef = ref()
const totalScoreInputRef = ref()

// 获取试卷详情
const fetchPaperDetail = async () => {
  if (!paperId.value || isNaN(paperId.value)) {
    ElMessage.error('试卷ID不存在或格式错误')
    return
  }

  loading.value = true
  try {
    const res = await getTestParticulars(paperId.value)

    if (res.data && res.data.code === 1) {
      const data = res.data.data

      // 填充试卷表单数据 - 修复0值被替换的问题
      paperForm.value = {
        paperId: data.paper.paperId,
        paperTitle: data.paper.paperTitle,
        totalScore: isNaN(Number(data.paper.totalScore)) ? 100 : Number(data.paper.totalScore),
        startTime: data.paper.startTime,
        endTime: data.paper.endTime,
        duration: isNaN(Number(data.paper.duration)) ? 120 : Number(data.paper.duration),
        status: data.paper.status,
        description: data.paper.description || ''
      }

      // 填充题目数据
      questions.value = (data.questionVOList || []).map((question, index) => ({
        ...question,
        id: question.id || `temp-${index}`,
        score: Number(question.score) || 0
      }))

      // 【新增】检查0分题目
      checkZeroScoreQuestions()

      ElMessage.success('试卷数据加载成功')
    } else {
      ElMessage.error(res.data?.msg || '获取试卷详情失败')
    }
  } catch (error) {
    console.error('获取试卷详情错误:', error)
    ElMessage.error('获取试卷详情失败: ' + (error.message || '网络错误'))
  } finally {
    loading.value = false
  }
}

// 仅刷新题目数据（不重置表单）- 修复表单数据丢失问题
const fetchQuestionsOnly = async () => {
  if (!paperId.value || isNaN(paperId.value)) return

  try {
    const res = await getTestParticulars(paperId.value)
    if (res.data?.code === 1) {
      const data = res.data.data
      questions.value = (data.questionVOList || []).map((question, index) => ({
        ...question,
        id: question.id || `temp-${index}`,
        score: Number(question.score) || 0
      }))

      // 【新增】检查0分题目
      checkZeroScoreQuestions()
    }
  } catch (error) {
    console.error('刷新题目数据错误:', error)
    ElMessage.error('刷新题目数据失败')
  }
}

// 计算属性：当前所有题目总分
const computedQuestionTotalScore = computed(() => {
  return questions.value.reduce((sum, question) => {
    return sum + (Number(question.score) || 0)
  }, 0)
})

// 计算属性：试卷总分与题目总分之差
const scoreDifference = computed(() => {
  return Number(paperForm.value.totalScore) - computedQuestionTotalScore.value
})

// 计算属性：分数是否匹配
const isScoreMismatch = computed(() => {
  return Math.abs(scoreDifference.value) > 0.01
})

// 计算属性：试卷总分是否错误
const isTotalScoreError = computed(() => {
  return isScoreMismatch.value && scoreDifference.value < 0
})

// 计算属性：按题型分组题目
const groupedQuestions = computed(() => {
  if (!Array.isArray(questions.value) || questions.value.length === 0) {
    return []
  }

  const typeOrder = ['单选题', '多选题', '判断题', '填空题', '简答题']
  const grouped = {}

  questions.value.forEach(question => {
    const type = question.questionType || '其他'
    if (!grouped[type]) {
      grouped[type] = []
    }
    grouped[type].push(question)
  })

  return typeOrder
    .filter(type => grouped[type] && grouped[type].length > 0)
    .map(type => ({
      type,
      questions: grouped[type]
    }))
})

// 计算属性：设置题型分数后的总分
const typeScoreTotal = computed(() => {
  return typeScoreData.value.reduce((total, item) => {
    const score = Number(item.typeScore) || 0
    const count = Number(item.count) || 0
    return total + (score * count)
  }, 0)
})

// 处理试卷总分输入
const handleTotalScoreInput = () => {
  // 移除之前可能添加的错误类
  if (totalScoreInputRef.value) {
    const inputEl = totalScoreInputRef.value.$el.querySelector('.el-input__wrapper')
    if (inputEl) {
      inputEl.classList.remove('error')
    }
  }
}

// 验证试卷总分
const validateTotalScore = () => {
  const totalScoreNum = Number(paperForm.value.totalScore)

  if (isNaN(totalScoreNum)) {
    paperForm.value.totalScore = 100
    return false
  }

  return true
}

// 处理考试时长输入 - 修复replace类型错误
const handleDurationInput = (value) => {
  // 先转为字符串，过滤非数字，空值置为1（符合min:1规则）
  const strValue = String(value).replace(/[^\d]/g, '')
  paperForm.value.duration = strValue || '1'
}

// 禁用开始时间 - 修复日期精度问题，禁用过去的日期
const disabledStartDate = (time) => {
  const today = new Date()
  today.setHours(0, 0, 0, 0) // 重置为当天0点，仅对比日期维度
  return time && time.getTime() < today.getTime()
}

// 禁用结束时间 - 修复日期精度问题，仅对比日期维度
const disabledEndDate = (time) => {
  if (!paperForm.value.startTime) return false
  const startDate = new Date(paperForm.value.startTime)
  startDate.setHours(0, 0, 0, 0) // 仅对比日期
  return time && time.getTime() < startDate.getTime()
}

// 保存试卷信息
const savePaperInfo = async () => {
  try {
    await validateForm()
  } catch (error) {
    ElMessage.warning('请完善试卷信息')
    return
  }

  saving.value = true
  try {
    const formData = {
      ...paperForm.value,
      totalScore: Number(paperForm.value.totalScore),
      duration: Number(paperForm.value.duration)
    }

    const res = await updateTestPaper(formData)

    if (res.data && res.data.code === 1) {
      ElMessage.success('试卷信息保存成功')
      fetchPaperDetail()
    } else {
      ElMessage.error(res.data?.msg || '保存试卷信息失败')
    }
  } catch (error) {
    console.error('保存试卷信息错误:', error)
    ElMessage.error('保存试卷信息失败: ' + (error.message || '网络错误'))
  } finally {
    saving.value = false
  }
}

// 表单验证
const validateForm = () => {
  return new Promise((resolve, reject) => {
    if (!paperFormRef.value) {
      resolve()
      return
    }

    paperFormRef.value.validate((valid) => {
      if (valid) {
        resolve()
      } else {
        reject(new Error('表单验证失败'))
      }
    })
  })
}

// 取消编辑
const cancelEdit = () => {
  router.go(-1)
}

// 添加题目
const addQuestion = () => {
  router.push({
    path: '/article/test/add-questions',
    query: {
      paperId: paperId.value
    }
  })
}

// 编辑题目
const editQuestion = (question) => {
  if (!question.id) {
    ElMessage.warning('题目信息不完整')
    return
  }
  router.push({
    path: '/article/question/add/',
    query: { id: question.id }
  })
}

// 刷新题目列表
const refreshQuestions = () => {
  fetchQuestionsOnly()
  ElMessage.info('正在刷新题目数据...')
}

// 打开设置题型分数对话框 - 核心修改：新增totalTypeScore字段
const openTypeScoreDialog = () => {
  if (!Array.isArray(questions.value) || questions.value.length === 0) {
    ElMessage.warning('当前试卷没有题目，无法设置题型分数')
    return
  }

  // 按题型分组统计
  const typeStats = {}
  const typeOrder = ['单选题', '多选题', '判断题', '填空题', '简答题']

  questions.value.forEach(question => {
    const type = question.questionType || '其他'
    if (!typeStats[type]) {
      typeStats[type] = {
        count: 0,
        totalScore: 0
      }
    }
    typeStats[type].count += 1
    typeStats[type].totalScore += Number(question.score) || 0
  })

  originalTypeScores.value.clear()

  // 按typeOrder排序生成对话框数据，新增totalTypeScore
  typeScoreData.value = typeOrder
    .filter(type => typeStats[type])
    .map(type => {
      const stats = typeStats[type]
      // 计算每题平均分，四舍五入保留2位小数
      const avgScore = stats.count > 0 ? Math.round((stats.totalScore / stats.count) * 100) / 100 : 0
      // 新增：计算题型总分（每题分值×数量）
      const totalTypeScore = avgScore * stats.count

      originalTypeScores.value.set(type, avgScore)

      return {
        questionType: type,
        count: stats.count,
        typeScore: avgScore, // 每题分值（用户输入/显示）
        totalTypeScore: totalTypeScore // 题型总分（传给后端）
      }
    })

  typeScoreDialogVisible.value = true
}

// 处理题型分数变化 - 核心修改：同步更新totalTypeScore
const handleTypeScoreChange = (row) => {
  const score = Number(row.typeScore)
  if (isNaN(score) || score < 0) {
    row.typeScore = 0
  } else if (score > 100) {
    row.typeScore = 100
    ElMessage.warning('每题分数不能超过100分')
  }
  // 同步更新题型总分
  row.totalTypeScore = row.typeScore * row.count
}

// 格式化题型分数 - 核心修改：同步更新totalTypeScore
const formatTypeScore = (row) => {
  if (row.typeScore === undefined || row.typeScore === null) {
    row.typeScore = 0
    row.totalTypeScore = 0
    return
  }

  const score = Number(row.typeScore)
  if (isNaN(score)) {
    row.typeScore = 0
  } else {
    // 保留2位小数，但不显示多余的0
    row.typeScore = Math.round(score * 100) / 100
  }
  // 同步更新题型总分
  row.totalTypeScore = row.typeScore * row.count
}

// 格式化题型总分（重命名优化语义）
const formatTypeTotalScore = (row) => {
  const score = Number(row.typeScore) || 0
  const count = Number(row.count) || 0
  const total = Math.round((score * count) * 100) / 100
  return formatNumber(total) + '分'
}

// 智能分配分数 - 保留原有正确逻辑，新增totalTypeScore同步
const autoDistributeScores = () => {
  if (!Array.isArray(typeScoreData.value) || typeScoreData.value.length === 0) {
    ElMessage.warning('没有可以分配的题型数据')
    return
  }

  const totalQuestions = typeScoreData.value.reduce((sum, item) => sum + (Number(item.count) || 0), 0)
  const totalScore = Number(paperForm.value.totalScore)

  if (totalQuestions === 0) {
    ElMessage.warning('没有题目可以分配分数')
    return
  }

  if (totalScore <= 0) {
    ElMessage.warning('试卷总分必须大于0')
    return
  }

  // 题型权重配置（根据难度和重要性）
  const typeWeights = {
    '简答题': 5,
    '填空题': 4,
    '多选题': 3,
    '单选题': 2,
    '判断题': 1,
    '其他': 1
  }

  // 计算总权重
  let totalWeight = 0
  const typeInfo = []

  typeScoreData.value.forEach(item => {
    const weight = typeWeights[item.questionType] || 1
    const questionCount = Number(item.count) || 0
    totalWeight += weight * questionCount

    typeInfo.push({
      type: item.questionType,
      count: questionCount,
      weight: weight
    })
  })

  if (totalWeight === 0) {
    ElMessage.warning('无法计算权重，请检查题目数据')
    return
  }

  // 第一次分配：按权重分配整数分数
  let remainingScore = totalScore
  const allocatedScores = new Map()
  const floatScores = []

  typeInfo.forEach(info => {
    const weightRatio = (info.weight * info.count) / totalWeight
    const allocated = Math.floor(totalScore * weightRatio)

    // 确保至少分配一定的分数
    let perQuestion = 0
    if (info.count > 0) {
      perQuestion = Math.max(0.5, allocated / info.count)

      // 根据题型设置最小最大值
      const minScore = getMinScoreForType(info.type)
      const maxScore = getMaxScoreForType(info.type)
      perQuestion = Math.max(minScore, Math.min(maxScore, perQuestion))

      // 计算实际分配的整数总分
      const actualTotal = Math.floor(perQuestion * info.count * 100) / 100
      remainingScore -= actualTotal

      allocatedScores.set(info.type, {
        perQuestion,
        total: actualTotal
      })

      // 记录浮点数部分用于后续调整
      floatScores.push({
        type: info.type,
        count: info.count,
        floatPart: perQuestion - Math.floor(perQuestion)
      })
    }
  })

  // 处理剩余分数（按浮点数部分分配）
  if (remainingScore > 0) {
    // 按浮点数部分降序排序
    floatScores.sort((a, b) => b.floatPart - a.floatPart)

    for (let i = 0; i < floatScores.length && remainingScore > 0; i++) {
      const item = floatScores[i]
      const allocation = allocatedScores.get(item.type)

      if (allocation && item.count > 0) {
        // 每道题增加0.01分
        const increment = 0.01
        const totalIncrement = increment * item.count

        if (totalIncrement <= remainingScore) {
          allocation.perQuestion += increment
          allocation.total += totalIncrement
          remainingScore -= totalIncrement
        }
      }
    }
  }

  // 如果还有剩余分数，按题目数量平均分配
  if (remainingScore > 0) {
    const perQuestionIncrement = remainingScore / totalQuestions
    allocatedScores.forEach((allocation, type) => {
      const typeInfoItem = typeInfo.find(item => item.type === type)
      if (typeInfoItem && typeInfoItem.count > 0) {
        allocation.perQuestion += perQuestionIncrement
        allocation.total += perQuestionIncrement * typeInfoItem.count
      }
    })
  }

  // 应用分配结果 - 同步更新totalTypeScore
  allocatedScores.forEach((allocation, type) => {
    const item = typeScoreData.value.find(item => item.questionType === type)
    if (item) {
      // 四舍五入保留2位小数
      item.typeScore = Math.round(allocation.perQuestion * 100) / 100
      // 同步更新题型总分
      item.totalTypeScore = item.typeScore * item.count
    }
  })

  // 最终调整确保总分精确匹配
  adjustScoresForExactMatch()

  // 【新增】四舍五入优化：如果最终总分与目标差异极小（如0.01），进行微调
  const finalDiff = totalScore - typeScoreTotal.value
  if (Math.abs(finalDiff) >= 0.01 && Math.abs(finalDiff) < 0.1) {
    // 找到分数最高的题型，进行微调
    const sortedTypes = [...typeScoreData.value].sort((a, b) => b.typeScore - a.typeScore)
    const targetType = sortedTypes.find(item => item.count > 0 && item.typeScore < getMaxScoreForType(item.questionType))

    if (targetType) {
      targetType.typeScore = Math.round((targetType.typeScore + finalDiff / targetType.count) * 100) / 100
      targetType.totalTypeScore = targetType.typeScore * targetType.count
      ElMessage.info(`已自动微调${targetType.questionType}分数，确保总分精确匹配`)
    }
  }

  // 【新增】检查是否需要四舍五入到100（如99.99→100）
  if (Math.abs(typeScoreTotal.value - 99.99) < 0.01 && totalScore === 100) {
    // 差异为0.01，找到最高分的题目加0.01
    const maxItem = typeScoreData.value.reduce((max, item) =>
      item.typeScore > max.typeScore ? item : max, typeScoreData.value[0])
    if (maxItem) {
      maxItem.typeScore = Math.round((maxItem.typeScore + 0.01 / maxItem.count) * 100) / 100
      maxItem.totalTypeScore = maxItem.typeScore * maxItem.count
      ElMessage.success('已自动四舍五入至目标总分')
    }
  }

  ElMessage.success('分数分配完成')
}

// 获取题型最小分数
const getMinScoreForType = (type) => {
  const minScores = {
    '简答题': 5,
    '填空题': 2,
    '多选题': 2,
    '单选题': 1,
    '判断题': 0.5,
    '其他': 1
  }
  return minScores[type] || 1
}

// 获取题型最大分数
const getMaxScoreForType = (type) => {
  const maxScores = {
    '简答题': 20,
    '填空题': 10,
    '多选题': 10,
    '单选题': 5,
    '判断题': 3,
    '其他': 10
  }
  return maxScores[type] || 10
}

// 精确匹配调整 - 核心修改：同步更新totalTypeScore
const adjustScoresForExactMatch = () => {
  const targetTotal = Number(paperForm.value.totalScore);
  let currentTotal = typeScoreTotal.value;
  let diff = targetTotal - currentTotal;

  if (Math.abs(diff) < 0.01) return;

  const validTypes = typeScoreData.value.filter(item => item.count > 0);
  if (validTypes.length === 0) return;

  // 优先调整分数最高的题型，避免低于最小值
  validTypes.sort((a, b) => b.typeScore - a.typeScore);

  let remainingDiff = diff;
  validTypes.forEach(item => {
    if (Math.abs(remainingDiff) < 0.01) return;

    const minScore = getMinScoreForType(item.questionType);
    const maxScore = getMaxScoreForType(item.questionType);
    // 单题可调整的最大幅度
    const maxAdjust = remainingDiff > 0
      ? maxScore - item.typeScore // 增加：最多到maxScore
      : item.typeScore - minScore; // 减少：最少到minScore

    if (maxAdjust <= 0) return;

    // 计算本次调整量（按题目数分摊）
    const adjustPerQuestion = Math.sign(remainingDiff) * Math.min(
      Math.abs(remainingDiff) / item.count,
      maxAdjust
    );
    // 应用调整
    item.typeScore = Number((item.typeScore + adjustPerQuestion).toFixed(2));
    // 同步更新题型总分
    item.totalTypeScore = item.typeScore * item.count;
    remainingDiff -= adjustPerQuestion * item.count;
  });
}

// 保存题型分数设置 - 核心修改：传给后端的typeScore使用totalTypeScore
const saveTypeScores = async () => {
  // 验证题型分数
  const invalidItems = []
  for (const item of typeScoreData.value) {
    const score = Number(item.typeScore)

    if (isNaN(score) || score < 0) {
      invalidItems.push(`"${item.questionType}"的分数无效`)
    }

    if (score > 100) {
      invalidItems.push(`"${item.questionType}"每题分数不能超过100分`)
    }

    // 检查是否小于最小分数
    const minScore = getMinScoreForType(item.questionType)
    if (score < minScore && score > 0) {
      invalidItems.push(`"${item.questionType}"每题分数不能低于${minScore}分`)
    }
  }

  if (invalidItems.length > 0) {
    ElMessage.warning(invalidItems.join('；'))
    return
  }

  // 检查总分
  const calculatedTotal = typeScoreTotal.value
  const paperTotal = Number(paperForm.value.totalScore)
  const diff = Math.abs(calculatedTotal - paperTotal)

  if (diff > 0.01) {
    if (calculatedTotal > paperTotal) {
      ElMessage.error(`设置后的总分${formatNumber(calculatedTotal)}分超过了试卷总分${formatNumber(paperTotal)}分`)
      return
    }

    // 分数不足时给出确认提示
    try {
      await ElMessageBox.confirm(
        `设置后的总分${formatNumber(calculatedTotal)}分少于试卷总分${formatNumber(paperTotal)}分，是否继续保存？`,
        '分数不足确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
    } catch {
      return // 用户取消
    }
  }

  savingTypeScores.value = true
  try {
    // 核心修改：传给后端的typeScore使用totalTypeScore（题型总分）
    const requestData = {
      paperId: paperId.value,
      questionTypeCountVOList: typeScoreData.value.map(item => ({
        questionType: item.questionType,
        count: item.count,
        typeScore: item.totalTypeScore // 替换为题型总分（每题分值×数量）
      }))
    }
    console.log('适配后端的请求数据：', requestData); // 调试用

    const res = await updateTypeScore(requestData)

    if (res.data && res.data.code === 1) {
      ElMessage.success('题型分数设置成功')
      typeScoreDialogVisible.value = false
      // 仅刷新题目数据，不重置表单
      fetchQuestionsOnly()
    } else {
      ElMessage.error(res.data?.msg || '设置题型分数失败')
    }
  } catch (error) {
    console.error('设置题型分数错误:', error)
    console.error('请求详情：', error) // 打印完整错误信息
    ElMessage.error('设置题型分数失败: ' + (error.message || '网络错误'))
  } finally {
    savingTypeScores.value = false
  }
}

// 关闭题型分数对话框 - 修复未调用done导致无法关闭的问题
const handleTypeScoreDialogClose = (done) => {
  let hasChanges = false
  typeScoreData.value.forEach(item => {
    const originalScore = originalTypeScores.value.get(item.questionType) || 0
    const currentScore = Number(item.typeScore) || 0
    if (Math.abs(currentScore - originalScore) > 0.01) {
      hasChanges = true
    }
  })

  if (hasChanges) {
    ElMessageBox.confirm(
      '您有未保存的修改，确定要关闭吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      done() // 确认关闭
    }).catch(() => {
      done() // 取消关闭（必须调用，否则对话框卡住）
    })
  } else {
    done() // 无变化直接关闭
  }
}

// 格式化数字显示 - 修复后行断言兼容问题
const formatNumber = (num) => {
  const n = Number(num)
  if (isNaN(n)) return '0'

  // 如果是整数，直接显示
  if (Number.isInteger(n)) {
    return n.toString()
  }

  // 兼容写法：先转固定2位，再去掉末尾的0和小数点
  return n.toFixed(2).replace(/\.0+$/, '').replace(/(\.\d*?)0+$/, '$1')
}

// 获取题型标题
const getSectionTitle = (type, index, count) => {
  const sectionNames = ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十']
  const typeNames = {
    '单选题': '单项选择题',
    '多选题': '多项选择题',
    '判断题': '判断题',
    '填空题': '填空题',
    '简答题': '简答题',
    '其他': '其他题型'
  }

  const sectionName = sectionNames[index] || (index + 1)
  return `${sectionName}、${typeNames[type] || type}（共${count}题）`
}

// 获取题目序号 - 修复冗余参数问题
const getQuestionNumber = (type, index) => {
  let prevCount = 0
  for (let i = 0; i < groupedQuestions.value.length; i++) {
    const section = groupedQuestions.value[i]
    if (section.type === type) break
    prevCount += (section.questions || []).length
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
    '简答题': 'danger',
    '其他': 'info'
  }
  return typeMap[type] || 'info'
}

// 计算题型总分
const getSectionTotalScore = (type) => {
  const section = groupedQuestions.value.find(s => s.type === type)
  if (!section || !section.questions) return 0

  return section.questions.reduce((sum, q) => sum + (Number(q.score) || 0), 0)
}

// 计算错误率
const calculateErrorRate = (question) => {
  const total = question.totalAnswerCount || 0
  const wrong = question.wrongAnswerCount || 0

  if (total === 0) return '0.0'

  const errorRate = (wrong / total) * 100
  return errorRate.toFixed(1)
}

// 截断文本 - 安全版
const truncateText = (text, length) => {
  if (!text || typeof text !== 'string') return ''
  if (text.length <= length) return text
  return text.substring(0, length) + '...'
}

// 获取题目标签
const getQuestionTags = (tags) => {
  if (!tags) return []
  if (Array.isArray(tags)) return tags
  if (typeof tags === 'string') {
    return tags.split(',').map(tag => tag.trim()).filter(tag => tag)
  }
  return []
}

// 【新增】检查0分题目并提醒
const checkZeroScoreQuestions = () => {
  const zeroScoreQuestions = questions.value.filter(q => {
    const score = Number(q.score) || 0
    return score === 0
  })

  if (zeroScoreQuestions.length > 0) {
    ElMessage.warning({
      message: `检测到${zeroScoreQuestions.length}道0分题目，请及时设置分数`,
      duration: 5000,
      showClose: true
    })
  }
}

// 监听路由参数变化
watch(
  () => route.params.id,
  (newId) => {
    if (newId && !isNaN(Number(newId))) {
      const newIdNum = Number(newId)
      if (newIdNum !== paperId.value) {
        paperId.value = newIdNum
        fetchPaperDetail()
      }
    } else {
      // 重置数据
      paperId.value = null
      paperForm.value = {
        paperId: '',
        paperTitle: '',
        totalScore: 100,
        startTime: '',
        endTime: '',
        duration: 120,
        status: 1,
        description: ''
      }
      questions.value = []
      ElMessage.warning('试卷ID无效')
    }
  },
  { immediate: true }
)

// 监听题目列表变化，自动验证总分 - 修复空指针风险
watch(
  () => computedQuestionTotalScore.value,
  (newTotal) => {
    if (paperFormRef.value) {
      // 触发总分字段的验证
      paperFormRef.value.validateField('totalScore')
    }
  }
)

// 组件挂载
onMounted(() => {
  // 如果路由中已有ID，则加载数据
  if (paperId.value && !isNaN(paperId.value)) {
    fetchPaperDetail()
  }
})
</script>

<style scoped>
.paper-edit-container {
  padding: 20px;
}

.paper-info-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 16px;
  font-weight: bold;
}

.form-row {
  display: flex;
  margin-bottom: 20px;
}

.symmetric-row {
  gap: 20px;
}

.form-item {
  margin-bottom: 0;
}

.symmetric-item {
  flex: 1;
}

.full-row {
  margin-bottom: 0;
}

.full-item {
  width: 100%;
}

.symmetric-input,
.symmetric-select,
.symmetric-date-picker,
.symmetric-textarea {
  width: 100%;
}

.score-input-wrapper,
.duration-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.unit-text {
  margin-left: 8px;
  color: #606266;
  white-space: nowrap;
}

.score-validation {
  display: flex;
  align-items: center;
  margin-top: 4px;
  color: #F56C6C;
  font-size: 12px;
}

.error-text {
  margin-left: 4px;
}

.input-error {
  :deep(.el-input__wrapper) {
    box-shadow: 0 0 0 1px #F56C6C inset;
  }
}

.score-error {
  :deep(.el-form-item__label) {
    color: #F56C6C;
  }
}

.questions-card {
  margin-bottom: 20px;
}

.score-warning-card {
  margin-bottom: 20px;
}

.warning-content {
  line-height: 1.5;
}

.warning-actions {
  margin-top: 8px;
}

.question-section {
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #ebeef5;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  margin: 0;
  margin-right: 12px;
}

.section-count {
  color: #606266;
  font-size: 14px;
  margin-right: 12px;
}

.section-total-score {
  color: #409eff;
  font-weight: bold;
}

.question-table {
  margin-top: 12px;
  width: 100%;
}

.question-content {
  line-height: 1.4;
}

.question-title {
  margin-bottom: 4px;
  word-break: break-word;
}

.question-meta {
  font-size: 12px;
  color: #909399;
}

.meta-item {
  margin-right: 12px;
}

.question-score {
  color: #e6a23c;
  font-weight: bold;
}

.question-stats {
  font-size: 12px;
  line-height: 1.4;
}

.stat-item {
  margin-bottom: 2px;
}

.question-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.tag-item {
  margin-right: 4px;
}

.more-tags {
  color: #909399;
  font-size: 12px;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 4px;
}

.empty-state {
  padding: 40px 0;
  text-align: center;
}

.empty-text {
  color: #909399;
  margin-bottom: 16px;
}

.type-score-dialog {
  padding: 0 10px;
}

.dialog-alert {
  margin-bottom: 20px;
}

.alert-content {
  line-height: 1.5;
}

.alert-content p {
  margin: 4px 0;
}

.type-score-table {
  margin-bottom: 20px;
}

.score-table {
  width: 100%;
}

.type-count {
  font-weight: bold;
}

.type-total-score {
  color: #e6a23c;
  font-weight: bold;
}

.dialog-summary {
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 16px;
  margin-top: 20px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.summary-item {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 200px;
}

.summary-label {
  color: #606266;
  margin-right: 8px;
  white-space: nowrap;
}

.summary-value {
  font-weight: bold;
  color: #409eff;
}

.summary-error {
  color: #f56c6c;
}

.summary-difference {
  font-weight: bold;
  color: #e6a23c;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .form-row {
    flex-direction: column;
    gap: 0;
  }

  .symmetric-item {
    width: 100%;
    margin-bottom: 20px;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .section-title {
    margin-bottom: 8px;
  }

  .summary-row {
    flex-direction: column;
    align-items: flex-start;
  }

  .summary-item {
    width: 100%;
    justify-content: space-between;
  }
}
</style>