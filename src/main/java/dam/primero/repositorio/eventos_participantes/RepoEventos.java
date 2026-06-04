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
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }

    public Evento crearEvento(Evento evento) {
        String sql = """
            INSERT INTO evento
            (Nombre, Descripcion, Fecha_Inicio, Fecha_Fin,
             Direccion, Ciudad, Capacidad, Estado, Modalidad, Lugar)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try {
            PreparedStatement ps = conector.getConnect().prepareStatement(sql);
            ps.setString(1, evento.getNombre());
            ps.setString(2, evento.getDescripcion());
            ps.setDate(3, Date.valueOf(evento.getFechaInicio()));
            ps.setDate(4, Date.valueOf(evento.getFechaFin()));
            ps.setString(5, evento.getDireccion());
            ps.setString(6, evento.getCiudad());
            ps.setInt(7, evento.getCapacidad());
            ps.setString(8, evento.getEstado().getValorBD());
            ps.setString(9, evento.getModalidad().getValorBD());
            ps.setString(10, evento.getLugar());
            int rows = ps.executeUpdate();
            ps.close();
            if (rows != 1) throw new MyException("No se insertó el evento");
        } catch (SQLException | MyException e) {
            System.out.println("Error crearEvento: " + e.getMessage());
        }
        return evento;
    }

    public List<Evento> listarEvento() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM evento";
        try {
            PreparedStatement ps = conector.getConnect().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventos.add(mapear(rs));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error listarEvento: " + e.getMessage());
        }
        return eventos;
    }

    public Evento mostrarEvento(int id) {
        String sql = "SELECT * FROM evento WHERE id_Evento = ?";
        try {
            PreparedStatement ps = conector.getConnect().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Evento e = mapear(rs);
                rs.close();
                ps.close();
                return e;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error mostrarEvento: " + e.getMessage());
        }
        return null;
    }

    public Evento modificarEvento(Evento evento) {
        String sql = """
            UPDATE evento SET
              Nombre=?, Descripcion=?, Fecha_Inicio=?, Fecha_Fin=?,
              Direccion=?, Ciudad=?, Capacidad=?, Estado=?, Modalidad=?, Lugar=?
            WHERE id_Evento=?
            """;
        try {
            PreparedStatement ps = conector.getConnect().prepareStatement(sql);
            ps.setString(1, evento.getNombre());
            ps.setString(2, evento.getDescripcion());
            ps.setDate(3, Date.valueOf(evento.getFechaInicio()));
            ps.setDate(4, Date.valueOf(evento.getFechaFin()));
            ps.setString(5, evento.getDireccion());
            ps.setString(6, evento.getCiudad());
            ps.setInt(7, evento.getCapacidad());
            // FIX: getValorBD()
            ps.setString(8, evento.getEstado().getValorBD());
            ps.setString(9, evento.getModalidad().getValorBD());
            ps.setString(10, evento.getLugar());
            ps.setInt(11, evento.getId_Evento());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error modificarEvento: " + e.getMessage());
        }
        return evento;
    }

    private Evento mapear(ResultSet rs) throws SQLException {
        return new Evento(
                rs.getInt("id_Evento"),
                rs.getString("Nombre"),
                rs.getString("Descripcion"),
                rs.getDate("Fecha_Inicio").toLocalDate(),
                rs.getDate("Fecha_Fin") != null ? rs.getDate("Fecha_Fin").toLocalDate() : null,
                rs.getString("Direccion"),
                rs.getString("Ciudad"),
                rs.getInt("Capacidad"),
                // FIX: fromValorBD() en lugar de valueOf() → "Borrador" → BORRADOR ok
                Estado.fromValorBD(rs.getString("Estado")),
                Modalidad.fromValorBD(rs.getString("Modalidad")),
                rs.getString("Lugar")
        );
    }
}