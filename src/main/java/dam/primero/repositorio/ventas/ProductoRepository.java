package dam.primero.repositorio.ventas;

/**
 * Repositorio JDBC para la jerarquía de productos (Producto, Entrada, VIP, Estandar,
 * Beca, Textil, Camisetas, Accesorios, Otros).
 * Gestiona el alta, edición y borrado en cascada sobre todas las tablas relacionadas,
 * usando transacciones para garantizar la consistencia de los datos.
 *
 * @author Alexandru Cozaru
 */

import dam.primero.modelos.ventas.*;
import dam.primero.config.ventas.MySqlConnectorVentas;
import dam.primero.exception.MyException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoRepository {

    private String SQL_FULL =
        "SELECT p.ID_Producto, p.Nombre_Producto, p.Precio, p.Stock_Disponible, " +
        "  p.Descripcion_Producto, p.Tipo_IVA, p.Descuento, " +
        "  e.ID_Entrada, e.Zona, e.Asiento, e.Acceso_Permitido, e.Accesos_Adicionales, " +
        "  e.Validez_Horas, e.EstadoEntrada, " +
        "  v.ID_Vip, v.Beneficios_Incluidos, v.Servicios_Adicionales, v.Precio_Extra, " +
        "  es.ID_Estandar, es.Incluye_Regalo, " +
        "  b.ID_Beca, b.Motivo_Beca, b.Porcentaje_Descuento AS BecaDescPct, b.Requisitos, " +
        "  t.ID_Textil, t.Talla, t.Color, t.Material, t.Genero, t.Tipo_Textil, " +
        "  c.ID_Camisetas, c.Modelo, c.Estampado, " +
        "  ac.ID_Accesorios, ac.Tipo_Accesorio, " +
        "  o.ID_Otros, o.Descripcion AS DescripcionOtros " +
        "FROM Producto p " +
        "LEFT JOIN Entrada e     ON e.ID_Producto  = p.ID_Producto " +
        "LEFT JOIN VIP v         ON v.ID_Entrada   = e.ID_Entrada " +
        "LEFT JOIN Estandar es   ON es.ID_Entrada  = e.ID_Entrada " +
        "LEFT JOIN Beca b        ON b.ID_Entrada   = e.ID_Entrada " +
        "LEFT JOIN Textil t      ON t.ID_Producto  = p.ID_Producto " +
        "LEFT JOIN Camisetas c   ON c.ID_Textil    = t.ID_Textil " +
        "LEFT JOIN Accesorios ac ON ac.ID_Textil   = t.ID_Textil " +
        "LEFT JOIN Otros o       ON o.ID_Producto  = p.ID_Producto ";

    static Producto mapProducto(ResultSet rs) throws SQLException {
        long    idProd = rs.getLong("ID_Producto");
        String  nombre = rs.getString("Nombre_Producto");
        double  precio = rs.getDouble("Precio");
        int     stock  = rs.getInt("Stock_Disponible");
        String  desc   = rs.getString("Descripcion_Producto");
        TipoIVA iva    = TipoIVA.fromString(rs.getString("Tipo_IVA"));
        double  dto    = rs.getDouble("Descuento");

        rs.getLong("ID_Vip");
        if (!rs.wasNull()) {
            VIP p = new VIP();
            fillBase(p, idProd, nombre, precio, stock, desc, iva, dto);
            fillEntrada(p, rs);
            p.setId_Vip(rs.getLong("ID_Vip"));
            p.setBeneficiosIncluidos(rs.getString("Beneficios_Incluidos"));
            p.setServiciosAdicionales(rs.getString("Servicios_Adicionales"));
            p.setPrecioExtra(rs.getDouble("Precio_Extra"));
            return p;
        }

        rs.getLong("ID_Estandar");
        if (!rs.wasNull()) {
            Estandar p = new Estandar();
            fillBase(p, idProd, nombre, precio, stock, desc, iva, dto);
            fillEntrada(p, rs);
            p.setId_Estandar(rs.getLong("ID_Estandar"));
            p.setIncluyeRegalo(rs.getString("Incluye_Regalo"));
            return p;
        }

        rs.getLong("ID_Beca");
        if (!rs.wasNull()) {
            Beca p = new Beca();
            fillBase(p, idProd, nombre, precio, stock, desc, iva, dto);
            fillEntrada(p, rs);
            p.setId_Beca(rs.getLong("ID_Beca"));
            p.setMotivoBeca(rs.getString("Motivo_Beca"));
            p.setPorcentajeDescuento(rs.getDouble("BecaDescPct"));
            p.setRequisitos(rs.getString("Requisitos"));
            return p;
        }

        rs.getLong("ID_Camisetas");
        if (!rs.wasNull()) {
            Camisetas p = new Camisetas();
            fillBase(p, idProd, nombre, precio, stock, desc, iva, dto);
            fillTextil(p, rs);
            p.setId_Camisetas(rs.getLong("ID_Camisetas"));
            p.setModelo(rs.getString("Modelo"));
            p.setEstampado(rs.getString("Estampado"));
            return p;
        }

        rs.getLong("ID_Accesorios");
        if (!rs.wasNull()) {
            Accesorios p = new Accesorios();
            fillBase(p, idProd, nombre, precio, stock, desc, iva, dto);
            fillTextil(p, rs);
            p.setId_Accesorios(rs.getLong("ID_Accesorios"));
            p.setTipoAccesorio(rs.getString("Tipo_Accesorio"));
            return p;
        }

        Otros p = new Otros();
        fillBase(p, idProd, nombre, precio, stock, desc, iva, dto);
        rs.getLong("ID_Otros");
        if (!rs.wasNull()) {
            p.setId_Otros(rs.getLong("ID_Otros"));
            p.setDescripcion(rs.getString("DescripcionOtros"));
        }
        return p;
    }

    private static void fillBase(Producto p, long id, String nombre, double precio,
                                  int stock, String desc, TipoIVA iva, double dto) {
        p.setId_Producto(id);
        p.setNombreProducto(nombre);
        p.setPrecio(precio);
        p.setStockDisponible(stock);
        p.setDescripcionProducto(desc);
        p.setTipoIVA(iva);
        p.setDescuento(dto);
    }

    static void fillEntrada(Entrada e, ResultSet rs) throws SQLException {
        e.setId_Entrada(rs.getLong("ID_Entrada"));
        e.setZona(rs.getString("Zona"));
        e.setAsiento(rs.getString("Asiento"));
        e.setAccesoPermitido(rs.getString("Acceso_Permitido"));
        e.setAccesosAdicionales(rs.getString("Accesos_Adicionales"));
        e.setValidezHoras(rs.getInt("Validez_Horas"));
        String estado = rs.getString("EstadoEntrada");
        if (estado != null) {
            try { e.setEstadoEntrada(EstadoEntrada.valueOf(estado.toUpperCase())); }
            catch (IllegalArgumentException ignored) {}
        }
    }

    static void fillTextil(Textil t, ResultSet rs) throws SQLException {
        t.setId_Textil(rs.getLong("ID_Textil"));
        t.setTalla(rs.getString("Talla"));
        t.setColor(rs.getString("Color"));
        t.setMaterial(rs.getString("Material"));
        String genero = rs.getString("Genero");
        if (genero != null) {
            try { t.setGenero(Genero.valueOf(genero.toUpperCase())); }
            catch (IllegalArgumentException ignored) {}
        }
        t.setTipoTextil(rs.getString("Tipo_Textil"));
    }

    public List<Producto> findAll() throws MyException {
        List<Producto> lista = new ArrayList<>();
        String sql = SQL_FULL + "ORDER BY p.ID_Producto";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapProducto(rs));
            }
        } catch (SQLException e) {
            throw new MyException("Error al listar productos: " + e.getMessage());
        } finally {
            conector.release();
        }
        return lista;
    }

    public Optional<Producto> findById(long id) throws MyException {
        String sql = SQL_FULL + "WHERE p.ID_Producto = ?";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapProducto(rs));
            }
        } catch (SQLException e) {
            throw new MyException("Error al buscar producto: " + e.getMessage());
        } finally {
            conector.release();
        }
        return Optional.empty();
    }

    public void save(Producto p) throws MyException {
        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = conector.getConnect();
        try {
            con.setAutoCommit(false);
            insertProducto(con, p);

            if (p instanceof VIP) {
                // Obtener siguiente ID de Entrada
                PreparedStatement psId = con.prepareStatement(
                    "SELECT COALESCE(MAX(ID_Entrada), 300) + 1 AS nid FROM Entrada");
                ResultSet rsId = psId.executeQuery();
                long eId = rsId.next() ? rsId.getLong("nid") : 301;

                insertEntrada(con, (Entrada) p, eId);
                ((Entrada) p).setId_Entrada(eId);

                // Obtener siguiente ID de VIP
                PreparedStatement psId2 = con.prepareStatement(
                    "SELECT COALESCE(MAX(ID_Vip), 200) + 1 AS nid FROM VIP");
                ResultSet rsId2 = psId2.executeQuery();
                long vId = rsId2.next() ? rsId2.getLong("nid") : 201;

                insertVip(con, (VIP) p, vId, eId);

            } else if (p instanceof Estandar) {
                PreparedStatement psId = con.prepareStatement(
                    "SELECT COALESCE(MAX(ID_Entrada), 300) + 1 AS nid FROM Entrada");
                ResultSet rsId = psId.executeQuery();
                long eId = rsId.next() ? rsId.getLong("nid") : 301;

                insertEntrada(con, (Entrada) p, eId);
                ((Entrada) p).setId_Entrada(eId);

                PreparedStatement psId2 = con.prepareStatement(
                    "SELECT COALESCE(MAX(ID_Estandar), 300) + 1 AS nid FROM Estandar");
                ResultSet rsId2 = psId2.executeQuery();
                long sId = rsId2.next() ? rsId2.getLong("nid") : 301;

                insertEstandar(con, (Estandar) p, sId, eId);

            } else if (p instanceof Beca) {
                PreparedStatement psId = con.prepareStatement(
                    "SELECT COALESCE(MAX(ID_Entrada), 300) + 1 AS nid FROM Entrada");
                ResultSet rsId = psId.executeQuery();
                long eId = rsId.next() ? rsId.getLong("nid") : 301;

                insertEntrada(con, (Entrada) p, eId);
                ((Entrada) p).setId_Entrada(eId);

                PreparedStatement psId2 = con.prepareStatement(
                    "SELECT COALESCE(MAX(ID_Beca), 400) + 1 AS nid FROM Beca");
                ResultSet rsId2 = psId2.executeQuery();
                long bId = rsId2.next() ? rsId2.getLong("nid") : 401;

                insertBeca(con, (Beca) p, bId, eId);

            } else if (p instanceof Camisetas) {
                PreparedStatement psId = con.prepareStatement(
                    "SELECT COALESCE(MAX(ID_Textil), 20) + 1 AS nid FROM Textil");
                ResultSet rsId = psId.executeQuery();
                long tId = rsId.next() ? rsId.getLong("nid") : 21;

                insertTextil(con, (Textil) p, tId);
                ((Textil) p).setId_Textil(tId);

                PreparedStatement psId2 = con.prepareStatement(
                    "SELECT COALESCE(MAX(ID_Camisetas), 1) + 1 AS nid FROM Camisetas");
                ResultSet rsId2 = psId2.executeQuery();
                long cId = rsId2.next() ? rsId2.getLong("nid") : 2;

                insertCamiseta(con, (Camisetas) p, cId, tId);

            } else if (p instanceof Accesorios) {
                PreparedStatement psId = con.prepareStatement(
                    "SELECT COALESCE(MAX(ID_Textil), 20) + 1 AS nid FROM Textil");
                ResultSet rsId = psId.executeQuery();
                long tId = rsId.next() ? rsId.getLong("nid") : 21;

                insertTextil(con, (Textil) p, tId);
                ((Textil) p).setId_Textil(tId);

                PreparedStatement psId2 = con.prepareStatement(
                    "SELECT COALESCE(MAX(ID_Accesorios), 1000) + 1 AS nid FROM Accesorios");
                ResultSet rsId2 = psId2.executeQuery();
                long aId = rsId2.next() ? rsId2.getLong("nid") : 1001;

                insertAccesorio(con, (Accesorios) p, aId, tId);

            } else if (p instanceof Otros) {
                PreparedStatement psId = con.prepareStatement(
                    "SELECT COALESCE(MAX(ID_Otros), 200) + 1 AS nid FROM Otros");
                ResultSet rsId = psId.executeQuery();
                long oId = rsId.next() ? rsId.getLong("nid") : 201;

                insertOtros(con, (Otros) p, oId);
            }

            con.commit();
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) {}
            throw new MyException("Error al guardar producto: " + e.getMessage());
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) {}
            conector.release();
        }
    }

    public void update(Producto p) throws MyException {
        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = conector.getConnect();
        try {
            con.setAutoCommit(false);
            updateProducto(con, p);

            if      (p instanceof VIP)        { updateEntrada(con, (Entrada) p);  updateVip(con, (VIP) p); }
            else if (p instanceof Estandar)   { updateEntrada(con, (Entrada) p);  updateEstandar(con, (Estandar) p); }
            else if (p instanceof Beca)       { updateEntrada(con, (Entrada) p);  updateBeca(con, (Beca) p); }
            else if (p instanceof Camisetas)  { updateTextil(con, (Textil) p);    updateCamiseta(con, (Camisetas) p); }
            else if (p instanceof Accesorios) { updateTextil(con, (Textil) p);    updateAccesorio(con, (Accesorios) p); }
            else if (p instanceof Otros)      { updateOtros(con, (Otros) p); }

            con.commit();
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) {}
            throw new MyException("Error al actualizar producto: " + e.getMessage());
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) {}
            conector.release();
        }
    }

    public boolean deleteById(long id) throws MyException {
        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = conector.getConnect();
        try {
            // Comprobar si tiene ventas asociadas
            PreparedStatement psCheck = con.prepareStatement(
                "SELECT COUNT(*) FROM Linea_Ticket WHERE ID_Producto = ?");
            psCheck.setLong(1, id);
            ResultSet rsCheck = psCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) > 0) return false;

            con.setAutoCommit(false);

            PreparedStatement ps1 = con.prepareStatement(
                "DELETE FROM VIP WHERE ID_Entrada IN (SELECT ID_Entrada FROM Entrada WHERE ID_Producto=?)");
            ps1.setLong(1, id);
            ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(
                "DELETE FROM Estandar WHERE ID_Entrada IN (SELECT ID_Entrada FROM Entrada WHERE ID_Producto=?)");
            ps2.setLong(1, id);
            ps2.executeUpdate();

            PreparedStatement ps3 = con.prepareStatement(
                "DELETE FROM Beca WHERE ID_Entrada IN (SELECT ID_Entrada FROM Entrada WHERE ID_Producto=?)");
            ps3.setLong(1, id);
            ps3.executeUpdate();

            PreparedStatement ps4 = con.prepareStatement(
                "DELETE FROM Entrada WHERE ID_Producto=?");
            ps4.setLong(1, id);
            ps4.executeUpdate();

            PreparedStatement ps5 = con.prepareStatement(
                "DELETE FROM Camisetas WHERE ID_Textil IN (SELECT ID_Textil FROM Textil WHERE ID_Producto=?)");
            ps5.setLong(1, id);
            ps5.executeUpdate();

            PreparedStatement ps6 = con.prepareStatement(
                "DELETE FROM Accesorios WHERE ID_Textil IN (SELECT ID_Textil FROM Textil WHERE ID_Producto=?)");
            ps6.setLong(1, id);
            ps6.executeUpdate();

            PreparedStatement ps7 = con.prepareStatement(
                "DELETE FROM Textil WHERE ID_Producto=?");
            ps7.setLong(1, id);
            ps7.executeUpdate();

            PreparedStatement ps8 = con.prepareStatement(
                "DELETE FROM Otros WHERE ID_Producto=?");
            ps8.setLong(1, id);
            ps8.executeUpdate();

            PreparedStatement ps9 = con.prepareStatement(
                "DELETE FROM Producto WHERE ID_Producto=?");
            ps9.setLong(1, id);
            ps9.executeUpdate();

            con.commit();
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) {}
            throw new MyException("Error al eliminar producto: " + e.getMessage());
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) {}
            conector.release();
        }
        return true;
    }

    public long getNextId() throws MyException {
        String sql = "SELECT COALESCE(MAX(ID_Producto), 0) + 1 AS next_id FROM Producto";

        MySqlConnectorVentas conector = new MySqlConnectorVentas();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conector.getConnect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("next_id");
            }
        } catch (SQLException e) {
            throw new MyException("Error al obtener siguiente ID: " + e.getMessage());
        } finally {
            conector.release();
        }
        return 1;
    }

    private void insertProducto(Connection con, Producto p) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO Producto (ID_Producto, Nombre_Producto, Precio, " +
            "Stock_Disponible, Descripcion_Producto, Tipo_IVA, Descuento) VALUES (?,?,?,?,?,?,?)");
        ps.setLong(1,   p.getId_Producto());
        ps.setString(2, p.getNombreProducto());
        ps.setDouble(3, p.getPrecio());
        ps.setInt(4,    p.getStockDisponible());
        ps.setString(5, p.getDescripcionProducto());
        ps.setString(6, p.getTipoIVA() != null ? (int)p.getTipoIVA().getPorcentaje() + "%" : "21%");
        ps.setDouble(7, p.getDescuento());
        ps.executeUpdate();
    }

    private void updateProducto(Connection con, Producto p) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "UPDATE Producto SET Nombre_Producto=?, Precio=?, Stock_Disponible=?, " +
            "Descripcion_Producto=?, Tipo_IVA=?, Descuento=? WHERE ID_Producto=?");
        ps.setString(1, p.getNombreProducto());
        ps.setDouble(2, p.getPrecio());
        ps.setInt(3,    p.getStockDisponible());
        ps.setString(4, p.getDescripcionProducto());
        ps.setString(5, p.getTipoIVA() != null ? (int)p.getTipoIVA().getPorcentaje() + "%" : "21%");
        ps.setDouble(6, p.getDescuento());
        ps.setLong(7,   p.getId_Producto());
        ps.executeUpdate();
    }

    private void insertEntrada(Connection con, Entrada e, long id) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO Entrada (ID_Entrada, ID_Producto, Zona, Asiento, " +
            "Acceso_Permitido, Accesos_Adicionales, Validez_Horas, EstadoEntrada) VALUES (?,?,?,?,?,?,?,?)");
        ps.setLong(1,   id);
        ps.setLong(2,   e.getId_Producto());
        ps.setString(3, e.getZona());
        ps.setString(4, e.getAsiento());
        ps.setString(5, e.getAccesoPermitido());
        ps.setString(6, e.getAccesosAdicionales());
        ps.setInt(7,    e.getValidezHoras());
        ps.setString(8, e.getEstadoEntrada() != null ? e.getEstadoEntrada().name() : "ACTIVA");
        ps.executeUpdate();
    }

    private void updateEntrada(Connection con, Entrada e) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "UPDATE Entrada SET Zona=?, Asiento=?, Acceso_Permitido=?, " +
            "Accesos_Adicionales=?, Validez_Horas=?, EstadoEntrada=? WHERE ID_Entrada=?");
        ps.setString(1, e.getZona());
        ps.setString(2, e.getAsiento());
        ps.setString(3, e.getAccesoPermitido());
        ps.setString(4, e.getAccesosAdicionales());
        ps.setInt(5,    e.getValidezHoras());
        ps.setString(6, e.getEstadoEntrada() != null ? e.getEstadoEntrada().name() : "ACTIVA");
        ps.setLong(7,   e.getId_Entrada());
        ps.executeUpdate();
    }

    private void insertVip(Connection con, VIP v, long id, long idEntrada) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO VIP (ID_Vip, ID_Entrada, Beneficios_Incluidos, Servicios_Adicionales, Precio_Extra) VALUES (?,?,?,?,?)");
        ps.setLong(1,   id);
        ps.setLong(2,   idEntrada);
        ps.setString(3, v.getBeneficiosIncluidos());
        ps.setString(4, v.getServiciosAdicionales());
        ps.setDouble(5, v.getPrecioExtra());
        ps.executeUpdate();
    }

    private void updateVip(Connection con, VIP v) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "UPDATE VIP SET Beneficios_Incluidos=?, Servicios_Adicionales=?, Precio_Extra=? WHERE ID_Vip=?");
        ps.setString(1, v.getBeneficiosIncluidos());
        ps.setString(2, v.getServiciosAdicionales());
        ps.setDouble(3, v.getPrecioExtra());
        ps.setLong(4,   v.getId_Vip());
        ps.executeUpdate();
    }

    private void insertEstandar(Connection con, Estandar e, long id, long idEntrada) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO Estandar (ID_Estandar, ID_Entrada, Incluye_Regalo) VALUES (?,?,?)");
        ps.setLong(1,   id);
        ps.setLong(2,   idEntrada);
        ps.setString(3, e.getIncluyeRegalo());
        ps.executeUpdate();
    }

    private void updateEstandar(Connection con, Estandar e) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "UPDATE Estandar SET Incluye_Regalo=? WHERE ID_Estandar=?");
        ps.setString(1, e.getIncluyeRegalo());
        ps.setLong(2,   e.getId_Estandar());
        ps.executeUpdate();
    }

    private void insertBeca(Connection con, Beca b, long id, long idEntrada) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO Beca (ID_Beca, ID_Entrada, Motivo_Beca, Porcentaje_Descuento, Requisitos) VALUES (?,?,?,?,?)");
        ps.setLong(1,   id);
        ps.setLong(2,   idEntrada);
        ps.setString(3, b.getMotivoBeca());
        ps.setDouble(4, b.getPorcentajeDescuento());
        ps.setString(5, b.getRequisitos());
        ps.executeUpdate();
    }

    private void updateBeca(Connection con, Beca b) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "UPDATE Beca SET Motivo_Beca=?, Porcentaje_Descuento=?, Requisitos=? WHERE ID_Beca=?");
        ps.setString(1, b.getMotivoBeca());
        ps.setDouble(2, b.getPorcentajeDescuento());
        ps.setString(3, b.getRequisitos());
        ps.setLong(4,   b.getId_Beca());
        ps.executeUpdate();
    }

    private void insertTextil(Connection con, Textil t, long id) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO Textil (ID_Textil, ID_Producto, Talla, Color, Material, Genero, Tipo_Textil) VALUES (?,?,?,?,?,?,?)");
        ps.setLong(1,   id);
        ps.setLong(2,   t.getId_Producto());
        ps.setString(3, t.getTalla());
        ps.setString(4, t.getColor());
        ps.setString(5, t.getMaterial());
        ps.setString(6, t.getGenero() != null ? t.getGenero().name() : null);
        ps.setString(7, t.getTipoTextil());
        ps.executeUpdate();
    }

    private void updateTextil(Connection con, Textil t) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "UPDATE Textil SET Talla=?, Color=?, Material=?, Genero=?, Tipo_Textil=? WHERE ID_Textil=?");
        ps.setString(1, t.getTalla());
        ps.setString(2, t.getColor());
        ps.setString(3, t.getMaterial());
        ps.setString(4, t.getGenero() != null ? t.getGenero().name() : null);
        ps.setString(5, t.getTipoTextil());
        ps.setLong(6,   t.getId_Textil());
        ps.executeUpdate();
    }

    private void insertCamiseta(Connection con, Camisetas c, long id, long idTextil) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO Camisetas (ID_Camisetas, ID_Textil, Modelo, Estampado) VALUES (?,?,?,?)");
        ps.setLong(1,   id);
        ps.setLong(2,   idTextil);
        ps.setString(3, c.getModelo());
        ps.setString(4, c.getEstampado());
        ps.executeUpdate();
    }

    private void updateCamiseta(Connection con, Camisetas c) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "UPDATE Camisetas SET Modelo=?, Estampado=? WHERE ID_Camisetas=?");
        ps.setString(1, c.getModelo());
        ps.setString(2, c.getEstampado());
        ps.setLong(3,   c.getId_Camisetas());
        ps.executeUpdate();
    }

    private void insertAccesorio(Connection con, Accesorios a, long id, long idTextil) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO Accesorios (ID_Accesorios, ID_Textil, Tipo_Accesorio) VALUES (?,?,?)");
        ps.setLong(1,   id);
        ps.setLong(2,   idTextil);
        ps.setString(3, a.getTipoAccesorio());
        ps.executeUpdate();
    }

    private void updateAccesorio(Connection con, Accesorios a) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "UPDATE Accesorios SET Tipo_Accesorio=? WHERE ID_Accesorios=?");
        ps.setString(1, a.getTipoAccesorio());
        ps.setLong(2,   a.getId_Accesorios());
        ps.executeUpdate();
    }

    private void insertOtros(Connection con, Otros o, long id) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO Otros (ID_Otros, ID_Producto, Descripcion) VALUES (?,?,?)");
        ps.setLong(1,   id);
        ps.setLong(2,   o.getId_Producto());
        ps.setString(3, o.getDescripcion());
        ps.executeUpdate();
    }

    private void updateOtros(Connection con, Otros o) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "UPDATE Otros SET Descripcion=? WHERE ID_Otros=?");
        ps.setString(1, o.getDescripcion());
        ps.setLong(2,   o.getId_Otros());
        ps.executeUpdate();
    }
}
