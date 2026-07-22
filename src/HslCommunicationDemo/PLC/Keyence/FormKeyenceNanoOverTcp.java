package HslCommunicationDemo.PLC.Keyence;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Keyence.KeyenceMcNet;
import HslCommunication.Profinet.Keyence.KeyenceNanoSerialOverTcp;
import HslCommunication.Profinet.Keyence.KeyencePLCS;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormKeyenceNanoOverTcp extends HslJPanel {

    public FormKeyenceNanoOverTcp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Nano Over Tcp", tabbedPane, this));
        AddConnectSegment(this);
        plc = new KeyenceNanoSerialOverTcp();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(KeyenceHelper.GetKeyenceKvAddress());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());

        nanoControl = new KeyenceNanoControl();
        userControlReadWriteDevice.AddSpecialFunctionTab(nanoControl, false, "Nano Function");
    }

    private AddressExampleControl addressExampleControl;
    private KeyenceNanoSerialOverTcp plc = null;
    private KeyenceNanoControl nanoControl = null;
    private String defaultAddress = "DM100";
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
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "8501");
        JLabel label3 = new JLabel("Station：");
        label3.setBounds(5, TcpConnectControl.LocationTwoLine,56, 17);
        tcpConnectControl.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(65,TcpConnectControl.LocationTwoLine - 3,40, 23);
        textField3.setText("0");
        tcpConnectControl.add(textField3);

        JCheckBox checkBox1 = new JCheckBox("Use Station?");
        checkBox1.setBounds(110,TcpConnectControl.LocationTwoLine - 1,106, 21);
        checkBox1.setSelected(false);
        tcpConnectControl.add(checkBox1);
        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(plc);
                    plc.Station = (byte) Integer.parseInt(textField3.getText());
                    plc.UseStation = checkBox1.isSelected();

                    OperateResult connect = plc.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(plc, defaultAddress, 10);
                        nanoControl.SetPlc(plc);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }


                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( plc, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "plc.Station = (byte) Integer.parseInt(\"" + textField3.getText() + "\");\r\n" );
                    stringBuilder.append( "plc.UseStation = " + checkBox1.isSelected() + ";\r\n");
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
