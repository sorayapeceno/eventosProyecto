package dam.primero.repositorio.logistica;

import dam.primero.modelos.logistica.modelo.Mercancia;

import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// Esta clase lo que realiza es exportar el csv de las mercancías filtradas.

public class ExportadorMercanciasCsv {

    private final Repositorio_Mercancias repositorio;

    public ExportadorMercanciasCsv() throws Exception {
        this.repositorio = new Repositorio_Mercancias();
    }

    public void exportarMercanciasStockMayor40() throws Exception {

        // cojo todas las mercancías que hay en la base de datos
        List<Mercancia> listaMercancias = repositorio.listarMercancias();

        // aquí voy a ir guardando las que cumplan la condición
        List<Mercancia> mercanciasFiltradas = new ArrayList<>();

        for (Mercancia mercancia : listaMercancias) {

            // me quedo solo con las que tengan más de 40 de stock
            if (mercancia.getStockActual() > 40) {
                mercanciasFiltradas.add(mercancia);
            }
        }

        // Aquí elijo la ruta donde se va a guardar
        String carpeta = "src/main/resources/Logistica y aprovisionamiento";
        String rutaArchivo = carpeta + "/mercancias.csv";

        // creo el fichero y empiezo a escribir dentro
        try (FileWriter escritor = new FileWriter(rutaArchivo)) {

            // esta es la cabecera
            escritor.write("id_mercancia,descripcion,categoria,precio_unitario,stock_actual\n");

            // ahora voy metiendo cada mercancía dentro
            for (Mercancia mercancia : mercanciasFiltradas) {

                escritor.write(
                        mercancia.getIdMercancia() + "," +
                                mercancia.getDescripcion() + "," +
                                mercancia.getCategoria() + "," +
                                mercancia.getPrecioUnitario() + "," +
                                mercancia.getStockActual() + "\n"
                );
            }
        }
    }
}