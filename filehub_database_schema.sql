# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.13)
# Database: cs157a
# Generation Time: 2017-12-14 07:53:18 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table file_data
# ------------------------------------------------------------

CREATE TABLE `file_data` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `folder_path` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `file_status` varchar(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `notes` text,
  `notes_by` int(11) DEFAULT NULL,
  `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `uploaded_by` int(11) DEFAULT NULL,
  `modified_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table file_upload_log
# ------------------------------------------------------------

CREATE TABLE `file_upload_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `action_by` int(11) DEFAULT NULL,
  `action_info` text,
  `action_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table file_url
# ------------------------------------------------------------

CREATE TABLE `file_url` (
  `url_code` varchar(128) NOT NULL DEFAULT '',
  `file_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`url_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table group_invites
# ------------------------------------------------------------

CREATE TABLE `group_invites` (
  `id` varchar(128) NOT NULL DEFAULT '',
  `invite_status` varchar(128) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `invited_by_id` int(11) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  `invite_access_level` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table group_members
# ------------------------------------------------------------

CREATE TABLE `group_members` (
  `user_id` int(11) unsigned NOT NULL,
  `group_id` int(11) unsigned NOT NULL,
  `user_permission` int(11) NOT NULL,
  KEY `fk_user_id` (`user_id`),
  KEY `fk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `group_members` WRITE;
/*!40000 ALTER TABLE `group_members` DISABLE KEYS */;

INSERT INTO `group_members` (`user_id`, `group_id`, `user_permission`)
VALUES
	(2,2,3),
	(2,3,2),
	(2,4,3),
	(2,5,1),
	(2,1,4),
	(3,1,2),
	(1,2,3),
	(1,1,3);

/*!40000 ALTER TABLE `group_members` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table groups
# ------------------------------------------------------------

CREATE TABLE `groups` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group_name` varchar(128) DEFAULT NULL,
  `group_owner` int(11) unsigned NOT NULL,
  `group_password` varchar(255) DEFAULT NULL,
  `group_status` varchar(20) DEFAULT NULL,
  `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_group_owner` (`group_owner`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;

INSERT INTO `groups` (`id`, `group_name`, `group_owner`, `group_password`, `group_status`, `created_on`)
VALUES
	(1,'Jennifer\'s Group',2,'1234','Active','2017-11-29 17:22:32'),
	(2,'The Fun Group',1,'1234','Active','2017-11-29 17:22:32'),
	(3,'CS157A Group',3,'1234','Active','2017-11-29 17:22:32'),
	(4,'The Cool People Group',3,'1234','Active','2017-11-29 17:22:32'),
	(5,'Recipes and Cooking Group',1,'1234','Active','2017-11-29 17:22:32');

/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user
# ------------------------------------------------------------

CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(128) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `login_status` varchar(40) DEFAULT NULL,
  `first_name` varchar(128) DEFAULT NULL,
  `last_name` varchar(128) DEFAULT NULL,
  `cellphone` varchar(30) DEFAULT NULL,
  `role` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `username`, `password`, `login_status`, `first_name`, `last_name`, `cellphone`, `role`)
VALUES
	(1,'bakatrinh@gmail.com','1234','Active','Trinh','Nguyen','2093823098',5),
	(2,'jennifer@gmail.com','1234','Active','Jennifer','Nguyen','2320938209',5),
	(3,'anil@gmail.com','1234','Active','Anil','Gherra','2390390230',5),
	(4,'john@gmail.com','123','Active','John','Nguyen','2123141234',5),
	(12,'test1@yahoo.com','lkjlkj','Active','test1','afsa','3423432',NULL),
	(13,'test3@yahoo.com','fadsfa','Active','anil','test2','fsdaf',NULL),
	(14,'john@gmail.com','123','Active','john ','smith','4234234432',NULL),
	(15,'testuser@gmail.com','123','Active','testuser','doe','4124321233',NULL),
	(16,'mytest@gmail.com','123','Active','mytest','teee','1234556',NULL),
	(17,'sharandeepsi@yahoo.com','12345','Active','sharandeep','singh','515155',NULL),
	(18,'sing2@yahoo.com','12345','Active','sharandeep','singha','123456',NULL),
	(19,'test11@yahoo.com','1234','Active','test5','test5','1234144',NULL),
	(20,'makbook2@yahoo.com','12345','Active','macbook','test','242415215',NULL);

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_issues
# ------------------------------------------------------------

CREATE TABLE `user_issues` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `issues_by` int(11) DEFAULT NULL,
  `issues_message` text,
  `issue_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table user_messages
# ------------------------------------------------------------

CREATE TABLE `user_messages` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `message` text,
  `message_status` varchar(20) DEFAULT NULL,
  `sent_to` int(11) DEFAULT NULL,
  `sent_from` int(11) DEFAULT NULL,
  `sent_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table user_permissions_type
# ------------------------------------------------------------

CREATE TABLE `user_permissions_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `permission_formal` varchar(128) DEFAULT NULL,
  `permission_description` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `user_permissions_type` WRITE;
/*!40000 ALTER TABLE `user_permissions_type` DISABLE KEYS */;

INSERT INTO `user_permissions_type` (`id`, `permission_formal`, `permission_description`)
VALUES
	(1,'Guest','Can only view and download files.\nCannot add folder.\nCannot upload.\nCannot delete.\nCannot remove other users from group.\nCannot invite users to group.\nCannot set users permissions.'),
	(2,'User','Can view and download files.\nCan add folder.\nCan upload.\nCannot delete.\nCannot remove other users from group.\nCannot invite users to group.\nCannot set users permissions.'),
	(3,'Advanced User','Can view and download files.\nCan add folder.\nCan upload.\nCan delete.\nCannot remove other users from group.\nCan invite users to group.\nCannot set users permissions.'),
	(4,'Admin','Can view and download files.\nCan add folder.\nCan upload.\nCan delete.\nCan remove other users from group.\nCan invite users to group.\nCan set users permissions.');

/*!40000 ALTER TABLE `user_permissions_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_report
# ------------------------------------------------------------

CREATE TABLE `user_report` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_reported` int(11) DEFAULT NULL,
  `reported_by` int(11) DEFAULT NULL,
  `report_message` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
