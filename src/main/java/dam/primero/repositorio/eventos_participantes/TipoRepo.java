package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.Formato;
import dam.primero.modelos.eventos_participantes.Modelo.Tipo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TipoRepo {
    private MySqlConectorEventosParticipantes conector;

    //Constructor
    public TipoRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public Set<Tipo> listarTipo() {

        Set<Tipo> tipos = new HashSet<>();

        String query = "SELECT Tipo FROM Ponencia;";

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {

                Tipo tipo = Tipo.valueOf(rs.getString("Tipo").toUpperCase());

                tipos.add(tipo);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return tipos;
    }
}

