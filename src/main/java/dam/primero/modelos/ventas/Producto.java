package dam.primero.modelos.ventas;

/**
 * Clase base abstracta para todos los productos del módulo de ventas.
 * Define los atributos comunes: nombre, precio, stock, descripción, IVA y descuento.
 * Las subclases deben implementar getTipoProducto() para identificar su categoría.
 * El método getPrecioConDescuento() devuelve el precio ya aplicado el descuento.
 *
 * @author Alexandru Cozaru
 */
public abstract class Producto {

    private long   id_Producto;
    private String nombreProducto;
    private double precio;
    private int    stockDisponible;
    private String descripcionProducto;
    private TipoIVA tipoIVA;
    private double descuento;

    public Producto() {}

    public Producto(long id_Producto, String nombreProducto, double precio,
                    int stockDisponible, String descripcionProducto,
                    TipoIVA tipoIVA, double descuento) {
        this.id_Producto          = id_Producto;
        this.nombreProducto       = nombreProducto;
        this.precio               = precio;
        this.stockDisponible      = stockDisponible;
        this.descripcionProducto  = descripcionProducto;
        this.tipoIVA              = tipoIVA;
        this.descuento            = descuento;
    }

    public long getId_Producto() { return id_Producto; }
    public void setId_Producto(long id_Producto) { this.id_Producto = id_Producto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(int stockDisponible) { this.stockDisponible = stockDisponible; }

    public String getDescripcionProducto() { return descripcionProducto; }
    public void setDescripcionProducto(String descripcionProducto) { this.descripcionProducto = descripcionProducto; }

    public TipoIVA getTipoIVA() { return tipoIVA; }
    public void setTipoIVA(TipoIVA tipoIVA) { this.tipoIVA = tipoIVA; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public abstract TipoProducto getTipoProducto();

    public double getPrecioConDescuento() {
        return precio - descuento;
    }

    @Override
    public String toString() {
        return "Producto{id=" + id_Producto +
               ", nombre='" + nombreProducto + '\'' +
               ", precio=" + precio +
               ", stock=" + stockDisponible + '}';
    }
}
