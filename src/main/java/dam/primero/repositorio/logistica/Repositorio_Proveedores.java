package dam.primero.repositorio.logistica;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.logistica.modelo.EstadoProveedor;
import dam.primero.modelos.logistica.modelo.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repositorio_Proveedores {
    private MySqlConector conector;

    public Repositorio_Proveedores() {
        try {
            this.conector = new MySqlConector();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public List<Proveedor> listarProveedores() throws SQLException {
        String consultaSql = "SELECT id_proveedor, nombre, direccion, telefono, email, CIF, pais, fecha_alta, estado FROM eventos.Proveedor";
        List<Proveedor> listaProveedores = new ArrayList<>();

        try (Connection conexion = conector.getConnect();
             PreparedStatement sentencia = conexion.prepareStatement(consultaSql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                Proveedor unProveedor = new Proveedor();

                unProveedor.setIdProveedor(resultado.getInt("id_proveedor"));
                unProveedor.setNombre(resultado.getString("nombre"));
                unProveedor.setDireccion(resultado.getString("direccion"));
                unProveedor.setTelefono(resultado.getString("telefono"));
                unProveedor.setEmail(resultado.getString("email"));
                unProveedor.setCif(resultado.getString("CIF"));
                unProveedor.setPais(resultado.getString("pais"));

                // Conversión de la fecha de la base de datos a LocalDate
                Date fechaAltaDb = resultado.getDate("fecha_alta");
                if (fechaAltaDb != null) {
                    unProveedor.setFechaAlta(fechaAltaDb.toLocalDate());
                }

                // Mapeo de la cadena de texto al Enum de Java
                String estadoTexto = resultado.getString("estado");
                if (estadoTexto != null) {
                    unProveedor.setEstadoProveedor(EstadoProveedor.valueOf(estadoTexto.toUpperCase()));
                }

                listaProveedores.add(unProveedor);
            }
        }
        return listaProveedores;
    }
}