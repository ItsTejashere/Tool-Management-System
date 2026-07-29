-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 29, 2026 at 12:12 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tool_management`
--

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE `department` (
  `department_id` int(11) NOT NULL,
  `department_name` varchar(100) NOT NULL,
  `plant_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`department_id`, `department_name`, `plant_id`) VALUES
(1, 'HUSCO', 1),
(2, 'NYB ', 1);

-- --------------------------------------------------------

--
-- Table structure for table `machine`
--

CREATE TABLE `machine` (
  `machine_id` int(11) NOT NULL,
  `machine_name` varchar(100) DEFAULT NULL,
  `department_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `machine`
--

INSERT INTO `machine` (`machine_id`, `machine_name`, `department_id`) VALUES
(1, 'HMC1', 1),
(2, 'HMC2', 2);

-- --------------------------------------------------------

--
-- Table structure for table `plant`
--

CREATE TABLE `plant` (
  `plant_id` int(11) NOT NULL,
  `plant_name` varchar(100) DEFAULT NULL,
  `image_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `plant`
--

INSERT INTO `plant` (`plant_id`, `plant_name`, `image_name`) VALUES
(1, 'Technovision', 'tvm.png'),
(2, 'Pragati', 'pragati.png'),
(3, 'Netmech', 'netmech.png'),
(4, 'Et', 'et.png'),
(5, 'Et2', 'et.png');

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

CREATE TABLE `project` (
  `project_id` int(11) NOT NULL,
  `project_name` varchar(100) DEFAULT NULL,
  `department_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `project`
--

INSERT INTO `project` (`project_id`, `project_name`, `department_id`) VALUES
(1, 'MX105-D', 1),
(2, 'MX107-D', 1),
(3, 'MX105-A', 1),
(4, 'MX187-E\r\n', 1),
(5, 'MX185-E\r\n', 1),
(6, 'MX107-B\r\n', 1),
(7, '92046\r\n', 1),
(8, 'FT107-C\r\n', 1),
(9, 'FT107-D\r\n', 1),
(10, '6601\r\n', 1),
(11, '6001\r\n', 1),
(12, 'ST50-M1B1', 1),
(13, '5614', 1),
(14, '5616', 1);

-- --------------------------------------------------------

--
-- Table structure for table `tool`
--

CREATE TABLE `tool` (
  `tool_id` int(11) NOT NULL,
  `tool_code` varchar(100) DEFAULT NULL,
  `tool_name` varchar(150) DEFAULT NULL,
  `minimum_quantity` int(11) DEFAULT NULL,
  `total_quantity` int(11) DEFAULT NULL,
  `storage_location` varchar(100) DEFAULT NULL,
  `status` enum('AVAILABLE','IN_USE','SHARPENING','DAMAGED','UNAVAILABLE') DEFAULT 'AVAILABLE',
  `project_id` int(11) DEFAULT NULL,
  `drawing_number` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tool`
--

INSERT INTO `tool` (`tool_id`, `tool_code`, `tool_name`, `minimum_quantity`, `total_quantity`, `storage_location`, `status`, `project_id`, `drawing_number`) VALUES
(145, 'TOOL008480', 'SCTC STEP DRILLØ19.67XSL16.19XFL50XOAL120XSHØ25h6', 1, 0, 'R25', 'UNAVAILABLE', 1, 'VP_103_4047'),
(146, 'TOOL008301', 'TC BRAZED STEP HOLEMILLØ20.17XSL16.28X∅33.836XFL50', 1, 0, 'R25', 'UNAVAILABLE', 1, 'VP_402_627'),
(147, 'TOOL008479', 'SCTC STEP DRILLØ12.11XSL12.13XFL50XOAL105XSHØ16h6', 1, 0, 'R25', 'UNAVAILABLE', 1, 'VP_103_4046'),
(148, 'TOOL008481', 'SCTC STEP HOLEMILLØ12.61XSL12.22XØ24.31XFL50XOAL105XSHØ25h6', 1, 0, 'R25', 'UNAVAILABLE', 1, 'VP_402_739'),
(149, 'TOOL008299', 'SCNTC ENDMILLØ13XCL25OAL75XSH14', 1, 0, 'R25', 'UNAVAILABLE', 1, 'VP_201_8162'),
(150, 'TOOL008464', 'SCTC STEP DRILLØ4.53XSL18XØ5.06XSL6.45XFL40 XOAL78XSH8', 1, 0, 'R25', 'UNAVAILABLE', 1, 'VP_103_3222'),
(151, 'TOOL006516', 'SCTC DRILLØ4.5XFL100XOAL130XSH6', 2, 0, 'R26', 'UNAVAILABLE', 1, 'VP_101_7066A'),
(152, 'TOOL008468', 'SCNTC ENDMILLØ20XCL75XOAL120XSHØ20h6', 1, 0, 'R26', 'UNAVAILABLE', 1, 'VP_201_10311'),
(153, 'TOOL008252', 'SCTC DRILLØ9XFL55XOAL120XSH10', 1, 0, 'R26', 'UNAVAILABLE', 1, 'VP_101_7059'),
(154, 'TOOL008474', 'SCTC HOLEMILL CUM REAMERØ9.55XSL4X∅9.655XCL30XOAL105XSH10', 1, 0, 'R26', 'UNAVAILABLE', 1, 'VP_302_839'),
(155, 'TOOL008302', 'SCTC STEP REAMERØ20.475XSL16.33X∅34.136XFL50XOAL150XSH∅25h6', 1, 0, 'R26', 'UNAVAILABLE', 1, 'VP-302-886A'),
(156, '', 'BRAZED TC FINISH PORT CUTTERØ24.92XSL18.56X∅41.28XFL40XOAL90XSH∅25h6', 1, 0, 'R26', 'UNAVAILABLE', 1, 'VP_501_5565'),
(157, 'TOOL008482', 'SCTC STEP REAMERØ12.914XSL12.235XØ24.612XFL50XOAL105XSHØ25h6', 1, 0, 'R27', 'UNAVAILABLE', 1, 'VP_302_1010'),
(158, 'TOOL008465', 'SCNTC CHAMFER TOOLØ16X90°XFL50XOAL120XSHØ16h6', 1, 0, 'R27', 'UNAVAILABLE', 1, 'VP_501_6238'),
(159, 'TOOL006512', 'SCNTC ROUGH PROFILE TOOLØ6XR0.281', 2, 0, 'R27', 'UNAVAILABLE', 1, 'VP_501_4285B'),
(160, 'TOOL006511/8702', 'SCNTC FINISH PROFILE TOOLØ5XR0.381', 1, 0, 'R27', 'UNAVAILABLE', 1, 'VP_501_4284B'),
(161, '', 'SCNTC BRAZED ROUGH TREPANNING TOOLØ34XFL20XOAL65XSH∅25h6', 1, 0, 'R27', 'UNAVAILABLE', 1, 'VP_501_5532'),
(162, '', 'SCNTC BRAZED FINISH TREPANNING TOOLØ35XFL20XOAL65XSH∅25h6', 1, 0, 'R27', 'UNAVAILABLE', 1, 'VP_501_5531'),
(163, 'TOOL006507', 'SCNTC ENDMILL∅4XCL5XOAL50XSH∅4h6', 1, 0, 'R28', 'UNAVAILABLE', 1, 'VP_201_9108'),
(164, 'TOOL008472', 'SCTC DRILLØ3.5XFL45XOAL65XSH∅6h6', 1, 0, 'R28', 'UNAVAILABLE', 1, 'VP_101_7061'),
(165, 'TOOL006508', 'SCNTC DRILLØ1.6XFL7XOAL65XSH∅4h6', 1, 0, 'R28', 'UNAVAILABLE', 1, 'VP_102_9260'),
(166, 'TOOL008473/8476', 'SCTC DRILLØ6.8XFL30XOAL65XSHØ8h6', 1, 0, 'R28', 'UNAVAILABLE', 1, 'VP_101_8857'),
(167, 'TOOL008478', 'SCTC STEP DRILLØ11XSL16XFL35XOAL75XSHØ14h6', 1, 0, 'R28', 'SHARPENING', 1, 'VP_103_3794'),
(168, '', 'SCTC CORE STEP DRILLØ17.95XSL9.96X∅20.175XSL27.02X∅33.84XFL60XOAL107XSH∅25h6', 1, 0, 'R28', 'UNAVAILABLE', 1, 'VP_103_3642'),
(169, '', 'SCTC PORT CUTTERØ18.25XSL12.32X∅20.475XSL27.21X∅34.14XFL60XOAL107XSH∅25h6', 1, 0, 'R29', 'UNAVAILABLE', 1, 'VP_501_5706'),
(170, 'TOOL008314', 'SCTC STEP REAMERØ18.25XSL2X∅18.648XFL60XOAL107XSH∅20h6', 1, 0, 'R29', 'UNAVAILABLE', 1, 'VP_302_906'),
(171, 'TOOL008469', 'SCNTC ENDMILLØ5.03XFL10XOAL105XSHØ6h6', 1, 0, 'R29', 'UNAVAILABLE', 1, 'VP_201_9184'),
(172, 'TOOL008309', 'SCTC PILOT DRILLØ6.02XFL25XOAL130XSH∅6h6', 1, 0, 'R29', 'UNAVAILABLE', 1, 'VP_101_7813'),
(173, 'TOOL008310', 'SCTC LONG DRILLØ6XFL125XOAL165XSH∅8h6', 1, 0, 'R29', 'UNAVAILABLE', 1, 'VP_101_7814'),
(174, '', 'SCTC DRILLØ3.5XFL45XOAL90XSH4', 1, 0, 'R29', 'UNAVAILABLE', 1, 'VP_101_7061'),
(175, '', 'SCTC DRILL 3.53XFL10XNECKXOAL75XSH4-D2', 1, 0, 'R30', 'UNAVAILABLE', 1, 'VP_101_8358'),
(176, '', 'SCTC RPT 19.8XSL15.03X24.5XFL30XOAL75XSH25-D2', 1, 0, 'R30', 'UNAVAILABLE', 1, ''),
(177, '', 'SCTC FPT 20.52XSL14.93X24.5CFL35XOAL75XSH25-D2', 1, 0, 'R30', 'UNAVAILABLE', 1, ''),
(178, '', 'SCTC DRILLØ23.7XFL50XOAL100XSHØ25h6-D2', 1, 0, 'R30', 'UNAVAILABLE', 1, 'VP_101_8310'),
(179, '', 'SCTC ROUGH PROFILE TOOLØ14.2XSL12.33XØ27.7XFL40XOAL90XSHØ25h6-D2', 1, 0, 'R30', 'UNAVAILABLE', 1, 'VP_501_5937'),
(180, '', 'SCTC FINISH PROFILE TOOLØ14.5XSL12.42XØ28XFL40XOAL90XSHØ25h6-D2', 1, 0, 'R30', 'UNAVAILABLE', 1, 'VP_501_5936'),
(181, '', 'TC BRAZED STEP HOLEMILLØ24.62XSL17.2XØ40.98XFL40XOAL90XSHØ25h6-D2', 1, 0, 'R31', 'UNAVAILABLE', 1, 'VP_402_705'),
(182, '', 'TC BRAZED FINISH PROFILE TOOLØ25XSL17.859XØ40.5XFL50XOAL100XSHØ25h6-D2', 1, 0, 'R31', 'UNAVAILABLE', 1, 'VP_501_5932'),
(183, '', 'SCTC DRILLØ9.525XFL50XOAL90XSHØ10h6-D3', 1, 0, 'R31', 'UNAVAILABLE', 1, 'VP_101_8808'),
(184, '', 'SCTC STEP HOLEMILLØ9.63XSL6.525XØ20.17XSL12.99XØ28.275XFL45XOAL100XSHØ25h6-D3', 1, 0, 'R31', 'UNAVAILABLE', 1, 'VP_402_736'),
(185, '', 'SCTC STEP REAMERØ9.93XSL6.47XØ20.47XSL13.08XØ28.575XFL45XOAL100XSHØ25h6-D3', 1, 0, 'R31', 'UNAVAILABLE', 1, 'VP_302_1005'),
(186, '', 'SCTC STEP DRILLØ9.13XSL5.88XØ19.67XSL12.84XFL45XOAL100XSHØ25h6-D3', 1, 0, 'R31', 'UNAVAILABLE', 1, 'VP_103_4024'),
(187, '', 'SCTC 4.53XSL9XDIA5.05XSL6.5XFL30XOAL70XSH8-D3', 2, 0, '', 'UNAVAILABLE', 1, ''),
(188, 'TOOL005399', 'DRILL-DIA.9XFL55XOAL120SH10H6', 3, 0, 'R32', 'UNAVAILABLE', 2, 'VP-101-7059'),
(189, 'TOOL008474/8115', 'RM-DIA9.5XSL4X9.655XCL30XOAL120SH10H6', 2, 0, 'R32', 'UNAVAILABLE', 2, 'VP-302-839/1018'),
(190, 'TOOL008321/8123', 'DRILL-DIA3.5XFL45XOAL90SH4H6', 1, 0, 'R32', 'UNAVAILABLE', 2, 'VP-101-7061'),
(191, 'TOOL008111', 'DIA14.2XSL14.0X15.36XSL16.41X...XOAL100XSH18H6', 1, 0, 'R32', 'UNAVAILABLE', 2, 'VP-103-3461'),
(192, 'TOOL008299', 'END MILL -DIA13XFL25XOAL75SH14H6 3XCH FACE', 1, 0, 'R32', 'UNAVAILABLE', 2, 'VP-201-8162'),
(193, 'TOOL008118', 'STEP DRILL -DIA12XSL3.73X12.23XSL6.07X14.23XSL10.39X15.85XSL6.26XFL50X22XOAL100XSH20H6', 1, 0, 'R32', 'UNAVAILABLE', 2, 'VP-103-3843'),
(194, 'TOOL005426', 'SHM -DIA12.7XSL7.95X14.6XSL10.37X...FL45XOAL100SH20H6', 2, 0, 'R33', 'UNAVAILABLE', 2, 'VP-402-595A'),
(195, 'TOOL005415/6944', 'SD -DIA3.3XSL9.15XFL25XOAL75X5H6', 1, 0, 'R33', 'UNAVAILABLE', 2, 'VP-103-3225'),
(196, 'TOOL005411/8464', '-DIA4.53XSL18.5X5.06SL6.5FL40XOAL78XSH8H6', 1, 0, 'R33', 'UNAVAILABLE', 2, 'VP-103-3222'),
(197, 'TOOL006516', 'LONG DRILL-DIA4.5XFL90XOAL130XSH6H6', 1, 0, 'R33', 'UNAVAILABLE', 2, 'VP_101_7066'),
(198, 'TOOL005402', 'DRILL-DIA7XFL45XOAL90SH8H6', 1, 0, 'R33', 'UNAVAILABLE', 2, 'VP-101-7062'),
(199, 'TOOL005402', 'LONG DRILL -DIA7XFL105XOAL150XSH8H6', 1, 0, 'R33', 'UNAVAILABLE', 2, 'VP-101-7069'),
(200, 'TOOL008084', 'SCTC STEP DRILLØ15.2XSL8.49XØ16.8XSL14.94XØ19.77XSL17.58XØ23.5XFL65XOAL100XSHØ25h6', 1, 0, 'R34', 'UNAVAILABLE', 2, 'VP_103_4013'),
(201, 'TOOL008087', 'TC BRAZED STEP HOLEMILLØ15.6XSL8.83XØ17.2XSL14.64XØ20.47XSL17.83XØ30.18XFL65XOAL120XSHØ25h6', 1, 0, 'R34', 'UNAVAILABLE', 2, 'VP_402_730'),
(202, 'TOOL008083', 'SCTC STEP REAMERØ15.905XSL8.64XØ17.505XFL65XOAL100XSHØ18h6', 1, 0, 'R34', 'UNAVAILABLE', 2, 'VP_302_999'),
(203, '', 'FLY BRIZING CUTTER -DIA17X35XSH14(WCDIA17-W3-100-DIA14H6', 1, 0, 'R34', 'UNAVAILABLE', 2, 'RT-710-0386'),
(204, 'TOOL005407', 'DRILL-DIA10.39XFL60XOAL120XSH12H6', 1, 0, 'R34', 'UNAVAILABLE', 2, 'VP-101-7067'),
(205, 'TOOL008082', 'SCTC STEP DRILLØ12.44XSL14.49XFL40XOAL100XSHØ16h6', 1, 0, 'R34', 'UNAVAILABLE', 2, 'VP_103_4014'),
(206, 'TOOL008080', 'SCNTC ENDMILLØ21.5XCL30XOAL100XSHØ20h6', 1, 0, 'R35', 'UNAVAILABLE', 2, 'VP_201_10194'),
(207, 'TOOL008086', 'TC BRAZED STEP HOLEMILLØ12.94XSL16.89XØ24.61XFL40XOAL120XSHØ25h6', 1, 0, 'R35', 'UNAVAILABLE', 2, 'VP_402_731'),
(208, 'TOOL008504', 'SCTC STEP REAMERØ12.9XSL7.57XØ14.9XSL10.39Ø16.75XSL6.17XFL45XØ23XOAL100XSH20H6', 1, 0, 'R35', 'UNAVAILABLE', 2, 'VP-302-1012'),
(209, '', 'TC BRAZED STEP REAMERØ13.132XSL7.57XØ15.132XSL10.39XØ16.75XSL6.17XFL45XØ23XOAL100XSHØ20h6', 1, 0, 'R35', 'UNAVAILABLE', 2, 'VP-302-1000'),
(210, 'TOOL008088', 'TC BRAZED STEP HOLEMILLØ15.17XSL15.22XØ26.97XFL35XOAL120XSHØ25h6', 1, 0, 'R35', 'UNAVAILABLE', 2, 'VP_402_732'),
(211, 'TOOL008496', 'SCTC DRILLØ5XFL30XOAL100XSHØ6h6', 1, 0, 'R35', 'UNAVAILABLE', 2, 'VP_101_8819'),
(212, 'TOOL008496', 'SCTC F DRILLØ5XFL30XOAL100XSHØ6h6', 1, 0, 'R36', 'UNAVAILABLE', 2, 'VP_101_8812'),
(213, 'TOOL008453', 'SCTC DRILLØ4XFL30XOAL100XSHØ6h6', 1, 0, 'R36', 'UNAVAILABLE', 2, 'VP_101_8814'),
(214, 'TOOL008081', 'SCTC STEP DRILLØ11.17XSL13.19XØ23.32XFL35XOAL78XSHØ25h6', 1, 0, 'R36', 'UNAVAILABLE', 2, 'VP_103_4015'),
(215, 'TOOL008453', 'SCTC FLAT DRILLØ4XFL30XOAL100XSHØ6h6', 1, 0, 'R36', 'UNAVAILABLE', 2, 'VP_101_8820'),
(216, 'TOOL008085', 'TC BRAZED STEP HOLEMILLØ11.67XSL13.37XØ23.82XFL35XOAL80XSHØ25h6', 1, 0, 'R36', 'UNAVAILABLE', 2, 'VP_402_733'),
(217, 'TOOL008134', 'SCTC STEP DRILLØ5.53XSL15X∅6.06XSL7.43XFL40XOAL78XSH∅8h6', 1, 0, 'R36', 'UNAVAILABLE', 2, 'VP_103_3582'),
(218, 'TOOL008455', 'LONG STEP DRILL -DIA5.0XSL8X5.5XFL100XOAL150XSH6H6', 1, 0, 'R37', 'UNAVAILABLE', 2, 'VP-103_3224'),
(219, 'TOOL008456', 'DRILL -DIA12.5FL70XOAL120XSH14H6', 1, 0, 'R37', 'UNAVAILABLE', 2, 'VP-101-7065'),
(220, 'TOOL008457', 'STEP DRILL -DIA18.4SL19.8X20.58XFL40XOAL90XSH20H6', 1, 0, 'R37', 'UNAVAILABLE', 2, 'VP-103-3228'),
(221, 'TOOL006518', 'SCTC STEP DRILLØ18.4XSL16.08XØ22XFL40XOAL90XSH20', 1, 0, 'R37', 'UNAVAILABLE', 2, 'VP_VP_103_3228'),
(222, '', 'TC BRAZED STEP HOLEMILLØ18.9XSL16.26XØ31.75XFL40XOAL120XSHØ25h6', 1, 0, 'R37', 'UNAVAILABLE', 2, 'VP_402_734'),
(223, 'TOOL008458/8738', 'SCTC STEP DRILLØ6.8XSL11.5XFL30 XOAL75XSH10', 1, 0, 'R37', 'UNAVAILABLE', 2, 'VP_103_3226'),
(224, 'TOOL008459', 'SCNTC CHAMFER TOOLØ16X90°XFL25XOAL120XSHØ16h6', 1, 0, 'R38', 'UNAVAILABLE', 2, 'VP_501_6238'),
(225, 'TOOL006506', 'SCNTC ENDMILLØ8XCL30XOAL75XSH∅8h6', 1, 0, 'R38', 'UNAVAILABLE', 2, 'VP_201_9440'),
(226, 'TOOL008460', 'PROFILE ENDMILLØ16XFL55XOAL95X∅16h6', 1, 0, 'R38', 'UNAVAILABLE', 2, 'VP201-10313'),
(227, 'TOOL008479', 'SCTC STEP DRILLØ12.11XSL12.13XFL50XOAL105XSHØ16h6-D4', 1, 0, 'R38', 'UNAVAILABLE', 2, 'VP_103_4046'),
(228, 'TOOL008461/8485', 'SCTC STEP HOLEMILLØ12.61XSL12.22XØ24.31XFL50XOAL105XSHØ25h6-D4', 1, 0, 'R38', 'UNAVAILABLE', 2, 'VP_402_739'),
(229, 'TOOL008462/8486', 'SCTC STEP REAMERØ12.914XSL12.235XØ24.612XFL50XOAL105XSHØ25h6-D4', 1, 0, 'R38', 'UNAVAILABLE', 2, 'VP_302_1010'),
(230, 'TOOL008463', 'DRILL 5.0X132X170XSH6H6-D4', 2, 0, 'R38', 'UNAVAILABLE', 2, 'VP_101_8222');

-- --------------------------------------------------------

--
-- Table structure for table `tool_instance`
--

CREATE TABLE `tool_instance` (
  `instance_id` int(11) NOT NULL,
  `tool_id` int(11) NOT NULL,
  `serial_number` varchar(100) NOT NULL,
  `current_status` varchar(50) DEFAULT 'AVAILABLE',
  `current_machine_id` int(11) DEFAULT NULL,
  `current_project_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tool_movement`
--

CREATE TABLE `tool_movement` (
  `movement_id` int(11) NOT NULL,
  `tool_id` int(11) NOT NULL,
  `machine_id` int(11) DEFAULT NULL,
  `project_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `involved_serials` varchar(500) DEFAULT NULL,
  `movement_type` enum('STOCK_IN','ISSUE','RETURN','SHARPEN_OUT','SHARPEN_IN','SCRAP') DEFAULT NULL,
  `movement_date` datetime DEFAULT current_timestamp(),
  `remarks` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tool_movement`
--

INSERT INTO `tool_movement` (`movement_id`, `tool_id`, `machine_id`, `project_id`, `quantity`, `involved_serials`, `movement_type`, `movement_date`, `remarks`) VALUES
(69, 226, NULL, NULL, 1, '1', 'STOCK_IN', '2026-07-26 17:54:59', '');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('INVENTORY','VIEWER') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`) VALUES
(1, 'Inayat@123', 'qa@123', 'INVENTORY'),
(2, 'Operator', '12345', 'VIEWER');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`department_id`),
  ADD KEY `plant_id` (`plant_id`);

--
-- Indexes for table `machine`
--
ALTER TABLE `machine`
  ADD PRIMARY KEY (`machine_id`);

--
-- Indexes for table `plant`
--
ALTER TABLE `plant`
  ADD PRIMARY KEY (`plant_id`);

--
-- Indexes for table `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`project_id`),
  ADD KEY `fk_project_department` (`department_id`);

--
-- Indexes for table `tool`
--
ALTER TABLE `tool`
  ADD PRIMARY KEY (`tool_id`),
  ADD KEY `fk_tool_project` (`project_id`);

--
-- Indexes for table `tool_instance`
--
ALTER TABLE `tool_instance`
  ADD PRIMARY KEY (`instance_id`),
  ADD UNIQUE KEY `serial_number` (`serial_number`),
  ADD UNIQUE KEY `serial_number_2` (`serial_number`),
  ADD KEY `tool_id` (`tool_id`);

--
-- Indexes for table `tool_movement`
--
ALTER TABLE `tool_movement`
  ADD PRIMARY KEY (`movement_id`),
  ADD KEY `tool_id` (`tool_id`),
  ADD KEY `machine_id` (`machine_id`),
  ADD KEY `project_id` (`project_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `department`
--
ALTER TABLE `department`
  MODIFY `department_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `machine`
--
ALTER TABLE `machine`
  MODIFY `machine_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `plant`
--
ALTER TABLE `plant`
  MODIFY `plant_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `project`
--
ALTER TABLE `project`
  MODIFY `project_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `tool`
--
ALTER TABLE `tool`
  MODIFY `tool_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=231;

--
-- AUTO_INCREMENT for table `tool_instance`
--
ALTER TABLE `tool_instance`
  MODIFY `instance_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT for table `tool_movement`
--
ALTER TABLE `tool_movement`
  MODIFY `movement_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=88;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `department`
--
ALTER TABLE `department`
  ADD CONSTRAINT `department_ibfk_1` FOREIGN KEY (`plant_id`) REFERENCES `plant` (`plant_id`) ON DELETE CASCADE;

--
-- Constraints for table `project`
--
ALTER TABLE `project`
  ADD CONSTRAINT `fk_project_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE SET NULL;

--
-- Constraints for table `tool`
--
ALTER TABLE `tool`
  ADD CONSTRAINT `fk_tool_project` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE SET NULL;

--
-- Constraints for table `tool_instance`
--
ALTER TABLE `tool_instance`
  ADD CONSTRAINT `tool_instance_ibfk_1` FOREIGN KEY (`tool_id`) REFERENCES `tool` (`tool_id`) ON DELETE CASCADE;

--
-- Constraints for table `tool_movement`
--
ALTER TABLE `tool_movement`
  ADD CONSTRAINT `tool_movement_ibfk_1` FOREIGN KEY (`tool_id`) REFERENCES `tool` (`tool_id`),
  ADD CONSTRAINT `tool_movement_ibfk_2` FOREIGN KEY (`machine_id`) REFERENCES `machine` (`machine_id`),
  ADD CONSTRAINT `tool_movement_ibfk_3` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
