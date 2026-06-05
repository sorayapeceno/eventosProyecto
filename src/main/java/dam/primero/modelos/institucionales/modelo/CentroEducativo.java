package dam.primero.modelos.institucionales.modelo;

import java.time.LocalDate;

public class CentroEducativo extends Organizacion{
    private static int contador;
    private int idCentroEducativo;
    private String tipoCentro;
    private int numAlumnos;


    public CentroEducativo() {
    }

    public CentroEducativo(int idOrganizacion, String nombre, String direccion, int telefono, String email, String ciudad, LocalDate fechaRegistro, int idCentroEducativo, String tipoCentro, int numAlumnos) {
        super(idOrganizacion, nombre, direccion, telefono, email, ciudad, fechaRegistro);
        contador++;
        this.idCentroEducativo = contador;
        this.tipoCentro = tipoCentro;
        this.numAlumnos = numAlumnos;
    }

    public CentroEducativo(int idOrganizacion, String nombre, String direccion, int telefono, String email, String ciudad, LocalDate fechaRegistro, String tipoCentro, int numAlumnos) {
        super(idOrganizacion, nombre, direccion, telefono, email, ciudad, fechaRegistro);
        this.tipoCentro = tipoCentro;
        this.numAlumnos = numAlumnos;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        CentroEducativo.contador = contador;
    }

    public int getIdCentroEducativo() {
        return idCentroEducativo;
    }

    public void setIdCentroEducativo(int idCentroEducativo) {
        this.idCentroEducativo = idCentroEducativo;
    }

    public String getTipoCentro() {
        return tipoCentro;
    }

    public void setTipoCentro(String tipoCentro) {
        this.tipoCentro = tipoCentro;
    }

    public int getNumAlumnos() {
        return numAlumnos;
    }

    public void setNumAlumnos(int numAlumnos) {
        this.numAlumnos = numAlumnos;
    }

    @Override
    public String toString() {
        return "CentroEducativo{" +
                "idCentroEducativo=" + idCentroEducativo +
                ", tipoCentro='" + tipoCentro + '\'' +
                ", numAlumnos=" + numAlumnos +
                '}';
    }
}
