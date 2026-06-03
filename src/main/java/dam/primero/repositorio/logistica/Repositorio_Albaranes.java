package dam.primero.repositorio.logistica;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.logistica.modelo.Albaran;
import dam.primero.modelos.logistica.modelo.EstadoPedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repositorio_Albaranes {

    private final MySqlConector conexionBaseDatos;

    public Repositorio_Albaranes() throws MyException {
        this.conexionBaseDatos = new MySqlConector();
    }

    public List<Albaran> listarAlbaranes() throws SQLException {

        String consultaSql = """
                SELECT id_albaran, fecha_albaran, estado,
                       id_pedido, numero_factura, transportista, fecha_recepcion
                FROM eventos.Albaran
        """;

        // aquí voy a ir guardando todos los albaranes que saque
        List<Albaran> listaAlbaranes = new ArrayList<>();

        try (
                PreparedStatement sentenciaPreparada =
                        conexionBaseDatos.getConnect().prepareStatement(consultaSql);

                ResultSet resultadoConsulta =
                        sentenciaPreparada.executeQuery()
        ) {

            while (resultadoConsulta.next()) {

                // por cada fila creo un objeto tipo Albaran
                Albaran albaran = new Albaran();

                // Introduzco los datos al objeto
                albaran.setIdAlbaran(resultadoConsulta.getInt("id_albaran"));

                albaran.setFechaAlbaran(resultadoConsulta.getDate("fecha_albaran").toLocalDate());

                // convierto el texto a enum
                albaran.setEstadopedido(EstadoPedido.valueOf(resultadoConsulta.getString("estado")));

                albaran.setNumeroFactura(resultadoConsulta.getString("numero_factura"));

                albaran.setTransportista(resultadoConsulta.getString("transportista"));

                //Si la fecha es null le pongo la actual
                Date fechaRecepcion = resultadoConsulta.getDate("fecha_recepcion");

                if (fechaRecepcion != null) {
                    albaran.setFechaRecepcion(fechaRecepcion.toLocalDate());
                }


                listaAlbaranes.add(albaran);
            }
        }


        return listaAlbaranes;
    }
}