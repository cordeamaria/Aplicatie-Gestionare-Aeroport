-- MySQL dump 10.13  Distrib 8.0.39, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: gestionare_aeroport
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `aeronava`
--

DROP TABLE IF EXISTS `aeronava`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aeronava` (
  `id_aeronava` bigint NOT NULL AUTO_INCREMENT,
  `cod_aeronava` varchar(50) NOT NULL,
  `model` varchar(100) DEFAULT NULL,
  `capacitate` int DEFAULT NULL,
  `stare_operationala` enum('activ','in_mentenanta','retras') DEFAULT 'activ',
  `locatie_curenta` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id_aeronava`),
  UNIQUE KEY `cod_aeronava` (`cod_aeronava`),
  CONSTRAINT `aeronava_chk_1` CHECK ((`capacitate` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aeronava`
--

LOCK TABLES `aeronava` WRITE;
/*!40000 ALTER TABLE `aeronava` DISABLE KEYS */;
/*!40000 ALTER TABLE `aeronava` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bagaj`
--

DROP TABLE IF EXISTS `bagaj`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bagaj` (
  `id_bagaj` bigint NOT NULL AUTO_INCREMENT,
  `id_bilet` bigint NOT NULL,
  `greutate` decimal(6,2) DEFAULT '0.00',
  `tip` enum('mana','cala') DEFAULT 'cala',
  `status_checkin` enum('nepredat','predat','incarcat','livrat','pierdut') DEFAULT 'nepredat',
  `eticheta` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_bagaj`),
  KEY `idx_bagaj_bilet` (`id_bilet`),
  CONSTRAINT `fk_bagaj_bilet` FOREIGN KEY (`id_bilet`) REFERENCES `bilet` (`id_bilet`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bagaj`
--

LOCK TABLES `bagaj` WRITE;
/*!40000 ALTER TABLE `bagaj` DISABLE KEYS */;
/*!40000 ALTER TABLE `bagaj` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bilet`
--

DROP TABLE IF EXISTS `bilet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bilet` (
  `id_bilet` bigint NOT NULL AUTO_INCREMENT,
  `id_zbor` bigint NOT NULL,
  `id_pasager` bigint NOT NULL,
  `loc` varchar(10) DEFAULT NULL,
  `clasa` enum('economy','business','first') DEFAULT 'economy',
  `pret` decimal(10,2) DEFAULT '0.00',
  `data_cumparare` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status_plata` enum('platit','neplatit','anulat') DEFAULT 'platit',
  PRIMARY KEY (`id_bilet`),
  UNIQUE KEY `id_zbor` (`id_zbor`,`loc`),
  KEY `idx_bilet_pasager` (`id_pasager`),
  CONSTRAINT `fk_bilet_pasager` FOREIGN KEY (`id_pasager`) REFERENCES `user` (`id_user`) ON DELETE CASCADE,
  CONSTRAINT `fk_bilet_zbor` FOREIGN KEY (`id_zbor`) REFERENCES `zbor` (`id_zbor`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bilet`
--

LOCK TABLES `bilet` WRITE;
/*!40000 ALTER TABLE `bilet` DISABLE KEYS */;
/*!40000 ALTER TABLE `bilet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crew_assignment`
--

DROP TABLE IF EXISTS `crew_assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crew_assignment` (
  `id_assignment` bigint NOT NULL AUTO_INCREMENT,
  `id_user` bigint NOT NULL,
  `id_zbor` bigint NOT NULL,
  `rol_in_zbor` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_assignment`),
  UNIQUE KEY `id_user` (`id_user`,`id_zbor`,`rol_in_zbor`),
  KEY `idx_crew_zbor` (`id_zbor`),
  CONSTRAINT `fk_crew_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE,
  CONSTRAINT `fk_crew_zbor` FOREIGN KEY (`id_zbor`) REFERENCES `zbor` (`id_zbor`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crew_assignment`
--

LOCK TABLES `crew_assignment` WRITE;
/*!40000 ALTER TABLE `crew_assignment` DISABLE KEYS */;
/*!40000 ALTER TABLE `crew_assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problema`
--

DROP TABLE IF EXISTS `problema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `problema` (
  `id_problema` bigint NOT NULL AUTO_INCREMENT,
  `id_user_reporter` bigint DEFAULT NULL,
  `descriere` text NOT NULL,
  `status` enum('noua','in_analiza','rezolvata','inchisa') DEFAULT 'noua',
  `data_raportare` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `data_rezolvare` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_problema`),
  KEY `fk_problema_user` (`id_user_reporter`),
  KEY `idx_problema_status` (`status`),
  CONSTRAINT `fk_problema_user` FOREIGN KEY (`id_user_reporter`) REFERENCES `user` (`id_user`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problema`
--

LOCK TABLES `problema` WRITE;
/*!40000 ALTER TABLE `problema` DISABLE KEYS */;
/*!40000 ALTER TABLE `problema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `id_task` bigint NOT NULL AUTO_INCREMENT,
  `id_muncitor` bigint DEFAULT NULL,
  `descriere` text,
  `status` enum('in_asteptare','in_desfasurare','completat','anulat') DEFAULT 'in_asteptare',
  `data_alocare` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `data_finalizare` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_task`),
  KEY `idx_task_muncitor` (`id_muncitor`),
  CONSTRAINT `fk_task_muncitor` FOREIGN KEY (`id_muncitor`) REFERENCES `user` (`id_user`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id_user` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL,
  `parola` varchar(255) NOT NULL,
  `rol` enum('Pasager','Muncitor','Dispecer','Administrator','Pilot','Stewardesa') DEFAULT 'Pasager',
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zbor`
--

DROP TABLE IF EXISTS `zbor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zbor` (
  `id_zbor` bigint NOT NULL AUTO_INCREMENT,
  `cod_zbor` varchar(50) NOT NULL,
  `id_aeronava` bigint DEFAULT NULL,
  `plecare_din` varchar(100) NOT NULL,
  `destinatie` varchar(100) NOT NULL,
  `data_plecare` datetime NOT NULL,
  `data_sosire` datetime NOT NULL,
  `status_zbor` enum('programat','imbarcare','decolat','in_cursa','aterizat','anulat') DEFAULT 'programat',
  `echipamente_sol` text,
  `nr_echipaj_bord` int DEFAULT '0',
  `nr_pasageri_estimat` int DEFAULT '0',
  `id_dispecer` bigint DEFAULT NULL,
  PRIMARY KEY (`id_zbor`),
  UNIQUE KEY `cod_zbor` (`cod_zbor`),
  KEY `fk_zbor_aeronava` (`id_aeronava`),
  CONSTRAINT `fk_zbor_aeronava` FOREIGN KEY (`id_aeronava`) REFERENCES `aeronava` (`id_aeronava`) ON DELETE SET NULL,
  CONSTRAINT `chk_zbor_date` CHECK ((`data_sosire` >= `data_plecare`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zbor`
--

LOCK TABLES `zbor` WRITE;
/*!40000 ALTER TABLE `zbor` DISABLE KEYS */;
/*!40000 ALTER TABLE `zbor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-04 18:21:30
