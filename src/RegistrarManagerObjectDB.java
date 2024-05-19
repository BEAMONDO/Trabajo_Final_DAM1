import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class RegistrarManagerObjectDB
{

    private EntityManager em;
    private JTextField uregf;
    private JTextField cregf;

    public RegistrarManagerObjectDB(EntityManager em, JTextField uregf, JTextField cregf)
    {
        this.em = em;
        this.uregf = uregf;
        this.cregf = cregf;
    }

    public void crearEmpleadoObjectDB()
    {
        String usuario = uregf.getText();
        String contraseña = cregf.getText();

        if (usuario.isEmpty() || contraseña.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Por favor, rellena todos los campos", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crear la consulta para verificar si el usuario ya existe
        TypedQuery<Empleados> query = em.createQuery("SELECT e FROM Empleados e WHERE e.usuario = :usuario", Empleados.class);
        query.setParameter("usuario", usuario);
        Empleados empleado = null;
        try
        {
            empleado = query.getSingleResult();
        }
        catch (NoResultException ex) {}

        if (empleado != null)
        {
            // Si el usuario ya existe, mostrar un mensaje de error
            JOptionPane.showMessageDialog(null, "Ese usuario ya está registrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            // Insertar el nuevo usuario
            em.getTransaction().begin();
            Empleados nuevoEmpleado = new Empleados();
            nuevoEmpleado.setUsuario(usuario);
            nuevoEmpleado.setContraseña(contraseña);
            em.persist(nuevoEmpleado);
            em.getTransaction().commit();
            uregf.setText("");
            cregf.setText("");
            JOptionPane.showMessageDialog(null, "Nuevo usuario creado exitosamente");
        }
    }
}