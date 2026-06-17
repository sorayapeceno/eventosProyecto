package dam.primero.repositorio.institucionales;

import dam.primero.config.institucionales.MySqlConectorInstitucionales;
import dam.primero.exception.MyException;
import dam.primero.modelos.institucionales.modelo.Ayuntamiento;
import dam.primero.modelos.institucionales.modelo.HistorialOportunidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AyuntamientoRepo {
    private MySqlConectorInstitucionales conector;

    //Constructor
    public AyuntamientoRepo() {
        try {
            this.conector = new MySqlConectorInstitucionales();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public List<Ayuntamiento> listarAyuntamientos() {

        List<Ayuntamiento> ayuntamientos = new ArrayList<>();

        String query = "SELECT o.ID_Organizacion, o.Nombre, o.Direccion, o.Telefono, o.Email, o.Ciudad, o.Fecha_Registro, o.ID_Historial, " +
                "       a.id_ayuntamiento, a.Provincia, a.Alcalde " +
                "FROM OrganizacionCRM o " +
                "INNER JOIN Ayuntamiento a ON o.ID_Organizacion = a.id_ayuntamiento;";

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                Ayuntamiento ayu = new Ayuntamiento();

                // Atributos de Organizacion
                ayu.setIdOrganizacion(rs.getInt("ID_Organizacion"));
                ayu.setNombre(rs.getString("Nombre"));
                ayu.setDireccion(rs.getString("Direccion"));
                ayu.setTelefono(rs.getInt("Telefono"));
                ayu.setEmail(rs.getString("Email"));
                ayu.setCiudad(rs.getString("Ciudad"));

                if (rs.getDate("Fecha_Registro") != null) {
                    ayu.setFechaRegistro(rs.getDate("Fecha_Registro").toLocalDate());
                }

                if (rs.getObject("ID_Historial") != null) {
                    HistorialOportunidad ho = new HistorialOportunidad();
                    ho.setIdHistorial(rs.getInt("ID_Historial"));
                    ayu.setIdHistorial(ho);
                }

                // Atributos de Ayuntamiento
                ayu.setIdAyuntamiento(rs.getInt("id_ayuntamiento"));
                ayu.setProvincia(rs.getString("Provincia"));
                ayu.setAlcalde(rs.getString("Alcalde"));

                ayuntamientos.add(ayu);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return ayuntamientos; /*Metodo Listado de Ayuntamientos*/
    }
}