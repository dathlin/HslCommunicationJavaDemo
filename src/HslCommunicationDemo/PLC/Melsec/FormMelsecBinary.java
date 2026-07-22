package HslCommunicationDemo.PLC.Melsec;

import HslCommunication.Core.Types.FunctionOperateExTwo;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Melsec.MelsecMcNet;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.HslJPanel;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormMelsecBinary extends HslJPanel
{

    public FormMelsecBinary( JTabbedPane tabbedPane ){
        setLayout(null);
        add( new UserControlReadWriteHead("Qna-3E Binary", tabbedPane, this));
        AddConnectSegment(this);
        melsecMcNet = new MelsecMcNet();

        melsecMcControl = new MelsecMcControl();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(melsecMcControl, false, "MelsecFunction");
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(DemoMelsecHelper.GetMcAddress(true));
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());

    }

    private AddressExampleControl addressExampleControl;
    private MelsecMcNet melsecMcNet = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private MelsecMcControl melsecMcControl;
    private TcpConnectControl tcpConnectControl;

    @Override
    public void OnClose() {
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
                        melsecMcControl.setEnabled(true);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(melsecMcNet, defaultAddress, 10);
                        userControlReadWriteDevice.SetWriteRandom(new FunctionOperateExTwo<String[], byte[], OperateResult>(){
                            @Override
                            public OperateResult Action(String[] content1, byte[] content2) {
                                return melsecMcNet.WriteRandom(content1, content2);
                            }
                        });
                        melsecMcControl.SetReadWritePlc(melsecMcNet);
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
                    melsecMcControl.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);

    }

}
