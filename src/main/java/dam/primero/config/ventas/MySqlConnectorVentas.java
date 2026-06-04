package dam.primero.config.ventas;

import dam.primero.exception.MyException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona la conexión a la base de datos del módulo de ventas.
 * Lee las credenciales del fichero db.propierties del classpath,
 * abre la conexión al inicializarse y la cierra con release().
 *
 * @author Jose Lazo
 */
public class MySqlConnectorVentas {

    private Connection connect;
    private String url;
    private String user;
    private String clave;

    public MySqlConnectorVentas() throws MyException {
        try {
            Properties properties = new Properties();
            var stream = MySqlConnectorVentas.class.getClassLoader()
                    .getResourceAsStream("Modulo_Ventas/db.propierties");
            if (stream == null) {
                throw new MyException("No se encontró Modulo_Ventas/db.propierties en el classpath");
            }
            properties.load(stream);
            this.url   = properties.getProperty("url");
            this.user  = properties.getProperty("user");
            this.clave = properties.getProperty("clave");

            Class.forName("com.mysql.cj.jdbc.Driver");

            this.connect = DriverManager.getConnection(this.url, this.user, this.clave);

        } catch (IOException e) {
            throw new MyException("Error al leer configuración de ventas: " + e.getMessage());
        } catch (SQLException e) {
            throw new MyException("Error al conectar a la BD de ventas: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new MyException("Driver MySQL no encontrado: " + e.getMessage());
        }
    }

    public Connection getConnect() {
        return connect;
    }

    public void release() {
        try {
            if (this.connect != null) {
                this.connect.close();
            }
        } catch (SQLException e) {
            System.err.println("No se pudo cerrar la conexión de ventas: " + e.getMessage());
        } finally {
            this.connect = null;
            this.url     = null;
            this.user    = null;
            this.clave   = null;
        }
    }
}
