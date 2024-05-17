import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;

public class MaterialEliminar {

    private EntityManager em;
    private GestionExistencias gestionExistencias;

    public MaterialEliminar(EntityManager em, GestionExistencias gestionExistencias) {
        this.em = em;
        this.gestionExistencias = gestionExistencias;
    }

    public void eliminarMaterial(Materiales materialSeleccionado) {
        try {
            if (materialSeleccionado == null) {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione un material.", "Material Incorrecto", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar " + materialSeleccionado.getNombre() + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                // Buscar el material en base al nombre
                TypedQuery<Materiales> query = em.createQuery("SELECT m FROM Materiales m WHERE m.nombre = :nombre", Materiales.class);
                query.setParameter("nombre", materialSeleccionado.getNombre());
                List<Materiales> resultados = query.getResultList();
                Materiales materialExistente = resultados.get(0);
                em.getTransaction().begin();
                em.remove(materialExistente);
                em.getTransaction().commit();
                gestionExistencias.transferirMaterialesObjectDBtoMySQL();
                gestionExistencias.actualizarComboBoxMateriales();
                gestionExistencias.agregarTextoHMF("Se ha eliminado el material " + materialSeleccionado + " con una cantidad de " + materialSeleccionado.getCantidad() + " y un valor de " + materialSeleccionado.getPrecioUnidad() + ".");
                gestionExistencias.actualizarTextoETF();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
