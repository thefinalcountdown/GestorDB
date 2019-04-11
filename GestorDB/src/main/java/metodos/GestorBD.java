package metodos;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class GestorBD {
	private String maquina = "localhost";
	private String usuario = "root";
	private String clave = "elorrieta";
	private int puerto = 3306;
	private String servidor = "";
	private static Connection conexion = null;
	private static Statement statement;
	private static ResultSet result;
	private static PreparedStatement preparedstatement;

	public GestorBD() {

		servidor = "jdbc:mysql://" + maquina + ":" + puerto + "/bidaion?serverTimezone=UTC";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Error al registrar el Driver");
			// System.err.println("Error al registrar el Driver");
			System.exit(0);
		}

		try {
			conexion = DriverManager.getConnection(servidor, usuario, clave);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al conectar con el servidor");
			// System.err.println("Error al conectar con el servidor");
			System.exit(0);
		}
		JOptionPane.showMessageDialog(null, "Conectando a la base de datos...");
		// System.out.println("Conectando a la base de datos...");
	}

	public static Connection getConexion() {
		return conexion;
	}

	public static ArrayList<String> obtenerUbicaciones() throws Exception {
		ArrayList<String> ubicaciones = new ArrayList<String>();
		String sentencia = "select distinct(ubicacion) from hoteles order by ubicacion";
		try {

			statement = conexion.createStatement();

			result = statement.executeQuery(sentencia);
			while (result.next()) {
				ubicaciones.add(new String(result.getString("ubicacion")));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ubicaciones;
	}

	public static ArrayList<String> obtenerHoteles(String ubicacion) throws Exception {
		ArrayList<String> hoteles = new ArrayList<String>();
		String sentencia = "select * from hoteles where ubicacion='%s'";
		sentencia = String.format(sentencia, ubicacion);
		try {

			statement = conexion.createStatement();

			result = statement.executeQuery(sentencia);
			while (result.next()) {
				String nombre = result.getString("nombre");
				String precio = Integer.toString(result.getInt("precio"));
				String estrellas = Integer.toString(result.getInt("estrellas"));
				hoteles.add(new String(nombre + ";" + precio + "; " + estrellas));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hoteles;
	}

	public static void insertarReserva(ArrayList<String> reserva) throws Exception {
		try {
			String sentencia = "insert into Reserva(NombreAlojamiento,Precio,NumPersonas,NumHabitaciones,Ubicacion,FechaEntrada,FechaSalida) values ('"
					+ reserva.get(0) + "'," + Float.parseFloat(reserva.get(1)) + "," + Integer.parseInt(reserva.get(2)) + "," + Integer.parseInt(reserva.get(3))
					+ ",'" + reserva.get(4) + "','" + reserva.get(5) + "','" + reserva.get(6) + "')";
			statement = conexion.createStatement();

			preparedstatement = conexion.prepareStatement(sentencia);
			preparedstatement.executeUpdate();

			JOptionPane.showMessageDialog(null, "Datos correctamente ingresados");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al enviar a informacion a la Base de Datos");
		}
	}

	public static boolean comprobarUsuario(String dni, String clave) {
		String sentencia = "select * from Usuario where DNI=\"" + dni + "\" and Clave=\"" + clave + "\"";
		try {
			preparedstatement = conexion.prepareStatement(sentencia);
			result = preparedstatement.executeQuery();
			if (result.next() == true) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta a la base de datos...");
		}
		return false;
	}

	public static boolean insertarUsuario(String dni, String nombre, String apellidos, String clave) {
		String sentencia = "insert into Usuario(DNI, Nombre, Apellido, Clave) " + "values(\"" + dni + "\", \"" + nombre
				+ "\", \"" + apellidos + "\", \"" + clave + "\")";
		try {
			statement = conexion.createStatement();
			preparedstatement = conexion.prepareStatement(sentencia);
			preparedstatement.executeUpdate();
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta a la base de datos...");
		}
		return false;

	}

	public static void borrarUsuario(String logindni, String loginpass) {
		String sentencia = "delete from Usuario where DNI=\"" + logindni + "\" and Clave=\"" + loginpass + "\"";
		try {
			statement = conexion.createStatement();
			preparedstatement = conexion.prepareStatement(sentencia);
			preparedstatement.executeUpdate();

		} catch (Exception error) {
			JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta a la base de datos...");

		}
	}

	public static void actualizarUsuario(String dni, String clave, String nuevaclave) {
		String sentencia = "update Usuario set Clave=\"" + nuevaclave + "\" where DNI=\"" + dni + "\" and Clave=\""
				+ clave + "\"";
		try {
			statement = conexion.createStatement();
			preparedstatement = conexion.prepareStatement(sentencia);
			preparedstatement.executeUpdate();

		} catch (Exception error) {
			JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta a la base de datos...");

		}
	}

	public static boolean introducirLogin(String logindni, String loginpass) {

		String sentencia = "select DNI, Contrase�a from Usuario where DNI=\"" + logindni + "\" and Clave=\"" + loginpass
				+ "\"";

		try {
			statement = conexion.createStatement();
			result = statement.executeQuery(sentencia);
			return result.first();
		} catch (Exception error) {
			JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta a la base de datos...");
		}
		return false;
	}
    
	public static ResultSet consulta(String sentencia) {

		try {
			preparedstatement = conexion.prepareStatement(sentencia);
			result = preparedstatement.executeQuery();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta a la base de datos");
		}
		return result;
	}

	public static boolean insertarDatos(String sentencia) {
		try {
			statement = conexion.createStatement();
			statement.executeUpdate(sentencia);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta a la base de datos");
			return false;
		}
		return true;
	}

	public static boolean isDbConnected() {
		final String CHECK_SQL_QUERY = "SELECT 1";
		boolean isConnected = false;
		try {
			final PreparedStatement statement = GestorBD.getConexion().prepareStatement(CHECK_SQL_QUERY);
			result = statement.executeQuery();
			String cadena = "";
			while (result.next())
				cadena = result.getString(1);
			if (cadena.equals("1"))
				isConnected = true;
		} catch (SQLException | NullPointerException e) {
			JOptionPane.showMessageDialog(null, "Se ha perdido la conexion a la base de datos...");
		}
		return isConnected;
	}
}
