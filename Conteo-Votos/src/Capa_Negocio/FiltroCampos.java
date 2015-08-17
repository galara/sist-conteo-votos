package Capa_Negocio;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author GLARA
 */
public class FiltroCampos extends DocumentFilter {

    public static final char SOLO_NUMEROS = 'N';
    public static final char SOLO_LETRAS = 'L';
    public static final char NUM_LETRAS = 'M';
    public static final char DEFAULT = '*';

    private char tipoEntrada;
    private int longitudCadena = 0;
    private boolean permitirEspacioEnBlanco = false;
    private int longitudActual = 0;

    public FiltroCampos() {
        tipoEntrada = DEFAULT;
        permitirEspacioEnBlanco = true;
    }

    public FiltroCampos(char tipoEntrada, boolean espacioEnBlanco) {
        this.tipoEntrada = tipoEntrada;
        permitirEspacioEnBlanco = espacioEnBlanco;
    }

    public FiltroCampos(char tipoEntrada, int longitudCadena) {
        this.tipoEntrada = tipoEntrada;
        this.longitudCadena = longitudCadena;
        permitirEspacioEnBlanco = false;
    }
    
    /**
     * 
     * <ul>
     * <li>SOLO_NUMEROS: el campo solo permitira numeros.</li>
     * <li>SOLO_LETRAS: el campo permitira solo letras</li>
     * <li>NUM_LETRAS: una mesclas de SOLO_NUMEROS y SOLO_LETRAS</li>
     * <li>DEFAULT: permitira casi cualquier caracter</li>
     * </ul>
     * @param lgCadena longitud maxima de la cadena. pasar cero(0) si la cedena es iliminada
     * @param espcBlco <code>true</code> para que se permita espacios en blanco.
     */
    public FiltroCampos(char tipoEntrada, int longitudCadena, boolean espacioEnBlanco) {
        this.tipoEntrada = tipoEntrada;
        this.longitudCadena = longitudCadena;
        permitirEspacioEnBlanco = espacioEnBlanco;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {

        if (string == null) {
            return;
        }
        if (string.isEmpty()) {
            return;
        } else {
            Document dc = fb.getDocument();
            longitudActual = dc.getLength();
            if (esValido(string)) {
                if (this.longitudCadena == 0 || longitudActual < longitudCadena) {
                    fb.insertString(offset, string, attr);
                }
            }
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Document dc = fb.getDocument();
        if (text == null) {
            fb.replace(0, length, "", attrs);
            return;
        }

        if (text.isEmpty()) {
            fb.replace(0, length, "", attrs);
            return;
        }

        longitudActual = dc.getLength();
        if (esValido(text)) {
            if (this.longitudCadena == 0 || longitudActual < longitudCadena) {
                fb.replace(offset, length, text, attrs);
            }
        }

    }

    private boolean esValido(String valor) {
        char[] letras = valor.toCharArray();
        boolean valido = false;
        for (int i = 0; i < letras.length; i++) {

            switch (tipoEntrada) {
                case SOLO_NUMEROS:
                    if (Character.isDigit(letras[i])) {
                        valido = true;
                    }
                    break;
                case SOLO_LETRAS:
                    if (Character.isLetter(letras[i]) || !letrasNoPermitidas(letras[i])) {
                        valido = true;
                    }
                    break;
                case NUM_LETRAS:
                    if (Character.isLetterOrDigit(letras[i]) || !letrasNoPermitidas(letras[i])) {
                        valido = true;
                    }
                    break;
                case DEFAULT:
                    valido = true;
                    break;
                default:
                    valido = false;
            }

            if (this.permitirEspacioEnBlanco) {
                if (Character.isSpaceChar(letras[i]) && longitudActual > 1) {
                    valido = true;
                }
            }

            if (valido) {
                break;
            }
        }

        return valido;
    }

    private boolean letrasNoPermitidas(char cr) {
        boolean encontrado = false;
        char[] noPermitidas = {'\\', '!', '|', '"', '@', '·', '$', '%', '&', '¬', '/',
            '=', '?', '¿', '¡', '^', '[', ']', '+', '*',
            '¨', '{', 'ç', '}', ',', ';', ':','\''};
        for (int i = 0; i < noPermitidas.length; i++) {
            if (cr == noPermitidas[i]) {
                encontrado = true;
                break;
            }
        }

        return encontrado;
    }
}
