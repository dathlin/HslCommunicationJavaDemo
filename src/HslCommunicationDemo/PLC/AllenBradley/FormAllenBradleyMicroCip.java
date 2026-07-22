package HslCommunicationDemo.PLC.AllenBradley;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.AllenBradley.AllenBradleyMicroCip;
import HslCommunication.Profinet.AllenBradley.AllenBradleyNet;
import HslCommunication.Profinet.Melsec.MelsecA1ENet;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;
import HslCommunicationDemo.PLC.Omron.OmronCipControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormAllenBradleyMicroCip  extends HslJPanel {
    public FormAllenBradleyMicroCip(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Micro Cip", tabbedPane, this));
        AddConnectSegment(this);
        allenBradleyNet = new AllenBradleyMicroCip();

        cipControl = new OmronCipControl();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(cipControl, false, "CipFunction");
        userControlReadWriteDevice.setEnabled(false);
        cipControl.setEnabled(false);

        addressExampleControl = new AddressExampleControl(DemoAllenBradleyHelper.GetCIPAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private AllenBradleyMicroCip allenBradleyNet = null;
    private String defaultAddress = "A";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private OmronCipControl cipControl;
    private TcpConnectControl tcpConnectControl;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()) {
            allenBradleyNet.ConnectClose();
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
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(allenBradleyNet);
                    allenBradleyNet.setSlot(Byte.parseByte(textField4.getText()));

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
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Connect Failed\r\nReason:"+ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }


                StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( allenBradleyNet, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                stringBuilder.append( "setSlot(Byte.parseByte(\"" + textField4.getText() + "\"));\r\n" );
                userControlReadWriteDevice.SetDeviceCode( stringBuilder.toString() );
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
