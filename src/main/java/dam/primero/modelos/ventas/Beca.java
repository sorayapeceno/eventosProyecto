package dam.primero.modelos.ventas;

import java.util.List;
import java.util.Objects;

public class Beca extends Entrada {
    //Atributos
    private int becaID;
    private String motivoBeca;
    private int porcentajeBeca;
    private List<String> Requisitos;

    //Constructor
    public Beca(String nombreProducto, double precio, int stockDisponible, String descripcion, TipoIVA tipoIva, int descuento, String zona, String asiento, boolean accesoPermitido, int accesoAdicional, int validezHoras, EstadoEntradas estadoEntradas, String motivoBeca, int porcentajeBeca, List<String> requisitos) {
        super(nombreProducto, precio, stockDisponible, descripcion, tipoIva, descuento, zona, asiento, accesoPermitido, accesoAdicional, validezHoras, estadoEntradas);
        this.motivoBeca = motivoBeca;
        this.porcentajeBeca = porcentajeBeca;
        Requisitos = requisitos;
    }

    //getter y setter
    public int getBecaID() {
        return becaID;
    }

    public void setBecaID(int becaID) {
        this.becaID = becaID;
    }

    public String getMotivoBeca() {
        return motivoBeca;
    }

    public void setMotivoBeca(String motivoBeca) {
        this.motivoBeca = motivoBeca;
    }

    public int getPorcentajeBeca() {
        return porcentajeBeca;
    }

    public void setPorcentajeBeca(int porcentajeBeca) {
        this.porcentajeBeca = porcentajeBeca;
    }

    public List<String> getRequisitos() {
        return Requisitos;
    }

    public void setRequisitos(List<String> requisitos) {
        Requisitos = requisitos;
    }

    //Metodos
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Beca beca = (Beca) o;
        return becaID == beca.becaID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), becaID);
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
                " | BecaID: " + getBecaID() +
                " | MotivoBeca: " + getMotivoBeca() +
                " | PorcentajeBeca: " + getPorcentajeBeca() +
                " | Requisitos: " + getRequisitos() +
                " | requisitos: " + getRequisitos() +
                " ]";
    }
}
