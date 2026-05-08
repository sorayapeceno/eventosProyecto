package dam.primero.modelos.ventas;

public enum TipoIVA {
    IVA_0(0),
    IVA_4(4),
    IVA_10(10),
    IVA_21(21);

    private final int porcentaje;
    TipoIVA(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public int getPorcentaje() {
        return porcentaje;
    }
}
