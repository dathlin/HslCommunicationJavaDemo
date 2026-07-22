package HslCommunicationDemo.PLC.Melsec;

import HslCommunication.Profinet.Melsec.MelsecMcUdp;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormMelsecUdp extends HslJPanel {
    public FormMelsecUdp(JTabbedPane tabbedPane){
        setLayout(null);

        add( new UserControlReadWriteHead("Qna-3E Udp Binary", tabbedPane, this));
        AddConnectSegment(this);
        melsecMcNet = new MelsecMcUdp();

        melsecMcControl = new MelsecMcControl();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(melsecMcControl, false, "MelsecFunction");
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(DemoMelsecHelper.GetMcAddress());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private MelsecMcUdp melsecMcNet = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private MelsecMcControl melsecMcControl;
    private TcpConnectControl tcpConnectControl;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            melsecMcNet.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl( panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationCenterLine, "6000" );
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
                    melsecMcNet.Close();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                    melsecMcControl.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }

}
