package dam.primero.repositorio.ventas;

/**
 * Repositorio JDBC para la gestión de tickets y sus líneas de compra.
 * Incluye operaciones de consulta, creación con descuento de stock en transacción,
 * y cancelación con restauración de stock. También proporciona métodos
 * para obtener el siguiente ID disponible de ticket y de línea.
 *
 * @author Sergio Galan
 */

import dam.primero.modelos.ventas.*;
import dam.primero.config.ventas.MySqlConnectorVentas;
import dam.primero.exception.MyException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketRepository {

    private Ticket mapTicket(ResultSet rs) throws SQLException {
        Ticket t = new Ticket();
        t.setId_Ticket(rs.getLong("ID_Ticket"));
        t.setCodigoQR(rs.getString("Codigo_QR"));
        t.setFechaCompra(rs.getDate("Fecha_Compra") != null
                         ? rs.getDate("Fecha_Compra").toLocalDate() : null);
        t.setDescuento(rs.getDouble("Descuento"));
        t.setCodPromocional(rs.getString("Codigo_Promocional"));

        String metodoPagoStr = rs.getString("Metodo_Pago");
        if (metodoPagoStr != null) {
            try {
                t.setMetodoPago(MetodoPago.valueOf(metodoPagoStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                t.setMetodoPago(MetodoPago.TARJETA);
            }
        }

        Asistente a = new Asistente();
        a.setId_Asistente(rs.getLong("ID_Asistente"));
        t.setDuenio(a);

        return t;
    }

    public List<Ticket> findAll() throws MyException {
        List<Ticket> lista = new ArrayList<>();
        String sql = "SELECT t.*, a.Tematica, a.Bio, a.Nivel_Imparticion " +
                     "FROM Ticket t " +
                     "JOIN Asistente a ON t.ID_Asistente = a.ID_Asistente " +
                     "ORDER BY t.Fecha_Compra DESC";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Ticket t = mapTicket(rs);
                t.getDuenio().setTematica(rs.getString("Tematica"));
                t.getDuenio().setBio(rs.getString("Bio"));
                t.getDuenio().setNivelImparticion(rs.getString("Nivel_Imparticion"));
                lista.add(t);
            }
        } catch (SQLException e) {
            throw new MyException("Error al listar tickets: " + e.getMessage());
        } finally {
            conector.release();
        }
        return lista;
    }

    public Optional<Ticket> findByIdConLineas(long id) throws MyException {
        String sqlTicket = "SELECT t.*, a.Tematica, a.Bio, a.Nivel_Imparticion, " +
                           "a.Direccion, a.Observaciones, a.Total_Gastado " +
                           "FROM Ticket t " +
                           "JOIN Asistente a ON t.ID_Asistente = a.ID_Asistente " +
                           "WHERE t.ID_Ticket = ?";

        String sqlLineas =
            "SELECT lt.ID_LineaTicket, lt.ID_Ticket, lt.Cantidad, " +
            "lt.IVA_Cuota, lt.Subtotal_Base, lt.Total_Linea, " +
            "p.ID_Producto, p.Nombre_Producto, p.Precio, p.Stock_Disponible, " +
            "p.Descripcion_Producto, p.Tipo_IVA, p.Descuento, " +
            "e.ID_Entrada, e.Zona, e.Asiento, e.Acceso_Permitido, e.Accesos_Adicionales, " +
            "e.Validez_Horas, e.EstadoEntrada, " +
            "v.ID_Vip, v.Beneficios_Incluidos, v.Servicios_Adicionales, v.Precio_Extra, " +
            "es.ID_Estandar, es.Incluye_Regalo, " +
            "b.ID_Beca, b.Motivo_Beca, b.Porcentaje_Descuento AS BecaDescPct, b.Requisitos, " +
            "t.ID_Textil, t.Talla, t.Color, t.Material, t.Genero, t.Tipo_Textil, " +
            "c.ID_Camisetas, c.Modelo, c.Estampado, " +
            "ac.ID_Accesorios, ac.Tipo_Accesorio, " +
            "o.ID_Otros, o.Descripcion AS DescripcionOtros " +
            "FROM Linea_Ticket lt " +
            "JOIN Producto p         ON lt.ID_Producto = p.ID_Producto " +
            "LEFT JOIN Entrada e     ON e.ID_Producto  = p.ID_Producto " +
            "LEFT JOIN VIP v         ON v.ID_Entrada   = e.ID_Entrada " +
            "LEFT JOIN Estandar es   ON es.ID_Entrada  = e.ID_Entrada " +
            "LEFT JOIN Beca b        ON b.ID_Entrada   = e.ID_Entrada " +
            "LEFT JOIN Textil t      ON t.ID_Producto  = p.ID_Producto " +
            "LEFT JOIN Camisetas c   ON c.ID_Textil    = t.ID_Textil " +
            "LEFT JOIN Accesorios ac ON ac.ID_Textil   = t.ID_Textil " +
            "LEFT JOIN Otros o       ON o.ID_Producto  = p.ID_Producto " +
            "WHERE lt.ID_Ticket = ?";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conector.getConnect();
            Ticket ticket = null;

            ps = con.prepareStatement(sqlTicket);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();

            ticket = mapTicket(rs);
            Asistente a = ticket.getDuenio();
            a.setTematica(rs.getString("Tematica"));
            a.setBio(rs.getString("Bio"));
            a.setNivelImparticion(rs.getString("Nivel_Imparticion"));
            a.setDireccion(rs.getString("Direccion"));
            a.setObservaciones(rs.getString("Observaciones"));
            a.setTotalGastado(rs.getDouble("Total_Gastado"));

            PreparedStatement ps2 = con.prepareStatement(sqlLineas);
            ps2.setLong(1, id);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                Producto prod = ProductoRepository.mapProducto(rs2);

                LineaTicket linea = new LineaTicket();
                linea.setId_LineaTicket(rs2.getLong("ID_LineaTicket"));
                linea.setId_Ticket(id);
                linea.setProducto(prod);
                linea.setCantidad(rs2.getInt("Cantidad"));
                linea.setIvaCuota(rs2.getDouble("IVA_Cuota"));
                linea.setSubtotalBase(rs2.getDouble("Subtotal_Base"));
                linea.setTotalLinea(rs2.getDouble("Total_Linea"));

                ticket.addLinea(linea);
            }

            return Optional.of(ticket);

        } catch (SQLException e) {
            throw new MyException("Error al buscar ticket id=" + id + ": " + e.getMessage());
        } finally {
            conector.release();
        }
    }

    public void saveConLineas(Ticket ticket) throws MyException {
        String sqlTicket = "INSERT INTO Ticket " +
                           "(ID_Ticket, ID_Asistente, Codigo_QR, Fecha_Compra, " +
                           "Precio_Final, Metodo_Pago, Descuento, Codigo_Promocional) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlLinea = "INSERT INTO Linea_Ticket " +
                          "(ID_LineaTicket, ID_Ticket, ID_Producto, Cantidad, " +
                          "IVA_Cuota, Subtotal_Base, Total_Linea) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlStock = "UPDATE Producto SET Stock_Disponible = Stock_Disponible - ? " +
                          "WHERE ID_Producto = ? AND Stock_Disponible >= ?";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = conector.getConnect();
        try {
            con.setAutoCommit(false);

            PreparedStatement psTicket = con.prepareStatement(sqlTicket);
            psTicket.setLong(1, ticket.getId_Ticket());
            psTicket.setLong(2, ticket.getDuenio().getId_Asistente());
            psTicket.setString(3, ticket.getCodigoQR());
            psTicket.setDate(4, ticket.getFechaCompra() != null
                                ? Date.valueOf(ticket.getFechaCompra()) : null);
            psTicket.setDouble(5, ticket.getPrecioFinal());
            psTicket.setString(6, ticket.getMetodoPago() != null
                                  ? ticket.getMetodoPago().name() : "TARJETA");
            psTicket.setDouble(7, ticket.getDescuento());
            psTicket.setString(8, ticket.getCodPromocional());
            psTicket.executeUpdate();

            for (LineaTicket linea : ticket.getLineas()) {
                linea.calcularTotales();

                PreparedStatement psLinea = con.prepareStatement(sqlLinea);
                psLinea.setLong(1, linea.getId_LineaTicket());
                psLinea.setLong(2, ticket.getId_Ticket());
                psLinea.setLong(3, linea.getProducto().getId_Producto());
                psLinea.setInt(4, linea.getCantidad());
                psLinea.setDouble(5, linea.getIvaCuota());
                psLinea.setDouble(6, linea.getSubtotalBase());
                psLinea.setDouble(7, linea.getTotalLinea());
                psLinea.executeUpdate();

                PreparedStatement psStock = con.prepareStatement(sqlStock);
                psStock.setInt(1, linea.getCantidad());
                psStock.setLong(2, linea.getProducto().getId_Producto());
                psStock.setInt(3, linea.getCantidad());
                int filas = psStock.executeUpdate();
                if (filas == 0) {
                    con.rollback();
                    throw new MyException("Stock insuficiente para: " +
                                          linea.getProducto().getNombreProducto());
                }
            }

            con.commit();

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) {}
            throw new MyException("Error al guardar ticket: " + e.getMessage());
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) {}
            conector.release();
        }
    }

    public void cancelar(long id_Ticket) throws MyException {
        String sqlGetLineas = "SELECT ID_Producto, Cantidad FROM Linea_Ticket WHERE ID_Ticket = ?";
        String sqlDelLineas = "DELETE FROM Linea_Ticket WHERE ID_Ticket = ?";
        String sqlDelTicket = "DELETE FROM Ticket WHERE ID_Ticket = ?";
        String sqlStock     = "UPDATE Producto SET Stock_Disponible = Stock_Disponible + ? " +
                              "WHERE ID_Producto = ?";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = conector.getConnect();
        try {
            con.setAutoCommit(false);

            PreparedStatement psLineas = con.prepareStatement(sqlGetLineas);
            psLineas.setLong(1, id_Ticket);
            ResultSet rs = psLineas.executeQuery();
            while (rs.next()) {
                PreparedStatement psStock = con.prepareStatement(sqlStock);
                psStock.setInt(1, rs.getInt("Cantidad"));
                psStock.setLong(2, rs.getLong("ID_Producto"));
                psStock.executeUpdate();
            }

            PreparedStatement psDel1 = con.prepareStatement(sqlDelLineas);
            psDel1.setLong(1, id_Ticket);
            psDel1.executeUpdate();

            PreparedStatement psDel2 = con.prepareStatement(sqlDelTicket);
            psDel2.setLong(1, id_Ticket);
            psDel2.executeUpdate();

            con.commit();

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) {}
            throw new MyException("Error al cancelar ticket: " + e.getMessage());
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) {}
            conector.release();
        }
    }

    public long getNextTicketId() throws MyException {
        String sql = "SELECT COALESCE(MAX(ID_Ticket), 99) + 1 AS next_id FROM Ticket";
        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getLong("next_id");
        } catch (SQLException e) {
            throw new MyException("Error al obtener siguiente ID ticket: " + e.getMessage());
        } finally {
            conector.release();
        }
        return 100;
    }

    public long getNextLineaId() throws MyException {
        String sql = "SELECT COALESCE(MAX(ID_LineaTicket), 500) + 1 AS next_id FROM Linea_Ticket";
        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getLong("next_id");
        } catch (SQLException e) {
            throw new MyException("Error al obtener siguiente ID línea: " + e.getMessage());
        } finally {
            conector.release();
        }
        return 501;
    }
}
