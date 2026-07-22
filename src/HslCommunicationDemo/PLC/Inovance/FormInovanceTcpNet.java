package HslCommunicationDemo.PLC.Inovance;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Inovance.InovanceSerialOverTcp;
import HslCommunication.Profinet.Inovance.InovanceSeries;
import HslCommunication.Profinet.Inovance.InovanceTcpNet;
import HslCommunication.Profinet.Melsec.MelsecA1ENet;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormInovanceTcpNet extends HslJPanel {

    public FormInovanceTcpNet(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Modbus Tcp", tabbedPane, this));
        AddConnectSegment(this);
        plc = new InovanceTcpNet();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(InovanceHelper.GetInovanceAddress());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private InovanceTcpNet plc = null;
    private String defaultAddress = "M100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private TcpConnectControl  tcpConnectControl = null;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            plc.ConnectClose();
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
        checkBox1.setBounds(110,TcpConnectControl.LocationTwoLine -1,106, 21);
        checkBox1.setSelected(true);
        tcpConnectControl.add(checkBox1);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(220,TcpConnectControl.LocationTwoLine - 4,80, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedIndex(2);
        tcpConnectControl.add(comboBox1);

        JLabel label4 = new JLabel("Series：");
        label4.setBounds(310, TcpConnectControl.LocationTwoLine,56, 17);
        tcpConnectControl.add(label4);

        JComboBox<InovanceSeries> comboBox2 = new JComboBox<>();
        comboBox2.setBounds(380,TcpConnectControl.LocationTwoLine - 4,60, 25);
        comboBox2.addItem(InovanceSeries.AM);
        comboBox2.addItem(InovanceSeries.H3U);
        comboBox2.addItem(InovanceSeries.H5U);
        comboBox2.addItem(InovanceSeries.Easy);
        comboBox2.setSelectedItem(InovanceSeries.AM);
        tcpConnectControl.add(comboBox2);
        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(plc);
                    plc.setStation(Byte.parseByte(textField3.getText()));
                    plc.setAddressStartWithZero(checkBox1.isSelected());
                    plc.setDataFormat((DataFormat) comboBox1.getSelectedItem());
                    plc.setSeries((InovanceSeries)comboBox2.getSelectedItem());

                    OperateResult connect = plc.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(plc, defaultAddress, 10);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( plc, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "plc.setStation(Byte.parseByte(\"" + textField3.getText() + "\"));\r\n" );
                    stringBuilder.append( "plc.setAddressStartWithZero(" + checkBox1.isSelected() + ");\r\n" );
                    stringBuilder.append( "plc.setDataFormat( DataFormat." + (DataFormat) comboBox1.getSelectedItem() + ");\r\n" );
                    stringBuilder.append( "plc.setSeries(InovanceSeries." + (InovanceSeries)comboBox2.getSelectedItem() + ");\r\n" );
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
                if(plc !=null){
                    plc.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }

}
