package dam.primero.repositorio.institucionales;

import dam.primero.config.institucionales.MySqlConectorInstitucionales;
import dam.primero.exception.MyException;
import dam.primero.modelos.institucionales.modelo.Asociacion;
import dam.primero.modelos.institucionales.modelo.HistorialOportunidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AsociacionRepo {
    private MySqlConectorInstitucionales conector;

    //Constructor
    public AsociacionRepo() {
        try {
            this.conector = new MySqlConectorInstitucionales();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public List<Asociacion> listarAsociaciones() {

        List<Asociacion> asociaciones = new ArrayList<>();

        String query = "SELECT o.ID_Organizacion, o.Nombre, o.Direccion, o.Telefono, o.Email, o.Ciudad, o.Fecha_Registro, o.ID_Historial, " +
                "       a.id_asociacion, a.Finalidad, a.Num_Socios " +
                "FROM OrganizacionCRM o " +
                "INNER JOIN Asociacion a ON o.ID_Organizacion = a.id_asociacion;";

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                Asociacion asoc = new Asociacion();

                // Atributos de Organizacion
                asoc.setIdOrganizacion(rs.getInt("ID_Organizacion"));
                asoc.setNombre(rs.getString("Nombre"));
                asoc.setDireccion(rs.getString("Direccion"));
                asoc.setTelefono(rs.getInt("Telefono"));
                asoc.setEmail(rs.getString("Email"));
                asoc.setCiudad(rs.getString("Ciudad"));

                if (rs.getDate("Fecha_Registro") != null) {
                    asoc.setFechaRegistro(rs.getDate("Fecha_Registro").toLocalDate());
                }

                if (rs.getObject("ID_Historial") != null) {
                    HistorialOportunidad ho = new HistorialOportunidad();
                    ho.setIdHistorial(rs.getInt("ID_Historial"));
                    asoc.setIdHistorial(ho);
                }

                // Atributos de Asociacion
                asoc.setIdAsociacion(rs.getInt("id_asociacion"));
                asoc.setFinalidad(rs.getString("Finalidad"));
                asoc.setNumSocios(rs.getInt("Num_Socios"));

                asociaciones.add(asoc);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return asociaciones; /*Metodo Listado de Asociaciones*/
    }
}