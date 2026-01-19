<template>
  <div style="width: 100%; height: 100%; background-color: #fafafa; border-radius: 10px">
    <div class="header-container">
      <div class="title">创建试卷</div>
      <ul>
        <li :class="{ active: activeStep === 0 }">
          <div class="circle" :class="{ active: activeStep === 0 }">1</div>
          <span>基本信息</span>
          <div class="line"></div>
        </li>
        <li :class="{ active: activeStep === 1 }">
          <div class="circle" :class="{ active: activeStep === 1 }">2</div>
          <span>选择试题</span>
          <div class="line"></div>
        </li>
        <li :class="{ active: activeStep === 2 }">
          <div class="circle" :class="{ active: activeStep === 2 }">3</div>
          <span>试卷设置</span>
          <div class="line"></div>
        </li>
        <li :class="{ active: activeStep === 3 }">
          <div class="circle" :class="{ active: activeStep === 3 }">4</div>
          <span>完成</span>
        </li>
      </ul>
    </div>
    <!-- <el-button @click="nextStep">下一步</el-button> -->
    <div v-if="activeStep == 0">
      <AddTestPaper @refresh="nextStep"></AddTestPaper>
    </div>
    <div v-if="activeStep == 1">
      <AddTestPaperQuestion @refresh="nextStep"></AddTestPaperQuestion>
    </div>

    <div v-if="activeStep == 2">
      <installTsetPaper @refresh="nextStep"></installTsetPaper>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const activeStep = ref(0)

const nextStep = () => {
  if (activeStep.value < 3) {
    activeStep.value++
  }
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}

.header-container {
  position: sticky;
  height: 50px;
  line-height: 50px;
  background-color: white;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
}

.title {
  font-size: 20px;
  font-weight: 700;
  color: #333;
  padding-left: 24px;
}

.header-container ul {
  position: absolute;
  width: 60%;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  justify-content: space-between;
  align-items: center;
  list-style: none;
  color: #999999;
}

.header-container ul li {
  width: calc((100% - 62px) / 3);
  display: flex;
  align-items: center;
}

.circle {
  border: 2px solid #e6e6e6;
  background-color: #fff;
  color: #999999;
  width: 24px;
  height: 24px;
  margin-right: 6px;
  text-align: center;
  border-radius: 24px !important;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
}

.circle.active {
  box-shadow: 0px 2px 10px 0px #36aafd;
  border-color: #36aafd;
  background-color: #36aafd;
  color: #fff;
}

ul li.active span {
  color: #36aafd;
}

.line {
  flex: 1;
  height: 4px;
  background-color: #f6f7f9;
  border-radius: 6px;
}
</style>
