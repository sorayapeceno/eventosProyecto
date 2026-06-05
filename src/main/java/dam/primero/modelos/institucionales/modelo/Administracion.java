package dam.primero.modelos.institucionales.modelo;

import java.time.LocalDate;

public class Administracion extends Organizacion{
    private static int contador;
    private int idEmpresa;
    private String ambito;
    private int presupuesto;

    public Administracion() {
    }

    public Administracion(int idOrganizacion, HistorialOportunidad idHistorial, String nombre, String direccion, int telefono, String email, String ciudad, LocalDate fechaRegistro, int idEmpresa, String ambito, int presupuesto) {
        super(idOrganizacion, idHistorial, nombre, direccion, telefono, email, ciudad, fechaRegistro);
        this.idEmpresa = idEmpresa;
        this.ambito = ambito;
        this.presupuesto = presupuesto;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Administracion.contador = contador;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public int getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(int presupuesto) {
        this.presupuesto = presupuesto;
    }

    @Override
    public String toString() {
        return "Administracion{" +
                "idEmpresa=" + idEmpresa +
                ", ambito='" + ambito + '\'' +
                ", presupuesto=" + presupuesto +
                '}';
    }
}
