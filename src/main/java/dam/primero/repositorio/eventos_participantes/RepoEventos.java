package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepoEventos {
    private MySqlConectorEventosParticipantes conector;

    public RepoEventos() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public Evento crearEvento(Evento evento) {
        String query = """
        INSERT INTO evento
        (Nombre, Descripcion, Fecha_Inicio, Fecha_Fin,
         Direccion, Ciudad, Capacidad, Estado, Modalidad, Lugar)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = this.conector.getConnect().prepareStatement(query)) {
            ps.setString(1, evento.getNombre());
            ps.setString(2, evento.getDescripcion());
            ps.setDate(3, java.sql.Date.valueOf(evento.getFechaInicio()));
            ps.setDate(4, java.sql.Date.valueOf(evento.getFechaFin()));
            ps.setString(5, evento.getDireccion());
            ps.setString(6, evento.getCiudad());
            ps.setInt(7, evento.getCapacidad());
            ps.setString(8, evento.getEstado().name().toUpperCase());
            ps.setString(9, evento.getModalidad().name().toUpperCase());
            ps.setString(10, evento.getLugar());

            int numActualizado = ps.executeUpdate();
            if (numActualizado != 1) {
                throw new MyException("Error al dar de alta un evento: " + evento);
            }
        } catch (SQLException | MyException e) {
            System.out.println("Error al crear evento: " + e.getMessage());
        }
        return evento;
    }

    public List<Evento> listarEvento() {
        List<Evento> eventos = new ArrayList<>();
        String query = "SELECT * FROM evento";

        // FIX: No envolver la conexión en try-with-resources, solo el Statement y ResultSet
        try {
            PreparedStatement stmt = this.conector.getConnect().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Evento evento = new Evento(
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
                eventos.add(evento);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return eventos;
    }

    public Evento mostrarEvento(int idEvento) {
        Evento evento = null;
        String query = "SELECT * FROM evento WHERE id_Evento = ?";

        // FIX: No envolver la conexión en try-with-resources
        try {
            PreparedStatement stmt = this.conector.getConnect().prepareStatement(query);
            stmt.setInt(1, idEvento);
            ResultSet rs = stmt.executeQuery();

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
            rs.close();
            stmt.close();
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