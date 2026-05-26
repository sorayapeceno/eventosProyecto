SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS Accesorios;
DROP TABLE IF EXISTS Camisetas;
DROP TABLE IF EXISTS Textil;
DROP TABLE IF EXISTS Beca;
DROP TABLE IF EXISTS Estandar;
DROP TABLE IF EXISTS VIP;
DROP TABLE IF EXISTS Entrada;
DROP TABLE IF EXISTS Linea_Ticket;
DROP TABLE IF EXISTS Ticket;
DROP TABLE IF EXISTS Otros;
DROP TABLE IF EXISTS Producto;
DROP TABLE IF EXISTS Asistente;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE Asistente (
                           ID_Asistente BIGINT AUTO_INCREMENT PRIMARY KEY,
                           Tematica VARCHAR(64),
                           Direccion VARCHAR(128),
                           Observaciones VARCHAR(256),
                           Bio VARCHAR(512),
                           Total_Gastado DECIMAL(10,2),
                           Nivel_Imparticion VARCHAR(32)
);

INSERT INTO Asistente VALUES
                          (1,'Música','Calle Melodía 12','Alérgico al humo','Productor musical',150.00,'Experto'),
                          (2,'Tecnología','Av. Digital 404','Requiere silla','Desarrollador Senior',260.00,'Medio'),
                          (3,'Arte','Plaza Pintor 5','VIP recurrente','Escultor novel',45.00,'Básico'),
                          (4,'Marketing','Gran Vía 88','Ninguna','Growth Hacker',120.00,'Experto'),
                          (5,'Literatura','Calle Verso 9','Estudiante','Poeta en activo',0.00,'Medio'),
                          (6,'Deporte','Polideportivo 1','Atleta PRO','Entrenador',210.00,'Experto'),
                          (7,'Cine','Av. Hollywood 7','Prensa','Crítico de cine',85.00,'Medio'),
                          (8,'Moda','Pasarela 10','Influencer','Diseñador',170.00,'Básico');

CREATE TABLE Producto (
                          ID_Producto BIGINT AUTO_INCREMENT PRIMARY KEY,
                          Nombre_Producto VARCHAR(64),
                          Precio DECIMAL(10,2),
                          Stock_Disponible INT,
                          Descripcion_Producto VARCHAR(256),
                          Tipo_IVA VARCHAR(10),
                          Descuento DECIMAL(10,2)
);

INSERT INTO Producto VALUES
                         (1,'Pase VIP Premium',150.00,25,'Acceso total y hotel','21%',0.00),
                         (2,'Entrada Estándar',85.00,150,'Acceso un día','21%',0.00),
                         (3,'Pase Beca Joven',45.00,10,'Solo estudiantes','21%',20.00),
                         (4,'Camiseta Oficial',25.00,100,'Algodón orgánico','21%',0.00),
                         (5,'Gorra Evento',15.00,200,'Talla única','21%',0.00),
                         (6,'Poster Firmado',10.00,50,'Coleccionista','21%',0.00);

CREATE TABLE Ticket (
                        ID_Ticket BIGINT AUTO_INCREMENT PRIMARY KEY,
                        ID_Asistente BIGINT,
                        Codigo_QR VARCHAR(32),
                        Fecha_Compra DATE,
                        Precio_Final DECIMAL(10,2),
                        Metodo_Pago VARCHAR(32),
                        Descuento DECIMAL(10,2),
                        Codigo_Promocional VARCHAR(32),
                        FOREIGN KEY (ID_Asistente) REFERENCES Asistente(ID_Asistente)
);

INSERT INTO Ticket VALUES
                       (100,1,'QR-8821','2024-05-10',150.00,'Tarjeta',0.00,NULL),
                       (101,2,'QR-9932','2024-05-11',260.00,'PayPal',0.00,NULL),
                       (102,3,'QR-1122','2024-05-11',45.00,'Efectivo',5.00,'BIENVENIDA'),
                       (103,4,'QR-4455','2024-05-12',120.00,'Tarjeta',0.00,NULL),
                       (104,6,'QR-6677','2024-05-12',210.00,'PayPal',10.00,'PROMO_DEPOR'),
                       (105,7,'QR-7788','2024-05-13',85.00,'PayPal',0.00,NULL),
                       (106,8,'QR-8899','2024-05-14',170.00,'Transferencia',0.00,NULL);

CREATE TABLE Linea_Ticket (
                              ID_LineaTicket BIGINT AUTO_INCREMENT PRIMARY KEY,
                              ID_Ticket BIGINT,
                              ID_Producto BIGINT NOT NULL,
                              Cantidad INT,
                              IVA_Cuota DECIMAL(10,2),
                              Subtotal_Base DECIMAL(10,2),
                              Total_Linea DECIMAL(10,2),
                              FOREIGN KEY (ID_Ticket) REFERENCES Ticket(ID_Ticket),
                              FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto)
);

INSERT INTO Linea_Ticket VALUES
                             (501,100,1,1,26.03,123.97,150.00),
                             (502,101,2,2,29.50,140.50,170.00),
                             (503,101,4,2,8.68,41.32,50.00),
                             (504,101,5,1,5.21,24.79,30.00),
                             (505,102,3,1,4.09,40.91,45.00),
                             (506,104,1,1,26.03,123.97,150.00),
                             (507,104,4,2,8.68,41.32,50.00);

CREATE TABLE Entrada (
                         ID_Entrada BIGINT AUTO_INCREMENT PRIMARY KEY,
                         ID_Producto BIGINT,
                         Zona VARCHAR(32),
                         Asiento VARCHAR(16),
                         Acceso_Permitido VARCHAR(64),
                         Accesos_Adicionales VARCHAR(128),
                         Validez_Horas INT,
                         EstadoEntrada VARCHAR(32),
                         FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto)
);

INSERT INTO Entrada VALUES
                        (301,1,'Palco O','Sofá 1','Todo el recinto','Catering, Masaje, Grabación, Meet&Greet',96,'Activa'),
                        (302,2,'Preferente','P-10','Escenario A y B','Zona de Descanso, Wi-Fi Premium',24,'Activa'),
                        (303,3,'Pista','Libre','Talleres infantiles','Parque de juegos, Merienda',12,'Pendiente'),
                        (304,4,'Grada','G-01','Escenario Principal','Consigna gratuita',12,'Activa');

CREATE TABLE VIP (
                     ID_Vip BIGINT AUTO_INCREMENT PRIMARY KEY,
                     ID_Entrada BIGINT NOT NULL,
                     Beneficios_Incluidos VARCHAR(256),
                     Servicios_Adicionales VARCHAR(256),
                     Precio_Extra DECIMAL(8,2),
                     FOREIGN KEY (ID_Entrada) REFERENCES Entrada(ID_Entrada)
);

INSERT INTO VIP VALUES
                    (200,301,'Kit de prensa, Vinilo firmado','Acceso a pruebas de sonido',100.00),
                    (201,302,'Fast track, Asiento reservado','Suscripción 1 año revista',50.00);

CREATE TABLE Estandar (
                          ID_Estandar BIGINT AUTO_INCREMENT PRIMARY KEY,
                          ID_Entrada BIGINT NOT NULL,
                          Incluye_Regalo VARCHAR(256),
                          FOREIGN KEY (ID_Entrada) REFERENCES Entrada(ID_Entrada)
);

INSERT INTO Estandar VALUES
                         (300,303,'Camiseta básica'),
                         (301,304,'No incluye (Entrada básica)');

CREATE TABLE Beca (
                      ID_Beca BIGINT AUTO_INCREMENT PRIMARY KEY,
                      ID_Entrada BIGINT NOT NULL,
                      Motivo_Beca VARCHAR(128),
                      Porcentaje_Descuento DECIMAL(5,2),
                      Requisitos VARCHAR(256),
                      FOREIGN KEY (ID_Entrada) REFERENCES Entrada(ID_Entrada)
);

INSERT INTO Beca VALUES
                     (400,301,'Inclusión Social',80.00,'Certificado Discapacidad'),
                     (401,302,'Talento Emergente',100.00,'Ganador concurso local');

CREATE TABLE Textil (
                        ID_Textil BIGINT AUTO_INCREMENT PRIMARY KEY,
                        ID_Producto BIGINT,
                        Talla VARCHAR(8),
                        Color VARCHAR(32),
                        Material VARCHAR(64),
                        Genero VARCHAR(32),
                        Tipo_Textil VARCHAR(32),
                        FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto)
);

INSERT INTO Textil VALUES
                       (20,4,'XL','Negro','Algodón','Hombre','Camiseta'),
                       (21,4,'S','Blanco','Algodón','Mujer','Camiseta'),
                       (22,5,'Única','Azul','Poliéster','Unisex','Gorra');

CREATE TABLE Camisetas (
                           ID_Camisetas BIGINT AUTO_INCREMENT PRIMARY KEY,
                           ID_Textil BIGINT NOT NULL,
                           Modelo VARCHAR(64),
                           Estampado VARCHAR(64),
                           FOREIGN KEY (ID_Textil) REFERENCES Textil(ID_Textil)
);

INSERT INTO Camisetas VALUES
                          (1,20,'Oversize','Logo Flúor'),
                          (2,21,'Slim Fit','Logo Minimalista');

CREATE TABLE Accesorios (
                            ID_Accesorios BIGINT AUTO_INCREMENT PRIMARY KEY,
                            ID_Textil BIGINT NOT NULL,
                            Tipo_Accesorio VARCHAR(64),
                            FOREIGN KEY (ID_Textil) REFERENCES Textil(ID_Textil)
);

INSERT INTO Accesorios VALUES
                           (1000,22,'Gorra ajustable metálica'),
                           (1001,22,'Pack deportivo'),
                           (1002,22,'Maleta de entrenamiento');

CREATE TABLE Otros (
                       ID_Otros BIGINT AUTO_INCREMENT PRIMARY KEY,
                       ID_Producto BIGINT NOT NULL,
                       Descripcion VARCHAR(256),
                       FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto)
);

INSERT INTO Otros VALUES
                      (200,6,'Edición de audífilo con bonus track inédito.'),
                      (201,5,'15 stickers diseñados por artistas urbanos locales.'),
                      (202,4,'Tapa dura, 200 páginas, bullet journal.'),
                      (203,3,'Grabado láser del mapa del evento.');