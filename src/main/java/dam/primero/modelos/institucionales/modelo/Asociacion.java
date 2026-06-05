package dam.primero.modelos.institucionales.modelo;

import java.time.LocalDate;

public class Asociacion extends Organizacion {
    private static int contador;
    private int idAsociacion;
    private String finalidad;
    private int numSocios;

    public Asociacion() {
    }

    public Asociacion(int idOrganizacion, String nombre, String direccion, int telefono, String email, String ciudad, LocalDate fechaRegistro, int idAsociacion, String finalidad, int numSocios) {
        super(idOrganizacion, nombre, direccion, telefono, email, ciudad, fechaRegistro);
        contador++;
        this.idAsociacion = contador;
        this.finalidad = finalidad;
        this.numSocios = numSocios;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Asociacion.contador = contador;
    }

    public int getIdAsociacion() {
        return idAsociacion;
    }

    public void setIdAsociacion(int idAsociacion) {
        this.idAsociacion = idAsociacion;
    }

    public String getFinalidad() {
        return finalidad;
    }

    public void setFinalidad(String finalidad) {
        this.finalidad = finalidad;
    }

    public int getNumSocios() {
        return numSocios;
    }

    public void setNumSocios(int numSocios) {
        this.numSocios = numSocios;
    }

    @Override
    public String toString() {
        return "Asociacion{" +
                "idAsociacion=" + idAsociacion +
                ", finalidad='" + finalidad + '\'' +
                ", numSocios=" + numSocios +
                '}';
    }
}
