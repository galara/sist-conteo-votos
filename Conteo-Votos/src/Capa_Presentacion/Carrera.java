/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Capa_Presentacion;

import Capa_Negocio.AccesoUsuario;
import Capa_Negocio.FiltroCampos;
import Capa_Negocio.Peticiones;
import Capa_Negocio.TipoFiltro;
import Capa_Negocio.Utilidades;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author GLARA
 */
public class Carrera extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model;
    String[] titulos = {"Código", "Descripción", "Estado"};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();

    /*Se hace una instancia de la clase que recibira las peticiones de mensages de la capa de aplicación*/
    //public static JOptionMessage msg = new JOptionMessage();
    /**
     * Creates new form Cliente
     */
    public Carrera() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();
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

    /*Este metodo visualiza una mensage de cinfirmación al usuario antes de Cerrar la ventana,
     * si por eror se intento cerrar el formulario devera indicar que "NO" para no perder los datos
     * que no haya Guardado de lo contrario presiona "SI" y se cerrara la ventana sin Guardar ningun dato. */
    private void cerrarVentana() {
        int nu = JOptionPane.showInternalConfirmDialog(this, "Todos los datos que no se ha guardado"
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
            busqueda.setText("");
            rbNombres.setSelected(true);
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

        TipoFiltro.setFiltraEntrada(descripcion.getDocument(), FiltroCampos.NUM_LETRAS, 100, true);
        TipoFiltro.setFiltraEntrada(observacion.getDocument(), FiltroCampos.NUM_LETRAS, 150, true);
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
        String[] campos = {"carrera.idcarrera", "carrera.descripcion", "carrera.estado"};
        String[] condiciones = {"carrera.idcarrera"};
        String[] Id = {Dato};

//        if (this.rbCodigo.isSelected()) {
//            if (!Dato.isEmpty()) {
//                removejtable();
//                Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
//                Utilidades.esObligatorio(this.JPanelCampos, false);
//                model = peticiones.getRegistroPorPks(model, "usuario", campos, condiciones, Id, "");
//            } else {
//                JOptionPane.showInternalMessageDialog(this, "Debe ingresar un codigo para la busqueda");
//            }
//        }
        if (this.rbNombres.isSelected()) {
            removejtable();
            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
            Utilidades.esObligatorio(this.JPanelCampos, false);
            model = peticiones.getRegistroPorLike(model, "carrera", campos, "carrera.descripcion", Dato, "");
        }
//        if (this.rbApellidos.isSelected()) {
//            removejtable();
//            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
//            Utilidades.esObligatorio(this.JPanelCampos, false);
//            model = peticiones.getRegistroPorLike(model, "usuario", campos, "usuario.usuario", Dato, "");
//        }
        Utilidades.ajustarAnchoColumnas(usuarios);
    }

    /* Este metodo  consulta en la BD el codigo de la fila seleccionada y llena los componentes
     * de la parte superior del formulario con los datos obtenidos.
     * 
     * @return 
     */
    private void filaseleccionada() {
        int fila = usuarios.getSelectedRow();
        String[] cond = {"carrera.idcarrera"};
        String[] id = {"" + usuarios.getValueAt(fila, 0)};
        if (usuarios.getValueAt(fila, 0) != null) {

            String[] campos = {"carrera.descripcion", "carrera.estado", "carrera.observacion"};
            Component[] cmps = {descripcion, estado, observacion};
            Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
            peticiones.getRegistroSeleccionado(cmps, "carrera", campos, cond, id, "", null);

            this.bntGuardar.setEnabled(false);
            this.bntModificar.setEnabled(true);
            this.bntEliminar.setEnabled(true);
            this.bntNuevo.setEnabled(false);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        descripcion = new elaprendiz.gui.textField.TextField();
        estado = new javax.swing.JRadioButton();
        observacion = new elaprendiz.gui.textField.TextField();
        JPanelTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        usuarios = new javax.swing.JTable();
        JPanelBusqueda = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        busqueda = new elaprendiz.gui.textField.TextField();
        rbNombres = new javax.swing.JRadioButton();
        pnlPaginador = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Carreras");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("carrera"); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
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
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
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
        bntNuevo.setName("Nueva Carrera"); // NOI18N
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
        bntModificar.setName("Modificar Carrera"); // NOI18N
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
        bntGuardar.setName("Guardar Carrera"); // NOI18N
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
        bntEliminar.setName("Eliminar Carrera"); // NOI18N
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
        JPanelCampos.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Descripción:");
        JPanelCampos.add(jLabel1);
        jLabel1.setBounds(90, 60, 90, 20);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Observación:");
        JPanelCampos.add(jLabel2);
        jLabel2.setBounds(80, 110, 100, 20);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Estado:");
        JPanelCampos.add(jLabel4);
        jLabel4.setBounds(500, 60, 110, 20);

        descripcion.setEditable(false);
        descripcion.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        descripcion.setName("descripcion"); // NOI18N
        JPanelCampos.add(descripcion);
        descripcion.setBounds(190, 60, 250, 21);

        estado.setBackground(new java.awt.Color(51, 153, 255));
        estado.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        estado.setForeground(new java.awt.Color(255, 255, 255));
        estado.setSelected(true);
        estado.setText("Activo");
        estado.setEnabled(false);
        estado.setName("JRadioButton"); // NOI18N
        JPanelCampos.add(estado);
        estado.setBounds(620, 60, 160, 21);

        observacion.setEditable(false);
        observacion.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        JPanelCampos.add(observacion);
        observacion.setBounds(190, 110, 590, 21);

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
            jLabel7.setBounds(170, 2, 120, 40);

            busqueda.setPreferredSize(new java.awt.Dimension(250, 27));
            busqueda.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    busquedaActionPerformed(evt);
                }
            });
            JPanelBusqueda.add(busqueda);
            busqueda.setBounds(300, 10, 250, 27);

            rbNombres.setBackground(java.awt.SystemColor.inactiveCaption);
            rbNombres.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbNombres.setForeground(new java.awt.Color(0, 102, 102));
            rbNombres.setSelected(true);
            rbNombres.setText("Descripción");
            rbNombres.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbNombresActionPerformed(evt);
                }
            });
            JPanelBusqueda.add(rbNombres);
            rbNombres.setBounds(370, 40, 110, 25);

            panelImage.add(JPanelBusqueda);
            JPanelBusqueda.setBounds(0, 230, 880, 70);

            pnlPaginador.setBackground(new java.awt.Color(57, 104, 163));
            pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
            pnlPaginador.setLayout(new java.awt.GridBagLayout());

            jLabel8.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
            jLabel8.setForeground(new java.awt.Color(255, 255, 255));
            jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/carrera.png"))); // NOI18N
            jLabel8.setText("<--Carreras-->");
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
            descripcion.requestFocus();
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

                boolean seguardo = false;
                String nombreTabla = "carrera";
                String campos = "descripcion, estado, observacion";
                int estad = 0;
                if (this.estado.isSelected()) {
                    estad = 1;
                }
                Object[] valores = {descripcion.getText(), estad, observacion.getText()
                };

                seguardo = peticiones.guardarRegistros(nombreTabla, campos, valores);
                if (seguardo) {
                    Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
                    MostrarDatos(busqueda.getText());
                    this.bntGuardar.setEnabled(false);
                    this.bntModificar.setEnabled(false);
                    this.bntEliminar.setEnabled(false);
                    this.bntNuevo.setEnabled(true);
                    busqueda.requestFocus();
                    JOptionPane.showInternalMessageDialog(this, "El dato se ha Guardado Correctamente", "Guardar", JOptionPane.INFORMATION_MESSAGE);
                }
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
        if (AccesoUsuario.AccesosUsuario(bntNuevo.getName()) == true) {

            int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Eliminar el Registro?", "Pregunta", 0);
            if (resp == 0) {

                int fila = usuarios.getSelectedRow();
                String id = (String) "" + usuarios.getValueAt(fila, 0);
                String nombreTabla = "carrera", nomColumnaCambiar = "estado";
                String nomColumnaId = "idcarrera";
                int seguardo = 0;

                seguardo = peticiones.eliminarRegistro(nombreTabla, nomColumnaCambiar, nomColumnaId, id);

                if (seguardo == 1) {
                    Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
                    MostrarDatos(busqueda.getText());
                    this.bntGuardar.setEnabled(false);
                    this.bntModificar.setEnabled(false);
                    this.bntEliminar.setEnabled(false);
                    this.bntNuevo.setEnabled(true);
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

                String nomTabla = "carrera";
                String columnaId = "idcarrera";
                int seguardo = 0;
                int fila = usuarios.getSelectedRow();
                String id = (String) "" + usuarios.getValueAt(fila, 0);
                String campos = "descripcion, estado, observacion";

                int estad = 0;
                if (this.estado.isSelected()) {
                    estad = 1;
                }
                Object[] valores = {descripcion.getText(), estad, observacion.getText(), id
                };

                seguardo = peticiones.actualizarRegistro(nomTabla, campos, valores, columnaId, id);
                if (seguardo == 1) {
                    Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
                    MostrarDatos(busqueda.getText());
                    busqueda.requestFocus();
                    JOptionPane.showInternalMessageDialog(this, "El dato se ha Modificado Correctamente", "Modificar", JOptionPane.INFORMATION_MESSAGE);
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
        busqueda.setText("");
        busqueda.requestFocus();

    }//GEN-LAST:event_bntCancelarActionPerformed

    private void rbNombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNombresActionPerformed
        // TODO add your handling code here:
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelCampos;
    private javax.swing.JPanel JPanelTable;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntEliminar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntModificar;
    private elaprendiz.gui.button.ButtonRect bntNuevo;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.textField.TextField busqueda;
    private elaprendiz.gui.textField.TextField descripcion;
    private javax.swing.JRadioButton estado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private elaprendiz.gui.textField.TextField observacion;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador;
    private javax.swing.JRadioButton rbNombres;
    private javax.swing.JTable usuarios;
    // End of variables declaration//GEN-END:variables
}
