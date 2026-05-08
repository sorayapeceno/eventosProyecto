package dam.primero.modelos.ventas;

import java.util.Objects;

public class Estandar extends Entrada{
    //Atributos
    private int estandarID;
    private boolean incluyeRegalo;

    //Contructor
    public Estandar(String nombreProducto, double precio, int stockDisponible, String descripcion, TipoIVA tipoIva, int descuento, String zona, String asiento, boolean accesoPermitido, int accesoAdicional, int validezHoras, EstadoEntradas estadoEntradas, boolean incluyeRegalo) {
        super(nombreProducto, precio, stockDisponible, descripcion, tipoIva, descuento, zona, asiento, accesoPermitido, accesoAdicional, validezHoras, estadoEntradas);
        this.incluyeRegalo = incluyeRegalo;
    }

    //getter y setter
    public int getEstandarID() {
        return estandarID;
    }

    public void setEstandarID(int estandarID) {
        this.estandarID = estandarID;
    }

    public boolean isIncluyeRegalo() {
        return incluyeRegalo;
    }

    public void setIncluyeRegalo(boolean incluyeRegalo) {
        this.incluyeRegalo = incluyeRegalo;
    }

    //Metodos
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Estandar estandar = (Estandar) o;
        return estandarID == estandar.estandarID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), estandarID);
    }

    @Override
    public String toString() {
        return  " [ ProductoID: " + getProductoID() +
                " | NombreProducto: " + getNombreProducto() +
                " | Precio: " + getPrecio() +
                " | StockDisponible: " + getStockDisponible() +
                " | Descripción: " + getDescripcion() +
                " | TipoIva: " + getTipoIva() +
                " | Descuento: " + getDescuento() +
                " | EntradaID: " + getEntradaID() +
                " | Zona: " + getZona() +
                " | Asiento: " + getAsiento() +
                " | AccesoPermitido:" + isAccesoPermitido() +
                " | AccesoAdicional:" + getAccesoAdicional() +
                " | ValidezHoras: " + getValidezHoras() +
                " | EstadoEntradas: " + getEstadoEntradas() +
                " | EstandarID: " + getEstandarID() +
                " | IncluyeRegalo=" + isIncluyeRegalo() +
                " ]";
    }
}
