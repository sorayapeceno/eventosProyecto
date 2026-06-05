package dam.primero.modelos.institucionales.modelo;

import dam.primero.repositorio.eventos_participantes.EstadoRepo;

import java.time.LocalDate;
import java.util.Objects;

public class Oportunidad {
    private static int contador;
    private int idOportunidad;
    private int idPersona; // al no tener persona porque no nos pertenece, lo pongo como int
    private String titulo;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Estados estado;
    private int presupuesto;

    public Oportunidad() {
    }

    public Oportunidad(int idOportunidad, String titulo, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, Estados estado, int presupuesto) {
        contador++;
        this.idOportunidad = contador;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.presupuesto = presupuesto;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Oportunidad.contador = contador;
    }

    public int getIdOportunidad() {
        return idOportunidad;
    }

    public void setIdOportunidad(int idOportunidad) {
        this.idOportunidad = idOportunidad;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public int getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(int presupuesto) {
        this.presupuesto = presupuesto;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Oportunidad that = (Oportunidad) o;
        return idOportunidad == that.idOportunidad;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idOportunidad);
    }

    @Override
    public String toString() {
        return "Oportunidad{" +
                "idOportunidad=" + idOportunidad +
                ", idPersona=" + idPersona +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado=" + estado +
                ", presupuesto=" + presupuesto +
                '}';
    }
}
