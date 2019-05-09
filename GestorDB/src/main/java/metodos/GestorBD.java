package metodos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

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

		servidor = "jdbc:mysql://" + maquina + ":" + puerto + "/bidaion_prueba?serverTimezone=UTC";
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
		} 
		catch (ClassNotFoundException e) 
		{
			JOptionPane.showMessageDialog(null, "Error al registrar el Driver.");
			// System.err.println("Error al registrar el Driver");
			System.exit(0);
		}

		try 
		{
			conexion = DriverManager.getConnection(servidor, usuario, clave);
		} 
		catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null, "Error al conectar con el servidor.");
			// System.err.println("Error al conectar con el servidor");
			System.exit(0);
		}
		
		conectando();
		
	}

	public static JOptionPane conectando() {
		JOptionPane miPanel = new JOptionPane("Conectando a la Base de datos.");
        final JDialog dialog = miPanel.createDialog(null, "Conectando");//aqui uso un JDialog para manipular
        																					//el JOptionPane
        dialog.setModal(true);  
        //Usando javax.swing.Timer para poder generar un evento en un tiempo determinado
        Timer timer = new Timer(1 * 1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();				
			}		
        });
        		
        timer.start();
        dialog.setVisible(true);
        timer.stop();
        
        return miPanel;
	}
	
	public static Connection getConexion() 
	{
		return conexion;
	}

	public static ArrayList<String> obtenerUbicaciones() throws Exception 
	{
		ArrayList<String> ubicaciones = new ArrayList<String>();
		String sentencia = "select distinct(ubicacion) from hoteles order by ubicacion";
		try 
		{

			statement = conexion.createStatement();

			result = statement.executeQuery(sentencia);
			while (result.next()) 
			{
				ubicaciones.add(new String(result.getString("ubicacion")));

			}

		} 
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		return ubicaciones;
	}

	public static ArrayList<String> obtenerHoteles(String ubicacion) throws Exception 
	{
		ArrayList<String> hoteles = new ArrayList<String>();
		String sentencia = "select * from hoteles where ubicacion='%s'";
		sentencia = String.format(sentencia, ubicacion);
		try 
		{

			statement = conexion.createStatement();

			result = statement.executeQuery(sentencia);
			while (result.next()) 
			{
				String nombre = result.getString("nombre");
				String precio = Integer.toString(result.getInt("precio"));
				String estrellas = Integer.toString(result.getInt("estrellas"));
				hoteles.add(new String(nombre + ";" + precio + "; " + estrellas));

			}

		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		return hoteles;
	}

	public static ResultSet consulta(String sentencia) 
	{

		try 
		{
			preparedstatement = conexion.prepareStatement(sentencia);
			result = preparedstatement.executeQuery();
		}
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta a la base de datos.");
			System.exit(0);
		}
		return result;
	}

	public static boolean insertarDatos(String sentencia) 
	{
		try 
		{
			statement = conexion.createStatement();
			statement.executeUpdate(sentencia);
		}
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "No se pudo insertar los datos.");
			System.exit(0);
			return false;
		}
		return true;
	}
	
	public static String conseguir_nombre_apellidos(String DNI) 
	{
	    CallableStatement call_procedure = null;
	    String nomape = "";
	    try 
	    {
	          call_procedure = conexion.prepareCall("{call conseguir_nombre_apellidos(?)}");
	          call_procedure.setString(1,DNI);
	          
		      result = call_procedure.executeQuery();
	
		      while (result.next()) 
		      {
		    	  nomape = result.getString("NomApe");
		      }
	    } 
	    catch (SQLException e) 
	    {
	    	JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta a la base de datos.");
	    	System.exit(0);
	    } 
	    
	    return nomape;
	 }	
}
