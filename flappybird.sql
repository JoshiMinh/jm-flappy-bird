SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";
SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS `flappybird`;
USE `flappybird`;

CREATE TABLE `single_player_scores` (
  `Attempt_id` int(11) NOT NULL AUTO_INCREMENT,
  `PlayerName` varchar(255) NOT NULL,
  `Score` int(11) NOT NULL,
  PRIMARY KEY (`Attempt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

COMMIT;