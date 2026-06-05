package dam.primero.repositorio.crm;

import dam.primero.config.crm.MySqlConectorCRM;
import dam.primero.exception.MyException;
import dam.primero.modelos.crm.FormularioOportunidad;
import dam.primero.modelos.crm.FormularioOrganizacion;
import dam.primero.modelos.crm.FormularioProducto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepoCRM {

    private MySqlConectorCRM conector;

    public RepoCRM() {
        try {
            this.conector = new MySqlConectorCRM();
        } catch (MyException e) {
            System.out.println("Error al conectar CRM: " + e.getMessage());
        }
    }

    public void insertarOrganizacion(FormularioOrganizacion organizacion) throws SQLException {
        String sql = "INSERT INTO FormularioOrganizacion " +
                "(Id_Formulario, Nombre, Direccion, Telefono, Email, Tipo_Organizacion) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = this.conector.getConnect().prepareStatement(sql);
        stmt.setInt(1, 1);
        stmt.setString(2, organizacion.getNombre());
        stmt.setString(3, organizacion.getDireccion());
        stmt.setString(4, organizacion.getTelefono());
        stmt.setString(5, organizacion.getEmail());
        stmt.setString(6, organizacion.getTipoOrganizacion());
        stmt.executeUpdate();
        stmt.close();
    }

    public void insertarOportunidad(FormularioOportunidad oportunidad) throws SQLException {
        String sql = "INSERT INTO FormularioOportunidad " +
                "(Id_Formulario, Titulo, Descripcion, Fecha_Inicio, Tipos_Oportunidad) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmt = this.conector.getConnect().prepareStatement(sql);
        stmt.setInt(1, 2);
        stmt.setString(2, oportunidad.getTitulo());
        stmt.setString(3, oportunidad.getDescripcion());
        stmt.setString(4, oportunidad.getFechaInicio());
        stmt.setString(5, oportunidad.getTiposOportunidad());
        stmt.executeUpdate();
        stmt.close();
    }

    public void insertarProducto(FormularioProducto producto) throws SQLException {
        String sql = "INSERT INTO FormularioProducto " +
                "(Id_Formulario, Nombre, Descripcion, Precio, Stock, Categoria) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = this.conector.getConnect().prepareStatement(sql);
        stmt.setInt(1, 3);
        stmt.setString(2, producto.getNombre());
        stmt.setString(3, producto.getDescripcion());
        stmt.setDouble(4, Double.parseDouble(producto.getPrecio()));
        stmt.setInt(5, Integer.parseInt(producto.getStock()));
        stmt.setString(6, producto.getCategoria());
        stmt.executeUpdate();
        stmt.close();
    }

    public List<FormularioProducto> obtenerProductosConStock() throws SQLException {
        List<FormularioProducto> productos = new ArrayList<>();
        String sql = "SELECT Nombre, Descripcion, Precio, Stock, Categoria FROM FormularioProducto WHERE Stock > 0";

        PreparedStatement stmt = this.conector.getConnect().prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            FormularioProducto producto = new FormularioProducto();
            producto.setNombre(rs.getString("Nombre"));
            producto.setDescripcion(rs.getString("Descripcion"));
            producto.setPrecio(String.valueOf(rs.getDouble("Precio")));
            producto.setStock(String.valueOf(rs.getInt("Stock")));
            producto.setCategoria(rs.getString("Categoria"));
            productos.add(producto);
        }

        rs.close();
        stmt.close();
        return productos;
    }
}
