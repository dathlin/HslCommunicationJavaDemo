package cn.hslcommunication.HslCommunicationDemo.PLC.Siemens;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import cn.hslcommunication.HslCommunicationDemo.DemoUtils;
import cn.hslcommunication.HslCommunicationDemo.UserControlReadWriteDevice;
import cn.hslcommunication.HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormSiemensS7200 extends JPanel
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
    }

    private SiemensPLCS siemensPLCS = SiemensPLCS.S1200;
    private SiemensS7Net siemensS7Net = null;
    private String defaultAddress = "M100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private SiemensS7Control siemensS7Control;

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
        textField2.setText("102");
        panelConnect.add(textField2);

        JLabel label3 = new JLabel("LocalTSAP：");
        label3.setBounds(305, 10,78, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(384,7,53, 23);
        textField3.setText("4D57");
        panelConnect.add(textField3);

        JLabel label4 = new JLabel("DestTSAP：");
        label4.setBounds(443, 10,78, 17);
        panelConnect.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(519,7,53, 23);
        textField4.setText("4D57");
        panelConnect.add(textField4);



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
        label5.setBounds(337, 33,110, 17);
        label5.setForeground(Color.red);
        panelConnect.add(label5);

        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(734,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(627,11,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    siemensS7Net.setIpAddress(textField1.getText());
                    siemensS7Net.setPort(Integer.parseInt(textField2.getText()));
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
                        button2.setEnabled(true);
                        button1.setEnabled(false);
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
                if(siemensS7Net!=null){
                    siemensS7Net.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                    siemensS7Control.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }
}
