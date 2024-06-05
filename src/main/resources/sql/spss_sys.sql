/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80011
Source Host           : localhost:3306
Source Database       : spss_sys

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2024-06-03 14:55:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `User_id` int(255) NOT NULL AUTO_INCREMENT,
  `User_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `User_pwd` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`User_id`,`User_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'Tom', '123456');
INSERT INTO `user` VALUES ('2', 'Jerry', '654321');
INSERT INTO `user` VALUES ('3', 'Joe', '012345');
INSERT INTO `user` VALUES ('4', 'John', '121212');
INSERT INTO `user` VALUES ('5', 'Mary', '343434');

-- ----------------------------
-- Table structure for user_workbook
-- ----------------------------
DROP TABLE IF EXISTS `user_workbook`;
CREATE TABLE `user_workbook` (
  `entryid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `workbookid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`entryid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_workbook
-- ----------------------------
INSERT INTO `user_workbook` VALUES ('1', '1', '665a9a9578221d79b4a5a28f');
