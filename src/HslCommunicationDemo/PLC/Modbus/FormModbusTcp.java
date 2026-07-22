package HslCommunicationDemo.PLC.Modbus;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.FunctionOperateExTwo;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.ModBus.ModbusTcpNet;
import HslCommunication.Profinet.Melsec.MelsecA1ENet;
import HslCommunication.Utilities;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;
import HslCommunicationDemo.PLC.Modbus.DemoModbusHelper;
import HslCommunicationDemo.PLC.Modbus.ModbusSpecialControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormModbusTcp extends HslJPanel {


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
    protected String addressMappingCode = "";
    private TcpConnectControl  tcpConnectControl = null;

    @Override
    public void OnClose() {
        if (tcpConnectControl.NeedCloseDevice())
        {
            //button_disconnect.doClick();
            modbusTcpNet.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "502");
        JLabel label3 = new JLabel("Station：");
        label3.setBounds(450, TcpConnectControl.LocationTwoLine,56, 17);
        tcpConnectControl.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(510,TcpConnectControl.LocationTwoLine - 3,40, 23);
        textField3.setText("1");
        tcpConnectControl.add(textField3);


        JCheckBox checkBox1 = new JCheckBox("Start from 0?");
        checkBox1.setBounds(560,TcpConnectControl.LocationTwoLine - 1,106, 21);
        checkBox1.setSelected(true);
        tcpConnectControl.add(checkBox1);

        JCheckBox checkBox_checkMessage = new JCheckBox("MessageID Check?");
        checkBox_checkMessage.setBounds(5,TcpConnectControl.LocationTwoLine - 1,140, 21);
        checkBox_checkMessage.setSelected(true);
        tcpConnectControl.add(checkBox_checkMessage);

        JLabel label10 = new JLabel("BroadcastStation:");
        label10.setBounds(150, TcpConnectControl.LocationTwoLine,130, 17);
        tcpConnectControl.add(label10);

        JTextField textField_broadcast = new JTextField();
        textField_broadcast.setBounds(270,TcpConnectControl.LocationTwoLine - 3,40, 23);
        textField_broadcast.setText("");
        tcpConnectControl.add(textField_broadcast);

        JCheckBox checkBox_stationCheck = new JCheckBox( "Station Check?" );
        checkBox_stationCheck.setBounds(320,TcpConnectControl.LocationTwoLine - 3,150, 21);
        checkBox_stationCheck.setSelected(true);
        tcpConnectControl.add(checkBox_stationCheck);

        JCheckBox checkBox_string_reverse = new JCheckBox("string reverse?");
        checkBox_string_reverse.setBounds(670,TcpConnectControl.LocationTwoLine - 1,120, 21);
        tcpConnectControl.add(checkBox_string_reverse);


        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(800,TcpConnectControl.LocationTwoLine - 3,80, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedItem(DataFormat.CDAB);
        tcpConnectControl.add(comboBox1);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(modbusTcpNet);
                    modbusTcpNet.setStation( (byte) Integer.parseInt( textField3.getText() ) );
                    modbusTcpNet.setAddressStartWithZero(checkBox1.isSelected());
                    modbusTcpNet.setDataFormat((DataFormat) comboBox1.getSelectedItem());
                    modbusTcpNet.setStringReverse(checkBox_string_reverse.isSelected());
                    modbusTcpNet.IsCheckMessageId = checkBox_checkMessage.isSelected();
                    modbusTcpNet.StationCheckMatch = checkBox_stationCheck.isSelected();
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
                        tcpConnectControl.SetConnectSuccess();
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

                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( modbusTcpNet, "modbus", tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "modbus.setStation( (byte) Integer.parseInt( \"" + textField3.getText() + "\" ) );\r\n" );
                    stringBuilder.append( "modbus.setAddressStartWithZero(" + checkBox1.isSelected() + ");\r\n" );
                    stringBuilder.append( "modbus.setDataFormat(DataFormat." + (DataFormat) comboBox1.getSelectedItem() + ");\r\n" );
                    stringBuilder.append( "modbus.setStringReverse(" + checkBox_string_reverse.isSelected() + ");\r\n" );
                    stringBuilder.append( "modbus.IsCheckMessageId = " + checkBox_checkMessage.isSelected() + ";\r\n" );
                    stringBuilder.append( "modbus.StationCheckMatch = " + checkBox_stationCheck.isSelected() + ";\r\n" );
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
        tcpConnectControl.ButtonDisconnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!tcpConnectControl.ButtonDisconnect.isEnabled()) return;
                if(modbusTcpNet!=null){
                    modbusTcpNet.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                    modbusSpecialControl.setEnabled(false);
                }
            }
        });

        panel.add(tcpConnectControl);



    }
}
