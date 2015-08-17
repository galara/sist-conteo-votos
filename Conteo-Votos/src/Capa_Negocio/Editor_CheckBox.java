package Capa_Negocio;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author GLARA
 */

public class Editor_CheckBox extends DefaultCellEditor implements TableCellRenderer  {
    
    private final JComponent component = new JCheckBox();    
    private boolean value = false; // valor de la celda
   
    /** Constructor de clase */
    public Editor_CheckBox() {
        super( new JCheckBox() );
    }

    /** retorna valor de celda
     * @return  */
    @Override
    public Object getCellEditorValue() {
        return ((JCheckBox)component).isSelected();        
    }

    /** Segun el valor de la celda selecciona/deseleciona el JCheckBox
     * @param table
     * @param value
     * @param isSelected
     * @param row
     * @param column
     * @return  */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Color de fondo en modo edicion
        //( (JCheckBox) component).setBackground( new Color(234,176,40) );
        //obtiene valor de celda y coloca en el JCheckBox
        boolean b = ((boolean) value );
        ( (JCheckBox) component).setSelected( b );
        return ( (JCheckBox) component);     
    }

    /** cuando termina la manipulacion de la celda
     * @return  */
    @Override
    public boolean stopCellEditing() {        
        value = ((boolean)getCellEditorValue()) ;
        ((JCheckBox)component).setSelected( value );
        return super.stopCellEditing();
    }

    /** retorna componente
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return  */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         if (value == null)
            return null;         
         return ( (JCheckBox) component );
    }

}
