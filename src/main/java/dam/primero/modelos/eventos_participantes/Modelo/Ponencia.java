package dam.primero.modelos.eventos_participantes.Modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Ponencia {

    private static int contador;
    private int id_Ponencia; // Modificado para que coincida exactamente con tu HTML y Repo
    private int id_Evento;   // Añadido para que puedas guardar y listar el evento
    private String titulo;
    private int tematica;
    private int duracion;
    private LocalDate fecha;
    private LocalDateTime hora;
    private String ubicacion;
    private String tema;
    private Nivel nivel;
    private Tipo tipo;
    private Formato formato;

    // Constructor vacío por buena práctica
    public Ponencia() {}

    // Constructor completo modificado para aceptar y respetar el ID real de la base de datos
    public Ponencia(int id_Ponencia, String titulo, int tematica, int duracion, LocalDate fecha, LocalDateTime hora, String ubicacion, String tema, Nivel nivel, Tipo tipo, Formato formato) {
        contador++;
        this.id_Ponencia = id_Ponencia; // Guardamos el ID real de la BBDD
        this.titulo = titulo;
        this.tematica = tematica;
        this.duracion = duracion;
        this.fecha = fecha;
        this.hora = hora;
        this.ubicacion = ubicacion;
        this.tema = tema;
        this.nivel = nivel;
        this.tipo = tipo;
        this.formato = formato;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Ponencia.contador = contador;
    }

    // Getters y Setters actualizados con el nombre correcto para Thymeleaf
    public int getId_Ponencia() {
        return id_Ponencia;
    }

    public void setId_Ponencia(int id_Ponencia) {
        this.id_Ponencia = id_Ponencia;
    }

    public int getId_Evento() {
        return id_Evento;
    }

    public void setId_Evento(int id_Evento) {
        this.id_Evento = id_Evento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getTematica() {
        return tematica;
    }

    public void setTematica(int tematica) {
        this.tematica = tematica;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Formato getFormato() {
        return formato;
    }

    public void setFormato(Formato formato) {
        this.formato = formato;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ponencia ponencia = (Ponencia) o;
        return id_Ponencia == ponencia.id_Ponencia;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id_Ponencia);
    }

    @Override
    public String toString() {
        return "Ponencia{" +
                "id_Ponencia=" + id_Ponencia +
                ", id_Evento=" + id_Evento +
                ", titulo='" + titulo + '\'' +
                ", tematica=" + tematica +
                ", duracion=" + duracion +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", ubicacion='" + ubicacion + '\'' +
                ", tema='" + tema + '\'' +
                ", nivel=" + nivel +
                ", tipo=" + tipo +
                ", formato=" + formato +
                '}';
    }
}