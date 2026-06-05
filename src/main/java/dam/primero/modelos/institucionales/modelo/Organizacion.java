package dam.primero.modelos.institucionales.modelo;

import java.time.LocalDate;
import java.util.Objects;

public class Organizacion {
    private static int contador;
    private int idOrganizacion;
    private HistorialOportunidad idHistorial;
    private String nombre;
    private String direccion;
    private int telefono;
    private String email;
    private String ciudad;
    private LocalDate FechaRegistro;

    public Organizacion() {
    }

    public Organizacion(int idOrganizacion, HistorialOportunidad idHistorial, String nombre, String direccion, int telefono, String email, String ciudad, LocalDate fechaRegistro) {
        contador++;
        this.idOrganizacion = contador;
        this.idHistorial = idHistorial;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.ciudad = ciudad;
        FechaRegistro = fechaRegistro;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Organizacion.contador = contador;
    }

    public int getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(int idOrganizacion) {
        this.idOrganizacion = idOrganizacion;
    }

    public HistorialOportunidad getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(HistorialOportunidad idHistorial) {
        this.idHistorial = idHistorial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public LocalDate getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        FechaRegistro = fechaRegistro;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Organizacion that = (Organizacion) o;
        return idOrganizacion == that.idOrganizacion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idOrganizacion);
    }

    @Override
    public String toString() {
        return "Organizacion{" +
                "idOrganizacion=" + idOrganizacion +
                ", idHistorial=" + idHistorial +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono=" + telefono +
                ", email='" + email + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", FechaRegistro=" + FechaRegistro +
                '}';
    }
}
