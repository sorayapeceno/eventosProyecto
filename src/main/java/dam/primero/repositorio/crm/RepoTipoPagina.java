package dam.primero.repositorio.crm;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;
import dam.primero.modelos.crm.PaginaWeb;
import dam.primero.modelos.crm.TipoPagina;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepoTipoPagina {
    private MySqlConector conector;

    public RepoTipoPagina() {
        try {
            this.conector = new MySqlConector();
        } catch (MyException e) {
            throw new RuntimeException("Error al conectar a la base de datos" + e.getMessage());
        }
    }

    public List<TipoPagina> listarTipoPagina() {
        List<TipoPagina> paginas = new ArrayList<>();

        String query = "SELECT * FROM tipopagina";

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.conector.getConnect().createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int idTipoPagina = rs.getInt("Id_Tipo_Pagina");
                String nombre = rs.getString("Nombre_Tipo");
                String descripcion = rs.getString("Descripcion");

                TipoPagina tp = new TipoPagina(idTipoPagina, nombre, descripcion);

                paginas.add(tp);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return paginas;
    }
}
