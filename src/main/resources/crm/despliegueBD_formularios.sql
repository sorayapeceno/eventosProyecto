SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS FormularioProducto;
DROP TABLE IF EXISTS FormularioOportunidad;
DROP TABLE IF EXISTS FormularioOrganizacion;
DROP TABLE IF EXISTS PaginaFichaCliente;
DROP TABLE IF EXISTS FormularioFichaCliente;
DROP TABLE IF EXISTS RespuestaFormulario;
DROP TABLE IF EXISTS CampoFormulario;
DROP TABLE IF EXISTS PaginaFormulario;
DROP TABLE IF EXISTS Formulario;
DROP TABLE IF EXISTS FichaCliente;
DROP TABLE IF EXISTS PerfilUsuario;
DROP TABLE IF EXISTS PaginaWeb;
DROP TABLE IF EXISTS TipoPagina;
DROP TABLE IF EXISTS Usuario;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE Usuario (
    Id_Usuario INT PRIMARY KEY,
    Nombre VARCHAR(64) NOT NULL,
    Apellidos VARCHAR(128),
    Email VARCHAR(128) UNIQUE NOT NULL,
    Password_Hash VARCHAR(256) NOT NULL,
    Rol VARCHAR(32),
    Fecha_Registro DATE DEFAULT (CURRENT_DATE)
);

CREATE TABLE PerfilUsuario (
    Id_Perfil INT PRIMARY KEY,
    Id_Usuario INT NOT NULL,
    Foto_Perfil VARCHAR(256),
    Bio VARCHAR(512),
    CONSTRAINT fk_perfil_usuario FOREIGN KEY(Id_Usuario)
        REFERENCES Usuario(Id_Usuario) ON DELETE CASCADE
);

CREATE TABLE TipoPagina (
    Id_Tipo_Pagina INT PRIMARY KEY,
    Nombre_Tipo VARCHAR(32) NOT NULL,
    Descripcion VARCHAR(128)
);

CREATE TABLE PaginaWeb (
    Id_Pagina INT PRIMARY KEY,
    Titulo VARCHAR(128) NOT NULL,
    Url VARCHAR(128) UNIQUE NOT NULL,
    Contenido_HTML TEXT,
    Fecha_Creacion DATE DEFAULT (CURRENT_DATE),
    Fecha_Modificacion DATE,
    Id_Tipo_Pagina INT NOT NULL,
    CONSTRAINT fk_pagina_tipo FOREIGN KEY(Id_Tipo_Pagina)
        REFERENCES TipoPagina(Id_Tipo_Pagina)
);

CREATE TABLE Formulario (
    Id_Formulario INT PRIMARY KEY,
    Nombre_Formulario VARCHAR(100) NOT NULL,
    Descripcion VARCHAR(300),
    Ruta_Action VARCHAR(150) NOT NULL,
    Metodo VARCHAR(10) NOT NULL,
    Fecha_Creacion DATE DEFAULT (CURRENT_DATE)
);

CREATE TABLE PaginaFormulario (
    Id_Pagina_Formulario INT PRIMARY KEY,
    Id_Pagina INT NOT NULL,
    Id_Formulario INT NOT NULL,
    CONSTRAINT fk_pagina_formulario_pagina FOREIGN KEY(Id_Pagina)
        REFERENCES PaginaWeb(Id_Pagina) ON DELETE CASCADE,
    CONSTRAINT fk_pagina_formulario_formulario FOREIGN KEY(Id_Formulario)
        REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE
);

CREATE TABLE CampoFormulario (
    Id_Campo INT PRIMARY KEY,
    Id_Formulario INT NOT NULL,
    Nombre_Campo VARCHAR(100) NOT NULL,
    Etiqueta VARCHAR(100) NOT NULL,
    Tipo_Input VARCHAR(50) NOT NULL,
    Obligatorio TINYINT NOT NULL,
    Validacion VARCHAR(150),
    Opciones VARCHAR(300),
    CONSTRAINT fk_campo_formulario FOREIGN KEY(Id_Formulario)
        REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE
);

CREATE TABLE FichaCliente (
    Id_Ficha INT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Apellidos VARCHAR(150),
    Email VARCHAR(150) UNIQUE NOT NULL,
    Telefono VARCHAR(20),
    Empresa_Centro VARCHAR(150),
    Observaciones VARCHAR(300),
    Fecha_Alta DATE DEFAULT (CURRENT_DATE)
);

CREATE TABLE RespuestaFormulario (
    Id_Respuesta INT PRIMARY KEY,
    Id_Formulario INT NOT NULL,
    Id_Ficha INT NOT NULL,
    Fecha_Respuesta DATE DEFAULT (CURRENT_DATE),
    CONSTRAINT fk_respuesta_formulario FOREIGN KEY(Id_Formulario)
        REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE,
    CONSTRAINT fk_respuesta_ficha FOREIGN KEY(Id_Ficha)
        REFERENCES FichaCliente(Id_Ficha) ON DELETE CASCADE
);

CREATE TABLE FormularioFichaCliente (
    Id_Formulario_Ficha INT PRIMARY KEY,
    Id_Formulario INT NOT NULL,
    Id_Ficha INT NOT NULL,
    CONSTRAINT fk_formulario_ficha_formulario FOREIGN KEY(Id_Formulario)
        REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE,
    CONSTRAINT fk_formulario_ficha_cliente FOREIGN KEY(Id_Ficha)
        REFERENCES FichaCliente(Id_Ficha) ON DELETE CASCADE
);

CREATE TABLE PaginaFichaCliente (
    Id_Pagina_Ficha INT PRIMARY KEY,
    Id_Pagina INT NOT NULL,
    Id_Ficha INT NOT NULL,
    CONSTRAINT fk_pagina_ficha_pagina FOREIGN KEY(Id_Pagina)
        REFERENCES PaginaWeb(Id_Pagina) ON DELETE CASCADE,
    CONSTRAINT fk_pagina_ficha_cliente FOREIGN KEY(Id_Ficha)
        REFERENCES FichaCliente(Id_Ficha) ON DELETE CASCADE
);

CREATE TABLE FormularioOrganizacion (
    Id_Formulario_Organizacion INT AUTO_INCREMENT PRIMARY KEY,
    Id_Formulario INT NOT NULL,
    Nombre VARCHAR(100) NOT NULL,
    Direccion VARCHAR(200),
    Telefono VARCHAR(20),
    Email VARCHAR(150) NOT NULL,
    Tipo_Organizacion VARCHAR(50) NOT NULL,
    CONSTRAINT fk_form_org_formulario FOREIGN KEY(Id_Formulario)
        REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE
);

CREATE TABLE FormularioOportunidad (
    Id_Formulario_Oportunidad INT AUTO_INCREMENT PRIMARY KEY,
    Id_Formulario INT NOT NULL,
    Titulo VARCHAR(100) NOT NULL,
    Descripcion VARCHAR(300),
    Fecha_Inicio DATE NOT NULL,
    Tipos_Oportunidad VARCHAR(200) NOT NULL,
    CONSTRAINT fk_form_op_formulario FOREIGN KEY(Id_Formulario)
        REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE
);

CREATE TABLE FormularioProducto (
    Id_Formulario_Producto INT AUTO_INCREMENT PRIMARY KEY,
    Id_Formulario INT NOT NULL,
    Nombre VARCHAR(100) NOT NULL,
    Descripcion VARCHAR(300),
    Precio DECIMAL(10,2) NOT NULL,
    Stock INT NOT NULL,
    Categoria VARCHAR(100) NOT NULL,
    CONSTRAINT fk_form_prod_formulario FOREIGN KEY(Id_Formulario)
        REFERENCES Formulario(Id_Formulario) ON DELETE CASCADE
);

INSERT INTO Usuario
(Id_Usuario, Nombre, Apellidos, Email, Password_Hash, Rol, Fecha_Registro)
VALUES
(1, 'Adrián', 'García López', 'adrian@example.com', 'hash123', 'admin', '2025-01-10');

INSERT INTO Usuario
(Id_Usuario, Nombre, Apellidos, Email, Password_Hash, Rol, Fecha_Registro)
VALUES
(2, 'Lucía', 'Martín Ruiz', 'lucia@example.com', 'hash456', 'editor', '2025-01-12');

INSERT INTO Usuario
(Id_Usuario, Nombre, Apellidos, Email, Password_Hash, Rol, Fecha_Registro)
VALUES
(3, 'Carlos', 'Pérez Soto', 'carlos@example.com', 'hash789', 'viewer', '2025-01-15');

INSERT INTO PerfilUsuario
(Id_Perfil, Id_Usuario, Foto_Perfil, Bio)
VALUES
(1, 1, 'foto1.png', 'Administrador del CRM');

INSERT INTO PerfilUsuario
(Id_Perfil, Id_Usuario, Foto_Perfil, Bio)
VALUES
(2, 2, 'foto2.png', 'Editora de contenido');

INSERT INTO PerfilUsuario
(Id_Perfil, Id_Usuario, Foto_Perfil, Bio)
VALUES
(3, 3, 'foto3.png', 'Usuario visitante');

INSERT INTO TipoPagina
(Id_Tipo_Pagina, Nombre_Tipo, Descripcion)
VALUES
(1, 'Basica', 'Página estática con contenido fijo');

INSERT INTO TipoPagina
(Id_Tipo_Pagina, Nombre_Tipo, Descripcion)
VALUES
(2, 'Post', 'Artículo o noticia');

INSERT INTO TipoPagina
(Id_Tipo_Pagina, Nombre_Tipo, Descripcion)
VALUES
(3, 'Formulario', 'Página que contiene un formulario');

INSERT INTO PaginaWeb
(Id_Pagina, Titulo, Url, Contenido_HTML, Fecha_Creacion, Fecha_Modificacion, Id_Tipo_Pagina)
VALUES
(1, 'Inicio', '/inicio', '<h1>Bienvenido</h1>', '2025-01-10', '2025-01-10', 1);

INSERT INTO PaginaWeb
(Id_Pagina, Titulo, Url, Contenido_HTML, Fecha_Creacion, Fecha_Modificacion, Id_Tipo_Pagina)
VALUES
(2, 'Noticias', '/noticias', '<h2>Últimas noticias</h2>', '2025-01-11', '2025-01-12', 2);

INSERT INTO PaginaWeb
(Id_Pagina, Titulo, Url, Contenido_HTML, Fecha_Creacion, Fecha_Modificacion, Id_Tipo_Pagina)
VALUES
(3, 'Contacto', '/contacto', '<form>...</form>', '2025-01-12', '2025-01-12', 3);

INSERT INTO PaginaWeb
(Id_Pagina, Titulo, Url, Contenido_HTML, Fecha_Creacion, Fecha_Modificacion, Id_Tipo_Pagina)
VALUES
(4, 'Formulario Organización', '/crm/organizacion', '<form>Formulario organización</form>', CURRENT_DATE, CURRENT_DATE, 3);

INSERT INTO PaginaWeb
(Id_Pagina, Titulo, Url, Contenido_HTML, Fecha_Creacion, Fecha_Modificacion, Id_Tipo_Pagina)
VALUES
(5, 'Formulario Oportunidad', '/crm/oportunidad', '<form>Formulario oportunidad</form>', CURRENT_DATE, CURRENT_DATE, 3);

INSERT INTO PaginaWeb
(Id_Pagina, Titulo, Url, Contenido_HTML, Fecha_Creacion, Fecha_Modificacion, Id_Tipo_Pagina)
VALUES
(6, 'Formulario Producto', '/crm/producto', '<form>Formulario producto</form>', CURRENT_DATE, CURRENT_DATE, 3);

INSERT INTO Formulario
(Id_Formulario, Nombre_Formulario, Descripcion, Ruta_Action, Metodo, Fecha_Creacion)
VALUES
(1, 'Formulario Organización', 'Formulario para registrar datos de una organización.', '/crm/organizacion', 'POST', CURRENT_DATE);

INSERT INTO Formulario
(Id_Formulario, Nombre_Formulario, Descripcion, Ruta_Action, Metodo, Fecha_Creacion)
VALUES
(2, 'Formulario Oportunidad', 'Formulario para registrar datos de una oportunidad.', '/crm/oportunidad', 'POST', CURRENT_DATE);

INSERT INTO Formulario
(Id_Formulario, Nombre_Formulario, Descripcion, Ruta_Action, Metodo, Fecha_Creacion)
VALUES
(3, 'Formulario Producto', 'Formulario para registrar datos de un producto.', '/crm/producto', 'POST', CURRENT_DATE);

INSERT INTO PaginaFormulario
(Id_Pagina_Formulario, Id_Pagina, Id_Formulario)
VALUES
(1, 4, 1);

INSERT INTO PaginaFormulario
(Id_Pagina_Formulario, Id_Pagina, Id_Formulario)
VALUES
(2, 5, 2);

INSERT INTO PaginaFormulario
(Id_Pagina_Formulario, Id_Pagina, Id_Formulario)
VALUES
(3, 6, 3);

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(1, 1, 'nombre', 'Nombre', 'text', 1, 'No puede estar vacío', NULL);

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(2, 1, 'direccion', 'Dirección', 'text', 0, NULL, NULL);

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(3, 1, 'telefono', 'Teléfono', 'tel', 0, 'Debe contener 9 dígitos', NULL);

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(4, 1, 'email', 'Correo electrónico', 'email', 1, 'Debe tener formato de email válido', NULL);

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(5, 1, 'tipo_organizacion', 'Tipo de organización', 'select', 1, 'Debe seleccionarse una opción', 'Centro educativo,Empresa,Asociación,Administración');

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(6, 2, 'titulo', 'Título', 'text', 1, 'No puede estar vacío', NULL);

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(7, 2, 'descripcion', 'Descripción', 'textarea', 0, 'Máximo 300 caracteres', NULL);

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(8, 2, 'fecha_inicio', 'Fecha de inicio', 'date', 1, 'Debe indicar una fecha', NULL);

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(9, 2, 'tipos_oportunidad', 'Tipos de oportunidad', 'select', 1, 'Debe seleccionarse al menos un tipo', 'Colaboración,Patrocinio,Evento,Actividad');

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(10, 3, 'nombre', 'Nombre', 'text', 1, 'No puede estar vacío', NULL);

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(11, 3, 'descripcion', 'Descripción', 'textarea', 0, 'Máximo 300 caracteres', NULL);

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(12, 3, 'precio', 'Precio', 'number', 1, 'Debe ser mayor que 0', NULL);

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(13, 3, 'stock', 'Stock', 'number', 1, 'Debe ser igual o mayor que 0', NULL);

INSERT INTO CampoFormulario
(Id_Campo, Id_Formulario, Nombre_Campo, Etiqueta, Tipo_Input, Obligatorio, Validacion, Opciones)
VALUES
(14, 3, 'categoria', 'Categoría', 'text', 1, 'No puede estar vacía', NULL);

INSERT INTO FichaCliente
(Id_Ficha, Nombre, Apellidos, Email, Telefono, Empresa_Centro, Observaciones, Fecha_Alta)
VALUES
(1, 'Lucía', 'García Pérez', 'lucia.garcia@email.com', '600123456', 'IES Al-Ándalus', 'Interesada en recibir información.', CURRENT_DATE);

INSERT INTO FormularioOrganizacion
(Id_Formulario, Nombre, Direccion, Telefono, Email, Tipo_Organizacion)
VALUES
(1, 'IES Torre de los Guzmanes', 'Calle Principal 1', '600111222', 'contacto@iestorre.es', 'Centro educativo');

INSERT INTO FormularioOportunidad
(Id_Formulario, Titulo, Descripcion, Fecha_Inicio, Tipos_Oportunidad)
VALUES
(2, 'Colaboración Hackathon', 'Posible colaboración para evento educativo.', '2026-05-20', 'Colaboración, Evento');

INSERT INTO FormularioProducto
(Id_Formulario, Nombre, Descripcion, Precio, Stock, Categoria)
VALUES
(3, 'Camiseta Evento', 'Camiseta oficial para asistentes.', 12.50, 100, 'Merchandising');

INSERT INTO RespuestaFormulario
(Id_Respuesta, Id_Formulario, Id_Ficha, Fecha_Respuesta)
VALUES
(1, 1, 1, CURRENT_DATE);

INSERT INTO FormularioFichaCliente
(Id_Formulario_Ficha, Id_Formulario, Id_Ficha)
VALUES
(1, 1, 1);

INSERT INTO PaginaFichaCliente
(Id_Pagina_Ficha, Id_Pagina, Id_Ficha)
VALUES
(1, 4, 1);
