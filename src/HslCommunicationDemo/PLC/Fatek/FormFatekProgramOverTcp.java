package HslCommunicationDemo.PLC.Fatek;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.FATEK.FatekProgramOverTcp;
import HslCommunication.Profinet.Melsec.MelsecA1ENet;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormFatekProgramOverTcp extends HslJPanel {

    public FormFatekProgramOverTcp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Fatek Program OverTcp", tabbedPane, this));
        AddConnectSegment(this);
        plc = new FatekProgramOverTcp();

        fatekFunction = AddFunction();
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(fatekFunction, false, "FatekFunction");
        userControlReadWriteDevice.setEnabled(false);
        DemoUtils.SetPanelEnabled(fatekFunction, false);

        addressExampleControl = new AddressExampleControl(FatekHelper.GetDeviceAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private FatekProgramOverTcp plc = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private JPanel fatekFunction;
    private TcpConnectControl tcpConnectControl = null;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            plc.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "2000");
        JLabel label4 = new JLabel("Station：");
        label4.setBounds(5, TcpConnectControl.LocationTwoLine,58, 17);
        tcpConnectControl.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(63,TcpConnectControl.LocationTwoLine - 3,53, 23);
        textField4.setText("0");
        tcpConnectControl.add(textField4);
        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(plc);
                    plc.setStation(Byte.parseByte(textField4.getText()));

                    OperateResult connect = plc.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(plc, defaultAddress, 1);
                        DemoUtils.SetPanelEnabled(fatekFunction, true);
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
                    DemoUtils.SetPanelEnabled(fatekFunction, false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }

    public JPanel AddFunction(){
        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);

        JButton button_run = new JButton("Run");
        button_run.setFocusPainted(false);
        button_run.setBounds(24,24,82, 28);
        button_run.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button_run.isEnabled()) return;
                super.mouseClicked(e);
                OperateResult read = plc.Run();
                if(read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Run Success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Run Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button_run);


        JButton button_stop = new JButton("Stop");
        button_stop.setFocusPainted(false);
        button_stop.setBounds(130,24,82, 28);
        button_stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button_stop.isEnabled()) return;
                super.mouseClicked(e);
                OperateResult read = plc.Stop();
                if(read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Stop Success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Stop Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button_stop);


        JButton button_status = new JButton("Status");
        button_status.setFocusPainted(false);
        button_status.setBounds(24,62,82, 28);
        button_status.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button_status.isEnabled()) return;
                super.mouseClicked(e);
                OperateResultExOne<boolean[]> read = plc.ReadStatus();
                if(read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            read.Content[0] ? "Run" : "Stop",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Stop Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button_status);



        return panelRead;
    }
}
