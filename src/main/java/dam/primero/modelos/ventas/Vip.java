package dam.primero.modelos.ventas;

import java.util.List;
import java.util.Objects;

public class Vip extends Entrada{
    //Atributos
    private int vipID;
    private List<String> beneficiosIncluidos;
    private List<String> serviciosAdicionales;
    private double precioExtra;

    //Constructor


    public Vip(String nombreProducto, double precio, int stockDisponible, String descripcion, TipoIVA tipoIva, int descuento, String zona, String asiento, boolean accesoPermitido, int accesoAdicional, int validezHoras, EstadoEntradas estadoEntradas, List<String> beneficiosIncluidos, List<String> serviciosAdicionales, double precioExtra) {
        super(nombreProducto, precio, stockDisponible, descripcion, tipoIva, descuento, zona, asiento, accesoPermitido, accesoAdicional, validezHoras, estadoEntradas);
        this.beneficiosIncluidos = beneficiosIncluidos;
        this.serviciosAdicionales = serviciosAdicionales;
        this.precioExtra = precioExtra;
    }

    //getter y setter
    public int getVipID() {
        return vipID;
    }

    public void setVipID(int vipID) {
        this.vipID = vipID;
    }

    public List<String> getBeneficiosIncluidos() {
        return beneficiosIncluidos;
    }

    public void setBeneficiosIncluidos(List<String> beneficiosIncluidos) {
        this.beneficiosIncluidos = beneficiosIncluidos;
    }

    public List<String> getServiciosAdicionales() {
        return serviciosAdicionales;
    }

    public void setServiciosAdicionales(List<String> serviciosAdicionales) {
        this.serviciosAdicionales = serviciosAdicionales;
    }

    public double getPrecioExtra() {
        return precioExtra;
    }

    public void setPrecioExtra(double precioExtra) {
        this.precioExtra = precioExtra;
    }

    //Metodos
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vip vip = (Vip) o;
        return vipID == vip.vipID;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(vipID);
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
                " | VipID: " + getVipID() +
                " | BeneficiosIncluidos: " + getBeneficiosIncluidos() +
                " | serviciosAdicionales: " + getServiciosAdicionales() +
                " | precioExtra: " + getPrecioExtra() +
                " ]";

    }
}
