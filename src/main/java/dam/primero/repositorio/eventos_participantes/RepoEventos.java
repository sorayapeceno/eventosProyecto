package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.MySqlConector;
import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepoEventos {
    private MySqlConectorEventosParticipantes conector;

    //Constructor
    public RepoEventos() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

        //Metodos
        public Evento  crearEvento(Evento evento) {


            String query = """
        INSERT INTO evento
        (Nombre, Descripcion, Fecha_Inicio, Fecha_Fin,
         Direccion, Ciudad, Capacidad, Estado, Modalidad, Lugar)
        VALUES (?, ?, ?,?, ?,?, ?, ?, ?,?)
        """;

            try (PreparedStatement ps = this.conector.getConnect().prepareStatement(query)) {

                ps.setString(1, evento.getNombre());
                ps.setString(2, evento.getDescripcion());

                // LocalDate -> java.sql.Date
                ps.setDate(3, java.sql.Date.valueOf(evento.getFechaInicio()));
                ps.setDate(4, java.sql.Date.valueOf(evento.getFechaFin()));

                ps.setString(5, evento.getDireccion());
                ps.setString(6, evento.getCiudad());
                ps.setInt(7, evento.getCapacidad());

                // Enum -> String
                ps.setString(8, evento.getEstado().name().toUpperCase());
                ps.setString(9, evento.getModalidad().name().toUpperCase());

                ps.setString(10, evento.getLugar());

               int numActualizado =  ps.executeUpdate();
               if(numActualizado == 1)
               {

               }
               else
               {
                   throw new MyException("Error al dar de alta un evento: "+evento);
               }



                } catch (SQLException | MyException e) {
                    System.out.println("Error al crear evento: " + e.getMessage());
                }
            return evento;

        }

    public List<Evento> listarEvento() {

        List<Evento> eventos = new ArrayList<>();

        String query = "SELECT * FROM evento";

        try (
                Connection conn = this.conector.getConnect();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                int id_Evento = rs.getInt("id_Evento");
                String nombre = rs.getString("Nombre");
                String descripcion = rs.getString("Descripcion");
                LocalDate fecha_Inicio = rs.getDate("Fecha_Inicio").toLocalDate();
                LocalDate fecha_Fin = rs.getDate("Fecha_Fin").toLocalDate();
                String direccion = rs.getString("Direccion");
                String ciudad = rs.getString("Ciudad");
                int capacidad = rs.getInt("Capacidad");

                Estado estado = Estado.valueOf(
                        rs.getString("Estado").toUpperCase()
                );

                Modalidad modalidad = Modalidad.valueOf(
                        rs.getString("Modalidad").toUpperCase()
                );

                String lugar = rs.getString("Lugar");

                Evento evento = new Evento(
                        id_Evento,
                        nombre,
                        descripcion,
                        fecha_Inicio,
                        fecha_Fin,
                        direccion,
                        ciudad,
                        capacidad,
                        estado,
                        modalidad,
                        lugar
                );

                eventos.add(evento);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return eventos;
    }

    public Evento mostrarEvento(int idEvento) {

        Evento evento = null;

        String query = "SELECT * FROM evento WHERE id_Evento = ?";

        try (
                Connection conn = this.conector.getConnect();
                PreparedStatement stmt = conn.prepareStatement(query)

        ) {

            stmt.setInt(1, idEvento);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    evento = new Evento(
                            rs.getInt("id_Evento"),
                            rs.getString("Nombre"),
                            rs.getString("Descripcion"),
                            rs.getDate("Fecha_Inicio").toLocalDate(),
                            rs.getDate("Fecha_Fin").toLocalDate(),
                            rs.getString("Direccion"),
                            rs.getString("Ciudad"),
                            rs.getInt("Capacidad"),
                            Estado.valueOf(rs.getString("Estado").toUpperCase()),
                            Modalidad.valueOf(rs.getString("Modalidad").toUpperCase()),
                            rs.getString("Lugar")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return evento;
    }
    public Evento modificarEvento(Evento evento) {

        String query = """
        UPDATE evento
        SET Nombre = ?,
            Descripcion = ?,
            Fecha_Inicio = ?,
            Fecha_Fin = ?,
            Direccion = ?,
            Ciudad = ?,
            Capacidad = ?,
            Estado = ?,
            Modalidad = ?,
            Lugar = ?
        WHERE id_Evento = ?
        """;

        try (PreparedStatement ps = this.conector.getConnect().prepareStatement(query)) {

            ps.setString(1, evento.getNombre());
            ps.setString(2, evento.getDescripcion());
            ps.setDate(3, Date.valueOf(evento.getFechaInicio()));
            ps.setDate(4, Date.valueOf(evento.getFechaFin()));
            ps.setString(5, evento.getDireccion());
            ps.setString(6, evento.getCiudad());
            ps.setInt(7, evento.getCapacidad());
            ps.setString(8, evento.getEstado().name().toUpperCase());
            ps.setString(9, evento.getModalidad().name().toUpperCase());
            ps.setString(10, evento.getLugar());

            ps.setInt(11, evento.getId_Evento());

            int numActualizado = ps.executeUpdate();

            if (numActualizado != 1) {
                throw new MyException("Error al modificar el evento: " + evento);
            }

        } catch (SQLException | MyException e) {
            System.out.println("Error al modificar evento: " + e.getMessage());
        }

        return evento;
    }

}
