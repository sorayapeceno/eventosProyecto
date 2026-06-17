package dam.primero.repositorio.institucionales;

import dam.primero.config.institucionales.MySqlConectorInstitucionales;
import dam.primero.exception.MyException;
import dam.primero.modelos.institucionales.modelo.CentroEducativo;
import dam.primero.modelos.institucionales.modelo.HistorialOportunidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CentroEducativoRepo {
    private MySqlConectorInstitucionales conector;

    //Constructor
    public CentroEducativoRepo() {
        try {
            this.conector = new MySqlConectorInstitucionales();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public List<CentroEducativo> listarCentros() {

        List<CentroEducativo> centros = new ArrayList<>();

        String query = "SELECT o.ID_Organizacion, o.Nombre, o.Direccion, o.Telefono, o.Email, o.Ciudad, o.Fecha_Registro, o.ID_Historial, " +
                "       c.id_centro, c.Tipo_Centro, c.Num_Alumnos " +
                "FROM OrganizacionCRM o " +
                "INNER JOIN Centro_Educativo c ON o.ID_Organizacion = c.id_centro;";

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                CentroEducativo centro = new CentroEducativo();

                // Atributos de Organizacion
                centro.setIdOrganizacion(rs.getInt("ID_Organizacion"));
                centro.setNombre(rs.getString("Nombre"));
                centro.setDireccion(rs.getString("Direccion"));
                centro.setTelefono(rs.getInt("Telefono"));
                centro.setEmail(rs.getString("Email"));
                centro.setCiudad(rs.getString("Ciudad"));

                if (rs.getDate("Fecha_Registro") != null) {
                    centro.setFechaRegistro(rs.getDate("Fecha_Registro").toLocalDate());
                }

                if (rs.getObject("ID_Historial") != null) {
                    HistorialOportunidad ho = new HistorialOportunidad();
                    ho.setIdHistorial(rs.getInt("ID_Historial"));
                    centro.setIdHistorial(ho);
                }

                // Atributos de CentroEducativo
                centro.setIdCentroEducativo(rs.getInt("id_centro"));
                centro.setTipoCentro(rs.getString("Tipo_Centro"));
                centro.setNumAlumnos(rs.getInt("Num_Alumnos"));

                centros.add(centro);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return centros; /*Metodo Listado de Centros Educativos*/
    }
}