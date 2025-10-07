-- =====================================================
-- BASE DE DATOS: CLUB NÁUTICO
-- Autor: Grupo-12
-- =====================================================

DROP DATABASE IF EXISTS club_nautico;
CREATE DATABASE club_nautico CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE club_nautico;

CREATE TABLE Socio (
    dni VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    fechaNacimiento DATE NOT NULL,
    direccion VARCHAR(200),
    fechaInscripcion DATE NOT NULL,
    esTitular BOOLEAN DEFAULT FALSE,
    tieneTituloPatron BOOLEAN DEFAULT FALSE
);

CREATE TABLE Patron (
    dni VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    fechaNacimiento DATE NOT NULL,
    fechaExpedicionTitulo DATE NOT NULL,
    FOREIGN KEY (dni) REFERENCES Socio(dni) ON DELETE CASCADE
);

CREATE TABLE Inscripcion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fechaCreacion DATE NOT NULL,
    tipo ENUM('INDIVIDUAL', 'FAMILIAR') NOT NULL,
    cuotaActual DOUBLE NOT NULL,
    dniTitular VARCHAR(15) NOT NULL,
    FOREIGN KEY (dniTitular) REFERENCES Socio(dni) ON DELETE CASCADE
);

CREATE TABLE SocioInscripcion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idInscripcion INT NOT NULL,
    dniSocio VARCHAR(15) NOT NULL,
    FOREIGN KEY (idInscripcion) REFERENCES Inscripcion(id) ON DELETE CASCADE,
    FOREIGN KEY (dniSocio) REFERENCES Socio(dni) ON DELETE CASCADE
);

CREATE TABLE Embarcacion (
    matricula VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    numPlazas INT NOT NULL,
    dimensiones VARCHAR(50),
    dniPatron VARCHAR(15),
    FOREIGN KEY (dniPatron) REFERENCES Patron(dni) ON DELETE SET NULL
);

CREATE TABLE Reserva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dniSocio VARCHAR(15) NOT NULL,
    matricula VARCHAR(20) NOT NULL,
    fecha DATE NOT NULL,
    plazasReservadas INT NOT NULL,
    proposito VARCHAR(200),
    precioTotal DOUBLE NOT NULL,
    FOREIGN KEY (dniSocio) REFERENCES Socio(dni) ON DELETE CASCADE,
    FOREIGN KEY (matricula) REFERENCES Embarcacion(matricula) ON DELETE CASCADE
);

CREATE TABLE Alquiler (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idInscripcion INT NOT NULL,
    matricula VARCHAR(20) NOT NULL,
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    plazasReservadas INT NOT NULL,
    precioTotal DOUBLE NOT NULL,
    FOREIGN KEY (idInscripcion) REFERENCES Inscripcion(id) ON DELETE CASCADE,
    FOREIGN KEY (matricula) REFERENCES Embarcacion(matricula) ON DELETE CASCADE
);

CREATE TABLE AlquilerSocio (
    idAlquiler INT NOT NULL,
    dniSocio VARCHAR(15) NOT NULL,
    PRIMARY KEY (idAlquiler, dniSocio),
    FOREIGN KEY (idAlquiler) REFERENCES Alquiler(id) ON DELETE CASCADE,
    FOREIGN KEY (dniSocio) REFERENCES Socio(dni) ON DELETE CASCADE
);

-- DATOS DE EJEMPLO
INSERT INTO Socio VALUES
('12345678A','Juan','Pérez García','1985-03-12','Calle Mar 12, Cádiz','2022-01-15',TRUE,TRUE),
('87654321B','Lucía','Martín López','1990-06-05','Av. Sol 22, Cádiz','2023-03-10',FALSE,FALSE),
('45678912C','Carlos','Ruiz Fernández','1978-11-21','Paseo del Puerto 9, Cádiz','2021-09-02',TRUE,FALSE),
('78912345D','María','Santos Romero','1995-07-30','Calle Arena 4, Cádiz','2024-02-11',FALSE,TRUE);

INSERT INTO Patron VALUES
('12345678A','Juan','Pérez García','1985-03-12','2010-05-01'),
('78912345D','María','Santos Romero','1995-07-30','2020-06-15');

INSERT INTO Inscripcion (fechaCreacion, tipo, cuotaActual, dniTitular) VALUES
('2022-01-15','INDIVIDUAL',300.00,'12345678A'),
('2021-09-02','FAMILIAR',500.00,'45678912C');

INSERT INTO SocioInscripcion (idInscripcion, dniSocio) VALUES
(1,'12345678A'),
(2,'45678912C'),
(2,'87654321B'),
(2,'78912345D');

INSERT INTO Embarcacion VALUES
('MAT001','Poseidón','VELERO',6,'8x3 m','12345678A'),
('MAT002','Aventura','LANCHA',4,'5x2 m','78912345D'),
('MAT003','Mar Azul','YATE',10,'12x4 m',NULL);

INSERT INTO Reserva (dniSocio, matricula, fecha, plazasReservadas, proposito, precioTotal) VALUES
('87654321B','MAT001','2025-07-10',2,'Paseo marítimo',120.00),
('78912345D','MAT002','2025-08-02',3,'Pesca deportiva',200.00);

INSERT INTO Alquiler (idInscripcion, matricula, fechaInicio, fechaFin, plazasReservadas, precioTotal) VALUES
(1,'MAT001','2025-07-01','2025-07-05',4,500.00),
(2,'MAT003','2025-08-15','2025-08-20',6,900.00);

INSERT INTO AlquilerSocio VALUES
(1,'12345678A'),
(2,'45678912C'),
(2,'78912345D');
