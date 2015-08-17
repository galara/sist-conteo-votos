/*http://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 *http://chuwiki.chuidiang.org/index.php?title=DecimalFormat
 */
package Capa_Negocio;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 *
 * @author GLARA
 */

/*
 * DecimalFormat es una subclase concreta de NumberFormat que da formato a los números decimales
 * ( #####0.00 ).  Nos permite mostrar los números en pantalla con el formato que queramos, es decir,
 * con cuántos decimales, si queremos punto o coma para los decimales.
 *
 * Nota: Al componente JFormattedTextField se debe agregar en propiedades-->formatterFactory--> <User Code>
 */
public class FormatoDecimal extends DecimalFormat {

    private boolean invertirSimbolos = false;
    private final DecimalFormatSymbols dfs;

    public FormatoDecimal(String patron, boolean invertirSimbolos) {
        this.invertirSimbolos = invertirSimbolos;
        if (this.invertirSimbolos) {
            dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            dfs.setGroupingSeparator(',');
            this.setDecimalFormatSymbols(dfs);
        } else {
            dfs = new DecimalFormatSymbols(Locale.getDefault());
            this.setDecimalFormatSymbols(dfs);
        }
        this.applyPattern(patron);
    }
}
