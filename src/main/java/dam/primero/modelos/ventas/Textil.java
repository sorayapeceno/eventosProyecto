package dam.primero.modelos.ventas;

/**
 * Clase abstracta que representa un producto textil del evento.
 * Extiende de Producto y define los atributos comunes a toda la ropa:
 * talla, color, material, género y tipo textil.
 * Sus subclases concretas son Camisetas y Accesorios.
 *
 * @author Alexandru Cozaru
 */
public abstract class Textil extends Producto {

    private long   id_Textil;
    private String talla;
    private String color;
    private String material;
    private Genero genero;
    private String tipoTextil;

    public Textil() {}

    public Textil(long id_Textil, long id_Producto, String nombreProducto,
                  double precio, int stockDisponible, String descripcionProducto,
                  TipoIVA tipoIVA, double descuento,
                  String talla, String color, String material,
                  Genero genero, String tipoTextil) {
        super(id_Producto, nombreProducto, precio, stockDisponible,
              descripcionProducto, tipoIVA, descuento);
        this.id_Textil  = id_Textil;
        this.talla      = talla;
        this.color      = color;
        this.material   = material;
        this.genero     = genero;
        this.tipoTextil = tipoTextil;
    }

    public long getId_Textil() { return id_Textil; }
    public void setId_Textil(long id_Textil) { this.id_Textil = id_Textil; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }

    public String getTipoTextil() { return tipoTextil; }
    public void setTipoTextil(String tipoTextil) { this.tipoTextil = tipoTextil; }
}
