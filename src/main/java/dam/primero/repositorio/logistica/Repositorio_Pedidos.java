package dam.primero.repositorio.logistica;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.logistica.modelo.EstadoPedido;
import dam.primero.modelos.logistica.modelo.LineaPedido;
import dam.primero.modelos.logistica.modelo.Pedido;

import java.sql.*;
import java.util.ArrayList;

public class Repositorio_Pedidos {
    private final MySqlConector conector;

    public Repositorio_Pedidos() throws MyException {
        this.conector = new MySqlConector();
    }

    public Pedido obtenerPedidoConLineas(int idPedido) throws SQLException, MyException {
        String sqlPedido = "SELECT id_pedido, fecha_pedido, fecha_entrega_prevista, estado_pedido FROM eventos.Pedido WHERE id_pedido = ?";
        String sqlLineas = "SELECT id_linea_pedido, id_mercancia, cantidad, precio_unitario, descuento_aplicado FROM eventos.Linea_Pedido WHERE id_pedido = ?";

        Pedido pedido = null;

        try (Connection conn = conector.getConnect();
             PreparedStatement psPedido = conn.prepareStatement(sqlPedido);
             PreparedStatement psLineas = conn.prepareStatement(sqlLineas)) {

            // 1. Buscar cabecera del Pedido
            psPedido.setInt(1, idPedido);
            try (ResultSet rsPedido = psPedido.executeQuery()) {
                if (rsPedido.next()) {
                    pedido = new Pedido();
                    pedido.setIdPedido(rsPedido.getInt("id_pedido"));
                    pedido.setFechaPedido(rsPedido.getDate("fecha_pedido").toLocalDate());
                    pedido.setFechaEntregaPrevista(rsPedido.getDate("fecha_entrega_prevista").toLocalDate());
                    pedido.setEstadoPedido(EstadoPedido.valueOf(rsPedido.getString("estado_pedido")));
                    pedido.setLineasPedidos(new ArrayList<>());
                } else {
                    // Excepción si el pedido no existe (Requisito del CU)
                    throw new MyException("El pedido con ID " + idPedido + " no existe en el sistema.");
                }
            }

            // 2. Recuperar y mapear sus líneas de mercancía
            psLineas.setInt(1, idPedido);
            try (ResultSet rsLineas = psLineas.executeQuery()) {
                while (rsLineas.next()) {
                    LineaPedido linea = new LineaPedido();
                    linea.setIdLineaPedido(rsLineas.getInt("id_linea_pedido"));
                    linea.setCantidad(rsLineas.getInt("cantidad"));
                    linea.setPrecioUnitarioEnPedido(rsLineas.getDouble("precio_unitario"));
                    linea.setDescuentoAplicado(rsLineas.getDouble("descuento_aplicado"));

                    // Forzamos el cálculo interno del subtotal
                    linea.calcularSubtotal();

                    pedido.getLineasPedidos().add(linea);
                }
            }
        }
        return pedido;
    }
}