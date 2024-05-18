import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.JTextField;

public class MaterialEditar {

    private EntityManager em;
    private GestionExistencias gestionExistencias;
    private JTextField ceditf, ppueditf;

    public MaterialEditar(EntityManager em, GestionExistencias gestionExistencias, JTextField ceditf, JTextField ppueditf) {
        this.em = em;
        this.gestionExistencias = gestionExistencias;
        this.ceditf = ceditf;
        this.ppueditf = ppueditf;
    }

    public void editarMaterial(Materiales materialSeleccionado, String cantidadStr, String nuevoPrecioStr) {
        try {
            if (materialSeleccionado == null) {
                gestionExistencias.rellenarCamposEditar();
                return;
            }
            if (cantidadStr.isEmpty() && nuevoPrecioStr.isEmpty()) {
                gestionExistencias.rellenarCamposEditar();
                return;
            }

            // Verificar si la cantidad no es un número
            if (!cantidadStr.isEmpty()) {
                try {
                    Integer.parseInt(cantidadStr);
                } catch (NumberFormatException e) {
                    gestionExistencias.cantidadIncorrecto();
                    return;
                }
            }

            // Verificar si el nuevo precio no es un número
            if (!nuevoPrecioStr.isEmpty()) {
                try {
                    Double.parseDouble(nuevoPrecioStr);
                } catch (NumberFormatException e) {
                    gestionExistencias.precioIncorrecto();
                    return;
                }
            }

            if (cantidadStr.isEmpty() && !nuevoPrecioStr.isEmpty()) {
                double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
                if (nuevoPrecio < 0) {
                    gestionExistencias.precioIncorrecto();
                    return;
                }
                em.getTransaction().begin();
                // Buscar el material en base al nombre
                TypedQuery<Materiales> query = em.createQuery("SELECT m FROM Materiales m WHERE m.nombre = :nombre", Materiales.class);
                query.setParameter("nombre", materialSeleccionado.getNombre());
                List<Materiales> resultados = query.getResultList();
                Materiales materialExistente = resultados.get(0);
                materialExistente.setPrecioUnidad(nuevoPrecio);
                em.getTransaction().commit();
                gestionExistencias.transferirMaterialesObjectDBtoMySQL();
                ppueditf.setText("");
                gestionExistencias.agregarTextoHMF("El material " + materialSeleccionado.getNombre() + " ha sido editado. Nuevo precio: " + nuevoPrecio + "€ por cada unidad.");
                gestionExistencias.actualizarTextoETF();
                return;
            }

            if (!cantidadStr.isEmpty() && nuevoPrecioStr.isEmpty()) {
                int nuevaCantidad = Integer.parseInt(cantidadStr);
                if (nuevaCantidad < 0) {
                    gestionExistencias.cantidadIncorrecto();
                    return;
                }
                em.getTransaction().begin();
                // Buscar el material en base al nombre
                TypedQuery<Materiales> query = em.createQuery("SELECT m FROM Materiales m WHERE m.nombre = :nombre", Materiales.class);
                query.setParameter("nombre", materialSeleccionado.getNombre());
                List<Materiales> resultados = query.getResultList();
                Materiales materialExistente = resultados.get(0);
                materialExistente.setCantidad(nuevaCantidad);
                em.getTransaction().commit();
                gestionExistencias.transferirMaterialesObjectDBtoMySQL();
                ceditf.setText("");
                gestionExistencias.agregarTextoHMF("El material " + materialSeleccionado.getNombre() + " ha sido editado. Nueva cantidad: " + nuevaCantidad + ".");
                gestionExistencias.actualizarTextoETF();
                return;
            }

            int nuevaCantidad = Integer.parseInt(cantidadStr);
            double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
            if (nuevaCantidad < 0 || nuevoPrecio < 0) {
                gestionExistencias.cantidadYPrecioIncorrecto();
                return;
            }
            em.getTransaction().begin();
            // Buscar el material en base al nombre
            TypedQuery<Materiales> query = em.createQuery("SELECT m FROM Materiales m WHERE m.nombre = :nombre", Materiales.class);
            query.setParameter("nombre", materialSeleccionado.getNombre());
            List<Materiales> resultados = query.getResultList();
            Materiales materialExistente = resultados.get(0);
            materialExistente.setCantidad(nuevaCantidad);
            materialExistente.setPrecioUnidad(nuevoPrecio);
            em.getTransaction().commit();
            gestionExistencias.transferirMaterialesObjectDBtoMySQL();
            ceditf.setText("");
            ppueditf.setText("");
            gestionExistencias.agregarTextoHMF("El material " + materialSeleccionado.getNombre() + " ha sido editado. Nueva cantidad: " + nuevaCantidad + ", Nuevo precio: " + nuevoPrecio + "€ por cada unidad.");
            gestionExistencias.actualizarTextoETF();
        } catch (NumberFormatException e) {
            gestionExistencias.cantidadYPrecioIncorrecto();
            return;
        }
    }

}