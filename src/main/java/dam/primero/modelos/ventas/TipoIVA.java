package dam.primero.modelos.ventas;

public enum TipoIVA {
    IVA_0(0.0),
    IVA_4(4.0),
    IVA_10(10.0),
    IVA_21(21.0);

    private final double porcentaje;

    TipoIVA(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public static TipoIVA fromString(String valor) {
        if (valor == null) return IVA_21;
        try {
            int pct = (int) Double.parseDouble(valor.replace("%", "").trim());
            switch (pct) {
                case 0:  return IVA_0;
                case 4:  return IVA_4;
                case 10: return IVA_10;
                default: return IVA_21;
            }
        } catch (NumberFormatException e) {
            return IVA_21;
        }
    }
}
