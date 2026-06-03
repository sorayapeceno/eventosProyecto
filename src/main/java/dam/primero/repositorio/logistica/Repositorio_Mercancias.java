package dam.primero.repositorio.logistica;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.logistica.modelo.Mercancia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repositorio_Mercancias {

    private final MySqlConector conexion;

    public Repositorio_Mercancias() throws MyException {

        this.conexion = new MySqlConector();
    }

    public void entradaMercancia(int idMercancia, int cantidad) throws SQLException, MyException {

        String consultaActualizarStock = """
                UPDATE eventos.Mercancia
                SET stock_actual = stock_actual + ?
                WHERE id_mercancia = ?
        """;

        try (PreparedStatement sentencia = conexion.getConnect().prepareStatement(consultaActualizarStock)) {

            // meto los valores en la consulta
            sentencia.setInt(1, cantidad);
            sentencia.setInt(2, idMercancia);

            int filasModificadas = sentencia.executeUpdate();

            // si no modifica es porque el id no existe
            if (filasModificadas == 0) {
                throw new MyException("No existe la mercancía con id: " + idMercancia);
            }
        }

        // compruebo la mercancia de nuevo
        Mercancia mercanciaActual = obtenerPorId(idMercancia);

        // si necesita reposicion se añade automaticamente
        if (mercanciaActual.necesitaReposicion()) {
            reponerStock(mercanciaActual);
        }
    }

    private void reponerStock(Mercancia mercancia) throws SQLException {

        // calculo el nuevo stock sumando el mínimo al actual
        int stockFinal = mercancia.getStockActual() + mercancia.getStockMinimo();

        String consultaReposicion = """
                UPDATE eventos.Mercancia
                SET stock_actual = ?
                WHERE id_mercancia = ?
        """;

        try (PreparedStatement sentenciaUpdate = conexion.getConnect().prepareStatement(consultaReposicion)) {


            sentenciaUpdate.setInt(1, stockFinal);
            sentenciaUpdate.setInt(2, mercancia.getIdMercancia());

            sentenciaUpdate.executeUpdate();
        }
    }

    public Mercancia obtenerPorId(int idMercancia) throws SQLException {

        // consulta para buscar una mercancía concreta por id
        String consultaBuscar = """
                SELECT id_mercancia, descripcion, categoria, precio_unitario,
                       stock_minimo, stock_actual, fecha_creacion
                FROM eventos.Mercancia
                WHERE id_mercancia = ?
        """;

        try (PreparedStatement sentenciaBuscar = conexion.getConnect().prepareStatement(consultaBuscar)) {

            sentenciaBuscar.setInt(1, idMercancia);

            try (ResultSet resultado = sentenciaBuscar.executeQuery()) {

                if (resultado.next()) {

                    // creo el objeto con los datos de la base de datos
                    return new Mercancia(
                            resultado.getInt("id_mercancia"),
                            resultado.getString("descripcion"),
                            resultado.getDouble("precio_unitario"),
                            resultado.getString("categoria"),
                            resultado.getInt("stock_minimo"),
                            resultado.getInt("stock_actual"),
                            resultado.getDate("fecha_creacion").toLocalDate()
                    );
                }
            }
        }

        // si no lo encuentra
        throw new SQLException("No se ha encontrado la mercancía con id " + idMercancia);
    }

    public List<Mercancia> listarMercancias() throws SQLException {

        // consulta para sacar todas las mercancías
        String consultaListado = """
                SELECT id_mercancia, descripcion, categoria, precio_unitario,
                       stock_minimo, stock_actual, fecha_creacion
                FROM eventos.Mercancia
        """;

        List<Mercancia> listaMercancias = new ArrayList<>();

        try (PreparedStatement sentenciaListado = conexion.getConnect().prepareStatement(consultaListado);
             ResultSet resultadoListado = sentenciaListado.executeQuery()) {

            while (resultadoListado.next()) {

                // creo un objeto por cada mercancía
                Mercancia mercancia = new Mercancia(
                        resultadoListado.getInt("id_mercancia"),
                        resultadoListado.getString("descripcion"),
                        resultadoListado.getDouble("precio_unitario"),
                        resultadoListado.getString("categoria"),
                        resultadoListado.getInt("stock_minimo"),
                        resultadoListado.getInt("stock_actual"),
                        resultadoListado.getDate("fecha_creacion").toLocalDate()
                );


                listaMercancias.add(mercancia);
            }
        }

        return listaMercancias;
    }
}