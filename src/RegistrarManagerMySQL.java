import java.sql.*;
import javax.swing.*;

public class RegistrarManagerMySQL
{

    private String msqls;
    private String usuarioMySQL;
    private String contraseñaMySQL;
    private JTextField uregf;
    private JTextField cregf;

    public RegistrarManagerMySQL(String msqls, String usuarioMySQL, String contraseñaMySQL, JTextField uregf, JTextField cregf)
    {
        this.msqls = msqls;
        this.usuarioMySQL = usuarioMySQL;
        this.contraseñaMySQL = contraseñaMySQL;
        this.uregf = uregf;
        this.cregf = cregf;
    }

    public void crearEmpleadoMySQL()
    {
        String usuario = uregf.getText();
        String contraseña = cregf.getText();

        if (usuario.isEmpty() || contraseña.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Por favor, rellena todos los campos", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try
        {
            // Conexión a MySQL
            Connection cmysql = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);

            // Crear la consulta SQL para verificar si el usuario ya existe
            String query = "SELECT * FROM Empleados WHERE usuario = ?";
            PreparedStatement pstmt = cmysql.prepareStatement(query);
            pstmt.setString(1, usuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
            {
                // Si el usuario ya existe, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "Ese usuario ya está registrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                // Insertar el nuevo usuario
                String insertQuery = "INSERT INTO Empleados (usuario, contraseña) VALUES (?, ?)";
                pstmt = cmysql.prepareStatement(insertQuery);
                pstmt.setString(1, usuario);
                pstmt.setString(2, contraseña);
                pstmt.executeUpdate();

                uregf.setText("");
                cregf.setText("");
                JOptionPane.showMessageDialog(null, "Nuevo usuario creado exitosamente");
            }

            // Cerrar la conexión
            pstmt.close();
            cmysql.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}