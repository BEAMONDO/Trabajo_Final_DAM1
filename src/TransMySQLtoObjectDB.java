import javax.persistence.*;
import java.sql.*;
import javax.swing.*;

public class TransMySQLtoObjectDB 
{

    private String msqls;
    private String usuarioMySQL;
    private String contraseñaMySQL;
    private EntityManager em;
    private GestionExistencias gestionExistencias;

    public TransMySQLtoObjectDB(String msqls, String usuarioMySQL, String contraseñaMySQL, EntityManager em, GestionExistencias gestionExistencias) 
    {
        this.msqls = msqls;
        this.usuarioMySQL = usuarioMySQL;
        this.contraseñaMySQL = contraseñaMySQL;
        this.em = em;
        this.gestionExistencias = gestionExistencias;
    }

    public void transferirMaterialesMySQLtoObjectDB() 
    {
        try 
        {
            // Conexión a MySQL
            Connection cmysql = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);

            // Consulta de materiales en MySQL
            Statement smysql = cmysql.createStatement();
            String sqlSelect = "SELECT * FROM Materiales";
            ResultSet resultado = smysql.executeQuery(sqlSelect);

            // Eliminar todos los materiales existentes en ObjectDB
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Materiales").executeUpdate();
            em.getTransaction().commit();

            // Inserción de materiales en ObjectDB
            em.getTransaction().begin();
            while (resultado.next()) 
            {
                String nombre = resultado.getString("nombre");
                int cantidad = resultado.getInt("cantidad");
                double precioUnidad = resultado.getDouble("precioUnidad");

                Materiales material = new Materiales(nombre, cantidad, precioUnidad);
                em.persist(material);
            }
            em.getTransaction().commit();

            // Cierre de recursos
            resultado.close();
            smysql.close();
            cmysql.close();

            gestionExistencias.actualizarComboBoxMateriales();
            gestionExistencias.actualizarTextoETF();
        } 
        catch (SQLException ex) 
        {
            // Mostrar un mensaje de error en caso de excepción
            JOptionPane.showMessageDialog(null, "Se ha producido un error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // Imprimir el mensaje de error en la consola
            //System.err.println(ex.getMessage());
        }
    }
}