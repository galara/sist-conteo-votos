package Capa_Negocio;

import Capa_Presentacion.Pagos;
import java.awt.Component;
import java.awt.Font;
import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author GLARA
 */
public class CellEditorSpinnerPago extends AbstractCellEditor implements TableCellEditor {

    private final JSpinner spinner;
    private JTable tbl;
    private Object valorInicial;
    private Object valorActual;

    private int fila;
    private int columna;

    public CellEditorSpinnerPago(int sizeDes) {
        spinner = new JSpinner();
        spinner.setFont(new Font("Tahoma", 1, 14));
        spinner.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        spinner.setModel(new SpinnerNumberModel(0.00, 0.00, 10000.00, sizeDes));

        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner temp = (JSpinner) e.getSource();
                ((DefaultEditor) temp.getEditor()).getTextField().setHorizontalAlignment(JTextField.RIGHT);
                Double vi = (Double) valorInicial;
                if (vi != null) {
                    int sd = -1;
                    valorActual = temp.getValue();
                    if (tbl != null) {
                        Float pr = (Float) tbl.getValueAt(fila, 2);
                        float subtotal=(float) (Math.round((Float.parseFloat(valorActual.toString()) * pr) * 100.0) / 100.0);
                        tbl.setValueAt(subtotal, getFila(), 4);
                    }
                }
                ((DefaultEditor) temp.getEditor()).getTextField().setHorizontalAlignment(JTextField.LEFT);
                fireEditingStopped();
            }
        };
        spinner.addChangeListener(listener);
    }

    @Override
    public Object getCellEditorValue() {
        return valorActual;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        tbl = table;
        fila = row;
        columna = column;
        valorActual = value;
        spinner.setValue(value);

        if (valorInicial == null) {
            valorInicial = (value);
        }
        return spinner;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public JSpinner getSpinner() {
        return spinner;
    }

}
