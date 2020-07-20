/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 5.5.27 : Database - keep_younger
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `sys_request_log` */

DROP TABLE IF EXISTS `sys_request_log`;

CREATE TABLE `sys_request_log` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `ip` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '用户操作所在ip',
  `path` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '请求路径',
  `method` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求方式',
  `params` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '请求参数',
  `body` text COLLATE utf8_bin COMMENT '请求body参数',
  `user` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  `status` int(10) DEFAULT NULL COMMENT '请求状态',
  `date` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求日期',
  `time` bigint(20) DEFAULT NULL COMMENT '请求具体时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin