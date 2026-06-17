package dam.primero.repositorio.institucionales;

import dam.primero.config.institucionales.MySqlConectorInstitucionales;
import dam.primero.exception.MyException;
import dam.primero.modelos.institucionales.modelo.HistorialOportunidad;
import dam.primero.modelos.institucionales.modelo.Organizacion;
import dam.primero.modelos.institucionales.modelo.Oportunidad;
import dam.primero.modelos.institucionales.modelo.Recinto;
import dam.primero.modelos.institucionales.modelo.Estados;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistorialRepo {
    private MySqlConectorInstitucionales conector;

    //Constructor
    public HistorialRepo() {
        try {
            this.conector = new MySqlConectorInstitucionales();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public List<HistorialOportunidad> listarHistorialOportunidades() {

        List<HistorialOportunidad> lista = new ArrayList<>();

        String query = "SELECT h.ID_Historial, h.Iteraciones, h.Fecha_Inicio, h.Fecha_Fin, " +
                "       o.ID_Oportunidad, o.Descripcion AS Op_Desc, o.Estado AS Op_Estado, o.Presupuesto AS Op_Pres, o.ID_Persona, " +
                "       org.ID_Organizacion, org.Nombre AS Org_Nombre, org.Email AS Org_Email, org.Direccion AS Org_Dir, org.Telefono AS Org_Tel, org.Ciudad AS Org_Ciu, org.Fecha_Registro AS Org_FReg, " +
                "       r.ID_Recinto, r.Nombre AS Rec_Nombre, r.Capacidad AS Rec_Cap, r.Ubicacion AS Rec_Ubi " +
                "FROM Historial h " +
                "LEFT JOIN Oportunidad o ON h.ID_Historial = o.ID_Historial " +
                "LEFT JOIN OrganizacionCRM org ON h.ID_Historial = org.ID_Historial " +
                "CROSS JOIN Recinto r;";

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                HistorialOportunidad ho = new HistorialOportunidad();

                // 1. Datos del Historial Central
                ho.setIdHistorial(rs.getInt("ID_Historial"));
                ho.setIteraciones(String.valueOf(rs.getInt("Iteraciones")));

                if (rs.getDate("Fecha_Inicio") != null) {
                    ho.setFechaInicio(rs.getDate("Fecha_Inicio").toLocalDate());
                }
                if (rs.getDate("Fecha_Fin") != null) {
                    ho.setFechaFin(rs.getDate("Fecha_Fin").toLocalDate());
                }

                // 2. Mapeo de Oportunidad (Ajustado a tus tipos e int)
                if (rs.getObject("ID_Oportunidad") != null) {
                    Oportunidad op = new Oportunidad();
                    op.setIdOportunidad(rs.getInt("ID_Oportunidad"));
                    op.setDescripcion(rs.getString("Op_Desc"));
                    op.setTitulo(rs.getString("Op_Desc")); // Usamos la descripción como título provisional
                    op.setIdPersona(rs.getInt("ID_Persona"));
                    op.setPresupuesto(rs.getInt("Op_Pres")); // Volcado seguro a INT de tu modelo

                    if (rs.getDate("Fecha_Inicio") != null) {
                        op.setFechaInicio(rs.getDate("Fecha_Inicio").toLocalDate());
                    }
                    if (rs.getDate("Fecha_Fin") != null) {
                        op.setFechaFin(rs.getDate("Fecha_Fin").toLocalDate());
                    }

                    // Mapeo seguro utilizando tu Enum "Estados" pasándolo a mayúsculas
                    if (rs.getString("Op_Estado") != null) {
                        op.setEstado(Estados.valueOf(rs.getString("Op_Estado").toUpperCase()));
                    }

                    ho.setIdOportunidad(op);
                }

                // 3. Mapeo de Organizacion (Evitando recursión infinita de Historial)
                if (rs.getObject("ID_Organizacion") != null) {
                    Organizacion org = new Organizacion();
                    org.setIdOrganizacion(rs.getInt("ID_Organizacion"));
                    org.setNombre(rs.getString("Org_Nombre"));
                    org.setDireccion(rs.getString("Org_Dir"));
                    org.setTelefono(rs.getInt("Org_Tel")); // Pasado como número entero
                    org.setEmail(rs.getString("Org_Email"));
                    org.setCiudad(rs.getString("Org_Ciu"));

                    if (rs.getDate("Org_FReg") != null) {
                        org.setFechaRegistro(rs.getDate("Org_FReg").toLocalDate());
                    }

                    org.setIdHistorial(ho); // Vincula este mismo historial para cerrar el mapa relacional
                    ho.setIdOrganizacion(org);
                }

                // 4. Mapeo de Recinto
                if (rs.getObject("ID_Recinto") != null) {
                    Recinto rec = new Recinto();
                    rec.setIdRecinto(rs.getInt("ID_Recinto"));
                    rec.setNombre(rs.getString("Rec_Nombre"));
                    rec.setCapacidad(rs.getInt("Rec_Cap"));
                    rec.setUbicacion(rs.getString("Rec_Ubi"));

                    ho.setIdRecinto(rec);
                }

                lista.add(ho);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return lista; /*Metodo Listado unificado de HistorialOportunidad*/
    }
}