/*
 Navicat Premium Data Transfer

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : localhost:3306
 Source Schema         : emp_sys

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 30/05/2026 02:09:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attendance
-- ----------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '考勤ID',
  `employee_id` bigint NOT NULL COMMENT '员工ID',
  `attendance_date` date NOT NULL COMMENT '考勤日期',
  `sign_in_time` datetime NULL DEFAULT NULL COMMENT '签到时间',
  `sign_out_time` datetime NULL DEFAULT NULL COMMENT '签退时间',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '\r\n  考勤状态：\r\n  0-正常\r\n  1-迟到\r\n  2-早退\r\n  3-缺卡\r\n  4-请假\r\n  ',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_employee_date`(`employee_id` ASC, `attendance_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考勤记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attendance
-- ----------------------------
INSERT INTO `attendance` VALUES (1, 2, '2026-05-20', '2026-05-20 08:55:00', '2026-05-20 18:05:00', 0, '正常上班', '2026-05-26 14:49:57');
INSERT INTO `attendance` VALUES (2, 2, '2026-05-21', '2026-05-21 09:18:00', '2026-05-21 18:00:00', 1, '迟到18分钟', '2026-05-26 14:49:57');
INSERT INTO `attendance` VALUES (3, 3, '2026-05-20', '2026-05-20 08:59:00', '2026-05-20 17:20:00', 2, '提前离开', '2026-05-26 14:49:57');
INSERT INTO `attendance` VALUES (4, 4, '2026-05-20', NULL, '2026-05-20 18:00:00', 3, '缺少签到记录', '2026-05-26 14:49:57');
INSERT INTO `attendance` VALUES (5, 5, '2026-05-20', NULL, NULL, 4, '请假', '2026-05-26 14:49:57');
INSERT INTO `attendance` VALUES (6, 6, '2026-05-20', '2026-05-20 08:40:00', '2026-05-20 18:10:00', 0, '正常上班', '2026-05-26 14:49:57');
INSERT INTO `attendance` VALUES (7, 1, '2026-05-29', '2026-05-29 09:00:00', '2026-05-29 18:00:00', 0, NULL, '2026-05-29 11:04:55');
INSERT INTO `attendance` VALUES (8, 2, '2026-05-29', NULL, NULL, 3, NULL, '2026-05-29 11:04:55');
INSERT INTO `attendance` VALUES (9, 3, '2026-05-29', NULL, NULL, 3, NULL, '2026-05-29 11:04:55');
INSERT INTO `attendance` VALUES (10, 4, '2026-05-29', NULL, NULL, 3, NULL, '2026-05-29 11:04:55');
INSERT INTO `attendance` VALUES (11, 5, '2026-05-29', NULL, NULL, 3, NULL, '2026-05-29 11:04:55');
INSERT INTO `attendance` VALUES (12, 6, '2026-05-29', NULL, NULL, 3, NULL, '2026-05-29 11:04:55');
INSERT INTO `attendance` VALUES (13, 7, '2026-05-29', NULL, NULL, 3, NULL, '2026-05-29 11:04:55');
INSERT INTO `attendance` VALUES (14, 8, '2026-05-29', NULL, NULL, 3, NULL, '2026-05-29 11:04:55');
INSERT INTO `attendance` VALUES (15, 1, '2026-05-30', NULL, NULL, 0, NULL, '2026-05-30 01:18:13');
INSERT INTO `attendance` VALUES (16, 2, '2026-05-30', NULL, NULL, 0, NULL, '2026-05-30 01:18:13');
INSERT INTO `attendance` VALUES (17, 3, '2026-05-30', NULL, NULL, 0, NULL, '2026-05-30 01:18:13');
INSERT INTO `attendance` VALUES (18, 4, '2026-05-30', NULL, NULL, 0, NULL, '2026-05-30 01:18:13');
INSERT INTO `attendance` VALUES (19, 5, '2026-05-30', NULL, NULL, 0, NULL, '2026-05-30 01:18:13');
INSERT INTO `attendance` VALUES (20, 6, '2026-05-30', NULL, NULL, 0, NULL, '2026-05-30 01:18:13');
INSERT INTO `attendance` VALUES (21, 7, '2026-05-30', NULL, NULL, 0, NULL, '2026-05-30 01:18:13');
INSERT INTO `attendance` VALUES (22, 8, '2026-05-30', NULL, NULL, 0, NULL, '2026-05-30 01:18:13');

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dept
-- ----------------------------
INSERT INTO `dept` VALUES (1, '总经办', '公司核心管理部门', '2026-05-26 14:48:46', '2026-05-26 17:32:48', 0);
INSERT INTO `dept` VALUES (2, '技术部', '负责系统开发与维护', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `dept` VALUES (3, '产品部', '负责产品设计与需求分析', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `dept` VALUES (4, '市场部', '负责市场推广与运营', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `dept` VALUES (5, '人事部', '负责人力资源管理', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `dept` VALUES (6, '财务部', '负责财务与报销', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `dept` VALUES (7, '调研部', '做市场调研的', '2026-05-26 17:05:28', '2026-05-26 17:33:59', 1);

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `user_id` bigint NOT NULL COMMENT '关联用户ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  `position_id` bigint NOT NULL COMMENT '职位ID',
  `real_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '真实姓名',
  `gender` tinyint NULL DEFAULT 1 COMMENT '性别：0-女，1-男',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `hire_date` date NULL DEFAULT NULL COMMENT '入职日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE,
  INDEX `idx_position_id`(`position_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, 4, 1, 1, '系统管理员', 1, 18, 'https://management33.oss-cn-beijing.aliyuncs.com//avatar/1/1779938169035_bb67f4a1.jpg', '13344556677', '144455@example.com', '广州', '2026-05-26', '2026-05-26 14:34:54', '2026-05-28 11:16:10', 0);
INSERT INTO `employee` VALUES (2, 5, 2, 2, '张三', 1, 24, 'https://dummyimage.com/100x100', '13800000001', 'zhangsan@example.com', '广州天河区', '2024-03-12', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `employee` VALUES (3, 6, 2, 3, '李四', 1, 25, 'https://dummyimage.com/100x100', '13800000002', 'lisi@example.com', '深圳南山区', '2024-04-01', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `employee` VALUES (4, 7, 3, 5, '王五', 1, 29, 'https://dummyimage.com/100x100', '13800000003', 'wangwu@example.com', '广州白云区', '2023-11-15', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `employee` VALUES (5, 8, 4, 7, '赵六', 0, 23, 'https://dummyimage.com/100x100', '13800000004', 'zhaoliu@example.com', '佛山禅城区', '2025-01-08', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `employee` VALUES (6, 9, 5, 8, '陈七', 0, 27, 'https://dummyimage.com/100x100', '13800000005', 'chenqi@example.com', '东莞南城区', '2022-09-21', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `employee` VALUES (7, 10, 1, 1, '测试管理员', 1, 30, 'https://dummyimage.com/100x100', '13800000006', 'admin_test@example.com', '广州越秀区', '2021-05-01', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `employee` VALUES (8, 11, 5, 5, '勾八', 0, 21, 'https://avatars.githubusercontent.com/u/48496739', '14791692459', '223354@example.com', '春街84号', '1989-10-27', '2026-05-28 08:53:22', '2026-05-28 10:38:11', 0);

-- ----------------------------
-- Table structure for leave_request
-- ----------------------------
DROP TABLE IF EXISTS `leave_request`;
CREATE TABLE `leave_request`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '请假ID',
  `employee_id` bigint NOT NULL COMMENT '申请员工ID',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `days` int NULL DEFAULT NULL COMMENT '请假天数',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请假类型',
  `reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请假原因',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '\r\n  审批状态：\r\n  0-待审批\r\n  1-已通过\r\n  2-已拒绝\r\n  ',
  `approver_id` bigint NULL DEFAULT NULL COMMENT '审批人ID',
  `approve_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批备注',
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `approve_time` datetime NULL DEFAULT NULL COMMENT '审批时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_employee_id`(`employee_id` ASC) USING BTREE,
  INDEX `idx_approver_id`(`approver_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '请假申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of leave_request
-- ----------------------------
INSERT INTO `leave_request` VALUES (1, 2, '2026-05-10', '2026-05-12', 3, '事假', '家中有事需要处理', 1, 1, '同意请假', '2026-05-26 14:48:46', '2026-05-09 10:20:00', '2026-05-26 14:48:46', '2026-05-26 14:48:46');
INSERT INTO `leave_request` VALUES (2, 3, '2026-05-15', '2026-05-15', 1, '病假', '感冒发烧', 1, 1, '注意休息', '2026-05-26 14:48:46', '2026-05-14 18:00:00', '2026-05-26 14:48:46', '2026-05-26 14:48:46');
INSERT INTO `leave_request` VALUES (3, 4, '2026-05-20', '2026-05-22', 3, '年假', '外出旅游', 0, NULL, NULL, '2026-05-26 14:48:46', NULL, '2026-05-26 14:48:46', '2026-05-26 14:48:46');
INSERT INTO `leave_request` VALUES (4, 5, '2026-05-18', '2026-05-18', 1, '调休', '个人事务', 2, 1, '当前项目较忙，暂不批准', '2026-05-26 14:48:46', '2026-05-17 16:30:00', '2026-05-26 14:48:46', '2026-05-26 14:48:46');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告内容',
  `publisher_id` bigint NOT NULL COMMENT '发布人ID',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '\r\n  状态：\r\n  0-草稿\r\n  1-已发布\r\n  ',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_publisher_id`(`publisher_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, '五一放假通知', '根据公司安排，五一劳动节放假5天，请大家提前安排工作。', 4, 1, '2026-05-26 14:48:46', '2026-05-26 14:48:46');
INSERT INTO `notice` VALUES (2, '系统升级通知', '本周六晚上22点进行服务器维护升级，请提前保存数据。', 4, 1, '2026-05-26 14:48:46', '2026-05-26 14:48:46');
INSERT INTO `notice` VALUES (3, '员工团建活动', '下周五组织员工团建，请大家积极参加。', 7, 1, '2026-05-26 14:48:46', '2026-05-26 14:48:46');
INSERT INTO `notice` VALUES (4, '招聘公告', '技术部新增前端开发岗位，欢迎内部推荐。', 6, 1, '2026-05-26 14:48:46', '2026-05-26 14:48:46');

-- ----------------------------
-- Table structure for position
-- ----------------------------
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '职位ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '职位名称',
  `dept_id` bigint NOT NULL COMMENT '所属部门ID',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职位描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '职位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of position
-- ----------------------------
INSERT INTO `position` VALUES (1, '系统管理员', 1, '系统最高权限管理员', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `position` VALUES (2, 'Java开发工程师', 2, '负责后端开发', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `position` VALUES (3, '前端开发工程师', 2, '负责前端开发', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `position` VALUES (4, '测试工程师', 2, '负责系统测试', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `position` VALUES (5, '产品经理', 3, '负责产品规划', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `position` VALUES (6, 'UI设计师', 3, '负责界面设计', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `position` VALUES (7, '运营专员', 4, '负责运营推广', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `position` VALUES (8, '招聘专员', 5, '负责人员招聘', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `position` VALUES (9, '财务专员', 6, '负责财务管理', '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `position` VALUES (10, '靳娜', 2, '很加而称。列我面制列应组快常。', '2026-05-27 14:45:50', '2026-05-27 17:04:37', 1);
INSERT INTO `position` VALUES (11, '尔馥君', 4, '受斯拉值采风证。的起厂自志线没作两。品低同管电。水史门况系表光完全。', '2026-05-27 14:47:36', '2026-05-27 15:29:31', 0);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '操作用户ID',
  `operation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1, 4, '用户登录', 'POST /api/auth/login', '{\"username\":\"admin\"}', '127.0.0.1', '2026-05-26 14:49:57');
INSERT INTO `sys_log` VALUES (2, 5, '新增请假申请', 'POST /api/leave/add', '{\"days\":3}', '127.0.0.1', '2026-05-26 14:49:57');
INSERT INTO `sys_log` VALUES (3, 6, '查询员工列表', 'GET /api/employee/list', '{}', '127.0.0.1', '2026-05-26 14:49:57');
INSERT INTO `sys_log` VALUES (4, 7, '发布公告', 'POST /api/notice/add', '{\"title\":\"系统升级通知\"}', '127.0.0.1', '2026-05-26 14:49:57');
INSERT INTO `sys_log` VALUES (5, 4, '删除员工', 'DELETE /api/employee/6', '{}', '127.0.0.1', '2026-05-26 14:49:57');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密密码',
  `role` tinyint NOT NULL DEFAULT 1 COMMENT '角色：0-管理员，1-员工',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (4, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 0, 1, '2026-05-26 11:48:29', '2026-05-26 15:44:47', 0);
INSERT INTO `sys_user` VALUES (5, 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `sys_user` VALUES (6, 'lisi', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `sys_user` VALUES (7, 'wangwu', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `sys_user` VALUES (8, 'zhaoliu', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `sys_user` VALUES (9, 'chenqi', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `sys_user` VALUES (10, 'admin_test', 'e10adc3949ba59abbe56e057f20f883e', 0, 1, '2026-05-26 14:48:46', '2026-05-26 14:48:46', 0);
INSERT INTO `sys_user` VALUES (11, 'gouba', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, '2026-05-28 09:38:32', '2026-05-28 09:38:32', 0);

SET FOREIGN_KEY_CHECKS = 1;
