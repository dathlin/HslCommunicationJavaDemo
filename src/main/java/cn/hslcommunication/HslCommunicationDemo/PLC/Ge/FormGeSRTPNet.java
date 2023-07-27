package cn.hslcommunication.HslCommunicationDemo.PLC.Ge;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.GE.GeSRTPNet;
import cn.hslcommunication.HslCommunicationDemo.DemoUtils;
import cn.hslcommunication.HslCommunicationDemo.UserControlReadWriteDevice;
import cn.hslcommunication.HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class FormGeSRTPNet extends JPanel {

    public FormGeSRTPNet(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("SRTP", tabbedPane, this));
        AddConnectSegment(this);
        plc = new GeSRTPNet();

        geControl = AddGeControl( );
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(geControl, false, "GeControl");
        userControlReadWriteDevice.setEnabled(false);
        DemoUtils.SetPanelEnabled(geControl, false);
    }

    private GeSRTPNet plc = null;
    private String defaultAddress = "R1";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private JPanel geControl;


    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 17,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,14,106, 23);
        textField1.setText("127.0.0.1");
        panelConnect.add(textField1);

        JLabel label2 = new JLabel("Port：");
        label2.setBounds(184, 17,56, 17);
        panelConnect.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(238,14,61, 23);
        textField2.setText("18245");
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
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    plc.setIpAddress(textField1.getText());
                    plc.setPort(Integer.parseInt(textField2.getText()));

                    OperateResult connect = plc.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
                        userControlReadWriteDevice.SetReadWriteNet(plc, defaultAddress, 10);
                        DemoUtils.SetPanelEnabled(geControl, true);
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
                if(plc !=null){
                    plc.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                    DemoUtils.SetPanelEnabled(geControl, false);
                }
            }
        });


        panel.add(panelConnect);
    }

    public JPanel AddGeControl( ){
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JTextArea textArea_data = new JTextArea();
        textArea_data.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea_data);
        jsp.setBounds(5, 45, 400, 80);
        panel.add(jsp);


        JButton button_read_time = new JButton("PLC Time");
        button_read_time.setBounds(5, 10, 120, 28);
        button_read_time.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!button_read_time.isEnabled()) return;

                OperateResultExOne<Date> read = plc.ReadPLCTime();
                if (read.IsSuccess){
                    textArea_data.setText( DemoUtils.FormatterDateTime.format(read.Content) );
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
        panel.add(button_read_time);

        JButton button_read_program = new JButton("ProgramName");
        button_read_program.setBounds(135, 10, 120, 28);
        button_read_program.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!button_read_program.isEnabled()) return;

                OperateResultExOne<String> read = plc.ReadProgramName();
                if (read.IsSuccess) {
                    textArea_data.setText(read.Content);
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(button_read_program);

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                jsp.setBounds(5, 45, panel.getWidth() - 10, panel.getHeight() - 50);
                jsp.updateUI();
            }
        });

        return panel;
    }
}
