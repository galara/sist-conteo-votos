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
    String[] titulos2 = {"Código", "Descripción", "Precio", "Cantidad", "SubTotal", "Agregar","otro","--"};//Titulos para Jtabla
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

//        cGrupo.addItemListener(
//                (ItemEvent e) -> {
//                    if (e.getStateChange() == ItemEvent.SELECTED) {
//                        selecciongrupo();
//                    }
//                });

//        colegiaturas.getModel().addTableModelListener(new TableModelListener() {
//            public void tableChanged(TableModelEvent e) {
//                sumartotal();
//                //formatotabla();
//            }
//        });

//        otrosproductos.getModel().addTableModelListener(new TableModelListener() {
//            public void tableChanged(TableModelEvent e) {
//                sumartotal();
//                //formatotabla();
//            }
//        });
        //colegiaturas.getColumnModel().getColumn(8).setCellEditor(new Editor_CheckBox());
        //colegiaturas.getColumnModel().getColumn(0).setPreferredWidth(0);
        tpresidente.getColumnModel().getColumn(0).setMaxWidth(0);
        tpresidente.getColumnModel().getColumn(0).setMinWidth(0);
        tpresidente.getColumnModel().getColumn(0).setPreferredWidth(0);
        tpresidente.doLayout();
        JCheckBox check = new JCheckBox();
//        tpresidente.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(check));
//        //tpresidente.getColumnModel().getColumn(9).setCellEditor(new DefaultCellEditor(check));
       otrosproductos.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(check));
//        //colegiaturas.getColumnModel().getColumn(8).setCellEditor(new Editor_CheckBox()); *
//        //colegiaturas.getColumnModel().getColumn(9).setCellEditor(new Editor_CheckBox()); *
//
//        //para pintar la columna con el CheckBox en la tabla, en este caso, la primera columna
//        tpresidente.getColumnModel().getColumn(5).setCellRenderer(new Renderer_CheckBox());
//        //tpresidente.getColumnModel().getColumn(9).setCellRenderer(new Renderer_CheckBox());
        otrosproductos.getColumnModel().getColumn(7).setCellRenderer(new Renderer_CheckBox());
//
        CellEditorSpinnerPago cnt = new CellEditorSpinnerPago(1);
        otrosproductos.getColumnModel().getColumn(6).setCellEditor(cnt);
        otrosproductos.getColumnModel().getColumn(6).setCellRenderer(new TableCellFormatter(null));

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
        while (tpresidente.getRowCount() != 0) {
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
        //profesor.setText("");
        //carrera.setText("");
        idcentro.setText("");
        idmunicipio.setText("");
        //fechainicio.setText("");
        //fechafin.setText("");
        //inscripcion.setValue(null);
        //colegiatura.setValue(null);
        nombrealumno.setText("");
        //beca.setText("");
        //inicioalumno.setText("");
        //dia.setText("");
        //cGrupo.setSelectedIndex(-1);
        Utilidades.esObligatorio(this.JPanelRecibo, false);
        Utilidades.esObligatorio(this.JPanelGrupo, false);
        //Utilidades.esObligatorio(this.JPanelPago, false);
        codigoa.requestFocus();
    }

//    public void sumartotal() {
//        if (colegiaturas.getRowCount() == 0 && /*colegiaturas.getSelectedRow() == -1*/ otrosproductos.getRowCount() == 0) {
//            totalapagar.setValue(0.0);
//        } else {
//            float Actual, Resultado = 0;
//            for (int i = 0; i < model.getRowCount(); i++) {//sumar total tabla meses
//                if (colegiaturas.getValueAt(i, 9).toString().equals("true") /*&& colegiaturas.getValueAt(i, 9).toString().equals(true)*/) {
//                    if (colegiaturas.getValueAt(i, 8).toString().equals("true")) {
//                        Actual = Float.parseFloat(colegiaturas.getValueAt(i, 7).toString());
//                        Resultado = Resultado + Actual;
//                    } else if (colegiaturas.getValueAt(i, 8).toString().equals("false")) {
//                        Actual = Float.parseFloat(colegiaturas.getValueAt(i, 4).toString());
//                        Resultado = Resultado + Actual;
//                    }
//                }
//                //totalapagar.setValue(Math.round(Resultado * 100.0) / 100.0);
//            }// fin sumar total meses
//
//            for (int i = 0; i < model2.getRowCount(); i++) {//sumar total tabla otrospagos
//                if (otrosproductos.getValueAt(i, 5).toString().equals("true") /*&& colegiaturas.getValueAt(i, 9).toString().equals(true)*/) {
//                    float canti = Float.parseFloat(otrosproductos.getValueAt(i, 3).toString());
//                    if (canti > 0) {
//                        Actual = Float.parseFloat(otrosproductos.getValueAt(i, 4).toString());
//                        Resultado = Resultado + Actual;
//                    }
//                }
//                //totalapagar.setValue(Math.round(Resultado * 100.0) / 100.0);
//            }// fin sumar total otrospagos
//
//            totalapagar.setValue(Math.round(Resultado * 100.0) / 100.0);
//
//        }
//    }

//    /*
//     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
//     *y se los envia a un metodo interno getRegistroCombo() 
//     *
//     */
//    public static void llenarcombogrupo(String idalumn) {
//        String Dato = "1";
//        String[] campos = {"grupo.codigo", "grupo.descripcion", "grupo.idgrupo"};
//        String[] condiciones = {"grupo.estado", "alumno.codigo"};
//        String[] Id = {Dato, idalumn};
//        cGrupo.removeAllItems();
//        String inner = " INNER JOIN alumnosengrupo ON grupo.idgrupo = alumnosengrupo.grupo_idgrupo INNER JOIN alumno ON alumnosengrupo.alumno_idalumno = alumno.idalumno ";
//        getRegistroCombo("grupo", campos, condiciones, Id, inner);
//
//    }

    /*El metodo llenarcombo() envia los parametros para la consulta a la BD y el medoto
     *getRegistroCombo() se encarga de enviarlos a la capa de AccesoDatos.getRegistros()
     *quiern devolcera un ResultSet para luego obtener los valores y agregarlos al JConboBox
     *y a una Hashtable que nos servira para obtener el id y seleccionar valores.
     */
//    public static void getRegistroCombo(String tabla, String[] campos, String[] campocondicion, String[] condicionid, String inner) {
//        try {
//            ResultSet rs;
//            AccesoDatos ac = new AccesoDatos();
//
//            rs = ac.getRegistros(tabla, campos, campocondicion, condicionid, inner);
//
//            int cantcampos = campos.length;
//            if (rs != null) {
//
//                DefaultComboBoxModel modeloComboBox;
//                modeloComboBox = new DefaultComboBoxModel();
//                cGrupo.setModel(modeloComboBox);
//
//                modeloComboBox.addElement(new mGrupo("", "0"));
//                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
//                    int count = 0;
//                    rs.beforeFirst();//regresa el puntero al primer registro
//                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
//                        count++;
//                        modeloComboBox.addElement(new mGrupo(rs.getString(1) + " " + rs.getString(2), "" + rs.getInt(3)));
//                        hashGrupo.put(rs.getString(1) + " " + rs.getString(2), "" + count);
//                    }
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Error", JOptionPane.INFORMATION_MESSAGE);
//            }
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }

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
        //cTipopago.removeAllItems();
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
                //cTipopago.setModel(modeloComboBox);

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

//    /* Metodo que llena los campos con la información de grupo
//     * Tambien llena en la pestaña de coelgiatura lo que el alumno tiene pendiente de pago
//     */
//    public void selecciongrupo() {
//        if (cGrupo.getSelectedIndex() == 0) {
//            profesor.setText("");
//            carrera.setText("");
//            idcentro.setText("");
//            idmunicipio.setText("");
//            fechainicio.setText("");
//            fechafin.setText("");
//            inscripcion.setValue(null);
//            colegiatura.setValue(null);
//            removejtable();
//            removejtable2();
//            sumartotal();
//            inicioalumno.setText("");
//            beca.setText("");
//            dia.setText("");
//            Utilidades.esObligatorio(this.JPanelRecibo, false);
//            //Utilidades.esObligatorio(this.JPanelBusqueda, false);
//            Utilidades.esObligatorio(this.JPanelGrupo, false);
//            Utilidades.esObligatorio(this.JPanelPago, false);
//
//        } else if (cGrupo.getSelectedIndex() != -1) {
//
//            mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
//            String[] id = {grup.getID()};
//
//            ResultSet rs;
//            AccesoDatos ac = new AccesoDatos();
//            String[] cond = {"grupo.idgrupo"};
//            String inner = " INNER JOIN profesor on grupo.profesor_idcatedratico=profesor.idcatedratico INNER JOIN carrera on grupo.carrera_idcarrera=carrera.idcarrera ";
//            if (!id.equals(0)) {
//
//                String conct = "concat(profesor.nombre,' ',profesor.apellido)";
//                String[] campos = {conct, "carrera.descripcion", "DATE_FORMAT(grupo.horariode,'%h:%i %p')", "DATE_FORMAT(grupo.horarioa,'%h:%i %p')", "DATE_FORMAT(grupo.fechainicio,'%d-%m-%Y')", "DATE_FORMAT(grupo.fechafin,'%d-%m-%Y')", "grupo.inscripcion", "grupo.colegiatura", "grupo.dia"};
//
//                rs = ac.getRegistros("grupo", campos, cond, id, inner);
//
//                if (rs != null) {
//                    try {
//                        if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
//                            rs.beforeFirst();//regresa el puntero al primer registro
//                            while (rs.next()) {//mientras tenga registros que haga lo siguiente
//                                profesor.setText(rs.getString(1));
//                                carrera.setText(rs.getString(2));
//                                idcentro.setText(rs.getString(3));
//                                idmunicipio.setText(rs.getString(4));
//                                fechainicio.setText((rs.getString(5)));
//                                fechafin.setText((rs.getString(6)));
//                                inscripcion.setValue(rs.getFloat(7));
//                                colegiatura.setValue(rs.getFloat(8));
//                                dia.setText(rs.getString(9));
//                            }
//                            idalumnosengrupo(idalumno, "" + grup.getID());
//                            MostrarPagos();
//                            MostrarProductos();
//                            sumartotal();
//                            Utilidades.esObligatorio(this.JPanelRecibo, false);
//                            Utilidades.esObligatorio(this.JPanelGrupo, false);
//                            Utilidades.esObligatorio(this.JPanelPago, false);
//                        }
//                    } catch (SQLException e) {
//                        JOptionPane.showInternalMessageDialog(this, e);
//                    }
//                }
//
//            }
//        }
//    }

    /*
     * Metodo para buscar un alumno por su codigo devuelde el id
     */
    public void balumnocodigo(String codigo) {
        if (codigo.isEmpty()) {
            nombrealumno.setText("");
            //beca.setText("");
            //inicioalumno.setDate(null);
            estado.setText("");
            //cGrupo.removeAllItems();
            idalumno = "";
            //inicioalumno.setText("");
            //beca.setText("");
            //dia.setText("");

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
                                //llenarcombogrupo(rs.getString(1));
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
        Utilidades.ajustarAnchoColumnas(tpresidente);

        tpresidente.getColumnModel().getColumn(0).setMaxWidth(0);
        tpresidente.getColumnModel().getColumn(0).setMinWidth(0);
        tpresidente.getColumnModel().getColumn(0).setPreferredWidth(0);
        tpresidente.doLayout();
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

        String sql = "select candidato.idcandidato, candidato.codigo, concat(candidato.nombres,' ',candidato.apellidos)AS nombre, partido_politico.nombre, puesto.nombre, municipio.nombre from candidato INNER JOIN partido_politico on candidato.partido_idpartido=partido_politico.idpartido INNER JOIN puesto on candidato.puesto_idpuesto=puesto.idpuesto INNER JOIN municipio on candidato.municipio_idmunicipio=municipio.idmunicipio   where puesto.nombre = 'Presidente' order by candidato.idcandidato";

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
            int cantcampos = 8;
            //if (rs != null) {
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                //int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos];

                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    fila[0] = rs.getString(1);
                    fila[1] = rs.getString(2);
                    fila[2] = rs.getString(3);
                    fila[3] = rs.getString(4);
                    fila[4] = rs.getString(5);
                    fila[5] = rs.getString(6);
                    fila[6] = 0.00;
                    fila[7] = false;
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
                        //inicioalumno.setText(fechainicio);
                        float becac = Float.parseFloat(rs.getString(3));
                       // beca.setText("" + becac);
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
        jButton2 = new javax.swing.JButton();
        JPanelTable = new javax.swing.JPanel();
        tbPane = new elaprendiz.gui.panel.TabbedPaneHeader();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tpresidente = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        otrosproductos = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        colegiaturas1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        colegiaturas2 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        colegiaturas3 = new javax.swing.JTable();
        JPanelBusqueda = new javax.swing.JPanel();
        codigoa = new elaprendiz.gui.textField.TextField();
        jLabel16 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        nombrealumno = new elaprendiz.gui.textField.TextField();
        jLabel19 = new javax.swing.JLabel();
        estado = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        municipio = new elaprendiz.gui.textField.TextField();
        idcentro = new elaprendiz.gui.textField.TextField();
        idmunicipio = new elaprendiz.gui.textField.TextField();
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

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        JPanelGrupo.add(jButton2);
        jButton2.setBounds(70, 10, 73, 23);

        panelImage.add(JPanelGrupo);
        JPanelGrupo.setBounds(0, 160, 880, 50);

        JPanelTable.setOpaque(false);
        JPanelTable.setPreferredSize(new java.awt.Dimension(786, 402));
        JPanelTable.setLayout(new java.awt.BorderLayout());

        tbPane.setOpaque(true);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tpresidente.setForeground(new java.awt.Color(51, 51, 51));
        tpresidente.setModel(model = new DefaultTableModel(null, titulos)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    if(column==8 || column==9){
                        return true;
                    }else{
                        return false;}
                }
            });
            tpresidente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            tpresidente.setFocusCycleRoot(true);
            tpresidente.setGridColor(new java.awt.Color(51, 51, 255));
            tpresidente.setRowHeight(20);
            tpresidente.setSelectionBackground(java.awt.SystemColor.activeCaption);
            tpresidente.setSurrendersFocusOnKeystroke(true);
            tpresidente.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tpresidenteMouseClicked(evt);
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    tpresidenteMouseClicked1(evt);
                }
            });
            tpresidente.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    tpresidenteKeyPressed(evt);
                }
            });
            jScrollPane4.setViewportView(tpresidente);

            jPanel3.add(jScrollPane4, java.awt.BorderLayout.CENTER);

            tbPane.addTab("Presidente", jPanel3);

            jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jPanel4.setLayout(new java.awt.BorderLayout());

            jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPane2.setOpaque(false);

            otrosproductos.setModel(model2 = new DefaultTableModel(null, titulos2)
                {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        if(column==6 || column==7){
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

                jPanel4.add(jScrollPane2, java.awt.BorderLayout.CENTER);

                tbPane.addTab("Diputado 1", jPanel4);

                jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                jPanel5.setLayout(new java.awt.BorderLayout());

                jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                colegiaturas1.setForeground(new java.awt.Color(51, 51, 51));
                colegiaturas1.setModel(model = new DefaultTableModel(null, titulos)
                    {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            if(column==8 || column==9){
                                return true;
                            }else{
                                return false;}
                        }
                    });
                    colegiaturas1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    colegiaturas1.setFocusCycleRoot(true);
                    colegiaturas1.setGridColor(new java.awt.Color(51, 51, 255));
                    colegiaturas1.setRowHeight(20);
                    colegiaturas1.setSelectionBackground(java.awt.SystemColor.activeCaption);
                    colegiaturas1.setSurrendersFocusOnKeystroke(true);
                    colegiaturas1.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            colegiaturas1MouseClicked(evt);
                        }
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                            colegiaturas1MouseClicked1(evt);
                        }
                    });
                    colegiaturas1.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyPressed(java.awt.event.KeyEvent evt) {
                            colegiaturas1KeyPressed(evt);
                        }
                    });
                    jScrollPane5.setViewportView(colegiaturas1);

                    jPanel5.add(jScrollPane5, java.awt.BorderLayout.CENTER);

                    tbPane.addTab("Diputado 2", jPanel5);

                    jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                    jPanel6.setLayout(new java.awt.BorderLayout());

                    jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    colegiaturas2.setForeground(new java.awt.Color(51, 51, 51));
                    colegiaturas2.setModel(model = new DefaultTableModel(null, titulos)
                        {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                if(column==8 || column==9){
                                    return true;
                                }else{
                                    return false;}
                            }
                        });
                        colegiaturas2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                        colegiaturas2.setFocusCycleRoot(true);
                        colegiaturas2.setGridColor(new java.awt.Color(51, 51, 255));
                        colegiaturas2.setRowHeight(20);
                        colegiaturas2.setSelectionBackground(java.awt.SystemColor.activeCaption);
                        colegiaturas2.setSurrendersFocusOnKeystroke(true);
                        colegiaturas2.addMouseListener(new java.awt.event.MouseAdapter() {
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                colegiaturas2MouseClicked(evt);
                            }
                            public void mousePressed(java.awt.event.MouseEvent evt) {
                                colegiaturas2MouseClicked1(evt);
                            }
                        });
                        colegiaturas2.addKeyListener(new java.awt.event.KeyAdapter() {
                            public void keyPressed(java.awt.event.KeyEvent evt) {
                                colegiaturas2KeyPressed(evt);
                            }
                        });
                        jScrollPane6.setViewportView(colegiaturas2);

                        jPanel6.add(jScrollPane6, java.awt.BorderLayout.CENTER);

                        tbPane.addTab("Diputado 4", jPanel6);

                        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                        jPanel7.setLayout(new java.awt.BorderLayout());

                        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                        colegiaturas3.setForeground(new java.awt.Color(51, 51, 51));
                        colegiaturas3.setModel(model = new DefaultTableModel(null, titulos)
                            {
                                @Override
                                public boolean isCellEditable(int row, int column) {
                                    if(column==8 || column==9){
                                        return true;
                                    }else{
                                        return false;}
                                }
                            });
                            colegiaturas3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                            colegiaturas3.setFocusCycleRoot(true);
                            colegiaturas3.setGridColor(new java.awt.Color(51, 51, 255));
                            colegiaturas3.setRowHeight(20);
                            colegiaturas3.setSelectionBackground(java.awt.SystemColor.activeCaption);
                            colegiaturas3.setSurrendersFocusOnKeystroke(true);
                            colegiaturas3.addMouseListener(new java.awt.event.MouseAdapter() {
                                public void mouseClicked(java.awt.event.MouseEvent evt) {
                                    colegiaturas3MouseClicked(evt);
                                }
                                public void mousePressed(java.awt.event.MouseEvent evt) {
                                    colegiaturas3MouseClicked1(evt);
                                }
                            });
                            colegiaturas3.addKeyListener(new java.awt.event.KeyAdapter() {
                                public void keyPressed(java.awt.event.KeyEvent evt) {
                                    colegiaturas3KeyPressed(evt);
                                }
                            });
                            jScrollPane7.setViewportView(colegiaturas3);

                            jPanel7.add(jScrollPane7, java.awt.BorderLayout.CENTER);

                            tbPane.addTab("Alcalde", jPanel7);

                            JPanelTable.add(tbPane, java.awt.BorderLayout.CENTER);

                            panelImage.add(JPanelTable);
                            JPanelTable.setBounds(0, 210, 880, 370);

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
                            jLabel16.setText("No. Mesa:");
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
                            nombrealumno.setBounds(390, 10, 190, 24);

                            jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                            jLabel19.setText("Centro:");
                            JPanelBusqueda.add(jLabel19);
                            jLabel19.setBounds(260, 10, 120, 24);

                            estado.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
                            estado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                            JPanelBusqueda.add(estado);
                            estado.setBounds(810, 10, 60, 30);

                            jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                            jLabel20.setText("Municipio:");
                            JPanelBusqueda.add(jLabel20);
                            jLabel20.setBounds(590, 10, 69, 24);

                            municipio.setEditable(false);
                            municipio.setPreferredSize(new java.awt.Dimension(250, 27));
                            JPanelBusqueda.add(municipio);
                            municipio.setBounds(660, 10, 200, 24);

                            idcentro.setEditable(false);
                            idcentro.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                            idcentro.setPreferredSize(new java.awt.Dimension(120, 21));
                            JPanelBusqueda.add(idcentro);
                            idcentro.setBounds(270, 10, 20, 24);

                            idmunicipio.setEditable(false);
                            idmunicipio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                            idmunicipio.setPreferredSize(new java.awt.Dimension(120, 21));
                            JPanelBusqueda.add(idmunicipio);
                            idmunicipio.setBounds(290, 10, 20, 24);

                            panelImage.add(JPanelBusqueda);
                            JPanelBusqueda.setBounds(0, 110, 880, 50);

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

    private void tpresidenteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tpresidenteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tpresidenteKeyPressed

    private void tpresidenteMouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tpresidenteMouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_tpresidenteMouseClicked1

    private void tpresidenteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tpresidenteMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tpresidenteMouseClicked

    private void bntGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardarActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntGuardar.getName()) == true) {

            if (Utilidades.esObligatorio(this.JPanelRecibo, true)
                    || Utilidades.esObligatorio(this.JPanelGrupo, true)
                    /*|| Utilidades.esObligatorio(this.JPanelPago, true)*/) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tpresidente.getRowCount() == 0 /*&& colegiaturas.getSelectedRow() == -1*/ && otrosproductos.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "La tabla no contiene datos");

            } else { //Inicio de Guardar datos
                int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
                if (resp == 0) {

                    String printHorario = "";
                    idcentro.getText();
                    idmunicipio.getText();
                    //printHorario = idcentro.getText() + " a " + idmunicipio.getText() + " " + /*dia.getText()*/;
                //GUARDAR DATOS DE RECIBO***************************************
                    //**************************************************************
                    int idrecibo = 0, n = 0;
                    String sql = "";
                    String fechapag = FormatoFecha.getFormato(fechapago.getCalendar().getTime(), FormatoFecha.A_M_D);
                    //mTipopago tipop = (mTipopago) cTipopago.getSelectedItem();
                    //String idtipop = tipop.getID();
                    //float total = Float.parseFloat(totalapagar.getText());

                    sql = "insert into recibodepago (fecha,alumno_idalumno,tipopago_idtipopago,total,usuario_idusuario) values (?,?,?,?,?)";
                    //int op = 0;
                    PreparedStatement ps = null;
                    conn = BdConexion.getConexion();

                    try {
                        conn.setAutoCommit(false);
                        ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                        ps.setString(1, fechapag);
                        ps.setInt(2, Integer.parseInt(idalumno));
                        //ps.setInt(3, Integer.parseInt(idtipop));
                        //ps.setFloat(4, total);
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
                                if (tpresidente.getValueAt(i, 9).toString().equals("true")) {
                                    camprec = true;
                                    String id = (String) "" + tpresidente.getValueAt(i, 0);
                                    String detrecibo = "insert into detrecibo (recibodepago_idrecibo,proyeccionpagos_idproyeccionpagos) values ('" + idrecibo + "','" + id + "')";
                                    String proypago = "update proyeccionpagos set  estado=true where idproyeccionpagos=" + id;

                                    n = ps.executeUpdate(detrecibo);
                                    n = ps.executeUpdate(proypago);

                                    if (tpresidente.getValueAt(i, 8).toString().equals("true") && !tpresidente.getValueAt(i, 6).toString().equals("0.0")) {
                                        String pmora = "update mora set  estado=true where proyeccionpagos_idproyeccionpagos=" + id;
                                        n = ps.executeUpdate(pmora);
                                    } else if (tpresidente.getValueAt(i, 8).toString().equals("false") && !tpresidente.getValueAt(i, 6).toString().equals("0.0")) {
                                        float exoneracion = Float.parseFloat(tpresidente.getValueAt(i, 6).toString());
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
                                    //mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
                                    //String[] id = {grup.getID()};
                                    //idalumnosengrupo(idalumno, "" + grup.getID());
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
                //mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
                //String id = grup.getID();
                //EstadoCuenta.comprobante(idalumno, grup.getID());
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntGuardar1ActionPerformed

    private void colegiaturas1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colegiaturas1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_colegiaturas1MouseClicked

    private void colegiaturas1MouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colegiaturas1MouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_colegiaturas1MouseClicked1

    private void colegiaturas1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_colegiaturas1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_colegiaturas1KeyPressed

    private void colegiaturas2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colegiaturas2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_colegiaturas2MouseClicked

    private void colegiaturas2MouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colegiaturas2MouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_colegiaturas2MouseClicked1

    private void colegiaturas2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_colegiaturas2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_colegiaturas2KeyPressed

    private void colegiaturas3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colegiaturas3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_colegiaturas3MouseClicked

    private void colegiaturas3MouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colegiaturas3MouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_colegiaturas3MouseClicked1

    private void colegiaturas3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_colegiaturas3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_colegiaturas3KeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        MostrarProductos();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Actualizar;
    private javax.swing.JMenuItem Actualizar_Carrera;
    private javax.swing.JMenuItem Actualizar_Profesor;
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelGrupo;
    private javax.swing.JPanel JPanelRecibo;
    private javax.swing.JPanel JPanelTable;
    private javax.swing.JMenuItem Nueva_Carrera;
    private javax.swing.JMenuItem Nuevo_Profesor;
    private javax.swing.JMenuItem Nuevo_Tipopago;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntGuardar1;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.varios.ClockDigital clockDigital2;
    public static elaprendiz.gui.textField.TextField codigoa;
    private javax.swing.JTable colegiaturas1;
    private javax.swing.JTable colegiaturas2;
    private javax.swing.JTable colegiaturas3;
    public static javax.swing.JLabel estado;
    public static com.toedter.calendar.JDateChooser fechapago;
    public static elaprendiz.gui.textField.TextField idcentro;
    public static elaprendiz.gui.textField.TextField idmunicipio;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    public static elaprendiz.gui.textField.TextField municipio;
    public static elaprendiz.gui.textField.TextField nombrealumno;
    private javax.swing.JTable otrosproductos;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador1;
    private javax.swing.JPopupMenu popupcarrera;
    private javax.swing.JPopupMenu popupprofesor;
    private javax.swing.JPopupMenu popupprotipopago;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane;
    private javax.swing.JTable tpresidente;
    // End of variables declaration//GEN-END:variables
}
