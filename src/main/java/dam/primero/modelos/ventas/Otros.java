package dam.primero.modelos.ventas;

public class Otros extends Producto {

    private long   id_Otros;
    private String descripcion;

    public Otros() {}

    public Otros(long id_Otros, long id_Producto, String nombreProducto,
                 double precio, int stockDisponible, String descripcionProducto,
                 TipoIVA tipoIVA, double descuento, String descripcion) {
        super(id_Producto, nombreProducto, precio, stockDisponible,
              descripcionProducto, tipoIVA, descuento);
        this.id_Otros    = id_Otros;
        this.descripcion = descripcion;
    }

    public long getId_Otros() { return id_Otros; }
    public void setId_Otros(long id_Otros) { this.id_Otros = id_Otros; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public TipoProducto getTipoProducto() { return TipoProducto.OTROS; }

    @Override
    public String toString() {
        return "Otros{id=" + id_Otros + ", descripcion='" + descripcion + "'}";
    }
}
