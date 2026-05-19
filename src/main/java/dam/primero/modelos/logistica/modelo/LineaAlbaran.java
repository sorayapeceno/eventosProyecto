package dam.primero.modelos.logistica.modelo;

import java.time.LocalDate;
import java.util.List;

public class LineaAlbaran {

    private int idLineaAlbaran;
    private int cantidadRecibida;
    private EstadoProducto estadoProducto;
    private String lote;
    private LocalDate fechaCaducidad;
    private int diferenciaCantidad;
    private List<LineaAlbaran> lineasAlbaran;

    public int getIdLineaAlbaran() {
        return idLineaAlbaran;
    }

    public void setIdLineaAlbaran(int idLineaAlbaran) {
        this.idLineaAlbaran = idLineaAlbaran;
    }

    public int getCantidadRecibida() {
        return cantidadRecibida;
    }

    public void setCantidadRecibida(int cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }

    public EstadoProducto getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(EstadoProducto estadoProducto) {
        this.estadoProducto = estadoProducto;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public int getDiferenciaCantidad() {
        return diferenciaCantidad;
    }

    public void setDiferenciaCantidad(int diferenciaCantidad) {
        this.diferenciaCantidad = diferenciaCantidad;
    }

    public List<LineaAlbaran> getLineasAlbaran() {
        return lineasAlbaran;
    }

    public void setLineasAlbaran(List<LineaAlbaran> lineasAlbaran) {
        this.lineasAlbaran = lineasAlbaran;
    }

    @Override
    public String toString() {
        return "LineaAlbaran{" +
                "idLineaAlbaran=" + idLineaAlbaran +
                ", cantidadRecibida=" + cantidadRecibida +
                ", estadoProducto=" + estadoProducto +
                ", lote='" + lote + '\'' +
                ", fechaCaducidad=" + fechaCaducidad +
                ", diferenciaCantidad=" + diferenciaCantidad +
                ", lineasAlbaran=" + lineasAlbaran +
                '}';
    }
}
