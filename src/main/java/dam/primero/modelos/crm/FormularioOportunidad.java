package dam.primero.modelos.crm;

public class FormularioOportunidad {
    private String titulo;
    private String descripcion;
    private String fechaInicio;
    private String tiposOportunidad;

    public FormularioOportunidad() {
    }

    public FormularioOportunidad(String titulo, String descripcion, String fechaInicio, String tiposOportunidad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.tiposOportunidad = tiposOportunidad;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getTiposOportunidad() { return tiposOportunidad; }
    public void setTiposOportunidad(String tiposOportunidad) { this.tiposOportunidad = tiposOportunidad; }
}
