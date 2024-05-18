import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;

public class TransObjectDBtoTXT {

    private EntityManager em;
    private String txtfile;

    public TransObjectDBtoTXT(EntityManager em, String txtfile) {
        this.em = em;
        this.txtfile = txtfile;
    }

    public void transferirMaterialesObjectDBtoTXT() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(txtfile));

            // Consulta la base de datos para obtener los materiales
            TypedQuery<Materiales> consulta = em.createQuery("SELECT m FROM Materiales m", Materiales.class);
            List<Materiales> materiales = consulta.getResultList();

            // Recorre la lista de materiales y escribe la información en el archivo
            for (Materiales material : materiales) {
                String otext = "'" + material.getNombre() + "', '" + material.getCantidad() + "', '" + material.getPrecioUnidad() + "'";
                bw.write(otext);
                bw.newLine();
            }

            // Cierra el BufferedWriter
            bw.close();

            // Muestra un mensaje de éxito
            JOptionPane.showMessageDialog(null, "Datos copiados al fichero '" + txtfile + "'", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ioe) {
            // Muestra un mensaje de error en caso de excepción de escritura
            JOptionPane.showMessageDialog(null, "Se ha producido un error de escritura", "Error", JOptionPane.ERROR_MESSAGE);
            // Imprime el mensaje de error en la consola
            System.err.println(ioe.getMessage());
        }
    }
}