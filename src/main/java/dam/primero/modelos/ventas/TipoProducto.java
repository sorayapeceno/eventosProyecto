package dam.primero.modelos.ventas;

public enum TipoProducto {
    VIP, ESTANDAR, BECA, CAMISETA, ACCESORIO, OTROS;

    public String getDisplayName() {
        switch (this) {
            case VIP:      return "VIP";
            case ESTANDAR: return "Estándar";
            case BECA:     return "Beca";
            case CAMISETA: return "Camiseta";
            case ACCESORIO: return "Accesorio";
            case OTROS:    return "Otros";
            default:       return name();
        }
    }
}
