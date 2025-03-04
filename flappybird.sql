-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 26, 2025 at 09:05 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `flappybird`
--

-- --------------------------------------------------------

--
-- Table structure for table `single_player_scores`
--

CREATE TABLE `single_player_scores` (
  `Attempt_id` int(11) NOT NULL,
  `PlayerName` varchar(255) NOT NULL,
  `Score` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `single_player_scores`
--

INSERT INTO `single_player_scores` (`Attempt_id`, `PlayerName`, `Score`) VALUES
(1, 'abc', 4),
(2, 'abc', 2),
(3, 'abc', 5),
(4, 'Joshi', 1),
(5, 'Joshi', 0),
(6, 'Joshi', 28),
(7, 'Joshi', 7),
(8, 'JoshiMinh', 16),
(9, 'JoshiMinh', 1),
(10, 'JoshiMinh', 5);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `single_player_scores`
--
ALTER TABLE `single_player_scores`
  ADD PRIMARY KEY (`Attempt_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `single_player_scores`
--
ALTER TABLE `single_player_scores`
  MODIFY `Attempt_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
