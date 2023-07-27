package HslCommunicationDemo.PLC.AllenBradley;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.AllenBradley.AllenBradleyNet;
import HslCommunication.Profinet.AllenBradley.MessageRouter;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunication.Utilities;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.PLC.Omron.OmronCipControl;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;
import HslCommunicationDemo.UserControlReadWriteOp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class FormABCip extends JPanel
{

    public FormABCip(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Cip", tabbedPane, this));
        AddConnectSegment(this);
        allenBradleyNet = new AllenBradleyNet();

        cipControl = new OmronCipControl();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(cipControl, false, "CipFunction");
        userControlReadWriteDevice.setEnabled(false);
        cipControl.setEnabled(false);
    }
    private AllenBradleyNet allenBradleyNet = null;
    private String defaultAddress = "A";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private OmronCipControl cipControl;

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 17,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(42,14,126, 23);
        textField1.setText("192.168.0.10");
        panelConnect.add(textField1);

        JLabel label2 = new JLabel("Port：");
        label2.setBounds(184, 17,56, 17);
        panelConnect.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(238,14,61, 23);
        textField2.setText("44818");
        panelConnect.add(textField2);

        JLabel label4 = new JLabel("Slot：");
        label4.setBounds(313, 17,48, 17);
        panelConnect.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(359,14,43, 23);
        textField4.setText("0");
        panelConnect.add(textField4);

        JLabel label_router = new JLabel("Router");
        label_router.setBounds(420, 17,47, 17);
        panelConnect.add(label_router);

        JTextField textField_router = new JTextField();
        textField_router.setBounds(470,14,150, 23);
        textField_router.setText("");
        panelConnect.add(textField_router);

        JLabel label_router_tip = new JLabel("if use router, example: 1.15.2.18.1.12");
        label_router_tip.setBounds(401, 36,220, 17);
        panelConnect.add(label_router_tip);


        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(744,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(637,11,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    allenBradleyNet.setIpAddress(textField1.getText());
                    allenBradleyNet.setPort(Integer.parseInt(textField2.getText()));
                    allenBradleyNet.setSlot(Byte.parseByte(textField4.getText()));

                    if(!Utilities.IsStringNullOrEmpty(textField_router.getText())){
                        allenBradleyNet.setMessageRouter( new MessageRouter(textField_router.getText()));
                    }

                    OperateResult connect = allenBradleyNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
                        userControlReadWriteDevice.SetReadWriteNet(allenBradleyNet, defaultAddress, 1);
                        cipControl.setEnabled(true);
                        cipControl.SetReadWriteCip(allenBradleyNet);
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
                if(allenBradleyNet !=null){
                    allenBradleyNet.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                    cipControl.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }

}
