package HslCommunicationDemo;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Core.Types.OperateResultExTwo;
import HslCommunication.Profinet.Omron.OmronCipNet;
import HslCommunication.Profinet.Omron.OmronConnectedCipNet;
import HslCommunication.Profinet.Siemens.SiemensS7Net;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormOmronConnectedCipNet  extends JDialog {

    public FormOmronConnectedCipNet(){
        this.setTitle("Omron plc Test Tool");
        this.setSize(1020, 684);
        this.setLocationRelativeTo(null);
        this.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        AddNormal(panel);
        AddConnectSegment(panel);
        AddContent(panel);

        this.add(panel);

        omronCipNet = new OmronConnectedCipNet();
    }

    private OmronConnectedCipNet omronCipNet = null;
    private JPanel panelContent = null;
    private String defaultAddress = "A";
    private UserControlReadWriteOp userControlReadWriteOp1 = null;


    public void AddNormal(JPanel panel){
        JLabel label1 = new JLabel("Blogs：");
        label1.setBounds(11, 9,68, 17);
        panel.add(label1);

        JLabel label5 = new JLabel("https://www.hslcommunication.cn");
        label5.setBounds(80, 9,400, 17);
        panel.add(label5);

        JLabel label2 = new JLabel("Protocols");
        label2.setBounds(466, 9,68, 17);
        panel.add(label2);

        JLabel label3 = new JLabel("Connected Cip");
        label3.setForeground(Color.RED);
        label3.setBounds(540, 9,260, 17);
        panel.add(label3);

        JLabel label4 = new JLabel("By Richard Hu");
        label4.setForeground(Color.ORANGE);
        label4.setBounds(887, 9,108, 17);
        panel.add(label4);
    }

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = new JPanel();
        panelConnect.setLayout(null);
        panelConnect.setBounds(4,36,996, 42);
        panelConnect.setBorder(BorderFactory.createTitledBorder( ""));

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
                        DemoUtils.SetPanelEnabled(panelContent,true);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
                        userControlReadWriteOp1.SetReadWriteNet(omronCipNet, defaultAddress, 1);
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
                    DemoUtils.SetPanelEnabled(panelContent,false);
                }
            }
        });


        panel.add(panelConnect);
    }

    public void AddContent(JPanel panel){
        JPanel panelContent = new JPanel();
        panelContent.setLayout(null);
        panelContent.setBounds(4,82,996, 559);
        panelContent.setBorder(BorderFactory.createTitledBorder( ""));

        AddReadWrite(panelContent);
        AddReadBulk(panelContent);
        AddCoreRead(panelContent);
        AddTimeReadWrite(panelContent);
        AddTypeReadWrite(panelContent);

        panel.add(panelContent);
        this.panelContent = panelContent;
        DemoUtils.SetPanelEnabled(this.panelContent,false);
    }

    public void AddReadWrite(JPanel panel){
        userControlReadWriteOp1 = new UserControlReadWriteOp();
        userControlReadWriteOp1.setLayout(null);
        userControlReadWriteOp1.setBounds(11,2,962, 235);
        panel.add(userControlReadWriteOp1);
    }

    public void AddReadBulk(JPanel panel){
        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);
        panelRead.setBounds(3,243,526, 154);
        panelRead.setBorder(BorderFactory.createTitledBorder( "Read byte by Length"));

        JLabel label1 = new JLabel("Address：");
        label1.setBounds(9, 30,70, 17);
        panelRead.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(83,27,82, 23);
        textField1.setText(defaultAddress);
        panelRead.add(textField1);

        JLabel label2 = new JLabel("Length：");
        label2.setBounds(185, 30,60, 17);
        panelRead.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(234,27,102, 23);
        textField2.setText("10");
        panelRead.add(textField2);


        JLabel label3 = new JLabel("Result：");
        label3.setBounds(9, 58,70, 17);
        panelRead.add(label3);

        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea1);
        jsp.setBounds(83,56,425, 78);
        panelRead.add(jsp);

        JButton button2 = new JButton("Read");
        button2.setFocusPainted(false);
        button2.setBounds(426,24,82, 28);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button2.isEnabled() == false) return;
                super.mouseClicked(e);
                OperateResultExOne<byte[]> read = omronCipNet.Read(textField1.getText(), Short.parseShort(textField2.getText()));

                if (read.IsSuccess) {
                    textArea1.setText("Result：" + HslCommunication.BasicFramework.SoftBasic.ByteToHexString(read.Content));
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button2);

        panel.add(panelRead);
    }

    public void AddCoreRead(JPanel panel){
        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);
        panelRead.setBounds(3,403,526, 147);
        panelRead.setBorder(BorderFactory.createTitledBorder( "报文读取测试-Full message read"));

        JLabel label1 = new JLabel("Message：");
        label1.setBounds(9, 30,70, 17);
        panelRead.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(83,27,337, 23);
        textField1.setText(SoftBasic.ByteToHexString(SiemensS7Net.BuildReadCommand(defaultAddress,(short) 1).Content, ' '));
        panelRead.add(textField1);

        JLabel label3 = new JLabel("Result：");
        label3.setBounds(9, 58,70, 17);
        panelRead.add(label3);

        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea1);
        jsp.setBounds(83,56,425, 52);
        panelRead.add(jsp);

        JButton button2 = new JButton("Read");
        button2.setFocusPainted(false);
        button2.setBounds(426,24,82, 28);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button2.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<byte[]> read = omronCipNet.ReadCipFromServer(SoftBasic.HexStringToBytes(textField1.getText()));
                if(read.IsSuccess){
                    textArea1.setText(SoftBasic.ByteToHexString(read.Content));
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button2);

        panel.add(panelRead);
    }

    public void AddTimeReadWrite(JPanel panel){
        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);
        panelRead.setBounds(535,243,456, 154);
        panelRead.setBorder(BorderFactory.createTitledBorder( "报文读取测试-Full message read"));

        panel.add(panelRead);
    }

    public void AddTypeReadWrite(JPanel panel) {
        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);
        panelRead.setBounds(535, 403, 452, 147);
        panelRead.setBorder(BorderFactory.createTitledBorder("Read Write Type and byte[]"));

        JLabel label1 = new JLabel("Address:");
        label1.setBounds(10, 30, 54, 17);
        panelRead.add(label1);

        JTextField textField_address = new JTextField();
        textField_address.setBounds(64, 27, 239, 23);
        textField_address.setText("A1");
        panelRead.add(textField_address);

        JLabel label2 = new JLabel("Type:");
        label2.setBounds(309, 30, 44, 17);
        panelRead.add(label2);

        JTextField textField_type = new JTextField();
        textField_type.setText("C1");
        textField_type.setBounds(363, 27, 79, 23);
        panelRead.add(textField_type);

        JLabel label3 = new JLabel("Data:");
        label3.setBounds(10, 59, 44, 17);
        panelRead.add(label3);

        JTextArea textArea_data = new JTextArea();
        textArea_data.setLineWrap(true);
        textArea_data.setBounds(64, 56, 378, 52);
        panelRead.add(textArea_data);

        JLabel label4 = new JLabel("Len:");
        label4.setBounds(10, 119, 44, 17);
        panelRead.add(label4);

        JTextField textField_length = new JTextField();
        textField_length.setText("1");
        textField_length.setBounds(64, 116, 87, 23);
        panelRead.add(textField_length);

        JButton button_write = new JButton("Write");
        button_write.setBounds(157, 114, 86, 28);
        button_write.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!button_write.isEnabled()) return;
                try
                {
                    OperateResult write = omronCipNet.WriteTag(
                            textField_address.getText(),
                            Short.parseShort( textField_type.getText(), 16 ),
                            SoftBasic.HexStringToBytes(textArea_data.getText() ),
                            Integer.parseInt( textField_length.getText() ) );
                    DemoUtils.WriteResultRender( write, textField_address.getText() );
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button_write);

        JButton button_read = new JButton("Read");
        button_read.setBounds(247, 114, 92, 28);
        button_read.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!button_read.isEnabled()) return;
                OperateResultExTwo<Short, byte[]> read = omronCipNet.ReadTag(textField_address.getText(), Short.parseShort(textField_length.getText()));
                if (!read.IsSuccess) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    textField_type.setText(String.format("%X", read.Content1));
                    textArea_data.setText(SoftBasic.ByteToHexString(read.Content2, ' '));
                }
            }
        });
        panelRead.add(button_read);

        JButton button_read_type = new JButton("ReadType");
        button_read_type.setBounds(343, 114, 99, 28);
        button_read_type.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(!button_read_type.isEnabled()) return;
                JOptionPane.showMessageDialog(
                        null,
                        omronCipNet.ProductName,
                        "Result",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panelRead.add(button_read_type);

        panel.add(panelRead);
    }
}
