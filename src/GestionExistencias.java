// Para que se vean las imagenes hay que cambiar la ruta
// de las imagenes, se encuentran en la linea 39 y 44
//
// Creado y pensado por BEAMONDO
//
// El codigo se encuentra publico aqui:
// https://github.com/BEAMONDO/Trabajo_Final_DAM1

import javax.persistence.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GestionExistencias extends JFrame 
{
    private EntityManagerFactory emf;
    private EntityManager em;
    private JTextField nrf, crf, ppurf, caf, cbf, ceditf, ppueditf, ncf;
    private JButton rb, ab, bb, eb, editb, ncb, bhb, eteb;
    private JTextArea hmf, etf;
    private JMenuItem ex, hi, ce;
    private JComboBox<Material> naf, nbf, nef, neditf, nof;
    private String usuarioMySQL = "root", contraseñaMySQL = "";

    public static void main(String args[]) 
    {
        GestionExistencias aplicacion = new GestionExistencias();
        aplicacion.setVisible(true);
    }

    public GestionExistencias() 
    {
        // Establecer imagen de fondo
        Imagenes fondo = new Imagenes("/home/david/eclipse-workspace/Pruebas_Trabajo_Final/imagenes/fondo.jpg");
        fondo.setLayout(null);
        setContentPane(fondo);

        // Cargar imagen de informacion
        ImageIcon iconoOriginal = new ImageIcon("/home/david/eclipse-workspace/Pruebas_Trabajo_Final/imagenes/info.png");
        Image imagenOriginal = iconoOriginal.getImage(); 
        Image imagenReescalada = imagenOriginal.getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Reescalar la imagen al tamaño deseado
        ImageIcon iconoReescalado = new ImageIcon(imagenReescalada); // Crear un nuevo ImageIcon con la imagen reescalada

//---------------------------------------- Menu superior ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
        // Crear una barra de menú        
        JMenuBar jmb = new JMenuBar();
        setJMenuBar(jmb);
        // Crear un menú
        JMenu menuOpciones = new JMenu("Opciones");
        menuOpciones.setMnemonic('O'); // Letra distinguida
        // Agregar elementos al menú
        menuOpciones.add(hi = new JMenuItem("Borrar el historial", 'B'));
        menuOpciones.add(ex = new JMenuItem("Eliminar todas las existencias", 'E'));
        jmb.add(menuOpciones);

        JMenu exitMenu = new JMenu("Salir");
        exitMenu.setMnemonic('S');
        jmb.add(exitMenu);
        exitMenu.add(ce = new JMenuItem("Cerrar", 'C'));

        //Agregar actionListener a los elementos del menú
        hi.addActionListener(new BorrarHistorialMenu());
        ex.addActionListener(new EliminarExistenciasMenu());
        ce.addActionListener(new CerrarMenu());
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Gui principal ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
        // Establecer el título y layout nulo para un posicionamiento absoluto de los componentes en la ventana
        setTitle("Gestión de Existencias - by DBSueños");
        setLayout(null);

        // Variables para facilitar la modificacion del tamaño y posicion de los componentes
        int alturaBoton = 200;
        int alturaBoton2 = 400;
        int largoBoton = 100;
        int largoTextos = 200;
        int altoCampos = 25;

        //Crear contenedores y elementos
        add(new JLabel("Registrar material: ")).setBounds(60, 40, largoTextos, altoCampos); // Establecer tamaño
        JLabel ir = new JLabel(iconoReescalado); // Crear y configurar el JLabel para mostrar la imagen reescalada
        ir.setBounds(240, 40, 20, 20); // Establecer posición y tamaño de la imagen
        fondo.add(ir); // Agregar la imagen
        add(new JLabel("Nombre: ")).setBounds(60, 80, largoTextos, altoCampos); // Crear texto y establecer posición y tamaño
        nrf = new JTextField(); // Crear un campo
        nrf.setBounds(130, 80, 150, altoCampos); // Establecer posición y tamaño
        fondo.add(nrf); // Agregarlo
        add(new JLabel("Cantidad: ")).setBounds(60, 120, largoTextos, altoCampos); // Crear texto y establecer posición y tamaño
        crf = new JTextField(); // Crear un campo
        crf.setBounds(150, 120, 130, altoCampos);  // Establecer posición y tamaño
        fondo.add(crf); // Agregarlo
        add(new JLabel("Precio unidad: ")).setBounds(60, 160, largoTextos, altoCampos); // Crear texto y establecer posición y tamaño
        ppurf = new JTextField(); // Crear un campo
        ppurf.setBounds(170, 160, 110, altoCampos); // Establecer posición y tamaño
        fondo.add(ppurf); // Agregarlo
        rb = new JButton("Registrar"); // Crear un botón
        rb.setBounds(120, alturaBoton, largoBoton, altoCampos); // Establecer posición y tamaño
        fondo.add(rb); // Agregarlo

        add(new JLabel("Añadir material: ")).setBounds(340, 40, largoTextos, altoCampos);
        JLabel ia = new JLabel(iconoReescalado);
        ia.setBounds(520, 40, 20, 20);
        fondo.add(ia);
        add(new JLabel("Nombre: ")).setBounds(340, 80, largoTextos, altoCampos);
        naf = new JComboBox<>(); // Crear lista de almacen de elementos registrados
        naf.setBounds(410, 80, 150, altoCampos);
        fondo.add(naf);
        add(new JLabel("Cantidad: ")).setBounds(340, 120, largoTextos, altoCampos);
        caf = new JTextField();
        caf.setBounds(430, 120, 130, altoCampos);
        fondo.add(caf);
        ab = new JButton("Añadir");
        ab.setBounds(400, alturaBoton, largoBoton, altoCampos);
        fondo.add(ab);

        add(new JLabel("Editar material: ")).setBounds(620, 40, largoTextos, 25);
        JLabel iedit = new JLabel(iconoReescalado);
        iedit.setBounds(800, 40, 20, 20);
        fondo.add(iedit);
        add(new JLabel("Nombre: ")).setBounds(620, 80, largoTextos, altoCampos);
        neditf = new JComboBox<>();
        neditf.setBounds(690, 80, 150, altoCampos);
        fondo.add(neditf);
        add(new JLabel("Cantidad: ")).setBounds(620, 120, largoTextos, altoCampos);
        ceditf = new JTextField();
        ceditf.setBounds(710, 120, 130, altoCampos);
        fondo.add(ceditf);
        add(new JLabel("Precio unidad: ")).setBounds(620, 160, largoTextos, altoCampos);
        ppueditf = new JTextField();
        ppueditf.setBounds(730, 160, 110, altoCampos);
        fondo.add(ppueditf);
        editb = new JButton("Editar");
        editb.setBounds(680, alturaBoton, largoBoton, altoCampos);
        fondo.add(editb);

        add(new JLabel("Retirar material: ")).setBounds(60, 280, largoTextos, 25);
        JLabel ib = new JLabel(iconoReescalado); 
        ib.setBounds(240, 280, 20, 20);
        fondo.add(ib);
        add(new JLabel("Nombre: ")).setBounds(60, 320, largoTextos, altoCampos);
        nbf = new JComboBox<>();
        nbf.setBounds(130, 320, 150, altoCampos);
        fondo.add(nbf);
        add(new JLabel("Cantidad: ")).setBounds(60, 360, largoTextos, altoCampos);
        cbf = new JTextField();
        cbf.setBounds(150, 360, 130, altoCampos);
        fondo.add(cbf);
        bb = new JButton("Retirar");
        bb.setBounds(120, alturaBoton2, largoBoton, altoCampos);
        fondo.add(bb);

        add(new JLabel("Eliminar material: ")).setBounds(340, 280, largoTextos, altoCampos);
        JLabel ie = new JLabel(iconoReescalado); 
        ie.setBounds(520, 280, 20, 20);
        fondo.add(ie);
        add(new JLabel("Nombre: ")).setBounds(340, 320, largoTextos, altoCampos);
        nef = new JComboBox<>();
        nef.setBounds(410, 320, 150, altoCampos);
        fondo.add(nef);
        eb = new JButton("Eliminar");
        eb.setBounds(400, alturaBoton2, largoBoton, altoCampos);
        fondo.add(eb);

        add(new JLabel("Modificar nombre: ")).setBounds(620, 280, largoTextos, altoCampos);
        JLabel im = new JLabel(iconoReescalado); 
        im.setBounds(800, 280, 20, 20);
        fondo.add(im);
        add(new JLabel("Actual: ")).setBounds(620, 320, largoTextos, altoCampos);
        nof = new JComboBox<>();
        nof.setBounds(690, 320, 150, altoCampos);
        fondo.add(nof);
        add(new JLabel("Nuevo: ")).setBounds(620, 360, largoTextos, altoCampos);
        ncf = new JTextField();
        ncf.setBounds(690, 360, 150, altoCampos);
        fondo.add(ncf);
        ncb = new JButton("Modificar");
        ncb.setBounds(680, alturaBoton2, largoBoton, altoCampos);
        fondo.add(ncb);

        // Crear area de texto
        add(new JLabel("Historial de modificaciones: ")).setBounds(60, 500, largoTextos, altoCampos);
        JLabel ihm = new JLabel(iconoReescalado);
        ihm.setBounds(300, 500, 20, 20);
        fondo.add(ihm);
        hmf = new JTextArea(); // Crear un area
        hmf.setEditable(false); // Que no sea editable
        JScrollPane scrollPane = new JScrollPane(hmf); // Añadir scroll
        scrollPane.setBounds(60, 540, 780, 160); // Establecer posición y tamaño
        fondo.add(scrollPane); // Agregarlo
        bhb = new JButton("Borrar el historial");
        bhb.setBounds(350, 500, largoBoton+80, altoCampos);
        fondo.add(bhb);
        eteb = new JButton("Eliminar todas las existencias");
        eteb.setBounds(590, 500, largoBoton+150, altoCampos);
        //fondo.add(eteb);

        add(new JLabel("Existencias disponibles: ")).setBounds(920, 40, largoTextos, altoCampos);
        JLabel ied = new JLabel(iconoReescalado);
        ied.setBounds(1140, 40, 20, 20);
        fondo.add(ied);
        etf = new JTextArea();
        etf.setEditable(false);
        JScrollPane scrollPane2 = new JScrollPane(etf);
        scrollPane2.setBounds(920, 80, 250, 620);
        fondo.add(scrollPane2);

        // Configuración de ObjectDB
        emf = Persistence.createEntityManagerFactory("$objectdb/db/existencias.odb");
        em = emf.createEntityManager();

        // Configuración de ventana
        setSize(1230, 820); // Tamano
        setVisible(true); // Visible
        setResizable(false); // No redimensionable
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar al cerrar la ventana
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        // Actualizar
        actualizarComboBoxMateriales(); // Actualizar el JComboBox
        actualizarTextoETF(); // Actualizar el JTextArea de existencias
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Configuracion de botones ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
        // Que ocurre al pulsar cada botón
        rb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                registrarMaterial();
            }
        });
        ab.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                agregarMaterial();
            }
        });
        bb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                retirarMaterial();
            }
        });
        eb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                eliminarMaterial();
            }
        });
        editb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                editarMaterial();
            }
        });
        ncb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                modificarNombre();
            }
        });
        bhb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                borrarHistorial();
            }
        });
        eteb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                borrarTodasExistencias();
            }
        });

        // Agregar un MouseListener al JLabel para detectar clics
        ir.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                // Mostrar ventana de información al hacer clic en la imagen
                JOptionPane.showMessageDialog(null, "- Introduce el nombre, la cantidad y el precio por unidad de\ncada producto que quieras registrar.\n- Cada vez que registras un producto, aparecerá en la lista\nde existencias disponibles situada a la derecha.\n- En la zona inferior se mostrará un mensaje de registro.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        ia.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                JOptionPane.showMessageDialog(null, "- Introduce el nombre y la cantidad quieras añadir de cada producto.\n- Cada vez que añadas una cantidad, se actualiza inmediatamente\nen la lista de existencias disponibles.\n- En la zona inferior se mostrará un mensaje de añadido.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        iedit.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                JOptionPane.showMessageDialog(null, "- Introduce el nombre, la cantidad y el precio por unidad de\ncada producto que quieras editar.\n- Cada vez que editas una cantidad o precio, se actualiza\nen la lista de existencias disponibles.\n- En la zona inferior se mostrará un mensaje de edición.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        ib.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                JOptionPane.showMessageDialog(null, "- Introduce el nombre y la cantidad quieras retirar de cada producto.\n- Cada vez que retiras una cantidad, se actualiza inmediatamente\nen la lista de existencias disponibles.\n- En la zona inferior se mostrará un mensaje de retirada.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        ie.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                JOptionPane.showMessageDialog(null, "- Introduce el nombre del producto que quieras eliminar.\n- Cada vez que eliminas un producto, se actualiza inmediatamente\nen la lista de existencias disponibles.\n- En la zona inferior se mostrará un mensaje de eliminación.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        im.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                JOptionPane.showMessageDialog(null, "- Introduce el nombre actual y el nuevo nombre del producto que\nquieras cambiar el nombre.\n- Cada vez que cambias el nombre de un producto, se actualiza\ninmediatamente en la lista de existencias disponibles.\n- En la zona inferior se mostrará un mensaje de modificación.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        ihm.addMouseListener(new MouseAdapter()
         {
            @Override
            public void mouseClicked(MouseEvent e)
             {
                JOptionPane.showMessageDialog(null, "- Aqui aparecen todas las modificaciones realizadas desde que\nse ha iniciado la aplicacion.\n- Cada vez que hagas algún cambio se actualiza inmediatamente.\n- Al cerrar la aplicacion el historial se borra automaticamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        ied.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                JOptionPane.showMessageDialog(null, "- Aqui aparecen todas las existencias disponibles.\n- Cada vez que se realice un cambio se actualiza inmediatamente.\n- Al cerrar la aplicacion las existencias se guardan automaticamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Funciones extra ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    // Agregar modificaciones al area de historial de modificaciones
    private void agregarTextoHMF(String texto) 
    {
        hmf.append(texto + "\n");
    }

    // Agregar materiales existentes al area de existencias disponibles
    private void agregarTextoETF(String texto) 
    {
        etf.append(texto + "\n");
    }

    // Actualizar el JTextArea con la lista de existencias
    private void actualizarTextoETF() 
    {
        TypedQuery<Material> query = em.createQuery("SELECT m FROM Material m ORDER BY m.nombre ASC", Material.class);
        List<Material> existencias = query.getResultList();
        etf.setText(""); // Borrar el contenido actual de etf
        // Escribir todos los materiales existentes
        for (Material material : existencias) 
        {
            agregarTextoETF("  Nombre: " + material.nombre);
            agregarTextoETF("  Cantidad: " + material.cantidad);
            agregarTextoETF("  Precio por unidad: " + material.precioUnidad);
            agregarTextoETF("  Valor total: " + material.cantidad * material.precioUnidad);
            agregarTextoETF("---------------------------------------\n");
        }    
    }

    // Actualizar todos los JComboBox con los materiales existentes
    private void actualizarComboBoxMateriales() 
    {
        em.getTransaction().begin();
        TypedQuery<Material> query = em.createQuery("SELECT m FROM Material m", Material.class);
        List<Material> resultados = query.getResultList();
        em.getTransaction().commit();
        // Limpiar el JComboBox antes de agregar los nuevos materiales
        naf.removeAllItems();
        nbf.removeAllItems();
        nef.removeAllItems();
        neditf.removeAllItems();
        nof.removeAllItems();
        // Agregar materiales al JComboBox
        for (Material material : resultados) 
        {
            naf.addItem(material);
            nbf.addItem(material);
            nef.addItem(material);
            neditf.addItem(material);
            nof.addItem(material);
        }
    }

    // Borrar el historial de modificaciones
    private void borrarHistorial() 
    {
        // Mostrar ventana de confirmación
        int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas borrar el historial?", "Confirmar borrado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        // Verificar la opción seleccionada por el usuario
        if (opcion == JOptionPane.YES_OPTION) 
        {
            hmf.setText("");
        }
    }

    // Funciones con texto generico para facilitar la modificacion de los mensajes
    private void rellenarCampos()
    {
        JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
    }
    private void rellenarCamposEditar()
    {
        JOptionPane.showMessageDialog(this, "Por favor, rellena la cantidad, el precio o los dos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
    }
    private void cantidadYPrecioIncorrecto()
    {
        JOptionPane.showMessageDialog(this, "Por favor, introduce un número válido para la cantidad y para el precio.", "Cantidad o Precio Incorrecto", JOptionPane.WARNING_MESSAGE);    
    }
    private void cantidadIncorrecto()
    {
        JOptionPane.showMessageDialog(this, "Por favor, introduce un número válido para la cantidad.", "Cantidad Incorrecta", JOptionPane.WARNING_MESSAGE);
    }
    private void precioIncorrecto()
    {
        JOptionPane.showMessageDialog(this, "Por favor, introduce un número válido para el precio.", "Precio Incorrecto", JOptionPane.WARNING_MESSAGE);
    }
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Funcion registrar material ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void registrarMaterial() 
    {
        String nombre = nrf.getText();
        String cantidad = crf.getText();
        String precioUnidadStr = ppurf.getText();
        if (cantidad.isEmpty() || precioUnidadStr.isEmpty()) 
        {
            rellenarCampos();
            return;
        }
        try 
        {
            int cantidadAnnadir = Integer.parseInt(cantidad);
            double precioUnidadRegistrar = Double.parseDouble(precioUnidadStr);
            if (cantidadAnnadir < 0 || precioUnidadRegistrar < 0) 
            {
                cantidadYPrecioIncorrecto();
                return;
            }
            TypedQuery<Material> query = em.createQuery("SELECT m FROM Material m WHERE m.nombre = :nombre", Material.class);
            query.setParameter("nombre", nombre);
            List<Material> results = query.getResultList();
            if (!results.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "El material ya existe.");
                return;
            }
            Material material = new Material(nombre, cantidadAnnadir, precioUnidadRegistrar);
            em.getTransaction().begin();
            em.persist(material);
            em.getTransaction().commit();

            // Crear tabla MySQL
            try{
                //Abrir la conexión
                Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/existencias", usuarioMySQL, contraseñaMySQL);

                //Crear y ejecutar la sentencia SQL            
                Statement sentencia = conexion.createStatement();
                String createTable = "CREATE TABLE IF NOT EXISTS materiales (" +      
                        "nombre VARCHAR(255), " +
                        "cantidad INT, " +
                        "precioUnidad DOUBLE) " +
                        "ENGINE=INNODB";
                sentencia.execute(createTable);
                //System.out.println("Tabla creada correctamente.");
            }
            catch (SQLException e)
            {
                //e.printStackTrace();
                System.out.println("ERROR al conectar con la base de datos MySQL: \n\n" + e.getMessage());
            }

            // Transferir materiales de ObjectDB a MySQL
            transferirMaterialesObjectDBtoMySQL();
            nrf.setText("");
            crf.setText("");
            ppurf.setText("");
            actualizarComboBoxMateriales(); // Actualizar el JComboBox después de registrar el material
            agregarTextoHMF("Se ha registrado " + nombre + " con una cantidad de " + cantidadAnnadir + " y un valor de " + precioUnidadStr + " por cada unidad.");
            actualizarTextoETF();
        } 
        catch (NumberFormatException e) 
        {
            cantidadYPrecioIncorrecto();
            return;
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Funcion agregar material ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void agregarMaterial() 
    {
        Material materialSeleccionado = (Material) naf.getSelectedItem();
        String cantidad = caf.getText();
        if (materialSeleccionado == null || cantidad.isEmpty()) 
        {
            rellenarCampos();
            return;
        }
        try 
        {
            int cantidadAnnadir = Integer.parseInt(cantidad);
            if (cantidadAnnadir <= 0) 
            {
                cantidadIncorrecto();
                return;
            }
            
            em.getTransaction().begin();
            // Buscar el material en base al nombre
            TypedQuery<Material> query = em.createQuery("SELECT m FROM Material m WHERE m.nombre = :nombre", Material.class);
            query.setParameter("nombre", materialSeleccionado.getNombre());
            List<Material> resultados = query.getResultList();
            Material materialExistente = resultados.get(0);
            materialExistente.setCantidad(materialExistente.getCantidad() + cantidadAnnadir);
            em.getTransaction().commit();
            transferirMaterialesObjectDBtoMySQL();
            caf.setText("");
            agregarTextoHMF("A " + materialSeleccionado + " se le han añadido " + cantidad + ".");
            actualizarTextoETF();
        } 
        catch (NumberFormatException e) 
        {
            cantidadIncorrecto();
            return;
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Funcion editar material ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void editarMaterial() 
    {
        Material materialSeleccionado = (Material) neditf.getSelectedItem();
        String cantidadStr = ceditf.getText();    
        String nuevoPrecioStr = ppueditf.getText();
        
        if (materialSeleccionado == null) 
        {
            rellenarCamposEditar();
            return;
        }
        if (cantidadStr.isEmpty() && nuevoPrecioStr.isEmpty()) 
        {
            rellenarCamposEditar();
            return;
        }
        try 
        {
            // Verificar si la cantidad no es un número
            if (!cantidadStr.isEmpty()) 
            {
                try 
                {
                    Integer.parseInt(cantidadStr);
                } 
                catch (NumberFormatException e) 
                {
                    cantidadIncorrecto();
                    return;
                }
            }

            // Verificar si el nuevo precio no es un número
            if (!nuevoPrecioStr.isEmpty()) 
            {
                try {
                    Double.parseDouble(nuevoPrecioStr);
                } 
                catch (NumberFormatException e) 
                {
                    precioIncorrecto();
                    return;
                }
            }
            
            if (cantidadStr.isEmpty() && !nuevoPrecioStr.isEmpty()) 
            {
                double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
                if(nuevoPrecio < 0)
                {
                    precioIncorrecto();
                    return;
                }
                em.getTransaction().begin();
                // Buscar el material en base al nombre
                TypedQuery<Material> query = em.createQuery("SELECT m FROM Material m WHERE m.nombre = :nombre", Material.class);
                query.setParameter("nombre", materialSeleccionado.getNombre());
                List<Material> resultados = query.getResultList();
                Material materialExistente = resultados.get(0);
                materialExistente.setPrecioUnidad(nuevoPrecio);
                em.getTransaction().commit();
                transferirMaterialesObjectDBtoMySQL();
                ppueditf.setText("");
                agregarTextoHMF("Se ha editado el material " + materialSeleccionado.getNombre() + ". Nuevo precio por unidad: " + nuevoPrecio + ".");
                actualizarTextoETF();
                return;
            }

            if (!cantidadStr.isEmpty() && nuevoPrecioStr.isEmpty()) 
            {
                int nuevaCantidad = Integer.parseInt(cantidadStr);
                if(nuevaCantidad < 0)
                {
                    cantidadIncorrecto();
                    return;
                }
                em.getTransaction().begin();
                // Buscar el material en base al nombre
                TypedQuery<Material> query = em.createQuery("SELECT m FROM Material m WHERE m.nombre = :nombre", Material.class);
                query.setParameter("nombre", materialSeleccionado.getNombre());
                List<Material> resultados = query.getResultList();
                Material materialExistente = resultados.get(0);
                materialExistente.setCantidad(nuevaCantidad);
                em.getTransaction().commit();
                transferirMaterialesObjectDBtoMySQL();
                ceditf.setText("");
                agregarTextoHMF("Se ha editado el material " + materialSeleccionado.getNombre() + ". Nueva cantidad: " + nuevaCantidad + ".");
                actualizarTextoETF();
                return;
            }

            int nuevaCantidad = Integer.parseInt(cantidadStr);
            double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
            if (nuevaCantidad < 0 || nuevoPrecio < 0) 
            {
                cantidadYPrecioIncorrecto();
                return;
            }
            em.getTransaction().begin();
            // Buscar el material en base al nombre
            TypedQuery<Material> query = em.createQuery("SELECT m FROM Material m WHERE m.nombre = :nombre", Material.class);
            query.setParameter("nombre", materialSeleccionado.getNombre());
            List<Material> resultados = query.getResultList();
            Material materialExistente = resultados.get(0);
            materialExistente.setCantidad(nuevaCantidad);
            materialExistente.setPrecioUnidad(nuevoPrecio);
            em.getTransaction().commit();
            transferirMaterialesObjectDBtoMySQL();
            ceditf.setText("");
            ppueditf.setText("");
            agregarTextoHMF("Se ha editado el material " + materialSeleccionado.getNombre() + ". Nueva cantidad: " + nuevaCantidad + ", nuevo precio por unidad: " + nuevoPrecio + ".");
            actualizarTextoETF();
        } 
        catch (NumberFormatException e) 
        {
            cantidadYPrecioIncorrecto();
            return;
        }
    }

//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Funcion borrar material ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void retirarMaterial() 
    {
        Material materialSeleccionado = (Material) nbf.getSelectedItem();
        String cantidad = cbf.getText();
        if (materialSeleccionado == null || cantidad.isEmpty()) 
        {
            rellenarCampos();
            return;
        }
        try
        {
            int cantidadBorrar = Integer.parseInt(cantidad);
            if (cantidadBorrar <= 0) 
            {
                cantidadIncorrecto();
                return;
            }
            if (cantidadBorrar > materialSeleccionado.cantidad) 
            {
                JOptionPane.showMessageDialog(this, "No puedes retirar más cantidad de la que tienes.");
                return;
            }
            em.getTransaction().begin();
            // Buscar el material en base al nombre
            TypedQuery<Material> query = em.createQuery("SELECT m FROM Material m WHERE m.nombre = :nombre", Material.class);
            query.setParameter("nombre", materialSeleccionado.getNombre());
            List<Material> resultados = query.getResultList();
            Material material = resultados.get(0);
            material.setCantidad(material.getCantidad() - cantidadBorrar);
            em.getTransaction().commit();
            transferirMaterialesObjectDBtoMySQL();
            cbf.setText("");
            agregarTextoHMF("A " + materialSeleccionado + " se le han retirado " + cantidad + ".");
            actualizarTextoETF();
        } 
        catch (NumberFormatException e) 
        {
            cantidadIncorrecto();
            return;
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Funcion eliminar material ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void eliminarMaterial() 
    {
        Material materialSeleccionado = (Material) nef.getSelectedItem();
        if (materialSeleccionado == null) 
        {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un material.", "Material Incorrecto", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar " + materialSeleccionado.getNombre() + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) 
        {
            em.getTransaction().begin();
            // Buscar el material en base al nombre
            TypedQuery<Material> query = em.createQuery("SELECT m FROM Material m WHERE m.nombre = :nombre", Material.class);
            query.setParameter("nombre", materialSeleccionado.getNombre());
            List<Material> resultados = query.getResultList();
            Material material = resultados.get(0);
            em.remove(material);
            em.getTransaction().commit();
            transferirMaterialesObjectDBtoMySQL();
            actualizarComboBoxMateriales();
            agregarTextoHMF("Se ha eliminado " + materialSeleccionado + " con una cantidad de " + materialSeleccionado.getCantidad() + " y un valor de " + materialSeleccionado.getPrecioUnidad() + ".");
            actualizarTextoETF();
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Funcion cambiar nombre ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void modificarNombre() 
    {
        Material materialSeleccionado = (Material) nof.getSelectedItem();
        String nuevoNombre = ncf.getText();
        if (materialSeleccionado == null || nuevoNombre.isEmpty()) 
        {
            rellenarCampos();
            return;
        }
        // Verificar si el nuevo nombre ya existe
        TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(m) FROM Material m WHERE m.nombre = :nombre", Long.class);
        countQuery.setParameter("nombre", nuevoNombre);
        long count = countQuery.getSingleResult();
        if (count > 0) 
        {
            JOptionPane.showMessageDialog(this, "El nombre '" + nuevoNombre + "' ya está en uso.", "Nombre duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombreAnterior = materialSeleccionado.getNombre(); // Guardar el nombre anterior
        em.getTransaction().begin();
        // Buscar el material en base al nombre
        TypedQuery<Material> query = em.createQuery("SELECT m FROM Material m WHERE m.nombre = :nombre", Material.class);
        query.setParameter("nombre", materialSeleccionado.getNombre());
        List<Material> resultados = query.getResultList();
        Material materialExistente = resultados.get(0);
        materialExistente.setNombre(nuevoNombre);
        em.getTransaction().commit();
        transferirMaterialesObjectDBtoMySQL();
        ncf.setText("");
        actualizarComboBoxMateriales();
        agregarTextoHMF("Se ha modificado el nombre del material " + nombreAnterior + " a " + nuevoNombre + ".");
        actualizarTextoETF();
    }
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Funcion borrar todo existencias ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void borrarTodasExistencias() 
    {
        // Crear la ventana de confirmación
        int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar todas las existencias?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        // Verificar la opción seleccionada
        if (opcion == JOptionPane.YES_OPTION) 
        {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Material").executeUpdate();
            em.getTransaction().commit();
            transferirMaterialesObjectDBtoMySQL();
            hmf.setText("");
            actualizarComboBoxMateriales();
            actualizarTextoETF();
            JOptionPane.showMessageDialog(null, "Se han borrado todas las existencias.");
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Funcion transferir datos de ObjectDB a MySQL ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------

    @SuppressWarnings("unchecked")
    private void transferirMaterialesObjectDBtoMySQL() {
        // Variables para la conexión a ObjectDB
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/existencias.odb");
        EntityManager em = emf.createEntityManager();
        
        Connection conexionMySQL = null;
        Statement sentenciaMySQL = null;
        
        try {
            // Conexión a MySQL
            conexionMySQL = DriverManager.getConnection("jdbc:mysql://localhost/existencias", usuarioMySQL, contraseñaMySQL);
            
            //int materialeseliminados = 0; // Contador de materiales eliminados
            // Eliminacion de materiales en MySQL
            sentenciaMySQL = conexionMySQL.createStatement();
            String sqlDelete = "DELETE FROM materiales";
            sentenciaMySQL.executeUpdate(sqlDelete);
            //int filasAfectadasDelete = sentenciaMySQL.executeUpdate(sqlDelete);
            /*if (filasAfectadasDelete > 0) {
                materialeseliminados++;
            }*/
            //System.out.println("Se eliminaron " + materialeseliminados + " materiales de MySQL.");

            // Consulta de materiales en ObjectDB
            Query consulta = em.createQuery("SELECT m FROM Material m", Material.class);
            List<Material> materiales = consulta.getResultList();
            //System.out.println("Se encontraron " + materiales.size() + " materiales en ObjectDB:");
            /*for (Material material : materiales) {
                System.out.println(material);
            }*/
            
            //int materialesTransferidos = 0; // Contador de materiales transferidos
            // Inserción de materiales en MySQL
            sentenciaMySQL = conexionMySQL.createStatement();
            for (Material material : materiales) {
                String nombre = material.getNombre();
                int cantidad = material.getCantidad();
                double precioUnidad = material.getPrecioUnidad();
                String sqlInsert = "INSERT INTO materiales (nombre, cantidad, precioUnidad) VALUES ('" +
                                    nombre + "', " + cantidad + ", " + precioUnidad + ")";
                sentenciaMySQL.executeUpdate(sqlInsert);
                //int filasAfectadasInsert = sentenciaMySQL.executeUpdate(sqlInsert);
                // Incrementar el contador si la inserción fue exitosa
                /*if (filasAfectadasInsert > 0) {
                    materialesTransferidos++;
                }*/
            }
            
            sentenciaMySQL.close();
            conexionMySQL.close();
            em.close();
            emf.close();

            //System.out.println("Se transfirieron " + materialesTransferidos + " materiales de ObjectDB a MySQL.");
        } catch (SQLException e) {
            System.out.println("ERROR al conectar con la base de datos MySQL: \n\n" + e.getMessage());
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Menu salir/cerrar ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    class CerrarMenu implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                System.exit(0);
            }
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Menu opcciones/eliminar todas las existencias ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    class EliminarExistenciasMenu implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                borrarTodasExistencias();
            }
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------

//---------------------------------------- Menu opcciones/eliminar todas las existencias ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    class BorrarHistorialMenu implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                borrarHistorial();
            }
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------
}
