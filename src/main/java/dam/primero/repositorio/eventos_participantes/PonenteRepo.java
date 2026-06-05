package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.*;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class PonenteRepo {
    private MySqlConectorEventosParticipantes conector;

    public PonenteRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }

    // ── CREAR (inserta primero en Persona, luego en Ponente) ───────────────
    public Ponente crearPonente(String nombre, String apellidos, String correo,
                                String telefono, String bio, String especialidad,
                                String cv, NivelImparticion nivel) {
        // Ponente tiene FK obligatoria a Persona → primero insertamos la Persona
        String sqlPersona = """
            INSERT INTO Persona (DNI, Username, Nombre, Ap1, Correo, Telefono, Password)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        String sqlPonente = """
            INSERT INTO Ponente (id_Persona, BIO, Especialidad, CV, Nivel_Imparticion)
            VALUES (?, ?, ?, ?, ?)
            """;
        try {
            // Generamos valores únicos para DNI y Username que son UNIQUE en BD
            String dniTemp  = "TMP" + System.currentTimeMillis();
            String userTemp = "usr" + System.currentTimeMillis();

            PreparedStatement psP = conector.getConnect()
                    .prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            psP.setString(1, dniTemp.substring(0, Math.min(dniTemp.length(), 15)));
            psP.setString(2, userTemp.substring(0, Math.min(userTemp.length(), 50)));
            psP.setString(3, nombre);
            psP.setString(4, apellidos);
            psP.setString(5, correo);
            psP.setString(6, telefono);
            psP.setString(7, "Temporal#1");  // contraseña temporal
            psP.executeUpdate();

            ResultSet keys = psP.getGeneratedKeys();
            int idPersona = keys.next() ? keys.getInt(1) : 0;
            psP.close();

            PreparedStatement psN = conector.getConnect().prepareStatement(sqlPonente);
            psN.setInt(1, idPersona);
            psN.setString(2, bio);
            psN.setString(3, especialidad);
            psN.setString(4, cv);
            psN.setString(5, nivel.name());
            psN.executeUpdate();
            psN.close();

            return new Ponente(0, bio, especialidad, cv, nivel);
        } catch (SQLException e) {
            System.out.println("Error crearPonente: " + e.getMessage());
            return null;
        }
    }

    // ── LISTAR ─────────────────────────────────────────────────────────────
    public Set<Ponente> listarPonente() {
        Set<Ponente> ponentes = new HashSet<>();
        String sql = "SELECT * FROM Ponente";
        try {
            PreparedStatement ps = conector.getConnect().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ponentes.add(new Ponente(
                        rs.getInt("id_Ponente"),
                        rs.getString("BIO"),
                        rs.getString("Especialidad"),
                        rs.getString("CV"),
                        NivelImparticion.valueOf(rs.getString("Nivel_Imparticion").toUpperCase())
                ));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error listarPonente: " + e.getMessage());
        }
        return ponentes;
    }

    // ── ASIGNAR PONENTE A PONENCIA (tabla Ponente_Ponencia) ────────────────
    public void asignarPonentePonencia(int idPonente, int idPonencia) {
        String sql = "INSERT IGNORE INTO Ponente_Ponencia (id_Ponente, id_Ponencia) VALUES (?, ?)";
        try {
            PreparedStatement ps = conector.getConnect().prepareStatement(sql);
            ps.setInt(1, idPonente);
            ps.setInt(2, idPonencia);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error asignarPonentePonencia: " + e.getMessage());
        }
    }

    // ── ASIGNAR PONENTE A EVENTO (lo asigna a todas las ponencias del evento)
    public void asignarPonenteEvento(int idPonente, int idEvento) {
        String sql = """
            INSERT IGNORE INTO Ponente_Ponencia (id_Ponente, id_Ponencia)
            SELECT ?, id_Ponencia FROM Ponencia WHERE id_Evento = ?
            """;
        try {
            PreparedStatement ps = conector.getConnect().prepareStatement(sql);
            ps.setInt(1, idPonente);
            ps.setInt(2, idEvento);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error asignarPonenteEvento: " + e.getMessage());
        }
    }
}