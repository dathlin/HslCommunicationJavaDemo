package HslCommunicationDemo.PLC.Melsec;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Melsec.MelsecMcAsciiUdp;
import HslCommunication.Profinet.Melsec.MelsecMcUdp;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;
import HslCommunicationDemo.UserControlReadWriteOp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormMelsecUdpAscii extends JPanel {
    public FormMelsecUdpAscii(JTabbedPane tabbedPane){
        setLayout(null);

        add( new UserControlReadWriteHead("Qna-3E Udp Ascii", tabbedPane, this));
        AddConnectSegment(this);

        melsecMcNet = new MelsecMcAsciiUdp();
        melsecMcControl = new MelsecMcControl();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(melsecMcControl, false, "MelsecFunction");
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(DemoMelsecHelper.GetMcAddress());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private MelsecMcAsciiUdp melsecMcNet = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private MelsecMcControl melsecMcControl;

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect =  DemoUtils.CreateConnectPanel(panel);

        JTextField textField1 = DemoUtils.CreateIpAddressTextBox(panelConnect);
        JTextField textField2 = DemoUtils.CreateIpPortTextBox(panelConnect, "6000");

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
                    melsecMcNet.setIpAddress(textField1.getText());
                    melsecMcNet.setPort(Integer.parseInt(textField2.getText()));

                    button2.setEnabled(true);
                    button1.setEnabled(false);
                    melsecMcControl.setEnabled(true);
                    userControlReadWriteDevice.SetReadWriteNet(melsecMcNet, defaultAddress, 10);
                    melsecMcControl.SetReadWritePlc(melsecMcNet);
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
                if(melsecMcNet!=null){
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                    melsecMcControl.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }

}
