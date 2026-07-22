package HslCommunicationDemo.PLC.Beckhoff;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.AllenBradley.AllenBradleyPcccNet;
import HslCommunication.Profinet.Beckhoff.BeckhoffAdsNet;
import HslCommunication.Profinet.Melsec.MelsecMcNet;
import HslCommunication.Utilities;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.HslJPanel;
import HslCommunicationDemo.PLC.Melsec.MelsecMcControl;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormBeckhoffAdsNet extends HslJPanel
{

    public FormBeckhoffAdsNet(JTabbedPane tabbedPane ){
        setLayout(null);
        add( new UserControlReadWriteHead("Beckhoff Ads", tabbedPane, this));
        AddConnectSegment(this);
        beckhoffAdsNet = new BeckhoffAdsNet();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this, 109);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl( GetDeviceAddressExamples( ) );
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle( ));
    }

    public static DeviceAddressExample[] GetDeviceAddressExamples( ) {
        return new DeviceAddressExample[]
                {
                        new DeviceAddressExample("M100", "", true, true, "绝对地址，访问位 M100.0"),
                        new DeviceAddressExample("I100", "", true, true, "绝对地址，访问位 I100.0"),
                        new DeviceAddressExample("Q100", "", true, true, "绝对地址，访问位 Q100.0"),
                        new DeviceAddressExample("s=.A", "", true, true, "符号地址，A全局变量，Twincat2全局变量前面加一个点" ),
                        new DeviceAddressExample("s=abc", "", true, true, "符号地址，abc全局变量"),
                        new DeviceAddressExample("s=MAIN.A", "", true, true, "符号地址，A是MAIN函数地址"),
                        new DeviceAddressExample("i=100000", "", true, true, "内存地址"),
                        new DeviceAddressExample("ig=0xF030;8", "Index Group", true, true, "手动指定Q数据区，偏移地址"),
                        new DeviceAddressExample("ig=0xF020;8", "Index Group", true, true, "手动指定I数据区，偏移地址"),
                        new DeviceAddressExample("ig=0x4020;8", "Index Group", true, true, "手动指定M数据区，偏移地址，等同于 M8"),
                };
    }

    private AddressExampleControl addressExampleControl;
    private BeckhoffAdsNet beckhoffAdsNet;
    private String defaultAddress = "M100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private TcpConnectControl tcpConnectControl = null;


    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            beckhoffAdsNet.ConnectClose();
        }
    }


    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, 80, TcpConnectControl.LocationOneLine, "48898");

        JLabel label5 = new JLabel("Target NetId：");
        label5.setBounds(5, TcpConnectControl.LocationTwoLine,95, 17);
        tcpConnectControl.add(label5);

        JTextField textField14 = new JTextField();
        textField14.setBounds(100,TcpConnectControl.LocationTwoLine - 3,160, 23);
        textField14.setText("");
        tcpConnectControl.add(textField14);

        JLabel label15 = new JLabel("Sender NetId：");
        label15.setBounds(5, TcpConnectControl.LocationThreeLine,98, 17);
        tcpConnectControl.add(label15);

        JTextField textField15 = new JTextField();
        textField15.setBounds(100,TcpConnectControl.LocationThreeLine - 3,160, 23);
        textField15.setText("");
        tcpConnectControl.add(textField15);

        JCheckBox checkBox_auto = new JCheckBox("自动AMS NetId");
        checkBox_auto.setBounds(270,TcpConnectControl.LocationThreeLine - 1, 115, 21);
        tcpConnectControl.add(checkBox_auto);

        JLabel label6 = new JLabel("<html><span style=\"color:green\">Ams Port</span></html>");
        label6.setBounds(390, TcpConnectControl.LocationThreeLine,64, 17);
        tcpConnectControl.add(label6);

        JTextField textField_ams_port = new JTextField();
        textField_ams_port.setBounds(460,TcpConnectControl.LocationThreeLine - 3,72, 23);
        textField_ams_port.setText("851");
        tcpConnectControl.add(textField_ams_port);

        JLabel label8 = new JLabel("示例：192.168.1.100.1.1:801");
        label8.setBounds(270, TcpConnectControl.LocationTwoLine,167, 17);
        tcpConnectControl.add(label8);

        JCheckBox checkBox_tag = new JCheckBox("标签缓存");
        checkBox_tag.setBounds(473,TcpConnectControl.LocationTwoLine - 1, 80, 21);
        checkBox_tag.setSelected(true);
        tcpConnectControl.add(checkBox_tag);


        JLabel label7 = new JLabel("<html><span style=\"color:red\">TwinCAT2，端口号801,811,821,831；TwinCAT3，端口号为851,852,853</span></html>");
        label7.setBounds(536, TcpConnectControl.LocationThreeLine,502, 17);
        tcpConnectControl.add(label7);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(beckhoffAdsNet);
                    if (checkBox_auto.isSelected()){
                        beckhoffAdsNet.setUseAutoAmsNetID(true);
                        if (!Utilities.IsStringNullOrEmpty(textField_ams_port.getText())){
                            beckhoffAdsNet.setAmsPort(Integer.parseInt(textField_ams_port.getText()));
                        }
                    }
                    else {
                        beckhoffAdsNet.setUseAutoAmsNetID(false);
                        beckhoffAdsNet.SetTargetAMSNetId( textField14.getText() );
                        beckhoffAdsNet.SetSenderAMSNetId( textField15.getText() );
                    }
                    beckhoffAdsNet.setUseTagCache(checkBox_tag.isSelected());
                    OperateResult connect = beckhoffAdsNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(beckhoffAdsNet, defaultAddress, 10);

                        if (checkBox_auto.isSelected()){
                            textField14.setText( beckhoffAdsNet.GetTargetAMSNetId( ));
                            textField15.setText( beckhoffAdsNet.GetSenderAMSNetId( ));
                        }
                        else {
                            if (Utilities.IsStringNullOrEmpty(textField14.getText())) textField14.setText( beckhoffAdsNet.GetTargetAMSNetId( ));
                            if (Utilities.IsStringNullOrEmpty(textField15.getText())) textField15.setText( beckhoffAdsNet.GetSenderAMSNetId( ));
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( beckhoffAdsNet, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    if (checkBox_auto.isSelected()){
                        stringBuilder.append( "plc.setUseAutoAmsNetID(true);\r\n" );
                        if (!Utilities.IsStringNullOrEmpty(textField_ams_port.getText())){
                            stringBuilder.append( "plc.setAmsPort(Integer.parseInt(\"" + textField_ams_port.getText() + "\"));\r\n");
                        }
                    }
                    else {
                        stringBuilder.append( "plc.setUseAutoAmsNetID(false);\r\n");
                        stringBuilder.append( "plc.SetTargetAMSNetId( \"" + textField14.getText() + "\" );\r\n" );
                        stringBuilder.append( "plc.SetSenderAMSNetId( \"" + textField15.getText() + "\" );\r\n" );
                    }
                    stringBuilder.append( "plc.setUseTagCache(" + checkBox_tag.isSelected() + ");\r\n" );
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
                if(beckhoffAdsNet!=null){
                    beckhoffAdsNet.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }

}
