package HslCommunicationDemo.PLC.Omron;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Omron.OmronConnectedCipNet;
import HslCommunication.Profinet.Omron.OmronFinsNet;
import HslCommunication.Profinet.Omron.OmronPlcType;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormOmronFins extends HslJPanel {
    public FormOmronFins(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Fins Tcp", tabbedPane, this));
        AddConnectSegment(this);
        omronFinsNet = new OmronFinsNet();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        finsControl = new OmronFinsControl();
        userControlReadWriteDevice.AddSpecialFunctionTab( finsControl, false,"Fins Function");

        addressExampleControl = new AddressExampleControl(DemoOmronHelper.GetOmronAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private OmronFinsNet omronFinsNet = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private OmronFinsControl finsControl = null;
    private TcpConnectControl tcpConnectControl = null;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice())
        {
            omronFinsNet.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "9600");

        JLabel label3 = new JLabel("PLC Unit：");
        label3.setBounds(5, TcpConnectControl.LocationTwoLine,77, 17);
        tcpConnectControl.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(85,TcpConnectControl.LocationTwoLine - 3,40, 23);
        textField3.setText("0");
        tcpConnectControl.add(textField3);

        JLabel label4 = new JLabel("SA1:");
        label4.setBounds(130, TcpConnectControl.LocationTwoLine,42, 17);
        tcpConnectControl.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(170,TcpConnectControl.LocationTwoLine - 3,45, 23);
        textField4.setText("");
        textField4.setEditable(false);
        tcpConnectControl.add(textField4);

        JLabel label5 = new JLabel("DA1:");
        label5.setBounds(230, TcpConnectControl.LocationTwoLine,44, 17);
        tcpConnectControl.add(label5);

        JTextField textField5 = new JTextField();
        textField5.setBounds(280,TcpConnectControl.LocationTwoLine - 3,45, 23);
        textField5.setText("");
        textField5.setEditable(false);
        tcpConnectControl.add(textField5);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(330,TcpConnectControl.LocationTwoLine - 3,80, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedIndex(2);
        tcpConnectControl.add(comboBox1);

        JComboBox<OmronPlcType> comboBox2 = new JComboBox<>();
        comboBox2.setBounds(420,TcpConnectControl.LocationTwoLine - 3,80, 25);
        comboBox2.addItem(OmronPlcType.CSCJ);
        comboBox2.addItem(OmronPlcType.CV);
        comboBox2.setSelectedIndex(0);
        tcpConnectControl.add(comboBox2);


        JCheckBox checkBox1 = new JCheckBox("String Reverse");
        checkBox1.setBounds(510,TcpConnectControl.LocationTwoLine - 1,140, 17);
        tcpConnectControl.add(checkBox1);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(omronFinsNet);
                    omronFinsNet.setDataFormat((DataFormat) comboBox1.getSelectedItem());
                    omronFinsNet.getByteTransform().setIsStringReverse(checkBox1.isSelected());
                    omronFinsNet.setPlcType((OmronPlcType)comboBox2.getSelectedItem());

                    OperateResult connect = omronFinsNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        textField4.setText(String.valueOf(omronFinsNet.SA1));
                        textField5.setText(String.valueOf(omronFinsNet.DA1));
                        userControlReadWriteDevice.SetReadWriteNet(omronFinsNet, defaultAddress, 10);
                        finsControl.SetOmronFins(omronFinsNet);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( omronFinsNet, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "plc.setDataFormat(DataFormat." + (DataFormat) comboBox1.getSelectedItem() + ");\r\n" );
                    stringBuilder.append( "plc.getByteTransform().setIsStringReverse(" + checkBox1.isSelected() + ");\r\n" );
                    stringBuilder.append( "plc.setPlcType(OmronPlcType." + (OmronPlcType)comboBox2.getSelectedItem() + ");\r\n" );
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
