package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.MySqlConector;
import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.Estado;
import dam.primero.modelos.eventos_participantes.Modelo.Evento;
import dam.primero.modelos.eventos_participantes.Modelo.Modalidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EstadoRepo {
    private MySqlConectorEventosParticipantes conector;

    //Constructor
    public EstadoRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    public List<Estado> listarEstados() {

        List<Estado> estados = new ArrayList<>();

        String query = "SELECT Estado FROM Evento;";

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {

                Estado estado = Estado.valueOf(rs.getString("Estado").toUpperCase());

                estados.add(estado);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return estados;/*Metodo Listado de estados de Eventos*/
    }
}
