package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.Evento;
import dam.primero.modelos.ventas.Cliente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepoEventos {
    private MySqlConector conector;

    //Constructor
    public RepoEventos() {
        try {
            this.conector = new MySqlConector();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }

        //Metodos
      /*  public List<Evento> listarEvento() {

            List<Evento> eventos = new ArrayList<>();

            String query = "select * from Evento";

            Statement stmt = null;
            ResultSet rs = null;

            try {

                stmt = this.conector.getConnect().createStatement();
                rs = stmt.executeQuery(query);

                while (rs.next()) {

                    int id_Evento = rs.getInt("id");
                    String Nombre = rs.getString("Nombre");
                    String Descripcion = rs.getString("Descripcion");

                    LocalDate fechaInicio = rs.getDate("Fecha_Inicio").toLocalDate();
                    LocalDate fechaFin = rs.getDate("Fecha_Fin").toLocalDate();

                    String Direccion = rs.getString("Direccion");

                    Evento evento = new Evento(id_Evento, Nombre, Descripcion, fechaInicio, fechaFin, Direccion);

                    eventos.add(evento);
                }

            } catch (SQLException e) {
                System.out.println("No hay Eventos disponibles");
            }

            return eventos;
        }*/


    }
}
