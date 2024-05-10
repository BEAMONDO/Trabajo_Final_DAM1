import javax.persistence.*;

// ObjectDB
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

// MySQL
@Entity
class MaterialMySQL {

    @Column(name = "nombre")
    private String nombre;
    @Column(name = "cantidad")
    private int cantidad;
    @Column(name = "precioUnidad")
    private double precioUnidad;

    // Getters y setters
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public double getPrecioUnidad() {
        return precioUnidad;
    }
    public void setPrecioUnidad(double precioUnidad) {
        this.precioUnidad = precioUnidad;
    }
}
