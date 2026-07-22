package HslCommunicationDemo.Instrument;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.AllenBradley.AllenBradleyPcccNet;
import HslCommunication.Profinet.FATEK.FatekProgramOverTcp;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.Demo.TcpConnectControl;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.PLC.Fatek.FatekHelper;
import HslCommunicationDemo.UserControlReadWriteDevice;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormRkcTemperatureControllerOverTcp extends JPanel {

    public FormRkcTemperatureControllerOverTcp(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Rkc OverTcp", tabbedPane, this));
        AddConnectSegment(this);
        rkc = new HslCommunication.Instrument.RKC.TemperatureControllerOverTcp();

        userControlReadWriteDevice = DemoUtils.CreateDevicePanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(GetRkcAddress( ));
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());
    }

    private AddressExampleControl addressExampleControl;
    private HslCommunication.Instrument.RKC.TemperatureControllerOverTcp rkc = null;
    private String defaultAddress = "M1";
    private UserControlReadWriteDevice userControlReadWriteDevice = null;
    private TcpConnectControl  tcpConnectControl = null;

    public static DeviceAddressExample[] GetRkcAddress( ) {
        return new DeviceAddressExample[]
                {
                        new DeviceAddressExample("M1", "测量值", false, true, "只读, ReadDouble"),
                        new DeviceAddressExample("M2", "CT1输入值", false, true, "只读(0.0~100.0 A), ReadDouble"),
                        new DeviceAddressExample("M3", "CT2输入值", false, true, "只读(0.0~100.0 A), ReadDouble"),
                        new DeviceAddressExample("AA", "第一报警输入", false, true, "只读,ReadDouble 0:关  1:开"),
                        new DeviceAddressExample("AB", "第二报警输入", false, true, "只读,ReadDouble 0:关  1:开"),
                        new DeviceAddressExample("B1", "熄火", false, true, "只读,ReadDouble 0:关  1:开"),
                        new DeviceAddressExample("ER", "错误代码", false, true, "只读,ReadDouble 0 ~ 255"),
                        new DeviceAddressExample("SR", "运行/停止转换", false, true, "Double 0:运行  1:停止"),
                        new DeviceAddressExample("G1", "PID/自整定", false, true, "Double 0:PID  1:AT"),
                        new DeviceAddressExample("S1", "设定值(SV1)", false, true, "Double 量程低限到量程高限"),
                        new DeviceAddressExample("A1", "第一报警设定", false, true, "Double -1999~9999"),
                        new DeviceAddressExample("A2", "第二报警设定", false, true, "Double -1999~9999"),
                        new DeviceAddressExample("A3", "第一加热断线报警设定", false, true, "Double 0.0 ~ 100.0 A"),
                        new DeviceAddressExample("A4", "第二加热断线报警设定", false, true, "Double 1.0 ~ 100.0 A"),
                        new DeviceAddressExample("A5", "控制回路断线报警设定", false, true, "Double 0 ~ 7200 秒"),
                        new DeviceAddressExample("P1", "比例带(加热侧)", false, true, "Double 0 ~ 满量程"),
                        new DeviceAddressExample("I1", "积分时间", false, true, "Double 0 ~ 3600 秒"),
                        new DeviceAddressExample("D1", "微分时间", false, true, "Double 0 ~ 3600 秒"),
                        new DeviceAddressExample("W1", "积分饱和带宽", false, true, "Double 比例带的1%-100%"),
                        new DeviceAddressExample("P2", "制冷侧比例带", false, true, "Double 比例带的1%-3000%"),
                        new DeviceAddressExample("V1", "冷热死区", false, true, "Double -10.0 ~ 10.0"),
                        new DeviceAddressExample("T0", "比例周期(输出1)", false, true, "Double 0 ~ 100 秒"),
                        new DeviceAddressExample("T1", "比例周期(输出12", false, true, "Double 0 ~ 100 秒"),
                        new DeviceAddressExample("G2", "自主校正", false, true, "Double  0:停止   1:开始"),
                        new DeviceAddressExample("PB", "PV基准(量程低限到量程高限)", false, true, "Double  -1999~1999 ℃"),
                };
    }

    public void AddConnectSegment(JPanel panel){
        tcpConnectControl = new TcpConnectControl( panel, TcpConnectControl.HeightTwoLine, TcpConnectControl.LocationOneLine, "2000" );

        JLabel label4 = new JLabel("Station：");
        label4.setBounds(5, TcpConnectControl.LocationTwoLine,58, 17);
        tcpConnectControl.add(label4);

        JTextField textField4 = new JTextField();
        textField4.setBounds(65,TcpConnectControl.LocationTwoLine - 3,53, 23);
        textField4.setText("1");
        tcpConnectControl.add(textField4);

        tcpConnectControl.ButtonConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tcpConnectControl.ButtonConnect.isEnabled())return;
                super.mouseClicked(e);
                try {
                    tcpConnectControl.SetNetworkIpPort(rkc);
                    rkc.setStation(Byte.parseByte(textField4.getText()));

                    OperateResult connect = rkc.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        tcpConnectControl.SetConnectSuccess();
                        userControlReadWriteDevice.SetReadWriteNet(rkc, defaultAddress, 1);
                        userControlReadWriteDevice.getUserControlReadWriteOp().EnableRKC();
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Connect Failed\r\nReason:"+ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }

                StringBuilder stringBuilder = DemoUtils.CreateDeviceCode( HslCommunication.Instrument.RKC.TemperatureControllerOverTcp.class.getName(), "rkc", tcpConnectControl.TextBoxIp.getText(), tcpConnectControl.TextBoxPort.getText() );
                stringBuilder.append( "rkc.setStation( Byte.parseByte(\"" + textField4.getText() + "\"));\r\n" );
                userControlReadWriteDevice.SetDeviceCode( stringBuilder.toString() );
            }
        });

        tcpConnectControl.ButtonDisconnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!tcpConnectControl.ButtonDisconnect.isEnabled()) return;
                if(rkc !=null){
                    rkc.ConnectClose();
                    tcpConnectControl.SetConnectClose();
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });

        panel.add(tcpConnectControl);
    }
}
