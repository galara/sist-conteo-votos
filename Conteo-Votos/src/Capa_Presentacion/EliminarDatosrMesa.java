/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Capa_Presentacion;

import Capa_Negocio.FiltroCampos;
import Capa_Negocio.Peticiones;
import Capa_Negocio.TipoFiltro;
import Capa_Negocio.Utilidades;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
public class EliminarDatosrMesa extends javax.swing.JInternalFrame {

    //private static Horario frmHorario = new Horario();
    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model;
    DefaultComboBoxModel modelCombo;
    String[] titulos = {"Código", "Centro", "Estado","Id","Municipio","idcentro","idmunicipio"};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();
    int nidalumno;
    //public Hashtable<String, String> hashGrupo = new Hashtable<>();

    /*Se hace una instancia de la clase que recibira las peticiones de mensages de la capa de aplicación*/
    //public static JOptionMessage msg = new JOptionMessage();
    /**
     * Creates new form Cliente
     */
    public EliminarDatosrMesa() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();
        limpiar();
        
        alumnos.getColumnModel().getColumn(5).setMaxWidth(0);
        alumnos.getColumnModel().getColumn(5).setMinWidth(0);
        alumnos.getColumnModel().getColumn(5).setPreferredWidth(0);
        alumnos.doLayout();
        alumnos.getColumnModel().getColumn(6).setMaxWidth(0);
        alumnos.getColumnModel().getColumn(6).setMinWidth(0);
        alumnos.getColumnModel().getColumn(6).setPreferredWidth(0);
        alumnos.doLayout();
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
        String[] campos = {"mesa.nombre", "centro.nombre", "mesa.estado", "mesa.idmesa","municipio.nombre","centro.idcentro","municipio.idmunicipio"};
        String[] condiciones = {"mesa.estado=1 and mesa.nombre"};
        String[] Id = {Dato};
        String inner = " INNER JOIN centro on mesa.centro_idcentro=centro.idcentro INNER JOIN municipio on centro.municipio_idmunicipio=municipio.idmunicipio ";

        if (this.rbCodigo.isSelected()) {
            if (!Dato.isEmpty()) {
                removejtable();
                model = peticiones.getRegistroPorPks(model, "mesa", campos, condiciones, Id, inner);
            } else {
                JOptionPane.showInternalMessageDialog(this, "Debe ingresar un codigo para la busqueda");
            }
        }
        if (this.rbNombre.isSelected()) {
            removejtable();
            model = peticiones.getRegistroPorLike(model, "mesa", campos, "mesa.estado=1 and centro.nombre", Dato, inner);
        }
        if (this.rbApellido.isSelected()) {
            removejtable();
            model = peticiones.getRegistroPorLike(model, "mesa", campos, "mesa.estado=1 and municipio.nombre", Dato, inner);
        }
        Utilidades.ajustarAnchoColumnas(alumnos);
        alumnos.getColumnModel().getColumn(5).setMaxWidth(0);
        alumnos.getColumnModel().getColumn(5).setMinWidth(0);
        alumnos.getColumnModel().getColumn(5).setPreferredWidth(0);
        alumnos.doLayout();
        alumnos.getColumnModel().getColumn(6).setMaxWidth(0);
        alumnos.getColumnModel().getColumn(6).setMinWidth(0);
        alumnos.getColumnModel().getColumn(6).setPreferredWidth(0);
        alumnos.doLayout();
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
//        String inner = " grupo INNER JOIN alumnosengrupo ON grupo.idgrupo = alumnosengrupo.grupo_idgrupo INNER JOIN alumno ON alumnosengrupo.alumno_idalumno = alumno.idmesa ";
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

        popupeliminarmesa = new javax.swing.JPopupMenu();
        Eliminar_Datos_mesa = new javax.swing.JMenuItem();
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
        jLabel1 = new javax.swing.JLabel();
        pnlPaginador = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();

        Eliminar_Datos_mesa.setText("Eliminar datos de mesa");
        Eliminar_Datos_mesa.setName("Carrera Principal"); // NOI18N
        Eliminar_Datos_mesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Eliminar_Datos_mesaActionPerformed(evt);
            }
        });
        popupeliminarmesa.add(Eliminar_Datos_mesa);

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Mesas");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("buscarmesa"); // NOI18N
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
            alumnos.setComponentPopupMenu(popupeliminarmesa);
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
            jLabel7.setText("Buscar :");
            JPanelBusqueda.add(jLabel7);
            jLabel7.setBounds(172, 2, 100, 40);

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
            rbNombre.setText("Centro");
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
            rbApellido.setText("Municipio");
            rbApellido.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbApellidoActionPerformed(evt);
                }
            });
            JPanelBusqueda.add(rbApellido);
            rbApellido.setBounds(490, 40, 90, 25);

            jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
            jLabel1.setForeground(new java.awt.Color(0, 51, 255));
            jLabel1.setText("Seleccione linea y Clic derecho");
            JPanelBusqueda.add(jLabel1);
            jLabel1.setBounds(650, 50, 220, 15);

            panelImage.add(JPanelBusqueda);
            JPanelBusqueda.setBounds(0, 40, 880, 70);

            pnlPaginador.setBackground(new java.awt.Color(57, 104, 163));
            pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
            pnlPaginador.setLayout(new java.awt.GridBagLayout());

            jLabel8.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
            jLabel8.setForeground(new java.awt.Color(255, 255, 255));
            jLabel8.setText("<--Borrar datos de Mesa-->");
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
//        int key = evt.getKeyCode();
//        if (key == java.awt.event.KeyEvent.VK_ENTER) {
//            int p = alumnos.getSelectedRow();
//
//            codigomesa.setText(alumnos.getValueAt(p, 0).toString());
//            //filaseleccionada(alumnos.getValueAt(p, 0).toString());
//            //cGrupo.removeAllItems();
//            //Ingreso_Votos.llenarcombogrupo(alumnos.getValueAt(p, 0).toString());
//            nombrecentro.setText(alumnos.getValueAt(p, 1).toString());
//            //beca.setText(alumnos.getValueAt(p, 4).toString());
//            //Date fechaini = FormatoFecha.StringToDate(alumnos.getValueAt(p, 5).toString());
//            //inicioalumno.setDate(fechaini);
//
////            if (alumnos.getValueAt(p, 2).toString().equals("Inactivo")) {
////                estado.setText(alumnos.getValueAt(p, 2).toString());
////                //estado.setText("Inactivo");
////                estado.setForeground(Color.red);
////            } else if (alumnos.getValueAt(p, 2).toString().equals("Activo")) {
////                estado.setText(alumnos.getValueAt(p, 2).toString());
////                estado.setForeground(Color.WHITE);
////            }
//            idmesa=(alumnos.getValueAt(p, 3).toString());
//            nombremunicipio.setText(alumnos.getValueAt(p, 4).toString());
//            idcentro.setText(alumnos.getValueAt(p, 5).toString());
//            idmunicipio.setText(alumnos.getValueAt(p, 6).toString());
//            
//            this.dispose();
//        }
    }//GEN-LAST:event_alumnosKeyPressed

    private void Eliminar_Datos_mesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Eliminar_Datos_mesaActionPerformed
        // TODO add your handling code here:
        int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Eliminar el Registro?", "Pregunta", 0);
            if (resp == 0) {

//                int fila = alumnos.getSelectedRow();
//                String id = (String) "" + alumnos.getValueAt(fila, 0);
//                String nombreTabla = "candidato", nomColumnaCambiar = "estado";
//                String nomColumnaId = "codigo";
                int seguardo = 0;
                
                
                
                int p = alumnos.getSelectedRow();
                String idmesa=(alumnos.getValueAt(p, 3).toString());
                String sql="DELETE FROM detalle_votos WHERE detalle_votos.mesa_idmesa="+idmesa;
                //String codigomesa=(alumnos.getValueAt(p, 0).toString());
                seguardo = peticiones.eliminacionMesa(sql);
                
                if (seguardo == 1) {
                    //Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
                    MostrarDatos(busqueda.getText());
                    //this.bntGuardar.setEnabled(false);
                    //this.bntModificar.setEnabled(false);
                   // this.bntEliminar.setEnabled(false);
                   // this.bntNuevo.setEnabled(true);
                    busqueda.requestFocus();
                    JOptionPane.showInternalMessageDialog(this, "El dato se ha Eliminado Correctamente", "Eliminar", JOptionPane.INFORMATION_MESSAGE);
                }
            }
//       // if (AccesoUsuario.AccesosUsuario(Eliminardatosmesa.getName()) == true) {
//            Cargo_politico frmCarrera = new Cargo_politico();
//            if (frmCarrera == null) {
//                frmCarrera = new Cargo_politico();
//            }
//            adminInternalFrame(dp, frmCarrera);
//        } else {
//            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
//        }
    }//GEN-LAST:event_Eliminar_Datos_mesaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Eliminar_Datos_mesa;
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelTable;
    private javax.swing.JTable alumnos;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.textField.TextField busqueda;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador;
    private javax.swing.JPopupMenu popupeliminarmesa;
    private javax.swing.JRadioButton rbApellido;
    private javax.swing.JRadioButton rbCodigo;
    private javax.swing.JRadioButton rbNombre;
    // End of variables declaration//GEN-END:variables
}
