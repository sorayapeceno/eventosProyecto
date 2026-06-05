package dam.primero.modelos.institucionales.modelo;

import java.util.Objects;

public class Recinto {
    private static int contador;
    private int idRecinto;
    private String nombre;
    private int capacidad;
    private String ubicacion;

    public Recinto() {
    }

    public Recinto(int idRecinto, String nombre, int capacidad, String ubicacion) {
        contador++;
        this.idRecinto = contador;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Recinto.contador = contador;
    }

    public int getIdRecinto() {
        return idRecinto;
    }

    public void setIdRecinto(int idRecinto) {
        this.idRecinto = idRecinto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Recinto recinto = (Recinto) o;
        return idRecinto == recinto.idRecinto;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idRecinto);
    }

    @Override
    public String toString() {
        return "Recinto{" +
                "idRecinto=" + idRecinto +
                ", nombre='" + nombre + '\'' +
                ", capacidad=" + capacidad +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }
}
