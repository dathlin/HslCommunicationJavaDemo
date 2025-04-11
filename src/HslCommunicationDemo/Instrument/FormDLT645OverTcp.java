package HslCommunicationDemo.Instrument;

import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Instrument.DLT.DLT645OverTcp;
import HslCommunication.Profinet.Omron.OmronFinsNet;
import HslCommunication.Profinet.Omron.OmronPlcType;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.PLC.Omron.DemoOmronHelper;
import HslCommunicationDemo.PLC.Omron.OmronFinsControl;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormDLT645OverTcp extends JPanel {
    public FormDLT645OverTcp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("DLT 645 OverTcp", tabbedPane, this));
        AddConnectSegment(this);
        dlt = new DLT645OverTcp();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        dlt645Control = new Dlt645Control();
        userControlReadWriteDevice.AddSpecialFunctionTab( dlt645Control, false,"DLT Function");

        addressExampleControl = new AddressExampleControl(DemoDltHelper.GetDlt645Address());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private DLT645OverTcp dlt = null;
    private String defaultAddress = "02-01-01-00";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private Dlt645Control dlt645Control = null;

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 5,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,2,150, 23);
        textField1.setText("127.0.0.1");
        panelConnect.add(textField1);

        JLabel label2 = new JLabel("Port：");
        label2.setBounds(220, 5,60, 17);
        panelConnect.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(280,2,61, 23);
        textField2.setText("2000");
        panelConnect.add(textField2);

        JLabel label3 = new JLabel("Password:");
        label3.setBounds(8, 34,80, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(90,31,150, 23);
        textField3.setText("");
        panelConnect.add(textField3);

        JLabel label4 = new JLabel("Op Code:");
        label4.setBounds(250, 34,60, 17);
        panelConnect.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(320,31,150, 23);
        textField4.setText("");
        panelConnect.add(textField4);

            JLabel label5 = new JLabel("Station:");
        label5.setBounds(480, 34,150, 17);
        panelConnect.add(label5);

        JTextField textField5 = new JTextField();
        textField5.setBounds(530,31,120, 23);
        textField5.setText("1");
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
                    dlt.setIpAddress(textField1.getText());
                    dlt.setPort(Integer.parseInt(textField2.getText()));
                    dlt.setPassword(textField3.getText());
                    dlt.setOpCode(textField4.getText());
                    dlt.setStation(textField5.getText());

                    OperateResult connect = dlt.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
                        userControlReadWriteDevice.SetReadWriteNet(dlt, defaultAddress, 10);
                        dlt645Control.SetReadWritePlc(dlt);
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
                if(dlt!=null){
                    dlt.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }
}
