package dam.primero.modelos.institucionales;

public class patrocinador {
    private int idpatrocinador;
    private String nombrePatrocinador;

    public int getIdpatrocinador() {
        return idpatrocinador;
    }

    public void setIdpatrocinador(int idpatrocinador) {
        this.idpatrocinador = idpatrocinador;
    }

    public String getNombrePatrocinador() {
        return nombrePatrocinador;
    }

    public void setNombrePatrocinador(String nombrePatrocinador) {
        this.nombrePatrocinador = nombrePatrocinador;
    }

    public patrocinador(int idpatrocinador, String nombrePatrocinador) {
        this.idpatrocinador = idpatrocinador;
        this.nombrePatrocinador = nombrePatrocinador;
    }
}
