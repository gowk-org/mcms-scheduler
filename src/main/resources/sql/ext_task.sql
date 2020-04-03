/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : localhost:3306
 Source Schema         : mcms5

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 03/04/2020 14:50:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ext_task
-- ----------------------------
DROP TABLE IF EXISTS `ext_task`;
CREATE TABLE `ext_task`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `job_state` int(11) NULL DEFAULT NULL COMMENT '是否启用',
  `job_cron` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'CRON表达式',
  `job_group` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组名称',
  `job_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `del` int(1) NULL DEFAULT 0 COMMENT '删除标记',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ext_task
-- ----------------------------
INSERT INTO `ext_task` VALUES (1, 1, '*/25 * * * * ?', 'g1', 'net.mingsoft.ext.scheduler.job.HelloJob', 0, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
