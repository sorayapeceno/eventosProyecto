package dam.primero.modelos.eventos_participantes.Modelo;

public enum Modalidad {
    PRESENCIAL,
    ONLINE,
    HIBRIDO;

    public String getValorBD() {
        return this.name();
    }

    public static Modalidad fromValorBD(String valorBD) {
        return Modalidad.valueOf(valorBD.toUpperCase());
    }
}