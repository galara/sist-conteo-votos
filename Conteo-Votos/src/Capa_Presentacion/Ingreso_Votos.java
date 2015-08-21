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
import Capa_Negocio.Peticiones;
import Capa_Negocio.Renderer_CheckBox;
import Capa_Negocio.TableCellFormatter;
import Capa_Negocio.Utilidades;
import static Capa_Presentacion.Principal.dp;
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
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author GLARA
 */
public class Ingreso_Votos extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model, model2, model3, model4, model5;

    //DefaultComboBoxModel modelCombo;
    //String[] titulos = {"Id", "Codigo", "Nombre Candidato", "Partido", "Candidatura", "Municipio", "Mora", "Subtotal", "Pagar Mora", "Pagar Mes"};//Titulos para Jtabla
    String[] titulos2 = {"Id", "Código", "Nombre Candidato", "Partido", "Candidatura", "Municipio", "Votos", "Ingresado"};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();
    //public static Hashtable<String, String> hashGrupo = new Hashtable<>();
    //public static Hashtable<String, String> hashTipopago = new Hashtable<>();
    AccesoDatos acceso = new AccesoDatos();
    static String idmesa = "";
    java.sql.Connection conn;//getConnection intentara establecer una conexión.

    /**
     * Creates new form Cliente
     */
    public Ingreso_Votos() {
        initComponents();
        setFiltroTexto();
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
        int nu = JOptionPane.showInternalConfirmDialog(this, "Todos los datos que no se ha guardado"
                + "se perderan.\n"
                + "¿Desea Cerrar esta ventana?", "Cerrar ventana", JOptionPane.YES_NO_OPTION);
        if (nu == JOptionPane.YES_OPTION || nu == 0) {
            Utilidades.setEditableTexto(this.JPanelGrupo, false, null, true, "");
            Utilidades.esObligatorio(this.JPanelGrupo, false);
            this.bntGuardar.setEnabled(false);
            //removejtable2();
            codigomesa.setText("");
            codigomesa.requestFocus();
            this.dispose();
        }
    }

    public void removejtable2(DefaultTableModel modelo, JTable table) {
        while (table.getRowCount() != 0) {
            modelo.removeRow(0);
        }
    }

    private void limpiartodo() {
        codigomesa.setText("");
        //codigomesa.requestFocus();
        idcentro.setText("");
        idmunicipio.setText("");
        nombrecentro.setText("");
        nombremunicipio.setText("");
        Utilidades.esObligatorio(this.JPanelBusqueda, false);
        codigomesa.requestFocus();
        removejtable2(model, tpresidentes);
        removejtable2(model2, tdiputados1);
        removejtable2(model3, tdiputados2);
        removejtable2(model4, tdiputados3);
        removejtable2(model4, tdiputados3);
        removejtable2(model5, talcalde);
    }

    /*
     * Metodo para buscar un alumno por su codigo devuelde el id
     */
    public void balumnocodigo(String codigo) {
        if (codigo.isEmpty()) {
            limpiartodo();
            //nombrecentro.setText("");
            //estado.setText("");
            //cGrupo.removeAllItems();
            //idmesa = "";
            //inicioalumno.setText("");
            //beca.setText("");
            //dia.setText("");

        } else if (!codigo.isEmpty()) {

            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();
            String[] campos = {"mesa.nombre", "centro.nombre", "mesa.estado", "mesa.idmesa", "municipio.nombre", "centro.idcentro", "municipio.idmunicipio"};
            String[] condiciones = {"mesa.estado=1 and mesa.nombre"};
            String[] id = {codigo};
            String inner = " INNER JOIN centro on mesa.centro_idcentro=centro.idcentro INNER JOIN municipio on centro.municipio_idmunicipio=municipio.idmunicipio ";

//            String[] campos = {"alumno.codigo", "alumno.nombres", "alumno.apellidos", "DATE_FORMAT(alumno.fechanacimiento,'%d-%m-%Y')", "alumno.estado", "alumno.idalumno"};
//            String[] cond = {"alumno.codigo"};
//            String[] id = {codigo};
            if (!id.equals(0)) {

                rs = ac.getRegistros("mesa", campos, condiciones, id, inner);

                if (rs != null) {
                    try {
                        if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                            rs.beforeFirst();//regresa el puntero al primer registro
                            while (rs.next()) {//mientras tenga registros que haga lo siguiente
                                codigomesa.setText(rs.getString(1));
                                nombrecentro.setText(rs.getString(2));
//                                if (rs.getString(3).equals("0")) {
//                                    estado.setText("Inactivo");
//                                    estado.setForeground(Color.red);
//                                } else if (rs.getString(3).equals("1")) {
//                                    estado.setText("Activo");
//                                    estado.setForeground(Color.WHITE/*new java.awt.Color(102, 204, 0)*/);
//                                }
                                idmesa = (rs.getString(4));
                                nombremunicipio.setText(rs.getString(5));
                                idcentro.setText(rs.getString(6));
                                idmunicipio.setText(rs.getString(7));
                                llenartablas();
                            }
                        } else {
                            JOptionPane.showInternalMessageDialog(this, " El codigo no fue encontrado ");
                            limpiartodo();
                            idmesa = null;
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
//    public DefaultTableModel getRegistroPorLikel(DefaultTableModel modelo, String tabla) {
//        try {
//
//            ResultSet rs;
//
//            rs = acceso.getRegistroProc(tabla);
//            int cantcampos = 9;
//            //if (rs != null) {
//            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
//                //int count = 0;
//                rs.beforeFirst();//regresa el puntero al primer registro
//                Object[] fila = new Object[cantcampos + 1];
//
//                while (rs.next()) {//mientras tenga registros que haga lo siguiente
//                    // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
//                    for (int i = 0; i < cantcampos - 2; i++) {
//
//                        fila[i] = rs.getObject(i + 1); // El primer indice en rs es el 1, no el cero, por eso se suma 1.
//                        if (i == 4) {
//                            float monto = (float) rs.getObject(i + 1);
//                            //float cbeca = Float.parseFloat(beca.getText());
//                            float resultado = (float) (Math.round((monto) * 100.0) / 100.0);
//                            fila[i] = resultado;
//                        }
//                        if (i == 6) {
//                            if (fila[i] == "0.0") {
//                                fila[i] = "0.0";
//                            } else {
//                                float mora = (float) rs.getFloat(i + 1);
//                                float resultado = (float) (Math.round(mora * 100.0) / 100.0);
//                                fila[i] = resultado;
//                            }
//                        }
//                        if (fila[i] == null) {
//                            fila[i] = "";
//                        } else {
//                        }
//                    }
//                    fila[7] = (float) (Math.round(((float) fila[4] + ((float) fila[6])) * 100.0) / 100.0);
//                    if (((float) fila[6] == 0.0)) {
//                        fila[8] = false;
//                    } else {
//                        fila[8] = true;
//                    }
//                    fila[9] = false;
//                    modelo.addRow(fila);
//                }
//
//            } //} 
//            else {
//                // JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Mensage", JOptionPane.INFORMATION_MESSAGE);
//            }
//            rs.close();
//            return modelo;
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
//            return null;
//        }
//    }
    private void llenartablas() {

        if (idmesa != null || !codigomesa.getText().isEmpty()) {

            //Presidentes
            String sql = "select candidato.idcandidato, candidato.codigo, concat(candidato.nombres,' ',candidato.apellidos)AS nombre, partido_politico.nombre, puesto.nombre, municipio.nombre,\n"
                    + "        IFNULL((SELECT detalle_votos.cant_votos FROM detalle_votos where candidato.idcandidato = detalle_votos.candidato_idcandidato and detalle_votos.mesa_idmesa=" + "'" + idmesa + "'),0.0) AS 'votoss'\n"
                    + "        from candidato INNER JOIN partido_politico on candidato.partido_idpartido=partido_politico.idpartido INNER JOIN puesto on candidato.puesto_idpuesto=puesto.idpuesto INNER JOIN municipio on candidato.municipio_idmunicipio=municipio.idmunicipio   where puesto.nombre = 'Presidente' order by candidato.idcandidato";
            MostrarProductos(model, tpresidentes, sql);

            //Diputados1
            String sql2 = "select candidato.idcandidato, candidato.codigo, concat(candidato.nombres,' ',candidato.apellidos)AS nombre, partido_politico.nombre, puesto.nombre, municipio.nombre,\n"
                    + "        IFNULL((SELECT detalle_votos.cant_votos FROM detalle_votos where candidato.idcandidato = detalle_votos.candidato_idcandidato and detalle_votos.mesa_idmesa=" + "'" + idmesa + "'),0.0) AS 'votoss'\n"
                    + "        from candidato INNER JOIN partido_politico on candidato.partido_idpartido=partido_politico.idpartido INNER JOIN puesto on candidato.puesto_idpuesto=puesto.idpuesto INNER JOIN municipio on candidato.municipio_idmunicipio=municipio.idmunicipio   where puesto.nombre = 'Diputados1' order by candidato.idcandidato";
            MostrarProductos(model2, tdiputados1, sql2);

            //Diputados2
            String sql3 = "select candidato.idcandidato, candidato.codigo, concat(candidato.nombres,' ',candidato.apellidos)AS nombre, partido_politico.nombre, puesto.nombre, municipio.nombre,\n"
                    + "        IFNULL((SELECT detalle_votos.cant_votos FROM detalle_votos where candidato.idcandidato = detalle_votos.candidato_idcandidato and detalle_votos.mesa_idmesa=" + "'" + idmesa + "'),0.0) AS 'votoss'\n"
                    + "        from candidato INNER JOIN partido_politico on candidato.partido_idpartido=partido_politico.idpartido INNER JOIN puesto on candidato.puesto_idpuesto=puesto.idpuesto INNER JOIN municipio on candidato.municipio_idmunicipio=municipio.idmunicipio   where puesto.nombre = 'Diputados2' order by candidato.idcandidato";
            MostrarProductos(model3, tdiputados2, sql3);

            //Diputados3
            String sql4 = "select candidato.idcandidato, candidato.codigo, concat(candidato.nombres,' ',candidato.apellidos)AS nombre, partido_politico.nombre, puesto.nombre, municipio.nombre,\n"
                    + "        IFNULL((SELECT detalle_votos.cant_votos FROM detalle_votos where candidato.idcandidato = detalle_votos.candidato_idcandidato and detalle_votos.mesa_idmesa=" + "'" + idmesa + "'),0.0) AS 'votoss'\n"
                    + "        from candidato INNER JOIN partido_politico on candidato.partido_idpartido=partido_politico.idpartido INNER JOIN puesto on candidato.puesto_idpuesto=puesto.idpuesto INNER JOIN municipio on candidato.municipio_idmunicipio=municipio.idmunicipio   where puesto.nombre = 'Diputados3' order by candidato.idcandidato";
            MostrarProductos(model4, tdiputados3, sql4);

            //Alcalde
            String sql5 = "select candidato.idcandidato, candidato.codigo, concat(candidato.nombres,' ',candidato.apellidos)AS nombre, partido_politico.nombre, puesto.nombre, municipio.nombre,\n"
                    + "        IFNULL((SELECT detalle_votos.cant_votos FROM detalle_votos where candidato.idcandidato = detalle_votos.candidato_idcandidato and detalle_votos.mesa_idmesa=" + "'" + idmesa + "'),0.0) AS 'votoss'\n"
                    + "        from candidato INNER JOIN partido_politico on candidato.partido_idpartido=partido_politico.idpartido INNER JOIN puesto on candidato.puesto_idpuesto=puesto.idpuesto INNER JOIN municipio on candidato.municipio_idmunicipio=municipio.idmunicipio   where puesto.nombre = 'Alcalde' and candidato.municipio_idmunicipio="+idmunicipio.getText()+" order by candidato.idcandidato ";
            MostrarProductos(model5, talcalde, sql5);
        } else {
            removejtable2(model, tpresidentes);
            removejtable2(model2, tdiputados1);
            removejtable2(model3, tdiputados2);
            removejtable2(model4, tdiputados3);
            removejtable2(model4, tdiputados3);
            removejtable2(model5, talcalde);
        }

    }

    private void MostrarProductos(DefaultTableModel modelo, JTable table, String sql) {

        //String sql = "select candidato.idcandidato, candidato.codigo, concat(candidato.nombres,' ',candidato.apellidos)AS nombre, partido_politico.nombre, puesto.nombre, municipio.nombre from candidato INNER JOIN partido_politico on candidato.partido_idpartido=partido_politico.idpartido INNER JOIN puesto on candidato.puesto_idpuesto=puesto.idpuesto INNER JOIN municipio on candidato.municipio_idmunicipio=municipio.idmunicipio   where puesto.nombre = 'Presidente' order by candidato.idcandidato";
//        String sql = "select candidato.idcandidato, candidato.codigo, concat(candidato.nombres,' ',candidato.apellidos)AS nombre, partido_politico.nombre, puesto.nombre, municipio.nombre,\n" +
//        "        IFNULL((SELECT detalle_votos.cant_votos FROM detalle_votos where candidato.idcandidato = detalle_votos.candidato_idcandidato and detalle_votos.mesa_idmesa="+"'"+idmesa+"'),0.0) AS 'votoss'\n" +
//        "        from candidato INNER JOIN partido_politico on candidato.partido_idpartido=partido_politico.idpartido INNER JOIN puesto on candidato.puesto_idpuesto=puesto.idpuesto INNER JOIN municipio on candidato.municipio_idmunicipio=municipio.idmunicipio   where puesto.nombre = 'Presidente' order by candidato.idcandidato";
//        
        removejtable2(modelo, table);

        JCheckBox check = new JCheckBox();
        table.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(check));
        table.getColumnModel().getColumn(7).setCellRenderer(new Renderer_CheckBox());
        CellEditorSpinnerPago cnt = new CellEditorSpinnerPago(1);
        table.getColumnModel().getColumn(6).setCellEditor(cnt);
        table.getColumnModel().getColumn(6).setCellRenderer(new TableCellFormatter(null));

        modelo = getRegistroPorLikell(modelo, sql);
        Utilidades.ajustarAnchoColumnas(table);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.doLayout();

        //removejtable2();
        //model2 = getRegistroPorLikell(model2, sql);
        //Utilidades.ajustarAnchoColumnas(tpresidentes);
        //tpresidentes.getColumnModel().getColumn(0).setMaxWidth(0);
        //tpresidentes.getColumnModel().getColumn(0).setMinWidth(0);
        //tpresidentes.getColumnModel().getColumn(0).setPreferredWidth(0);
        //tpresidentes.doLayout();
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
                    //fila[6] = 0.0;
                    fila[6] = Double.parseDouble(rs.getString(7));
                    if (Double.parseDouble(rs.getString(7)) > 0) {
                        fila[7] = true;
                    } else {
                        fila[7] = false;
                    }
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
        bntGuardar = new elaprendiz.gui.button.ButtonRect();
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        bntGuardar1 = new elaprendiz.gui.button.ButtonRect();
        JPanelGrupo = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        JPanelTable = new javax.swing.JPanel();
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
        JPanelBusqueda = new javax.swing.JPanel();
        codigomesa = new elaprendiz.gui.textField.TextField();
        jLabel16 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        nombrecentro = new elaprendiz.gui.textField.TextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        nombremunicipio = new elaprendiz.gui.textField.TextField();
        idcentro = new elaprendiz.gui.textField.TextField();
        idmunicipio = new elaprendiz.gui.textField.TextField();
        JPanelRecibo = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        clockDigital2 = new elaprendiz.gui.varios.ClockDigital();
        pnlPaginador1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Registro de Votos");
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
        bntGuardar1.setText("Resultado General");
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

        jButton2.setText("Actualizar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        JPanelGrupo.add(jButton2);
        jButton2.setBounds(120, 10, 100, 23);

        panelImage.add(JPanelGrupo);
        JPanelGrupo.setBounds(0, 160, 880, 50);

        JPanelTable.setOpaque(false);
        JPanelTable.setPreferredSize(new java.awt.Dimension(786, 402));
        JPanelTable.setLayout(new java.awt.BorderLayout());

        tbPane.setOpaque(true);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setOpaque(false);

        tpresidentes.setModel(model = new DefaultTableModel(null, titulos2)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    if(column==6 || column==7){
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

            tbPane.addTab("Presidentes", jPanel4);

            jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jPanel3.setLayout(new java.awt.BorderLayout());

            jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            tdiputados1.setForeground(new java.awt.Color(51, 51, 51));
            tdiputados1.setModel(model2 = new DefaultTableModel(null, titulos2)
                {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        if(column==6 || column==7){
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

                tbPane.addTab("Diputado 1", jPanel3);

                jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                jPanel5.setLayout(new java.awt.BorderLayout());

                jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                tdiputados2.setForeground(new java.awt.Color(51, 51, 51));
                tdiputados2.setModel(model3 = new DefaultTableModel(null, titulos2)
                    {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            if(column==6 || column==7){
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

                    tbPane.addTab("Diputado 2", jPanel5);

                    jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                    jPanel6.setLayout(new java.awt.BorderLayout());

                    jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    tdiputados3.setForeground(new java.awt.Color(51, 51, 51));
                    tdiputados3.setModel(model4 = new DefaultTableModel(null, titulos2)
                        {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                if(column==6 || column==7){
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

                        tbPane.addTab("Diputado 3", jPanel6);

                        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                        jPanel7.setLayout(new java.awt.BorderLayout());

                        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                        talcalde.setForeground(new java.awt.Color(51, 51, 51));
                        talcalde.setModel(model5 = new DefaultTableModel(null, titulos2)
                            {
                                @Override
                                public boolean isCellEditable(int row, int column) {
                                    if(column==6 || column==7){
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

                            JPanelTable.add(tbPane, java.awt.BorderLayout.CENTER);

                            panelImage.add(JPanelTable);
                            JPanelTable.setBounds(0, 210, 880, 370);

                            JPanelBusqueda.setBackground(java.awt.SystemColor.inactiveCaption);
                            JPanelBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                            JPanelBusqueda.setLayout(null);

                            codigomesa.setPreferredSize(new java.awt.Dimension(250, 27));
                            codigomesa.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    codigomesaActionPerformed(evt);
                                }
                            });
                            JPanelBusqueda.add(codigomesa);
                            codigomesa.setBounds(120, 10, 97, 24);

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

                            nombrecentro.setEditable(false);
                            nombrecentro.setPreferredSize(new java.awt.Dimension(250, 27));
                            JPanelBusqueda.add(nombrecentro);
                            nombrecentro.setBounds(390, 10, 190, 24);

                            jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                            jLabel19.setText("Centro:");
                            JPanelBusqueda.add(jLabel19);
                            jLabel19.setBounds(320, 10, 60, 24);

                            jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                            jLabel20.setText("Municipio:");
                            JPanelBusqueda.add(jLabel20);
                            jLabel20.setBounds(590, 10, 69, 24);

                            nombremunicipio.setEditable(false);
                            nombremunicipio.setPreferredSize(new java.awt.Dimension(250, 27));
                            JPanelBusqueda.add(nombremunicipio);
                            nombremunicipio.setBounds(660, 10, 200, 24);

                            idcentro.setEditable(false);
                            idcentro.setVisible(false);
                            idcentro.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                            idcentro.setPreferredSize(new java.awt.Dimension(120, 21));
                            JPanelBusqueda.add(idcentro);
                            idcentro.setBounds(270, 10, 20, 24);

                            idmunicipio.setEditable(false);
                            idmunicipio.setVisible(false);
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

                            panelImage.add(JPanelRecibo);
                            JPanelRecibo.setBounds(0, 40, 880, 70);

                            pnlPaginador1.setBackground(new java.awt.Color(57, 104, 163));
                            pnlPaginador1.setPreferredSize(new java.awt.Dimension(786, 40));
                            pnlPaginador1.setLayout(new java.awt.GridBagLayout());

                            jLabel11.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
                            jLabel11.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/update.png"))); // NOI18N
                            jLabel11.setText("<--Registro de Votos-->");
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        BuscarAlumno frmBuscarAlumno = new BuscarAlumno();
        if (frmBuscarAlumno == null) {
            frmBuscarAlumno = new BuscarAlumno();
        }
        adminInternalFrame(dp, frmBuscarAlumno);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void codigomesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigomesaActionPerformed
        // TODO add your handling code here:

        balumnocodigo(codigomesa.getText());

    }//GEN-LAST:event_codigomesaActionPerformed

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
        if (AccesoUsuario.AccesosUsuario(bntGuardar.getName()) == true) {

            if (Utilidades.esObligatorio(this.JPanelBusqueda, true)) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

//            if (tdiputados1.getRowCount() == 0 /*&& colegiaturas.getSelectedRow() == -1*/ && tpresidentes.getRowCount() == 0) {
//                JOptionPane.showMessageDialog(null, "La tabla no contiene datos");
//            } else { //Inicio de Guardar datos
            int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
            if (resp == 0) {

                //idcentro.getText();
                //idmunicipio.getText();
                int n = 0;
                PreparedStatement ps = null;
                conn = BdConexion.getConexion();

                try {
                    conn.setAutoCommit(false);

                    DefaultTableModel modelo = null;
                    JTable tabla = null;

                    for (int j = 0; j < 5; j++) {

                        if (j == 0) {
                            modelo = model;
                            tabla = tpresidentes;
                        } else if (j == 1) {
                            modelo = model2;
                            tabla = tdiputados1;
                        } else if (j == 2) {
                            modelo = model3;
                            tabla = tdiputados2;
                        } else if (j == 3) {
                            modelo = model4;
                            tabla = tdiputados3;
                        } else if (j == 4) {
                            modelo = model5;
                            tabla = talcalde;
                        }

                        if (modelo.getRowCount() != -1 || modelo.getRowCount() > 0) {

                            for (int i = 0; i < modelo.getRowCount(); i++) { //for pago de meses
                                if (tabla.getValueAt(i, 7).toString().equals("false") && Float.parseFloat(""+tabla.getValueAt(i, 6)) > 0 ) {
                                    // String idcandidato = (String) "" + tabla.getValueAt(i, 0);
                                    String sql = "INSERT INTO detalle_votos (cant_votos, candidato_idcandidato, mesa_idmesa, usuario_idusuario) VALUES (?, ?, ?, ?)";
                                    ps = conn.prepareStatement(sql);
                                    ps.setString(1, "" + tabla.getValueAt(i, 6));
                                    ps.setInt(2, Integer.parseInt("" + tabla.getValueAt(i, 0)));
                                    ps.setInt(3, Integer.parseInt(idmesa));
                                    ps.setFloat(4, AccesoUsuario.getIdusuario());
                                    n = ps.executeUpdate();

                                } else if (tabla.getValueAt(i, 7).toString().equals("true") && Float.parseFloat(""+tabla.getValueAt(i, 6)) > 0 ) {
                                    String idcandidato = (String) "" + tabla.getValueAt(i, 0);
                                    String sql2 = "update detalle_votos set  cant_votos=? where mesa_idmesa=" + idmesa + " and  candidato_idcandidato=" + idcandidato;
                                    ps = conn.prepareStatement(sql2);
                                    ps.setString(1, "" + tabla.getValueAt(i, 6));
                                    n = ps.executeUpdate();
                                }
                            }//fin for pago de meses
                        }
                    }

                    if (n > 0) {
                        JOptionPane.showInternalMessageDialog(this, "Se ha Guardado Correctamente", "Error", JOptionPane.INFORMATION_MESSAGE);
                        llenartablas();
                    }

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
                        Logger.getLogger(Ingreso_Votos.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
            //}//Fin Guardar datos
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
   
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntGuardar1ActionPerformed

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        balumnocodigo(codigomesa.getText());
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelGrupo;
    private javax.swing.JPanel JPanelRecibo;
    private javax.swing.JPanel JPanelTable;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntGuardar1;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.varios.ClockDigital clockDigital2;
    public static elaprendiz.gui.textField.TextField codigomesa;
    public static elaprendiz.gui.textField.TextField idcentro;
    public static elaprendiz.gui.textField.TextField idmunicipio;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
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
    public static elaprendiz.gui.textField.TextField nombrecentro;
    public static elaprendiz.gui.textField.TextField nombremunicipio;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador1;
    private javax.swing.JTable talcalde;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane;
    private javax.swing.JTable tdiputados1;
    private javax.swing.JTable tdiputados2;
    private javax.swing.JTable tdiputados3;
    private javax.swing.JTable tpresidentes;
    // End of variables declaration//GEN-END:variables
}
