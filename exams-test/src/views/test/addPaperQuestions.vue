<template>
  <div class="add-question-to-paper-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span class="header-title">添加题目到试卷</span>
          <div class="header-actions">
            <el-button type="primary" @click="batchAddToPaper" :disabled="selectedQuestionIds.length === 0">
              批量添加到试卷
            </el-button>
            <el-button @click="backToPaperEdit">返回试卷编辑</el-button>
          </div>
        </div>
      </template>

      <!-- 查询条件 -->
      <div class="filter-section">
        <el-form :model="queryParams" :inline="true">
          <el-form-item label="题目名称">
            <el-input
              v-model="queryParams.title"
              placeholder="请输入题目名称"
              clearable
              @clear="handleQuery"
              @keyup.enter="handleQuery"
            />
          </el-form-item>

          <el-form-item label="题目分类">
            <el-select
              v-model="queryParams.categoryId"
              placeholder="请选择分类"
              clearable
              @change="handleQuery"
            >
              <el-option
                v-for="cat in categoryData"
                :key="cat.id"
                :label="cat.typeName"
                :value="cat.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="题型">
            <el-select
              v-model="queryParams.questionType"
              placeholder="请选择题型"
              clearable
              @change="handleQuery"
            >
              <el-option label="单选题" value="单选题" />
              <el-option label="多选题" value="多选题" />
              <el-option label="判断题" value="判断题" />
              <el-option label="填空题" value="填空题" />
              <el-option label="简答题" value="简答题" />
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 提示信息 -->
      <div class="tips-section">
        <el-alert
          type="info"
          :closable="false"
          show-icon
        >
          <div class="tips-content">
            <p>当前试卷已有题目将不会显示在此列表中</p>
            <p>已选题目: <span class="selected-count">{{ selectedQuestionIds.length }}</span> 个</p>
          </div>
        </el-alert>
      </div>

      <!-- 题目列表 -->
      <div class="question-list-section">
        <el-table
          ref="multipleTableRef"
          :data="questionList"
          :border="true"
          :stripe="true"
          class="question-table"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />

          <el-table-column label="序号" width="80" align="center">
            <template #default="scope">
              {{ (queryParams.page - 1) * queryParams.pageSize + scope.$index + 1 }}
            </template>
          </el-table-column>

          <el-table-column label="题目内容" min-width="300">
            <template #default="scope">
              <div class="question-content">
                <div class="question-title">
                  <span class="question-type-tag">
                    <el-tag size="small" :type="getQuestionTypeTagType(scope.row.questionType)">
                      {{ scope.row.questionType }}
                    </el-tag>
                  </span>
                  <span class="title-text">{{ scope.row.title }}</span>
                </div>
                <div class="question-meta">
                  <span class="meta-item">分类: {{ scope.row.categoryName || '未分类' }}</span>
                  <span class="meta-item">分值: {{ formatScore(scope.row.score) }}分</span>
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="创建信息" width="200">
            <template #default="scope">
              <div class="create-info">
                <div class="create-user">创建: {{ scope.row.createUser || '未知' }}</div>
                <div class="create-time">{{ formatDate(scope.row.createTime) }}</div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="答题统计" width="150" align="center">
            <template #default="scope">
              <div class="answer-stats">
                <div class="stat-item">答题: {{ scope.row.totalAnswerCount || 0 }}</div>
                <div class="stat-item">错误率: {{ calculateErrorRate(scope.row) }}%</div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="120" align="center" fixed="right">
            <template #default="scope">
              <div class="action-buttons">
                <el-button
                  type="primary"
                  size="small"
                  @click="viewQuestionDetail(scope.row)"
                >
                  详情
                </el-button>
                <el-button
                  type="success"
                  size="small"
                  @click="addSingleQuestion(scope.row)"
                >
                  添加
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 题目详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="题目详情"
      width="700px"
      :before-close="handleDetailDialogClose"
    >
      <div v-if="currentQuestion" class="question-detail">
        <!-- 题目基本信息 -->
        <div class="detail-header">
          <div class="question-type-info">
            <el-tag :type="getQuestionTypeTagType(currentQuestion.questionType)">
              {{ currentQuestion.questionType }}
            </el-tag>
            <span class="question-score">分值: {{ formatScore(currentQuestion.score) }}分</span>
          </div>
          <h3 class="question-title">{{ currentQuestion.title }}</h3>
        </div>

        <!-- 单选题/多选题选项 -->
        <div v-if="['单选题', '多选题'].includes(currentQuestion.questionType) && currentQuestion.questionOptions" class="options-section">
          <div class="options-title">选项：</div>
          <div class="options-list">
            <div
              v-for="(option, index) in currentQuestion.questionOptions"
              :key="option.id"
              class="option-item"
              :class="{ 'correct-option': option.isCorrect === 1 }"
            >
              <span class="option-label">{{ getOptionLabel(index) }}</span>
              <span class="option-content">{{ option.optionContent }}</span>
              <el-icon v-if="option.isCorrect === 1" class="correct-icon">
                <CircleCheck />
              </el-icon>
            </div>
          </div>
        </div>

        <!-- 判断题选项 -->
        <div v-if="currentQuestion.questionType === '判断题' && currentQuestion.questionOptions" class="judgement-section">
          <div class="judgement-title">选项：</div>
          <div class="judgement-list">
            <div
              v-for="option in currentQuestion.questionOptions"
              :key="option.id"
              class="judgement-item"
              :class="{ 'correct-judgement': option.isCorrect === 1 }"
            >
              <span class="judgement-content">{{ option.optionContent }}</span>
              <el-icon v-if="option.isCorrect === 1" class="correct-icon">
                <CircleCheck />
              </el-icon>
            </div>
          </div>
        </div>

        <!-- 填空题答案 -->
        <div v-if="currentQuestion.questionType === '填空题'" class="answer-section">
          <div class="answer-title">正确答案：</div>
          <div class="answer-content">{{ currentQuestion.correctAnswer || '无标准答案' }}</div>
        </div>

        <!-- 简答题答案 -->
        <div v-if="currentQuestion.questionType === '简答题'" class="answer-section">
          <div class="answer-title">参考答案：</div>
          <div class="answer-content">{{ currentQuestion.correctAnswer || '无标准答案' }}</div>
        </div>

        <!-- 题目解析 -->
        <div v-if="currentQuestion.answerAnalysis" class="analysis-section">
          <div class="analysis-title">解析：</div>
          <div class="analysis-content">{{ currentQuestion.answerAnalysis }}</div>
        </div>

        <!-- 题目标签 -->
        <div v-if="currentQuestion.tags" class="tags-section">
          <div class="tags-title">标签：</div>
          <div class="tags-list">
            <el-tag
              v-for="tag in currentQuestion.tags.split(',')"
              :key="tag"
              size="small"
              class="tag-item"
              v-show="tag.trim()"
            >
              {{ tag }}
            </el-tag>
          </div>
        </div>

        <!-- 题目统计信息 -->
        <div class="stats-section">
          <div class="stats-title">统计信息：</div>
          <div class="stats-content">
            <div class="stat-item">答题次数: {{ currentQuestion.totalAnswerCount || 0 }}</div>
            <div class="stat-item">错误次数: {{ currentQuestion.wrongAnswerCount || 0 }}</div>
            <div class="stat-item">错误率: {{ calculateErrorRate(currentQuestion) }}%</div>
            <div class="stat-item">创建者: {{ currentQuestion.createUser || '未知' }}</div>
            <div class="stat-item">创建时间: {{ formatDate(currentQuestion.createTime) }}</div>
          </div>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addCurrentQuestion">添加到试卷</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, ElCard, ElForm, ElFormItem, ElInput, ElSelect,
  ElOption, ElButton, ElTable, ElTableColumn, ElTag, ElPagination, ElAlert, ElDialog, ElIcon } from 'element-plus'
import { CircleCheck } from '@element-plus/icons-vue'
import { categoryList as fetchCategoryList } from '@/api/category'
import { page } from '@/api/question'
import { getTestParticulars, addQuestionsToPaper, AddTestPaperQuestion } from '@/api/testPaper'
// import { getTestParticulars } from '@/api/testPaper'


const route = useRoute()
const router = useRouter()

// 从查询参数获取试卷ID
const paperId = ref(Number(route.query.paperId))

// 查询参数
const queryParams = ref({
  title: '',
  categoryId: '',
  questionType: '',
  page: 1,
  pageSize: 10
})

// 题目分类列表
const categoryData = ref([])

// 题目列表
const questionList = ref([])

// 总题目数
const total = ref(0)

// 当前试卷已有的题目ID列表
const existingQuestionIds = ref([])

// 选中的题目ID列表
const selectedQuestionIds = ref([])

// 详情对话框相关
const detailDialogVisible = ref(false)
const currentQuestion = ref(null)

// 多选表格引用
const multipleTableRef = ref()

// 初始化数据
const initData = async () => {
  try {
    // 1. 获取当前试卷已有的题目ID列表
    if (paperId.value) {
      const paperRes = await getTestParticulars(paperId.value)
      if (paperRes.data && paperRes.data.code === 1) {
        const paperData = paperRes.data.data
        existingQuestionIds.value = paperData.questionVOList?.map(q => q.id) || []
      }
    }

    // 2. 获取题目分类列表
    const categoryRes = await fetchCategoryList()
    if (categoryRes.data && categoryRes.data.code === 1) {
      categoryData.value = categoryRes.data.data || []
    }

    // 3. 加载题目列表
    await loadQuestionList()

  } catch (error) {
    console.error('初始化数据失败:', error)
    ElMessage.error('初始化数据失败')
  }
}

// 加载题目列表
const loadQuestionList = async () => {
  try {
    // 构建查询参数
    const params = { ...queryParams.value }

    // 如果有试卷ID，添加排除已存在题目的条件
    if (paperId.value && existingQuestionIds.value.length > 0) {
      // 这里假设后端支持排除特定ID的题目
      // 如果后端不支持，需要在前端过滤
      params.excludeIds = existingQuestionIds.value.join(',')
    }

    const res = await page(params)

    if (res.data && res.data.code === 1) {
      const data = res.data.data
      questionList.value = data.records || []
      total.value = data.total || 0

      // 如果后端不支持排除，这里需要在前端过滤
      if (!params.excludeIds && existingQuestionIds.value.length > 0) {
        questionList.value = questionList.value.filter(question =>
          !existingQuestionIds.value.includes(question.id)
        )
        // 注意：这样过滤后总数可能不准确，但这是后端的限制
      }
    } else {
      ElMessage.error(res.data?.msg || '加载题目列表失败')
    }
  } catch (error) {
    console.error('加载题目列表失败:', error)
    ElMessage.error('加载题目列表失败')
  }
}

// 查询
const handleQuery = () => {
  queryParams.value.page = 1
  loadQuestionList()
}

// 重置查询
const resetQuery = () => {
  queryParams.value.title = ''
  queryParams.value.categoryId = ''
  queryParams.value.questionType = ''
  queryParams.value.page = 1
  loadQuestionList()
}

// 分页大小变化
const handleSizeChange = (size) => {
  queryParams.value.pageSize = size
  queryParams.value.page = 1
  loadQuestionList()
}

// 页码变化
const handleCurrentChange = (page) => {
  queryParams.value.page = page
  loadQuestionList()
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedQuestionIds.value = selection.map(item => item.id)
}

// 查看题目详情
const viewQuestionDetail = (question) => {
  currentQuestion.value = question
  detailDialogVisible.value = true
}

// 详情对话框关闭
const handleDetailDialogClose = () => {
  detailDialogVisible.value = false
}

// 添加当前查看的题目
const addCurrentQuestion = async () => {
  if (!currentQuestion.value) return
  await addQuestionsToPaperHandler([currentQuestion.value.id])
  detailDialogVisible.value = false
}

// 添加单个题目
const addSingleQuestion = async (question) => {
  await addQuestionsToPaperHandler([question.id])
}

// 批量添加到试卷
const batchAddToPaper = async () => {
  if (selectedQuestionIds.value.length === 0) {
    ElMessage.warning('请先选择要添加的题目')
    return
  }

  await addQuestionsToPaperHandler(selectedQuestionIds.value)
}

// 添加到试卷的处理函数
const addQuestionsToPaperHandler = async (questionIds) => {
  if (!paperId.value) {
    ElMessage.error('试卷ID不存在')
    return
  }

  // 检查是否选择了题目
  if (!questionIds || questionIds.length === 0) {
    ElMessage.warning('请选择要添加的题目')
    return
  }

  try {
    // 修改这里：使用 AddTestPaperQuestion API，并按照后端要求的参数格式传参
    const res = await AddTestPaperQuestion({
      paperId: paperId.value,
      ids: questionIds  // 注意：这里字段名改为 ids，与后端 DTO 保持一致
    })

    if (res.data && res.data.code === 1) {
      ElMessage.success(res.data.msg || '添加题目成功')

      // 从现有列表中移除已添加的题目
      questionList.value = questionList.value.filter(question =>
        !questionIds.includes(question.id)
      )

      // 清空选择
      selectedQuestionIds.value = []
      if (multipleTableRef.value) {
        multipleTableRef.value.clearSelection()
      }

      // 更新现有题目ID列表
      existingQuestionIds.value.push(...questionIds)

      // 更新总数
      total.value = Math.max(0, total.value - questionIds.length)

    } else {
      // 如果后端返回特定的错误码，可能是试题已存在
      if (res.data && res.data.code === 0) {
        ElMessage.error(res.data.msg || '添加题目失败，可能试题已存在')
      } else {
        ElMessage.error(res.data?.msg || '添加题目失败')
      }
    }
  } catch (error) {
    console.error('添加题目到试卷失败:', error)


  }
}

// 返回试卷编辑页面
const backToPaperEdit = () => {
  router.push(`/article/test/update/${paperId.value}`)
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

// 格式化分数
const formatScore = (score) => {
  const num = Number(score)
  if (isNaN(num)) return '0'

  if (Number.isInteger(num)) {
    return num.toString()
  }

  const formatted = num.toFixed(2)
  return formatted.replace(/\.?0+$/, '')
}

// 计算错误率
const calculateErrorRate = (question) => {
  if (!question.totalAnswerCount || question.totalAnswerCount === 0) return 0
  const errorRate = ((question.wrongAnswerCount / question.totalAnswerCount) * 100) || 0
  return errorRate.toFixed(1)
}

// 获取选项标签 (A, B, C, D...)
const getOptionLabel = (index) => {
  return String.fromCharCode(65 + index)
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  try {
    const date = new Date(dateString)
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
  } catch (e) {
    return dateString
  }
}

// 组件挂载
onMounted(() => {
  if (!paperId.value) {
    ElMessage.error('试卷ID不存在，无法添加题目')
    router.back()
    return
  }

  initData()
})
</script>

<style scoped>
.add-question-to-paper-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

/* 卡片头部样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 5px;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
}

/* 查询区域样式 */
.filter-section {
  padding: 20px;
  background-color: #f9fafc;
  border-radius: 4px;
  margin-bottom: 20px;
}

.filter-section :deep(.el-form-item) {
  margin-bottom: 0;
}

/* 提示信息样式 */
.tips-section {
  margin-bottom: 20px;
}

.tips-content {
  line-height: 1.6;
}

.tips-content p {
  margin: 5px 0;
}

.selected-count {
  font-weight: 600;
  color: #409EFF;
}

/* 题目列表样式 */
.question-list-section {
  margin-bottom: 20px;
}

.question-table {
  width: 100%;
}

.question-content {
  padding: 8px 0;
}

.question-title {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.question-type-tag {
  margin-right: 10px;
}

.title-text {
  font-size: 14px;
  line-height: 1.5;
  color: #303133;
}

.question-meta {
  font-size: 12px;
  color: #909399;
}

.meta-item {
  margin-right: 15px;
}

.create-info {
  font-size: 12px;
  color: #909399;
}

.create-user {
  margin-bottom: 4px;
}

.create-time {
  color: #c0c4cc;
}

.answer-stats {
  font-size: 12px;
  color: #606266;
}

.stat-item {
  margin-bottom: 2px;
}

.action-buttons {
  display: flex;
  gap: 5px;
  justify-content: center;
}

/* 分页样式 */
.pagination-section {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

/* 题目详情对话框样式 */
.question-detail {
  max-height: 60vh;
  overflow-y: auto;
  padding-right: 10px;
}

.detail-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.question-type-info {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 10px;
}

.question-score {
  font-size: 14px;
  color: #67C23A;
  font-weight: 500;
}

.question-title {
  font-size: 16px;
  line-height: 1.6;
  color: #303133;
  margin: 0;
}

/* 选项样式 */
.options-section,
.judgement-section,
.answer-section,
.analysis-section,
.tags-section,
.stats-section {
  margin-bottom: 20px;
}

.options-title,
.judgement-title,
.answer-title,
.analysis-title,
.tags-title,
.stats-title {
  font-weight: 600;
  color: #606266;
  margin-bottom: 10px;
  font-size: 14px;
}

.options-list {
  border-radius: 4px;
  overflow: hidden;
  border: 1px solid #ebeef5;
}

.option-item {
  display: flex;
  align-items: center;
  padding: 10px 15px;
  border-bottom: 1px solid #ebeef5;
  transition: background-color 0.3s;
  position: relative;
}

.option-item:last-child {
  border-bottom: none;
}

.option-item:hover {
  background-color: #f5f7fa;
}

.correct-option {
  background-color: #f0f9ff;
}

.option-label {
  display: inline-block;
  width: 24px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  background-color: #e4e7ed;
  border-radius: 50%;
  margin-right: 10px;
  flex-shrink: 0;
}

.correct-option .option-label {
  background-color: #409EFF;
  color: white;
}

.option-content {
  flex: 1;
  font-size: 14px;
  line-height: 1.5;
}

.correct-icon {
  color: #67C23A;
  font-size: 16px;
  margin-left: 10px;
}

/* 判断题样式 */
.judgement-list {
  display: flex;
  gap: 20px;
}

.judgement-item {
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  text-align: center;
  transition: all 0.3s;
  position: relative;
}

.judgement-item:hover {
  background-color: #f5f7fa;
}

.correct-judgement {
  background-color: #f0f9ff;
  border-color: #a0cfff;
}

.judgement-content {
  font-weight: 500;
  font-size: 14px;
}

/* 答案和解析样式 */
.answer-content,
.analysis-content {
  padding: 12px 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  border-left: 3px solid #409EFF;
  font-size: 14px;
  line-height: 1.6;
}

/* 标签样式 */
.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-item {
  font-size: 12px;
}

/* 统计信息样式 */
.stats-content {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.stat-item {
  padding: 8px 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
  font-size: 13px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .add-question-to-paper-container {
    padding: 10px;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .header-actions {
    width: 100%;
    justify-content: flex-start;
  }

  .filter-section :deep(.el-form) {
    display: flex;
    flex-direction: column;
    gap: 15px;
  }

  .judgement-list {
    flex-direction: column;
    gap: 10px;
  }

  .stats-content {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .header-actions {
    flex-direction: column;
    width: 100%;
  }

  .header-actions .el-button {
    width: 100%;
    margin-bottom: 5px;
  }

  .action-buttons {
    flex-direction: column;
    gap: 5px;
  }
}
</style>