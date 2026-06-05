package dam.primero.modelos.ventas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un ticket de compra generado en el evento.
 * Agrupa las líneas de compra de un asistente, el método de pago,
 * un posible código promocional y el descuento aplicado.
 * El precio final se calcula sumando los totales de todas las líneas
 * y restando el descuento del ticket.
 *
 * @author Sergio Galan
 */
public class Ticket {

    private long        id_Ticket;
    private Asistente   duenio;
    private String      codigoQR;
    private LocalDate   fechaCompra;
    private MetodoPago  metodoPago;
    private double      descuento;
    private String      codPromocional;
    private List<LineaTicket> lineas = new ArrayList<>();

    public Ticket() {}

    public Ticket(long id_Ticket, Asistente duenio, String codigoQR,
                  LocalDate fechaCompra, MetodoPago metodoPago,
                  double descuento, String codPromocional) {
        this.id_Ticket      = id_Ticket;
        this.duenio         = duenio;
        this.codigoQR       = codigoQR;
        this.fechaCompra    = fechaCompra;
        this.metodoPago     = metodoPago;
        this.descuento      = descuento;
        this.codPromocional = codPromocional;
    }

    public double getPrecioFinal() {
        double suma = lineas.stream()
                            .mapToDouble(LineaTicket::getTotalLinea)
                            .sum();
        return suma - descuento;
    }

    public long getId_Ticket() { return id_Ticket; }
    public void setId_Ticket(long id_Ticket) { this.id_Ticket = id_Ticket; }

    public Asistente getDuenio() { return duenio; }
    public void setDuenio(Asistente duenio) { this.duenio = duenio; }

    public String getCodigoQR() { return codigoQR; }
    public void setCodigoQR(String codigoQR) { this.codigoQR = codigoQR; }

    public LocalDate getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDate fechaCompra) { this.fechaCompra = fechaCompra; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public String getCodPromocional() { return codPromocional; }
    public void setCodPromocional(String codPromocional) { this.codPromocional = codPromocional; }

    public List<LineaTicket> getLineas() { return lineas; }
    public void setLineas(List<LineaTicket> lineas) { this.lineas = lineas; }

    public void addLinea(LineaTicket linea) {
        this.lineas.add(linea);
        linea.setId_Ticket(this.id_Ticket);
    }

    @Override
    public String toString() {
        return "Ticket{id=" + id_Ticket +
               ", asistente=" + (duenio != null ? duenio.getId_Asistente() : "null") +
               ", fecha=" + fechaCompra +
               ", total=" + getPrecioFinal() + '}';
    }
}
