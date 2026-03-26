package HslCommunicationDemo.PLC.Keyence;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Keyence.KeyenceKvOldOverTcp;
import HslCommunication.Profinet.Keyence.KeyenceNanoSerialOverTcp;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.HslJPanel;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormKeyenceKvOldOverTcp  extends HslJPanel {

    public FormKeyenceKvOldOverTcp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("KvOld Over Tcp", tabbedPane, this));
        AddConnectSegment(this);
        plc = new KeyenceKvOldOverTcp();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(KeyenceHelper.GetKeyenceKvOld());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());

    }

    private AddressExampleControl addressExampleControl;
    private KeyenceKvOldOverTcp plc = null;
    private String defaultAddress = "DM100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private JButton button_connect;
    private JButton button_disconnect;

    @Override
    public void OnClose() {
        super.OnClose();
        if (button_connect == null || button_disconnect == null) return;
        if (button_disconnect.isEnabled()){
            plc.ConnectClose();
        }
    }


    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 12,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,9,106, 23);
        textField1.setText("127.0.0.1");
        panelConnect.add(textField1);

        JLabel label2 = new JLabel("Port：");
        label2.setBounds(184, 12,56, 17);
        panelConnect.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(238,9,61, 23);
        textField2.setText("8501");
        panelConnect.add(textField2);

        JLabel label3 = new JLabel("适用于 Kv-10xx, 16xx, 24xx, 40xx, kv-80, kv-300，期中 xx 表示 AR/AT/DR/DT");
        label3.setBounds(8, 35,500, 17);
        panelConnect.add(label3);
//
//        JLabel label3 = new JLabel("Station：");
//        label3.setBounds(338, 17,56, 17);
//        panelConnect.add(label3);
//
//        JTextField textField3 = new JTextField();
//        textField3.setBounds(392,14,40, 23);
//        textField3.setText("0");
//        panelConnect.add(textField3);
//
//        JCheckBox checkBox1 = new JCheckBox("Use Station?");
//        checkBox1.setBounds(447,16,106, 21);
//        checkBox1.setSelected(false);
//        panelConnect.add(checkBox1);

        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(684,11,121, 28);
        button_disconnect = button2;
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(577,11,91, 28);
        button_connect = button1;
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
                    //plc.Station = (byte) Integer.parseInt(textField3.getText());
                    //plc.UseStation = checkBox1.isSelected();

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
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }


                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( KeyenceNanoSerialOverTcp.class, textField1.getText(), textField2.getText() );
                    userControlReadWriteDevice.SetDeviceCode( stringBuilder.toString() );
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
                }
            }
        });


        panel.add(panelConnect);
    }
}
