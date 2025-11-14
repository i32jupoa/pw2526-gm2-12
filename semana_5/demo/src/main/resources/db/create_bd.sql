-- TABLA: Socio

DROP TABLE IF EXISTS `Socio`;
CREATE TABLE IF NOT EXISTS `Socio` (
  `dni` VARCHAR(15) NOT NULL,
  `nombre` VARCHAR(100) COLLATE utf8_unicode_ci NOT NULL,
  `apellidos` VARCHAR(150) COLLATE utf8_unicode_ci NOT NULL,
  `fechaNacimiento` DATE NOT NULL,
  `direccion` VARCHAR(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fechaInscripcion` DATE DEFAULT NULL,
  `esTitular` BOOLEAN DEFAULT FALSE,
  `tieneTituloPatron` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `Socio` VALUES ('12345678A','Juan','Pérez García','1985-03-12','Calle Mar 12, Cádiz','2022-01-15',TRUE,TRUE);
INSERT INTO `Socio` VALUES ('87654321B','Lucía','Martín López','1990-06-05','Av. Sol 22, Cádiz','2023-03-10',FALSE,FALSE);
INSERT INTO `Socio` VALUES ('45678912C','Carlos','Ruiz Fernández','1978-11-21','Paseo del Puerto 9, Cádiz','2021-09-02',TRUE,FALSE);
INSERT INTO `Socio` VALUES ('78912345D','María','Santos Romero','1995-07-30','Calle Arena 4, Cádiz','2024-02-11',FALSE,TRUE);


-- TABLA: Patron

DROP TABLE IF EXISTS `Patron`;
CREATE TABLE IF NOT EXISTS `Patron` (
  `dni` VARCHAR(15) NOT NULL,
  `nombre` VARCHAR(100) COLLATE utf8_unicode_ci NOT NULL,
  `apellidos` VARCHAR(150) COLLATE utf8_unicode_ci NOT NULL,
  `fechaNacimiento` DATE NOT NULL,
  `fechaExpedicionTitulo` DATE NOT NULL,
  PRIMARY KEY (`dni`),
  FOREIGN KEY (`dni`) REFERENCES `Socio`(`dni`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `Patron` VALUES ('12345678A','Juan','Pérez García','1985-03-12','2010-05-01');
INSERT INTO `Patron` VALUES ('78912345D','María','Santos Romero','1995-07-30','2020-06-15');


-- TABLA: Inscripcion

DROP TABLE IF EXISTS `Inscripcion`;
CREATE TABLE IF NOT EXISTS `Inscripcion` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `fechaCreacion` DATE NOT NULL,
  `tipo` ENUM('COMPLETA','INFANTIL','ADULTO'),
  `cuotaActual` DOUBLE NOT NULL,
  `dniTitular` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`dniTitular`) REFERENCES `Socio`(`dni`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `Inscripcion` (`fechaCreacion`,`tipo`,`cuotaActual`,`dniTitular`) VALUES ('2022-01-15','INFANTIL',300.00,'12345678A');
INSERT INTO `Inscripcion` (`fechaCreacion`,`tipo`,`cuotaActual`,`dniTitular`) VALUES ('2021-09-02','COMPLETA',500.00,'45678912C');


-- TABLA: SocioInscripcion

DROP TABLE IF EXISTS `SocioInscripcion`;
CREATE TABLE IF NOT EXISTS `SocioInscripcion` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `idInscripcion` INT(11) NOT NULL,
  `dniSocio` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`idInscripcion`) REFERENCES `Inscripcion`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`dniSocio`) REFERENCES `Socio`(`dni`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `SocioInscripcion` (`idInscripcion`,`dniSocio`) VALUES (1,'12345678A');
INSERT INTO `SocioInscripcion` (`idInscripcion`,`dniSocio`) VALUES (2,'45678912C');
INSERT INTO `SocioInscripcion` (`idInscripcion`,`dniSocio`) VALUES (2,'87654321B');
INSERT INTO `SocioInscripcion` (`idInscripcion`,`dniSocio`) VALUES (2,'78912345D');


-- TABLA: Embarcacion

DROP TABLE IF EXISTS `Embarcacion`;
CREATE TABLE IF NOT EXISTS `Embarcacion` (
  `matricula` VARCHAR(20) NOT NULL,
  `nombre` VARCHAR(100) COLLATE utf8_unicode_ci NOT NULL,
  `tipo` VARCHAR(50) COLLATE utf8_unicode_ci NOT NULL,
  `numPlazas` INT(11) NOT NULL,
  `dimensiones` VARCHAR(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dniPatron` VARCHAR(15) DEFAULT NULL,
  PRIMARY KEY (`matricula`),
  FOREIGN KEY (`dniPatron`) REFERENCES `Patron`(`dni`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


INSERT INTO `Embarcacion` VALUES ('MAT001','Poseidón','VELERO',6,'8x3 m','12345678A');
INSERT INTO `Embarcacion` VALUES ('MAT002','Aventura','LANCHA',4,'5x2 m','78912345D');
INSERT INTO `Embarcacion` VALUES ('MAT003','Mar Azul','YATE',10,'12x4 m',NULL);


-- TABLA: Reserva

DROP TABLE IF EXISTS `Reserva`;
CREATE TABLE IF NOT EXISTS `Reserva` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `dniSocio` VARCHAR(15) NOT NULL,
  `matricula` VARCHAR(20) NOT NULL,
  `fecha` DATE NOT NULL,
  `plazasReservadas` INT(11) NOT NULL,
  `proposito` VARCHAR(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `precioTotal` DOUBLE NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`dniSocio`) REFERENCES `Socio`(`dni`) ON DELETE CASCADE,
  FOREIGN KEY (`matricula`) REFERENCES `Embarcacion`(`matricula`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `Reserva` (`dniSocio`,`matricula`,`fecha`,`plazasReservadas`,`proposito`,`precioTotal`) VALUES ('87654321B','MAT001','2025-07-10',2,'Paseo marítimo',120.00);
INSERT INTO `Reserva` (`dniSocio`,`matricula`,`fecha`,`plazasReservadas`,`proposito`,`precioTotal`) VALUES ('78912345D','MAT002','2025-08-02',3,'Pesca deportiva',200.00);


-- TABLA: Alquiler

DROP TABLE IF EXISTS `Alquiler`;
CREATE TABLE IF NOT EXISTS `Alquiler` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `idInscripcion` INT(11) NOT NULL,
  `matricula` VARCHAR(20) NOT NULL,
  `fechaInicio` DATE NOT NULL,
  `fechaFin` DATE NOT NULL,
  `plazasReservadas` INT(11) NOT NULL,
  `precioTotal` DOUBLE NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`idInscripcion`) REFERENCES `Inscripcion`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`matricula`) REFERENCES `Embarcacion`(`matricula`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `Alquiler` (`idInscripcion`,`matricula`,`fechaInicio`,`fechaFin`,`plazasReservadas`,`precioTotal`) VALUES (1,'MAT001','2025-07-01','2025-07-05',4,500.00);
INSERT INTO `Alquiler` (`idInscripcion`,`matricula`,`fechaInicio`,`fechaFin`,`plazasReservadas`,`precioTotal`) VALUES (2,'MAT003','2025-08-15','2025-08-20',6,900.00);


-- TABLA: AlquilerSocio

DROP TABLE IF EXISTS `AlquilerSocio`;
CREATE TABLE IF NOT EXISTS `AlquilerSocio` (
  `idAlquiler` INT(11) NOT NULL,
  `dniSocio` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`idAlquiler`,`dniSocio`),
  FOREIGN KEY (`idAlquiler`) REFERENCES `Alquiler`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`dniSocio`) REFERENCES `Socio`(`dni`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO AlquilerSocio VALUES (1,'12345678A');
INSERT INTO AlquilerSocio VALUES (2,'45678912C');
INSERT INTO AlquilerSocio VALUES (2,'78912345D');