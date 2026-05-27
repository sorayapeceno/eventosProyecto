package dam.primero.repositorio.logistica;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;

public class Repositorio_Pedidos {
    private MySqlConector conector;

    //Constructor
    public Repositorio_Pedidos() {
        try {
            this.conector = new MySqlConector();
        } catch (MyException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }


}
