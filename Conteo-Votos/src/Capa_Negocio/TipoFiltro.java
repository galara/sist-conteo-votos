package Capa_Negocio;

import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author GLARA
 */
public class TipoFiltro {

     /**
     * @param dc Docuement del compoenente
     * @param tipoValidacion tipo de filtro:<br/>
     * <ul>
     * <li>SOLO_NUMEROS: el campo solo permitira numeros.</li>
     * <li>SOLO_LETRAS: el campo permitira solo letras</li>
     * <li>NUM_LETRAS: una mesclas de SOLO_NUMEROS y SOLO_LETRAS</li>
     * <li>DEFAULT: permitira casi cualquier caracter</li>
     * </ul>
     * @param lgCadena longitud maxima de la cadena. pasar cero(0) si la cedena es iliminada
     * @param espcBlco <code>true</code> para que se permita espacios en blanco.
     */
    public static void setFiltraEntrada(Document dc, char tipoValidacion, int lgCadena, boolean espcBlco) {
        FiltroCampos fe = new FiltroCampos(tipoValidacion, lgCadena, espcBlco);
        ((AbstractDocument) dc).setDocumentFilter(fe);
    }

    public static void removerFiltraEntrada(Document dc) {
        ((AbstractDocument) dc).setDocumentFilter(new DocumentFilter());
    }

}
