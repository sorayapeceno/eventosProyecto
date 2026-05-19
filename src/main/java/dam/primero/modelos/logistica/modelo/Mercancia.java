package dam.primero.modelos.logistica.modelo;

import java.time.LocalDate;

public class Mercancia {

    private int idMercancia;
    private String descripcion;
    private double precioUnitario;
    private String categoria;
    private String unidadMedida;
    private int stockMinimo;
    private int stockActual;
    private LocalDate fechaCreacion;

    public int getIdMercancia() {
        return idMercancia;
    }

    public void setIdMercancia(int idMercancia) {
        this.idMercancia = idMercancia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public boolean necesitaReposicion(){
        boolean necesitaReposicion;
        if (stockActual<=stockMinimo){
            necesitaReposicion=true;
        }
        else{
            necesitaReposicion=false;
        }
        return necesitaReposicion;
    }

    @Override
    public String toString() {
        return "Mercancia{" +
                "idMercancia=" + idMercancia +
                ", descripcion='" + descripcion + '\'' +
                ", precioUnitario=" + precioUnitario +
                ", categoria='" + categoria + '\'' +
                ", unidadMedida='" + unidadMedida + '\'' +
                ", stockMinimo=" + stockMinimo +
                ", stockActual=" + stockActual +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
