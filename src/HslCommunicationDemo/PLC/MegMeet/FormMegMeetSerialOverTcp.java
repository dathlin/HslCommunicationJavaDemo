package HslCommunicationDemo.PLC.MegMeet;

import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.ModBus.ModbusRtuOverTcp;
import HslCommunication.Profinet.Inovance.InovanceSeries;
import HslCommunication.Profinet.MegMeet.MegMeetSerialOverTcp;
import HslCommunication.Profinet.Melsec.MelsecA1ENet;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.HslJPanel;
import HslCommunicationDemo.PLC.Modbus.ModbusSpecialControl;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormMegMeetSerialOverTcp extends HslJPanel {
    public FormMegMeetSerialOverTcp(JTabbedPane tabbedPane){
        modbusTcpNet = new MegMeetSerialOverTcp();
        setLayout(null);
        add( new UserControlReadWriteHead("Modbus RTU Over Tcp", tabbedPane, this));
        AddConnectSegment(this);

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);


        addressExampleControl = new AddressExampleControl(MegMeetHelper.GetMegMeetAddress());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private MegMeetSerialOverTcp modbusTcpNet = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
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
        textField3.setBounds(65,TcpConnectControl.LocationTwoLine - 3,40, 23);
        textField3.setText("1");
        tcpConnectControl.add(textField3);


        JCheckBox checkBox1 = new JCheckBox("Start from 0?");
        checkBox1.setBounds(110,TcpConnectControl.LocationTwoLine - 1,106, 21);
        checkBox1.setSelected(true);
        tcpConnectControl.add(checkBox1);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(220,TcpConnectControl.LocationTwoLine - 4,70, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedItem(DataFormat.CDAB);
        tcpConnectControl.add(comboBox1);


        JCheckBox checkBox2 = new JCheckBox("string reverse?");
        checkBox2.setBounds(300,TcpConnectControl.LocationTwoLine - 1,116, 21);
        tcpConnectControl.add(checkBox2);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(modbusTcpNet);
                    modbusTcpNet.setStation(Byte.parseByte(textField3.getText()));
                    modbusTcpNet.setAddressStartWithZero(checkBox1.isSelected());
                    modbusTcpNet.setDataFormat((DataFormat) comboBox1.getSelectedItem());
                    modbusTcpNet.setStringReverse(checkBox2.isSelected());

                    OperateResult connect = modbusTcpNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(modbusTcpNet, defaultAddress, 10);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( modbusTcpNet, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );;
                    stringBuilder.append( "plc.setStation(Byte.parseByte(\"" + textField3.getText() + "\"));\r\n" );
                    stringBuilder.append( "plc.setAddressStartWithZero(" + checkBox1.isSelected() + ");\r\n" );
                    stringBuilder.append( "plc.setDataFormat( DataFormat." + (DataFormat) comboBox1.getSelectedItem() + ");\r\n" );
                    stringBuilder.append( "plc.setStringReverse(" + checkBox2.isSelected() + ");\r\n" );
                    userControlReadWriteDevice.SetDeviceCode( stringBuilder.toString() );
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
                }
            }
        });


        panel.add(tcpConnectControl);
    }
}
