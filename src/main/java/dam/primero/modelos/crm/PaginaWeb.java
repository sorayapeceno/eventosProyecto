package dam.primero.modelos.crm;

import java.time.LocalDate;
import java.util.Objects;

public class PaginaWeb {
    private int idPagina;
    private String titulo;
    private String url;
    private String contenidoHTML;
    private LocalDate fechaCreacion;
    private LocalDate fechaModificacion;
    private int idTipoPagina;

    public PaginaWeb() {
    }

    public PaginaWeb(int idPagina, String titulo, String url, String contenidoHTML, LocalDate fechaCreacion, LocalDate fechaModificacion, int idTipoPagina) {
        this.idPagina = idPagina;
        this.titulo = titulo;
        this.url = url;
        this.contenidoHTML = contenidoHTML;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.idTipoPagina = idTipoPagina;
    }

    public int getIdPagina() {
        return idPagina;
    }

    public void setIdPagina(int idPagina) {
        this.idPagina = idPagina;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContenidoHTML() {
        return contenidoHTML;
    }

    public void setContenidoHTML(String contenidoHTML) {
        this.contenidoHTML = contenidoHTML;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public int getIdTipoPagina() {
        return idTipoPagina;
    }

    public void setIdTipoPagina(int idTipoPagina) {
        this.idTipoPagina = idTipoPagina;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaginaWeb paginaWeb)) return false;
        return Objects.equals(url, paginaWeb.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }

    @Override
    public String toString() {
        return "PaginaWeb{" +
                "idPagina=" + idPagina +
                ", titulo='" + titulo + '\'' +
                ", url='" + url + '\'' +
                ", contenidoHTML='" + contenidoHTML + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaModificacion=" + fechaModificacion +
                ", idTipoPagina=" + idTipoPagina +
                '}';
    }
}
