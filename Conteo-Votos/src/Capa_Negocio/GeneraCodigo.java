/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_Negocio;

/**
 *
 * @author GLARA
 */
public class GeneraCodigo {

    public static String actualizarRegistro(String cadena) {

        String cad = adjuntarSimbolo(cadena, " ");
        //return CODIGO;
        return cad;
    }

    /**
     *
     * @param cad cadena
     * @param separador simbolo separador, utilizado para partir la cadena
     * @param simbolo simbolo que asigna a cada item que se genere de la cadena
     * @return cadena: (CADENA)
     */
    public static String adjuntarSimbolo(String cad, String separador) {
        String[] campos = stringToArray(cad, separador);
        String ncad = "";
        for (int i = 0; i < campos.length; i++) {
            if (campos[i].isEmpty()) {
            } else {
                ncad += campos[i].substring(0, 1).toUpperCase();
            }
        }
        return ncad;
    }

    /**
     *
     * @param cad cadena
     * @param separador simbolo para dividir la cadena
     * @return devuelve array de cadenas
     */
    public static String[] stringToArray(String cad, String separador) {
        return cad.split(separador);
    }
}
