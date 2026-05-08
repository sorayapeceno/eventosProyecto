DROP DATABASE IF EXISTS eventos;

create database eventos;
use eventos;

CREATE USER IF NOT EXISTS 'alumno'@'localhost' IDENTIFIED BY 'alumnodam#1234';
GRANT ALL PRIVILEGES ON *.* TO 'alumno'@'localhost';
FLUSH PRIVILEGES;



