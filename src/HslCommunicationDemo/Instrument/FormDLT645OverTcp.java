package HslCommunicationDemo.Instrument;

import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Instrument.DLT.DLT645OverTcp;
import HslCommunication.ModBus.ModbusTcpNet;
import HslCommunication.Profinet.Omron.OmronFinsNet;
import HslCommunication.Profinet.Omron.OmronPlcType;
import HslCommunication.Utilities;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.PLC.Omron.DemoOmronHelper;
import HslCommunicationDemo.PLC.Omron.OmronFinsControl;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormDLT645OverTcp extends JPanel {
    public FormDLT645OverTcp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("DLT 645 OverTcp", tabbedPane, this));
        AddConnectSegment(this);
        dlt = new DLT645OverTcp();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        dlt645Control = new Dlt645Control();
        userControlReadWriteDevice.AddSpecialFunctionTab( dlt645Control, false,"DLT Function");

        addressExampleControl = new AddressExampleControl(DemoDltHelper.GetDlt645Address());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private DLT645OverTcp dlt = null;
    private String defaultAddress = "02-01-01-00";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private Dlt645Control dlt645Control = null;
    private TcpConnectControl tcpConnectControl = null;

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "2000");

        JLabel label3 = new JLabel("Password:");
        label3.setBounds(5, TcpConnectControl.LocationTwoLine,80, 17);
        tcpConnectControl.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(90,TcpConnectControl.LocationTwoLine - 3,150, 23);
        textField3.setText("");
        tcpConnectControl.add(textField3);

        JLabel label4 = new JLabel("Op Code:");
        label4.setBounds(250, TcpConnectControl.LocationTwoLine,60, 17);
        tcpConnectControl.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(320,TcpConnectControl.LocationTwoLine - 3,150, 23);
        textField4.setText("");
        tcpConnectControl.add(textField4);

        JLabel label5 = new JLabel("Station:");
        label5.setBounds(480, TcpConnectControl.LocationTwoLine,150, 17);
        tcpConnectControl.add(label5);

        JTextField textField5 = new JTextField();
        textField5.setBounds(530,TcpConnectControl.LocationTwoLine - 3,120, 23);
        textField5.setText("1");
        tcpConnectControl.add(textField5);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(dlt);
                    dlt.setPassword(textField3.getText());
                    dlt.setOpCode(textField4.getText());
                    dlt.setStation(textField5.getText());

                    OperateResult connect = dlt.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(dlt, defaultAddress, 10);
                        dlt645Control.SetReadWritePlc(dlt);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( dlt, "dlt", tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "dlt.setPassword( \"" + textField3.getText() + "\" );\r\n" );
                    stringBuilder.append( "dlt.setOpCode( \"" + textField4.getText() + "\" );\r\n" );
                    stringBuilder.append( "dlt.setStation( \"" + textField5.getText() + "\" );\r\n" );
                    userControlReadWriteDevice.SetDeviceCode( stringBuilder.toString(), "dlt" );
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
                if(dlt!=null){
                    dlt.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }
}
