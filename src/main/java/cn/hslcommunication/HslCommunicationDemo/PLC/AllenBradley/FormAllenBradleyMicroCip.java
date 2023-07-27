package cn.hslcommunication.HslCommunicationDemo.PLC.AllenBradley;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.AllenBradley.AllenBradleyMicroCip;
import cn.hslcommunication.HslCommunicationDemo.DemoUtils;
import cn.hslcommunication.HslCommunicationDemo.PLC.Omron.OmronCipControl;
import cn.hslcommunication.HslCommunicationDemo.UserControlReadWriteDevice;
import cn.hslcommunication.HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormAllenBradleyMicroCip  extends JPanel {
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
    }

    private AllenBradleyMicroCip allenBradleyNet = null;
    private String defaultAddress = "A";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private OmronCipControl cipControl;

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 17,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,14,106, 23);
        textField1.setText("192.168.0.10");
        panelConnect.add(textField1);

        JLabel label2 = new JLabel("Port：");
        label2.setBounds(184, 17,56, 17);
        panelConnect.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(238,14,61, 23);
        textField2.setText("44818");
        panelConnect.add(textField2);

        JLabel label4 = new JLabel("Slot：");
        label4.setBounds(333, 17,48, 17);
        panelConnect.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(379,14,53, 23);
        textField4.setText("0");
        panelConnect.add(textField4);


        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(584,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(477,11,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    allenBradleyNet.setIpAddress(textField1.getText());
                    allenBradleyNet.setPort(Integer.parseInt(textField2.getText()));
                    allenBradleyNet.setSlot(Byte.parseByte(textField4.getText()));

                    OperateResult connect = allenBradleyNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
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
            }
        });
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (button2.isEnabled() == false) return;
                if(allenBradleyNet !=null){
                    allenBradleyNet.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                    cipControl.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }

}
