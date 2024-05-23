import java.sql.*;
import javax.swing.*;

public class EliminarManagerMySQL
{

    private String msqls;
    private String usuarioMySQL;
    private String contraseñaMySQL;
    private JTextField uregf, cregf;

    public EliminarManagerMySQL(String msqls, String usuarioMySQL, String contraseñaMySQL, JTextField uregf, JTextField cregf)
    {
        this.msqls = msqls;
        this.usuarioMySQL = usuarioMySQL;
        this.contraseñaMySQL = contraseñaMySQL;
        this.uregf = uregf;
        this.cregf = cregf;
    }

    public void eliminarEmpleadoMySQL()
    {
        String usuario = uregf.getText();

        try
        {
            // Conexión a MySQL
            Connection cmysql = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);

            if (usuario.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Por favor, ingresa el usuario a eliminar", "Campo vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Crear la consulta SQL para verificar si el usuario existe
            String query = "SELECT * FROM Empleados WHERE usuario = ?";
            PreparedStatement pstmt = cmysql.prepareStatement(query);
            pstmt.setString(1, usuario);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next())
            {
                // Si el usuario no existe, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "El usuario no existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                // Si el usuario existe, proceder a eliminarlo
                String deleteQuery = "DELETE FROM Empleados WHERE usuario = ?";
                pstmt = cmysql.prepareStatement(deleteQuery);
                pstmt.setString(1, usuario);
                pstmt.executeUpdate();
                uregf.setText("");
                cregf.setText("");
                JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente");
            }

            // Cerrar la conexión
            pstmt.close();
            cmysql.close();
        }
        catch (Exception ex)
        {
            //ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Se ha producido un error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}