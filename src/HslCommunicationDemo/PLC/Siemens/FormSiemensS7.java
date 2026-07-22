package HslCommunicationDemo.PLC.Siemens;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Pipe.PipeBase;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Siemens.SiemensFetchWriteNet;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class FormSiemensS7 extends HslJPanel
{

    public FormSiemensS7(JTabbedPane tabbedPane, SiemensPLCS siemensPLCS ){
        this.siemensPLCS = siemensPLCS;
        setLayout(null);
        add( new UserControlReadWriteHead("s7-" + siemensPLCS.toString(), tabbedPane, this));
        AddConnectSegment(this);
        siemensS7Net = new SiemensS7Net(siemensPLCS);

        siemensS7Control = new SiemensS7Control();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(siemensS7Control, false, "S7Function");
        userControlReadWriteDevice.setEnabled(false);
        siemensS7Control.setEnabled(false);

        writeControl = new SiemensS7WriteControl();
        userControlReadWriteDevice.AddSpecialFunctionTab(writeControl, false, "S7WriteFunction");
        writeControl.setEnabled(false);

        addressExampleControl = new AddressExampleControl(SiemensHelper.GetSiemensS7Address());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private SiemensPLCS siemensPLCS = SiemensPLCS.S1200;
    private SiemensS7Net siemensS7Net = null;
    private String defaultAddress = "M100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private SiemensS7Control siemensS7Control;
    private SiemensS7WriteControl writeControl;
    private TcpConnectControl  tcpConnectControl;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            siemensS7Net.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "102");
        JLabel label3 = new JLabel("Rack：");
        label3.setBounds(5, TcpConnectControl.LocationTwoLine,48, 17);
        tcpConnectControl.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(55,TcpConnectControl.LocationTwoLine - 3,50, 23);
        textField3.setText("0");
        tcpConnectControl.add(textField3);

        JLabel label4 = new JLabel("Slot：");
        label4.setBounds(110, TcpConnectControl.LocationTwoLine,48, 17);
        tcpConnectControl.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(165,TcpConnectControl.LocationTwoLine - 3,50, 23);
        textField4.setText("0");
        tcpConnectControl.add(textField4);

        JLabel label6 = new JLabel("pdu：");
        label6.setBounds(220, TcpConnectControl.LocationTwoLine,48, 17);
        tcpConnectControl.add(label6);

        JTextField textField6 = new JTextField();
        textField6.setBounds(270,TcpConnectControl.LocationTwoLine - 3,40, 23);
        textField6.setText("0");
        textField6.setEnabled(false);
        tcpConnectControl.add(textField6);


        if (this.siemensPLCS == SiemensPLCS.S400)
        {
            textField3.setText("0");
            textField4.setText("3");
        }
        else if(this.siemensPLCS == SiemensPLCS.S1200)
        {
            textField3.setText("0");
            textField4.setText("0");
        }
        else if (this.siemensPLCS == SiemensPLCS.S300)
        {
            textField3.setText("0");
            textField4.setText("2");
        }
        else if (this.siemensPLCS == SiemensPLCS.S1500)
        {
            textField3.setText("0");
            textField4.setText("0");
        }
        else if (this.siemensPLCS == SiemensPLCS.S200Smart)
        {
            textField3.setEnabled(false);
            textField4.setEnabled(false);
        }


        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(siemensS7Net);

                    if (siemensPLCS != SiemensPLCS.S200Smart) {
                        siemensS7Net.setRack((byte) Integer.parseInt(textField3.getText()));
                        siemensS7Net.setSlot((byte) Integer.parseInt(textField4.getText()));
                    }


                    OperateResult connect = siemensS7Net.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        textField6.setText(String.valueOf(siemensS7Net.getPduLength()));
                        userControlReadWriteDevice.SetReadWriteNet(siemensS7Net, defaultAddress, 10);
                        siemensS7Control.setEnabled(true);
                        siemensS7Control.SetReadWriteS7(siemensS7Net);
                        writeControl.SetSiemensS7Net(siemensS7Net);
                        writeControl.setEnabled(true);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }


                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( siemensS7Net, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                    stringBuilder.append( "plc.setRack((byte) Integer.parseInt(\"" + textField3.getText() + "\"));\r\n" );
                    stringBuilder.append( "plc.setSlot((byte) Integer.parseInt(\"" + textField4.getText() + "\"));\r\n" );
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
                if(siemensS7Net!=null){
                    siemensS7Net.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                    siemensS7Control.setEnabled(false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }

}
