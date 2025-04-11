package HslCommunicationDemo.PLC.Siemens;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.HslExtension;
import HslCommunication.Core.Types.List;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunication.Utilities;
import HslCommunicationDemo.Demo.ComboBoxTypeEditor;
import HslCommunicationDemo.Demo.ComboBoxTypeRenderer;
import HslCommunicationDemo.DemoUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Vector;

public class SiemensS7WriteControl extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private SiemensS7Net siemens;
    public SiemensS7WriteControl(){
        setLayout(null);
        // 定义表头
        String[] columnNames = {"Address", "Type", "Value"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        table = new JTable(model);
        table.getColumnModel().getColumn(1).setCellRenderer(new ComboBoxTypeRenderer());
        table.getColumnModel().getColumn(1).setCellEditor(new ComboBoxTypeEditor());

        table.getModel().addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (row == model.getRowCount() - 1) {
                    if (table.getValueAt(row, column)!= null ){
                        String string = table.getValueAt(row, column).toString();
                        if (!Utilities.IsStringNullOrEmpty(string)){
                            Vector<Object> newRow = new Vector<>();
                            for (int i = 0; i < model.getColumnCount(); i++) {
                                newRow.add(null);
                            }
                            model.addRow(newRow);
                        }
                    }
                }
            }
        });
         table.setRowHeight(20);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds( 10, 5, 500, 200 );
        add(scrollPane);

        TableClear();

        JLabel labelCode = new JLabel("Code:");
        labelCode.setBounds( 10, 300, 50, 18 );
        add(labelCode);

        JTextField textFieldCode = new JTextField();
        textFieldCode.setBounds( 65, 300, 150, 23 );
        add(textFieldCode);

        JButton button1 = new JButton("Write All");
        button1.setFocusPainted(false);
        button1.setBounds(520,5,120, 28);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false) return;
                super.mouseClicked(e);

                List<String> adds = new List<String>( );
                List<byte[]> buffer = new List<byte[]>( );

                try {
                    for (int i = 0; i < table.getRowCount(); i++) {
                        if (table.getValueAt(i, 0) == null) continue;
                        if (table.getValueAt(i, 1) == null) continue;
                        if (table.getValueAt(i, 2) == null) continue;

                        adds.Add(table.getValueAt(i, 0).toString());
                        if (table.getValueAt(i, 1) != null && table.getValueAt(i, 2) != null) {
                            String dataType = table.getValueAt(i, 1).toString();
                            if (dataType == "byte") {
                                buffer.Add(HslExtension.StringToByteArray(table.getValueAt(i, 2).toString()));
                            } else if (dataType == "short") {
                                buffer.Add(siemens.getByteTransform().TransByte(HslExtension.StringToShortArray(table.getValueAt(i, 2).toString())));
                            } else if (dataType == "ushort") {
                                buffer.Add(siemens.getByteTransform().TransByte(HslExtension.StringToUShortArray(table.getValueAt(i, 2).toString())));
                            } else if (dataType == "int") {
                                buffer.Add(siemens.getByteTransform().TransByte(HslExtension.StringToIntArray(table.getValueAt(i, 2).toString())));
                            } else if (dataType == "uint") {
                                buffer.Add(siemens.getByteTransform().TransByte(HslExtension.StringToUIntArray(table.getValueAt(i, 2).toString())));
                            } else if (dataType == "long") {
                                buffer.Add(siemens.getByteTransform().TransByte(HslExtension.StringToLongArray(table.getValueAt(i, 2).toString()) ) );
                            } else if (dataType == "float") {
                                buffer.Add(siemens.getByteTransform().TransByte(HslExtension.StringToFloatArray(table.getValueAt(i, 2).toString()) ) );
                            } else if (dataType == "double") {
                                buffer.Add(siemens.getByteTransform().TransByte(HslExtension.StringToDoubleArray(table.getValueAt(i, 2).toString()) ) );
                            } else if (dataType == "hex") {
                                buffer.Add(SoftBasic.HexStringToBytes(table.getValueAt(i, 2).toString()));
                            }
                        } else {
                            buffer.Add(new byte[0]);
                        }
                    }

                    OperateResult write = siemens.Write(HslExtension.GetStringArray(adds), buffer);
                    if (write.IsSuccess) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Write Success",
                                "Result",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Write Failed:" + write.ToMessageShowString(),
                                "Result",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    textFieldCode.setText( "OperateResult write = siemens.Write(String[] address, List<byte[]> data);" );
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(button1);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // 设置列宽
        table.getColumnModel().getColumn(0).setPreferredWidth( 200 );
        table.getColumnModel().getColumn(1).setPreferredWidth( 150 );
        table.getColumnModel().getColumn(2).setPreferredWidth( 200 );

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                scrollPane.setBounds( 3, 3, getWidth() - 108, getHeight() - 31 );
                button1.setBounds(getWidth() - 103,3,95, 28);
                labelCode.setBounds( 3, getHeight() - 22, 50, 18 );
                textFieldCode.setBounds( 65, getHeight() - 25, getWidth() - 105 - 65, 23 );
                table.getColumnModel().getColumn(2).setPreferredWidth( getWidth() - 350 - 105 - 23 );
            }
        });
    }

    public void SetSiemensS7Net(SiemensS7Net s7Net){
        this.siemens = s7Net;
    }

    public void TableClear(){
        Vector<Object> newRow = new Vector<>();
        for (int i = 0; i < model.getColumnCount(); i++) {
            newRow.add(null);
        }
        model.addRow(newRow);
    }


}
