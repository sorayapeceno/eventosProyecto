package dam.primero.repositorio.institucionales;

import dam.primero.config.institucionales.MySqlConectorInstitucionales;
import dam.primero.exception.MyException;
import dam.primero.modelos.institucionales.modelo.Administracion;
import dam.primero.modelos.institucionales.modelo.HistorialOportunidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdministracionRepo {
    private MySqlConectorInstitucionales conector;

    //Constructor
    public AdministracionRepo() {
        try {
            this.conector = new MySqlConectorInstitucionales();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public List<Administracion> listarAdministraciones() {

        List<Administracion> administraciones = new ArrayList<>();

        String query = "SELECT o.ID_Organizacion, o.Nombre, o.Direccion, o.Telefono, o.Email, o.Ciudad, o.Fecha_Registro, o.ID_Historial, " +
                "       a.id_administracion, a.Ambito, a.Presupuesto " +
                "FROM OrganizacionCRM o " +
                "INNER JOIN Administracion a ON o.ID_Organizacion = a.id_administracion;";

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                Administracion adm = new Administracion();

                // Atributos de Organizacion
                adm.setIdOrganizacion(rs.getInt("ID_Organizacion"));
                adm.setNombre(rs.getString("Nombre"));
                adm.setDireccion(rs.getString("Direccion"));
                adm.setTelefono(rs.getInt("Telefono"));
                adm.setEmail(rs.getString("Email"));
                adm.setCiudad(rs.getString("Ciudad"));

                if (rs.getDate("Fecha_Registro") != null) {
                    adm.setFechaRegistro(rs.getDate("Fecha_Registro").toLocalDate());
                }

                if (rs.getObject("ID_Historial") != null) {
                    HistorialOportunidad ho = new HistorialOportunidad();
                    ho.setIdHistorial(rs.getInt("ID_Historial"));
                    adm.setIdHistorial(ho);
                }

                // Atributos de Administracion
                adm.setIdEmpresa(rs.getInt("id_administracion"));
                adm.setAmbito(rs.getString("Ambito"));
                adm.setPresupuesto(rs.getInt("Presupuesto")); // Volcado seguro al int de tu modelo

                administraciones.add(adm);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return administraciones; /*Metodo Listado de Administraciones publicas*/
    }
}