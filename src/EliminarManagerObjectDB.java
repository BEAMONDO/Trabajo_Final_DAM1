import javax.persistence.*;
import javax.swing.*;

public class EliminarManagerObjectDB
{

    private EntityManager em;
    private JTextField uregf;
    private JTextField cregf;

    public EliminarManagerObjectDB(EntityManager em, JTextField uregf, JTextField cregf)
    {
        this.em = em;
        this.uregf = uregf;
        this.cregf = cregf;
    }

    public void eliminarEmpleadoObjectDB() 
    {
        String usuario = uregf.getText();

        if (usuario.isEmpty()) 
        {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa el usuario a eliminar", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crear la consulta para verificar si el usuario existe
        TypedQuery<Empleados> query = em.createQuery("SELECT e FROM Empleados e WHERE e.usuario = :usuario", Empleados.class);
        query.setParameter("usuario", usuario);

        Empleados empleado = null;
        try
        {
            empleado = query.getSingleResult();
        } 
        catch (NoResultException ex) 
        {
            // Si el usuario no existe, mostrar un mensaje de error
            JOptionPane.showMessageDialog(null, "El usuario no existe", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si el usuario existe, proceder a eliminarlo
        em.getTransaction().begin();
        em.remove(empleado);
        em.getTransaction().commit();
        uregf.setText("");
        cregf.setText("");
        JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente");
    }
}