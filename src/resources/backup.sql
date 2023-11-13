-- RUN THIS QUERY TO CREATE A DATABASE NAMED "BankSystem" in MYSQL
-- AND ADD TABLES "user" AND "accounts" TO IT;
-- ------------------------------------------------------
-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
-- Host: localhost    Database: BankSystem
-- Server version	8.0.33
-- ------------------------------------------------------
--
-- Create Database
CREATE DATABASE IF NOT EXISTS BankSystem;
--
USE BankSystem;
--
-- Table structure for table `accounts`
DROP TABLE IF EXISTS `accounts`;
--
CREATE TABLE `accounts` (
    `account_number` bigint NOT NULL,
    `full_name` varchar(255) NOT NULL,
    `email` varchar(255) NOT NULL,
    `balance` decimal(10, 2) NOT NULL,
    `security_pin` char(4) NOT NULL,
    PRIMARY KEY (`account_number`),
    UNIQUE KEY `email` (`email`)
);
--
-- Table structure for table `users`
DROP TABLE IF EXISTS `users`;
--
CREATE TABLE `users` (
    `full_name` varchar(255) NOT NULL,
    `email` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    PRIMARY KEY (`email`)
);