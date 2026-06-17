package dam.primero.repositorio.institucionales;

import dam.primero.config.institucionales.MySqlConectorInstitucionales;
import dam.primero.exception.MyException;
import dam.primero.modelos.institucionales.modelo.Colaboracion;
import dam.primero.modelos.institucionales.modelo.HistorialOportunidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ColaboracionRepo {
    private MySqlConectorInstitucionales conector;

    //Constructor
    public ColaboracionRepo() {
        try {
            this.conector = new MySqlConectorInstitucionales();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public List<Colaboracion> listarColaboraciones() {

        List<Colaboracion> colaboraciones = new ArrayList<>();

        String query = "SELECT ID_Colaboracion, Tipo, Fecha, Firma, Dinero, Convenio, ID_Historial FROM Colaboracion;";

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                Colaboracion col = new Colaboracion();
                col.setIdColaboracion(rs.getInt("ID_Colaboracion"));
                col.setTipo(rs.getString("Tipo"));

                if (rs.getDate("Fecha") != null) {
                    col.setFecha(rs.getDate("Fecha").toLocalDate());
                }

                // Conversión de VARCHAR('SI'/'NO') a Boolean
                String firmaDb = rs.getString("Firma");
                col.setFirma(firmaDb != null && firmaDb.equalsIgnoreCase("SI"));

                col.setDinero(rs.getInt("Dinero")); // Mapeado a int conforme a tu modelo
                col.setConvenio(rs.getString("Convenio"));

                if (rs.getObject("ID_Historial") != null) {
                    HistorialOportunidad ho = new HistorialOportunidad();
                    ho.setIdHistorial(rs.getInt("ID_Historial"));
                    col.setIdHistorial(ho);
                }

                colaboraciones.add(col);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return colaboraciones; /*Metodo Listado de Colaboraciones*/
    }
}