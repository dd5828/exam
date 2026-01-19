//试卷模块
import { ref } from 'vue'
import { defineStore } from 'pinia'

interface TestPaper {
  paperId: number
  paperPictures: string
}

export const useTestPaperStore = defineStore(
  'testPaper',
  () => {
    const testPaperData = ref<TestPaper>({
      paperId: 0,
      paperPictures: ''
    })

    const setPaperId = (id: number) => {
      testPaperData.value.paperId = id
    }

    const removePaperId = () => {
      testPaperData.value.paperId = 0
    }

    const setPaperPictures = (pictures: string) => {
      testPaperData.value.paperPictures = pictures
    }

    const removePaperPictures = () => {
      testPaperData.value.paperPictures = ''
    }

    return { testPaperData, setPaperId, removePaperId, setPaperPictures, removePaperPictures }
  },
  {
    persist: true
  }
)
