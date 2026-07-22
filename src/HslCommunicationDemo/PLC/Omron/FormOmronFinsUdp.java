package HslCommunicationDemo.PLC.Omron;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Melsec.MelsecMcUdp;
import HslCommunication.Profinet.Omron.OmronFinsNet;
import HslCommunication.Profinet.Omron.OmronFinsUdp;
import HslCommunication.Profinet.Omron.OmronPlcType;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormOmronFinsUdp extends HslJPanel {
    public FormOmronFinsUdp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Fins Udp", tabbedPane, this));
        AddConnectSegment(this);
        omronFinsNet = new OmronFinsUdp();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        finsControl = new OmronFinsControl();
        userControlReadWriteDevice.AddSpecialFunctionTab( finsControl, false,"Fins Function");

        addressExampleControl = new AddressExampleControl(DemoOmronHelper.GetOmronAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private OmronFinsUdp omronFinsNet = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private OmronFinsControl finsControl = null;
    private TcpConnectControl tcpConnectControl = null;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            omronFinsNet.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "9600" );
        JLabel label4 = new JLabel("PC Net Num：");
        label4.setBounds(5, TcpConnectControl.LocationTwoLine,100, 17);
        tcpConnectControl.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(110,TcpConnectControl.LocationTwoLine - 3,40, 23);
        textField4.setText("192");
        tcpConnectControl.add(textField4);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(160,TcpConnectControl.LocationTwoLine - 3,80, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedIndex(2);
        tcpConnectControl.add(comboBox1);


        JComboBox<OmronPlcType> comboBox2 = new JComboBox<>();
        comboBox2.setBounds(250,TcpConnectControl.LocationTwoLine - 3,80, 25);
        comboBox2.addItem(OmronPlcType.CSCJ);
        comboBox2.addItem(OmronPlcType.CV);
        comboBox2.setSelectedIndex(0);
        tcpConnectControl.add(comboBox2);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(omronFinsNet);
                    omronFinsNet.SA1 = (byte) Integer.parseInt(textField4.getText());
                    omronFinsNet.getByteTransform().setDataFormat((DataFormat) comboBox1.getSelectedItem());
                    omronFinsNet.setPlcType((OmronPlcType)comboBox2.getSelectedItem());
                    tcpConnectControl.SetConnectSuccess();
                    userControlReadWriteDevice.SetReadWriteNet(omronFinsNet, defaultAddress, 10);
                    finsControl.SetOmronFins( omronFinsNet );


                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( omronFinsNet, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "plc.SA1 = (byte) Integer.parseInt(\"" + textField4.getText() + "\");\r\n" );
                    stringBuilder.append( "plc.getByteTransform().setDataFormat(DataFormat." + (DataFormat) comboBox1.getSelectedItem() + ");\r\n" );
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
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }
}
