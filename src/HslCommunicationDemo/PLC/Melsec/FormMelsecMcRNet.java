package HslCommunicationDemo.PLC.Melsec;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Melsec.MelsecMcNet;
import HslCommunication.Profinet.Melsec.MelsecMcRNet;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormMelsecMcRNet extends HslJPanel
{
    public FormMelsecMcRNet( JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Qna-3E R Binary", tabbedPane, this));
        AddConnectSegment(this);
        melsecMcNet = new MelsecMcRNet();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(DemoMelsecHelper.GetMcAddress());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private MelsecMcRNet melsecMcNet = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private TcpConnectControl tcpConnectControl = null;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            melsecMcNet.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationCenterLine, "6000");
        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(melsecMcNet);

                    OperateResult connect = melsecMcNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(melsecMcNet, defaultAddress, 10);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( melsecMcNet, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
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
                if(melsecMcNet!=null){
                    melsecMcNet.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }
}
