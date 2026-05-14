package dam.primero.modelos.eventos_participantes.Modelo;

import java.time.LocalDate;
import java.util.Objects;

public class Persona {
    private static int contador;
    private int id_Persona;
    private String dni;
    private String username; //usuario
    private String apellido1;
    private String apellido2;
    private String correo;
    private Alergeno alergeno;
    private int telefono;
    private String ciudad;
    private String pais;
    private String foto; //enlace
    private char genero;
    private LocalDate fecha_Nacimiento;
    private String password; // contraseña
    private String direccion;
    private String redes_Sociales;
    private String web; //podemos poner enlace

    public Persona(int id_Persona, String dni, String username, String apellido1, String apellido2, String correo, Alergeno alergeno, int telefono, String ciudad, String pais, String foto, char genero, LocalDate fecha_Nacimiento, String password, String direccion, String redes_Sociales, String web) {
        contador++;
        this.id_Persona = contador;
        this.dni = dni;
        this.username = username;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.correo = correo;
        this.alergeno = alergeno;
        this.telefono = telefono;
        this.ciudad = ciudad;
        this.pais = pais;
        this.foto = foto;
        this.genero = genero;
        this.fecha_Nacimiento = fecha_Nacimiento;
        this.password = password;
        this.direccion = direccion;
        this.redes_Sociales = redes_Sociales;
        this.web = web;
    }

    public int getId_Persona() {
        return id_Persona;
    }

    public void setId_Persona(int id_Persona) {
        this.id_Persona = id_Persona;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Alergeno getAlergeno() {
        return alergeno;
    }

    public void setAlergeno(Alergeno alergeno) {
        this.alergeno = alergeno;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    public LocalDate getFecha_Nacimiento() {
        return fecha_Nacimiento;
    }

    public void setFecha_Nacimiento(LocalDate fecha_Nacimiento) {
        this.fecha_Nacimiento = fecha_Nacimiento;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRedes_Sociales() {
        return redes_Sociales;
    }

    public void setRedes_Sociales(String redes_Sociales) {
        this.redes_Sociales = redes_Sociales;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return id_Persona == persona.id_Persona;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id_Persona);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id_Persona=" + id_Persona +
                ", dni='" + dni + '\'' +
                ", username='" + username + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", correo='" + correo + '\'' +
                ", alergeno=" + alergeno +
                ", telefono=" + telefono +
                ", ciudad='" + ciudad + '\'' +
                ", pais='" + pais + '\'' +
                ", foto='" + foto + '\'' +
                ", genero=" + genero +
                ", fecha_Nacimiento=" + fecha_Nacimiento +
                ", password='" + password + '\'' +
                ", direccion='" + direccion + '\'' +
                ", redes_Sociales='" + redes_Sociales + '\'' +
                ", web='" + web + '\'' +
                '}';
    }
}
