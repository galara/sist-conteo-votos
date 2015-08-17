package Capa_Negocio;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author GLARA
 */
public class FormatoFecha {

    private static SimpleDateFormat formato;
    public static final int A_M_D = 1;
    public static final int D_M_A = 2;
    private static Date fechaString;

    /**
     * Este metodo recibe un Date y le coloca un formato de año-mes-dia o
     * dia-mes-año devuelve un String con el formato indicado.
     *
     * @param fecha
     * @param tipoFormato
     * @return
     */
    public static String getFormato(Date fecha, int tipoFormato) {
        formato = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (tipoFormato == D_M_A) {
                formato = new SimpleDateFormat("dd-MM-yyyy");
            }
            if (tipoFormato == A_M_D) {
                formato = new SimpleDateFormat("yyy/MM/dd");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
            //msg.Error(ErrorDatos + ": " + ex, TituloErrDatos);
        }
        return formato.format((Date) fecha);
    }

    /**
     * Este metodo recibe un String y lo comvierte a Date le coloca un formato
     * de año-mes-dia devuelve un Date con el formato indicado.
     *
     * @param text
     * @return
     */
    public static Date StringToDate(String text) {
        //formato = new SimpleDateFormat("yyyy-MM-dd");
        formato = new SimpleDateFormat("dd-MM-yyyy");
        try {
            fechaString = (Date) formato.parseObject(text);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
            //msg.Error(ErrorDatos + ": " + ex, TituloErrDatos);
        }
        return fechaString;
    }
    
    
    /**
     * Este metodo recibe un Time  y le coloca un formato de 24 hrs o
     * devuelve un String con el formato indicado para guardarlo en la BD.
     *
     * @param time
     * @return
     */
    public static String getTime(Object time) {
      //modificar
        try {
                formato = new SimpleDateFormat("HH:mm:ss");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return formato.format(time);
    }
    
    public static String getTimedoce(Object time) {
      //modificar
        try {
                formato = new SimpleDateFormat("hh:mm a");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return formato.format(time);
    }
}
