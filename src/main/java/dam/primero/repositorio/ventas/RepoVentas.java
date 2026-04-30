package dam.primero.repositorio.ventas;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.ventas.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepoVentas {
    //Atributos
    private MySqlConector conector;

    //Constructor
    public RepoVentas() {
        try {
            this.conector = new MySqlConector();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    //Metodos
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<Cliente>();
        String query = "select * from Cliente;";
        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next()){
                int idCliente = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String correo = rs.getString("correo");
                String telefono = rs.getString("telefono");

                Cliente cliente = new Cliente(idCliente, nombre, correo, telefono);
                clientes.add(cliente);
            }
        } catch (SQLException e){
            System.out.println("No hay Clientes disponibles");
        }
        return clientes;
    }

    public void darAltaCliente(String nombre, String correo, String telefono) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            String query = "INSERT INTO Cliente (nombre, correo, telefono) VALUES (?, ?, ?)";
            stmt = this.conector.getConnect().prepareStatement(query);
            stmt.setString(1, nombre);
            stmt.setString(2, correo);
            stmt.setString(3, telefono);
            stmt.executeUpdate();
        } catch (SQLException e){
            System.out.println("No se puede añadir este cliente");
        }
    }
}