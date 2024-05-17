import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class MaterialRegistrar {

    private EntityManager em;
    private String msqls, usuarioMySQL, contraseñaMySQL;
    private GestionExistencias gestionExistencias; // Campo de instancia para gestionExistencias
    private JTextField nrf, crf, ppurf; // Campos de instancia para los JTextField

    // Modificar el constructor para aceptar gestionExistencias y los JTextField como parámetros
    public MaterialRegistrar(EntityManager em, String msqls, String usuarioMySQL, String contraseñaMySQL, GestionExistencias gestionExistencias, JTextField nrf, JTextField crf, JTextField ppurf) {
        this.em = em;
        this.msqls = msqls;
        this.usuarioMySQL = usuarioMySQL;
        this.contraseñaMySQL = contraseñaMySQL;
        this.gestionExistencias = gestionExistencias; // Asignar gestionExistencias al campo de instancia
        this.nrf = nrf; // Asignar nrf al campo de instancia
        this.crf = crf; // Asignar crf al campo de instancia
        this.ppurf = ppurf; // Asignar ppurf al campo de instancia
    }

    public void registrarMaterial(String nombre, String cantidad, String precioUnidadStr) {
        try {
            if (cantidad.isEmpty() || precioUnidadStr.isEmpty()) {
                gestionExistencias.rellenarCampos(); // Usar gestionExistencias
                return;
            }

            int cantidadAnnadir = Integer.parseInt(cantidad);
            double precioUnidadRegistrar = Double.parseDouble(precioUnidadStr);
            if (cantidadAnnadir < 0 || precioUnidadRegistrar < 0) {
                gestionExistencias.cantidadYPrecioIncorrecto(); // Usar gestionExistencias
                return;
            }

            TypedQuery<Materiales> query = em.createQuery("SELECT m FROM Materiales m WHERE m.nombre = :nombre", Materiales.class);
            query.setParameter("nombre", nombre);
            List<Materiales> results = query.getResultList();
            if (!results.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El material ya existe.");
                return;
            }

            Materiales material = new Materiales(nombre, cantidadAnnadir, precioUnidadRegistrar);
            em.getTransaction().begin();
            em.persist(material);
            em.getTransaction().commit();

            // Crear tabla MySQL
            try {
                Connection conexion = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);
                Statement sentencia = conexion.createStatement();
                String createTable = "CREATE TABLE IF NOT EXISTS Materiales (" +
                        "nombre VARCHAR(255), " +
                        "cantidad INT, " +
                        "precioUnidad DOUBLE) " +
                        "ENGINE=INNODB";
                sentencia.execute(createTable);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al crear la tabla materiales en MySQL: \n" + e.getMessage());
            }

            // Vaciar los JTextField
            nrf.setText("");
            crf.setText("");
            ppurf.setText("");

            // Transferir materiales de ObjectDB a MySQL
            gestionExistencias.transferirMaterialesObjectDBtoMySQL();

            gestionExistencias.actualizarComboBoxMateriales();
            gestionExistencias.agregarTextoHMF("Nuevo material: " + nombre + ", Nueva cantidad: " + cantidadAnnadir + ", Nuevo precio: " + precioUnidadStr + "€ por cada unidad.");
            gestionExistencias.actualizarTextoETF();
        } catch (NumberFormatException e) {
            gestionExistencias.cantidadYPrecioIncorrecto(); // Usar gestionExistencias
        }
    }
}