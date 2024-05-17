import javax.persistence.*;
import javax.swing.JOptionPane;

public class LoginManagerObjectDB {

    private EntityManager em;

    public LoginManagerObjectDB(EntityManagerFactory emf) {
        this.em = emf.createEntityManager();
    }

    public boolean comprobacionLoginEmpleadoObjectDB(String usuario, String contraseña) {
        try {
            // Crear la consulta
            TypedQuery<Empleados> query = em.createQuery("SELECT e FROM Empleados e WHERE e.usuario = :usuario AND e.contraseña = :contraseña", Empleados.class);
            query.setParameter("usuario", usuario);
            query.setParameter("contraseña", contraseña);

            // Ejecutar la consulta
            Empleados empleado = query.getSingleResult();

            // Si se encuentra una coincidencia, retornar true
            return empleado != null;
        } catch (NoResultException ex) {
            // Si no se encuentra ninguna coincidencia, mostrar un mensaje de error
            JOptionPane.showMessageDialog(null, "El usuario o la contraseña son incorrectos");
            return false;
        }
    }
}
