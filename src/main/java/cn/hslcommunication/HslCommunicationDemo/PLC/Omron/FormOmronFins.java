package cn.hslcommunication.HslCommunicationDemo.PLC.Omron;

import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Omron.OmronFinsNet;
import cn.hslcommunication.HslCommunicationDemo.DemoUtils;
import cn.hslcommunication.HslCommunicationDemo.UserControlReadWriteDevice;
import cn.hslcommunication.HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormOmronFins extends JPanel {
    public FormOmronFins(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Fins Tcp", tabbedPane, this));
        AddConnectSegment(this);
        omronFinsNet = new OmronFinsNet();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);
    }

    private OmronFinsNet omronFinsNet = null;
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
        textField2.setText("9600");
        panelConnect.add(textField2);

        JLabel label3 = new JLabel("PLC Unit：");
        label3.setBounds(311, 4,77, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(387,1,40, 23);
        textField3.setText("0");
        panelConnect.add(textField3);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(450,1,80, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedIndex(2);
        panelConnect.add(comboBox1);

        JCheckBox checkBox1 = new JCheckBox("String Reverse");
        checkBox1.setBounds(580,17,140, 17);
        panelConnect.add(checkBox1);

        JLabel label4 = new JLabel("SA1:");
        label4.setBounds(311, 31,42, 17);
        panelConnect.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(358,28,45, 23);
        textField4.setText("");
        textField4.setEditable(false);
        panelConnect.add(textField4);

        JLabel label5 = new JLabel("DA1:");
        label5.setBounds(434, 31,44, 17);
        panelConnect.add(label5);

        JTextField textField5 = new JTextField();
        textField5.setBounds(481,28,45, 23);
        textField5.setText("");
        textField5.setEditable(false);
        panelConnect.add(textField5);

        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(850,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(752,11,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    omronFinsNet.setIpAddress(textField1.getText());
                    omronFinsNet.setPort(Integer.parseInt(textField2.getText()));
                    omronFinsNet.setDataFormat((DataFormat) comboBox1.getSelectedItem());
                    omronFinsNet.getByteTransform().setIsStringReverse(checkBox1.isSelected());

                    OperateResult connect = omronFinsNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
                        textField4.setText(String.valueOf(omronFinsNet.SA1));
                        textField5.setText(String.valueOf(omronFinsNet.DA1));
                        userControlReadWriteDevice.SetReadWriteNet(omronFinsNet, defaultAddress, 10);
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
                if (!button2.isEnabled()) return;
                if(omronFinsNet!=null){
                    omronFinsNet.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }
}
