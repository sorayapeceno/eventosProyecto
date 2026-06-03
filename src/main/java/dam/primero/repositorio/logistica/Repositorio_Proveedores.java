package dam.primero.repositorio.logistica;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.logistica.modelo.EstadoProveedor;
import dam.primero.modelos.logistica.modelo.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repositorio_Proveedores {

    private MySqlConector conexion;

    public Repositorio_Proveedores() {
        try {
            this.conexion = new MySqlConector();
        } catch (MyException errorConexion) {
            System.out.println("Error conectando con la base de datos: " + errorConexion.getMessage());
        }
    }

    public List<Proveedor> listarProveedores() throws SQLException {

        // consulta para sacar todos los proveedores
        String consultaSql = "SELECT id_proveedor, nombre, direccion, telefono, email, CIF, pais, fecha_alta, estado " +
                "FROM eventos.Proveedor";

        // lista donde voy a guardar los proveedores
        List<Proveedor> listaProveedores = new ArrayList<>();

        try (Connection conexionBaseDatos = conexion.getConnect();
             PreparedStatement sentenciaPreparada = conexionBaseDatos.prepareStatement(consultaSql);
             ResultSet resultadoConsulta = sentenciaPreparada.executeQuery()) {

            while (resultadoConsulta.next()) {

                // creo un objeto proveedor por cada fila
                Proveedor proveedor = new Proveedor();

                proveedor.setIdProveedor(resultadoConsulta.getInt("id_proveedor"));
                proveedor.setNombre(resultadoConsulta.getString("nombre"));
                proveedor.setDireccion(resultadoConsulta.getString("direccion"));
                proveedor.setTelefono(resultadoConsulta.getString("telefono"));
                proveedor.setEmail(resultadoConsulta.getString("email"));
                proveedor.setCif(resultadoConsulta.getString("CIF"));
                proveedor.setPais(resultadoConsulta.getString("pais"));

                // Si la fecha es null le pongo la actual
                Date fechaAltaBD = resultadoConsulta.getDate("fecha_alta");
                if (fechaAltaBD != null) {
                    proveedor.setFechaAlta(fechaAltaBD.toLocalDate());
                }

                // convierto el estado de texto a enum
                String estadoTexto = resultadoConsulta.getString("estado");
                if (estadoTexto != null) {
                    proveedor.setEstadoProveedor(
                            EstadoProveedor.valueOf(estadoTexto.toUpperCase())
                    );
                }

                // añado el proveedor a la lista final
                listaProveedores.add(proveedor);
            }
        }

        return listaProveedores;
    }
}