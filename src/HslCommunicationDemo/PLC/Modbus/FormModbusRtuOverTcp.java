package HslCommunicationDemo.PLC.Modbus;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.FunctionOperateExTwo;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.ModBus.ModbusRtuOverTcp;
import HslCommunication.ModBus.ModbusTcpNet;
import HslCommunication.Profinet.Melsec.MelsecA1ENet;
import HslCommunication.Utilities;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.PLC.Modbus.ModbusSpecialControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormModbusRtuOverTcp extends HslJPanel {
    public FormModbusRtuOverTcp(JTabbedPane tabbedPane){
        modbusTcpNet = new ModbusRtuOverTcp();
        defaultAddress = getDefaultAddress();
        setLayout(null);
        add( new UserControlReadWriteHead(getWindowHead(), tabbedPane, this));
        AddConnectSegment(this);

        modbusSpecialControl = new ModbusSpecialControl();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(modbusSpecialControl, false, "0x17Funtion");
        userControlReadWriteDevice.setEnabled(false);
        modbusSpecialControl.setEnabled(false);

        addressExampleControl = new AddressExampleControl(getAddressExample());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private ModbusRtuOverTcp modbusTcpNet = null;
    private String defaultAddress = "100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private ModbusSpecialControl modbusSpecialControl;
    protected FunctionOperateExTwo<String, Byte,OperateResultExOne<String>> addressMapping = null;
    protected String addressMappingCode = "";
    private JButton button_connect = null;
    private JButton button_disconnect = null;

    public String getWindowHead(){
        return "Modbus RTU Over Tcp";
    }
    public String getDefaultAddress(){
        return "100";
    }

    public DeviceAddressExample[] getAddressExample(){
        return DemoModbusHelper.GetModbusAddressExamples();
    }

    @Override
    public void OnClose() {
        super.OnClose();
        if (button_connect == null || button_disconnect == null) return;
        if (button_disconnect.isEnabled())
        {
            modbusTcpNet.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JTextField textField1 = DemoUtils.CreateIpAddressTextBox(panelConnect, 7);
        JTextField textField2 = DemoUtils.CreateIpPortTextBox(panelConnect, "502", 7);

        JLabel label3 = new JLabel("Station：");
        label3.setBounds(390, 7,56, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(440,4,40, 23);
        textField3.setText("1");
        panelConnect.add(textField3);


        JLabel label10 = new JLabel("BroadcastStation:");
        label10.setBounds(10, 33,130, 17);
        panelConnect.add(label10);

        JTextField textField_broadcast = new JTextField();
        textField_broadcast.setBounds(130,30,40, 23);
        textField_broadcast.setText("");
        panelConnect.add(textField_broadcast);



        JCheckBox checkBox1 = new JCheckBox("Start from 0?");
        checkBox1.setBounds(490,4,106, 21);
        checkBox1.setSelected(true);
        panelConnect.add(checkBox1);

        JCheckBox checkBox_string_reverse = new JCheckBox("string reverse?");
        checkBox_string_reverse.setBounds(490,28,150, 21);
        panelConnect.add(checkBox_string_reverse);


        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(650,13,80, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedItem(DataFormat.CDAB);
        panelConnect.add(comboBox1);

        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(850,11,121, 28);
        button_disconnect = button2;
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(752,11,91, 28);
        button_connect = button1;
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
                    modbusTcpNet.setStation( (byte) Integer.parseInt( textField3.getText() ) );
                    modbusTcpNet.setAddressStartWithZero(checkBox1.isSelected());
                    modbusTcpNet.setDataFormat((DataFormat) comboBox1.getSelectedItem());
                    modbusTcpNet.setStringReverse(checkBox_string_reverse.isSelected());
                    if (!Utilities.IsStringNullOrEmpty(textField_broadcast.getText()))
                        modbusTcpNet.setBroadcastStation(Integer.parseInt(textField_broadcast.getText()));
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


                    StringBuilder stringBuilder = DemoUtils.CreateDeviceCode( ModbusRtuOverTcp.class.getName(), "modbus", textField1.getText(), textField2.getText() );
                    stringBuilder.append( "modbus.setStation( (byte) Integer.parseInt( \"" + textField3.getText() + "\" ) );\r\n" );
                    stringBuilder.append( "modbus.setAddressStartWithZero(" + checkBox1.isSelected() + ");\r\n" );
                    stringBuilder.append( "modbus.setDataFormat(DataFormat." + (DataFormat) comboBox1.getSelectedItem() + ");\r\n" );
                    stringBuilder.append( "modbus.setStringReverse(" + checkBox_string_reverse.isSelected() + ");\r\n" );
                    if (!Utilities.IsStringNullOrEmpty(textField_broadcast.getText())){
                        stringBuilder.append( "modbus.setBroadcastStation(Integer.parseInt(\"" + textField_broadcast.getText() + "\"));\r\n" );
                    }
                    if (addressMapping!=null) {
                        stringBuilder.append( "FunctionOperateExTwo<String, Byte,OperateResultExOne<String>> addressMapping = " + addressMappingCode + "\r\n" );
                        stringBuilder.append( "modbus.RegisteredAddressMapping(addressMapping);\r\n" );
                    }
                    userControlReadWriteDevice.SetDeviceCode( stringBuilder.toString(), "modbus" );
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
