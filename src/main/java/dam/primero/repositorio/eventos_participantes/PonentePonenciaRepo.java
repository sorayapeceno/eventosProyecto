package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PonentePonenciaRepo {
    private MySqlConectorEventosParticipantes conector;

    //Constructor
    public PonentePonenciaRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    public void asignarPonentePonencia(int id_Ponente, int idPonencia) {

        String sql = """
        INSERT INTO ponente_ponencia (id_Ponente, id_Ponencia)
        VALUES (?, ?)
    """;

        try (PreparedStatement ps = this.conector.getConnect().prepareStatement(sql)) {

            ps.setInt(1, idPonencia);
            ps.setInt(2, id_Ponente);

            int filas = ps.executeUpdate();

            if (filas != 1) {
                throw new RuntimeException("Error al asignar ponencia al evento");
            }

        } catch (SQLException e) {
            System.out.println("Error en asignarPonenciaEvento: " + e.getMessage());
        }
    }

}
