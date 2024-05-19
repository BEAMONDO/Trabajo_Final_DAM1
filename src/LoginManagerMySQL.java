import java.sql.*;
import javax.swing.JOptionPane;

public class LoginManagerMySQL
{

    private String msqls, usuarioMySQL, contraseñaMySQL;

    public LoginManagerMySQL(String msqls, String usuarioMySQL, String contraseñaMySQL)
    {
        this.msqls = msqls;
        this.usuarioMySQL = usuarioMySQL;
        this.contraseñaMySQL = contraseñaMySQL;
    }

    public boolean comprobacionLoginMySQL(String usuario, String contraseña)
    {
        boolean loginExitoso = false;
        try
        {
            // Conexión a MySQL
            Connection cmysql = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);

            // Crear la consulta SQL
            String query = "SELECT * FROM Empleados WHERE usuario = ? AND contraseña = ?";
            PreparedStatement statement = cmysql.prepareStatement(query);
            statement.setString(1, usuario);
            statement.setString(2, contraseña);

            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery();

            // Verificar si se encontró alguna coincidencia
            if (resultSet.next())
            {
                loginExitoso = true;
            }

            // Cerrar la conexión
            cmysql.close();
        }
        catch (SQLException ex)
        {
            // Manejar cualquier error de SQL
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
        }
        return loginExitoso;
    }
}