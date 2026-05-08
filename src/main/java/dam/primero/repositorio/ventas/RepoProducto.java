package dam.primero.repositorio.ventas;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;

import java.sql.*;
import dam.primero.modelos.ventas.*;

public class RepoProducto {
    //Atributos
    private MySqlConector conector;

    //Constructor
    public RepoProducto() {
        try {
            this.conector = new MySqlConector();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    //Metodos
    public void añadirProducto(Producto producto) {
        try {
            Connection con = this.conector.getConnect();

            /*
             * INSERT PRODUCTO
             */
            String queryProducto = "INSERT INTO producto " + "(Nombre_Producto, Precio, Stock_Disponible, Descripcion_Producto, tipo_IVA, Descuento) " + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtProducto = con.prepareStatement(queryProducto, Statement.RETURN_GENERATED_KEYS);

            stmtProducto.setString(1, producto.getNombreProducto());
            stmtProducto.setDouble(2, producto.getPrecio());
            stmtProducto.setInt(3, producto.getStockDisponible());
            stmtProducto.setString(4, producto.getDescripcion());
            stmtProducto.setString(5, producto.getTipoIva().name());
            stmtProducto.setInt(6, producto.getDescuento());

            stmtProducto.executeUpdate();
            ResultSet rsProducto = stmtProducto.getGeneratedKeys();
            if (!rsProducto.next()) {
                throw new SQLException("No se ha generado ID de producto");
            }
            int idProducto = rsProducto.getInt(1);

            /*
             * SOLO SI ES ENTRADA
             */
            if (producto instanceof Entrada entrada) {
                String queryEntrada = "INSERT INTO entrada " + "(ID_Producto, Zona, Asiento, Acceso_Permitido, Acceso_Adicional, Validez_Horas, EstadoEntradas) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmtEntrada = con.prepareStatement(queryEntrada, Statement.RETURN_GENERATED_KEYS);
                stmtEntrada.setInt(1, idProducto);
                stmtEntrada.setString(2, entrada.getZona());
                stmtEntrada.setString(3, entrada.getAsiento());
                stmtEntrada.setBoolean(4, entrada.isAccesoPermitido());
                stmtEntrada.setInt(5, entrada.getAccesoAdicional());
                stmtEntrada.setInt(6, entrada.getValidezHoras());
                stmtEntrada.setString(7, entrada.getEstadoEntradas().name());

                stmtEntrada.executeUpdate();
                ResultSet rsEntrada = stmtEntrada.getGeneratedKeys();
                if (!rsEntrada.next()) {
                    throw new SQLException("No se ha generado ID de entrada");
                }
                int idEntrada = rsEntrada.getInt(1);

                /*
                 * SOLO SI ES VIP
                 */
                if (producto instanceof Vip vip) {
                    String queryVip = "INSERT INTO vip " + "(ID_Entrada, Beneficios_Incluidos, Servicios_dicionales, Precio_Extra) " + "VALUES (?, ?, ?, ?)";
                    PreparedStatement stmtVip = con.prepareStatement(queryVip);
                    stmtVip.setInt(1, idEntrada);
                    stmtVip.setString(2, String.join(",", vip.getBeneficiosIncluidos()));
                    stmtVip.setString(3, String.join(",", vip.getServiciosAdicionales()));
                    stmtVip.setDouble(4, vip.getPrecioExtra());
                    stmtVip.executeUpdate();
                }

                /*
                 * SOLO SI ES ESTANDAR
                 */
                else if (producto instanceof Estandar estandar) {
                    String queryEstandar = "INSERT INTO vip " + "(ID_Entrada, Incluye_Regalo) + VALUES (?, ?)";
                    PreparedStatement stmtEstandar = con.prepareStatement(queryEstandar);
                    stmtEstandar.setInt(1, idEntrada);
                    stmtEstandar.setBoolean(2, estandar.isIncluyeRegalo());
                    stmtEstandar.executeUpdate();
                }

                /*
                 * SOLO SI ES BECA
                 */
                else if (producto instanceof Beca beca) {
                    String queryBeca = "INSERT INTO beca" + "(ID_Entrada, Motivo_Beca, Porcentaje_Descuento, Requisitos)" +  "VALUES (?, ?, ?, ?)";
                    PreparedStatement stmtBeca = con.prepareStatement(queryBeca);
                    stmtBeca.setInt(1, idEntrada);
                    stmtBeca.setString(2, beca.getMotivoBeca());
                    stmtBeca.setInt(3, beca.getPorcentajeBeca());
                    stmtBeca.setString(4, String.join(",", beca.getRequisitos()));
                    stmtBeca.executeUpdate();
                }

            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}