package dam.primero.config;

import dam.primero.exception.MyException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



public class MySqlConector {
	private Connection connect;
	private String url;
	private String user;
	private String clave;

	public MySqlConector() throws MyException {
		try {
			Properties properties = new Properties();
			var stream = MySqlConector.class.getClassLoader()
					.getResourceAsStream("db.properties");
			if (stream == null) {
				throw new MyException("No se encontró db.properties en el classpath");
			}
			properties.load(stream);
			this.url = properties.getProperty("url");
			this.user = properties.getProperty("user");
			this.clave = properties.getProperty("clave");

			// Carga explícita del driver — necesario en Tomcat con classloaders separados
			Class.forName("com.mysql.cj.jdbc.Driver");

			this.connect = DriverManager.getConnection(this.url, this.user, this.clave);

			System.out.println("Conectado");
		} catch (IOException e) {
			throw new MyException("Error al conectar a la base de datos" + e.getMessage());
		} catch (SQLException e) {
			throw new MyException("Error al conectar a la base de datos" + e.getMessage());
		} catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

	public Connection getConnect() {
		return connect;
	}

	public void release() {
		try {
			System.out.print("--- CERRANDO CONEXION ---");
			if (this.connect != null)
				this.connect.close();
			this.connect = null;
			this.url = null;
			this.user = null;
			this.clave = null;

		} catch (SQLException e) {
			System.err.println("No se ha podido cerrar la conexion con la BD");
			e.printStackTrace();
		}
	}
}