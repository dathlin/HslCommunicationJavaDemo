package HslCommunicationDemo.PLC.Ge;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.GE.GeSRTPNet;
import HslCommunication.Profinet.Melsec.MelsecA1ENet;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class FormGeSRTPNet extends HslJPanel {

    public FormGeSRTPNet(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("SRTP", tabbedPane, this));
        AddConnectSegment(this);
        plc = new GeSRTPNet();

        geControl = AddGeControl( );
        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.AddSpecialFunctionTab(geControl, false, "GeControl");
        userControlReadWriteDevice.setEnabled(false);
        DemoUtils.SetPanelEnabled(geControl, false);

        addressExampleControl = new AddressExampleControl(GeHelper.GetDeviceAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private GeSRTPNet plc = null;
    private String defaultAddress = "R1";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private JPanel geControl;
    private TcpConnectControl tcpConnectControl = null;

    @Override
    public void OnClose() {
        super.OnClose();
        if (tcpConnectControl.NeedCloseDevice()){
            plc.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl(panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationCenterLine, "18245");
        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(plc);
                    OperateResult connect = plc.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(plc, defaultAddress, 10);
                        DemoUtils.SetPanelEnabled(geControl, true);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }


                    StringBuilder stringBuilder = DemoUtils.CreatePlcDeviceCode( plc, tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
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
                    DemoUtils.SetPanelEnabled(geControl, false);
                }
            }
        });


        panel.add(tcpConnectControl);
    }

    public JPanel AddGeControl( ){
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JTextArea textArea_data = new JTextArea();
        textArea_data.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea_data);
        jsp.setBounds(5, 45, 400, 80);
        panel.add(jsp);


        JButton button_read_time = new JButton("PLC Time");
        button_read_time.setBounds(5, 10, 120, 28);
        button_read_time.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!button_read_time.isEnabled()) return;

                OperateResultExOne<Date> read = plc.ReadPLCTime();
                if (read.IsSuccess){
                    textArea_data.setText( DemoUtils.FormatterDateTime.format(read.Content) );
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(button_read_time);

        JButton button_read_program = new JButton("ProgramName");
        button_read_program.setBounds(135, 10, 120, 28);
        button_read_program.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!button_read_program.isEnabled()) return;

                OperateResultExOne<String> read = plc.ReadProgramName();
                if (read.IsSuccess) {
                    textArea_data.setText(read.Content);
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(button_read_program);

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                jsp.setBounds(5, 45, panel.getWidth() - 10, panel.getHeight() - 50);
                jsp.updateUI();
            }
        });

        return panel;
    }
}
