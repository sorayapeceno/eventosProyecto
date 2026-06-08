package dam.primero.repositorio.eventos_participantes;

import dam.primero.modelos.eventos_participantes.Modelo.Estado;
import dam.primero.modelos.eventos_participantes.Modelo.Evento;
import dam.primero.modelos.eventos_participantes.Modelo.Modalidad;
import dam.primero.modelos.eventos_participantes.Modelo.Ponencia;
import dam.primero.modelos.eventos_participantes.Modelo.Ponente;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Genera JSON manualmente (sin Gson ni ficheros .json)
 * para las entidades del módulo eventos_participantes.
 *
 * Métodos disponibles:
 *   - eventosAJson(List<Evento>)                          → todos los eventos
 *   - eventosFiltradosPorEstado(List<Evento>, Estado)     → filtrar por estado
 *   - eventosFiltradosPorModalidad(List<Evento>, Modalidad) → filtrar por modalidad
 *   - eventosFiltradosPorCiudad(List<Evento>, String)     → filtrar por ciudad
 *   - ponenciasAJson(Set<Ponencia>)                       → todas las ponencias
 *   - ponenciasFiltradosPorNivel(Set<Ponencia>, String)   → filtrar por nivel
 *   - ponentesAJson(Set<Ponente>)                         → todos los ponentes
 */
public class GeneradorJsonEventos {

    // =========================================================
    //  MÉTODOS DE FILTRADO
    // =========================================================

    /**
     * Filtra una lista de eventos por su estado (BORRADOR, ABIERTO, CERRADO…)
     */
    public List<Evento> filtrarEventosPorEstado(List<Evento> eventos, Estado estado) {
        List<Evento> resultado = new ArrayList<>();
        for (Evento e : eventos) {
            if (e.getEstado() == estado) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    /**
     * Filtra una lista de eventos por modalidad (PRESENCIAL, ONLINE, HIBRIDO)
     */
    public List<Evento> filtrarEventosPorModalidad(List<Evento> eventos, Modalidad modalidad) {
        List<Evento> resultado = new ArrayList<>();
        for (Evento e : eventos) {
            if (e.getModalidad() == modalidad) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    /**
     * Filtra una lista de eventos por ciudad (sin distinguir mayúsculas/minúsculas)
     */
    public List<Evento> filtrarEventosPorCiudad(List<Evento> eventos, String ciudad) {
        List<Evento> resultado = new ArrayList<>();
        for (Evento e : eventos) {
            if (e.getCiudad() != null && e.getCiudad().equalsIgnoreCase(ciudad)) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    /**
     * Filtra ponencias por nivel (BASICO, INTERMEDIO, AVANZADO)
     */
    public List<Ponencia> filtrarPonenciasPorNivel(Set<Ponencia> ponencias, String nivel) {
        List<Ponencia> resultado = new ArrayList<>();
        for (Ponencia p : ponencias) {
            if (p.getNivel() != null && p.getNivel().name().equalsIgnoreCase(nivel)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // =========================================================
    //  MÉTODOS DE GENERACIÓN DE JSON
    // =========================================================

    /**
     * Convierte una lista de Eventos a JSON en formato String.
     * Ejemplo de salida:
     * [
     *   {
     *     "id_Evento": 1,
     *     "nombre": "JavaDay",
     *     ...
     *   }
     * ]
     */
    public String eventosAJson(List<Evento> eventos) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            sb.append("  {\n");
            sb.append("    \"id_Evento\": ").append(e.getId_Evento()).append(",\n");
            sb.append("    \"nombre\": \"").append(escapar(e.getNombre())).append("\",\n");
            sb.append("    \"descripcion\": \"").append(escapar(e.getDescripcion())).append("\",\n");
            sb.append("    \"fechaInicio\": \"").append(e.getFechaInicio()).append("\",\n");
            sb.append("    \"fechaFin\": \"").append(e.getFechaFin()).append("\",\n");
            sb.append("    \"direccion\": \"").append(escapar(e.getDireccion())).append("\",\n");
            sb.append("    \"ciudad\": \"").append(escapar(e.getCiudad())).append("\",\n");
            sb.append("    \"capacidad\": ").append(e.getCapacidad()).append(",\n");
            sb.append("    \"estado\": \"").append(e.getEstado()).append("\",\n");
            sb.append("    \"modalidad\": \"").append(e.getModalidad()).append("\",\n");
            sb.append("    \"lugar\": \"").append(escapar(e.getLugar())).append("\"\n");
            sb.append("  }");

            // Si no es el último elemento, ponemos coma
            if (i < eventos.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * Convierte un Set de Ponencias a JSON en formato String.
     */
    public String ponenciasAJson(Set<Ponencia> ponencias) {
        // Pasamos el Set a List para poder recorrerlo con índice
        List<Ponencia> lista = new ArrayList<>(ponencias);

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < lista.size(); i++) {
            Ponencia p = lista.get(i);
            sb.append("  {\n");
            sb.append("    \"id_Ponencia\": ").append(p.getId_Ponencia()).append(",\n");
            sb.append("    \"id_Evento\": ").append(p.getId_Evento()).append(",\n");
            sb.append("    \"titulo\": \"").append(escapar(p.getTitulo())).append("\",\n");
            sb.append("    \"duracion\": ").append(p.getDuracion()).append(",\n");
            sb.append("    \"fecha\": \"").append(p.getFecha()).append("\",\n");
            sb.append("    \"hora\": \"").append(p.getHora()).append("\",\n");
            sb.append("    \"ubicacion\": \"").append(escapar(p.getUbicacion())).append("\",\n");
            sb.append("    \"nivel\": \"").append(p.getNivel()).append("\",\n");
            sb.append("    \"tipo\": \"").append(p.getTipo()).append("\",\n");
            sb.append("    \"formato\": \"").append(p.getFormato()).append("\"\n");
            sb.append("  }");

            if (i < lista.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * Convierte un Set de Ponentes a JSON en formato String.
     */
    public String ponentesAJson(Set<Ponente> ponentes) {
        List<Ponente> lista = new ArrayList<>(ponentes);

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < lista.size(); i++) {
            Ponente p = lista.get(i);
            sb.append("  {\n");
            sb.append("    \"idPonente\": ").append(p.getIdPonente()).append(",\n");
            sb.append("    \"bio\": \"").append(escapar(p.getBIO())).append("\",\n");
            sb.append("    \"especialidad\": \"").append(escapar(p.getEspecialidad())).append("\",\n");
            sb.append("    \"cv\": \"").append(escapar(p.getCV())).append("\",\n");
            sb.append("    \"nivelImparticion\": \"").append(p.getNivelImparticion()).append("\"\n");
            sb.append("  }");

            if (i < lista.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("]");
        return sb.toString();
    }

    // =========================================================
    //  MÉTODOS COMBINADOS (filtrar + generar JSON de una vez)
    // =========================================================

    /**
     * Filtra por estado Y devuelve el JSON resultante.
     * Equivale a llamar filtrarEventosPorEstado() + eventosAJson()
     */
    public String eventosFiltradosPorEstadoAJson(List<Evento> eventos, Estado estado) {
        List<Evento> filtrados = filtrarEventosPorEstado(eventos, estado);
        return eventosAJson(filtrados);
    }

    /**
     * Filtra por modalidad Y devuelve el JSON resultante.
     */
    public String eventosFiltradosPorModalidadAJson(List<Evento> eventos, Modalidad modalidad) {
        List<Evento> filtrados = filtrarEventosPorModalidad(eventos, modalidad);
        return eventosAJson(filtrados);
    }

    /**
     * Filtra por ciudad Y devuelve el JSON resultante.
     */
    public String eventosFiltradosPorCiudadAJson(List<Evento> eventos, String ciudad) {
        List<Evento> filtrados = filtrarEventosPorCiudad(eventos, ciudad);
        return eventosAJson(filtrados);
    }

    /**
     * Filtra ponencias por nivel Y devuelve el JSON resultante.
     */
    public String ponenciasFiltradosPorNivelAJson(Set<Ponencia> ponencias, String nivel) {
        List<Ponencia> filtrados = filtrarPonenciasPorNivel(ponencias, nivel);
        // Reutilizamos ponenciasAJson pasando los filtrados como Set (o directamente como lista)
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < filtrados.size(); i++) {
            Ponencia p = filtrados.get(i);
            sb.append("  {\n");
            sb.append("    \"id_Ponencia\": ").append(p.getId_Ponencia()).append(",\n");
            sb.append("    \"id_Evento\": ").append(p.getId_Evento()).append(",\n");
            sb.append("    \"titulo\": \"").append(escapar(p.getTitulo())).append("\",\n");
            sb.append("    \"duracion\": ").append(p.getDuracion()).append(",\n");
            sb.append("    \"fecha\": \"").append(p.getFecha()).append("\",\n");
            sb.append("    \"hora\": \"").append(p.getHora()).append("\",\n");
            sb.append("    \"ubicacion\": \"").append(escapar(p.getUbicacion())).append("\",\n");
            sb.append("    \"nivel\": \"").append(p.getNivel()).append("\",\n");
            sb.append("    \"tipo\": \"").append(p.getTipo()).append("\",\n");
            sb.append("    \"formato\": \"").append(p.getFormato()).append("\"\n");
            sb.append("  }");
            if (i < filtrados.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    // =========================================================
    //  MÉTODO AUXILIAR PRIVADO
    // =========================================================

    /**
     * Escapa caracteres especiales para que el JSON sea válido.
     * Reemplaza comillas dobles y barras invertidas que podrían romper el JSON.
     */
    private String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        // Primero las barras, luego las comillas (orden importa)
        texto = texto.replace("\\", "\\\\");
        texto = texto.replace("\"", "\\\"");
        return texto;
    }
}
