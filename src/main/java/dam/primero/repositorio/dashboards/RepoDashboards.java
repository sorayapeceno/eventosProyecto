package dam.primero.repositorio.dashboards;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RepoDashboards {

    private MySqlConector conector;

    public RepoDashboards() {
        try {
            this.conector = new MySqlConector();
        } catch (MyException e) {
            System.out.println("RepoDashboards – Error al conectar: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════
    //  EVENTOS
    // ══════════════════════════════════════════════════════
    public int contarEventos() { return contarFila("SELECT COUNT(*) FROM proyectofinaljud.Evento"); }
    public Map<String, Integer> eventosPorEstado() { return agrupar("SELECT Estado, COUNT(*) AS total FROM proyectofinaljud.Evento GROUP BY Estado ORDER BY total DESC", "Estado", "total"); }
    public Map<String, Integer> eventosPorModalidad() { return agrupar("SELECT Modalidad, COUNT(*) AS total FROM proyectofinaljud.Evento GROUP BY Modalidad ORDER BY total DESC", "Modalidad", "total"); }
    public Map<String, Integer> eventosPorCiudad() { return agrupar("SELECT Ciudad, COUNT(*) AS total FROM proyectofinaljud.Evento WHERE Ciudad IS NOT NULL GROUP BY Ciudad ORDER BY total DESC", "Ciudad", "total"); }
    public List<Map<String, String>> detalleEventos() { return listar("SELECT Nombre, DATE_FORMAT(Fecha_Inicio,'%d/%m/%Y') AS Fecha_Inicio, DATE_FORMAT(Fecha_Fin,'%d/%m/%Y') AS Fecha_Fin, Ciudad, Estado, Modalidad, Capacidad, Lugar FROM proyectofinaljud.Evento ORDER BY Fecha_Inicio", new String[]{"Nombre","Fecha_Inicio","Fecha_Fin","Ciudad","Estado","Modalidad","Capacidad","Lugar"}); }

    // ══════════════════════════════════════════════════════
    //  PONENCIAS
    // ══════════════════════════════════════════════════════
    public int contarPonencias() { return contarFila("SELECT COUNT(*) FROM proyectofinaljud.Ponencia"); }
    public Map<String, Integer> ponenciasPorTipo() { return agrupar("SELECT Tipo, COUNT(*) AS total FROM proyectofinaljud.Ponencia GROUP BY Tipo ORDER BY total DESC", "Tipo", "total"); }
    public Map<String, Integer> ponenciasPorNivel() { return agrupar("SELECT Nivel, COUNT(*) AS total FROM proyectofinaljud.Ponencia WHERE Nivel IS NOT NULL GROUP BY Nivel ORDER BY total DESC", "Nivel", "total"); }
    public Map<String, Integer> ponenciasPorFormato() { return agrupar("SELECT Formato, COUNT(*) AS total FROM proyectofinaljud.Ponencia GROUP BY Formato ORDER BY total DESC", "Formato", "total"); }
    public Map<String, Integer> ponenciasPorTematica() { return agrupar("SELECT t.Tema, COUNT(*) AS total FROM proyectofinaljud.Ponencia p JOIN proyectofinaljud.Tematica t ON p.id_Tematica = t.id_Tematica GROUP BY t.Tema ORDER BY total DESC", "Tema", "total"); }
    public List<Map<String, String>> detallePonencias() { return listar("SELECT p.Titulo, t.Tema, p.Tipo, p.Nivel, p.Formato, p.Duracion, DATE_FORMAT(p.Fecha,'%d/%m/%Y') AS Fecha, p.Hora, p.Sala, e.Nombre AS Evento FROM proyectofinaljud.Ponencia p JOIN proyectofinaljud.Tematica t ON p.id_Tematica = t.id_Tematica JOIN proyectofinaljud.Evento e ON p.id_Evento = e.id_Evento ORDER BY p.Fecha, p.Hora", new String[]{"Titulo","Tema","Tipo","Nivel","Formato","Duracion","Fecha","Hora","Sala","Evento"}); }

    // ══════════════════════════════════════════════════════
    //  PONENTES
    // ══════════════════════════════════════════════════════
    public int contarPonentes() { return contarFila("SELECT COUNT(*) FROM proyectofinaljud.Ponente"); }
    public Map<String, Integer> ponentesPorEspecialidad() { return agrupar("SELECT Especialidad, COUNT(*) AS total FROM proyectofinaljud.Ponente GROUP BY Especialidad ORDER BY total DESC", "Especialidad", "total"); }
    public Map<String, Integer> ponentesPorNivelImparticion() { return agrupar("SELECT Nivel_Imparticion, COUNT(*) AS total FROM proyectofinaljud.Ponente GROUP BY Nivel_Imparticion ORDER BY total DESC", "Nivel_Imparticion", "total"); }
    public Map<String, Integer> topPonentes() { return agrupar("SELECT CONCAT(per.Nombre, ' ', per.Ap1) AS nombre, COUNT(pp.id_Ponencia) AS total FROM proyectofinaljud.Ponente_Ponencia pp JOIN proyectofinaljud.Ponente po ON pp.id_Ponente = po.id_Ponente JOIN proyectofinaljud.Persona per ON po.id_Persona = per.id_Persona GROUP BY per.Nombre, per.Ap1 ORDER BY total DESC LIMIT 10", "nombre", "total"); }
    public List<Map<String, String>> detallePonentes() { return listar("SELECT CONCAT(per.Nombre,' ',per.Ap1) AS Nombre, po.Especialidad, po.Nivel_Imparticion, COUNT(pp.id_Ponencia) AS Ponencias, per.Correo, per.Ciudad FROM proyectofinaljud.Ponente po JOIN proyectofinaljud.Persona per ON po.id_Persona = per.id_Persona LEFT JOIN proyectofinaljud.Ponente_Ponencia pp ON po.id_Ponente = pp.id_Ponente GROUP BY po.id_Ponente, per.Nombre, per.Ap1, po.Especialidad, po.Nivel_Imparticion, per.Correo, per.Ciudad ORDER BY Ponencias DESC", new String[]{"Nombre","Especialidad","Nivel_Imparticion","Ponencias","Correo","Ciudad"}); }

    // ══════════════════════════════════════════════════════
    //  VENTAS
    // ══════════════════════════════════════════════════════
    public int contarAsistentes() { return contarFila("SELECT COUNT(*) FROM ventas.Asistente"); }
    public int contarTickets() { return contarFila("SELECT COUNT(*) FROM ventas.Ticket"); }
    public int contarProductosVentas() { return contarFila("SELECT COUNT(*) FROM ventas.Producto"); }
    public Map<String, Integer> ticketsPorMetodoPago() { return agrupar("SELECT Metodo_Pago, COUNT(*) AS total FROM ventas.Ticket GROUP BY Metodo_Pago ORDER BY total DESC", "Metodo_Pago", "total"); }
    public Map<String, Integer> asistentePorNivel() { return agrupar("SELECT Nivel_Imparticion, COUNT(*) AS total FROM ventas.Asistente GROUP BY Nivel_Imparticion ORDER BY total DESC", "Nivel_Imparticion", "total"); }
    public Map<String, Integer> asistentePorTematica() { return agrupar("SELECT Tematica, COUNT(*) AS total FROM ventas.Asistente WHERE Tematica IS NOT NULL GROUP BY Tematica ORDER BY total DESC", "Tematica", "total"); }
    public Map<String, Integer> stockProductos() { return agrupar("SELECT Nombre_Producto, Stock_Disponible FROM ventas.Producto ORDER BY Stock_Disponible DESC", "Nombre_Producto", "Stock_Disponible"); }
    public Map<String, Integer> entradasPorEstado() { return agrupar("SELECT EstadoEntrada, COUNT(*) AS total FROM ventas.Entrada GROUP BY EstadoEntrada ORDER BY total DESC", "EstadoEntrada", "total"); }
    public List<Map<String, String>> detalleTickets() { return listar("SELECT t.ID_Ticket, DATE_FORMAT(t.Fecha_Compra,'%d/%m/%Y') AS Fecha_Compra, t.Precio_Final, t.Metodo_Pago, t.Descuento, t.Codigo_Promocional, a.Tematica, a.Nivel_Imparticion FROM ventas.Ticket t JOIN ventas.Asistente a ON t.ID_Asistente = a.ID_Asistente ORDER BY t.Fecha_Compra DESC", new String[]{"ID_Ticket","Fecha_Compra","Precio_Final","Metodo_Pago","Descuento","Codigo_Promocional","Tematica","Nivel_Imparticion"}); }
    public List<Map<String, String>> detalleProductosVentas() { return listar("SELECT Nombre_Producto, Precio, Stock_Disponible, Tipo_IVA, Descuento, Descripcion_Producto FROM ventas.Producto ORDER BY Precio DESC", new String[]{"Nombre_Producto","Precio","Stock_Disponible","Tipo_IVA","Descuento","Descripcion_Producto"}); }

    // ══════════════════════════════════════════════════════
    //  CRM
    // ══════════════════════════════════════════════════════
    public int contarOrganizaciones() { return contarFila("SELECT COUNT(*) FROM crm.FormularioOrganizacion"); }
    public int contarOportunidades() { return contarFila("SELECT COUNT(*) FROM crm.FormularioOportunidad"); }
    public int contarProductosCRM() { return contarFila("SELECT COUNT(*) FROM crm.FormularioProducto"); }
    public int contarFichasCliente() { return contarFila("SELECT COUNT(*) FROM crm.FichaCliente"); }
    public Map<String, Integer> organizacionesPorTipo() { return agrupar("SELECT Tipo_Organizacion, COUNT(*) AS total FROM crm.FormularioOrganizacion GROUP BY Tipo_Organizacion ORDER BY total DESC", "Tipo_Organizacion", "total"); }
    public Map<String, Integer> productosPorCategoria() { return agrupar("SELECT Categoria, COUNT(*) AS total FROM crm.FormularioProducto GROUP BY Categoria ORDER BY total DESC", "Categoria", "total"); }
    public Map<String, Integer> stockPorProducto() { return agrupar("SELECT Nombre, Stock FROM crm.FormularioProducto ORDER BY Stock DESC", "Nombre", "Stock"); }
    public Map<String, Integer> usuariosPorRol() { return agrupar("SELECT Rol, COUNT(*) AS total FROM crm.Usuario GROUP BY Rol ORDER BY total DESC", "Rol", "total"); }
    public Map<String, Integer> paginasPorTipo() { return agrupar("SELECT t.Nombre_Tipo, COUNT(*) AS total FROM crm.PaginaWeb p JOIN crm.TipoPagina t ON p.Id_Tipo_Pagina = t.Id_Tipo_Pagina GROUP BY t.Nombre_Tipo ORDER BY total DESC", "Nombre_Tipo", "total"); }
    public List<Map<String, String>> detalleOrganizaciones() { return listar("SELECT Nombre, Tipo_Organizacion, Email, Telefono, Direccion FROM crm.FormularioOrganizacion ORDER BY Nombre", new String[]{"Nombre","Tipo_Organizacion","Email","Telefono","Direccion"}); }
    public List<Map<String, String>> detalleProductosCRM() { return listar("SELECT Nombre, Descripcion, Precio, Stock, Categoria FROM crm.FormularioProducto ORDER BY Categoria, Nombre", new String[]{"Nombre","Descripcion","Precio","Stock","Categoria"}); }
    public List<Map<String, String>> detalleOportunidades() { return listar("SELECT Titulo, Descripcion, DATE_FORMAT(Fecha_Inicio,'%d/%m/%Y') AS Fecha_Inicio, Tipos_Oportunidad FROM crm.FormularioOportunidad ORDER BY Fecha_Inicio", new String[]{"Titulo","Descripcion","Fecha_Inicio","Tipos_Oportunidad"}); }

    // ══════════════════════════════════════════════════════
    //  RELACIONES INSTITUCIONALES
    // ══════════════════════════════════════════════════════
    public int contarOrganizacionesRel() { return contarFila("SELECT COUNT(*) FROM relaciones.Organizacion"); }
    public int contarOportunidadesRel() { return contarFila("SELECT COUNT(*) FROM relaciones.Oportunidad"); }
    public int contarRecintos() { return contarFila("SELECT COUNT(*) FROM relaciones.Recinto"); }
    public int contarPatrocinios() { return contarFila("SELECT COUNT(*) FROM relaciones.Patrocinio"); }
    public Map<String, Integer> oportunidadesPorEstado() { return agrupar("SELECT Estado, COUNT(*) AS total FROM relaciones.Oportunidad GROUP BY Estado ORDER BY total DESC", "Estado", "total"); }
    public Map<String, Integer> patrociniosPorTipo() { return agrupar("SELECT Tipo, COUNT(*) AS total FROM relaciones.Patrocinio GROUP BY Tipo ORDER BY total DESC", "Tipo", "total"); }
    public Map<String, Integer> colaboracionesPorTipo() { return agrupar("SELECT Tipo, COUNT(*) AS total FROM relaciones.Colaboracion GROUP BY Tipo ORDER BY total DESC", "Tipo", "total"); }
    public Map<String, Integer> organizacionesPorCiudad() { return agrupar("SELECT Ciudad, COUNT(*) AS total FROM relaciones.Organizacion WHERE Ciudad IS NOT NULL GROUP BY Ciudad ORDER BY total DESC", "Ciudad", "total"); }
    public Map<String, Integer> recintoPorCapacidad() { return agrupar("SELECT Nombre, Capacidad FROM relaciones.Recinto ORDER BY Capacidad DESC", "Nombre", "Capacidad"); }
    public List<Map<String, String>> detalleOrganizacionesRel() { return listar("SELECT Nombre, Ciudad, Telefono, Email, DATE_FORMAT(Fecha_Registro,'%d/%m/%Y') AS Fecha_Registro FROM relaciones.Organizacion ORDER BY Nombre", new String[]{"Nombre","Ciudad","Telefono","Email","Fecha_Registro"}); }
    public List<Map<String, String>> detalleOportunidadesRel() { return listar("SELECT Descripcion, DATE_FORMAT(Fecha_Inicio,'%d/%m/%Y') AS Fecha_Inicio, DATE_FORMAT(Fecha_Fin,'%d/%m/%Y') AS Fecha_Fin, Estado, Presupuesto FROM relaciones.Oportunidad ORDER BY Fecha_Inicio", new String[]{"Descripcion","Fecha_Inicio","Fecha_Fin","Estado","Presupuesto"}); }
    public List<Map<String, String>> detalleColaboraciones() { return listar("SELECT Tipo, DATE_FORMAT(Fecha,'%d/%m/%Y') AS Fecha, Firma, Dinero, Convenio FROM relaciones.Colaboracion ORDER BY Fecha", new String[]{"Tipo","Fecha","Firma","Dinero","Convenio"}); }

    // ══════════════════════════════════════════════════════
    //  LOGÍSTICA
    // ══════════════════════════════════════════════════════
    public int contarProveedores() { return contarFila("SELECT COUNT(*) FROM logistica.Proveedor"); }
    public int contarPedidos() { return contarFila("SELECT COUNT(*) FROM logistica.Pedido"); }
    public int contarMercancias() { return contarFila("SELECT COUNT(*) FROM logistica.Mercancia"); }
    public int contarAlbaranes() { return contarFila("SELECT COUNT(*) FROM logistica.Albaran"); }

    public Map<String, Integer> proveedoresPorEstado() {
        return agrupar("SELECT estado, COUNT(*) AS total FROM logistica.Proveedor GROUP BY estado ORDER BY total DESC", "estado", "total");
    }
    public Map<String, Integer> pedidosPorEstado() {
        return agrupar("SELECT estado_pedido, COUNT(*) AS total FROM logistica.Pedido GROUP BY estado_pedido ORDER BY total DESC", "estado_pedido", "total");
    }
    public Map<String, Integer> mercanciasPorCategoria() {
        return agrupar("SELECT categoria, COUNT(*) AS total FROM logistica.Mercancia GROUP BY categoria ORDER BY total DESC", "categoria", "total");
    }
    public Map<String, Integer> stockMercancias() {
        return agrupar("SELECT descripcion, stock_actual FROM logistica.Mercancia ORDER BY stock_actual DESC", "descripcion", "stock_actual");
    }
    public Map<String, Integer> albAranesPorEstado() {
        return agrupar("SELECT estado, COUNT(*) AS total FROM logistica.Albaran GROUP BY estado ORDER BY total DESC", "estado", "total");
    }
    public Map<String, Integer> pedidosPorProveedor() {
        return agrupar("SELECT p.nombre, COUNT(pe.id_pedido) AS total FROM logistica.Pedido pe JOIN logistica.Proveedor p ON pe.id_proveedor = p.id_proveedor GROUP BY p.nombre ORDER BY total DESC", "nombre", "total");
    }

    public List<Map<String, String>> detalleProveedores() {
        return listar("SELECT nombre, direccion, telefono, email, pais, DATE_FORMAT(fecha_alta,'%d/%m/%Y') AS fecha_alta, estado FROM logistica.Proveedor ORDER BY nombre", new String[]{"nombre","direccion","telefono","email","pais","fecha_alta","estado"});
    }
    public List<Map<String, String>> detallePedidos() {
        return listar("SELECT pe.id_pedido, DATE_FORMAT(pe.fecha_pedido,'%d/%m/%Y') AS fecha_pedido, DATE_FORMAT(pe.fecha_entrega_prevista,'%d/%m/%Y') AS fecha_entrega_prevista, pr.nombre AS proveedor, pe.estado_pedido FROM logistica.Pedido pe JOIN logistica.Proveedor pr ON pe.id_proveedor = pr.id_proveedor ORDER BY pe.fecha_pedido DESC", new String[]{"id_pedido","fecha_pedido","fecha_entrega_prevista","proveedor","estado_pedido"});
    }
    public List<Map<String, String>> detalleMercancias() {
        return listar("SELECT descripcion, categoria, precio_unitario, stock_minimo, stock_actual FROM logistica.Mercancia ORDER BY categoria, descripcion", new String[]{"descripcion","categoria","precio_unitario","stock_minimo","stock_actual"});
    }

    // ══════════════════════════════════════════════════════
    //  UTILIDADES PRIVADAS
    // ══════════════════════════════════════════════════════
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