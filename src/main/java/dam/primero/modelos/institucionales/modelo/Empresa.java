package dam.primero.modelos.institucionales.modelo;

import java.time.LocalDate;

public class Empresa extends Organizacion {
    private static int contador;
    private int idEmpresa;
    private String sector;
    private int numEmpleados;

    public Empresa() {
    }

    public Empresa(int idOrganizacion, HistorialOportunidad idHistorial, String nombre, String direccion, int telefono, String email, String ciudad, LocalDate fechaRegistro, int idEmpresa, String sector, int numEmpleados) {
        super(idOrganizacion, idHistorial, nombre, direccion, telefono, email, ciudad, fechaRegistro);
        contador++;
        this.idEmpresa = contador;
        this.sector = sector;
        this.numEmpleados = numEmpleados;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Empresa.contador = contador;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public int getNumEmpleados() {
        return numEmpleados;
    }

    public void setNumEmpleados(int numEmpleados) {
        this.numEmpleados = numEmpleados;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "idEmpresa=" + idEmpresa +
                ", sector='" + sector + '\'' +
                ", numEmpleados=" + numEmpleados +
                '}';
    }
}
