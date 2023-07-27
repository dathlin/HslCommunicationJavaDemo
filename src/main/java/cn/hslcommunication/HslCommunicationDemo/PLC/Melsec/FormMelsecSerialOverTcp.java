package cn.hslcommunication.HslCommunicationDemo.PLC.Melsec;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Melsec.MelsecFxSerialOverTcp;
import cn.hslcommunication.HslCommunicationDemo.DemoUtils;
import cn.hslcommunication.HslCommunicationDemo.UserControlReadWriteDevice;
import cn.hslcommunication.HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormMelsecSerialOverTcp extends JPanel
{

    public FormMelsecSerialOverTcp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("MelsecSerialOverTcp", tabbedPane, this));
        AddConnectSegment(this);
        melsec = new MelsecFxSerialOverTcp();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);
    }

    private MelsecFxSerialOverTcp melsec = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;

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
        textField2.setText("5014");
        panelConnect.add(textField2);

        JCheckBox checkBox1 = new JCheckBox("Use New Version");
        checkBox1.setBounds(325, 11, 130, 21);
        checkBox1.setSelected(true);
        panelConnect.add(checkBox1);

        JCheckBox checkBox2 = new JCheckBox("Got?");
        checkBox2.setBounds(478, 11, 77, 21);
        panelConnect.add(checkBox2);

        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(684,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(577,11,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button1.isEnabled())return;
                super.mouseClicked(e);
                try {
                    melsec.setIpAddress(textField1.getText());
                    melsec.setPort(Integer.parseInt(textField2.getText()));
                    melsec.IsNewVersion = checkBox1.isSelected();
                    melsec.setUseGot(checkBox2.isSelected());

                    OperateResult connect = melsec.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
                        userControlReadWriteDevice.SetReadWriteNet(melsec, defaultAddress, 10);
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
                if(melsec !=null){
                    melsec.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }
}
