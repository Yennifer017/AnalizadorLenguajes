
package lenguajes.proyectolenguajesydl.util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author yenni
 */
public class Render extends DefaultTableCellRenderer{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
                                                   boolean isSelected, 
                                                   boolean hasFocus, 
                                                   int row, 
                                                   int column) {
        int numero = (Integer) table.getValueAt(row, 0);
 
        if (numero == 1) {
            setBackground(Color.WHITE);
            setForeground(new Color(52, 70, 198));
        } else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
        }
 
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
