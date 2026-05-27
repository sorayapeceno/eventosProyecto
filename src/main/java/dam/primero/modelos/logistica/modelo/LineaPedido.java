package dam.primero.modelos.logistica.modelo;

public class LineaPedido {
    private int idLineaPedido;
    private int cantidad;
    private double precioUnitarioEnPedido;
    private double subtotal;
    private double descuentoAplicado;

    public int getIdLineaPedido() {
        return idLineaPedido;
    }

    public void setIdLineaPedido(int idLineaPedido) {
        this.idLineaPedido = idLineaPedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitarioEnPedido() {
        return precioUnitarioEnPedido;
    }

    public void setPrecioUnitarioEnPedido(double precioUnitarioEnPedido) {
        this.precioUnitarioEnPedido = precioUnitarioEnPedido;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    @Override
    public String toString() {
        return "LineaPedido{" +
                "idLineaPedido=" + idLineaPedido +
                ", cantidad=" + cantidad +
                ", precioUnitarioEnPedido=" + precioUnitarioEnPedido +
                ", subtotal=" + subtotal +
                ", descuentoAplicado=" + descuentoAplicado +
                '}';
    }

    public double calcularSubtotal() {
        double totalSinDescuento = this.cantidad * this.precioUnitarioEnPedido;
        this.subtotal = totalSinDescuento - (totalSinDescuento * (this.descuentoAplicado / 100.0));
        return this.subtotal;
    }
    
}
