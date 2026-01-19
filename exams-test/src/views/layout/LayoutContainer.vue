<script setup>
import {
  Management,
  Promotion,
  UserFilled,
  User,
  Crop,
  EditPen,
  SwitchButton,
  CaretBottom,
  HomeFilled,
  DataBoard,
  Setting,
  Expand,
  Fold
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'
import { useRouter } from 'vue-router'
import { userUserStore } from '@/stores'
import { ref, computed } from 'vue'

const router = useRouter()
const userStore = userUserStore()
const isCollapsed = ref(false)

const handleCommand = async (key) => {
  if (key === 'logout') {
    // 退出操作
    await ElMessageBox.confirm('你确认要进行退出么', '温馨提示', {
      type: 'warning',
      confirmButtonText: '确认',
      cancelButtonText: '取消'
    })

    // 清除本地的数据 (token + user信息)
    userStore.removeId()
    userStore.removeName()
    userStore.removePhone()
    userStore.removeStudentId()
    userStore.removeToken()
    router.push('/login')
  } else {
    // 跳转操作
    router.push(`/user/${key}`)
  }
}

const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}

const currentTime = computed(() => {
  const now = new Date()
  return now.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
})
</script>

<template>
  <el-container class="layout-container">
    <!-- 左侧菜单 -->
    <el-aside :width="isCollapsed ? '80px' : '260px'" class="sidebar">
      <div class="sidebar-header">
        <div class="logo" :class="{ 'logo-collapsed': isCollapsed }">
          <div class="logo-icon">
            <i class="icon-exam"></i>
          </div>
          <span v-if="!isCollapsed" class="logo-text">在线考试系统</span>
        </div>
        <el-tooltip :content="isCollapsed ? '展开菜单' : '收起菜单'" placement="right">
          <el-icon class="collapse-btn" @click="toggleSidebar">
            <Expand v-if="isCollapsed" />
            <Fold v-else />
          </el-icon>
        </el-tooltip>
      </div>

      <el-menu
        active-text-color="#fff"
        background-color="transparent"
        :default-active="$route.path"
        text-color="rgba(255,255,255,0.7)"
        router
        :collapse="isCollapsed"
        class="sidebar-menu"
      >
        <el-menu-item index="/answer/preview" class="menu-item">
          <el-icon><Management /></el-icon>
          <template #title>
            <span>答题预览</span>
          </template>
        </el-menu-item>

        <el-menu-item index="/article/test" class="menu-item">
          <el-icon><DataBoard /></el-icon>
          <template #title>
            <span>试卷管理</span>
          </template>
        </el-menu-item>

        <el-menu-item index="/article/question" class="menu-item">
          <el-icon><Promotion /></el-icon>
          <template #title>
            <span>题库管理</span>
          </template>
        </el-menu-item>

        <el-sub-menu index="/user" class="menu-item">
          <template #title>
            <el-icon><UserFilled /></el-icon>
            <span>个人中心</span>
          </template>
          <el-menu-item index="/user/profile" class="submenu-item">
            <el-icon><User /></el-icon>
            <span>基本资料</span>
          </el-menu-item>
          <el-menu-item index="/user/avatar" class="submenu-item">
            <el-icon><Crop /></el-icon>
            <span>更换头像</span>
          </el-menu-item>
          <el-menu-item index="/user/password" class="submenu-item">
            <el-icon><EditPen /></el-icon>
            <span>重置密码</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <!-- 右侧内容区域 -->
    <el-container class="main-container">
      <!-- 顶部导航 -->
      <el-header class="header">
        <div class="header-left">
          <div class="page-title">
            <span class="title-text">控制台</span>
            <span class="title-subtext"><span class="current-time">{{ currentTime }}</span>欢迎回来，{{ userStore.name }}</span>
          </div>
        </div>

<!--        <div class="header-center">-->
<!--          <div class="time-display">-->
<!--            <span class="current-time">{{ currentTime }}</span>-->
<!--          </div>-->
<!--        </div>-->

        <div class="header-right">
          <div class="user-info">
            <el-avatar :src="userStore.img || avatar" :size="40" />
            <div class="user-details">
              <div class="user-name">{{ userStore.name }}</div>
              <div class="user-role">管理员</div>
            </div>
          </div>

          <el-dropdown placement="bottom-end" @command="handleCommand" trigger="click" class="user-dropdown">
            <span class="dropdown-trigger">
              <el-icon><Setting /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu class="user-menu">
                <el-dropdown-item command="profile" class="menu-item">
                  <el-icon><User /></el-icon>
                  <span>基本资料</span>
                </el-dropdown-item>
                <el-dropdown-item command="avatar" class="menu-item">
                  <el-icon><Crop /></el-icon>
                  <span>更换头像</span>
                </el-dropdown-item>
                <el-dropdown-item command="password" class="menu-item">
                  <el-icon><EditPen /></el-icon>
                  <span>重置密码</span>
                </el-dropdown-item>
                <el-dropdown-item divided command="logout" class="menu-item logout">
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容区域 -->
      <el-main class="content-area">
        <div class="content-wrapper">
<!--          <div class="page-header">-->
<!--            <div class="header-info">-->
<!--              <h1 class="page-title">控制台</h1>-->
<!--              <div class="page-subtitle">-->
<!--                今日有 <span class="highlight">3</span> 个待处理事项，<span class="highlight">12</span> 个新试卷提交-->
<!--              </div>-->
<!--            </div>-->
<!--            <div class="header-actions">-->
<!--              <el-button type="primary" icon="Plus">新建试卷</el-button>-->
<!--              <el-button icon="Refresh">刷新</el-button>-->
<!--            </div>-->
<!--          </div>-->

          <div class="content-card">
            <transition name="fade-transform" mode="out-in">
              <router-view v-slot="{ Component }">
                <keep-alive :include="[]">
                  <component :is="Component" />
                </keep-alive>
              </router-view>
            </transition>
          </div>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .sidebar {
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(20px);
    border-right: 1px solid rgba(255, 255, 255, 0.1);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

    .sidebar-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 20px 16px;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);

      .logo {
        display: flex;
        align-items: center;
        transition: all 0.3s;

        .logo-icon {
          width: 40px;
          height: 40px;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;

          .icon-exam {
            width: 24px;
            height: 24px;
            background: #fff;
            mask: url("data:image/svg+xml,%3Csvg viewBox='0 0 24 24' fill='none' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 5.5V7H9V5.5L3 7V9L9 10.5V12L3 13.5V15.5L9 14V16L3 17.5V19.5L9 18V22H15V18L21 19.5V17.5L15 16V14L21 15.5V13.5L15 12V10.5L21 9Z' fill='currentColor'/%3E%3C/svg%3E") no-repeat center;
          }
        }

        .logo-text {
          color: #fff;
          font-size: 20px;
          font-weight: 700;
          white-space: nowrap;
        }

        &.logo-collapsed {
          justify-content: center;
          width: 100%;

          .logo-icon {
            margin-right: 0;
          }
        }
      }

      .collapse-btn {
        color: rgba(255, 255, 255, 0.7);
        font-size: 20px;
        cursor: pointer;
        transition: all 0.3s;
        padding: 8px;
        border-radius: 8px;

        &:hover {
          color: #fff;
          background: rgba(255, 255, 255, 0.1);
        }
      }
    }

    .sidebar-menu {
      border: none;
      background: transparent;
      padding: 16px 8px;

      :deep(.el-menu-item),
      :deep(.el-sub-menu__title) {
        height: 48px;
        line-height: 48px;
        margin: 4px 0;
        border-radius: 12px;
        transition: all 0.3s ease;

        &.is-active {
          background: linear-gradient(135deg, rgba(102, 126, 234, 0.8) 0%, rgba(118, 75, 162, 0.8) 100%) !important;
          color: #fff !important;
          box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);

          .el-icon {
            color: #fff !important;
          }
        }

        &:hover {
          background: rgba(255, 255, 255, 0.1);
          color: #fff;
        }

        .el-icon {
          font-size: 20px;
          margin-right: 12px;
        }
      }

      :deep(.el-sub-menu) {
        .el-menu {
          background: rgba(0, 0, 0, 0.2);
          border-radius: 12px;
          margin: 8px 0;

          .el-menu-item {
            margin: 2px 8px;
            width: calc(100% - 16px);

            &.is-active {
              background: rgba(102, 126, 234, 0.6) !important;
            }
          }
        }
      }
    }
  }

  .main-container {
    display: flex;
    flex-direction: column;
    background: #f8fafc;

    .header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 24px;
      height: 80px;
      background: rgba(255, 255, 255, 0.8);
      backdrop-filter: blur(20px);
      border-bottom: 1px solid rgba(255, 255, 255, 0.2);

      .header-left {
        .page-title {
          .title-text {
            font-size: 24px;
            font-weight: 700;
            color: #1e293b;
          }

          .title-subtext {
            display: block;
            font-size: 14px;
            color: #64748b;
            margin-top: 4px;
          }
        }
      }

      .header-center {
        .time-display {
          padding: 8px 16px;
          background: rgba(255, 255, 255, 0.7);
          border-radius: 12px;
          backdrop-filter: blur(10px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);

          .current-time {
            font-size: 14px;
            font-weight: 600;
            color: #1e293b;
          }
        }
      }

      .header-right {
        display: flex;
        align-items: center;
        gap: 16px;

        .user-info {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 8px 12px;
          border-radius: 12px;
          transition: all 0.3s;

          &:hover {
            background: rgba(102, 126, 234, 0.1);
          }

          .user-details {
            text-align: left;

            .user-name {
              font-weight: 600;
              color: #1e293b;
            }

            .user-role {
              font-size: 12px;
              color: #64748b;
            }
          }
        }

        .dropdown-trigger {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 40px;
          height: 40px;
          border-radius: 10px;
          cursor: pointer;
          transition: all 0.3s;

          &:hover {
            background: rgba(102, 126, 234, 0.1);
          }

          .el-icon {
            color: #595959;
            font-size: 20px;
          }
        }
      }
    }

    .content-area {
      padding: 0;
      overflow: hidden;

      .content-wrapper {
        height: 100%;
        padding: 24px;
        overflow: auto;

        .page-header {
          display: flex;
          align-items: flex-end;
          justify-content: space-between;
          margin-bottom: 24px;

          .header-info {
            .page-title {
              font-size: 32px;
              font-weight: 800;
              color: #1e293b;
              margin: 0 0 8px 0;
            }

            .page-subtitle {
              font-size: 16px;
              color: #64748b;

              .highlight {
                color: #667eea;
                font-weight: 600;
              }
            }
          }

          .header-actions {
            display: flex;
            gap: 12px;
          }
        }

        .content-card {
          background: #fff;
          border-radius: 20px;
          box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
          overflow: hidden;
          min-height: 600px;
        }
      }
    }
  }
}

/* 路由切换动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.5);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .sidebar {
    position: fixed !important;
    z-index: 1000;
    height: 100vh;

    &:not(.is-collapsed) {
      width: 260px !important;
    }
  }

  .header-center {
    .time-display {
      padding: 6px 12px !important;

      .current-time {
        font-size: 12px !important;
      }
    }
  }

  .content-wrapper {
    padding: 16px !important;
  }
}

@media (max-width: 768px) {
  .header {
    padding: 0 16px !important;

    .header-center {
      display: none !important;
    }

    .user-info .user-details {
      display: none;
    }
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start !important;
    gap: 16px;
  }

  .header-actions {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>