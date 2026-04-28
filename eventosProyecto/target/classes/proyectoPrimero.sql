
CREATE DATABASE IF NOT EXISTS proyectoPrimero;
USE proyectoPrimero;

CREATE TABLE IF NOT EXISTS usuarios (
    usuario VARCHAR(50) PRIMARY KEY,
    clave VARCHAR(100) NOT NULL
);

-- Usuario de prueba
INSERT INTO usuarios (usuario, clave) VALUES ('usuario', 'usuario1234')
ON DUPLICATE KEY UPDATE clave = 'usuario1234';
