package dam.primero.repositorio.logistica;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.logistica.modelo.Albaran;
import dam.primero.modelos.logistica.modelo.EstadoPedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repositorio_Albaranes {

    private final MySqlConector conector;

    public Repositorio_Albaranes() throws MyException {
        this.conector = new MySqlConector();
    }

    public List<Albaran> listarAlbaranes() throws SQLException {

        String sql = """
                SELECT id_albaran, fecha_albaran, estado,
                       id_pedido, numero_factura, transportista, fecha_recepcion
                FROM eventos.Albaran
        """;

        List<Albaran> lista = new ArrayList<>();

        try (PreparedStatement ps = conector.getConnect().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Albaran a = new Albaran();

                a.setIdAlbaran(rs.getInt("id_albaran"));
                a.setFechaAlbaran(rs.getDate("fecha_albaran").toLocalDate());

                a.setEstadopedido(
                        EstadoPedido.valueOf(rs.getString("estado"))
                );

                a.setNumeroFactura(rs.getString("numero_factura"));
                a.setTransportista(rs.getString("transportista"));

                Date fechaRecepcion = rs.getDate("fecha_recepcion");
                if (fechaRecepcion != null) {
                    a.setFechaRecepcion(fechaRecepcion.toLocalDate());
                }

                lista.add(a);
            }
        }

        return lista;
    }
}