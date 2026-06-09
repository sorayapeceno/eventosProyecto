package dam.primero.config.dashboards;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dam.primero.config.dashboards.MySqlConectorDashboards;
import dam.primero.exception.MyException;
import dam.primero.modelos.dashboards.Ponente;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UtilidadesJsonPonentes {

    public static void main(String[] args) {

        // 1. Leer todos los ponentes de la BBDD
        List<Ponente> ponentes = leerPonentes();
        System.out.println("Ponentes leídos: " + ponentes.size());

        // 2. Filtrar solo los de nivel FP
        List<Ponente> ponentesFP = new ArrayList<>();
        for (Ponente p : ponentes) {
            if ("FP".equals(p.getNivelImparticion())) {
                ponentesFP.add(p);
            }
        }
        System.out.println("Ponentes de nivel FP: " + ponentesFP.size());

        // 3. Escribir la lista filtrada en un fichero JSON (método abajo)
        escribirJson(ponentesFP, "src/main/resources/dashboards/ponentes_FP.json");
    }

    // Lee los ponentes de la base de datos y los mete en una lista
    private static List<Ponente> leerPonentes() {
        List<Ponente> lista = new ArrayList<>();
        try {
            MySqlConectorDashboards conector = new MySqlConectorDashboards();
            Statement stmt = conector.getConnect().createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT CONCAT(per.Nombre,' ',per.Ap1) AS Nombre, " +
                            "po.Especialidad, po.Nivel_Imparticion, " +
                            "per.Correo, per.Ciudad, " +
                            "COUNT(pp.id_Ponencia) AS TotalPonencias " +
                            "FROM Ponente po " +
                            "JOIN Persona per ON po.id_Persona = per.id_Persona " +
                            "LEFT JOIN Ponente_Ponencia pp ON po.id_Ponente = pp.id_Ponente " +
                            "GROUP BY po.id_Ponente, per.Nombre, per.Ap1, " +
                            "po.Especialidad, po.Nivel_Imparticion, per.Correo, per.Ciudad"
            );
            while (rs.next()) {
                lista.add(new Ponente(
                        rs.getString("Nombre"),
                        rs.getString("Especialidad"),
                        rs.getString("Nivel_Imparticion"),
                        rs.getString("Correo"),
                        rs.getString("Ciudad"),
                        rs.getInt("TotalPonencias")
                ));
            }
        } catch (MyException | SQLException e) {
            System.out.println("Error al leer ponentes: " + e.getMessage());
        }
        return lista;
    }

    // Convierte la lista a JSON y la escribe en un fichero
    private static void escribirJson(List<Ponente> ponentes, String rutaFichero) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); //se crea el objeto Gson
        String json = gson.toJson(ponentes); //Convierte la lista de ponentes a String con formato JSON

        FileWriter writer = null;
        try {
            writer = new FileWriter(rutaFichero); //crea fichero en la ruta
            writer.write(json); //escribe el String JSON en el fichero
            System.out.println("Fichero JSON generado en: " + rutaFichero);
        } catch (IOException e) {
            System.out.println("Error al escribir el fichero: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close(); //cierra el fichero
                } catch (IOException e) {
                    System.out.println("Error al cerrar el fichero: " + e.getMessage());
                }
            }
        }
    }
}