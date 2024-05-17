// Creado y pensado por BEAMONDO
//
// El codigo se encuentra publico aqui:
// https://github.com/BEAMONDO/Trabajo_Final_DAM1

import javax.persistence.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.List;

public class GestionExistencias
{
    private EntityManagerFactory emf;
    private EntityManager em;
    private Connection cmysql = null;
    private Statement smysql = null;
    private JTextField nrf, crf, ppurf, caf, cbf, ceditf, ppueditf, ncf, uloginf, cloginf;
    private JButton rb, ab, bb, eb, editb, ncb, bhb, eteb, accederb, BOTONPRUEBAS;
    private JTextArea hmf, etf;
    private JMenuItem ex, hi, ce, cs, tom, tmo, tot, tmt, tto, ttm, ea, emod, ee;
    private JComboBox<Materiales> naf, nbf, nef, neditf, nof;
    private JFrame ventana = new JFrame(), ventanalogin = new JFrame();
    private String usuarioMySQL = "root", contraseñaMySQL = "";
    private final String msqls="jdbc:mysql://localhost/GestionExistencias";
    private final String odbs="$objectdb/db/GestionExistencias.odb";
    private final String txtfile="Pruebas_Trabajo_Final/Materiales.txt";

    public static void main(String args[]) 
    {
        GestionExistencias aplicacion = new GestionExistencias();
    }

    public GestionExistencias() 
    {
        // Establecer imagen de fondo
        Imagenes fondo = new Imagenes("Pruebas_Trabajo_Final/imagenes/fondo.jpg");
        fondo.setLayout(null);
        ventana.setContentPane(fondo);
        ventana.setVisible(false);

        // Cargar imagen de informacion
        ImageIcon iconoOriginal = new ImageIcon("Pruebas_Trabajo_Final/imagenes/info.png");
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
        }

        // Configuración de ObjectDB
        emf = Persistence.createEntityManagerFactory(odbs);
        em = emf.createEntityManager();
//---------------------------------------- Ventana login ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
        JPanel loginPanel = new JPanel(null);
        ventanalogin.add(new JLabel("Usuario:")).setBounds(40, 20, 100, 25);
        uloginf = new JTextField();
        uloginf.setBounds(150, 20, 200, 25);
        loginPanel.add(uloginf);
        ventanalogin.add(new JLabel("Contraseña:")).setBounds(40, 60, 100, 25);
        cloginf = new JTextField();
        cloginf.setBounds(150, 60, 200, 25);
        loginPanel.add(cloginf);
        accederb = new JButton("Acceder");
        accederb.setBounds(150, 100, 100, 25);
        loginPanel.add(accederb);

        accederb.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                manejadorLoginMySQL();
                //manejadorLoginObjectDB();
            }
        });
        
        ventanalogin.getContentPane().add(loginPanel);
        ventanalogin.setTitle("Login");
        ventanalogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanalogin.setSize(400, 200); // Tamaño de la ventana
        ventanalogin.setLocationRelativeTo(null);
        ventanalogin.setVisible(true);
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Menu superior ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
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
        transferir.add(cs = new JMenuItem("Copia de tabla MySQL", 'C'));
        transferir.addSeparator();
        transferir.add(tom = new JMenuItem("Transferir ObjectDB a MySQL", 'T'));
        transferir.add(tmo = new JMenuItem("Transferir MySQL a ObjectDB", 'T'));
        transferir.addSeparator();
        transferir.add(tot = new JMenuItem("Transferir ObjectDB a TXT", 'T'));
        transferir.add(tto = new JMenuItem("Transferir TXT a ObjectDB", 'T'));
        transferir.add(tmt = new JMenuItem("Transferir MySQL a TXT", 'T'));
        transferir.add(ttm = new JMenuItem("Transferir TXT a MySQL", 'T'));
        jmb.add(transferir);

        JMenu empleado = new JMenu("Empleados");
        empleado.setMnemonic('E');
        empleado.add(ea = new JMenuItem("Añadir empleado", 'A'));
        empleado.add(emod = new JMenuItem("Modificar datos empleado", 'M'));
        empleado.add(ee = new JMenuItem("Eliminar empleado", 'E'));
        jmb.add(empleado);
        empleado.addSeparator();

        JMenu exitMenu = new JMenu("Salir");
        exitMenu.setMnemonic('S');
        exitMenu.add(ce = new JMenuItem("Cerrar", 'C'));
        jmb.add(exitMenu);

        //Agregar actionListener a los elementos del menú
        hi.addActionListener(new BorrarHistorialMenu());
        ex.addActionListener(new EliminarExistenciasMenu());

        cs.addActionListener(new CopiaSeguridadMenu());
        tom.addActionListener(new TransferirMaterialesObjectDBtoMySQLMenu());
        tmo.addActionListener(new TransferirMaterialesMySQLtoObjectDBMenu());
        tot.addActionListener(new TransferirMaterialesObjectDBtoTXTMenu());
        tto.addActionListener(new TransferirMaterialesTXTtoObjectDBMenu());
        tmt.addActionListener(new TransferirMaterialesMySQLtoTXTMenu());
        ttm.addActionListener(new TransferirMaterialesTXTtoMySQLMenu());

        //ea.addActionListener(new AñadirEmpleadoMenu());
        //emod.addActionListener(new ModificarEmpleadoMenu());
        //ee.addActionListener(new EliminarEmpleadoMenu());

        ce.addActionListener(new CerrarMenu());
//---------------------------------------- Hasta aqui ----------------------------------------*/
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        BOTONPRUEBAS = new JButton("BOTON PARA PRUEBAS");
        BOTONPRUEBAS.setBounds(590, 500, 250, 25);
        ventana.add(BOTONPRUEBAS);
        BOTONPRUEBAS.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                //copiaSeguridadMySQL();
            }
        });
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/
//---------------------------------------- Gui principal ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
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
        eteb = new JButton("Eliminar todos los materiales");
        eteb.setBounds(590, 500, largoBoton+150, altoCampos);
        //ventana.add(eteb);

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
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Configuracion de botones ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
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
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funciones extra ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
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
            agregarTextoETF("  Nombre: " + material.nombre);
            agregarTextoETF("  Cantidad: " + material.cantidad);
            agregarTextoETF("  Precio por unidad: " + material.precioUnidad);
            agregarTextoETF("  Valor total: " + material.cantidad * material.precioUnidad);
            agregarTextoETF("---------------------------------------\n");
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
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion registrar material ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
private void manejadorRegistrarMaterial() {
    MaterialRegistrar materialRegistrar = new MaterialRegistrar(em, msqls, usuarioMySQL, contraseñaMySQL, this, nrf, crf, ppurf);
    String nombre = nrf.getText();
    String cantidad = crf.getText();
    String precioUnidadStr = ppurf.getText();
    materialRegistrar.registrarMaterial(nombre, cantidad, precioUnidadStr);
}
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion agregar material ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    public void manejadorAgregarMaterial() {
        MaterialAgregar materialAgregar = new MaterialAgregar(em, this, caf);
        Materiales materialSeleccionado = (Materiales) naf.getSelectedItem();
        String cantidad = caf.getText();
        materialAgregar.agregarMaterial(materialSeleccionado, cantidad);
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion editar material ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void manejadorEditarMaterial() {
        MaterialEditar materialEditar = new MaterialEditar(em, this, ceditf, ppueditf);
        Materiales materialSeleccionado = (Materiales) neditf.getSelectedItem();
        String cantidadStr = ceditf.getText();
        String nuevoPrecioStr = ppueditf.getText();
        materialEditar.editarMaterial(materialSeleccionado, cantidadStr, nuevoPrecioStr);
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion borrar material ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void manejadorRetirarMaterial() {
        MaterialRetirar materialRetirar = new MaterialRetirar(em, this, cbf);
        Materiales materialSeleccionado = (Materiales) nbf.getSelectedItem();
        String cantidad = cbf.getText();
        materialRetirar.retirarMaterial(materialSeleccionado, cantidad);
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion eliminar material ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
private void manejadorEliminarMaterial() {
    MaterialEliminar materialEliminar = new MaterialEliminar(em, this);
    Materiales materialSeleccionado = (Materiales) nef.getSelectedItem();
    materialEliminar.eliminarMaterial(materialSeleccionado);
}

//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion modificar nombre ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void manejadorModificarNombre() {
        ModificarNombre modificarNombre = new ModificarNombre(em, this, ncf);
        Materiales materialSeleccionado = (Materiales) nof.getSelectedItem();
        String nuevoNombre = ncf.getText();
        modificarNombre.modificarNombre(materialSeleccionado, nuevoNombre);
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion borrar historial ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
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
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion borrar todo existencias ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
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
            transferirMaterialesObjectDBtoMySQL();
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
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion transferir datos de ObjectDB a MySQL ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    @SuppressWarnings("unchecked")
    public void transferirMaterialesObjectDBtoMySQL()
    {
        try
        {
            // Conexión a MySQL
            cmysql = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);
            
            // Eliminacion de materiales en MySQL
            smysql = cmysql.createStatement();
            String sqlDelete = "DELETE FROM Materiales";
            smysql.executeUpdate(sqlDelete);

            // Consulta de materiales en ObjectDB
            Query consulta = em.createQuery("SELECT m FROM Materiales m", Materiales.class);
            List<Materiales> materiales = consulta.getResultList();

            // Inserción de materiales en MySQL
            smysql = cmysql.createStatement();
            for (Materiales material : materiales)
            {
                String nombre = material.getNombre();
                int cantidad = material.getCantidad();
                double precioUnidad = material.getPrecioUnidad();
                String sqlInsert = "INSERT INTO Materiales (nombre, cantidad, precioUnidad) VALUES ('" +
                                    nombre + "', " + cantidad + ", " + precioUnidad + ")";
                smysql.executeUpdate(sqlInsert);
            }
            
            smysql.close();
            cmysql.close();
            //JOptionPane.showMessageDialog(null, "Datos cargados desde ObjectDB a la base de datos MySQL", "Info", JOptionPane.INFORMATION_MESSAGE);
            //System.out.println("Se transfirieron " + materialesTransferidos + " materiales de ObjectDB a MySQL.");
        }
        catch (SQLException e)
        {
            //System.out.println("ERROR al modificar los datos de la tabla materiales en MySQL: \n" + e.getMessage());
        }
    }

    class TransferirMaterialesObjectDBtoMySQLMenu implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                transferirMaterialesObjectDBtoMySQL();
            }
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion transferir datos de MySQL a ObjectDB ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void transferirMaterialesMySQLtoObjectDB()
    {
        try
        {
            // Conexión a MySQL
            cmysql = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);

            // Consulta de materiales en MySQL
            smysql = cmysql.createStatement();
            String sqlSelect = "SELECT * FROM Materiales";
            ResultSet resultado = smysql.executeQuery(sqlSelect);

            // Eliminar todos los materiales existentes en ObjectDB
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Materiales").executeUpdate();
            em.getTransaction().commit();

            // Inserción de materiales en ObjectDB
            em.getTransaction().begin();
            while (resultado.next())
            {
                String nombre = resultado.getString("nombre");
                int cantidad = resultado.getInt("cantidad");
                double precioUnidad = resultado.getDouble("precioUnidad");

                Materiales material = new Materiales(nombre, cantidad, precioUnidad);
                em.persist(material);
            }
            em.getTransaction().commit();

            // Cierre de recursos
            resultado.close();
            smysql.close();
            cmysql.close();
            actualizarComboBoxMateriales();
            actualizarTextoETF();
            JOptionPane.showMessageDialog(null, "Datos cargados desde MySQL a la base de datos ObjectDB", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (SQLException e)
        {
            // Manejo de excepciones
            e.printStackTrace();
        }
    }

    class TransferirMaterialesMySQLtoObjectDBMenu implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                transferirMaterialesMySQLtoObjectDB();
            }
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion y menu copia de tabla mysql ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void copiaSeguridadMySQL() 
    {
        // Ventana de entrada de texto para el nombre de la copia de seguridad
        String nombreTablaDuplicada = JOptionPane.showInputDialog(null, "Introduce el nombre de la copia de seguridad:", "Nombre de la copia de seguridad", JOptionPane.PLAIN_MESSAGE);

        if (nombreTablaDuplicada != null && !nombreTablaDuplicada.isEmpty()) 
        {
            // Reemplazar cualquier caracter que no sea letra o numero por guion bajo
            nombreTablaDuplicada = nombreTablaDuplicada.replaceAll("[^a-zA-Z0-9]", "_");

            try 
            {
                // Abrir la conexión
                Connection conexion = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);

                // Crear y ejecutar la sentencia SQL para duplicar la tabla
                Statement sentencia = conexion.createStatement();
                String createTable = "CREATE TABLE IF NOT EXISTS " + nombreTablaDuplicada + " AS SELECT * FROM Materiales";
                sentencia.execute(createTable);
                System.out.println("Copia de seguridad creada correctamente.");
            } 
            catch (SQLException e) 
            {
                // Manejar errores
                System.out.println("ERROR al crear la copia de seguridad: \n" + e.getMessage());
            }
        } 
        else 
        {
            System.out.println("Nombre de la copia de seguridad no válido.");
        }
    }

    class CopiaSeguridadMenu implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                copiaSeguridadMySQL();
            }
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion y menu transferir materiales de ObjectDB a TXT ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void transferirMaterialesObjectDBtoTXT()
    {
        try 
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(txtfile));
    
            // Consulta la base de datos para obtener los materiales
            TypedQuery<Materiales> consulta = em.createQuery("SELECT m FROM Materiales m", Materiales.class);
            List<Materiales> materiales = consulta.getResultList();
    
            // Recorre la lista de materiales y escribe la información en el archivo
            for (Materiales material : materiales) 
            {   
                String otext = "'" + material.getNombre() + "', '" + material.getCantidad() + "', '" + material.getPrecioUnidad() + "'";
                bw.write(otext);
                bw.newLine();
            }
    
            // Cierra el BufferedWriter
            bw.close();
    
            // Muestra un mensaje de éxito
            JOptionPane.showMessageDialog(null, "Datos copiados al fichero '" + txtfile + "'", "Info", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (IOException ioe) 
        {
            // Muestra un mensaje de error en caso de excepción de escritura
            JOptionPane.showMessageDialog(null, "Se ha producido un error de escritura", "Error", JOptionPane.ERROR_MESSAGE);
            // Imprime el mensaje de error en la consola
            System.err.println(ioe.getMessage());
        }
    }

    class TransferirMaterialesObjectDBtoTXTMenu implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                transferirMaterialesObjectDBtoTXT();
            }
            
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion y menu transferir materiales de MySQL a TXT ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void transferirMaterialesMySQLtoTXT()
    {
        try 
        {
            // Establecer la conexión con MySQL
            Connection conexion = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);
    
            // Crear el archivo de texto
            BufferedWriter bw = new BufferedWriter(new FileWriter(txtfile));
    
            // Crear una consulta SQL para obtener los materiales
            Statement sentencia = conexion.createStatement();
            String consultaSQL = "SELECT nombre, cantidad, precioUnidad FROM Materiales";
            ResultSet resultado = sentencia.executeQuery(consultaSQL);
    
            // Escribir la información en el archivo de texto
            while (resultado.next())
            {
                String nombre = resultado.getString("nombre");
                int cantidad = resultado.getInt("cantidad");
                double precioUnidad = resultado.getDouble("precioUnidad");
    
                String otext = "'" + nombre + "', '" + cantidad + "', '" + precioUnidad + "'";
                bw.write(otext);
                bw.newLine();
            }
    
            // Cerrar recursos
            bw.close();
            resultado.close();
            sentencia.close();
            conexion.close();
    
            // Mostrar un mensaje de éxito
            JOptionPane.showMessageDialog(null, "Datos copiados al fichero '" + txtfile + "'", "Info", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (IOException | SQLException e) 
        {
            // Mostrar un mensaje de error en caso de excepción
            JOptionPane.showMessageDialog(null, "Se ha producido un error", "Error", JOptionPane.ERROR_MESSAGE);
            // Imprimir el mensaje de error en la consola
            System.err.println(e.getMessage());
        }
    }

    class TransferirMaterialesMySQLtoTXTMenu implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                transferirMaterialesMySQLtoTXT();
            }
            
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion y menu transferir materiales de TXT a ObjectDB ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void cargarMaterialesTXTtoObjectDB() 
    {
        try 
        {
            em.getTransaction().begin();

            // Crear un lector de archivos para leer el archivo de texto
            BufferedReader br = new BufferedReader(new FileReader(txtfile));

            // Leer cada línea del archivo de texto y procesarla
            String linea;
            while ((linea = br.readLine()) != null) 
            {
                // Dividir la línea en partes separadas por comas
                String[] partes = linea.split(", ");

                // Obtener los valores de nombre, cantidad y precioUnidad
                String nombre = partes[0].substring(1, partes[0].length() - 1);
                int cantidad = Integer.parseInt(partes[1].substring(1, partes[1].length() - 1));
                double precioUnidad = Double.parseDouble(partes[2].substring(1, partes[2].length() - 1));

                // Crear una instancia de Material y establecer sus atributos
                Materiales material = new Materiales();
                material.setNombre(nombre);
                material.setCantidad(cantidad);
                material.setPrecioUnidad(precioUnidad);

                // Persistir el objeto Material en la base de datos ObjectDB
                em.persist(material);
            }

            // Commit de la transacción
            em.getTransaction().commit();

            // Cerrar recursos
            br.close();

            actualizarTextoETF();
            actualizarComboBoxMateriales();
            // Mostrar un mensaje de éxito
            JOptionPane.showMessageDialog(null, "Datos cargados desde el fichero '" + txtfile + "' a la base de datos ObjectDB", "Info", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (IOException | NumberFormatException e) 
        {
            // Mostrar un mensaje de error en caso de excepción
            JOptionPane.showMessageDialog(null, "Se ha producido un error", "Error", JOptionPane.ERROR_MESSAGE);
            // Imprimir el mensaje de error en la consola
            System.err.println(e.getMessage());
        }
    }

    class TransferirMaterialesTXTtoObjectDBMenu implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                cargarMaterialesTXTtoObjectDB();
            }
            
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

//---------------------------------------- Funcion y menu transferir materiales de TXT a MySQL ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void cargarMaterialesTXTtoMySQL() 
    {
        try 
        {
            // Establecer la conexión con MySQL
            Connection conexion = DriverManager.getConnection(msqls, usuarioMySQL, contraseñaMySQL);

            // Crear un lector de archivos para leer el archivo de texto
            BufferedReader br = new BufferedReader(new FileReader(txtfile));

            // Preparar una consulta SQL para insertar los datos
            PreparedStatement statement = conexion.prepareStatement("INSERT INTO Materiales (nombre, cantidad, precioUnidad) VALUES (?, ?, ?)");

            // Leer cada línea del archivo de texto y procesarla
            String linea;
            while ((linea = br.readLine()) != null)
            {
                // Dividir la línea en partes separadas por comas
                String[] partes = linea.split(", ");

                // Obtener los valores de nombre, cantidad y precioUnidad
                String nombre = partes[0].substring(1, partes[0].length() - 1);
                int cantidad = Integer.parseInt(partes[1].substring(1, partes[1].length() - 1));
                double precioUnidad = Double.parseDouble(partes[2].substring(1, partes[2].length() - 1));

                // Establecer los parámetros de la consulta preparada
                statement.setString(1, nombre);
                statement.setInt(2, cantidad);
                statement.setDouble(3, precioUnidad);

                // Ejecutar la consulta preparada
                statement.executeUpdate();
            }

            // Cerrar recursos
            br.close();
            statement.close();
            conexion.close();

            // Mostrar un mensaje de éxito
            JOptionPane.showMessageDialog(null, "Datos cargados desde el fichero '" + txtfile + "' a la base de datos", "Info", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (IOException | SQLException e) 
        {
            // Mostrar un mensaje de error en caso de excepción
            JOptionPane.showMessageDialog(null, "Se ha producido un error", "Error", JOptionPane.ERROR_MESSAGE);
            // Imprimir el mensaje de error en la consola
            System.err.println(e.getMessage());
        }
    }

    class TransferirMaterialesTXTtoMySQLMenu implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                cargarMaterialesTXTtoMySQL();
            }
            
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

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
//---------------------------------------- Hasta aqui ----------------------------------------*/
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//---------------------------------------- Funcion login MySQL, en la clase MySQLLoginManager ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void manejadorLoginMySQL() {
        LoginManagerMySQL loginManagerMySQL = new LoginManagerMySQL(msqls, usuarioMySQL, contraseñaMySQL);
        String usuario = uloginf.getText();
        String contraseña = cloginf.getText();

        boolean loginExitoso = loginManagerMySQL.comprobacionLoginMySQL(usuario, contraseña);

        if (loginExitoso) {
            ventanalogin.setVisible(false);
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "El usuario o la contraseña son incorrectos");
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

/*//---------------------------------------- Funcion login ObjectDB, en la clase ObjectDBLoginManager ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void manejadorLoginObjectDB() {
        LoginManagerObjectDB loginManagerObjectDB = new ObjectDBLoginManager(emf);
        String usuario = uloginf.getText();
        String contraseña = cloginf.getText();

        boolean loginExitoso = loginManagerObjectDB.comprobacionLoginEmpleadoObjectDB(usuario, contraseña);

        if (loginExitoso) {
            ventanalogin.setVisible(false);
            ventana.setVisible(true);
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

/*//---------------------------------------- Funcion crear usuario ObjectDB ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void crearEmpleadoObjectDB() {
        try {
            String usuario = uloginf.getText();
            String contraseña = cloginf.getText();

            if (usuario.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, rellena todos los campos", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Crear la consulta para verificar si el usuario ya existe
            TypedQuery<Empleados> query = em.createQuery("SELECT e FROM Empleados e WHERE e.usuario = :usuario", Empleados.class);
            query.setParameter("usuario", usuario);
            Empleados empleado = null;
            try { empleado = query.getSingleResult(); } catch (NoResultException ex) {}

            if (empleado != null) {
                // Si el usuario ya existe, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "Ese usuario ya está registrado", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Insertar el nuevo usuario
                em.getTransaction().begin();
                Empleados nuevoEmpleado = new Empleados();
                nuevoEmpleado.setUsuario(usuario);
                nuevoEmpleado.setContraseña(contraseña);
                em.persist(nuevoEmpleado);
                em.getTransaction().commit();
                uloginf.setText("");
                cloginf.setText("");
                JOptionPane.showMessageDialog(null, "Nuevo usuario creado exitosamente");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
        }
    }

    class AñadirEmpleadoMenu implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                crearEmpleadoObjectDB();
            }
            
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/

/*//---------------------------------------- Funcion eliminar usuario ObjectDB ----------------------------------------
//---------------------------------------- Desde aqui ----------------------------------------
    private void eliminarEmpleadoObjectDB() {
        try {
            String usuario = uloginf.getText();

            if (usuario.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, rellena el campo de usuario", "Campo vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Crear la consulta para verificar si el usuario existe
            TypedQuery<Empleados> query = em.createQuery("SELECT e FROM Empleados e WHERE e.usuario = :usuario", Empleados.class);
            query.setParameter("usuario", usuario);

            Empleados empleado = null;
            try {
                empleado = query.getSingleResult();
            } catch (NoResultException ex) {
                // Si el usuario no existe, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "El usuario no existe", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Si el usuario existe, proceder a eliminarlo
            em.getTransaction().begin();
            em.remove(empleado);
            em.getTransaction().commit();
            uloginf.setText("");
            cloginf.setText("");
            JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
        }
    }

    class EliminarEmpleadoMenu implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JMenuItem) 
            {
                eliminarEmpleadoObjectDB();
            }
            
        }
    }
//---------------------------------------- Hasta aqui ----------------------------------------*/
}