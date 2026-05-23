package dam.primero.modelos.crm;

import java.util.Objects;

public class TipoPagina {
    private int idTipoPagina;
    private String nombreTipo;
    private String descripcion;

    public TipoPagina() {
    }

    public TipoPagina(int idTipoPagina, String nombreTipo, String descripcion) {
        this.idTipoPagina = idTipoPagina;
        this.nombreTipo = nombreTipo;
        this.descripcion = descripcion;
    }

    public int getIdTipoPagina() {
        return idTipoPagina;
    }

    public void setIdTipoPagina(int idTipoPagina) {
        this.idTipoPagina = idTipoPagina;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TipoPagina that)) return false;
        return idTipoPagina == that.idTipoPagina;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idTipoPagina);
    }

    @Override
    public String toString() {
        return "TipoPagina{" +
                "idTipoPagina=" + idTipoPagina +
                ", nombreTipo='" + nombreTipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
