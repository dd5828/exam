import { ref } from 'vue'
import { defineStore } from 'pinia'

//用户模块token
export const userUserStore = defineStore(
  'user',
  () => {
    const token = ref('')
    const studentId = ref('')
    const id = ref('')
    const name = ref('')
    const phone = ref('')
    const img = ref('')
    const testImg = ref('')
    const setToken = (newToken: string) => {
      token.value = newToken
    }
    const removeToken = () => {
      token.value = ''
    }
    const setStudentId = (newSrudentId: string) => {
      studentId.value = newSrudentId
    }
    const removeStudentId = () => {
      studentId.value = ''
    }
    const setId = (newId: string) => {
      id.value = newId
    }
    const removeId = () => {
      id.value = ''
    }
    const setName = (newName: string) => {
      name.value = newName
    }
    const removeName = () => {
      name.value = ''
    }
    const setPhone = (newPhoen: string) => {
      phone.value = newPhoen
    }
    const removePhone = () => {
      phone.value = ''
    }
    const setImg = (newImg: string) => {
      img.value = newImg
    }
    const removeImg = () => {
      img.value = ''
    }
    const setTestImg = (newTestImg: string) => {
      testImg.value = newTestImg
    }
    const removeTestImg = () => {
      testImg.value = ''
    }

    return {
      token,
      id,
      name,
      studentId,
      phone,
      img,
      testImg,
      setTestImg,
      removeTestImg,
      removeImg,
      setImg,
      removeToken,
      setToken,
      setStudentId,
      removeStudentId,
      setId,
      removeId,
      setName,
      removeName,
      setPhone,
      removePhone
    }
  },
  {
    persist: true
  }
)
