package dam.primero.controlador;

import dam.primero.modelos.crm.PaginaWeb;
import dam.primero.repositorio.crm.RepoPaginaWeb;

import java.util.List;

public class GestionaPaginasWeb {
    public static void main(String[] args) {
        RepoPaginaWeb r = new RepoPaginaWeb();

        System.out.println("Listando paginas...");
        List<PaginaWeb> paginas = r.listarPaginaWeb();
        for (PaginaWeb pagina : paginas){
            System.out.println(pagina);
        }
    }
}
