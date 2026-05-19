package dam.primero.modelos.logistica.modelo;

import dam.primero.modelos.eventos_participantes.Modelo.Estado;

import java.time.LocalDate;
import java.util.List;

public class Pedido {

    private int idPedido;
    private LocalDate fechaPedido;
    private LocalDate fechaEntregaPrevista;
    private EstadoPedido estadoPedido;
    private List<LineaPedido> lineasPedidos;

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDate getFechaEntregaPrevista() {
        return fechaEntregaPrevista;
    }

    public void setFechaEntregaPrevista(LocalDate fechaEntregaPrevista) {
        this.fechaEntregaPrevista = fechaEntregaPrevista;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public List<LineaPedido> getLineasPedidos() {
        return lineasPedidos;
    }

    public void setLineasPedidos(List<LineaPedido> lineasPedidos) {
        this.lineasPedidos = lineasPedidos;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", fechaPedido=" + fechaPedido +
                ", fechaEntregaPrevista=" + fechaEntregaPrevista +
                ", estadoPedido=" + estadoPedido +
                ", lineasPedidos=" + lineasPedidos +
                '}';
    }

    //TODO public double calcularTotal(){

    //}
}
