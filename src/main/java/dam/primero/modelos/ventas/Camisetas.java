package dam.primero.modelos.ventas;

/**
 * Producto textil de tipo Camiseta.
 * Extiende de Textil y añade modelo y estampado específicos de la camiseta.
 *
 * @author Alexandru Cozaru
 */
public class Camisetas extends Textil {

    private long   id_Camisetas;
    private String modelo;
    private String estampado;

    public Camisetas() {}

    public Camisetas(long id_Camisetas, long id_Textil, long id_Producto,
                     String nombreProducto, double precio, int stockDisponible,
                     String descripcionProducto, TipoIVA tipoIVA, double descuento,
                     String talla, String color, String material,
                     Genero genero, String tipoTextil,
                     String modelo, String estampado) {
        super(id_Textil, id_Producto, nombreProducto, precio, stockDisponible,
              descripcionProducto, tipoIVA, descuento,
              talla, color, material, genero, tipoTextil);
        this.id_Camisetas = id_Camisetas;
        this.modelo       = modelo;
        this.estampado    = estampado;
    }

    public long getId_Camisetas() { return id_Camisetas; }
    public void setId_Camisetas(long id_Camisetas) { this.id_Camisetas = id_Camisetas; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getEstampado() { return estampado; }
    public void setEstampado(String estampado) { this.estampado = estampado; }

    @Override
    public TipoProducto getTipoProducto() { return TipoProducto.CAMISETA; }

    @Override
    public String toString() {
        return "Camiseta{id=" + id_Camisetas + ", modelo='" + modelo +
               "', estampado='" + estampado + "', talla=" + getTalla() + '}';
    }
}
