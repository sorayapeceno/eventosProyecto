package dam.primero.modelos.logistica.modelo;

import java.time.LocalDate;

public class Albaran {

    private int idAlbaran;
    private LocalDate fechaAlbaran;
    private EstadoPedido estadopedido;
    private String numeroFactura;
    private String transportista;
    private LocalDate fechaRecepcion;

    public int getIdAlbaran() {
        return idAlbaran;
    }

    public void setIdAlbaran(int idAlbaran) {
        this.idAlbaran = idAlbaran;
    }

    public LocalDate getFechaAlbaran() {
        return fechaAlbaran;
    }

    public void setFechaAlbaran(LocalDate fechaAlbaran) {
        this.fechaAlbaran = fechaAlbaran;
    }

    public EstadoPedido getEstadopedido() {
        return estadopedido;
    }

    public void setEstadopedido(EstadoPedido estadopedido) {
        this.estadopedido = estadopedido;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getTransportista() {
        return transportista;
    }

    public void setTransportista(String transportista) {
        this.transportista = transportista;
    }

    public LocalDate getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDate fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    @Override
    public String toString() {
        return "Albaran{" +
                "idAlbaran=" + idAlbaran +
                ", fechaAlbaran=" + fechaAlbaran +
                ", estadopedido=" + estadopedido +
                ", numeroFactura='" + numeroFactura + '\'' +
                ", transportista='" + transportista + '\'' +
                ", fechaRecepcion=" + fechaRecepcion +
                '}';
    }
}
