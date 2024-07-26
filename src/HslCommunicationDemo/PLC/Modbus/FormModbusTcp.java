package HslCommunicationDemo.PLC.Modbus;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.FunctionOperateExTwo;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.ModBus.ModbusTcpNet;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.PLC.Modbus.DemoModbusHelper;
import HslCommunicationDemo.PLC.Modbus.ModbusSpecialControl;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;
import HslCommunicationDemo.UserControlReadWriteOp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormModbusTcp extends JPanel {


    public FormModbusTcp(JTabbedPane tabbedPane){
        defaultAddress = getDefaultAddress();
        modbusTcpNet = new ModbusTcpNet();
        setLayout(null);
        add( new UserControlReadWriteHead( getWindowHead(), tabbedPane, this));
        AddConnectSegment(this);

        modbusSpecialControl = new ModbusSpecialControl();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(modbusSpecialControl, false, "0x17Function");
        userControlReadWriteDevice.setEnabled(false);
        modbusSpecialControl.setEnabled(false);

        addressExampleControl = new AddressExampleControl(getAddressExample());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    public String getWindowHead(){
        return "Modbus Tcp";
    }
    public String getDefaultAddress(){
        return "100";
    }

    public DeviceAddressExample[] getAddressExample(){
        return DemoModbusHelper.GetModbusAddressExamples();
    }

    private AddressExampleControl addressExampleControl;
    private ModbusTcpNet modbusTcpNet = null;
    private String defaultAddress = "100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private ModbusSpecialControl modbusSpecialControl;
    protected FunctionOperateExTwo<String, Byte,OperateResultExOne<String>> addressMapping = null;

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JTextField textField1 = DemoUtils.CreateIpAddressTextBox(panelConnect);
        JTextField textField2 = DemoUtils.CreateIpPortTextBox(panelConnect, "502");

        JLabel label3 = new JLabel("Station：");
        label3.setBounds(390, 17,56, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(440,14,40, 23);
        textField3.setText("1");
        panelConnect.add(textField3);


        JCheckBox checkBox1 = new JCheckBox("Start from 0?");
        checkBox1.setBounds(490,4,106, 21);
        checkBox1.setSelected(true);
        panelConnect.add(checkBox1);

        JCheckBox checkBox_checkMessage = new JCheckBox("MessageID Check?");
        checkBox_checkMessage.setBounds(490,28,150, 21);
        checkBox_checkMessage.setSelected(true);
        panelConnect.add(checkBox_checkMessage);

        JCheckBox checkBox_string_reverse = new JCheckBox("string reverse?");
        checkBox_string_reverse.setBounds(600,4,120, 21);
        panelConnect.add(checkBox_string_reverse);


        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(650,26,80, 25);
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
                    modbusTcpNet.setStringReverse(checkBox_string_reverse.isSelected());
                    modbusTcpNet.IsCheckMessageId = checkBox_checkMessage.isSelected();
                    if (addressMapping!=null) {
                        modbusTcpNet.RegisteredAddressMapping(addressMapping);
                    }

                    OperateResult connect = modbusTcpNet.ConnectServer();
                    if(connect.IsSuccess){
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
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }
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
                    modbusTcpNet.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                    modbusSpecialControl.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }
}
