# 在线考试管理系统

## 简介：

目前系统只包含管理员系统，项目基于Vue+Typescript+SpringBoot+MybatisPlus

## 技术栈：

1.spring boot

2.vue

## 快速启动：

1.在mysql创建exams数据库，字符集选择utf-8，再导入sql文件夹里的数据库文件

- exams.sql

2.启动管理后台前端

打开命令行，输入以下命令

- cd exams-test
- npm install
- npm run dev

3.启动管理后台的后端服务

- 打开 "exam\exams\exams-service\src\main\resources\application.yml"  编辑你的数据库账户密码![image-20260119210718216](G:\springboot_project\exam\运行图片\image-20260119210718216.png)
- 在idea中配置好maven并刷新下载对应依赖        （不会的可以直接搜索maven配置）
- 在idea中选择直接运行即可

## 运行效果：

浏览器输入：[在线考试系统接口文档](http://localhost:9999/doc.html#/default/教师相关接口/loginaUsingPOST)

可以看到自动生成的接口文档

![image-20260119211539231](G:\springboot_project\exam\运行图片\image-20260119211539231.png)

注意使用接口文档需要登录后保存token，刷新即可![image-20260119211754386](G:\springboot_project\exam\运行图片\image-20260119211754386.png)

![image-20260119211902346](G:\springboot_project\exam\运行图片\image-20260119211902346.png)

登录界面

![image-20260119212149256](G:\springboot_project\exam\运行图片\image-20260119212149256.png)

管理员后台界面

![image-20260119212308756](G:\springboot_project\exam\运行图片\image-20260119212308756.png)

![image-20260119212343115](G:\springboot_project\exam\运行图片\image-20260119212343115.png)

![image-20260119212409679](G:\springboot_project\exam\运行图片\image-20260119212409679.png)

![image-20260119212450482](G:\springboot_project\exam\运行图片\image-20260119212450482.png)

![image-20260119212519825](G:\springboot_project\exam\运行图片\image-20260119212519825.png)

![image-20260119212544373](G:\springboot_project\exam\运行图片\image-20260119212544373.png)

![image-20260119212617114](G:\springboot_project\exam\运行图片\image-20260119212617114.png)

![image-20260119212643835](G:\springboot_project\exam\运行图片\image-20260119212643835.png)