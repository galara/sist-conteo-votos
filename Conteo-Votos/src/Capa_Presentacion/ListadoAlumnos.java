/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Capa_Presentacion;

import Capa_Datos.AccesoDatos;
import Capa_Negocio.AccesoUsuario;
import static Capa_Negocio.AddForms.adminInternalFrame;
import Capa_Negocio.FormatoDecimal;
import Capa_Negocio.FormatoFecha;
import Capa_Negocio.Peticiones;
import Capa_Negocio.Utilidades;
import static Capa_Presentacion.Principal.dp;
import Reportes.ListadoAlumnosGrupo;
import Reportes.ListadoAlumnosGrupoMineduc;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import modelos.mGrupo;
import modelos.mProfesor;

/**
 *
 * @author GLARA
 */
public class ListadoAlumnos extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model, model2;
    DefaultComboBoxModel modelCombo;
    // alumno.idalumno,alumno.codigo,alumno.nombres,alumno.apellidos,alumnosengrupo.beca 
    String[] titulos = {"Id", "No.", "Codigo", "Nombres", "Apellidos", "Beca"};//Titulos para Jtabla
    String[] titulos2 = {"Id", "No.", "Codigo", "Nombres", "Apellidos", "Beca"};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();
    public static Hashtable<String, String> hashGrupo = new Hashtable<>();
    public static Hashtable<String, String> hashTipopago = new Hashtable<>();
    public Hashtable<String, String> hashProfesor = new Hashtable<>();
    String condicion = "";
    AccesoDatos acceso = new AccesoDatos();
    static String idalumno = "", iddetallegrupo = "";
    java.sql.Connection conn;//getConnection intentara establecer una conexión.

    /**
     * Creates new form Cliente
     */
    public ListadoAlumnos() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();
        //llenarcombotipopago();
        llenarcomboprofesor();
        //Date d=new Date(); 
        Calendar c1 = GregorianCalendar.getInstance();
        //c1.get(Calendar.YEAR);
        ciclo.setText(Integer.toString(c1.get(Calendar.YEAR)));

        cGrupo.addItemListener(
                (ItemEvent e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        selecciongrupo();
                    }
                });

        cProfesor.addItemListener(
                (ItemEvent e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        grupos();

                    }
                });

        colegiaturas.getColumnModel().getColumn(0).setMaxWidth(0);
        colegiaturas.getColumnModel().getColumn(0).setMinWidth(0);
        colegiaturas.getColumnModel().getColumn(0).setPreferredWidth(0);
        colegiaturas.doLayout();
        otrosproductos.getColumnModel().getColumn(0).setMaxWidth(0);
        otrosproductos.getColumnModel().getColumn(0).setMinWidth(0);
        otrosproductos.getColumnModel().getColumn(0).setPreferredWidth(0);
        otrosproductos.doLayout();
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
            this.Reporte.setEnabled(false);
            removejtable();
            removejtable2();
//            codigoa.setText("");
//            codigoa.requestFocus();
            this.dispose();
        }
    }

    /* Para no sobrecargar la memoria y hacer una instancia cada vez que actualizamos la JTable se hace una
     * sola instancia y lo unico que se hace antes de actualizar la JTable es limpiar el modelo y enviarle los
     * nuevos datos a mostrar en la JTable  */
    public void removejtable() {
        inscritos.setText("");
        while (colegiaturas.getRowCount() != 0) {
            model.removeRow(0);

        }
    }

    public void removejtable2() {
        retirado.setText("");
        while (otrosproductos.getRowCount() != 0) {
            model2.removeRow(0);

        }
    }

    private void limpiartodo() {
        removejtable();
        removejtable2();
        //llenarcombotipopago();
        llenarcomboprofesor();
//        codigoa.setText("");
//        codigoa.requestFocus();
        //profesor.setText("");
        carrera.setText("");
        //horade.setText("");
        //horaa.setText("");
        fechainicio.setText("");
        fechafin.setText("");
        inscripcion.setValue(null);
        colegiatura.setValue(null);
        //nombrealumno.setText("");
        //beca.setText("");
        //inicioalumno.setText("");
        //dia.setText("");
        cGrupo.setSelectedIndex(0);
        Utilidades.esObligatorio(this.JPanelRecibo, false);
        Utilidades.esObligatorio(this.JPanelGrupo, false);
        Utilidades.esObligatorio(this.JPanelPago, false);
        //codigoa.requestFocus();
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
    private void grupos() {

        if (cProfesor.getSelectedIndex() != -1) {
            mProfesor prof = (mProfesor) cProfesor.getSelectedItem();
            String idprof = prof.getID();
            condicion = "p";
            llenarcombogrupo(idprof, "p");
        }
    }
    /*
     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
     *y se los envia a un metodo interno getRegistroCombo() 
     *
     */

    public static void llenarcombogrupo(String idalumn, String prof) {
        if (prof.equals("p")) {
            String Dato = "1";
            String[] campos = {"grupo.dia", "DATE_FORMAT(grupo.horariode,'%h:%i %p')", "DATE_FORMAT(grupo.horarioa,'%h:%i %p')", "grupo.idgrupo"};
            String[] condiciones = {"grupo.estado", "grupo.profesor_idcatedratico"};
            String[] Id = {Dato, idalumn};
            nombrealumno.setText("");
            estado.setText("");
            cGrupo.removeAllItems();
            //String inner = " INNER JOIN alumnosengrupo ON grupo.idgrupo = alumnosengrupo.grupo_idgrupo INNER JOIN alumno ON alumnosengrupo.alumno_idalumno = alumno.idalumno ";
            getRegistroCombo("grupo", campos, condiciones, Id, "");
        } else if (prof.equals("a")) {
            String Dato = "1";
            String[] campos = {"grupo.dia", "DATE_FORMAT(grupo.horariode,'%h:%i %p')", "DATE_FORMAT(grupo.horarioa,'%h:%i %p')", "grupo.idgrupo"};
            String[] condiciones = {"grupo.estado", "alumno.codigo"};
            String[] Id = {Dato, idalumn};
            cGrupo.removeAllItems();
            String inner = " INNER JOIN alumnosengrupo ON grupo.idgrupo = alumnosengrupo.grupo_idgrupo INNER JOIN alumno ON alumnosengrupo.alumno_idalumno = alumno.idalumno ";
            getRegistroCombo("grupo", campos, condiciones, Id, inner);
        }

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
                        modeloComboBox.addElement(new mGrupo(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3), "" + rs.getInt(4)));
                        hashGrupo.put(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3), "" + count);
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
    public void llenarcomboprofesor() {
        String Dato = "1";
        String[] campos = {"codigo", "nombre", "apellido", "idcatedratico"};
        String[] condiciones = {"estado"};
        String[] Id = {Dato};
        cProfesor.removeAllItems();
        //Component cmps = profesor;
        getRegistroCombo("profesor", campos, condiciones, Id);

    }

    /*El metodo llenarcombo() envia los parametros para la consulta a la BD y el medoto
     *getRegistroCombo() se encarga de enviarlos a la capa de AccesoDatos.getRegistros()
     *quiern devolcera un ResultSet para luego obtener los valores y agregarlos al JConboBox
     *y a una Hashtable que nos servira para obtener el id y seleccionar valores.
     */
    public void getRegistroCombo(String tabla, String[] campos, String[] campocondicion, String[] condicionid) {
        try {
            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getRegistros(tabla, campos, campocondicion, condicionid, "");

            int cantcampos = campos.length;
            if (rs != null) {

                DefaultComboBoxModel modeloComboBox;
                modeloComboBox = new DefaultComboBoxModel();
                cProfesor.setModel(modeloComboBox);

                modeloComboBox.addElement(new mProfesor("", "0"));
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    Object[] fila = new Object[cantcampos];
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        count++;
                        modeloComboBox.addElement(new mProfesor(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3), "" + rs.getInt(4)));
                        hashProfesor.put(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3), "" + count);
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

//    /*
//     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
//     *y se los envia a un metodo interno getRegistroCombo() 
//     *
//     */
//    public void llenarcombotipopago() {
//        String Dato = "1";
//        String[] campos = {"tipopago", "idtipopago"};
//        String[] condiciones = {"estado"};
//        String[] Id = {Dato};
//        //cTipopago.removeAllItems();
//        getRegistroCombotipopago("tipopago", campos, condiciones, Id);
//
//    }

    /*El metodo llenarcombo() envia los parametros para la consulta a la BD y el medoto
     *getRegistroCombo() se encarga de enviarlos a la capa de AccesoDatos.getRegistros()
     *quiern devolcera un ResultSet para luego obtener los valores y agregarlos al JConboBox
     *y a una Hashtable que nos servira para obtener el id y seleccionar valores.
     */
//    public void getRegistroCombotipopago(String tabla, String[] campos, String[] campocondicion, String[] condicionid) {
//        try {
//            ResultSet rs;
//            AccesoDatos ac = new AccesoDatos();
//
//            rs = ac.getRegistros(tabla, campos, campocondicion, condicionid, "");
//            int cantcampos = campos.length;
//            if (rs != null) {
//
//                DefaultComboBoxModel modeloComboBox;
//                modeloComboBox = new DefaultComboBoxModel();
//                cTipopago.setModel(modeloComboBox);
//
//                modeloComboBox.addElement(new mTipopago("", "0"));
//                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
//                    int count = 0;
//                    rs.beforeFirst();//regresa el puntero al primer registro
//                    Object[] fila = new Object[cantcampos];
//                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
//                        count++;
//                        modeloComboBox.addElement(new mTipopago(rs.getString(1), "" + rs.getInt(2)));
//                        hashTipopago.put(rs.getString(1), "" + count);
//                    }
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Error", JOptionPane.INFORMATION_MESSAGE);
//            }
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }

    /* Metodo que llena los campos con la información de grupo
     * Tambien llena en la pestaña de coelgiatura lo que el alumno tiene pendiente de pago
     */
    public void selecciongrupo() {
        if (cGrupo.getSelectedIndex() == 0) {
            //profesor.setText("");
            carrera.setText("");
            //horade.setText("");
            //horaa.setText("");
            fechainicio.setText("");
            fechafin.setText("");
            inscripcion.setValue(null);
            colegiatura.setValue(null);
            removejtable2();
            removejtable();

            //sumartotal();
            //inicioalumno.setText("");
            //beca.setText("");
            //dia.setText("");
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
                                //profesor.setText(rs.getString(1));
                                carrera.setText(rs.getString(2));
                                //horade.setText(rs.getString(3));
                                //horaa.setText(rs.getString(4));
                                fechainicio.setText((rs.getString(5)));
                                fechafin.setText((rs.getString(6)));
                                inscripcion.setValue(rs.getFloat(7));
                                colegiatura.setValue(rs.getFloat(8));
                                //dia.setText(rs.getString(9));
                            }
                            //idalumnosengrupo(idalumno, "" + grup.getID());
                            MostrarPagos(condicion);
                            MostrarProductos(condicion);
                            //sumartotal();
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
            //nombrealumno.setText("");
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
                                //codigoa.setText(rs.getString(1));
                                cProfesor.setSelectedIndex(0);
                                condicion = "a";
                                llenarcombogrupo(rs.getString(1), condicion);
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
    private void MostrarPagos(String condicion) {

        if (condicion.equals("p")) {
            mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
            String id = grup.getID();
            String sql = "SELECT alumno.idalumno,alumno.codigo,alumno.nombres,alumno.apellidos,alumnosengrupo.beca FROM alumno INNER JOIN alumnosengrupo ON alumno.idalumno = alumnosengrupo.alumno_idalumno where alumno.estado='1' and alumnosengrupo.grupo_idgrupo=" + id;

            removejtable();
            model = getRegistroPorLikel(model, sql);

        } else if (condicion.equals("a")) {

            //String id = codigoa.getText();
            String sql = "SELECT alumno.idalumno,alumno.codigo,alumno.nombres,alumno.apellidos,alumnosengrupo.beca FROM alumno INNER JOIN alumnosengrupo ON alumno.idalumno = alumnosengrupo.alumno_idalumno where alumno.estado='1' and alumno.idalumno='" + idalumno + "'";
            removejtable();
            model = getRegistroPorLikel(model, sql);
        }

        Utilidades.ajustarAnchoColumnas(colegiaturas);
        inscritos.setText("" + colegiaturas.getRowCount());
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
            int cantcampos = 6;
            //if (rs != null) {
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                //int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos];
                int count = 1;
                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
                    for (int i = 0; i < cantcampos; i++) {

                        //if (i == 1) {
                        fila[0] = rs.getObject(1);
                        fila[1] = count;
                        fila[2] = rs.getObject(2);
                        fila[3] = rs.getObject(3);
                        fila[4] = rs.getObject(4);
                        fila[5] = rs.getObject(5);

                        //} else {
                        //fila[i] = rs.getObject(i + 1); // El primer indice en rs es el 1, no el cero, por eso se suma 1.
                        //}
//                        if (fila[i] == null) {
//                            fila[i] = "";
//                        } else {
//                        }
                    }
                    modelo.addRow(fila);
                    count++;
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

    private void MostrarProductos(String condicion) {

        if (condicion.equals("p")) {
            mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
            String id = grup.getID();

            String sql = "SELECT alumno.idalumno,alumno.codigo,alumno.nombres,alumno.apellidos,alumnosengrupo.beca FROM alumno INNER JOIN alumnosengrupo ON alumno.idalumno = alumnosengrupo.alumno_idalumno where alumno.estado='0' and alumnosengrupo.grupo_idgrupo=" + id;
            //String sql = "SELECT otrospagos.idpago,otrospagos.descripcion,otrospagos.costo FROM otrospagos order by otrospagos.descripcion";

            removejtable2();
            model2 = getRegistroPorLikell(model2, sql);
        } else if (condicion.equals("a")) {
            mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
            String id = grup.getID();

            String sql = "SELECT alumno.idalumno,alumno.codigo,alumno.nombres,alumno.apellidos,alumnosengrupo.beca FROM alumno INNER JOIN alumnosengrupo ON alumno.idalumno = alumnosengrupo.alumno_idalumno where alumno.estado='0' and alumno.codigo=" + id;
            //String sql = "SELECT otrospagos.idpago,otrospagos.descripcion,otrospagos.costo FROM otrospagos order by otrospagos.descripcion";

            removejtable2();
            model2 = getRegistroPorLikell(model2, sql);
        }

        Utilidades.ajustarAnchoColumnas(otrosproductos);
        retirado.setText("" + otrosproductos.getRowCount());
        otrosproductos.getColumnModel().getColumn(0).setMaxWidth(0);
        otrosproductos.getColumnModel().getColumn(0).setMinWidth(0);
        otrosproductos.getColumnModel().getColumn(0).setPreferredWidth(0);
        otrosproductos.doLayout();
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
                int count = 1;
                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    for (int i = 0; i < cantcampos; i++) {
                        fila[0] = rs.getObject(1);
                        fila[1] = count;
                        fila[2] = rs.getObject(2);
                        fila[3] = rs.getObject(3);
                        fila[4] = rs.getObject(4);
                        fila[5] = rs.getObject(5);
                    }
                    modelo.addRow(fila);
                    count++;
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
                        //beca.setText("" + becac);
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
        Reporte = new elaprendiz.gui.button.ButtonRect();
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        Reporte1 = new elaprendiz.gui.button.ButtonRect();
        Reporte2 = new elaprendiz.gui.button.ButtonRect();
        JPanelGrupo = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cGrupo = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        carrera = new elaprendiz.gui.textField.TextField();
        inscripcion = new javax.swing.JFormattedTextField();
        colegiatura = new javax.swing.JFormattedTextField();
        fechainicio = new elaprendiz.gui.textField.TextField();
        fechafin = new elaprendiz.gui.textField.TextField();
        jLabel4 = new javax.swing.JLabel();
        cProfesor = new javax.swing.JComboBox();
        rbNombre = new javax.swing.JRadioButton();
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
        rbCodigo = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        tbPane1 = new elaprendiz.gui.panel.TabbedPaneHeader();
        JPanelPago = new javax.swing.JPanel();
        retirado = new javax.swing.JFormattedTextField();
        jLabel27 = new javax.swing.JLabel();
        inscritos = new javax.swing.JFormattedTextField();
        jLabel28 = new javax.swing.JLabel();
        JPanelRecibo = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        clockDigital2 = new elaprendiz.gui.varios.ClockDigital();
        ciclo = new elaprendiz.gui.textField.TextField();
        jLabel17 = new javax.swing.JLabel();
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
        setTitle("Reporte Alumnos");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Listado de Alumnos"); // NOI18N
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

        Reporte.setBackground(new java.awt.Color(51, 153, 255));
        Reporte.setMnemonic(KeyEvent.VK_A);
        Reporte.setText("Alumnos Activos");
        Reporte.setName("Generar Reporte ListadoAlumnos"); // NOI18N
        Reporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReporteActionPerformed(evt);
            }
        });

        bntCancelar.setBackground(new java.awt.Color(51, 153, 255));
        bntCancelar.setMnemonic(KeyEvent.VK_X);
        bntCancelar.setText("Cancelar");
        bntCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntCancelarActionPerformed(evt);
            }
        });

        bntSalir.setBackground(new java.awt.Color(51, 153, 255));
        bntSalir.setText("Salir    ");
        bntSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntSalirActionPerformed(evt);
            }
        });

        Reporte1.setBackground(new java.awt.Color(51, 153, 255));
        Reporte1.setMnemonic(KeyEvent.VK_M);
        Reporte1.setText("Reporte Mineduc");
        Reporte1.setName("Reporte Mineduc Alta ListadoAlumnos"); // NOI18N
        Reporte1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Reporte1ActionPerformed(evt);
            }
        });

        Reporte2.setBackground(new java.awt.Color(51, 153, 255));
        Reporte2.setMnemonic(KeyEvent.VK_B);
        Reporte2.setText("Alumnos de Baja");
        Reporte2.setName("Reporte Mineduc Baja ListadoAlumnos"); // NOI18N
        Reporte2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Reporte2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlActionButtonsLayout = new javax.swing.GroupLayout(pnlActionButtons);
        pnlActionButtons.setLayout(pnlActionButtonsLayout);
        pnlActionButtonsLayout.setHorizontalGroup(
            pnlActionButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlActionButtonsLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(Reporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(Reporte1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Reporte2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bntCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(bntSalir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlActionButtonsLayout.setVerticalGroup(
            pnlActionButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlActionButtonsLayout.createSequentialGroup()
                .addGroup(pnlActionButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlActionButtonsLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(Reporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlActionButtonsLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(Reporte1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlActionButtonsLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(pnlActionButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bntCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Reporte2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlActionButtonsLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(bntSalir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11))
        );

        panelImage.add(pnlActionButtons);
        pnlActionButtons.setBounds(0, 580, 880, 50);

        JPanelGrupo.setBackground(java.awt.SystemColor.activeCaption);
        JPanelGrupo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelGrupo.setForeground(new java.awt.Color(204, 204, 204));
        JPanelGrupo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JPanelGrupo.setLayout(null);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Fecha Fin:");
        JPanelGrupo.add(jLabel6);
        jLabel6.setBounds(730, 60, 110, 20);

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
        cGrupo.setBounds(90, 80, 210, 24);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Grupo:");
        JPanelGrupo.add(jLabel7);
        jLabel7.setBounds(20, 80, 60, 27);

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

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Profesor:");
        JPanelGrupo.add(jLabel4);
        jLabel4.setBounds(10, 40, 70, 24);

        cProfesor.setEditable(true);
        cProfesor.setModel(modelCombo = new DefaultComboBoxModel());
        cProfesor.setName("Profesor"); // NOI18N
        JPanelGrupo.add(cProfesor);
        cProfesor.setBounds(90, 40, 210, 24);

        rbNombre.setBackground(java.awt.SystemColor.activeCaption);
        rbNombre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        rbNombre.setForeground(new java.awt.Color(0, 102, 102));
        rbNombre.setSelected(true);
        rbNombre.setText("Buscar por Profesor");
        rbNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNombreActionPerformed(evt);
            }
        });
        JPanelGrupo.add(rbNombre);
        rbNombre.setBounds(2, 2, 240, 25);

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

            tbPane.addTab("Inscritos", jPanel3);

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
                otrosproductos.setName("otrosproductos"); // NOI18N
                otrosproductos.setOpaque(false);
                otrosproductos.setRowHeight(20);
                jScrollPane2.setViewportView(otrosproductos);

                jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 756, 210));

                tbPane.addTab("Retirados", jPanel4);

                JPanelTable.add(tbPane, java.awt.BorderLayout.CENTER);

                panelImage.add(JPanelTable);
                JPanelTable.setBounds(0, 330, 760, 250);

                JPanelBusqueda.setBackground(java.awt.SystemColor.inactiveCaption);
                JPanelBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                JPanelBusqueda.setLayout(null);

                codigoa.setEditable(false);
                codigoa.setEnabled(false);
                codigoa.setPreferredSize(new java.awt.Dimension(250, 27));
                codigoa.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        codigoaActionPerformed(evt);
                    }
                });
                JPanelBusqueda.add(codigoa);
                codigoa.setBounds(120, 30, 97, 24);

                jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                jLabel16.setText("Codigo:");
                JPanelBusqueda.add(jLabel16);
                jLabel16.setBounds(10, 30, 100, 24);

                jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar.png"))); // NOI18N
                jButton1.setEnabled(false);
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton1ActionPerformed(evt);
                    }
                });
                JPanelBusqueda.add(jButton1);
                jButton1.setBounds(220, 30, 20, 27);

                nombrealumno.setEditable(false);
                nombrealumno.setPreferredSize(new java.awt.Dimension(250, 27));
                JPanelBusqueda.add(nombrealumno);
                nombrealumno.setBounds(440, 30, 360, 24);

                jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                jLabel19.setText("Alumno:");
                JPanelBusqueda.add(jLabel19);
                jLabel19.setBounds(310, 30, 120, 24);

                estado.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
                estado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                JPanelBusqueda.add(estado);
                estado.setBounds(260, 30, 110, 27);

                rbCodigo.setBackground(java.awt.SystemColor.inactiveCaption);
                rbCodigo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                rbCodigo.setForeground(new java.awt.Color(0, 102, 102));
                rbCodigo.setText("Buscar por Alumno");
                rbCodigo.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        rbCodigoActionPerformed(evt);
                    }
                });
                JPanelBusqueda.add(rbCodigo);
                rbCodigo.setBounds(2, 2, 240, 25);

                panelImage.add(JPanelBusqueda);
                JPanelBusqueda.setBounds(0, 90, 880, 70);

                jPanel1.setBackground(new java.awt.Color(51, 51, 51));
                jPanel1.setLayout(new java.awt.BorderLayout());

                tbPane1.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
                tbPane1.setOpaque(true);

                JPanelPago.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                JPanelPago.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                retirado.setEditable(false);
                retirado.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
                retirado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                retirado.setToolTipText("");
                retirado.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
                retirado.setPreferredSize(new java.awt.Dimension(80, 23));
                JPanelPago.add(retirado, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 130, 105, 30));

                jLabel27.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
                jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel27.setText("Retirados");
                jLabel27.setOpaque(true);
                JPanelPago.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 110, 105, 20));

                inscritos.setEditable(false);
                inscritos.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
                inscritos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                inscritos.setToolTipText("");
                inscritos.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
                inscritos.setPreferredSize(new java.awt.Dimension(80, 23));
                JPanelPago.add(inscritos, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 60, 105, 30));

                jLabel28.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
                jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel28.setText("Inscritos");
                jLabel28.setOpaque(true);
                JPanelPago.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 40, 105, 20));

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
                jLabel21.setBounds(690, 0, 100, 19);

                clockDigital2.setForeground(new java.awt.Color(255, 255, 255));
                clockDigital2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
                JPanelRecibo.add(clockDigital2);
                clockDigital2.setBounds(690, 20, 100, 27);

                ciclo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                ciclo.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
                ciclo.setName("ciclo"); // NOI18N
                ciclo.setPreferredSize(new java.awt.Dimension(250, 27));
                ciclo.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        cicloActionPerformed(evt);
                    }
                });
                JPanelRecibo.add(ciclo);
                ciclo.setBounds(120, 10, 97, 24);

                jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                jLabel17.setText("Ciclo:");
                JPanelRecibo.add(jLabel17);
                jLabel17.setBounds(10, 10, 100, 24);

                panelImage.add(JPanelRecibo);
                JPanelRecibo.setBounds(0, 40, 880, 50);

                pnlPaginador1.setBackground(new java.awt.Color(57, 104, 163));
                pnlPaginador1.setPreferredSize(new java.awt.Dimension(786, 40));
                pnlPaginador1.setLayout(new java.awt.GridBagLayout());

                jLabel11.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
                jLabel11.setForeground(new java.awt.Color(255, 255, 255));
                jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/alumno.png"))); // NOI18N
                jLabel11.setText("<--Reporte de Alumnos-->");
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
        // llenarcombotipopago();
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        BuscarAlumnoLista frmBuscarAlumnoLista = new BuscarAlumnoLista();
        if (frmBuscarAlumnoLista == null) {
            frmBuscarAlumnoLista = new BuscarAlumnoLista();
        }
        adminInternalFrame(dp, frmBuscarAlumnoLista);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void codigoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoaActionPerformed
        // TODO add your handling code here:
        balumnocodigo(codigoa.getText());
    }//GEN-LAST:event_codigoaActionPerformed

    private void rbCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCodigoActionPerformed
        // TODO add your handling code here:
        rbNombre.setSelected(false);
//        rbApellido.setSelected(false);
//        busqueda.requestFocus();
        cProfesor.setEnabled(false);
        codigoa.setEditable(true);
        codigoa.setEnabled(true);

        jButton1.setEnabled(true);
        //cProfesor.setSelectedIndex(-1);
        //cGrupo.removeAllItems();
        limpiartodo();
    }//GEN-LAST:event_rbCodigoActionPerformed

    private void rbNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNombreActionPerformed
        // TODO add your handling code here:
        rbCodigo.setSelected(false);
        codigoa.setEnabled(false);
//        rbApellido.setSelected(false);
//        busqueda.requestFocus();
        cProfesor.setEnabled(true);
        codigoa.setEditable(false);
        codigoa.setText("");
        estado.setText("");
        jButton1.setEnabled(false);
    }//GEN-LAST:event_rbNombreActionPerformed

    private void ReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReporteActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(Reporte.getName()) == true) {

            if (Utilidades.esObligatorio(this.JPanelRecibo, true)) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (cGrupo.getSelectedIndex() == -1 || cGrupo.getSelectedIndex() == 0) {
                JOptionPane.showInternalMessageDialog(this, "Debe seleccionar un Grupo");
            } else if (cGrupo.getSelectedIndex() != -1) {
                mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
                String id = grup.getID();
                ListadoAlumnosGrupo.ReporteGrupo(id, ciclo.getText());
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_ReporteActionPerformed

    private void cicloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cicloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cicloActionPerformed

    private void Reporte1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Reporte1ActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(Reporte1.getName()) == true) {
            if (cGrupo.getSelectedIndex() == -1 || cGrupo.getSelectedIndex() == 0) {
                JOptionPane.showInternalMessageDialog(this, "Debe seleccionar un Grupo");
            } else if (cGrupo.getSelectedIndex() != -1) {
                mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
                String id = grup.getID();
                //Cambiar por la nueva clase del reporte Mineduc
                ListadoAlumnosGrupoMineduc.ReporteGrupo(id, "1");
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_Reporte1ActionPerformed

    private void Reporte2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Reporte2ActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(Reporte2.getName()) == true) {
            if (cGrupo.getSelectedIndex() == -1 || cGrupo.getSelectedIndex() == 0) {
                JOptionPane.showInternalMessageDialog(this, "Debe seleccionar un Grupo");
            } else if (cGrupo.getSelectedIndex() != -1) {
                mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
                String id = grup.getID();
                //Cambiar por la nueva clase del reporte Mineduc
                ListadoAlumnosGrupoMineduc.ReporteGrupo(id, "2");
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_Reporte2ActionPerformed


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
    private elaprendiz.gui.button.ButtonRect Reporte;
    private elaprendiz.gui.button.ButtonRect Reporte1;
    private elaprendiz.gui.button.ButtonRect Reporte2;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    public static javax.swing.JComboBox cGrupo;
    public static javax.swing.JComboBox cProfesor;
    private elaprendiz.gui.textField.TextField carrera;
    public static elaprendiz.gui.textField.TextField ciclo;
    private elaprendiz.gui.varios.ClockDigital clockDigital2;
    public static elaprendiz.gui.textField.TextField codigoa;
    private javax.swing.JFormattedTextField colegiatura;
    private javax.swing.JTable colegiaturas;
    public static javax.swing.JLabel estado;
    private elaprendiz.gui.textField.TextField fechafin;
    private elaprendiz.gui.textField.TextField fechainicio;
    private javax.swing.JFormattedTextField inscripcion;
    private javax.swing.JFormattedTextField inscritos;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JRadioButton rbCodigo;
    private javax.swing.JRadioButton rbNombre;
    private javax.swing.JFormattedTextField retirado;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane1;
    // End of variables declaration//GEN-END:variables
}
