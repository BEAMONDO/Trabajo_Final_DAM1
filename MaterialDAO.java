import java.util.List;
import javax.persistence.*;

@Entity
class Material 
{
    String nombre;
    int cantidad;
    double precioUnidad;

    public Material(String nombre, int cantidad, double precioUnidad) 
    {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUnidad = precioUnidad;
    }

    // Constructor vacío requerido por JPA
    public Material() {}

    // Getters y setters
    public String getNombre()
    {
        return nombre;
    }
    public void setNombre(String nombre) 
    {
        this.nombre = nombre;
    }
    public int getCantidad() 
    {
        return cantidad;
    }
    public void setCantidad(int cantidad) 
    {
        this.cantidad = cantidad;
    }
    public double getPrecioUnidad() 
    {
        return precioUnidad;
    }
    public void setPrecioUnidad(double precioUnidad) 
    {
        this.precioUnidad = precioUnidad;
    }

    @Override
    public String toString() 
    {
        return nombre;
    }
}

public class MaterialDAO 
{
    private EntityManagerFactory emf;
    private EntityManager em;

    public MaterialDAO() 
    {
        emf = Persistence.createEntityManagerFactory("$objectdb/db/existencias.odb");
        em = emf.createEntityManager();
    }

    public void registrarMaterial(Material material) 
    {
        em.getTransaction().begin();
        em.persist(material);
        em.getTransaction().commit();
    }
    public void agregarCantidad(Material material, int cantidad) 
    {
        em.getTransaction().begin();
        material.setCantidad(material.getCantidad() + cantidad);
        em.getTransaction().commit();
    }
    public void editarMaterial(Material material, int nuevaCantidad, double nuevoPrecio) 
    {
        em.getTransaction().begin();
        material.setCantidad(nuevaCantidad);
        material.setPrecioUnidad(nuevoPrecio);
        em.getTransaction().commit();
    }
    public void editarMaterial2(Material material, int nuevaCantidad) 
    {
        em.getTransaction().begin();
        material.setCantidad(nuevaCantidad);
        em.getTransaction().commit();
    }
    public void editarMaterial3(Material material, double nuevoPrecio) 
    {
        em.getTransaction().begin();
        material.setPrecioUnidad(nuevoPrecio);
        em.getTransaction().commit();
    }
    public void eliminarMaterial(Material material) 
    {
        em.getTransaction().begin();
        em.remove(material);
        em.getTransaction().commit();
    }
    public List<Material> mostrarMateriales() 
    {
        TypedQuery<Material> query = em.createQuery("SELECT m FROM Material m", Material.class);
        return query.getResultList();
    }
    public void nuevoNombre(Material material, String nuevoNombre) 
    {
        em.getTransaction().begin();
        material.setNombre(nuevoNombre);
        em.getTransaction().commit();
    }
    public void retirarMaterial(Material materialSeleccionado, int cantidadBorrar) 
    {
        em.getTransaction().begin();
        materialSeleccionado.setCantidad(materialSeleccionado.getCantidad() - cantidadBorrar);
        em.getTransaction().commit();
    }
}
