/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Capa_Presentacion;

import Capa_Datos.AccesoDatos;
import Capa_Negocio.AccesoUsuario;
import static Capa_Negocio.AddForms.adminInternalFrame;
import Capa_Negocio.FiltroCampos;
import Capa_Negocio.FormatoFecha;
import Capa_Negocio.GeneraCodigo;
import Capa_Negocio.Peticiones;
import Capa_Negocio.TipoFiltro;
import Capa_Negocio.Utilidades;
import static Capa_Presentacion.Principal.dp;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import modelos.mCarrera;
import modelos.mMunicipio;
import modelos.mProfesor;

/**
 *
 * @author GLARA
 */
public class Candidato extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model;
    DefaultComboBoxModel modelCombo;
    String[] titulos = {"Codigo", "Partido", "Candidatura", "Municipio"};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();
    public Hashtable<String, String> hashProfesor = new Hashtable<>();
    public Hashtable<String, String> hashCarrera = new Hashtable<>();
    public Hashtable<String, String> hashMunicipio = new Hashtable<>();
    int newcodcandidato, idcandidato;
    //private static Profesor frmProfesor = new Profesor();
    //private static Carrera frmCarrera = new Carrera();
    /*Se hace una instancia de la clase que recibira las peticiones de mensages de la capa de aplicación*/

    //public static JOptionMessage msg = new JOptionMessage();
    /**
     * Creates new form Cliente
     */
    public Candidato() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();
        //llenarcombopartido_politico();
        //llenarcombopuesto();
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
            this.bntModificar.setEnabled(false);
            this.bntEliminar.setEnabled(false);
            this.bntNuevo.setEnabled(true);
            removejtable();
            busqueda.setText("");
            rbNombres.setSelected(true);
            rbCodigo.setSelected(false);
            rbApellidos.setSelected(false);
            busqueda.requestFocus();
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
        while (horarios.getRowCount() != 0) {
            model.removeRow(0);
        }
    }

    /*
     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
     *y se los envia a un metodo interno getRegistroCombo() 
     *
     */
    public void llenarcombopartido_politico() {
        String Dato = "1";
        String[] campos = {"nombre", "idpartido"};
        String[] condiciones = {"estado"};
        String[] Id = {Dato};
        partido.removeAllItems();
        //Component cmps = partido_politico;
        getRegistroCombo("partido_politico", campos, condiciones, Id);

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
                partido.setModel(modeloComboBox);

                modeloComboBox.addElement(new mProfesor("", "0"));
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    Object[] fila = new Object[cantcampos];
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        count++;
                        modeloComboBox.addElement(new mProfesor(rs.getString(1), "" + rs.getInt(2)));
                        hashProfesor.put(rs.getString(1), "" + count);
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
    public void llenarcombopuesto() {
        String Dato = "1";
        String[] campos = {"nombre", "idpuesto"};
        String[] condiciones = {"estado"};
        String[] Id = {Dato};
        puesto.removeAllItems();
        Component cmps = puesto;
        getRegistroCombopuesto("puesto", campos, condiciones, Id);

    }

    /*El metodo llenarcombo() envia los parametros para la consulta a la BD y el medoto
     *getRegistroCombo() se encarga de enviarlos a la capa de AccesoDatos.getRegistros()
     *quiern devolcera un ResultSet para luego obtener los valores y agregarlos al JConboBox
     *y a una Hashtable que nos servira para obtener el id y seleccionar valores.
     */
    public void getRegistroCombopuesto(String tabla, String[] campos, String[] campocondicion, String[] condicionid) {
        try {
            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getRegistros(tabla, campos, campocondicion, condicionid, "");

            int cantcampos = campos.length;
            if (rs != null) {

                DefaultComboBoxModel modeloComboBox;
                modeloComboBox = new DefaultComboBoxModel();
                puesto.setModel(modeloComboBox);

                modeloComboBox.addElement(new mCarrera("", "0"));
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    Object[] fila = new Object[cantcampos];
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        count++;
                        modeloComboBox.addElement(new mCarrera(rs.getString(1), "" + rs.getInt(2)));
                        hashCarrera.put(rs.getString(1), "" + count);
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
    public void llenarcombomunicipio() {
        String Dato = "1";
        String[] campos = {"nombre", "idmunicipio"};
        String[] condiciones = {"estado"};
        String[] Id = {Dato};
        Cmunicipio.removeAllItems();
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
                Cmunicipio.setModel(modeloComboBox);

                modeloComboBox.addElement(new mMunicipio("", "0"));
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    Object[] fila = new Object[cantcampos];
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        count++;
                        modeloComboBox.addElement(new mMunicipio(rs.getString(1), "" + rs.getInt(2)));
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

    /* Este metodo se encarga de filtrar los datos que se deben ingresar en cada uno de los campos del formulario
     * podemos indicar que el usuario ingrese solo numeros , solo letras, numeros y letras, o cualquier caracter
     * tambien podemos validar si se aseptaran espacios en blanco en la cadena ingresada , para mas detalle visualizar
     * la clase TipoFiltro()  */
    private void setFiltroTexto() {

        TipoFiltro.setFiltraEntrada(codigo.getDocument(), FiltroCampos.NUM_LETRAS, 45, false);
        //TipoFiltro.setFiltraEntrada(nombres.getDocument(), FiltroCampos.NUM_LETRAS, 60, true);
        //TipoFiltro.setFiltraEntrada(dia.getDocument(), FiltroCampos.SOLO_LETRAS, 45, false);
        //TipoFiltro.setFiltraEntrada(partido_politico.getDocument(), FiltroCampos.NUM_LETRAS, 200, true);
        //TipoFiltro.setFiltraEntrada(cantalumnos.getDocument(), FiltroCampos.SOLO_NUMEROS, 5, true);
//        TipoFiltro.setFiltraEntrada(colegiatura.getDocument(), FiltroCampos.SOLO_NUMEROS, 12, false);
        TipoFiltro.setFiltraEntrada(busqueda.getDocument(), FiltroCampos.NUM_LETRAS, 100, true);
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
        //String[] titulos = {"Codigo", "Descripción", "Dia", "Profesor","Carrera", "Hora De", "Hora A", "Fecha Inicio","Fecha Fin", "Alumnos","Estado"};//Titulos para Jtabla
        //String conct = "concat(candidato.nombres,' ',candidato.apellidos)";
        String[] campos = {"candidato.codigo", "partido_politico.nombre", "puesto.nombre", "municipio.nombre"};
        //String[] titulos = {"Codigo", "Nombre", "Partido", "Cargo", "Municipio"};//Titulos para Jtabla
        //String[] campos = {"codigo", "descripcion", "dia", "horariode", "horarioa", "fechainicio", "estado"};
        String[] condiciones = {"candidato.codigo"};
        String[] Id = {Dato};
        String inner = " INNER JOIN partido_politico on candidato.partido_idpartido=partido_politico.idpartido INNER JOIN puesto on candidato.puesto_idpuesto=puesto.idpuesto INNER JOIN municipio on candidato.municipio_idmunicipio=municipio.idmunicipio ";

        if (this.rbCodigo.isSelected()) {
            if (!Dato.isEmpty()) {
                removejtable();
                Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
                Utilidades.esObligatorio(this.JPanelCampos, false);
                model = peticiones.getRegistroPorPks(model, "candidato", campos, condiciones, Id, inner);
            } else {
                JOptionPane.showInternalMessageDialog(this, "Debe ingresar un codigo para la busqueda");
            }
        }
        if (this.rbNombres.isSelected()) {
            removejtable();
            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
            Utilidades.esObligatorio(this.JPanelCampos, false);
            model = peticiones.getRegistroPorLike(model, "candidato", campos, "partido_politico.nombre", Dato, inner);
        }
        if (this.rbApellidos.isSelected()) {
            removejtable();
            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
            Utilidades.esObligatorio(this.JPanelCampos, false);
            model = peticiones.getRegistroPorLike(model, "candidato", campos, "municipio.nombre", Dato, inner);
        }
        Utilidades.ajustarAnchoColumnas(horarios);
    }

//    /* Este metodo  consulta en la BD el codigo de la fila seleccionada y llena los componentes
//     * de la parte superior del formulario con los datos obtenidos en la capa de Negocio getRegistroSeleccionado().
//     * 
//     * @return 
//     */
//    private void filaseleccionada3() {
//
//        int fila = horarios.getSelectedRow();
//        String[] cond = {"candidato.codigo"};
//        String[] id = {(String) horarios.getValueAt(fila, 0)};
//        String inner = " INNER JOIN partido_politico on candidato.partido_politico_idcatedratico=partido_politico.idcatedratico INNER JOIN puesto on candidato.puesto_idpuesto=puesto.idpuesto ";
//        if (horarios.getValueAt(fila, 0) != null) {
//
//            String conct = "concat(partido_politico.nombre,' ',partido_politico.apellido)";
//            String[] campos = {"candidato.codigo", "candidato.descripcion", "candidato.dia", conct, "puesto.descripcion", "candidato.horariode", "candidato.horarioa", "candidato.fechainicio", "candidato.fechafin", "candidato.cantalumnos", "candidato.estado", "candidato.graduados"};
//            llenarcombopartido_politico(); // borra los items de comboBox y lo vuelve a llenar
//            Component[] cmps = {codigo, descripcion, dia, partido_politico, puesto, horade, horaa, fechainicio, fechafin, cantalumnos, estado, graduados};
//            Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
//
//            peticiones.getRegistroSeleccionado(cmps, "candidato", campos, cond, id, inner, hashProfesor);
//
//            this.bntGuardar.setEnabled(false);
//            this.bntModificar.setEnabled(true);
//            this.bntEliminar.setEnabled(true);
//            this.bntNuevo.setEnabled(false);
//        }
//    }

    /* Este metodo  consulta en la BD el codigo de la fila seleccionada y llena los componentes
     * de la parte superior del formulario con los datos obtenidos en la capa de Negocio getRegistroSeleccionado().
     * 
     * @return 
     */
    private void filaseleccionada() {

        int fila = horarios.getSelectedRow();
        String[] cond = {"candidato.codigo"};
        String[] id = {(String) horarios.getValueAt(fila, 0)};
        String inner = " INNER JOIN partido_politico on candidato.partido_idpartido=partido_politico.idpartido INNER JOIN puesto on candidato.puesto_idpuesto=puesto.idpuesto INNER JOIN municipio on candidato.municipio_idmunicipio=municipio.idmunicipio";
        if (horarios.getValueAt(fila, 0) != null) {

            //String conct = "concat(candidato.nombres,' ',candidato.apellidos)";
            String[] campos = {"candidato.codigo", "partido_politico.nombre", "puesto.nombre", "candidato.estado", "candidato.idcandidato", "municipio.nombre"};

            llenarcombopartido_politico();
            llenarcombopuesto();
            llenarcombomunicipio();
            Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");

            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getRegistros("candidato", campos, cond, id, inner);

            if (rs != null) {
                try {
                    if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                        rs.beforeFirst();//regresa el puntero al primer registro
                        while (rs.next()) {//mientras tenga registros que haga lo siguiente

                            codigo.setText(rs.getString(1));
                            //nombres.setText(rs.getString(2));
                            //apellidos.setText(rs.getString(3));
                            //dia.setSelectedItem(rs.getString(3));
                            int pr = Integer.parseInt((String) hashProfesor.get(rs.getString(2)));
                            partido.setSelectedIndex(pr);
                            int car = Integer.parseInt((String) hashCarrera.get(rs.getString(3)));
                            puesto.setSelectedIndex(car);
                            //horade.setValue(rs.getTime(6));
                            //horaa.setValue(rs.getTime(7));
                            //fechainicio.setDate((rs.getDate(6)));
                            //fechafin.setDate((rs.getDate(7)));
                            //cantalumnos.setText(rs.getString(10));

                            if (rs.getObject(4).equals(true)) {
                                estado.setText("Activo");
                                estado.setSelected(true);
                                estado.setBackground(new java.awt.Color(102, 204, 0));
                            } else {
                                estado.setText("Inactivo");
                                estado.setSelected(false);
                                estado.setBackground(Color.red);
                            }
                            //inscripcion.setValue(rs.getFloat(12));
                            //colegiatura.setValue(rs.getFloat(13));
                            newcodcandidato = rs.getInt(5);
                            int munici = Integer.parseInt((String) hashMunicipio.get(rs.getString(6)));
                            Cmunicipio.setSelectedIndex(munici);
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showInternalMessageDialog(this, e);
                }
            }
            this.bntGuardar.setEnabled(false);
            this.bntModificar.setEnabled(true);
            this.bntEliminar.setEnabled(true);
            this.bntNuevo.setEnabled(false);
        }
    }

    public void idacandidato(String codigo) {

        String[] id = {codigo};

        ResultSet rs;
        AccesoDatos ac = new AccesoDatos();
        String[] cond = {"candidato.codigo"};
        String[] campos = {"candidato.idcandidato"};
        //String inner=" inner join alumnosencandidato on  alumno.idalumno=alumnosencandidato.idasignacandidato ";

        rs = ac.getRegistros("candidato", campos, cond, id, "");

        if (rs != null) {
            try {
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    rs.beforeFirst();//regresa el puntero al primer registro
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        idcandidato = (rs.getInt(1));
                        //idasignacandidato.setText(rs.getString(2));
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showInternalMessageDialog(this, e);
            }
        }
    }

    private int ultimocandidato() {
        if (newcodcandidato == 0) {
            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getUltimoRegistro("candidato", "idcandidato");
            if (rs != null) {
                try {
                    if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                        rs.beforeFirst();//regresa el puntero al primer registro
                        while (rs.next()) {//mientras tenga registros que haga lo siguiente
                            newcodcandidato = (rs.getInt(1) + 1);
                        }
                    } else {
                        newcodcandidato = newcodcandidato + 1;
                    }

                } catch (SQLException e) {
                    JOptionPane.showInternalMessageDialog(this, e);
                }
            }
        }
        return newcodcandidato;

    }

    private void generacodigocandidato() {
        String txtdia = "";
        if (partido.getSelectedIndex() == 0) {
        } else if (partido.getSelectedIndex() != -1) {
            txtdia = partido.getSelectedItem().toString();
        }
        String tx = txtdia + " ";
        if (tx.isEmpty()) {
        } else {
            String cod = GeneraCodigo.actualizarRegistro(txtdia + " ");
            codigo.setText(cod + "-" + ultimocandidato());
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
        Nuevo_Partido = new javax.swing.JMenuItem();
        Actualizar_Partido = new javax.swing.JMenuItem();
        popupcarrera = new javax.swing.JPopupMenu();
        Nueva_Candidatura = new javax.swing.JMenuItem();
        Actualizar_Candidatura = new javax.swing.JMenuItem();
        panelImage = new elaprendiz.gui.panel.PanelImage();
        pnlActionButtons = new javax.swing.JPanel();
        bntNuevo = new elaprendiz.gui.button.ButtonRect();
        bntModificar = new elaprendiz.gui.button.ButtonRect();
        bntGuardar = new elaprendiz.gui.button.ButtonRect();
        bntEliminar = new elaprendiz.gui.button.ButtonRect();
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        JPanelCampos = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        codigo = new elaprendiz.gui.textField.TextField();
        estado = new javax.swing.JRadioButton();
        jLabel12 = new javax.swing.JLabel();
        partido = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        puesto = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        Cmunicipio = new javax.swing.JComboBox();
        JPanelTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        horarios = new javax.swing.JTable();
        JPanelBusqueda = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        busqueda = new elaprendiz.gui.textField.TextField();
        rbCodigo = new javax.swing.JRadioButton();
        rbNombres = new javax.swing.JRadioButton();
        rbApellidos = new javax.swing.JRadioButton();
        pnlPaginador = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();

        Nuevo_Partido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/profesor.png"))); // NOI18N
        Nuevo_Partido.setText("Nuevo Profesor");
        Nuevo_Partido.setName("Profesor Principal"); // NOI18N
        Nuevo_Partido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Nuevo_PartidoActionPerformed(evt);
            }
        });
        popupprofesor.add(Nuevo_Partido);

        Actualizar_Partido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/update.png"))); // NOI18N
        Actualizar_Partido.setText("Actualizar Combo");
        Actualizar_Partido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Actualizar_PartidoActionPerformed(evt);
            }
        });
        popupprofesor.add(Actualizar_Partido);

        Nueva_Candidatura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/carrera.png"))); // NOI18N
        Nueva_Candidatura.setText("Nueva Carrera");
        Nueva_Candidatura.setName("Carrera Principal"); // NOI18N
        Nueva_Candidatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Nueva_CandidaturaActionPerformed(evt);
            }
        });
        popupcarrera.add(Nueva_Candidatura);

        Actualizar_Candidatura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/update.png"))); // NOI18N
        Actualizar_Candidatura.setText("Actualizar Combo");
        Actualizar_Candidatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Actualizar_CandidaturaActionPerformed(evt);
            }
        });
        popupcarrera.add(Actualizar_Candidatura);

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Candidato");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Candidato"); // NOI18N
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
        bntNuevo.setMnemonic(KeyEvent.VK_N);
        bntNuevo.setText("Nuevo");
        bntNuevo.setName("Nuevo Horario"); // NOI18N
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

        bntModificar.setBackground(new java.awt.Color(51, 153, 255));
        bntModificar.setMnemonic(KeyEvent.VK_M);
        bntModificar.setText("Modificar");
        bntModificar.setEnabled(false);
        bntModificar.setName("Modificar Horario"); // NOI18N
        bntModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntModificarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
        pnlActionButtons.add(bntModificar, gridBagConstraints);

        bntGuardar.setBackground(new java.awt.Color(51, 153, 255));
        bntGuardar.setMnemonic(KeyEvent.VK_G);
        bntGuardar.setText("Guardar");
        bntGuardar.setEnabled(false);
        bntGuardar.setName("Guardar Horario"); // NOI18N
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

        bntEliminar.setBackground(new java.awt.Color(51, 153, 255));
        bntEliminar.setMnemonic(KeyEvent.VK_E);
        bntEliminar.setText("Eliminar");
        bntEliminar.setBorderPainted(true);
        bntEliminar.setEnabled(false);
        bntEliminar.setName("Eliminar Horario"); // NOI18N
        bntEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntEliminarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
        pnlActionButtons.add(bntEliminar, gridBagConstraints);

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

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Partido:");
        JPanelCampos.add(jLabel3);
        jLabel3.setBounds(130, 50, 80, 20);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Estado:");
        JPanelCampos.add(jLabel4);
        jLabel4.setBounds(100, 140, 110, 20);

        codigo.setEditable(false);
        codigo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        codigo.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelCampos.add(codigo);
        codigo.setBounds(220, 20, 250, 21);

        estado.setBackground(new java.awt.Color(51, 153, 255));
        estado.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        estado.setForeground(new java.awt.Color(255, 255, 255));
        estado.setText("Activo");
        estado.setEnabled(false);
        estado.setName("JRadioButton"); // NOI18N
        JPanelCampos.add(estado);
        estado.setBounds(220, 140, 150, 21);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("Codigo:");
        JPanelCampos.add(jLabel12);
        jLabel12.setBounds(130, 20, 80, 17);

        partido.setModel(modelCombo = new DefaultComboBoxModel());
        partido.setComponentPopupMenu(popupprofesor);
        partido.setEnabled(false);
        partido.setName("Profesor"); // NOI18N
        partido.setNextFocusableComponent(puesto);
        JPanelCampos.add(partido);
        partido.setBounds(220, 50, 250, 21);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Candidatura:");
        JPanelCampos.add(jLabel5);
        jLabel5.setBounds(120, 80, 90, 20);

        puesto.setModel(modelCombo = new DefaultComboBoxModel());
        puesto.setComponentPopupMenu(popupcarrera);
        puesto.setEnabled(false);
        puesto.setName("Profesor"); // NOI18N
        JPanelCampos.add(puesto);
        puesto.setBounds(220, 80, 250, 21);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Municipio:");
        JPanelCampos.add(jLabel10);
        jLabel10.setBounds(140, 110, 70, 17);

        Cmunicipio.setModel(modelCombo = new DefaultComboBoxModel());
        Cmunicipio.setComponentPopupMenu(popupcarrera);
        Cmunicipio.setEnabled(false);
        Cmunicipio.setName("Profesor"); // NOI18N
        JPanelCampos.add(Cmunicipio);
        Cmunicipio.setBounds(220, 110, 250, 20);

        panelImage.add(JPanelCampos);
        JPanelCampos.setBounds(0, 40, 880, 190);

        JPanelTable.setOpaque(false);
        JPanelTable.setPreferredSize(new java.awt.Dimension(786, 402));
        JPanelTable.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        horarios.setForeground(new java.awt.Color(51, 51, 51));
        horarios.setModel(model = new DefaultTableModel(null, titulos)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            horarios.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            horarios.setFocusCycleRoot(true);
            horarios.setGridColor(new java.awt.Color(51, 51, 255));
            horarios.setRowHeight(22);
            horarios.setSelectionBackground(java.awt.SystemColor.activeCaption);
            horarios.setSurrendersFocusOnKeystroke(true);
            horarios.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    horariosMouseClicked(evt);
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    horariosMouseClicked(evt);
                }
            });
            horarios.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    horariosKeyPressed(evt);
                }
            });
            jScrollPane1.setViewportView(horarios);
            horarios.getAccessibleContext().setAccessibleName("");

            JPanelTable.add(jScrollPane1, java.awt.BorderLayout.CENTER);

            panelImage.add(JPanelTable);
            JPanelTable.setBounds(0, 300, 880, 130);

            JPanelBusqueda.setBackground(java.awt.SystemColor.inactiveCaption);
            JPanelBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            JPanelBusqueda.setLayout(null);

            jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar.png"))); // NOI18N
            jLabel7.setText("Buscar Por:");
            JPanelBusqueda.add(jLabel7);
            jLabel7.setBounds(170, 2, 120, 40);

            busqueda.setPreferredSize(new java.awt.Dimension(250, 27));
            busqueda.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    busquedaActionPerformed(evt);
                }
            });
            JPanelBusqueda.add(busqueda);
            busqueda.setBounds(300, 10, 250, 27);

            rbCodigo.setBackground(java.awt.SystemColor.inactiveCaption);
            rbCodigo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbCodigo.setForeground(new java.awt.Color(0, 102, 102));
            rbCodigo.setText("Codigo");
            rbCodigo.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbCodigoActionPerformed(evt);
                }
            });
            JPanelBusqueda.add(rbCodigo);
            rbCodigo.setBounds(270, 40, 80, 25);

            rbNombres.setBackground(java.awt.SystemColor.inactiveCaption);
            rbNombres.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbNombres.setForeground(new java.awt.Color(0, 102, 102));
            rbNombres.setSelected(true);
            rbNombres.setText("Partido");
            rbNombres.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbNombresActionPerformed(evt);
                }
            });
            JPanelBusqueda.add(rbNombres);
            rbNombres.setBounds(370, 40, 110, 25);

            rbApellidos.setBackground(java.awt.SystemColor.inactiveCaption);
            rbApellidos.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            rbApellidos.setForeground(new java.awt.Color(0, 102, 102));
            rbApellidos.setText("Municipio");
            rbApellidos.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    rbApellidosActionPerformed(evt);
                }
            });
            JPanelBusqueda.add(rbApellidos);
            rbApellidos.setBounds(500, 40, 90, 25);

            panelImage.add(JPanelBusqueda);
            JPanelBusqueda.setBounds(0, 230, 880, 70);

            pnlPaginador.setBackground(new java.awt.Color(57, 104, 163));
            pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
            pnlPaginador.setLayout(new java.awt.GridBagLayout());

            jLabel8.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
            jLabel8.setForeground(new java.awt.Color(255, 255, 255));
            jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/horario3.png"))); // NOI18N
            jLabel8.setText("<--Candidato-->");
            pnlPaginador.add(jLabel8, new java.awt.GridBagConstraints());

            panelImage.add(pnlPaginador);
            pnlPaginador.setBounds(0, 0, 880, 40);

            getContentPane().add(panelImage, java.awt.BorderLayout.CENTER);

            getAccessibleContext().setAccessibleName("Profesores");

            setBounds(0, 0, 890, 512);
        }// </editor-fold>//GEN-END:initComponents

    private void bntNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntNuevoActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntNuevo.getName()) == true) {

            Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
            llenarcombopartido_politico();
            llenarcombopuesto();
            llenarcombomunicipio();
            estado.setSelected(true);
            this.bntGuardar.setEnabled(true);
            this.bntModificar.setEnabled(false);
            this.bntEliminar.setEnabled(false);
            this.bntNuevo.setEnabled(false);
            //nombres.requestFocus();
            newcodcandidato = 0;
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntNuevoActionPerformed

    private void bntGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardarActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntGuardar.getName()) == true) {

            if (Utilidades.esObligatorio(this.JPanelCampos, true)) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
//            if (fechainicio.getCalendar().after(fechafin.getCalendar())) {
//                JOptionPane.showInternalMessageDialog(this, "Las fecha de inicio debe ser menor a la fecha fin del Grupo", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
            int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
            if (resp == 0) {
                generacodigocandidato();
                boolean seguardo = false;
                String nombreTabla = "candidato";
                String campos = "codigo, partido_idpartido,puesto_idpuesto, municipio_idmunicipio, estado";
                //String fechaini = FormatoFecha.getFormato(fechainicio.getCalendar().getTime(), FormatoFecha.A_M_D);
                //String fechafn = FormatoFecha.getFormato(fechafin.getCalendar().getTime(), FormatoFecha.A_M_D);

                //String fechaini2 = FormatoFecha.getFormato(fechainicio.getCalendar().getTime(), FormatoFecha.D_M_A);
                //String fechafn2 = FormatoFecha.getFormato(fechafin.getCalendar().getTime(), FormatoFecha.D_M_A);
                mProfesor prof = (mProfesor) partido.getSelectedItem();
                String idprof = prof.getID();
                mCarrera carr = (mCarrera) puesto.getSelectedItem();
                String idpuesto = carr.getID();
                mMunicipio mun = (mMunicipio) Cmunicipio.getSelectedItem();
                String idmun = mun.getID();

                int estad = 0;
                if (this.estado.isSelected()) {
                    estad = 1;
                }

                Object[] valores = {codigo.getText(), idprof, idpuesto, idmun , estad
                };

                seguardo = peticiones.guardarRegistros(nombreTabla, campos, valores);

                if (seguardo) {

//                    AccesoDatos ac = new AccesoDatos();
//                    Calendar a = ProyeccionPagos.convierteacalendar(fechaini2);
//                    //float cole = Float.parseFloat(colegiatura.getText());
//                    Calendar b = ProyeccionPagos.convierteacalendar(fechafn2);
//                    idacandidato(codigo.getText());
//                    String sql = ProyeccionPagos.calculapagos(a, b, "" + idcandidato, inscripcion.getText());
//
//                    int pagos = ac.agregarRegistrosql("INSERT INTO PAGOS (mes_idmes,año,monto,fechavencimiento,candidato_idcandidato) VALUES " + sql);
//                    System.out.print(pagos);
//                    if (pagos > 0) {
//                    } else {
//                        JOptionPane.showInternalMessageDialog(this, "Los pagos no se Guardaron", "Error", JOptionPane.ERROR_MESSAGE);
//                    }
                    Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
                    MostrarDatos(busqueda.getText());
                    this.bntGuardar.setEnabled(false);
                    this.bntModificar.setEnabled(false);
                    this.bntEliminar.setEnabled(false);
                    this.bntNuevo.setEnabled(true);
                    busqueda.requestFocus();
                    JOptionPane.showInternalMessageDialog(this, "El dato se ha Guardado Correctamente", "Guardar", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntGuardarActionPerformed

    private void bntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalirActionPerformed
        cerrarVentana();
    }//GEN-LAST:event_bntSalirActionPerformed

    private void horariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_horariosMouseClicked
        // TODO add your handling code here:
        filaseleccionada();

    }//GEN-LAST:event_horariosMouseClicked

    private void bntEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntEliminar.getName()) == true) {

            int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Eliminar el Registro?", "Pregunta", 0);
            if (resp == 0) {

                int fila = horarios.getSelectedRow();
                String id = (String) "" + horarios.getValueAt(fila, 0);
                String nombreTabla = "candidato", nomColumnaCambiar = "estado";
                String nomColumnaId = "codigo";
                int seguardo = 0;

                seguardo = peticiones.eliminarRegistro(nombreTabla, nomColumnaCambiar, nomColumnaId, id);

                if (seguardo == 1) {
                    Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
                    MostrarDatos(busqueda.getText());
                    this.bntGuardar.setEnabled(false);
                    this.bntModificar.setEnabled(false);
                    this.bntEliminar.setEnabled(false);
                    this.bntNuevo.setEnabled(true);
                    busqueda.requestFocus();
                    JOptionPane.showInternalMessageDialog(this, "El dato se ha Eliminado Correctamente", "Eliminar", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntEliminarActionPerformed

    private void bntModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntModificarActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntModificar.getName()) == true) {

            if (Utilidades.esObligatorio(this.JPanelCampos, true)) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
//            if (fechainicio.getCalendar().after(fechafin.getCalendar())) {
//                JOptionPane.showInternalMessageDialog(this, "Las fecha de inicio debe ser menor a la fecha fin del Grupo", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
            int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Modificar el Registro?", "Pregunta", 0);
            if (resp == 0) {
                generacodigocandidato();
                String nomTabla = "candidato";
                String columnaId = "codigo";
                int seguardo = 0;
                int fila = horarios.getSelectedRow();
                String id = (String) "" + horarios.getValueAt(fila, 0);

                String campos = "codigo, partido_idpartido, puesto_idpuesto,municipio_idmunicipio,estado ";
                //String fechaini = FormatoFecha.getFormato(fechainicio.getCalendar().getTime(), FormatoFecha.A_M_D);
                //String fechafn = FormatoFecha.getFormato(fechafin.getCalendar().getTime(), FormatoFecha.A_M_D);
                //Para obtener el id en la base de datos
                mProfesor prof = (mProfesor) partido.getSelectedItem();
                String idprof = prof.getID();
                mCarrera carr = (mCarrera) puesto.getSelectedItem();
                String idpuesto = carr.getID();
                mMunicipio mun = (mMunicipio) Cmunicipio.getSelectedItem();
                String idmun = mun.getID();

                int estad = 0;
                if (this.estado.isSelected()) {
                    estad = 1;
                }

                Object[] valores = {codigo.getText(), idprof, idpuesto, idmun,estad, id
                };

                seguardo = peticiones.actualizarRegistro(nomTabla, campos, valores, columnaId, id);
                if (seguardo == 1) {
                    Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
                    MostrarDatos(busqueda.getText());
                    busqueda.requestFocus();
                    JOptionPane.showInternalMessageDialog(this, "El dato se ha Modificado Correctamente", "Modificar", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntModificarActionPerformed

    private void bntCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntCancelarActionPerformed
        // TODO add your handling code here:
        Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
        Utilidades.esObligatorio(this.JPanelCampos, false);
        removejtable();
        this.bntGuardar.setEnabled(false);
        this.bntModificar.setEnabled(false);
        this.bntEliminar.setEnabled(false);
        this.bntNuevo.setEnabled(true);
        removejtable();
        busqueda.setText("");
        busqueda.requestFocus();
        newcodcandidato = 0;

    }//GEN-LAST:event_bntCancelarActionPerformed

    private void rbCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCodigoActionPerformed
        // TODO add your handling code here:
        rbNombres.setSelected(false);
        rbApellidos.setSelected(false);
        busqueda.requestFocus();
    }//GEN-LAST:event_rbCodigoActionPerformed

    private void rbNombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNombresActionPerformed
        // TODO add your handling code here:
        rbCodigo.setSelected(false);
        rbApellidos.setSelected(false);
        busqueda.requestFocus();
    }//GEN-LAST:event_rbNombresActionPerformed

    private void busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busquedaActionPerformed
        // TODO add your handling code here:
        MostrarDatos(busqueda.getText());
    }//GEN-LAST:event_busquedaActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing

    private void horariosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_horariosKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == java.awt.event.KeyEvent.VK_SPACE) {
            filaseleccionada();
        }
        if (key == java.awt.event.KeyEvent.VK_DOWN || key == java.awt.event.KeyEvent.VK_UP) {
            limpiar();
        }
    }//GEN-LAST:event_horariosKeyPressed

    private void rbApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbApellidosActionPerformed
        // TODO add your handling code here:
        rbNombres.setSelected(false);
        rbCodigo.setSelected(false);
        busqueda.requestFocus();
    }//GEN-LAST:event_rbApellidosActionPerformed

    private void Actualizar_PartidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Actualizar_PartidoActionPerformed
        // TODO add your handling code here:
        llenarcombopartido_politico();
    }//GEN-LAST:event_Actualizar_PartidoActionPerformed

    private void Nuevo_PartidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Nuevo_PartidoActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(Nuevo_Partido.getName()) == true) {
            Cargo_politico frmProfesor = new Cargo_politico();
            if (frmProfesor == null) {
                frmProfesor = new Cargo_politico();
            }
            adminInternalFrame(dp, frmProfesor);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_Nuevo_PartidoActionPerformed

    private void Nueva_CandidaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Nueva_CandidaturaActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(Nueva_Candidatura.getName()) == true) {
            Partido_politico frmCarrera = new Partido_politico();
            if (frmCarrera == null) {
                frmCarrera = new Partido_politico();
            }
            adminInternalFrame(dp, frmCarrera);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_Nueva_CandidaturaActionPerformed

    private void Actualizar_CandidaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Actualizar_CandidaturaActionPerformed
        // TODO add your handling code here:
        llenarcombopuesto();
    }//GEN-LAST:event_Actualizar_CandidaturaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Actualizar_Candidatura;
    private javax.swing.JMenuItem Actualizar_Partido;
    private javax.swing.JComboBox Cmunicipio;
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelCampos;
    private javax.swing.JPanel JPanelTable;
    private javax.swing.JMenuItem Nueva_Candidatura;
    private javax.swing.JMenuItem Nuevo_Partido;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntEliminar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntModificar;
    private elaprendiz.gui.button.ButtonRect bntNuevo;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.textField.TextField busqueda;
    private elaprendiz.gui.textField.TextField codigo;
    private javax.swing.JRadioButton estado;
    private javax.swing.JTable horarios;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JComboBox partido;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador;
    private javax.swing.JPopupMenu popupcarrera;
    private javax.swing.JPopupMenu popupprofesor;
    private javax.swing.JComboBox puesto;
    private javax.swing.JRadioButton rbApellidos;
    private javax.swing.JRadioButton rbCodigo;
    private javax.swing.JRadioButton rbNombres;
    // End of variables declaration//GEN-END:variables
}
