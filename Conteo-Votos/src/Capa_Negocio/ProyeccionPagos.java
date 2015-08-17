/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_Negocio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GLARA
 */
public class ProyeccionPagos {

    /*
     * metodo para convertir String a Calendar y poderlo manipular
     * devuelve un Calendar 
     */
    public static Calendar convierteacalendar(String fecha) {
        //
        try {
            String dateStr = fecha;
            SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy");
            Date dateObj = curFormater.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateObj);
            return calendar;
        } catch (ParseException ex) {
            Logger.getLogger(ProyeccionPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String calculapagos(Calendar fechainicial, Calendar fechafinal, float colegiatura, String idg, String inscripcion) {
        String sql = pagos(fechainicial, fechafinal, colegiatura, idg, inscripcion);
        return sql;
    }

    /*
     * Metodo para calcular los pagos que el alumno debe realizar
     * Toma la fecha inicial y le se agrega 1 mes hasta que la fecha seha
     * menor a fecha  final + 1 mes
     * devuelve un string que contiene la sentencia sql para insertar la proteccion de pagos
     */
    private static String pagos(Calendar fechainicial, Calendar fechafinal, float colegiatura, String idg, String inscripcion) {
        String fechavenvimiento = "";
        String sql = "";
        String diavencimiento = "08";

        /*mientras fecha inicial seha menor a fecha final*/
        while (fechainicial.before(fechafinal)) {
            fechainicial.add(Calendar.MONTH, 1); //suma un mes a la fecha inicial
            int mespago = fechainicial.get(Calendar.MONTH);
            int añopago = fechainicial.get(Calendar.YEAR);

            if (mespago == 0) {
                mespago = 12;
                añopago = (añopago - 1);
            }

            int añovenvimiento = fechainicial.get(Calendar.YEAR);
            int mesvencimiento = fechainicial.get(Calendar.MONTH) + 1;
            if (mesvencimiento == 0) {
                mesvencimiento = 12;
                añovenvimiento = añopago + 1;
            }
            /*Fecha de vencimiento es el 08 del siguiente mes*/
            fechavenvimiento = añovenvimiento + "-" + mesvencimiento + "-" + diavencimiento;

            if (sql.equals("")) {
                sql = sql + "('" + 13 + "','" + añopago + "','" + inscripcion + "','" + fechavenvimiento + "','" + idg + "')";
            }

            if (!sql.equals("")) {
                sql = sql + ",";
            }

            sql = sql + "('" + mespago + "','" + añopago + "','" + colegiatura + "','" + fechavenvimiento + "','" + idg + "')";
        }
        /*Si mes-año de fecha inicial es igual a mes-año de fecha final
         * unicamene calcula un mes de pago ya que las dos fecha estan en el mismo mes*/
        if (((fechainicial.get(Calendar.MONTH) + 1) + "-" + fechainicial.get(Calendar.YEAR)).equals(((fechafinal.get(Calendar.MONTH) + 1) + "-" + fechafinal.get(Calendar.YEAR)))) {

            fechainicial.add(Calendar.MONTH, 1); //suma un mes a fecha inicial
            int mespago = fechainicial.get(Calendar.MONTH);
            int añopago = fechainicial.get(Calendar.YEAR);
            if (mespago == 0) {
                mespago = 12;
                añopago = (añopago - 1);
            }

            int mesvencimiento = fechainicial.get(Calendar.MONTH) + 1;
            int añovencimiento = fechainicial.get(Calendar.YEAR);
            if (mesvencimiento == 0) {
                mesvencimiento = 12;
                añovencimiento = (añopago + 1);
            }
            /*fecha de vencimiento es el 08 del siguiente mes*/
            fechavenvimiento = añovencimiento + "-" + mesvencimiento + "-" + diavencimiento;

            if (sql.equals("")) {
                sql = sql + "('" + 13 + "','" + añopago + "','" + inscripcion + "','" + fechavenvimiento + "','" + idg + "')";
            }

            if (!sql.equals("")) {
                sql = sql + ",";
            }

            sql = sql + "('" + mespago + "','" + añopago + "','" + colegiatura + "','" + fechavenvimiento + "','" + idg + "')";
        }

        return sql; /*retorna la sentencia sql para insertar los pagos*/

    }
}
