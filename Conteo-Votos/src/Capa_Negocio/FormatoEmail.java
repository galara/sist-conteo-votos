package Capa_Negocio;
/*
 *http://docs.oracle.com/javase/7/docs/api/javax/swing/InputVerifier.html
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

/**
 *
 * @author GLARA
 */
public class FormatoEmail extends InputVerifier {

    public final static int EMAIL = 1;
    private Pattern patron;
    private Matcher matcher;

    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private int longitudCadena = 0;
    private boolean esObligatorio = false;
    private int tipoValidacion;

    public FormatoEmail() {
        tipoValidacion = EMAIL;
    }

    public FormatoEmail(boolean esObligatorio) {
        tipoValidacion = EMAIL;
        this.esObligatorio = esObligatorio;
    }

    public FormatoEmail(int tipoValidacion) {
        this.tipoValidacion = tipoValidacion;
    }

    public FormatoEmail(int longitudCadena, int tipoValidacion) {
        this.longitudCadena = longitudCadena;
        this.tipoValidacion = tipoValidacion;
    }

    /*
     *  http://chuwiki.chuidiang.org/index.php?title=InputVerifier
     */
    @Override
    public boolean verify(JComponent input) {
        JTextComponent cmp = (JTextComponent) input;
        String texto = cmp.getText();
        if (!esObligatorio && texto.isEmpty()) {
            return true;
        }
        if (!texto.isEmpty()) {
            switch (tipoValidacion) {
                case EMAIL:
                    if (longitudCadena == 0 || texto.length() < longitudCadena) {
                        if (esValido(texto) && this.esObligatorio) {
                            JOptionPane.showMessageDialog(cmp.getParent(), "Este campo es Obligatorio");
                            return true;
                        } else {
                            if (!esValido(texto)) {
                                cmp.setText("");
                                JOptionPane.showMessageDialog(cmp.getParent(), "El Email ingresado no es Valido");
                            }
                            if (!esObligatorio) {
                                return true;
                            }
                        }
                    }
            }
        }
        return false;
    }

    private boolean esValido(String string) {
        switch (tipoValidacion) {
            case EMAIL:
                patron = Pattern.compile(EMAIL_PATTERN);
                matcher = patron.matcher(string);
                return matcher.matches();
            default:
                return false;
        }
    }

}
