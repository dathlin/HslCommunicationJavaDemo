package HslCommunicationDemo.PLC.YASKAWA;

import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Siemens.SiemensFetchWriteNet;
import HslCommunication.Profinet.YASKAWA.MemobusTcpNet;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.HslJPanel;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormYASKAWAMemobusTcpNet extends HslJPanel {

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
    private TcpConnectControl tcpConnectControl = null;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            plc.ConnectClose();
        }
    }


    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "9999");
        JLabel label3 = new JLabel("Cpu From：");
        label3.setBounds(5, TcpConnectControl.LocationTwoLine,80, 17);
        tcpConnectControl.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(90,TcpConnectControl.LocationTwoLine - 3,50, 23);
        textField3.setText("1");
        tcpConnectControl.add(textField3);

        JLabel label4 = new JLabel("Cpu To：");
        label4.setBounds(145, TcpConnectControl.LocationTwoLine,80, 17);
        tcpConnectControl.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(230,TcpConnectControl.LocationTwoLine - 3,50, 23);
        textField4.setText("2");
        tcpConnectControl.add(textField4);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(290,TcpConnectControl.LocationTwoLine - 3,80, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedItem(DataFormat.CDAB);
        tcpConnectControl.add(comboBox1);
        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(plc);
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
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(plc, defaultAddress, 10);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }


                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( plc, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "plc.setCpuFrom((byte) Integer.parseInt(\"" + textField3.getText() + "\"));\r\n" );
                    stringBuilder.append( "plc.setCpuTo((byte) Integer.parseInt(\"" + textField4.getText() + "\"));\r\n" );
                    stringBuilder.append( "plc.getByteTransform().setDataFormat(DataFormat." + (DataFormat)comboBox1.getSelectedItem() + ");\r\n" );
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
        tcpConnectControl.ButtonDisconnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!tcpConnectControl.ButtonDisconnect.isEnabled()) return;
                if(plc !=null){
                    plc.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }
}
