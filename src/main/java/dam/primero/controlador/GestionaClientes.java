package dam.primero.controlador;

import dam.primero.exception.MyException;
import dam.primero.repositorio.ventas.RepoVentas;

import java.sql.SQLException;

public class GestionaClientes {
    public static void main(String[] args) {
        RepoVentas rv = new RepoVentas();
        try {
            System.out.println("Añadiendo cliente");
            rv.darAltaCliente("Alex", "alexcozaru27@gmail.com", "+34 641-543-576");
        } catch (SQLException ex) {
            System.out.println("No se a podido agregar al cliente");
        }

            System.out.println("Listando clientes...");
            System.out.println(rv.listarClientes());

    }


}
