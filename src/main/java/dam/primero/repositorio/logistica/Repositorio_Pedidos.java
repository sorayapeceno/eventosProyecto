package dam.primero.repositorio.logistica;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.logistica.modelo.EstadoPedido;
import dam.primero.modelos.logistica.modelo.LineaPedido;
import dam.primero.modelos.logistica.modelo.Pedido;

import java.sql.*;
import java.util.ArrayList;

public class Repositorio_Pedidos {

    private final MySqlConector conexion;

    public Repositorio_Pedidos() throws MyException {

        this.conexion = new MySqlConector();
    }

    public Pedido obtenerPedidoConLineas(int idPedido) throws SQLException, MyException {

        // consulta para sacar los datos del pedido
        String sqlPedido = """
                SELECT id_pedido, fecha_pedido, fecha_entrega_prevista, estado_pedido
                FROM eventos.Pedido
                WHERE id_pedido = ?
        """;

        // consulta para sacar las lineas del pedido
        String sqlLineas = """
                SELECT id_linea_pedido, id_mercancia, cantidad, precio_unitario, descuento_aplicado
                FROM eventos.Linea_Pedido
                WHERE id_pedido = ?
        """;

        Pedido pedido = null;

        try (Connection conexionBd = conexion.getConnect();
             PreparedStatement sentenciaPedido = conexionBd.prepareStatement(sqlPedido);
             PreparedStatement sentenciaLineas = conexionBd.prepareStatement(sqlLineas)) {

            // busco el pedido por id
            sentenciaPedido.setInt(1, idPedido);

            try (ResultSet resultadoPedido = sentenciaPedido.executeQuery()) {

                if (resultadoPedido.next()) {

                    // creo el objeto pedido con los datos de la base de datos
                    pedido = new Pedido();

                    pedido.setIdPedido(resultadoPedido.getInt("id_pedido"));
                    pedido.setFechaPedido(resultadoPedido.getDate("fecha_pedido").toLocalDate());
                    pedido.setFechaEntregaPrevista(resultadoPedido.getDate("fecha_entrega_prevista").toLocalDate());

                    // convierto el estado a enum
                    pedido.setEstadoPedido(EstadoPedido.valueOf(resultadoPedido.getString("estado_pedido")));

                    // inicializo la lista de lineas del pedido
                    pedido.setLineasPedidos(new ArrayList<>());

                } else {
                    // si no existe el pedido lanzo error
                    throw new MyException("No existe el pedido con id " + idPedido);
                }
            }

            // busco las lineas del pedido
            sentenciaLineas.setInt(1, idPedido);

            try (ResultSet resultadoLineas = sentenciaLineas.executeQuery()) {

                while (resultadoLineas.next()) {

                    // creo cada linea del pedido
                    LineaPedido linea = new LineaPedido();

                    linea.setIdLineaPedido(resultadoLineas.getInt("id_linea_pedido"));
                    linea.setCantidad(resultadoLineas.getInt("cantidad"));
                    linea.setPrecioUnitarioEnPedido(resultadoLineas.getDouble("precio_unitario"));
                    linea.setDescuentoAplicado(resultadoLineas.getDouble("descuento_aplicado"));

                    linea.calcularSubtotal();
                    pedido.getLineasPedidos().add(linea);
                }
            }
        }


        return pedido;
    }
}