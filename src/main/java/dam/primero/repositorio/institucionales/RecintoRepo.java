package dam.primero.repositorio.institucionales;

import dam.primero.config.institucionales.MySqlConectorInstitucionales;
import dam.primero.exception.MyException;
import dam.primero.modelos.institucionales.modelo.Recinto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecintoRepo {
    private MySqlConectorInstitucionales conector;

    //Constructor
    public RecintoRepo() {
        try {
            this.conector = new MySqlConectorInstitucionales();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public List<Recinto> listarRecintos() {

        List<Recinto> recintos = new ArrayList<>();

        String query = "SELECT ID_Recinto, Nombre, Capacidad, Ubicacion FROM Recinto;";

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                Recinto recinto = new Recinto();

                recinto.setIdRecinto(rs.getInt("ID_Recinto"));
                recinto.setNombre(rs.getString("Nombre"));
                recinto.setCapacidad(rs.getInt("Capacidad"));
                recinto.setUbicacion(rs.getString("Ubicacion"));

                recintos.add(recinto);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return recintos; /*Metodo Listado de Recintos*/
    }
}