package dam.primero.repositorio.logistica;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.logistica.modelo.Mercancia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repositorio_Mercancias {

    private final MySqlConector conector;

    public Repositorio_Mercancias() throws MyException {
        this.conector = new MySqlConector();
    }

    public void registrarEntradaMercancia(int idMercancia, int cantidad) throws SQLException, MyException {

        // 1- Aumenta el stock
        String consultaSql = """
                UPDATE eventos.Mercancia
                SET stock_actual = stock_actual + ?
                WHERE id_mercancia = ?
        """;

        try (PreparedStatement sentencia = conector.getConnect().prepareStatement(consultaSql)) {

            sentencia.setInt(1, cantidad);
            sentencia.setInt(2, idMercancia);

            int filasAfectadas = sentencia.executeUpdate();

            // Si no se ha modificado ninguna fila, es porque el ID no existe en la BD
            if (filasAfectadas == 0) {
                throw new MyException("No existe ninguna mercancía registrada con el ID: " + idMercancia);
            }
        }

        // 2. Obtiene la mercancía actualizada
        Mercancia mercancíaModificada = obtenerPorId(idMercancia);

        // 3. Comprueba si necesita Stock
        if (mercancíaModificada.necesitaReposicion()) {
            reponerStock(mercancíaModificada);
        }
    }

    //El stock se repone automáticamente cuando baja de su mínimo.
    // stockActual = stockActual + stockMinimo
    private void reponerStock(Mercancia m) throws SQLException {

        int nuevoStock = m.getStockActual() + m.getStockMinimo();

        String sql = """
                UPDATE eventos.Mercancia
                SET stock_actual = ?
                WHERE id_mercancia = ?
        """;

        try (PreparedStatement ps = conector.getConnect().prepareStatement(sql)) {

            ps.setInt(1, nuevoStock);
            ps.setInt(2, m.getIdMercancia());

            ps.executeUpdate();
        }
    }


    //Obtiene por ID
    public Mercancia obtenerPorId(int idMercancia) throws SQLException {

        String sql = """
                SELECT id_mercancia, descripcion, categoria, precio_unitario,
                       stock_minimo, stock_actual, fecha_creacion
                FROM eventos.Mercancia
                WHERE id_mercancia = ?
        """;

        try (PreparedStatement ps = conector.getConnect().prepareStatement(sql)) {

            ps.setInt(1, idMercancia);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Mercancia(
                            rs.getInt("id_mercancia"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio_unitario"),
                            rs.getString("categoria"),
                            rs.getInt("stock_minimo"),
                            rs.getInt("stock_actual"),
                            rs.getDate("fecha_creacion").toLocalDate()
                    );
                }
            }
        }

        throw new SQLException("Mercancía no encontrada con id " + idMercancia);
    }

    //Listar Mercancia
    public List<Mercancia> listarMercancias() throws SQLException {

        String sql = """
                SELECT id_mercancia, descripcion, categoria, precio_unitario,
                       stock_minimo, stock_actual, fecha_creacion
                FROM eventos.Mercancia
        """;

        List<Mercancia> lista = new ArrayList<>();

        try (PreparedStatement ps = conector.getConnect().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Mercancia m = new Mercancia(
                        rs.getInt("id_mercancia"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_unitario"),
                        rs.getString("categoria"),
                        rs.getInt("stock_minimo"),
                        rs.getInt("stock_actual"),
                        rs.getDate("fecha_creacion").toLocalDate()
                );

                lista.add(m);
            }
        }

        System.out.println("Mercancías cargadas: " + lista.size());
        return lista;
    }
}