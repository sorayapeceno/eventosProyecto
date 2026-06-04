package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.Tematica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TematicaRepo {
    private MySqlConectorEventosParticipantes conector;

    public TematicaRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }

    public List<Tematica> listarTematicas() {
        List<Tematica> tematicas = new ArrayList<>();
        String sql = "SELECT * FROM Tematica";
        try {
            PreparedStatement ps = conector.getConnect().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tematicas.add(new Tematica(rs.getInt("id_Tematica"), rs.getString("Tema")));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error listarTematicas: " + e.getMessage());
        }
        return tematicas;
    }
}