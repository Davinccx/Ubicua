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
  `id` int(11) NOT NULL,
  `user` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla vendify.admin: ~1 rows (aproximadamente)
INSERT IGNORE INTO `admin` (`id`, `user`, `password`) VALUES
	(1, 'admin', 'vendify_2023');

-- Volcando estructura para tabla vendify.maquina
CREATE TABLE IF NOT EXISTS `maquina` (
  `id` int(11) NOT NULL,
  `location` varchar(50) DEFAULT NULL,
  `saldo` double DEFAULT NULL,
  `id_producto` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_maquina_productos` (`id_producto`),
  CONSTRAINT `FK_maquina_productos` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla vendify.maquina: ~0 rows (aproximadamente)
INSERT IGNORE INTO `maquina` (`id`, `location`, `saldo`, `id_producto`) VALUES
	(1, 'Spain', 0, 1);

-- Volcando estructura para tabla vendify.productos
CREATE TABLE IF NOT EXISTS `productos` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `precio` double DEFAULT NULL,
  `descripcion` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla vendify.productos: ~1 rows (aproximadamente)
INSERT IGNORE INTO `productos` (`id`, `nombre`, `precio`, `descripcion`) VALUES
	(1, 'cocacola', 1.5, 'Fresquita');

-- Volcando estructura para tabla vendify.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `saldo` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla vendify.users: ~2 rows (aproximadamente)
INSERT IGNORE INTO `users` (`id`, `email`, `password`, `username`, `telefono`, `saldo`) VALUES
	(1, 'davidfernandezsanz@gmail.com', '1234', 'Davinccx', '6666666', 10000),
	(2, 'prueba@prueba', '1234', '1', '545454', 322);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
