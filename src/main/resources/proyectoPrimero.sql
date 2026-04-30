DROP DATABASE IF EXISTS eventos;

create database eventos;
use eventos;

CREATE USER IF NOT EXISTS 'alumno'@'localhost' IDENTIFIED BY 'alumnodam#1234';
GRANT ALL PRIVILEGES ON *.* TO 'alumno'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE Cliente (
    id_Cliente INT(8) PRIMARY KEY,
    nombre VARCHAR(50),
    correo VARCHAR(80),
    telefono VARCHAR(12)
);

