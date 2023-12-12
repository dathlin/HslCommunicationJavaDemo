package HslCommunicationDemo.PLC.Beckhoff;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Beckhoff.BeckhoffAdsNet;
import HslCommunication.Profinet.Melsec.MelsecMcNet;
import HslCommunication.Utilities;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.PLC.Melsec.MelsecMcControl;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormBeckhoffAdsNet extends JPanel
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

    public static DeviceAddressExample[] GetDeviceAddressExamples( )
    {
        return new DeviceAddressExample[]
                {
                        new DeviceAddressExample( "M100",     "", true, true, "绝对地址，访问位 M100.0" ),
                        new DeviceAddressExample( "I100",     "", true, true, "绝对地址，访问位 I100.0" ),
                        new DeviceAddressExample( "Q100",     "", true, true, "绝对地址，访问位 Q100.0" ),
                        new DeviceAddressExample( "s=abc",    "", true, true, "符号地址，abc全局变量" ),
                        new DeviceAddressExample( "s=MAIN.A", "", true, true, "符号地址，A是MAIN函数地址" ),
                        new DeviceAddressExample( "i=100000", "", true, true, "内存地址" ),
                };
    }

    private AddressExampleControl addressExampleControl;
    private BeckhoffAdsNet beckhoffAdsNet;
    private String defaultAddress = "M100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel, 79);

        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 5,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,2,114, 23);
        textField1.setText("127.0.0.1");
        panelConnect.add(textField1);

        JLabel label2 = new JLabel("Port：");
        label2.setBounds(8, 30,56, 17);
        panelConnect.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(62,27,114, 23);
        textField2.setText("48898");
        panelConnect.add(textField2);

        JLabel label5 = new JLabel("Target NetId：");
        label5.setBounds(179, 5,95, 17);
        panelConnect.add(label5);

        JTextField textField14 = new JTextField();
        textField14.setBounds(273,2,179, 23);
        textField14.setText("");
        panelConnect.add(textField14);

        JLabel label15 = new JLabel("Sender NetId：");
        label15.setBounds(179, 30,98, 17);
        panelConnect.add(label15);

        JTextField textField15 = new JTextField();
        textField15.setBounds(273,27,179, 23);
        textField15.setText("");
        panelConnect.add(textField15);

        JCheckBox checkBox_auto = new JCheckBox("自动AMS NetId");
        checkBox_auto.setBounds(178,54, 115, 21);
        panelConnect.add(checkBox_auto);

        JLabel label6 = new JLabel("<html><span style=\"color:green\">Ams Port</span></html>");
        label6.setBounds(310, 55,64, 17);
        panelConnect.add(label6);

        JTextField textField_ams_port = new JTextField();
        textField_ams_port.setBounds(379,52,72, 23);
        textField_ams_port.setText("851");
        panelConnect.add(textField_ams_port);

        JLabel label8 = new JLabel("示例：192.168.1.100.1.1:801");
        label8.setBounds(473, 4,167, 17);
        panelConnect.add(label8);

        JCheckBox checkBox_tag = new JCheckBox("标签缓存");
        checkBox_tag.setBounds(473,28, 80, 21);
        checkBox_tag.setSelected(true);
        panelConnect.add(checkBox_tag);


        JLabel label7 = new JLabel("<html><span style=\"color:red\">TwinCAT2，端口号801,811,821,831；TwinCAT3，端口号为851,852,853</span></html>");
        label7.setBounds(456, 54,502, 17);
        panelConnect.add(label7);


        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(661,23,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(561,23,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button1.isEnabled())return;
                super.mouseClicked(e);
                try {
                    beckhoffAdsNet.setIpAddress(textField1.getText());
                    beckhoffAdsNet.setPort(Integer.parseInt(textField2.getText()));
                    if (checkBox_auto.isSelected()){
                        beckhoffAdsNet.setUseAutoAmsNetID(true);
                        if (!Utilities.IsStringNullOrEmpty(textField_ams_port.getText())){
                            beckhoffAdsNet.setAmsPort(Integer.parseInt(textField_ams_port.getText()));
                        }
                    }
                    else {
                        beckhoffAdsNet.SetTargetAMSNetId( textField14.getText() );
                        beckhoffAdsNet.SetSenderAMSNetId( textField15.getText() );
                    }
                    beckhoffAdsNet.setUseAutoAmsNetID(checkBox_tag.isSelected());
                    OperateResult connect = beckhoffAdsNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
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
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (button2.isEnabled() == false) return;
                if(beckhoffAdsNet!=null){
                    beckhoffAdsNet.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }

}
