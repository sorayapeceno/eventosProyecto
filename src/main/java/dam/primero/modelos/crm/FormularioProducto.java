package dam.primero.modelos.crm;

public class FormularioProducto {
    private String nombre;
    private String descripcion;
    private String precio;
    private String stock;
    private String categoria;

    public FormularioProducto() {
    }

    public FormularioProducto(String nombre, String descripcion, String precio, String stock, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getPrecio() { return precio; }
    public void setPrecio(String precio) { this.precio = precio; }

    public String getStock() { return stock; }
    public void setStock(String stock) { this.stock = stock; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
