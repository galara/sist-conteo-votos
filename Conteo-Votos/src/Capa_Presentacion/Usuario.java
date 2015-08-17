/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Capa_Presentacion;

import Capa_Datos.AccesoDatos;
import Capa_Datos.BdConexion;
import Capa_Negocio.AccesoUsuario;
import Capa_Negocio.FiltroCampos;
import Capa_Negocio.FormatoFecha;
import Capa_Negocio.Peticiones;
import Capa_Negocio.Renderer_CheckBox;
import Capa_Negocio.TipoFiltro;
import Capa_Negocio.Utilidades;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author GLARA
 */
public class Usuario extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model, modelperfil;
    String[] titulos = {"Código", "Nombre", "Usuario", "Estado", "Fecha Alta"};//Titulos para Jtabla
    String[] titulosperfil = {"id", "No.", "Menu", "Principal", "Permitir"};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();
    AccesoDatos acceso = new AccesoDatos();
    java.sql.Connection conn;//getConnection intentara establecer una conexión.

    /*Se hace una instancia de la clase que recibira las peticiones de mensages de la capa de aplicación*/
    //public static JOptionMessage msg = new JOptionMessage();
    /**
     * Creates new form Cliente
     */
    public Usuario() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();

        perfilusuarios.getColumnModel().getColumn(0).setMaxWidth(0);
        perfilusuarios.getColumnModel().getColumn(0).setMinWidth(0);
        perfilusuarios.getColumnModel().getColumn(0).setPreferredWidth(0);
        perfilusuarios.doLayout();
        JCheckBox check = new JCheckBox();
        perfilusuarios.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(check));
        perfilusuarios.getColumnModel().getColumn(4).setCellRenderer(new Renderer_CheckBox());
    }

    /*addEscapeKey agrega a este JInternalFrame un evento de cerrarVentana() al presionar la tecla "ESC" */
    private void addEscapeKey() {
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarVentana();
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
    }

    public void removejtable2() {
        while (perfilusuarios.getRowCount() != 0) {
            modelperfil.removeRow(0);
        }
    }

    /*Este metodo visualiza una mensage de cinfirmación al usuario antes de Cerrar la ventana,
     * si por eror se intento cerrar el formulario devera indicar que "NO" para no perder los datos
     * que no haya Guardado de lo contrario presiona "SI" y se cerrara la ventana sin Guardar ningun dato. */
    private void cerrarVentana() {
        int nu = JOptionPane.showInternalConfirmDialog(this, "Todos los datos que no se ha guardado "
                + "se perderan.\n"
                + "¿Desea Cerrar esta ventana?", "Cerrar ventana", JOptionPane.YES_NO_OPTION);
        if (nu == JOptionPane.YES_OPTION || nu == 0) {
            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
            Utilidades.esObligatorio(this.JPanelCampos, false);
            this.bntGuardar.setEnabled(false);
            this.bntModificar.setEnabled(false);
            this.bntEliminar.setEnabled(false);
            this.bntNuevo.setEnabled(true);
            removejtable();
            removejtable2();
            busqueda.setText("");
            rbNombres.setSelected(true);
            rbCodigo.setSelected(false);
            rbApellidos.setSelected(false);
            psword.setText("");
            Cvercontraseña.setSelected(false);
            busqueda.requestFocus();

            this.dispose();
        }
    }

    /* La funcion de este metodo es limpiar y desabilitar campos que se encuentren en un contenedor
     * ejem: los JTextFiel de un panel, se envian a la capa de negocio "Utilidades.setEditableTexto()" 
     * para que este los limpie,habilite o desabilite dichos componentes */
    public void limpiar() {
        Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
    }

    /* Para no sobrecargar la memoria y hacer una instancia cada vez que actualizamos la JTable se hace una
     * sola instancia y lo unico que se hace antes de actualizar la JTable es limpiar el modelo y enviarle los
     * nuevos datos a mostrar en la JTable  */
    public void removejtable() {
        while (usuarios.getRowCount() != 0) {
            model.removeRow(0);
        }
    }

    /* Este metodo se encarga de filtrar los datos que se deben ingresar en cada uno de los campos del formulario
     * podemos indicar que el usuario ingrese solo numeros , solo letras, numeros y letras, o cualquier caracter
     * tambien podemos validar si se aseptaran espacios en blanco en la cadena ingresada , para mas detalle visualizar
     * la clase TipoFiltro()  */
    private void setFiltroTexto() {

        TipoFiltro.setFiltraEntrada(nombres.getDocument(), FiltroCampos.SOLO_LETRAS, 60, true);
        TipoFiltro.setFiltraEntrada(usuario.getDocument(), FiltroCampos.SOLO_LETRAS, 45, true);
        TipoFiltro.setFiltraEntrada(password.getDocument(), FiltroCampos.NUM_LETRAS, 45, true);
        TipoFiltro.setFiltraEntrada(busqueda.getDocument(), FiltroCampos.NUM_LETRAS, 150, true);
    }

    /* Este metodo recibe de el campo busqueda un parametro que es el que servirá para realizar la cunsulta
     * de los datos, este envia a la capa de negocio "peticiones.getRegistroPorPks( el modelo de la JTable,
     * el nombre de la tabla, los campos de la tabla a consultar, los campos de condiciones, y el dato a comparar
     * en la(s) condicion(es) de la busqueda) .
     *   
     * Nota: si el campo busqueda no contiene ningun dato devolvera todos los datos de la tabla o un mensage
     * indicando que no hay datos para la busqueda  
     *
     * @param Dato , dato a buscar
     * @return 
     */
    private void MostrarDatos(String Dato) {
        String[] campos = {"usuario.idusuario", "usuario.nombre", "usuario.usuario", "usuario.estado", "DATE_FORMAT(usuario.fechacreacion,'%d-%m-%Y')"};
        String[] condiciones = {"usuario.idusuario"};
        String[] Id = {Dato};

        if (this.rbCodigo.isSelected()) {
            if (!Dato.isEmpty()) {
                removejtable();
                Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
                Utilidades.esObligatorio(this.JPanelCampos, false);
                model = peticiones.getRegistroPorPks(model, "usuario", campos, condiciones, Id, "");
            } else {
                JOptionPane.showInternalMessageDialog(this, "Debe ingresar un codigo para la busqueda");
            }
        }
        if (this.rbNombres.isSelected()) {
            removejtable();
            removejtable2();
            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
            Utilidades.esObligatorio(this.JPanelCampos, false);
            model = peticiones.getRegistroPorLike(model, "usuario", campos, "usuario.nombre", Dato, "");
        }
        if (this.rbApellidos.isSelected()) {
            removejtable();
            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
            Utilidades.esObligatorio(this.JPanelCampos, false);
            model = peticiones.getRegistroPorLike(model, "usuario", campos, "usuario.usuario", Dato, "");
        }
        Utilidades.ajustarAnchoColumnas(usuarios);
        tbPane2.setSelectedIndex(0);
    }

    /* Este metodo  consulta en la BD el codigo de la fila seleccionada y llena los componentes
     * de la parte superior del formulario con los datos obtenidos.
     * 
     * @return 
     */
    private void filaseleccionada() {
        int fila = usuarios.getSelectedRow();
        String[] cond = {"idusuario"};
        String[] id = {"" + usuarios.getValueAt(fila, 0)};
        if (usuarios.getValueAt(fila, 0) != null) {

            String[] campos = {"nombre", "usuario", "password", "estado", "fechacreacion"};
            Component[] cmps = {nombres, usuario, password, estado, fecharegistro};
            Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
            peticiones.getRegistroSeleccionado(cmps, "usuario", campos, cond, id, "", null);
            MostrarProductos(2);
            this.bntGuardar.setEnabled(false);
            this.bntModificar.setEnabled(true);
            this.bntEliminar.setEnabled(true);
            this.bntNuevo.setEnabled(false);
            psword.setText("");
            Cvercontraseña.setSelected(false);
        }
    }

    private void MostrarProductos(int opcion) {
        String sql = "";
        if (opcion == 1) {
            sql = "SELECT idmenu, nombre, principal FROM menu where estado=true order by idmenu";
        } else if (opcion == 2) {
            int fila = usuarios.getSelectedRow();
            String id = (String) "" + usuarios.getValueAt(fila, 0);
            sql = "SELECT perfilusuario.idperfilusuario,menu.nombre,menu.principal,perfilusuario.estado FROM menu INNER JOIN perfilusuario ON menu.idmenu = perfilusuario.menu_idmenu WHERE perfilusuario.usuario_idusuario=" + id;
        }
        removejtable2();
        modelperfil = getRegistroPorLikell(modelperfil, sql, opcion);
        Utilidades.ajustarAnchoColumnas(perfilusuarios);
        perfilusuarios.getColumnModel().getColumn(0).setMaxWidth(0);
        perfilusuarios.getColumnModel().getColumn(0).setMinWidth(0);
        perfilusuarios.getColumnModel().getColumn(0).setPreferredWidth(0);
        perfilusuarios.doLayout();
    }

    /**
     * Para una condicion WHERE condicionid LIKE '% campocondicion' * @param
     * modelo ,modelo de la JTable
     *
     * @param tabla , el nombre de la tabla a consultar en la BD
     * @param campocondicion , los campos de la tabla para las condiciones ejem:
     * id,estado etc
     * @return
     */
    public DefaultTableModel getRegistroPorLikell(DefaultTableModel modelo, String tabla, int opcion) {
        try {

            ResultSet rs;

            rs = acceso.getRegistroProc(tabla);
            int cantcampos = 5;
            int cant = 0;
            //if (rs != null) {
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                //int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos];

                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    fila[0] = rs.getString(1);
                    fila[1] = cant = cant + 1;
                    fila[2] = rs.getString(2);
                    fila[3] = rs.getString(3);
                    if (opcion == 1) {
                        fila[4] = false;
                    } else {
                        fila[4] = rs.getBoolean(4);
                    }
                    modelo.addRow(fila);
                }

            } //} 
            else {
                // JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Mensage", JOptionPane.INFORMATION_MESSAGE);
            }
            rs.close();
            return modelo;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelImage = new elaprendiz.gui.panel.PanelImage();
        pnlActionButtons = new javax.swing.JPanel();
        bntNuevo = new elaprendiz.gui.button.ButtonRect();
        bntModificar = new elaprendiz.gui.button.ButtonRect();
        bntGuardar = new elaprendiz.gui.button.ButtonRect();
        bntEliminar = new elaprendiz.gui.button.ButtonRect();
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        JPanelCampos = new javax.swing.JPanel();
        tbPane2 = new elaprendiz.gui.panel.TabbedPaneHeader();
        JPanelCampos1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        nombres = new elaprendiz.gui.textField.TextField();
        usuario = new elaprendiz.gui.textField.TextField();
        fecharegistro = new com.toedter.calendar.JDateChooser();
        estado = new javax.swing.JRadioButton();
        password = new elaprendiz.gui.passwordField.PasswordFieldRectIcon();
        Cvercontraseña = new javax.swing.JCheckBox();
        psword = new javax.swing.JLabel();
        JPanelPago1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        perfilusuarios = new javax.swing.JTable();
        JPanelTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        usuarios = new javax.swing.JTable();
        JPanelBusqueda = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        busqueda = new elaprendiz.gui.textField.TextField();
        rbCodigo = new javax.swing.JRadioButton();
        rbNombres = new javax.swing.JRadioButton();
        rbApellidos = new javax.swing.JRadioButton();
        buttonMostrar1 = new elaprendiz.gui.button.ButtonRect();
        pnlPaginador = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Usuarios");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("usuarios"); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        panelImage.setLayout(null);

        pnlActionButtons.setBackground(java.awt.SystemColor.activeCaption);
        pnlActionButtons.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 255), 1, true));
        pnlActionButtons.setForeground(new java.awt.Color(204, 204, 204));
        pnlActionButtons.setPreferredSize(new java.awt.Dimension(786, 52));
        pnlActionButtons.setLayout(new java.awt.GridBagLayout());

        bntNuevo.setBackground(new java.awt.Color(51, 153, 255));
        bntNuevo.setMnemonic(KeyEvent.VK_N);
        bntNuevo.setText("Nuevo");
        bntNuevo.setName("Nuevo Usuario"); // NOI18N
        bntNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntNuevoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 84, 12, 0);
        pnlActionButtons.add(bntNuevo, gridBagConstraints);

        bntModificar.setBackground(new java.awt.Color(51, 153, 255));
        bntModificar.setMnemonic(KeyEvent.VK_M);
        bntModificar.setText("Modificar");
        bntModificar.setEnabled(false);
        bntModificar.setName("Modificar Usuario"); // NOI18N
        bntModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntModificarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
        pnlActionButtons.add(bntModificar, gridBagConstraints);

        bntGuardar.setBackground(new java.awt.Color(51, 153, 255));
        bntGuardar.setMnemonic(KeyEvent.VK_G);
        bntGuardar.setText("Guardar");
        bntGuardar.setEnabled(false);
        bntGuardar.setName("Guardar Usuario"); // NOI18N
        bntGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntGuardarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
        pnlActionButtons.add(bntGuardar, gridBagConstraints);

        bntEliminar.setBackground(new java.awt.Color(51, 153, 255));
        bntEliminar.setMnemonic(KeyEvent.VK_E);
        bntEliminar.setText("Eliminar");
        bntEliminar.setEnabled(false);
        bntEliminar.setName("Eliminar Usuario"); // NOI18N
        bntEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntEliminarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
        pnlActionButtons.add(bntEliminar, gridBagConstraints);

        bntCancelar.setBackground(new java.awt.Color(51, 153, 255));
        bntCancelar.setMnemonic(KeyEvent.VK_X);
        bntCancelar.setText("Cancelar");
        bntCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntCancelarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
        pnlActionButtons.add(bntCancelar, gridBagConstraints);

        bntSalir.setBackground(new java.awt.Color(51, 153, 255));
        bntSalir.setText("Salir");
        bntSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntSalirActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 93);
        pnlActionButtons.add(bntSalir, gridBagConstraints);

        panelImage.add(pnlActionButtons);
        pnlActionButtons.setBounds(0, 430, 880, 50);

        JPanelCampos.setBackground(java.awt.SystemColor.activeCaption);
        JPanelCampos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelCampos.setForeground(new java.awt.Color(204, 204, 204));
        JPanelCampos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JPanelCampos.setLayout(new java.awt.BorderLayout());

        tbPane2.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        tbPane2.setOpaque(true);

        JPanelCampos1.setBackground(java.awt.SystemColor.activeCaption);
        JPanelCampos1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelCampos1.setForeground(new java.awt.Color(204, 204, 204));
        JPanelCampos1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JPanelCampos1.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Nombre:");
        JPanelCampos1.add(jLabel5);
        jLabel5.setBounds(90, 40, 80, 20);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Usuario:");
        JPanelCampos1.add(jLabel9);
        jLabel9.setBounds(90, 70, 80, 20);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Contraseña:");
        JPanelCampos1.add(jLabel10);
        jLabel10.setBounds(80, 100, 90, 20);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Fecha Alta:");
        JPanelCampos1.add(jLabel11);
        jLabel11.setBounds(450, 100, 150, 21);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("Estado:");
        JPanelCampos1.add(jLabel12);
        jLabel12.setBounds(490, 40, 110, 20);

        nombres.setEditable(false);
        nombres.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nombres.setName("nombres"); // NOI18N
        nombres.setNextFocusableComponent(usuario);
        JPanelCampos1.add(nombres);
        nombres.setBounds(180, 40, 250, 21);

        usuario.setEditable(false);
        usuario.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        usuario.setName("usuario"); // NOI18N
        usuario.setNextFocusableComponent(password);
        JPanelCampos1.add(usuario);
        usuario.setBounds(180, 70, 250, 21);

        fecharegistro.setDate(Calendar.getInstance().getTime());
        fecharegistro.setDateFormatString("dd/MM/yyyy");
        fecharegistro.setEnabled(false);
        fecharegistro.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        fecharegistro.setMaxSelectableDate(new java.util.Date(3093496470100000L));
        fecharegistro.setMinSelectableDate(new java.util.Date(-62135744300000L));
        fecharegistro.setPreferredSize(new java.awt.Dimension(120, 22));
        JPanelCampos1.add(fecharegistro);
        fecharegistro.setBounds(610, 100, 160, 21);

        estado.setBackground(new java.awt.Color(51, 153, 255));
        estado.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        estado.setForeground(new java.awt.Color(255, 255, 255));
        estado.setSelected(true);
        estado.setText("Activo");
        estado.setEnabled(false);
        estado.setName("JRadioButton"); // NOI18N
        estado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                estadoActionPerformed(evt);
            }
        });
        JPanelCampos1.add(estado);
        estado.setBounds(610, 40, 160, 21);

        password.setPreferredSize(new java.awt.Dimension(150, 24));
        JPanelCampos1.add(password);
        password.setBounds(180, 100, 250, 24);

        Cvercontraseña.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Cvercontraseña.setForeground(new java.awt.Color(255, 255, 255));
        Cvercontraseña.setText("Ver Contraseña");
        Cvercontraseña.setName("Ver Contraseña Usuarios"); // NOI18N
        Cvercontraseña.setOpaque(false);
        Cvercontraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CvercontraseñaActionPerformed(evt);
            }
        });
        JPanelCampos1.add(Cvercontraseña);
        Cvercontraseña.setBounds(180, 120, 120, 23);

        psword.setForeground(new java.awt.Color(255, 255, 255));
        JPanelCampos1.add(psword);
        psword.setBounds(310, 122, 120, 20);

        tbPane2.addTab("Datos Usuario", JPanelCampos1);

        JPanelPago1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelPago1.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setOpaque(false);

        perfilusuarios.setModel(modelperfil = new DefaultTableModel(null, titulosperfil)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    if(column==4 ){
                        return true;
                    }else{
                        return false;}
                }
            });
            perfilusuarios.setFocusCycleRoot(true);
            perfilusuarios.setGridColor(new java.awt.Color(51, 51, 255));
            perfilusuarios.setName("perfilusuarios"); // NOI18N
            perfilusuarios.setRowHeight(20);
            perfilusuarios.setSelectionBackground(java.awt.SystemColor.activeCaption);
            jScrollPane3.setViewportView(perfilusuarios);

            JPanelPago1.add(jScrollPane3, java.awt.BorderLayout.CENTER);

            tbPane2.addTab("Perfil de Usuario", JPanelPago1);

            JPanelCampos.add(tbPane2, java.awt.BorderLayout.CENTER);

            panelImage.add(JPanelCampos);
            JPanelCampos.setBounds(0, 40, 880, 190);

            JPanelTable.setOpaque(false);
            JPanelTable.setPreferredSize(new java.awt.Dimension(786, 402));
            JPanelTable.setLayout(new java.awt.BorderLayout());

            jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            usuarios.setForeground(new java.awt.Color(51, 51, 51));
            usuarios.setModel(model = new DefaultTableModel(null, titulos)
                {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                usuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                usuarios.setFocusCycleRoot(true);
                usuarios.setGridColor(new java.awt.Color(51, 51, 255));
                usuarios.setRowHeight(22);
                usuarios.setSelectionBackground(java.awt.SystemColor.activeCaption);
                usuarios.setSurrendersFocusOnKeystroke(true);
                usuarios.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        usuariosMouseClicked(evt);
                    }
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        usuariosMouseClicked(evt);
                    }
                });
                usuarios.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        usuariosKeyPressed(evt);
                    }
                });
                jScrollPane1.setViewportView(usuarios);
                usuarios.getAccessibleContext().setAccessibleName("");

                JPanelTable.add(jScrollPane1, java.awt.BorderLayout.CENTER);

                panelImage.add(JPanelTable);
                JPanelTable.setBounds(0, 300, 880, 130);

                JPanelBusqueda.setBackground(java.awt.SystemColor.inactiveCaption);
                JPanelBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                JPanelBusqueda.setLayout(null);

                jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar.png"))); // NOI18N
                jLabel7.setText("Buscar Por:");
                JPanelBusqueda.add(jLabel7);
                jLabel7.setBounds(174, 2, 116, 40);

                busqueda.setPreferredSize(new java.awt.Dimension(250, 27));
                busqueda.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        busquedaActionPerformed(evt);
                    }
                });
                JPanelBusqueda.add(busqueda);
                busqueda.setBounds(300, 10, 250, 27);

                rbCodigo.setBackground(java.awt.SystemColor.inactiveCaption);
                rbCodigo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                rbCodigo.setForeground(new java.awt.Color(0, 102, 102));
                rbCodigo.setText("Codigo");
                rbCodigo.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        rbCodigoActionPerformed(evt);
                    }
                });
                JPanelBusqueda.add(rbCodigo);
                rbCodigo.setBounds(270, 40, 80, 25);

                rbNombres.setBackground(java.awt.SystemColor.inactiveCaption);
                rbNombres.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                rbNombres.setForeground(new java.awt.Color(0, 102, 102));
                rbNombres.setSelected(true);
                rbNombres.setText("Nombre");
                rbNombres.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        rbNombresActionPerformed(evt);
                    }
                });
                JPanelBusqueda.add(rbNombres);
                rbNombres.setBounds(380, 40, 90, 25);

                rbApellidos.setBackground(java.awt.SystemColor.inactiveCaption);
                rbApellidos.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                rbApellidos.setForeground(new java.awt.Color(0, 102, 102));
                rbApellidos.setText("Usuario");
                rbApellidos.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        rbApellidosActionPerformed(evt);
                    }
                });
                JPanelBusqueda.add(rbApellidos);
                rbApellidos.setBounds(500, 40, 90, 25);

                buttonMostrar1.setBackground(new java.awt.Color(102, 204, 0));
                buttonMostrar1.setText("Perfil usuario");
                buttonMostrar1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonMostrar1ActionPerformed(evt);
                    }
                });
                JPanelBusqueda.add(buttonMostrar1);
                buttonMostrar1.setBounds(620, 20, 120, 25);

                panelImage.add(JPanelBusqueda);
                JPanelBusqueda.setBounds(0, 230, 880, 70);

                pnlPaginador.setBackground(new java.awt.Color(57, 104, 163));
                pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
                pnlPaginador.setLayout(new java.awt.GridBagLayout());

                jLabel8.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
                jLabel8.setForeground(new java.awt.Color(255, 255, 255));
                jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/login.png"))); // NOI18N
                jLabel8.setText("<--Usuarios-->");
                pnlPaginador.add(jLabel8, new java.awt.GridBagConstraints());

                panelImage.add(pnlPaginador);
                pnlPaginador.setBounds(0, 0, 880, 40);

                getContentPane().add(panelImage, java.awt.BorderLayout.CENTER);

                setBounds(0, 0, 890, 512);
            }// </editor-fold>//GEN-END:initComponents

    private void bntNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntNuevoActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntNuevo.getName()) == true) {

            Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
            estado.setSelected(true);
            this.bntGuardar.setEnabled(true);
            this.bntModificar.setEnabled(false);
            this.bntEliminar.setEnabled(false);
            this.bntNuevo.setEnabled(false);
            nombres.requestFocus();
            MostrarProductos(1);
            tbPane2.setSelectedIndex(0);
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntNuevoActionPerformed

    private void bntGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardarActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntGuardar.getName()) == true) {

            if (Utilidades.esObligatorio(this.JPanelCampos, true)) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
            if (resp == 0) {
                PreparedStatement ps = null;
                conn = BdConexion.getConexion();

                //Variables usuario*************************************************
                int estad = 0, idusuario = 0;
                int n1 = 0;
                if (this.estado.isSelected()) {
                    estad = 1;
                }
                String fecha = FormatoFecha.getFormato(fecharegistro.getCalendar().getTime(), FormatoFecha.A_M_D);
                String sqlusuario = "insert into usuario (nombre, usuario, password, estado, fechacreacion)"
                        + " values ('" + nombres.getText() + "','" + usuario.getText() + "','" + password.getText() + "','" + estad + "','" + fecha + "')";
                //******************************************************************

                try {
                    conn.setAutoCommit(false);
                    System.out.print(sqlusuario + "\n");

                    ps = conn.prepareStatement(sqlusuario, PreparedStatement.RETURN_GENERATED_KEYS);
                    n1 = ps.executeUpdate();
                    if (n1 > 0) {
                        ResultSet rs = ps.getGeneratedKeys();
                        while (rs.next()) {
                            idusuario = rs.getInt(1);//retorna el idrecibo guardado
                        }

                        //Variables perfil usuario**********************************
                        boolean camprec = false;
                        int n = 0;
                        int cant2 = modelperfil.getRowCount();
                        int estadomenu = 0;
                        //**********************************************************

                        for (int i = 0; i < cant2; i++) {//for perfil usuarios
                            String id = (String) "" + perfilusuarios.getValueAt(i, 0);
                            if (perfilusuarios.getValueAt(i, 4).toString().equals("true")) {
                                estadomenu = 1;
                                camprec = true;
                            } else {
                                estadomenu = 0;
                            }
                            String descriprecibo = "insert into perfilusuario (menu_idmenu,usuario_idusuario,estado) "
                                    + "values ('" + id + "','" + idusuario + "','" + estadomenu + "')";
                            n = ps.executeUpdate(descriprecibo);
                        }//fin for perfil usuario

                        if (!camprec) {
                            JOptionPane.showInternalMessageDialog(this, "No se ha marcado ningun Acceso", "Mensage", JOptionPane.INFORMATION_MESSAGE);
                        }

                        if (n > 0) {
                            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
                            MostrarDatos(busqueda.getText());
                            this.bntGuardar.setEnabled(false);
                            this.bntModificar.setEnabled(false);
                            this.bntEliminar.setEnabled(false);
                            this.bntNuevo.setEnabled(true);
                            psword.setText("");
                            Cvercontraseña.setSelected(false);
                            busqueda.requestFocus();
                            JOptionPane.showInternalMessageDialog(this, "El dato se ha Guardado Correctamente", "Guardar", JOptionPane.INFORMATION_MESSAGE);
                        }

                    }
                    conn.commit();// guarda todas las consultas si no ubo error
                    ps.close();
                    if (!conn.getAutoCommit()) {
                        conn.setAutoCommit(true);
                    }
                } catch (SQLException ex) {
                    try {
                        conn.rollback();// no guarda ninguna de las consultas ya que ubo error
                        ps.close();
                        if (!conn.getAutoCommit()) {
                            conn.setAutoCommit(true);
                        }
                    } catch (SQLException ex1) {
                        Logger.getLogger(Pagos.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    JOptionPane.showMessageDialog(null, ex);
                }

                //seguardo = peticiones.guardarRegistros(nombreTabla, campos, valores);
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntGuardarActionPerformed

    private void bntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalirActionPerformed
        cerrarVentana();
    }//GEN-LAST:event_bntSalirActionPerformed

    private void usuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usuariosMouseClicked
        // TODO add your handling code here:
        filaseleccionada();

    }//GEN-LAST:event_usuariosMouseClicked

    private void bntEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntEliminar.getName()) == true) {

            int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Eliminar el Registro?", "Pregunta", 0);
            if (resp == 0) {

                int fila = usuarios.getSelectedRow();
                String id = (String) "" + usuarios.getValueAt(fila, 0);
                String nombreTabla = "usuario", nomColumnaCambiar = "estado";
                String nomColumnaId = "idusuario";
                int seguardo = 0;

                seguardo = peticiones.eliminarRegistro(nombreTabla, nomColumnaCambiar, nomColumnaId, id);

                if (seguardo == 1) {
                    Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
                    MostrarDatos(busqueda.getText());
                    this.bntGuardar.setEnabled(false);
                    this.bntModificar.setEnabled(false);
                    this.bntEliminar.setEnabled(false);
                    this.bntNuevo.setEnabled(true);
                    psword.setText("");
                    Cvercontraseña.setSelected(false);
                    busqueda.requestFocus();
                    JOptionPane.showInternalMessageDialog(this, "El dato se ha Eliminado Correctamente", "Eliminar", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntEliminarActionPerformed

    private void bntModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntModificarActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntModificar.getName()) == true) {

            if (Utilidades.esObligatorio(this.JPanelCampos, true)) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Modificar el Registro?", "Pregunta", 0);
            if (resp == 0) {

                PreparedStatement ps = null;
                conn = BdConexion.getConexion();

                try {

                    conn.setAutoCommit(false);
                    //Variables modificar usuaio***********************************
                    int n1 = 0;
                    int fila = usuarios.getSelectedRow();
                    String id = (String) "" + usuarios.getValueAt(fila, 0);
                    int estad = 0;
                    if (this.estado.isSelected()) {
                        estad = 1;
                    }
                    String fecha = FormatoFecha.getFormato(fecharegistro.getCalendar().getTime(), FormatoFecha.A_M_D);
                    String sql = "update usuario set nombre='" + nombres.getText() + "', " + " usuario='" + usuario.getText() + "', " + " password='" + password.getText() + "', " + " estado='" + estad + "', " + " fechacreacion='" + fecha + "'  where idusuario=" + id;
                    //**************************************************************

                    ps = conn.prepareStatement(sql);
                    n1 = ps.executeUpdate();

                    if (n1 > 0) {
                        //Variables perfil usuario**********************************
                        int n = 0;
                        int cant2 = modelperfil.getRowCount();
                        int estadomenu = 0;
                        //**********************************************************

                        for (int i = 0; i < cant2; i++) {//for perfil usuarios
                            String idmenu = (String) "" + perfilusuarios.getValueAt(i, 0);
                            if (perfilusuarios.getValueAt(i, 4).toString().equals("true")) {
                                estadomenu = 1;
                            } else {
                                estadomenu = 0;
                            }
                            String descriprecibo = "update perfilusuario set estado=" + estadomenu + " where idperfilusuario=" + idmenu;
                            n = ps.executeUpdate(descriprecibo);

                        }//fin for perfil usuario
                        if (n > 0) {
                            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
                            MostrarDatos(busqueda.getText());
                            busqueda.requestFocus();
                            psword.setText("");
                            Cvercontraseña.setSelected(false);
                            tbPane2.setSelectedIndex(0);
                            JOptionPane.showInternalMessageDialog(this, "El dato se ha Modificado Correctamente", "Modificar", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showInternalMessageDialog(this, "ERROR El dato no se ha Modificado Correctamente", "Modificar", JOptionPane.ERROR_MESSAGE);
                        }

                    } else {
                        JOptionPane.showInternalMessageDialog(this, "ERROR El dato no se ha Modificado Correctamente", "Modificar", JOptionPane.ERROR_MESSAGE);
                    }
                    conn.commit();// guarda todas las consultas si no ubo error
                    ps.close();
                    if (!conn.getAutoCommit()) {
                        conn.setAutoCommit(true);
                    }

                } catch (Exception e) {
                    try {
                        conn.rollback();// no guarda ninguna de las consultas ya que ubo error
                        ps.close();
                        if (!conn.getAutoCommit()) {
                            conn.setAutoCommit(true);
                        }
                    } catch (SQLException ex1) {
                        JOptionPane.showMessageDialog(null, ex1);
                    }
                    JOptionPane.showMessageDialog(null, e);

                }

            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntModificarActionPerformed

    private void bntCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntCancelarActionPerformed
        // TODO add your handling code here:
        Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
        Utilidades.esObligatorio(this.JPanelCampos, false);
        removejtable();
        this.bntGuardar.setEnabled(false);
        this.bntModificar.setEnabled(false);
        this.bntEliminar.setEnabled(false);
        this.bntNuevo.setEnabled(true);
        removejtable();
        removejtable2();
        busqueda.setText("");
        psword.setText("");
        Cvercontraseña.setSelected(false);
        busqueda.requestFocus();
        tbPane2.setSelectedIndex(0);

    }//GEN-LAST:event_bntCancelarActionPerformed

    private void rbCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCodigoActionPerformed
        // TODO add your handling code here:
        rbNombres.setSelected(false);
        rbApellidos.setSelected(false);
        busqueda.requestFocus();
    }//GEN-LAST:event_rbCodigoActionPerformed

    private void rbNombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNombresActionPerformed
        // TODO add your handling code here:
        rbCodigo.setSelected(false);
        rbApellidos.setSelected(false);
        busqueda.requestFocus();
    }//GEN-LAST:event_rbNombresActionPerformed

    private void busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busquedaActionPerformed
        // TODO add your handling code here:
        MostrarDatos(busqueda.getText());
    }//GEN-LAST:event_busquedaActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing

    private void usuariosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usuariosKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == java.awt.event.KeyEvent.VK_SPACE) {
            filaseleccionada();
        }
        if (key == java.awt.event.KeyEvent.VK_DOWN || key == java.awt.event.KeyEvent.VK_UP) {
            limpiar();
        }
    }//GEN-LAST:event_usuariosKeyPressed

    private void rbApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbApellidosActionPerformed
        // TODO add your handling code here:
        rbNombres.setSelected(false);
        rbCodigo.setSelected(false);
        busqueda.requestFocus();
    }//GEN-LAST:event_rbApellidosActionPerformed

    private void buttonMostrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMostrar1ActionPerformed
        // TODO add your handling code here:
        tbPane2.setSelectedIndex(1);
    }//GEN-LAST:event_buttonMostrar1ActionPerformed

    private void estadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_estadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_estadoActionPerformed

    private void CvercontraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CvercontraseñaActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(Cvercontraseña.getName()) == true) {
            if (Cvercontraseña.isSelected()) {
                if (usuarios.getSelectedRow() == -1) {
                } else {
                    psword.setText(password.getText());
                }
            } else {
                psword.setText("");
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }

    }//GEN-LAST:event_CvercontraseñaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Cvercontraseña;
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelCampos;
    private javax.swing.JPanel JPanelCampos1;
    private javax.swing.JPanel JPanelPago1;
    private javax.swing.JPanel JPanelTable;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntEliminar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntModificar;
    private elaprendiz.gui.button.ButtonRect bntNuevo;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.textField.TextField busqueda;
    private elaprendiz.gui.button.ButtonRect buttonMostrar1;
    private javax.swing.JRadioButton estado;
    private com.toedter.calendar.JDateChooser fecharegistro;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private elaprendiz.gui.textField.TextField nombres;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private elaprendiz.gui.passwordField.PasswordFieldRectIcon password;
    private javax.swing.JTable perfilusuarios;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador;
    private javax.swing.JLabel psword;
    private javax.swing.JRadioButton rbApellidos;
    private javax.swing.JRadioButton rbCodigo;
    private javax.swing.JRadioButton rbNombres;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane2;
    private elaprendiz.gui.textField.TextField usuario;
    private javax.swing.JTable usuarios;
    // End of variables declaration//GEN-END:variables
}
