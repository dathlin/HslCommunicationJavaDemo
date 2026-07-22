package HslCommunicationDemo.PLC.Omron;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Core.Types.OperateResultExTwo;
import HslCommunication.Profinet.Melsec.MelsecMcNet;
import HslCommunication.Profinet.Omron.OmronCipNet;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;
import HslCommunicationDemo.PLC.AllenBradley.DemoAllenBradleyHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormOmronCipNet extends HslJPanel {

    public FormOmronCipNet(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Cip", tabbedPane, this));
        AddConnectSegment(this);
        omronCipNet = new OmronCipNet();

        cipControl = new OmronCipControl();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(cipControl, false, "CIP Function");
        userControlReadWriteDevice.setEnabled(false);
        cipControl.setEnabled(false);

        addressExampleControl = new AddressExampleControl(DemoAllenBradleyHelper.GetCIPAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private OmronCipNet omronCipNet = null;
    private String defaultAddress = "A";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private OmronCipControl cipControl;
    private TcpConnectControl tcpConnectControl;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            omronCipNet.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "44818");
        JLabel label4 = new JLabel("Slot：");
        label4.setBounds(5, TcpConnectControl.LocationTwoLine,48, 17);
        tcpConnectControl.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(55,TcpConnectControl.LocationTwoLine - 3,53, 23);
        textField4.setText("0");
        tcpConnectControl.add(textField4);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tcpConnectControl.ButtonConnect.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(omronCipNet);
                    omronCipNet.setSlot(Byte.parseByte(textField4.getText()));

                    OperateResult connect = omronCipNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(omronCipNet, defaultAddress, 1);
                        cipControl.setEnabled(true);
                        cipControl.SetReadWriteCip(omronCipNet);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( omronCipNet, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "plc.setSlot(Byte.parseByte(\"" + textField4.getText() + "\"));\r\n" );
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
                if (tcpConnectControl.ButtonDisconnect.isEnabled() == false) return;
                if(omronCipNet !=null){
                    omronCipNet.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                    cipControl.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }

}
