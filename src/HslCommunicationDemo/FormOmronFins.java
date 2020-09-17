package HslCommunicationDemo;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Omron.OmronFinsNet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormOmronFins extends JDialog {



    public FormOmronFins(){
        this.setTitle("Omrom Fins Test Tool");
        this.setSize(1020, 684);
        this.setLocationRelativeTo(null);
        this.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        AddNormal(panel);
        AddConnectSegment(panel);
        AddContent(panel);

        this.add(panel);

        omronFinsNet = new OmronFinsNet();
    }

    private OmronFinsNet omronFinsNet = null;
    private JPanel panelContent = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteOp userControlReadWriteOp1 = null;


    public void AddNormal(JPanel panel){
        JLabel label1 = new JLabel("Blogs：");
        label1.setBounds(11, 9,68, 17);
        panel.add(label1);

        JLabel label5 = new JLabel("none");
        label5.setBounds(80, 9,400, 17);
        panel.add(label5);

        JLabel label2 = new JLabel("Protocols");
        label2.setBounds(466, 9,68, 17);
        panel.add(label2);

        JLabel label3 = new JLabel("Fins Tcp");
        label3.setForeground(Color.RED);
        label3.setBounds(540, 9,160, 17);
        panel.add(label3);

        JLabel label4 = new JLabel("By Richard Hu");
        label4.setForeground(Color.ORANGE);
        label4.setBounds(887, 9,108, 17);
        panel.add(label4);
    }

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = new JPanel();
        panelConnect.setLayout(null);
        panelConnect.setBounds(14,32,978, 54);
        panelConnect.setBorder(BorderFactory.createTitledBorder( ""));

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
        label3.setBounds(338, 17,56, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(400,14,40, 23);
        textField3.setText("0");
        panelConnect.add(textField3);

        JLabel label4 = new JLabel("PC Net：");
        label4.setBounds(452, 17,56, 17);
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
        comboBox1.setSelectedItem(0);
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
                    omronFinsNet.DA2 = (byte) Integer.parseInt(textField3.getText());
                    omronFinsNet.setSA1((byte) Integer.parseInt(textField4.getText()));
                    omronFinsNet.setDataFormat((DataFormat) comboBox1.getSelectedItem());

                    OperateResult connect = omronFinsNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        DemoUtils.SetPanelEnabled(panelContent,true);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
                        userControlReadWriteOp1.SetReadWriteNet(omronFinsNet, defaultAddress, 10);
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
                    DemoUtils.SetPanelEnabled(panelContent,false);
                }
            }
        });


        panel.add(panelConnect);
    }

    public void AddContent(JPanel panel){
        JPanel panelContent = new JPanel();
        panelContent.setLayout(null);
        panelContent.setBounds(14,95,978, 537);
        panelContent.setBorder(BorderFactory.createTitledBorder( ""));

        AddReadWrite(panelContent);
        AddReadBulk(panelContent);
        AddCoreRead(panelContent);

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
        panelRead.setBounds(11,243,518, 154);
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
                OperateResultExOne<byte[]> read = omronFinsNet.Read(textField1.getText(),Short.parseShort(textField2.getText()));
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

    public void AddCoreRead(JPanel panel){
        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);
        panelRead.setBounds(11,403,518, 118);
        panelRead.setBorder(BorderFactory.createTitledBorder( "报文读取测试-Full message read"));

        JLabel label1 = new JLabel("Message：");
        label1.setBounds(9, 30,70, 17);
        panelRead.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(83,27,337, 23);
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
                if (button2.isEnabled() == false) return;
                super.mouseClicked(e);
                OperateResultExOne<byte[]> read = omronFinsNet.ReadFromCoreServer(SoftBasic.HexStringToBytes(textField1.getText()));
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
}
