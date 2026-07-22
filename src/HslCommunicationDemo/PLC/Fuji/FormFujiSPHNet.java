package HslCommunicationDemo.PLC.Fuji;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Fuji.FujiSPBOverTcp;
import HslCommunication.Profinet.Fuji.FujiSPHNet;
import HslCommunication.Profinet.Melsec.MelsecA1ENet;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormFujiSPHNet extends HslJPanel {

    public FormFujiSPHNet(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("SPH", tabbedPane, this));
        AddConnectSegment(this);
        plc = new FujiSPHNet();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(FujiHelper.GetSPHAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private FujiSPHNet plc = null;
    private String defaultAddress = "M1.100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private TcpConnectControl tcpConnectControl = null;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            plc.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "507");

        JLabel label3 = new JLabel("ConnectId：");
        label3.setBounds(5, TcpConnectControl.LocationTwoLine,80, 17);
        tcpConnectControl.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(90,TcpConnectControl.LocationTwoLine - 3,50, 23);
        textField3.setText("254");
        tcpConnectControl.add(textField3);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(plc);
                    plc.setConnectionID((byte) Integer.parseInt(textField3.getText()));

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
                    stringBuilder.append( "plc.setConnectionID((byte) Integer.parseInt(\"" + textField3.getText() + "\"));\r\n" );
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
