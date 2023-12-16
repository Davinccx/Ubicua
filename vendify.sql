-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         11.2.2-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para vendify
CREATE DATABASE IF NOT EXISTS `vendify` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `vendify`;

-- Volcando estructura para tabla vendify.admin
CREATE TABLE IF NOT EXISTS `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla vendify.admin: ~1 rows (aproximadamente)
INSERT IGNORE INTO `admin` (`id`, `user`, `password`) VALUES
	(1, 'admin', 'vendify_2023');

-- Volcando estructura para tabla vendify.maquina
CREATE TABLE IF NOT EXISTS `maquina` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(50) DEFAULT NULL,
  `saldo` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla vendify.maquina: ~3 rows (aproximadamente)
INSERT IGNORE INTO `maquina` (`id`, `location`, `saldo`) VALUES
	(1, 'Alcala de Henares', 0),
	(2, 'Barcelona', 3),
	(3, 'Meco', 1);

-- Volcando estructura para tabla vendify.productos
CREATE TABLE IF NOT EXISTS `productos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `precio` double DEFAULT NULL,
  `descripcion` varchar(50) DEFAULT NULL,
  `id_maquina` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK` (`id_maquina`),
  CONSTRAINT `FK` FOREIGN KEY (`id_maquina`) REFERENCES `maquina` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla vendify.productos: ~4 rows (aproximadamente)
INSERT IGNORE INTO `productos` (`id`, `nombre`, `precio`, `descripcion`, `id_maquina`) VALUES
	(1, 'cocacola', 1.5, 'Fresquita', 1),
	(2, 'redbull', 1.8, 'Te da alas', 1),
	(3, 'Agua', 0.9, 'Bezoya', 3),
	(4, 'Monster', 1.8, 'Energy', 2);

-- Volcando estructura para tabla vendify.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `saldo` double NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla vendify.users: ~2 rows (aproximadamente)
INSERT IGNORE INTO `users` (`id`, `email`, `password`, `username`, `telefono`, `saldo`) VALUES
	(1, 'davidfernandezsanz@gmail.com', '1234', 'Davinccx', '638673981', 0),
	(2, 'prueba@prueba.com', '1234', 'root', '691581823', 0);

-- Volcando estructura para tabla vendify.ventas
CREATE TABLE IF NOT EXISTS `ventas` (
  `id_venta` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) DEFAULT NULL,
  `id_producto` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_venta`),
  KEY `FK_user` (`id_user`),
  KEY `FK_producto` (`id_producto`),
  CONSTRAINT `FK_producto` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_user` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla vendify.ventas: ~3 rows (aproximadamente)
INSERT IGNORE INTO `ventas` (`id_venta`, `id_user`, `id_producto`) VALUES
	(1, 1, 3),
	(2, 2, 4),
	(3, 1, 1);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
