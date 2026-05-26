package dam.primero.repositorio.dashboards;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Repositorio de consultas agregadas para los Dashboards.
 * Lee de la base de datos "proyectofinaljud" (eventos/ponencias/ponentes)
 * y de "eventos" (ventas/clientes), usando el mismo MySqlConector
 * que el resto del proyecto.
 *
 * Cada método devuelve un Map<String, Integer> con la etiqueta y el valor,
 * listo para serializar a JSON en la plantilla Thymeleaf y pintarlo con Chart.js.
 */
public class RepoDashboards {

    private MySqlConector conector;

    public RepoDashboards() {
        try {
            this.conector = new MySqlConector();
        } catch (MyException e) {
            System.out.println("RepoDashboards – Error al conectar: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════
    //  EVENTOS
    // ══════════════════════════════════════════════════════

    /** Número total de eventos registrados. */
    public int contarEventos() {
        return contarFila("SELECT COUNT(*) FROM proyectofinaljud.Evento");
    }

    /** Eventos agrupados por Estado (Borrador, Abierto, Cerrado…). */
    public Map<String, Integer> eventosPorEstado() {
        return agrupar(
                "SELECT Estado, COUNT(*) AS total " +
                        "FROM proyectofinaljud.Evento " +
                        "GROUP BY Estado ORDER BY total DESC",
                "Estado", "total"
        );
    }

    /** Eventos agrupados por Modalidad (Presencial, Online, Híbrido). */
    public Map<String, Integer> eventosPorModalidad() {
        return agrupar(
                "SELECT Modalidad, COUNT(*) AS total " +
                        "FROM proyectofinaljud.Evento " +
                        "GROUP BY Modalidad ORDER BY total DESC",
                "Modalidad", "total"
        );
    }

    /** Eventos agrupados por Ciudad. */
    public Map<String, Integer> eventosPorCiudad() {
        return agrupar(
                "SELECT Ciudad, COUNT(*) AS total " +
                        "FROM proyectofinaljud.Evento " +
                        "WHERE Ciudad IS NOT NULL " +
                        "GROUP BY Ciudad ORDER BY total DESC",
                "Ciudad", "total"
        );
    }

    // ══════════════════════════════════════════════════════
    //  PONENCIAS
    // ══════════════════════════════════════════════════════

    /** Número total de ponencias. */
    public int contarPonencias() {
        return contarFila("SELECT COUNT(*) FROM proyectofinaljud.Ponencia");
    }

    /** Ponencias agrupadas por Tipo (Charla, Taller, Mesa, Podcast). */
    public Map<String, Integer> ponenciasPorTipo() {
        return agrupar(
                "SELECT Tipo, COUNT(*) AS total " +
                        "FROM proyectofinaljud.Ponencia " +
                        "GROUP BY Tipo ORDER BY total DESC",
                "Tipo", "total"
        );
    }

    /** Ponencias agrupadas por Nivel (Basico, Intermedio, Avanzado). */
    public Map<String, Integer> ponenciasPorNivel() {
        return agrupar(
                "SELECT Nivel, COUNT(*) AS total " +
                        "FROM proyectofinaljud.Ponencia " +
                        "WHERE Nivel IS NOT NULL " +
                        "GROUP BY Nivel ORDER BY total DESC",
                "Nivel", "total"
        );
    }

    /** Ponencias agrupadas por Formato (Presencial, Online, Híbrido). */
    public Map<String, Integer> ponenciasPorFormato() {
        return agrupar(
                "SELECT Formato, COUNT(*) AS total " +
                        "FROM proyectofinaljud.Ponencia " +
                        "GROUP BY Formato ORDER BY total DESC",
                "Formato", "total"
        );
    }

    /** Ponencias agrupadas por Temática. */
    public Map<String, Integer> ponenciasPorTematica() {
        return agrupar(
                "SELECT t.Tema, COUNT(*) AS total " +
                        "FROM proyectofinaljud.Ponencia p " +
                        "JOIN proyectofinaljud.Tematica t ON p.id_Tematica = t.id_Tematica " +
                        "GROUP BY t.Tema ORDER BY total DESC",
                "Tema", "total"
        );
    }

    // ══════════════════════════════════════════════════════
    //  PONENTES
    // ══════════════════════════════════════════════════════

    /** Número total de ponentes. */
    public int contarPonentes() {
        return contarFila("SELECT COUNT(*) FROM proyectofinaljud.Ponente");
    }

    /** Ponentes agrupados por Especialidad. */
    public Map<String, Integer> ponentesPorEspecialidad() {
        return agrupar(
                "SELECT Especialidad, COUNT(*) AS total " +
                        "FROM proyectofinaljud.Ponente " +
                        "GROUP BY Especialidad ORDER BY total DESC",
                "Especialidad", "total"
        );
    }

    /** Ponentes agrupados por Nivel de Impartición. */
    public Map<String, Integer> ponentesPorNivelImparticion() {
        return agrupar(
                "SELECT Nivel_Imparticion, COUNT(*) AS total " +
                        "FROM proyectofinaljud.Ponente " +
                        "GROUP BY Nivel_Imparticion ORDER BY total DESC",
                "Nivel_Imparticion", "total"
        );
    }

    /**
     * Top ponentes por número de ponencias impartidas.
     * Devuelve nombre completo → número de ponencias.
     */
    public Map<String, Integer> topPonentes() {
        return agrupar(
                "SELECT CONCAT(per.Nombre, ' ', per.Ap1) AS nombre, " +
                        "       COUNT(pp.id_Ponencia) AS total " +
                        "FROM proyectofinaljud.Ponente_Ponencia pp " +
                        "JOIN proyectofinaljud.Ponente po ON pp.id_Ponente = po.id_Ponente " +
                        "JOIN proyectofinaljud.Persona per ON po.id_Persona = per.id_Persona " +
                        "GROUP BY nombre ORDER BY total DESC LIMIT 10",
                "nombre", "total"
        );
    }

    // ══════════════════════════════════════════════════════
    //  VENTAS / CLIENTES
    // ══════════════════════════════════════════════════════

    /** Número total de clientes en la BD de ventas. */
    public int contarClientes() {
        return contarFila("SELECT COUNT(*) FROM eventos.Cliente");
    }

    /**
     * Clientes por ciudad (campo Ciudad en Persona de proyectofinaljud).
     * Se usa como proxy demográfico ya que la tabla Cliente de ventas
     * solo tiene nombre/correo/teléfono.
     */
    public Map<String, Integer> clientesPorCiudad() {
        return agrupar(
                "SELECT Ciudad, COUNT(*) AS total " +
                        "FROM proyectofinaljud.Persona " +
                        "WHERE Ciudad IS NOT NULL " +
                        "GROUP BY Ciudad ORDER BY total DESC",
                "Ciudad", "total"
        );
    }

    // ══════════════════════════════════════════════════════
    //  UTILIDADES PRIVADAS
    // ══════════════════════════════════════════════════════

    /**
     * Ejecuta una query que devuelve exactamente una fila con COUNT(*)
     * y retorna ese entero.
     */
    private int contarFila(String sql) {
        try {
            Statement stmt = conector.getConnect().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("RepoDashboards contarFila – " + e.getMessage());
        }
        return 0;
    }

    /**
     * Ejecuta una query de agrupación y construye un Map<etiqueta, valor>
     * manteniendo el orden de filas del resultado.
     *
     * @param sql         La consulta SELECT con dos columnas: etiqueta y número.
     * @param colEtiqueta Nombre de la columna de texto (eje X / leyenda).
     * @param colValor    Nombre de la columna numérica.
     */
    private Map<String, Integer> agrupar(String sql, String colEtiqueta, String colValor) {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        try {
            Statement stmt = conector.getConnect().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String etiqueta = rs.getString(colEtiqueta);
                int valor = rs.getInt(colValor);
                resultado.put(etiqueta != null ? etiqueta : "Sin datos", valor);
            }
        } catch (SQLException e) {
            System.out.println("RepoDashboards agrupar – " + e.getMessage());
        }
        return resultado;
    }
}