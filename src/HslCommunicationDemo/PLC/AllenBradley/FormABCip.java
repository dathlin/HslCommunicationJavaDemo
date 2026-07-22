package HslCommunicationDemo.PLC.AllenBradley;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.AllenBradley.AllenBradleyNet;
import HslCommunication.Profinet.AllenBradley.MessageRouter;
import HslCommunication.Profinet.Melsec.MelsecMcAsciiUdp;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunication.Utilities;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;
import HslCommunicationDemo.PLC.Omron.OmronCipControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class FormABCip extends HslJPanel
{

    public FormABCip(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Cip", tabbedPane, this));
        AddConnectSegment(this);
        allenBradleyNet = new AllenBradleyNet();

        cipControl = new OmronCipControl();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(cipControl, false, "CipFunction");
        userControlReadWriteDevice.setEnabled(false);
        cipControl.setEnabled(false);

        addressExampleControl = new AddressExampleControl(DemoAllenBradleyHelper.GetCIPAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private AllenBradleyNet allenBradleyNet = null;
    private String defaultAddress = "A";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private OmronCipControl cipControl;
    private TcpConnectControl  tcpConnectControl;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice())
        {
            allenBradleyNet.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "44818");

        JLabel label4 = new JLabel("Slot：");
        label4.setBounds(5, TcpConnectControl.LocationTwoLine,48, 17);
        tcpConnectControl.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(50,TcpConnectControl.LocationTwoLine - 3,43, 23);
        textField4.setText("0");
        tcpConnectControl.add(textField4);

        JLabel label_router = new JLabel("Router:");
        label_router.setBounds(100, TcpConnectControl.LocationTwoLine,47, 17);
        tcpConnectControl.add(label_router);

        JTextField textField_router = new JTextField();
        textField_router.setBounds(150,TcpConnectControl.LocationTwoLine - 3,150, 23);
        textField_router.setText("");
        tcpConnectControl.add(textField_router);

        JLabel label_router_tip = new JLabel("if use router, example: 1.15.2.18.1.12");
        label_router_tip.setBounds(305, TcpConnectControl.LocationTwoLine,220, 17);
        tcpConnectControl.add(label_router_tip);
        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(allenBradleyNet);
                    allenBradleyNet.setSlot(Byte.parseByte(textField4.getText()));

                    if(!Utilities.IsStringNullOrEmpty(textField_router.getText())){
                        allenBradleyNet.setMessageRouter( new MessageRouter(textField_router.getText()));
                    }

                    OperateResult connect = allenBradleyNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(allenBradleyNet, defaultAddress, 1);
                        cipControl.setEnabled(true);
                        cipControl.SetReadWriteCip(allenBradleyNet);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( allenBradleyNet, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "plc.setSlot(Byte.parseByte(\"" + textField4.getText() + "\"));\r\n" );
                    if(!Utilities.IsStringNullOrEmpty(textField_router.getText())) {
                        stringBuilder.append("plc.setMessageRouter( new MessageRouter(\"" + textField_router.getText() + "\"));\r\n");
                    }
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
                if(allenBradleyNet !=null){
                    allenBradleyNet.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                    cipControl.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }

}
