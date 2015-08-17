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
import Capa_Negocio.GeneraCodigo;
import Capa_Negocio.Peticiones;
import Capa_Negocio.TipoFiltro;
import Capa_Negocio.Utilidades;
import static Capa_Presentacion.Principal.dp;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import static java.awt.Color.WHITE;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import modelos.mGrupo;

/**
 *
 * @author GLARA
 */
public class Alumno extends javax.swing.JInternalFrame {

    private static Horario frmHorario = new Horario();
    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model, modelgrupo;
    DefaultComboBoxModel modelCombo;
    String[] titulos = {"codigo", "Nombres", "Apellidos", "Fecha Nec", "Estado"};//Titulos para Jtabla
    String[] titulosgrupo = {"Codigo", "Dia", "Profesor", "Carrera", "De", "A", "Fech Ini", "Fech Fin",};//Titulos para Jtabla
    /*Se hace una instancia de la clase que recibira las peticiones de esta capa de aplicación*/
    Peticiones peticiones = new Peticiones();
    public static Hashtable<String, String> hashGrupo = new Hashtable<>();
    int nidalumno, idalumno, iddetallegrupo;
    boolean matricula = true;
    java.sql.Connection conn;//getConnection intentara establecer una conexión.
    Color FObligatorio = WHITE; //Color ColorR; //cambiar por uno mas claro    
    Color BObligatorio = (Color.getHSBColor(0, 155, 185));
//public Hashtable<String, String> hashGrupo = new Hashtable<>();

    /*Se hace una instancia de la clase que recibira las peticiones de mensages de la capa de aplicación*/
    //public static JOptionMessage msg = new JOptionMessage();
    /**
     * Creates new form Cliente
     */
    public Alumno() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();
        limpiar();

        tbPane1.remove(jPanel5);
        cDia.addItemListener(
                (ItemEvent e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        selecciondia();
                    }
                });

        cGrupo.addItemListener(
                (ItemEvent e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        selecciongrupo();
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
        int nu = JOptionPane.showInternalConfirmDialog(this, "Todos los datos que no se ha guardadox "
                + "se perderan.\n"
                + "¿Desea Cerrar esta ventana?", "Cerrar ventana", JOptionPane.YES_NO_OPTION);
        if (nu == JOptionPane.YES_OPTION || nu == 0) {
            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
            //Utilidades.esObligatorio(this.JPanelCampos, false);
            Utilidades.esObligatorio(this.jPanel1, false);
            Utilidades.esObligatorio(this.jPanel2, false);
            //Utilidades.esObligatorio(this.jPanel3, false);
            Utilidades.esObligatorio(this.jPanel4, false);
            Utilidades.esObligatorio(this.jPanel5, false);
            this.bntGuardar.setEnabled(false);
            this.bntModificar.setEnabled(false);
            this.bntEliminar.setEnabled(false);
            this.bntNuevo.setEnabled(true);
            removejtable();
            busqueda.setText("");
            rbNombre.setSelected(true);
            rbCodigo.setSelected(false);
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
        while (alumnos.getRowCount() != 0) {
            model.removeRow(0);
        }
    }

    public void removejtablegrupo() {
        while (horarios.getRowCount() != 0) {
            modelgrupo.removeRow(0);
        }
    }

    /* Este metodo se encarga de filtrar los datos que se deben ingresar en cada uno de los campos del formulario
     * podemos indicar que el usuario ingrese solo numeros , solo letras, numeros y letras, o cualquier caracter
     * tambien podemos validar si se aseptaran espacios en blanco en la cadena ingresada , para mas detalle visualizar
     * la clase TipoFiltro()  */
    private void setFiltroTexto() {

        TipoFiltro.setFiltraEntrada(codigo.getDocument(), FiltroCampos.NUM_LETRAS, 25, true);
        TipoFiltro.setFiltraEntrada(nombres.getDocument(), FiltroCampos.SOLO_LETRAS, 60, true);
        TipoFiltro.setFiltraEntrada(apellidos.getDocument(), FiltroCampos.SOLO_LETRAS, 60, true);
        TipoFiltro.setFiltraEntrada(direccion.getDocument(), FiltroCampos.NUM_LETRAS, 150, true);
        TipoFiltro.setFiltraEntrada(titularnombre.getDocument(), FiltroCampos.SOLO_LETRAS, 60, true);
        TipoFiltro.setFiltraEntrada(titularapellido.getDocument(), FiltroCampos.SOLO_LETRAS, 60, true);
        TipoFiltro.setFiltraEntrada(dpi.getDocument(), FiltroCampos.NUM_LETRAS, 20, true);
        TipoFiltro.setFiltraEntrada(telefono.getDocument(), FiltroCampos.SOLO_NUMEROS, 16, false);
        TipoFiltro.setFiltraEntrada(busqueda.getDocument(), FiltroCampos.NUM_LETRAS, 100, true);
    }

    private void selecciondia() {
        if (cDia.getSelectedIndex() == 0) {
            cGrupo.removeAllItems();
        } else if (cDia.getSelectedIndex() > 0) {
            String sdia = (String) cDia.getSelectedItem();
            llenarcombogrupo(sdia);
        }
    }

    /*
     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
     *y se los envia a un metodo interno getRegistroCombo() 
     *
     */
    public void llenarcombogrupo(String dia) {
        String Dato = "1";
        String[] campos = {"codigo", "descripcion", "idgrupo"};
        String[] condiciones = {"estado", "dia"};
        String[] Id = {Dato, dia};
        cGrupo.removeAllItems();
        //Component cmps = profesor;
        getRegistroCombo("grupo", campos, condiciones, Id);

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
                cGrupo.setModel(modeloComboBox);

                modeloComboBox.addElement(new mGrupo("", "0"));
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    //Object[] fila = new Object[cantcampos];
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        count++;
                        modeloComboBox.addElement(new mGrupo(rs.getString(1) + " " + rs.getString(2), "" + rs.getInt(3)));
                        hashGrupo.put(rs.getString(1) + " " + rs.getString(2), "" + count);
                        //System.out.print(rs.getString(1) + " " + rs.getString(2)+ "" + count+"\n");
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

    public void selecciongrupo() {
        if (cGrupo.getSelectedIndex() == 0) {
            profesor.setText("");
            carrera.setText("");
            horade.setText("");
            horaa.setText("");
            fechaini.setText("");
            fechafin.setText("");
            inscripcion.setValue(null);
            colegiatura.setValue(null);
        } else if (cGrupo.getSelectedIndex() != -1) {

            mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
            String[] id = {grup.getID()};

            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();
            String[] cond = {"grupo.idgrupo"};
            String inner = " INNER JOIN profesor on grupo.profesor_idcatedratico=profesor.idcatedratico INNER JOIN carrera on grupo.carrera_idcarrera=carrera.idcarrera ";
            if (!id.equals(0)) {

                String conct = "concat(profesor.nombre,' ',profesor.apellido)";
                String[] campos = {conct, "carrera.descripcion", "DATE_FORMAT(grupo.horariode,'%h:%i %p')", "DATE_FORMAT(grupo.horarioa,'%h:%i %p')", "DATE_FORMAT(grupo.fechainicio,'%d-%m-%Y')", "DATE_FORMAT(grupo.fechafin,'%d-%m-%Y')", "grupo.inscripcion", "grupo.colegiatura"};

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
                                fechaini.setText((rs.getString(5)));
                                fechafin.setText((rs.getString(6)));
                                inscripcion.setValue(rs.getFloat(7));
                                colegiatura.setValue(rs.getFloat(8));
                                
                                inscripalumno.setValue(rs.getFloat(7));
                            }
                        }
                        //profesor.setEditable(false);
                    } catch (SQLException e) {
                        //profesor.setEditable(false);
                        JOptionPane.showInternalMessageDialog(this, e);
                    }
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
    private void MostrarDatosGrupos(String Dato) {
        String conct = "concat(profesor.nombre,' ',profesor.apellido)";
        String conct2 = "concat(grupo.codigo,' ',grupo.descripcion)";
        String[] campos = {"grupo.codigo", "grupo.dia", conct, "carrera.descripcion", "DATE_FORMAT(grupo.horariode,'%h:%i %p')", "DATE_FORMAT(grupo.horarioa,'%h:%i %p')", "DATE_FORMAT(grupo.fechainicio,'%d-%m-%Y')", "DATE_FORMAT(grupo.fechafin,'%d-%m-%Y')"};
        String[] condiciones = {"alumno.codigo"};
        String[] Id = {Dato};
        String inner = " INNER JOIN alumnosengrupo ON grupo.idgrupo = alumnosengrupo.grupo_idgrupo INNER JOIN alumno ON alumnosengrupo.alumno_idalumno = alumno.idalumno INNER JOIN profesor on grupo.profesor_idcatedratico=profesor.idcatedratico INNER JOIN carrera on grupo.carrera_idcarrera=carrera.idcarrera ";

        if (!Dato.isEmpty()) {
            removejtablegrupo();
            modelgrupo = peticiones.getRegistroPorPks(modelgrupo, "grupo", campos, condiciones, Id, inner);
        } else {
            JOptionPane.showInternalMessageDialog(this, "Debe ingresar un codigo para la busqueda");
        }
        Utilidades.ajustarAnchoColumnas(horarios);
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
        String[] campos = {"alumno.codigo", "alumno.nombres", "alumno.apellidos", "DATE_FORMAT(alumno.fechanacimiento,'%d-%m-%Y')", "alumno.estado"};
        String[] condiciones = {"alumno.codigo"};
        String[] Id = {Dato};

        if (this.rbCodigo.isSelected()) {
            if (!Dato.isEmpty()) {
                removejtable();
                Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
                //Utilidades.esObligatorio(this.JPanelCampos, false);
                Utilidades.esObligatorio(this.jPanel1, false);
                Utilidades.esObligatorio(this.jPanel2, false);
                Utilidades.esObligatorio(this.jPanel4, false);
                Utilidades.esObligatorio(this.jPanel5, false);
                model = peticiones.getRegistroPorPks(model, "alumno", campos, condiciones, Id, "");
            } else {
                JOptionPane.showInternalMessageDialog(this, "Debe ingresar un codigo para la busqueda");
            }
        }
        if (this.rbNombre.isSelected()) {
            removejtable();
            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
            //Utilidades.esObligatorio(this.JPanelCampos, false);
            Utilidades.esObligatorio(this.jPanel1, false);
            Utilidades.esObligatorio(this.jPanel2, false);
            Utilidades.esObligatorio(this.jPanel4, false);
            Utilidades.esObligatorio(this.jPanel5, false);
            model = peticiones.getRegistroPorLike(model, "alumno", campos, "alumno.nombres", Dato, "");
        }
        if (this.rbApellido.isSelected()) {
            removejtable();
            Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
            //Utilidades.esObligatorio(this.JPanelCampos, false);
            Utilidades.esObligatorio(this.jPanel1, false);
            Utilidades.esObligatorio(this.jPanel2, false);
            Utilidades.esObligatorio(this.jPanel4, false);
            Utilidades.esObligatorio(this.jPanel5, false);
            model = peticiones.getRegistroPorLike(model, "alumno", campos, "alumno.apellidos", Dato, "");
        }
        Utilidades.ajustarAnchoColumnas(alumnos);
    }

//    /* Este metodo  consulta en la BD el codigo de la fila seleccionada y llena los componentes
//     * de la parte superior del formulario con los datos obtenidos en la capa de Negocio getRegistroSeleccionado().
//     * 
//     * @return 
//     */
//    private void filaseleccionada2() {
//
//        int fila = alumnos.getSelectedRow();
//        String[] cond = {"alumno.codigo"};
//        String[] id = {"" + alumnos.getValueAt(fila, 0)};
//
//        if (alumnos.getValueAt(fila, 0) != null) {
//
//            String[] campos = {"alumno.codigo", "alumno.nombres", "alumno.apellidos", "alumno.fechanacimiento", "alumno.direccion", "alumno.sexo", "alumno.telefono", "alumno.titularnombres", "alumno.titularapellidos", "alumno.titulardpi", "alumno.estado"};
//            Component[] cmps = {codigo, nombres, apellidos, fechanacimiento, direccion, sexo, telefono, titularnombre, dpi, dpi, estado
//            };
//
//            Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
//
//            peticiones.getRegistroSeleccionado(cmps, "alumno", campos, cond, id, "", null);
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

        int fila = alumnos.getSelectedRow();
        String[] cond = {"alumno.codigo"};
        String[] id = {(String) alumnos.getValueAt(fila, 0)};
        if (alumnos.getValueAt(fila, 0) != null) {
            matricula = false;
            String[] campos = {"alumno.codigo", "alumno.nombres", "alumno.apellidos", "alumno.fechanacimiento", "alumno.direccion", "alumno.sexo", "alumno.telefono", "alumno.titularnombres", "alumno.titularapellidos", "alumno.titulardpi", "alumno.estado", "alumno.idalumno", "alumno.establecimiento", "alumno.direccionestablecimiento", "alumno.gradoestablecimiento", "alumno.codigomineduc", "alumno.fechabaja", "alumno.observacion"};
            //Utilidades.esObligatorio(this.JPanelCampos, false);
            Utilidades.esObligatorio(this.jPanel1, false);
            Utilidades.esObligatorio(this.jPanel2, false);
            //Utilidades.esObligatorio(this.jPanel3, false);
            Utilidades.esObligatorio(this.jPanel4, false);
            Utilidades.esObligatorio(this.jPanel5, false);

            Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getRegistros("alumno", campos, cond, id, "");

            if (rs != null) {
                try {
                    if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                        rs.beforeFirst();//regresa el puntero al primer registro
                        while (rs.next()) {//mientras tenga registros que haga lo siguiente
                            codigo.setText(rs.getString(1));
                            nombres.setText(rs.getString(2));
                            apellidos.setText(rs.getString(3));
                            fechanacimiento.setDate((rs.getDate(4)));
                            direccion.setText(rs.getString(5));
                            sexo.setSelectedItem(rs.getString(6));
                            telefono.setText(rs.getString(7));
                            //colegiatura.setText(rs.getString(8));
                            //beca.setText(rs.getString(8));
                            //fechainicio.setDate((rs.getDate(9)));
                            titularnombre.setText(rs.getString(8));
                            titularapellido.setText(rs.getString(9));
                            dpi.setText(rs.getString(10));
                            if (rs.getObject(11).equals(true)) {
                                estado.setText("Activo");
                                estado.setSelected(true);
                                estado.setBackground(new java.awt.Color(102, 204, 0));
                                tbPane1.remove(jPanel5);
                            } else {
                                estado.setText("Inactivo");
                                estado.setSelected(false);
                                estado.setBackground(Color.red);
                                tbPane1.addTab("Baja Alumno", jPanel5);
                            }
                            nidalumno = rs.getInt(12);
                            establecimiento.setText(rs.getString(13));
                            direccion_establecimiento.setText(rs.getString(14));
                            grado_establecimiento.setText(rs.getString(15));
                            codigomineduc.setText(rs.getString(16));
                            fechabaja.setDate((rs.getDate(17)));
                            motivobaja.setText(rs.getString(18));

                            MostrarDatosGrupos(alumnos.getValueAt(fila, 0).toString());
                            //establecimiento.setText(rs.getString(13));
                            //direccion_establecimiento.setText(rs.getString(14));
                            //grado_establecimiento.setText(rs.getString(15));
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
            codigo.setEditable(false);
            //beca.setEditable(false);
            profesor.setEditable(false);
            horade.setEditable(false);
            horaa.setEditable(false);
            carrera.setEditable(false);
            fechaini.setEditable(false);
            fechafin.setEditable(false);
            inscripcion.setEditable(false);
            colegiatura.setEditable(false);
            buttonAction1.setEnabled(true);

        }
    }

    private int ultimoalumno() {
        if (nidalumno == 0) {
            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getUltimoRegistro("alumno", "idalumno");
            if (rs != null) {
                try {
                    if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                        rs.beforeFirst();//regresa el puntero al primer registro
                        while (rs.next()) {//mientras tenga registros que haga lo siguiente
                            nidalumno = (rs.getInt(1) + 1);
                        }
                    } else {
                        nidalumno = nidalumno + 1;
                    }

                } catch (SQLException e) {
                    JOptionPane.showInternalMessageDialog(this, e);
                }
            }
        }
        return nidalumno;

    }

    private void codigoalumno() {
        String tx = nombres.getText() + " " + apellidos.getText();
        if (tx.isEmpty()) {
        } else {
            String cod = GeneraCodigo.actualizarRegistro(nombres.getText() + " " + apellidos.getText());
            codigo.setText(cod + "-" + ultimoalumno());
        }
    }

    public void idalumnog(String codigo) {

        String[] id = {codigo};

        ResultSet rs;
        AccesoDatos ac = new AccesoDatos();
        String[] cond = {"alumno.codigo"};
        String[] campos = {"alumno.idalumno"};
        //String inner=" inner join alumnosengrupo on  alumno.idalumno=alumnosengrupo.idasignagrupo ";

        rs = ac.getRegistros("alumno", campos, cond, id, "");

        if (rs != null) {
            try {
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    rs.beforeFirst();//regresa el puntero al primer registro
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        idalumno = (rs.getInt(1));
                        //idasignagrupo.setText(rs.getString(2));
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showInternalMessageDialog(this, e);
            }
        }
    }

    public void idalumnosengrupo(String idalumno, String idgrupo) {

        String[] id = {idalumno, idgrupo};

        ResultSet rs;
        AccesoDatos ac = new AccesoDatos();
        String[] cond = {"alumnosengrupo.alumno_idalumno", "alumnosengrupo.grupo_idgrupo"};
        String[] campos = {"alumnosengrupo.iddetallegrupo"};
        rs = ac.getRegistros("alumnosengrupo", campos, cond, id, "");

        if (rs != null) {
            try {
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    rs.beforeFirst();//regresa el puntero al primer registro
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        iddetallegrupo = (rs.getInt(1));
                    }

                }
            } catch (SQLException e) {
                JOptionPane.showInternalMessageDialog(this, e);
            }
        }
    }

    public boolean cantalumnosgrupo(String idg) {
        String id = idg;
        ResultSet rs;
        //String sql = "select count(*) from alumnosengrupo  where grupo_idgrupo=" + idg + " union all select cantalumnos  from grupo where idgrupo=" + idg;
        String sql = "select count(*) from alumnosengrupo  where grupo_idgrupo=" + idg;
        String sql2 = "select cantalumnos  from grupo where idgrupo=" + idg;

        int cantalumnos = 0, maxallumnos = 0;
        boolean estado = false;

        if (!id.equals(0)) {
            rs = BdConexion.getResultSet(sql);
            if (rs != null) {
                try {
                    if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                        rs.beforeFirst();//regresa el puntero al primer registro
                        while (rs.next()) {//mientras tenga registros que haga lo siguiente
                            cantalumnos = rs.getInt(1);
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showInternalMessageDialog(this, e);
                }
            }
        }

        if (!id.equals(0)) {
            rs = BdConexion.getResultSet(sql2);
            if (rs != null) {
                try {
                    if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                        rs.beforeFirst();//regresa el puntero al primer registro
                        while (rs.next()) {//mientras tenga registros que haga lo siguiente
                            maxallumnos = rs.getInt(1);
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showInternalMessageDialog(this, e);
                }
            }
        }

        System.out.print(cantalumnos + "\n" + maxallumnos + "\n");

        if (cantalumnos < maxallumnos) {
            estado = true;
        } else if (cantalumnos >= maxallumnos) {
            estado = false;
        }

        return estado;
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

        popupgrupo = new javax.swing.JPopupMenu();
        Nuevo_Grupo = new javax.swing.JMenuItem();
        Actualizar_Grupo = new javax.swing.JMenuItem();
        panelImage = new elaprendiz.gui.panel.PanelImage();
        pnlActionButtons = new javax.swing.JPanel();
        bntNuevo = new elaprendiz.gui.button.ButtonRect();
        bntModificar = new elaprendiz.gui.button.ButtonRect();
        bntGuardar = new elaprendiz.gui.button.ButtonRect();
        bntEliminar = new elaprendiz.gui.button.ButtonRect();
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        JPanelCampos = new javax.swing.JPanel();
        tbPane1 = new elaprendiz.gui.panel.TabbedPaneHeader();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        codigo = new elaprendiz.gui.textField.TextField();
        jLabel1 = new javax.swing.JLabel();
        nombres = new elaprendiz.gui.textField.TextField();
        jLabel11 = new javax.swing.JLabel();
        apellidos = new elaprendiz.gui.textField.TextField();
        jLabel14 = new javax.swing.JLabel();
        direccion = new elaprendiz.gui.textField.TextField();
        jLabel12 = new javax.swing.JLabel();
        fechanacimiento = new com.toedter.calendar.JDateChooser();
        sexo = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        estado = new javax.swing.JRadioButton();
        codigomineduc = new elaprendiz.gui.textField.TextField();
        jLabel29 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        grado_establecimiento = new elaprendiz.gui.textField.TextField();
        jLabel2 = new javax.swing.JLabel();
        dpi = new elaprendiz.gui.textField.TextField();
        jLabel15 = new javax.swing.JLabel();
        telefono = new elaprendiz.gui.textField.TextField();
        titularapellido = new elaprendiz.gui.textField.TextField();
        jLabel20 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        titularnombre = new elaprendiz.gui.textField.TextField();
        jLabel16 = new javax.swing.JLabel();
        establecimiento = new elaprendiz.gui.textField.TextField();
        jLabel28 = new javax.swing.JLabel();
        direccion_establecimiento = new elaprendiz.gui.textField.TextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        horarios = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        cDia = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        cGrupo = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        carrera = new elaprendiz.gui.textField.TextField();
        profesor = new elaprendiz.gui.textField.TextField();
        inscripcion = new javax.swing.JFormattedTextField();
        colegiatura = new javax.swing.JFormattedTextField();
        horaa = new elaprendiz.gui.textField.TextField();
        horade = new elaprendiz.gui.textField.TextField();
        fechaini = new elaprendiz.gui.textField.TextField();
        fechafin = new elaprendiz.gui.textField.TextField();
        buttonAction1 = new elaprendiz.gui.button.ButtonAction();
        fechainicioalumno = new com.toedter.calendar.JDateChooser();
        jLabel27 = new javax.swing.JLabel();
        becagrupo = new javax.swing.JFormattedTextField();
        jLabel30 = new javax.swing.JLabel();
        inscripalumno = new javax.swing.JFormattedTextField();
        jLabel31 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        fechabaja = new com.toedter.calendar.JDateChooser();
        jScrollPane4 = new javax.swing.JScrollPane();
        motivobaja = new javax.swing.JTextArea();
        jLabel33 = new javax.swing.JLabel();
        buttonMostrar1 = new elaprendiz.gui.button.ButtonRect();
        JPanelTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        alumnos = new javax.swing.JTable();
        JPanelBusqueda = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        busqueda = new elaprendiz.gui.textField.TextField();
        rbCodigo = new javax.swing.JRadioButton();
        rbNombre = new javax.swing.JRadioButton();
        rbApellido = new javax.swing.JRadioButton();
        pnlPaginador = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();

        Nuevo_Grupo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/carrera.png"))); // NOI18N
        Nuevo_Grupo.setText("Nuevo Grupo");
        Nuevo_Grupo.setName("Grupo/Horario Principal"); // NOI18N
        Nuevo_Grupo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Nuevo_GrupoActionPerformed(evt);
            }
        });
        popupgrupo.add(Nuevo_Grupo);

        Actualizar_Grupo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/update.png"))); // NOI18N
        Actualizar_Grupo.setText("Actualizar Combo");
        Actualizar_Grupo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Actualizar_GrupoActionPerformed(evt);
            }
        });
        popupgrupo.add(Actualizar_Grupo);

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Alumnos");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("alumnos"); // NOI18N
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
        bntNuevo.setName("Nuevo Alumno"); // NOI18N
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
        bntModificar.setName("Modificar Alumno"); // NOI18N
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
        bntGuardar.setName("Guardar Alumno"); // NOI18N
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
        bntEliminar.setEnabled(false);
        bntEliminar.setName("Eliminar Alumno"); // NOI18N
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

        tbPane1.setOpaque(true);

        jPanel1.setBackground(java.awt.SystemColor.activeCaption);
        jPanel1.setLayout(null);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Código:");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(110, 20, 80, 20);

        codigo.setEditable(false);
        codigo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        codigo.setName("codigo"); // NOI18N
        codigo.setNextFocusableComponent(nombres);
        jPanel1.add(codigo);
        codigo.setBounds(200, 20, 130, 21);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Nombres:");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(110, 50, 80, 20);

        nombres.setEditable(false);
        nombres.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nombres.setName("nombres"); // NOI18N
        nombres.setNextFocusableComponent(apellidos);
        nombres.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nombresFocusLost(evt);
            }
        });
        nombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nombresKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombresKeyPressed(evt);
            }
        });
        jPanel1.add(nombres);
        nombres.setBounds(200, 50, 250, 21);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("Apellidos:");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(110, 80, 80, 20);

        apellidos.setEditable(false);
        apellidos.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        apellidos.setName("apellidos"); // NOI18N
        apellidos.setNextFocusableComponent(direccion);
        apellidos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                apellidosFocusLost(evt);
            }
        });
        apellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nombresKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombresKeyPressed(evt);
            }
        });
        jPanel1.add(apellidos);
        apellidos.setBounds(200, 80, 250, 21);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel14.setText("Dirección:");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(110, 110, 80, 20);

        direccion.setEditable(false);
        direccion.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        direccion.setNextFocusableComponent(sexo);
        jPanel1.add(direccion);
        direccion.setBounds(200, 110, 250, 21);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Fecha Nacimiento:");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(460, 80, 150, 21);

        fechanacimiento.setDate(Calendar.getInstance().getTime());
        fechanacimiento.setDateFormatString("dd/MM/yyyy");
        fechanacimiento.setEnabled(false);
        fechanacimiento.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        fechanacimiento.setMaxSelectableDate(new java.util.Date(3093496470100000L));
        fechanacimiento.setMinSelectableDate(new java.util.Date(-62135744300000L));
        fechanacimiento.setNextFocusableComponent(fechanacimiento);
        fechanacimiento.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel1.add(fechanacimiento);
        fechanacimiento.setBounds(620, 80, 130, 21);

        sexo.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        sexo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "F", "M" }));
        sexo.setName("sexo"); // NOI18N
        sexo.setNextFocusableComponent(estado);
        sexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sexoActionPerformed(evt);
            }
        });
        jPanel1.add(sexo);
        sexo.setBounds(620, 20, 130, 21);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel18.setText("Sexo:");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(530, 20, 80, 20);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Estado:");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(500, 50, 110, 20);

        estado.setBackground(new java.awt.Color(51, 153, 255));
        estado.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        estado.setForeground(new java.awt.Color(255, 255, 255));
        estado.setText("Activo");
        estado.setEnabled(false);
        estado.setNextFocusableComponent(fechanacimiento);
        estado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                estadoActionPerformed(evt);
            }
        });
        jPanel1.add(estado);
        estado.setBounds(620, 50, 130, 21);

        codigomineduc.setEditable(false);
        codigomineduc.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        codigomineduc.setNextFocusableComponent(nombres);
        jPanel1.add(codigomineduc);
        codigomineduc.setBounds(620, 110, 130, 21);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel29.setText("Código MINEDUC:");
        jPanel1.add(jLabel29);
        jLabel29.setBounds(460, 110, 150, 21);

        tbPane1.addTab("Datos del Alumno", jPanel1);

        jPanel2.setBackground(java.awt.SystemColor.activeCaption);
        jPanel2.setLayout(null);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel17.setText("Titular Apellidos:");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(0, 45, 130, 20);

        grado_establecimiento.setEditable(false);
        grado_establecimiento.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        grado_establecimiento.setNextFocusableComponent(titularapellido);
        jPanel2.add(grado_establecimiento);
        grado_establecimiento.setBounds(570, 80, 210, 21);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Nivel Academico:");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(450, 80, 120, 20);

        dpi.setEditable(false);
        dpi.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        dpi.setNextFocusableComponent(establecimiento);
        jPanel2.add(dpi);
        dpi.setBounds(140, 115, 250, 21);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel15.setText("DPI:");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(10, 115, 40, 20);

        telefono.setEditable(false);
        telefono.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        telefono.setNextFocusableComponent(dpi);
        jPanel2.add(telefono);
        telefono.setBounds(140, 80, 250, 21);

        titularapellido.setEditable(false);
        titularapellido.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        titularapellido.setNextFocusableComponent(telefono);
        jPanel2.add(titularapellido);
        titularapellido.setBounds(140, 45, 250, 21);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel20.setText("Telefono:");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(0, 80, 80, 20);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator1);
        jSeparator1.setBounds(430, 10, 10, 140);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Titular Nombres:");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(10, 10, 120, 20);

        titularnombre.setEditable(false);
        titularnombre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        titularnombre.setName("descripcion"); // NOI18N
        titularnombre.setNextFocusableComponent(titularapellido);
        jPanel2.add(titularnombre);
        titularnombre.setBounds(140, 10, 250, 21);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel16.setText("Establecimiento:");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(440, 10, 120, 20);

        establecimiento.setEditable(false);
        establecimiento.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        establecimiento.setNextFocusableComponent(direccion_establecimiento);
        jPanel2.add(establecimiento);
        establecimiento.setBounds(570, 10, 280, 21);

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("Direccion:");
        jPanel2.add(jLabel28);
        jLabel28.setBounds(450, 45, 90, 20);

        direccion_establecimiento.setEditable(false);
        direccion_establecimiento.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        direccion_establecimiento.setNextFocusableComponent(grado_establecimiento);
        jPanel2.add(direccion_establecimiento);
        direccion_establecimiento.setBounds(570, 45, 280, 21);

        tbPane1.addTab("Otros Datos", jPanel2);

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(786, 402));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        horarios.setForeground(new java.awt.Color(51, 51, 51));
        horarios.setModel(modelgrupo = new DefaultTableModel(null, titulosgrupo)
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
            jScrollPane2.setViewportView(horarios);

            jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

            tbPane1.addTab("Carreras Asignadas", jPanel3);

            jPanel4.setBackground(java.awt.SystemColor.activeCaption);
            jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jPanel4.setForeground(new java.awt.Color(204, 204, 204));
            jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel4.setLayout(null);

            jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel13.setText("Horario A:");
            jPanel4.add(jLabel13);
            jLabel13.setBounds(740, 0, 110, 20);

            jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            jLabel3.setText("Día:");
            jPanel4.add(jLabel3);
            jLabel3.setBounds(10, 20, 50, 27);

            jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel5.setText("Profesor:");
            jPanel4.add(jLabel5);
            jLabel5.setBounds(290, 20, 70, 20);

            jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel6.setText("Fecha Fin:");
            jPanel4.add(jLabel6);
            jLabel6.setBounds(740, 40, 110, 20);

            jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel19.setText("Horario De:");
            jPanel4.add(jLabel19);
            jLabel19.setBounds(620, 0, 100, 20);

            cDia.setEditable(true);
            cDia.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
            cDia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo", "Mixto" }));
            cDia.setName("cdia"); // NOI18N
            jPanel4.add(cDia);
            cDia.setBounds(70, 20, 180, 21);

            jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel21.setText("Fecha Inicio:");
            jPanel4.add(jLabel21);
            jLabel21.setBounds(620, 40, 110, 20);

            jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel22.setText("Carrera:");
            jPanel4.add(jLabel22);
            jLabel22.setBounds(290, 60, 70, 20);

            cGrupo.setEditable(true);
            cGrupo.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
            cGrupo.setComponentPopupMenu(popupgrupo);
            cGrupo.setName("cgrupo"); // NOI18N
            jPanel4.add(cGrupo);
            cGrupo.setBounds(70, 60, 180, 21);

            jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            jLabel23.setText("Grupo:");
            jPanel4.add(jLabel23);
            jLabel23.setBounds(10, 60, 50, 27);

            jLabel24.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
            jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel24.setText("Inscripción Q.");
            jPanel4.add(jLabel24);
            jLabel24.setBounds(620, 100, 110, 20);

            jLabel25.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
            jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel25.setText("Colegiatura Q.");
            jPanel4.add(jLabel25);
            jLabel25.setBounds(740, 100, 110, 20);

            carrera.setEditable(false);
            carrera.setHorizontalAlignment(javax.swing.JTextField.LEFT);
            carrera.setName("codigo"); // NOI18N
            carrera.setPreferredSize(new java.awt.Dimension(120, 21));
            jPanel4.add(carrera);
            carrera.setBounds(360, 60, 250, 21);

            profesor.setEditable(false);
            profesor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
            profesor.setName("codigo"); // NOI18N
            profesor.setPreferredSize(new java.awt.Dimension(120, 21));
            jPanel4.add(profesor);
            profesor.setBounds(360, 20, 250, 21);

            inscripcion.setEditable(false);
            inscripcion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
            inscripcion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            inscripcion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            inscripcion.setName("inscripcion"); // NOI18N
            inscripcion.setPreferredSize(new java.awt.Dimension(80, 23));
            jPanel4.add(inscripcion);
            inscripcion.setBounds(620, 120, 110, 23);

            colegiatura.setEditable(false);
            colegiatura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
            colegiatura.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            colegiatura.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            colegiatura.setName("colegiatura"); // NOI18N
            colegiatura.setPreferredSize(new java.awt.Dimension(80, 23));
            jPanel4.add(colegiatura);
            colegiatura.setBounds(740, 120, 110, 23);

            horaa.setEditable(false);
            horaa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            horaa.setName("codigo"); // NOI18N
            horaa.setPreferredSize(new java.awt.Dimension(120, 21));
            jPanel4.add(horaa);
            horaa.setBounds(740, 20, 110, 21);

            horade.setEditable(false);
            horade.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            horade.setName("codigo"); // NOI18N
            horade.setPreferredSize(new java.awt.Dimension(120, 21));
            jPanel4.add(horade);
            horade.setBounds(620, 20, 110, 21);

            fechaini.setEditable(false);
            fechaini.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            fechaini.setName("codigo"); // NOI18N
            fechaini.setPreferredSize(new java.awt.Dimension(120, 21));
            jPanel4.add(fechaini);
            fechaini.setBounds(620, 60, 110, 21);

            fechafin.setEditable(false);
            fechafin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            fechafin.setName("codigo"); // NOI18N
            fechafin.setPreferredSize(new java.awt.Dimension(120, 21));
            jPanel4.add(fechafin);
            fechafin.setBounds(740, 60, 110, 21);

            buttonAction1.setText("NuevoGrupo/Carrera");
            buttonAction1.setEnabled(false);
            buttonAction1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
            buttonAction1.setName("NuevoGrupo/Carrera Alumno"); // NOI18N
            buttonAction1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonAction1ActionPerformed(evt);
                }
            });
            jPanel4.add(buttonAction1);
            buttonAction1.setBounds(70, 110, 180, 35);

            fechainicioalumno.setDate(Calendar.getInstance().getTime());
            fechainicioalumno.setDateFormatString("dd/MM/yyyy");
            fechainicioalumno.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
            fechainicioalumno.setMaxSelectableDate(new java.util.Date(3093496470100000L));
            fechainicioalumno.setMinSelectableDate(new java.util.Date(-62135744300000L));
            fechainicioalumno.setNextFocusableComponent(grado_establecimiento);
            fechainicioalumno.setPreferredSize(new java.awt.Dimension(120, 22));
            jPanel4.add(fechainicioalumno);
            fechainicioalumno.setBounds(290, 120, 100, 21);

            jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
            jLabel27.setForeground(new java.awt.Color(0, 0, 255));
            jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel27.setText("Beca Alumno:");
            jPanel4.add(jLabel27);
            jLabel27.setBounds(410, 100, 90, 20);

            becagrupo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
            becagrupo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            becagrupo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            becagrupo.setName("becaalumno"); // NOI18N
            becagrupo.setPreferredSize(new java.awt.Dimension(80, 23));
            jPanel4.add(becagrupo);
            becagrupo.setBounds(410, 120, 90, 23);

            jLabel30.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
            jLabel30.setForeground(new java.awt.Color(0, 0, 255));
            jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel30.setText("Inscripción:");
            jPanel4.add(jLabel30);
            jLabel30.setBounds(520, 100, 90, 16);

            inscripalumno.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new FormatoDecimal("#####0.00",true))));
            inscripalumno.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            inscripalumno.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            inscripalumno.setName("inscripalumno"); // NOI18N
            inscripalumno.setPreferredSize(new java.awt.Dimension(80, 23));
            jPanel4.add(inscripalumno);
            inscripalumno.setBounds(520, 120, 90, 23);

            jLabel31.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
            jLabel31.setForeground(new java.awt.Color(0, 0, 255));
            jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel31.setText("Fecha Inicio:");
            jPanel4.add(jLabel31);
            jLabel31.setBounds(290, 100, 100, 16);
            jPanel4.add(jSeparator2);
            jSeparator2.setBounds(290, 93, 320, 10);

            tbPane1.addTab("Matricular Alumno", jPanel4);

            jPanel5.setBackground(java.awt.SystemColor.activeCaption);
            jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            jPanel5.setForeground(new java.awt.Color(204, 204, 204));
            jPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel5.setLayout(null);

            jLabel32.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel32.setText("Motivo Baja:");
            jPanel5.add(jLabel32);
            jLabel32.setBounds(70, 50, 90, 21);

            fechabaja.setDate(Calendar.getInstance().getTime());
            fechabaja.setDateFormatString("dd/MM/yyyy");
            fechabaja.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
            fechabaja.setMaxSelectableDate(new java.util.Date(3093496470100000L));
            fechabaja.setMinSelectableDate(new java.util.Date(-62135744300000L));
            fechabaja.setName("JDateChooserbaja"); // NOI18N
            fechabaja.setNextFocusableComponent(fechanacimiento);
            fechabaja.setPreferredSize(new java.awt.Dimension(120, 22));
            jPanel5.add(fechabaja);
            fechabaja.setBounds(170, 20, 130, 21);

            jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            motivobaja.setColumns(20);
            motivobaja.setRows(5);
            motivobaja.setName("motivobaja"); // NOI18N
            jScrollPane4.setViewportView(motivobaja);

            jPanel5.add(jScrollPane4);
            jScrollPane4.setBounds(170, 50, 440, 80);

            jLabel33.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel33.setText("Fecha Baja:");
            jPanel5.add(jLabel33);
            jLabel33.setBounds(80, 20, 80, 21);

            buttonMostrar1.setBackground(new java.awt.Color(102, 204, 0));
            buttonMostrar1.setText("Datos del Usuario");
            buttonMostrar1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonMostrar1ActionPerformed(evt);
                }
            });
            jPanel5.add(buttonMostrar1);
            buttonMostrar1.setBounds(630, 50, 140, 25);

            tbPane1.addTab("Baja Alumno", jPanel5);

            JPanelCampos.add(tbPane1);
            tbPane1.setBounds(0, 0, 880, 190);

            panelImage.add(JPanelCampos);
            JPanelCampos.setBounds(0, 40, 880, 190);

            JPanelTable.setOpaque(false);
            JPanelTable.setPreferredSize(new java.awt.Dimension(786, 402));
            JPanelTable.setLayout(new java.awt.BorderLayout());

            jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            alumnos.setForeground(new java.awt.Color(51, 51, 51));
            alumnos.setModel(model = new DefaultTableModel(null, titulos)
                {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                alumnos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                alumnos.setFocusCycleRoot(true);
                alumnos.setGridColor(new java.awt.Color(51, 51, 255));
                alumnos.setRowHeight(22);
                alumnos.setSelectionBackground(java.awt.SystemColor.activeCaption);
                alumnos.setSurrendersFocusOnKeystroke(true);
                alumnos.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        alumnosMouseClicked(evt);
                    }
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        alumnosMouseClicked(evt);
                    }
                });
                alumnos.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        alumnosKeyPressed(evt);
                    }
                });
                jScrollPane1.setViewportView(alumnos);
                alumnos.getAccessibleContext().setAccessibleName("");

                JPanelTable.add(jScrollPane1, java.awt.BorderLayout.CENTER);

                panelImage.add(JPanelTable);
                JPanelTable.setBounds(0, 300, 880, 130);

                JPanelBusqueda.setBackground(java.awt.SystemColor.inactiveCaption);
                JPanelBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                JPanelBusqueda.setLayout(null);

                jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar.png"))); // NOI18N
                jLabel7.setText("Buscar Alumno por:");
                JPanelBusqueda.add(jLabel7);
                jLabel7.setBounds(117, 2, 173, 40);

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
                rbCodigo.setBounds(280, 40, 80, 25);

                rbNombre.setBackground(java.awt.SystemColor.inactiveCaption);
                rbNombre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                rbNombre.setForeground(new java.awt.Color(0, 102, 102));
                rbNombre.setSelected(true);
                rbNombre.setText("Nombre");
                rbNombre.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        rbNombreActionPerformed(evt);
                    }
                });
                JPanelBusqueda.add(rbNombre);
                rbNombre.setBounds(380, 40, 90, 25);

                rbApellido.setBackground(java.awt.SystemColor.inactiveCaption);
                rbApellido.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                rbApellido.setForeground(new java.awt.Color(0, 102, 102));
                rbApellido.setText("Apellido");
                rbApellido.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        rbApellidoActionPerformed(evt);
                    }
                });
                JPanelBusqueda.add(rbApellido);
                rbApellido.setBounds(490, 40, 79, 25);

                panelImage.add(JPanelBusqueda);
                JPanelBusqueda.setBounds(0, 230, 880, 70);

                pnlPaginador.setBackground(new java.awt.Color(57, 104, 163));
                pnlPaginador.setPreferredSize(new java.awt.Dimension(786, 40));
                pnlPaginador.setLayout(new java.awt.GridBagLayout());

                jLabel8.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
                jLabel8.setForeground(new java.awt.Color(255, 255, 255));
                jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/alumno.png"))); // NOI18N
                jLabel8.setText("<--Alumnos-->");
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
            estado.setSelected(true);
            this.bntGuardar.setEnabled(true);
            this.bntModificar.setEnabled(false);
            this.bntEliminar.setEnabled(false);
            this.bntNuevo.setEnabled(false);
            //colegiatura.setEditable(false);
            //beca.setEditable(false);
            codigo.setEditable(false);
            profesor.setEditable(false);
            horade.setEditable(false);
            horaa.setEditable(false);
            carrera.setEditable(false);
            fechaini.setEditable(false);
            fechafin.setEditable(false);
            inscripcion.setEditable(false);
            colegiatura.setEditable(false);
            buttonAction1.setEnabled(false);

            nombres.requestFocus();
            nidalumno = 0;
            matricula = true;
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");

        }
    }//GEN-LAST:event_bntNuevoActionPerformed

    private void bntGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardarActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntGuardar.getName()) == true) {
            if (Utilidades.esObligatorio(this.jPanel1, true) || (Utilidades.esObligatorio(this.jPanel2, true)) || (Utilidades.esObligatorio(this.jPanel4, true))) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Float.parseFloat(becagrupo.getText()) > Float.parseFloat(colegiatura.getText()) || Float.parseFloat(becagrupo.getText()) > Float.parseFloat(colegiatura.getText())) {
                JOptionPane.showInternalMessageDialog(this, "La beca debe ser menor al monto de Inscripcion y Colegiatura", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (fechainicioalumno.getCalendar().getTime().after(FormatoFecha.StringToDate(fechafin.getText()))) {
                JOptionPane.showInternalMessageDialog(this, "Las fecha de inicio debe ser menor a la fecha fin del Grupo", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (fechainicioalumno.getCalendar().getTime().before(FormatoFecha.StringToDate(fechaini.getText())) /*|| fechainicioalumno.getCalendar().getTime().equals(FormatoFecha.StringToDate(fechaini.getText()))*/) {
                JOptionPane.showInternalMessageDialog(this, "Las fecha de inicio debe ser mayor o igual a la fecha inicio del Grupo", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.print(fechainicioalumno.getCalendar().getTime().toString() + "--" + FormatoFecha.StringToDate(fechaini.getText()));
                return;
            }
            int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
            if (resp == 0) {

                String mensagecodigo = null;
                codigoalumno();
                String fechanacimient = FormatoFecha.getFormato(fechanacimiento.getCalendar().getTime(), FormatoFecha.A_M_D);
                String alumnoid = "";

                int estad = 0;
                if (this.estado.isSelected()) {
                    estad = 1;
                }
                if (becagrupo.getText().isEmpty()) {
                    becagrupo.setValue(0);
                }

                String sql1 = "insert into alumno (codigo, nombres, apellidos, fechanacimiento, sexo, direccion, telefono, titularnombres, titularapellidos, titulardpi, establecimiento, direccionestablecimiento, gradoestablecimiento, estado, codigomineduc) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                int n = 0;
                PreparedStatement ps = null;
                conn = BdConexion.getConexion();
                mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
                String idg = grup.getID();

                if (cantalumnosgrupo(idg) == false) {
                    JOptionPane.showInternalMessageDialog(this, "El grupo se encuentra lleno.\nAsigne un grupo diferente o amplie la capacidad del Grupo", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // DATOS DEL ALUMNO ********************************************
                    conn.setAutoCommit(false);
                    ps = conn.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setString(1, codigo.getText());
                    ps.setString(2, nombres.getText());
                    ps.setString(3, apellidos.getText());
                    ps.setString(4, fechanacimient);
                    ps.setString(5, "" + sexo.getSelectedItem());
                    ps.setString(6, direccion.getText());
                    ps.setString(7, telefono.getText());
                    ps.setString(8, titularnombre.getText());
                    ps.setString(9, titularapellido.getText());
                    ps.setString(10, dpi.getText());
                    ps.setString(11, establecimiento.getText());
                    ps.setString(12, direccion_establecimiento.getText());
                    ps.setString(13, grado_establecimiento.getText());
                    ps.setString(14, "" + estad);
                    ps.setString(15, codigomineduc.getText());

                    n = ps.executeUpdate();
                    if (n > 0) {// Si los datos del alumno se guardaron
                        ResultSet rs = ps.getGeneratedKeys();
                        while (rs.next()) {
                            alumnoid = "" + rs.getInt(1);//retorna el idalumno guardado
                        }
                        if (cGrupo.getSelectedIndex() != -1) {

                            // DATOS DEL ALUMNOS EN GRUPO **************************
                            mensagecodigo = codigo.getText();
                            String fechainicioalum = FormatoFecha.getFormato(fechainicioalumno.getCalendar().getTime(), FormatoFecha.A_M_D);

                            String alumnosengrup = "insert into alumnosengrupo  (alumno_idalumno, grupo_idgrupo,fechainicio,beca) values ('" + alumnoid + "','" + idg + "','" + fechainicioalum + "','" + becagrupo.getText() + "' )";
                            n = ps.executeUpdate(alumnosengrup);

                            if (n > 0) { // SI los datos del alumnosengrupo se guardaron 

                                // DATOS DEL PROYECCION DE PAGOS *******************
                                idalumnosengrupo(alumnoid, idg); // Obtiene el idalumnosengrupo
                                String proyeccionp = "INSERT INTO proyeccionpagos (mes_idmes, año, monto, fechavencimiento,alumnosengrupo_iddetallegrupo)\n"
                                        + "SELECT mes_idmes, año,(monto-" + becagrupo.getText() + "),fechavencimiento," + "'" + iddetallegrupo + "' from pagos \n"
                                        + "WHERE pagos.grupo_idgrupo='" + idg + "'";
                                n = ps.executeUpdate(proyeccionp);

                                if (n > 0) {// Si los datos de proyeccionpagos se guardaron
                                    String sqlinscripcion = "update proyeccionpagos set  monto='" + inscripalumno.getText() + "' where mes_idmes='13' and alumnosengrupo_iddetallegrupo=" + iddetallegrupo;
                                    n = ps.executeUpdate(sqlinscripcion);

                                } else {
                                    JOptionPane.showInternalMessageDialog(this, "Los pagos no se Guardaron", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                                JOptionPane.showInternalMessageDialog(this, "El alumno no se ha Guardado con el código:\n" + mensagecodigo, "Guardar", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showInternalMessageDialog(this, "El grupo no se Guardo", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showInternalMessageDialog(this, "El alumno no se ha Guardado con el código:\n" + mensagecodigo, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    conn.commit();// guarda todas las consultas si no ubo error
                    ps.close();
                    if (!conn.getAutoCommit()) {
                        conn.setAutoCommit(true);
                    }
                    //Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
                    Utilidades.esObligatorio(this.jPanel1, false);
                    Utilidades.esObligatorio(this.jPanel2, false);
                    //Utilidades.esObligatorio(this.jPanel3, false);
                    Utilidades.esObligatorio(this.jPanel4, false);
                    Utilidades.esObligatorio(this.jPanel5, false);

                    Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
                    MostrarDatos(busqueda.getText());
                    removejtablegrupo();
                    this.bntGuardar.setEnabled(false);
                    this.bntModificar.setEnabled(false);
                    this.bntEliminar.setEnabled(false);
                    this.bntNuevo.setEnabled(true);
                    busqueda.requestFocus();
                    idalumno = 0;
                    matricula = false;
                    buttonAction1.setEnabled(false);

                } catch (SQLException ex) {
                    try {
                        conn.rollback();// no guarda ninguna de las consultas ya que ubo error
                        ps.close();
                        if (!conn.getAutoCommit()) {
                            conn.setAutoCommit(true);
                        }
                    } catch (SQLException ex1) {
                        Logger.getLogger(Pagos.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");

        }
    }//GEN-LAST:event_bntGuardarActionPerformed

    private void bntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalirActionPerformed
        cerrarVentana();
    }//GEN-LAST:event_bntSalirActionPerformed

    private void alumnosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_alumnosMouseClicked
        // TODO add your handling code here:
        filaseleccionada();

    }//GEN-LAST:event_alumnosMouseClicked

    private void bntEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntEliminar.getName()) == true) {

            int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Eliminar el Registro?", "Pregunta", 0);
            if (resp == 0) {

                int fila = alumnos.getSelectedRow();
                String id = (String) "" + alumnos.getValueAt(fila, 0);
                String nombreTabla = "alumno", nomColumnaCambiar = "estado";
                String nomColumnaId = "codigo";
                int seguardo = 0;

                seguardo = peticiones.eliminarRegistro(nombreTabla, nomColumnaCambiar, nomColumnaId, id);

                if (seguardo == 1) {
                    Utilidades.setEditableTexto(this.JPanelCampos, true, null, true, "");
                    MostrarDatos(busqueda.getText());
                    removejtablegrupo();
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
            if (Utilidades.esObligatorio(this.jPanel1, true) || (Utilidades.esObligatorio(this.jPanel2, true))) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!estado.isSelected()) {
                if (fechabaja.getDate() == null || motivobaja.getText().isEmpty()) {
                    if (fechabaja.getDate() == null) {
                        ((JTextFieldDateEditor) ((JDateChooser) fechabaja).getDateEditor()).setBackground(BObligatorio);
                        ((JTextFieldDateEditor) ((JDateChooser) fechabaja).getDateEditor()).setForeground(FObligatorio);
                    } else {
                        ((JTextFieldDateEditor) ((JDateChooser) fechabaja).getDateEditor()).setBackground(Color.WHITE);
                        ((JTextFieldDateEditor) ((JDateChooser) fechabaja).getDateEditor()).setForeground(Color.BLACK);
                    }
                    if (motivobaja.getText().isEmpty()) {
                        motivobaja.setBackground(BObligatorio);
                        motivobaja.setForeground(FObligatorio);
                    } else {
                        motivobaja.setBackground(Color.WHITE);
                        motivobaja.setForeground(Color.BLACK);
                    }
                    JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
            int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Modificar el Registro?", "Pregunta", 0);
            if (resp == 0) {

                codigoalumno();
                int seguardo = 0;
                int fila = alumnos.getSelectedRow();
                String id = (String) "" + alumnos.getValueAt(fila, 0);
                String columnaId = "codigo";

                String nombreTabla = "alumno";
                String fechanacimient = FormatoFecha.getFormato(fechanacimiento.getCalendar().getTime(), FormatoFecha.A_M_D);

                int estad = 0;

                if (estado.isSelected()) {
                    estad = 1;
                    String campos1 = "codigo, nombres, apellidos, fechanacimiento, sexo, direccion, telefono, titularnombres, titularapellidos, titulardpi, establecimiento, direccionestablecimiento, gradoestablecimiento, estado";
                    Object[] valores1 = {codigo.getText(), nombres.getText(), apellidos.getText(), fechanacimient, sexo.getSelectedItem(), direccion.getText(), telefono.getText(), titularnombre.getText(), titularapellido.getText(), dpi.getText(), establecimiento.getText(), direccion_establecimiento.getText(), grado_establecimiento.getText(), estad, id
                    };
                    seguardo = peticiones.actualizarRegistro(nombreTabla, campos1, valores1, columnaId, id);
                } else if (!estado.isSelected()) {
                    String fechabajaalumno = FormatoFecha.getFormato(fechabaja.getCalendar().getTime(), FormatoFecha.A_M_D);
                    String campos2 = "codigo, nombres, apellidos, fechanacimiento, sexo, direccion, telefono, titularnombres, titularapellidos, titulardpi, establecimiento, direccionestablecimiento, gradoestablecimiento, estado, codigomineduc, fechabaja, observacion";
                    Object[] valores2 = {codigo.getText(), nombres.getText(), apellidos.getText(), fechanacimient, sexo.getSelectedItem(), direccion.getText(), telefono.getText(), titularnombre.getText(), titularapellido.getText(), dpi.getText(), establecimiento.getText(), direccion_establecimiento.getText(), grado_establecimiento.getText(), estad, codigomineduc.getText(), fechabajaalumno, motivobaja.getText(), id
                    };
                    seguardo = peticiones.actualizarRegistro(nombreTabla, campos2, valores2, columnaId, id);
                }

                if (seguardo == 1) {
                    Utilidades.setEditableTexto(this.JPanelCampos, false, null, true, "");
                    MostrarDatos(busqueda.getText());
                    removejtablegrupo();
                    this.bntGuardar.setEnabled(false);
                    this.bntModificar.setEnabled(false);
                    this.bntEliminar.setEnabled(false);
                    this.bntNuevo.setEnabled(true);
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
        Utilidades.esObligatorio(this.jPanel1, false);
        Utilidades.esObligatorio(this.jPanel2, false);
        //Utilidades.esObligatorio(this.jPanel3, false);
        Utilidades.esObligatorio(this.jPanel4, false);
        Utilidades.esObligatorio(this.jPanel5, false);
        this.bntGuardar.setEnabled(false);
        this.bntModificar.setEnabled(false);
        this.bntEliminar.setEnabled(false);
        this.bntNuevo.setEnabled(true);
        removejtable();
        removejtablegrupo();
        busqueda.requestFocus();
        nidalumno = 0;
        matricula = false;
        buttonAction1.setEnabled(false);
    }//GEN-LAST:event_bntCancelarActionPerformed

    private void rbCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCodigoActionPerformed
        // TODO add your handling code here:
        rbNombre.setSelected(false);
        rbApellido.setSelected(false);
        busqueda.requestFocus();
    }//GEN-LAST:event_rbCodigoActionPerformed

    private void rbNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNombreActionPerformed
        // TODO add your handling code here:
        rbCodigo.setSelected(false);
        rbApellido.setSelected(false);
        busqueda.requestFocus();
    }//GEN-LAST:event_rbNombreActionPerformed

    private void busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busquedaActionPerformed
        // TODO add your handling code here:
        MostrarDatos(busqueda.getText());
    }//GEN-LAST:event_busquedaActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing

    private void alumnosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == java.awt.event.KeyEvent.VK_SPACE) {
            filaseleccionada();
        }
        if (key == java.awt.event.KeyEvent.VK_DOWN || key == java.awt.event.KeyEvent.VK_UP) {
            limpiar();
        }
    }//GEN-LAST:event_alumnosKeyPressed

    private void rbApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbApellidoActionPerformed
        // TODO add your handling code here:
        rbCodigo.setSelected(false);
        rbNombre.setSelected(false);
        busqueda.requestFocus();
    }//GEN-LAST:event_rbApellidoActionPerformed

    private void sexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sexoActionPerformed

    private void nombresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombresKeyPressed
        // TODO add your handling code here:
        codigoalumno();
    }//GEN-LAST:event_nombresKeyPressed

    private void nombresFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nombresFocusLost
        // TODO add your handling code here:
        nombres.setText(nombres.getText().toUpperCase());
    }//GEN-LAST:event_nombresFocusLost

    private void apellidosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_apellidosFocusLost
        // TODO add your handling code here:
        apellidos.setText(apellidos.getText().toUpperCase());
    }//GEN-LAST:event_apellidosFocusLost

    private void buttonAction1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAction1ActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(buttonAction1.getName()) == true) {
            if (matricula == false) {
                if ((Utilidades.esObligatorio(this.jPanel4, true))) {
                    JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
                if (resp == 0) {
                    if (cGrupo.getSelectedIndex() != -1) {

                        //Para saber si el grupo ya ha sigo asignado y no asiganar el mismo grupo dos veces.
                        boolean carrera = false;
                        int cant = modelgrupo.getRowCount();
                        if (cant != 0) {
                            for (int i = 0; i < cant; i++) {
                                if (modelgrupo.getValueAt(i, 0).toString().equals(cGrupo.getSelectedItem().toString())) {
                                    carrera = true;
                                }
                            }
                        }

                        if (carrera == false) { // si no se ha asignado el grupo 
                            mGrupo grup = (mGrupo) cGrupo.getSelectedItem();
                            String idg = grup.getID();

                            if (cantalumnosgrupo(idg) == false) {
                                JOptionPane.showInternalMessageDialog(this, "El grupo se encuentra lleno.\nAsigne un grupo diferente o amplie la capacidad del Grupo", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            idalumnog(codigo.getText());
                            String fechainicioalum = FormatoFecha.getFormato(fechainicioalumno.getCalendar().getTime(), FormatoFecha.A_M_D);
                            String alumnoid = "" + idalumno;
                            int n = 0;
                            String alumnosengrup = "insert into alumnosengrupo  (alumno_idalumno, grupo_idgrupo,fechainicio,beca) values ('" + alumnoid + "','" + idg + "','" + fechainicioalum + "','" + becagrupo.getText() + "' )";

                            PreparedStatement ps = null;
                            conn = BdConexion.getConexion();

                            try {
                                conn.setAutoCommit(false);
                                ps = conn.prepareStatement(alumnosengrup);
                                n = ps.executeUpdate();

                                if (n > 0) {
                                    idalumnosengrupo(alumnoid, idg);
                                    String sql = "INSERT INTO proyeccionpagos (mes_idmes, año, monto, fechavencimiento,alumnosengrupo_iddetallegrupo)\n"
                                            + "SELECT mes_idmes, año,monto,fechavencimiento," + "'" + iddetallegrupo + "' from pagos \n"
                                            + "WHERE pagos.`grupo_idgrupo`='" + idg + "'";
                                    n = ps.executeUpdate(sql);

                                    if (n > 0) {
                                        String sqlinscripcion = "update proyeccionpagos set  monto='" + inscripalumno.getText() + "' where mes_idmes='13' and alumnosengrupo_iddetallegrupo=" + iddetallegrupo;
                                        n = ps.executeUpdate(sqlinscripcion);
                                    } else {
                                        JOptionPane.showInternalMessageDialog(this, "Los pagos no se Guardaron", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                    filaseleccionada();
                                    JOptionPane.showInternalMessageDialog(this, "El grupo se ha asignado Correctamente", "Guardar", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showInternalMessageDialog(this, "El grupo no se Guardo", "Error", JOptionPane.ERROR_MESSAGE);
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
                                    JOptionPane.showMessageDialog(null, ex);
                                    Logger.getLogger(Pagos.class.getName()).log(Level.SEVERE, null, ex1);
                                }
                                JOptionPane.showMessageDialog(null, ex);
                                Logger.getLogger(Alumno.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else {
                            JOptionPane.showInternalMessageDialog(this, "El grupo ya fue asiganado al alumno\n"
                                    + " porfavor selecciones uno diferente ", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } else {
                JOptionPane.showInternalMessageDialog(this, "Debe seleccionar un alumno antes de asignar Nuevo Grupo", "Mensage", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");

        }

    }//GEN-LAST:event_buttonAction1ActionPerformed

    private void Nuevo_GrupoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Nuevo_GrupoActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(Nuevo_Grupo.getName()) == true) {
            Horario frmHorario = new Horario();
            if (frmHorario == null) {
                frmHorario = new Horario();
            }
            adminInternalFrame(dp, frmHorario);
        } else {
            JOptionPane.showMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_Nuevo_GrupoActionPerformed

    private void Actualizar_GrupoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Actualizar_GrupoActionPerformed
        // TODO add your handling code here:
        selecciondia();
    }//GEN-LAST:event_Actualizar_GrupoActionPerformed

    private void estadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_estadoActionPerformed
        // TODO add your handling code here:
        if (alumnos.getSelectedRow() != -1) {
            if (!estado.isSelected()) {
                tbPane1.addTab("Baja Alumno", jPanel5);
                Utilidades.setEditableTexto(this.jPanel5, true, null, true, "");
                tbPane1.setSelectedComponent(jPanel5);
            } else if (estado.isSelected()) {
                tbPane1.remove(jPanel5);
            }
        }
    }//GEN-LAST:event_estadoActionPerformed

    private void buttonMostrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMostrar1ActionPerformed
        // TODO add your handling code here:
        tbPane1.setSelectedComponent(jPanel1);
    }//GEN-LAST:event_buttonMostrar1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Actualizar_Grupo;
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelCampos;
    private javax.swing.JPanel JPanelTable;
    private javax.swing.JMenuItem Nuevo_Grupo;
    private javax.swing.JTable alumnos;
    private elaprendiz.gui.textField.TextField apellidos;
    private javax.swing.JFormattedTextField becagrupo;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntEliminar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntModificar;
    private elaprendiz.gui.button.ButtonRect bntNuevo;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    private elaprendiz.gui.textField.TextField busqueda;
    private elaprendiz.gui.button.ButtonAction buttonAction1;
    private elaprendiz.gui.button.ButtonRect buttonMostrar1;
    public static javax.swing.JComboBox cDia;
    public static javax.swing.JComboBox cGrupo;
    private elaprendiz.gui.textField.TextField carrera;
    private elaprendiz.gui.textField.TextField codigo;
    private elaprendiz.gui.textField.TextField codigomineduc;
    private javax.swing.JFormattedTextField colegiatura;
    private elaprendiz.gui.textField.TextField direccion;
    private elaprendiz.gui.textField.TextField direccion_establecimiento;
    private elaprendiz.gui.textField.TextField dpi;
    private elaprendiz.gui.textField.TextField establecimiento;
    private javax.swing.JRadioButton estado;
    private com.toedter.calendar.JDateChooser fechabaja;
    private elaprendiz.gui.textField.TextField fechafin;
    private elaprendiz.gui.textField.TextField fechaini;
    private com.toedter.calendar.JDateChooser fechainicioalumno;
    private com.toedter.calendar.JDateChooser fechanacimiento;
    private elaprendiz.gui.textField.TextField grado_establecimiento;
    private elaprendiz.gui.textField.TextField horaa;
    private elaprendiz.gui.textField.TextField horade;
    private javax.swing.JTable horarios;
    private javax.swing.JFormattedTextField inscripalumno;
    private javax.swing.JFormattedTextField inscripcion;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea motivobaja;
    private elaprendiz.gui.textField.TextField nombres;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador;
    private javax.swing.JPopupMenu popupgrupo;
    private elaprendiz.gui.textField.TextField profesor;
    private javax.swing.JRadioButton rbApellido;
    private javax.swing.JRadioButton rbCodigo;
    private javax.swing.JRadioButton rbNombre;
    private javax.swing.JComboBox sexo;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane1;
    private elaprendiz.gui.textField.TextField telefono;
    private elaprendiz.gui.textField.TextField titularapellido;
    private elaprendiz.gui.textField.TextField titularnombre;
    // End of variables declaration//GEN-END:variables
}
