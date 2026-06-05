package dam.primero.repositorio.institucionales;

import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.Estado;
import dam.primero.modelos.institucionales.modelo.Recinto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class recintoRepo {
    private MySqlConectorEventosParticipantes conector;

    //Constructor
    public recintoRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    public Set<Recinto> listarEstados() {
        Set<Recinto> recintos = new HashSet<>();

        String query = "SELECT ID_Recinto, Nombre, Capacidad, Ubicacion FROM Recinto;";

        Statement stmt = null;
        ResultSet rs = null;

        try {
            if (this.conector != null && this.conector.getConnect() != null) {
                stmt = this.conector.getConnect().createStatement();
                rs = stmt.executeQuery(query);

                while (rs.next()) {
                    Recinto recinto = new Recinto();

                    // Mapeo de columnas de la BBDD al objeto Java
                    recinto.setIdRecinto(rs.getInt("ID_Recinto"));
                    recinto.setNombre(rs.getString("Nombre"));
                    recinto.setCapacidad(rs.getInt("Capacidad"));
                    recinto.setUbicacion(rs.getString("Ubicacion"));

                    recintos.add(recinto);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en listarRecintos: " + e.getMessage());
        } finally {
            // Buenas prácticas: Cerrar recursos para evitar fugas de memoria
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return recintos;
    }
}
