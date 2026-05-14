package dam.primero.modelos.eventos_participantes.Modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Ponencia {

    private static int contador;
    private int idPonencia;
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

    public Ponencia(int idPonencia, String titulo, int tematica, int duracion, LocalDate fecha, LocalDateTime hora, String ubicacion, String tema, Nivel nivel, Tipo tipo, Formato formato) {

        contador++;
        this.idPonencia = contador;
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

    public int getIdPonencia() {
        return idPonencia;
    }

    public void setIdPonencia(int idPonencia) {
        this.idPonencia = idPonencia;
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
        return idPonencia == ponencia.idPonencia;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idPonencia);
    }

    @Override
    public String toString() {
        return "Ponencia{" +
                "idPonencia=" + idPonencia +
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
