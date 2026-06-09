package dam.primero.modelos.dashboards;

public class Ponente {

    //atributos
    private String nombre;
    private String especialidad;
    private String nivelImparticion;
    private String correo;
    private String ciudad;
    private int totalPonencias;

    //constructor
    public Ponente(String nombre, String especialidad, String nivelImparticion,
                   String correo, String ciudad, int totalPonencias) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.nivelImparticion = nivelImparticion;
        this.correo = correo;
        this.ciudad = ciudad;
        this.totalPonencias = totalPonencias;
    }

    //métodos (esto se va a convertir en String de JSON)
    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }
    public String getNivelImparticion() { return nivelImparticion; }
    public String getCorreo() { return correo; }
    public String getCiudad() { return ciudad; }
    public int getTotalPonencias() { return totalPonencias; }

    //toString
    @Override
    public String toString() {
        return "Ponente{nombre='" + nombre + "', especialidad='" + especialidad +
                "', nivel='" + nivelImparticion + "', ponencias=" + totalPonencias + "}";
    }
}
