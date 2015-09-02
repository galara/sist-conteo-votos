/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Capa_Presentacion;

import Capa_Datos.AccesoDatos;
import Capa_Negocio.Peticiones;
import Capa_Negocio.Utilidades;
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
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import modelos.MMunicipio;
//import modelos.MPartido;
//import modelos.MMunicipio;

/**
 *
 * @author GLARA
 */
public class Conteo_Votos extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model, model2, model3, model4, model5;
    DefaultComboBoxModel modelCombo;
    //DefaultComboBoxModel modelCombo;
    //String[] titulos = {"Id", "Codigo", "Nombre Candidato", "Partido", "Candidatura", "Municipio", "Mora", "Subtotal", "Pagar Mora", "Pagar Mes"};//Titulos para Jtabla
    String[] titulos2 = {"Partido", "Votos", "%"};//Titulos para Jtabla
    String[] titulos3 = {"Municipio", "Partido", "Votos", "%"};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();
    public Hashtable<String, String> hashCandidatura = new Hashtable<>();
    public Hashtable<String, String> hashMunicipio = new Hashtable<>();
    //public static Hashtable<String, String> hashGrupo = new Hashtable<>();
    //public static Hashtable<String, String> hashTipopago = new Hashtable<>();
    AccesoDatos acceso = new AccesoDatos();
    static String idmesa = "";
    java.sql.Connection conn;//getConnection intentara establecer una conexión.

    /**
     * Creates new form Cliente
     */
    public Conteo_Votos() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();
        //llenarcombopuesto();
        llenarcombomunicipio();

        seleccion.addItemListener(
                (ItemEvent e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        seleccion();
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
            Utilidades.setEditableTexto(this.JPanelGrupo, false, null, true, "");
            Utilidades.esObligatorio(this.JPanelGrupo, false);
            this.bntGuardar.setEnabled(false);
            //removejtable2();
            //codigomesa.setText("");
            //codigomesa.requestFocus();
            this.dispose();
        }
    }

    private void seleccion() {
        if (seleccion.getSelectedItem() == "General") {
            cmunicipio.setSelectedIndex(0);
            cmunicipio.setVisible(false);
            jLabel10.setVisible(false);
            cmunicipio.setEnabled(false);
            cmunicipio.setEditable(false);
        } else {
            cmunicipio.setSelectedIndex(0);
            cmunicipio.setVisible(true);
            jLabel10.setVisible(true);
            cmunicipio.setEnabled(true);
            cmunicipio.setEditable(true);
        }

    }

    public void removejtable2(DefaultTableModel modelo, JTable table) {
        while (table.getRowCount() != 0) {
            modelo.removeRow(0);
        }
    }

    private void limpiartodo() {
//        codigomesa.setText("");
//        //codigomesa.requestFocus();
//        idcentro.setText("");
//        idmunicipio.setText("");
//        nombrecentro.setText("");
//        nombremunicipio.setText("");
        Utilidades.esObligatorio(this.JPanelBusqueda, false);
        //codigomesa.requestFocus();
        removejtable2(model, tpresidentes);
        removejtable2(model2, tdiputados1);
        removejtable2(model3, tdiputados2);
        removejtable2(model4, tdiputados3);
        removejtable2(model4, tdiputados3);
        removejtable2(model5, talcalde);
    }

//    /*
//     * Metodo para buscar un alumno por su codigo devuelde el id
//     */
//    public void balumnocodigo(String codigo) {
//        if (codigo.isEmpty()) {
//            limpiartodo();
//            //nombrecentro.setText("");
//            //estado.setText("");
//            //cGrupo.removeAllItems();
//            //idmesa = "";
//            //inicioalumno.setText("");
//            //beca.setText("");
//            //dia.setText("");
//
//        } else if (!codigo.isEmpty()) {
//
//            ResultSet rs;
//            AccesoDatos ac = new AccesoDatos();
//            String[] campos = {"mesa.nombre", "centro.nombre", "mesa.estado", "mesa.idmesa", "municipio.nombre", "centro.idcentro", "municipio.idmunicipio"};
//            String[] condiciones = {"mesa.estado=1 and mesa.nombre"};
//            String[] id = {codigo};
//            String inner = " INNER JOIN centro on mesa.centro_idcentro=centro.idcentro INNER JOIN municipio on centro.municipio_idmunicipio=municipio.idmunicipio ";
//
////            String[] campos = {"alumno.codigo", "alumno.nombres", "alumno.apellidos", "DATE_FORMAT(alumno.fechanacimiento,'%d-%m-%Y')", "alumno.estado", "alumno.idalumno"};
////            String[] cond = {"alumno.codigo"};
////            String[] id = {codigo};
//            if (!id.equals(0)) {
//
//                rs = ac.getRegistros("mesa", campos, condiciones, id, inner);
//
//                if (rs != null) {
//                    try {
//                        if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
//                            rs.beforeFirst();//regresa el puntero al primer registro
//                            while (rs.next()) {//mientras tenga registros que haga lo siguiente
//                                codigomesa.setText(rs.getString(1));
//                                nombrecentro.setText(rs.getString(2));
////                                if (rs.getString(3).equals("0")) {
////                                    estado.setText("Inactivo");
////                                    estado.setForeground(Color.red);
////                                } else if (rs.getString(3).equals("1")) {
////                                    estado.setText("Activo");
////                                    estado.setForeground(Color.WHITE/*new java.awt.Color(102, 204, 0)*/);
////                                }
//                                idmesa = (rs.getString(4));
//                                nombremunicipio.setText(rs.getString(5));
//                                idcentro.setText(rs.getString(6));
//                                idmunicipio.setText(rs.getString(7));
//                                llenartablas();
//                            }
//                        } else {
//                            JOptionPane.showInternalMessageDialog(this, " El codigo no fue encontrado ");
//                            limpiartodo();
//                            idmesa = null;
//                        }
//                    } catch (SQLException e) {
//                        JOptionPane.showInternalMessageDialog(this, e);
//                    }
//                } else {
//                    JOptionPane.showInternalMessageDialog(this, " El codigo no fue encontrado ");
//                    limpiartodo();
//                }
//
//            }
//        }
//    }
//    /*
//     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
//     *y se los envia a un metodo interno getRegistroCombo() 
//     *
//     */
//    public void llenarcombopuesto() {
//        String Dato = "1";
//        String[] campos = {"nombre", "idpuesto"};
//        String[] condiciones = {"estado"};
//        String[] Id = {Dato};
//        puesto.removeAllItems();
//        Component cmps = puesto;
//        getRegistroCombopuesto("puesto", campos, condiciones, Id);
//
//    }
//
//    /*El metodo llenarcombo() envia los parametros para la consulta a la BD y el medoto
//     *getRegistroCombo() se encarga de enviarlos a la capa de AccesoDatos.getRegistros()
//     *quiern devolcera un ResultSet para luego obtener los valores y agregarlos al JConboBox
//     *y a una Hashtable que nos servira para obtener el id y seleccionar valores.
//     */
//    public void getRegistroCombopuesto(String tabla, String[] campos, String[] campocondicion, String[] condicionid) {
//        try {
//            ResultSet rs;
//            AccesoDatos ac = new AccesoDatos();
//
//            rs = ac.getRegistros(tabla, campos, campocondicion, condicionid, "");
//
//            int cantcampos = campos.length;
//            if (rs != null) {
//
//                DefaultComboBoxModel modeloComboBox;
//                modeloComboBox = new DefaultComboBoxModel();
//                puesto.setModel(modeloComboBox);
//
//                modeloComboBox.addElement(new MCandidatura("", "0"));
//                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
//                    int count = 0;
//                    rs.beforeFirst();//regresa el puntero al primer registro
//                    Object[] fila = new Object[cantcampos];
//                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
//                        count++;
//                        modeloComboBox.addElement(new MCandidatura(rs.getString(1), "" + rs.getInt(2)));
//                        hashCandidatura.put(rs.getString(1), "" + count);
//                    }
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Error", JOptionPane.INFORMATION_MESSAGE);
//            }
//            //rs.close();
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }

    /*
     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
     *y se los envia a un metodo interno getRegistroCombo() 
     *
     */
    public void llenarcombomunicipio() {
        String Dato = "1";
        String[] campos = {"nombre", "idmunicipio"};
        String[] condiciones = {"estado"};
        String[] Id = {Dato};
        cmunicipio.removeAllItems();
        //Component cmps = municipio;
        getRegistromunicipio("municipio", campos, condiciones, Id);

    }

    /*El metodo llenarcombo() envia los parametros para la consulta a la BD y el medoto
     *getRegistroCombo() se encarga de enviarlos a la capa de AccesoDatos.getRegistros()
     *quiern devolcera un ResultSet para luego obtener los valores y agregarlos al JConboBox
     *y a una Hashtable que nos servira para obtener el id y seleccionar valores.
     */
    public void getRegistromunicipio(String tabla, String[] campos, String[] campocondicion, String[] condicionid) {
        try {
            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getRegistros(tabla, campos, campocondicion, condicionid, "");

            int cantcampos = campos.length;
            if (rs != null) {

                DefaultComboBoxModel modeloComboBox;
                modeloComboBox = new DefaultComboBoxModel();
                cmunicipio.setModel(modeloComboBox);

                modeloComboBox.addElement(new MMunicipio("", "0"));
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    Object[] fila = new Object[cantcampos];
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        count++;
                        modeloComboBox.addElement(new MMunicipio(rs.getString(1), "" + rs.getInt(2)));
                        hashMunicipio.put(rs.getString(1), "" + count);
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

//    /* Este metodo recibe de el campo busqueda un parametro que es el que servirá para realizar la cunsulta
//     * de los datos, este envia a la capa de negocio "peticiones.getRegistroPorPks( el modelo de la JTable,
//     * el nombre de la tabla, los campos de la tabla a consultar, los campos de condiciones, y el dato a comparar
//     * en la(s) condicion(es) de la busqueda) .
//     *   
//     * Nota: si el campo busqueda no contiene ningun dato devolvera todos los datos de la tabla o un mensage
//     * indicando que no hay datos para la busqueda  
//     *
//     * @param Dato , dato a buscar
//     * @return 
//     */
//    private void MostrarPagos() {
//
//        String sql = "SELECT proyeccionpagos.idproyeccionpagos,proyeccionpagos.mes_idmes,mes.mes,proyeccionpagos.año,proyeccionpagos.monto,\n"
//                + "     proyeccionpagos.fechavencimiento,IFNULL((SELECT mora.mora FROM mora where proyeccionpagos.idproyeccionpagos = mora.proyeccionpagos_idproyeccionpagos and mora.exoneracion=0),0.0) AS 'Mora',proyeccionpagos.alumnosengrupo_iddetallegrupo FROM\n"
//                + "     mes INNER JOIN proyeccionpagos ON mes.idmes = proyeccionpagos.mes_idmes  where alumnosengrupo_iddetallegrupo='" + iddetallegrupo + "' and proyeccionpagos.estado='0' and proyeccionpagos.asignado='1' order by proyeccionpagos.idproyeccionpagos asc ";
//
//        removejtable();
//        model = getRegistroPorLikel(model, sql);
//        Utilidades.ajustarAnchoColumnas(tdiputados1);
//
//        tdiputados1.getColumnModel().getColumn(0).setMaxWidth(0);
//        tdiputados1.getColumnModel().getColumn(0).setMinWidth(0);
//        tdiputados1.getColumnModel().getColumn(0).setPreferredWidth(0);
//        tdiputados1.doLayout();
//    }
//
//    /**
//     * Para una condicion WHERE condicionid LIKE '% campocondicion' * @param
//     * modelo ,modelo de la JTable
//     *
//     * @param tabla , el nombre de la tabla a consultar en la BD
//     * @param campos , los campos de la tabla a consultar ejem: nombre, codigo
//     * ,dirección etc
//     * @param campocondicion , los campos de la tabla para las condiciones ejem:
//     * id,estado etc
//     * @param condicionid , los valores que se compararan con campocondicion
//     * ejem: campocondicion = condicionid
//     * @return
//     */
    public float getAlcalde(String tabla) {
        float cantvotos = 0;
        try {
            String sqll = "SELECT municipio.nombre,SUM(detalle_votos.cant_votos) AS votos\n"
                    + "FROM mesa INNER JOIN detalle_votos ON mesa.idmesa = detalle_votos.mesa_idmesa \n"
                    + "INNER JOIN centro ON mesa.centro_idcentro = centro.idcentro\n"
                    + "INNER JOIN candidato candidato ON detalle_votos.candidato_idcandidato = candidato.idcandidato \n"
                    + "INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido \n"
                    + "INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto \n"
                    + "INNER JOIN municipio ON centro.municipio_idmunicipio = municipio.idmunicipio \n"
                    + "where puesto.nombre = 'Alcalde' and municipio.nombre='" + tabla + "' and partido_politico.nombre!='VOTO NULO' and partido_politico.nombre<>'VOTO BLANCO' group by municipio.nombre order by candidato.idcandidato";
            System.out.print(sqll + "\n\n");
            ResultSet rs;

            rs = acceso.getRegistroProc(sqll);
            //int cantcampos = 2;

            if (rs != null) {
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    //int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    //Object[] fila = new Object[cantcampos];
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        //rs.getString(1);
                        cantvotos = Float.parseFloat((rs.getString(2)));
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Error", JOptionPane.INFORMATION_MESSAGE);
            }

            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return cantvotos;
    }

    public float getGeneral(String puesto, String municipio) {
        float cantvotos = 0;
        try {
            String sqll = "SELECT municipio.nombre,SUM(detalle_votos.cant_votos) AS votos\n"
                    + "FROM mesa INNER JOIN detalle_votos ON mesa.idmesa = detalle_votos.mesa_idmesa \n"
                    + "INNER JOIN centro ON mesa.centro_idcentro = centro.idcentro\n"
                    + "INNER JOIN candidato candidato ON detalle_votos.candidato_idcandidato = candidato.idcandidato \n"
                    + "INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido \n"
                    + "INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto \n"
                    + "INNER JOIN municipio ON centro.municipio_idmunicipio = municipio.idmunicipio \n"
                    + "where puesto.nombre = '" + puesto + "' and municipio.nombre='" + municipio + "' and partido_politico.nombre!='VOTO NULO' and partido_politico.nombre<>'VOTO BLANCO' group by municipio.nombre order by candidato.idcandidato";
            System.out.print(sqll + "\n\n");
            ResultSet rs;

            rs = acceso.getRegistroProc(sqll);
            //int cantcampos = 2;

            if (rs != null) {
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    //int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    //Object[] fila = new Object[cantcampos];
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        //rs.getString(1);
                        cantvotos = Float.parseFloat((rs.getString(2)));
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Error", JOptionPane.INFORMATION_MESSAGE);
            }

            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return cantvotos;
    }

    private void llenartablas() {

        removejtable2(model, tpresidentes);
        removejtable2(model2, tdiputados1);
        removejtable2(model3, tdiputados2);
        removejtable2(model4, tdiputados3);
        removejtable2(model4, tdiputados3);
        removejtable2(model5, talcalde);

        //if (cmunicipio.getSelectedIndex() == 0 || cmunicipio.getSelectedIndex() == -1/*idmesa != null || !codigomesa.getText().isEmpty()*/) {
        if (cmunicipio.getSelectedIndex() == 0 || cmunicipio.getSelectedIndex() == -1) {
            //titulos2=titulos3;
            talcalde.setModel(model5 = new DefaultTableModel(null, titulos3));
            String sql = "SELECT partido_politico.nombre, sum(detalle_votos.cant_votos), (SELECT sum(detalle_votos.cant_votos) FROM candidato \n"
                    + "INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato \n"
                    + "INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido \n"
                    + "INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto \n"
                    + "where puesto.nombre = 'Presidente' and partido_politico.nombre!='VOTO NULO' and partido_politico.nombre<>'VOTO BLANCO') as total FROM candidato INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto where puesto.nombre = 'Presidente' group by partido_politico.nombre order by candidato.idcandidato";
            MostrarProductos(model, tpresidentes, sql);
            //Diputados1
            String sql2 = "SELECT partido_politico.nombre, sum(detalle_votos.cant_votos),(SELECT sum(detalle_votos.cant_votos) FROM candidato \n"
                    + "INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato \n"
                    + "INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido \n"
                    + "INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto \n"
                    + "where puesto.nombre = 'Diputado Listado Nacianal' and partido_politico.nombre!='VOTO NULO' and partido_politico.nombre<>'VOTO BLANCO')as total FROM candidato INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto where puesto.nombre = 'Diputado Listado Nacianal' group by partido_politico.nombre order by candidato.idcandidato";
            MostrarProductos(model2, tdiputados1, sql2);
            //Diputados2
            String sql3 = "SELECT partido_politico.nombre, sum(detalle_votos.cant_votos),(SELECT sum(detalle_votos.cant_votos) FROM candidato \n"
                    + "INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato \n"
                    + "INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido \n"
                    + "INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto \n"
                    + "where puesto.nombre = 'Diputado Parlacen' and partido_politico.nombre!='VOTO NULO' and partido_politico.nombre<>'VOTO BLANCO')as total FROM candidato INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto where puesto.nombre = 'Diputado Parlacen' group by partido_politico.nombre order by candidato.idcandidato";
            MostrarProductos(model3, tdiputados2, sql3);
            //Diputados3
            String sql4 = "SELECT partido_politico.nombre, sum(detalle_votos.cant_votos),(SELECT sum(detalle_votos.cant_votos) FROM candidato \n"
                    + "INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato \n"
                    + "INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido \n"
                    + "INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto \n"
                    + "where puesto.nombre = 'Diputado Distrital' and partido_politico.nombre!='VOTO NULO' and partido_politico.nombre<>'VOTO BLANCO')as total FROM candidato INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto where puesto.nombre = 'Diputado Distrital' group by partido_politico.nombre order by candidato.idcandidato";
            MostrarProductos(model4, tdiputados3, sql4);

//                if (cmunicipio.getSelectedIndex() == 0 || cmunicipio.getSelectedIndex() == -1) {
//                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Municipio para ver resultado de Alcalde");
//                    removejtable2(model5, talcalde);
//                } else {
//                    //Alcalde
            MMunicipio municip = (MMunicipio) cmunicipio.getSelectedItem();
            String idmun = municip.getID();
            //String sql5 = "SELECT partido_politico.nombre, sum(detalle_votos.cant_votos) FROM candidato INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto where puesto.nombre = 'Alcalde' and candidato.municipio_idmunicipio=" + idmun + " group by partido_politico.nombre";
            String sql5 = "SELECT municipio.nombre,partido_politico.nombre,SUM(detalle_votos.cant_votos) AS votos FROM mesa INNER JOIN detalle_votos ON mesa.idmesa = detalle_votos.mesa_idmesa INNER JOIN centro ON mesa.centro_idcentro = centro.idcentro\n"
                    + "INNER JOIN candidato candidato ON detalle_votos.candidato_idcandidato = candidato.idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto INNER JOIN municipio ON centro.municipio_idmunicipio = municipio.idmunicipio where puesto.nombre = 'Alcalde' group by partido_politico.nombre order by candidato.idcandidato";
            MostrarProductos2(model5, talcalde, sql5);

            //}
        } else {
            MMunicipio municip = (MMunicipio) cmunicipio.getSelectedItem();
            String idmun = municip.getID();
            talcalde.setModel(model5 = new DefaultTableModel(null, titulos2));
            //String sql = "SELECT partido_politico.nombre, sum(detalle_votos.cant_votos) FROM candidato INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto where puesto.nombre = 'Presidente' group by partido_politico.nombre";
            String sql = "SELECT partido_politico.nombre,SUM(detalle_votos.cant_votos) AS votos,puesto.nombre FROM mesa INNER JOIN detalle_votos ON mesa.idmesa = detalle_votos.mesa_idmesa INNER JOIN centro ON mesa.centro_idcentro = centro.idcentro\n"
                    + "INNER JOIN candidato candidato ON detalle_votos.candidato_idcandidato = candidato.idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto INNER JOIN municipio ON centro.municipio_idmunicipio = municipio.idmunicipio where puesto.nombre = 'Presidente' and centro.municipio_idmunicipio=" + idmun + " group by partido_politico.nombre order by candidato.idcandidato";
            MostrarProductos3(model, tpresidentes, sql);
            //Diputados1
            //String sql2 = "SELECT partido_politico.nombre, sum(detalle_votos.cant_votos) FROM candidato INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto where puesto.nombre = 'Diputado Listado Nacianal' group by partido_politico.nombre";
            String sql2 = "SELECT partido_politico.nombre,SUM(detalle_votos.cant_votos) AS votos,puesto.nombre FROM mesa INNER JOIN detalle_votos ON mesa.idmesa = detalle_votos.mesa_idmesa INNER JOIN centro ON mesa.centro_idcentro = centro.idcentro\n"
                    + "INNER JOIN candidato candidato ON detalle_votos.candidato_idcandidato = candidato.idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto INNER JOIN municipio ON centro.municipio_idmunicipio = municipio.idmunicipio where puesto.nombre = 'Diputado Listado Nacianal' and centro.municipio_idmunicipio=" + idmun + " group by partido_politico.nombre order by candidato.idcandidato";
            MostrarProductos3(model2, tdiputados1, sql2);
            //Diputados2
            //String sql3 = "SELECT partido_politico.nombre, sum(detalle_votos.cant_votos) FROM candidato INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto where puesto.nombre = 'Diputado Parlacen' group by partido_politico.nombre";
            String sql3 = "SELECT partido_politico.nombre,SUM(detalle_votos.cant_votos) AS votos,puesto.nombre FROM mesa INNER JOIN detalle_votos ON mesa.idmesa = detalle_votos.mesa_idmesa INNER JOIN centro ON mesa.centro_idcentro = centro.idcentro\n"
                    + "INNER JOIN candidato candidato ON detalle_votos.candidato_idcandidato = candidato.idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto INNER JOIN municipio ON centro.municipio_idmunicipio = municipio.idmunicipio where puesto.nombre = 'Diputado Parlacen' and centro.municipio_idmunicipio=" + idmun + " group by partido_politico.nombre order by candidato.idcandidato";
            MostrarProductos3(model3, tdiputados2, sql3);
            //Diputados3
            //String sql4 = "SELECT partido_politico.nombre, sum(detalle_votos.cant_votos) FROM candidato INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto where puesto.nombre = 'Diputado Distrital' group by partido_politico.nombre";
            String sql4 = "SELECT partido_politico.nombre,SUM(detalle_votos.cant_votos) AS votos,puesto.nombre FROM mesa INNER JOIN detalle_votos ON mesa.idmesa = detalle_votos.mesa_idmesa INNER JOIN centro ON mesa.centro_idcentro = centro.idcentro\n"
                    + "INNER JOIN candidato candidato ON detalle_votos.candidato_idcandidato = candidato.idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto INNER JOIN municipio ON centro.municipio_idmunicipio = municipio.idmunicipio where puesto.nombre = 'Diputado Distrital' and centro.municipio_idmunicipio=" + idmun + " group by partido_politico.nombre order by candidato.idcandidato";
            MostrarProductos3(model4, tdiputados3, sql4);

//                if (cmunicipio.getSelectedIndex() == 0 || cmunicipio.getSelectedIndex() == -1) {
//                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Municipio para ver resultado de Alcalde");
//                    removejtable2(model5, talcalde);
//                } else {
            //Alcalde
//                    MMunicipio municip = (MMunicipio) cmunicipio.getSelectedItem();
//                    String idmun = municip.getID();
            String sql5 = "SELECT partido_politico.nombre, sum(detalle_votos.cant_votos),puesto.nombre FROM candidato INNER JOIN detalle_votos ON candidato.idcandidato = detalle_votos.candidato_idcandidato INNER JOIN partido_politico ON candidato.partido_idpartido = partido_politico.idpartido INNER JOIN puesto ON candidato.puesto_idpuesto = puesto.idpuesto where puesto.nombre = 'Alcalde' and candidato.municipio_idmunicipio=" + idmun + " group by partido_politico.nombre order by candidato.idcandidato";
            MostrarProductos3(model5, talcalde, sql5);
            //}
        }
//        } else {
//            removejtable2(model, tpresidentes);
//            removejtable2(model2, tdiputados1);
//            removejtable2(model3, tdiputados2);
//            removejtable2(model4, tdiputados3);
//            removejtable2(model4, tdiputados3);
//            removejtable2(model5, talcalde);
//        }

    }

    private void MostrarProductos(DefaultTableModel modelo, JTable table, String sql) {
        removejtable2(modelo, table);
        modelo = getRegistroPorLikell(modelo, sql);
        Utilidades.ajustarAnchoColumnas(table);
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
                    fila[1] = rs.getInt(2);
                    if (rs.getString(1).equals("VOTO NULO") || rs.getString(1).equals("VOTO BLANCO")) {
                        fila[2] = "0";
                    } else {
                        float i = Float.parseFloat(rs.getString(2));
                        float j = Integer.parseInt(rs.getString(3));
                        float o = ((i / j) * 100);
                        o = (float) (Math.round(o * 100.0) / 100.0);
                        fila[2] = o;//((i/j)*100);/*"0.0";//*///rs.getInt(3);
                    }

//                    fila[3] = rs.getString(4);
//                    //fila[4] = rs.getString(5);
//                    //fila[5] = rs.getString(6);
//                    //fila[6] = 0.0;
//                    fila[4] = Double.parseDouble(rs.getString(5));
//                    if (Double.parseDouble(rs.getString(5)) > 0) {
//                        fila[5] = true;
//                    } else {
//                        fila[5] = false;
//                    }
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

    private void MostrarProductos3(DefaultTableModel modelo, JTable table, String sql) {
        removejtable2(modelo, table);
        modelo = getRegistroPorLikell3(modelo, sql);
        Utilidades.ajustarAnchoColumnas(table);
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
    public DefaultTableModel getRegistroPorLikell3(DefaultTableModel modelo, String tabla) {
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
                    fila[1] = rs.getInt(2);
                    if (rs.getString(1).equals("VOTO NULO") || rs.getString(1).equals("VOTO BLANCO")) {
                        fila[2] = "0";
                    } else {
                        float i = Float.parseFloat(rs.getString(2));
                        MMunicipio municip = (MMunicipio) cmunicipio.getSelectedItem();
                        String idmun = municip.toString();
                        float j = getGeneral(rs.getString(3), idmun);//Integer.parseInt(rs.getString(3));
                        float o = ((i / j) * 100);
                        o = (float) (Math.round(o * 100.0) / 100.0);
                        fila[2] = o;//((i/j)*100);/*"0.0";//*///rs.getInt(3);
                    }

//                    fila[3] = rs.getString(4);
//                    //fila[4] = rs.getString(5);
//                    //fila[5] = rs.getString(6);
//                    //fila[6] = 0.0;
//                    fila[4] = Double.parseDouble(rs.getString(5));
//                    if (Double.parseDouble(rs.getString(5)) > 0) {
//                        fila[5] = true;
//                    } else {
//                        fila[5] = false;
//                    }
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

    private void MostrarProductos2(DefaultTableModel modelo, JTable table, String sql) {
        removejtable2(modelo, table);
        modelo = getRegistroPorLikell2(modelo, sql);
        Utilidades.ajustarAnchoColumnas(table);
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
    public DefaultTableModel getRegistroPorLikell2(DefaultTableModel modelo, String tabla) {
        try {

            ResultSet rs;

            rs = acceso.getRegistroProc(tabla);
            int cantcampos = 4;
            //if (rs != null) {
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                //int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos];

                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    fila[0] = rs.getString(1);
                    fila[1] = rs.getString(2);
                    fila[2] = rs.getString(3);
                    //fila[3] = "0.0";//rs.getString(3);

                    if (rs.getString(2).equals("VOTO NULO") || rs.getString(2).equals("VOTO BLANCO")) {
                        fila[3] = "0";
                    } else {
                        float i = Float.parseFloat(rs.getString(3));
                        float j = getAlcalde(rs.getString(1));
                        //float j = Integer.parseInt(rs.getString(4));//modificar por el total
                        float o = ((i / j) * 100);
                        o = (float) (Math.round(o * 100.0) / 100.0);
                        fila[3] = o;//((i/j)*100);/*"0.0";//*///rs.getInt(3);
                    }
//                    fila[3] = rs.getString(4);
//                    //fila[4] = rs.getString(5);
//                    //fila[5] = rs.getString(6);
//                    //fila[6] = 0.0;
//                    fila[4] = Double.parseDouble(rs.getString(5));
//                    if (Double.parseDouble(rs.getString(5)) > 0) {
//                        fila[5] = true;
//                    } else {
//                        fila[5] = false;
//                    }
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

//    public void idalumnosengrupo(String idalumno, String idgrupo) {
//
//        String[] id = {idalumno, idgrupo};
//        ResultSet rs;
//        AccesoDatos ac = new AccesoDatos();
//        String[] cond = {"alumnosengrupo.alumno_idalumno", "alumnosengrupo.grupo_idgrupo"};
//        String[] campos = {"alumnosengrupo.iddetallegrupo", "alumnosengrupo.fechainicio", "alumnosengrupo.beca"};
//        rs = ac.getRegistros("alumnosengrupo", campos, cond, id, "");
//
//        if (rs != null) {
//            try {
//                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
//                    rs.beforeFirst();//regresa el puntero al primer registro
//                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
//                        iddetallegrupo = (rs.getString(1));
//                        String fechainicio = FormatoFecha.getFormato(rs.getDate(2), FormatoFecha.D_M_A);
//                        //inicioalumno.setText(fechainicio);
//                        float becac = Float.parseFloat(rs.getString(3));
//                        // beca.setText("" + becac);
//                    }
//                }
//            } catch (SQLException e) {
//                iddetallegrupo = "";
//                JOptionPane.showInternalMessageDialog(this, e);
//            }
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
        JPanelGrupo = new javax.swing.JPanel();
        bntGuardar = new elaprendiz.gui.button.ButtonRect();
        JPanelBusqueda = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cmunicipio = new javax.swing.JComboBox();
        seleccion = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        pnlPaginador1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        clockDigital2 = new elaprendiz.gui.varios.ClockDigital();
        tbPane = new elaprendiz.gui.panel.TabbedPaneHeader();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tpresidentes = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tdiputados1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tdiputados2 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tdiputados3 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        talcalde = new javax.swing.JTable();

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Registro de Votos");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("ConteoVotos"); // NOI18N
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

        panelImage.add(pnlActionButtons);
        pnlActionButtons.setBounds(0, 580, 880, 50);

        JPanelGrupo.setBackground(java.awt.SystemColor.activeCaption);
        JPanelGrupo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelGrupo.setForeground(new java.awt.Color(204, 204, 204));
        JPanelGrupo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JPanelGrupo.setLayout(null);

        bntGuardar.setBackground(new java.awt.Color(51, 153, 255));
        bntGuardar.setMnemonic(KeyEvent.VK_G);
        bntGuardar.setText("Actualizar");
        bntGuardar.setName("Guardar Pagos"); // NOI18N
        bntGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntGuardarActionPerformed(evt);
            }
        });
        JPanelGrupo.add(bntGuardar);
        bntGuardar.setBounds(50, 10, 100, 30);

        panelImage.add(JPanelGrupo);
        JPanelGrupo.setBounds(0, 90, 880, 50);

        JPanelBusqueda.setBackground(java.awt.SystemColor.inactiveCaption);
        JPanelBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelBusqueda.setLayout(null);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Seleccion Municipio:");
        JPanelBusqueda.add(jLabel10);
        jLabel10.setBounds(360, 20, 140, 20);
        jLabel10.setVisible(false);

        cmunicipio.setModel(modelCombo = new DefaultComboBoxModel());
        cmunicipio.setName("Profesor"); // NOI18N
        JPanelBusqueda.add(cmunicipio);
        cmunicipio.setBounds(510, 20, 240, 20);
        cmunicipio.setVisible(false);

        seleccion.setEditable(true);
        seleccion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "General", "Por Municipio" }));
        seleccion.setName("Seleccion"); // NOI18N
        JPanelBusqueda.add(seleccion);
        seleccion.setBounds(190, 20, 130, 20);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("Selección de Resultados:");
        JPanelBusqueda.add(jLabel12);
        jLabel12.setBounds(10, 20, 170, 20);

        panelImage.add(JPanelBusqueda);
        JPanelBusqueda.setBounds(0, 40, 880, 50);

        pnlPaginador1.setBackground(new java.awt.Color(57, 104, 163));
        pnlPaginador1.setPreferredSize(new java.awt.Dimension(786, 40));
        pnlPaginador1.setLayout(new java.awt.GridBagLayout());

        jLabel11.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("<--Registro de Votos-->");
        pnlPaginador1.add(jLabel11, new java.awt.GridBagConstraints());

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("   Hora   ");
        pnlPaginador1.add(jLabel21, new java.awt.GridBagConstraints());

        clockDigital2.setForeground(new java.awt.Color(255, 255, 255));
        clockDigital2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        pnlPaginador1.add(clockDigital2, new java.awt.GridBagConstraints());

        panelImage.add(pnlPaginador1);
        pnlPaginador1.setBounds(0, 0, 880, 40);

        tbPane.setOpaque(true);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setOpaque(false);

        tpresidentes.setModel(model = new DefaultTableModel(null, titulos2)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    if(column==4){
                        return true;
                    }else{
                        return false;}
                }
            });
            tpresidentes.setFocusCycleRoot(true);
            tpresidentes.setGridColor(new java.awt.Color(51, 51, 255));
            tpresidentes.setName("tpresidentes"); // NOI18N
            tpresidentes.setRowHeight(20);
            tpresidentes.setSelectionBackground(java.awt.SystemColor.activeCaption);
            jScrollPane2.setViewportView(tpresidentes);

            jPanel4.add(jScrollPane2, java.awt.BorderLayout.CENTER);

            tbPane.addTab("Presidente", jPanel4);

            jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jPanel3.setLayout(new java.awt.BorderLayout());

            jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            tdiputados1.setForeground(new java.awt.Color(51, 51, 51));
            tdiputados1.setModel(model2 = new DefaultTableModel(null, titulos2)
                {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        if(column==4){
                            return true;
                        }else{
                            return false;}
                    }
                });
                tdiputados1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                tdiputados1.setFocusCycleRoot(true);
                tdiputados1.setGridColor(new java.awt.Color(51, 51, 255));
                tdiputados1.setRowHeight(20);
                tdiputados1.setSelectionBackground(java.awt.SystemColor.activeCaption);
                tdiputados1.setSurrendersFocusOnKeystroke(true);
                tdiputados1.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        tdiputados1MouseClicked(evt);
                    }
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        tdiputados1MouseClicked1(evt);
                    }
                });
                tdiputados1.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        tdiputados1KeyPressed(evt);
                    }
                });
                jScrollPane4.setViewportView(tdiputados1);

                jPanel3.add(jScrollPane4, java.awt.BorderLayout.CENTER);

                tbPane.addTab("Diputado Listado Nacional", jPanel3);

                jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                jPanel5.setLayout(new java.awt.BorderLayout());

                jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                tdiputados2.setForeground(new java.awt.Color(51, 51, 51));
                tdiputados2.setModel(model3 = new DefaultTableModel(null, titulos2)
                    {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            if(column==4){
                                return true;
                            }else{
                                return false;}
                        }
                    });
                    tdiputados2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    tdiputados2.setFocusCycleRoot(true);
                    tdiputados2.setGridColor(new java.awt.Color(51, 51, 255));
                    tdiputados2.setRowHeight(20);
                    tdiputados2.setSelectionBackground(java.awt.SystemColor.activeCaption);
                    tdiputados2.setSurrendersFocusOnKeystroke(true);
                    tdiputados2.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            tdiputados2MouseClicked(evt);
                        }
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                            tdiputados2MouseClicked1(evt);
                        }
                    });
                    tdiputados2.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyPressed(java.awt.event.KeyEvent evt) {
                            tdiputados2KeyPressed(evt);
                        }
                    });
                    jScrollPane5.setViewportView(tdiputados2);

                    jPanel5.add(jScrollPane5, java.awt.BorderLayout.CENTER);

                    tbPane.addTab("Diputado Parlacen", jPanel5);

                    jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                    jPanel6.setLayout(new java.awt.BorderLayout());

                    jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    tdiputados3.setForeground(new java.awt.Color(51, 51, 51));
                    tdiputados3.setModel(model4 = new DefaultTableModel(null, titulos2)
                        {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                if(column==4){
                                    return true;
                                }else{
                                    return false;}
                            }
                        });
                        tdiputados3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                        tdiputados3.setFocusCycleRoot(true);
                        tdiputados3.setGridColor(new java.awt.Color(51, 51, 255));
                        tdiputados3.setRowHeight(20);
                        tdiputados3.setSelectionBackground(java.awt.SystemColor.activeCaption);
                        tdiputados3.setSurrendersFocusOnKeystroke(true);
                        tdiputados3.addMouseListener(new java.awt.event.MouseAdapter() {
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                tdiputados3MouseClicked(evt);
                            }
                            public void mousePressed(java.awt.event.MouseEvent evt) {
                                tdiputados3MouseClicked1(evt);
                            }
                        });
                        tdiputados3.addKeyListener(new java.awt.event.KeyAdapter() {
                            public void keyPressed(java.awt.event.KeyEvent evt) {
                                tdiputados3KeyPressed(evt);
                            }
                        });
                        jScrollPane6.setViewportView(tdiputados3);

                        jPanel6.add(jScrollPane6, java.awt.BorderLayout.CENTER);

                        tbPane.addTab("Diputado Distrital", jPanel6);

                        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                        jPanel7.setLayout(new java.awt.BorderLayout());

                        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                        talcalde.setForeground(new java.awt.Color(51, 51, 51));
                        talcalde.setModel(model5 = new DefaultTableModel(null, titulos2)
                            {
                                @Override
                                public boolean isCellEditable(int row, int column) {
                                    if(column==4){
                                        return true;
                                    }else{
                                        return false;}
                                }
                            });
                            talcalde.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                            talcalde.setFocusCycleRoot(true);
                            talcalde.setGridColor(new java.awt.Color(51, 51, 255));
                            talcalde.setRowHeight(20);
                            talcalde.setSelectionBackground(java.awt.SystemColor.activeCaption);
                            talcalde.setSurrendersFocusOnKeystroke(true);
                            talcalde.addMouseListener(new java.awt.event.MouseAdapter() {
                                public void mouseClicked(java.awt.event.MouseEvent evt) {
                                    talcaldeMouseClicked(evt);
                                }
                                public void mousePressed(java.awt.event.MouseEvent evt) {
                                    talcaldeMouseClicked1(evt);
                                }
                            });
                            talcalde.addKeyListener(new java.awt.event.KeyAdapter() {
                                public void keyPressed(java.awt.event.KeyEvent evt) {
                                    talcaldeKeyPressed(evt);
                                }
                            });
                            jScrollPane7.setViewportView(talcalde);

                            jPanel7.add(jScrollPane7, java.awt.BorderLayout.CENTER);

                            tbPane.addTab("Alcalde", jPanel7);

                            panelImage.add(tbPane);
                            tbPane.setBounds(0, 140, 880, 440);

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

    private void tdiputados1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tdiputados1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tdiputados1KeyPressed

    private void tdiputados1MouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdiputados1MouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_tdiputados1MouseClicked1

    private void tdiputados1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdiputados1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tdiputados1MouseClicked

    private void bntGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardarActionPerformed
        // TODO add your handling code here:
        llenartablas();
    }//GEN-LAST:event_bntGuardarActionPerformed

    private void tdiputados2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdiputados2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tdiputados2MouseClicked

    private void tdiputados2MouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdiputados2MouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_tdiputados2MouseClicked1

    private void tdiputados2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tdiputados2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tdiputados2KeyPressed

    private void tdiputados3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdiputados3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tdiputados3MouseClicked

    private void tdiputados3MouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdiputados3MouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_tdiputados3MouseClicked1

    private void tdiputados3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tdiputados3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tdiputados3KeyPressed

    private void talcaldeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_talcaldeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_talcaldeMouseClicked

    private void talcaldeMouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_talcaldeMouseClicked1
        // TODO add your handling code here:
    }//GEN-LAST:event_talcaldeMouseClicked1

    private void talcaldeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_talcaldeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_talcaldeKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelGrupo;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.varios.ClockDigital clockDigital2;
    private javax.swing.JComboBox cmunicipio;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel21;
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
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador1;
    private javax.swing.JComboBox seleccion;
    private javax.swing.JTable talcalde;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane;
    private javax.swing.JTable tdiputados1;
    private javax.swing.JTable tdiputados2;
    private javax.swing.JTable tdiputados3;
    private javax.swing.JTable tpresidentes;
    // End of variables declaration//GEN-END:variables
}
