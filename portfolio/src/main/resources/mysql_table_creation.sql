CREATE DATABASE `portfolio`;

USE `portfolio`;

CREATE TABLE `daily_quotes` (
  `id` int NOT NULL,
  `ticker` varchar(10) NOT NULL ,
  `date` DATE NOT NULL ,
  `value` DECIMAL NOT NULL ,
  PRIMARY KEY (`id`)
);