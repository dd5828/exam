<template>
  <div>
    <div ref="chartRef" style="width: 300px; height: 200px; display: flex"></div>
  </div>
</template>

<script setup>
import { onMounted, nextTick, ref, watch } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  num: Number,
  sum: Number
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
  const totalAnswerCount = props.sum || 0
  const wrongAnswerCount = props.num || 0

  const wrongPercentage = `${Math.round((wrongAnswerCount / totalAnswerCount) * 100)}%`
  const rightPercentage = `${Math.round(((totalAnswerCount - wrongAnswerCount) / totalAnswerCount) * 100)}%`

  const data = [
    { value: wrongAnswerCount, name: '做错' },
    { value: totalAnswerCount - wrongAnswerCount, name: '做对' }
  ]

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '试题对错率',
        type: 'pie',
        radius: '50%',
        data: data,
        label: {
          show: true,
          position: 'outside',
          formatter: function (params) {
            if (params.name === '做错') {
              return `做错 (${wrongPercentage})`
            } else if (params.name === '做对') {
              return `做对 (${rightPercentage})`
            }
          },
          labelLine: {
            show: false
          }
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }

  myChart.setOption(option)
}

watch(
  () => props.num,
  () => updateChart()
)

watch(
  () => props.sum,
  () => updateChart()
)
</script>
