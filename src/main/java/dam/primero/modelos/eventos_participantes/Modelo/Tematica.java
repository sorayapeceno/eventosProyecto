package dam.primero.modelos.eventos_participantes.Modelo;

import java.util.Objects;

public class Tematica  {
    private static int contador;
    private int id_Tematica;
    private String Tema;

    public Tematica(int id_Tematica, String tema) {
        contador++;
        this.id_Tematica = id_Tematica;
        Tema = tema;
    }

    public int getId_Tematica() {
        return id_Tematica;
    }

    public void setId_Tematica(int id_Tematica) {
        this.id_Tematica = id_Tematica;
    }

    public String getTema() {
        return Tema;
    }

    public void setTema(String tema) {
        Tema = tema;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tematica tematica = (Tematica) o;
        return id_Tematica == tematica.id_Tematica;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id_Tematica);
    }

    @Override
    public String toString() {
        return "Tematica{" +
                "id_Tematica=" + id_Tematica +
                ", Tema='" + Tema + '\'' +
                '}';
    }
}
