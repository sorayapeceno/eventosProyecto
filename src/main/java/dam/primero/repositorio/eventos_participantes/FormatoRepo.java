package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.Formato;
import dam.primero.modelos.eventos_participantes.Modelo.Modalidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FormatoRepo {
    private MySqlConectorEventosParticipantes conector;

    //Constructor
    public FormatoRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public Set<Formato> listarFormato() {

        Set<Formato> formatos = new HashSet<>();

        String query = "SELECT Formato FROM Ponencia;";

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {

                Formato formato = Formato.valueOf(rs.getString("Formato").toUpperCase());

                formatos.add(formato);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return formatos;
    }
}
