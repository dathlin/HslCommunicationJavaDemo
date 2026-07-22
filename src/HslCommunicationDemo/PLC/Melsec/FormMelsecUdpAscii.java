package HslCommunicationDemo.PLC.Melsec;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Melsec.MelsecMcAsciiUdp;
import HslCommunication.Profinet.Melsec.MelsecMcUdp;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormMelsecUdpAscii extends HslJPanel {
    public FormMelsecUdpAscii(JTabbedPane tabbedPane){
        setLayout(null);

        add( new UserControlReadWriteHead("Qna-3E Udp Ascii", tabbedPane, this));
        AddConnectSegment(this);

        melsecMcNet = new MelsecMcAsciiUdp();
        melsecMcControl = new MelsecMcControl();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(melsecMcControl, false, "MelsecFunction");
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(DemoMelsecHelper.GetMcAddress());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private MelsecMcAsciiUdp melsecMcNet = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private MelsecMcControl melsecMcControl;
    private TcpConnectControl  tcpConnectControl;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            melsecMcNet.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationTwoLine, "6000");
        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(melsecMcNet);
                    tcpConnectControl.SetConnectSuccess();
                    melsecMcControl.setEnabled(true);
                    userControlReadWriteDevice.SetReadWriteNet(melsecMcNet, defaultAddress, 10);
                    melsecMcControl.SetReadWritePlc(melsecMcNet);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Connect Failed\r\nReason:"+ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }


                StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( melsecMcNet, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                userControlReadWriteDevice.SetDeviceCode( stringBuilder.toString() );
            }
        });
        tcpConnectControl.ButtonDisconnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!tcpConnectControl.ButtonDisconnect.isEnabled()) return;
                if(melsecMcNet!=null){
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                    melsecMcControl.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }

}
