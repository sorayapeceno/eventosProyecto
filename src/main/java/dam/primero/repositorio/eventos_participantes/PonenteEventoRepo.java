package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PonenteEventoRepo {
    private MySqlConectorEventosParticipantes conector;

    //Constructor
    public PonenteEventoRepo() {
        try {
            this.conector = new MySqlConectorEventosParticipantes();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    public void asignarPonenteEvento(int idEvento, int idPonente) {

        String sql = """
        INSERT INTO evento_ponente (id_Evento, id_Ponente)
        VALUES (?, ?)
    """;

        try (PreparedStatement ps = this.conector.getConnect().prepareStatement(sql)) {

            ps.setInt(1, idEvento);
            ps.setInt(2, idPonente);

            int filas = ps.executeUpdate();

            if (filas != 1) {
                throw new RuntimeException("Error al asignar ponente al evento");
            }

        } catch (SQLException e) {
            System.out.println("Error en asignarPonenteEvento: " + e.getMessage());
        }
    }

}
