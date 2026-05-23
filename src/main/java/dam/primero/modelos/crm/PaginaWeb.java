package dam.primero.modelos.crm;

import java.util.Objects;

public class PaginaWeb {
    private int idPagina;
    private String titulo;
    private String url;
    private String contenidoHTML;

    public PaginaWeb() {
    }

    public PaginaWeb(int idPagina, String titulo, String url, String contenidoHTML) {
        this.idPagina = idPagina;
        this.titulo = titulo;
        this.url = url;
        this.contenidoHTML = contenidoHTML;
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
                '}';
    }
}
