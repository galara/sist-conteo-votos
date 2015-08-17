/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Capa_Presentacion;

import Capa_Negocio.FiltroCampos;
import Capa_Negocio.FormatoFecha;
import Capa_Negocio.Peticiones;
import Capa_Negocio.TipoFiltro;
import Capa_Negocio.Utilidades;
import static Capa_Presentacion.Pagos.beca;
import static Capa_Presentacion.Pagos.cGrupo;
import static Capa_Presentacion.Pagos.codigoa;
import static Capa_Presentacion.Pagos.estado;
import static Capa_Presentacion.Pagos.inicioalumno;
import static Capa_Presentacion.Pagos.nombrealumno;
import static Capa_Presentacion.Pagos.idalumno;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author GLARA
 */
public class BuscarAlumno extends javax.swing.JInternalFrame {

    private static Horario frmHorario = new Horario();
    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model;
    DefaultComboBoxModel modelCombo;
    String[] titulos = {"Código", "Nombres", "Apellidos", "Fecha Nec", "Estado","Id"};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();
    int nidalumno;
    //public Hashtable<String, String> hashGrupo = new Hashtable<>();

    /*Se hace una instancia de la clase que recibira las peticiones de mensages de la capa de aplicación*/
    //public static JOptionMessage msg = new JOptionMessage();
    /**
     * Creates new form Cliente
     */
    public BuscarAlumno() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();
        limpiar();
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
        int nu = JOptionPane.showInternalConfirmDialog(this, "Todos los datos que no se ha guardadox "
                + "se perderan.\n"
                + "¿Desea Cerrar esta ventana?", "Cerrar ventana", JOptionPane.YES_NO_OPTION);
        if (nu == JOptionPane.YES_OPTION || nu == 0) {
            //this.bntGuardar.setEnabled(false);
            removejtable();
            busqueda.setText("");
            rbNombre.setSelected(true);
            rbCodigo.setSelected(false);
            busqueda.requestFocus();
            this.dispose();
        }
    }

    /* La funcion de este metodo es limpiar y desabilitar campos que se encuentren en un contenedor
     * ejem: los JTextFiel de un panel, se envian a la capa de negocio "Utilidades.setEditableTexto()" 
     * para que este los limpie,habilite o desabilite dichos componentes */
    public void limpiar() {
//        Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
        busqueda.requestFocus();
    }

    /* Para no sobrecargar la memoria y hacer una instancia cada vez que actualizamos la JTable se hace una
     * sola instancia y lo unico que se hace antes de actualizar la JTable es limpiar el modelo y enviarle los
     * nuevos datos a mostrar en la JTable  */
    public void removejtable() {
        while (alumnos.getRowCount() != 0) {
            model.removeRow(0);
        }
    }

    /* Este metodo se encarga de filtrar los datos que se deben ingresar en cada uno de los campos del formulario
     * podemos indicar que el usuario ingrese solo numeros , solo letras, numeros y letras, o cualquier caracter
     * tambien podemos validar si se aseptaran espacios en blanco en la cadena ingresada , para mas detalle visualizar
     * la clase TipoFiltro()  */
    private void setFiltroTexto() {

        TipoFiltro.setFiltraEntrada(busqueda.getDocument(), FiltroCampos.NUM_LETRAS, 100, true);
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
        String[] campos = {"alumno.codigo", "alumno.nombres", "alumno.apellidos", "DATE_FORMAT(alumno.fechanacimiento,'%d-%m-%Y')", "alumno.estado","alumno.idalumno"};
        String[] condiciones = {"alumno.estado=1 and alumno.codigo"};
        String[] Id = {Dato};

        if (this.rbCodigo.isSelected()) {
            if (!Dato.isEmpty()) {
                removejtable();
                model = peticiones.getRegistroPorPks(model, "alumno", campos, condiciones, Id, "");
            } else {
                JOptionPane.showInternalMessageDialog(this, "Debe ingresar un codigo para la busqueda");
            }
        }
        if (this.rbNombre.isSelected()) {
            removejtable();
            model = peticiones.getRegistroPorLike(model, "alumno", campos, "alumno.estado=1 and alumno.nombres", Dato, "");
        }
        if (this.rbApellido.isSelected()) {
            removejtable();
            model = peticiones.getRegistroPorLike(model, "alumno", campos, "alumno.estado=1 and alumno.apellidos", Dato, "");
        }
        Utilidades.ajustarAnchoColumnas(alumnos);
    }

//    /* Este metodo  consulta en la BD el codigo de la fila seleccionada y llena los componentes
//     * de la parte superior del formulario con los datos obtenidos en la capa de Negocio getRegistroSeleccionado().
//     * 
//     * @return 
//     */
//    private void filaseleccionada(String codigo) {
//
//        String[] cond = {"alumno.codigo"};
//        String[] id = {codigo};
//
//        String inner = " grupo INNER JOIN alumnosengrupo ON grupo.idgrupo = alumnosengrupo.grupo_idgrupo INNER JOIN alumno ON alumnosengrupo.alumno_idalumno = alumno.idalumno ";
//        if (!codigo.isEmpty()) {
//
//            String conct = "concat(grupo.codigo,' ',grupo.descripcion)";
//            String[] campos = {"grupo.dia", conct, "grupo.idgrupo"};
//
//            ResultSet rs;
//            AccesoDatos ac = new AccesoDatos();
//
//            rs = ac.getRegistros("grupo", campos, cond, id, inner);
//
//            if (rs != null) {
//                try {
//                    if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
//                        rs.beforeFirst();//regresa el puntero al primer registro
//                        while (rs.next()) {//mientras tenga registros que haga lo siguiente
//                            int pr = 0;
//                            cDia.setSelectedItem(rs.getString(1));
//                            pr = Integer.parseInt(hashGrupo.get(rs.getString(2)));
//                            cGrupo.setSelectedIndex(pr);
//                        }
//                    }
//                } catch (SQLException e) {
//                    JOptionPane.showInternalMessageDialog(this, e);
//                }
//            }
//            //this.bntGuardar.setEnabled(false);
//        }
//    }
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
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        JPanelTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        alumnos = new javax.swing.JTable();
        JPanelBusqueda = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        busqueda = new elaprendiz.gui.textField.TextField();
        rbCodigo = new javax.swing.JRadioButton();
        rbNombre = new javax.swing.JRadioButton();
        rbApellido = new javax.swing.JRadioButton();
        pnlPaginador = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Alumnos");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("buscaralumnos"); // NOI18N
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
        pnlActionButtons.setBounds(0, 240, 880, 50);

        JPanelTable.setOpaque(false);
        JPanelTable.setPreferredSize(new java.awt.Dimension(786, 402));
        JPanelTable.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        alumnos.setForeground(new java.awt.Color(51, 51, 51));
        alumnos.setModel(model = new DefaultTableModel(null, titulos)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            alumnos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            alumnos.setFocusCycleRoot(true);
            alumnos.setGridColor(new java.awt.Color(51, 51, 255));
            alumnos.setRowHeight(22);
            alumnos.setSelectionBackground(java.awt.SystemColor.activeCaption);
            alumnos.setSurrendersFocusOnKeystroke(true);
            alumnos.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    alumnosKeyPressed(evt);
                }
            });
            jScrollPane1.setViewportView(alumnos);
            alumnos.getAccessibleContext().setAccessibleName("");

            JPanelTable.add(jScrollPane1, java.awt.BorderLayout.CENTER);

            panelImage.add(JPanelTable);
            JPanelTable.setBounds(0, 110, 880, 130);

            JPanelBusqueda.setBackground(java.awt.SystemColor.inactiveCaption);
            JPanelBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            JPanelBusqueda.setLayout(null);

            jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar.png"))); // NOI18N
            jLabel7.setText("Buscar Alumno por:");
            JPanelBusqueda.add(jLabel7);
            jLabel7.setBounds(117, 2, 173, 40);

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
            rbCodigo.setBounds(280, 40, 80, 25);

            rbNombre.setBackground(java.awt.SystemColor.inactiveCaption);
            rbNombre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbNombre.setForeground(new java.awt.Color(0, 102, 102));
            rbNombre.setSelected(true);
            rbNombre.setText("Nombre");
            rbNombre.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbNombreActionPerformed(evt);
                }
            });
            JPanelBusqueda.add(rbNombre);
            rbNombre.setBounds(380, 40, 90, 25);

            rbApellido.setBackground(java.awt.SystemColor.inactiveCaption);
            rbApellido.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbApellido.setForeground(new java.awt.Color(0, 102, 102));
            rbApellido.setText("Apellido");
            rbApellido.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbApellidoActionPerformed(evt);
                }
            });
            JPanelBusqueda.add(rbApellido);
            rbApellido.setBounds(490, 40, 79, 25);

            panelImage.add(JPanelBusqueda);
            JPanelBusqueda.setBounds(0, 40, 880, 70);

            pnlPaginador.setBackground(new java.awt.Color(57, 104, 163));
            pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
            pnlPaginador.setLayout(new java.awt.GridBagLayout());

            jLabel8.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
            jLabel8.setForeground(new java.awt.Color(255, 255, 255));
            jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/alumno.png"))); // NOI18N
            jLabel8.setText("<--Alumnos-->");
            pnlPaginador.add(jLabel8, new java.awt.GridBagConstraints());

            panelImage.add(pnlPaginador);
            pnlPaginador.setBounds(0, 0, 880, 40);

            getContentPane().add(panelImage, java.awt.BorderLayout.CENTER);

            getAccessibleContext().setAccessibleName("Profesores");

            setBounds(0, 0, 890, 322);
        }// </editor-fold>//GEN-END:initComponents

    private void bntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalirActionPerformed
        cerrarVentana();
    }//GEN-LAST:event_bntSalirActionPerformed

    private void bntCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntCancelarActionPerformed
        // TODO add your handling code here:
        removejtable();
        busqueda.requestFocus();

    }//GEN-LAST:event_bntCancelarActionPerformed

    private void rbCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCodigoActionPerformed
        // TODO add your handling code here:
        rbNombre.setSelected(false);
        rbApellido.setSelected(false);
        busqueda.requestFocus();
    }//GEN-LAST:event_rbCodigoActionPerformed

    private void rbNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNombreActionPerformed
        // TODO add your handling code here:
        rbCodigo.setSelected(false);
        rbApellido.setSelected(false);
        busqueda.requestFocus();
    }//GEN-LAST:event_rbNombreActionPerformed

    private void busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busquedaActionPerformed
        // TODO add your handling code here:
        MostrarDatos(busqueda.getText());
    }//GEN-LAST:event_busquedaActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing

    private void rbApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbApellidoActionPerformed
        // TODO add your handling code here:
        rbCodigo.setSelected(false);
        rbNombre.setSelected(false);
        busqueda.requestFocus();
    }//GEN-LAST:event_rbApellidoActionPerformed

    private void alumnosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == java.awt.event.KeyEvent.VK_ENTER) {
            int p = alumnos.getSelectedRow();

            codigoa.setText(alumnos.getValueAt(p, 0).toString());
            //filaseleccionada(alumnos.getValueAt(p, 0).toString());
            cGrupo.removeAllItems();
            Pagos.llenarcombogrupo(alumnos.getValueAt(p, 0).toString());
            nombrealumno.setText(alumnos.getValueAt(p, 1).toString() + " " + alumnos.getValueAt(p, 2).toString());
            //beca.setText(alumnos.getValueAt(p, 4).toString());
            //Date fechaini = FormatoFecha.StringToDate(alumnos.getValueAt(p, 5).toString());
            //inicioalumno.setDate(fechaini);

            if (alumnos.getValueAt(p, 4).toString().equals("Inactivo")) {
                estado.setText(alumnos.getValueAt(p, 4).toString());
                //estado.setText("Inactivo");
                estado.setForeground(Color.red);
            } else if (alumnos.getValueAt(p, 4).toString().equals("Activo")) {
                estado.setText(alumnos.getValueAt(p, 4).toString());
                estado.setForeground(Color.WHITE);
            }
            idalumno=(alumnos.getValueAt(p, 5).toString());
            this.dispose();
        }
    }//GEN-LAST:event_alumnosKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelTable;
    private javax.swing.JTable alumnos;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.textField.TextField busqueda;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador;
    private javax.swing.JRadioButton rbApellido;
    private javax.swing.JRadioButton rbCodigo;
    private javax.swing.JRadioButton rbNombre;
    // End of variables declaration//GEN-END:variables
}
