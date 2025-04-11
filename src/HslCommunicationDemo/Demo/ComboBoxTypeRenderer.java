package HslCommunicationDemo.Demo;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ComboBoxTypeRenderer extends JComboBox<String> implements TableCellRenderer {
    public ComboBoxTypeRenderer() {
        for (String option : comboBoxOptions) {
            addItem(option);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setSelectedItem(value);
        return this;
    }

    public static String[] comboBoxOptions = {"byte", "short", "ushort", "int", "uint", "long", "float", "double", "hex"};
}
