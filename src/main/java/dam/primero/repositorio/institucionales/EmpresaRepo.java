package dam.primero.repositorio.institucionales;

import dam.primero.config.institucionales.MySqlConectorInstitucionales;
import dam.primero.exception.MyException;
import dam.primero.modelos.institucionales.modelo.Empresa;
import dam.primero.modelos.institucionales.modelo.HistorialOportunidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpresaRepo {
    private MySqlConectorInstitucionales conector;

    //Constructor
    public EmpresaRepo() {
        try {
            this.conector = new MySqlConectorInstitucionales();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public List<Empresa> listarEmpresas() {

        List<Empresa> empresas = new ArrayList<>();

        String query = "SELECT o.ID_Organizacion, o.Nombre, o.Direccion, o.Telefono, o.Email, o.Ciudad, o.Fecha_Registro, o.ID_Historial, " +
                "       e.id_empresa, e.Sector, e.Num_Empleados " +
                "FROM OrganizacionCRM o " +
                "INNER JOIN Empresa e ON o.ID_Organizacion = e.id_empresa;";

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                Empresa emp = new Empresa();

                // Datos de la clase Padre (Organizacion)
                emp.setIdOrganizacion(rs.getInt("ID_Organizacion"));
                emp.setNombre(rs.getString("Nombre"));
                emp.setDireccion(rs.getString("Direccion"));
                emp.setTelefono(rs.getInt("Telefono"));
                emp.setEmail(rs.getString("Email"));
                emp.setCiudad(rs.getString("Ciudad"));

                if (rs.getDate("Fecha_Registro") != null) {
                    emp.setFechaRegistro(rs.getDate("Fecha_Registro").toLocalDate());
                }

                if (rs.getObject("ID_Historial") != null) {
                    HistorialOportunidad ho = new HistorialOportunidad();
                    ho.setIdHistorial(rs.getInt("ID_Historial"));
                    emp.setIdHistorial(ho);
                }

                // Datos de la clase Hija (Empresa)
                emp.setIdEmpresa(rs.getInt("id_empresa"));
                emp.setSector(rs.getString("Sector"));
                emp.setNumEmpleados(rs.getInt("Num_Empleados"));

                empresas.add(emp);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return empresas; /*Metodo Listado de Empresas*/
    }
}