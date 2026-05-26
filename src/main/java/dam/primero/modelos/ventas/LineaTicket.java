package dam.primero.modelos.ventas;

public class LineaTicket {

    private long    id_LineaTicket;
    private long    id_Ticket;
    private Producto producto;
    private int     cantidad;
    private double  ivaCuota;
    private double  subtotalBase;
    private double  totalLinea;

    public LineaTicket() {}

    public LineaTicket(long id_LineaTicket, long id_Ticket, Producto producto,
                       int cantidad, double ivaCuota,
                       double subtotalBase, double totalLinea) {
        this.id_LineaTicket = id_LineaTicket;
        this.id_Ticket      = id_Ticket;
        this.producto       = producto;
        this.cantidad       = cantidad;
        this.ivaCuota       = ivaCuota;
        this.subtotalBase   = subtotalBase;
        this.totalLinea     = totalLinea;
    }

    public void calcularTotales() {
        if (producto == null || cantidad <= 0) return;

        double precioUnitario = producto.getPrecioConDescuento();
        double tipoIVAPct     = producto.getTipoIVA() != null
                                ? producto.getTipoIVA().getPorcentaje() / 100.0
                                : 0.21;

        this.subtotalBase = precioUnitario * cantidad / (1 + tipoIVAPct);
        this.ivaCuota     = precioUnitario * cantidad - this.subtotalBase;
        this.totalLinea   = precioUnitario * cantidad;
    }

    public long getId_LineaTicket() { return id_LineaTicket; }
    public void setId_LineaTicket(long id_LineaTicket) { this.id_LineaTicket = id_LineaTicket; }

    public long getId_Ticket() { return id_Ticket; }
    public void setId_Ticket(long id_Ticket) { this.id_Ticket = id_Ticket; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getIvaCuota() { return ivaCuota; }
    public void setIvaCuota(double ivaCuota) { this.ivaCuota = ivaCuota; }

    public double getSubtotalBase() { return subtotalBase; }
    public void setSubtotalBase(double subtotalBase) { this.subtotalBase = subtotalBase; }

    public double getTotalLinea() { return totalLinea; }
    public void setTotalLinea(double totalLinea) { this.totalLinea = totalLinea; }

    @Override
    public String toString() {
        return "LineaTicket{id=" + id_LineaTicket +
               ", producto=" + (producto != null ? producto.getNombreProducto() : "null") +
               ", cantidad=" + cantidad +
               ", total=" + totalLinea + '}';
    }
}
