package dam.primero.modelos.ventas;

public class VIP extends Entrada {

    private long   id_Vip;
    private String beneficiosIncluidos;
    private String serviciosAdicionales;
    private double precioExtra;

    public VIP() {}

    public VIP(long id_Vip, long id_Entrada, long id_Producto,
               String nombreProducto, double precio, int stockDisponible,
               String descripcionProducto, TipoIVA tipoIVA, double descuento,
               String zona, String asiento, String accesoPermitido,
               String accesosAdicionales, int validezHoras, EstadoEntrada estadoEntrada,
               String beneficiosIncluidos, String serviciosAdicionales, double precioExtra) {
        super(id_Entrada, id_Producto, nombreProducto, precio, stockDisponible,
              descripcionProducto, tipoIVA, descuento,
              zona, asiento, accesoPermitido, accesosAdicionales, validezHoras, estadoEntrada);
        this.id_Vip               = id_Vip;
        this.beneficiosIncluidos  = beneficiosIncluidos;
        this.serviciosAdicionales = serviciosAdicionales;
        this.precioExtra          = precioExtra;
    }

    public long getId_Vip() { return id_Vip; }
    public void setId_Vip(long id_Vip) { this.id_Vip = id_Vip; }

    public String getBeneficiosIncluidos() { return beneficiosIncluidos; }
    public void setBeneficiosIncluidos(String beneficiosIncluidos) { this.beneficiosIncluidos = beneficiosIncluidos; }

    public String getServiciosAdicionales() { return serviciosAdicionales; }
    public void setServiciosAdicionales(String serviciosAdicionales) { this.serviciosAdicionales = serviciosAdicionales; }

    public double getPrecioExtra() { return precioExtra; }
    public void setPrecioExtra(double precioExtra) { this.precioExtra = precioExtra; }

    @Override
    public TipoProducto getTipoProducto() { return TipoProducto.VIP; }

    @Override
    public String toString() {
        return "VIP{id=" + id_Vip + ", beneficios='" + beneficiosIncluidos + "', precioExtra=" + precioExtra + '}';
    }
}
