--Esta es la base de datos completa del proyecto general unificado

DROP DATABASE IF EXISTS dashboards;

create database dashboards;
use dashboards;

CREATE USER IF NOT EXISTS 'alumno'@'localhost' IDENTIFIED BY 'alumnodam#1234';
GRANT ALL PRIVILEGES ON *.* TO 'alumno'@'localhost';
FLUSH PRIVILEGES;

-- =============================================================
--  MÓDULO 1: PERSONAS, EVENTOS Y PONENCIAS
-- =============================================================

CREATE TABLE Persona (
                         id_Persona        BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         DNI               VARCHAR(15)   NOT NULL UNIQUE,
                         Username          VARCHAR(50)   NOT NULL UNIQUE,
                         Nombre            VARCHAR(50)   NOT NULL,
                         Ap1               VARCHAR(50)   NOT NULL,
                         Ap2               VARCHAR(50),
                         Correo            VARCHAR(100)  NOT NULL,
                         Alergenos         VARCHAR(30),
                         Telefono          VARCHAR(15),
                         Ciudad            VARCHAR(50),
                         Pais              VARCHAR(50),
                         Foto              VARCHAR(255),
                         Genero            VARCHAR(20),
                         Fecha_Nacimiento  DATE,
                         Password          VARCHAR(50),
                         Direccion         VARCHAR(100),
                         Redes_Sociales    VARCHAR(100),
                         Web               VARCHAR(100),
                         CONSTRAINT ck_Correo
                             CHECK (Correo REGEXP '^[^[:space:]]+@[^[:space:]]+\\.[^[:space:]]+$'),
    CONSTRAINT ck_Telefono
        CHECK (Telefono IS NULL OR Telefono REGEXP '^[0-9]{9}$'),
    CONSTRAINT ck_Alergenos
        CHECK (Alergenos IS NULL OR Alergenos IN (
            'Frutos Secos','Lácteos','Gluten','Aguacate',
            'Marisco','Fruta','Otro'
        ))
);

CREATE TABLE Organizacion (
                              id_Organizacion BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE Tematica (
                          id_Tematica BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          Tema        VARCHAR(100) NOT NULL
);

CREATE TABLE Evento (
                        id_Evento    BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        Nombre       VARCHAR(100)  NOT NULL,
                        Descripcion  VARCHAR(500),
                        Fecha_Inicio DATE          NOT NULL,
                        Fecha_Fin    DATE,
                        Direccion    VARCHAR(100),
                        Ciudad       VARCHAR(50),
                        Capacidad    INT,
                        Estado       VARCHAR(20)   NOT NULL,
                        Modalidad    VARCHAR(20)   NOT NULL,
                        Lugar        VARCHAR(100),
                        CONSTRAINT ck_Estado
                            CHECK (Estado IN ('Borrador','Abierto','Cerrado','Cancelado','Pospuesto')),
                        CONSTRAINT ck_Modalidad
                            CHECK (Modalidad IN ('Presencial','Online','Hibrido')),
                        CONSTRAINT ck_Capacidad
                            CHECK (Capacidad IS NULL OR Capacidad > 0)
);

CREATE TABLE Ponente (
                         id_Ponente         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         id_Persona         BIGINT       NOT NULL,
                         BIO                VARCHAR(500),
                         Especialidad       VARCHAR(50)  NOT NULL,
                         CV                 VARCHAR(100),
                         Nivel_Imparticion  VARCHAR(20)  NOT NULL,
                         CONSTRAINT fk_Ponente_Persona
                             FOREIGN KEY (id_Persona) REFERENCES Persona(id_Persona) ON DELETE CASCADE,
                         CONSTRAINT ck_Nivel_Imparticion
                             CHECK (Nivel_Imparticion IN (
                                                          'Infantil','Primaria','Secundaria',
                                                          'Bachillerato','FP','Universidad',
                                                          'Postgrados','Otros'
                                 ))
);

CREATE TABLE Ponencia (
                          id_Ponencia  BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          id_Evento    BIGINT        NOT NULL,
                          id_Tematica  BIGINT        NOT NULL,
                          Titulo       VARCHAR(150)  NOT NULL,
                          Duracion     INT,
                          Fecha        DATE,
                          Hora         VARCHAR(10),
                          Ubicacion    VARCHAR(100),
                          Sala         VARCHAR(50),
                          Nivel        VARCHAR(20),
                          Tipo         VARCHAR(20)   NOT NULL,
                          Formato      VARCHAR(20)   NOT NULL,
                          CONSTRAINT fk_Ponencia_Evento
                              FOREIGN KEY (id_Evento) REFERENCES Evento(id_Evento) ON DELETE CASCADE,
                          CONSTRAINT fk_Ponencia_Tematica
                              FOREIGN KEY (id_Tematica) REFERENCES Tematica(id_Tematica) ON DELETE CASCADE,
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
                                      id_Persona                   BIGINT NOT NULL,
                                      id_Organizacion              BIGINT NOT NULL,
                                      Fecha_Inclusion_Organizacion DATE,
                                      CONSTRAINT pk_Persona_Organizacion
                                          PRIMARY KEY (id_Persona, id_Organizacion),
                                      CONSTRAINT fk_PersonaOrganizacion_Persona
                                          FOREIGN KEY (id_Persona) REFERENCES Persona(id_Persona) ON DELETE CASCADE,
                                      CONSTRAINT fk_PersonaOrganizacion_Organizacion
                                          FOREIGN KEY (id_Organizacion) REFERENCES Organizacion(id_Organizacion) ON DELETE CASCADE
);

CREATE TABLE Ponente_Ponencia (
                                  id_Ponente  BIGINT NOT NULL,
                                  id_Ponencia BIGINT NOT NULL,
                                  CONSTRAINT pk_Ponente_Ponencia
                                      PRIMARY KEY (id_Ponente, id_Ponencia),
                                  CONSTRAINT fk_PP_Ponente
                                      FOREIGN KEY (id_Ponente) REFERENCES Ponente(id_Ponente) ON DELETE CASCADE,
                                  CONSTRAINT fk_PP_Ponencia
                                      FOREIGN KEY (id_Ponencia) REFERENCES Ponencia(id_Ponencia) ON DELETE CASCADE
);

-- =============================================================
--  MÓDULO 2: TICKETS, PRODUCTOS Y ENTRADAS
-- =============================================================

CREATE TABLE Asistente (
                           ID_Asistente      BIGINT         PRIMARY KEY,
                           Tematica          VARCHAR(64),
                           Direccion         VARCHAR(128),
                           Observaciones     VARCHAR(256),
                           Bio               VARCHAR(512),
                           Total_Gastado     DECIMAL(10,2),
                           Nivel_Imparticion VARCHAR(32)
);

CREATE TABLE Ticket (
                        ID_Ticket          BIGINT        PRIMARY KEY,
                        ID_Asistente       BIGINT,
                        Codigo_QR          VARCHAR(32),
                        Fecha_Compra       DATE,
                        Precio_Final       DECIMAL(10,2),
                        Metodo_Pago        VARCHAR(32),
                        Descuento          DECIMAL(10,2),
                        Codigo_Promocional VARCHAR(32),
                        FOREIGN KEY (ID_Asistente) REFERENCES Asistente(ID_Asistente)
);

CREATE TABLE Producto (
                          ID_Producto          BIGINT        PRIMARY KEY,
                          Nombre_Producto      VARCHAR(64),
                          Precio               DECIMAL(10,2),
                          Stock_Disponible     INT,
                          Descripcion_Producto VARCHAR(256),
                          Tipo_IVA             VARCHAR(10),
                          Descuento            DECIMAL(10,2)
);

CREATE TABLE Linea_Ticket (
                              ID_LineaTicket BIGINT        PRIMARY KEY,
                              ID_Ticket      BIGINT,
                              ID_Producto    BIGINT        NOT NULL,
                              Cantidad       INT,
                              IVA_Cuota      DECIMAL(10,2),
                              Subtotal_Base  DECIMAL(10,2),
                              Total_Linea    DECIMAL(10,2),
                              FOREIGN KEY (ID_Ticket)   REFERENCES Ticket(ID_Ticket),
                              FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto)
);

CREATE TABLE Entrada (
                         ID_Entrada          BIGINT       PRIMARY KEY,
                         ID_Producto         BIGINT,
                         Zona                VARCHAR(32),
                         Asiento             VARCHAR(16),
                         Acceso_Permitido    VARCHAR(64),
                         Accesos_Adicionales VARCHAR(128),
                         Validez_Horas       INT,
                         EstadoEntrada       VARCHAR(32),
                         FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto)
);

CREATE TABLE VIP (
                     ID_Vip                BIGINT        PRIMARY KEY,
                     ID_Entrada            BIGINT        NOT NULL,
                     Beneficios_Incluidos  VARCHAR(256),
                     Servicios_Adicionales VARCHAR(256),
                     Precio_Extra          DECIMAL(8,2),
                     FOREIGN KEY (ID_Entrada) REFERENCES Entrada(ID_Entrada)
);

CREATE TABLE Estandar (
                          ID_Estandar    BIGINT       PRIMARY KEY,
                          ID_Entrada     BIGINT       NOT NULL,
                          Incluye_Regalo VARCHAR(256),
                          FOREIGN KEY (ID_Entrada) REFERENCES Entrada(ID_Entrada)
);

CREATE TABLE Beca (
                      ID_Beca              BIGINT        PRIMARY KEY,
                      ID_Entrada           BIGINT        NOT NULL,
                      Motivo_Beca          VARCHAR(128),
                      Porcentaje_Descuento DECIMAL(5,2),
                      Requisitos           VARCHAR(256),
                      FOREIGN KEY (ID_Entrada) REFERENCES Entrada(ID_Entrada)
);

CREATE TABLE Textil (
                        ID_Textil   BIGINT       PRIMARY KEY,
                        ID_Producto BIGINT,
                        Talla       VARCHAR(8),
                        Color       VARCHAR(32),
                        Material    VARCHAR(64),
                        Genero      VARCHAR(32),
                        Tipo_Textil VARCHAR(32),
                        FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto)
);

CREATE TABLE Camisetas (
                           ID_Camisetas BIGINT       PRIMARY KEY,
                           ID_Textil    BIGINT       NOT NULL,
                           Modelo       VARCHAR(64),
                           Estampado    VARCHAR(64),
                           FOREIGN KEY (ID_Textil) REFERENCES Textil(ID_Textil)
);

CREATE TABLE Accesorios (
                            ID_Accesorios  BIGINT      PRIMARY KEY,
                            ID_Textil      BIGINT      NOT NULL,
                            Tipo_Accesorio VARCHAR(64),
                            FOREIGN KEY (ID_Textil) REFERENCES Textil(ID_Textil)
);

CREATE TABLE Otros (
                       ID_Otros    BIGINT       PRIMARY KEY,
                       ID_Producto BIGINT       NOT NULL,
                       Descripcion VARCHAR(256),
                       FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto)
);

-- =============================================================
--  MÓDULO 3: CRM (USUARIOS, FORMULARIOS, FICHAS)
-- =============================================================

CREATE TABLE Usuario (
                         Id_Usuario    INT          PRIMARY KEY,
                         Nombre        VARCHAR(64)  NOT NULL,
                         Apellidos     VARCHAR(128),
                         Email         VARCHAR(128) UNIQUE NOT NULL,
                         Password_Hash VARCHAR(256) NOT NULL,
                         Rol           VARCHAR(32),
                         Fecha_Registro DATE        DEFAULT (CURRENT_DATE)
);

CREATE TABLE PerfilUsuario (
                               Id_Perfil   INT          PRIMARY KEY,
                               Id_Usuario  INT          NOT NULL,
                               Foto_Perfil VARCHAR(256),
                               Bio         VARCHAR(512),
                               CONSTRAINT fk_perfil_usuario
                                   FOREIGN KEY (Id_Usuario) REFERENCES Usuario(Id_Usuario) ON DELETE CASCADE
);

CREATE TABLE TipoPagina (
                            Id_Tipo_Pagina INT         PRIMARY KEY,
                            Nombre_Tipo    VARCHAR(32) NOT NULL,
                            Descripcion    VARCHAR(128)
);

CREATE TABLE PaginaWeb (
                           Id_Pagina         INT          PRIMARY KEY,
                           Titulo            VARCHAR(128) NOT NULL,
                           Url               VARCHAR(128) UNIQUE NOT NULL,
                           Contenido_HTML    TEXT,
                           Fecha_Creacion    DATE         DEFAULT (CURRENT_DATE),
                           Fecha_Modificacion DATE,
                           Id_Tipo_Pagina    INT          NOT NULL,
                           CONSTRAINT fk_pagina_tipo
                               FOREIGN KEY (Id_Tipo_Pagina) REFERENCES TipoPagina(Id_Tipo_Pagina)
);

CREATE TABLE Formulario (
                            Id_Formulario    INT          PRIMARY KEY,
                            Nombre_Formulario VARCHAR(100) NOT NULL,
                            Descripcion      VARCHAR(300),
                            Ruta_Action      VARCHAR(150) NOT NULL,
                            Metodo           VARCHAR(10)  NOT NULL,
                            Fecha_Creacion   DATE         DEFAULT (CURRENT_DATE)
);

CREATE TABLE PaginaFormulario (
                                  Id_Pagina_Formulario INT PRIMARY KEY,
                                  Id_Pagina            INT NOT NULL,
                                  Id_Formulario        INT NOT NULL,
                                  CONSTRAINT fk_pagina_formulario_pagina
                                      FOREIGN KEY (Id_Pagina) REFERENCES PaginaWeb(Id_Pagina) ON DELETE CASCADE,
                                  CONSTRAINT fk_pagina_formulario_formulario
                                      FOREIGN KEY (Id_Formulario) REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE
);

CREATE TABLE CampoFormulario (
                                 Id_Campo      INT          PRIMARY KEY,
                                 Id_Formulario INT          NOT NULL,
                                 Nombre_Campo  VARCHAR(100) NOT NULL,
                                 Etiqueta      VARCHAR(100) NOT NULL,
                                 Tipo_Input    VARCHAR(50)  NOT NULL,
                                 Obligatorio   TINYINT      NOT NULL,
                                 Validacion    VARCHAR(150),
                                 Opciones      VARCHAR(300),
                                 CONSTRAINT fk_campo_formulario
                                     FOREIGN KEY (Id_Formulario) REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE
);

CREATE TABLE FichaCliente (
                              Id_Ficha        INT          PRIMARY KEY,
                              Nombre          VARCHAR(100) NOT NULL,
                              Apellidos       VARCHAR(150),
                              Email           VARCHAR(150) UNIQUE NOT NULL,
                              Telefono        VARCHAR(20),
                              Empresa_Centro  VARCHAR(150),
                              Observaciones   VARCHAR(300),
                              Fecha_Alta      DATE         DEFAULT (CURRENT_DATE)
);

CREATE TABLE RespuestaFormulario (
                                     Id_Respuesta   INT PRIMARY KEY,
                                     Id_Formulario  INT NOT NULL,
                                     Id_Ficha       INT NOT NULL,
                                     Fecha_Respuesta DATE DEFAULT (CURRENT_DATE),
                                     CONSTRAINT fk_respuesta_formulario
                                         FOREIGN KEY (Id_Formulario) REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE,
                                     CONSTRAINT fk_respuesta_ficha
                                         FOREIGN KEY (Id_Ficha) REFERENCES FichaCliente(Id_Ficha) ON DELETE CASCADE
);

CREATE TABLE FormularioFichaCliente (
                                        Id_Formulario_Ficha INT PRIMARY KEY,
                                        Id_Formulario       INT NOT NULL,
                                        Id_Ficha            INT NOT NULL,
                                        CONSTRAINT fk_formulario_ficha_formulario
                                            FOREIGN KEY (Id_Formulario) REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE,
                                        CONSTRAINT fk_formulario_ficha_cliente
                                            FOREIGN KEY (Id_Ficha) REFERENCES FichaCliente(Id_Ficha) ON DELETE CASCADE
);

CREATE TABLE PaginaFichaCliente (
                                    Id_Pagina_Ficha INT PRIMARY KEY,
                                    Id_Pagina       INT NOT NULL,
                                    Id_Ficha        INT NOT NULL,
                                    CONSTRAINT fk_pagina_ficha_pagina
                                        FOREIGN KEY (Id_Pagina) REFERENCES PaginaWeb(Id_Pagina) ON DELETE CASCADE,
                                    CONSTRAINT fk_pagina_ficha_cliente
                                        FOREIGN KEY (Id_Ficha) REFERENCES FichaCliente(Id_Ficha) ON DELETE CASCADE
);

CREATE TABLE FormularioOrganizacion (
                                        Id_Formulario_Organizacion INT AUTO_INCREMENT PRIMARY KEY,
                                        Id_Formulario              INT          NOT NULL,
                                        Nombre                     VARCHAR(100) NOT NULL,
                                        Direccion                  VARCHAR(200),
                                        Telefono                   VARCHAR(20),
                                        Email                      VARCHAR(150) NOT NULL,
                                        Tipo_Organizacion          VARCHAR(50)  NOT NULL,
                                        CONSTRAINT fk_form_org_formulario
                                            FOREIGN KEY (Id_Formulario) REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE
);

CREATE TABLE FormularioOportunidad (
                                       Id_Formulario_Oportunidad INT AUTO_INCREMENT PRIMARY KEY,
                                       Id_Formulario             INT          NOT NULL,
                                       Titulo                    VARCHAR(100) NOT NULL,
                                       Descripcion               VARCHAR(300),
                                       Fecha_Inicio              DATE         NOT NULL,
                                       Tipos_Oportunidad         VARCHAR(200) NOT NULL,
                                       CONSTRAINT fk_form_op_formulario
                                           FOREIGN KEY (Id_Formulario) REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE
);

CREATE TABLE FormularioProducto (
                                    Id_Formulario_Producto INT AUTO_INCREMENT PRIMARY KEY,
                                    Id_Formulario          INT           NOT NULL,
                                    Nombre                 VARCHAR(100)  NOT NULL,
                                    Descripcion            VARCHAR(300),
                                    Precio                 DECIMAL(10,2) NOT NULL,
                                    Stock                  INT           NOT NULL,
                                    Categoria              VARCHAR(100)  NOT NULL,
                                    CONSTRAINT fk_form_prod_formulario
                                        FOREIGN KEY (Id_Formulario) REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE
);

-- =============================================================
--  MÓDULO 4: HISTORIAL, OPORTUNIDADES Y ORGANIZACIÓN (CRM)
-- =============================================================

CREATE TABLE Historial (
                           ID_Historial INT  PRIMARY KEY,
                           Iteraciones  INT,
                           Fecha_Inicio DATE,
                           Fecha_Fin    DATE
);

CREATE TABLE OrganizacionCRM (
                                 ID_Organizacion INT          PRIMARY KEY,
                                 Nombre          VARCHAR(100),
                                 Direccion       VARCHAR(150),
                                 Telefono        VARCHAR(20),
                                 Email           VARCHAR(100),
                                 Ciudad          VARCHAR(50),
                                 Fecha_Registro  DATE,
                                 ID_Historial    INT,
                                 CONSTRAINT FK_Organizacion_Historial
                                     FOREIGN KEY (ID_Historial) REFERENCES Historial(ID_Historial)
);

CREATE TABLE Oportunidad (
                             ID_Oportunidad INT            PRIMARY KEY,
                             Descripcion    VARCHAR(200),
                             Fecha_Inicio   DATE,
                             Fecha_Fin      DATE,
                             Estado         VARCHAR(50),
                             Presupuesto    DECIMAL(15,2),
                             ID_Persona     INT,
                             ID_Historial   INT,
                             CONSTRAINT FK_Oportunidad_Historial
                                 FOREIGN KEY (ID_Historial) REFERENCES Historial(ID_Historial),
                             CONSTRAINT chk_estado
                                 CHECK (Estado IN ('Aprobado','Rechazado','Pendiente'))
);

CREATE TABLE Colaboracion (
                              ID_Colaboracion INT            PRIMARY KEY,
                              Tipo            VARCHAR(200),
                              Fecha           DATE,
                              Firma           VARCHAR(2),
                              Dinero          DECIMAL(15,2),
                              Convenio        VARCHAR(200),
                              ID_Historial    INT,
                              CONSTRAINT FK_COLABORACION_HISTORIAL
                                  FOREIGN KEY (ID_Historial) REFERENCES Historial(ID_Historial),
                              CONSTRAINT chk_colaboracion_firma
                                  CHECK (Firma IN ('SI','NO'))
);

CREATE TABLE Actividad (
                           ID_Actividad INT            PRIMARY KEY,
                           Descripcion  VARCHAR(200),
                           Fecha        DATE,
                           Dinero       DECIMAL(15,2),
                           Firma        VARCHAR(2),
                           ID_Historial INT,
                           CONSTRAINT FK_ACTIVIDAD_HISTORIAL
                               FOREIGN KEY (ID_Historial) REFERENCES Historial(ID_Historial),
                           CONSTRAINT chk_actividad_firma
                               CHECK (Firma IN ('SI','NO'))
);

CREATE TABLE Patrocinio (
                            ID_Patrocinio INT        PRIMARY KEY,
                            Firma         VARCHAR(2),
                            Tipo          VARCHAR(200),
                            ID_Historial  INT,
                            CONSTRAINT FK_PATROCINIO_HISTORIAL
                                FOREIGN KEY (ID_Historial) REFERENCES Historial(ID_Historial),
                            CONSTRAINT chk_patrocinio_firma
                                CHECK (Firma IN ('SI','NO')),
                            CONSTRAINT chk_tipo_patrocinio
                                CHECK (Tipo IN ('Bronce','Plata','Oro'))
);

CREATE TABLE Recinto (
                         ID_Recinto INT          PRIMARY KEY,
                         Nombre     VARCHAR(200),
                         Capacidad  INT,
                         Ubicacion  VARCHAR(200)
);

CREATE TABLE Administracion (
                                id_administracion INT AUTO_INCREMENT PRIMARY KEY,
                                Ambito            VARCHAR(100),
                                Presupuesto       DECIMAL(15,2)
);

CREATE TABLE Empresa (
                         id_empresa   INT AUTO_INCREMENT PRIMARY KEY,
                         Sector       VARCHAR(100),
                         Num_Empleados INT
);

CREATE TABLE Centro_Educativo (
                                  id_centro   INT AUTO_INCREMENT PRIMARY KEY,
                                  Tipo_Centro VARCHAR(100),
                                  Num_Alumnos INT
);

CREATE TABLE Asociacion (
                            id_asociacion INT AUTO_INCREMENT PRIMARY KEY,
                            Finalidad     VARCHAR(150),
                            Num_Socios    INT
);

CREATE TABLE Ayuntamiento (
                              id_ayuntamiento INT AUTO_INCREMENT PRIMARY KEY,
                              Provincia       VARCHAR(100),
                              Alcalde         VARCHAR(150)
);

-- =============================================================
--  MÓDULO 5: LOGÍSTICA (PROVEEDORES, MERCANCÍA, PEDIDOS)
-- =============================================================

CREATE TABLE Proveedor (
                           id_proveedor          INT          PRIMARY KEY,
                           nombre                VARCHAR(100),
                           direccion             VARCHAR(100),
                           telefono              VARCHAR(20),
                           email                 VARCHAR(100),
                           CIF                   VARCHAR(20),
                           pais                  VARCHAR(50),
                           fecha_alta            DATE,
                           estado                VARCHAR(20)
);

CREATE TABLE Mercancia (
                           id_mercancia    INT           PRIMARY KEY,
                           descripcion     VARCHAR(100),
                           categoria       VARCHAR(50),
                           precio_unitario DECIMAL(10,2),
                           stock_minimo    INT,
                           stock_actual    INT,
                           fecha_creacion  DATE
);

CREATE TABLE Pedido (
                        id_pedido              INT         PRIMARY KEY,
                        fecha_pedido           DATE,
                        fecha_entrega_prevista DATE,
                        id_proveedor           INT,
                        estado_pedido          VARCHAR(20),
                        FOREIGN KEY (id_proveedor) REFERENCES Proveedor(id_proveedor)
);

CREATE TABLE Linea_Pedido (
                              id_linea_pedido    INT           PRIMARY KEY,
                              id_pedido          INT,
                              id_mercancia       INT,
                              cantidad           INT,
                              precio_unitario    DECIMAL(10,2),
                              descuento_aplicado DECIMAL(5,2),
                              FOREIGN KEY (id_pedido)    REFERENCES Pedido(id_pedido),
                              FOREIGN KEY (id_mercancia) REFERENCES Mercancia(id_mercancia)
);

CREATE TABLE Albaran (
                         id_albaran       INT         PRIMARY KEY,
                         fecha_albaran    DATE,
                         estado           VARCHAR(30),
                         id_pedido        INT,
                         numero_factura   VARCHAR(50),
                         transportista    VARCHAR(50),
                         fecha_recepcion  DATE,
                         FOREIGN KEY (id_pedido) REFERENCES Pedido(id_pedido)
);

CREATE TABLE Linea_Albaran (
                               id_linea_albaran    INT         PRIMARY KEY,
                               id_albaran          INT,
                               id_mercancia        INT,
                               cantidad_recibida   INT,
                               estado_producto     VARCHAR(30),
                               lote                VARCHAR(50),
                               fecha_caducidad     DATE,
                               diferencia_cantidad INT,
                               FOREIGN KEY (id_albaran)   REFERENCES Albaran(id_albaran),
                               FOREIGN KEY (id_mercancia) REFERENCES Mercancia(id_mercancia)
);

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================
--  INSERTS — MÓDULO 1: PERSONAS, EVENTOS Y PONENCIAS
-- =============================================================

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
                                                                                                 (1,  1, '2020-06-12'),
                                                                                                 (2,  2, '2019-01-02'),
                                                                                                 (3,  1, '2025-01-01'),
                                                                                                 (4,  1, '2024-03-03'),
                                                                                                 (5,  1, '2023-03-03'),
                                                                                                 (6,  2, '2022-02-14'),
                                                                                                 (7,  3, '2005-05-09'),
                                                                                                 (8,  3, '2010-06-02'),
                                                                                                 (9,  2, '2015-11-07'),
                                                                                                 (10, 2, '2026-01-10'),
                                                                                                 (11, 1, '2020-02-05'),
                                                                                                 (12, 2, '2023-02-23'),
                                                                                                 (13, 3, '2024-09-07');

INSERT INTO Ponente (id_Persona, BIO, Especialidad, CV, Nivel_Imparticion) VALUES
                                                                               (1,  'Docente especializado en BBDD con experiencia en SQL, MySQL y PostgreSQL. Imparte formación en ciclos de FP relacionados con informática y desarrollo de aplicaciones, destacando por su enfoque práctico y orientado a proyectos reales. Ha participado en seminarios tecnológicos y proyectos de innovación educativa vinculados a la digitalización y gestión de datos.', 'BBDD', NULL, 'FP'),
                                                                               (2,  'Especialista en programación Java y desarrollo de software, con experiencia docente en FP. Imparte contenidos sobre programación orientada a objetos y desarrollo de aplicaciones mediante un enfoque práctico y orientado a proyectos.', 'Java', NULL, 'FP'),
                                                                               (3,  'Programador universitario con experiencia en desarrollo de software y proyectos académicos relacionados con programación y tecnologías informáticas. Participa en actividades de investigación y divulgación tecnológica.', 'Programación', NULL, 'Universidad'),
                                                                               (4,  'Profesor universitario especializado en bases de datos, con experiencia en SQL, modelado relacional y gestión de sistemas de información. Imparte formación en informática y participa en proyectos tecnológicos y de investigación.', 'BBDD', NULL, 'Universidad'),
                                                                               (5,  'Especialista en JavaScript con experiencia en formación de postgrado, centrado en desarrollo web avanzado (frontend y backend) con tecnologías como Node.js y frameworks modernos.', 'JS', NULL, 'Postgrados'),
                                                                               (6,  'Programador especializado en Python con experiencia docente en FP, centrado en programación básica y desarrollo de aplicaciones mediante un enfoque práctico.', 'Python', NULL, 'FP'),
                                                                               (7,  'Desarrollador frontend con experiencia en FP, especializado en HTML, CSS y JavaScript. Enfocado en la creación de interfaces web responsivas mediante un enfoque práctico.', 'FrontEnd', NULL, 'FP'),
                                                                               (8,  'Profesor de secundaria especializado en informática e inteligencia artificial, con experiencia en la enseñanza de conceptos básicos de IA y su aplicación educativa mediante actividades prácticas.', 'IA', NULL, 'Secundaria'),
                                                                               (9,  'Especialista en ciberseguridad con experiencia en formación de postgrado, centrado en pentesting, seguridad de redes y respuesta ante incidentes en entornos críticos.', 'Ciberseguridad', NULL, 'Postgrados'),
                                                                               (10, 'Especialista en inteligencia artificial que desarrolla soluciones de machine learning y deep learning, y participa en cursos y ponencias sobre IA y sus aplicaciones prácticas.', 'IA', NULL, 'Otros');

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
       '2026-06-20','2026-06-21','Calle Leonardo Da Vinci, 18','Sevilla',
       150,'Cerrado','Hibrido','TecnoIncubadora Marie Curie'),

      ('Frontend Lab Conference: UX y Desarrollo Moderno', NULL,
       '2026-09-10','2026-09-10','Avenida Reina Mercedes s/n','Sevilla',
       250,'Borrador','Presencial','Escuela Técnica Superior de Ingeniería Informática'),

      ('Transformando el aula con IA', NULL,
       '2026-12-12','2026-12-12','Avenida Alcalde Luis Uruñuela,1','Sevilla',
       300,'Borrador','Hibrido','FIBES'),

      ('Seguridad en Redes Modernas', NULL,
       '2026-11-05','2026-11-05','Avenida Alcalde Luis Uruñuela,1','Sevilla',
       300,'Abierto','Hibrido','FIBES');

INSERT INTO Ponencia (
    id_Evento, id_Tematica, Titulo, Duracion,
    Fecha, Hora, Ubicacion, Sala, Nivel, Tipo, Formato
) VALUES
      (1, 1, 'Diseño y optimización de BBDD en entornos reales: SQL en acción.', 120, '2026-06-21','09:00:00','Calle Leonardo Da Vinci, 18','12','Intermedio','Charla','Hibrido'),
      (1, 1, 'Del modelo E/R a PostgreSQL', 90, '2026-06-20','09:00:00','Calle Leonardo Da Vinci, 18','5','Avanzado','Charla','Hibrido'),
      (1, 2, 'POO con Java: del aula al proyecto real.', 120, '2026-06-20','11:00:00','Calle Leonardo Da Vinci, 18','5','Basico','Taller','Presencial'),
      (1, 2, 'Construyendo apps robustas con Java en entornos educativos.', 120, '2026-06-20','16:30:00','Calle Leonardo Da Vinci, 18','5','Intermedio','Mesa','Hibrido'),
      (1, 2, 'Investigación y desarrollo en programación: tendencias actuales.', 60, '2026-06-21','12:00:00','Calle Leonardo Da Vinci, 18','12','Avanzado','Mesa','Hibrido'),
      (2, 3, 'UX y desarrollo frontend: creando experiencias de usuario efectivas.', 90, '2026-09-10','09:00:00','Avenida Reina Mercedes s/n','10','Intermedio','Taller','Presencial'),
      (2, 3, 'Arquitecturas modernas con JS: Node.js y más allá.', 90, '2026-09-10','11:15:00','Avenida Reina Mercedes s/n','10','Intermedio','Taller','Presencial'),
      (1, 2, 'Python desde 0: resolucion de problemas reales.', 120, '2026-06-21','16:00:00','Calle Leonardo Da Vinci, 18','12','Basico','Taller','Presencial'),
      (3, 4, 'La IA en el aula: aplicaciones prácticas y educativas.', 60, '2026-12-12','10:00:00','Avenida Alcalde Luis Uruñuela,1','1','Basico','Mesa','Hibrido'),
      (4, 5, 'Seguridad en redes modernas: amenazas y soluciones.', 120, '2026-11-05','10:00:00','Avenida Alcalde Luis Uruñuela,1','3','Avanzado','Mesa','Hibrido');

INSERT INTO Ponente_Ponencia (id_Ponente, id_Ponencia) VALUES
                                                           (1, 1),(1, 2),(2, 3),(3, 4),(5, 7),
                                                           (6, 8),(7, 6),(8, 9),(10, 9),(9, 10),
                                                           (2, 5),(3, 5),(6, 5);

-- =============================================================
--  INSERTS — MÓDULO 2: TICKETS, PRODUCTOS Y ENTRADAS
-- =============================================================

INSERT INTO Asistente VALUES
                          (1, 'Música',      'Calle Melodía 12',  'Alérgico al humo', 'Productor musical',   150.00, 'Experto'),
                          (2, 'Tecnología',  'Av. Digital 404',   'Requiere silla',   'Desarrollador Senior',260.00, 'Medio'),
                          (3, 'Arte',        'Plaza Pintor 5',    'VIP recurrente',   'Escultor novel',       45.00, 'Básico'),
                          (4, 'Marketing',   'Gran Vía 88',       'Ninguna',          'Growth Hacker',       120.00, 'Experto'),
                          (5, 'Literatura',  'Calle Verso 9',     'Estudiante',       'Poeta en activo',       0.00, 'Medio'),
                          (6, 'Deporte',     'Polideportivo 1',   'Atleta PRO',       'Entrenador',          210.00, 'Experto'),
                          (7, 'Cine',        'Av. Hollywood 7',   'Prensa',           'Crítico de cine',      85.00, 'Medio'),
                          (8, 'Moda',        'Pasarela 10',       'Influencer',       'Diseñador',           170.00, 'Básico');

INSERT INTO Ticket VALUES
                       (100, 1, 'QR-8821', '2024-05-10', 150.00, 'Tarjeta',       0.00, NULL),
                       (101, 2, 'QR-9932', '2024-05-11', 260.00, 'PayPal',         0.00, NULL),
                       (102, 3, 'QR-1122', '2024-05-11',  45.00, 'Efectivo',       5.00, 'BIENVENIDA'),
                       (103, 4, 'QR-4455', '2024-05-12', 120.00, 'Tarjeta',        0.00, NULL),
                       (104, 6, 'QR-6677', '2024-05-12', 210.00, 'PayPal',        10.00, 'PROMO_DEPOR'),
                       (105, 7, 'QR-7788', '2024-05-13',  85.00, 'PayPal',         0.00, NULL),
                       (106, 8, 'QR-8899', '2024-05-14', 170.00, 'Transferencia',  0.00, NULL);

INSERT INTO Producto VALUES
                         (1, 'Pase VIP Premium',  150.00,  25, 'Acceso total y hotel', '21%',  0.00),
                         (2, 'Entrada Estándar',   85.00, 150, 'Acceso un día',        '21%',  0.00),
                         (3, 'Pase Beca Joven',    45.00,  10, 'Solo estudiantes',     '21%', 20.00),
                         (4, 'Camiseta Oficial',   25.00, 100, 'Algodón orgánico',     '21%',  0.00),
                         (5, 'Gorra Evento',       15.00, 200, 'Talla única',          '21%',  0.00),
                         (6, 'Poster Firmado',     10.00,  50, 'Coleccionista',        '21%',  0.00);

INSERT INTO Linea_Ticket VALUES
                             (501, 100, 1, 1, 26.03, 123.97, 150.00),
                             (502, 101, 2, 2, 29.50, 140.50, 170.00),
                             (503, 101, 4, 2,  8.68,  41.32,  50.00),
                             (504, 101, 5, 1,  5.21,  24.79,  30.00),
                             (505, 102, 3, 1,  4.09,  40.91,  45.00),
                             (506, 104, 1, 1, 26.03, 123.97, 150.00),
                             (507, 104, 4, 2,  8.68,  41.32,  50.00);

INSERT INTO Entrada VALUES
                        (301, 1, 'Palco O',    'Sofá 1', 'Todo el recinto',   'Catering, Masaje, Grabación, Meet&Greet', 96, 'Activa'),
                        (302, 2, 'Preferente', 'P-10',   'Escenario A y B',   'Zona de Descanso, Wi-Fi Premium',         24, 'Activa'),
                        (303, 3, 'Pista',      'Libre',  'Talleres infantiles','Parque de juegos, Merienda',              12, 'Pendiente'),
                        (304, 4, 'Grada',      'G-01',   'Escenario Principal','Consigna gratuita',                       12, 'Activa');

INSERT INTO VIP VALUES
                    (200, 301, 'Kit de prensa, Vinilo firmado',   'Acceso a pruebas de sonido',      100.00),
                    (201, 302, 'Fast track, Asiento reservado',   'Suscripción 1 año revista',        50.00);

INSERT INTO Estandar VALUES
                         (300, 303, 'Camiseta básica'),
                         (301, 304, 'No incluye (Entrada básica)');

INSERT INTO Beca VALUES
                     (400, 301, 'Inclusión Social',    80.00, 'Certificado Discapacidad'),
                     (401, 302, 'Talento Emergente',  100.00, 'Ganador concurso local');

INSERT INTO Textil VALUES
                       (20, 4, 'XL',    'Negro', 'Algodón',   'Hombre', 'Camiseta'),
                       (21, 4, 'S',     'Blanco','Algodón',   'Mujer',  'Camiseta'),
                       (22, 5, 'Única', 'Azul',  'Poliéster', 'Unisex', 'Gorra');

INSERT INTO Camisetas VALUES
                          (1, 20, 'Oversize',  'Logo Flúor'),
                          (2, 21, 'Slim Fit',  'Logo Minimalista');

INSERT INTO Accesorios VALUES
                           (1000, 22, 'Gorra ajustable metálica'),
                           (1001, 22, 'Pack deportivo'),
                           (1002, 22, 'Maleta de entrenamiento');

INSERT INTO Otros VALUES
                      (200, 6, 'Edición de audífilo con bonus track inédito.'),
                      (201, 5, '15 stickers diseñados por artistas urbanos locales.'),
                      (202, 4, 'Tapa dura, 200 páginas, puntos (bullet journal).'),
                      (203, 3, 'Grabado láser con el mapa del recinto del evento.');

-- =============================================================
--  INSERTS — MÓDULO 3: CRM (USUARIOS, FORMULARIOS, FICHAS)
-- =============================================================

INSERT INTO Usuario (Id_Usuario, Nombre, Apellidos, Email, Password_Hash, Rol, Fecha_Registro) VALUES
                                                                                                   (1, 'Adrián', 'García López',  'adrian@example.com', 'hash123', 'admin',  '2025-01-10'),
                                                                                                   (2, 'Lucía',  'Martín Ruiz',   'lucia@example.com',  'hash456', 'editor', '2025-01-12'),
                                                                                                   (3, 'Carlos', 'Pérez Soto',    'carlos@example.com', 'hash789', 'viewer', '2025-01-15');

INSERT INTO PerfilUsuario (Id_Perfil, Id_Usuario, Foto_Perfil, Bio) VALUES
                                                                        (1, 1, 'foto1.png', 'Administrador del CRM'),
                                                                        (2, 2, 'foto2.png', 'Editora de contenido'),
                                                                        (3, 3, 'foto3.png', 'Usuario visitante');

INSERT INTO TipoPagina (Id_Tipo_Pagina, Nombre_Tipo, Descripcion) VALUES
                                                                      (1, 'Basica',      'Página estática con contenido fijo'),
                                                                      (2, 'Post',        'Artículo o noticia'),
                                                                      (3, 'Formulario',  'Página que contiene un formulario');

INSERT INTO PaginaWeb (Id_Pagina, Titulo, Url, Contenido_HTML, Fecha_Creacion, Fecha_Modificacion, Id_Tipo_Pagina) VALUES
                                                                                                                       (1, 'Inicio',                  '/inicio',           '<h1>Bienvenido</h1>',                    '2025-01-10', '2025-01-10', 1),
                                                                                                                       (2, 'Noticias',                '/noticias',          '<h2>Últimas noticias</h2>',               '2025-01-11', '2025-01-12', 2),
                                                                                                                       (3, 'Contacto',                '/contacto',          '<form>...</form>',                        '2025-01-12', '2025-01-12', 3),
                                                                                                                       (4, 'Formulario Organización', '/crm/organizacion',  '<form>Formulario organización</form>',   CURRENT_DATE, CURRENT_DATE, 3),
                                                                                                                       (5, 'Formulario Oportunidad',  '/crm/oportunidad',   '<form>Formulario oportunidad</form>',    CURRENT_DATE, CURRENT_DATE, 3),
                                                                                                                       (6, 'Formulario Producto',     '/crm/producto',      '<form>Formulario producto</form>',       CURRENT_DATE, CURRENT_DATE, 3);

INSERT INTO Formulario (Id_Formulario, Nombre_Formulario, Descripcion, Ruta_Action, Metodo, Fecha_Creacion) VALUES
                                                                                                                (1, 'Formulario Organización', 'Formulario para registrar datos de una organización.', '/crm/organizacion', 'POST', CURRENT_DATE),
                                                                                                                (2, 'Formulario Oportunidad',  'Formulario para registrar datos de una oportunidad.',  '/crm/oportunidad',  'POST', CURRENT_DATE),
                                                                                                                (3, 'Formulario Producto',     'Formulario para registrar datos de un producto.',      '/crm/producto',     'POST', CURRENT_DATE);

INSERT INTO PaginaFormulario (Id_Pagina_Formulario, Id_Pagina, Id_Formulario) VALUES
                                                                                  (1, 4, 1),(2, 5, 2),(3, 6, 3);

INSERT INTO CampoFormulario (Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones) VALUES
                                                                                                                                 (1,  1, 'nombre',           'Nombre',                'text',     1, 'No puede estar vacío',                NULL),
                                                                                                                                 (2,  1, 'direccion',        'Dirección',             'text',     0, NULL,                                  NULL),
                                                                                                                                 (3,  1, 'telefono',         'Teléfono',              'tel',      0, 'Debe contener 9 dígitos',             NULL),
                                                                                                                                 (4,  1, 'email',            'Correo electrónico',    'email',    1, 'Debe tener formato de email válido',  NULL),
                                                                                                                                 (5,  1, 'tipo_organizacion','Tipo de organización',  'select',   1, 'Debe seleccionarse una opción',       'Centro educativo,Empresa,Asociación,Administración'),
                                                                                                                                 (6,  2, 'titulo',           'Título',                'text',     1, 'No puede estar vacío',                NULL),
                                                                                                                                 (7,  2, 'descripcion',      'Descripción',           'textarea', 0, 'Máximo 300 caracteres',              NULL),
                                                                                                                                 (8,  2, 'fecha_inicio',     'Fecha de inicio',       'date',     1, 'Debe indicar una fecha',              NULL),
                                                                                                                                 (9,  2, 'tipos_oportunidad','Tipos de oportunidad',  'select',   1, 'Debe seleccionarse al menos un tipo', 'Colaboración,Patrocinio,Evento,Actividad'),
                                                                                                                                 (10, 3, 'nombre',           'Nombre',                'text',     1, 'No puede estar vacío',                NULL),
                                                                                                                                 (11, 3, 'descripcion',      'Descripción',           'textarea', 0, 'Máximo 300 caracteres',              NULL),
                                                                                                                                 (12, 3, 'precio',           'Precio',                'number',   1, 'Debe ser mayor que 0',                NULL),
                                                                                                                                 (13, 3, 'stock',            'Stock',                 'number',   1, 'Debe ser igual o mayor que 0',        NULL),
                                                                                                                                 (14, 3, 'categoria',        'Categoría',             'text',     1, 'No puede estar vacía',                NULL);

INSERT INTO FichaCliente (Id_Ficha, Nombre, Apellidos, Email, Telefono, Empresa_Centro, Observaciones, Fecha_Alta) VALUES
    (1, 'Lucía', 'García Pérez', 'lucia.garcia@email.com', '600123456', 'IES Al-Ándalus', 'Interesada en recibir información.', CURRENT_DATE);

INSERT INTO FormularioOrganizacion (Id_Formulario, Nombre, Direccion, Telefono, Email, Tipo_Organizacion) VALUES
    (1, 'IES Torre de los Guzmanes', 'Calle Principal 1', '600111222', 'contacto@iestorre.es', 'Centro educativo');

INSERT INTO FormularioOportunidad (Id_Formulario, Titulo, Descripcion, Fecha_Inicio, Tipos_Oportunidad) VALUES
    (2, 'Colaboración Hackathon', 'Posible colaboración para evento educativo.', '2026-05-20', 'Colaboración, Evento');

INSERT INTO FormularioProducto (Id_Formulario, Nombre, Descripcion, Precio, Stock, Categoria) VALUES
    (3, 'Camiseta Evento', 'Camiseta oficial para asistentes.', 12.50, 100, 'Merchandising');

INSERT INTO RespuestaFormulario (Id_Respuesta, Id_Formulario, Id_Ficha, Fecha_Respuesta) VALUES
    (1, 1, 1, CURRENT_DATE);

INSERT INTO FormularioFichaCliente (Id_Formulario_Ficha, Id_Formulario, Id_Ficha) VALUES
    (1, 1, 1);

INSERT INTO PaginaFichaCliente (Id_Pagina_Ficha, Id_Pagina, Id_Ficha) VALUES
    (1, 4, 1);

-- =============================================================
--  INSERTS — MÓDULO 4: HISTORIAL, OPORTUNIDADES Y ORGANIZACIÓN
-- =============================================================

INSERT INTO Historial (ID_Historial, Iteraciones, Fecha_Inicio, Fecha_Fin) VALUES
                                                                               (1, 3, '2026-06-10', '2026-06-12'),
                                                                               (2, 2, '2026-07-05', '2026-07-06'),
                                                                               (3, 5, '2026-09-15', '2026-09-30');

INSERT INTO OrganizacionCRM (ID_Organizacion, Nombre, Direccion, Telefono, Email, Ciudad, Fecha_Registro, ID_Historial) VALUES
                                                                                                                            (1, 'Tech_Solutions',            'Av_Andalucia 25',       '654321987', 'contacto@techsolutions.com', 'Sevilla',   '2025-03-12', 1),
                                                                                                                            (2, 'Asociación Futuro Joven',   'Calle Real 14',         '611223344', 'info@futurojoven.org',       'Málaga',    '2025-04-20', 2),
                                                                                                                            (3, 'Ayuntamiento de La Algaba', 'Plaza Constitución 1',  '955667788', 'ayuntamiento@algaba.es',     'La Algaba', '2025-01-05', 3);

INSERT INTO Oportunidad (ID_Oportunidad, Descripcion, Fecha_Inicio, Fecha_Fin, Estado, Presupuesto, ID_Persona, ID_Historial) VALUES
                                                                                                                                  (1, 'Feria de Empleo Tecnologico',      '2026-06-10', '2026-06-12', 'Aprobado',  150000.00, 1, 1),
                                                                                                                                  (2, 'Jornadas de Orientacion Prof',     '2026-07-05', '2026-07-06', 'Pendiente',  80000.00, 2, 2),
                                                                                                                                  (3, 'Programa de Colaboracion Er',       NULL,        '2026-09-15', 'Rechazado', 220000.00, 3, 3);

INSERT INTO Colaboracion (ID_Colaboracion, Tipo, Fecha, Firma, Dinero, Convenio, ID_Historial) VALUES
                                                                                                   (1, 'Académica',     '2026-05-15', 'SI',  5000.00, 'Convenio Unive',  1),
                                                                                                   (2, 'Empresarial',   '2026-06-20', 'NO',  7500.00, 'Acuerdo Tempc',   2),
                                                                                                   (3, 'Institucional', '2026-07-01', 'SI', 12000.00, 'Convenio Anual',  3);

INSERT INTO Actividad (ID_Actividad, Descripcion, Fecha, Dinero, Firma, ID_Historial) VALUES
                                                                                          (1, 'Taller de Programación', '2026-06-11', 2500.00, 'SI', 1),
                                                                                          (2, 'Conferencia de Empleo',  '2026-07-06', 1000.00, 'NO', 2),
                                                                                          (3, 'Jornada de Networking',  '2026-09-18', 2000.00, 'SI', 3);

INSERT INTO Patrocinio (ID_Patrocinio, Firma, Tipo, ID_Historial) VALUES
                                                                      (1, 'SI', 'Bronce', 1),
                                                                      (2, 'NO', 'Plata',  2),
                                                                      (3, 'SI', 'Oro',    3);

INSERT INTO Recinto (ID_Recinto, Nombre, Capacidad, Ubicacion) VALUES
                                                                   (1, 'Palacio de Congresos',  2000, 'Sevilla'),
                                                                   (2, 'Centro Cultural Norte',  750, 'Málaga'),
                                                                   (3, 'Pabellón Municipal',    1200, 'Cádiz');

INSERT INTO Administracion (id_administracion, Ambito, Presupuesto) VALUES
                                                                        (1, 'Educación', 2500000.00),
                                                                        (2, 'Sanidad',   4200000.00),
                                                                        (3, 'Cultura',   1800000.00);

INSERT INTO Empresa (id_empresa, Sector, Num_Empleados) VALUES
                                                            (1, 'Tecnología', 120),
                                                            (2, 'Marketing',   45),
                                                            (3, 'Transporte', 300);

INSERT INTO Centro_Educativo (id_centro, Tipo_Centro, Num_Alumnos) VALUES
                                                                       (1, 'Instituto',        850),
                                                                       (2, 'Universidad',    12000),
                                                                       (3, 'Colegio Privado',  430);

INSERT INTO Asociacion (id_asociacion, Finalidad, Num_Socios) VALUES
                                                                  (1, 'Ayuda Social',           250),
                                                                  (2, 'Protección Animal',      180),
                                                                  (3, 'Actividades Juveniles',  320);

INSERT INTO Ayuntamiento (id_ayuntamiento, Provincia, Alcalde) VALUES
                                                                   (1, 'Sevilla', 'María Lopez'),
                                                                   (2, 'Málaga',  'Antonio Ruiz'),
                                                                   (3, 'Cádiz',   'Carmen Ortega');

-- =============================================================
--  INSERTS — MÓDULO 5: LOGÍSTICA
-- =============================================================

INSERT INTO Proveedor VALUES
                          (1, 'Distribuciones Sur', 'Sevilla',  '600123456', 'sur@mail.com',    'B1234567', 'España', '2024-01-10', 'activo'),
                          (2, 'Tech Iberia',        'Madrid',   '600234567', 'tech@iberia.com', 'B2345678', 'España', '2024-02-15', 'activo'),
                          (3, 'Global Supplies',    'Badajoz',  '600345678', 'global@sup.com',  'B3456789', 'España', '2024-03-05', 'activo');

INSERT INTO Mercancia VALUES
                          (1, 'Teclado',  'Periferico',  20.00, 10,  50, '2024-01-01'),
                          (2, 'Raton',    'Periferico',  10.00, 15,  40, '2024-01-01'),
                          (3, 'Monitor',  'Pantalla',   150.00,  5,  20, '2024-01-01');

INSERT INTO Pedido VALUES
                       (1, '2024-03-01', '2024-03-10', 1, 'pendiente'),
                       (2, '2024-03-05', '2024-03-15', 2, 'enviado');

INSERT INTO Linea_Pedido VALUES
                             (1, 1, 1, 10,  20.00, 0),
                             (2, 1, 2,  5,  10.00, 0),
                             (3, 2, 3,  3, 150.00, 5);

INSERT INTO Albaran VALUES
                        (1, '2024-03-08', 'recibido_completo', 1, 'F001', 'DHL',  '2024-03-08'),
                        (2, '2024-03-12', 'recibido_parcial',  2, 'F002', 'SEUR', '2024-03-12');

INSERT INTO Linea_Albaran VALUES
                              (1, 1, 1, 10, 'correcto', 'L001', '2026-01-01', 0),
                              (2, 1, 2,  5, 'correcto', 'L002', '2026-01-01', 0),
                              (3, 2, 3,  2, 'faltante', 'L003', '2026-01-01', 1);