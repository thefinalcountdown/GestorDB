package metodos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

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

	public static ArrayList<String> obtenerHoteles(String ubicacion) throws Exception 
	{
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
	
	
	public static void introducir_reserva()
	{
		try
		{
			String linea;
			FileReader fichero = new FileReader("Reto4_TFC\\ficheroReserva.txt");
			BufferedReader buf = new BufferedReader(fichero);
			
			ArrayList <String> fichero_texto = new ArrayList<String>();
			ArrayList <String> palabra = new ArrayList<String>();
			
			while ((linea = buf.readLine()) != null)
			{
				fichero_texto.add(linea);
			}
			
			for(int cont=0; cont < fichero_texto.size(); cont++)
			{
				int posicion = fichero_texto.size() - cont - 1;
				String [] partes = fichero_texto.get(cont).split(": ");
				palabra.add(partes[1]);
			}
			
			
			String nombreHotel = palabra.get(0);
			int numPersonas = Integer.parseInt(palabra.get(1));
			String ubicacion = palabra.get(2);
			float precio = Float.parseFloat(palabra.get(3));
			for(int cont=0; cont<palabra.size(); cont++)
			{
				System.out.println(palabra.get(cont));
			}
			
			buf.close();
			fichero.close();
		}
		catch(IOException ex)
		{
			 System.err.println("No se puede leer del archivo");
		   	 System.exit(-1);
		}
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
}
