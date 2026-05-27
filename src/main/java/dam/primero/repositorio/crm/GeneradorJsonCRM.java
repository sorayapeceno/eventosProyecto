package dam.primero.repositorio.crm;

import dam.primero.modelos.crm.FormularioProducto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GeneradorJsonCRM {

    public void generarProductos(List<FormularioProducto> productos, String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);

        // Creamos la carpeta si todavía no existe.
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            writer.println("[");

            for (int i = 0; i < productos.size(); i++) {
                FormularioProducto producto = productos.get(i);

                writer.println("  {");
                writer.println("    \"nombre\": \"" + limpiarJson(producto.getNombre()) + "\",");
                writer.println("    \"descripcion\": \"" + limpiarJson(producto.getDescripcion()) + "\",");
                writer.println("    \"precio\": " + producto.getPrecio() + ",");
                writer.println("    \"stock\": " + producto.getStock() + ",");
                writer.println("    \"categoria\": \"" + limpiarJson(producto.getCategoria()) + "\"");
                writer.print("  }");

                if (i < productos.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }

            writer.println("]");
        }
    }

    private String limpiarJson(String texto) {
        if (texto == null) {
            return "";
        }

        // Evita que las comillas rompan el formato del JSON.
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
