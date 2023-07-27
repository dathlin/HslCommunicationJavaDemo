package cn.hslcommunication.HslCommunicationDemo.PLC.Omron;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Omron.OmronConnectedCipNet;
import cn.hslcommunication.HslCommunicationDemo.DemoUtils;
import cn.hslcommunication.HslCommunicationDemo.UserControlReadWriteDevice;
import cn.hslcommunication.HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormOmronConnectedCipNet  extends JPanel {

    public FormOmronConnectedCipNet(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Connected Cip", tabbedPane, this));
        AddConnectSegment(this);
        omronCipNet = new OmronConnectedCipNet();

        omronCipControl = new OmronCipControl();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(omronCipControl, false, "CipFunction");
        userControlReadWriteDevice.setEnabled(false);
        omronCipControl.setEnabled(false);
    }

    private OmronConnectedCipNet omronCipNet = null;
    private String defaultAddress = "A";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private OmronCipControl omronCipControl;


    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect =  DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 12,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,9,106, 23);
        textField1.setText("192.168.0.10");
        panelConnect.add(textField1);

        JLabel label2 = new JLabel("Port：");
        label2.setBounds(184, 12,56, 17);
        panelConnect.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(238,9,61, 23);
        textField2.setText("44818");
        panelConnect.add(textField2);

        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(584,6,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(477,6,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    omronCipNet.setIpAddress(textField1.getText());
                    omronCipNet.setPort(Integer.parseInt(textField2.getText()));

                    OperateResult connect = omronCipNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
                        userControlReadWriteDevice.SetReadWriteNet(omronCipNet, defaultAddress, 1);
                        omronCipControl.setEnabled(true);
                        omronCipControl.SetReadWriteCip(omronCipNet);
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
                if(omronCipNet !=null){
                    omronCipNet.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                    omronCipControl.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }

}
