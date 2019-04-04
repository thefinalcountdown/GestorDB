package metodos;

import java.sql.Connection;
import Modelo.Ubicacion;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GestorBD {
	private String maquina = "localhost";
	private String usuario = "root";
	private String clave = "elorrieta";
	private int puerto = 3306;
	private String servidor = "";
	private static Connection conexion = null;
	Statement statement;
	ResultSet result;

	public GestorBD() {

		servidor = "jdbc:mysql://" + maquina + ":" + puerto + "/bidaion?serverTimezone=UTC";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Error al registrar el Driver");
			System.exit(0);
		}

		try {
			conexion = DriverManager.getConnection(servidor, usuario, clave);
		} catch (SQLException e) {
			System.err.println("Error al conectar con el servidor");
			System.exit(0);
		}
		System.out.println("Conectando a la base de datos...");
	}

	public Connection getConexion() {
		return conexion;
	}
	
	
	public ArrayList<Ubicacion> comboBoxUbicacion() throws Exception {
		ArrayList<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();

		try {

			statement = conexion.createStatement();

			result = statement.executeQuery("select distinct ubicacion from hoteles");
			while (result.next()) {
				ubicaciones.add(new Ubicacion(result.getString("Ubicacion")));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ubicaciones;
	}
}
