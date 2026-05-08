package dam.primero.modelos.ventas;

import java.util.Objects;

public abstract class Entrada extends Producto {
    //Atributos
    private int entradaID;
    private String zona;
    private String asiento;
    private boolean accesoPermitido;
    private int accesoAdicional;
    private int validezHoras;
    private EstadoEntradas estadoEntradas;

    //Contructor


    public Entrada(String nombreProducto, double precio, int stockDisponible, String descripcion, TipoIVA tipoIva, int descuento, String zona, String asiento, boolean accesoPermitido, int accesoAdicional, int validezHoras, EstadoEntradas estadoEntradas) {
        super(nombreProducto, precio, stockDisponible, descripcion, tipoIva, descuento);
        this.zona = zona;
        this.asiento = asiento;
        this.accesoPermitido = accesoPermitido;
        this.accesoAdicional = accesoAdicional;
        this.validezHoras = validezHoras;
        this.estadoEntradas = estadoEntradas;
    }

    //getter y setter
    public int getEntradaID() {
        return entradaID;
    }

    public void setEntradaID(int entradaID) {
        this.entradaID = entradaID;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public boolean isAccesoPermitido() {
        return accesoPermitido;
    }

    public void setAccesoPermitido(boolean accesoPermitido) {
        this.accesoPermitido = accesoPermitido;
    }

    public int getAccesoAdicional() {
        return accesoAdicional;
    }

    public void setAccesoAdicional(int accesoAdicional) {
        this.accesoAdicional = accesoAdicional;
    }

    public int getValidezHoras() {
        return validezHoras;
    }

    public void setValidezHoras(int validezHoras) {
        this.validezHoras = validezHoras;
    }

    public EstadoEntradas getEstadoEntradas() {
        return estadoEntradas;
    }

    public void setEstadoEntradas(EstadoEntradas estadoEntradas) {
        this.estadoEntradas = estadoEntradas;
    }

    //Metodos
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entrada entrada = (Entrada) o;
        return entradaID == entrada.entradaID;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entradaID);
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
                " ]";
    }
}
