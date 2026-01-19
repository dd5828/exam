/*
 Navicat Premium Data Transfer

 Source Server         : 外卖
 Source Server Type    : MySQL
 Source Server Version : 80035 (8.0.35)
 Source Host           : localhost:3306
 Source Schema         : exams

 Target Server Type    : MySQL
 Target Server Version : 80035 (8.0.35)
 File Encoding         : 65001

 Date: 19/01/2026 17:47:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for answers
-- ----------------------------
DROP TABLE IF EXISTS `answers`;
CREATE TABLE `answers`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '答题编号',
  `paper_id` int NULL DEFAULT NULL COMMENT '所属试卷编号',
  `question_id` int NULL DEFAULT NULL COMMENT '题目编号',
  `student_answer` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_mysql500_ci NULL COMMENT '学生答案',
  `score` int NULL DEFAULT NULL COMMENT '该题得分',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_mysql500_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of answers
-- ----------------------------

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '分类名称',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '题目分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '默认', '2024-09-11 18:27:26', '2024-09-11 18:27:26', 4, 4);
INSERT INTO `category` VALUES (25, '常识', '2024-09-11 18:28:35', '2024-09-11 18:28:35', 4, 4);
INSERT INTO `category` VALUES (26, 'python', '2024-09-20 22:35:30', '2024-09-20 22:37:41', 4, 4);
INSERT INTO `category` VALUES (27, 'java', '2024-09-20 22:35:35', '2024-09-20 22:35:35', 4, 4);
INSERT INTO `category` VALUES (28, '电子电工', '2024-09-20 22:36:11', '2024-09-20 22:36:11', 4, 4);
INSERT INTO `category` VALUES (29, 'English', '2024-09-20 22:36:27', '2024-11-17 14:22:31', 4, 4);
INSERT INTO `category` VALUES (30, 'Linux', '2024-09-20 22:37:02', '2024-09-20 22:37:02', 4, 4);

-- ----------------------------
-- Table structure for class_info
-- ----------------------------
DROP TABLE IF EXISTS `class_info`;
CREATE TABLE `class_info`  (
  `class_id` int NOT NULL AUTO_INCREMENT COMMENT '班级的唯一标识 ID',
  `class_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_mysql500_ci NOT NULL COMMENT '班级名称',
  `major` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_mysql500_ci NOT NULL COMMENT '专业',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '班级信息创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '班级信息最后更新时间',
  PRIMARY KEY (`class_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_mysql500_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class_info
-- ----------------------------
INSERT INTO `class_info` VALUES (1, '2024软工1班', '软件工程', '2024-09-02 22:56:27', '2026-01-18 22:53:04');
INSERT INTO `class_info` VALUES (2, '2024软工2班', '软件工程', '2024-09-04 21:24:13', '2026-01-18 22:53:15');
INSERT INTO `class_info` VALUES (3, '2024软工3班', '软件工程', '2024-09-04 21:24:17', '2026-01-18 22:53:23');
INSERT INTO `class_info` VALUES (4, '2024计本1班', '计算机科学与技术', '2024-09-04 21:24:39', '2026-01-18 22:53:36');
INSERT INTO `class_info` VALUES (5, '2024计本2班', '计算机科学与技术', '2024-09-04 21:24:43', '2026-01-18 22:53:39');
INSERT INTO `class_info` VALUES (6, '2024计本3班', '计算机科学与技术', '2024-09-04 21:24:46', '2026-01-18 22:53:43');

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '考试编号',
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_mysql500_ci NULL DEFAULT NULL COMMENT '考试名称',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_mysql500_ci NULL DEFAULT NULL COMMENT '考试描述',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `teacher_id` int NULL DEFAULT NULL COMMENT '出题教师编号',
  `paper_id` int NULL DEFAULT NULL COMMENT '试卷编号',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_mysql500_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam
-- ----------------------------

-- ----------------------------
-- Table structure for examination_papers
-- ----------------------------
DROP TABLE IF EXISTS `examination_papers`;
CREATE TABLE `examination_papers`  (
  `paper_id` int NOT NULL AUTO_INCREMENT COMMENT '试卷编号，自增主键',
  `paper_title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_mysql500_ci NULL DEFAULT NULL COMMENT '试卷标题',
  `description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_mysql500_ci NULL COMMENT '试卷描述',
  `duration` int NULL DEFAULT NULL COMMENT '考试时长（分钟）',
  `total_score` int NULL DEFAULT NULL COMMENT '试卷总分',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint NULL DEFAULT 0 COMMENT '试卷状态，1：启用，0：禁用',
  `start_time` datetime NULL DEFAULT NULL COMMENT '考试开始时间（如果试卷已发布）',
  `end_time` datetime NULL DEFAULT NULL COMMENT '考试结束时间（如果试卷已发布）',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建用户id',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '修改用户id',
  `paper_pictures` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_mysql500_ci NULL DEFAULT NULL COMMENT '试卷图片',
  `category_id` int NULL DEFAULT 0 COMMENT '试卷分类id',
  PRIMARY KEY (`paper_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_mysql500_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of examination_papers
-- ----------------------------
INSERT INTO `examination_papers` VALUES (26, 'linux测试889', '你不点进来，怎么知道你很厉害', 60, 100, '2024-09-29 21:09:08', '2025-12-16 08:36:45', 1, '2025-12-16 08:36:20', '2025-12-26 08:36:32', 4, 5, 'https://wm-plus-images.oss-cn-beijing.aliyuncs.com/a6571a1e-d3c1-4030-87eb-fa61dd869c5e.png', 30);
INSERT INTO `examination_papers` VALUES (32, '测试1018', '你不点进来，怎么知道你很厉害', 30, 100, '2024-10-18 20:14:37', '2025-12-13 18:10:50', 0, '2024-10-10 00:00:00', '2024-11-04 00:00:00', 4, 5, 'https://wm-plus-images.oss-cn-beijing.aliyuncs.com/36049797-f7c9-4d99-bbe9-2e06113f5a50.png', 27);
INSERT INTO `examination_papers` VALUES (36, '哈哈哈', '你不点进来，怎么知道你很厉害', 30, 100, '2025-11-28 16:50:16', '2025-11-28 16:51:35', 0, '2025-11-28 00:00:00', '2025-11-30 00:00:00', 5, 5, '', 1);
INSERT INTO `examination_papers` VALUES (37, '1123455', '你不点进来，怎么知道你很厉害', 30, 100, '2025-12-01 10:40:46', '2025-12-01 10:41:21', 0, '2025-12-19 00:00:00', '2025-12-27 00:00:00', 5, 5, '', 25);
INSERT INTO `examination_papers` VALUES (38, '888', '你不点进来，怎么知道你很厉害', 60, 100, '2025-12-01 11:15:34', '2025-12-01 11:16:28', 0, '2025-12-17 00:00:00', '2025-12-25 00:00:00', 5, 5, 'https://wm-plus-images.oss-cn-beijing.aliyuncs.com/605988bd-4539-4eea-bc35-67e103d53780.png', 1);
INSERT INTO `examination_papers` VALUES (39, 'hhhhhhhhhhh', '你不点进来，怎么知道你很厉害', 60, 100, '2025-12-09 09:29:32', '2025-12-09 09:30:24', 0, '2025-12-16 00:00:00', '2025-12-18 00:00:00', 5, 5, 'https://wm-plus-images.oss-cn-beijing.aliyuncs.com/605988bd-4539-4eea-bc35-67e103d53780.png', 1);
INSERT INTO `examination_papers` VALUES (40, '灌灌灌灌', '你不点进来，怎么知道你很厉害哈哈哈', 30, 100, '2025-12-13 20:43:45', '2025-12-13 20:54:06', 0, '2025-12-23 21:43:59', '2025-12-27 00:00:00', 5, 5, '', 25);
INSERT INTO `examination_papers` VALUES (41, '1216', '你不点进来，怎么知道你很厉害', 50, 100, '2025-12-16 08:38:32', '2025-12-16 08:39:13', 0, '2025-12-23 08:38:45', '2025-12-25 00:00:00', 5, 5, 'https://wm-plus-images.oss-cn-beijing.aliyuncs.com/f726bdb6-9f0b-4812-b8ba-0bcc44bd4d78.jpg', 27);
INSERT INTO `examination_papers` VALUES (42, '永远永远', '你不点进来，怎么知道你很厉害', 34, 100, '2025-12-16 10:59:56', '2025-12-16 11:00:32', 0, '2025-12-24 00:00:00', '2025-12-26 00:00:00', 5, 5, 'https://wm-plus-images.oss-cn-beijing.aliyuncs.com/b7641a00-2d75-4abf-9b5e-4da2bb22e54a.jpg', 25);

-- ----------------------------
-- Table structure for paper_questions
-- ----------------------------
DROP TABLE IF EXISTS `paper_questions`;
CREATE TABLE `paper_questions`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `paper_id` int NULL DEFAULT NULL COMMENT '试卷编号（逻辑外键）',
  `question_id` int NULL DEFAULT NULL COMMENT '题目编号（逻辑外键）',
  `type_score` float NULL DEFAULT 0 COMMENT '题目分数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 82 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_mysql500_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper_questions
-- ----------------------------
INSERT INTO `paper_questions` VALUES (1, 1, 11, 0);
INSERT INTO `paper_questions` VALUES (2, 1, 12, 0);
INSERT INTO `paper_questions` VALUES (4, 25, 12, 30);
INSERT INTO `paper_questions` VALUES (5, 25, 14, 30);
INSERT INTO `paper_questions` VALUES (6, 25, 18, 40);
INSERT INTO `paper_questions` VALUES (7, 26, 12, 10);
INSERT INTO `paper_questions` VALUES (8, 26, 14, 10);
INSERT INTO `paper_questions` VALUES (9, 27, 23, 20);
INSERT INTO `paper_questions` VALUES (10, 27, 24, 20);
INSERT INTO `paper_questions` VALUES (11, 27, 25, 20);
INSERT INTO `paper_questions` VALUES (12, 27, 26, 20);
INSERT INTO `paper_questions` VALUES (13, 27, 27, 20);
INSERT INTO `paper_questions` VALUES (14, 28, 14, 50);
INSERT INTO `paper_questions` VALUES (15, 28, 12, 50);
INSERT INTO `paper_questions` VALUES (16, 29, 12, 50);
INSERT INTO `paper_questions` VALUES (17, 29, 14, 50);
INSERT INTO `paper_questions` VALUES (18, 30, 23, 20);
INSERT INTO `paper_questions` VALUES (19, 30, 24, 20);
INSERT INTO `paper_questions` VALUES (20, 30, 25, 20);
INSERT INTO `paper_questions` VALUES (21, 30, 26, 20);
INSERT INTO `paper_questions` VALUES (22, 30, 27, 20);
INSERT INTO `paper_questions` VALUES (23, 32, 23, 20);
INSERT INTO `paper_questions` VALUES (24, 32, 24, 20);
INSERT INTO `paper_questions` VALUES (25, 32, 25, 20);
INSERT INTO `paper_questions` VALUES (26, 32, 26, 20);
INSERT INTO `paper_questions` VALUES (27, 32, 27, 20);
INSERT INTO `paper_questions` VALUES (28, 33, 12, 25);
INSERT INTO `paper_questions` VALUES (29, 33, 14, 25);
INSERT INTO `paper_questions` VALUES (30, 33, 18, 50);
INSERT INTO `paper_questions` VALUES (31, 34, 12, 16.5);
INSERT INTO `paper_questions` VALUES (32, 34, 14, 16.5);
INSERT INTO `paper_questions` VALUES (33, 34, 18, 34);
INSERT INTO `paper_questions` VALUES (34, 34, 21, 16.5);
INSERT INTO `paper_questions` VALUES (35, 34, 23, 16.5);
INSERT INTO `paper_questions` VALUES (36, 35, 12, 20);
INSERT INTO `paper_questions` VALUES (37, 35, 14, 20);
INSERT INTO `paper_questions` VALUES (38, 35, 18, 20);
INSERT INTO `paper_questions` VALUES (39, 35, 21, 20);
INSERT INTO `paper_questions` VALUES (40, 35, 23, 20);
INSERT INTO `paper_questions` VALUES (41, 36, 12, 7.27273);
INSERT INTO `paper_questions` VALUES (42, 36, 14, 7.27273);
INSERT INTO `paper_questions` VALUES (43, 36, 18, 10);
INSERT INTO `paper_questions` VALUES (44, 36, 21, 7.27273);
INSERT INTO `paper_questions` VALUES (45, 36, 23, 7.27273);
INSERT INTO `paper_questions` VALUES (46, 36, 24, 7.27273);
INSERT INTO `paper_questions` VALUES (47, 36, 25, 7.27273);
INSERT INTO `paper_questions` VALUES (48, 36, 26, 7.27273);
INSERT INTO `paper_questions` VALUES (49, 36, 27, 7.27273);
INSERT INTO `paper_questions` VALUES (50, 36, 32, 7.27273);
INSERT INTO `paper_questions` VALUES (51, 36, 33, 7.27273);
INSERT INTO `paper_questions` VALUES (52, 36, 34, 7.27273);
INSERT INTO `paper_questions` VALUES (53, 36, 35, 10);
INSERT INTO `paper_questions` VALUES (54, 37, 33, 15);
INSERT INTO `paper_questions` VALUES (55, 37, 34, 15);
INSERT INTO `paper_questions` VALUES (56, 37, 35, 39);
INSERT INTO `paper_questions` VALUES (57, 38, 12, 20);
INSERT INTO `paper_questions` VALUES (58, 38, 14, 20);
INSERT INTO `paper_questions` VALUES (59, 38, 18, 20);
INSERT INTO `paper_questions` VALUES (60, 38, 21, 20);
INSERT INTO `paper_questions` VALUES (61, 38, 23, 20);
INSERT INTO `paper_questions` VALUES (62, 39, 12, 10);
INSERT INTO `paper_questions` VALUES (63, 39, 14, 10);
INSERT INTO `paper_questions` VALUES (64, 39, 18, 60);
INSERT INTO `paper_questions` VALUES (65, 39, 21, 10);
INSERT INTO `paper_questions` VALUES (66, 39, 23, 10);
INSERT INTO `paper_questions` VALUES (67, 26, 21, 10);
INSERT INTO `paper_questions` VALUES (68, 26, 18, 5);
INSERT INTO `paper_questions` VALUES (69, 26, 24, 10);
INSERT INTO `paper_questions` VALUES (70, 26, 25, 10);
INSERT INTO `paper_questions` VALUES (71, 40, 34, 50);
INSERT INTO `paper_questions` VALUES (72, 40, 35, 50);
INSERT INTO `paper_questions` VALUES (73, 26, 23, 10);
INSERT INTO `paper_questions` VALUES (74, 26, 27, 10);
INSERT INTO `paper_questions` VALUES (75, 26, 32, 10);
INSERT INTO `paper_questions` VALUES (76, 26, 35, 10);
INSERT INTO `paper_questions` VALUES (77, 41, 18, 50);
INSERT INTO `paper_questions` VALUES (78, 41, 21, 50);
INSERT INTO `paper_questions` VALUES (79, 42, 12, 50);
INSERT INTO `paper_questions` VALUES (80, 42, 14, 50);
INSERT INTO `paper_questions` VALUES (81, 26, 36, 5);

-- ----------------------------
-- Table structure for papers
-- ----------------------------
DROP TABLE IF EXISTS `papers`;
CREATE TABLE `papers`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '试卷编号',
  `exam_id` int NULL DEFAULT NULL COMMENT '所属考试编号',
  `student_id` int NULL DEFAULT NULL COMMENT '参加考试的学生编号',
  `score` int NULL DEFAULT NULL COMMENT '得分',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_mysql500_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of papers
-- ----------------------------

-- ----------------------------
-- Table structure for question_options
-- ----------------------------
DROP TABLE IF EXISTS `question_options`;
CREATE TABLE `question_options`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '选项主键 ID，自增',
  `question_id` bigint NULL DEFAULT NULL COMMENT '题目 ID，关联题目表',
  `option_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '选项内容',
  `is_correct` bigint NOT NULL COMMENT '是否是正确答案（1 表示是，0 表示否）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 274 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '题目选项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question_options
-- ----------------------------
INSERT INTO `question_options` VALUES (15, 14, 'Administrator', 0);
INSERT INTO `question_options` VALUES (16, 14, 'root', 1);
INSERT INTO `question_options` VALUES (17, 14, 'guest', 0);
INSERT INTO `question_options` VALUES (77, 18, '是的', 1);
INSERT INTO `question_options` VALUES (78, 18, '似的', 0);
INSERT INTO `question_options` VALUES (108, 23, '电感', 0);
INSERT INTO `question_options` VALUES (109, 23, '电压源', 0);
INSERT INTO `question_options` VALUES (110, 23, '电阻0', 0);
INSERT INTO `question_options` VALUES (111, 23, '电容', 1);
INSERT INTO `question_options` VALUES (112, 24, '外电路上电流比较小', 0);
INSERT INTO `question_options` VALUES (113, 24, '外电路断路', 0);
INSERT INTO `question_options` VALUES (114, 24, '外电路短路', 1);
INSERT INTO `question_options` VALUES (115, 24, '电源内电阻为零', 0);
INSERT INTO `question_options` VALUES (124, 26, 'u1比u2超前30°', 0);
INSERT INTO `question_options` VALUES (125, 26, 'u1比u2超前90°', 1);
INSERT INTO `question_options` VALUES (126, 26, '无法判断', 0);
INSERT INTO `question_options` VALUES (127, 26, 'u1比u2滞后30°', 0);
INSERT INTO `question_options` VALUES (128, 27, '1×103J( )', 0);
INSERT INTO `question_options` VALUES (129, 27, '6×103 J', 0);
INSERT INTO `question_options` VALUES (130, 27, ' 6×104 J', 1);
INSERT INTO `question_options` VALUES (131, 27, '1×104 J', 0);
INSERT INTO `question_options` VALUES (144, 21, '111', 0);
INSERT INTO `question_options` VALUES (145, 21, '222', 1);
INSERT INTO `question_options` VALUES (146, 21, '333', 0);
INSERT INTO `question_options` VALUES (147, 21, '555', 0);
INSERT INTO `question_options` VALUES (166, 33, '1111', 0);
INSERT INTO `question_options` VALUES (167, 33, '1111', 0);
INSERT INTO `question_options` VALUES (168, 33, '222', 1);
INSERT INTO `question_options` VALUES (169, 33, '333', 0);
INSERT INTO `question_options` VALUES (174, 34, '铸、铸', 0);
INSERT INTO `question_options` VALUES (175, 34, '筑、筑', 0);
INSERT INTO `question_options` VALUES (176, 34, '铸、筑', 1);
INSERT INTO `question_options` VALUES (177, 34, '筑、铸', 0);
INSERT INTO `question_options` VALUES (187, 25, '指针偏转到最左端时，电阻为无穷大', 1);
INSERT INTO `question_options` VALUES (188, 25, '指针偏转到最右端时，电阻为无穷大', 0);
INSERT INTO `question_options` VALUES (189, 25, '刻度是线性的', 0);
INSERT INTO `question_options` VALUES (190, 25, '--', 0);
INSERT INTO `question_options` VALUES (219, 32, '222', 0);
INSERT INTO `question_options` VALUES (220, 32, '333', 1);
INSERT INTO `question_options` VALUES (221, 32, '444夫覅', 0);
INSERT INTO `question_options` VALUES (222, 32, '555', 0);
INSERT INTO `question_options` VALUES (224, 36, '风的季节国防教育府库一份酷酷酷酷酷酷酷酷酷酷酷酷酷', 1);
INSERT INTO `question_options` VALUES (225, 12, 'Centos', 0);
INSERT INTO `question_options` VALUES (226, 12, 'Ubuntu', 0);
INSERT INTO `question_options` VALUES (227, 12, 'Red Hat', 1);
INSERT INTO `question_options` VALUES (228, 12, 'BSDeeeeee', 0);
INSERT INTO `question_options` VALUES (253, 35, '共同开拓辽阔的疆域', 1);
INSERT INTO `question_options` VALUES (254, 35, '共同书写悠久的历史', 1);
INSERT INTO `question_options` VALUES (255, 35, '共同创造灿烂的文化', 1);
INSERT INTO `question_options` VALUES (256, 35, '共同培育伟大民族精神.', 1);
INSERT INTO `question_options` VALUES (257, 35, '', 0);
INSERT INTO `question_options` VALUES (262, 43, '嗡嗡嗡', 0);
INSERT INTO `question_options` VALUES (263, 43, '威威', 0);
INSERT INTO `question_options` VALUES (264, 43, '33', 1);
INSERT INTO `question_options` VALUES (265, 43, '333', 0);
INSERT INTO `question_options` VALUES (270, 45, '裴李岗文化', 1);
INSERT INTO `question_options` VALUES (271, 45, '彭头山文化', 0);
INSERT INTO `question_options` VALUES (272, 45, '良渚文化', 0);
INSERT INTO `question_options` VALUES (273, 45, '红山文化', 0);

-- ----------------------------
-- Table structure for questions
-- ----------------------------
DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '题目主键 ID，自增',
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '题目标题',
  `tags` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '题目标签，多个标签可以用逗号分隔',
  `category_id` bigint NULL DEFAULT NULL COMMENT '题目所属分类 ID，关联分类表',
  `question_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '题型，如单选题、多选题等',
  `total_answer_count` int NULL DEFAULT 1 COMMENT '答题总次数',
  `wrong_answer_count` int NULL DEFAULT 0 COMMENT '错误答题次数',
  `create_time` datetime NULL DEFAULT NULL COMMENT '题目创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '题目更新时间',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建题目用户 ID',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新题目用户 ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '题目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of questions
-- ----------------------------
INSERT INTO `questions` VALUES (12, '以下软件中，()不是 Linux 系统的发行版本。', '', 1, '单选题', 3, 1, '2024-09-17 15:06:16', '2025-12-05 15:35:03', 4, 2);
INSERT INTO `questions` VALUES (14, '登录Linux 字符界面后，命令提示符中最后的符号为“#”，表示当前的用户是(  ）。', '', 1, '单选题', 5, 3, '2024-09-18 18:18:07', '2024-09-18 18:18:06', 4, 4);
INSERT INTO `questions` VALUES (18, '你是中国人吗', '', 1, '判断题', 8, 0, '2024-09-19 20:04:49', '2024-09-19 20:04:49', 4, 4);
INSERT INTO `questions` VALUES (21, '新增测试', '', 1, '单选题', 3, 0, '2024-09-20 00:18:52', '2024-09-20 00:18:52', 4, 4);
INSERT INTO `questions` VALUES (23, '在稳定的直流电路中，可按开路处理的元件是(   )', '', 28, '单选题', 4, 4, '2024-09-20 22:48:12', '2024-09-20 22:48:12', 4, 4);
INSERT INTO `questions` VALUES (24, '用电压表测得电路端电压为0，这说明(   )', '', 28, '单选题', 5, 5, '2024-09-20 23:08:07', NULL, 4, 4);
INSERT INTO `questions` VALUES (25, '用万用表测量电阻的刻度，下列说法正确的是( )', '', 28, '单选题', 6, 6, '2024-09-20 23:09:03', NULL, 4, 4);
INSERT INTO `questions` VALUES (26, '已知u1=20sin(314t+π/6)A,   u2=10sin(314t-π/3)A,则(  )。', '', 28, '单选题', 7, 7, '2024-09-20 23:10:22', NULL, 4, 4);
INSERT INTO `questions` VALUES (27, '220V的照明用输电线，每根导线电阻为1Ω，通过电流为10A，则10min内可产生热量(   )', '', 28, '单选题', 8, 0, '2024-09-20 23:18:05', NULL, 4, 4);
INSERT INTO `questions` VALUES (32, '测试5555', '', 26, '单选题', 5, 0, '2024-11-17 14:26:32', '2025-11-16 15:59:18', 4, 4);
INSERT INTO `questions` VALUES (33, '测试', '', 27, '单选题', 6, 0, '2024-11-17 16:05:02', NULL, 4, 4);
INSERT INTO `questions` VALUES (34, '（）牢中华民族共同体意识：中华民族一家亲，同心共（）中国梦：（）', '', 1, '单选题', 10, 0, '2025-11-14 11:52:36', NULL, 5, 5);
INSERT INTO `questions` VALUES (35, '正确的中华民族历史观是指生活在中华大地上的人们：（）', '', 1, '多选题', 1, 0, '2025-11-14 15:04:48', '2025-12-16 08:43:53', 5, 5);
INSERT INTO `questions` VALUES (36, '哈哈哈哈哈哈哈哈哈哈', '', 1, '简答题', 1, 0, '2025-12-02 08:52:11', '2025-12-02 08:52:38', 5, 5);
INSERT INTO `questions` VALUES (43, '草草草草5655', '', 27, '单选题', 1, 0, '2025-12-16 11:02:45', '2025-12-16 11:02:57', 5, 5);
INSERT INTO `questions` VALUES (45, '早期中华文化圈或文化上的早期中国开始萌芽，代表性的文化是', '', 1, '单选题', 1, 0, '2025-12-16 11:04:26', NULL, 5, 5);

-- ----------------------------
-- Table structure for student_info
-- ----------------------------
DROP TABLE IF EXISTS `student_info`;
CREATE TABLE `student_info`  (
  `student_id` int NOT NULL AUTO_INCREMENT COMMENT '学生的唯一标识 ID',
  `student_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_mysql500_ci NOT NULL COMMENT '学生姓名',
  `student_number` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_mysql500_ci NOT NULL COMMENT '学生学号',
  `class_id` int NOT NULL COMMENT '学生所属班级的 ID',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_mysql500_ci NOT NULL COMMENT '学生密码',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '学生信息创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '学生信息最后更新时间',
  PRIMARY KEY (`student_id`) USING BTREE,
  UNIQUE INDEX `student_number`(`student_number` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_mysql500_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_info
-- ----------------------------
INSERT INTO `student_info` VALUES (1, '柔柔弱弱', '20240010', 1, '$2a$10$C1BGDDuYstrL4OUTcmVJDeC.rphMhvGp07mu.9OYCD78c0sjYezI2', '2024-09-02 23:41:53', '2024-09-03 21:45:23');
INSERT INTO `student_info` VALUES (2, 'z2', '20240012', 1, '$2a$10$cjJPQzbijxZ/8AYt8VRfGONvwMzEDkvtDpdIUwq8u.Zjh/WI5U9Ji', '2024-09-03 23:17:05', '2024-09-03 23:17:05');
INSERT INTO `student_info` VALUES (3, 'z4', '20240013', 1, '$2a$10$dIzduhZ06pLSd6VjDUuKdeWfmBrqVYp4wL80sumVxXOK4HU8Pab9K', '2024-09-03 23:17:12', '2024-09-03 23:17:12');
INSERT INTO `student_info` VALUES (4, 'z6', '20240015', 1, '$2a$10$NElWTd99Z.jpEabzbrfKcOB1oBOzolU8YYk6QAHVgJ2jD7XULW8eq', '2024-09-03 23:17:19', '2024-09-03 23:17:19');
INSERT INTO `student_info` VALUES (5, '酷酷酷', '20240017', 1, '$2a$10$1fArINyatcoE1T5SRvFSee2P5ZVUgBpMSHBP5yXzyEjmaHJA2yl5q', '2024-09-03 23:17:39', '2024-09-03 23:17:39');
INSERT INTO `student_info` VALUES (6, '呃呃呃', '20240018', 1, '$2a$10$Ucw7x5Gdr5mC95BoOPTECujOdVmSsLDevSZTa32XOQNygbFxwATcS', '2024-09-03 23:17:49', '2024-09-03 23:17:49');
INSERT INTO `student_info` VALUES (7, '去去去', '20240019', 1, '$2a$10$J9Utra/nUQclsWfTodUnDufmYzAxOjbOw8lGJho/yDnTStUpdMgA2', '2024-09-03 23:17:57', '2024-09-03 23:17:57');
INSERT INTO `student_info` VALUES (8, '嘎嘎嘎', '20240020', 1, '$2a$10$/WRViI21fOVcRCO/UcyJTuql44jemIrxOxPVpn8LFIxbGThW.ms6G', '2024-09-03 23:18:06', '2024-09-03 23:18:06');
INSERT INTO `student_info` VALUES (9, '热热热', '20240021', 1, '$2a$10$Cv9moM.XzlRK6CEkJ3fgS.5k6XSxsYJZEHTgEaItp01WDQre6ZhkG', '2024-09-03 23:18:20', '2024-09-03 23:18:20');
INSERT INTO `student_info` VALUES (10, '涛涛涛', '20240022', 1, '$2a$10$Z66PLORCkwx8.F.WkEmMHODk0jVoNpQw3Nz.Ea4kgqhWIvhu8BeAe', '2024-09-03 23:19:11', '2024-09-03 23:19:11');
INSERT INTO `student_info` VALUES (11, '么么么', '20240026', 1, '$2a$10$H8p0YVrAtjDJWz6Bu1u/EeiFfj5IdxZKA19t4gCSC..ytnZxp0Uem', '2024-09-03 23:19:23', '2024-09-03 23:19:23');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '姓名',
  `student_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '学号',
  `password` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '密码',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '手机号',
  `status` int NULL DEFAULT 1 COMMENT '状态 0:禁用，1:启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `img` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '用户头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`student_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '用户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '管理员', '20240001', '123456', '10086', 1, '2024-08-27 16:40:10', '2024-09-02 15:15:00', NULL);
INSERT INTO `user` VALUES (2, '张三', '20240002', '123456', '10086', 1, NULL, '2024-09-17 15:15:05', NULL);
INSERT INTO `user` VALUES (3, 'fshytj666', '20240003', '$2a$10$4W2bol8/ckBcERchIaSnTuBcDioU2vcIi.uZc.wNuY3bO6l03m19O', '13212345678', 1, NULL, '2024-09-20 00:33:10', NULL);
INSERT INTO `user` VALUES (4, '何时去1', 'dd55555', '$2a$10$9drVXyXenl8fJvb8/HnBWO6qyCtJp3AlfupMZl.Td21iu9D/wIAmO', '13123456789', 1, NULL, '2026-01-18 22:51:06', 'https://wm-plus-images.oss-cn-beijing.aliyuncs.com/73d8baf0-2d53-4bc3-8776-8ad353ae0d27.png');
INSERT INTO `user` VALUES (5, '哈哈哈哈', '20251111', '$2a$10$b/r5uNanPVvp0mt4U0G2AuW25V71X.uU1PFcl.BVFijeZ0PJD/9Ey', '10010', 1, '2025-11-05 09:38:31', '2026-01-18 22:46:39', 'https://wm-plus-images.oss-cn-beijing.aliyuncs.com/ddb0197d-3f55-43ac-86be-78b95fec52a5.jpg');
INSERT INTO `user` VALUES (6, 'test', 'test123', '$2a$10$KhdyXfFjiemNGt6ivwDpWOYTUxTN09w/pFmZ1c2oJUzm9SgRgXTZ6', '13412345678', 1, '2025-11-07 15:07:55', '2026-01-18 22:45:57', 'https://wm-plus-images.oss-cn-beijing.aliyuncs.com/644fedcd-217f-40b2-8260-120f12ce9621.png');

SET FOREIGN_KEY_CHECKS = 1;
