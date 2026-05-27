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
import java.util.ArrayList;
import java.util.List;

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



    public List<Ponencia> listarPonencias() {

        List<Ponencia> ponencias = new ArrayList<>();

        String query = "SELECT * FROM Ponencia;";

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id_Ponencia = rs.getInt("id_Ponencia");
                int id_Evento = rs.getInt("id_Evento");
                int tematica = rs.getInt("id_Tematica");
                String titulo = rs.getString("Titulo");
                int duracion = rs.getInt("Duracion");
                LocalDate fecha = rs.getDate("Fecha").toLocalDate();
                LocalDateTime hora = rs.getTimestamp("Hora").toLocalDateTime();
                String ubicacion = rs.getString("Ubicacion");
                String sala = rs.getString("Sala");
                Nivel nivel = Nivel.valueOf(rs.getString("Nivel").toUpperCase());
                Tipo tipo = Tipo.valueOf(rs.getString("Tipo").toUpperCase());
                Formato formato = Formato.valueOf(rs.getString("Formato").toUpperCase());

                Ponencia p = new Ponencia(id_Ponencia,titulo,tematica,duracion,fecha,hora,
                        ubicacion,sala,nivel,tipo,formato);

                ponencias.add(p);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return ponencias; /*Metodo listado de Ponencias*/
    }

}
