/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Capa_Presentacion;

import Capa_Datos.AccesoDatos;
import Capa_Datos.BdConexion;
import Capa_Negocio.AccesoUsuario;
import static Capa_Negocio.AddForms.adminInternalFrame;
import Capa_Negocio.FormatoDecimal;
import Capa_Negocio.FormatoFecha;
import Capa_Negocio.Peticiones;
import Capa_Negocio.Utilidades;
import static Capa_Presentacion.Principal.dp;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import modelos.mGrupo;
import modelos.mGrupo1;
import modelos.mProfesor;
import modelos.mProfesor1;
import Reportes.ListadoAlumnosGrupo;

/**
 *
 * @author GLARA
 */
public class FusionDeGrupos extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model, model2;
    DefaultComboBoxModel modelCombo;
    String[] titulos = {"Id", "No.", "Codigo", "Nombres", "Apellidos", "Beca", "iddetallegrupo"};//Titulos para Jtabla
    String[] titulos2 = {"Id", "No.", "Codigo", "Nombres", "Apellidos", "Beca"};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();
    public static Hashtable<String, String> hashGrupo = new Hashtable<>();
    public static Hashtable<String, String> hashGrupo1 = new Hashtable<>();
    public static Hashtable<String, String> hashTipopago = new Hashtable<>();
    public Hashtable<String, String> hashProfesor = new Hashtable<>();
    public Hashtable<String, String> hashProfesor1 = new Hashtable<>();
    String condicion = "";
    AccesoDatos acceso = new AccesoDatos();
    static String idalumno = "", iddetallegrupo = "";
    java.sql.Connection conn;//getConnection intentara establecer una conexión.

    /**
     * Creates new form Cliente
     */
    public FusionDeGrupos() {
        initComponents();
        //setFiltroTexto();
        addEscapeKey();
        llenarcomboprofesor();
        llenarcomboprofesor2();

        cGrupo.addItemListener(
                (ItemEvent e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        selecciongrupo();
                    }
                });

        cGrupo1.addItemListener(
                (ItemEvent e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        selecciongrupo2();
                    }
                });

        cProfesor.addItemListener(
                (ItemEvent e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        grupos();

                    }
                });

        cProfesor1.addItemListener(
                (ItemEvent e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        grupos2();

                    }
                });

        Grupo.getColumnModel().getColumn(0).setMaxWidth(0);
        Grupo.getColumnModel().getColumn(0).setMinWidth(0);
        Grupo.getColumnModel().getColumn(0).setPreferredWidth(0);
        Grupo.getColumnModel().getColumn(6).setMaxWidth(0);
        Grupo.getColumnModel().getColumn(6).setMinWidth(0);
        Grupo.getColumnModel().getColumn(6).setPreferredWidth(0);
        Grupo.doLayout();
        Grupo2.getColumnModel().getColumn(0).setMaxWidth(0);
        Grupo2.getColumnModel().getColumn(0).setMinWidth(0);
        Grupo2.getColumnModel().getColumn(0).setPreferredWidth(0);
        Grupo2.doLayout();
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
            //this.Reporte.setEnabled(false);
            removejtable();
            removejtable2();
            this.dispose();
        }
    }

    /* Para no sobrecargar la memoria y hacer una instancia cada vez que actualizamos la JTable se hace una
     * sola instancia y lo unico que se hace antes de actualizar la JTable es limpiar el modelo y enviarle los
     * nuevos datos a mostrar en la JTable  */
    public void removejtable() {
        while (Grupo.getRowCount() != 0) {
            model.removeRow(0);

        }
    }

    public void removejtable2() {
        while (Grupo2.getRowCount() != 0) {
            model2.removeRow(0);

        }
    }

    private void limpiartodo() {
        removejtable();
        removejtable2();
        llenarcomboprofesor();
        carrera.setText("");
        fechainicio.setText("");
        fechafin.setText("");
        inscripcion.setValue(null);
        colegiatura.setValue(null);
        cGrupo.setSelectedIndex(0);
        cGrupo1.setSelectedIndex(0);
        Utilidades.esObligatorio(this.JPanelGrupo, false);
    }

    private void grupos() {

        if (cProfesor.getSelectedIndex() != -1) {
            mProfesor prof = (mProfesor) cProfesor.getSelectedItem();
            String idprof = prof.getID();
            condicion = "1";
            llenarcombogrupo(idprof, condicion);
        }
    }

    private void grupos2() {
        if (cProfesor1.getSelectedIndex() != -1) {
            mProfesor1 prof = (mProfesor1) cProfesor1.getSelectedItem();
            String idprof = prof.getID();
            condicion = "2";
            llenarcombogrupo(idprof, condicion);
        }
    }

    /*
     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
     *y se los envia a un metodo interno getRegistroCombo() 
     *
     */
    public static void llenarcombogrupo(String idalumn, String prof) {
        if (prof.equals("1")) {
            String Dato = "1";
            String[] campos = {"grupo.dia", "DATE_FORMAT(grupo.horariode,'%h:%i %p')", "DATE_FORMAT(grupo.horarioa,'%h:%i %p')", "grupo.idgrupo"};
            String[] condiciones = {"grupo.estado", "grupo.profesor_idcatedratico"};
            String[] Id = {Dato, idalumn};
            cGrupo.removeAllItems();
            getRegistroCombo("grupo", campos, condiciones, Id, "");
        } else if (prof.equals("2")) {
            String Dato = "1";
            String[] campos = {"grupo.dia", "DATE_FORMAT(grupo.horariode,'%h:%i %p')", "DATE_FORMAT(grupo.horarioa,'%h:%i %p')", "grupo.idgrupo"};
            String[] condiciones = {"grupo.estado", "grupo.profesor_idcatedratico"};
            String[] Id = {Dato, idalumn};
            cGrupo1.removeAllItems();
            getRegistroCombo2("grupo", campos, condiciones, Id, "");
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

    /*El metodo llenarcombo() envia los parametros para la consulta a la BD y el medoto
     *getRegistroCombo() se encarga de enviarlos a la capa de AccesoDatos.getRegistros()
     *quiern devolcera un ResultSet para luego obtener los valores y agregarlos al JConboBox
     *y a una Hashtable que nos servira para obtener el id y seleccionar valores.
     */
    public static void getRegistroCombo2(String tabla, String[] campos, String[] campocondicion, String[] condicionid, String inner) {
        try {
            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getRegistros(tabla, campos, campocondicion, condicionid, inner);

            int cantcampos = campos.length;
            if (rs != null) {

                DefaultComboBoxModel modeloComboBox;
                modeloComboBox = new DefaultComboBoxModel();
                cGrupo1.setModel(modeloComboBox);

                modeloComboBox.addElement(new mGrupo1("", "0"));
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        count++;
                        modeloComboBox.addElement(new mGrupo1(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3), "" + rs.getInt(4)));
                        hashGrupo1.put(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3), "" + count);
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

    /*
     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
     *y se los envia a un metodo interno getRegistroCombo() 
     *
     */
    public void llenarcomboprofesor2() {
        String Dato = "1";
        String[] campos = {"codigo", "nombre", "apellido", "idcatedratico"};
        String[] condiciones = {"estado"};
        String[] Id = {Dato};
        cProfesor1.removeAllItems();
        //Component cmps = profesor;
        getRegistroCombo2("profesor", campos, condiciones, Id);

    }

    /*El metodo llenarcombo() envia los parametros para la consulta a la BD y el medoto
     *getRegistroCombo() se encarga de enviarlos a la capa de AccesoDatos.getRegistros()
     *quiern devolcera un ResultSet para luego obtener los valores y agregarlos al JConboBox
     *y a una Hashtable que nos servira para obtener el id y seleccionar valores.
     */
    public void getRegistroCombo2(String tabla, String[] campos, String[] campocondicion, String[] condicionid) {
        try {
            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getRegistros(tabla, campos, campocondicion, condicionid, "");

            int cantcampos = campos.length;
            if (rs != null) {

                DefaultComboBoxModel modeloComboBox;
                modeloComboBox = new DefaultComboBoxModel();
                cProfesor1.setModel(modeloComboBox);

                modeloComboBox.addElement(new mProfesor1("", "0"));
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    Object[] fila = new Object[cantcampos];
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        count++;
                        modeloComboBox.addElement(new mProfesor1(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3), "" + rs.getInt(4)));
                        hashProfesor1.put(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3), "" + count);
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

    /* Metodo que llena los campos con la información de grupo
     * Tambien llena en la pestaña de coelgiatura lo que el alumno tiene pendiente de pago
     */
    public void selecciongrupo() {
        if (cGrupo.getSelectedIndex() == 0) {
            carrera.setText("");
            fechainicio.setText("");
            fechafin.setText("");
            inscripcion.setValue(null);
            colegiatura.setValue(null);
            removejtable();
            Utilidades.esObligatorio(this.JPanelGrupo, false);
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
                                carrera.setText(rs.getString(2));
                                fechainicio.setText((rs.getString(5)));
                                fechafin.setText((rs.getString(6)));
                                inscripcion.setValue(rs.getFloat(7));
                                colegiatura.setValue(rs.getFloat(8));
                            }
                            MostrarPagos(condicion);
                            Utilidades.esObligatorio(this.JPanelGrupo, false);
                        }
                    } catch (SQLException e) {
                        JOptionPane.showInternalMessageDialog(this, e);
                    }
                }

            }
        }
    }

    /* Metodo que llena los campos con la información de grupo
     * Tambien llena en la pestaña de coelgiatura lo que el alumno tiene pendiente de pago
     */
    public void selecciongrupo2() {
        if (cGrupo1.getSelectedIndex() == 0) {
            carrera1.setText("");
            fechainicio1.setText("");
            fechafin1.setText("");
            inscripcion1.setValue(null);
            colegiatura1.setValue(null);
            removejtable2();
            Utilidades.esObligatorio(this.JPanelGrupo, false);
        } else if (cGrupo1.getSelectedIndex() != -1) {

            mGrupo1 grup = (mGrupo1) cGrupo1.getSelectedItem();
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
                                carrera1.setText(rs.getString(2));
                                fechainicio1.setText((rs.getString(5)));
                                fechafin1.setText((rs.getString(6)));
                                inscripcion1.setValue(rs.getFloat(7));
                                colegiatura1.setValue(rs.getFloat(8));
                            }
                            MostrarPagos(condicion);
                            Utilidades.esObligatorio(this.JPanelGrupo, false);
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
                                cProfesor.setSelectedIndex(0);
                                condicion = "a";
                                llenarcombogrupo(rs.getString(1), condicion);
                                if (rs.getString(5).equals("0")) {
                                } else if (rs.getString(5).equals("1")) {
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

        if (condicion.equals("1")) {
            mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
            String id = grup.getID();
            String sql = "SELECT alumno.idalumno,alumno.codigo,alumno.nombres,alumno.apellidos,alumnosengrupo.beca,alumnosengrupo.iddetallegrupo FROM alumno INNER JOIN alumnosengrupo ON alumno.idalumno = alumnosengrupo.alumno_idalumno where alumno.estado='1' and alumnosengrupo.grupo_idgrupo=" + id;

            removejtable();
            model = getRegistroPorLikel(model, sql);
            Utilidades.ajustarAnchoColumnas(Grupo);

        } else if (condicion.equals("2")) {

            mGrupo1 grup = (mGrupo1) cGrupo1.getSelectedItem();
            String id = grup.getID();
            String sql = "SELECT alumno.idalumno,alumno.codigo,alumno.nombres,alumno.apellidos,alumnosengrupo.beca FROM alumno INNER JOIN alumnosengrupo ON alumno.idalumno = alumnosengrupo.alumno_idalumno where alumno.estado='1' and alumnosengrupo.grupo_idgrupo=" + id;

            removejtable2();
            model2 = getRegistroPorLikell(model2, sql);
            Utilidades.ajustarAnchoColumnas(Grupo2);
        }
        Grupo.getColumnModel().getColumn(0).setMaxWidth(0);
        Grupo.getColumnModel().getColumn(0).setMinWidth(0);
        Grupo.getColumnModel().getColumn(0).setPreferredWidth(0);
        Grupo.getColumnModel().getColumn(6).setMaxWidth(0);
        Grupo.getColumnModel().getColumn(6).setMinWidth(0);
        Grupo.getColumnModel().getColumn(6).setPreferredWidth(0);
        Grupo.doLayout();
        Grupo2.getColumnModel().getColumn(0).setMaxWidth(0);
        Grupo2.getColumnModel().getColumn(0).setMinWidth(0);
        Grupo2.getColumnModel().getColumn(0).setPreferredWidth(0);
        Grupo2.doLayout();
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
            int cantcampos = 7;
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
                        fila[6] = rs.getObject(6);
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

//    /* Este metodo se encarga de filtrar los datos que se deben ingresar en cada uno de los campos del formulario
//     * podemos indicar que el usuario ingrese solo numeros , solo letras, numeros y letras, o cualquier caracter
//     * tambien podemos validar si se aseptaran espacios en blanco en la cadena ingresada , para mas detalle visualizar
//     * la clase TipoFiltro()  */
//    private void setFiltroTexto() {
//    }
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
        java.awt.GridBagConstraints gridBagConstraints;

        panelImage = new elaprendiz.gui.panel.PanelImage();
        pnlActionButtons = new javax.swing.JPanel();
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        bntGuardar = new elaprendiz.gui.button.ButtonRect();
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
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cGrupo1 = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        carrera1 = new elaprendiz.gui.textField.TextField();
        inscripcion1 = new javax.swing.JFormattedTextField();
        colegiatura1 = new javax.swing.JFormattedTextField();
        fechainicio1 = new elaprendiz.gui.textField.TextField();
        fechafin1 = new elaprendiz.gui.textField.TextField();
        jLabel14 = new javax.swing.JLabel();
        cProfesor1 = new javax.swing.JComboBox();
        JPanelTable = new javax.swing.JPanel();
        tbPane = new elaprendiz.gui.panel.TabbedPaneHeader();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        Grupo = new javax.swing.JTable();
        JPanelTable1 = new javax.swing.JPanel();
        tbPane2 = new elaprendiz.gui.panel.TabbedPaneHeader();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        Grupo2 = new javax.swing.JTable();
        pnlPaginador1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Fusión de Grupos");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("FusionDeGrupos"); // NOI18N
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

        bntGuardar.setBackground(new java.awt.Color(51, 153, 255));
        bntGuardar.setMnemonic(KeyEvent.VK_F);
        bntGuardar.setText("Fusionar");
        bntGuardar.setName("Fusionar Fusion de Grupos"); // NOI18N
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

        panelImage.add(pnlActionButtons);
        pnlActionButtons.setBounds(0, 530, 880, 50);

        JPanelGrupo.setBackground(java.awt.SystemColor.activeCaption);
        JPanelGrupo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelGrupo.setForeground(new java.awt.Color(204, 204, 204));
        JPanelGrupo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JPanelGrupo.setLayout(null);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Fecha Fin:");
        JPanelGrupo.add(jLabel6);
        jLabel6.setBounds(230, 50, 80, 20);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Fecha Inicio:");
        JPanelGrupo.add(jLabel9);
        jLabel9.setBounds(20, 50, 110, 20);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Carrera:");
        JPanelGrupo.add(jLabel5);
        jLabel5.setBounds(20, 20, 110, 20);

        cGrupo.setEditable(true);
        cGrupo.setName("grupo"); // NOI18N
        JPanelGrupo.add(cGrupo);
        cGrupo.setBounds(140, 160, 270, 24);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Grupo:");
        JPanelGrupo.add(jLabel7);
        jLabel7.setBounds(20, 160, 70, 27);

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText("Inscripción Q.");
        JPanelGrupo.add(jLabel24);
        jLabel24.setBounds(20, 80, 120, 20);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Colegiatura Q.");
        JPanelGrupo.add(jLabel18);
        jLabel18.setBounds(220, 80, 110, 20);

        carrera.setEditable(false);
        carrera.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        carrera.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(carrera);
        carrera.setBounds(140, 20, 270, 24);

        inscripcion.setEditable(false);
        inscripcion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        inscripcion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inscripcion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        inscripcion.setPreferredSize(new java.awt.Dimension(80, 23));
        JPanelGrupo.add(inscripcion);
        inscripcion.setBounds(140, 80, 80, 24);

        colegiatura.setEditable(false);
        colegiatura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        colegiatura.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        colegiatura.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        colegiatura.setPreferredSize(new java.awt.Dimension(80, 23));
        JPanelGrupo.add(colegiatura);
        colegiatura.setBounds(330, 80, 80, 24);

        fechainicio.setEditable(false);
        fechainicio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechainicio.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(fechainicio);
        fechainicio.setBounds(140, 50, 80, 24);

        fechafin.setEditable(false);
        fechafin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechafin.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(fechafin);
        fechafin.setBounds(330, 50, 80, 24);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Profesor:");
        JPanelGrupo.add(jLabel4);
        jLabel4.setBounds(20, 130, 70, 24);

        cProfesor.setEditable(true);
        cProfesor.setModel(modelCombo = new DefaultComboBoxModel());
        cProfesor.setName("Profesor"); // NOI18N
        JPanelGrupo.add(cProfesor);
        cProfesor.setBounds(140, 130, 270, 24);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Fecha Fin:");
        JPanelGrupo.add(jLabel8);
        jLabel8.setBounds(670, 50, 80, 20);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Fecha Inicio:");
        JPanelGrupo.add(jLabel10);
        jLabel10.setBounds(460, 50, 110, 20);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Carrera:");
        JPanelGrupo.add(jLabel12);
        jLabel12.setBounds(460, 20, 110, 20);

        cGrupo1.setEditable(true);
        cGrupo1.setName("grupo"); // NOI18N
        JPanelGrupo.add(cGrupo1);
        cGrupo1.setBounds(580, 160, 270, 24);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Grupo:");
        JPanelGrupo.add(jLabel13);
        jLabel13.setBounds(460, 160, 70, 27);

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("Inscripción Q.");
        JPanelGrupo.add(jLabel25);
        jLabel25.setBounds(460, 80, 120, 20);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Colegiatura Q.");
        JPanelGrupo.add(jLabel19);
        jLabel19.setBounds(660, 80, 110, 20);

        carrera1.setEditable(false);
        carrera1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        carrera1.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(carrera1);
        carrera1.setBounds(580, 20, 270, 24);

        inscripcion1.setEditable(false);
        inscripcion1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        inscripcion1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inscripcion1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        inscripcion1.setPreferredSize(new java.awt.Dimension(80, 23));
        JPanelGrupo.add(inscripcion1);
        inscripcion1.setBounds(580, 80, 80, 24);

        colegiatura1.setEditable(false);
        colegiatura1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        colegiatura1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        colegiatura1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        colegiatura1.setPreferredSize(new java.awt.Dimension(80, 23));
        JPanelGrupo.add(colegiatura1);
        colegiatura1.setBounds(770, 80, 80, 24);

        fechainicio1.setEditable(false);
        fechainicio1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechainicio1.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(fechainicio1);
        fechainicio1.setBounds(580, 50, 80, 24);

        fechafin1.setEditable(false);
        fechafin1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechafin1.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(fechafin1);
        fechafin1.setBounds(770, 50, 80, 24);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Profesor:");
        JPanelGrupo.add(jLabel14);
        jLabel14.setBounds(460, 130, 70, 24);

        cProfesor1.setEditable(true);
        cProfesor1.setModel(modelCombo = new DefaultComboBoxModel());
        cProfesor1.setName("Profesor"); // NOI18N
        JPanelGrupo.add(cProfesor1);
        cProfesor1.setBounds(580, 130, 270, 24);

        panelImage.add(JPanelGrupo);
        JPanelGrupo.setBounds(0, 40, 880, 200);

        JPanelTable.setOpaque(false);
        JPanelTable.setPreferredSize(new java.awt.Dimension(786, 402));
        JPanelTable.setLayout(new java.awt.BorderLayout());

        tbPane.setOpaque(true);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        Grupo.setForeground(new java.awt.Color(51, 51, 51));
        Grupo.setModel(model = new DefaultTableModel(null, titulos)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    if(column==8 || column==9){
                        return true;
                    }else{
                        return false;}
                }
            });
            Grupo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            Grupo.setFocusCycleRoot(true);
            Grupo.setGridColor(new java.awt.Color(51, 51, 255));
            Grupo.setRowHeight(20);
            Grupo.setSelectionBackground(java.awt.SystemColor.activeCaption);
            Grupo.setSurrendersFocusOnKeystroke(true);
            Grupo.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    GrupoMouseClicked(evt);
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    GrupoMouseClicked1(evt);
                }
            });
            Grupo.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    GrupoKeyPressed(evt);
                }
            });
            jScrollPane4.setViewportView(Grupo);

            jPanel3.add(jScrollPane4, java.awt.BorderLayout.CENTER);

            tbPane.addTab("Fusionar de : Grupo 1", jPanel3);

            JPanelTable.add(tbPane, java.awt.BorderLayout.CENTER);

            panelImage.add(JPanelTable);
            JPanelTable.setBounds(0, 240, 440, 290);

            JPanelTable1.setOpaque(false);
            JPanelTable1.setPreferredSize(new java.awt.Dimension(786, 402));
            JPanelTable1.setLayout(new java.awt.BorderLayout());

            tbPane2.setOpaque(true);

            jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jPanel5.setLayout(new java.awt.BorderLayout());

            jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            Grupo2.setForeground(new java.awt.Color(51, 51, 51));
            Grupo2.setModel(model2 = new DefaultTableModel(null, titulos2)
                {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        if(column==8 || column==9){
                            return true;
                        }else{
                            return false;}
                    }
                });
                Grupo2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                Grupo2.setFocusCycleRoot(true);
                Grupo2.setGridColor(new java.awt.Color(51, 51, 255));
                Grupo2.setRowHeight(20);
                Grupo2.setSelectionBackground(java.awt.SystemColor.activeCaption);
                Grupo2.setSurrendersFocusOnKeystroke(true);
                Grupo2.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        Grupo2MouseClicked(evt);
                    }
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        Grupo2MouseClicked1(evt);
                    }
                });
                Grupo2.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        Grupo2KeyPressed(evt);
                    }
                });
                jScrollPane5.setViewportView(Grupo2);

                jPanel5.add(jScrollPane5, java.awt.BorderLayout.CENTER);

                tbPane2.addTab("A: Grupo 2", jPanel5);

                JPanelTable1.add(tbPane2, java.awt.BorderLayout.CENTER);

                panelImage.add(JPanelTable1);
                JPanelTable1.setBounds(440, 240, 440, 290);

                pnlPaginador1.setBackground(new java.awt.Color(57, 104, 163));
                pnlPaginador1.setPreferredSize(new java.awt.Dimension(786, 40));
                pnlPaginador1.setLayout(new java.awt.GridBagLayout());

                jLabel11.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
                jLabel11.setForeground(new java.awt.Color(255, 255, 255));
                jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/alumno.png"))); // NOI18N
                jLabel11.setText("<--Fusión de Grupos-->");
                pnlPaginador1.add(jLabel11, new java.awt.GridBagConstraints());

                panelImage.add(pnlPaginador1);
                pnlPaginador1.setBounds(0, 0, 880, 40);

                getContentPane().add(panelImage, java.awt.BorderLayout.CENTER);

                getAccessibleContext().setAccessibleName("Profesores");

                setBounds(0, 0, 890, 614);
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

    private void GrupoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GrupoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_GrupoKeyPressed

    private void GrupoMouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GrupoMouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_GrupoMouseClicked1

    private void GrupoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GrupoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_GrupoMouseClicked

    private void Grupo2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Grupo2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Grupo2MouseClicked

    private void Grupo2MouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Grupo2MouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_Grupo2MouseClicked1

    private void Grupo2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Grupo2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Grupo2KeyPressed

    private void bntGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardarActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntGuardar.getName()) == true) {

            if (Grupo.getRowCount() == 0 /*&& colegiaturas.getSelectedRow() == -1*/ || Grupo.getRowCount() == -1) {
                JOptionPane.showMessageDialog(null, "La tabla no contiene datos que Fucionar");
                return;
            } else { //Inicio de Guardar datos

                if (!carrera.getText().equals(carrera1.getText())) {
                    JOptionPane.showMessageDialog(null, "La carrera de los Grupos debe ser Igual");
                    return;
                }
                if (cGrupo.getSelectedItem().toString().equals(cGrupo1.getSelectedItem().toString())) {
                    JOptionPane.showMessageDialog(null, "Los Grupos a fusionar deben ser distintos");
                    return;
                }

                int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
                if (resp == 0) {

                    //String printHorario = "";
                    int n = 0;
                    conn = BdConexion.getConexion();
                    Statement ps = null;

                    try {
                        conn.setAutoCommit(false);
                        ps = conn.createStatement();

                        //GUARDAR FUSION DE GRUPOS...............***************
                        //******************************************************
                        boolean camprec = false;
                        int cant = model.getRowCount();
                        mGrupo1 grup = (mGrupo1) cGrupo1.getSelectedItem();
                        String idgrupo = grup.getID();//nuevo grupo

                        if (cant > 0 || cant != -1) {
                            for (int i = 0; i < cant; i++) { //for pago de meses
                                camprec = true;
                                String iddetgrupo = (String) "" + Grupo.getValueAt(i, 6);
                                String fgrupo = "update alumnosengrupo set  grupo_idgrupo='" + idgrupo + "'  where iddetallegrupo=" + iddetgrupo;
                                n = ps.executeUpdate(fgrupo);
                            }//fin for pago de meses
                        }
                        if (!camprec) {
                            JOptionPane.showInternalMessageDialog(this, "No hay datos para Fusionar", "Mensage", JOptionPane.INFORMATION_MESSAGE);
                        }
                        if (n > 0) {
                            JOptionPane.showInternalMessageDialog(this, "La fusion se Guardo Correctamente");
                            //limpiartodo();
                        }
                        //FIN GUARDAR FUSION DE GRUPOS**********************************
                        //**************************************************************

                        conn.commit();// guarda todas las consultas si no ubo error
                        ps.close();
                        if (!conn.getAutoCommit()) {
                            conn.setAutoCommit(true);
                        }
                        //ListadoAlumnosGrupo.ReporteGrupo(idgrupo);
                        MostrarPagos("1");
                        MostrarPagos("2");
                    } catch (SQLException ex) {
                        try {
                            conn.rollback();// no guarda ninguna de las consultas ya que ubo error
                            ps.close();
                            if (!conn.getAutoCommit()) {
                                conn.setAutoCommit(true);
                            }
                        } catch (SQLException ex1) {
                            JOptionPane.showMessageDialog(null, ex1);
                        }
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
            }//Fin Guardar datos
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntGuardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Grupo;
    private javax.swing.JTable Grupo2;
    private javax.swing.JPanel JPanelGrupo;
    private javax.swing.JPanel JPanelTable;
    private javax.swing.JPanel JPanelTable1;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    public static javax.swing.JComboBox cGrupo;
    public static javax.swing.JComboBox cGrupo1;
    public static javax.swing.JComboBox cProfesor;
    public static javax.swing.JComboBox cProfesor1;
    private elaprendiz.gui.textField.TextField carrera;
    private elaprendiz.gui.textField.TextField carrera1;
    private javax.swing.JFormattedTextField colegiatura;
    private javax.swing.JFormattedTextField colegiatura1;
    private elaprendiz.gui.textField.TextField fechafin;
    private elaprendiz.gui.textField.TextField fechafin1;
    private elaprendiz.gui.textField.TextField fechainicio;
    private elaprendiz.gui.textField.TextField fechainicio1;
    private javax.swing.JFormattedTextField inscripcion;
    private javax.swing.JFormattedTextField inscripcion1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador1;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane2;
    // End of variables declaration//GEN-END:variables
}
