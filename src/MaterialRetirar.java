import java.util.*;
import javax.persistence.*;
import javax.swing.*;

public class MaterialRetirar 
{

    private EntityManager em;
    private GestionExistencias gestionExistencias;
    private TransObjectDBtoMySQL transObjectDBtoMySQL;
    private JTextField cbf;

    public MaterialRetirar(EntityManager em, GestionExistencias gestionExistencias, JTextField cbf, TransObjectDBtoMySQL transObjectDBtoMySQL) 
    {
        this.em = em;
        this.gestionExistencias = gestionExistencias;
        this.cbf = cbf;
        this.transObjectDBtoMySQL = transObjectDBtoMySQL;
    }

    public void retirarMaterial(Materiales materialSeleccionado, String cantidad) 
    {
        try 
        {
            if (materialSeleccionado == null || cantidad.isEmpty()) 
            {
                gestionExistencias.rellenarCampos();
                return;
            }

            int cantidadBorrar = Integer.parseInt(cantidad);
            if (cantidadBorrar <= 0) 
            {
                gestionExistencias.cantidadIncorrecto();
                return;
            }
            if (cantidadBorrar > materialSeleccionado.getCantidad()) 
            {
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
            transObjectDBtoMySQL.transferirMaterialesObjectDBtoMySQL();
            cbf.setText("");
            gestionExistencias.agregarTextoHMF("Al material " + materialSeleccionado.getNombre() + " se le ha retirado una cantidad de " + cantidad + ". Nueva cantidad: " + materialExistente.getCantidad() + ".");
            gestionExistencias.actualizarTextoETF();
            gestionExistencias.actualizarComboBoxMateriales();
        } 
        catch (NumberFormatException e) 
        {
            gestionExistencias.cantidadIncorrecto();
            return;
        }
    }

}