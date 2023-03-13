/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:3306
 Source Schema         : ai_home

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 11/08/2020 13:52:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for home
-- ----------------------------
DROP TABLE IF EXISTS `home`;
CREATE TABLE `home`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基础表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_carousel
-- ----------------------------
DROP TABLE IF EXISTS `home_carousel`;
CREATE TABLE `home_carousel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '轮播图',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态（0 禁用 1 启用）',
  `sort` int(10) NULL DEFAULT NULL COMMENT '排序',
  `page_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '轮播图表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_chat_record
-- ----------------------------
DROP TABLE IF EXISTS `home_chat_record`;
CREATE TABLE `home_chat_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `home_consultant_user_id` int(11) NULL DEFAULT NULL COMMENT '用户顾问表Id',
  `send_id` int(11) NULL DEFAULT NULL COMMENT '发送人Id',
  `receive_id` int(11) NULL DEFAULT NULL COMMENT '接收人Id',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `type` int(11) NULL DEFAULT NULL COMMENT '消息类型（1、文字 2、测评 3、购房报告 4、评价 5、置业顾问名片 6、照片 7 快速回复）',
  `status` int(11) NULL DEFAULT NULL COMMENT '读取状态（0 未读 1 已读）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_consultant
-- ----------------------------
DROP TABLE IF EXISTS `home_consultant`;
CREATE TABLE `home_consultant`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `type` int(1) NULL DEFAULT NULL COMMENT '类型（1 客户顾问 2 置业顾问）',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态（1 认证中 2 认证成功 3 认证失败）',
  `star` int(1) NULL DEFAULT NULL COMMENT '星级（1-5）',
  `advisory_count` int(10) NULL DEFAULT NULL COMMENT '咨询次数',
  `buy_home_count` int(10) NULL DEFAULT NULL COMMENT '推荐置业次数',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `home_user_id` int(11) NULL DEFAULT NULL COMMENT '用户Id',
  `firm_id` int(11) NULL DEFAULT NULL COMMENT '企业Id',
  `project` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '项目信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '顾问表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_consultant_audit
-- ----------------------------
DROP TABLE IF EXISTS `home_consultant_audit`;
CREATE TABLE `home_consultant_audit`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `home_consultant_id` int(11) NULL DEFAULT NULL COMMENT '顾问Id',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态（0 拒绝 1 同意 ）',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `type` int(10) NULL DEFAULT NULL COMMENT '类型（1、申请 2、平台审核 3、企业审核）',
  `operate_id` int(11) NULL DEFAULT NULL COMMENT '操作人Id',
  `operate_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '顾问审核记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_consultant_comment
-- ----------------------------
DROP TABLE IF EXISTS `home_consultant_comment`;
CREATE TABLE `home_consultant_comment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `home_consultant_id` int(11) NULL DEFAULT NULL COMMENT '顾问Id',
  `comment` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论',
  `star` int(1) NULL DEFAULT NULL COMMENT '打星（1-5）',
  `home_user_id` int(11) NULL DEFAULT NULL COMMENT '用户Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '顾问评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_consultant_user
-- ----------------------------
DROP TABLE IF EXISTS `home_consultant_user`;
CREATE TABLE `home_consultant_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `home_consultant_id` int(11) NULL DEFAULT NULL COMMENT '顾问表Id',
  `home_user_id` int(11) NULL DEFAULT NULL COMMENT '用户Id',
  `home_consultant_user_id` int(11) NULL DEFAULT NULL COMMENT '顾问用户Id',
  `recommend_id` int(11) NULL DEFAULT NULL COMMENT '顾问表Id（AI推荐）',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态（0 拉黑 1 正常）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户顾问表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_demand
-- ----------------------------
DROP TABLE IF EXISTS `home_demand`;
CREATE TABLE `home_demand`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `region` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `price` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '价格',
  `area` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '面积',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `explanation` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `home_user_id` int(11) NULL DEFAULT NULL COMMENT '用户Id',
  `is_delete` int(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除 0 是 1 否',
  `ai_consultant_id` int(11) NULL DEFAULT NULL COMMENT 'AI顾问Id',
  `home_consultant_id` int(11) NULL DEFAULT NULL COMMENT '置业顾问Id',
  `intentions` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '意向楼盘',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '购房需求表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_evaluation
-- ----------------------------
DROP TABLE IF EXISTS `home_evaluation`;
CREATE TABLE `home_evaluation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 0 隐藏 1 显示',
  `type` int(1) NULL DEFAULT NULL COMMENT '类型 1 测评报告 2 快讯',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
  `original` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原创',
  `the_public_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公众号名称',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `main_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主题图',
  `forward_count` int(10) NULL DEFAULT NULL COMMENT '转发数',
  `like_count` int(10) NULL DEFAULT NULL COMMENT '点赞数',
  `comment_count` int(10) NULL DEFAULT NULL COMMENT '评论数',
  `collect_count` int(10) NULL DEFAULT NULL COMMENT '收藏数',
  `region` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `price` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '价格',
  `area` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '面积',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `home_user_id` int(11) NULL DEFAULT NULL COMMENT '用户Id',
  `sys_user_id` int(11) NULL DEFAULT NULL COMMENT '管理员Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '测评表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_evaluation_collect
-- ----------------------------
DROP TABLE IF EXISTS `home_evaluation_collect`;
CREATE TABLE `home_evaluation_collect`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `home_evaluation_id` int(11) NULL DEFAULT NULL COMMENT '测评表Id',
  `home_user_id` int(11) NULL DEFAULT NULL COMMENT '用户Id',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态（0 失效 1 生效）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '测评收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_evaluation_comment
-- ----------------------------
DROP TABLE IF EXISTS `home_evaluation_comment`;
CREATE TABLE `home_evaluation_comment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `home_evaluation_id` int(11) NULL DEFAULT NULL COMMENT '测评表Id',
  `home_user_id` int(11) NULL DEFAULT NULL COMMENT '用户Id',
  `comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论',
  `like_count` int(11) NULL DEFAULT NULL COMMENT '点赞数',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 1 未审核 2 审核通过 3 审核失败',
  `operate_id` int(11) NULL DEFAULT NULL COMMENT '操作人Id',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '测评评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_evaluation_comment_like
-- ----------------------------
DROP TABLE IF EXISTS `home_evaluation_comment_like`;
CREATE TABLE `home_evaluation_comment_like`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `home_evaluation_comment_id` int(11) NULL DEFAULT NULL COMMENT '测评评论表Id',
  `home_user_id` int(11) NULL DEFAULT NULL COMMENT '用户Id',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态（0 失效 1 生效）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '测评评论点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_evaluation_details
-- ----------------------------
DROP TABLE IF EXISTS `home_evaluation_details`;
CREATE TABLE `home_evaluation_details`  (
  `id` int(11) NOT NULL COMMENT '测评表Id',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '测评详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_evaluation_forward
-- ----------------------------
DROP TABLE IF EXISTS `home_evaluation_forward`;
CREATE TABLE `home_evaluation_forward`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `home_evaluation_id` int(11) NULL DEFAULT NULL COMMENT '测评表Id',
  `home_user_id` int(11) NULL DEFAULT NULL COMMENT '用户Id',
  `forward_count` int(11) NULL DEFAULT NULL COMMENT '转发次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '测评转发表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_evaluation_like
-- ----------------------------
DROP TABLE IF EXISTS `home_evaluation_like`;
CREATE TABLE `home_evaluation_like`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `home_evaluation_id` int(11) NULL DEFAULT NULL COMMENT '测评表Id',
  `home_user_id` int(11) NULL DEFAULT NULL COMMENT '用户Id',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态（0 失效 1 生效）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '测评点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_firm
-- ----------------------------
DROP TABLE IF EXISTS `home_firm`;
CREATE TABLE `home_firm`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业地址',
  `license` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业执照',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '企业表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_login
-- ----------------------------
DROP TABLE IF EXISTS `home_login`;
CREATE TABLE `home_login`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `home_user_id` int(11) NULL DEFAULT NULL COMMENT '用户Id',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Ip',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户登录记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_opening_notice
-- ----------------------------
DROP TABLE IF EXISTS `home_opening_notice`;
CREATE TABLE `home_opening_notice`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态（0 禁用 1 启用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '开盘预告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_report
-- ----------------------------
DROP TABLE IF EXISTS `home_report`;
CREATE TABLE `home_report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `real_estate` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '楼盘',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `brand` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `price` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '价格',
  `project_advantage` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目优势',
  `project_disadvantage` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目劣势',
  `suitability` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '匹配度',
  `related_suggestion` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关推荐',
  `discount` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠',
  `copyright` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版权',
  `disclaimer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '免责声明',
  `home_user_id` int(11) NULL DEFAULT NULL COMMENT '用户Id',
  `home_consultant_id` int(11) NULL DEFAULT NULL COMMENT '顾问Id',
  `home_demand_id` int(11) NULL DEFAULT NULL COMMENT '购房报告Id',
  `image_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购房报告图片地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '购房报告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_user
-- ----------------------------
DROP TABLE IF EXISTS `home_user`;
CREATE TABLE `home_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `is_delete` int(1) NOT NULL DEFAULT 1 COMMENT '是否删除 0 是 1 否',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `pass_word` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信oppenId',
  `union_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信unionId',
  `session_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信sessionKey',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` int(1) NULL DEFAULT NULL COMMENT '性别（0：未知，1：男，2：女）',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态（0 锁定 1 正常）',
  `role` int(1) NULL DEFAULT NULL COMMENT '角色（0 用户 1 AI顾问 2 置业顾问）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE,
  UNIQUE INDEX `open_id`(`open_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spring bean名称',
  `params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '任务状态  0：正常  1：暂停',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, 'testTask', 'quartz', '0/10 * * * * ?', 0, '参数测试', '2020-06-08 15:05:20');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `job_id` bigint(20) NOT NULL COMMENT '任务id',
  `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spring bean名称',
  `params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `status` tinyint(4) NOT NULL COMMENT '任务状态    0：成功    1：失败',
  `error` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失败信息',
  `times` int(11) NOT NULL COMMENT '耗时(单位：毫秒)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `job_id`(`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha`  (
  `uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uuid',
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '验证码',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `num` int(1) NULL DEFAULT NULL COMMENT '验证次数',
  `type` int(1) NULL DEFAULT NULL COMMENT '验证类型（0 通用 1 注册 2 登录 3 找回密码）',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统验证码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `is_delete` int(1) NULL DEFAULT NULL COMMENT '是否删除 0 是 1 否',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `param_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'value',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态   0：禁用  1：启用',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `param_key`(`param_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 1, 1, '2020-06-13 22:49:58', '2020-06-13 22:50:03', 'cloud_storage_config_key', '{\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"aliyunDomain\":\"\",\"aliyunEndPoint\":\"\",\"aliyunPrefix\":\"\",\"qcloudBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuBucketName\":\"ios-app\",\"qiniuDomain\":\"http://7xqbwh.dl1.z0.glb.clouddn.com\",\"qiniuPrefix\":\"upload\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"type\":1}', 1, '云存储配置信息');
INSERT INTO `sys_config` VALUES (2, 1, 1, '2020-06-13 15:28:44', '2020-06-13 15:28:48', 'ali_yun_sms_config', '{\"accessKey\":\"LTAI4GHN9a3Rx9PUtKwL2Dba\",\"accessSecret\":\"BclWTtASfyuLyPhmy7m0XmG7GtHdz8\",\"sign\":\"AI家楼盘点评\",\"status\":1,\"templateCode\":\"SMS_196225006\",\"url\":\"https://dysmsapi.aliyuncs.com\"}', 1, '阿里云短信配置');
INSERT INTO `sys_config` VALUES (3, 1, 1, '2020-06-13 15:28:44', '2020-07-18 23:26:17', 'king_kong_config', '[{\"evaluationIds\":[136,135,134,132,75,71,16,69,20,4],\"imagesUrl\":\"/file/jpg/2020/6/15931771890371868502029.jpg\",\"name\":\"正在登记\",\"status\":1,\"type\":\"REGISTERING\"},{\"evaluationIds\":[1,2,136,135,134,132,75,71,16,69,20,4],\"imagesUrl\":\"/file/jpg/2020/6/15931772155602023997728.jpg\",\"name\":\"开盘预售\",\"status\":1,\"type\":\"PRE_SALE\"},{\"evaluationIds\":[136,135,134,132,75,71,16,69,20,4],\"imagesUrl\":\"/file/jpg/2020/6/15931772428732035659474.jpg\",\"name\":\"24h快讯\",\"status\":1,\"type\":\"24H_NEWS\"}]', 1, '金刚区配置');
INSERT INTO `sys_config` VALUES (4, 1, 1, '2020-06-13 15:28:44', '2020-07-18 22:33:49', 'newcomer_guide_config', '[{\"explanation\":\"这里可以输入查找您意向的楼盘评测\",\"status\":1,\"type\":\"SEARCH\"},{\"explanation\":\"这里可以查看相关的资讯哦\",\"status\":1,\"type\":\"INFORMATION\"},{\"explanation\":\"这里可以筛选楼盘测评哦，查看更多可以有更多测评哦\",\"status\":1,\"type\":\"FILTER\"},{\"explanation\":\"点击测评可以查看详细的测评报告哦 \",\"status\":1,\"type\":\"EVALUATION\"},{\"explanation\":\"asdas\",\"status\":1,\"type\":\"NAVIGATION\"}]', 1, '新人指引配置');
INSERT INTO `sys_config` VALUES (5, 1, 1, '2020-06-13 15:28:44', '2020-06-13 15:28:48', 'switch_config', '[{\"key\":\"sms\",\"name\":\"短信控制\",\"value\":true},{\"key\":\"wx_config\",\"name\":\"微信登录开关\",\"value\":true}]', 1, '开关配置');
INSERT INTO `sys_config` VALUES (6, 1, 1, '2020-06-13 15:28:44', '2020-06-13 15:28:48', 'wx_config', '{\"appId\":\"wx6e3a7cd884741e58\",\"appSecret\":\"a39019ce949644e2a083f8fe81de5e4b\"}', 1, '微信配置');
INSERT INTO `sys_config` VALUES (7, 1, 1, '2020-06-13 15:28:44', '2020-06-13 15:28:48', 'release_config', '{\"topDescription\":\"我是AI君，请问您需要什么样的房子？\",\"inDescription\":\"目前已有$count$人提交了购房需求\",\"baseCount\":15000}', 1, '购房需求配置');
INSERT INTO `sys_config` VALUES (8, 1, 1, '2020-06-13 15:28:44', '2020-06-13 15:28:48', 'website_config', '{\"address\":\"西安市莲湖区太和大厦\",\"appLogo\":\"/file/jpg/2020/6/1592916044846721043474.jpg\",\"appMainLogo\":\"/file/jpg/2020/6/1592916044846721043474.jpg\",\"appName\":\"AI家\",\"consumerLink\":\"https://www.baidu.com\",\"consumerPhone\":\"18300000001\",\"copyright\":\"CopyRight © 2020 rong6666\",\"officeHours\":\"08:00-21:00\",\"record\":\"京ICP备11026754号-13\",\"startPage\":\"/file/png/2020/6/15929163013571748431838.png\",\"thePublicQrCode\":\"/file/png/2020/6/15929164016661665481493.png\"}', 1, '网站配置');
INSERT INTO `sys_config` VALUES (9, 1, 1, '2020-06-13 15:28:44', '2020-07-18 17:38:57', 'quick_reply_config', '[{\"content\":\"亲！您好\",\"id\":\"5efc9a0afc911964feebe582\",\"status\":1,\"type\":3},{\"content\":\"我们是专业的\",\"id\":\"5efdf3a760b21e5f4bcd2057\",\"status\":1,\"type\":3},{\"content\":\"您想的怎么样\",\"id\":\"5efdf3b660b21e5f4bcd2058\",\"status\":1,\"type\":3},{\"content\":\"您有什么需求\",\"id\":\"5efdf3c260b21e5f4bcd2059\",\"status\":1,\"type\":2},{\"content\":\"您需要多大面积\",\"id\":\"5efdf3d560b21e5f4bcd205a\",\"status\":1,\"type\":3},{\"content\":\"我们提供多样化楼盘\",\"id\":\"5efdf3ef60b21e5f4bcd205b\",\"status\":1,\"type\":2},{\"content\":\"今天天气真好\",\"id\":\"5efdfe0960b21e5f4bcd205c\",\"status\":1,\"type\":1},{\"content\":\"你有多少存款\",\"id\":\"5efdfe1960b21e5f4bcd205d\",\"status\":1,\"type\":1},{\"content\":\"你家在哪\",\"id\":\"5efdfe2460b21e5f4bcd205e\",\"status\":1,\"type\":0},{\"content\":\"你多大了?\",\"id\":\"5efdfe2d60b21e5f4bcd205f\",\"status\":1,\"type\":0},{\"content\":\"你好\",\"id\":\"5f0b1f5560b24662c763d0b2\",\"status\":1,\"type\":0}]', 1, '快速回复配置');
INSERT INTO `sys_config` VALUES (10, 1, 1, '2020-06-13 15:28:44', '2020-07-22 22:49:33', 'chat_card_config', '[{\"auth\":0,\"code\":\"image\",\"imagesUrl\":\"/file/png/2020/7/15954292817281608899982.png\",\"name\":\"照片\",\"sort\":1,\"status\":1},{\"auth\":0,\"code\":\"photograph\",\"imagesUrl\":\"/file/png/2020/7/15954292983191960315481.png\",\"name\":\"拍照\",\"sort\":2,\"status\":1},{\"auth\":2,\"code\":\"businessCard\",\"imagesUrl\":\"/file/png/2020/7/1595429336721344537339.png\",\"name\":\"发送顾问名片\",\"sort\":3,\"status\":1},{\"auth\":4,\"code\":\"customerReviews\",\"imagesUrl\":\"/file/png/2020/7/15954293481131860737832.png\",\"name\":\"发送客户评价\",\"sort\":4,\"status\":1},{\"auth\":0,\"code\":\"evaluation\",\"imagesUrl\":\"/file/png/2020/7/15954293611031368896132.png\",\"name\":\"发送楼盘测评\",\"sort\":5,\"status\":1},{\"auth\":0,\"code\":\"quickReply\",\"imagesUrl\":\"/file/png/2020/7/15954293712752063241427.png\",\"name\":\"快速回复\",\"sort\":6,\"status\":1}]', 1, '聊天卡片配置');

-- ----------------------------
-- Table structure for sys_datadict
-- ----------------------------
DROP TABLE IF EXISTS `sys_datadict`;
CREATE TABLE `sys_datadict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `is_delete` int(1) NOT NULL DEFAULT 1 COMMENT '是否删除 0 是 1 否',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典值',
  `n1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展1',
  `n2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展2',
  `n3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展3',
  `n4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展4',
  `n5` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展5（JSON）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `type`(`type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_datadict
-- ----------------------------
INSERT INTO `sys_datadict` VALUES (1, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT', '区域选择', NULL, NULL, NULL, NULL, '1', NULL);
INSERT INTO `sys_datadict` VALUES (2, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'PRICE_SELECT', '价格选择', NULL, NULL, NULL, NULL, '1', NULL);
INSERT INTO `sys_datadict` VALUES (3, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'AREA_SELECT', '面积选择', NULL, NULL, NULL, NULL, '1', NULL);
INSERT INTO `sys_datadict` VALUES (4, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'UNIT_TYPE_SELECT', '户型选择', NULL, NULL, NULL, NULL, '1', NULL);
INSERT INTO `sys_datadict` VALUES (5, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'TYPE_SELECT', '类型选择', NULL, NULL, NULL, NULL, '1', NULL);
INSERT INTO `sys_datadict` VALUES (6, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'OTHER_SELECT', '其他选择', NULL, NULL, NULL, NULL, '1', NULL);
INSERT INTO `sys_datadict` VALUES (7, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_1', '城东', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (8, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_2', '城南', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (9, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_3', '城西', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (10, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_4', '城北', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (11, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_5', '长安', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (12, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_6', '高新', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (13, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_7', '曲江', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (14, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_8', '浐灞', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (15, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_9', '经开', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (16, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_10', '沣东新城', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (17, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_11', '沣西新城', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (18, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_12', '秦汉新城', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (19, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_13', '泾河新城', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (20, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_14', '空港新城', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (21, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_15', '能源金贸区', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (22, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_16', '航天基地', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (23, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_17', '国际港务区', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (24, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'REGION_SELECT_18', '临潼', NULL, 'REGION_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (25, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'PRICE_SELECT_1', '9000元/平以下', NULL, 'PRICE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (26, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'PRICE_SELECT_2', '9000-12000元/平', NULL, 'PRICE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (27, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'PRICE_SELECT_3', '12000-15000元/平', NULL, 'PRICE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (28, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'PRICE_SELECT_4', '15000-18000元/平', NULL, 'PRICE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (29, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'PRICE_SELECT_5', '18000-20000元/平', NULL, 'PRICE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (30, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'AREA_SELECT_1', '50㎡以下', NULL, 'AREA_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (31, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'AREA_SELECT_2', '50-70㎡', NULL, 'AREA_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (32, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'AREA_SELECT_3', '70-90㎡', NULL, 'AREA_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (33, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'AREA_SELECT_4', '90-110㎡', NULL, 'AREA_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (34, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'AREA_SELECT_6', '110-130㎡', NULL, 'AREA_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (35, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'AREA_SELECT_7', '130-150㎡', NULL, 'AREA_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (36, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'AREA_SELECT_8', '150-200㎡', NULL, 'AREA_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (37, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'AREA_SELECT_9', '200㎡以上', NULL, 'AREA_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (38, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'UNIT_TYPE_SELECT_1', '一室', NULL, 'UNIT_TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (39, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'UNIT_TYPE_SELECT_2', '两室', NULL, 'UNIT_TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (40, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'UNIT_TYPE_SELECT_3', '三室', NULL, 'UNIT_TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (41, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'UNIT_TYPE_SELECT_4', '四室', NULL, 'UNIT_TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (42, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'UNIT_TYPE_SELECT_5', '五室以上', NULL, 'UNIT_TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (43, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'TYPE_SELECT_1', '高层', NULL, 'TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (44, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'TYPE_SELECT_2', '小高层', NULL, 'TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (45, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'TYPE_SELECT_3', '超高层', NULL, 'TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (46, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'TYPE_SELECT_4', '洋房', NULL, 'TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (47, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'TYPE_SELECT_5', '别墅', NULL, 'TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (48, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'TYPE_SELECT_6', '公寓', NULL, 'TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (49, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'TYPE_SELECT_7', 'LOFT', NULL, 'TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (50, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'TYPE_SELECT_8', '跃层', NULL, 'TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (51, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'TYPE_SELECT_9', '办公', NULL, 'TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (52, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'TYPE_SELECT_10', '商铺', NULL, 'TYPE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (53, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'OTHER_SELECT_1', '地铁盘', NULL, 'OTHER_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (54, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'OTHER_SELECT_2', '临学校', NULL, 'OTHER_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (55, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'OTHER_SELECT_3', '临公园', NULL, 'OTHER_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (56, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'OTHER_SELECT_4', '临河', NULL, 'OTHER_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (57, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'OTHER_SELECT_5', '临湖', NULL, 'OTHER_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (58, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'OTHER_SELECT_6', '临大型商业', NULL, 'OTHER_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (59, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'OTHER_SELECT_7', '品牌', NULL, 'OTHER_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (60, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'LABEL_SIZE', '标签数量', '5', NULL, NULL, NULL, '3', NULL);
INSERT INTO `sys_datadict` VALUES (61, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'STAR_SIZE', '打星数量', '5', NULL, NULL, NULL, '3', NULL);
INSERT INTO `sys_datadict` VALUES (62, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'DEMAND_SIZE', '购房需求数量', '12000', NULL, NULL, NULL, '3', NULL);
INSERT INTO `sys_datadict` VALUES (66, 1, 1, '2020-07-18 19:32:59', '2020-07-18 19:32:59', 'COLLECT_COUNT', '基准收藏数', '100', '', '', '', '3', NULL);
INSERT INTO `sys_datadict` VALUES (69, 1, 1, '2020-07-21 21:41:21', '2020-07-21 21:41:25', 'CONTACT_CONSULTANT_ICON', '联系顾问图标', '/file/png/2020/7/15954293712752063241427.png', NULL, NULL, NULL, '3', NULL);
INSERT INTO `sys_datadict` VALUES (70, 1, 1, '2020-07-21 21:41:21', '2020-07-21 21:41:21', 'OPEN_SCREEN_URL', '开屏图片', '/file/png/2020/7/15954177394661173746541.png', NULL, NULL, NULL, '3', NULL);
INSERT INTO `sys_datadict` VALUES (80, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'PRICE_SELECT_6', '20000-25000元/平', NULL, 'PRICE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (81, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'PRICE_SELECT_7', '25000-30000元/平', NULL, 'PRICE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (82, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:41', 'PRICE_SELECT_8', '30000元/平以上', NULL, 'PRICE_SELECT', NULL, NULL, '2', NULL);
INSERT INTO `sys_datadict` VALUES (83, 1, 1, '2020-06-13 23:21:37', '2020-06-13 23:21:37', 'AUTO_RESPONSE', '自动回复', '我是AI君$name$，很高兴为您服务', NULL, NULL, NULL, '3', NULL);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 127 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', NULL, NULL, 0, 'system', 0);
INSERT INTO `sys_menu` VALUES (2, 1, '管理员列表', 'sys/user', NULL, 1, 'admin', 1);
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', 'sys/role', NULL, 1, 'role', 2);
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', 'sys/menu', NULL, 1, 'menu', 3);
INSERT INTO `sys_menu` VALUES (5, 1, 'SQL监控', 'https://www.029aijia.cn/home-service/druid/sql.html', NULL, 1, 'sql', 4);
INSERT INTO `sys_menu` VALUES (6, 1, '定时任务', 'job/job', NULL, 1, 'job', 5);
INSERT INTO `sys_menu` VALUES (7, 6, '查看', NULL, 'sys:job:list,sys:job:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (8, 6, '新增', NULL, 'sys:job:save', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (9, 6, '修改', NULL, 'sys:job:update', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (10, 6, '删除', NULL, 'sys:job:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (11, 6, '暂停', NULL, 'sys:job:pause', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (12, 6, '恢复', NULL, 'sys:job:resume', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (13, 6, '立即执行', NULL, 'sys:job:run', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (14, 6, '日志列表', NULL, 'sys:job:log', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 2, '新增', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (27, 1, '参数管理', 'sys/config', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', 1, 'config', 6);
INSERT INTO `sys_menu` VALUES (29, 1, '系统日志', 'sys/log', 'sys:log:list', 1, 'log', 7);
INSERT INTO `sys_menu` VALUES (30, 1, '文件上传', 'oss/oss', 'sys:oss:all', 1, 'oss', 8);
INSERT INTO `sys_menu` VALUES (31, 1, '单页面管理', 'sys/single-page', 'sys:single-page:all', 1, 'editor', 9);
INSERT INTO `sys_menu` VALUES (32, 1, '数据字典', 'sys/datadict', 'sys:datadict:all', 1, 'device', 10);
INSERT INTO `sys_menu` VALUES (100, 0, '用户管理', '', '', 0, 'admin', 0);
INSERT INTO `sys_menu` VALUES (101, 0, '测评管理', '', '', 0, 'data-history', 0);
INSERT INTO `sys_menu` VALUES (102, 0, '消息管理', '', '', 0, 'pinglun', 0);
INSERT INTO `sys_menu` VALUES (103, 0, '顾问管理', '', '', 0, 'role', 0);
INSERT INTO `sys_menu` VALUES (104, 0, '网站管理', '', '', 0, 'oss', 0);
INSERT INTO `sys_menu` VALUES (105, 0, '购房需求管理', '', '', 0, 'log', 0);
INSERT INTO `sys_menu` VALUES (106, 100, '用户管理', 'user/user', '', 1, 'admin', 0);
INSERT INTO `sys_menu` VALUES (107, 100, 'AI君用户', 'user/ai', '', 1, 'admin', 1);
INSERT INTO `sys_menu` VALUES (108, 100, '置业用户', 'user/home', '', 1, 'admin', 3);
INSERT INTO `sys_menu` VALUES (109, 100, '企业管理', 'user/firm', '', 1, 'role', 4);
INSERT INTO `sys_menu` VALUES (110, 101, '测评管理', 'evaluation/evaluation', '', 1, 'log', 0);
INSERT INTO `sys_menu` VALUES (111, 101, '快讯管理', 'evaluation/news', '', 1, 'data-history', 0);
INSERT INTO `sys_menu` VALUES (112, 101, '金刚区管理', 'evaluation/kingkong', '', 1, 'ensure', 0);
INSERT INTO `sys_menu` VALUES (113, 101, '开盘公告管理', 'evaluation/opening-notice', '', 1, 'config', 4);
INSERT INTO `sys_menu` VALUES (114, 102, '好友列表', 'message/buddy', '', 1, 'person', 0);
INSERT INTO `sys_menu` VALUES (115, 102, '快速回复', 'message/quick_reply', '', 1, 'plain', 0);
INSERT INTO `sys_menu` VALUES (116, 102, '聊天卡片', 'message/chat_card', '', 1, 'join', 0);
INSERT INTO `sys_menu` VALUES (117, 103, 'AI君管理', 'consultant/ai-consultant', '', 1, 'role', 0);
INSERT INTO `sys_menu` VALUES (118, 103, '置业顾问管理', 'consultant/home-consultant', '', 1, 'role', 0);
INSERT INTO `sys_menu` VALUES (119, 104, '短信配置', 'website/aliyun', '', 1, 'duanxin', 0);
INSERT INTO `sys_menu` VALUES (120, 104, '微信配置', 'website/wechat', '', 1, 'chat', 0);
INSERT INTO `sys_menu` VALUES (121, 104, '新人指引', 'website/newcomer_guide', '', 1, 'daohang', 0);
INSERT INTO `sys_menu` VALUES (122, 104, '网站配置', 'website/website', '', 1, 'status', 0);
INSERT INTO `sys_menu` VALUES (123, 104, '开关配置', 'website/switch_config', '', 1, 'jiesuo', 0);
INSERT INTO `sys_menu` VALUES (124, 104, '轮播图管理', 'website/carousel', '', 1, 'zonghe', 0);
INSERT INTO `sys_menu` VALUES (125, 105, '购房需求管理', 'demand/demand', '', 1, 'log', 0);
INSERT INTO `sys_menu` VALUES (126, 105, '购房报告', 'demand/report', '', 1, 'data-history', 0);

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', '系统最高权限', 1, '2020-07-06 22:07:07');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 1);
INSERT INTO `sys_role_menu` VALUES (2, 1, 2);
INSERT INTO `sys_role_menu` VALUES (3, 1, 3);
INSERT INTO `sys_role_menu` VALUES (4, 1, 4);
INSERT INTO `sys_role_menu` VALUES (5, 1, 5);
INSERT INTO `sys_role_menu` VALUES (6, 1, 6);
INSERT INTO `sys_role_menu` VALUES (7, 1, 7);
INSERT INTO `sys_role_menu` VALUES (8, 1, 8);
INSERT INTO `sys_role_menu` VALUES (9, 1, 9);
INSERT INTO `sys_role_menu` VALUES (10, 1, 10);
INSERT INTO `sys_role_menu` VALUES (11, 1, 11);
INSERT INTO `sys_role_menu` VALUES (12, 1, 12);
INSERT INTO `sys_role_menu` VALUES (13, 1, 13);
INSERT INTO `sys_role_menu` VALUES (14, 1, 14);
INSERT INTO `sys_role_menu` VALUES (15, 1, 15);
INSERT INTO `sys_role_menu` VALUES (16, 1, 16);
INSERT INTO `sys_role_menu` VALUES (17, 1, 17);
INSERT INTO `sys_role_menu` VALUES (18, 1, 18);
INSERT INTO `sys_role_menu` VALUES (19, 1, 19);
INSERT INTO `sys_role_menu` VALUES (20, 1, 20);
INSERT INTO `sys_role_menu` VALUES (21, 1, 21);
INSERT INTO `sys_role_menu` VALUES (22, 1, 22);
INSERT INTO `sys_role_menu` VALUES (23, 1, 23);
INSERT INTO `sys_role_menu` VALUES (24, 1, 24);
INSERT INTO `sys_role_menu` VALUES (25, 1, 25);
INSERT INTO `sys_role_menu` VALUES (26, 1, 26);
INSERT INTO `sys_role_menu` VALUES (27, 1, 27);
INSERT INTO `sys_role_menu` VALUES (28, 1, 29);
INSERT INTO `sys_role_menu` VALUES (29, 1, 30);
INSERT INTO `sys_role_menu` VALUES (30, 1, 31);
INSERT INTO `sys_role_menu` VALUES (31, 1, 32);
INSERT INTO `sys_role_menu` VALUES (32, 1, 100);
INSERT INTO `sys_role_menu` VALUES (33, 1, 101);
INSERT INTO `sys_role_menu` VALUES (34, 1, 102);
INSERT INTO `sys_role_menu` VALUES (35, 1, 103);
INSERT INTO `sys_role_menu` VALUES (36, 1, 104);
INSERT INTO `sys_role_menu` VALUES (37, 1, 105);
INSERT INTO `sys_role_menu` VALUES (38, 1, 106);
INSERT INTO `sys_role_menu` VALUES (39, 1, 107);
INSERT INTO `sys_role_menu` VALUES (40, 1, 108);
INSERT INTO `sys_role_menu` VALUES (41, 1, 109);
INSERT INTO `sys_role_menu` VALUES (42, 1, 110);
INSERT INTO `sys_role_menu` VALUES (43, 1, 111);
INSERT INTO `sys_role_menu` VALUES (44, 1, 112);
INSERT INTO `sys_role_menu` VALUES (45, 1, 113);
INSERT INTO `sys_role_menu` VALUES (46, 1, 114);
INSERT INTO `sys_role_menu` VALUES (47, 1, 115);
INSERT INTO `sys_role_menu` VALUES (48, 1, 116);
INSERT INTO `sys_role_menu` VALUES (49, 1, 117);
INSERT INTO `sys_role_menu` VALUES (50, 1, 118);
INSERT INTO `sys_role_menu` VALUES (51, 1, 119);
INSERT INTO `sys_role_menu` VALUES (52, 1, 120);
INSERT INTO `sys_role_menu` VALUES (53, 1, 121);
INSERT INTO `sys_role_menu` VALUES (54, 1, 122);
INSERT INTO `sys_role_menu` VALUES (55, 1, 123);
INSERT INTO `sys_role_menu` VALUES (56, 1, 124);
INSERT INTO `sys_role_menu` VALUES (57, 1, 125);
INSERT INTO `sys_role_menu` VALUES (58, 1, 126);

-- ----------------------------
-- Table structure for sys_single_page
-- ----------------------------
DROP TABLE IF EXISTS `sys_single_page`;
CREATE TABLE `sys_single_page`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `type` int(1) NULL DEFAULT NULL COMMENT '类型',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `cover_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态（0 禁用 1 启用）',
  `is_delete` int(1) NULL DEFAULT NULL COMMENT '是否删除 （0 是 1 否）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code`) USING BTREE COMMENT '编码'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统单页面表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `app_user_id` int(11) NULL DEFAULT NULL COMMENT 'App端用户Id',
  `service_number` int(11) NULL DEFAULT NULL COMMENT '服务数量',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d', 'YzcmCZNvbXocrsz9dm8e', '1048516133@qq.com', '18309295990', '管理员', 1, 1, '2016-11-11 11:11:11', NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与角色对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `token`(`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户Token' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
