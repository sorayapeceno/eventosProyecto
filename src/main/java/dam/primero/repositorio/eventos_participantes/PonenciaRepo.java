package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.MySqlConector;
import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PonenciaRepo {
    private MySqlConectorEventosParticipantes conector;

    public PonenciaRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar el conector de base de datos", e);
        }
    }

    public List<Ponencia> listarPonencias() {
        if (this.conector == null || this.conector.getConnect() == null) {
            throw new RuntimeException("La conexión con la base de datos no está disponible.");
        }

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

                Ponencia p = new Ponencia(id_Ponencia, titulo, tematica, duracion, fecha, hora,
                        ubicacion, sala, nivel, tipo, formato);

                ponencias.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
            throw new RuntimeException("Error al leer los datos de las ponencias", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return ponencias;
    }
}