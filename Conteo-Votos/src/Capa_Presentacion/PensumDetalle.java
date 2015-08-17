/* To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Capa_Presentacion;

import Capa_Datos.AccesoDatos;
import Capa_Negocio.AccesoUsuario;
import Capa_Negocio.AddForms;
import Capa_Negocio.Editor_CheckBox;
import Capa_Negocio.FormatoFecha;
import Capa_Negocio.Peticiones;
import Capa_Negocio.Renderer_CheckBox;
import Capa_Negocio.Utilidades;
import static Capa_Presentacion.Principal.dp;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import modelos.mPensum;

/**
 *
 * @author GLARA
 */
public class PensumDetalle extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model, model2;
    DefaultComboBoxModel modelCombo;
    String[] titulos = {"Id", "Nombre Curso", "Check"};//Titulos para Jtabla
    String[] titulos2 = {"Id", "Nombre Curso"};//Titulos para Jtabla
    public Hashtable<String, String> hashPensum = new Hashtable<>();
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();
    AccesoDatos acceso = new AccesoDatos();
    /*Se hace una instancia de la clase que recibira las peticiones de mensages de la capa de aplicación*/

    /**
     * Creates new form Cliente
     */
    public PensumDetalle() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();
        llenarcombopensum();

        Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");

        cursos.getColumnModel().getColumn(2).setCellEditor(new Editor_CheckBox());
        //para pintar la columna con el CheckBox en la tabla, en este caso, la primera columna
        cursos.getColumnModel().getColumn(2).setCellRenderer(new Renderer_CheckBox());

        pensum.addItemListener(
                (ItemEvent e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        //profesor();
                        filaseleccionada();
                    }
                });

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
            removejtable();
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
        while (cursos.getRowCount() != 0) {
            model.removeRow(0);
        }
    }

    public void removejtable2() {
        while (detallepensum.getRowCount() != 0) {
            model2.removeRow(0);
        }
    }

    /*
     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
     *y se los envia a un metodo interno getRegistroCombo() 
     *
     */
    /* Este metodo se encarga de filtrar los datos que se deben ingresar en cada uno de los campos del formulario
     * podemos indicar que el usuario ingrese solo numeros , solo letras, numeros y letras, o cualquier caracter
     * tambien podemos validar si se aseptaran espacios en blanco en la cadena ingresada , para mas detalle visualizar
     * la clase TipoFiltro()  */
    private void setFiltroTexto() {

        //TipoFiltro.setFiltraEntrada(año.getDocument(), FiltroCampos.SOLO_NUMEROS, 5, true);
        //TipoFiltro.setFiltraEntrada(busqueda.getDocument(), FiltroCampos.SOLO_NUMEROS, 5, true);
    }

    public void llenarcombopensum() {
        String Dato = "1";
        String[] campos = {"pensum.descripcion", "carrera.descripcion", "pensum.idpensum"};
        String[] condiciones = {"pensum.estado"};
        String[] Id = {Dato};
        String inner = " INNER JOIN carrera on pensum.carrera_idcarrera=carrera.idcarrera";

        pensum.removeAllItems();
        Component cmps = pensum;
        getRegistroCombopensum("pensum", campos, condiciones, Id, inner);

    }

    /*El metodo llenarcombo() envia los parametros para la consulta a la BD y el medoto
     *getRegistroCombo() se encarga de enviarlos a la capa de AccesoDatos.getRegistros()
     *quiern devolcera un ResultSet para luego obtener los valores y agregarlos al JConboBox
     *y a una Hashtable que nos servira para obtener el id y seleccionar valores.
     */
    public void getRegistroCombopensum(String tabla, String[] campos, String[] campocondicion, String[] condicionid, String inner) {
        try {
            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getRegistros(tabla, campos, campocondicion, condicionid, inner);

            int cantcampos = campos.length;
            if (rs != null) {

                DefaultComboBoxModel modeloComboBox;
                modeloComboBox = new DefaultComboBoxModel();
                pensum.setModel(modeloComboBox);

                modeloComboBox.addElement(new mPensum("", "0"));
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    Object[] fila = new Object[cantcampos];
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        count++;
                        modeloComboBox.addElement(new mPensum(rs.getString(1) + " - " + rs.getString(2), "" + rs.getInt(3)));
                        hashPensum.put(rs.getString(1), "" + count);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            //rs.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
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
    private void MostrarDatosDetalle(String Dato) {
        //String conct = "concat(horario.codigo,' ',horario.dia,' ',DATE_FORMAT(horario.horariode,'%h:%i %p'),' ',DATE_FORMAT(horario.horarioa,'%h:%i %p'))";
        String[] campos = {"detallepensun.iddetallepensun", "curso.nombrecurso"};
        String[] condiciones = {"detallepensun.pensun_idpensun"};
        String[] Id = {Dato};
        String inner = " INNER JOIN curso on detallepensun.curso_idcurso=curso.idcurso";

        removejtable2();
        model2 = getRegistroPorLike(model2, "detallepensun", campos, "detallepensun.pensun_idpensun", Dato, inner);
        Utilidades.ajustarAnchoColumnas(detallepensum);
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
        String sql = "select curso.idcurso , curso.nombrecurso from curso where curso.idcurso not in (select detallepensun.curso_idcurso from detallepensun where detallepensun.pensun_idpensun='" + Dato + "')";

        removejtable();
        model = getRegistroPorLikel(model, sql);
        Utilidades.ajustarAnchoColumnas(cursos);
    }

    /**
     * Para una condicion WHERE condicionid LIKE '% campocondicion' * @param
     * modelo ,modelo de la JTable
     *
     * @param tabla , el nombre de la tabla a consultar en la BD
     * @param campos , los campos de la tabla a consultar ejem: nombre, codigo
     * ,dirección etc
     * @param campocondicion , los campos de la tabla para las condiciones ejem:
     * id,estado etc
     * @param condicionid , los valores que se compararan con campocondicion
     * ejem: campocondicion = condicionid
     * @return
     */
    public DefaultTableModel getRegistroPorLikel(DefaultTableModel modelo, String tabla) {
        try {
            ResultSet rs;

            rs = acceso.getRegistroProc(tabla);
            int cantcampos = 2;
            //if (rs != null) {
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                //int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos + 1];

                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
                    for (int i = 0; i < cantcampos; i++) {

                        fila[i] = rs.getObject(i + 1); // El primer indice en rs es el 1, no el cero, por eso se suma 1.
                        if (fila[i] == null) {
                            fila[i] = "";
                        } else {
                        }
                    }
                    fila[2] = false;
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
     * Para una condicion WHERE condicionid LIKE '% campocondicion' * @param
     * modelo ,modelo de la JTable
     *
     * @param tabla , el nombre de la tabla a consultar en la BD
     * @param campos , los campos de la tabla a consultar ejem: nombre, codigo
     * ,dirección etc
     * @param campocondicion , los campos de la tabla para las condiciones ejem:
     * id,estado etc
     * @param condicionid , los valores que se compararan con campocondicion
     * ejem: campocondicion = condicionid
     * @return
     */
    public DefaultTableModel getRegistroPorLike(DefaultTableModel modelo, String tabla, String[] campos, String campocondicion, String condicionid, String inner) {
        try {
            ResultSet rs;
            rs = acceso.selectPorLike(tabla, campos, campocondicion, condicionid, inner);
            int cantcampos = campos.length;
            //if (rs != null) {
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                //int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos];

                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
                    for (int i = 0; i < cantcampos; i++) {

                        fila[i] = rs.getObject(i + 1); // El primer indice en rs es el 1, no el cero, por eso se suma 1.
                        if (fila[i] == null) {
                            fila[i] = "";
                        } else {
                            if (fila[i].equals(true)) {
                                fila[i] = "Activo";
                            }
                            if (fila[i].equals(false)) {
                                fila[i] = "Inactivo";
                            }
                            if (campos[i].equals("horario.horariode") || campos[i].equals("horario.horarioa") || campos[i].equals("horariode") || campos[i].equals("horarioa")) {
                                fila[i] = FormatoFecha.getTimedoce(rs.getTime(i + 1));
                            }
                        }
                    }
                    modelo.addRow(fila);
                }

            } //} 
            else {
                //JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Mensage", JOptionPane.INFORMATION_MESSAGE);
            }
            rs.close();
            return modelo;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /* Este metodo  consulta en la BD el codigo de la fila seleccionada y llena los componentes
     * de la parte superior del formulario con los datos obtenidos en la capa de Negocio getRegistroSeleccionado().
     * 
     * @return 
     */
    private void filaseleccionada() {

        if (pensum.getSelectedIndex() == 0) {
            removejtable();
            removejtable2();
        } else {
            mPensum pem = (mPensum) pensum.getSelectedItem();
            String idpensum = pem.getID();

            MostrarDatos(idpensum);
            MostrarDatosDetalle(idpensum);

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

        PdetallePensum = new javax.swing.JPopupMenu();
        Eliminar = new javax.swing.JMenuItem();
        Ppensum = new javax.swing.JPopupMenu();
        Nuevo_Pensum = new javax.swing.JMenuItem();
        Actualizar_combo = new javax.swing.JMenuItem();
        panelImage = new elaprendiz.gui.panel.PanelImage();
        pnlActionButtons = new javax.swing.JPanel();
        bntGuardar = new elaprendiz.gui.button.ButtonRect();
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        JPanelCampos = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        pensum = new javax.swing.JComboBox();
        buttonAction1 = new elaprendiz.gui.button.ButtonAction();
        JPanelTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        detallepensum = new javax.swing.JTable();
        JPanelBusqueda = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnlPaginador = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        JPanelTable1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        cursos = new javax.swing.JTable();

        Eliminar.setText("Eliminar Curso");
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });
        PdetallePensum.add(Eliminar);

        Nuevo_Pensum.setText("Nuevo Pensum");
        Nuevo_Pensum.setName("Pensum Principal"); // NOI18N
        Nuevo_Pensum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Nuevo_PensumActionPerformed(evt);
            }
        });
        Ppensum.add(Nuevo_Pensum);

        Actualizar_combo.setText("Actualizar");
        Actualizar_combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Actualizar_comboActionPerformed(evt);
            }
        });
        Ppensum.add(Actualizar_combo);

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Pensum Detalle");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("ciclo"); // NOI18N
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

        bntGuardar.setBackground(new java.awt.Color(51, 153, 255));
        bntGuardar.setMnemonic(KeyEvent.VK_G);
        bntGuardar.setText("Guardar");
        bntGuardar.setName("Guardar PensumDetalle"); // NOI18N
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

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar.png"))); // NOI18N
        jLabel5.setText("Pensum");
        JPanelCampos.add(jLabel5);
        jLabel5.setBounds(60, 65, 100, 30);

        pensum.setModel(modelCombo = new DefaultComboBoxModel());
        pensum.setComponentPopupMenu(Ppensum);
        pensum.setEnabled(false);
        pensum.setName("Pensum"); // NOI18N
        JPanelCampos.add(pensum);
        pensum.setBounds(170, 70, 310, 21);

        buttonAction1.setText("Nuevo Pensum");
        buttonAction1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        buttonAction1.setName("Pensum Principal"); // NOI18N
        buttonAction1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAction1ActionPerformed(evt);
            }
        });
        JPanelCampos.add(buttonAction1);
        buttonAction1.setBounds(490, 65, 110, 30);

        panelImage.add(JPanelCampos);
        JPanelCampos.setBounds(0, 40, 880, 190);

        JPanelTable.setOpaque(false);
        JPanelTable.setPreferredSize(new java.awt.Dimension(786, 402));
        JPanelTable.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        detallepensum.setForeground(new java.awt.Color(51, 51, 51));
        detallepensum.setModel(model2 = new DefaultTableModel(null, titulos2)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            detallepensum.setComponentPopupMenu(PdetallePensum);
            detallepensum.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            detallepensum.setFocusCycleRoot(true);
            detallepensum.setGridColor(new java.awt.Color(51, 51, 255));
            detallepensum.setRowHeight(22);
            detallepensum.setSelectionBackground(java.awt.SystemColor.activeCaption);
            detallepensum.setSurrendersFocusOnKeystroke(true);
            jScrollPane1.setViewportView(detallepensum);
            detallepensum.getAccessibleContext().setAccessibleName("");

            JPanelTable.add(jScrollPane1, java.awt.BorderLayout.CENTER);

            panelImage.add(JPanelTable);
            JPanelTable.setBounds(0, 270, 440, 160);

            JPanelBusqueda.setBackground(java.awt.SystemColor.inactiveCaption);
            JPanelBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            JPanelBusqueda.setLayout(null);

            jLabel1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
            jLabel1.setForeground(new java.awt.Color(0, 102, 102));
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel1.setText("Marque un Check a los cursos que desee agregar al pensum");
            JPanelBusqueda.add(jLabel1);
            jLabel1.setBounds(440, 10, 430, 14);

            jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
            jLabel2.setForeground(new java.awt.Color(0, 102, 102));
            jLabel2.setText("Detalle de cursos del Pensum");
            JPanelBusqueda.add(jLabel2);
            jLabel2.setBounds(10, 10, 260, 17);

            panelImage.add(JPanelBusqueda);
            JPanelBusqueda.setBounds(0, 230, 880, 40);

            pnlPaginador.setBackground(new java.awt.Color(57, 104, 163));
            pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
            pnlPaginador.setLayout(new java.awt.GridBagLayout());

            jLabel8.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
            jLabel8.setForeground(new java.awt.Color(255, 255, 255));
            jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/horario.png"))); // NOI18N
            jLabel8.setText("<--Pensum-->");
            pnlPaginador.add(jLabel8, new java.awt.GridBagConstraints());

            panelImage.add(pnlPaginador);
            pnlPaginador.setBounds(0, 0, 880, 40);

            JPanelTable1.setOpaque(false);
            JPanelTable1.setPreferredSize(new java.awt.Dimension(786, 402));
            JPanelTable1.setLayout(new java.awt.BorderLayout());

            jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            cursos.setForeground(new java.awt.Color(51, 51, 51));
            cursos.setModel(model = new DefaultTableModel(null, titulos)
                {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        if(column==2){
                            return true;
                        }else{
                            return false;}
                    }
                });
                cursos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                cursos.setFocusCycleRoot(true);
                cursos.setGridColor(new java.awt.Color(51, 51, 255));
                cursos.setRowHeight(22);
                cursos.setSelectionBackground(java.awt.SystemColor.activeCaption);
                cursos.setSurrendersFocusOnKeystroke(true);
                cursos.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        cursosMouseClicked(evt);
                    }
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        cursosMouseClicked1(evt);
                    }
                });
                cursos.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        cursosKeyPressed(evt);
                    }
                });
                jScrollPane2.setViewportView(cursos);

                JPanelTable1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

                panelImage.add(JPanelTable1);
                JPanelTable1.setBounds(440, 270, 440, 160);

                getContentPane().add(panelImage, java.awt.BorderLayout.CENTER);

                getAccessibleContext().setAccessibleName("Profesores");

                setBounds(0, 0, 890, 512);
            }// </editor-fold>//GEN-END:initComponents

    private void bntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalirActionPerformed
        cerrarVentana();
    }//GEN-LAST:event_bntSalirActionPerformed

    private void bntCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntCancelarActionPerformed
        removejtable();
        removejtable2();
        pensum.setSelectedIndex(-1);
    }//GEN-LAST:event_bntCancelarActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing

    private void cursosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cursosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cursosMouseClicked

    private void cursosMouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cursosMouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_cursosMouseClicked1

    private void cursosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cursosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cursosKeyPressed

    private void bntGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardarActionPerformed
        if (AccesoUsuario.AccesosUsuario(bntGuardar.getName()) == true) {

            if (pensum.getSelectedIndex() > 0) {

                int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
                if (resp == 0) {

                    boolean seguardo = false;
                    String nombreTabla = "detallepensun";
                    String campos = "pensun_idpensun, curso_idcurso";
                    mPensum pem = (mPensum) pensum.getSelectedItem();
                    String idpensum = pem.getID();

                    Object[] fila = new Object[2];
                    boolean camprec = false;
                    int cant = model.getRowCount();

                    try {
                        cursos.getCellEditor().stopCellEditing();
                    } catch (Exception e) {
                    }

                    for (int i = 0; i < cant; i++) {
                        if (model.getValueAt(i, 2).toString().equals("true")) {
                            camprec = true;
                            fila[0] = idpensum;
                            fila[1] = model.getValueAt(i, 0).toString();
                            seguardo = peticiones.guardarRegistros(nombreTabla, campos, fila);
                        }
                    }
                    if (!camprec) {
                        JOptionPane.showInternalMessageDialog(this, "No se ha marcado ningun curso", "Mensage", JOptionPane.INFORMATION_MESSAGE);
                    }
                    if (seguardo) {
                        MostrarDatos(idpensum);
                        MostrarDatosDetalle(idpensum);
                        JOptionPane.showInternalMessageDialog(this, "El dato se ha Guardado Correctamente", "Guardar", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showInternalMessageDialog(this, "No se ha seleccionado ningun Pensum", "Mensage", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntGuardarActionPerformed

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed

        int fila = detallepensum.getSelectedRow();
        if (fila >= 0) {
            int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Eliminar el Registro?", "Pregunta", 0);
            if (resp == 0) {

                String id = (String) "" + detallepensum.getValueAt(fila, 0);

                String nombreTabla = "detallepensun";
                String nomColumnaId = "iddetallepensun";
                int seguardo = 0;
                mPensum pem = (mPensum) pensum.getSelectedItem();
                String idpensum = pem.getID();

                seguardo = peticiones.eliminarRegistro(nombreTabla, "", nomColumnaId, id);

                if (seguardo == 1) {
                    MostrarDatos(idpensum);
                    MostrarDatosDetalle(idpensum);
                    JOptionPane.showInternalMessageDialog(this, "El dato se ha Eliminado Correctamente", "Eliminar", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No ha seleccionado ninguna fila", "Eliminar", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_EliminarActionPerformed

    private void Nuevo_PensumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Nuevo_PensumActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(Nuevo_Pensum.getName()) == true) {
            Pensum frmPensum = new Pensum();
            if (frmPensum == null) {
                frmPensum = new Pensum();
            }
            AddForms.adminInternalFrame(dp, frmPensum);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_Nuevo_PensumActionPerformed

    private void Actualizar_comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Actualizar_comboActionPerformed
        // TODO add your handling code here:
        llenarcombopensum();
    }//GEN-LAST:event_Actualizar_comboActionPerformed

    private void buttonAction1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAction1ActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(buttonAction1.getName()) == true) {
            Pensum frmPensum = new Pensum();
            if (frmPensum == null) {
                frmPensum = new Pensum();
            }
            AddForms.adminInternalFrame(dp, frmPensum);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_buttonAction1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Actualizar_combo;
    private javax.swing.JMenuItem Eliminar;
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelCampos;
    private javax.swing.JPanel JPanelTable;
    private javax.swing.JPanel JPanelTable1;
    private javax.swing.JMenuItem Nuevo_Pensum;
    private javax.swing.JPopupMenu PdetallePensum;
    private javax.swing.JPopupMenu Ppensum;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.button.ButtonAction buttonAction1;
    private javax.swing.JTable cursos;
    private javax.swing.JTable detallepensum;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JComboBox pensum;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador;
    // End of variables declaration//GEN-END:variables
}
