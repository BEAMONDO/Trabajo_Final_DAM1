import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

public class TransTXTtoObjectDB {

    private EntityManager em;
    private GestionExistencias gestionExistencias;
    private String txtfile;

    public TransTXTtoObjectDB(EntityManager em, GestionExistencias gestionExistencias, String txtfile) {
        this.em = em;
        this.gestionExistencias = gestionExistencias;
        this.txtfile = txtfile;
    }

    public void cargarMaterialesTXTtoObjectDB() {
        try {
            em.getTransaction().begin();

            // Crear un lector de archivos para leer el archivo de texto
            BufferedReader br = new BufferedReader(new FileReader(txtfile));

            // Leer cada línea del archivo de texto y procesarla
            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir la línea en partes separadas por comas
                String[] partes = linea.split(", ");

                // Obtener los valores de nombre, cantidad y precioUnidad
                String nombre = partes[0].substring(1, partes[0].length() - 1);
                int cantidad = Integer.parseInt(partes[1].substring(1, partes[1].length() - 1));
                double precioUnidad = Double.parseDouble(partes[2].substring(1, partes[2].length() - 1));

                // Crear una instancia de Material y establecer sus atributos
                Materiales material = new Materiales();
                material.setNombre(nombre);
                material.setCantidad(cantidad);
                material.setPrecioUnidad(precioUnidad);

                // Persistir el objeto Material en la base de datos ObjectDB
                em.persist(material);
            }

            // Commit de la transacción
            em.getTransaction().commit();

            // Cerrar recursos
            br.close();

            gestionExistencias.actualizarTextoETF();
            gestionExistencias.actualizarComboBoxMateriales();
            // Mostrar un mensaje de éxito
            JOptionPane.showMessageDialog(null, "Datos cargados desde el fichero '" + txtfile + "' a la base de datos ObjectDB", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | NumberFormatException e) {
            // Mostrar un mensaje de error en caso de excepción
            JOptionPane.showMessageDialog(null, "Se ha producido un error", "Error", JOptionPane.ERROR_MESSAGE);
            // Imprimir el mensaje de error en la consola
            System.err.println(e.getMessage());
        }
    }
}