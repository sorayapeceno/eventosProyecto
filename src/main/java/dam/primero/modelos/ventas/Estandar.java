package dam.primero.modelos.ventas;

/**
 * Entrada de tipo Estándar. Extiende de Entrada y añade un campo
 * para indicar si la entrada incluye algún regalo para el asistente.
 *
 * @author Alexandru Cozaru
 */
public class Estandar extends Entrada {

    private long   id_Estandar;
    private String incluyeRegalo;

    public Estandar() {}

    public Estandar(long id_Estandar, long id_Entrada, long id_Producto,
                    String nombreProducto, double precio, int stockDisponible,
                    String descripcionProducto, TipoIVA tipoIVA, double descuento,
                    String zona, String asiento, String accesoPermitido,
                    String accesosAdicionales, int validezHoras, EstadoEntrada estadoEntrada,
                    String incluyeRegalo) {
        super(id_Entrada, id_Producto, nombreProducto, precio, stockDisponible,
              descripcionProducto, tipoIVA, descuento,
              zona, asiento, accesoPermitido, accesosAdicionales, validezHoras, estadoEntrada);
        this.id_Estandar  = id_Estandar;
        this.incluyeRegalo = incluyeRegalo;
    }

    public long getId_Estandar() { return id_Estandar; }
    public void setId_Estandar(long id_Estandar) { this.id_Estandar = id_Estandar; }

    public String getIncluyeRegalo() { return incluyeRegalo; }
    public void setIncluyeRegalo(String incluyeRegalo) { this.incluyeRegalo = incluyeRegalo; }

    @Override
    public TipoProducto getTipoProducto() { return TipoProducto.ESTANDAR; }

    @Override
    public String toString() {
        return "Estandar{id=" + id_Estandar + ", regalo='" + incluyeRegalo + "'}";
    }
}
