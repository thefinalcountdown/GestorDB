package metodos;

import java.sql.Connection;
//import Reto4_TFC.Modelo.Ubicacion;

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
			//System.err.println("Error al registrar el Driver");
			System.exit(0);
		}

		try {
			conexion = DriverManager.getConnection(servidor, usuario, clave);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al conectar con el servidor");
			//System.err.println("Error al conectar con el servidor");
			System.exit(0);
		}
		JOptionPane.showMessageDialog(null, "Conectando a la base de datos...");
		//System.out.println("Conectando a la base de datos...");
	}

	public Connection getConexion() {
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
				String nombre =result.getString("nombre");
				String precio= Integer.toString(result.getInt("precio"));
				String estrellas=Integer.toString(result.getInt("estrellas"));
				hoteles.add(new String(nombre+";"+precio+"; "+estrellas));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hoteles;
	}
	
	public static boolean comprobarUsuario(String dni) {
		String sentencia = "select * from Usuarios where DNI=\""+ dni +"\"";
		try {
			preparedstatement = conexion.prepareStatement(sentencia);
			result = preparedstatement.executeQuery();
			if (result.next() == true) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta a la Base de Datos");
		}
		return false;
	}

	public static boolean comprobarCampos(String dni, String nombre, String apellido, String clave) {
		String sentencia = "insert into usuario(DNI, Nombre, Apellido, Clave)" + "values(\"" + dni + "\", \"" + nombre
				+ "\", \"" + apellido + "\", \"" + clave + "\")";
		try {
			statement = conexion.createStatement();
			preparedstatement = conexion.prepareStatement(sentencia);
			if(dni.equals("") || nombre.equals("") || apellido.equals("") || clave.equals("")) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta a la Base de Datos");

		}
		return false;

	}

	public static boolean insertarUsuario(String dni, String nombre, String apellido, String clave) {
		String sentencia = "insert into usuario(DNI, Nombre, Apellido, Clave)" + "values(\"" + dni + "\", \"" + nombre
				+ "\", \"" + apellido + "\", \"" + clave + "\")";
		try {
			statement = conexion.createStatement();
			preparedstatement = conexion.prepareStatement(sentencia);
			preparedstatement.executeUpdate();
			return true;

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta a la Base de Datos");

		}
		return false;
	}

}
