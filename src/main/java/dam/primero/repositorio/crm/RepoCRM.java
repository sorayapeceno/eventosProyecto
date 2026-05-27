package dam.primero.repositorio.crm;

import dam.primero.modelos.crm.FormularioOportunidad;
import dam.primero.modelos.crm.FormularioOrganizacion;
import dam.primero.modelos.crm.FormularioProducto;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepoCRM {
    private String url;
    private String user;
    private String clave;

    public RepoCRM() {
        cargarConfiguracion();
    }

    private void cargarConfiguracion() {
        try {
            Properties properties = new Properties();
            var stream = RepoCRM.class.getClassLoader().getResourceAsStream("db.properties");

            if (stream == null) {
                System.out.println("No se encontró db.properties");
                return;
            }

            properties.load(stream);
            this.url = properties.getProperty("url");
            this.user = properties.getProperty("user");
            this.clave = properties.getProperty("clave");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error cargando configuración CRM: " + e.getMessage());
        }
    }

    private Connection abrirConexion() throws SQLException {
        return DriverManager.getConnection(this.url, this.user, this.clave);
    }

    public void insertarOrganizacion(FormularioOrganizacion organizacion) throws SQLException {
        String sql = "INSERT INTO FormularioOrganizacion " +
                "(Id_Formulario, Nombre, Direccion, Telefono, Email, Tipo_Organizacion) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = abrirConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Id_Formulario 1 corresponde al formulario de organización definido en el script SQL.
            stmt.setInt(1, 1);
            stmt.setString(2, organizacion.getNombre());
            stmt.setString(3, organizacion.getDireccion());
            stmt.setString(4, organizacion.getTelefono());
            stmt.setString(5, organizacion.getEmail());
            stmt.setString(6, organizacion.getTipoOrganizacion());
            stmt.executeUpdate();
        }
    }

    public void insertarOportunidad(FormularioOportunidad oportunidad) throws SQLException {
        String sql = "INSERT INTO FormularioOportunidad " +
                "(Id_Formulario, Titulo, Descripcion, Fecha_Inicio, Tipos_Oportunidad) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = abrirConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Id_Formulario 2 corresponde al formulario de oportunidad definido en el script SQL.
            stmt.setInt(1, 2);
            stmt.setString(2, oportunidad.getTitulo());
            stmt.setString(3, oportunidad.getDescripcion());
            stmt.setString(4, oportunidad.getFechaInicio());
            stmt.setString(5, oportunidad.getTiposOportunidad());
            stmt.executeUpdate();
        }
    }

    public void insertarProducto(FormularioProducto producto) throws SQLException {
        String sql = "INSERT INTO FormularioProducto " +
                "(Id_Formulario, Nombre, Descripcion, Precio, Stock, Categoria) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = abrirConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Id_Formulario 3 corresponde al formulario de producto definido en el script SQL.
            stmt.setInt(1, 3);
            stmt.setString(2, producto.getNombre());
            stmt.setString(3, producto.getDescripcion());
            stmt.setDouble(4, Double.parseDouble(producto.getPrecio()));
            stmt.setInt(5, Integer.parseInt(producto.getStock()));
            stmt.setString(6, producto.getCategoria());
            stmt.executeUpdate();
        }
    }
    public List<FormularioProducto> obtenerProductosConStock() throws SQLException {
        List<FormularioProducto> productos = new ArrayList<>();
        String sql = "SELECT Nombre, Descripcion, Precio, Stock, Categoria FROM FormularioProducto WHERE Stock > 0";

        try (Connection conn = abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FormularioProducto producto = new FormularioProducto();
                producto.setNombre(rs.getString("Nombre"));
                producto.setDescripcion(rs.getString("Descripcion"));
                producto.setPrecio(String.valueOf(rs.getDouble("Precio")));
                producto.setStock(String.valueOf(rs.getInt("Stock")));
                producto.setCategoria(rs.getString("Categoria"));
                productos.add(producto);
            }
        }

        return productos;
    }

}
