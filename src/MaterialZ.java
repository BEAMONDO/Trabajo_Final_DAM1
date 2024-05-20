import javax.persistence.*;

// ObjectDB Materiales
@Entity
class Materiales 
{
    String nombre;
    int cantidad;
    double precioUnidad;

    public Materiales(String nombre, int cantidad, double precioUnidad) 
    {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUnidad = precioUnidad;
    }

    // Constructor vacío requerido por JPA
    public Materiales() {}

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

// ObjectDB Empleados
@Entity
class Empleados
{
    String usuario;
    String contraseña;

    public Empleados(String usuario, String contraseña)
    {
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    // Constructor vacío requerido por JPA
    public Empleados() {}

    // Getters y setters
    public String getUsuario()
    {
        return usuario;
    }
    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }
    public String getContraseña()
    {
        return contraseña;
    }
    public void setContraseña(String contraseña)
    {
        this.contraseña = contraseña;
    }
}

// MySQL Materiales
@Entity
class MaterialMySQL
{
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "cantidad")
    private int cantidad;
    @Column(name = "precioUnidad")
    private double precioUnidad;

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
}

// MySQL Empleados
@Entity
class EmpleadoMySQL
{
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "contraseña")
    private String contraseña;

    // Getters y setters
    public String getUsuario()
    {
        return usuario;
    }
    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }
    public String getContraseña()
    {
        return contraseña;
    }
    public void setContraseña(String contraseña)
    {
        this.contraseña = contraseña;
    }
}