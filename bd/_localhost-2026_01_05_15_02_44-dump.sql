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
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cod_aeronava` varchar(50) NOT NULL,
  `model` varchar(100) DEFAULT NULL,
  `capacitate` int DEFAULT NULL,
  `stare_operationala` enum('activ','in_mentenanta','retras') DEFAULT 'activ',
  `locatie_curenta` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cod_aeronava` (`cod_aeronava`),
  CONSTRAINT `aeronava_chk_1` CHECK ((`capacitate` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aeronava`
--

LOCK TABLES `aeronava` WRITE;
/*!40000 ALTER TABLE `aeronava` DISABLE KEYS */;
INSERT INTO `aeronava` VALUES (1,'A001','Boeing 737',180,'activ','Bucuresti'),(2,'A002','Boeing 737',180,'activ','Londra'),(3,'A003','Airbus A320',150,'in_mentenanta','Hangar 1'),(4,'A004','Airbus A320',150,'activ','Paris'),(5,'A005','Boeing 747',400,'activ','New York'),(6,'A006','Embraer 190',100,'activ','Cluj'),(7,'A007','Bombardier CRJ',90,'retras','Hangar 2'),(8,'A008','Airbus A350',300,'activ','Dubai'),(9,'A009','Boeing 777',350,'in_mentenanta','Hangar 3'),(10,'A010','ATR 72',70,'activ','Iasi');
/*!40000 ALTER TABLE `aeronava` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bagaj`
--

DROP TABLE IF EXISTS `bagaj`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bagaj` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_bilet` bigint NOT NULL,
  `greutate` decimal(6,2) DEFAULT '0.00',
  `tip` enum('mana','cala') DEFAULT 'cala',
  `status_checkin` enum('nepredat','predat','incarcat','livrat','pierdut') DEFAULT 'nepredat',
  `eticheta` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_bagaj_bilet` (`id_bilet`),
  CONSTRAINT `fk_bagaj_bilet` FOREIGN KEY (`id_bilet`) REFERENCES `bilet` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bagaj`
--

LOCK TABLES `bagaj` WRITE;
/*!40000 ALTER TABLE `bagaj` DISABLE KEYS */;
INSERT INTO `bagaj` VALUES (1,1,22.50,'cala','incarcat','TAG1001'),(2,1,8.00,'mana','nepredat','TAG1002'),(3,2,15.00,'cala','predat','TAG1003'),(4,3,20.00,'cala','livrat','TAG1004'),(5,5,30.00,'cala','incarcat','TAG1005'),(6,6,12.00,'mana','nepredat','TAG1006'),(7,7,25.00,'cala','pierdut','TAG1007'),(8,8,10.00,'cala','livrat','TAG1008'),(9,9,5.00,'mana','nepredat','TAG1009'),(10,10,23.00,'cala','predat','TAG1010');
/*!40000 ALTER TABLE `bagaj` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bilet`
--

DROP TABLE IF EXISTS `bilet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bilet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_zbor` bigint NOT NULL,
  `id_pasager` bigint NOT NULL,
  `loc` varchar(10) DEFAULT NULL,
  `clasa` enum('economy','business','first') DEFAULT 'economy',
  `pret` decimal(10,2) DEFAULT '0.00',
  `data_cumparare` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status_plata` enum('platit','neplatit','anulat') DEFAULT 'platit',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_zbor` (`id_zbor`,`loc`),
  KEY `idx_bilet_pasager` (`id_pasager`),
  CONSTRAINT `fk_bilet_pasager` FOREIGN KEY (`id_pasager`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_bilet_zbor` FOREIGN KEY (`id_zbor`) REFERENCES `zbor` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bilet`
--

LOCK TABLES `bilet` WRITE;
/*!40000 ALTER TABLE `bilet` DISABLE KEYS */;
INSERT INTO `bilet` VALUES (1,1,9,'12A','economy',150.00,'2026-01-05 12:20:56','platit'),(2,1,10,'12B','economy',150.00,'2026-01-05 12:20:56','platit'),(3,2,9,'1A','business',450.00,'2026-01-05 12:20:56','platit'),(4,3,10,'5C','economy',120.00,'2026-01-05 12:20:56','platit'),(5,4,9,'20D','economy',100.00,'2026-01-05 12:20:56','neplatit'),(6,5,10,'2A','first',1200.00,'2026-01-05 12:20:56','platit'),(7,6,9,'15F','economy',80.00,'2026-01-05 12:20:56','platit'),(8,7,10,'30K','economy',350.00,'2026-01-05 12:20:56','anulat'),(9,8,9,'4A','economy',50.00,'2026-01-05 12:20:56','platit'),(10,10,10,'10C','business',500.00,'2026-01-05 12:20:56','platit');
/*!40000 ALTER TABLE `bilet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crew_assignment`
--

DROP TABLE IF EXISTS `crew_assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crew_assignment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_user` bigint NOT NULL,
  `id_zbor` bigint NOT NULL,
  `rol_in_zbor` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_user` (`id_user`,`id_zbor`,`rol_in_zbor`),
  KEY `idx_crew_zbor` (`id_zbor`),
  CONSTRAINT `fk_crew_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_crew_zbor` FOREIGN KEY (`id_zbor`) REFERENCES `zbor` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crew_assignment`
--

LOCK TABLES `crew_assignment` WRITE;
/*!40000 ALTER TABLE `crew_assignment` DISABLE KEYS */;
INSERT INTO `crew_assignment` VALUES (1,2,1,'Pilot Principal'),(5,2,2,'Pilot Principal'),(9,2,4,'Copilot'),(2,3,1,'Copilot'),(7,3,3,'Pilot Principal'),(3,4,1,'Sef Cabina'),(6,4,2,'Sef Cabina'),(10,4,5,'Stewardesa'),(4,5,1,'Stewardesa'),(8,5,3,'Stewardesa');
/*!40000 ALTER TABLE `crew_assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problema`
--

DROP TABLE IF EXISTS `problema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `problema` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_user_reporter` bigint DEFAULT NULL,
  `descriere` text NOT NULL,
  `status` enum('noua','in_analiza','rezolvata','inchisa') DEFAULT 'noua',
  `data_raportare` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `data_rezolvare` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_problema_user` (`id_user_reporter`),
  KEY `idx_problema_status` (`status`),
  CONSTRAINT `fk_problema_user` FOREIGN KEY (`id_user_reporter`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problema`
--

LOCK TABLES `problema` WRITE;
/*!40000 ALTER TABLE `problema` DISABLE KEYS */;
INSERT INTO `problema` VALUES (1,9,'Pierdut bagaj la zborul ZB107','noua','2026-01-05 12:20:56',NULL),(2,2,'Eroare la sistemul de navigatie secundar','in_analiza','2026-01-05 12:20:56',NULL),(3,4,'Pasager recalcitrant pe locul 12A','rezolvata','2026-01-05 12:20:56',NULL),(4,6,'Lipsa echipament protectie','inchisa','2026-01-05 12:20:56',NULL),(5,10,'Intarziere neanuntata zbor ZB101','noua','2026-01-05 12:20:56',NULL),(6,3,'Scaun pilot defect','in_analiza','2026-01-05 12:20:56',NULL),(7,8,'Eroare software dispecerat','noua','2026-01-05 12:20:56',NULL),(8,5,'Catering insuficient','rezolvata','2026-01-05 12:20:56',NULL),(9,7,'Usa hangar blocata','rezolvata','2026-01-05 12:20:56',NULL),(10,9,'Aplicatia mobila nu merge','inchisa','2026-01-05 12:20:56',NULL);
/*!40000 ALTER TABLE `problema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_muncitor` bigint DEFAULT NULL,
  `descriere` text,
  `status` enum('in_asteptare','in_desfasurare','completat','anulat') DEFAULT 'in_asteptare',
  `data_alocare` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `data_finalizare` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_task_muncitor` (`id_muncitor`),
  CONSTRAINT `fk_task_muncitor` FOREIGN KEY (`id_muncitor`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,6,'Curatenie pista 1','in_desfasurare','2026-01-05 12:20:56',NULL),(2,7,'Alimentare aeronava A001','completat','2026-01-05 12:20:56',NULL),(3,6,'Verificare bagaje zbor ZB102','in_asteptare','2026-01-05 12:20:56',NULL),(4,7,'Reparatie usa hangar','in_desfasurare','2026-01-05 12:20:56',NULL),(5,6,'Deigivrare aeronava A005','anulat','2026-01-05 12:20:56',NULL),(6,7,'Transport pasageri terminal','completat','2026-01-05 12:20:56',NULL),(7,6,'Incarcare catering ZB105','in_asteptare','2026-01-05 12:20:56',NULL),(8,7,'Mentenanta roata A003','in_asteptare','2026-01-05 12:20:56',NULL),(9,6,'Curatenie toalete terminal','completat','2026-01-05 12:20:56',NULL),(10,7,'Verificare lumini pista','in_desfasurare','2026-01-05 12:20:56',NULL);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL,
  `parola` varchar(255) NOT NULL,
  `rol` enum('Pasager','Muncitor','Dispecer','Administrator','Pilot','Stewardesa') DEFAULT 'Pasager',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','admin123','Administrator'),(2,'pilot_ion','pass123','Pilot'),(3,'pilot_alex','pass123','Pilot'),(4,'stew_ana','pass123','Stewardesa'),(5,'stew_maria','pass123','Stewardesa'),(6,'muncitor_bob','pass123','Muncitor'),(7,'muncitor_dorel','pass123','Muncitor'),(8,'dispecer_dan','pass123','Dispecer'),(9,'pasager_popescu','pass123','Pasager'),(10,'pasager_ionescu','pass123','Pasager');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zbor`
--

DROP TABLE IF EXISTS `zbor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zbor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `cod_zbor` (`cod_zbor`),
  KEY `fk_zbor_aeronava` (`id_aeronava`),
  CONSTRAINT `fk_zbor_aeronava` FOREIGN KEY (`id_aeronava`) REFERENCES `aeronava` (`id`) ON DELETE SET NULL,
  CONSTRAINT `chk_zbor_date` CHECK ((`data_sosire` >= `data_plecare`))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zbor`
--

LOCK TABLES `zbor` WRITE;
/*!40000 ALTER TABLE `zbor` DISABLE KEYS */;
INSERT INTO `zbor` VALUES (1,'ZB101',1,'Bucuresti','Londra','2026-02-01 08:00:00','2026-02-01 11:30:00','programat',NULL,6,150,8),(2,'ZB102',2,'Londra','Bucuresti','2026-02-01 14:00:00','2026-02-01 17:30:00','programat',NULL,6,140,8),(3,'ZB103',4,'Paris','Bucuresti','2026-02-02 09:00:00','2026-02-02 12:00:00','aterizat',NULL,5,120,8),(4,'ZB104',1,'Bucuresti','Roma','2026-02-03 10:00:00','2026-02-03 12:00:00','programat',NULL,6,160,8),(5,'ZB105',5,'New York','Bucuresti','2026-02-04 18:00:00','2026-02-05 06:00:00','in_cursa',NULL,12,350,8),(6,'ZB106',6,'Cluj','Viena','2026-02-05 08:00:00','2026-02-05 09:30:00','imbarcare',NULL,4,80,8),(7,'ZB107',8,'Dubai','Bucuresti','2026-02-06 14:00:00','2026-02-06 19:00:00','programat',NULL,10,280,8),(8,'ZB108',10,'Iasi','Bucuresti','2026-02-07 07:00:00','2026-02-07 08:00:00','decolat',NULL,3,50,8),(9,'ZB109',4,'Bucuresti','Berlin','2026-02-08 11:00:00','2026-02-08 13:00:00','anulat',NULL,0,0,8),(10,'ZB110',5,'Bucuresti','Madrid','2026-02-09 15:00:00','2026-02-09 18:30:00','programat',NULL,8,300,8);
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

-- Dump completed on 2026-01-05 15:02:44
