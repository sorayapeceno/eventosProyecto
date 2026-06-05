package dam.primero.modelos.institucionales.modelo;

import java.time.LocalDate;
import java.util.Objects;

public class Colaboracion {
    private static int contador;
    private int idColaboracion;
    private HistorialOportunidad idHistorial;
    private String tipo;
    private LocalDate Fecha;
    private Boolean firma;
    private int dinero;
    private String convenio;

    public Colaboracion() {
    }

    public Colaboracion(int idColaboracion, HistorialOportunidad idHistorial, String tipo, LocalDate fecha, Boolean firma, int dinero, String convenio) {
        this.idColaboracion = idColaboracion;
        this.idHistorial = idHistorial;
        this.tipo = tipo;
        Fecha = fecha;
        this.firma = firma;
        this.dinero = dinero;
        this.convenio = convenio;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Colaboracion.contador = contador;
    }

    public int getIdColaboracion() {
        return idColaboracion;
    }

    public void setIdColaboracion(int idColaboracion) {
        this.idColaboracion = idColaboracion;
    }

    public HistorialOportunidad getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(HistorialOportunidad idHistorial) {
        this.idHistorial = idHistorial;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return Fecha;
    }

    public void setFecha(LocalDate fecha) {
        Fecha = fecha;
    }

    public Boolean getFirma() {
        return firma;
    }

    public void setFirma(Boolean firma) {
        this.firma = firma;
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Colaboracion that = (Colaboracion) o;
        return idColaboracion == that.idColaboracion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idColaboracion);
    }

    @Override
    public String toString() {
        return "Colaboracion{" +
                "idColaboracion=" + idColaboracion +
                ", idHistorial=" + idHistorial +
                ", tipo='" + tipo + '\'' +
                ", Fecha=" + Fecha +
                ", firma=" + firma +
                ", dinero=" + dinero +
                ", convenio='" + convenio + '\'' +
                '}';
    }
}
