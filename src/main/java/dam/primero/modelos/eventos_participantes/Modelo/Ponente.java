package dam.primero.modelos.eventos_participantes.Modelo;

import java.util.Objects;

public class Ponente {

    private static int contador;
    private int idPonente;
    private String BIO;
    private String especialidad;
    private String CV;
    private NivelImparticion nivelImparticion;

    public Ponente(int idPonente, String BIO, String especialidad, String CV, NivelImparticion nivelImparticion) {

        contador++;
        this.idPonente = contador;
        this.BIO = BIO;
        this.especialidad = especialidad;
        this.CV = CV;
        this.nivelImparticion = nivelImparticion;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Ponente.contador = contador;
    }

    public int getIdPonente() {
        return idPonente;
    }

    public void setIdPonente(int idPonente) {
        this.idPonente = idPonente;
    }

    public String getBIO() {
        return BIO;
    }

    public void setBIO(String BIO) {
        this.BIO = BIO;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCV() {
        return CV;
    }

    public void setCV(String CV) {
        this.CV = CV;
    }

    public NivelImparticion getNivelImparticion() {
        return nivelImparticion;
    }

    public void setNivelImparticion(NivelImparticion nivelImparticion) {
        this.nivelImparticion = nivelImparticion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ponente ponente = (Ponente) o;
        return idPonente == ponente.idPonente;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idPonente);
    }

    @Override
    public String toString() {
        return "Ponente{" +
                "idPonente=" + idPonente +
                ", BIO='" + BIO + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", CV='" + CV + '\'' +
                ", nivelImparticion=" + nivelImparticion +
                '}';
    }
}
