package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.MySqlConector;
import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PonenciaRepo {
    private MySqlConectorEventosParticipantes conector;

    //Constructor
    public PonenciaRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public Ponencia crearPonencia(Ponencia ponencia) {

        String query = """
        INSERT INTO ponencia
        (id_Evento, Titulo, id_Tematica, Duracion, Fecha,
         Hora, Ubicacion,Nivel, Tipo, Formato)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps = this.conector.getConnect().prepareStatement(query)) {

            ps.setInt(1, ponencia.getId_Evento());
            ps.setString(2, ponencia.getTitulo());
            ps.setInt(3, ponencia.getTematica().getId_Tematica());
            ps.setInt(4, ponencia.getDuracion());

            // LocalDate -> SQL Date
            ps.setDate(5, java.sql.Date.valueOf(ponencia.getFecha()));

            // LocalDateTime -> SQL Timestamp
            ps.setString(6, ponencia.getHora().toString());
            ps.setString(7, ponencia.getUbicacion());

            // Enum -> String (IMPORTANTE: coincide con BD)
            ps.setString(8, ponencia.getNivel().name().toUpperCase());
            ps.setString(9, ponencia.getTipo().name().toUpperCase());
            ps.setString(10, ponencia.getFormato().name().toUpperCase());

            int numActualizado = ps.executeUpdate();

            if (numActualizado != 1) {
                throw new MyException("Error al crear ponencia: " + ponencia);
            }

        } catch (SQLException | MyException e) {
            System.out.println("Error al crear ponencia: " + e.getMessage());
        }

        return ponencia;
    }



    public Set<Ponencia> listarPonencias() {

        Set<Ponencia> ponencias =new HashSet<>();

        String query = "SELECT * FROM Ponencia;";

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {

                Ponencia p = new Ponencia();

                p.setId_Ponencia(rs.getInt("id_Ponencia"));
                p.setTitulo(rs.getString("Titulo"));

                p.setDuracion(rs.getInt("Duracion"));
                p.setFecha(rs.getDate("Fecha").toLocalDate());

                LocalDate fecha = rs.getDate("Fecha").toLocalDate();
                LocalTime hora = rs.getTime("Hora").toLocalTime();

                p.setHora(LocalDateTime.of(fecha, hora));

                p.setUbicacion(rs.getString("Ubicacion"));
                p.setSala(rs.getString("Sala"));

                p.setNivel(Nivel.valueOf(rs.getString("Nivel").toUpperCase()));
                p.setTipo(Tipo.valueOf(rs.getString("Tipo").toUpperCase()));
                p.setFormato(Formato.valueOf(rs.getString("Formato").toUpperCase()));

                ponencias.add(p);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return ponencias; /*Metodo listado de Ponencias*/
    }



}
