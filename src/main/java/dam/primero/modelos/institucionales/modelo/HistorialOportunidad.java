package dam.primero.modelos.institucionales.modelo;

import java.time.LocalDate;
import java.util.Objects;

public class HistorialOportunidad {
    private static int contador;
    private int idHistorial;
    private Organizacion idOrganizacion;
    private Oportunidad idOportunidad;
    private Recinto idRecinto;
    private String iteraciones;
    private LocalDate fechaInicio;
    private LocalDate fechaFin; //no lo pongo en el constructor porque puede que no se haya realizado todavia

    public HistorialOportunidad() {
    }

    public HistorialOportunidad(int idHistorial, Organizacion idOrganizacion, Oportunidad idOportunidad, Recinto idRecinto, String iteraciones, LocalDate fechaInicio, LocalDate fechaFin) {
        contador++;
        this.idHistorial = contador;
        this.idOrganizacion = idOrganizacion;
        this.idOportunidad = idOportunidad;
        this.idRecinto = idRecinto;
        this.iteraciones = iteraciones;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        HistorialOportunidad.contador = contador;
    }

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Organizacion getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(Organizacion idOrganizacion) {
        this.idOrganizacion = idOrganizacion;
    }

    public Oportunidad getIdOportunidad() {
        return idOportunidad;
    }

    public void setIdOportunidad(Oportunidad idOportunidad) {
        this.idOportunidad = idOportunidad;
    }

    public Recinto getIdRecinto() {
        return idRecinto;
    }

    public void setIdRecinto(Recinto idRecinto) {
        this.idRecinto = idRecinto;
    }

    public String getIteraciones() {
        return iteraciones;
    }

    public void setIteraciones(String iteraciones) {
        this.iteraciones = iteraciones;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HistorialOportunidad that = (HistorialOportunidad) o;
        return idHistorial == that.idHistorial;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idHistorial);
    }

    @Override
    public String toString() {
        return "HistorialOportunidad{" +
                "idHistorial=" + idHistorial +
                ", idOrganizacion=" + idOrganizacion +
                ", idOportunidad=" + idOportunidad +
                ", idRecinto=" + idRecinto +
                ", iteraciones='" + iteraciones + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                '}';
    }
}
