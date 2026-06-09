package dam.primero.config.dashboards;

import dam.primero.exception.MyException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Conector específico del módulo Dashboard con la base de datos MySQL.
 * <p>
 * Esta clase gestiona la conexión con la base de datos {@code dashboards},
 * que contiene todas las tablas unificadas de los módulos del proyecto
 * (eventos, ventas, CRM, relaciones institucionales y logística).
 * </p>
 * <p>
 * Las credenciales de conexión se leen del archivo
 * {@code dashboards/db.properties} ubicado en el classpath, que contiene:
 * </p>
 * <pre>
 * url=jdbc:mysql://localhost:3306/dashboards
 * user=alumno
 * clave=alumnodam#1234
 * </pre>
 * <p>
 * A diferencia del {@code MySqlConector} general del proyecto, esta clase
 * apunta exclusivamente a la BD {@code dashboards} para que el módulo
 * de dashboards funcione de forma independiente.
 * </p>
 *
 * @author Elena Pablo Benítez
 * @version 1.0
 */
public class MySqlConectorDashboards {

	/** Conexión activa con la base de datos. */
	protected Connection connect;

	/** URL de conexión JDBC leída del archivo de propiedades. */
	protected String url;

	/** Usuario de la base de datos leído del archivo de propiedades. */
	protected String user;

	/** Contraseña de la base de datos leída del archivo de propiedades. */
	protected String clave;

	/**
	 * Constructor que abre la conexión con la base de datos {@code dashboards}.
	 * <p>
	 * El proceso es:
	 * </p>
	 * <ol>
	 *   <li>Carga el archivo {@code dashboards/db.properties} desde el classpath</li>
	 *   <li>Lee las propiedades {@code url}, {@code user} y {@code clave}</li>
	 *   <li>Registra el driver JDBC de MySQL con {@code Class.forName}</li>
	 *   <li>Abre la conexión con {@code DriverManager.getConnection}</li>
	 * </ol>
	 *
	 * @throws MyException si no se encuentra el archivo {@code db.properties},
	 *                     si hay un error de conexión SQL, o si no se encuentra
	 *                     el driver JDBC de MySQL
	 */
	public MySqlConectorDashboards() throws MyException {
		try {
			Properties properties = new Properties();
			var stream = MySqlConectorDashboards.class.getClassLoader()
					.getResourceAsStream("dashboards/db.properties");
			if (stream == null) {
				throw new MyException("No se encontró db.properties en el classpath");
			}
			properties.load(stream);
			this.url   = properties.getProperty("url");
			this.user  = properties.getProperty("user");
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

	/**
	 * Devuelve la conexión activa con la base de datos.
	 * <p>
	 * Esta conexión es usada por {@link dam.primero.repositorio.dashboards.RepoDashboards}
	 * para ejecutar las queries SQL mediante {@code Statement}.
	 * </p>
	 *
	 * @return objeto {@link Connection} con la conexión activa a MySQL
	 */
	public Connection getConnect() {
		return connect;
	}

	/**
	 * Establece una nueva conexión, permitiendo reemplazar la conexión activa.
	 *
	 * @param connect nueva conexión a establecer
	 */
	public void setConnect(Connection connect) {
		this.connect = connect;
	}
}