package HslCommunicationDemo.PLC.Fatek;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.FATEK.FatekProgramOverTcp;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;
import HslCommunicationDemo.UserControlReadWriteOp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormFatekProgramOverTcp extends JPanel {

    public FormFatekProgramOverTcp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Fatek Program OverTcp", tabbedPane, this));
        AddConnectSegment(this);
        plc = new FatekProgramOverTcp();

        fatekFunction = AddFunction();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(fatekFunction, false, "FatekFunction");
        userControlReadWriteDevice.setEnabled(false);
        DemoUtils.SetPanelEnabled(fatekFunction, false);
    }

    private FatekProgramOverTcp plc = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private JPanel fatekFunction;

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
        textField2.setText("2000");
        panelConnect.add(textField2);

        JLabel label4 = new JLabel("Station：");
        label4.setBounds(333, 17,58, 17);
        panelConnect.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(389,14,53, 23);
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
                    plc.setIpAddress(textField1.getText());
                    plc.setPort(Integer.parseInt(textField2.getText()));
                    plc.setStation(Byte.parseByte(textField4.getText()));

                    OperateResult connect = plc.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
                        userControlReadWriteDevice.SetReadWriteNet(plc, defaultAddress, 1);
                        DemoUtils.SetPanelEnabled(fatekFunction, true);
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
                if(plc !=null){
                    plc.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                    DemoUtils.SetPanelEnabled(fatekFunction, false);
                }
            }
        });


        panel.add(panelConnect);
    }

    public JPanel AddFunction(){
        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);

        JButton button_run = new JButton("Run");
        button_run.setFocusPainted(false);
        button_run.setBounds(24,24,82, 28);
        button_run.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button_run.isEnabled()) return;
                super.mouseClicked(e);
                OperateResult read = plc.Run();
                if(read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Run Success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Run Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button_run);


        JButton button_stop = new JButton("Stop");
        button_stop.setFocusPainted(false);
        button_stop.setBounds(130,24,82, 28);
        button_stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button_stop.isEnabled()) return;
                super.mouseClicked(e);
                OperateResult read = plc.Stop();
                if(read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Stop Success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Stop Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button_stop);


        JButton button_status = new JButton("Status");
        button_status.setFocusPainted(false);
        button_status.setBounds(24,62,82, 28);
        button_status.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button_status.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = plc.ReadStatus();
                if(read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            read.Content[0] ? "Run" : "Stop",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Stop Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button_status);



        return panelRead;
    }
}
