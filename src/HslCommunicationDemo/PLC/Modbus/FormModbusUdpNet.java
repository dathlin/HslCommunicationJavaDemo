package HslCommunicationDemo.PLC.Modbus;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.ModBus.ModbusTcpNet;
import HslCommunication.ModBus.ModbusUdpNet;
import HslCommunication.Profinet.Melsec.MelsecA1ENet;
import HslCommunication.Utilities;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;
import HslCommunicationDemo.PLC.Modbus.ModbusSpecialControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormModbusUdpNet extends HslJPanel {
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

        addressExampleControl = new AddressExampleControl(DemoModbusHelper.GetModbusAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private ModbusUdpNet modbusTcpNet = null;
    private String defaultAddress = "100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private ModbusSpecialControl modbusSpecialControl;
    private TcpConnectControl tcpConnectControl = null;
    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            modbusTcpNet.ConnectClose();
        }
    }


    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "502");

        JLabel label3 = new JLabel("Station：");
        label3.setBounds(5, TcpConnectControl.LocationTwoLine,56, 17);
        tcpConnectControl.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(60,TcpConnectControl.LocationTwoLine - 3,40, 23);
        textField3.setText("1");
        tcpConnectControl.add(textField3);

        JLabel label10 = new JLabel("BroadcastStation:");
        label10.setBounds(110, TcpConnectControl.LocationTwoLine,130, 17);
        tcpConnectControl.add(label10);

        JTextField textField_broadcast = new JTextField();
        textField_broadcast.setBounds(245,TcpConnectControl.LocationTwoLine - 3,40, 23);
        textField_broadcast.setText("");
        tcpConnectControl.add(textField_broadcast);


        JCheckBox checkBox1 = new JCheckBox("Start from 0?");
        checkBox1.setBounds(295,TcpConnectControl.LocationTwoLine - 1,106, 21);
        checkBox1.setSelected(true);
        tcpConnectControl.add(checkBox1);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(410,TcpConnectControl.LocationTwoLine - 4,111, 25);
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
                    if (!Utilities.IsStringNullOrEmpty(textField_broadcast.getText()))
                        modbusTcpNet.setBroadcastStation(Integer.parseInt(textField_broadcast.getText()));
                    JOptionPane.showMessageDialog(
                            null,
                            "Connect Success",
                            "Result",
                            JOptionPane.PLAIN_MESSAGE);
                    tcpConnectControl.SetConnectSuccess();
                    userControlReadWriteDevice.SetReadWriteNet(modbusTcpNet, defaultAddress, 10);
                    modbusSpecialControl.setEnabled(true);
                    modbusSpecialControl.SetReadWriteModbus(modbusTcpNet, "100");


                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( modbusTcpNet, "modbus", tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "modbus.setStation( (byte) Integer.parseInt( \"" + textField3.getText() + "\" ) );\r\n" );
                    stringBuilder.append( "modbus.setAddressStartWithZero(" + checkBox1.isSelected() + ");\r\n" );
                    stringBuilder.append( "modbus.setDataFormat(DataFormat." + (DataFormat) comboBox1.getSelectedItem() + ");\r\n" );
                    //stringBuilder.append( "modbus.setStringReverse(" + checkBox_string_reverse.isSelected() + ");\r\n" );
                    if (!Utilities.IsStringNullOrEmpty(textField_broadcast.getText())){
                        stringBuilder.append( "modbus.setBroadcastStation(Integer.parseInt(\"" + textField_broadcast.getText() + "\"));\r\n" );
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
                    //modbusTcpNet.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }
}
