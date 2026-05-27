package dam.primero.controlador;

import dam.primero.repositorio.dashboards.RepoDashboards;


public class GestionaDashboards {
    public static void main(String[] args) {
        RepoDashboards repo = new RepoDashboards();
        System.out.println(repo.albAranesPorEstado());
        System.out.println(repo.contarOrganizaciones());
    }
}
