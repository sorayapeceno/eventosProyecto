package dam.primero.config.eventos_participantes;

import dam.primero.config.MySqlConector;
import dam.primero.exception.MyException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class MySqlConectorEventosParticipantes  {
	protected Connection connect;
	protected String url;
	protected String user;
	protected String clave;
	public MySqlConectorEventosParticipantes() throws MyException {
		try {
			Properties properties = new Properties();
			var stream = MySqlConectorEventosParticipantes.class.getClassLoader()
					.getResourceAsStream("eventos_participantes/db.properties");
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

	public void setConnect(Connection connect) {
		this.connect = connect;
	}
}