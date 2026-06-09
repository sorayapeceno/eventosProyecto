package dam.primero.repositorio.dashboards;

import dam.primero.config.dashboards.MySqlConectorDashboards;
import dam.primero.exception.MyException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Repositorio de consultas SQL para el módulo de Dashboards.
 * <p>
 * Esta clase centraliza todas las consultas a la base de datos {@code dashboards},
 * proporcionando métodos para obtener KPIs (conteos), datos agrupados para gráficas
 * y listados completos para las tablas de detalle de cada módulo del proyecto.
 * </p>
 * <p>
 * Utiliza {@link MySqlConectorDashboards} para abrir la conexión con MySQL,
 * leyendo las credenciales del archivo {@code dashboards/db.properties}.
 * </p>
 *
 * @author Elena Pablo Benítez
 * @version 1.0
 */
public class RepoDashboards {

    /** Conexión activa con la base de datos dashboards. */
    private MySqlConectorDashboards conector;

    /**
     * Constructor que inicializa la conexión con la base de datos.
     * Si la conexión falla, imprime el error por consola y el conector queda como null.
     */
    public RepoDashboards() {
        try {
            this.conector = new MySqlConectorDashboards();
        } catch (MyException e) {
            System.out.println("RepoDashboards – Error al conectar: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════
    //  EVENTOS
    // ══════════════════════════════════════════════════════

    /**
     * Cuenta el total de eventos registrados en la base de datos.
     *
     * @return número total de eventos
     */
    public int contarEventos() { return contarFila("SELECT COUNT(*) FROM Evento"); }

    /**
     * Cuenta el total de personas registradas en la base de datos.
     *
     * @return número total de personas
     */
    public int contarPersonas() { return contarFila("SELECT COUNT(*) FROM Persona"); }

    /**
     * Agrupa los eventos por su estado (Borrador, Abierto, Cerrado, Cancelado, Pospuesto).
     *
     * @return Map con estado como clave y número de eventos como valor, ordenado descendentemente
     */
    public Map<String, Integer> eventosPorEstado() { return agrupar("SELECT Estado, COUNT(*) AS total FROM Evento GROUP BY Estado ORDER BY total DESC", "Estado", "total"); }

    /**
     * Agrupa los eventos por su modalidad (Presencial, Online, Híbrido).
     *
     * @return Map con modalidad como clave y número de eventos como valor
     */
    public Map<String, Integer> eventosPorModalidad() { return agrupar("SELECT Modalidad, COUNT(*) AS total FROM Evento GROUP BY Modalidad ORDER BY total DESC", "Modalidad", "total"); }

    /**
     * Agrupa los eventos por ciudad, excluyendo los que no tienen ciudad registrada.
     *
     * @return Map con ciudad como clave y número de eventos como valor
     */
    public Map<String, Integer> eventosPorCiudad() { return agrupar("SELECT Ciudad, COUNT(*) AS total FROM Evento WHERE Ciudad IS NOT NULL GROUP BY Ciudad ORDER BY total DESC", "Ciudad", "total"); }

    /**
     * Obtiene el listado completo de eventos con todos sus campos formateados.
     *
     * @return lista de Maps donde cada Map representa un evento con sus campos
     */
    public List<Map<String, String>> detalleEventos() { return listar("SELECT Nombre, DATE_FORMAT(Fecha_Inicio,'%d/%m/%Y') AS Fecha_Inicio, DATE_FORMAT(Fecha_Fin,'%d/%m/%Y') AS Fecha_Fin, Ciudad, Estado, Modalidad, Capacidad, Lugar FROM Evento ORDER BY Fecha_Inicio", new String[]{"Nombre","Fecha_Inicio","Fecha_Fin","Ciudad","Estado","Modalidad","Capacidad","Lugar"}); }

    // ══════════════════════════════════════════════════════
    //  PONENCIAS
    // ══════════════════════════════════════════════════════

    /**
     * Cuenta el total de ponencias registradas.
     *
     * @return número total de ponencias
     */
    public int contarPonencias() { return contarFila("SELECT COUNT(*) FROM Ponencia"); }

    /**
     * Agrupa las ponencias por tipo de actividad (Charla, Taller, Mesa, Podcast).
     *
     * @return Map con tipo como clave y número de ponencias como valor
     */
    public Map<String, Integer> ponenciasPorTipo() { return agrupar("SELECT Tipo, COUNT(*) AS total FROM Ponencia GROUP BY Tipo ORDER BY total DESC", "Tipo", "total"); }

    /**
     * Agrupa las ponencias por nivel (Basico, Intermedio, Avanzado).
     *
     * @return Map con nivel como clave y número de ponencias como valor
     */
    public Map<String, Integer> ponenciasPorNivel() { return agrupar("SELECT Nivel, COUNT(*) AS total FROM Ponencia WHERE Nivel IS NOT NULL GROUP BY Nivel ORDER BY total DESC", "Nivel", "total"); }

    /**
     * Agrupa las ponencias por formato (Presencial, Online, Híbrido).
     *
     * @return Map con formato como clave y número de ponencias como valor
     */
    public Map<String, Integer> ponenciasPorFormato() { return agrupar("SELECT Formato, COUNT(*) AS total FROM Ponencia GROUP BY Formato ORDER BY total DESC", "Formato", "total"); }

    /**
     * Agrupa las ponencias por temática, haciendo JOIN con la tabla Tematica.
     *
     * @return Map con nombre de la temática como clave y número de ponencias como valor
     */
    public Map<String, Integer> ponenciasPorTematica() { return agrupar("SELECT t.Tema, COUNT(*) AS total FROM Ponencia p JOIN Tematica t ON p.id_Tematica = t.id_Tematica GROUP BY t.Tema ORDER BY total DESC", "Tema", "total"); }

    /**
     * Obtiene el listado completo de ponencias con su temática y evento asociado.
     *
     * @return lista de Maps donde cada Map representa una ponencia con sus campos
     */
    public List<Map<String, String>> detallePonencias() { return listar("SELECT p.Titulo, t.Tema, p.Tipo, p.Nivel, p.Formato, p.Duracion, DATE_FORMAT(p.Fecha,'%d/%m/%Y') AS Fecha, p.Hora, p.Sala, e.Nombre AS Evento FROM Ponencia p JOIN Tematica t ON p.id_Tematica = t.id_Tematica JOIN Evento e ON p.id_Evento = e.id_Evento ORDER BY p.Fecha, p.Hora", new String[]{"Titulo","Tema","Tipo","Nivel","Formato","Duracion","Fecha","Hora","Sala","Evento"}); }

    // ══════════════════════════════════════════════════════
    //  PONENTES
    // ══════════════════════════════════════════════════════

    /**
     * Cuenta el total de ponentes registrados.
     *
     * @return número total de ponentes
     */
    public int contarPonentes() { return contarFila("SELECT COUNT(*) FROM Ponente"); }

    /**
     * Agrupa los ponentes por su especialidad.
     *
     * @return Map con especialidad como clave y número de ponentes como valor
     */
    public Map<String, Integer> ponentesPorEspecialidad() { return agrupar("SELECT Especialidad, COUNT(*) AS total FROM Ponente GROUP BY Especialidad ORDER BY total DESC", "Especialidad", "total"); }

    /**
     * Agrupa los ponentes por su nivel de impartición.
     *
     * @return Map con nivel de impartición como clave y número de ponentes como valor
     */
    public Map<String, Integer> ponentesPorNivelImparticion() { return agrupar("SELECT Nivel_Imparticion, COUNT(*) AS total FROM Ponente GROUP BY Nivel_Imparticion ORDER BY total DESC", "Nivel_Imparticion", "total"); }

    /**
     * Obtiene el ranking de los 10 ponentes con más ponencias impartidas.
     * Hace JOIN entre Ponente_Ponencia, Ponente y Persona para obtener el nombre completo.
     *
     * @return Map con nombre completo del ponente como clave y número de ponencias como valor
     */
    public Map<String, Integer> topPonentes() { return agrupar("SELECT CONCAT(per.Nombre, ' ', per.Ap1) AS nombre, COUNT(pp.id_Ponencia) AS total FROM Ponente_Ponencia pp JOIN Ponente po ON pp.id_Ponente = po.id_Ponente JOIN Persona per ON po.id_Persona = per.id_Persona GROUP BY per.Nombre, per.Ap1 ORDER BY total DESC LIMIT 10", "nombre", "total"); }

    /**
     * Obtiene el listado completo de ponentes con su número de ponencias impartidas.
     *
     * @return lista de Maps donde cada Map representa un ponente con sus campos
     */
    public List<Map<String, String>> detallePonentes() { return listar("SELECT CONCAT(per.Nombre,' ',per.Ap1) AS Nombre, po.Especialidad, po.Nivel_Imparticion, COUNT(pp.id_Ponencia) AS Ponencias, per.Correo, per.Ciudad FROM Ponente po JOIN Persona per ON po.id_Persona = per.id_Persona LEFT JOIN Ponente_Ponencia pp ON po.id_Ponente = pp.id_Ponente GROUP BY po.id_Ponente, per.Nombre, per.Ap1, po.Especialidad, po.Nivel_Imparticion, per.Correo, per.Ciudad ORDER BY Ponencias DESC", new String[]{"Nombre","Especialidad","Nivel_Imparticion","Ponencias","Correo","Ciudad"}); }

    // ══════════════════════════════════════════════════════
    //  VENTAS
    // ══════════════════════════════════════════════════════

    /**
     * Cuenta el total de asistentes registrados.
     *
     * @return número total de asistentes
     */
    public int contarAsistentes() { return contarFila("SELECT COUNT(*) FROM Asistente"); }

    /**
     * Cuenta el total de tickets vendidos.
     *
     * @return número total de tickets
     */
    public int contarTickets() { return contarFila("SELECT COUNT(*) FROM Ticket"); }

    /**
     * Cuenta el total de productos en el catálogo de ventas.
     *
     * @return número total de productos
     */
    public int contarProductosVentas() { return contarFila("SELECT COUNT(*) FROM Producto"); }

    /**
     * Agrupa los tickets por método de pago (Tarjeta, PayPal, Efectivo, Transferencia).
     *
     * @return Map con método de pago como clave y número de tickets como valor
     */
    public Map<String, Integer> ticketsPorMetodoPago() { return agrupar("SELECT Metodo_Pago, COUNT(*) AS total FROM Ticket GROUP BY Metodo_Pago ORDER BY total DESC", "Metodo_Pago", "total"); }

    /**
     * Agrupa los asistentes por su nivel de impartición.
     *
     * @return Map con nivel como clave y número de asistentes como valor
     */
    public Map<String, Integer> asistentePorNivel() { return agrupar("SELECT Nivel_Imparticion, COUNT(*) AS total FROM Asistente GROUP BY Nivel_Imparticion ORDER BY total DESC", "Nivel_Imparticion", "total"); }

    /**
     * Agrupa los asistentes por su temática de interés.
     *
     * @return Map con temática como clave y número de asistentes como valor
     */
    public Map<String, Integer> asistentePorTematica() { return agrupar("SELECT Tematica, COUNT(*) AS total FROM Asistente WHERE Tematica IS NOT NULL GROUP BY Tematica ORDER BY total DESC", "Tematica", "total"); }

    /**
     * Obtiene el stock disponible de cada producto ordenado de mayor a menor.
     *
     * @return Map con nombre del producto como clave y stock disponible como valor
     */
    public Map<String, Integer> stockProductos() { return agrupar("SELECT Nombre_Producto, Stock_Disponible FROM Producto ORDER BY Stock_Disponible DESC", "Nombre_Producto", "Stock_Disponible"); }

    /**
     * Agrupa las entradas por su estado (Activa, Pendiente, etc.).
     *
     * @return Map con estado de entrada como clave y número de entradas como valor
     */
    public Map<String, Integer> entradasPorEstado() { return agrupar("SELECT EstadoEntrada, COUNT(*) AS total FROM Entrada GROUP BY EstadoEntrada ORDER BY total DESC", "EstadoEntrada", "total"); }

    /**
     * Obtiene el listado completo de tickets con los datos del asistente asociado.
     *
     * @return lista de Maps donde cada Map representa un ticket con sus campos
     */
    public List<Map<String, String>> detalleTickets() { return listar("SELECT t.ID_Ticket, DATE_FORMAT(t.Fecha_Compra,'%d/%m/%Y') AS Fecha_Compra, t.Precio_Final, t.Metodo_Pago, t.Descuento, t.Codigo_Promocional, a.Tematica, a.Nivel_Imparticion FROM Ticket t JOIN Asistente a ON t.ID_Asistente = a.ID_Asistente ORDER BY t.Fecha_Compra DESC", new String[]{"ID_Ticket","Fecha_Compra","Precio_Final","Metodo_Pago","Descuento","Codigo_Promocional","Tematica","Nivel_Imparticion"}); }

    /**
     * Obtiene el catálogo completo de productos de ventas ordenado por precio.
     *
     * @return lista de Maps donde cada Map representa un producto con sus campos
     */
    public List<Map<String, String>> detalleProductosVentas() { return listar("SELECT Nombre_Producto, Precio, Stock_Disponible, Tipo_IVA, Descuento, Descripcion_Producto FROM Producto ORDER BY Precio DESC", new String[]{"Nombre_Producto","Precio","Stock_Disponible","Tipo_IVA","Descuento","Descripcion_Producto"}); }

    // ══════════════════════════════════════════════════════
    //  CRM
    // ══════════════════════════════════════════════════════

    /**
     * Cuenta el total de organizaciones registradas en OrganizacionCRM.
     *
     * @return número total de organizaciones
     */
    public int contarOrganizaciones() { return contarFila("SELECT COUNT(*) FROM OrganizacionCRM"); }

    /**
     * Cuenta el total de oportunidades registradas.
     *
     * @return número total de oportunidades
     */
    public int contarOportunidades() { return contarFila("SELECT COUNT(*) FROM Oportunidad"); }

    /**
     * Cuenta el total de productos en el catálogo del CRM.
     *
     * @return número total de productos CRM
     */
    public int contarProductosCRM() { return contarFila("SELECT COUNT(*) FROM Producto"); }

    /**
     * Cuenta el total de fichas de cliente registradas.
     *
     * @return número total de fichas de cliente
     */
    public int contarFichasCliente() { return contarFila("SELECT COUNT(*) FROM FichaCliente"); }

    /**
     * Agrupa las organizaciones del formulario CRM por tipo.
     *
     * @return Map con tipo de organización como clave y número como valor
     */
    public Map<String, Integer> organizacionesPorTipo() { return agrupar("SELECT Tipo_Organizacion, COUNT(*) AS total FROM FormularioOrganizacion GROUP BY Tipo_Organizacion ORDER BY total DESC", "Tipo_Organizacion", "total"); }

    /**
     * Agrupa los productos del formulario CRM por categoría.
     *
     * @return Map con categoría como clave y número de productos como valor
     */
    public Map<String, Integer> productosPorCategoria() { return agrupar("SELECT Categoria, COUNT(*) AS total FROM FormularioProducto GROUP BY Categoria ORDER BY total DESC", "Categoria", "total"); }

    /**
     * Obtiene el stock de cada producto del formulario CRM.
     *
     * @return Map con nombre del producto como clave y stock como valor
     */
    public Map<String, Integer> stockPorProducto() { return agrupar("SELECT Nombre, Stock FROM FormularioProducto ORDER BY Stock DESC", "Nombre", "Stock"); }

    /**
     * Agrupa los usuarios del CRM por su rol (admin, editor, viewer).
     *
     * @return Map con rol como clave y número de usuarios como valor
     */
    public Map<String, Integer> usuariosPorRol() { return agrupar("SELECT Rol, COUNT(*) AS total FROM Usuario GROUP BY Rol ORDER BY total DESC", "Rol", "total"); }

    /**
     * Agrupa las páginas web por su tipo, haciendo JOIN con TipoPagina.
     *
     * @return Map con tipo de página como clave y número de páginas como valor
     */
    public Map<String, Integer> paginasPorTipo() { return agrupar("SELECT t.Nombre_Tipo, COUNT(*) AS total FROM PaginaWeb p JOIN TipoPagina t ON p.Id_Tipo_Pagina = t.Id_Tipo_Pagina GROUP BY t.Nombre_Tipo ORDER BY total DESC", "Nombre_Tipo", "total"); }

    /**
     * Obtiene el listado de organizaciones del formulario CRM.
     *
     * @return lista de Maps donde cada Map representa una organización
     */
    public List<Map<String, String>> detalleOrganizaciones() { return listar("SELECT Nombre, Tipo_Organizacion, Email, Telefono, Direccion FROM FormularioOrganizacion ORDER BY Nombre", new String[]{"Nombre","Tipo_Organizacion","Email","Telefono","Direccion"}); }

    /**
     * Obtiene el listado completo de páginas web con su tipo y fechas.
     *
     * @return lista de Maps donde cada Map representa una página web
     */
    public List<Map<String, String>> detallePaginasWeb() { return listar("SELECT p.Titulo, p.Url, t.Nombre_Tipo, DATE_FORMAT(p.Fecha_Creacion,'%d/%m/%Y') AS Fecha_Creacion, DATE_FORMAT(p.Fecha_Modificacion,'%d/%m/%Y') AS Fecha_Modificacion FROM PaginaWeb p JOIN TipoPagina t ON p.Id_Tipo_Pagina = t.Id_Tipo_Pagina ORDER BY t.Nombre_Tipo", new String[]{"Titulo","Url","Nombre_Tipo","Fecha_Creacion","Fecha_Modificacion"}); }

    /**
     * Obtiene el listado de productos del formulario CRM.
     *
     * @return lista de Maps donde cada Map representa un producto CRM
     */
    public List<Map<String, String>> detalleProductosCRM() { return listar("SELECT Nombre, Descripcion, Precio, Stock, Categoria FROM FormularioProducto ORDER BY Categoria, Nombre", new String[]{"Nombre","Descripcion","Precio","Stock","Categoria"}); }

    /**
     * Obtiene el listado de oportunidades del formulario CRM.
     *
     * @return lista de Maps donde cada Map representa una oportunidad
     */
    public List<Map<String, String>> detalleOportunidades() { return listar("SELECT Titulo, Descripcion, DATE_FORMAT(Fecha_Inicio,'%d/%m/%Y') AS Fecha_Inicio, Tipos_Oportunidad FROM FormularioOportunidad ORDER BY Fecha_Inicio", new String[]{"Titulo","Descripcion","Fecha_Inicio","Tipos_Oportunidad"}); }

    // ══════════════════════════════════════════════════════
    //  RELACIONES INSTITUCIONALES
    // ══════════════════════════════════════════════════════

    /**
     * Cuenta el total de organizaciones en OrganizacionCRM para el módulo de Relaciones.
     *
     * @return número total de organizaciones
     */
    public int contarOrganizacionesRel() { return contarFila("SELECT COUNT(*) FROM OrganizacionCRM"); }

    /**
     * Cuenta el total de oportunidades para el módulo de Relaciones.
     *
     * @return número total de oportunidades
     */
    public int contarOportunidadesRel() { return contarFila("SELECT COUNT(*) FROM Oportunidad"); }

    /**
     * Cuenta el total de recintos registrados.
     *
     * @return número total de recintos
     */
    public int contarRecintos() { return contarFila("SELECT COUNT(*) FROM Recinto"); }

    /**
     * Cuenta el total de patrocinios registrados.
     *
     * @return número total de patrocinios
     */
    public int contarPatrocinios() { return contarFila("SELECT COUNT(*) FROM Patrocinio"); }

    /**
     * Agrupa las oportunidades por estado (Aprobado, Pendiente, Rechazado).
     *
     * @return Map con estado como clave y número de oportunidades como valor
     */
    public Map<String, Integer> oportunidadesPorEstado() { return agrupar("SELECT Estado, COUNT(*) AS total FROM Oportunidad GROUP BY Estado ORDER BY total DESC", "Estado", "total"); }

    /**
     * Agrupa los patrocinios por tipo (Bronce, Plata, Oro).
     *
     * @return Map con tipo de patrocinio como clave y número como valor
     */
    public Map<String, Integer> patrociniosPorTipo() { return agrupar("SELECT Tipo, COUNT(*) AS total FROM Patrocinio GROUP BY Tipo ORDER BY total DESC", "Tipo", "total"); }

    /**
     * Agrupa las colaboraciones por tipo (Académica, Empresarial, Institucional).
     *
     * @return Map con tipo de colaboración como clave y número como valor
     */
    public Map<String, Integer> colaboracionesPorTipo() { return agrupar("SELECT Tipo, COUNT(*) AS total FROM Colaboracion GROUP BY Tipo ORDER BY total DESC", "Tipo", "total"); }

    /**
     * Agrupa las organizaciones de Relaciones por ciudad.
     *
     * @return Map con ciudad como clave y número de organizaciones como valor
     */
    public Map<String, Integer> organizacionesPorCiudad() { return agrupar("SELECT Ciudad, COUNT(*) AS total FROM OrganizacionCRM WHERE Ciudad IS NOT NULL GROUP BY Ciudad ORDER BY total DESC", "Ciudad", "total"); }

    /**
     * Obtiene la capacidad de cada recinto ordenada de mayor a menor.
     *
     * @return Map con nombre del recinto como clave y capacidad como valor
     */
    public Map<String, Integer> recintoPorCapacidad() { return agrupar("SELECT Nombre, Capacidad FROM Recinto ORDER BY Capacidad DESC", "Nombre", "Capacidad"); }

    /**
     * Obtiene el listado de organizaciones del módulo de Relaciones.
     *
     * @return lista de Maps donde cada Map representa una organización
     */
    public List<Map<String, String>> detalleOrganizacionesRel() { return listar("SELECT Nombre, Ciudad, Telefono, Email, DATE_FORMAT(Fecha_Registro,'%d/%m/%Y') AS Fecha_Registro FROM OrganizacionCRM ORDER BY Nombre", new String[]{"Nombre","Ciudad","Telefono","Email","Fecha_Registro"}); }

    /**
     * Obtiene el listado de oportunidades del módulo de Relaciones.
     *
     * @return lista de Maps donde cada Map representa una oportunidad
     */
    public List<Map<String, String>> detalleOportunidadesRel() { return listar("SELECT Descripcion, DATE_FORMAT(Fecha_Inicio,'%d/%m/%Y') AS Fecha_Inicio, DATE_FORMAT(Fecha_Fin,'%d/%m/%Y') AS Fecha_Fin, Estado, Presupuesto FROM Oportunidad ORDER BY Fecha_Inicio", new String[]{"Descripcion","Fecha_Inicio","Fecha_Fin","Estado","Presupuesto"}); }

    /**
     * Obtiene el listado de colaboraciones con sus datos de firma y convenio.
     *
     * @return lista de Maps donde cada Map representa una colaboración
     */
    public List<Map<String, String>> detalleColaboraciones() { return listar("SELECT Tipo, DATE_FORMAT(Fecha,'%d/%m/%Y') AS Fecha, Firma, Dinero, Convenio FROM Colaboracion ORDER BY Fecha", new String[]{"Tipo","Fecha","Firma","Dinero","Convenio"}); }

    // ══════════════════════════════════════════════════════
    //  LOGÍSTICA
    // ══════════════════════════════════════════════════════

    /**
     * Cuenta el total de proveedores registrados.
     *
     * @return número total de proveedores
     */
    public int contarProveedores() { return contarFila("SELECT COUNT(*) FROM Proveedor"); }

    /**
     * Cuenta el total de pedidos registrados.
     *
     * @return número total de pedidos
     */
    public int contarPedidos() { return contarFila("SELECT COUNT(*) FROM Pedido"); }

    /**
     * Cuenta el total de mercancías en el almacén.
     *
     * @return número total de mercancías
     */
    public int contarMercancias() { return contarFila("SELECT COUNT(*) FROM Mercancia"); }

    /**
     * Cuenta el total de albaranes registrados.
     *
     * @return número total de albaranes
     */
    public int contarAlbaranes() { return contarFila("SELECT COUNT(*) FROM Albaran"); }

    /**
     * Agrupa los proveedores por su estado (activo, inactivo, bloqueado).
     *
     * @return Map con estado como clave y número de proveedores como valor
     */
    public Map<String, Integer> proveedoresPorEstado() { return agrupar("SELECT estado, COUNT(*) AS total FROM Proveedor GROUP BY estado ORDER BY total DESC", "estado", "total"); }

    /**
     * Agrupa los pedidos por su estado (pendiente, enviado, etc.).
     *
     * @return Map con estado del pedido como clave y número de pedidos como valor
     */
    public Map<String, Integer> pedidosPorEstado() { return agrupar("SELECT estado_pedido, COUNT(*) AS total FROM Pedido GROUP BY estado_pedido ORDER BY total DESC", "estado_pedido", "total"); }

    /**
     * Agrupa las mercancías por categoría.
     *
     * @return Map con categoría como clave y número de mercancías como valor
     */
    public Map<String, Integer> mercanciasPorCategoria() { return agrupar("SELECT categoria, COUNT(*) AS total FROM Mercancia GROUP BY categoria ORDER BY total DESC", "categoria", "total"); }

    /**
     * Obtiene el stock actual de cada mercancía ordenado de mayor a menor.
     *
     * @return Map con descripción de la mercancía como clave y stock actual como valor
     */
    public Map<String, Integer> stockMercancias() { return agrupar("SELECT descripcion, stock_actual FROM Mercancia ORDER BY stock_actual DESC", "descripcion", "stock_actual"); }

    /**
     * Agrupa los albaranes por su estado (recibido_completo, recibido_parcial, etc.).
     *
     * @return Map con estado del albarán como clave y número de albaranes como valor
     */
    public Map<String, Integer> albAranesPorEstado() { return agrupar("SELECT estado, COUNT(*) AS total FROM Albaran GROUP BY estado ORDER BY total DESC", "estado", "total"); }

    /**
     * Cuenta los pedidos agrupados por proveedor, haciendo JOIN entre Pedido y Proveedor.
     *
     * @return Map con nombre del proveedor como clave y número de pedidos como valor
     */
    public Map<String, Integer> pedidosPorProveedor() { return agrupar("SELECT p.nombre, COUNT(pe.id_pedido) AS total FROM Pedido pe JOIN Proveedor p ON pe.id_proveedor = p.id_proveedor GROUP BY p.nombre ORDER BY total DESC", "nombre", "total"); }

    /**
     * Obtiene el listado completo de proveedores con todos sus datos.
     *
     * @return lista de Maps donde cada Map representa un proveedor
     */
    public List<Map<String, String>> detalleProveedores() { return listar("SELECT nombre, direccion, telefono, email, pais, DATE_FORMAT(fecha_alta,'%d/%m/%Y') AS fecha_alta, estado FROM Proveedor ORDER BY nombre", new String[]{"nombre","direccion","telefono","email","pais","fecha_alta","estado"}); }

    /**
     * Obtiene el listado de pedidos con el nombre del proveedor asociado.
     *
     * @return lista de Maps donde cada Map representa un pedido
     */
    public List<Map<String, String>> detallePedidos() { return listar("SELECT pe.id_pedido, DATE_FORMAT(pe.fecha_pedido,'%d/%m/%Y') AS fecha_pedido, DATE_FORMAT(pe.fecha_entrega_prevista,'%d/%m/%Y') AS fecha_entrega_prevista, pr.nombre AS proveedor, pe.estado_pedido FROM Pedido pe JOIN Proveedor pr ON pe.id_proveedor = pr.id_proveedor ORDER BY pe.fecha_pedido DESC", new String[]{"id_pedido","fecha_pedido","fecha_entrega_prevista","proveedor","estado_pedido"}); }

    /**
     * Obtiene el listado de mercancías con su stock mínimo y actual para control de inventario.
     *
     * @return lista de Maps donde cada Map representa una mercancía
     */
    public List<Map<String, String>> detalleMercancias() { return listar("SELECT descripcion, categoria, precio_unitario, stock_minimo, stock_actual FROM Mercancia ORDER BY categoria, descripcion", new String[]{"descripcion","categoria","precio_unitario","stock_minimo","stock_actual"}); }

    // ══════════════════════════════════════════════════════
    //  UTILIDADES PRIVADAS
    // ══════════════════════════════════════════════════════

    /**
     * Ejecuta una query SQL que devuelve exactamente una fila con COUNT(*)
     * y retorna ese valor entero.
     *
     * @param sql la consulta SQL con COUNT(*)
     * @return el número entero resultado del COUNT, o 0 si hay error
     */
    private int contarFila(String sql) {
        try {
            Statement stmt = conector.getConnect().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("RepoDashboards contarFila – " + e.getMessage());
        }
        return 0;
    }

    /**
     * Ejecuta una query de agrupación y construye un Map con etiqueta y valor.
     * Mantiene el orden de filas del resultado usando LinkedHashMap.
     *
     * @param sql         la consulta SQL con GROUP BY
     * @param colEtiqueta nombre de la columna de texto (eje X de la gráfica)
     * @param colValor    nombre de la columna numérica (eje Y de la gráfica)
     * @return Map ordenado con etiqueta como clave y valor numérico como valor
     */
    private Map<String, Integer> agrupar(String sql, String colEtiqueta, String colValor) {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        try {
            Statement stmt = conector.getConnect().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String etiqueta = rs.getString(colEtiqueta);
                int valor = rs.getInt(colValor);
                resultado.put(etiqueta != null ? etiqueta : "Sin datos", valor);
            }
        } catch (SQLException e) {
            System.out.println("RepoDashboards agrupar – " + e.getMessage());
        }
        return resultado;
    }

    /**
     * Ejecuta una query que devuelve múltiples filas y construye una lista de Maps.
     * Cada Map representa una fila con los nombres de columna como claves.
     * Los valores nulos se sustituyen por el texto "-".
     *
     * @param sql      la consulta SQL
     * @param columnas array con los nombres de columnas a extraer
     * @return lista de Maps donde cada Map representa una fila del resultado
     */
    private List<Map<String, String>> listar(String sql, String[] columnas) {
        List<Map<String, String>> filas = new ArrayList<>();
        try {
            Statement stmt = conector.getConnect().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, String> fila = new LinkedHashMap<>();
                for (String col : columnas) {
                    String val = rs.getString(col);
                    fila.put(col, val != null ? val : "-");
                }
                filas.add(fila);
            }
        } catch (SQLException e) {
            System.out.println("RepoDashboards listar – " + e.getMessage());
        }
        return filas;
    }
}