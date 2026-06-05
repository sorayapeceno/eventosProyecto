package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class PonenciaRepo {
    private MySqlConectorEventosParticipantes conector;

    public PonenciaRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }

    // ── CREAR ──────────────────────────────────────────────────────────────
    public Ponencia crearPonencia(Ponencia p) {
        String sql = """
            INSERT INTO ponencia
            (id_Evento, Titulo, id_Tematica, Duracion, Fecha, Hora, Ubicacion, Nivel, Tipo, Formato)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try {
            PreparedStatement ps = conector.getConnect().prepareStatement(sql);
            ps.setInt(1, p.getId_Evento());
            ps.setString(2, p.getTitulo());
            ps.setInt(3, p.getTematica().getId_Tematica());
            ps.setInt(4, p.getDuracion());
            ps.setDate(5, Date.valueOf(p.getFecha()));
            // Hora se guarda como VARCHAR HH:mm:ss
            ps.setString(6, p.getHora().toLocalTime().toString());
            ps.setString(7, p.getUbicacion());
            ps.setString(8, p.getNivel().name());
            ps.setString(9, p.getTipo().name());
            ps.setString(10, p.getFormato().name());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error crearPonencia: " + e.getMessage());
        }
        return p;
    }

    // ── LISTAR ─────────────────────────────────────────────────────────────
    public Set<Ponencia> listarPonencias() {
        Set<Ponencia> ponencias = new HashSet<>();
        String sql = "SELECT * FROM Ponencia";
        try {
            PreparedStatement ps = conector.getConnect().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int       id_Ponencia = rs.getInt("id_Ponencia");
                int       id_Evento   = rs.getInt("id_Evento");
                int       idTematica  = rs.getInt("id_Tematica");
                String    titulo      = rs.getString("Titulo");
                int       duracion    = rs.getInt("Duracion");
                LocalDate fecha       = rs.getDate("Fecha").toLocalDate();
                // Hora guardada como VARCHAR "HH:mm:ss"
                String horaStr        = rs.getString("Hora");
                LocalDateTime horaLDT = fecha.atTime(LocalTime.parse(horaStr));
                String    ubicacion   = rs.getString("Ubicacion");
                Nivel     nivel       = Nivel.valueOf(rs.getString("Nivel").toUpperCase());
                Tipo      tipo        = Tipo.valueOf(rs.getString("Tipo").toUpperCase());
                Formato   formato     = Formato.valueOf(rs.getString("Formato").toUpperCase());

                // FIX: usar el constructor correcto (no el vacío que no asigna nada)
                Ponencia p = new Ponencia(id_Ponencia, titulo,
                        new Tematica(idTematica, ""),
                        duracion, fecha, horaLDT, ubicacion, nivel, tipo, formato);
                p.setId_Evento(id_Evento);
                ponencias.add(p);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error listarPonencias: " + e.getMessage());
        }
        return ponencias;
    }

    // ── ASIGNAR PONENCIA A EVENTO (cambia el id_Evento de la ponencia) ─────
    public void asignarPonenciaEvento(int idPonencia, int idEvento) {
        String sql = "UPDATE Ponencia SET id_Evento = ? WHERE id_Ponencia = ?";
        try {
            PreparedStatement ps = conector.getConnect().prepareStatement(sql);
            ps.setInt(1, idEvento);
            ps.setInt(2, idPonencia);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error asignarPonenciaEvento: " + e.getMessage());
        }
    }
}