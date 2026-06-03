package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RepoEventosPonencias {
    private MySqlConectorEventosParticipantes conector;

    //Constructor
    public RepoEventosPonencias() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    public void asignarPonenciaEvento(int idEvento, int idPonencia) {

        String sql = """
        INSERT INTO evento_ponencia (id_Evento, id_Ponencia)
        VALUES (?, ?)
    """;

        try (PreparedStatement ps = this.conector.getConnect().prepareStatement(sql)) {

            ps.setInt(1, idEvento);
            ps.setInt(2, idPonencia);

            int filas = ps.executeUpdate();

            if (filas != 1) {
                throw new RuntimeException("Error al asignar ponencia al evento");
            }

        } catch (SQLException e) {
            System.out.println("Error en asignarPonenciaEvento: " + e.getMessage());
        }
    }

}
