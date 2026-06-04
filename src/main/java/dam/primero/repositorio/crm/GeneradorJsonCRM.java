package dam.primero.repositorio.crm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dam.primero.modelos.crm.FormularioProducto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeneradorJsonCRM {

    public void generarProductos(List<FormularioProducto> productos, String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);

        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fichero = null;

        try {
            fichero = new FileWriter(archivo);
            gson.toJson(productos, fichero);
        } finally {
            if (fichero != null) {
                fichero.close();
            }
        }
    }
}
