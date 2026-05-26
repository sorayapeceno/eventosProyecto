package dam.primero.config;

import dam.primero.exception.MyException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



public class MySqlConector {
	protected Connection connect;
	protected String url;
	protected String user;
	protected String clave;

	public MySqlConector() throws MyException {
		this("eventos_participantes/db.properties");
	}

	public MySqlConector(String propertiesPath) throws MyException {
		try {
			Properties properties = new Properties();
			var stream = MySqlConector.class.getClassLoader()
					.getResourceAsStream(propertiesPath);
			if (stream == null) {
				throw new MyException("No se encontró " + propertiesPath + " en el classpath");
			}
			properties.load(stream);
			this.url   = properties.getProperty("url");
			this.user  = properties.getProperty("user");
			this.clave = properties.getProperty("clave");

			Class.forName("com.mysql.cj.jdbc.Driver");

			this.connect = DriverManager.getConnection(this.url, this.user, this.clave);

			System.out.println("Conectado a: " + this.url);
		} catch (IOException e) {
			throw new MyException("Error al conectar a la base de datos: " + e.getMessage());
		} catch (SQLException e) {
			throw new MyException("Error al conectar a la base de datos: " + e.getMessage());
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