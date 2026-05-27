package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.Estado;
import dam.primero.modelos.eventos_participantes.Modelo.Modalidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ModalidadRepo {
    private MySqlConectorEventosParticipantes conector;

    //Constructor
    public ModalidadRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    public List<Modalidad> listarModalidad() {

        List<Modalidad> modalidades = new ArrayList<>();

        String query = "SELECT Modalidad FROM Evento;";

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {

                Modalidad modalidad = Modalidad.valueOf(rs.getString("Modalidad").toUpperCase());

                modalidades.add(modalidad);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return modalidades;/*Metodo Listado de estados de Eventos*/
    }
}
