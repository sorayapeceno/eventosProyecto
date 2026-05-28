package dam.primero.repositorio.eventos_participantes;

import dam.primero.config.eventos_participantes.MySqlConectorEventosParticipantes;
import dam.primero.exception.MyException;
import dam.primero.modelos.eventos_participantes.Modelo.Estado;
import dam.primero.modelos.eventos_participantes.Modelo.NivelImparticion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class NivelImparticionRepo {
        private MySqlConectorEventosParticipantes conector;

        //Constructor
        public NivelImparticionRepo() {
            try {
                this.conector = new MySqlConectorEventosParticipantes();
            } catch (MyException e) {
                System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            }
        }
        public Set<NivelImparticion> listarNivelImparticion() {

            Set<NivelImparticion> nivelImp = new HashSet<NivelImparticion>();

            String query = "SELECT Estado FROM Evento;";

            Statement stmt = null;
            ResultSet rs = null;

            try {

                stmt = this.conector.getConnect().createStatement();
                rs = stmt.executeQuery(query);

                while (rs.next()) {

                    NivelImparticion nivelImparticion = NivelImparticion.valueOf(rs.getString("NivelImparticion").toUpperCase());

                    nivelImp.add(nivelImparticion);
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return nivelImp;/*Metodo Listado de estados de Eventos*/
        }


}
