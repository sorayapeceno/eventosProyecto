package dam.primero.modelos.eventos_participantes.Modelo;

import java.time.LocalDate;
import java.util.Objects;

public class Evento {
    private static int contador;
    private int id_Evento;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String direccion;
    private String ciudad;
    private int capacidad;
    private Estado estado;
    private Modalidad modalidad;
    private String lugar;

    public Evento(int id_Evento, String nombre, String descripcion,
                  LocalDate fechaInicio, LocalDate fechaFin, String direccion, String ciudad, int capacidad,
                  Estado estado, Modalidad modalidad, String lugar) {

        contador++;
        this.id_Evento = contador;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.capacidad = capacidad;
        this.estado = estado;
        this.modalidad = modalidad;
        this.lugar = lugar;
    }

    public Evento() {
    }

    public int getId_Evento() {
        return id_Evento;
    }

    public void setId_Evento(int id_Evento) {
        this.id_Evento = id_Evento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Evento.contador = contador;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return id_Evento == evento.id_Evento;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id_Evento);
    }

    @Override
    public String toString() {
        return "Evento{" +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", direccion='" + direccion + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", capacidad=" + capacidad +
                ", estado=" + estado +
                ", modalidad=" + modalidad +
                ", lugar='" + lugar + '\'' +
                '}';
    }
}
