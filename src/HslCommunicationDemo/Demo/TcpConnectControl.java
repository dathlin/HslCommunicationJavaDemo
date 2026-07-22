package HslCommunicationDemo.Demo;

import HslCommunication.Core.Net.NetworkBase.NetworkDoubleBase;
import HslCommunication.Core.Net.NetworkBase.NetworkUdpDeviceBase;
import HslCommunication.Utilities;
import HslCommunicationDemo.DemoUtils;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.InetSocketAddress;

public class TcpConnectControl extends JPanel {
    public TcpConnectControl( JPanel parent, int y, String port ) {
        this(parent, 58, y, port);
    }
    public TcpConnectControl( JPanel parent, int height, int y, String port ) {
        this.setLayout(null);
        this.setBounds(3,28,1000, height);
        this.setBorder(BorderFactory.createTitledBorder( ""));

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                setBounds(3, 27, parent.getWidth() - 5, height);
            }
        });
        this.TextBoxIp = DemoUtils.CreateIpAddressTextBox(this, y);
        this.TextBoxPort = DemoUtils.CreateIpPortTextBox(this, port, y);
        this.TextBoxTimeout = this.CreateTimeOutTextBox(this, y);
        this.TextBoxLocalPort = this.CreateLocalPortTextBox(this, y);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(640,y - 6,91, 28);
        ButtonConnect = button1;
        add(button1);

        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(750,y - 6,101, 28);
        ButtonDisconnect = button2;
        add(button2);

        ButtonConnect.setEnabled(true);
        ButtonDisconnect.setEnabled(false);
    }
    private  JTextField CreateTimeOutTextBox( JPanel panelConnect, int y ) {
        JLabel label1 = new JLabel("ConnectTimeout：");
        label1.setBounds(317, y, 120, 17);
        panelConnect.add(label1);

        JTextField textField2 = new JTextField();
        textField2.setBounds(422, y - 3, 61, 23);
        textField2.setText("");
        panelConnect.add(textField2);
        return textField2;
    }

    private  JTextField CreateLocalPortTextBox( JPanel panelConnect, int y ) {
        JLabel label1 = new JLabel("LocalPort：");
        label1.setBounds(490, y, 80, 17);
        panelConnect.add(label1);

        JTextField textField2 = new JTextField();
        textField2.setBounds(560, y - 3, 61, 23);
        textField2.setText("");
        panelConnect.add(textField2);
        return textField2;
    }


    public void SetNetworkIpPort(NetworkDoubleBase device) {
        device.setIpAddress(TextBoxIp.getText());
        device.setPort(Integer.parseInt(TextBoxPort.getText()));

        if (!Utilities.IsStringNullOrEmpty(TextBoxTimeout.getText())){
            device.setConnectTimeOut(Integer.parseInt(TextBoxTimeout.getText()));
        }
        if (!Utilities.IsStringNullOrEmpty(TextBoxLocalPort.getText())){
            device.setLocalAddress(new InetSocketAddress(Integer.parseInt(TextBoxLocalPort.getText())));
        }
    }

    public void SetNetworkIpPort(NetworkUdpDeviceBase  device) {
        device.setIpAddress(TextBoxIp.getText());
        device.setPort(Integer.parseInt(TextBoxPort.getText()));

        if (!Utilities.IsStringNullOrEmpty(TextBoxLocalPort.getText())){
            device.setLocalAddress(new InetSocketAddress(Integer.parseInt(TextBoxLocalPort.getText())));
        }
    }

    public void SetConnectSuccess(){
        ButtonConnect.setEnabled(false);
        ButtonDisconnect.setEnabled(true);
    }
    public void SetConnectClose(){
        ButtonConnect.setEnabled(true);
        ButtonDisconnect.setEnabled(false);
    }

    public boolean NeedCloseDevice(){
        if (ButtonConnect == null || ButtonDisconnect == null) return false;
        if (ButtonDisconnect.isEnabled()){
            return true;
        }
        return false;
    }

    public JTextField TextBoxIp = null;
    public JTextField TextBoxPort = null;
    public JTextField TextBoxLocalPort = null;
    public JTextField TextBoxTimeout = null;
    public JButton ButtonConnect = null;
    public JButton ButtonDisconnect = null;

    public static final int HeightTwoLine = 58;
    public static final int LocationCenterLine = 18;
    public static final int LocationOneLine = 8;
    public static final int LocationTwoLine = 34;
    public static final int LocationThreeLine = 59;


}
