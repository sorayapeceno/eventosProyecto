package dam.primero.modelos.ventas;

public class Beca extends Entrada {

    private long   id_Beca;
    private String motivoBeca;
    private double porcentajeDescuento;
    private String requisitos;

    public Beca() {}

    public Beca(long id_Beca, long id_Entrada, long id_Producto,
                String nombreProducto, double precio, int stockDisponible,
                String descripcionProducto, TipoIVA tipoIVA, double descuento,
                String zona, String asiento, String accesoPermitido,
                String accesosAdicionales, int validezHoras, EstadoEntrada estadoEntrada,
                String motivoBeca, double porcentajeDescuento, String requisitos) {
        super(id_Entrada, id_Producto, nombreProducto, precio, stockDisponible,
              descripcionProducto, tipoIVA, descuento,
              zona, asiento, accesoPermitido, accesosAdicionales, validezHoras, estadoEntrada);
        this.id_Beca             = id_Beca;
        this.motivoBeca          = motivoBeca;
        this.porcentajeDescuento = porcentajeDescuento;
        this.requisitos          = requisitos;
    }

    public long getId_Beca() { return id_Beca; }
    public void setId_Beca(long id_Beca) { this.id_Beca = id_Beca; }

    public String getMotivoBeca() { return motivoBeca; }
    public void setMotivoBeca(String motivoBeca) { this.motivoBeca = motivoBeca; }

    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }

    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }

    @Override
    public TipoProducto getTipoProducto() { return TipoProducto.BECA; }

    public double getPrecioConBeca() {
        return getPrecio() * (1.0 - (porcentajeDescuento / 100.0));
    }

    @Override
    public String toString() {
        return "Beca{id=" + id_Beca + ", motivo='" + motivoBeca +
               "', descuento=" + porcentajeDescuento + "%, requisitos='" + requisitos + "'}";
    }
}
