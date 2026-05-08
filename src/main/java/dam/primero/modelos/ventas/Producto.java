package dam.primero.modelos.ventas;

import java.util.Objects;

public abstract class Producto {
    //Atributos
    private int productoID;
    private String nombreProducto;
    private double precio;
    private int stockDisponible;
    private String descripcion;
    private TipoIVA tipoIva;
    private int descuento;

    //Constructor
    public Producto(String nombreProducto, double precio, int stockDisponible, String descripcion, TipoIVA tipoIva, int descuento) {
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.stockDisponible = stockDisponible;
        this.descripcion = descripcion;
        this.tipoIva = tipoIva;
        this.descuento = descuento;
    }

    //Guetter y setter
    public int getProductoID() {
        return productoID;
    }

    public void setProductoID(int productoID) {
        this.productoID = productoID;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoIVA getTipoIva() {
        return tipoIva;
    }

    public void setTipoIva(TipoIVA tipoIva) {
        this.tipoIva = tipoIva;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    //Metodos
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return productoID == producto.productoID;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productoID);
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
                " ]";
    }
}
