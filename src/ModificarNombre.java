import java.util.*;
import javax.persistence.*;
import javax.swing.*;

public class ModificarNombre 
{

    private EntityManager em;
    private GestionExistencias gestionExistencias;
    private TransObjectDBtoMySQL transObjectDBtoMySQL;
    private JTextField ncf;

    public ModificarNombre(EntityManager em, GestionExistencias gestionExistencias, JTextField ncf, TransObjectDBtoMySQL transObjectDBtoMySQL) 
    {
        this.em = em;
        this.gestionExistencias = gestionExistencias;
        this.ncf = ncf;
        this.transObjectDBtoMySQL = transObjectDBtoMySQL;
    }

    public void modificarNombre(Materiales materialSeleccionado, String nuevoNombre)
    {
        try 
        {
            if (materialSeleccionado == null || nuevoNombre.isEmpty()) 
            {
                gestionExistencias.rellenarCampos();
                return;
            }

            // Verificar si el nuevo nombre ya existe
            TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(m) FROM Materiales m WHERE m.nombre = :nombre", Long.class);
            countQuery.setParameter("nombre", nuevoNombre);
            long count = countQuery.getSingleResult();
            if (count > 0) 
            {
                JOptionPane.showMessageDialog(null, "El nombre '" + nuevoNombre + "' ya está en uso.", "Nombre duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombreAnterior = materialSeleccionado.getNombre(); // Guardar el nombre anterior
            em.getTransaction().begin();
            // Buscar el material en base al nombre
            TypedQuery<Materiales> query = em.createQuery("SELECT m FROM Materiales m WHERE m.nombre = :nombre", Materiales.class);
            query.setParameter("nombre", materialSeleccionado.getNombre());
            List<Materiales> resultados = query.getResultList();
            Materiales materialExistente = resultados.get(0);
            materialExistente.setNombre(nuevoNombre);
            em.getTransaction().commit();
            transObjectDBtoMySQL.transferirMaterialesObjectDBtoMySQL();
            ncf.setText("");
            gestionExistencias.actualizarComboBoxMateriales();
            gestionExistencias.agregarTextoHMF("Se ha modificado el nombre del material " + nombreAnterior + ", Nuevo nombre: " + nuevoNombre + ".");
            gestionExistencias.actualizarTextoETF();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}