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


CREATE DATABASE IF NOT EXISTS proyectofinaljud;
USE proyectofinaljud;

CREATE USER IF NOT EXISTS 'alumno'@'localhost' IDENTIFIED BY 'alumnodam#1234';
GRANT ALL PRIVILEGES ON *.* TO 'alumno'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE Persona (
                         id_Persona BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,

                         DNI VARCHAR(15) NOT NULL UNIQUE,
                         Username VARCHAR(50) NOT NULL UNIQUE,

                         Nombre VARCHAR(50) NOT NULL,
                         Ap1 VARCHAR(50) NOT NULL,
                         Ap2 VARCHAR(50),
                         Correo VARCHAR(100) NOT NULL,

                         Alergenos VARCHAR(30),
                         Telefono VARCHAR(15),
                         Ciudad VARCHAR(50),
                         Pais VARCHAR(50),
                         Foto VARCHAR(255),
                         Genero VARCHAR(20),
                         Fecha_Nacimiento DATE,
                         Password VARCHAR(50),
                         Direccion VARCHAR(100),
                         Redes_Sociales VARCHAR(100),
                         Web VARCHAR(100),

                         CONSTRAINT ck_Correo
                             CHECK (Correo REGEXP '^[^[:space:]]+@[^[:space:]]+\\.[^[:space:]]+$'),

    CONSTRAINT ck_Telefono
        CHECK (Telefono IS NULL OR Telefono REGEXP '^[0-9]{9}$'),

    CONSTRAINT ck_Alergenos
        CHECK (
            Alergenos IS NULL OR Alergenos IN (
                'Frutos Secos','Lácteos','Gluten','Aguacate',
                'Marisco','Fruta','Otro'
            )
        )
);



CREATE TABLE Organizacion (
                              id_Organizacion BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
);



CREATE TABLE Tematica (
                          id_Tematica BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          Tema VARCHAR(100) NOT NULL
);


CREATE TABLE Evento (
                        id_Evento BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,

                        Nombre VARCHAR(100) NOT NULL,
                        Descripcion VARCHAR(500),

                        Fecha_Inicio DATE NOT NULL,
                        Fecha_Fin DATE,

                        Direccion VARCHAR(100),
                        Ciudad VARCHAR(50),

                        Capacidad INT,

                        Estado VARCHAR(20) NOT NULL,
                        Modalidad VARCHAR(20) NOT NULL,
                        Lugar VARCHAR(100),

                        CONSTRAINT ck_Estado
                            CHECK (Estado IN ('Borrador','Abierto','Cerrado','Cancelado','Pospuesto')),

                        CONSTRAINT ck_Modalidad
                            CHECK (Modalidad IN ('Presencial','Online','Hibrido')),

                        CONSTRAINT ck_Capacidad
                            CHECK (Capacidad IS NULL OR Capacidad > 0)
);



CREATE TABLE Ponente (
                         id_Ponente BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         id_Persona BIGINT NOT NULL,

                         BIO VARCHAR(500),
                         Especialidad VARCHAR(50) NOT NULL,
                         CV VARCHAR(100),
                         Nivel_Imparticion VARCHAR(20) NOT NULL,

                         CONSTRAINT fk_Ponente_Persona
                             FOREIGN KEY (Id_Persona)
                                 REFERENCES Persona(Id_Persona)
                                 ON DELETE CASCADE,

                         CONSTRAINT ck_Nivel_Imparticion
                             CHECK (
                                 Nivel_Imparticion IN (
                                                       'Infantil','Primaria','Secundaria',
                                                       'Bachillerato','FP','Universidad',
                                                       'Postgrados','Otros'
                                     )
                                 )
);


CREATE TABLE Ponencia (
                          id_Ponencia BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          id_Evento BIGINT NOT NULL,
                          id_Tematica BIGINT NOT NULL,
                          Titulo VARCHAR(150) NOT NULL,
                          Duracion INT,
                          Fecha DATE,
                          Hora VARCHAR(10),
                          Ubicacion VARCHAR(100),
                          Sala VARCHAR(50),
                          Nivel VARCHAR(20),
                          Tipo VARCHAR(20) NOT NULL,
                          Formato VARCHAR(20) NOT NULL,

                          CONSTRAINT fk_Ponencia_Evento
                              FOREIGN KEY (Id_Evento)
                                  REFERENCES Evento(Id_Evento)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_Ponencia_Tematica
                              FOREIGN KEY (Id_Tematica)
                                  REFERENCES Tematica(Id_Tematica)
                                  ON DELETE CASCADE,

                          CONSTRAINT ck_Nivel
                              CHECK (Nivel IN ('Basico','Intermedio','Avanzado')),

                          CONSTRAINT ck_Tipo
                              CHECK (Tipo IN ('Charla','Taller','Mesa','Podcast')),

                          CONSTRAINT ck_Formato
                              CHECK (Formato IN ('Presencial','Online','Hibrido')),

                          CONSTRAINT ck_Duracion
                              CHECK (Duracion IS NULL OR Duracion > 0)
);


CREATE TABLE Persona_Organizacion (
                                      id_Persona BIGINT NOT NULL,
                                      id_Organizacion BIGINT NOT NULL,

                                      Fecha_Inclusion_Organizacion DATE,

                                      CONSTRAINT pk_Persona_Organizacion
                                          PRIMARY KEY (Id_Persona, Id_Organizacion),

                                      CONSTRAINT fk_PersonaOrganizacion_Persona
                                          FOREIGN KEY (Id_Persona)
                                              REFERENCES Persona(Id_Persona)
                                              ON DELETE CASCADE,

                                      CONSTRAINT fk_PersonaOrganizacion_Organizacion
                                          FOREIGN KEY (Id_Organizacion)
                                              REFERENCES Organizacion(Id_Organizacion)
                                              ON DELETE CASCADE
);

CREATE TABLE Ponente_Ponencia (
                                  id_Ponente BIGINT NOT NULL,
                                  id_Ponencia BIGINT NOT NULL,

                                  CONSTRAINT pk_Ponente_Ponencia
                                      PRIMARY KEY (Id_Ponente, Id_Ponencia),

                                  CONSTRAINT fk_PP_Ponente
                                      FOREIGN KEY (Id_Ponente)
                                          REFERENCES Ponente(Id_Ponente)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_PP_Ponencia
                                      FOREIGN KEY (Id_Ponencia)
                                          REFERENCES Ponencia(Id_Ponencia)
                                          ON DELETE CASCADE
);


INSERT INTO Persona (
    DNI, Username, Nombre, Ap1, Ap2, Correo,
    Alergenos, Telefono, Ciudad, Pais,
    Foto, Genero, Fecha_Nacimiento, Password,
    Direccion, Redes_Sociales, Web
) VALUES

      ('27458542A','jlopez','Javier','López','Martín','jlopez@gmail.com',
       NULL,'612345678','Sevilla','España',NULL,'Hombre','1992-04-15',
       'Jav!2024','Calle Sol 12','@jlopez','www.javierlopez.es'),

      ('35229856B','mgarcia','María','García','Pérez','maria.gp@hotmail.com',
       NULL,'623456789','Sevilla','España',NULL,'Mujer','1988-05-15',
       'Mgp#889','Avenida Andalucía 45','@mariagp',NULL),

      ('47586924C','cfernandez','Carlos','Fernández','Luengo','carlosf@gmail.com',
       'Lácteos','658956875','Sevilla','España',NULL,'Hombre','1996-06-02',
       'Cf-2025','Calle Luna 10','@cfernandez',NULL),

      ('48273615M','alopez','Ana','López','Ruiz','ana_lopez@gmail.com',
       NULL,'642325487','Málaga','España',NULL,'Mujer','1990-05-20',
       'Ana$2023','Plaza Mayor 3','@anaruiz','www.analopez.net'),



      ('59381724K','dmartin','Diego','Martín','Sosa','die_marso@gmail.com',
       NULL,'655232398','Córdoba','España',NULL,'Hombre','1999-02-12',
       'DieMar222*','Plaza Menor 4','@diegoomar',NULL),

      ('71826354T','julsrod','Julio','Rodríguez','Gutiérrez','julio_rod_guti@gmail.com',
       'Frutos Secos','666777888','Huelva','España',NULL,'Hombre','1996-12-12',
       'julioRd*43','Calle Santa Ana 1','@juliordgt',NULL),

      ('26483917R','jsanchez','Julia','Sánchez','Mateos','julia@gmail.com',
       'Frutos Secos','666777999','Cádiz','España',NULL,'Mujer','1995-05-05',
       'juliaSaaan95*','Calle Amor 3','@juliasanmat',NULL),

      ('83927164P','albasantos','Alba','Santos','Expósito','alba@gmail.com',
       'Lácteos','661122448','Cádiz','España',NULL,'Mujer','1994-01-01',
       'albitaSExpo11*2','Calle Élite 14','@alba0101',NULL),

      ('47192836L','anthonywest','Anthony','West',NULL,'anthony@gmail.com',
       'Fruta','666778844','Madrid','España',NULL,'Hombre','1999-02-03',
       'Anthony789+012','Calle Grande 5','@anthony99',NULL),

      ('35461728X','lrod','Luna','Rodríguez','Noa','lunaa96@gmail.com',
       NULL,'657199741','Sevilla','España',NULL,'Mujer','1996-07-16',
       'ToTheMoonAndBack00*','Calle Asunción 14','@moon9696',NULL),

      ('46998745P','plorar','Pepe','Lora','Reyes','lora11@gmail.com',
       NULL,'666444787','Sevilla','España',NULL,'Hombre','2002-01-12',
       'pepitoPassword*7','Calle Soto 11','@pepelora02',NULL),

      ('78985465O','ogugo','Olga','Gutiérrez','Gonzalez','olgaguti@hotmail.com',
       NULL,'676212122','Almería','España',NULL,'Mujer','2004-12-22',
       'olgaolga004//','Calle Lora 4','@olgaGutii',NULL),

      ('11225578G','alejisan','Alejandra','Jiménez','Sánchez','alejandrajim@gmail.com',
       'Marisco','666887954','Almería','España',NULL,'Mujer','1999-02-13',
       'alejime7878*','Calle Santa Gracia 9','@alejimenez99',NULL);

INSERT INTO Organizacion () VALUES ();
INSERT INTO Organizacion () VALUES ();
INSERT INTO Organizacion () VALUES ();
INSERT INTO Organizacion () VALUES ();
INSERT INTO Organizacion () VALUES ();
INSERT INTO Organizacion () VALUES ();


INSERT INTO Persona_Organizacion (id_Persona, id_Organizacion, Fecha_Inclusion_Organizacion) VALUES
                                                                                                 (1, 1, '2020-06-12'),
                                                                                                 (2, 2, '2019-01-02'),
                                                                                                 (3, 1, '2025-01-01'),
                                                                                                 (4, 1, '2024-03-03'),
                                                                                                 (5, 1, '2023-03-03'),
                                                                                                 (6, 2, '2022-02-14'),
                                                                                                 (7, 3, '2005-05-09'),
                                                                                                 (8, 3, '2010-06-02'),
                                                                                                 (9, 2, '2015-11-07'),
                                                                                                 (10, 2, '2026-01-10'),
                                                                                                 (11, 1, '2020-02-05'),
                                                                                                 (12, 2, '2023-02-23'),
                                                                                                 (13, 3, '2024-09-07');


INSERT INTO Ponente (
    id_Persona, BIO, Especialidad, CV, Nivel_Imparticion
) VALUES
      (1, 'Docente especializado en BBDD con experiencia en SQL, MySQL y PostgreSQL. Imparte formación en ciclos de FP relacionados con informática y desarrollo de aplicaciones, destacando por su enfoque práctico y orientado a proyectos reales. Ha participado en seminarios tecnológicos y proyectos de innovación educativa vinculados a la digitalización y gestión de datos.',
       'BBDD', NULL, 'FP'),

      (2, 'Especialista en programación Java y desarrollo de software, con experiencia docente en FP. Imparte contenidos sobre programación orientada a objetos y desarrollo de aplicaciones mediante un enfoque práctico y orientado a proyectos.',
       'Java', NULL, 'FP'),

      (3, 'Programador universitario con experiencia en desarrollo de software y proyectos académicos relacionados con programación y tecnologías informáticas. Participa en actividades de investigación y divulgación tecnológica.',
       'Programación', NULL, 'Universidad'),

      (4, 'Profesor universitario especializado en bases de datos, con experiencia en SQL, modelado relacional y gestión de sistemas de información. Imparte formación en informática y participa en proyectos tecnológicos y de investigación.',
       'BBDD', NULL, 'Universidad'),

      (5, 'Especialista en JavaScript con experiencia en formación de postgrado, centrado en desarrollo web avanzado (frontend y backend) con tecnologías como Node.js y frameworks modernos.',
       'JS', NULL, 'Postgrados'),

      (6, 'Programador especializado en Python con experiencia docente en FP, centrado en programación básica y desarrollo de aplicaciones mediante un enfoque práctico.',
       'Python', NULL, 'FP'),

      (7, 'Desarrollador frontend con experiencia en FP, especializado en HTML, CSS y JavaScript. Enfocado en la creación de interfaces web responsivas mediante un enfoque práctico.',
       'FrontEnd', NULL, 'FP'),

      (8, 'Profesor de secundaria especializado en informática e inteligencia artificial, con experiencia en la enseñanza de conceptos básicos de IA y su aplicación educativa mediante actividades prácticas.',
       'IA', NULL, 'Secundaria'),

      (9, 'Especialista en ciberseguridad con experiencia en formación de postgrado, centrado en pentesting, seguridad de redes y respuesta ante incidentes en entornos críticos.',
       'Ciberseguridad', NULL, 'Postgrados'),

      (10, 'Especialista en inteligencia artificial que desarrolla soluciones de machine learning y deep learning, y participa en cursos y ponencias sobre IA y sus aplicaciones prácticas.',
       'IA', NULL, 'Otros');


INSERT INTO Tematica (Tema) VALUES
                                ('BBDD'),
                                ('Programación'),
                                ('LDM'),
                                ('IA'),
                                ('Ciberseguridad');
INSERT INTO Evento (
    Nombre, Descripcion, Fecha_Inicio, Fecha_Fin,
    Direccion, Ciudad, Capacidad, Estado, Modalidad, Lugar
) VALUES
      ('InnovaCode: Jornadas de Programación y BBDD', NULL,
       '2026-06-20','2026-06-21',
       'Calle Leonardo Da Vinci, 18','Sevilla',
       150,'Cerrado','Hibrido','TecnoIncubadora Marie Curie'),

      ('Frontend Lab Conference: UX y Desarrollo Moderno', NULL,
       '2026-09-10','2026-09-10',
       'Avenida Reina Mercedes s/n','Sevilla',
       250,'Borrador','Presencial',
       'Escuela Técnica Superior de Ingeniería Informática'),

      ('Transformando el aula con IA', NULL,
       '2026-12-12','2026-12-12',
       'Avenida Alcalde Luis Uruñuela,1','Sevilla',
       300,'Borrador','Hibrido','FIBES'),

      ('Seguridad en Redes Modernas', NULL,
       '2026-11-05','2026-11-05',
       'Avenida Alcalde Luis Uruñuela,1','Sevilla',
       300,'Abierto','Hibrido','FIBES');


INSERT INTO Ponencia (
    id_Evento, id_Tematica, Titulo, Duracion,
    Fecha, Hora, Ubicacion, Sala, Nivel, Tipo, Formato
) VALUES
      (1, 1, 'Diseño y optimización de BBDD en entornos reales: SQL en acción.', 120,
       '2026-06-21','09:00:00','Calle Leonardo Da Vinci, 18','12','Intermedio','Charla','Hibrido'),

      (1, 1, 'Del modelo E/R a PostgreSQL', 90,
       '2026-06-20','09:00:00','Calle Leonardo Da Vinci, 18','5','Avanzado','Charla','Hibrido'),

      (1, 2, 'POO con Java: del aula al proyecto real.', 120,
       '2026-06-20','11:00:00','Calle Leonardo Da Vinci, 18','5','Basico','Taller','Presencial'),

      (1, 2, 'Construyendo apps robustas con Java en entornos educativos.', 120,
       '2026-06-20','16:30:00','Calle Leonardo Da Vinci, 18','5','Intermedio','Mesa','Hibrido'),

      (1, 2, 'Investigación y desarrollo en programación: tendencias actuales.', 60,
       '2026-06-21','12:00:00','Calle Leonardo Da Vinci, 18','12','Avanzado','Mesa','Hibrido'),

      (2, 3, 'UX y desarrollo frontend: creando experiencias de usuario efectivas.', 90,
       '2026-09-10','09:00:00','Avenida Reina Mercedes s/n','10','Intermedio','Taller','Presencial'),

      (2, 3, 'Arquitecturas modernas con JS: Node.js y más allá.', 90,
       '2026-09-10','11:15:00','Avenida Reina Mercedes s/n','10','Intermedio','Taller','Presencial'),

      (1, 2, 'Python desde 0: resolucion de problemas reales.', 120,
       '2026-06-21','16:00:00','Calle Leonardo Da Vinci, 18','12','Basico','Taller','Presencial'),

      (3, 4, 'La IA en el aula: aplicaciones prácticas y educativas.', 60,
       '2026-12-12','10:00:00','Avenida Alcalde Luis Uruñuela,1','1','Basico','Mesa','Hibrido'),

      (4, 5, 'Seguridad en redes modernas: amenazas y soluciones.', 120,
       '2026-11-05','10:00:00','Avenida Alcalde Luis Uruñuela,1','3','Avanzado','Mesa','Hibrido');


INSERT INTO Ponente_Ponencia (id_Ponente, id_Ponencia) VALUES
                                                           (1, 1),
                                                           (1, 2),
                                                           (2, 3),
                                                           (3, 4),
                                                           (5, 7),
                                                           (6, 8),
                                                           (7, 6),
                                                           (8, 9),
                                                           (10, 9),
                                                           (9, 10),
                                                           (2, 5),
                                                           (3, 5),
                                                           (6, 5);