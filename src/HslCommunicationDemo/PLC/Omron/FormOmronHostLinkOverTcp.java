package HslCommunicationDemo.PLC.Omron;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Melsec.MelsecMcUdp;
import HslCommunication.Profinet.Omron.OmronFinsNet;
import HslCommunication.Profinet.Omron.OmronHostLinkOverTcp;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormOmronHostLinkOverTcp extends HslJPanel {

    public FormOmronHostLinkOverTcp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Fins HostLink OverTcp", tabbedPane, this));
        AddConnectSegment(this);
        omronFinsNet = new OmronHostLinkOverTcp();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(DemoOmronHelper.GetHostlinkAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private OmronHostLinkOverTcp omronFinsNet = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private TcpConnectControl  tcpConnectControl = null;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            omronFinsNet.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "2000");

        JLabel label3 = new JLabel("Station：");
        label3.setBounds(5, TcpConnectControl.LocationTwoLine,77, 17);
        tcpConnectControl.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(85,TcpConnectControl.LocationTwoLine - 3,40, 23);
        textField3.setText("0");
        tcpConnectControl.add(textField3);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(130,TcpConnectControl.LocationTwoLine - 3,80, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedItem(DataFormat.CDAB);
        tcpConnectControl.add(comboBox1);

        JLabel label4 = new JLabel("SA2:");
        label4.setBounds(220, TcpConnectControl.LocationTwoLine,42, 17);
        tcpConnectControl.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(270,TcpConnectControl.LocationTwoLine - 3,45, 23);
        textField4.setText("0");
        tcpConnectControl.add(textField4);

        JLabel label5 = new JLabel("DA2:");
        label5.setBounds(330, TcpConnectControl.LocationTwoLine,44, 17);
        tcpConnectControl.add(label5);

        JTextField textField5 = new JTextField();
        textField5.setBounds(380,TcpConnectControl.LocationTwoLine - 3,45, 23);
        textField5.setText("0");
        tcpConnectControl.add(textField5);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(omronFinsNet);
                    omronFinsNet.UnitNumber = (byte) Integer.parseInt(textField3.getText());
                    omronFinsNet.SA2 = (byte) Integer.parseInt(textField4.getText());
                    omronFinsNet.DA2 = (byte) Integer.parseInt(textField5.getText());
                    omronFinsNet.getByteTransform().setDataFormat((DataFormat) comboBox1.getSelectedItem());

                    OperateResult connect = omronFinsNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(omronFinsNet, defaultAddress, 10);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }


                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( omronFinsNet, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "plc.UnitNumber = (byte) Integer.parseInt(\"" + textField3.getText() + "\");\r\n" );
                    stringBuilder.append( "plc.SA2 = (byte) Integer.parseInt(\"" + textField4.getText() + "\");\r\n" );
                    stringBuilder.append( "plc.DA2 = (byte) Integer.parseInt(\"" + textField5.getText() + "\");\r\n" );
                    stringBuilder.append( "plc.getByteTransform().setDataFormat(DataFormat." + (DataFormat) comboBox1.getSelectedItem() + ");\r\n" );
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
                if(omronFinsNet!=null){
                    omronFinsNet.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }
}
