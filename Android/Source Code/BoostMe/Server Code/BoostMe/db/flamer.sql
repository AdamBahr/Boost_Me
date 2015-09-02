-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 15, 2014 at 01:59 PM
-- Server version: 5.5.27
-- PHP Version: 5.4.7

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `flamer`
--

-- --------------------------------------------------------

--
-- Table structure for table `t_admin`
--

CREATE TABLE IF NOT EXISTS `t_admin` (
  `aid` int(5) NOT NULL AUTO_INCREMENT COMMENT 'Admin id',
  `admin_username` varchar(30) NOT NULL COMMENT 'admin username',
  `admin_password` varchar(30) NOT NULL COMMENT 'admin password',
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_answer`
--

CREATE TABLE IF NOT EXISTS `t_answer` (
  `ans_id` int(50) NOT NULL AUTO_INCREMENT,
  `Entity_Id` int(50) NOT NULL,
  `que_id` int(50) NOT NULL,
  `your_ans` int(50) NOT NULL,
  `pref_a` int(50) NOT NULL,
  `pref_b` int(50) NOT NULL,
  `pref_c` int(50) NOT NULL,
  `pref_d` int(50) NOT NULL,
  PRIMARY KEY (`ans_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=40 ;

--
-- Dumping data for table `t_answer`
--

INSERT INTO `t_answer` (`ans_id`, `Entity_Id`, `que_id`, `your_ans`, `pref_a`, `pref_b`, `pref_c`, `pref_d`) VALUES
(30, 8, 1, 1, 1, 2, 3, 4),
(33, 8, 2, 2, 1, 2, 3, 4),
(37, 9, 2, 5, 1, 2, 3, 4),
(38, 9, 1, 3, 1, 2, 3, 4),
(39, 8, 3, 2, 1, 2, 3, 4);

-- --------------------------------------------------------

--
-- Table structure for table `t_authtypes`
--

CREATE TABLE IF NOT EXISTS `t_authtypes` (
  `authType_id` tinyint(3) NOT NULL AUTO_INCREMENT COMMENT 'Authentication type',
  `authName` varchar(150) NOT NULL COMMENT 'Authentication name',
  PRIMARY KEY (`authType_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='Types of authentications for the appl' AUTO_INCREMENT=3 ;

--
-- Dumping data for table `t_authtypes`
--

INSERT INTO `t_authtypes` (`authType_id`, `authName`) VALUES
(1, 'Facebook'),
(2, 'Google+');

-- --------------------------------------------------------

--
-- Table structure for table `t_chatmessages`
--

CREATE TABLE IF NOT EXISTS `t_chatmessages` (
  `mid` int(30) NOT NULL AUTO_INCREMENT COMMENT 'Message id',
  `sender` int(25) NOT NULL COMMENT 'sender entity id',
  `receiver` int(25) NOT NULL COMMENT 'reciever entity id',
  `message` varchar(2000) NOT NULL COMMENT 'actual chat message ',
  `msg_dt` datetime NOT NULL COMMENT 'Date and time of message',
  `user1` varchar(100) NOT NULL,
  `user2` varchar(11) NOT NULL,
  PRIMARY KEY (`mid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Contains chat messages' AUTO_INCREMENT=119 ;

--
-- Dumping data for table `t_chatmessages`
--

INSERT INTO `t_chatmessages` (`mid`, `sender`, `receiver`, `message`, `msg_dt`, `user1`, `user2`) VALUES
(116, 8, 9, 'h', '0000-00-00 00:00:00', '9', ''),
(117, 9, 8, 'h', '0000-00-00 00:00:00', '8', ''),
(118, 8, 8, '121f', '2014-04-09 11:20:07', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `t_city`
--

CREATE TABLE IF NOT EXISTS `t_city` (
  `City_Id` int(8) NOT NULL AUTO_INCREMENT COMMENT 'City Id',
  `State_Id` int(7) DEFAULT NULL COMMENT 'State Id',
  `Country_Id` int(6) NOT NULL COMMENT 'Country Id',
  `City_Name` varchar(100) NOT NULL COMMENT 'City Name',
  PRIMARY KEY (`City_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Cities in all states around all the countries' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_country`
--

CREATE TABLE IF NOT EXISTS `t_country` (
  `Country_Id` int(6) NOT NULL AUTO_INCREMENT COMMENT 'Country Id',
  `Country_Name` varchar(100) NOT NULL COMMENT 'Country name',
  PRIMARY KEY (`Country_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Country table' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_dev_type`
--

CREATE TABLE IF NOT EXISTS `t_dev_type` (
  `dev_id` int(4) NOT NULL AUTO_INCREMENT COMMENT 'Device type id',
  `name` varchar(100) NOT NULL COMMENT 'Name of the device',
  PRIMARY KEY (`dev_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Stores device types which the appl can allow' AUTO_INCREMENT=3 ;

--
-- Dumping data for table `t_dev_type`
--

INSERT INTO `t_dev_type` (`dev_id`, `name`) VALUES
(1, 'Apple'),
(2, 'Android');

-- --------------------------------------------------------

--
-- Table structure for table `t_education`
--

CREATE TABLE IF NOT EXISTS `t_education` (
  `Education_Id` int(25) NOT NULL AUTO_INCREMENT COMMENT 'User Education Id',
  `Entity_Id` int(25) NOT NULL COMMENT 'Entity_Id',
  `Education_City` varchar(50) DEFAULT NULL COMMENT 'Education city',
  `Education_Country` varchar(50) DEFAULT NULL COMMENT 'Education Country',
  `Education_Institute` varchar(250) DEFAULT NULL COMMENT 'Education Institute',
  `Education_Degree` varchar(100) NOT NULL COMMENT 'Education Degree',
  `Education_Start_Date` date NOT NULL COMMENT 'Education start date',
  `Education_End_Date` date NOT NULL COMMENT 'Education end date',
  PRIMARY KEY (`Education_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_entity`
--

CREATE TABLE IF NOT EXISTS `t_entity` (
  `Entity_Id` int(25) NOT NULL AUTO_INCREMENT COMMENT 'Entity Identifier',
  `Status` tinyint(2) NOT NULL DEFAULT '1',
  `Unique_Identifier` varchar(100) NOT NULL COMMENT 'Unique identifier for the entity',
  `Password` varchar(25) DEFAULT NULL COMMENT 'User password. Not encrypted for this phase.',
  `Create_Dt` datetime DEFAULT NULL COMMENT 'Date/time of creation.',
  `Last_Active_Dt_Time` datetime DEFAULT NULL COMMENT 'Date/time of last activity',
  `Device_Type` tinyint(2) NOT NULL COMMENT 'Device Type',
  `Device_Id` varchar(250) NOT NULL COMMENT 'Device Id',
  `authType` tinyint(2) NOT NULL COMMENT 'Authentication type fk from authtypes',
  PRIMARY KEY (`Entity_Id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='Entity identifier' AUTO_INCREMENT=10 ;

--
-- Dumping data for table `t_entity`
--

INSERT INTO `t_entity` (`Entity_Id`, `Status`, `Unique_Identifier`, `Password`, `Create_Dt`, `Last_Active_Dt_Time`, `Device_Type`, `Device_Id`, `authType`) VALUES
(8, 1, '100001404765028', '', '2014-04-03 09:05:56', '2014-04-11 07:01:54', 1, 'D1947262-531C-4574-B57D-1A7BBDAD378B', 1),
(9, 1, '100006859655115', '', '2014-04-03 09:15:15', '2014-04-11 07:02:02', 1, '82008805-E11A-4873-A6FB-96E75AA110A9', 1);

-- --------------------------------------------------------

--
-- Table structure for table `t_entity_details`
--

CREATE TABLE IF NOT EXISTS `t_entity_details` (
  `Entity_Id` int(25) NOT NULL COMMENT 'App User Id',
  `Fb_Id` varchar(150) DEFAULT NULL COMMENT 'User Facebook Id',
  `Google_Id` varchar(150) DEFAULT NULL COMMENT 'User Google Id',
  `First_Name` varchar(100) NOT NULL COMMENT 'User Firstname',
  `Last_Name` varchar(100) DEFAULT NULL COMMENT 'User Lastname',
  `Email` varchar(250) NOT NULL COMMENT 'User email address',
  `Country` varchar(70) DEFAULT NULL COMMENT 'Country from which the user is logging in',
  `City` varchar(70) DEFAULT NULL COMMENT 'City from which the user is logging in',
  `Current_Lat` varchar(30) DEFAULT NULL COMMENT 'Latitude for current logged in location',
  `Current_Long` varchar(30) DEFAULT NULL COMMENT 'Longitude for current logged in location',
  `Profile_Pic_Url` varchar(300) DEFAULT NULL COMMENT 'URL of the profile picture',
  `TagLine` varchar(400) DEFAULT NULL COMMENT 'Tag line for the user profile',
  `Sex` tinyint(1) NOT NULL COMMENT '1 - male, 2 - female',
  `Quickblocks_Id` varchar(70) NOT NULL COMMENT 'Quickblocks_Id',
  `Personal_Desc` varchar(600) DEFAULT NULL COMMENT 'Personal Description of the user',
  `DOB` date DEFAULT NULL COMMENT 'User Date of Birth',
  `Skill_Rating` tinyint(1) DEFAULT NULL COMMENT 'User skill rating',
  PRIMARY KEY (`Entity_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='User details table';

--
-- Dumping data for table `t_entity_details`
--

INSERT INTO `t_entity_details` (`Entity_Id`, `Fb_Id`, `Google_Id`, `First_Name`, `Last_Name`, `Email`, `Country`, `City`, `Current_Lat`, `Current_Long`, `Profile_Pic_Url`, `TagLine`, `Sex`, `Quickblocks_Id`, `Personal_Desc`, `DOB`, `Skill_Rating`) VALUES
(8, '100001404765028', '', 'Jigs', 'Chanchiya', 'chanchiyajignesh@gmail.com', 'Rajkot, Gujarat', 'Rajkot, Gujarat', '37.785800', '-122.406000', 'http://192.168.0.114/PHP_ServerF/phpserver/pics/1003582_657062257683927_622259159_n.jpg', '', 1, '456', '', '1985-11-18', NULL),
(9, '100006859655115', '', 'Vipul', 'Katha', 'abc@gmail.com', '', '', '37.785800', '-122.406000', 'http://192.168.0.114/PHP_ServerF/phpserver/pics/10006203_1434379276800714_2041199481_n.jpg', '', 2, '456', '', '1989-08-24', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `t_images`
--

CREATE TABLE IF NOT EXISTS `t_images` (
  `image_id` int(30) NOT NULL AUTO_INCREMENT COMMENT 'Image id',
  `entity_id` int(25) NOT NULL COMMENT 'Entity Id',
  `image_url` varchar(600) NOT NULL COMMENT 'Image url',
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='Contains user profile images' AUTO_INCREMENT=17 ;

--
-- Dumping data for table `t_images`
--

INSERT INTO `t_images` (`image_id`, `entity_id`, `image_url`) VALUES
(1, 1, 'http://192.168.0.114/PHP_Server/phpserver/pics/1525666_618827248174095_9463236_n.jpg'),
(2, 1, 'http://192.168.0.114/PHP_Server/phpserver/pics/542973_433816770008478_1834625388_n.jpg'),
(3, 1, 'http://192.168.0.114/PHP_Server/phpserver/pics/385424_395280797195409_365666578_n.jpg'),
(4, 1, 'http://192.168.0.114/PHP_Server/phpserver/pics/389705_361095680613921_775994226_n.jpg'),
(5, 2, 'http://192.168.0.114/PHP_ServerF/phpserver/pics/1525666_618827248174095_9463236_n.jpg'),
(6, 2, 'http://192.168.0.114/PHP_ServerF/phpserver/pics/542973_433816770008478_1834625388_n.jpg'),
(7, 2, 'http://192.168.0.114/PHP_ServerF/phpserver/pics/385424_395280797195409_365666578_n.jpg'),
(8, 2, 'http://192.168.0.114/PHP_ServerF/phpserver/pics/389705_361095680613921_775994226_n.jpg'),
(9, 7, 'http://192.168.0.114/PHP_ServerF/phpserver/pics/1525666_618827248174095_9463236_n.jpg'),
(10, 7, 'http://192.168.0.114/PHP_ServerF/phpserver/pics/542973_433816770008478_1834625388_n.jpg'),
(11, 7, 'http://192.168.0.114/PHP_ServerF/phpserver/pics/385424_395280797195409_365666578_n.jpg'),
(12, 7, 'http://192.168.0.114/PHP_ServerF/phpserver/pics/389705_361095680613921_775994226_n.jpg'),
(13, 8, 'http://192.168.0.114/PHP_ServerF/phpserver/pics/1525666_618827248174095_9463236_n.jpg'),
(14, 8, 'http://192.168.0.114/PHP_ServerF/phpserver/pics/542973_433816770008478_1834625388_n.jpg'),
(15, 8, 'http://192.168.0.114/PHP_ServerF/phpserver/pics/385424_395280797195409_365666578_n.jpg'),
(16, 8, 'http://192.168.0.114/PHP_ServerF/phpserver/pics/389705_361095680613921_775994226_n.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `t_likes`
--

CREATE TABLE IF NOT EXISTS `t_likes` (
  `Like_Id` int(25) NOT NULL AUTO_INCREMENT COMMENT 'Like Id for the user',
  `Entity1_Id` int(25) NOT NULL COMMENT 'User id who liked the other user',
  `Entity2_Id` int(25) NOT NULL COMMENT 'User id who is getting liked',
  `Like_Flag` tinyint(1) NOT NULL COMMENT '1 -> Liked, 2-> DisLiked, 3-> Liked by both, 4->blocked',
  `Like_DateTime` datetime NOT NULL COMMENT 'Date and time of the like',
  `Update_Dt` datetime NOT NULL COMMENT 'updated date time',
  `Dislike_Count` int(5) NOT NULL COMMENT 'Count of dislikes',
  PRIMARY KEY (`Like_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='List of mutual  likes' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_matches`
--

CREATE TABLE IF NOT EXISTS `t_matches` (
  `Match_Id` int(25) NOT NULL AUTO_INCREMENT COMMENT 'March Id',
  `Matched_entity1_Id` int(25) NOT NULL COMMENT 'First User who is participating in the match',
  `Matched_entity2_Id` int(25) NOT NULL COMMENT 'Second User who is participating in the match',
  `Mathc_requestor_Id` int(25) NOT NULL COMMENT 'User who is Match maker ',
  `Match_Type` tinyint(1) NOT NULL COMMENT '0 ->  normal match, 1 -> booty call match, 2 -> blind date match, 4 -> matchmaking',
  `Match_Date_Time` datetime NOT NULL COMMENT 'March date and time',
  `Status` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'default = 0 = unblocked , 1 = unblocked and invite sent , 2 = invite declined 3 = invite accepted 4 = blocked',
  `Blind_Date_Location` varchar(400) DEFAULT NULL COMMENT 'This is the location for the blind date',
  PRIMARY KEY (`Match_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='List of Matches Per User' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_notifications`
--

CREATE TABLE IF NOT EXISTS `t_notifications` (
  `notif_id` int(30) NOT NULL AUTO_INCREMENT COMMENT 'Notification id',
  `notif_type` tinyint(2) NOT NULL COMMENT '1 - like note, 2 - chat',
  `sender` int(11) NOT NULL COMMENT 'id of sender',
  `reciever` int(11) NOT NULL COMMENT 'id of reciever',
  `message` varchar(500) NOT NULL COMMENT 'notification message',
  `notif_dt` datetime NOT NULL COMMENT 'Notification date time',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '1 - active, 2 - inactive',
  PRIMARY KEY (`notif_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=109 ;

--
-- Dumping data for table `t_notifications`
--

INSERT INTO `t_notifications` (`notif_id`, `notif_type`, `sender`, `reciever`, `message`, `notif_dt`, `status`) VALUES
(1, 2, 3, 2, 'Hi', '2014-04-03 06:32:51', 1),
(2, 3, 9, 8, 'Congratulations! You got a match in Flamer app!', '2014-04-03 10:58:03', 1),
(3, 3, 9, 8, 'Congratulations! You got a match in Flamer app!', '2014-04-03 11:10:42', 1),
(4, 2, 9, 8, 'hi', '2014-04-03 11:11:27', 1),
(5, 2, 8, 8, 'fa', '2014-04-03 11:55:51', 1),
(6, 2, 8, 8, 'fa', '2014-04-03 11:56:23', 1),
(7, 2, 8, 8, 'fa', '2014-04-03 11:56:56', 1),
(8, 2, 8, 8, 'fa', '2014-04-03 11:57:19', 1),
(9, 2, 8, 8, 'fa', '2014-04-03 11:59:40', 1),
(10, 2, 8, 8, '', '2014-04-03 12:00:54', 1),
(11, 2, 8, 8, '', '2014-04-03 12:01:45', 1),
(12, 2, 8, 8, '', '2014-04-03 12:08:24', 1),
(13, 2, 8, 8, '', '2014-04-03 12:10:59', 1),
(14, 2, 0, 8, '', '2014-04-03 12:11:19', 1),
(15, 2, 8, 8, '', '2014-04-03 12:12:30', 1),
(16, 2, 8, 8, '', '2014-04-03 12:12:56', 1),
(17, 2, 8, 8, '', '2014-04-03 12:13:10', 1),
(18, 2, 8, 8, '', '2014-04-03 12:13:58', 1),
(19, 2, 8, 8, '', '2014-04-03 12:16:11', 1),
(20, 2, 8, 8, '', '2014-04-03 12:16:26', 1),
(21, 2, 8, 8, '', '2014-04-03 12:24:41', 1),
(22, 2, 8, 8, '', '2014-04-03 12:25:28', 1),
(23, 2, 8, 8, '', '2014-04-03 12:25:43', 1),
(24, 2, 8, 9, '', '2014-04-03 12:28:36', 1),
(25, 2, 8, 9, '', '2014-04-05 06:32:39', 1),
(26, 2, 8, 9, '', '2014-04-05 06:32:57', 1),
(27, 2, 8, 9, '', '2014-04-05 06:33:11', 1),
(28, 2, 8, 9, '', '2014-04-05 06:42:28', 1),
(29, 2, 8, 9, '', '2014-04-05 06:53:15', 1),
(30, 2, 8, 9, '', '2014-04-05 06:54:06', 1),
(31, 2, 8, 9, '', '2014-04-05 06:54:44', 1),
(32, 2, 8, 9, '', '2014-04-05 06:55:14', 1),
(33, 2, 8, 9, '', '2014-04-05 07:00:59', 1),
(34, 2, 8, 9, '', '2014-04-05 07:07:12', 1),
(35, 2, 8, 9, '', '2014-04-05 07:07:36', 1),
(36, 2, 8, 9, '', '2014-04-05 07:09:16', 1),
(37, 2, 8, 9, '', '2014-04-05 07:10:12', 1),
(38, 2, 8, 9, '', '2014-04-05 07:10:38', 1),
(39, 2, 8, 9, '', '2014-04-05 08:45:42', 1),
(40, 2, 8, 9, '', '2014-04-05 08:50:52', 1),
(41, 2, 8, 9, '', '2014-04-05 08:51:38', 1),
(42, 2, 8, 9, '', '2014-04-05 09:00:16', 1),
(43, 2, 8, 9, '', '2014-04-05 09:09:22', 1),
(44, 2, 8, 9, '', '2014-04-05 09:09:44', 1),
(45, 2, 8, 9, '', '2014-04-05 09:29:34', 1),
(46, 2, 8, 9, '', '2014-04-05 11:37:56', 1),
(47, 2, 8, 9, '', '2014-04-05 11:38:16', 1),
(48, 2, 8, 9, '', '2014-04-05 11:38:25', 1),
(49, 2, 8, 9, '', '2014-04-05 11:41:37', 1),
(50, 2, 9, 8, '', '2014-04-05 11:43:50', 1),
(51, 2, 8, 9, '', '2014-04-07 04:49:43', 1),
(52, 2, 8, 9, '', '2014-04-07 04:53:21', 1),
(53, 2, 8, 9, '', '2014-04-07 04:56:58', 1),
(54, 2, 8, 9, '', '2014-04-07 04:57:57', 1),
(55, 2, 8, 9, '', '2014-04-07 04:58:41', 1),
(56, 2, 8, 9, '', '2014-04-07 05:01:51', 1),
(57, 2, 8, 9, '', '2014-04-07 05:02:22', 1),
(58, 2, 8, 9, '', '2014-04-07 05:03:50', 1),
(59, 2, 8, 9, '', '2014-04-07 05:03:59', 1),
(60, 2, 8, 9, '', '2014-04-07 05:04:07', 1),
(61, 2, 8, 9, '', '2014-04-07 05:07:39', 1),
(62, 2, 8, 9, '', '2014-04-07 05:08:26', 1),
(63, 2, 9, 8, '', '2014-04-07 05:28:02', 1),
(64, 2, 8, 8, '', '2014-04-07 05:30:04', 1),
(65, 2, 8, 8, '', '2014-04-07 05:30:16', 1),
(66, 2, 8, 8, '', '2014-04-07 05:30:28', 1),
(67, 2, 9, 8, '', '2014-04-07 05:38:58', 1),
(68, 2, 8, 8, 'fs', '2014-04-07 05:58:34', 1),
(69, 2, 8, 8, 'fs', '2014-04-07 05:58:58', 1),
(70, 2, 8, 8, 'fsafaff', '2014-04-07 05:59:08', 1),
(71, 2, 8, 8, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396850550000hbg_img.gif', '2014-04-07 06:02:30', 1),
(72, 2, 9, 8, 'Testing only text', '2014-04-07 06:06:11', 1),
(73, 2, 8, 8, 'fsafaffffff', '2014-04-07 06:16:43', 1),
(74, 2, 8, 8, 'fsafaffffff', '2014-04-07 06:16:56', 1),
(75, 2, 8, 8, 'fsafaffffff', '2014-04-07 06:17:34', 1),
(76, 2, 8, 8, 'fsafaffffff', '2014-04-07 06:17:40', 1),
(77, 2, 8, 8, '121', '2014-04-07 06:17:48', 1),
(78, 2, 8, 8, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396851477000hbg_img.gif', '2014-04-07 06:17:57', 1),
(79, 2, 9, 8, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396855824000temp.jpeg', '2014-04-07 07:30:24', 1),
(80, 2, 9, 8, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396860727000temp.jpeg', '2014-04-07 08:52:07', 1),
(81, 2, 8, 9, 'New\ndfdfdfdsf', '2014-04-07 09:31:25', 1),
(82, 2, 8, 9, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396863248000temp.jpeg', '2014-04-07 09:34:08', 1),
(83, 2, 8, 9, 'Hghg', '2014-04-07 09:38:14', 1),
(84, 2, 9, 8, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396865530000temp.jpeg', '2014-04-07 10:12:10', 1),
(85, 2, 9, 8, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396866095000temp.jpeg', '2014-04-07 10:21:35', 1),
(86, 2, 9, 8, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396866153000temp.jpeg', '2014-04-07 10:22:33', 1),
(87, 2, 9, 8, 'Ghgjg', '2014-04-07 10:25:25', 1),
(88, 2, 9, 8, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396866331000temp.jpeg', '2014-04-07 10:25:31', 1),
(89, 2, 9, 8, 'Hello Vipul', '2014-04-07 12:30:43', 1),
(90, 2, 9, 8, 'How r u?', '2014-04-07 12:30:51', 1),
(91, 2, 8, 9, 'Hi', '2014-04-07 12:32:56', 1),
(92, 2, 9, 8, 'hello', '2014-04-07 12:35:38', 1),
(93, 2, 8, 9, 'Hello', '2014-04-07 12:44:07', 1),
(94, 2, 8, 9, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396874685000temp.jpeg', '2014-04-07 12:44:45', 1),
(95, 2, 8, 9, 'test', '2014-04-07 12:45:44', 1),
(96, 2, 9, 8, 'test', '2014-04-07 12:47:22', 1),
(97, 2, 8, 9, 'Hello', '2014-04-07 13:05:03', 1),
(98, 2, 8, 9, 'dadsasd', '2014-04-07 13:05:08', 1),
(99, 2, 8, 9, 'Asdasfdaf\nDfdfd\nDfdfdfdsfds', '2014-04-07 13:05:15', 1),
(100, 2, 8, 9, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396875922000temp.jpeg', '2014-04-07 13:05:22', 1),
(101, 2, 8, 9, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396875937000temp.jpeg', '2014-04-07 13:05:37', 1),
(102, 2, 8, 9, 'dfdfdsfsdf', '2014-04-07 13:06:02', 1),
(103, 2, 8, 9, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396875975000temp.jpeg', '2014-04-07 13:06:15', 1),
(104, 2, 8, 9, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396876036000temp.jpeg', '2014-04-07 13:07:16', 1),
(105, 2, 8, 9, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396876148000temp.jpeg', '2014-04-07 13:09:08', 1),
(106, 2, 8, 9, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396876556000temp.jpeg', '2014-04-07 13:15:56', 1),
(107, 2, 8, 9, 'http://192.168.0.114/PHP_Server/phpserver/pics/1396876676000temp.jpeg', '2014-04-07 13:17:56', 1),
(108, 2, 8, 8, '121f', '2014-04-09 11:20:07', 1);

-- --------------------------------------------------------

--
-- Table structure for table `t_preferences`
--

CREATE TABLE IF NOT EXISTS `t_preferences` (
  `Entity_Id` int(25) NOT NULL COMMENT 'Entityid',
  `Preference_Sex` tinyint(1) NOT NULL COMMENT '1 - male, 2 - female',
  `Preference_lower_age` int(3) NOT NULL DEFAULT '0' COMMENT 'Prefered lower age',
  `Preference_upper_age` int(3) NOT NULL DEFAULT '0' COMMENT 'prefered upper age',
  `Preference_radius` double NOT NULL DEFAULT '10' COMMENT 'prefered radius',
  UNIQUE KEY `Entity_Id` (`Entity_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='List of PreferencesPer User';

--
-- Dumping data for table `t_preferences`
--

INSERT INTO `t_preferences` (`Entity_Id`, `Preference_Sex`, `Preference_lower_age`, `Preference_upper_age`, `Preference_radius`) VALUES
(8, 3, 10, 55, 100),
(9, 1, 10, 50, 50);

-- --------------------------------------------------------

--
-- Table structure for table `t_question`
--

CREATE TABLE IF NOT EXISTS `t_question` (
  `que_id` int(50) NOT NULL AUTO_INCREMENT,
  `quetion` text NOT NULL,
  `option_a` varchar(255) NOT NULL,
  `option_b` varchar(255) NOT NULL,
  `option_c` varchar(255) NOT NULL,
  `option_d` varchar(250) NOT NULL,
  PRIMARY KEY (`que_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=25 ;

--
-- Dumping data for table `t_question`
--

INSERT INTO `t_question` (`que_id`, `quetion`, `option_a`, `option_b`, `option_c`, `option_d`) VALUES
(1, 'Say you''ve started seeing someone tou really like.as far as  you''re concerned,how long will it take before you have sex?', '1-25 dates ', '3-5 dates', '6 or more dates', 'only after the wedding'),
(2, 'Rate your self-confidence', 'very very,high', 'higher than average', 'average', 'below average'),
(3, 'What is the most  exciting thing about getting to know someone new?', 'Discovering your shared interests', 'Discovering their body', '', ''),
(4, 'How important is religion/god in your life?', 'Extremely important', 'Somewhat important', 'Not Very Important', 'Not At all Important'),
(5, 'which makes for a  better relationship?', 'passion', 'Dediction', '', ''),
(6, 'Do you enjoy intense intellectual conversations?', 'YES', 'NO', '', ''),
(7, 'Do you enjoy discussing politics?', 'YES', 'NO', '', ''),
(8, 'Jealousy healthy or unhealthy,in the context of a relationships?', 'Healthy', 'Unhealthy', '', ''),
(9, 'Would you prefer good things happened, or interesting things?', 'Good', 'Intresting', '', ''),
(10, 'In a certain light ,wouldn''t nuclear war be exciting?', 'YES,it would.', 'NO,it wouldn''t', '', ''),
(11, 'Do you like horror movies?', 'YES', 'NO', '', ''),
(12, 'Are you a cat peson or a dog person?', 'Cats', 'Dogs', 'Both', 'Neither'),
(13, 'Which word describes you better?', 'Carefree', 'intense', '', ''),
(14, 'Do spelling mistakes annoy you?', 'YES', 'NO', '', ''),
(15, 'If you turn left-handed glove inside out,it fits..', 'on my left hand', 'on my right hand', '', ''),
(16, 'Stale is steal as 89475 is to ...', '89457', '98547', '89754', '89547'),
(17, 'What''s your relationship with marijuana?', 'i smoke regulary', 'i smoke occasionally', 'i smoked in the past but no longer ', 'never'),
(18, 'What''s your deal with harder drugs(stuff beyond pot?', 'i do drugs regulary', 'i do drugs occasinally', 'i''ve done drugs  in past ,but no longer', 'i never do drugs'),
(19, 'How frequently do you drink alcohol?', 'veryoften', 'sometimes', 'rarely', 'never'),
(20, 'Are you either vegetarian or vegan?', 'YES', 'NO', '', ''),
(21, 'which best describes your plitical beliefs?', ' liberal/left-wing', 'conservative/right-wing', 'other', ''),
(22, 'Should evolution and creationism be taught side -by -side in school?', 'YES, students should hear both sides', 'NO ,evolution has no place in schools', '', ''),
(23, 'Do you think homosexuality is a sin?', 'YES', 'NO', '', ''),
(24, 'For you pesonlly, is abortion an option  in case of an accidental pregnancy?', 'YES', 'NO', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `t_state`
--

CREATE TABLE IF NOT EXISTS `t_state` (
  `State_Id` int(7) NOT NULL AUTO_INCREMENT COMMENT 'State Id',
  `Country_Id` int(6) NOT NULL COMMENT 'Country id',
  `state_name` varchar(100) NOT NULL COMMENT 'State name',
  PRIMARY KEY (`State_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='States list in all countries' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_statusmessages`
--

CREATE TABLE IF NOT EXISTS `t_statusmessages` (
  `sid` int(4) NOT NULL AUTO_INCREMENT COMMENT 'Status id',
  `statusNumber` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0 - success, 1 - error',
  `statusMessage` varchar(400) NOT NULL COMMENT 'brief status message',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='status messages for the appl response' AUTO_INCREMENT=67 ;

--
-- Dumping data for table `t_statusmessages`
--

INSERT INTO `t_statusmessages` (`sid`, `statusNumber`, `statusMessage`) VALUES
(1, 1, 'Mandatory field missing'),
(2, 0, 'Login completed successfully'),
(3, 0, 'Signup completed successfully'),
(5, 1, 'Device type not supported'),
(6, 1, 'Failed to login'),
(7, 1, 'Problem in signing up'),
(8, 1, 'Bad request, cannot be processed'),
(9, 1, 'Session token expired, please login'),
(10, 0, 'Details updated successfully.'),
(11, 1, 'No data to update.'),
(12, 1, 'Failed to update the data.'),
(13, 0, 'Preferences updated successfully.'),
(14, 1, 'Change any preferences to update.'),
(15, 1, 'Failed to update the preferences.'),
(16, 1, 'Please upload an image with valid extension'),
(17, 1, 'Chunk size is larger than 512 kb.'),
(18, 0, 'Image upload completed successfully.'),
(19, 1, 'Please set your preference settings to find out matches around you.'),
(20, 0, 'Matches Found!'),
(21, 1, 'No matches in the vicinity! Please modify your search criteria!'),
(22, 1, 'Search unsuccessful, error occured '),
(23, 0, 'Image deleted successfully'),
(24, 1, 'Image not found'),
(25, 1, 'Error in deleting the image'),
(26, 0, 'Likes updated successfully'),
(27, 1, 'Unable to insert likes'),
(28, 1, 'Unable to modify likes'),
(29, 0, 'Like sent!'),
(30, 1, 'Unable to connect apple'),
(31, 1, 'Invalid token, please login or register'),
(32, 0, 'Profile updated successfully'),
(33, 1, 'Nothing to update'),
(34, 1, 'Failed to update profile'),
(35, 0, 'Notification listed successfully'),
(36, 1, 'Notification unavailable'),
(37, 1, 'Server Error! Please try again after sometime!'),
(38, 1, 'Provided FBid is unavailable'),
(39, 1, 'Unable to find the profile'),
(40, 0, 'Got the profile!'),
(41, 0, 'Logged out successfully'),
(42, 0, 'Location updated successfully'),
(43, 1, 'Location already up to date'),
(44, 0, 'Message sent!'),
(45, 1, 'Failed to send message'),
(46, 1, 'Unable to send push'),
(47, 0, 'Got chat history!'),
(48, 1, 'Chat history not found'),
(49, 1, 'Queried user is deleted or inactive'),
(50, 0, 'Matches found!'),
(51, 1, 'Sorry, no matches found!'),
(52, 0, 'User blocked successfully'),
(53, 0, 'Got the settings!'),
(54, 1, 'Sorry, settings not found.'),
(55, 0, 'Congrats! You got a match'),
(56, 0, 'Got the message!'),
(57, 1, 'Message not found.'),
(58, 0, 'User unblocked successfully.'),
(59, 0, 'Session updated!'),
(60, 1, 'Session not found, please signup to become a member.'),
(61, 0, 'Delete message Successfully'),
(62, 1, 'delete flag set'),
(63, 0, 'Question list'),
(64, 0, 'Submit answer Successfully'),
(65, 0, 'Already Updated'),
(66, 0, 'Enter valid file');

-- --------------------------------------------------------

--
-- Table structure for table `t_user_likes`
--

CREATE TABLE IF NOT EXISTS `t_user_likes` (
  `id` int(25) NOT NULL AUTO_INCREMENT,
  `entity_id` int(25) NOT NULL,
  `like_id` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=523 ;

--
-- Dumping data for table `t_user_likes`
--

INSERT INTO `t_user_likes` (`id`, `entity_id`, `like_id`) VALUES
(1, 1, '84373376522'),
(2, 1, '280389558793533'),
(3, 1, '100484820802'),
(4, 1, '286893159420'),
(5, 1, '134771819253'),
(6, 1, '137209536349'),
(7, 1, '209365255909469'),
(8, 1, '1427604810810134'),
(9, 1, '353386506511'),
(10, 1, '105310924507'),
(11, 1, '92284851236'),
(12, 1, '569026396502025'),
(13, 1, '697309713612425'),
(14, 1, '184695207750'),
(15, 1, '157697300924538'),
(16, 1, '550194655029359'),
(17, 1, '113472061996206'),
(18, 1, '136399183231434'),
(19, 1, '449070525189777'),
(20, 1, '273285326069305'),
(21, 1, '104105339655077'),
(22, 1, '159460420880667'),
(23, 1, '380898288618872'),
(24, 1, '217905271554262'),
(25, 1, '150426751672388'),
(85, 3, '371024872978909'),
(86, 3, '625021667555636'),
(87, 3, '19292868552'),
(88, 4, '371024872978909'),
(89, 4, '625021667555636'),
(90, 4, '19292868552'),
(91, 6, '371024872978909'),
(92, 6, '625021667555636'),
(93, 6, '19292868552'),
(94, 2, '84373376522'),
(95, 2, '280389558793533'),
(96, 2, '100484820802'),
(97, 2, '286893159420'),
(98, 2, '134771819253'),
(99, 2, '137209536349'),
(100, 2, '209365255909469'),
(101, 2, '1427604810810134'),
(102, 2, '353386506511'),
(103, 2, '105310924507'),
(104, 2, '92284851236'),
(105, 2, '569026396502025'),
(106, 2, '697309713612425'),
(107, 2, '184695207750'),
(108, 2, '157697300924538'),
(109, 2, '550194655029359'),
(110, 2, '113472061996206'),
(111, 2, '136399183231434'),
(112, 2, '449070525189777'),
(113, 2, '273285326069305'),
(114, 2, '104105339655077'),
(115, 2, '159460420880667'),
(116, 2, '380898288618872'),
(117, 2, '217905271554262'),
(118, 2, '150426751672388'),
(144, 7, '84373376522'),
(145, 7, '280389558793533'),
(146, 7, '100484820802'),
(147, 7, '286893159420'),
(148, 7, '134771819253'),
(149, 7, '137209536349'),
(150, 7, '209365255909469'),
(151, 7, '1427604810810134'),
(152, 7, '353386506511'),
(153, 7, '105310924507'),
(154, 7, '92284851236'),
(155, 7, '569026396502025'),
(156, 7, '697309713612425'),
(157, 7, '184695207750'),
(158, 7, '157697300924538'),
(159, 7, '550194655029359'),
(160, 7, '113472061996206'),
(161, 7, '136399183231434'),
(162, 7, '449070525189777'),
(163, 7, '273285326069305'),
(164, 7, '104105339655077'),
(165, 7, '159460420880667'),
(166, 7, '380898288618872'),
(167, 7, '217905271554262'),
(168, 7, '150426751672388'),
(495, 9, '371024872978909'),
(496, 9, '625021667555636'),
(497, 9, '19292868552'),
(498, 8, '84373376522'),
(499, 8, '280389558793533'),
(500, 8, '100484820802'),
(501, 8, '286893159420'),
(502, 8, '134771819253'),
(503, 8, '137209536349'),
(504, 8, '209365255909469'),
(505, 8, '1427604810810134'),
(506, 8, '353386506511'),
(507, 8, '105310924507'),
(508, 8, '92284851236'),
(509, 8, '569026396502025'),
(510, 8, '697309713612425'),
(511, 8, '184695207750'),
(512, 8, '157697300924538'),
(513, 8, '550194655029359'),
(514, 8, '113472061996206'),
(515, 8, '136399183231434'),
(516, 8, '449070525189777'),
(517, 8, '273285326069305'),
(518, 8, '104105339655077'),
(519, 8, '159460420880667'),
(520, 8, '380898288618872'),
(521, 8, '217905271554262'),
(522, 8, '150426751672388');

-- --------------------------------------------------------

--
-- Table structure for table `t_user_sessions`
--

CREATE TABLE IF NOT EXISTS `t_user_sessions` (
  `sid` int(20) NOT NULL AUTO_INCREMENT COMMENT 'Session id',
  `oid` int(20) NOT NULL COMMENT 'Object Id',
  `token` varchar(500) NOT NULL COMMENT 'Session token',
  `expiry_gmt` datetime NOT NULL COMMENT 'Session expiry date and time in GMT',
  `device` varchar(500) NOT NULL COMMENT 'Device on which session is generated',
  `type` int(4) NOT NULL COMMENT 'Type of device or platform',
  `push_token` varchar(700) DEFAULT NULL COMMENT 'Token for push notification',
  `create_date_gmt` datetime NOT NULL COMMENT 'Current date and time in GMT',
  `loggedIn` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1 - logged in, 2 - logged out',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='stores multiple session token for all the users' AUTO_INCREMENT=43 ;

--
-- Dumping data for table `t_user_sessions`
--

INSERT INTO `t_user_sessions` (`sid`, `oid`, `token`, `expiry_gmt`, `device`, `type`, `push_token`, `create_date_gmt`, `loggedIn`) VALUES
(17, 8, '44313934oL0uKvjqhyy45BV8BQyq373236322D353331432D343537342D423537442D314137424244414433373842oL0uKvjqhyy45BV8BQyq', '2014-04-25 09:12:49', 'D1947262-531C-4574-B57D-1A7BBDAD378B', 1, '', '2014-04-03 09:05:57', 2),
(18, 9, '38323030383830352D453131412D343837332D413646422D393645373541413131304139e7Ke7KNrsIm2lKFCpBEFR74NrsIm2lKFCpBEFR74', '2014-04-30 09:22:00', '82008805-E11A-4873-A6FB-96E75AA110A9', 1, '', '2014-04-03 09:15:15', 1),
(19, 9, '41444644363636432D314536412D343842412D413636322D463232303030393239304343Xjj8RBAN6aDpXjj8RBAN6aDpZxmujHvAZxmujHvA', '2014-04-05 09:57:51', 'ADFD666C-1E6A-48BA-A662-F220009290CC', 1, '', '2014-04-03 09:29:15', 1),
(20, 9, '41384431363431392D383143392D344344372D383941422D323534354342343aNNgXqGXY2rhKXSE7wg8133443830aNNgXqGXY2rhKXSE7wg8', '2014-04-05 10:09:56', 'A8D16419-81C9-4CD7-89AB-2545CB413D80', 1, '', '2014-04-03 10:08:38', 1),
(21, 9, '30374230414331342D354433382D344542312D414644392D4BNXjXiKXLcjH4YUZIIBr54439384139353630334630BNXjXiKXLcjH4YUZIIBr', '2014-04-05 10:12:04', '07B0AC14-5D38-4EB1-AFD9-ED98A95603F0', 1, 'gfghfgjhgjhgkhgkkhkhk', '2014-04-03 10:12:04', 1),
(22, 9, '38413841373641312D334143452D344231382D424246422D343646414639368dKH4VY6ksISx0XqflQf41353637468dKH4VY6ksISx0XqflQf', '2014-04-05 10:39:44', '8A8A76A1-3ACE-4B18-BBFB-46FAF96A567F', 1, '', '2014-04-03 10:16:03', 2),
(23, 8, '38413841373641312D334143452D344231382ltpkTDAUmCD4L5rT21QeD424246422D343646414639364135363746ltpkTDAUmCD4L5rT21Qe', '2014-04-05 10:48:21', '8A8A76A1-3ACE-4B18-BBFB-46FAF96A567F', 1, '', '2014-04-03 10:41:01', 1),
(24, 9, '44434630413433412D303737332D344130442D383231442D34jkqUGufliOsepumWMFsd4137413033313545353945jkqUGufliOsepumWMFsd', '2014-04-05 11:03:21', 'DCF0A43A-0773-4A0D-821D-4A7A0315E59E', 1, '', '2014-04-03 10:57:21', 1),
(25, 8, '36363731423237322D423139372D344639412D383136352D3433434545RCcivfjmtIqVLvjIqiUN46314135393444RCcivfjmtIqVLvjIqiUN', '2014-04-05 11:06:21', '6671B272-B197-4F9A-8165-43CEEF1A594D', 1, 'gfghfgjhgjhgkhgkkhkhk', '2014-04-03 11:06:21', 1),
(26, 8, '38333836313930312D354634382D3439nwwzw38XBMRQP1tgLHH743342D424137452D324533303636393732313444nwwzw38XBMRQP1tgLHH7', '2014-04-05 11:08:52', '83861901-5F48-49C4-BA7E-2E306697214D', 1, 'gfghfgjhgjhgkhgkkhkhk', '2014-04-03 11:08:52', 2),
(27, 9, '38333836313930312D354634382D343943342D424137452D324533303636393732313444yQGNgtuBhmOjEqxyQGNgtuBhmOjEqx9dGfW9dGfW', '2014-04-05 11:10:34', '83861901-5F48-49C4-BA7E-2E306697214D', 1, 'gfghfgjhgjhgkhgkkhkhk', '2014-04-03 11:10:34', 1),
(28, 8, '35363133303844392D313442452D343242372D423931452D383645384hXntwSyoJPTOiVSKOm4D634373633383241hXntwSyoJPTOiVSKOm4D', '2014-04-06 10:06:58', '561308D9-14BE-42B7-B91E-86E8F476382A', 1, '', '2014-04-04 10:05:55', 1),
(29, 8, '30463239313636442D433836342D344630Mz53dAHHwThk07MjFKnn382D423333442D463635354437374146343645Mz53dAHHwThk07MjFKnn', '2014-04-06 11:58:38', '0F29166D-C864-4F08-B33D-F655D77AF46E', 1, '', '2014-04-04 10:10:20', 1),
(30, 8, '31454541343146332D303630452D344438342D424xy2t8P8ZATU6b2qYDEdO433432D424134303246453146393337xy2t8P8ZATU6b2qYDEdO', '2014-04-06 13:06:07', '1EEA41F3-060E-4D84-BD3C-BA402FE1F937', 1, '', '2014-04-04 12:53:11', 1),
(31, 9, '46304542324138392D314444342D343131302D394545422D4230443034334338314436446uQ4sqzYF41YOGw84C6q6uQ4sqzYF41YOGw84C6q', '2014-04-07 12:14:00', 'F0EB2A89-1DD4-4110-9EEB-B0D043C81D6D', 1, '', '2014-04-05 05:04:02', 1),
(32, 9, '344249qOjYXFcn40GUYJISH13144423537322D423037432D343145312D393939302D3131443838444541383433469qOjYXFcn40GUYJISH13', '2014-04-09 08:52:22', '4BADB572-B07C-41E1-9990-11D88DEA843F', 1, '', '2014-04-07 05:27:12', 1),
(33, 8, '44423539424141432D394331342D344144342D39424332BBhrFdZC8N1OTZLNHbTL2D413841463436313141384142BBhrFdZC8N1OTZLNHbTL', '2014-04-09 09:35:45', 'DB59BAAC-9C14-4AD4-9BC2-A8AF4611A8AB', 1, '', '2014-04-07 08:57:25', 1),
(34, 9, '42343135303433332D354342432D343338332D39424342kO6MSadIaOWw7x9ov1oS2D314441303236323837453545kO6MSadIaOWw7x9ov1oS', '2014-04-09 12:46:34', 'B4150433-5CBC-4383-9BCB-1DA026287E5E', 1, 'gfghfgjhgjhgkhgkkhkhk', '2014-04-07 09:39:31', 2),
(35, 8, '42343135303433332D354342432D343338332D394243422D31444XGc8oVqGavNvEXwaKSu21303236323837453545XGc8oVqGavNvEXwaKSu2', '2014-04-09 13:37:12', 'B4150433-5CBC-4383-9BCB-1DA026287E5E', 1, '', '2014-04-07 12:31:50', 1),
(36, 9, '37394537303644302D443034412D344633422D4143323RIFtmilKzxAi13arw6zK52D393446383439353332423146RIFtmilKzxAi13arw6zK', '2014-04-10 08:55:18', '79E706D0-D04A-4F3B-AC25-94F849532B1F', 1, 'gfghfgjhgjhgkhgkkhkhk', '2014-04-08 08:55:18', 1),
(37, 9, '33423831384LfOLSJTgne2XRLyjA92Z335362D434437412D343334332D423033452D313735313544434539353643LfOLSJTgne2XRLyjA92Z', '2014-04-10 13:15:09', '3B818C56-CD7A-4343-B03E-17515DCE956C', 1, '', '2014-04-08 11:14:18', 1),
(38, 9, '45434430313834322D313239372x0mz7sVrjJbihaIM6bRsD344437342D424436342D424333453431383834353934x0mz7sVrjJbihaIM6bRs', '2014-04-11 06:52:53', 'ECD01842-1297-4D74-BD64-BC3E41884594', 1, '', '2014-04-09 05:40:02', 1),
(39, 9, '38364636433638342D313031362D344246362D393030442D333738454438413842433132vhW78YArqGgKcIaOkvhW78YArqGgKcIaOk92z92z', '2014-04-11 11:58:17', '86F6C684-1016-4BF6-900D-378ED8A8BC12', 1, 'gfghfgjhgjhgkhgkkhkhk', '2014-04-09 11:58:17', 1),
(40, 9, '35314136333133392r6rWPgo0qY03UeiOJCzHD423838452D344531302D383638302D394235433538453236323242r6rWPgo0qY03UeiOJCzH', '2014-04-11 13:30:12', '51A63139-B88E-4E10-8680-9B5C58E2622B', 1, 'gfghfgjhgjhgkhgkkhkhk', '2014-04-09 13:30:12', 1),
(41, 9, '30373835323530322D423832362D343739382D413333352D454137464546343135423634oKr2hrnTHb5lHAGoM0oKr2hrnTHb5lHAGoM09m9m', '2014-04-12 10:41:41', '07852502-B826-4798-A335-EA7FEF415B64', 1, '', '2014-04-10 07:39:31', 1),
(42, 8, '34364231414633352D433431442D343933432D393746452D443836313335333341344339rbOhl6ZrbOhl6ZE0Oiy98mGIQa2E0Oiy98mGIQa2', '2014-04-12 11:35:19', '46B1AF35-C41D-493C-97FE-D8613533A4C9', 1, '', '2014-04-10 10:49:46', 1);

-- --------------------------------------------------------

--
-- Table structure for table `t_work_experience`
--

CREATE TABLE IF NOT EXISTS `t_work_experience` (
  `Work_Id` int(25) NOT NULL AUTO_INCREMENT,
  `Entity_Id` int(25) NOT NULL COMMENT 'Entity_id',
  `Work_city` varchar(50) DEFAULT NULL,
  `work_country` varchar(50) DEFAULT NULL,
  `work_company` varchar(150) NOT NULL,
  `work_post` varchar(100) NOT NULL,
  `work_start_date` date DEFAULT NULL,
  `work_end_date` date DEFAULT NULL,
  PRIMARY KEY (`Work_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='List of work experience for each user' AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
