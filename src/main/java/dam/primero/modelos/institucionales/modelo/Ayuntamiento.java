package dam.primero.modelos.institucionales.modelo;

import java.time.LocalDate;

public class Ayuntamiento extends  Organizacion{
    private static int contador;
    private int idAyuntamiento;
    private String provincia;
    private String alcalde;

    public Ayuntamiento() {
    }

    public Ayuntamiento(int idOrganizacion, HistorialOportunidad idHistorial, String nombre, String direccion, int telefono, String email, String ciudad, LocalDate fechaRegistro, int idAyuntamiento, String provincia, String alcalde) {
        super(idOrganizacion, idHistorial, nombre, direccion, telefono, email, ciudad, fechaRegistro);
        contador++;
        this.idAyuntamiento = contador;
        this.provincia = provincia;
        this.alcalde = alcalde;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Ayuntamiento.contador = contador;
    }

    public int getIdAyuntamiento() {
        return idAyuntamiento;
    }

    public void setIdAyuntamiento(int idAyuntamiento) {
        this.idAyuntamiento = idAyuntamiento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getAlcalde() {
        return alcalde;
    }

    public void setAlcalde(String alcalde) {
        this.alcalde = alcalde;
    }

    @Override
    public String toString() {
        return "Ayuntamiento{" +
                "idAyuntamiento=" + idAyuntamiento +
                ", provincia='" + provincia + '\'' +
                ", alcalde='" + alcalde + '\'' +
                '}';
    }
}
