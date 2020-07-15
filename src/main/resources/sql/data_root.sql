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
/*Table structure for table `data_root_column` */

DROP TABLE IF EXISTS `data_root_column`;

CREATE TABLE `data_root_column` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` bigint(20) DEFAULT NULL COMMENT '父id',
  `column` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '字段名称',
  `type` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '字段类型',
  `len` int(10) DEFAULT NULL COMMENT '长度限制',
  `src_show` tinyint(1) DEFAULT '1' COMMENT '详情是否显示',
  `src_key` tinyint(1) DEFAULT NULL COMMENT '是否为查询详情主键',
  `list_show` tinyint(1) DEFAULT NULL COMMENT '列表是否显示',
  `list_key` tinyint(1) DEFAULT NULL COMMENT '是否为查询列表字段',
  `page_show` tinyint(1) DEFAULT NULL COMMENT '分页是否显示',
  `page_key` tinyint(1) DEFAULT NULL COMMENT '是否为分页查询字段',
  `order` int(10) DEFAULT NULL COMMENT '1:正排，2:倒排',
  `save` tinyint(1) DEFAULT '1' COMMENT '保存时是否必须',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `data_root_column` */

insert  into `data_root_column`(`id`,`pid`,`column`,`type`,`len`,`src_show`,`src_key`,`list_show`,`list_key`,`page_show`,`page_key`,`order`,`save`) values 
(1,1,'id','BIGINT',20,1,1,1,0,1,0,1,1),
(2,1,'name','STRING',20,1,0,1,1,1,0,0,1),
(3,1,'py','STRING',20,1,0,1,0,1,1,0,1),
(4,1,'src','TEXT',32,1,0,0,0,0,0,0,0),
(5,1,'update','UPDATE',13,1,0,1,0,1,0,0,1),
(6,1,'delete','BOOLEAN',0,1,0,1,0,1,0,0,0),
(7,2,'id','BIGINT',20,1,1,1,0,1,0,1,1),
(8,2,'name','STRING',50,1,0,1,1,1,0,0,1),
(9,2,'auth','STRING',50,1,0,1,1,1,0,0,1),
(10,2,'image','STRING',100,1,0,0,0,0,0,0,1),
(11,2,'s_image','STRING',100,1,0,1,0,1,0,0,1),
(12,2,'intro','TEXT',32,1,0,0,0,0,0,0,0),
(13,2,'guide','STRING',50,1,0,1,0,1,0,0,1),
(14,2,'guide_auth','STRING',8,1,0,1,0,1,0,0,1),
(15,2,'score','STRING',3,1,0,1,0,1,0,0,1),
(16,2,'type','STRING',10,1,0,1,0,1,0,0,1),
(17,2,'type_id','INT',5,1,0,1,0,1,1,0,1),
(18,2,'dynasty','STRING',10,1,0,1,0,1,0,0,1),
(19,2,'dynasty_id','INT',5,1,0,1,0,1,1,0,1),
(20,2,'count','BIGINT',20,1,0,1,0,1,0,1,1),
(21,2,'free','BOOLEAN',1,1,0,1,0,1,1,0,0),
(22,2,'off_sale','BOOLEAN',0,1,0,1,0,1,1,0,0),
(23,2,'update','UPDATE',13,1,0,1,0,1,0,0,1),
(24,2,'delete','BOOLEAN',0,1,0,1,0,1,0,0,0),
(25,3,'id','BIGINT',20,1,1,1,0,0,0,1,1),
(26,3,'order','INT',10,1,0,1,0,0,0,1,1),
(27,3,'name','STRING',50,1,0,1,0,0,0,0,1),
(28,3,'book_id','BIGINT',20,1,0,1,0,0,0,0,1),
(29,3,'content','TEXT',32,1,0,0,0,0,0,0,0),
(30,3,'update','UPDATE',13,1,0,1,0,0,0,0,1),
(31,3,'delete','BOOLEAN',0,1,0,1,0,0,0,0,0),
(32,4,'id','BIGINT',20,1,1,1,0,1,0,1,1),
(33,4,'name','STRING',50,1,0,1,1,1,0,0,1),
(34,4,'auth','STRING',50,1,0,1,1,1,0,0,1),
(35,4,'image','STRING',100,1,0,1,0,1,0,0,1),
(36,4,'type','STRING',20,1,0,1,0,1,0,0,1),
(37,4,'type_id','INT',10,1,0,1,0,1,1,0,1),
(38,4,'intro','TEXT',32,1,0,0,0,0,0,0,0),
(39,4,'update','UPDATE',13,1,0,1,0,1,0,0,1),
(40,4,'delete','BOOLEAN',0,1,0,1,0,1,0,0,0),
(41,5,'id','BIGINT',20,1,1,1,0,0,0,1,1),
(42,5,'pid','BIGINT',20,1,0,1,0,0,0,0,1),
(43,5,'order','INT',10,1,0,1,0,0,0,1,1),
(44,5,'title','STRING',50,1,0,1,0,0,0,0,1),
(45,5,'src','TEXT',32,1,0,0,0,0,0,0,0),
(46,5,'update','UPDATE',13,1,0,1,0,0,0,0,1),
(47,5,'delete','BOOLEAN',0,1,0,1,0,0,0,0,0),
(48,6,'id','BIGINT',20,1,1,1,0,1,0,1,1),
(49,6,'name','STRING',50,1,0,1,1,1,0,0,1),
(50,6,'url','STRING',200,1,0,0,0,0,0,0,1),
(51,6,'type','INT',10,1,0,1,0,1,1,0,1),
(52,6,'kind','STRING',20,1,0,1,0,1,0,0,1),
(53,6,'image','STRING',100,1,0,1,0,1,0,0,1),
(54,6,'tips','STRING',50,1,0,1,0,1,0,0,1),
(55,6,'useful','BOOLEAN',1,1,0,1,0,1,1,0,0),
(56,6,'update','UPDATE',13,1,0,1,0,1,0,0,1),
(57,6,'delete','BOOLEAN',0,1,0,1,0,1,0,0,0),
(58,7,'id','BIGINT',20,1,1,1,0,1,0,1,1),
(59,7,'name','STRING',50,1,0,1,1,1,0,0,1),
(60,7,'image','STRING',200,1,0,1,0,1,0,0,1),
(61,7,'actor','STRING',200,1,0,1,0,1,0,0,0),
(62,7,'type','STRING',20,1,0,1,0,1,0,0,1),
(63,7,'type_id','INT',10,1,0,1,0,1,1,0,1),
(64,7,'time','STRING',20,1,0,1,0,1,0,0,0),
(65,7,'place','STRING',20,1,0,1,0,1,0,0,1),
(66,7,'place_id','INT',10,1,0,1,0,1,1,0,1),
(67,7,'weight','STRING',20,1,0,1,0,1,0,0,1),
(68,7,'intro','TEXT',32,1,0,0,0,0,0,0,0),
(69,7,'m3u8','TEXT',32,1,0,0,0,0,0,0,0),
(70,7,'update','UPDATE',13,1,0,1,0,1,0,0,1),
(71,7,'delete','BOOLEAN',0,1,0,1,0,1,0,0,0),
(72,8,'id','BIGINT',20,1,1,1,0,1,0,1,1),
(73,8,'name','STRING',50,1,0,1,1,1,0,0,1),
(74,8,'prl','INT',10,1,0,0,0,0,0,0,1),
(75,8,'psl','INT',10,1,0,0,0,0,0,0,1),
(76,8,'pid','BIGINT',20,1,0,1,0,1,1,0,1),
(77,8,'sid','BIGINT',20,1,0,1,0,1,1,0,1),
(78,8,'s_image','STRING',100,1,0,1,0,1,0,0,1),
(79,8,'image','STRING',100,1,0,0,0,0,0,0,1),
(80,8,'key','STRING',400,1,0,0,1,0,0,0,0),
(81,8,'update','UPDATE',13,1,0,1,0,1,0,0,1),
(82,8,'delete','BOOLEAN',0,1,0,1,0,1,0,0,0);

/*Table structure for table `data_root_table` */

DROP TABLE IF EXISTS `data_root_table`;

CREATE TABLE `data_root_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '数据名称',
  `table` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '表名称',
  `main` tinyint(1) DEFAULT NULL COMMENT '是否为主表',
  `main_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '若非主表，则填写指向主表id的字段名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `data_root_table` */

insert  into `data_root_table`(`id`,`name`,`table`,`main`,`main_id`) values 
(1,'bjx','tt_bjx',1,''),
(2,'book','tt_book',1,NULL),
(3,'book_chapter','tt_book_chapter',0,'book_id'),
(4,'buddhist','tt_buddhist',1,NULL),
(5,'buddhist_chapter','tt_buddhist_chapter',0,'pid'),
(6,'live','tt_live_m3u8',1,NULL),
(7,'movie','tt_movie_m3u8',1,NULL),
(8,'pic','tt_pic',1,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
