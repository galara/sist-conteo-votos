/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Capa_Presentacion;

import Capa_Datos.AccesoDatos;
import Capa_Datos.BdConexion;
import Capa_Negocio.AccesoUsuario;
import static Capa_Negocio.AddForms.adminInternalFrame;
import Capa_Negocio.FiltroCampos;
import Capa_Negocio.FormatoDecimal;
import Capa_Negocio.FormatoFecha;
import Capa_Negocio.Peticiones;
import Capa_Negocio.Renderer_CheckBox;
import Capa_Negocio.TipoFiltro;
import Capa_Negocio.Utilidades;
import static Capa_Presentacion.Principal.dp;
import Reportes.Recibodepago;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author GLARA
 */
public class AnulacionPagos extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model, model2, model3;
    DefaultComboBoxModel modelCombo;
    String[] titulos = {"Id", "Codigo", "Descripción", "Año", "Monto", "Fecha V", "Mora", "Subtotal", "Anular", "ExMora"};//Titulos para Jtabla
    String[] titulos2 = {"Id", "Código", "Descripción", "Precio", "Cantidad", "SubTotal", "Anular"};//Titulos para Jtabla
    String[] titulos3 = {"Codigo Alumno", "Nombre", "Recibo No.", "Fecha", "Total"};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();
    public static Hashtable<String, String> hashGrupo = new Hashtable<>();
    public static Hashtable<String, String> hashTipopago = new Hashtable<>();
    AccesoDatos acceso = new AccesoDatos();
    static String idalumno = "", iddetallegrupo = "";
    java.sql.Connection conn;//getConnection intentara establecer una conexión.

    /**
     * Creates new form Cliente
     */
    public AnulacionPagos() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();

        colegiaturas.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                sumartotal();
            }
        });

        otrosproductos.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                sumartotal();
            }
        });

        colegiaturas.getColumnModel().getColumn(0).setMaxWidth(0);
        colegiaturas.getColumnModel().getColumn(0).setMinWidth(0);
        colegiaturas.getColumnModel().getColumn(0).setPreferredWidth(0);
        colegiaturas.doLayout();

        colegiaturas.getColumnModel().getColumn(9).setMaxWidth(0);
        colegiaturas.getColumnModel().getColumn(9).setMinWidth(0);
        colegiaturas.getColumnModel().getColumn(9).setPreferredWidth(0);
        colegiaturas.doLayout();

        otrosproductos.getColumnModel().getColumn(0).setMaxWidth(0);
        otrosproductos.getColumnModel().getColumn(0).setMinWidth(0);
        otrosproductos.getColumnModel().getColumn(0).setPreferredWidth(0);
        otrosproductos.doLayout();

        JCheckBox check = new JCheckBox();
        colegiaturas.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(check));
        //colegiaturas.getColumnModel().getColumn(9).setCellEditor(new DefaultCellEditor(check));
        otrosproductos.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(check));
        colegiaturas.getColumnModel().getColumn(8).setCellRenderer(new Renderer_CheckBox());
        //colegiaturas.getColumnModel().getColumn(9).setCellRenderer(new Renderer_CheckBox());
        otrosproductos.getColumnModel().getColumn(6).setCellRenderer(new Renderer_CheckBox());

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
            Utilidades.setEditableTexto(this.JPanelGrupo, false, null, true, "");
            Utilidades.esObligatorio(this.JPanelGrupo, false);
            this.bntGuardar.setEnabled(false);
            removejtable();
            removejtable2();
            removejtable3();
            codigoa.setText("");
            codigoa.requestFocus();
            this.dispose();
        }
    }

    /* Para no sobrecargar la memoria y hacer una instancia cada vez que actualizamos la JTable se hace una
     * sola instancia y lo unico que se hace antes de actualizar la JTable es limpiar el modelo y enviarle los
     * nuevos datos a mostrar en la JTable  */
    public void removejtable() {
        while (colegiaturas.getRowCount() != 0) {
            model.removeRow(0);
        }
    }

    public void removejtable2() {
        while (otrosproductos.getRowCount() != 0) {
            model2.removeRow(0);
        }
    }

    public void removejtable3() {
        while (recibos.getRowCount() != 0) {
            model3.removeRow(0);
        }
    }

    private void limpiartodo() {
        removejtable();
        removejtable2();
        removejtable3();
        codigoa.setText("");
        codigoa.requestFocus();
        //nombrealumno.setText("");
        //Utilidades.esObligatorio(this.JPanelRecibo, false);
        Utilidades.esObligatorio(this.JPanelGrupo, false);
        Utilidades.esObligatorio(this.JPanelPago, false);
        codigoa.requestFocus();
    }

    public void sumartotal() {
        if (colegiaturas.getRowCount() == 0 && /*colegiaturas.getSelectedRow() == -1*/ otrosproductos.getRowCount() == 0) {
            totalapagar.setValue(0.0);
        } else {
            float Actual, Resultado = 0;

            if (colegiaturas.getRowCount() != 0) {
                for (int i = 0; i < model.getRowCount(); i++) {//sumar total tabla meses
                    if (colegiaturas.getValueAt(i, 8).toString().equals("false") /*&& colegiaturas.getValueAt(i, 9).toString().equals(true)*/) {
                        Actual = Float.parseFloat(colegiaturas.getValueAt(i, 7).toString());
                        Resultado = Resultado + Actual;
                    }
//                if (colegiaturas.getValueAt(i, 8).toString().equals("false")) {
//                    Actual = Float.parseFloat(colegiaturas.getValueAt(i, 6).toString());
//                    Resultado = Resultado + Actual;
//                }
                }// fin sumar total meses
            }

            if (otrosproductos.getRowCount() != 0) {
                for (int i = 0; i < model2.getRowCount(); i++) {//sumar total tabla otrospagos
                    if (otrosproductos.getValueAt(i, 6).toString().equals("false") /*&& colegiaturas.getValueAt(i, 9).toString().equals(true)*/) {
                        float canti = Float.parseFloat(otrosproductos.getValueAt(i, 4).toString());
                        if (canti > 0) {
                            Actual = Float.parseFloat(otrosproductos.getValueAt(i, 5).toString());
                            Resultado = Resultado + Actual;
                        }
                    }
                }// fin sumar total otrospagos
            }
            totalapagar.setValue(Math.round(Resultado * 100.0) / 100.0);

        }
    }

    /*
     * Metodo para buscar un alumno por su codigo devuelde el id
     */
    public void balumnocodigo(String codigo) {
        if (codigo.isEmpty()) {
            //nombrealumno.setText("");
            //estado.setText("");
            idalumno = "";
        } else if (!codigo.isEmpty()) {

            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            String[] campos = {"alumno.codigo", "alumno.nombres", "alumno.apellidos", "DATE_FORMAT(alumno.fechanacimiento,'%d-%m-%Y')", "alumno.estado", "alumno.idalumno"};
            String[] cond = {"alumno.codigo"};
            String[] id = {codigo};

            if (!id.equals(0)) {

                rs = ac.getRegistros("alumno", campos, cond, id, "");

                if (rs != null) {
                    try {
                        if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                            rs.beforeFirst();//regresa el puntero al primer registro
                            while (rs.next()) {//mientras tenga registros que haga lo siguiente
                                codigoa.setText(rs.getString(1));
                                //nombrealumno.setText(rs.getString(2) + " " + rs.getString(3));
                                if (rs.getString(5).equals("0")) {
                                    //  estado.setText("Inactivo");
                                    //  estado.setForeground(Color.red);
                                } else if (rs.getString(5).equals("1")) {
                                    // estado.setText("Activo");
                                    // estado.setForeground(Color.WHITE/*new java.awt.Color(102, 204, 0)*/);
                                }
                                idalumno = (rs.getString(6));
                                MostrarRecibos("alumno");
                            }
                        } else {
                            JOptionPane.showInternalMessageDialog(this, " El codigo no fue encontrado ");
                            limpiartodo();
                        }
                    } catch (SQLException e) {
                        JOptionPane.showInternalMessageDialog(this, e);
                    }
                } else {
                    JOptionPane.showInternalMessageDialog(this, " El codigo no fue encontrado ");
                    limpiartodo();
                }

            }
        }
    }

    private void reciboseleccion() {

        int fila = recibos.getSelectedRow();
        String id = "" + recibos.getValueAt(fila, 2);
        if (recibos.getValueAt(fila, 0) != null) {
            MostrarPagos(id);
            MostrarProductos(id);
            sumartotal();

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
    private void MostrarPagos(String idrecibo) {

        String sql = "SELECT proyeccionpagos.idproyeccionpagos, mes.idmes ,mes.mes,proyeccionpagos.año ,proyeccionpagos.monto , proyeccionpagos.fechavencimiento ,IFNULL((SELECT mora.mora FROM mora where proyeccionpagos.idproyeccionpagos = mora.proyeccionpagos_idproyeccionpagos and mora.exoneracion=0 ),0.0) AS 'Mora',IFNULL((SELECT mora.exoneracion FROM mora where proyeccionpagos.idproyeccionpagos = mora.proyeccionpagos_idproyeccionpagos and mora.exoneracion>0 ),0.0) AS 'ExMora' FROM proyeccionpagos INNER JOIN detrecibo ON proyeccionpagos.idproyeccionpagos = detrecibo.proyeccionpagos_idproyeccionpagos INNER JOIN mes ON proyeccionpagos.mes_idmes = mes.idmes where detrecibo.recibodepago_idrecibo=" + idrecibo;
        removejtable();
        model = getRegistroPorLikel(model, sql);
        Utilidades.ajustarAnchoColumnas(colegiaturas);

        colegiaturas.getColumnModel().getColumn(0).setMaxWidth(0);
        colegiaturas.getColumnModel().getColumn(0).setMinWidth(0);
        colegiaturas.getColumnModel().getColumn(0).setPreferredWidth(0);
        colegiaturas.doLayout();
        colegiaturas.getColumnModel().getColumn(9).setMaxWidth(0);
        colegiaturas.getColumnModel().getColumn(9).setMinWidth(0);
        colegiaturas.getColumnModel().getColumn(9).setPreferredWidth(0);
        colegiaturas.doLayout();
    }

    /**
     * Para una condicion WHERE condicionid LIKE '% campocondicion' * @param
     * modelo ,modelo de la JTable
     *
     * @param tabla , el nombre de la tabla a consultar en la BD
     * @return
     */
    public DefaultTableModel getRegistroPorLikel(DefaultTableModel modelo, String tabla) {
        try {

            ResultSet rs;

            rs = acceso.getRegistroProc(tabla);
            int cantcampos = 9;
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos + 1];

                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
                    for (int i = 0; i < cantcampos - 1; i++) {

                        fila[i] = rs.getObject(i + 1); // El primer indice en rs es el 1, no el cero, por eso se suma 1.
                        if (i == 4) {
                            float monto = (float) rs.getObject(i + 1);
                            float resultado = (float) (Math.round((monto) * 100.0) / 100.0);
                            fila[i] = resultado;
                        }
                        if (i == 6) {
                            if (fila[i] == "0.0") {
                                fila[i] = "0.0";
                            } else {
                                float mora = (float) rs.getFloat(i + 1);
                                float resultado = (float) (Math.round(mora * 100.0) / 100.0);
                                fila[i] = resultado;
                            }
                        }
                        if (fila[i] == null) {
                            fila[i] = "";
                        } else {
                        }
                    }
                    fila[7] = (float) (Math.round(((float) fila[4] + ((float) fila[6])) * 100.0) / 100.0);

                    fila[8] = false;
                    fila[9] = (float) rs.getFloat(8);
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

    private void MostrarProductos(String idrecibo) {

        String sql = "SELECT descripcionrecibo.iddescripcionrecibo, otrospagos.idpago , otrospagos.descripcion, descripcionrecibo.precio ,descripcionrecibo.cantidad FROM otrospagos INNER JOIN descripcionrecibo ON otrospagos.idpago = descripcionrecibo.pago_idpago where descripcionrecibo.recibo_idrecibo=" + idrecibo;
        removejtable2();
        model2 = getRegistroPorLikell(model2, sql);
        Utilidades.ajustarAnchoColumnas(otrosproductos);

        otrosproductos.getColumnModel().getColumn(0).setMaxWidth(0);
        otrosproductos.getColumnModel().getColumn(0).setMinWidth(0);
        otrosproductos.getColumnModel().getColumn(0).setPreferredWidth(0);
        otrosproductos.doLayout();
    }

    /**
     * Para una condicion WHERE condicionid LIKE '% campocondicion' * @param
     * modelo ,modelo de la JTable
     *
     * @param modelo
     * @param tabla , el nombre de la tabla a consultar en la BD
     * @return
     */
    public DefaultTableModel getRegistroPorLikell(DefaultTableModel modelo, String tabla) {
        try {

            ResultSet rs;

            rs = acceso.getRegistroProc(tabla);
            int cantcampos = 7;
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos];

                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    fila[0] = rs.getString(1);
                    fila[1] = rs.getString(2);
                    fila[2] = rs.getString(3);
                    fila[3] = Float.parseFloat(rs.getString(4));
                    fila[4] = rs.getString(5);
                    fila[5] = (Math.round((Float.parseFloat(rs.getString(5)) * Float.parseFloat(rs.getString(4))) * 100.0) / 100.0);
                    fila[6] = false;
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

    private void MostrarRecibos(String condicion) {
        String sql = "";

        if (condicion.equals("alumno")) {
            sql = "SELECT alumno.codigo, concat(alumno.nombres,' ',alumno.apellidos),recibodepago.idrecibo ,   DATE_FORMAT(recibodepago.fecha,'%d-%m-%Y'),recibodepago.total FROM alumno INNER JOIN recibodepago ON alumno.idalumno =recibodepago.alumno_idalumno where alumno.idalumno=" + idalumno;
        } else if (condicion.equals("fecha")) {
            String fechaini = FormatoFecha.getFormato(inicioreporte.getCalendar().getTime(), FormatoFecha.A_M_D);
            String fechafn = FormatoFecha.getFormato(fechapago1.getCalendar().getTime(), FormatoFecha.A_M_D);
            sql = "SELECT alumno.codigo, concat(alumno.nombres,' ',alumno.apellidos),recibodepago.idrecibo ,   DATE_FORMAT(recibodepago.fecha,'%d-%m-%Y'),recibodepago.total FROM alumno INNER JOIN recibodepago ON alumno.idalumno =recibodepago.alumno_idalumno where recibodepago.fecha >= '" + fechaini + "' and recibodepago.fecha <= '" + fechafn + "' ";
        } else if (condicion.equals("recibo")) {
            sql = "SELECT alumno.codigo, concat(alumno.nombres,' ',alumno.apellidos),recibodepago.idrecibo ,   DATE_FORMAT(recibodepago.fecha,'%d-%m-%Y'),recibodepago.total FROM alumno INNER JOIN recibodepago ON alumno.idalumno =recibodepago.alumno_idalumno where recibodepago.idrecibo=" + reciboa.getText();
        }
        removejtable3();
        model3 = getRegistroRecibos(model3, sql);
        Utilidades.ajustarAnchoColumnas(recibos);
    }

    /**
     * Para una condicion WHERE condicionid LIKE '% campocondicion' * @param
     * modelo ,modelo de la JTable
     *
     * @param modelo
     * @param tabla , el nombre de la tabla a consultar en la BD
     * @return
     */
    public DefaultTableModel getRegistroRecibos(DefaultTableModel modelo, String tabla) {
        try {

            ResultSet rs;

            rs = acceso.getRegistroProc(tabla);
            int cantcampos = 5;
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos];

                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    fila[0] = rs.getString(1);
                    fila[1] = rs.getString(2);
                    fila[2] = rs.getString(3);
                    fila[3] = rs.getString(4);
                    fila[4] = rs.getObject(5);
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

    /* Este metodo se encarga de filtrar los datos que se deben ingresar en cada uno de los campos del formulario
     * podemos indicar que el usuario ingrese solo numeros , solo letras, numeros y letras, o cualquier caracter
     * tambien podemos validar si se aseptaran espacios en blanco en la cadena ingresada , para mas detalle visualizar
     * la clase TipoFiltro()  */
    private void setFiltroTexto() {
        TipoFiltro.setFiltraEntrada(codigoa.getDocument(), FiltroCampos.NUM_LETRAS, 45, false);
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

        popupprofesor = new javax.swing.JPopupMenu();
        Nuevo_Profesor = new javax.swing.JMenuItem();
        Actualizar_Profesor = new javax.swing.JMenuItem();
        popupcarrera = new javax.swing.JPopupMenu();
        Nueva_Carrera = new javax.swing.JMenuItem();
        Actualizar_Carrera = new javax.swing.JMenuItem();
        popupprotipopago = new javax.swing.JPopupMenu();
        Nuevo_Tipopago = new javax.swing.JMenuItem();
        Actualizar = new javax.swing.JMenuItem();
        panelImage = new elaprendiz.gui.panel.PanelImage();
        pnlActionButtons = new javax.swing.JPanel();
        bntGuardar = new elaprendiz.gui.button.ButtonRect();
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        JPanelGrupo = new javax.swing.JPanel();
        tbPane2 = new elaprendiz.gui.panel.TabbedPaneHeader();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        recibos = new javax.swing.JTable();
        JPanelTable = new javax.swing.JPanel();
        tbPane = new elaprendiz.gui.panel.TabbedPaneHeader();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        colegiaturas = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        otrosproductos = new javax.swing.JTable();
        JPanelBusqueda = new javax.swing.JPanel();
        codigoa = new elaprendiz.gui.textField.TextField();
        jLabel16 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        inicioreporte = new com.toedter.calendar.JDateChooser();
        jLabel24 = new javax.swing.JLabel();
        fechapago1 = new com.toedter.calendar.JDateChooser();
        clockDigital2 = new elaprendiz.gui.varios.ClockDigital();
        rbFecha = new javax.swing.JRadioButton();
        rbCodigo = new javax.swing.JRadioButton();
        jLabel17 = new javax.swing.JLabel();
        reciboa = new elaprendiz.gui.textField.TextField();
        rbRecibo = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        buttonAction1 = new elaprendiz.gui.button.ButtonAction();
        jPanel1 = new javax.swing.JPanel();
        tbPane1 = new elaprendiz.gui.panel.TabbedPaneHeader();
        JPanelPago = new javax.swing.JPanel();
        totalapagar = new javax.swing.JFormattedTextField();
        jLabel27 = new javax.swing.JLabel();
        pnlPaginador1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        tbPane3 = new elaprendiz.gui.panel.TabbedPaneHeader();
        JPanelPago1 = new javax.swing.JPanel();
        buttonAction4 = new elaprendiz.gui.button.ButtonAction();

        Nuevo_Profesor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/profesor.png"))); // NOI18N
        Nuevo_Profesor.setText("Nuevo Profesor");
        Nuevo_Profesor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Nuevo_ProfesorActionPerformed(evt);
            }
        });
        popupprofesor.add(Nuevo_Profesor);

        Actualizar_Profesor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/update.png"))); // NOI18N
        Actualizar_Profesor.setText("Actualizar Combo");
        Actualizar_Profesor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Actualizar_ProfesorActionPerformed(evt);
            }
        });
        popupprofesor.add(Actualizar_Profesor);

        Nueva_Carrera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/carrera.png"))); // NOI18N
        Nueva_Carrera.setText("Nueva Carrera");
        Nueva_Carrera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Nueva_CarreraActionPerformed(evt);
            }
        });
        popupcarrera.add(Nueva_Carrera);

        Actualizar_Carrera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/update.png"))); // NOI18N
        Actualizar_Carrera.setText("Actualizar Combo");
        Actualizar_Carrera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Actualizar_CarreraActionPerformed(evt);
            }
        });
        popupcarrera.add(Actualizar_Carrera);

        Nuevo_Tipopago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/profesor.png"))); // NOI18N
        Nuevo_Tipopago.setText("Nuevo Tipo Pago");
        Nuevo_Tipopago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Nuevo_TipopagoActionPerformed(evt);
            }
        });
        popupprotipopago.add(Nuevo_Tipopago);

        Actualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/update.png"))); // NOI18N
        Actualizar.setText("Actualizar Combo");
        Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarActionPerformed(evt);
            }
        });
        popupprotipopago.add(Actualizar);

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Anulación de Pagos");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("anularpagos"); // NOI18N
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
        bntGuardar.setMnemonic(KeyEvent.VK_A);
        bntGuardar.setText("Anular");
        bntGuardar.setName("Anular Pago"); // NOI18N
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
        bntSalir.setText("Salir    ");
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
        pnlActionButtons.setBounds(0, 580, 880, 50);

        JPanelGrupo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelGrupo.setForeground(new java.awt.Color(204, 204, 204));
        JPanelGrupo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JPanelGrupo.setLayout(null);

        tbPane2.setOpaque(true);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        recibos.setForeground(new java.awt.Color(51, 51, 51));
        recibos.setModel(model3 = new DefaultTableModel(null, titulos3)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            recibos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            recibos.setFocusCycleRoot(true);
            recibos.setGridColor(new java.awt.Color(51, 51, 255));
            recibos.setRowHeight(20);
            recibos.setSelectionBackground(java.awt.SystemColor.activeCaption);
            recibos.setSurrendersFocusOnKeystroke(true);
            recibos.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    recibosMouseClicked(evt);
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    recibosMouseClicked1(evt);
                }
            });
            recibos.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    recibosKeyPressed(evt);
                }
            });
            jScrollPane5.setViewportView(recibos);

            jPanel5.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 756, 130));

            tbPane2.addTab("Listado de Pagos", jPanel5);

            JPanelGrupo.add(tbPane2);
            tbPane2.setBounds(0, 0, 758, 170);

            panelImage.add(JPanelGrupo);
            JPanelGrupo.setBounds(0, 160, 760, 170);

            JPanelTable.setOpaque(false);
            JPanelTable.setPreferredSize(new java.awt.Dimension(786, 402));
            JPanelTable.setLayout(new java.awt.BorderLayout());

            tbPane.setOpaque(true);

            jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            colegiaturas.setForeground(new java.awt.Color(51, 51, 51));
            colegiaturas.setModel(model = new DefaultTableModel(null, titulos)
                {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        if(column==8 || column==9){
                            return true;
                        }else{
                            return false;}
                    }
                });
                colegiaturas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                colegiaturas.setFocusCycleRoot(true);
                colegiaturas.setGridColor(new java.awt.Color(51, 51, 255));
                colegiaturas.setRowHeight(20);
                colegiaturas.setSelectionBackground(java.awt.SystemColor.activeCaption);
                colegiaturas.setSurrendersFocusOnKeystroke(true);
                colegiaturas.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        colegiaturasMouseClicked(evt);
                    }
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        colegiaturasMouseClicked1(evt);
                    }
                });
                colegiaturas.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        colegiaturasKeyPressed(evt);
                    }
                });
                jScrollPane4.setViewportView(colegiaturas);

                jPanel3.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 756, 210));

                tbPane.addTab("Colegiatura", jPanel3);

                jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                jScrollPane2.setOpaque(false);

                otrosproductos.setModel(model2 = new DefaultTableModel(null, titulos2)
                    {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            if(column==6){
                                return true;
                            }else{
                                return false;}
                        }
                    });
                    otrosproductos.setName("otrosproductos"); // NOI18N
                    otrosproductos.setOpaque(false);
                    otrosproductos.setRowHeight(20);
                    jScrollPane2.setViewportView(otrosproductos);

                    jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 756, 210));

                    tbPane.addTab("Otros Pagos", jPanel4);

                    JPanelTable.add(tbPane, java.awt.BorderLayout.CENTER);

                    panelImage.add(JPanelTable);
                    JPanelTable.setBounds(0, 330, 760, 250);

                    JPanelBusqueda.setBackground(java.awt.SystemColor.inactiveCaption);
                    JPanelBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                    JPanelBusqueda.setLayout(null);

                    codigoa.setEnabled(false);
                    codigoa.setPreferredSize(new java.awt.Dimension(250, 27));
                    codigoa.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            codigoaActionPerformed(evt);
                        }
                    });
                    JPanelBusqueda.add(codigoa);
                    codigoa.setBounds(280, 35, 97, 21);

                    jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
                    jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                    jLabel16.setText("Codigo Alumno: ");
                    JPanelBusqueda.add(jLabel16);
                    jLabel16.setBounds(170, 35, 110, 21);

                    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar 2.png"))); // NOI18N
                    jButton1.setEnabled(false);
                    jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton1ActionPerformed(evt);
                        }
                    });
                    JPanelBusqueda.add(jButton1);
                    jButton1.setBounds(380, 35, 20, 21);

                    jLabel23.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
                    jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                    jLabel23.setText("Fecha Inicial: ");
                    JPanelBusqueda.add(jLabel23);
                    jLabel23.setBounds(180, 6, 100, 21);

                    inicioreporte.setDate(Calendar.getInstance().getTime());
                    inicioreporte.setDateFormatString("dd/MM/yyyy");
                    inicioreporte.setEnabled(false);
                    inicioreporte.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                    inicioreporte.setMaxSelectableDate(new java.util.Date(3093496470100000L));
                    inicioreporte.setMinSelectableDate(new java.util.Date(-62135744300000L));
                    inicioreporte.setPreferredSize(new java.awt.Dimension(120, 22));
                    JPanelBusqueda.add(inicioreporte);
                    inicioreporte.setBounds(280, 6, 120, 21);

                    jLabel24.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
                    jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    jLabel24.setText("Fecha Final:");
                    JPanelBusqueda.add(jLabel24);
                    jLabel24.setBounds(430, 6, 80, 21);

                    fechapago1.setDate(Calendar.getInstance().getTime());
                    fechapago1.setDateFormatString("dd/MM/yyyy");
                    fechapago1.setEnabled(false);
                    fechapago1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                    fechapago1.setMaxSelectableDate(new java.util.Date(3093496470100000L));
                    fechapago1.setMinSelectableDate(new java.util.Date(-62135744300000L));
                    fechapago1.setPreferredSize(new java.awt.Dimension(120, 22));
                    fechapago1.addAncestorListener(new javax.swing.event.AncestorListener() {
                        public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                        }
                        public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                            fechapago1AncestorAdded(evt);
                        }
                        public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                        }
                    });
                    JPanelBusqueda.add(fechapago1);
                    fechapago1.setBounds(510, 6, 120, 21);

                    clockDigital2.setForeground(new java.awt.Color(0, 0, 0));
                    clockDigital2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
                    JPanelBusqueda.add(clockDigital2);
                    clockDigital2.setBounds(770, 0, 100, 30);

                    rbFecha.setBackground(java.awt.SystemColor.inactiveCaption);
                    rbFecha.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
                    rbFecha.setForeground(new java.awt.Color(0, 102, 102));
                    rbFecha.setText("Buscar por fecha");
                    rbFecha.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            rbFechaActionPerformed(evt);
                        }
                    });
                    JPanelBusqueda.add(rbFecha);
                    rbFecha.setBounds(10, 6, 160, 21);

                    rbCodigo.setBackground(java.awt.SystemColor.inactiveCaption);
                    rbCodigo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
                    rbCodigo.setForeground(new java.awt.Color(0, 102, 102));
                    rbCodigo.setText("Buscar por alumno");
                    rbCodigo.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            rbCodigoActionPerformed(evt);
                        }
                    });
                    JPanelBusqueda.add(rbCodigo);
                    rbCodigo.setBounds(10, 35, 160, 21);

                    jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
                    jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                    jLabel17.setText("No. Recibo: ");
                    JPanelBusqueda.add(jLabel17);
                    jLabel17.setBounds(190, 65, 90, 21);

                    reciboa.setPreferredSize(new java.awt.Dimension(250, 27));
                    reciboa.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            reciboaActionPerformed(evt);
                        }
                    });
                    JPanelBusqueda.add(reciboa);
                    reciboa.setBounds(280, 65, 97, 21);

                    rbRecibo.setBackground(java.awt.SystemColor.inactiveCaption);
                    rbRecibo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
                    rbRecibo.setForeground(new java.awt.Color(0, 102, 102));
                    rbRecibo.setSelected(true);
                    rbRecibo.setText("Buscar por No. Recibibo");
                    rbRecibo.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            rbReciboActionPerformed(evt);
                        }
                    });
                    JPanelBusqueda.add(rbRecibo);
                    rbRecibo.setBounds(10, 65, 180, 21);
                    JPanelBusqueda.add(jSeparator1);
                    jSeparator1.setBounds(10, 91, 850, 10);

                    buttonAction1.setText("Ejecutar Filtro");
                    buttonAction1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
                    buttonAction1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            buttonAction1ActionPerformed(evt);
                        }
                    });
                    JPanelBusqueda.add(buttonAction1);
                    buttonAction1.setBounds(390, 95, 100, 24);

                    panelImage.add(JPanelBusqueda);
                    JPanelBusqueda.setBounds(0, 40, 880, 120);

                    jPanel1.setBackground(new java.awt.Color(51, 51, 51));
                    jPanel1.setLayout(new java.awt.BorderLayout());

                    tbPane1.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
                    tbPane1.setOpaque(true);

                    JPanelPago.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                    JPanelPago.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                    totalapagar.setEditable(false);
                    totalapagar.setBackground(new java.awt.Color(204, 255, 102));
                    totalapagar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
                    totalapagar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                    totalapagar.setToolTipText("");
                    totalapagar.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                    totalapagar.setPreferredSize(new java.awt.Dimension(80, 23));
                    JPanelPago.add(totalapagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 60, 105, 40));

                    jLabel27.setBackground(new java.awt.Color(255, 204, 0));
                    jLabel27.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
                    jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    jLabel27.setText("Total Q.");
                    jLabel27.setOpaque(true);
                    JPanelPago.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 20, 105, 30));

                    tbPane1.addTab("============", JPanelPago);

                    jPanel1.add(tbPane1, java.awt.BorderLayout.CENTER);
                    tbPane1.getAccessibleContext().setAccessibleName("TOTAL");

                    panelImage.add(jPanel1);
                    jPanel1.setBounds(760, 330, 120, 250);

                    pnlPaginador1.setBackground(new java.awt.Color(57, 104, 163));
                    pnlPaginador1.setPreferredSize(new java.awt.Dimension(786, 40));
                    pnlPaginador1.setLayout(new java.awt.GridBagLayout());

                    jLabel11.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
                    jLabel11.setForeground(new java.awt.Color(255, 255, 255));
                    jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/money.png"))); // NOI18N
                    jLabel11.setText("<--Anulacion y Reimpresión de pagos-->");
                    pnlPaginador1.add(jLabel11, new java.awt.GridBagConstraints());

                    jPanel2.setBackground(new java.awt.Color(51, 51, 51));
                    jPanel2.setLayout(new java.awt.BorderLayout());
                    pnlPaginador1.add(jPanel2, new java.awt.GridBagConstraints());

                    jPanel6.setBackground(new java.awt.Color(51, 51, 51));
                    jPanel6.setLayout(new java.awt.BorderLayout());
                    pnlPaginador1.add(jPanel6, new java.awt.GridBagConstraints());

                    panelImage.add(pnlPaginador1);
                    pnlPaginador1.setBounds(0, 0, 880, 40);

                    tbPane3.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
                    tbPane3.setOpaque(true);

                    JPanelPago1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                    JPanelPago1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                    buttonAction4.setText("Re-Impresión");
                    buttonAction4.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
                    buttonAction4.setName("Re-Impresion Pago"); // NOI18N
                    buttonAction4.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            buttonAction4ActionPerformed(evt);
                        }
                    });
                    JPanelPago1.add(buttonAction4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, -1));

                    tbPane3.addTab("============", JPanelPago1);

                    panelImage.add(tbPane3);
                    tbPane3.setBounds(760, 160, 115, 170);

                    getContentPane().add(panelImage, java.awt.BorderLayout.CENTER);

                    getAccessibleContext().setAccessibleName("Profesores");

                    setBounds(0, 0, 890, 662);
                }// </editor-fold>//GEN-END:initComponents

    private void bntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalirActionPerformed
        cerrarVentana();
    }//GEN-LAST:event_bntSalirActionPerformed

    private void bntCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntCancelarActionPerformed
        // TODO add your handling code here:
        limpiartodo();
    }//GEN-LAST:event_bntCancelarActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing

    private void Actualizar_ProfesorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Actualizar_ProfesorActionPerformed
        // TODO add your handling code here:
        //llenarcomboprofesor();
    }//GEN-LAST:event_Actualizar_ProfesorActionPerformed

    private void Nuevo_ProfesorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Nuevo_ProfesorActionPerformed
        // TODO add your handling code here:
        Profesor frmProfesor = new Profesor();
        if (frmProfesor == null) {
            frmProfesor = new Profesor();
        }
        adminInternalFrame(dp, frmProfesor);
    }//GEN-LAST:event_Nuevo_ProfesorActionPerformed

    private void Nueva_CarreraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Nueva_CarreraActionPerformed
        // TODO add your handling code here:
        Carrera frmCarrera = new Carrera();
        if (frmCarrera == null) {
            frmCarrera = new Carrera();
        }
        adminInternalFrame(dp, frmCarrera);
    }//GEN-LAST:event_Nueva_CarreraActionPerformed

    private void Actualizar_CarreraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Actualizar_CarreraActionPerformed
        // TODO add your handling code here  
    }//GEN-LAST:event_Actualizar_CarreraActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        BuscarAlumnoAnulacionPago frmBuscarAlumnoAnulacionPago = new BuscarAlumnoAnulacionPago();
        if (frmBuscarAlumnoAnulacionPago == null) {
            frmBuscarAlumnoAnulacionPago = new BuscarAlumnoAnulacionPago();
        }
        adminInternalFrame(dp, frmBuscarAlumnoAnulacionPago);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void codigoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoaActionPerformed
        // TODO add your handling code here:
        balumnocodigo(codigoa.getText());

    }//GEN-LAST:event_codigoaActionPerformed

    private void Nuevo_TipopagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Nuevo_TipopagoActionPerformed
        // TODO add your handling code here:
        TipoPago frmTipoPago = new TipoPago();
        if (frmTipoPago == null) {
            frmTipoPago = new TipoPago();
        }
        adminInternalFrame(dp, frmTipoPago);

    }//GEN-LAST:event_Nuevo_TipopagoActionPerformed

    private void ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarActionPerformed
        // TODO add your handling code here:
        //llenarcombotipopago();
    }//GEN-LAST:event_ActualizarActionPerformed

    private void colegiaturasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_colegiaturasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_colegiaturasKeyPressed

    private void colegiaturasMouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colegiaturasMouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_colegiaturasMouseClicked1

    private void colegiaturasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colegiaturasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_colegiaturasMouseClicked

    private void bntGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardarActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntGuardar.getName()) == true) {
            if (colegiaturas.getRowCount() == 0 /*&& colegiaturas.getSelectedRow() == -1*/ && otrosproductos.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "La tabla no contiene datos que anular");

            } else { //Inicio de Guardar datos
                int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
                if (resp == 0) {

                    String printHorario = "";

                    //GUARDAR DATOS DE RECIBO***************************************
                    //**************************************************************
                    int idrecibo = 0, n = 0;
                    String sql = "";

                    int fila = recibos.getSelectedRow();
                    String idr = "" + recibos.getValueAt(fila, 2);
                    idrecibo = Integer.parseInt(idr);

                    float total = Float.parseFloat(totalapagar.getText());
                    sql = "update recibodepago set  total=" + total + " where idrecibo=" + idr;
                    PreparedStatement ps = null;
                    conn = BdConexion.getConexion();

                    try {
                        conn.setAutoCommit(false);
                        ps = conn.prepareStatement(sql);
                        n = ps.executeUpdate();
                        if (n > 0) {

                            //GUARDAR MESES PAGADOS y OTROS PRODUCTOS***************
                            //******************************************************
                            boolean camprec = false;
                            int cant = model.getRowCount();
                            int cant2 = model2.getRowCount();

                            for (int i = 0; i < cant; i++) { //for pago de meses
                                if (colegiaturas.getValueAt(i, 8).toString().equals("true")) {
                                    camprec = true;
                                    String idc = (String) "" + colegiaturas.getValueAt(i, 0);

                                    if (!colegiaturas.getValueAt(i, 6).toString().equals("0.0")) {
                                        String pmora = "update mora set  estado=false,exoneracion=0  where proyeccionpagos_idproyeccionpagos=" + idc;
                                        n = ps.executeUpdate(pmora);
                                    } else if (!colegiaturas.getValueAt(i, 9).toString().equals("0.0")) {
                                        String expmora = "update mora set  estado=false,exoneracion=0  where proyeccionpagos_idproyeccionpagos=" + idc;
                                        n = ps.executeUpdate(expmora);
                                    }
                                    String detrecibo = "delete from detrecibo where proyeccionpagos_idproyeccionpagos=" + idc;
                                    String proypago = "update proyeccionpagos set  estado=false where idproyeccionpagos=" + idc;

                                    n = ps.executeUpdate(detrecibo);
                                    n = ps.executeUpdate(proypago);
                                }
                            }//fin for pago de meses

                            for (int i = 0; i < cant2; i++) {//for pago otros productos
                                if (otrosproductos.getValueAt(i, 6).toString().equals("true")) {

                                    String idotp = (String) "" + otrosproductos.getValueAt(i, 0);
                                    String descriprecibo = "delete from descripcionrecibo where iddescripcionrecibo=" + idotp;
                                    camprec = true;
                                    n = ps.executeUpdate(descriprecibo);
                                }
                            }//fin for otros productos

                            if (!camprec) {
                                JOptionPane.showInternalMessageDialog(this, "No se ha marcado ninguna linea para Anular", "Mensage", JOptionPane.INFORMATION_MESSAGE);
                            }
                            if (n > 0) {
                                JOptionPane.showInternalMessageDialog(this, "El Pago se ha Anulado Correctamente");
                                limpiartodo();
                            }
                            //FIN GUARDAR MESES PAGADOS*************************************
                            //**************************************************************
                        }

                        conn.commit();// guarda todas las consultas si no ubo error
                        ps.close();
                        if (!conn.getAutoCommit()) {
                            conn.setAutoCommit(true);
                        }

                        Recibodepago.comprobante(idrecibo, printHorario);
                    } catch (SQLException ex) {
                        try {
                            conn.rollback();// no guarda ninguna de las consultas ya que ubo error
                            ps.close();
                            if (!conn.getAutoCommit()) {
                                conn.setAutoCommit(true);
                            }
                        } catch (SQLException ex1) {
                            Logger.getLogger(AnulacionPagos.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
            }//Fin Guardar datos
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntGuardarActionPerformed

    private void recibosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recibosMouseClicked
        // TODO add your handling code here:
        reciboseleccion();
    }//GEN-LAST:event_recibosMouseClicked

    private void recibosMouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recibosMouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_recibosMouseClicked1

    private void recibosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_recibosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_recibosKeyPressed

    private void buttonAction1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAction1ActionPerformed
        // TODO add your handling code here:

        if (rbCodigo.isSelected()) {
            if (codigoa.getText().isEmpty()) {
                JOptionPane.showInternalMessageDialog(this, "Debe Ingresar un No. de Recibo");
            } else {
                balumnocodigo(codigoa.getText());
            }
        } else if (rbFecha.isSelected()) {
            if (Utilidades.esObligatorio(this.JPanelBusqueda, true)) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                MostrarRecibos("fecha");
            }
        } else if (rbRecibo.isSelected()) {
            if (reciboa.getText().isEmpty()) {
                JOptionPane.showInternalMessageDialog(this, "Debe Ingresar un No. de Recibo");
            } else {
                MostrarRecibos("recibo");
            }
        }
    }//GEN-LAST:event_buttonAction1ActionPerformed

    private void rbFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbFechaActionPerformed
        // TODO add your handling code here:
        rbCodigo.setSelected(false);
        rbRecibo.setSelected(false);

        codigoa.setEnabled(false);
        codigoa.setEditable(false);
        reciboa.setEnabled(false);
        reciboa.setEditable(false);
        jButton1.setEnabled(false);

        inicioreporte.setEnabled(true);
        fechapago1.setEnabled(true);

        codigoa.setText("");
        reciboa.setText("");
        limpiartodo();

    }//GEN-LAST:event_rbFechaActionPerformed

    private void rbCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCodigoActionPerformed
        // TODO add your handling code here:
        rbFecha.setSelected(false);
        rbRecibo.setSelected(false);
        codigoa.setEnabled(false);
        codigoa.setEditable(false);
        reciboa.setEnabled(false);

        inicioreporte.setEnabled(false);
        fechapago1.setEnabled(false);

        codigoa.setEditable(true);
        codigoa.setEnabled(true);
        jButton1.setEnabled(true);
        reciboa.setText("");
        //buttonAction1.setEnabled(false);
        //cProfesor.setSelectedIndex(-1);
        //cGrupo.removeAllItems();
        limpiartodo();
    }//GEN-LAST:event_rbCodigoActionPerformed

    private void rbReciboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbReciboActionPerformed
        // TODO add your handling code here:
        rbFecha.setSelected(false);
        rbCodigo.setSelected(false);
        codigoa.setEditable(false);
        codigoa.setEnabled(false);
        jButton1.setEnabled(false);
        inicioreporte.setEnabled(false);
        fechapago1.setEnabled(false);

        reciboa.setEnabled(true);
        reciboa.setEditable(true);
        codigoa.setText("");
        //buttonAction1.setEnabled(false);
        //cProfesor.setSelectedIndex(-1);
        //cGrupo.removeAllItems();
        limpiartodo();
    }//GEN-LAST:event_rbReciboActionPerformed

    private void reciboaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reciboaActionPerformed
        // TODO add your handling code here:
        if (rbRecibo.isSelected()) {
            if (reciboa.getText().isEmpty()) {
                JOptionPane.showInternalMessageDialog(this, "Debe Ingresar un No. de Recibo");
            } else {
                MostrarRecibos("recibo");
            }
        }
    }//GEN-LAST:event_reciboaActionPerformed

    private void fechapago1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_fechapago1AncestorAdded
        // TODO add your handling code here:
        if (rbFecha.isSelected()) {
            if (Utilidades.esObligatorio(this.JPanelBusqueda, true)) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                MostrarRecibos("fecha");
            }
        }
    }//GEN-LAST:event_fechapago1AncestorAdded

    private void buttonAction4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAction4ActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(buttonAction4.getName()) == true) {
            if (recibos.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "La tabla no contiene datos que Reimprimir");
            } else {
                if (recibos.getSelectedRow() != -1) {
                    int fila = recibos.getSelectedRow();
                    System.out.print(fila);
                    String idr = "" + recibos.getValueAt(fila, 2);
                    int idrecibo = Integer.parseInt(idr);
                    String printHorario = "";
                    Recibodepago.comprobante(idrecibo, printHorario);
                } else {
                    JOptionPane.showInternalMessageDialog(this, "No hay ningun recibo seleccionado para Reimprimir");
                }
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_buttonAction4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Actualizar;
    private javax.swing.JMenuItem Actualizar_Carrera;
    private javax.swing.JMenuItem Actualizar_Profesor;
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelGrupo;
    private javax.swing.JPanel JPanelPago;
    private javax.swing.JPanel JPanelPago1;
    private javax.swing.JPanel JPanelTable;
    private javax.swing.JMenuItem Nueva_Carrera;
    private javax.swing.JMenuItem Nuevo_Profesor;
    private javax.swing.JMenuItem Nuevo_Tipopago;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.button.ButtonAction buttonAction1;
    private elaprendiz.gui.button.ButtonAction buttonAction4;
    private elaprendiz.gui.varios.ClockDigital clockDigital2;
    public static elaprendiz.gui.textField.TextField codigoa;
    private javax.swing.JTable colegiaturas;
    public static com.toedter.calendar.JDateChooser fechapago1;
    public static com.toedter.calendar.JDateChooser inicioreporte;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable otrosproductos;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador1;
    private javax.swing.JPopupMenu popupcarrera;
    private javax.swing.JPopupMenu popupprofesor;
    private javax.swing.JPopupMenu popupprotipopago;
    private javax.swing.JRadioButton rbCodigo;
    private javax.swing.JRadioButton rbFecha;
    private javax.swing.JRadioButton rbRecibo;
    public static elaprendiz.gui.textField.TextField reciboa;
    private javax.swing.JTable recibos;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane1;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane2;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane3;
    private javax.swing.JFormattedTextField totalapagar;
    // End of variables declaration//GEN-END:variables
}
