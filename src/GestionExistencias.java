// Creado y pensado por BEAMONDO
//
// El codigo se encuentra publico aqui:
// https://github.com/BEAMONDO/Trabajo_Final_DAM1

import javax.persistence.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.List;

public class GestionExistencias
{
    private EntityManagerFactory emf;
    private EntityManager em;
    private JTextField nrf, crf, ppurf, caf, cbf, ceditf, ppueditf, ncf, ulogf, clogf, uregf, cregf;
    private JButton rb, ab, bb, eb, editb, ncb, bhb, lbm, lbo, rbm, rbo, ebo, ebm;
    private JTextArea hmf, etf;
    private JMenuItem ex, hi, ce, tom, tmo, tot, tmt, tto, ttm, empmen;
    private JComboBox<Materiales> naf, nbf, nef, neditf, nof;
    private JFrame ventana, ventanalogin, ventanaRegistrarEliminar;
    private String usuarioMySQL = "root", contraseñaMySQL = "";
    private final String msqls="jdbc:mysql://localhost/GestionExistencias";
    private final String odbs="$objectdb/db/GestionExistencias.odb";
    private final String txtfile="Trabajo_Final_Programacion_DavidBeamonde/Materiales.txt";

    public static void main(String args[]) 
    {
        GestionExistencias aplicacion = new GestionExistencias();
        aplicacion.ventana.setVisible(false);
    }

    public GestionExistencias() 
    {
        // Establecer imagen de fondo
        Fondo fondo = new Fondo("Trabajo_Final_Programacion_DavidBeamonde/imagenes/fondo.jpg");
        fondo.setLayout(null);
        ventana = new JFrame();
        ventana.setContentPane(fondo);
        ventana.setVisible(false);

        // Cargar imagen de informacion
        ImageIcon iconoOriginal = new ImageIcon("Trabajo_Final_Programacion_DavidBeamonde/imagenes/info.png");
        Image imagenOriginal = iconoOriginal.getImage(); 
        Image imagenReescalada = imagenOriginal.getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Reescalar la imagen al tamaño deseado
        ImageIcon iconoReescalado = new ImageIcon(imagenReescalada); // Crear un nuevo ImageIcon con la imagen reescalada

        // Configuración de MySQL
        try
        {
            //Abrir la conexión con MySQL
            System.out.println("Conectando con la base de datos MySQL...");
            DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);
            System.out.println("Conexion establecida.");
        }
        catch (SQLException e)
        {
            //e.printStackTrace();
            System.out.println("ERROR al conectar con la base de datos MySQL: \n" + e.getMessage());
            JOptionPane.showMessageDialog(ventanalogin, "ERROR al conectar con la base de datos MySQL, \nRevisa la consola para mas detalles.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        // Configuración de ObjectDB
        emf = Persistence.createEntityManagerFactory(odbs);
        em = emf.createEntityManager();

//---------------------------------------- Ventana login ----------------------------------------
        ventanalogin = new JFrame();
        JPanel loginPanel = new JPanel(null);
        ventanalogin.add(new JLabel("Usuario:")).setBounds(40, 20, 100, 25);
        ulogf = new JTextField();
        ulogf.setBounds(150, 20, 200, 25);
        loginPanel.add(ulogf);
        ventanalogin.add(new JLabel("Contraseña:")).setBounds(40, 60, 100, 25);
        clogf = new JTextField();
        clogf.setBounds(150, 60, 200, 25);
        loginPanel.add(clogf);

        lbm = new JButton("Login MySQL");
        lbm.setBounds(40, 100, 150, 25);
        loginPanel.add(lbm);
        lbo = new JButton("Login ObjectDB");
        lbo.setBounds(200, 100, 150, 25);
        loginPanel.add(lbo);
        
        ventanalogin.getContentPane().add(loginPanel);
        ventanalogin.setTitle("Login");
        ventanalogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanalogin.setSize(400, 200);
        ventanalogin.setLocationRelativeTo(null);
        ventanalogin.setVisible(true);

//---------------------------------------- Ventana registrar o eliminar ----------------------------------------
        ventanaRegistrarEliminar = new JFrame();
        JPanel registroPanel = new JPanel(null);
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setBounds(65, 20, 100, 25);
        registroPanel.add(usuarioLabel);
        uregf = new JTextField();
        uregf.setBounds(175, 20, 200, 25);
        registroPanel.add(uregf);
        JLabel contraseñaLabel = new JLabel("Contraseña:");
        contraseñaLabel.setBounds(65, 60, 100, 25);
        registroPanel.add(contraseñaLabel);
        cregf = new JTextField();
        cregf.setBounds(175, 60, 200, 25);
        registroPanel.add(cregf);

        rbm = new JButton("Registrar MySQL");
        rbm.setBounds(40, 110, 175, 25);
        registroPanel.add(rbm);
        rbo = new JButton("Registrar ObjectDB");
        rbo.setBounds(225, 110, 175, 25);
        registroPanel.add(rbo);
        ebm = new JButton("Eliminar MySQL");
        ebm.setBounds(40, 150, 175, 25);
        registroPanel.add(ebm);
        ebo = new JButton("Eliminar ObjectDB");
        ebo.setBounds(225, 150, 175, 25);
        registroPanel.add(ebo);
        
        ventanaRegistrarEliminar.getContentPane().add(registroPanel);
        ventanaRegistrarEliminar.setTitle("Registrar o Eliminar Empleados");
        ventanaRegistrarEliminar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaRegistrarEliminar.setSize(450, 260);
        ventanaRegistrarEliminar.setLocationRelativeTo(null);
        ventanaRegistrarEliminar.setVisible(false);

//---------------------------------------- Menu superior ----------------------------------------
        // Crear una barra de menú        
        JMenuBar jmb = new JMenuBar();
        ventana.setJMenuBar(jmb);

        // Crear un menú
        JMenu menuOpciones = new JMenu("Opciones");
        menuOpciones.setMnemonic('O'); // Letra distinguida
        // Agregar elementos al menú
        menuOpciones.add(hi = new JMenuItem("Borrar el historial", 'B'));
        menuOpciones.addSeparator();
        menuOpciones.add(ex = new JMenuItem("Eliminar todos los materiales", 'E'));
        jmb.add(menuOpciones);

        JMenu transferir = new JMenu("Transferir");
        transferir.setMnemonic('T');
        transferir.add(tom = new JMenuItem("Transferir ObjectDB a MySQL", 'T'));
        transferir.add(tmo = new JMenuItem("Transferir MySQL a ObjectDB", 'T'));
        transferir.addSeparator();
        transferir.add(tot = new JMenuItem("Transferir ObjectDB a TXT", 'T'));
        transferir.add(tto = new JMenuItem("Transferir TXT a ObjectDB", 'T'));
        transferir.addSeparator();
        transferir.add(tmt = new JMenuItem("Transferir MySQL a TXT", 'T'));
        transferir.add(ttm = new JMenuItem("Transferir TXT a MySQL", 'T'));
        jmb.add(transferir);

        JMenu empleado = new JMenu("Empleados");
        empleado.setMnemonic('E');
        empleado.add(empmen = new JMenuItem("Registrar o Eliminar empleados", 'R'));
        jmb.add(empleado);

        JMenu exitMenu = new JMenu("Salir");
        exitMenu.setMnemonic('S');
        exitMenu.add(ce = new JMenuItem("Cerrar", 'C'));
        jmb.add(exitMenu);

        //Agregar actionListener a los elementos del menú
        hi.addActionListener(new BorrarHistorialMenu());
        ex.addActionListener(new EliminarExistenciasMenu());

        tom.addActionListener(new TransferirMaterialesObjectDBtoMySQLMenu());
        tmo.addActionListener(new TransferirMaterialesMySQLtoObjectDBMenu());
        tot.addActionListener(new TransferirMaterialesObjectDBtoTXTMenu());
        tto.addActionListener(new TransferirMaterialesTXTtoObjectDBMenu());
        tmt.addActionListener(new TransferirMaterialesMySQLtoTXTMenu());
        ttm.addActionListener(new TransferirMaterialesTXTtoMySQLMenu());

        empmen.addActionListener(new EmpleadosMenu());

        ce.addActionListener(new CerrarMenu());
        
//---------------------------------------- Gui principal ----------------------------------------
        // Establecer el título y layout nulo para un posicionamiento absoluto de los componentes en la ventana
        ventana.setTitle("Gestión de Existencias - by DBSueños");
        ventana.setLayout(null);

        // Variables para facilitar la modificacion del tamaño y posicion de los componentes
        int alturaBoton = 200;
        int alturaBoton2 = 400;
        int largoBoton = 100;
        int largoTextos = 200;
        int altoCampos = 25;

        //Crear contenedores y elementos
        ventana.add(new JLabel("Registrar material: ")).setBounds(60, 40, largoTextos, altoCampos); // Establecer tamaño
        JLabel ir = new JLabel(iconoReescalado); // Crear y configurar el JLabel para mostrar la imagen reescalada
        ir.setBounds(240, 40, 20, 20); // Establecer posición y tamaño de la imagen
        ventana.add(ir); // Agregar la imagen
        ventana.add(new JLabel("Nombre: ")).setBounds(60, 80, largoTextos, altoCampos); // Crear texto y establecer posición y tamaño
        nrf = new JTextField(); // Crear un campo
        nrf.setBounds(130, 80, 170, altoCampos); // Establecer posición y tamaño
        ventana.add(nrf); // Agregarlo
        ventana.add(new JLabel("Cantidad: ")).setBounds(60, 120, largoTextos, altoCampos); // Crear texto y establecer posición y tamaño
        crf = new JTextField(); // Crear un campo
        crf.setBounds(150, 120, 150, altoCampos);  // Establecer posición y tamaño
        ventana.add(crf); // Agregarlo
        ventana.add(new JLabel("Precio unidad: ")).setBounds(60, 160, largoTextos, altoCampos); // Crear texto y establecer posición y tamaño
        ppurf = new JTextField(); // Crear un campo
        ppurf.setBounds(170, 160, 130, altoCampos); // Establecer posición y tamaño
        ventana.add(ppurf); // Agregarlo
        rb = new JButton("Registrar"); // Crear un botón
        rb.setBounds(130, alturaBoton, largoBoton, altoCampos); // Establecer posición y tamaño
        ventana.add(rb); // Agregarlo

        ventana.add(new JLabel("Añadir material: ")).setBounds(340, 40, largoTextos, altoCampos);
        JLabel ia = new JLabel(iconoReescalado);
        ia.setBounds(520, 40, 20, 20);
        ventana.add(ia);
        ventana.add(new JLabel("Nombre: ")).setBounds(340, 80, largoTextos, altoCampos);
        naf = new JComboBox<>(); // Crear lista de almacen de elementos registrados
        naf.setBounds(410, 80, 170, altoCampos);
        ventana.add(naf);
        ventana.add(new JLabel("Cantidad: ")).setBounds(340, 120, largoTextos, altoCampos);
        caf = new JTextField();
        caf.setBounds(430, 120, 150, altoCampos);
        ventana.add(caf);
        ab = new JButton("Añadir");
        ab.setBounds(410, alturaBoton, largoBoton, altoCampos);
        ventana.add(ab);

        ventana.add(new JLabel("Editar material: ")).setBounds(620, 40, largoTextos, 25);
        JLabel iedit = new JLabel(iconoReescalado);
        iedit.setBounds(800, 40, 20, 20);
        ventana.add(iedit);
        ventana.add(new JLabel("Nombre: ")).setBounds(620, 80, largoTextos, altoCampos);
        neditf = new JComboBox<>();
        neditf.setBounds(690, 80, 170, altoCampos);
        ventana.add(neditf);
        ventana.add(new JLabel("Cantidad: ")).setBounds(620, 120, largoTextos, altoCampos);
        ceditf = new JTextField();
        ceditf.setBounds(710, 120, 150, altoCampos);
        ventana.add(ceditf);
        ventana.add(new JLabel("Precio unidad: ")).setBounds(620, 160, largoTextos, altoCampos);
        ppueditf = new JTextField();
        ppueditf.setBounds(730, 160, 130, altoCampos);
        ventana.add(ppueditf);
        editb = new JButton("Editar");
        editb.setBounds(690, alturaBoton, largoBoton, altoCampos);
        ventana.add(editb);

        ventana.add(new JLabel("Retirar material: ")).setBounds(60, 280, largoTextos, 25);
        JLabel ib = new JLabel(iconoReescalado); 
        ib.setBounds(240, 280, 20, 20);
        ventana.add(ib);
        ventana.add(new JLabel("Nombre: ")).setBounds(60, 320, largoTextos, altoCampos);
        nbf = new JComboBox<>();
        nbf.setBounds(130, 320, 170, altoCampos);
        ventana.add(nbf);
        ventana.add(new JLabel("Cantidad: ")).setBounds(60, 360, largoTextos, altoCampos);
        cbf = new JTextField();
        cbf.setBounds(150, 360, 150, altoCampos);
        ventana.add(cbf);
        bb = new JButton("Retirar");
        bb.setBounds(130, alturaBoton2, largoBoton, altoCampos);
        ventana.add(bb);

        ventana.add(new JLabel("Eliminar material: ")).setBounds(340, 280, largoTextos, altoCampos);
        JLabel ie = new JLabel(iconoReescalado); 
        ie.setBounds(520, 280, 20, 20);
        ventana.add(ie);
        ventana.add(new JLabel("Nombre: ")).setBounds(340, 320, largoTextos, altoCampos);
        nef = new JComboBox<>();
        nef.setBounds(410, 320, 170, altoCampos);
        ventana.add(nef);
        eb = new JButton("Eliminar");
        eb.setBounds(410, alturaBoton2, largoBoton, altoCampos);
        ventana.add(eb);

        ventana.add(new JLabel("Modificar nombre: ")).setBounds(620, 280, largoTextos, altoCampos);
        JLabel im = new JLabel(iconoReescalado); 
        im.setBounds(800, 280, 20, 20);
        ventana.add(im);
        ventana.add(new JLabel("Actual: ")).setBounds(620, 320, largoTextos, altoCampos);
        nof = new JComboBox<>();
        nof.setBounds(690, 320, 170, altoCampos);
        ventana.add(nof);
        ventana.add(new JLabel("Nuevo: ")).setBounds(620, 360, largoTextos, altoCampos);
        ncf = new JTextField();
        ncf.setBounds(690, 360, 170, altoCampos);
        ventana.add(ncf);
        ncb = new JButton("Modificar");
        ncb.setBounds(690, alturaBoton2, largoBoton, altoCampos);
        ventana.add(ncb);

        // Crear area de texto
        ventana.add(new JLabel("Historial de modificaciones: ")).setBounds(60, 500, largoTextos, altoCampos);
        JLabel ihm = new JLabel(iconoReescalado);
        ihm.setBounds(300, 500, 20, 20);
        ventana.add(ihm);
        hmf = new JTextArea(); // Crear un area
        hmf.setEditable(false); // Que no sea editable
        JScrollPane scrollPane = new JScrollPane(hmf); // Añadir scroll
        scrollPane.setBounds(60, 540, 800, 160); // Establecer posición y tamaño
        ventana.add(scrollPane); // Agregarlo
        bhb = new JButton("Borrar el historial");
        bhb.setBounds(350, 500, largoBoton+80, altoCampos);
        ventana.add(bhb);

        ventana.add(new JLabel("Materiales disponibles: ")).setBounds(920, 40, largoTextos, altoCampos);
        JLabel ied = new JLabel(iconoReescalado);
        ied.setBounds(1140, 40, 20, 20);
        ventana.add(ied);
        etf = new JTextArea();
        etf.setEditable(false);
        JScrollPane scrollPane2 = new JScrollPane(etf);
        scrollPane2.setBounds(920, 80, 250, 620);
        ventana.add(scrollPane2);

        // Configuración de ventana
        ventana.setSize(1230, 820); // Tamano de la ventana
        ventana.setResizable(false); // No redimensionable
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar al cerrar la ventana
        ventana.setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Actualizar la lista de materiales
        actualizarComboBoxMateriales(); // Actualizar el JComboBox
        actualizarTextoETF(); // Actualizar el JTextArea de existencias

//---------------------------------------- Configuracion de botones ----------------------------------------
        // Que ocurre al pulsar cada botón
        rb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                manejadorRegistrarMaterial();
            }
        });
        ab.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                manejadorAgregarMaterial();
            }
        });
        bb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                manejadorRetirarMaterial();
            }
        });
        eb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                manejadorEliminarMaterial();
            }
        });
        editb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                manejadorEditarMaterial();
            }
        });
        ncb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                manejadorModificarNombre();
            }
        });
        bhb.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                borrarHistorial();
            }
        });
        lbm.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                manejadorLoginEmpleadoMySQL();
            }
        });
        lbo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                manejadorLoginEmpleadoObjectDB();
            }
        });
        rbo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                manejadorRegistrarEmpleadoObjectDB();
            }
        });
        ebo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                manejadorEliminarEmpleadoObjectDB();
            }
        });
        rbm.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                manejadorRegistrarEmpleadoMySQL();
            }
        });
        ebm.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                manejadorEliminarEmpleadoMySQL();
            }
        });

        // Agregar un MouseListener al JLabel para detectar clics
        ir.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                // Mostrar ventana de información al hacer clic en la imagen
                JOptionPane.showMessageDialog(null, "- Introduce el nombre, la cantidad y el precio por unidad de\ncada producto que quieras registrar.\n- Cada vez que registras un producto, aparecerá en la lista\nde materiales disponibles situada a la derecha.\n- En la zona inferior se mostrará un mensaje de registro.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        ia.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                JOptionPane.showMessageDialog(null, "- Introduce el nombre y la cantidad quieras añadir de cada producto.\n- Cada vez que añadas una cantidad, se actualiza inmediatamente\nen la lista de materiales disponibles.\n- En la zona inferior se mostrará un mensaje de añadido.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        iedit.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                JOptionPane.showMessageDialog(null, "- Introduce el nombre, la cantidad y el precio por unidad de\ncada producto que quieras editar.\n- Cada vez que editas una cantidad o precio, se actualiza\nen la lista de materiales disponibles.\n- En la zona inferior se mostrará un mensaje de edición.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        ib.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                JOptionPane.showMessageDialog(null, "- Introduce el nombre y la cantidad quieras retirar de cada producto.\n- Cada vez que retiras una cantidad, se actualiza inmediatamente\nen la lista de materiales disponibles.\n- En la zona inferior se mostrará un mensaje de retirada.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        ie.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                JOptionPane.showMessageDialog(null, "- Introduce el nombre del producto que quieras eliminar.\n- Cada vez que eliminas un producto, se actualiza inmediatamente\nen la lista de materiales disponibles.\n- En la zona inferior se mostrará un mensaje de eliminación.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        im.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                JOptionPane.showMessageDialog(null, "- Introduce el nombre actual y el nuevo nombre del producto que\nquieras cambiar el nombre.\n- Cada vez que cambias el nombre de un producto, se actualiza\ninmediatamente en la lista de materiales disponibles.\n- En la zona inferior se mostrará un mensaje de modificación.", "Información", JOptionPane.INFORMATION_MESSAGE);
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
                JOptionPane.showMessageDialog(null, "- Aqui aparecen todos los materiales disponibles.\n- Cada vez que se realice un cambio se actualiza inmediatamente.\n- Al cerrar la aplicacion los materiales se guardan automaticamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

//---------------------------------------- Metodos extra ----------------------------------------
    // Agregar modificaciones al area de historial de modificaciones
    public void agregarTextoHMF(String texto) 
    {
        hmf.append(texto + "\n");
    }

    // Agregar materiales existentes al area de existencias disponibles
    public void agregarTextoETF(String texto) 
    {
        etf.append(texto + "\n");
    }

    // Actualizar el JTextArea con la lista de existencias
    public void actualizarTextoETF() 
    {
        TypedQuery<Materiales> query = em.createQuery("SELECT m FROM Materiales m ORDER BY m.nombre ASC", Materiales.class);
        List<Materiales> existencias = query.getResultList();
        etf.setText(""); // Borrar el contenido actual de etf
        // Escribir todos los materiales existentes
        for (Materiales material : existencias) 
        {
            agregarTextoETF(" Nombre: " + material.nombre);
            agregarTextoETF(" Cantidad: " + material.cantidad);
            agregarTextoETF(" Precio: " + material.precioUnidad);
            agregarTextoETF("---------------------------------------");
        }    
    }

    // Actualizar todos los JComboBox con los materiales existentes
    public void actualizarComboBoxMateriales() 
    {
        em.getTransaction().begin();
        TypedQuery<Materiales> query = em.createQuery("SELECT m FROM Materiales m ORDER BY m.nombre ASC", Materiales.class);
        List<Materiales> resultados = query.getResultList();
        em.getTransaction().commit();
        // Limpiar el JComboBox antes de agregar los nuevos materiales
        naf.removeAllItems();
        nbf.removeAllItems();
        nef.removeAllItems();
        neditf.removeAllItems();
        nof.removeAllItems();
        // Agregar materiales al JComboBox
        for (Materiales material : resultados) 
        {
            naf.addItem(material);
            nbf.addItem(material);
            nef.addItem(material);
            neditf.addItem(material);
            nof.addItem(material);
        }
    }

    // Ventanas con texto generico para facilitar la modificacion de los mensajes
    public void rellenarCampos()
    {
        JOptionPane.showMessageDialog(ventana, "Por favor, rellena todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
    }
    public void rellenarCamposEditar()
    {
        JOptionPane.showMessageDialog(ventana, "Por favor, rellena la cantidad, el precio o los dos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
    }
    public void cantidadYPrecioIncorrecto()
    {
        JOptionPane.showMessageDialog(ventana, "Por favor, introduce un número válido para la cantidad y para el precio.", "Cantidad o Precio Incorrecto", JOptionPane.WARNING_MESSAGE);    
    }
    public void cantidadIncorrecto()
    {
        JOptionPane.showMessageDialog(ventana, "Por favor, introduce un número válido para la cantidad.", "Cantidad Incorrecta", JOptionPane.WARNING_MESSAGE);
    }
    public void precioIncorrecto()
    {
        JOptionPane.showMessageDialog(ventana, "Por favor, introduce un número válido para el precio.", "Precio Incorrecto", JOptionPane.WARNING_MESSAGE);
    }

//---------------------------------------- Metodo registrar material ----------------------------------------
    private void manejadorRegistrarMaterial()
    {
        TransObjectDBtoMySQL transObjectDBtoMySQL = new TransObjectDBtoMySQL(msqls, usuarioMySQL, contraseñaMySQL, em);
        MaterialRegistrar materialRegistrar = new MaterialRegistrar(em, msqls, usuarioMySQL, contraseñaMySQL, this, nrf, crf, ppurf, transObjectDBtoMySQL);
        String nombre = nrf.getText();
        String cantidad = crf.getText();
        String precioUnidadStr = ppurf.getText();
        materialRegistrar.registrarMaterial(nombre, cantidad, precioUnidadStr);
    }

//---------------------------------------- Metodo agregar material ----------------------------------------
    public void manejadorAgregarMaterial()
    {
        TransObjectDBtoMySQL transObjectDBtoMySQL = new TransObjectDBtoMySQL(msqls, usuarioMySQL, contraseñaMySQL, em);
        MaterialAgregar materialAgregar = new MaterialAgregar(em, this, caf, transObjectDBtoMySQL);
        Materiales materialSeleccionado = (Materiales) naf.getSelectedItem();
        String cantidad = caf.getText();
        materialAgregar.agregarMaterial(materialSeleccionado, cantidad);
    }

//---------------------------------------- Metodo editar material ----------------------------------------
    private void manejadorEditarMaterial()
    {
        TransObjectDBtoMySQL transObjectDBtoMySQL = new TransObjectDBtoMySQL(msqls, usuarioMySQL, contraseñaMySQL, em);
        MaterialEditar materialEditar = new MaterialEditar(em, this, ceditf, ppueditf, transObjectDBtoMySQL);
        Materiales materialSeleccionado = (Materiales) neditf.getSelectedItem();
        String cantidadStr = ceditf.getText();
        String nuevoPrecioStr = ppueditf.getText();
        materialEditar.editarMaterial(materialSeleccionado, cantidadStr, nuevoPrecioStr);
    }

//---------------------------------------- Metodo borrar material ----------------------------------------
    private void manejadorRetirarMaterial()
    {
        TransObjectDBtoMySQL transObjectDBtoMySQL = new TransObjectDBtoMySQL(msqls, usuarioMySQL, contraseñaMySQL, em);
        MaterialRetirar materialRetirar = new MaterialRetirar(em, this, cbf, transObjectDBtoMySQL);
        Materiales materialSeleccionado = (Materiales) nbf.getSelectedItem();
        String cantidad = cbf.getText();
        materialRetirar.retirarMaterial(materialSeleccionado, cantidad);
    }

//---------------------------------------- Metodo eliminar material ----------------------------------------
    private void manejadorEliminarMaterial()
    {
        TransObjectDBtoMySQL transObjectDBtoMySQL = new TransObjectDBtoMySQL(msqls, usuarioMySQL, contraseñaMySQL, em);
        MaterialEliminar materialEliminar = new MaterialEliminar(em, this, transObjectDBtoMySQL);
        Materiales materialSeleccionado = (Materiales) nef.getSelectedItem();
        materialEliminar.eliminarMaterial(materialSeleccionado);
    }

//---------------------------------------- Metodo modificar nombre ----------------------------------------
    private void manejadorModificarNombre()
    {
        TransObjectDBtoMySQL transObjectDBtoMySQL = new TransObjectDBtoMySQL(msqls, usuarioMySQL, contraseñaMySQL, em);
        ModificarNombre modificarNombre = new ModificarNombre(em, this, ncf, transObjectDBtoMySQL);
        Materiales materialSeleccionado = (Materiales) nof.getSelectedItem();
        String nuevoNombre = ncf.getText();
        modificarNombre.modificarNombre(materialSeleccionado, nuevoNombre);
    }

//---------------------------------------- Metodo y menu borrar historial ----------------------------------------
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

//---------------------------------------- Metodo y menu borrar todo existencias ----------------------------------------
    private void borrarTodasExistencias() 
    {
        // Crear la ventana de confirmación
        int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar todos los materiales?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        // Verificar la opción seleccionada
        if (opcion == JOptionPane.YES_OPTION) 
        {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Materiales").executeUpdate();
            em.getTransaction().commit();
            TransObjectDBtoMySQL transObjectDBtoMySQL = new TransObjectDBtoMySQL(msqls, usuarioMySQL, contraseñaMySQL, em);
            transObjectDBtoMySQL.transferirMaterialesObjectDBtoMySQL();
            hmf.setText("");
            actualizarComboBoxMateriales();
            actualizarTextoETF();
            JOptionPane.showMessageDialog(null, "Se han borrado todos los materiales.");
        }
    }

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

//---------------------------------------- Metodo y menu transferir datos de ObjectDB a MySQL ----------------------------------------
    private void manejadorTransObjectDBtoMySQL()
    {
        TransObjectDBtoMySQL transObjectDBtoMySQL = new TransObjectDBtoMySQL(msqls, usuarioMySQL, contraseñaMySQL, em);
        transObjectDBtoMySQL.transferirMaterialesObjectDBtoMySQL();
    }

    class TransferirMaterialesObjectDBtoMySQLMenu implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                manejadorTransObjectDBtoMySQL();
            }
        }
    }

//---------------------------------------- Metodo y menu transferir datos de MySQL a ObjectDB ----------------------------------------
    private void manejadorTransMySQLtoObjectDB()
    {
        TransMySQLtoObjectDB transMySQLtoObjectDB = new TransMySQLtoObjectDB(msqls, usuarioMySQL, contraseñaMySQL, em, this);
        transMySQLtoObjectDB.transferirMaterialesMySQLtoObjectDB();
    }

    class TransferirMaterialesMySQLtoObjectDBMenu implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                manejadorTransMySQLtoObjectDB();
            }
        }
    }

//---------------------------------------- Metodo y menu transferir materiales de ObjectDB a TXT ----------------------------------------
    private void manejadorTransObjectDBtoTXT()
    {
        TransObjectDBtoTXT transObjectDBtoTXT = new TransObjectDBtoTXT(em, txtfile);
        transObjectDBtoTXT.transferirMaterialesObjectDBtoTXT();
    }

    class TransferirMaterialesObjectDBtoTXTMenu implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                manejadorTransObjectDBtoTXT();
            }
            
        }
    }

//---------------------------------------- Metodo y menu transferir materiales de MySQL a TXT ----------------------------------------
    private void manejadorTransMySQLtoTXT()
    {
        TransMySQLtoTXT transMySQLtoTXT = new TransMySQLtoTXT(msqls, usuarioMySQL, contraseñaMySQL, txtfile);
        transMySQLtoTXT.transferirMaterialesMySQLtoTXT();
    }

    class TransferirMaterialesMySQLtoTXTMenu implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                manejadorTransMySQLtoTXT();
            }
            
        }
    }

//---------------------------------------- Metodo y menu transferir materiales de TXT a ObjectDB ----------------------------------------
    private void manejadorTransTXTtoObjectDB()
    {
        TransTXTtoObjectDB transTXTtoObjectDB = new TransTXTtoObjectDB(em, this, txtfile);
        transTXTtoObjectDB.cargarMaterialesTXTtoObjectDB();
    }

    class TransferirMaterialesTXTtoObjectDBMenu implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                manejadorTransTXTtoObjectDB();
            }
            
        }
    }

//---------------------------------------- Metodo y menu transferir materiales de TXT a MySQL ----------------------------------------
    private void manejadorTransTXTtoMySQL()
    {
        TransTXTtoMySQL transTXTtoMySQL = new TransTXTtoMySQL(msqls, usuarioMySQL, contraseñaMySQL, txtfile);
        transTXTtoMySQL.cargarMaterialesTXTtoMySQL();
    }

    class TransferirMaterialesTXTtoMySQLMenu implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                manejadorTransTXTtoMySQL();
            }
            
        }
    }

//---------------------------------------- Metodo login MySQL ----------------------------------------
    private void manejadorLoginEmpleadoMySQL()
    {
        LoginManagerMySQL loginManagerMySQL = new LoginManagerMySQL(msqls, usuarioMySQL, contraseñaMySQL);
        String usuario = ulogf.getText();
        String contraseña = clogf.getText();
        boolean loginExitoso = loginManagerMySQL.comprobacionLoginMySQL(usuario, contraseña);
        if (loginExitoso)
        {
            ventanalogin.setVisible(false);
            ventana.setVisible(true);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "El usuario o la contraseña son incorrectos");
        }
    }

//---------------------------------------- Metodo login ObjectDB ----------------------------------------
    private void manejadorLoginEmpleadoObjectDB()
    {
        LoginManagerObjectDB loginManagerObjectDB = new LoginManagerObjectDB(emf);
        String usuario = ulogf.getText();
        String contraseña = clogf.getText();
        boolean loginExitoso = loginManagerObjectDB.comprobacionLoginEmpleadoObjectDB(usuario, contraseña);
        if (loginExitoso)
        {
            ventanalogin.setVisible(false);
            ventana.setVisible(true);
        }
    }

//---------------------------------------- Metodo crear usuario ObjectDB ----------------------------------------
    public void manejadorRegistrarEmpleadoObjectDB()
    {
        RegistrarManagerObjectDB registrarManager = new RegistrarManagerObjectDB(em, uregf, cregf);
        registrarManager.crearEmpleadoObjectDB();
    }

//---------------------------------------- Metodo eliminar usuario ObjectDB ----------------------------------------
    public void manejadorEliminarEmpleadoObjectDB()
    {
        EliminarManagerObjectDB registerManager = new EliminarManagerObjectDB(em, uregf, cregf);
        registerManager.eliminarEmpleadoObjectDB();
    }

//---------------------------------------- Metodo crear usuario MySQL ----------------------------------------
    public void manejadorRegistrarEmpleadoMySQL()
    {
        RegistrarManagerMySQL registrarManager = new RegistrarManagerMySQL(msqls, usuarioMySQL, contraseñaMySQL, uregf, cregf);
        registrarManager.crearEmpleadoMySQL();
    }

//---------------------------------------- Metodo eliminar usuario MySQL ----------------------------------------
    public void manejadorEliminarEmpleadoMySQL()
    {
        EliminarManagerMySQL eliminarManager = new EliminarManagerMySQL(msqls, usuarioMySQL, contraseñaMySQL, uregf, cregf);
        eliminarManager.eliminarEmpleadoMySQL();
    }

//---------------------------------------- Menu empleados/registrar o eliminar empleados ----------------------------------------
    class EmpleadosMenu implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                ventanaRegistrarEliminar.setVisible(true);
            }
            
        }
    }

//---------------------------------------- Menu salir/cerrar ----------------------------------------
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
}
// https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
