package HslCommunicationDemo.Demo;

import javax.swing.*;
import java.awt.*;

public class ComboBoxTypeEditor extends DefaultCellEditor {
    public ComboBoxTypeEditor() {
        super(new JComboBox<>(ComboBoxTypeRenderer.comboBoxOptions));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComboBox<String> comboBox = (JComboBox<String>) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        comboBox.setSelectedItem(value);
        return comboBox;
    }
}
