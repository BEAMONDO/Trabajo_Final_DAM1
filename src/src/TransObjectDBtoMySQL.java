import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JOptionPane;

public class TransObjectDBtoMySQL {

    private String msqls;
    private String usuarioMySQL;
    private String contraseñaMySQL;
    private EntityManager em;

    public TransObjectDBtoMySQL(String msqls, String usuarioMySQL, String contraseñaMySQL, EntityManager em) {
        this.msqls = msqls;
        this.usuarioMySQL = usuarioMySQL;
        this.contraseñaMySQL = contraseñaMySQL;
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public void transferirMaterialesObjectDBtoMySQL() {
        try {
            // Conexión a MySQL
            Connection cmysql = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);
            
            // Eliminación de materiales en MySQL
            Statement smysql = cmysql.createStatement();
            String sqlDelete = "DELETE FROM Materiales";
            smysql.executeUpdate(sqlDelete);

            // Consulta de materiales en ObjectDB
            Query consulta = em.createQuery("SELECT m FROM Materiales m", Materiales.class);
            List<Materiales> materiales = consulta.getResultList();

            // Inserción de materiales en MySQL
            for (Materiales material : materiales) {
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
            //JOptionPane.showMessageDialog(null, "Datos cargados desde ObjectDB a la base de datos MySQL", "Info", JOptionPane.INFORMATION_MESSAGE);
            //System.out.println("Se transfirieron " + materialesTransferidos + " materiales de ObjectDB a MySQL.");
        } catch (SQLException e) {
            // Mostrar un mensaje de error en caso de excepción
            JOptionPane.showMessageDialog(null, "Se ha producido un error", "Error", JOptionPane.ERROR_MESSAGE);
            // Imprimir el mensaje de error en la consola
            System.err.println(e.getMessage());
        }
    }
}