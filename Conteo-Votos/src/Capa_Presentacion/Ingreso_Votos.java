/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Capa_Presentacion;

import Capa_Datos.AccesoDatos;
import Capa_Datos.BdConexion;
import Capa_Negocio.AccesoUsuario;
import static Capa_Negocio.AddForms.adminInternalFrame;
import Capa_Negocio.CellEditorSpinnerPago;
import Capa_Negocio.FormatoDecimal;
import Capa_Negocio.FormatoFecha;
import Capa_Negocio.Peticiones;
import Capa_Negocio.Renderer_CheckBox;
import Capa_Negocio.TableCellFormatter;
import Capa_Negocio.Utilidades;
import static Capa_Presentacion.Principal.dp;
import Reportes.EstadoCuenta;
import Reportes.Recibodepago;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
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
import modelos.mGrupo;
import modelos.mTipopago;

/**
 *
 * @author GLARA
 */
public class Ingreso_Votos extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model, model2;
    DefaultComboBoxModel modelCombo;
    String[] titulos = {"Id", "Codigo", "Descripción", "Año", "Monto", "Fecha V", "Mora", "Subtotal", "Pagar Mora", "Pagar Mes"};//Titulos para Jtabla
    String[] titulos2 = {"Código", "Descripción", "Precio", "Cantidad", "SubTotal", "Agregar"};//Titulos para Jtabla
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
    public Ingreso_Votos() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();
        llenarcombotipopago();

        cGrupo.addItemListener(
                (ItemEvent e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        selecciongrupo();
                    }
                });

        colegiaturas.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                sumartotal();
                //formatotabla();
            }
        });

        otrosproductos.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                sumartotal();
                //formatotabla();
            }
        });
        //colegiaturas.getColumnModel().getColumn(8).setCellEditor(new Editor_CheckBox());
        //colegiaturas.getColumnModel().getColumn(0).setPreferredWidth(0);
        colegiaturas.getColumnModel().getColumn(0).setMaxWidth(0);
        colegiaturas.getColumnModel().getColumn(0).setMinWidth(0);
        colegiaturas.getColumnModel().getColumn(0).setPreferredWidth(0);
        colegiaturas.doLayout();
        JCheckBox check = new JCheckBox();
        colegiaturas.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(check));
        colegiaturas.getColumnModel().getColumn(9).setCellEditor(new DefaultCellEditor(check));
        otrosproductos.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(check));
        //colegiaturas.getColumnModel().getColumn(8).setCellEditor(new Editor_CheckBox()); *
        //colegiaturas.getColumnModel().getColumn(9).setCellEditor(new Editor_CheckBox()); *

        //para pintar la columna con el CheckBox en la tabla, en este caso, la primera columna
        colegiaturas.getColumnModel().getColumn(8).setCellRenderer(new Renderer_CheckBox());
        colegiaturas.getColumnModel().getColumn(9).setCellRenderer(new Renderer_CheckBox());
        otrosproductos.getColumnModel().getColumn(5).setCellRenderer(new Renderer_CheckBox());

        CellEditorSpinnerPago cnt = new CellEditorSpinnerPago(1);
        otrosproductos.getColumnModel().getColumn(3).setCellEditor(cnt);
        otrosproductos.getColumnModel().getColumn(3).setCellRenderer(new TableCellFormatter(null));

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
            Utilidades.setEditableTexto(this.JPanelGrupo, false, null, true, "");
            Utilidades.esObligatorio(this.JPanelGrupo, false);
            this.bntGuardar.setEnabled(false);
            removejtable();
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

    private void limpiartodo() {
        removejtable();
        llenarcombotipopago();
        codigoa.setText("");
        codigoa.requestFocus();
        profesor.setText("");
        carrera.setText("");
        horade.setText("");
        horaa.setText("");
        fechainicio.setText("");
        fechafin.setText("");
        inscripcion.setValue(null);
        colegiatura.setValue(null);
        nombrealumno.setText("");
        beca.setText("");
        inicioalumno.setText("");
        dia.setText("");
        cGrupo.setSelectedIndex(-1);
        Utilidades.esObligatorio(this.JPanelRecibo, false);
        Utilidades.esObligatorio(this.JPanelGrupo, false);
        Utilidades.esObligatorio(this.JPanelPago, false);
        codigoa.requestFocus();
    }

    public void sumartotal() {
        if (colegiaturas.getRowCount() == 0 && /*colegiaturas.getSelectedRow() == -1*/ otrosproductos.getRowCount() == 0) {
            totalapagar.setValue(0.0);
        } else {
            float Actual, Resultado = 0;
            for (int i = 0; i < model.getRowCount(); i++) {//sumar total tabla meses
                if (colegiaturas.getValueAt(i, 9).toString().equals("true") /*&& colegiaturas.getValueAt(i, 9).toString().equals(true)*/) {
                    if (colegiaturas.getValueAt(i, 8).toString().equals("true")) {
                        Actual = Float.parseFloat(colegiaturas.getValueAt(i, 7).toString());
                        Resultado = Resultado + Actual;
                    } else if (colegiaturas.getValueAt(i, 8).toString().equals("false")) {
                        Actual = Float.parseFloat(colegiaturas.getValueAt(i, 4).toString());
                        Resultado = Resultado + Actual;
                    }
                }
                //totalapagar.setValue(Math.round(Resultado * 100.0) / 100.0);
            }// fin sumar total meses

            for (int i = 0; i < model2.getRowCount(); i++) {//sumar total tabla otrospagos
                if (otrosproductos.getValueAt(i, 5).toString().equals("true") /*&& colegiaturas.getValueAt(i, 9).toString().equals(true)*/) {
                    float canti = Float.parseFloat(otrosproductos.getValueAt(i, 3).toString());
                    if (canti > 0) {
                        Actual = Float.parseFloat(otrosproductos.getValueAt(i, 4).toString());
                        Resultado = Resultado + Actual;
                    }
                }
                //totalapagar.setValue(Math.round(Resultado * 100.0) / 100.0);
            }// fin sumar total otrospagos

            totalapagar.setValue(Math.round(Resultado * 100.0) / 100.0);

        }
    }

    /*
     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
     *y se los envia a un metodo interno getRegistroCombo() 
     *
     */
    public static void llenarcombogrupo(String idalumn) {
        String Dato = "1";
        String[] campos = {"grupo.codigo", "grupo.descripcion", "grupo.idgrupo"};
        String[] condiciones = {"grupo.estado", "alumno.codigo"};
        String[] Id = {Dato, idalumn};
        cGrupo.removeAllItems();
        String inner = " INNER JOIN alumnosengrupo ON grupo.idgrupo = alumnosengrupo.grupo_idgrupo INNER JOIN alumno ON alumnosengrupo.alumno_idalumno = alumno.idalumno ";
        getRegistroCombo("grupo", campos, condiciones, Id, inner);

    }

    /*El metodo llenarcombo() envia los parametros para la consulta a la BD y el medoto
     *getRegistroCombo() se encarga de enviarlos a la capa de AccesoDatos.getRegistros()
     *quiern devolcera un ResultSet para luego obtener los valores y agregarlos al JConboBox
     *y a una Hashtable que nos servira para obtener el id y seleccionar valores.
     */
    public static void getRegistroCombo(String tabla, String[] campos, String[] campocondicion, String[] condicionid, String inner) {
        try {
            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getRegistros(tabla, campos, campocondicion, condicionid, inner);

            int cantcampos = campos.length;
            if (rs != null) {

                DefaultComboBoxModel modeloComboBox;
                modeloComboBox = new DefaultComboBoxModel();
                cGrupo.setModel(modeloComboBox);

                modeloComboBox.addElement(new mGrupo("", "0"));
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        count++;
                        modeloComboBox.addElement(new mGrupo(rs.getString(1) + " " + rs.getString(2), "" + rs.getInt(3)));
                        hashGrupo.put(rs.getString(1) + " " + rs.getString(2), "" + count);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
     *y se los envia a un metodo interno getRegistroCombo() 
     *
     */
    public void llenarcombotipopago() {
        String Dato = "1";
        String[] campos = {"tipopago", "idtipopago"};
        String[] condiciones = {"estado"};
        String[] Id = {Dato};
        cTipopago.removeAllItems();
        getRegistroCombotipopago("tipopago", campos, condiciones, Id);

    }

    /*El metodo llenarcombo() envia los parametros para la consulta a la BD y el medoto
     *getRegistroCombo() se encarga de enviarlos a la capa de AccesoDatos.getRegistros()
     *quiern devolcera un ResultSet para luego obtener los valores y agregarlos al JConboBox
     *y a una Hashtable que nos servira para obtener el id y seleccionar valores.
     */
    public void getRegistroCombotipopago(String tabla, String[] campos, String[] campocondicion, String[] condicionid) {
        try {
            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getRegistros(tabla, campos, campocondicion, condicionid, "");
            int cantcampos = campos.length;
            if (rs != null) {

                DefaultComboBoxModel modeloComboBox;
                modeloComboBox = new DefaultComboBoxModel();
                cTipopago.setModel(modeloComboBox);

                modeloComboBox.addElement(new mTipopago("", "0"));
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    Object[] fila = new Object[cantcampos];
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        count++;
                        modeloComboBox.addElement(new mTipopago(rs.getString(1), "" + rs.getInt(2)));
                        hashTipopago.put(rs.getString(1), "" + count);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* Metodo que llena los campos con la información de grupo
     * Tambien llena en la pestaña de coelgiatura lo que el alumno tiene pendiente de pago
     */
    public void selecciongrupo() {
        if (cGrupo.getSelectedIndex() == 0) {
            profesor.setText("");
            carrera.setText("");
            horade.setText("");
            horaa.setText("");
            fechainicio.setText("");
            fechafin.setText("");
            inscripcion.setValue(null);
            colegiatura.setValue(null);
            removejtable();
            removejtable2();
            sumartotal();
            inicioalumno.setText("");
            beca.setText("");
            dia.setText("");
            Utilidades.esObligatorio(this.JPanelRecibo, false);
            //Utilidades.esObligatorio(this.JPanelBusqueda, false);
            Utilidades.esObligatorio(this.JPanelGrupo, false);
            Utilidades.esObligatorio(this.JPanelPago, false);

        } else if (cGrupo.getSelectedIndex() != -1) {

            mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
            String[] id = {grup.getID()};

            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();
            String[] cond = {"grupo.idgrupo"};
            String inner = " INNER JOIN profesor on grupo.profesor_idcatedratico=profesor.idcatedratico INNER JOIN carrera on grupo.carrera_idcarrera=carrera.idcarrera ";
            if (!id.equals(0)) {

                String conct = "concat(profesor.nombre,' ',profesor.apellido)";
                String[] campos = {conct, "carrera.descripcion", "DATE_FORMAT(grupo.horariode,'%h:%i %p')", "DATE_FORMAT(grupo.horarioa,'%h:%i %p')", "DATE_FORMAT(grupo.fechainicio,'%d-%m-%Y')", "DATE_FORMAT(grupo.fechafin,'%d-%m-%Y')", "grupo.inscripcion", "grupo.colegiatura", "grupo.dia"};

                rs = ac.getRegistros("grupo", campos, cond, id, inner);

                if (rs != null) {
                    try {
                        if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                            rs.beforeFirst();//regresa el puntero al primer registro
                            while (rs.next()) {//mientras tenga registros que haga lo siguiente
                                profesor.setText(rs.getString(1));
                                carrera.setText(rs.getString(2));
                                horade.setText(rs.getString(3));
                                horaa.setText(rs.getString(4));
                                fechainicio.setText((rs.getString(5)));
                                fechafin.setText((rs.getString(6)));
                                inscripcion.setValue(rs.getFloat(7));
                                colegiatura.setValue(rs.getFloat(8));
                                dia.setText(rs.getString(9));
                            }
                            idalumnosengrupo(idalumno, "" + grup.getID());
                            MostrarPagos();
                            MostrarProductos();
                            sumartotal();
                            Utilidades.esObligatorio(this.JPanelRecibo, false);
                            Utilidades.esObligatorio(this.JPanelGrupo, false);
                            Utilidades.esObligatorio(this.JPanelPago, false);
                        }
                    } catch (SQLException e) {
                        JOptionPane.showInternalMessageDialog(this, e);
                    }
                }

            }
        }
    }

    /*
     * Metodo para buscar un alumno por su codigo devuelde el id
     */
    public void balumnocodigo(String codigo) {
        if (codigo.isEmpty()) {
            nombrealumno.setText("");
            beca.setText("");
            //inicioalumno.setDate(null);
            estado.setText("");
            cGrupo.removeAllItems();
            idalumno = "";
            inicioalumno.setText("");
            beca.setText("");
            dia.setText("");

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
                                llenarcombogrupo(rs.getString(1));
                                nombrealumno.setText(rs.getString(2) + " " + rs.getString(3));
                                if (rs.getString(5).equals("0")) {
                                    estado.setText("Inactivo");
                                    estado.setForeground(Color.red);
                                } else if (rs.getString(5).equals("1")) {
                                    estado.setText("Activo");
                                    estado.setForeground(Color.WHITE/*new java.awt.Color(102, 204, 0)*/);
                                }
                                idalumno = (rs.getString(6));
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
    private void MostrarPagos() {

        String sql = "SELECT proyeccionpagos.idproyeccionpagos,proyeccionpagos.mes_idmes,mes.mes,proyeccionpagos.año,proyeccionpagos.monto,\n"
                + "     proyeccionpagos.fechavencimiento,IFNULL((SELECT mora.mora FROM mora where proyeccionpagos.idproyeccionpagos = mora.proyeccionpagos_idproyeccionpagos and mora.exoneracion=0),0.0) AS 'Mora',proyeccionpagos.alumnosengrupo_iddetallegrupo FROM\n"
                + "     mes INNER JOIN proyeccionpagos ON mes.idmes = proyeccionpagos.mes_idmes  where alumnosengrupo_iddetallegrupo='" + iddetallegrupo + "' and proyeccionpagos.estado='0' and proyeccionpagos.asignado='1' order by proyeccionpagos.idproyeccionpagos asc ";

        removejtable();
        model = getRegistroPorLikel(model, sql);
        Utilidades.ajustarAnchoColumnas(colegiaturas);

        colegiaturas.getColumnModel().getColumn(0).setMaxWidth(0);
        colegiaturas.getColumnModel().getColumn(0).setMinWidth(0);
        colegiaturas.getColumnModel().getColumn(0).setPreferredWidth(0);
        colegiaturas.doLayout();
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
            int cantcampos = 9;
            //if (rs != null) {
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                //int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos + 1];

                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
                    for (int i = 0; i < cantcampos - 2; i++) {

                        fila[i] = rs.getObject(i + 1); // El primer indice en rs es el 1, no el cero, por eso se suma 1.
                        if (i == 4) {
                            float monto = (float) rs.getObject(i + 1);
                            //float cbeca = Float.parseFloat(beca.getText());
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
                    if (((float) fila[6] == 0.0)) {
                        fila[8] = false;
                    } else {
                        fila[8] = true;
                    }
                    fila[9] = false;
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

    private void MostrarProductos() {

        String sql = "SELECT otrospagos.idpago,otrospagos.descripcion,otrospagos.costo FROM otrospagos order by otrospagos.descripcion";

        removejtable2();
        model2 = getRegistroPorLikell(model2, sql);
        Utilidades.ajustarAnchoColumnas(otrosproductos);
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
    public DefaultTableModel getRegistroPorLikell(DefaultTableModel modelo, String tabla) {
        try {

            ResultSet rs;

            rs = acceso.getRegistroProc(tabla);
            int cantcampos = 6;
            //if (rs != null) {
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                //int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos];

                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    fila[0] = rs.getString(1);
                    fila[1] = rs.getString(2);
                    fila[2] = Float.parseFloat(rs.getString(3));
                    fila[3] = 1.0;
                    fila[4] = (Math.round((1.0 * Float.parseFloat(rs.getString(3))) * 100.0) / 100.0);
                    fila[5] = false;
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
        //TipoFiltro.setFiltraEntrada(codigo.getDocument(), FiltroCampos.NUM_LETRAS, 45, false);
        //TipoFiltro.setFiltraEntrada(descripcion.getDocument(), FiltroCampos.NUM_LETRAS, 60, true);
        //TipoFiltro.setFiltraEntrada(dia.getDocument(), FiltroCampos.SOLO_LETRAS, 45, false);
        //TipoFiltro.setFiltraEntrada(profesor.getDocument(), FiltroCampos.NUM_LETRAS, 200, true);
        //TipoFiltro.setFiltraEntrada(cantalumnos.getDocument(), FiltroCampos.SOLO_NUMEROS, 5, true);
//        TipoFiltro.setFiltraEntrada(colegiatura.getDocument(), FiltroCampos.SOLO_NUMEROS, 12, false);
        //TipoFiltro.setFiltraEntrada(busqueda.getDocument(), FiltroCampos.NUM_LETRAS, 100, true);
    }

    public void idalumnosengrupo(String idalumno, String idgrupo) {

        String[] id = {idalumno, idgrupo};
        ResultSet rs;
        AccesoDatos ac = new AccesoDatos();
        String[] cond = {"alumnosengrupo.alumno_idalumno", "alumnosengrupo.grupo_idgrupo"};
        String[] campos = {"alumnosengrupo.iddetallegrupo", "alumnosengrupo.fechainicio", "alumnosengrupo.beca"};
        rs = ac.getRegistros("alumnosengrupo", campos, cond, id, "");

        if (rs != null) {
            try {
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    rs.beforeFirst();//regresa el puntero al primer registro
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        iddetallegrupo = (rs.getString(1));
                        String fechainicio = FormatoFecha.getFormato(rs.getDate(2), FormatoFecha.D_M_A);
                        inicioalumno.setText(fechainicio);
                        float becac = Float.parseFloat(rs.getString(3));
                        beca.setText("" + becac);
                    }
                }
            } catch (SQLException e) {
                iddetallegrupo = "";
                JOptionPane.showInternalMessageDialog(this, e);
            }
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
        bntGuardar1 = new elaprendiz.gui.button.ButtonRect();
        JPanelGrupo = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cGrupo = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        carrera = new elaprendiz.gui.textField.TextField();
        profesor = new elaprendiz.gui.textField.TextField();
        inscripcion = new javax.swing.JFormattedTextField();
        colegiatura = new javax.swing.JFormattedTextField();
        horaa = new elaprendiz.gui.textField.TextField();
        horade = new elaprendiz.gui.textField.TextField();
        fechainicio = new elaprendiz.gui.textField.TextField();
        fechafin = new elaprendiz.gui.textField.TextField();
        jLabel12 = new javax.swing.JLabel();
        dia = new elaprendiz.gui.textField.TextField();
        jLabel26 = new javax.swing.JLabel();
        inicioalumno = new elaprendiz.gui.textField.TextField();
        jLabel25 = new javax.swing.JLabel();
        beca = new elaprendiz.gui.textField.TextField();
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
        nombrealumno = new elaprendiz.gui.textField.TextField();
        jLabel19 = new javax.swing.JLabel();
        estado = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        tbPane1 = new elaprendiz.gui.panel.TabbedPaneHeader();
        JPanelPago = new javax.swing.JPanel();
        totalapagar = new javax.swing.JFormattedTextField();
        jLabel27 = new javax.swing.JLabel();
        cTipopago = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        JPanelRecibo = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        clockDigital2 = new elaprendiz.gui.varios.ClockDigital();
        jLabel23 = new javax.swing.JLabel();
        fechapago = new com.toedter.calendar.JDateChooser();
        pnlPaginador1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();

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
        setTitle("Registro de Pagos");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Pagos"); // NOI18N
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
        bntGuardar.setName("Guardar Pagos"); // NOI18N
        bntGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntGuardarActionPerformed(evt);
            }
        });
        pnlActionButtons.add(bntGuardar, new java.awt.GridBagConstraints());

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

        bntGuardar1.setBackground(new java.awt.Color(51, 153, 255));
        bntGuardar1.setMnemonic(KeyEvent.VK_E);
        bntGuardar1.setText("Estado de Cuenta");
        bntGuardar1.setName("EstadoDeCuenta"); // NOI18N
        bntGuardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntGuardar1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
        pnlActionButtons.add(bntGuardar1, gridBagConstraints);

        panelImage.add(pnlActionButtons);
        pnlActionButtons.setBounds(0, 580, 880, 50);

        JPanelGrupo.setBackground(java.awt.SystemColor.activeCaption);
        JPanelGrupo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelGrupo.setForeground(new java.awt.Color(204, 204, 204));
        JPanelGrupo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JPanelGrupo.setLayout(null);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Horario A:");
        JPanelGrupo.add(jLabel10);
        jLabel10.setBounds(730, 10, 110, 20);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Profesor:");
        JPanelGrupo.add(jLabel3);
        jLabel3.setBounds(320, 10, 250, 20);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Fecha Fin:");
        JPanelGrupo.add(jLabel6);
        jLabel6.setBounds(730, 60, 110, 20);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Horario De:");
        JPanelGrupo.add(jLabel13);
        jLabel13.setBounds(600, 10, 100, 20);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Fecha Inicio:");
        JPanelGrupo.add(jLabel9);
        jLabel9.setBounds(600, 60, 110, 20);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Carrera:");
        JPanelGrupo.add(jLabel5);
        jLabel5.setBounds(320, 60, 250, 20);

        cGrupo.setEditable(true);
        cGrupo.setName("grupo"); // NOI18N
        JPanelGrupo.add(cGrupo);
        cGrupo.setBounds(80, 30, 210, 24);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Grupo:");
        JPanelGrupo.add(jLabel7);
        jLabel7.setBounds(10, 30, 60, 27);

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Inscripción Q.");
        JPanelGrupo.add(jLabel24);
        jLabel24.setBounds(600, 110, 110, 20);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Colegiatura Q.");
        JPanelGrupo.add(jLabel18);
        jLabel18.setBounds(730, 110, 110, 20);

        carrera.setEditable(false);
        carrera.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        carrera.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(carrera);
        carrera.setBounds(320, 80, 260, 24);

        profesor.setEditable(false);
        profesor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        profesor.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(profesor);
        profesor.setBounds(320, 30, 260, 24);

        inscripcion.setEditable(false);
        inscripcion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        inscripcion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inscripcion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        inscripcion.setPreferredSize(new java.awt.Dimension(80, 23));
        JPanelGrupo.add(inscripcion);
        inscripcion.setBounds(600, 130, 110, 24);

        colegiatura.setEditable(false);
        colegiatura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        colegiatura.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        colegiatura.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        colegiatura.setPreferredSize(new java.awt.Dimension(80, 23));
        JPanelGrupo.add(colegiatura);
        colegiatura.setBounds(730, 130, 110, 24);

        horaa.setEditable(false);
        horaa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        horaa.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(horaa);
        horaa.setBounds(730, 30, 110, 24);

        horade.setEditable(false);
        horade.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        horade.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(horade);
        horade.setBounds(600, 30, 110, 24);

        fechainicio.setEditable(false);
        fechainicio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechainicio.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(fechainicio);
        fechainicio.setBounds(600, 80, 110, 24);

        fechafin.setEditable(false);
        fechafin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechafin.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(fechafin);
        fechafin.setBounds(730, 80, 110, 24);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Día:");
        JPanelGrupo.add(jLabel12);
        jLabel12.setBounds(320, 110, 250, 20);

        dia.setEditable(false);
        dia.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        dia.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(dia);
        dia.setBounds(320, 130, 260, 24);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Fecha Inicio Alumno:");
        JPanelGrupo.add(jLabel26);
        jLabel26.setBounds(10, 80, 160, 24);

        inicioalumno.setEditable(false);
        inicioalumno.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inicioalumno.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        inicioalumno.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(inicioalumno);
        inicioalumno.setBounds(170, 80, 120, 24);

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Beca Alumno: Q.");
        JPanelGrupo.add(jLabel25);
        jLabel25.setBounds(40, 130, 130, 27);

        beca.setEditable(false);
        beca.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        beca.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        beca.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(beca);
        beca.setBounds(170, 130, 120, 24);

        panelImage.add(JPanelGrupo);
        JPanelGrupo.setBounds(0, 160, 880, 170);

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
                        if(column==3 || column==5){
                            return true;
                        }else{
                            return false;}
                    }
                });
                otrosproductos.setFocusCycleRoot(true);
                otrosproductos.setGridColor(new java.awt.Color(51, 51, 255));
                otrosproductos.setName("otrosproductos"); // NOI18N
                otrosproductos.setRowHeight(20);
                otrosproductos.setSelectionBackground(java.awt.SystemColor.activeCaption);
                jScrollPane2.setViewportView(otrosproductos);

                jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 756, 210));

                tbPane.addTab("Otros Pagos", jPanel4);

                JPanelTable.add(tbPane, java.awt.BorderLayout.CENTER);

                panelImage.add(JPanelTable);
                JPanelTable.setBounds(0, 330, 760, 250);

                JPanelBusqueda.setBackground(java.awt.SystemColor.inactiveCaption);
                JPanelBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                JPanelBusqueda.setLayout(null);

                codigoa.setPreferredSize(new java.awt.Dimension(250, 27));
                codigoa.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        codigoaActionPerformed(evt);
                    }
                });
                JPanelBusqueda.add(codigoa);
                codigoa.setBounds(120, 10, 97, 24);

                jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                jLabel16.setText("Codigo:");
                JPanelBusqueda.add(jLabel16);
                jLabel16.setBounds(10, 10, 100, 24);

                jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar 2.png"))); // NOI18N
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton1ActionPerformed(evt);
                    }
                });
                JPanelBusqueda.add(jButton1);
                jButton1.setBounds(220, 10, 20, 24);

                nombrealumno.setEditable(false);
                nombrealumno.setPreferredSize(new java.awt.Dimension(250, 27));
                JPanelBusqueda.add(nombrealumno);
                nombrealumno.setBounds(440, 10, 360, 24);

                jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                jLabel19.setText("Alumno:");
                JPanelBusqueda.add(jLabel19);
                jLabel19.setBounds(310, 10, 120, 24);

                estado.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
                estado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                JPanelBusqueda.add(estado);
                estado.setBounds(810, 10, 60, 30);

                panelImage.add(JPanelBusqueda);
                JPanelBusqueda.setBounds(0, 110, 880, 50);

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

                cTipopago.setEditable(true);
                cTipopago.setComponentPopupMenu(popupprotipopago);
                cTipopago.setName("tipopago"); // NOI18N
                JPanelPago.add(cTipopago, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 180, 105, 25));

                jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel8.setText("Tipo Pago:");
                JPanelPago.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 157, 105, 20));

                tbPane1.addTab("============", JPanelPago);

                jPanel1.add(tbPane1, java.awt.BorderLayout.CENTER);
                tbPane1.getAccessibleContext().setAccessibleName("TOTAL");

                panelImage.add(jPanel1);
                jPanel1.setBounds(760, 330, 120, 250);

                JPanelRecibo.setBackground(java.awt.SystemColor.activeCaption);
                JPanelRecibo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                JPanelRecibo.setLayout(null);

                jLabel21.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
                jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel21.setText("Hora");
                JPanelRecibo.add(jLabel21);
                jLabel21.setBounds(690, 10, 100, 19);

                clockDigital2.setForeground(new java.awt.Color(255, 255, 255));
                clockDigital2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
                JPanelRecibo.add(clockDigital2);
                clockDigital2.setBounds(690, 30, 100, 27);

                jLabel23.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
                jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel23.setText("Fecha");
                JPanelRecibo.add(jLabel23);
                jLabel23.setBounds(120, 10, 120, 19);

                fechapago.setDate(Calendar.getInstance().getTime());
                fechapago.setDateFormatString("dd/MM/yyyy");
                fechapago.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                fechapago.setMaxSelectableDate(new java.util.Date(3093496470100000L));
                fechapago.setMinSelectableDate(new java.util.Date(-62135744300000L));
                fechapago.setPreferredSize(new java.awt.Dimension(120, 22));
                JPanelRecibo.add(fechapago);
                fechapago.setBounds(120, 30, 120, 27);

                panelImage.add(JPanelRecibo);
                JPanelRecibo.setBounds(0, 40, 880, 70);

                pnlPaginador1.setBackground(new java.awt.Color(57, 104, 163));
                pnlPaginador1.setPreferredSize(new java.awt.Dimension(786, 40));
                pnlPaginador1.setLayout(new java.awt.GridBagLayout());

                jLabel11.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
                jLabel11.setForeground(new java.awt.Color(255, 255, 255));
                jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/money.png"))); // NOI18N
                jLabel11.setText("<--Registro de pagos-->");
                pnlPaginador1.add(jLabel11, new java.awt.GridBagConstraints());

                panelImage.add(pnlPaginador1);
                pnlPaginador1.setBounds(0, 0, 880, 40);

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
        BuscarAlumno frmBuscarAlumno = new BuscarAlumno();
        if (frmBuscarAlumno == null) {
            frmBuscarAlumno = new BuscarAlumno();
        }
        adminInternalFrame(dp, frmBuscarAlumno);
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
        llenarcombotipopago();
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

            if (Utilidades.esObligatorio(this.JPanelRecibo, true)
                    || Utilidades.esObligatorio(this.JPanelGrupo, true)
                    || Utilidades.esObligatorio(this.JPanelPago, true)) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (colegiaturas.getRowCount() == 0 /*&& colegiaturas.getSelectedRow() == -1*/ && otrosproductos.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "La tabla no contiene datos");

            } else { //Inicio de Guardar datos
                int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
                if (resp == 0) {

                    String printHorario = "";
                    horade.getText();
                    horaa.getText();
                    printHorario = horade.getText() + " a " + horaa.getText() + " " + dia.getText();
                //GUARDAR DATOS DE RECIBO***************************************
                    //**************************************************************
                    int idrecibo = 0, n = 0;
                    String sql = "";
                    String fechapag = FormatoFecha.getFormato(fechapago.getCalendar().getTime(), FormatoFecha.A_M_D);
                    mTipopago tipop = (mTipopago) cTipopago.getSelectedItem();
                    String idtipop = tipop.getID();
                    float total = Float.parseFloat(totalapagar.getText());

                    sql = "insert into recibodepago (fecha,alumno_idalumno,tipopago_idtipopago,total,usuario_idusuario) values (?,?,?,?,?)";
                    //int op = 0;
                    PreparedStatement ps = null;
                    conn = BdConexion.getConexion();

                    try {
                        conn.setAutoCommit(false);
                        ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                        ps.setString(1, fechapag);
                        ps.setInt(2, Integer.parseInt(idalumno));
                        ps.setInt(3, Integer.parseInt(idtipop));
                        ps.setFloat(4, total);
                        ps.setInt(5, Integer.parseInt("" + 1));//modificar por el usuario logeado

                        n = ps.executeUpdate();
                        if (n > 0) {
                            ResultSet rs = ps.getGeneratedKeys();
                            while (rs.next()) {
                                idrecibo = rs.getInt(1);//retorna el idrecibo guardado
                            }

                        //GUARDAR MESES PAGADOS y OTROS PRODUCTOS***************
                            //******************************************************
                            boolean camprec = false;
                            int cant = model.getRowCount();
                            int cant2 = model2.getRowCount();

                            for (int i = 0; i < cant; i++) { //for pago de meses
                                if (colegiaturas.getValueAt(i, 9).toString().equals("true")) {
                                    camprec = true;
                                    String id = (String) "" + colegiaturas.getValueAt(i, 0);
                                    String detrecibo = "insert into detrecibo (recibodepago_idrecibo,proyeccionpagos_idproyeccionpagos) values ('" + idrecibo + "','" + id + "')";
                                    String proypago = "update proyeccionpagos set  estado=true where idproyeccionpagos=" + id;

                                    n = ps.executeUpdate(detrecibo);
                                    n = ps.executeUpdate(proypago);

                                    if (colegiaturas.getValueAt(i, 8).toString().equals("true") && !colegiaturas.getValueAt(i, 6).toString().equals("0.0")) {
                                        String pmora = "update mora set  estado=true where proyeccionpagos_idproyeccionpagos=" + id;
                                        n = ps.executeUpdate(pmora);
                                    } else if (colegiaturas.getValueAt(i, 8).toString().equals("false") && !colegiaturas.getValueAt(i, 6).toString().equals("0.0")) {
                                        float exoneracion = Float.parseFloat(colegiaturas.getValueAt(i, 6).toString());
                                        String pmora = "update mora set  exoneracion=" + exoneracion + ", estado=true where proyeccionpagos_idproyeccionpagos=" + id;
                                        n = ps.executeUpdate(pmora);
                                    }
                                }
                            }//fin for pago de meses

                            for (int i = 0; i < cant2; i++) {//for pago otros productos
                                if (otrosproductos.getValueAt(i, 5).toString().equals("true")) {

                                    String id = (String) "" + otrosproductos.getValueAt(i, 0);
                                    float canti = Float.parseFloat(otrosproductos.getValueAt(i, 3).toString());
                                    float prec = Float.parseFloat(otrosproductos.getValueAt(i, 2).toString());

                                    String descriprecibo = "insert into descripcionrecibo (cantidad,precio,recibo_idrecibo,pago_idpago) "
                                            + "values ('" + canti + "','" + prec + "','" + idrecibo + "','" + id + "')";
                                    if (canti > 0) {
                                        camprec = true;
                                        n = ps.executeUpdate(descriprecibo);
                                    } else if (canti == 0) {
                                    }
                                }
                            }//fin for otros productos

                            if (!camprec) {
                                JOptionPane.showInternalMessageDialog(this, "No se ha marcado ningun Pago", "Mensage", JOptionPane.INFORMATION_MESSAGE);
                                System.out.print(n);
                            }
                            if (n > 0) {
                                int resp2 = JOptionPane.showInternalConfirmDialog(this, "El Pago se ha Guardado Correctamente\n ¿Desea realizar otro Pago de este Alumno?", "Pregunta", 0);
                                if (resp2 == 0) {
                                    mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
                                    String[] id = {grup.getID()};
                                    idalumnosengrupo(idalumno, "" + grup.getID());
                                    MostrarPagos();
                                    MostrarProductos();

                                } else {
                                    limpiartodo();
                                }
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
                            Logger.getLogger(Ingreso_Votos.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
            }//Fin Guardar datos
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntGuardarActionPerformed

    private void bntGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardar1ActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntGuardar1.getName()) == true) {

            if (Utilidades.esObligatorio(this.JPanelGrupo, true)) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
                String id = grup.getID();
                EstadoCuenta.comprobante(idalumno, grup.getID());
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntGuardar1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Actualizar;
    private javax.swing.JMenuItem Actualizar_Carrera;
    private javax.swing.JMenuItem Actualizar_Profesor;
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelGrupo;
    private javax.swing.JPanel JPanelPago;
    private javax.swing.JPanel JPanelRecibo;
    private javax.swing.JPanel JPanelTable;
    private javax.swing.JMenuItem Nueva_Carrera;
    private javax.swing.JMenuItem Nuevo_Profesor;
    private javax.swing.JMenuItem Nuevo_Tipopago;
    public static elaprendiz.gui.textField.TextField beca;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntGuardar1;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    public static javax.swing.JComboBox cGrupo;
    public static javax.swing.JComboBox cTipopago;
    private elaprendiz.gui.textField.TextField carrera;
    private elaprendiz.gui.varios.ClockDigital clockDigital2;
    public static elaprendiz.gui.textField.TextField codigoa;
    private javax.swing.JFormattedTextField colegiatura;
    private javax.swing.JTable colegiaturas;
    private elaprendiz.gui.textField.TextField dia;
    public static javax.swing.JLabel estado;
    private elaprendiz.gui.textField.TextField fechafin;
    private elaprendiz.gui.textField.TextField fechainicio;
    public static com.toedter.calendar.JDateChooser fechapago;
    private elaprendiz.gui.textField.TextField horaa;
    private elaprendiz.gui.textField.TextField horade;
    public static elaprendiz.gui.textField.TextField inicioalumno;
    private javax.swing.JFormattedTextField inscripcion;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    public static elaprendiz.gui.textField.TextField nombrealumno;
    private javax.swing.JTable otrosproductos;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador1;
    private javax.swing.JPopupMenu popupcarrera;
    private javax.swing.JPopupMenu popupprofesor;
    private javax.swing.JPopupMenu popupprotipopago;
    private elaprendiz.gui.textField.TextField profesor;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane1;
    private javax.swing.JFormattedTextField totalapagar;
    // End of variables declaration//GEN-END:variables
}
