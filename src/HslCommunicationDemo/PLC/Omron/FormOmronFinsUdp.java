package HslCommunicationDemo.PLC.Omron;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Omron.OmronFinsNet;
import HslCommunication.Profinet.Omron.OmronFinsUdp;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;
import HslCommunicationDemo.UserControlReadWriteOp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormOmronFinsUdp extends JPanel {
    public FormOmronFinsUdp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Fins Udp", tabbedPane, this));
        AddConnectSegment(this);
        omronFinsNet = new OmronFinsUdp();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);
    }

    private OmronFinsUdp omronFinsNet = null;
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

        JLabel label4 = new JLabel("PC Net Num：");
        label4.setBounds(400, 17,100, 17);
        panelConnect.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(500,14,40, 23);
        textField4.setText("192");
        panelConnect.add(textField4);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(558,13,111, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedIndex(2);
        panelConnect.add(comboBox1);

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
                    omronFinsNet.SA1 = (byte) Integer.parseInt(textField4.getText());
                    omronFinsNet.getByteTransform().setDataFormat((DataFormat) comboBox1.getSelectedItem());

                    button2.setEnabled(true);
                    button1.setEnabled(false);
                    userControlReadWriteDevice.SetReadWriteNet(omronFinsNet, defaultAddress, 10);
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
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }
}
