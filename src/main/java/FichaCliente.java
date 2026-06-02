import java.time.LocalDate;
import java.util.Objects;

public class FichaCliente {
    private int idFicha;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String empresaCentro;
    private String observaciones;
    private LocalDate fechaAlta;

    public FichaCliente(int idFicha, String nombre, String apellidos, String email, String telefono, String empresaCentro, String observaciones, LocalDate fechaAlta) {
        this.idFicha = idFicha;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.empresaCentro = empresaCentro;
        this.observaciones = observaciones;
        this.fechaAlta = fechaAlta;
    }

    public int getIdFicha() {
        return idFicha;
    }

    public void setIdFicha(int idFicha) {
        this.idFicha = idFicha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmpresaCentro() {
        return empresaCentro;
    }

    public void setEmpresaCentro(String empresaCentro) {
        this.empresaCentro = empresaCentro;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FichaCliente that = (FichaCliente) o;
        return idFicha == that.idFicha && Objects.equals(nombre, that.nombre) && Objects.equals(apellidos, that.apellidos) && Objects.equals(email, that.email) && Objects.equals(telefono, that.telefono) && Objects.equals(empresaCentro, that.empresaCentro) && Objects.equals(observaciones, that.observaciones) && Objects.equals(fechaAlta, that.fechaAlta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFicha, nombre, apellidos, email, telefono, empresaCentro, observaciones, fechaAlta);
    }

    @Override
    public String toString() {
        return "FichaCliente{" +
                "idFicha=" + getIdFicha() +
                ", nombre='" + getNombre() + '\'' +
                ", apellidos='" + getApellidos() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", telefono='" + getTelefono() + '\'' +
                ", empresaCentro='" + getEmpresaCentro() + '\'' +
                ", observaciones='" + getObservaciones() + '\'' +
                ", fechaAlta=" + getFechaAlta() +
                '}';
    }
}
