/*
Navicat MySQL Data Transfer

Source Server         : AA
Source Server Version : 50622
Source Host           : localhost:3306
Source Database       : contacts

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2015-06-26 21:18:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for login
-- ----------------------------
DROP TABLE IF EXISTS `login`;
CREATE TABLE `login` (
  `user` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Records of login
-- ----------------------------
INSERT INTO `login` VALUES ('15555215554', '15555215554');

-- ----------------------------
-- Table structure for u15555215554
-- ----------------------------
DROP TABLE IF EXISTS `u15555215554`;
CREATE TABLE `u15555215554` (
  `name` varchar(255) NOT NULL,
  `tel_1` varchar(255) NOT NULL,
  `tel_2` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `del` int(1) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Records of u15555215554
-- ----------------------------
