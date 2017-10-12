-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 2017-10-12 12:12:12
-- 服务器版本： 10.1.13-MariaDB
-- PHP Version: 5.6.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `android_app_frag2`
--

-- --------------------------------------------------------

--
-- 表的结构 `shijianfabu`
--

CREATE TABLE `shijianfabu` (
  `ID` int(10) UNSIGNED NOT NULL,
  `user_phone` varchar(256) DEFAULT NULL,
  `start_time` varchar(256) DEFAULT NULL,
  `end_time` varchar(256) DEFAULT NULL,
  `work_describe` varchar(1024) DEFAULT NULL,
  `work_position` varchar(512) DEFAULT NULL,
  `is_finish` varchar(256) NOT NULL DEFAULT 'N',
  `send_time` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- 转存表中的数据 `shijianfabu`
--

INSERT INTO `shijianfabu` (`ID`, `user_phone`, `start_time`, `end_time`, `work_describe`, `work_position`, `is_finish`, `send_time`) VALUES
(4, '17854259542', 'nullnullnullnull', 'nullnullnullnull', '哈哈', '山东科技大学', 'Y', '2016-04-30 01:00:06'),
(6, '17854259542', '2016-4-30/8:30', '2017-4-30/8:30', '起床', '明天必须起床', 'N', '2016-04-30 19:39:58'),
(7, '17854259542', '2018-4-30/8:30', '2019-4-30/8:30', '很好很好很好', '写作业快些', 'Y', '2016-04-30 19:44:00'),
(8, '17854259542', '2016-4-30/8:30', '2017-4-30/8:32', '哈哈哈还好还好', '打游戏', 'N', '2016-04-30 19:46:36'),
(10, '17854259542', '2016-4-30/8:30', '2016-4-30/8:30', '超级长测试', 'bbhhhhhhhhhhhhhhhbbbbbbbbbvvvvggghhhhhhhhhhbbbbbbbbbbbbbbbvvvvvvvvvvvbhhhhgggvvvvvcvvvbbhhjjjjjjjjjjjjjjhhbbvvvvvvvccccvvvhhhffffffgggg', 'Y', '2016-04-30 22:57:06'),
(11, '17854259542', '2016-5-1/8:30', '2016-5-2/8:30', '早起吃饭', '一天一顿饭，这什么日子，', 'N', '2016-05-01 02:28:24'),
(13, '17863963884', '2020-4-30/00:00', '2050-4-30/00:00', '时空穿梭', '回到30年前删掉这条任务', 'N', '2016-05-01 21:41:34'),
(14, '17863963884', '2016-5-1/8:30', '2017-5-1/8:30', '测试', '第一天任务，删除上一条内容', 'N', '2016-05-01 21:45:30'),
(15, '17863963884', '2016-5-1/8:30', '2016-5-1/8:30', '测试', '测试刷新', 'Y', '2016-05-01 21:52:45'),
(16, '17863963884', '2016-5-1/8:30', '2016-5-2/8:30', '一天一顿饭，这什么日子，', '早起吃饭', 'Y', '2016-05-02 15:54:21'),
(17, '17863963884', '2016-4-30/8:30', '2017-4-30/8:32', '打游戏', '哈哈哈还好还好', 'N', '2016-05-02 16:11:57'),
(18, '17854259542', '2016-5-2/8:30', '2016-5-2/8:30', '吃饭去', '快点', 'Y', '2016-05-02 16:36:19'),
(19, '17854259542', '2016-5-2/8:30', '2016-5-2/8:30', '嘿嘿', '发送任务给好友测试接受者，17854259542', 'N', '2016-05-02 16:48:56'),
(20, '18561749963', '2020-4-30/00:00', '2050-4-30/00:00', '时空穿梭', '回到30年前删掉这条任务', 'N', '2016-05-16 17:40:42'),
(21, '13715573769', '2020-4-30/00:00', '2050-4-30/00:00', '时空穿梭', '回到30年前删掉这条任务', 'N', '2016-05-22 20:05:10'),
(22, '17854259542', '2016-6-3/8:30', '2016-6-9/8:30', '们', '的都', 'Y', '2016-06-03 22:33:23'),
(23, '17854251719', '2016-6-3/8:30', '2016-6-5/8:30', '喇叭', '啊啊啊', 'N', '2016-06-03 22:34:09'),
(24, '17854251719', '2016-6-3/8:30', '2016-7-3/8:30', '录制视频', '录制视频', 'N', '2016-06-03 23:50:14'),
(25, '17854259542', '2016-6-3/8:30', '2016-6-3/8:32', '嗨', '嗨', 'N', '2016-06-03 23:50:49'),
(26, '17854251719', '2016-5-2/8:30', '2016-5-2/8:30', '发送任务给好友测试接受者，17854259542', '嘿嘿', 'N', '2016-06-03 23:57:36'),
(27, '17854259542', '2016-6-3/8:30', '2016-6-3/8:33', '自己的任务', '给自己的任务', 'N', '2016-06-03 23:58:09'),
(28, '17854251719', '2016-6-3/8:30', '2016-6-3/8:35', '给别人的任务', '给别人', 'N', '2016-06-03 23:58:45'),
(29, '17854259542', '2016-6-8/8:30', '2016-6-8/8:30', '啊啊啊', '啦咔咔', 'N', '2016-06-08 14:33:42'),
(30, '13658223800', '2020-4-30/00:00', '2050-4-30/00:00', '时空穿梭', '回到30年前删掉这条任务', 'N', '2016-08-02 19:19:23'),
(31, '17854259542', '2016-5-1/8:30', '2016-5-1/8:30', '测试刷新', '测试', 'N', '2016-09-29 00:03:49'),
(32, '18636876332', '2020-4-30/00:00', '2050-4-30/00:00', '时空穿梭', '回到30年前删掉这条任务', 'N', '2016-11-09 21:45:38');

-- --------------------------------------------------------

--
-- 表的结构 `talk_information`
--

CREATE TABLE `talk_information` (
  `user_review_num` int(10) UNSIGNED NOT NULL COMMENT 'AUTO_INCREMENT',
  `user_name` varchar(26) DEFAULT NULL,
  `user_phone` varchar(11) DEFAULT NULL,
  `user_talk` varchar(512) DEFAULT NULL,
  `user_agree` int(10) DEFAULT NULL,
  `user_browser` int(10) DEFAULT NULL,
  `user_talk_img` varchar(512) DEFAULT NULL,
  `talk_time` varchar(110) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- --------------------------------------------------------

--
-- 表的结构 `talk_review_information`
--

CREATE TABLE `talk_review_information` (
  `ID` int(10) UNSIGNED NOT NULL COMMENT 'AUTO_INCREMENT',
  `talk_review` varchar(512) DEFAULT NULL,
  `review_time` varchar(110) DEFAULT NULL,
  `review_imag` varchar(512) DEFAULT NULL,
  `user_name` varchar(26) DEFAULT NULL,
  `user_phone` varchar(11) DEFAULT NULL,
  `review_id` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- --------------------------------------------------------

--
-- 表的结构 `user_information`
--

CREATE TABLE `user_information` (
  `ID` int(10) UNSIGNED NOT NULL,
  `user_name` varchar(26) DEFAULT NULL,
  `user_password` varchar(26) DEFAULT NULL,
  `user_phone` varchar(11) DEFAULT NULL,
  `user_sign` varchar(256) DEFAULT NULL,
  `user_image_head` varchar(256) DEFAULT NULL,
  `user_sex` varchar(10) DEFAULT NULL,
  `user_age` int(11) DEFAULT NULL,
  `user_schoolname` varchar(26) DEFAULT NULL,
  `user_talk` varchar(256) DEFAULT NULL,
  `user_signtime` varchar(110) DEFAULT NULL,
  `user_bg` varchar(256) DEFAULT NULL,
  `user_vip` varchar(256) DEFAULT 'NO'
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

--
-- 转存表中的数据 `user_information`
--

INSERT INTO `user_information` (`ID`, `user_name`, `user_password`, `user_phone`, `user_sign`, `user_image_head`, `user_sex`, `user_age`, `user_schoolname`, `user_talk`, `user_signtime`, `user_bg`, `user_vip`) VALUES
(9, '冰糖雪梨有点甜', 'xbw12138', '17854259542', '嗯，真甜', 'http://115.159.26.120/androidapp/image_head_upload/upload/_17854259542_head.jpg', '男', 21, '清华大学', NULL, '2016-03-11 23:43:59', 'http://115.159.26.120/androidapp/image_bg_upload/upload/_17854259542_bg.jpg', 'NO'),
(11, '刘昊', 'liu428haolh', '17854251719', '我说哈喽，你说皇上吉祥', 'http://115.159.26.120/androidapp/image_head_upload/upload/_17854251719_head.jpg', '男', 20, '斯坦福大学', NULL, '2016-03-30 23:43:34', 'http://115.159.26.120/androidapp/image_bg_upload/upload/_17854251719_bg.jpg', 'YES'),
(13, '冰糖雪梨', 'xbw12138', '17863963884', 'frag,,,,,,,', NULL, '男', 21, '麻省理工大学aa', NULL, '2016-05-01 21:41:34', NULL, 'NO'),
(14, NULL, '123456', '18561749963', NULL, NULL, NULL, NULL, NULL, NULL, '2016-05-16 17:40:42', NULL, 'NO'),
(15, NULL, 'huxiaomiLYC', '13715573769', NULL, NULL, NULL, NULL, NULL, NULL, '2016-05-22 20:05:10', NULL, 'NO'),
(16, '鄢洪均', 'mm612138A', '13658223800', '彼岸', 'http://115.159.26.120/androidapp/image_head_upload/upload/_13658223800_head.jpg', '男', 32, '人民大学', NULL, '2016-08-02 19:19:23', NULL, 'NO'),
(17, NULL, '159357', '18636876332', NULL, NULL, NULL, NULL, NULL, NULL, '2016-11-09 21:45:38', NULL, 'NO');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `shijianfabu`
--
ALTER TABLE `shijianfabu`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `talk_information`
--
ALTER TABLE `talk_information`
  ADD PRIMARY KEY (`user_review_num`);

--
-- Indexes for table `talk_review_information`
--
ALTER TABLE `talk_review_information`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `user_information`
--
ALTER TABLE `user_information`
  ADD PRIMARY KEY (`ID`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `shijianfabu`
--
ALTER TABLE `shijianfabu`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;
--
-- 使用表AUTO_INCREMENT `talk_information`
--
ALTER TABLE `talk_information`
  MODIFY `user_review_num` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT';
--
-- 使用表AUTO_INCREMENT `talk_review_information`
--
ALTER TABLE `talk_review_information`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT';
--
-- 使用表AUTO_INCREMENT `user_information`
--
ALTER TABLE `user_information`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
