package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PonenteRepo {
    private MySqlConector conector;

    //Constructor
    public PonenteRepo() {
        try {
            this.conector = new MySqlConector();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    //Metodos
    public List<Ponente> listarPonente() {

        List<Ponente> ponentes = new ArrayList<>();

        String query = "select * from ponente;";

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {

                int id_Ponente = rs.getInt("id_Ponente");
                String bio = rs.getString("BIO");
                String especialidad = rs.getString("Especialidad");
                String cv  = rs.getString("CV");
                NivelImparticion nivelImparticion = NivelImparticion.valueOf(rs.getString("Nivel_Imparticion"));

                Ponente ponente = new Ponente(id_Ponente,bio,especialidad,
                        cv,nivelImparticion);

                ponentes.add(ponente);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return ponentes;
    }

}
