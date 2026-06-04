package dam.primero.modelos.ventas;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un asistente del evento.
 * Guarda sus datos personales, temática de interés, nivel de impartición
 * y el total acumulado en compras. Puede tener varios tickets asociados.
 *
 * @author Jose Lazo
 */
public class Asistente {

    private long id_Asistente;
    private String tematica;
    private String direccion;
    private String observaciones;
    private String bio;
    private double totalGastado;
    private String nivelImparticion;

    private List<Ticket> listaTickets = new ArrayList<>();

    public Asistente() {}

    public Asistente(long id_Asistente, String tematica, String direccion,
                     String observaciones, String bio,
                     double totalGastado, String nivelImparticion) {
        this.id_Asistente    = id_Asistente;
        this.tematica        = tematica;
        this.direccion       = direccion;
        this.observaciones   = observaciones;
        this.bio             = bio;
        this.totalGastado    = totalGastado;
        this.nivelImparticion = nivelImparticion;
    }

    public long getId_Asistente() { return id_Asistente; }
    public void setId_Asistente(long id_Asistente) { this.id_Asistente = id_Asistente; }

    public String getTematica() { return tematica; }
    public void setTematica(String tematica) { this.tematica = tematica; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public double getTotalGastado() { return totalGastado; }
    public void setTotalGastado(double totalGastado) { this.totalGastado = totalGastado; }

    public String getNivelImparticion() { return nivelImparticion; }
    public void setNivelImparticion(String nivelImparticion) { this.nivelImparticion = nivelImparticion; }

    public List<Ticket> getListaTickets() { return listaTickets; }
    public void setListaTickets(List<Ticket> listaTickets) { this.listaTickets = listaTickets; }

    @Override
    public String toString() {
        return "Asistente{id=" + id_Asistente +
               ", tematica='" + tematica + '\'' +
               ", nivelImparticion='" + nivelImparticion + '\'' +
               ", totalGastado=" + totalGastado + '}';
    }
}
