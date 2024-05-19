import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.JTextField;

public class MaterialAgregar 
{

    private EntityManager em;
    private GestionExistencias gestionExistencias;
    private TransObjectDBtoMySQL transObjectDBtoMySQL;
    private JTextField caf;

    public MaterialAgregar(EntityManager em, GestionExistencias gestionExistencias, JTextField caf, TransObjectDBtoMySQL transObjectDBtoMySQL) 
    {
        this.em = em;
        this.gestionExistencias = gestionExistencias;
        this.caf = caf;
        this.transObjectDBtoMySQL = transObjectDBtoMySQL;
    }

    public void agregarMaterial(Materiales materialSeleccionado, String cantidad) 
    {
        try 
        {
            if (materialSeleccionado == null || cantidad.isEmpty()) 
            {
                gestionExistencias.rellenarCampos();
                return;
            }

            int cantidadAnnadir = Integer.parseInt(cantidad);
            if (cantidadAnnadir <= 0) 
            {
                gestionExistencias.cantidadIncorrecto();
                return;
            }

            em.getTransaction().begin();
            // Buscar el material en base al nombre
            TypedQuery<Materiales> query = em.createQuery("SELECT m FROM Materiales m WHERE m.nombre = :nombre", Materiales.class);
            query.setParameter("nombre", materialSeleccionado.getNombre());
            List<Materiales> resultados = query.getResultList();
            Materiales materialExistente = resultados.get(0);
            materialExistente.setCantidad(materialExistente.getCantidad() + cantidadAnnadir);
            em.getTransaction().commit();
            transObjectDBtoMySQL.transferirMaterialesObjectDBtoMySQL();

            gestionExistencias.actualizarComboBoxMateriales();
            gestionExistencias.agregarTextoHMF("Al material " + materialSeleccionado + " se le ha agregado una cantidad de " + cantidad + ". Nueva cantidad: " + materialExistente.getCantidad() + ".");
            gestionExistencias.actualizarTextoETF();
            caf.setText("");
        } 
        catch (NumberFormatException e) 
        {
            gestionExistencias.cantidadIncorrecto();
            return;
        }
    }
}