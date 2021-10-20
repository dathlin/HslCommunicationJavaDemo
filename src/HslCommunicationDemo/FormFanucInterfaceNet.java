package HslCommunicationDemo;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunication.Robot.FANUC.FanucData;
import HslCommunication.Robot.FANUC.FanucInterfaceNet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormFanucInterfaceNet extends JDialog {
    public FormFanucInterfaceNet(){
        this.setTitle("Fanuc robot Test Tool");
        this.setSize(1020, 684);
        this.setLocationRelativeTo(null);
        this.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        AddNormal(panel);
        AddConnectSegment(panel);
        AddContent(panel);

        this.add(panel);

        fanucInterfaceNet = new FanucInterfaceNet();
    }

    private FanucInterfaceNet fanucInterfaceNet = null;
    private JPanel panelContent = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteOp userControlReadWriteOp1 = null;


    public void AddNormal(JPanel panel){
        JLabel label1 = new JLabel("Blogs：");
        label1.setBounds(11, 9,68, 17);
        panel.add(label1);

        JLabel label5 = new JLabel("");
        label5.setBounds(80, 9,400, 17);
        panel.add(label5);

        JLabel label2 = new JLabel("Protocols");
        label2.setBounds(466, 9,68, 17);
        panel.add(label2);

        JLabel label3 = new JLabel("Robot Interface");
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
        textField2.setText("60008");
        panelConnect.add(textField2);


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
                if (!button1.isEnabled())return;
                super.mouseClicked(e);
                try {
                    fanucInterfaceNet.setIpAddress(textField1.getText());
                    fanucInterfaceNet.setPort(Integer.parseInt(textField2.getText()));

                    OperateResult connect = fanucInterfaceNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        DemoUtils.SetPanelEnabled(panelContent,true);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
                        userControlReadWriteOp1.SetReadWriteNet(fanucInterfaceNet, defaultAddress, 10);
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
                if(fanucInterfaceNet !=null){
                    fanucInterfaceNet.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    DemoUtils.SetPanelEnabled(panelContent,false);
                }
            }
        });


        panel.add(panelConnect);
    }

    public void AddContent(JPanel panel){

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(14,95,978, 537);
        panel.add(tabbedPane);

        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);
        panelRead.setBounds(0,0,968, 514);
        panelRead.setBorder(BorderFactory.createTitledBorder( ""));
        AddAllRead(panelRead);
        tabbedPane.add("All Read", panelRead);

        JPanel panelContent = new JPanel();
        panelContent.setLayout(null);
        panelContent.setBounds(0,0,968, 514);
        panelContent.setBorder(BorderFactory.createTitledBorder( ""));

        AddReadWrite(panelContent);
        AddReadBulk(panelContent);

        tabbedPane.add("Single Read Write", panelContent);
        this.panelContent = panelContent;
        DemoUtils.SetPanelEnabled(this.panelContent,false);
    }

    public void AddAllRead(JPanel panel){
        JButton button_read = new JButton("String Read");
        button_read.setBounds(60, 6, 112, 28);
        panel.add(button_read);

        JLabel label1 = new JLabel("Result:");
        label1.setBounds(3,37,55,17);
        panel.add(label1);


        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea1);
        jsp.setBounds(61,39,901, 469);
        panel.add(jsp);

        button_read.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(!panelContent.isEnabled()) return;

                OperateResultExOne<FanucData> read = fanucInterfaceNet.ReadFanucData();
                if(!read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    textArea1.setText(read.Content.toString());
                }
            }
        });
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
        panelRead.setBounds(7,252,954, 256);
        panelRead.setBorder(BorderFactory.createTitledBorder( "Single function test"));

        JLabel label1 = new JLabel("Area：");
        label1.setBounds(770, 23,56, 17);
        panelRead.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(841,20,92, 23);
        textField1.setText("70");
        panelRead.add(textField1);

        JLabel label2 = new JLabel("Offset：");
        label2.setBounds(770, 52,68, 17);
        panelRead.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(841,49,92, 23);
        textField2.setText("1");
        panelRead.add(textField2);

        JLabel label3 = new JLabel("Length：");
        label3.setBounds(770, 80,50, 17);
        panelRead.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(841,77,92, 23);
        textField3.setText("4");
        panelRead.add(textField3);


        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea1);
        jsp.setBounds(6,133,942, 117);
        panelRead.add(jsp);


        JButton button32 = new JButton("Read");
        button32.setFocusPainted(false);
        button32.setBounds(773,103,77, 25);
        button32.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button32.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<byte[]> read = fanucInterfaceNet.Read((byte) Short.parseShort(textField1.getText()),
                        Integer.parseInt(textField2.getText()),
                        Short.parseShort(textField3.getText()));
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
        panelRead.add(button32);

        JButton button3 = new JButton("r-SDO");
        button3.setBounds(6,22,96,31);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button3.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadSDO(1, (short) 100);
                if(read.IsSuccess){
                    textArea1.setText(SoftBasic.ArrayFormat(read.Content));
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
        panelRead.add(button3);

        JButton button7 = new JButton("r-SDI");
        button7.setBounds(108,22,71,31);
        button7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button7.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadSDI( 1, (short) 10 );
                if (read.IsSuccess)
                {
                    textArea1.setText(SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button7);

        JButton button10 = new JButton("r-SO");
        button10.setBounds(185,22,71,31);
        button10.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button10.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadSO(0, (short) 10);
                if (read.IsSuccess)
                {
                    textArea1.setText(SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button10);

        JButton button13 = new JButton("r-UI");
        button13.setBounds(262,22,68,31);
        button13.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button13.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadUI(1, (short)10);
                if (read.IsSuccess)
                {
                    textArea1.setText(SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button13);

        JButton button16 = new JButton("r-GI");
        button16.setBounds(336,22,72,31);
        button16.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button16.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<short[]> read = fanucInterfaceNet.ReadGI(1, (short) 3);
                if (read.IsSuccess)
                {
                    textArea1.setText(SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button16);

        JButton button19 = new JButton("r-WO");
        button19.setBounds(414,22,72,31);
        button19.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button19.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadSDO( 1, (short) 5 );
                if (read.IsSuccess)
                {
                    textArea1.setText(SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog(
                        null,
                        "Read Failed:" + read.ToMessageShowString(),
                        "Result",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button19);

        JButton button22 = new JButton("w-SDO");
        button22.setBounds(492,22,108,31);
        button22.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button22.isEnabled()) return;
                super.mouseClicked(e);
                WriteSDO(1);
            }
        });
        panelRead.add(button22);

        JButton button25 = new JButton("w-SDI");
        button25.setBounds(606,22,69,31);
        button25.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button25.isEnabled()) return;
                super.mouseClicked(e);
                w_sdi++;

                boolean[] value = new boolean[10];
                if (w_sdi % 2 == 1)
                {
                    for (int i = 0; i < value.length; i++)
                    {
                        value[i] = true;
                    }
                }

                OperateResult write = fanucInterfaceNet.WriteSDI( 1, value );
                if (write.IsSuccess)
                {
                    JOptionPane.showMessageDialog( null,  "Write Success！value:" + value[0] );
                }
                else
                {
                    JOptionPane.showMessageDialog( null,  "Write Failed！" + write.Message );
                }
            }
        });
        panelRead.add(button25);

        JButton button28 = new JButton("w-GO");
        button28.setBounds(681,22,69,31);
        button28.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button28.isEnabled()) return;
                super.mouseClicked(e);
                int intStartIndex = 1;
                w_go++;

                short[] value = new short[3];
                for (int i = 0; i < value.length; i++)
                {
                    value[i] = (short) ((w_go % 3 + 1) * 11);
                }

                OperateResult write = fanucInterfaceNet.WriteGO( intStartIndex, value );
                if (write.IsSuccess)
                {
                    JOptionPane.showMessageDialog( null,   "Write Success！value:" + value[0] );
                }
                else
                {
                    JOptionPane.showMessageDialog( null,   "Write Failed！" + write.Message );
                }
            }
        });
        panelRead.add(button28);

        JButton button5 = new JButton("r-SDO[1000X]");
        button5.setBounds(6,59,96,31);
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button5.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadSDO( 10001, (short) 100 );
                if (read.IsSuccess)
                {
                    textArea1.setText("SDO[1000x]:" + SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Read Failed！" + read.Message );
                }
            }
        });
        panelRead.add(button5);

        JButton button8 = new JButton("r-RDO");
        button8.setBounds(108,59,71,31);
        button8.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button8.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadRDO( 1, (short) 10 );
                if (read.IsSuccess)
                {
                    textArea1.setText("RDO:" + SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog( null,  "Read Failed！" + read.Message );
                }
            }
        });
        panelRead.add(button8);

        JButton button11 = new JButton("r-SI");
        button11.setBounds(185,59,71,31);
        button11.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button11.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadSI( 0, (short) 10 );
                if (read.IsSuccess)
                {
                    textArea1.setText("SI:" + SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Read Failed！" + read.Message );
                }
            }
        });
        panelRead.add(button11);

        JButton button14 = new JButton("r-GO");
        button14.setBounds(262,59,68,31);
        button14.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button14.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<short[]> read = fanucInterfaceNet.ReadGO( 1, (short) 3 );
                if (read.IsSuccess)
                {
                    textArea1.setText("GO:" + SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog( null,  "Read Failed！" + read.Message );
                }
            }
        });
        panelRead.add(button14);

        JButton button17 = new JButton("r-AO");
        button17.setBounds(336,59,72,31);
        button17.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button17.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<short[]> read = fanucInterfaceNet.ReadGO( 1001, (short) 3 );
                if (read.IsSuccess)
                {
                    textArea1.setText("AO:" + SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog( null,  "Read Failed！" + read.Message );
                }
            }
        });
        panelRead.add(button17);

        JButton button20 = new JButton("r-WI");
        button20.setBounds(414,59,72,31);
        button20.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button20.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadSDI( 8001, (short) 5 );
                if (read.IsSuccess)
                {
                    textArea1.setText("WI:" + SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Read Failed！" + read.Message );
                }
            }
        });
        panelRead.add(button20);

        JButton button6 = new JButton("r-SDO[1100X]");
        button6.setBounds(6,96,96,31);
        button6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button6.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadSDO( 11001, (short) 100 );
                if (read.IsSuccess)
                {
                    textArea1.setText("SDO[1100x]:" + SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Read Failed！" + read.Message );
                }
            }
        });
        panelRead.add(button6);

        JButton button9 = new JButton("r-RDI");
        button9.setBounds(108,96,71,31);
        button9.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button9.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadRDI( 1, (short) 10 );
                if (read.IsSuccess)
                {
                    textArea1.setText("RDI:" + SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog( null,"Read Failed！" + read.Message );
                }
            }
        });
        panelRead.add(button9);

        JButton button12 = new JButton("r-UO");
        button12.setBounds(185,96,71,31);
        button12.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button12.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadUO( 1, (short) 10 );
                if (read.IsSuccess)
                {
                    textArea1.setText("UO:" + SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog( null,"Read Failed！" + read.Message );
                }
            }
        });
        panelRead.add(button12);

        JButton button15 = new JButton("r-GO2");
        button15.setBounds(262,96,68,31);
        button15.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button15.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<short[]> read = fanucInterfaceNet.ReadGO( 10001, (short) 3 );
                if (read.IsSuccess)
                {
                    textArea1.setText("AO:" + SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Read Failed！" + read.Message );
                }
            }
        });
        panelRead.add(button15);

        JButton button18 = new JButton("r-AI");
        button18.setBounds(336,96,72,31);
        button18.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button18.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<short[]> read = fanucInterfaceNet.ReadGI( 1001, (short) 2 );
                if (read.IsSuccess)
                {
                    textArea1.setText("AI" + SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Read Failed！" + read.Message );
                }
            }
        });
        panelRead.add(button18);

        JButton button21 = new JButton("r-WSI");
        button21.setBounds(414,96,72,31);
        button21.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button21.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = fanucInterfaceNet.ReadSDI( 8401, (short) 1 );
                if (read.IsSuccess)
                {
                    textArea1.setText("WSI" + SoftBasic.ArrayFormat(read.Content));
                }
                else
                {
                    JOptionPane.showMessageDialog( null,  "Read Failed！" + read.Message );
                }
            }
        });

        panel.add(panelRead);
    }



    private void WriteSDO(int intStartIndex) {
        w_sdo++;

        boolean[] value = new boolean[100];
        if (w_sdo % 2 == 1) {
            for (int i = 0; i < value.length; i++) {
                value[i] = true;
            }
        }

        OperateResult write = fanucInterfaceNet.WriteSDO(intStartIndex, value);
        if (write.IsSuccess) {
            JOptionPane.showMessageDialog(null, "Write Success！value:" + value[0]);
        } else {
            JOptionPane.showMessageDialog(null, "Write Failed！" + write.Message);
        }
    }

    private int w_sdo = 0;
    private int w_sdi = 0;
    private int w_go = 0;

}
