package HslCommunicationDemo.PLC.Delta;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Delta.DeltaSerialOverTcp;
import HslCommunication.Profinet.Delta.DeltaSeries;
import HslCommunication.Profinet.Delta.DeltaTcpNet;
import HslCommunication.Profinet.Melsec.MelsecA1ENet;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormDeltaSerialOverTcp extends HslJPanel {

    public FormDeltaSerialOverTcp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Modbus Rtu Over Tcp", tabbedPane, this));
        AddConnectSegment(this);
        plc = new DeltaSerialOverTcp();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(DeltaHelper.GetDeviceAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private DeltaSerialOverTcp plc = null;
    private String defaultAddress = "M100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private TcpConnectControl  tcpConnectControl = null;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            plc.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "502");

        JLabel label4 = new JLabel("Station：");
        label4.setBounds(5, TcpConnectControl.LocationTwoLine,58, 17);
        tcpConnectControl.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(62,TcpConnectControl.LocationTwoLine - 3,53, 23);
        textField4.setText("1");
        tcpConnectControl.add(textField4);


        JLabel label3 = new JLabel("Series：");
        label3.setBounds(120, TcpConnectControl.LocationTwoLine,56, 17);
        tcpConnectControl.add(label3);

        JComboBox<DeltaSeries> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(180,TcpConnectControl.LocationTwoLine - 4,80, 25);
        comboBox1.addItem(DeltaSeries.Dvp);
        comboBox1.addItem(DeltaSeries.AS);
        comboBox1.setSelectedItem(0);
        tcpConnectControl.add(comboBox1);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(plc);
                    plc.setStation(Byte.parseByte(textField4.getText()));
                    plc.SetSeries((DeltaSeries) comboBox1.getSelectedItem());

                    OperateResult connect = plc.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(plc, defaultAddress, 1);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }


                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( plc, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "plc.setStation(Byte.parseByte(\"" + textField4.getText() + "\"));\r\n" );
                    stringBuilder.append( "plc.SetSeries( DeltaSeries." + (DeltaSeries) comboBox1.getSelectedItem() + ");\r\n" );
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
