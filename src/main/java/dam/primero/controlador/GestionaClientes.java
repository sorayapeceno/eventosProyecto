package dam.primero.controlador;

import dam.primero.repositorio.ventas.RepoProducto;

import java.sql.SQLException;

public class GestionaClientes {
    public static void main(String[] args) {
        RepoProducto rv = new RepoProducto();
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
