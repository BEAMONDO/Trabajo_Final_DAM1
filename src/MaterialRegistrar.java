import java.util.*;
import javax.persistence.*;
import javax.swing.*;

public class MaterialRegistrar 
{
    private EntityManager em;
    private GestionExistencias gestionExistencias;
    private TransObjectDBtoMySQL transObjectDBtoMySQL;
    private JTextField nrf, crf, ppurf;

    public MaterialRegistrar(EntityManager em, GestionExistencias gestionExistencias, JTextField nrf, JTextField crf, JTextField ppurf, TransObjectDBtoMySQL transObjectDBtoMySQL) 
    {
        this.em = em;
        this.gestionExistencias = gestionExistencias; 
        this.nrf = nrf; 
        this.crf = crf;
        this.ppurf = ppurf;
        this.transObjectDBtoMySQL = transObjectDBtoMySQL;
    }

    public void registrarMaterial(String nombre, String cantidad, String precioUnidadStr) 
    {
        try 
        {
            if (cantidad.isEmpty() || precioUnidadStr.isEmpty()) 
            {
                gestionExistencias.rellenarCampos();
                return;
            }

            int cantidadAnnadir = Integer.parseInt(cantidad);
            double precioUnidadRegistrar = Double.parseDouble(precioUnidadStr);
            if (cantidadAnnadir < 0 || precioUnidadRegistrar < 0) 
            {
                gestionExistencias.cantidadYPrecioIncorrecto();
                return;
            }

            TypedQuery<Materiales> query = em.createQuery("SELECT m FROM Materiales m WHERE m.nombre = :nombre", Materiales.class);
            query.setParameter("nombre", nombre);
            List<Materiales> results = query.getResultList();
            if (!results.isEmpty()) 
            {
                JOptionPane.showMessageDialog(null, "El material ya existe.");
                return;
            }

            Materiales material = new Materiales(nombre, cantidadAnnadir, precioUnidadRegistrar);
            em.getTransaction().begin();
            em.persist(material);
            em.getTransaction().commit();

            nrf.setText("");
            crf.setText("");
            ppurf.setText("");

            transObjectDBtoMySQL.transferirMaterialesObjectDBtoMySQL();

            gestionExistencias.actualizarComboBoxMateriales();
            gestionExistencias.agregarTextoHMF("Nuevo material: " + nombre + ", Nueva cantidad: " + cantidadAnnadir + ", Nuevo precio: " + precioUnidadStr + "€ por cada unidad.");
            gestionExistencias.actualizarTextoETF();
        } 
        catch (NumberFormatException e) 
        {
            gestionExistencias.cantidadYPrecioIncorrecto();
        }
    }
}