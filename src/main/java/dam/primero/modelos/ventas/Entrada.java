package dam.primero.modelos.ventas;

/**
 * Clase abstracta que representa una entrada al evento.
 * Extiende de Producto y añade los datos propios de una entrada:
 * zona, asiento, accesos permitidos, validez en horas y estado.
 * Sus subclases concretas son VIP, Estandar y Beca.
 *
 * @author Alexandru Cozaru
 */
public abstract class Entrada extends Producto {

    private long         id_Entrada;
    private String       zona;
    private String       asiento;
    private String       accesoPermitido;
    private String       accesosAdicionales;
    private int          validezHoras;
    private EstadoEntrada estadoEntrada;

    public Entrada() {}

    public Entrada(long id_Entrada, long id_Producto, String nombreProducto,
                   double precio, int stockDisponible, String descripcionProducto,
                   TipoIVA tipoIVA, double descuento,
                   String zona, String asiento, String accesoPermitido,
                   String accesosAdicionales, int validezHoras, EstadoEntrada estadoEntrada) {
        super(id_Producto, nombreProducto, precio, stockDisponible,
              descripcionProducto, tipoIVA, descuento);
        this.id_Entrada         = id_Entrada;
        this.zona               = zona;
        this.asiento            = asiento;
        this.accesoPermitido    = accesoPermitido;
        this.accesosAdicionales = accesosAdicionales;
        this.validezHoras       = validezHoras;
        this.estadoEntrada      = estadoEntrada;
    }

    public long getId_Entrada() { return id_Entrada; }
    public void setId_Entrada(long id_Entrada) { this.id_Entrada = id_Entrada; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getAsiento() { return asiento; }
    public void setAsiento(String asiento) { this.asiento = asiento; }

    public String getAccesoPermitido() { return accesoPermitido; }
    public void setAccesoPermitido(String accesoPermitido) { this.accesoPermitido = accesoPermitido; }

    public String getAccesosAdicionales() { return accesosAdicionales; }
    public void setAccesosAdicionales(String accesosAdicionales) { this.accesosAdicionales = accesosAdicionales; }

    public int getValidezHoras() { return validezHoras; }
    public void setValidezHoras(int validezHoras) { this.validezHoras = validezHoras; }

    public EstadoEntrada getEstadoEntrada() { return estadoEntrada; }
    public void setEstadoEntrada(EstadoEntrada estadoEntrada) { this.estadoEntrada = estadoEntrada; }
}
