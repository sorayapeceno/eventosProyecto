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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PonenteRepo {
    private MySqlConectorEventosParticipantes conector;

    //Constructor
    public PonenteRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    //Metodos

    public Ponente crearPonente(Ponente ponente){
       {
           String query = """
    INSERT INTO Ponente
    (id_Persona, BIO, Especialidad, CV, Nivel_Imparticion)
    VALUES (?, ?, ?, ?, ?)
    """;

            try (PreparedStatement ps = this.conector.getConnect().prepareStatement(query)) {

                ps.setInt(1, ponente.getIdPonente());
                ps.setString(2, ponente.getBIO());
                ps.setString(3, ponente.getEspecialidad());
                ps.setString(4, ponente.getCV());

                // Enum -> String
                ps.setString(5, ponente.getNivelImparticion().name().toUpperCase());


                int numActualizado =  ps.executeUpdate();
                if(numActualizado == 1)
                {

                }
                else
                {
                    throw new MyException("Error al dar de alta un  e vento: " + ponente);
                }

            } catch (SQLException | MyException e) {
                System.out.println("Error al crear evento: " + e.getMessage());
            }
            return ponente;

        }
    }


    public Set<Ponente> listarPonente() {

        Set<Ponente> ponentes = new HashSet<>() {
        };

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

        return ponentes; /*Metodo listado de Ponentes*/
    }

}
