package HslCommunicationDemo.PLC.Siemens;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Siemens.Helper.SiemensS7Helper;
import HslCommunication.Profinet.Siemens.SiemensFetchWriteNet;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormSiemensS7200 extends HslJPanel
{

    public FormSiemensS7200(JTabbedPane tabbedPane,SiemensPLCS siemensPLCS){
        this.siemensPLCS = siemensPLCS;
        setLayout(null);
        add( new UserControlReadWriteHead("s7-" + siemensPLCS.toString(), tabbedPane, this));
        AddConnectSegment(this);
        siemensS7Net = new SiemensS7Net(siemensPLCS);

        siemensS7Control = new SiemensS7Control();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(siemensS7Control, false, "S7Control");
        userControlReadWriteDevice.setEnabled(false);
        siemensS7Control.setEnabled(false);

        addressExampleControl = new AddressExampleControl(SiemensHelper.GetSiemensS7Address());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private SiemensPLCS siemensPLCS = SiemensPLCS.S1200;
    private SiemensS7Net siemensS7Net = null;
    private String defaultAddress = "M100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private SiemensS7Control siemensS7Control;
    private TcpConnectControl tcpConnectControl = null;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            siemensS7Net.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "102");
        JLabel label3 = new JLabel("LocalTSAP：");
        label3.setBounds(5, TcpConnectControl.LocationTwoLine,78, 17);
        tcpConnectControl.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(85,TcpConnectControl.LocationTwoLine -3,53, 23);
        textField3.setText("4D57");
        tcpConnectControl.add(textField3);

        JLabel label4 = new JLabel("DestTSAP：");
        label4.setBounds(145, TcpConnectControl.LocationTwoLine,78, 17);
        tcpConnectControl.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(230,TcpConnectControl.LocationTwoLine - 3,53, 23);
        textField4.setText("4D57");
        tcpConnectControl.add(textField4);



        if (this.siemensPLCS == SiemensPLCS.S400)
        {
            textField3.setText("0");
            textField4.setText("3");
        }
        else if(this.siemensPLCS == SiemensPLCS.S1200)
        {
            textField3.setText("0");
            textField4.setText("0");
        }
        else if (this.siemensPLCS == SiemensPLCS.S300)
        {
            textField3.setText("0");
            textField4.setText("2");
        }
        else if (this.siemensPLCS == SiemensPLCS.S1500)
        {
            textField3.setText("0");
            textField4.setText("0");
        }

        JLabel label5 = new JLabel("16进制!!! [16 HEX]");
        label5.setBounds(290, TcpConnectControl.LocationTwoLine,110, 17);
        label5.setForeground(Color.red);
        tcpConnectControl.add(label5);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(siemensS7Net);
                    if (siemensPLCS == SiemensPLCS.S200)
                    {
                        siemensS7Net.setLocalTSAP((int)Long.parseLong(textField3.getText(), 16));
                        siemensS7Net.setDestTSAP((int)Long.parseLong(textField4.getText(), 16));
                    }
                    OperateResult connect = siemensS7Net.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(siemensS7Net, defaultAddress, 10);
                        siemensS7Control.setEnabled(true);
                        siemensS7Control.SetReadWriteS7(siemensS7Net);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }


                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( siemensS7Net, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    if (siemensPLCS == SiemensPLCS.S200)
                    {
                        stringBuilder.append( "plc.setLocalTSAP((int)Long.parseLong(\"" + textField3.getText() + "\", 16));\r\n" );
                        stringBuilder.append( "plc.setDestTSAP((int)Long.parseLong(\"" + textField4.getText() + "\", 16));\r\n" );
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
                if (tcpConnectControl.ButtonDisconnect.isEnabled() == false) return;
                if(siemensS7Net!=null){
                    siemensS7Net.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                    siemensS7Control.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }
}
