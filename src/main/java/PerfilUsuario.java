import java.time.LocalDate;
import java.util.Objects;

public class PerfilUsuario {
    private int idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String passwordHash;
    private String rol;
    private LocalDate fechaRegistro;

    public PerfilUsuario(int idUsuario, String nombre, String apellidos, String email, String passwordHash, String rol, LocalDate fechaRegistro) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PerfilUsuario that = (PerfilUsuario) o;
        return idUsuario == that.idUsuario && Objects.equals(nombre, that.nombre) && Objects.equals(apellidos, that.apellidos) && Objects.equals(email, that.email) && Objects.equals(passwordHash, that.passwordHash) && Objects.equals(rol, that.rol) && Objects.equals(fechaRegistro, that.fechaRegistro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, nombre, apellidos, email, passwordHash, rol, fechaRegistro);
    }

    @Override
    public String toString() {
        return "PerfilUsuario{" +
                "idUsuario=" + getIdUsuario() +
                ", nombre='" + getNombre() + '\'' +
                ", apellidos='" + getApellidos() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", passwordHash='" + getPasswordHash() + '\'' +
                ", rol='" + getRol() + '\'' +
                ", fechaRegistro=" + getFechaRegistro() +
                '}';
    }
}
