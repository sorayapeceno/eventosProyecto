package dam.primero.modelos.ventas;

/**
 * Producto textil de tipo Accesorio (gorras, pulseras, etc.).
 * Extiende de Textil y añade el campo tipo de accesorio.
 *
 * @author Alexandru Cozaru
 */
public class Accesorios extends Textil {

    private long   id_Accesorios;
    private String tipoAccesorio;

    public Accesorios() {}

    public Accesorios(long id_Accesorios, long id_Textil, long id_Producto,
                      String nombreProducto, double precio, int stockDisponible,
                      String descripcionProducto, TipoIVA tipoIVA, double descuento,
                      String talla, String color, String material,
                      Genero genero, String tipoTextil,
                      String tipoAccesorio) {
        super(id_Textil, id_Producto, nombreProducto, precio, stockDisponible,
              descripcionProducto, tipoIVA, descuento,
              talla, color, material, genero, tipoTextil);
        this.id_Accesorios = id_Accesorios;
        this.tipoAccesorio = tipoAccesorio;
    }

    public long getId_Accesorios() { return id_Accesorios; }
    public void setId_Accesorios(long id_Accesorios) { this.id_Accesorios = id_Accesorios; }

    public String getTipoAccesorio() { return tipoAccesorio; }
    public void setTipoAccesorio(String tipoAccesorio) { this.tipoAccesorio = tipoAccesorio; }

    @Override
    public TipoProducto getTipoProducto() { return TipoProducto.ACCESORIO; }

    @Override
    public String toString() {
        return "Accesorio{id=" + id_Accesorios + ", tipo='" + tipoAccesorio + "'}";
    }
}
