package HslCommunicationDemo.PLC.XinJE;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Siemens.SiemensFetchWriteNet;
import HslCommunication.Profinet.XINJE.XinJESeries;
import HslCommunication.Profinet.XINJE.XinJETcpNet;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormXINJETcp extends HslJPanel {

    public FormXINJETcp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Modbus Tcp", tabbedPane, this));
        AddConnectSegment(this);
        plc = new XinJETcpNet();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(XinJEHelper.GetXinJEAddress());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private XinJETcpNet plc = null;
    private String defaultAddress = "D100";
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
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "502");
        JLabel label3 = new JLabel("Station：");
        label3.setBounds(5, TcpConnectControl.LocationTwoLine,56, 17);
        tcpConnectControl.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(65,TcpConnectControl.LocationTwoLine - 3,40, 23);
        textField3.setText("1");
        tcpConnectControl.add(textField3);

        JCheckBox checkBox1 = new JCheckBox("Start from 0?");
        checkBox1.setBounds(110,TcpConnectControl.LocationTwoLine - 1,106, 21);
        checkBox1.setSelected(true);
        tcpConnectControl.add(checkBox1);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(220,TcpConnectControl.LocationTwoLine - 3,61, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedItem(0);
        tcpConnectControl.add(comboBox1);

        JLabel label4 = new JLabel("Series:");
        label4.setBounds(290,TcpConnectControl.LocationTwoLine,40, 23);
        tcpConnectControl.add(label4);

        JComboBox<XinJESeries> comboBox_Series = new JComboBox<>();
        comboBox_Series.setBounds(340,TcpConnectControl.LocationTwoLine - 3,61, 25);
        comboBox_Series.addItem(XinJESeries.XC);
        comboBox_Series.addItem(XinJESeries.XD);
        comboBox_Series.addItem(XinJESeries.XL);
        comboBox_Series.setSelectedItem(0);
        tcpConnectControl.add(comboBox_Series);
        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(plc);
                    plc.setStation( (byte) Integer.parseInt( textField3.getText() ) );
                    plc.setAddressStartWithZero(checkBox1.isSelected());
                    plc.setDataFormat((DataFormat) comboBox1.getSelectedItem());
                    plc.Series = (XinJESeries) comboBox_Series.getSelectedItem();

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
                    stringBuilder.append( "plc.setStation( (byte) Integer.parseInt( \"" + textField3.getText() + "\" ) );\r\n" );
                    stringBuilder.append( "plc.setAddressStartWithZero(" + checkBox1.isSelected() + ");\r\n" );
                    stringBuilder.append( "plc.setDataFormat(DataFormat." + (DataFormat) comboBox1.getSelectedItem() + ");\r\n" );
                    stringBuilder.append( "plc.Series = XinJESeries." + (XinJESeries) comboBox_Series.getSelectedItem() + ";\r\n" );
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
