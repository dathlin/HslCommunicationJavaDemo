package HslCommunicationDemo.PLC.YASKAWA;

import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.YASKAWA.MemobusTcpNet;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormYASKAWAMemobusTcpNet extends JPanel {

    public FormYASKAWAMemobusTcpNet(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Memobus Tcp", tabbedPane, this));
        AddConnectSegment(this);
        plc = new MemobusTcpNet();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(YASKAWAHelper.GetMemobusAddress());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private MemobusTcpNet plc = null;
    private String defaultAddress = "100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;


    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JTextField textField1 = DemoUtils.CreateIpAddressTextBox(panelConnect);
        JTextField textField2 = DemoUtils.CreateIpPortTextBox(panelConnect, "9999");

        JLabel label3 = new JLabel("Cpu From：");
        label3.setBounds(400, 5,80, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(480,4,50, 23);
        textField3.setText("1");
        panelConnect.add(textField3);

        JLabel label4 = new JLabel("Cpu To：");
        label4.setBounds(400, 30,80, 17);
        panelConnect.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(480,29,50, 23);
        textField4.setText("2");
        panelConnect.add(textField4);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(550,14,80, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedItem(DataFormat.CDAB);
        panelConnect.add(comboBox1);


        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(784,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(677,11,91, 28);
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
                    plc.setCpuFrom((byte) Integer.parseInt(textField3.getText()));
                    plc.setCpuTo((byte) Integer.parseInt(textField4.getText()));
                    plc.getByteTransform().setDataFormat((DataFormat)comboBox1.getSelectedItem());

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
