package dam.primero.modelos.eventos_participantes.Modelo;

public enum Estado {
    BORRADOR,
    ABIERTO,
    CERRADO,
    CANCELADO,
    POSTPUESTO;

    public String getValorBD() {
        return this.name();
    }

    public static Estado fromValorBD(String valorBD) {
        return Estado.valueOf(valorBD.toUpperCase());
    }
}