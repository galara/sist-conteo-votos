/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Capa_Presentacion;

import Capa_Negocio.AccesoUsuario;
import Capa_Negocio.FormatoFecha;
import Capa_Negocio.Peticiones;
import Capa_Negocio.Utilidades;
import Reportes.Pagos_diarios;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Calendar;
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
public class PagosDiarios extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model;
    DefaultComboBoxModel modelCombo;
    String[] titulos = {"Id", "Descripción", "Fecha Inicio", "Fecha Fin", "Estado"};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();

    /*Se hace una instancia de la clase que recibira las peticiones de mensages de la capa de aplicación*/
    //public static JOptionMessage msg = new JOptionMessage();
    /**
     * Creates new form Cliente
     */
    public PagosDiarios() {
        initComponents();
        //setFiltroTexto();
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
        int nu = JOptionPane.showInternalConfirmDialog(this, "Todos los datos que no se ha guardadox "
                + "se perderan.\n"
                + "¿Desea Cerrar esta ventana?", "Cerrar ventana", JOptionPane.YES_NO_OPTION);
        if (nu == JOptionPane.YES_OPTION || nu == 0) {
            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
            Utilidades.esObligatorio(this.JPanelCampos, false);
//            this.bntGuardar.setEnabled(false);
//            this.bntModificar.setEnabled(false);
//            this.bntEliminar.setEnabled(false);
//            this.bntNuevo.setEnabled(true);
            //removejtable();
//            busqueda.setText("");
//            rbNombres.setSelected(true);
//            rbCodigo.setSelected(false);
//            busqueda.requestFocus();
            this.dispose();
        }
    }

    /* La funcion de este metodo es limpiar y desabilitar campos que se encuentren en un contenedor
     * ejem: los JTextFiel de un panel, se envian a la capa de negocio "Utilidades.setEditableTexto()" 
     * para que este los limpie,habilite o desabilite dichos componentes */
    public void limpiar() {
        Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
    }

//    /* Para no sobrecargar la memoria y hacer una instancia cada vez que actualizamos la JTable se hace una
//     * sola instancia y lo unico que se hace antes de actualizar la JTable es limpiar el modelo y enviarle los
//     * nuevos datos a mostrar en la JTable  */
//    public void removejtable() {
//        while (cicloescolar.getRowCount() != 0) {
//            model.removeRow(0);
//        }
//    }

    /*
     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
     *y se los envia a un metodo interno getRegistroCombo() 
     *
     */
    /* Este metodo se encarga de filtrar los datos que se deben ingresar en cada uno de los campos del formulario
     * podemos indicar que el usuario ingrese solo numeros , solo letras, numeros y letras, o cualquier caracter
     * tambien podemos validar si se aseptaran espacios en blanco en la cadena ingresada , para mas detalle visualizar
     * la clase TipoFiltro()  */
//    private void setFiltroTexto() {
//
//        TipoFiltro.setFiltraEntrada(año.getDocument(), FiltroCampos.SOLO_NUMEROS, 5, true);
//        TipoFiltro.setFiltraEntrada(busqueda.getDocument(), FiltroCampos.SOLO_NUMEROS, 5, true);
//    }

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
//    private void MostrarDatos(String Dato) {
//        //String conct = "concat(horario.codigo,' ',horario.dia,' ',DATE_FORMAT(horario.horariode,'%h:%i %p'),' ',DATE_FORMAT(horario.horarioa,'%h:%i %p'))";
//        String[] campos = {"cicloescolar.idañoescolar", "cicloescolar.descripcion", "DATE_FORMAT(cicloescolar.fechainicio,'%d-%m-%Y')", "DATE_FORMAT(cicloescolar.fechacierre,'%d-%m-%Y')", "cicloescolar.estado"};
//
//        String[] condiciones = {"cicloescolar.idañoescolar"};
//        String[] Id = {Dato};
//
//        if (this.rbCodigo.isSelected()) {
//            if (!Dato.isEmpty()) {
//                removejtable();
//                Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
//                Utilidades.esObligatorio(this.JPanelCampos, false);
//                model = peticiones.getRegistroPorPks(model, "cicloescolar", campos, condiciones, Id, "");
//            } else {
//                JOptionPane.showInternalMessageDialog(this, "Debe ingresar un codigo para la busqueda");
//            }
//        }
//        if (this.rbNombres.isSelected()) {
//            removejtable();
//            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
//            Utilidades.esObligatorio(this.JPanelCampos, false);
//            model = peticiones.getRegistroPorLike(model, "cicloescolar", campos, "cicloescolar.descripcion", Dato, "");
//        }
//        Utilidades.ajustarAnchoColumnas(cicloescolar);
//    }
//    /* Este metodo  consulta en la BD el codigo de la fila seleccionada y llena los componentes
//     * de la parte superior del formulario con los datos obtenidos en la capa de Negocio getRegistroSeleccionado().
//     * 
//     * @return 
//     */
//    private void filaseleccionada() {
//
//        int fila = cicloescolar.getSelectedRow();
//        String[] cond = {"cicloescolar.idañoescolar"};
//        String[] id = {"" + cicloescolar.getValueAt(fila, 0)};
//
//        if (cicloescolar.getValueAt(fila, 0) != null) {
//            String[] campos = {"cicloescolar.descripcion", "cicloescolar.fechainicio", "cicloescolar.fechacierre", "cicloescolar.estado"};
//            Component[] cmps = {año, fechainicio, fechafin, estado};
//            Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
//
//            peticiones.getRegistroSeleccionado(cmps, "cicloescolar", campos, cond, id, "", null);
//
//            this.bntGuardar.setEnabled(false);
//            this.bntModificar.setEnabled(true);
//            this.bntEliminar.setEnabled(true);
//            this.bntNuevo.setEnabled(false);
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
        bntNuevo = new elaprendiz.gui.button.ButtonRect();
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        JPanelCampos = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        fechainicio = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        fechafin = new com.toedter.calendar.JDateChooser();
        pnlPaginador = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Reporte de Pagos");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Pagos Diarios"); // NOI18N
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
        bntNuevo.setMnemonic(KeyEvent.VK_R);
        bntNuevo.setText("Generar Reporte");
        bntNuevo.setName("Reporte PagosDiarios"); // NOI18N
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
        pnlActionButtons.setBounds(0, 150, 590, 50);

        JPanelCampos.setBackground(java.awt.SystemColor.activeCaption);
        JPanelCampos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelCampos.setForeground(new java.awt.Color(204, 204, 204));
        JPanelCampos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JPanelCampos.setLayout(null);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Fecha Inicio:");
        JPanelCampos.add(jLabel6);
        jLabel6.setBounds(20, 30, 150, 21);

        fechainicio.setDate(Calendar.getInstance().getTime());
        fechainicio.setDateFormatString("dd/MM/yyyy");
        fechainicio.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        fechainicio.setMaxSelectableDate(new java.util.Date(3093496470100000L));
        fechainicio.setMinSelectableDate(new java.util.Date(-62135744300000L));
        fechainicio.setNextFocusableComponent(fechafin);
        fechainicio.setPreferredSize(new java.awt.Dimension(120, 22));
        JPanelCampos.add(fechainicio);
        fechainicio.setBounds(180, 30, 130, 21);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Fecha Fin:");
        JPanelCampos.add(jLabel9);
        jLabel9.setBounds(80, 70, 90, 17);

        fechafin.setDate(Calendar.getInstance().getTime());
        fechafin.setDateFormatString("dd/MM/yyyy");
        fechafin.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        fechafin.setMaxSelectableDate(new java.util.Date(3093496470100000L));
        fechafin.setMinSelectableDate(new java.util.Date(-62135744300000L));
        fechafin.setPreferredSize(new java.awt.Dimension(120, 22));
        JPanelCampos.add(fechafin);
        fechafin.setBounds(180, 70, 130, 21);

        panelImage.add(JPanelCampos);
        JPanelCampos.setBounds(0, 40, 590, 110);

        pnlPaginador.setBackground(new java.awt.Color(57, 104, 163));
        pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
        pnlPaginador.setLayout(new java.awt.GridBagLayout());

        jLabel8.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/horario.png"))); // NOI18N
        jLabel8.setText("<--Reporte de Pagos-->");
        pnlPaginador.add(jLabel8, new java.awt.GridBagConstraints());

        panelImage.add(pnlPaginador);
        pnlPaginador.setBounds(0, 0, 590, 40);

        getContentPane().add(panelImage, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleName("Profesores");

        setBounds(0, 0, 603, 231);
    }// </editor-fold>//GEN-END:initComponents

    private void bntNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntNuevoActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntNuevo.getName()) == true) {
        
        if (Utilidades.esObligatorio(this.JPanelCampos, true)) {
            JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            String fechaini = FormatoFecha.getFormato(fechainicio.getCalendar().getTime(), FormatoFecha.A_M_D);
            String fechafn = FormatoFecha.getFormato(fechafin.getCalendar().getTime(), FormatoFecha.A_M_D);
            Pagos_diarios.Pagos_diario(fechaini, fechafn);
        }} else {
           JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntNuevoActionPerformed

    private void bntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalirActionPerformed
        cerrarVentana();
    }//GEN-LAST:event_bntSalirActionPerformed

    private void bntCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntCancelarActionPerformed
        // TODO add your handling code here:
        //Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
        Utilidades.esObligatorio(this.JPanelCampos, false);
//        removejtable();
        //      this.bntGuardar.setEnabled(false);
        //     this.bntModificar.setEnabled(false);
        //     this.bntEliminar.setEnabled(false);
        //     this.bntNuevo.setEnabled(true);
        //     removejtable();
        //    busqueda.setText("");
        //   busqueda.requestFocus();

    }//GEN-LAST:event_bntCancelarActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanelCampos;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntNuevo;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private com.toedter.calendar.JDateChooser fechafin;
    private com.toedter.calendar.JDateChooser fechainicio;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador;
    // End of variables declaration//GEN-END:variables
}
