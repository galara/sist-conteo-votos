package Capa_Negocio;

import Capa_Datos.AccesoDatos;
import Capa_Datos.OpSql;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.util.Hashtable;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

/**
 *
 * @author GLARA
 */
public class Peticiones extends AccesoDatos {

    /*Metodo guarda registros desde un procedimiento almacenado
     *Para mas detalle consultal la clase AccesoDatos.agregarRegistroPs( )
     */
    public boolean guardarRegistro(Object[] valores1, String ps) {
        int gravado;
        boolean resp;

        gravado = this.agregarRegistroPs(ps, valores1);

        if (gravado == 1) {
            resp = true;
        } else {
            resp = true;
        }
        return resp;

    }

    /**
     * Paa varias condiciones WHERE campo1=condicionid1 and campo2=condicionid2
     * ...
     *
     * @param nombreTabla , nombre de la tabla en la BD
     * @param campos , los campos de la tabla a consultar ejem: nombre, codigo ,
     * rirección etc
     * @param valores , los valores que guardaremos en la BD.
     * @return
     */
    public boolean guardarRegistros(String nombreTabla, String campos, Object[] valores) {

        int gravado = 0;
        gravado = this.agregarRegistroPss(nombreTabla, this.stringToArray(campos, ","), valores);
        if (gravado == 1) {
            return true;
        } else {
            return false;
        }

    }

    public int actualizarRegistro(String nomTabla, String campos, Object[] valores, String columnaId, Object id) {
        int gravado = 0;
        gravado = this.actualizarRegistroPs(nomTabla, this.adjuntarSimbolo(campos, ",", "?") + OpSql.WHERE + columnaId + " = ? ", valores);
        return gravado;
    }

    public int eliminarRegistro(String nombreTabla, String nomColumnaCambiar, String nomColumnaId, Object id) {

        int gravado = 0;
        gravado = this.eliminacionReal(nombreTabla, nomColumnaCambiar, nomColumnaId, id);
        return gravado;
    }

    /**
     * Paa varias condiciones WHERE campo1=condicionid1 and campo2=condicionid2
     * ...
     *
     * @param modelo ,modelo de la JTable
     * @param tabla , el nombre de la tabla a consultar en la BD
     * @param campos , los campos de la tabla a consultar ejem: nombre, codigo ,
     * rirección etc
     * @param campocondicion , los campos de la tabla para las condiciones ejem:
     * id,estado etc
     * @param condicionid , los valores que se compararan con campocondicion
     * ejem: campocondicion = condicionid
     * @return
     */
    public DefaultTableModel getRegistroPorPks(DefaultTableModel modelo, String tabla, String[] campos, String[] campocondicion, String[] condicionid, String inner) {
        try {

            /*rs es un ResultSet, una tabla de datos que representan un conjunto de resultados de base de datos,
             * generados mediante la ejecución de una consulta a la base de datos en el metodo getRegistros()
             */
            rs = this.getRegistros(tabla, campos, campocondicion, condicionid, inner);
            int cantcampos = campos.length;

            //if (rs != null) {
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                rs.beforeFirst();//regresa el puntero al primer registro

                Object[] fila = new Object[cantcampos];

                while (rs.next()) {//mientras tenga registros 

                    // Se rellena cada posición del array con una de las columnas de la tabla del rs.
                    for (int i = 0; i < cantcampos; i++) {

                        fila[i] = rs.getObject(i + 1); // El primer indice en rs es el 1, no el cero, por eso se suma 1.
                        if (fila[i].equals(true)) {
                            fila[i] = "Activo";
                        }
                        if (fila[i].equals(false)) {
                            fila[i] = "Inactivo";
                        }
                        if (campos[i].equals("horario.horariode") || campos[i].equals("horario.horarioa") || campos[i].equals("horariode") || campos[i].equals("horarioa")) {
                            fila[i] = FormatoFecha.getTimedoce(rs.getTime(i + 1));
                        }
                    }
                    modelo.addRow(fila);
                }
            } //} 
            else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Mensage", JOptionPane.INFORMATION_MESSAGE);
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
     * @param campos , los campos de la tabla a consultar ejem: nombre, codigo
     * ,dirección etc
     * @param campocondicion , los campos de la tabla para las condiciones ejem:
     * id,estado etc
     * @param condicionid , los valores que se compararan con campocondicion
     * ejem: campocondicion = condicionid
     * @return
     */
    public DefaultTableModel getRegistroPorLike(DefaultTableModel modelo, String tabla, String[] campos, String campocondicion, String condicionid, String inner) {
        try {
            rs = this.selectPorLike(tabla, campos, campocondicion, condicionid, inner);
            int cantcampos = campos.length;
            //if (rs != null) {
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                //int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos];

                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
                    for (int i = 0; i < cantcampos; i++) {

                        fila[i] = rs.getObject(i + 1); // El primer indice en rs es el 1, no el cero, por eso se suma 1.
                        if (fila[i] == null) {
                            fila[i] = "";
                        } else {
                            if (fila[i].equals(true)) {
                                fila[i] = "Activo";
                            }
                            if (fila[i].equals(false)) {
                                fila[i] = "Inactivo";
                            }
                            if (campos[i].equals("horario.horariode") || campos[i].equals("horario.horarioa") || campos[i].equals("horariode") || campos[i].equals("horarioa")) {
                                fila[i] = FormatoFecha.getTimedoce(rs.getTime(i + 1));
                            }
                        }
                    }
                    modelo.addRow(fila);
                }

            } //} 
            else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Mensage", JOptionPane.INFORMATION_MESSAGE);
            }
            rs.close();
            return modelo;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Obtiene datos para una condición WHERE campocondicion = id Luego rellena
     * los componentes del form con los datos obtenridos
     *
     * @param cmps , los componentes JComboBox , JTextFiel etc
     * @param tabla , el nombre de la tabla a consultar en la BD
     * @param campos , los campos de la tabla a consultar ejem: nombre, codigo
     * ,dirección etc
     * @param campocondicion, el campo de la tabla para las condiciones ejem:
     * idalumno etc
     * @param id , los valores que se compararan con campocondicion ejem: "lara"
     * @param inner , INNER JOINS PARA LA CONSULTA
     * @param has, tabla HasTable para la seleccion del JComboBox
     */
    public void getRegistroSeleccionado(Component[] cmps, String tabla, String[] campos, String[] campocondicion, String[] id, String inner, Hashtable has) {
        try {
            rs = this.getRegistros(tabla, campos, campocondicion, id, inner);
            int cantcampos = campos.length;
            Object[] fila = new Object[cantcampos];

            //if (rs != null) {
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                rs.beforeFirst();//regresa el puntero al primer registro

                while (rs.next()) {//mientras tenga registros 
                    // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
                    for (int i = 0; i < cantcampos; i++) {

                        /*
                         * Agregar los componenetes que falten 
                         */
                        if (cmps[i] instanceof JTextField) {
                            JTextComponent tmp = (JTextComponent) cmps[i];
                            tmp.setText(rs.getString(i + 1));
                        } else if (cmps[i] instanceof JSpinner) {
                            JSpinner tmp = (JSpinner) cmps[i];
                            tmp.setValue(rs.getTime(i + 1));
                        } else if (cmps[i] instanceof JFormattedTextField) {
                            JFormattedTextField tmp = (JFormattedTextField) cmps[i];
                            tmp.setValue(rs.getString(i + 1));
                        } else if (cmps[i] instanceof JDateChooser) {
                            JDateChooser tmp = (JDateChooser) cmps[i];
                            tmp.setDate((rs.getDate(i + 1)));
                        } else if (cmps[i] instanceof JComboBox) {
                            JComboBox tmp = (JComboBox) cmps[i];
                            try {
                                int pr = Integer.parseInt((String) has.get(rs.getString(i + 1)));
                                tmp.setSelectedIndex(pr);
                            } catch (SQLException | NumberFormatException e) {
                                tmp.setSelectedItem(rs.getString(i + 1));
                            }

                        } else if (cmps[i] instanceof JRadioButton) {
                            JRadioButton tmp = (JRadioButton) cmps[i];

                            if (rs.getObject(i + 1).equals(true)) {
                                tmp.setText("Activo");
                                tmp.setSelected(true);
                                tmp.setBackground(new java.awt.Color(102, 204, 0));
                            } else {
                                tmp.setText("Inactivo");
                                tmp.setSelected(false);
                                tmp.setBackground(Color.red);
                            }

                        }
                    }
                }

            } //} 
            else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Mensage", JOptionPane.INFORMATION_MESSAGE);
            }
            rs.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Esta funcion se paso al los  formularios 
//    public void getRegistroCombo(Component cmps, String tabla, String[] campos, String[] campocondicion, String[] condicionid) {
//        try {
//            rs = this.getRegistrosCombo(tabla, campos, campocondicion, condicionid);
//            int cantcampos = campos.length;
//            if (rs != null) {
//
//                DefaultComboBoxModel modeloComboBox;
//                modeloComboBox = new DefaultComboBoxModel();
//
//                JComboBox tmp;
//                if (cmps instanceof JComboBox) {
//                    tmp = (JComboBox) cmps;
//                    tmp.setModel(modeloComboBox);
//                }
//                //modeloComboBox.addElement(new mProfesor("","0"));
//                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
//                    //int count = 0;
//                    rs.beforeFirst();//regresa el puntero al primer registro
//                    Object[] fila = new Object[cantcampos];
//                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
//                        System.out.print(rs.getString(1)+" "+rs.getInt(2)+"\n");
//                        modeloComboBox.insertElementAt(rs.getString(1), rs.getInt(2));
//                        
//                        //modeloComboBox.addElement(new mProfesor(rs.getString(1), "" + rs.getInt(2)));
//                    }
//                     }
//            } else {
//                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Error", JOptionPane.INFORMATION_MESSAGE);
//                 }
//            rs.close();
//            // return modelo;
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
//            //msg.Error(Datos + ": " + ex, TituloDatos);
//            // return null;
//        }
//    }
}
