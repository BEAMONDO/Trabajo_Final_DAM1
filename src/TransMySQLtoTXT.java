import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class TransMySQLtoTXT 
{

    private String msqls;
    private String usuarioMySQL;
    private String contraseñaMySQL;
    private String txtfile;

    public TransMySQLtoTXT(String msqls, String usuarioMySQL, String contraseñaMySQL, String txtfile) 
    {
        this.msqls = msqls;
        this.usuarioMySQL = usuarioMySQL;
        this.contraseñaMySQL = contraseñaMySQL;
        this.txtfile = txtfile;
    }

    public void transferirMaterialesMySQLtoTXT() 
    {
        try 
        {
            // Establecer la conexión con MySQL
            Connection conexion = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);

            // Crear el archivo de texto
            BufferedWriter bw = new BufferedWriter(new FileWriter(txtfile));

            // Crear una consulta SQL para obtener los materiales
            Statement sentencia = conexion.createStatement();
            String consultaSQL = "SELECT nombre, cantidad, precioUnidad FROM Materiales";
            ResultSet resultado = sentencia.executeQuery(consultaSQL);

            // Escribir la información en el archivo de texto
            while (resultado.next()) 
            {
                String nombre = resultado.getString("nombre");
                int cantidad = resultado.getInt("cantidad");
                double precioUnidad = resultado.getDouble("precioUnidad");

                String otext = "'" + nombre + "', '" + cantidad + "', '" + precioUnidad + "'";
                bw.write(otext);
                bw.newLine();
            }

            // Cerrar recursos
            bw.close();
            resultado.close();
            sentencia.close();
            conexion.close();

            // Mostrar un mensaje de éxito
            JOptionPane.showMessageDialog(null, "Datos copiados al fichero '" + txtfile + "'", "Info", JOptionPane.INFORMATION_MESSAGE);
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