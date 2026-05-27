package dam.primero.repositorio.dashboards;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    //  EVENTOS - resumen
    // ══════════════════════════════════════════════════════

    public int contarEventos() {
        return contarFila("SELECT COUNT(*) FROM proyectofinaljud.Evento");
    }

    public Map<String, Integer> eventosPorEstado() {
        return agrupar(
                "SELECT Estado, COUNT(*) AS total FROM proyectofinaljud.Evento " +
                        "GROUP BY Estado ORDER BY total DESC", "Estado", "total");
    }

    public Map<String, Integer> eventosPorModalidad() {
        return agrupar(
                "SELECT Modalidad, COUNT(*) AS total FROM proyectofinaljud.Evento " +
                        "GROUP BY Modalidad ORDER BY total DESC", "Modalidad", "total");
    }

    public Map<String, Integer> eventosPorCiudad() {
        return agrupar(
                "SELECT Ciudad, COUNT(*) AS total FROM proyectofinaljud.Evento " +
                        "WHERE Ciudad IS NOT NULL GROUP BY Ciudad ORDER BY total DESC",
                "Ciudad", "total");
    }

    // ── Detalle eventos ──────────────────────────────────
    public List<Map<String, String>> detalleEventos() {
        return listar(
                "SELECT Nombre, DATE_FORMAT(Fecha_Inicio,'%d/%m/%Y') AS Fecha_Inicio, " +
                        "DATE_FORMAT(Fecha_Fin,'%d/%m/%Y') AS Fecha_Fin, " +
                        "Ciudad, Estado, Modalidad, Capacidad, Lugar " +
                        "FROM proyectofinaljud.Evento ORDER BY Fecha_Inicio",
                new String[]{"Nombre","Fecha_Inicio","Fecha_Fin","Ciudad","Estado","Modalidad","Capacidad","Lugar"});
    }

    // ══════════════════════════════════════════════════════
    //  PONENCIAS - resumen
    // ══════════════════════════════════════════════════════

    public int contarPonencias() {
        return contarFila("SELECT COUNT(*) FROM proyectofinaljud.Ponencia");
    }

    public Map<String, Integer> ponenciasPorTipo() {
        return agrupar(
                "SELECT Tipo, COUNT(*) AS total FROM proyectofinaljud.Ponencia " +
                        "GROUP BY Tipo ORDER BY total DESC", "Tipo", "total");
    }

    public Map<String, Integer> ponenciasPorNivel() {
        return agrupar(
                "SELECT Nivel, COUNT(*) AS total FROM proyectofinaljud.Ponencia " +
                        "WHERE Nivel IS NOT NULL GROUP BY Nivel ORDER BY total DESC",
                "Nivel", "total");
    }

    public Map<String, Integer> ponenciasPorFormato() {
        return agrupar(
                "SELECT Formato, COUNT(*) AS total FROM proyectofinaljud.Ponencia " +
                        "GROUP BY Formato ORDER BY total DESC", "Formato", "total");
    }

    public Map<String, Integer> ponenciasPorTematica() {
        return agrupar(
                "SELECT t.Tema, COUNT(*) AS total " +
                        "FROM proyectofinaljud.Ponencia p " +
                        "JOIN proyectofinaljud.Tematica t ON p.id_Tematica = t.id_Tematica " +
                        "GROUP BY t.Tema ORDER BY total DESC", "Tema", "total");
    }

    // ── Detalle ponencias ────────────────────────────────
    public List<Map<String, String>> detallePonencias() {
        return listar(
                "SELECT p.Titulo, t.Tema, p.Tipo, p.Nivel, p.Formato, " +
                        "p.Duracion, DATE_FORMAT(p.Fecha,'%d/%m/%Y') AS Fecha, p.Hora, " +
                        "p.Sala, e.Nombre AS Evento " +
                        "FROM proyectofinaljud.Ponencia p " +
                        "JOIN proyectofinaljud.Tematica t ON p.id_Tematica = t.id_Tematica " +
                        "JOIN proyectofinaljud.Evento e ON p.id_Evento = e.id_Evento " +
                        "ORDER BY p.Fecha, p.Hora",
                new String[]{"Titulo","Tema","Tipo","Nivel","Formato","Duracion","Fecha","Hora","Sala","Evento"});
    }

    // ══════════════════════════════════════════════════════
    //  PONENTES - resumen
    // ══════════════════════════════════════════════════════

    public int contarPonentes() {
        return contarFila("SELECT COUNT(*) FROM proyectofinaljud.Ponente");
    }

    public Map<String, Integer> ponentesPorEspecialidad() {
        return agrupar(
                "SELECT Especialidad, COUNT(*) AS total FROM proyectofinaljud.Ponente " +
                        "GROUP BY Especialidad ORDER BY total DESC", "Especialidad", "total");
    }

    public Map<String, Integer> ponentesPorNivelImparticion() {
        return agrupar(
                "SELECT Nivel_Imparticion, COUNT(*) AS total FROM proyectofinaljud.Ponente " +
                        "GROUP BY Nivel_Imparticion ORDER BY total DESC",
                "Nivel_Imparticion", "total");
    }

    public Map<String, Integer> topPonentes() {
        return agrupar(
                "SELECT CONCAT(per.Nombre, ' ', per.Ap1) AS nombre, " +
                        "COUNT(pp.id_Ponencia) AS total " +
                        "FROM proyectofinaljud.Ponente_Ponencia pp " +
                        "JOIN proyectofinaljud.Ponente po ON pp.id_Ponente = po.id_Ponente " +
                        "JOIN proyectofinaljud.Persona per ON po.id_Persona = per.id_Persona " +
                        "GROUP BY per.Nombre, per.Ap1 ORDER BY total DESC LIMIT 10",
                "nombre", "total");
    }

    // ── Detalle ponentes ─────────────────────────────────
    public List<Map<String, String>> detallePonentes() {
        return listar(
                "SELECT CONCAT(per.Nombre,' ',per.Ap1) AS Nombre, " +
                        "po.Especialidad, po.Nivel_Imparticion, " +
                        "COUNT(pp.id_Ponencia) AS Ponencias, " +
                        "per.Correo, per.Ciudad " +
                        "FROM proyectofinaljud.Ponente po " +
                        "JOIN proyectofinaljud.Persona per ON po.id_Persona = per.id_Persona " +
                        "LEFT JOIN proyectofinaljud.Ponente_Ponencia pp ON po.id_Ponente = pp.id_Ponente " +
                        "GROUP BY po.id_Ponente, per.Nombre, per.Ap1, po.Especialidad, " +
                        "po.Nivel_Imparticion, per.Correo, per.Ciudad " +
                        "ORDER BY Ponencias DESC",
                new String[]{"Nombre","Especialidad","Nivel_Imparticion","Ponencias","Correo","Ciudad"});
    }

    // ══════════════════════════════════════════════════════
    //  VENTAS - resumen
    // ══════════════════════════════════════════════════════

    public int contarClientes() {
        return contarFila(
                "SELECT COUNT(*) FROM proyectofinaljud.Persona WHERE Ciudad IS NOT NULL");
    }

    public Map<String, Integer> clientesPorCiudad() {
        return agrupar(
                "SELECT Ciudad, COUNT(*) AS total FROM proyectofinaljud.Persona " +
                        "WHERE Ciudad IS NOT NULL GROUP BY Ciudad ORDER BY total DESC",
                "Ciudad", "total");
    }

    // ── Detalle participantes ────────────────────────────
    public List<Map<String, String>> detalleParticipantes() {
        return listar(
                "SELECT CONCAT(Nombre,' ',Ap1) AS Nombre, Correo, " +
                        "Telefono, Ciudad, Pais, Genero " +
                        "FROM proyectofinaljud.Persona " +
                        "ORDER BY Ciudad, Nombre",
                new String[]{"Nombre","Correo","Telefono","Ciudad","Pais","Genero"});
    }

    // ══════════════════════════════════════════════════════
    //  UTILIDADES PRIVADAS
    // ══════════════════════════════════════════════════════

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

    /** Devuelve una lista de filas, cada fila es un Map columna→valor. */
    private List<Map<String, String>> listar(String sql, String[] columnas) {
        List<Map<String, String>> filas = new ArrayList<>();
        try {
            Statement stmt = conector.getConnect().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, String> fila = new LinkedHashMap<>();
                for (String col : columnas) {
                    String val = rs.getString(col);
                    fila.put(col, val != null ? val : "-");
                }
                filas.add(fila);
            }
        } catch (SQLException e) {
            System.out.println("RepoDashboards listar – " + e.getMessage());
        }
        return filas;
    }
}