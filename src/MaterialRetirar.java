import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class MaterialRetirar {

    private EntityManager em;
    private GestionExistencias gestionExistencias;
    private JTextField cbf;

    public MaterialRetirar(EntityManager em, GestionExistencias gestionExistencias, JTextField cbf) {
        this.em = em;
        this.gestionExistencias = gestionExistencias;
        this.cbf = cbf;
    }

    public void retirarMaterial(Materiales materialSeleccionado, String cantidad) {
        try {
            if (materialSeleccionado == null || cantidad.isEmpty()) {
                gestionExistencias.rellenarCampos();
                return;
            }

            int cantidadBorrar = Integer.parseInt(cantidad);
            if (cantidadBorrar <= 0) {
                gestionExistencias.cantidadIncorrecto();
                return;
            }
            if (cantidadBorrar > materialSeleccionado.getCantidad()) {
                JOptionPane.showMessageDialog(null, "No puedes retirar más cantidad de la que tienes.");
                return;
            }
            em.getTransaction().begin();
            // Buscar el material en base al nombre
            TypedQuery<Materiales> query = em.createQuery("SELECT m FROM Materiales m WHERE m.nombre = :nombre", Materiales.class);
            query.setParameter("nombre", materialSeleccionado.getNombre());
            List<Materiales> resultados = query.getResultList();
            Materiales materialExistente = resultados.get(0);
            materialExistente.setCantidad(materialExistente.getCantidad() - cantidadBorrar);
            em.getTransaction().commit();
            gestionExistencias.transferirMaterialesObjectDBtoMySQL();
            cbf.setText("");
            gestionExistencias.agregarTextoHMF("Al material " + materialSeleccionado.getNombre() + " se le ha retirado una cantidad de " + cantidad + ". Nueva cantidad: " + materialExistente.getCantidad() + ".");
            gestionExistencias.actualizarTextoETF();
        } catch (NumberFormatException e) {
            gestionExistencias.cantidadIncorrecto();
            return;
        }
    }

}