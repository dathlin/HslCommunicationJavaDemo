package HslCommunicationDemo.PLC.Melsec;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Melsec.MelsecA1EAsciiNet;
import HslCommunication.Profinet.Melsec.MelsecA3CNetOverTcp;
import HslCommunication.Profinet.Melsec.MelsecFxLinksOverTcp;
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

public class FormMelsecA3CNetOverTcp extends HslJPanel {
    public FormMelsecA3CNetOverTcp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("MelsecA3CNetOverTcp", tabbedPane, this));
        AddConnectSegment(this);
        melsec = new MelsecA3CNetOverTcp();

        fxLinksControl = AddFunction();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(fxLinksControl, false, "FxLinksFunction");
        userControlReadWriteDevice.setEnabled(false);
        DemoUtils.SetPanelEnabled(fxLinksControl, false);

        addressExampleControl = new AddressExampleControl(DemoMelsecHelper.GetMcAddress());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private MelsecA3CNetOverTcp melsec = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private JPanel fxLinksControl;
    private TcpConnectControl  tcpConnectControl;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            melsec.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "2000");
        JLabel label3 = new JLabel("Station：");
        label3.setBounds(5, TcpConnectControl.LocationTwoLine,65, 17);
        tcpConnectControl.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(70,TcpConnectControl.LocationTwoLine - 3,50, 23);
        textField3.setText("0");
        tcpConnectControl.add(textField3);

        JCheckBox checkBox1 = new JCheckBox("SumCheck?");
        checkBox1.setBounds(130, TcpConnectControl.LocationTwoLine - 1, 100, 21);
        checkBox1.setSelected(true);
        tcpConnectControl.add(checkBox1);

        JCheckBox checkBox2 = new JCheckBox("EnableWriteBitToWordRegister?");
        checkBox2.setBounds(230, TcpConnectControl.LocationTwoLine - 1, 250, 21);
        checkBox2.setSelected(false);
        tcpConnectControl.add(checkBox2);

        JLabel label5 = new JLabel("Format：");
        label5.setBounds(480, TcpConnectControl.LocationTwoLine,70, 17);
        tcpConnectControl.add(label5);


        JComboBox<Integer> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(560,TcpConnectControl.LocationTwoLine - 4,60, 25);
        comboBox1.addItem(1);
        comboBox1.addItem(2);
        comboBox1.addItem(3);
        comboBox1.addItem(4);
        comboBox1.setSelectedItem(1);
        tcpConnectControl.add(comboBox1);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(melsec);
                    melsec.Station = Byte.parseByte(textField3.getText());
                    melsec.SumCheck = checkBox1.isSelected();
                    melsec.EnableWriteBitToWordRegister = checkBox2.isSelected();
                    melsec.Format = (int)comboBox1.getSelectedItem();

                    OperateResult connect = melsec.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(melsec, defaultAddress, 10);
                        DemoUtils.SetPanelEnabled(fxLinksControl, true);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }


                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( melsec, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "plc.Station = Byte.parseByte(\"" + textField3.getText() + "\");\r\n");
                    stringBuilder.append( "plc.SumCheck = " + melsec.SumCheck + ";\r\n");
                    stringBuilder.append( "plc.EnableWriteBitToWordRegister = " + checkBox2.isSelected() + ";\r\n" );
                    stringBuilder.append( "plc.Format = " + melsec.Format + ";\r\n");
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
                if(melsec !=null){
                    melsec.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                    DemoUtils.SetPanelEnabled(fxLinksControl, false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }

    public JPanel AddFunction(){
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton button_start = new JButton("Run PLC");
        button_start.setBounds(10, 5, 120, 28);
        button_start.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResult op = melsec.RemoteRun();
                if (op.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Run Success",
                            "Result",
                            JOptionPane.PLAIN_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Run failed: " + op.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(button_start);

        JButton button_stop = new JButton("Stop PLC");
        button_stop.setBounds(140, 5, 120, 28);
        button_stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResult op = melsec.RemoteStop();
                if (op.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Stop Success",
                            "Result",
                            JOptionPane.PLAIN_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Stop failed: " + op.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(button_stop);

        JButton button_read_type = new JButton("PLC Type");
        button_read_type.setBounds(270, 5, 120, 28);
        button_read_type.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<String> op = melsec.ReadPlcType();
                if (op.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Success: " + op.Content,
                            "Result",
                            JOptionPane.PLAIN_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read failed: " + op.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(button_read_type);

        return panel;
    }
}
