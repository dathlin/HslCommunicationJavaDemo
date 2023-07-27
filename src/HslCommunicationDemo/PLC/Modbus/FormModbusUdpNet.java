package HslCommunicationDemo.PLC.Modbus;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.ModBus.ModbusTcpNet;
import HslCommunication.ModBus.ModbusUdpNet;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.PLC.Modbus.ModbusSpecialControl;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;
import HslCommunicationDemo.UserControlReadWriteOp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormModbusUdpNet extends JPanel {
    public FormModbusUdpNet(JTabbedPane tabbedPane){
        modbusTcpNet = new ModbusUdpNet();
        setLayout(null);
        add( new UserControlReadWriteHead("Modbus Tcp Over Udp", tabbedPane, this));
        AddConnectSegment(this);

        modbusSpecialControl = new ModbusSpecialControl();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(modbusSpecialControl, false, "0x17Funtion");
        userControlReadWriteDevice.setEnabled(false);
        modbusSpecialControl.setEnabled(false);
    }

    private ModbusUdpNet modbusTcpNet = null;
    private String defaultAddress = "100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private ModbusSpecialControl modbusSpecialControl;


    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 17,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,14,106, 23);
        textField1.setText("192.168.0.10");
        panelConnect.add(textField1);

        JLabel label2 = new JLabel("Port：");
        label2.setBounds(184, 17,56, 17);
        panelConnect.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(238,14,61, 23);
        textField2.setText("502");
        panelConnect.add(textField2);

        JLabel label3 = new JLabel("Station：");
        label3.setBounds(338, 17,56, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(392,14,40, 23);
        textField3.setText("1");
        panelConnect.add(textField3);


        JCheckBox checkBox1 = new JCheckBox("Start from 0?");
        checkBox1.setBounds(447,16,106, 21);
        checkBox1.setSelected(true);
        panelConnect.add(checkBox1);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(558,13,111, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedItem(DataFormat.CDAB);
        panelConnect.add(comboBox1);

        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(850,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(752,11,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    modbusTcpNet.setIpAddress(textField1.getText());
                    modbusTcpNet.setPort(Integer.parseInt(textField2.getText()));
                    modbusTcpNet.setAddressStartWithZero(checkBox1.isSelected());
                    modbusTcpNet.setDataFormat((DataFormat) comboBox1.getSelectedItem());

                    JOptionPane.showMessageDialog(
                            null,
                            "Connect Success",
                            "Result",
                            JOptionPane.PLAIN_MESSAGE);
                    button2.setEnabled(true);
                    button1.setEnabled(false);
                    userControlReadWriteDevice.SetReadWriteNet(modbusTcpNet, defaultAddress, 10);
                    modbusSpecialControl.setEnabled(true);
                    modbusSpecialControl.SetReadWriteModbus(modbusTcpNet, "100");
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Connect Failed\r\nReason:"+ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!button2.isEnabled()) return;
                if(modbusTcpNet!=null){
                    //modbusTcpNet.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }
}
