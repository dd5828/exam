<template>
  <div>
    <div ref="chartRef" style="width: 300px; height: 350px"></div>
  </div>
</template>

<script setup>
import { onMounted, nextTick, ref, watch } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  questionTypes: {
    type: Array,
    default: () => [],
    // 修正校验规则：确保元素包含questionType（字符串）和sumQuestion（数字）
    validator: (value) => {
      return value.every(item =>
        typeof item === 'object' &&
        'questionType' in item &&
        'sumQuestion' in item &&
        typeof item.questionType === 'string' && // 校验题型名称为字符串
        typeof item.sumQuestion === 'number'    // 校验数量为数字
      )
    }
  }
})

const chartRef = ref(null)
let myChart

onMounted(() => {
  nextTick(() => {
    const chartDom = chartRef.value
    myChart = echarts.init(chartDom)
    updateChart()
  })
})

function updateChart() {
  console.log("准备渲染的题型数据：", props.questionTypes);

  // 检查数据是否为空或格式错误
  if (!Array.isArray(props.questionTypes)) {
    console.error("questionTypes 不是数组！");
    return;
  }
  // 修正无效数据判断：基于questionType和sumQuestion
  const invalidItem = props.questionTypes.find(item =>
    typeof item.questionType !== 'string' || typeof item.sumQuestion !== 'number'
  );
  if (invalidItem) {
    console.error("格式错误的元素：", invalidItem);
    return;
  }

  // 将questionType和sumQuestion映射为图表所需的name和value
  const chartData = props.questionTypes.map(item => ({
    name: item.questionType,  // 题型名称
    value: item.sumQuestion   // 题型数量
  }));

  const option = {
    tooltip: { trigger: 'item', formatter: '{b}: {c}题 ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      name: '题型分布',
      type: 'pie',
      radius: '50%',
      data: chartData,  // 使用映射后的数据
      label: { show: true, formatter: '{b}: {c}题' }
    }]
  };

  myChart.setOption(option);
}

// 监听数据变化并更新图表
watch(
  () => props.questionTypes,
  () => {
    console.log("数据变化，更新图表");
    updateChart();
  },
  { deep: true }
);
</script>