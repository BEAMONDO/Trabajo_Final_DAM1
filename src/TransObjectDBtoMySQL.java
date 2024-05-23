import java.util.*;
import javax.persistence.*;
import java.sql.*;

public class TransObjectDBtoMySQL 
{
    private String msqls;
    private String usuarioMySQL;
    private String contraseñaMySQL;
    private EntityManager em;

    public TransObjectDBtoMySQL(String msqls, String usuarioMySQL, String contraseñaMySQL, EntityManager em) 
    {
        this.msqls = msqls;
        this.usuarioMySQL = usuarioMySQL;
        this.contraseñaMySQL = contraseñaMySQL;
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public void transferirMaterialesObjectDBtoMySQL() 
    {
        try 
        {
            // Conexión a MySQL
            Connection cmysql = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);
            
            // Crear tabla MySQL
            Statement sentencia = cmysql.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS Materiales (" +
                                "nombre VARCHAR(255), " +
                                "cantidad INT, " +
                                "precioUnidad DOUBLE)";
            sentencia.execute(createTable);

            // Eliminación de materiales en MySQL
            Statement smysql = cmysql.createStatement();
            String sqlDelete = "DELETE FROM Materiales";
            smysql.executeUpdate(sqlDelete);

            // Consulta de materiales en ObjectDB
            Query consulta = em.createQuery("SELECT m FROM Materiales m", Materiales.class);
            List<Materiales> materiales = consulta.getResultList();

            // Inserción de materiales en MySQL
            for (Materiales material : materiales) 
            {
                String nombre = material.getNombre();
                int cantidad = material.getCantidad();
                double precioUnidad = material.getPrecioUnidad();
                String sqlInsert = "INSERT INTO Materiales (nombre, cantidad, precioUnidad) VALUES ('" +
                                    nombre + "', " + cantidad + ", " + precioUnidad + ")";
                smysql.executeUpdate(sqlInsert);
            }
            
            // Cierre de recursos
            smysql.close();
            cmysql.close();
        } 
        catch (SQLException ex)
        {
            // Imprimir el mensaje de error en la consola
            //System.err.println(ex.getMessage());
        }
    }
}