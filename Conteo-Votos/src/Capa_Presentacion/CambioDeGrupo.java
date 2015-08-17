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
import Capa_Negocio.Peticiones;
import Capa_Negocio.TipoFiltro;
import Capa_Negocio.Utilidades;
import static Capa_Presentacion.Principal.dp;
import Reportes.EstadoCuenta;
import java.awt.Color;
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

/**
 *
 * @author GLARA
 */
public class CambioDeGrupo extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model, model2;
    DefaultComboBoxModel modelCombo;
    String[] titulos = {"Id", "Codigo", "Descripción", "Año", "Monto", "Fecha V", "Mora", "Subtotal", "Pagar Mora", "Pagar Mes"};//Titulos para Jtabla
    String[] titulos2 = {"Código", "Descripción", "Precio", "Cantidad", "SubTotal", "Agregar"};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();
    public static Hashtable<String, String> hashGrupo = new Hashtable<>();
    public static Hashtable<String, String> hashGrupo1 = new Hashtable<>();
    public static Hashtable<String, String> hashTipopago = new Hashtable<>();
    public Hashtable<String, String> hashProfesor = new Hashtable<>();
    AccesoDatos acceso = new AccesoDatos();
    static String idalumno = "", iddetallegrupo = "";
    java.sql.Connection conn;//getConnection intentara establecer una conexión.

    /**
     * Creates new form Cliente
     */
    public CambioDeGrupo() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();
        llenarcomboprofesor2();
        //llenarcombotipopago();

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

        cProfesor1.addItemListener(
                (ItemEvent e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        grupos2();

                    }
                });

//        colegiaturas.getModel().addTableModelListener(new TableModelListener() {
//            public void tableChanged(TableModelEvent e) {
//                sumartotal();
//                //formatotabla();
//            }
//        });
//
//        otrosproductos.getModel().addTableModelListener(new TableModelListener() {
//            public void tableChanged(TableModelEvent e) {
//                sumartotal();
//                //formatotabla();
//            }
//        });
//        //colegiaturas.getColumnModel().getColumn(8).setCellEditor(new Editor_CheckBox());
//        //colegiaturas.getColumnModel().getColumn(0).setPreferredWidth(0);
//        colegiaturas.getColumnModel().getColumn(0).setMaxWidth(0);
//        colegiaturas.getColumnModel().getColumn(0).setMinWidth(0);
//        colegiaturas.getColumnModel().getColumn(0).setPreferredWidth(0);
//        colegiaturas.doLayout();
//        JCheckBox check = new JCheckBox();
//        colegiaturas.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(check));
//        colegiaturas.getColumnModel().getColumn(9).setCellEditor(new DefaultCellEditor(check));
//        otrosproductos.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(check));
//        //colegiaturas.getColumnModel().getColumn(8).setCellEditor(new Editor_CheckBox()); *
//        //colegiaturas.getColumnModel().getColumn(9).setCellEditor(new Editor_CheckBox()); *
//
//        //para pintar la columna con el CheckBox en la tabla, en este caso, la primera columna
//        colegiaturas.getColumnModel().getColumn(8).setCellRenderer(new Renderer_CheckBox());
//        colegiaturas.getColumnModel().getColumn(9).setCellRenderer(new Renderer_CheckBox());
//        otrosproductos.getColumnModel().getColumn(5).setCellRenderer(new Renderer_CheckBox());
//
//        CellEditorSpinnerPago cnt = new CellEditorSpinnerPago(1);
//        otrosproductos.getColumnModel().getColumn(3).setCellEditor(cnt);
//        otrosproductos.getColumnModel().getColumn(3).setCellRenderer(new TableCellFormatter(null));
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
            //removejtable();
            codigoa.setText("");
            codigoa.requestFocus();
            this.dispose();
        }
    }

//    /* Para no sobrecargar la memoria y hacer una instancia cada vez que actualizamos la JTable se hace una
//     * sola instancia y lo unico que se hace antes de actualizar la JTable es limpiar el modelo y enviarle los
//     * nuevos datos a mostrar en la JTable  */
//    public void removejtable() {
//        while (colegiaturas.getRowCount() != 0) {
//            model.removeRow(0);
//        }
//    }
//
//    public void removejtable2() {
//        while (otrosproductos.getRowCount() != 0) {
//            model2.removeRow(0);
//        }
//    }
    private void limpiartodo() {
        codigoa.setText("");
        codigoa.requestFocus();
        nombrealumno.setText("");

        profesor.setText("");
        carrera.setText("");
        horade.setText("");
        horaa.setText("");
        fechainicio.setText("");
        fechafin.setText("");
        inscripcion.setValue(null);
        colegiatura.setValue(null);
        dia.setText("");
        cGrupo.setSelectedIndex(-1);

        carrera1.setText("");
        horade1.setText("");
        horaa1.setText("");
        fechainicio1.setText("");
        fechafin1.setText("");
        inscripcion1.setValue(null);
        colegiatura1.setValue(null);
        dia1.setText("");
        cGrupo1.setSelectedIndex(-1);
        cProfesor1.setSelectedIndex(-1);
        
        Utilidades.esObligatorio(this.JPanelRecibo, false);
        Utilidades.esObligatorio(this.JPanelGrupo, false);
        
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

    private void grupos2() {
        if (cProfesor1.getSelectedIndex() != -1) {
            mProfesor prof = (mProfesor) cProfesor1.getSelectedItem();
            String idprof = prof.getID();
            llenarcombogrupo2(idprof);
        }
    }

    /*
     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
     *y se los envia a un metodo interno getRegistroCombo() 
     *
     */
    public static void llenarcombogrupo2(String idalumn) {

        String Dato = "1";
        String[] campos = {"grupo.dia", "DATE_FORMAT(grupo.horariode,'%h:%i %p')", "DATE_FORMAT(grupo.horarioa,'%h:%i %p')", "grupo.idgrupo"};
        String[] condiciones = {"grupo.estado", "grupo.profesor_idcatedratico"};
        String[] Id = {Dato, idalumn};
        cGrupo1.removeAllItems();
        getRegistroCombo2("grupo", campos, condiciones, Id, "");

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
            dia.setText("");
            Utilidades.esObligatorio(this.JPanelGrupo, false);
            Utilidades.esObligatorio(this.JPanelGrupo1, false);
       
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
                            Utilidades.esObligatorio(this.JPanelGrupo, false);
                            Utilidades.esObligatorio(this.JPanelGrupo1, false);
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
            Utilidades.esObligatorio(this.JPanelGrupo, false);
            Utilidades.esObligatorio(this.JPanelGrupo1, false);
            
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
                                horade1.setText(rs.getString(3));
                                horaa1.setText(rs.getString(4));
                                fechainicio1.setText((rs.getString(5)));
                                fechafin1.setText((rs.getString(6)));
                                inscripcion1.setValue(rs.getFloat(7));
                                colegiatura1.setValue(rs.getFloat(8));
                                dia1.setText(rs.getString(9));
                            }
                            idalumnosengrupo(idalumno, "" + grup.getID());
                            Utilidades.esObligatorio(this.JPanelGrupo, false);
                            Utilidades.esObligatorio(this.JPanelGrupo1, false);
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
            estado.setText("");
            cGrupo.removeAllItems();
            idalumno = "";
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

    /* Este metodo se encarga de filtrar los datos que se deben ingresar en cada uno de los campos del formulario
     * podemos indicar que el usuario ingrese solo numeros , solo letras, numeros y letras, o cualquier caracter
     * tambien podemos validar si se aseptaran espacios en blanco en la cadena ingresada , para mas detalle visualizar
     * la clase TipoFiltro()  */
    private void setFiltroTexto() {
        TipoFiltro.setFiltraEntrada(codigoa.getDocument(), FiltroCampos.NUM_LETRAS, 45, false);
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
                        //String fechainicio = FormatoFecha.getFormato(rs.getDate(2), FormatoFecha.D_M_A);
                        //inicioalumno.setText(fechainicio);
                        //float becac = Float.parseFloat(rs.getString(3));
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
        bntGuardar = new elaprendiz.gui.button.ButtonRect();
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
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
        JPanelBusqueda = new javax.swing.JPanel();
        codigoa = new elaprendiz.gui.textField.TextField();
        jLabel16 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        nombrealumno = new elaprendiz.gui.textField.TextField();
        jLabel19 = new javax.swing.JLabel();
        estado = new javax.swing.JLabel();
        JPanelRecibo = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        clockDigital2 = new elaprendiz.gui.varios.ClockDigital();
        pnlPaginador1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        JPanelGrupo1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cGrupo1 = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        carrera1 = new elaprendiz.gui.textField.TextField();
        inscripcion1 = new javax.swing.JFormattedTextField();
        colegiatura1 = new javax.swing.JFormattedTextField();
        horaa1 = new elaprendiz.gui.textField.TextField();
        horade1 = new elaprendiz.gui.textField.TextField();
        fechainicio1 = new elaprendiz.gui.textField.TextField();
        fechafin1 = new elaprendiz.gui.textField.TextField();
        jLabel29 = new javax.swing.JLabel();
        dia1 = new elaprendiz.gui.textField.TextField();
        cProfesor1 = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Fusion de Alumno");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Cambio de Grupo"); // NOI18N
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
        bntGuardar.setName("Guardar Fusion Alumno"); // NOI18N
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

        panelImage.add(pnlActionButtons);
        pnlActionButtons.setBounds(0, 500, 880, 50);

        JPanelGrupo.setBackground(java.awt.SystemColor.inactiveCaption);
        JPanelGrupo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelGrupo.setForeground(new java.awt.Color(204, 204, 204));
        JPanelGrupo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JPanelGrupo.setLayout(null);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Horario A:");
        JPanelGrupo.add(jLabel10);
        jLabel10.setBounds(730, 10, 90, 20);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Profesor:");
        JPanelGrupo.add(jLabel3);
        jLabel3.setBounds(320, 10, 250, 20);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Fecha Fin:");
        JPanelGrupo.add(jLabel6);
        jLabel6.setBounds(730, 60, 90, 20);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Horario De:");
        JPanelGrupo.add(jLabel13);
        jLabel13.setBounds(600, 10, 80, 20);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Fecha Inicio:");
        JPanelGrupo.add(jLabel9);
        jLabel9.setBounds(600, 60, 90, 20);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Carrera:");
        JPanelGrupo.add(jLabel5);
        jLabel5.setBounds(320, 60, 250, 20);

        cGrupo.setEditable(true);
        cGrupo.setName("grupo"); // NOI18N
        JPanelGrupo.add(cGrupo);
        cGrupo.setBounds(90, 30, 210, 24);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("De Grupo:");
        JPanelGrupo.add(jLabel7);
        jLabel7.setBounds(10, 30, 70, 27);

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Inscripción Q.");
        JPanelGrupo.add(jLabel24);
        jLabel24.setBounds(600, 110, 90, 20);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Colegiatura Q.");
        JPanelGrupo.add(jLabel18);
        jLabel18.setBounds(730, 110, 91, 20);

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
        inscripcion.setBounds(600, 130, 90, 24);

        colegiatura.setEditable(false);
        colegiatura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        colegiatura.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        colegiatura.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        colegiatura.setPreferredSize(new java.awt.Dimension(80, 23));
        JPanelGrupo.add(colegiatura);
        colegiatura.setBounds(730, 130, 90, 24);

        horaa.setEditable(false);
        horaa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        horaa.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(horaa);
        horaa.setBounds(730, 30, 90, 24);

        horade.setEditable(false);
        horade.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        horade.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(horade);
        horade.setBounds(600, 30, 90, 24);

        fechainicio.setEditable(false);
        fechainicio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechainicio.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(fechainicio);
        fechainicio.setBounds(600, 80, 90, 24);

        fechafin.setEditable(false);
        fechafin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechafin.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(fechafin);
        fechafin.setBounds(730, 80, 90, 24);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Día:");
        JPanelGrupo.add(jLabel12);
        jLabel12.setBounds(320, 110, 250, 20);

        dia.setEditable(false);
        dia.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        dia.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(dia);
        dia.setBounds(320, 130, 260, 24);

        panelImage.add(JPanelGrupo);
        JPanelGrupo.setBounds(0, 160, 880, 170);

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
        estado.setBounds(700, 50, 110, 27);

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

        panelImage.add(JPanelRecibo);
        JPanelRecibo.setBounds(0, 40, 880, 70);

        pnlPaginador1.setBackground(new java.awt.Color(57, 104, 163));
        pnlPaginador1.setPreferredSize(new java.awt.Dimension(786, 40));
        pnlPaginador1.setLayout(new java.awt.GridBagLayout());

        jLabel11.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/money.png"))); // NOI18N
        jLabel11.setText("<--Fusión de Alumno-->");
        pnlPaginador1.add(jLabel11, new java.awt.GridBagConstraints());

        panelImage.add(pnlPaginador1);
        pnlPaginador1.setBounds(0, 0, 880, 40);

        JPanelGrupo1.setBackground(java.awt.SystemColor.inactiveCaption);
        JPanelGrupo1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelGrupo1.setForeground(new java.awt.Color(204, 204, 204));
        JPanelGrupo1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JPanelGrupo1.setLayout(null);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Horario A:");
        JPanelGrupo1.add(jLabel14);
        jLabel14.setBounds(730, 10, 90, 20);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Fecha Fin:");
        JPanelGrupo1.add(jLabel8);
        jLabel8.setBounds(730, 60, 90, 20);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Horario De:");
        JPanelGrupo1.add(jLabel15);
        jLabel15.setBounds(600, 10, 80, 20);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Fecha Inicio:");
        JPanelGrupo1.add(jLabel17);
        jLabel17.setBounds(600, 60, 90, 20);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("Carrera:");
        JPanelGrupo1.add(jLabel20);
        jLabel20.setBounds(320, 10, 250, 20);

        cGrupo1.setEditable(true);
        cGrupo1.setName("grupo1"); // NOI18N
        JPanelGrupo1.add(cGrupo1);
        cGrupo1.setBounds(90, 80, 210, 24);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 51, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel22.setText("A Grupo:");
        JPanelGrupo1.add(jLabel22);
        jLabel22.setBounds(10, 80, 70, 27);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Inscripción Q.");
        JPanelGrupo1.add(jLabel27);
        jLabel27.setBounds(600, 110, 90, 20);

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Colegiatura Q.");
        JPanelGrupo1.add(jLabel28);
        jLabel28.setBounds(730, 110, 91, 20);

        carrera1.setEditable(false);
        carrera1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        carrera1.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo1.add(carrera1);
        carrera1.setBounds(320, 30, 260, 24);

        inscripcion1.setEditable(false);
        inscripcion1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        inscripcion1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inscripcion1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        inscripcion1.setPreferredSize(new java.awt.Dimension(80, 23));
        JPanelGrupo1.add(inscripcion1);
        inscripcion1.setBounds(600, 130, 90, 24);

        colegiatura1.setEditable(false);
        colegiatura1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
        colegiatura1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        colegiatura1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        colegiatura1.setPreferredSize(new java.awt.Dimension(80, 23));
        JPanelGrupo1.add(colegiatura1);
        colegiatura1.setBounds(730, 130, 90, 24);

        horaa1.setEditable(false);
        horaa1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        horaa1.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo1.add(horaa1);
        horaa1.setBounds(730, 30, 90, 24);

        horade1.setEditable(false);
        horade1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        horade1.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo1.add(horade1);
        horade1.setBounds(600, 30, 90, 24);

        fechainicio1.setEditable(false);
        fechainicio1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechainicio1.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo1.add(fechainicio1);
        fechainicio1.setBounds(600, 80, 90, 24);

        fechafin1.setEditable(false);
        fechafin1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechafin1.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo1.add(fechafin1);
        fechafin1.setBounds(730, 80, 90, 24);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel29.setText("Día:");
        JPanelGrupo1.add(jLabel29);
        jLabel29.setBounds(320, 60, 250, 20);

        dia1.setEditable(false);
        dia1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        dia1.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo1.add(dia1);
        dia1.setBounds(320, 80, 260, 24);

        cProfesor1.setEditable(true);
        cProfesor1.setModel(modelCombo = new DefaultComboBoxModel());
        cProfesor1.setName("Profesor"); // NOI18N
        JPanelGrupo1.add(cProfesor1);
        cProfesor1.setBounds(90, 30, 210, 24);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 51, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel23.setText("Profesor:");
        JPanelGrupo1.add(jLabel23);
        jLabel23.setBounds(10, 30, 70, 24);

        panelImage.add(JPanelGrupo1);
        JPanelGrupo1.setBounds(0, 330, 880, 170);

        getContentPane().add(panelImage, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleName("Profesores");

        setBounds(0, 0, 890, 583);
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        BuscarAlumnoCambioGrupo frmBuscarAlumnoCambioGrupo = new BuscarAlumnoCambioGrupo();
        if (frmBuscarAlumnoCambioGrupo == null) {
            frmBuscarAlumnoCambioGrupo = new BuscarAlumnoCambioGrupo();
        }
        adminInternalFrame(dp, frmBuscarAlumnoCambioGrupo);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void codigoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoaActionPerformed
        // TODO add your handling code here:

        balumnocodigo(codigoa.getText());

    }//GEN-LAST:event_codigoaActionPerformed

    private void bntGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardarActionPerformed
        // TODO add your handling code here:

        if (AccesoUsuario.AccesosUsuario(bntGuardar.getName()) == true) {

            if (Utilidades.esObligatorio(this.JPanelGrupo, true)
                    || Utilidades.esObligatorio(this.JPanelGrupo1, true)) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
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

                    int n = 0;
                    conn = BdConexion.getConexion();
                    Statement ps = null;

                    try {
                        conn.setAutoCommit(false);
                        ps = conn.createStatement();

                        //GUARDAR FUSION DE GRUPOS...............***************
                        //******************************************************
                        mGrupo1 grup = (mGrupo1) cGrupo1.getSelectedItem();
                        String idgrupo = grup.getID();//nuevo grupo

                        String fgrupo = "update alumnosengrupo set  grupo_idgrupo='" + idgrupo + "'  where iddetallegrupo=" + iddetallegrupo;
                        n = ps.executeUpdate(fgrupo);
                        if (n > 0) {
                            JOptionPane.showInternalMessageDialog(this, "La fusion se Guardo Correctamente");
                            limpiartodo();
                        }
                        //FIN GUARDAR FUSION DE GRUPOS**********************************
                        //**************************************************************

                        conn.commit();// guarda todas las consultas si no ubo error
                        ps.close();
                        if (!conn.getAutoCommit()) {
                            conn.setAutoCommit(true);
                        }
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
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelGrupo;
    private javax.swing.JPanel JPanelGrupo1;
    private javax.swing.JPanel JPanelRecibo;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    public static javax.swing.JComboBox cGrupo;
    public static javax.swing.JComboBox cGrupo1;
    public static javax.swing.JComboBox cProfesor1;
    private elaprendiz.gui.textField.TextField carrera;
    private elaprendiz.gui.textField.TextField carrera1;
    private elaprendiz.gui.varios.ClockDigital clockDigital2;
    public static elaprendiz.gui.textField.TextField codigoa;
    private javax.swing.JFormattedTextField colegiatura;
    private javax.swing.JFormattedTextField colegiatura1;
    private elaprendiz.gui.textField.TextField dia;
    private elaprendiz.gui.textField.TextField dia1;
    public static javax.swing.JLabel estado;
    private elaprendiz.gui.textField.TextField fechafin;
    private elaprendiz.gui.textField.TextField fechafin1;
    private elaprendiz.gui.textField.TextField fechainicio;
    private elaprendiz.gui.textField.TextField fechainicio1;
    private elaprendiz.gui.textField.TextField horaa;
    private elaprendiz.gui.textField.TextField horaa1;
    private elaprendiz.gui.textField.TextField horade;
    private elaprendiz.gui.textField.TextField horade1;
    private javax.swing.JFormattedTextField inscripcion;
    private javax.swing.JFormattedTextField inscripcion1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public static elaprendiz.gui.textField.TextField nombrealumno;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador1;
    private elaprendiz.gui.textField.TextField profesor;
    // End of variables declaration//GEN-END:variables
}
