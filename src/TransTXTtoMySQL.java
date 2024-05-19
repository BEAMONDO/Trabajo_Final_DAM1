import java.io.*;
import java.sql.*;
import javax.swing.*;

public class TransTXTtoMySQL 
{

    private String msqls;
    private String usuarioMySQL;
    private String contraseñaMySQL;
    private String txtfile;

    public TransTXTtoMySQL(String msqls, String usuarioMySQL, String contraseñaMySQL, String txtfile) 
    {
        this.msqls = msqls;
        this.usuarioMySQL = usuarioMySQL;
        this.contraseñaMySQL = contraseñaMySQL;
        this.txtfile = txtfile;
    }

    public void cargarMaterialesTXTtoMySQL() 
    {
        try 
        {
            // Establecer la conexión con MySQL
            Connection conexion = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);

            // Crear un lector de archivos para leer el archivo de texto
            BufferedReader br = new BufferedReader(new FileReader(txtfile));

            // Preparar una consulta SQL para insertar los datos
            PreparedStatement statement = conexion.prepareStatement("INSERT INTO Materiales (nombre, cantidad, precioUnidad) VALUES (?, ?, ?)");

            // Leer cada línea del archivo de texto y procesarla
            String linea;
            while ((linea = br.readLine()) != null) 
            {
                // Dividir la línea en partes separadas por comas
                String[] partes = linea.split(", ");

                // Obtener los valores de nombre, cantidad y precioUnidad
                String nombre = partes[0].substring(1, partes[0].length() - 1);
                int cantidad = Integer.parseInt(partes[1].substring(1, partes[1].length() - 1));
                double precioUnidad = Double.parseDouble(partes[2].substring(1, partes[2].length() - 1));

                // Establecer los parámetros de la consulta preparada
                statement.setString(1, nombre);
                statement.setInt(2, cantidad);
                statement.setDouble(3, precioUnidad);

                // Ejecutar la consulta preparada
                statement.executeUpdate();
            }

            // Cerrar recursos
            br.close();
            statement.close();
            conexion.close();

            // Mostrar un mensaje de éxito
            JOptionPane.showMessageDialog(null, "Datos cargados desde el fichero '" + txtfile + "' a la base de datos", "Info", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (IOException | SQLException e) 
        {
            // Mostrar un mensaje de error en caso de excepción
            JOptionPane.showMessageDialog(null, "Se ha producido un error", "Error", JOptionPane.ERROR_MESSAGE);
            // Imprimir el mensaje de error en la consola
            System.err.println(e.getMessage());
        }
    }
}