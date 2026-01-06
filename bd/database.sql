-- 1. DROP AND RECREATE DATABASE
DROP DATABASE IF EXISTS gestionare_aeroport;
CREATE DATABASE gestionare_aeroport;
USE gestionare_aeroport;

-- ==========================================================
-- TABLE: user
-- ==========================================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(200) NOT NULL,
  `parola` varchar(255) NOT NULL,
  `rol` enum('Pasager','Muncitor','Dispecer','Administrator','Pilot','Stewardesa') DEFAULT 'Pasager',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` (`username`, `parola`, `rol`) VALUES
('admin', 'admin123', 'Administrator'),
('pilot_ion', 'pass123', 'Pilot'),
('pilot_alex', 'pass123', 'Pilot'),
('stew_ana', 'pass123', 'Stewardesa'),
('stew_maria', 'pass123', 'Stewardesa'),
('muncitor_bob', 'pass123', 'Muncitor'),
('muncitor_dorel', 'pass123', 'Muncitor'),
('dispecer_dan', 'pass123', 'Dispecer'),
('pasager_popescu', 'pass123', 'Pasager'),
('pasager_ionescu', 'pass123', 'Pasager');

-- ==========================================================
-- TABLE: aeronava
-- ==========================================================
DROP TABLE IF EXISTS `aeronava`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `aeronava` (`cod_aeronava`, `model`, `capacitate`, `stare_operationala`, `locatie_curenta`) VALUES
('A001', 'Boeing 737', 180, 'activ', 'Bucuresti'),
('A002', 'Boeing 737', 180, 'activ', 'Londra'),
('A003', 'Airbus A320', 150, 'in_mentenanta', 'Hangar 1'),
('A004', 'Airbus A320', 150, 'activ', 'Paris'),
('A005', 'Boeing 747', 400, 'activ', 'New York'),
('A006', 'Embraer 190', 100, 'activ', 'Cluj'),
('A007', 'Bombardier CRJ', 90, 'retras', 'Hangar 2'),
('A008', 'Airbus A350', 300, 'activ', 'Dubai'),
('A009', 'Boeing 777', 350, 'in_mentenanta', 'Hangar 3'),
('A010', 'ATR 72', 70, 'activ', 'Iasi');

-- ==========================================================
-- TABLE: zbor
-- ==========================================================
DROP TABLE IF EXISTS `zbor`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `zbor` (`cod_zbor`, `id_aeronava`, `plecare_din`, `destinatie`, `data_plecare`, `data_sosire`, `status_zbor`, `nr_echipaj_bord`, `nr_pasageri_estimat`, `id_dispecer`) VALUES
('ZB101', 1, 'Bucuresti', 'Londra', '2026-02-01 08:00:00', '2026-02-01 11:30:00', 'programat', 6, 150, 8),
('ZB102', 2, 'Londra', 'Bucuresti', '2026-02-01 14:00:00', '2026-02-01 17:30:00', 'programat', 6, 140, 8),
('ZB103', 4, 'Paris', 'Bucuresti', '2026-02-02 09:00:00', '2026-02-02 12:00:00', 'aterizat', 5, 120, 8),
('ZB104', 1, 'Bucuresti', 'Roma', '2026-02-03 10:00:00', '2026-02-03 12:00:00', 'programat', 6, 160, 8),
('ZB105', 5, 'New York', 'Bucuresti', '2026-02-04 18:00:00', '2026-02-05 06:00:00', 'in_cursa', 12, 350, 8),
('ZB106', 6, 'Cluj', 'Viena', '2026-02-05 08:00:00', '2026-02-05 09:30:00', 'imbarcare', 4, 80, 8),
('ZB107', 8, 'Dubai', 'Bucuresti', '2026-02-06 14:00:00', '2026-02-06 19:00:00', 'programat', 10, 280, 8),
('ZB108', 10, 'Iasi', 'Bucuresti', '2026-02-07 07:00:00', '2026-02-07 08:00:00', 'decolat', 3, 50, 8),
('ZB109', 4, 'Bucuresti', 'Berlin', '2026-02-08 11:00:00', '2026-02-08 13:00:00', 'anulat', 0, 0, 8),
('ZB110', 5, 'Bucuresti', 'Madrid', '2026-02-09 15:00:00', '2026-02-09 18:30:00', 'programat', 8, 300, 8);

-- ==========================================================
-- TABLE: bilet
-- ==========================================================
DROP TABLE IF EXISTS `bilet`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `bilet` (`id_zbor`, `id_pasager`, `loc`, `clasa`, `pret`, `status_plata`) VALUES
(1, 9, '12A', 'economy', 150.00, 'platit'),
(1, 10, '12B', 'economy', 150.00, 'platit'),
(2, 9, '1A', 'business', 450.00, 'platit'),
(3, 10, '5C', 'economy', 120.00, 'platit'),
(4, 9, '20D', 'economy', 100.00, 'neplatit'),
(5, 10, '2A', 'first', 1200.00, 'platit'),
(6, 9, '15F', 'economy', 80.00, 'platit'),
(7, 10, '30K', 'economy', 350.00, 'anulat'),
(8, 9, '4A', 'economy', 50.00, 'platit'),
(10, 10, '10C', 'business', 500.00, 'platit');

-- ==========================================================
-- TABLE: bagaj
-- ==========================================================
DROP TABLE IF EXISTS `bagaj`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `bagaj` (`id_bilet`, `greutate`, `tip`, `status_checkin`, `eticheta`) VALUES
(1, 22.5, 'cala', 'incarcat', 'TAG1001'),
(1, 8.0, 'mana', 'nepredat', 'TAG1002'),
(2, 15.0, 'cala', 'predat', 'TAG1003'),
(3, 20.0, 'cala', 'livrat', 'TAG1004'),
(5, 30.0, 'cala', 'incarcat', 'TAG1005'),
(6, 12.0, 'mana', 'nepredat', 'TAG1006'),
(7, 25.0, 'cala', 'pierdut', 'TAG1007'),
(8, 10.0, 'cala', 'livrat', 'TAG1008'),
(9, 5.0, 'mana', 'nepredat', 'TAG1009'),
(10, 23.0, 'cala', 'predat', 'TAG1010');

-- ==========================================================
-- TABLE: crewassignment (RENAMED FROM crew_assignment)
-- ==========================================================
DROP TABLE IF EXISTS `crewassignment`;
CREATE TABLE `crewassignment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_user` bigint NOT NULL,
  `id_zbor` bigint NOT NULL,
  `rol_in_zbor` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_user` (`id_user`,`id_zbor`,`rol_in_zbor`),
  KEY `idx_crew_zbor` (`id_zbor`),
  CONSTRAINT `fk_crew_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_crew_zbor` FOREIGN KEY (`id_zbor`) REFERENCES `zbor` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `crewassignment` (`id_user`, `id_zbor`, `rol_in_zbor`) VALUES
(2, 1, 'Pilot Principal'),
(3, 1, 'Copilot'),
(4, 1, 'Sef Cabina'),
(5, 1, 'Stewardesa'),
(2, 2, 'Pilot Principal'),
(4, 2, 'Sef Cabina'),
(3, 3, 'Pilot Principal'),
(5, 3, 'Stewardesa'),
(2, 4, 'Copilot'),
(4, 5, 'Stewardesa');

-- ==========================================================
-- TABLE: problema
-- ==========================================================
DROP TABLE IF EXISTS `problema`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `problema` (`id_user_reporter`, `descriere`, `status`) VALUES
(9, 'Pierdut bagaj la zborul ZB107', 'noua'),
(2, 'Eroare la sistemul de navigatie secundar', 'in_analiza'),
(4, 'Pasager recalcitrant pe locul 12A', 'rezolvata'),
(6, 'Lipsa echipament protectie', 'inchisa'),
(10, 'Intarziere neanuntata zbor ZB101', 'noua'),
(3, 'Scaun pilot defect', 'in_analiza'),
(8, 'Eroare software dispecerat', 'noua'),
(5, 'Catering insuficient', 'rezolvata'),
(7, 'Usa hangar blocata', 'rezolvata'),
(9, 'Aplicatia mobila nu merge', 'inchisa');

-- ==========================================================
-- TABLE: task
-- ==========================================================
DROP TABLE IF EXISTS `task`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `task` (`id_muncitor`, `descriere`, `status`) VALUES
(6, 'Curatenie pista 1', 'in_desfasurare'),
(7, 'Alimentare aeronava A001', 'completat'),
(6, 'Verificare bagaje zbor ZB102', 'in_asteptare'),
(7, 'Reparatie usa hangar', 'in_desfasurare'),
(6, 'Deigivrare aeronava A005', 'anulat'),
(7, 'Transport pasageri terminal', 'completat'),
(6, 'Incarcare catering ZB105', 'in_asteptare'),
(7, 'Mentenanta roata A003', 'in_asteptare'),
(6, 'Curatenie toalete terminal', 'completat'),
(7, 'Verificare lumini pista', 'in_desfasurare');