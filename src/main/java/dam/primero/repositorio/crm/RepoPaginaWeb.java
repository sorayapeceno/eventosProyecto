package dam.primero.repositorio.crm;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.crm.PaginaWeb;
import dam.primero.modelos.eventos_participantes.Modelo.NivelImparticion;
import dam.primero.modelos.eventos_participantes.Modelo.Ponente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepoPaginaWeb {
    private MySqlConector conector;

    public RepoPaginaWeb() {
        try {
            this.conector = new MySqlConector();
        } catch (MyException e) {
            throw new RuntimeException("Error al conectar a la base de datos" + e.getMessage());
        }
    }

    public List<PaginaWeb> listarPaginaWeb() {
        List<PaginaWeb> paginas = new ArrayList<>();

        String query = "SELECT * from paginaweb;";

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id_Pagina = rs.getInt("id_Pagina");
                String Titulo = rs.getString("Titulo");
                String Url = rs.getString("Url");
                String Contenido_HTML  = rs.getString("Contenido_HTML");
                LocalDate Fecha_Creacion = rs.getDate("Fecha_Creacion").toLocalDate();
                LocalDate Fecha_Modificacion = rs.getDate("Fecha_Modificacion").toLocalDate();
                int Id_Tipo_Pagina = rs.getInt("Id_Tipo_Pagina");

                PaginaWeb p = new PaginaWeb(id_Pagina, Titulo, Url, Contenido_HTML, Fecha_Creacion, Fecha_Modificacion, Id_Tipo_Pagina);

                paginas.add(p);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return paginas;
    }
}
