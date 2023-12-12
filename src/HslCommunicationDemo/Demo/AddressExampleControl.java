package HslCommunicationDemo.Demo;

import HslCommunication.Utilities;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class AddressExampleControl extends JPanel {
    private JTable table;
    private String[] columns = new String[]{ "Address", "Description", "Bit", "Word", "Mark" };
    public AddressExampleControl( DeviceAddressExample[] addressExamples ){
        setLayout(null);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(3, 3, 500, 100);
        String[][] tableData = new String[addressExamples.length][columns.length];
        for (int row = 0; row < addressExamples.length; row ++){
            tableData[row][0] = addressExamples[row].AddressExample;
            if (!addressExamples[row].IsHeader){
                if (Utilities.IsStringNullOrEmpty(addressExamples[row].Unit)){
                    tableData[row][1] = addressExamples[row].AddressType;
                }
                else {
                    tableData[row][1] = addressExamples[row].AddressType + "(" + addressExamples[row].Unit + ")";
                }
                tableData[row][2] = addressExamples[row].BitEnable ? "√" : " ";
                tableData[row][3] = addressExamples[row].WordEnable ? "√" : " ";
                tableData[row][4] = addressExamples[row].Mark;
            }
        }

        table = new JTable( tableData, columns);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(20);
        scrollPane.setViewportView(table);
        add(scrollPane);

        // 设置列宽
        table.getColumnModel().getColumn(0).setPreferredWidth( 180 );
        table.getColumnModel().getColumn(1).setPreferredWidth( 200 );
        table.getColumnModel().getColumn(2).setPreferredWidth( 40 );
        table.getColumnModel().getColumn(3).setPreferredWidth( 40 );
        table.getColumnModel().getColumn(4).setPreferredWidth( getWidth() - 460- 25 );

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                scrollPane.setBounds(3, 3, getWidth() - 5, getHeight() - 5);
                table.getColumnModel().getColumn(4).setPreferredWidth( getWidth() - 460- 23 );
            }
        });
    }

}
