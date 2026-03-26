package HslCommunicationDemo.PLC.Modbus;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.FunctionOperateExTwo;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.LogNet.Core.HslMessageDegree;
import HslCommunication.LogNet.Core.HslMessageItem;
import HslCommunication.LogNet.Core.ILogNet;
import HslCommunication.LogNet.Core.LogNetBase;
import HslCommunication.ModBus.ModbusMappingAddress;
import HslCommunication.ModBus.ModbusTcpNet;
import HslCommunication.ModBus.ModbusTcpServer;
import HslCommunication.Profinet.Delta.Helper.DeltaDvpHelper;
import HslCommunication.Profinet.Inovance.InovanceHelper;
import HslCommunication.Profinet.MegMeet.MegMeetHelper;
import HslCommunication.Profinet.XINJE.XinJEHelper;
import HslCommunication.Profinet.XINJE.XinJESeries;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class FormModbusServer extends HslJPanel {


    public FormModbusServer(JTabbedPane tabbedPane){
        setLayout(null);

        add( new UserControlReadWriteHead("Modbus Tcp Server", tabbedPane, this));
        AddConnectSegment(this);

        modbusTcpServer = new ModbusTcpServer();
        userControlReadWriteDevice = DemoUtils.CreateServerPanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(DemoModbusHelper.GetModbusAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());

        // 创建定时器
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userControlReadWriteDevice!= null)
                    userControlReadWriteDevice.getLogControl().SetOnlineText(modbusTcpServer.GetOnlineCount());
            }
        });
        timer.start();

    }

    private AddressExampleControl addressExampleControl;
    private Timer timer;
    private String defaultAddress = "100";
    private ModbusTcpServer modbusTcpServer = null;
    private UserControlReadWriteServer userControlReadWriteDevice = null;
    private JButton button_connect;
    private JButton button_disconnect;

    @Override
    public void OnClose() {
        super.OnClose();
        if (button_connect == null || button_disconnect == null) return;
        if (button_disconnect.isEnabled()){
            modbusTcpServer.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Port：");
        label1.setBounds(8, 7,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,4,86, 23);
        textField1.setText("502");
        panelConnect.add(textField1);

        JCheckBox checkBox1 = new JCheckBox("Use Modbus Rtu?");
        checkBox1.setBounds(160,6,140, 21);
        checkBox1.setSelected(false);
        panelConnect.add(checkBox1);


        JLabel label3 = new JLabel("Station：");
        label3.setBounds(298, 7,56, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(352,4,40, 23);
        textField3.setText("1");
        panelConnect.add(textField3);

        JCheckBox checkBox2 = new JCheckBox("Enable Write?");
        checkBox2.setBounds(420,6,120, 21);
        checkBox2.setSelected(true);
        panelConnect.add(checkBox2);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(558,3,111, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedItem(DataFormat.CDAB);
        panelConnect.add(comboBox1);

        JLabel label_address = new JLabel("Address Mapping:");
        label_address.setBounds(8, 32,120, 17);
        panelConnect.add(label_address);

        JComboBox<String> comboBox_address_mapping = new JComboBox<>();
        comboBox_address_mapping.setBounds(130,30,201, 25);
        comboBox_address_mapping.addItem("No Address Mapping"); // 0
        comboBox_address_mapping.addItem("InovanceAM");         // 1
        comboBox_address_mapping.addItem("InovanceH3U");        // 2
        comboBox_address_mapping.addItem("InovanceH5U");        // 3
        comboBox_address_mapping.addItem("InovanceEasy");       // 4
        comboBox_address_mapping.addItem("DeltaAS[台达]");       // 5
        comboBox_address_mapping.addItem("DeltaDvp[台达]");      // 6
        comboBox_address_mapping.addItem("MegMeet[麦格米特]");    // 7
        comboBox_address_mapping.addItem("XinJE_XC"); // 0
        comboBox_address_mapping.addItem("XinJE_XD_XL"); // 0
        comboBox_address_mapping.addItem("WeCon[维控]"); // 0
        comboBox_address_mapping.addItem("Invt_Ts[英威腾]"); // 0
        comboBox_address_mapping.setSelectedItem("No Address Mapping");
        panelConnect.add(comboBox_address_mapping);
        comboBox_address_mapping.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // System.out.println("选中: " + e.getItem());
                    if (modbusTcpServer != null)
                    {
                        switch (comboBox_address_mapping.getSelectedIndex())
                        {
                            case 0:  modbusTcpServer.RegisteredAddressMapping( null ); break;
                            case 1:  modbusTcpServer.RegisteredAddressMapping( new FunctionOperateExTwo<String, Byte,OperateResultExOne<String>>(){
                                @Override
                                public OperateResultExOne<String> Action(String address, Byte function) {
                                    return InovanceHelper.PraseInovanceAMAddress( address, function );
                                }
                            }  ); break;
                            case 2:  modbusTcpServer.RegisteredAddressMapping( new FunctionOperateExTwo<String, Byte,OperateResultExOne<String>>(){
                                @Override
                                public OperateResultExOne<String> Action(String address, Byte function) {
                                    return InovanceHelper.PraseInovanceH3UAddress( address, function );
                                }
                            } ); break;
                            case 3:  modbusTcpServer.RegisteredAddressMapping(new FunctionOperateExTwo<String, Byte,OperateResultExOne<String>>(){
                                @Override
                                public OperateResultExOne<String> Action(String address, Byte function) {
                                    return InovanceHelper.PraseInovanceH5UAddress( address, function );
                                }
                            } ); break;
                            case 4:  modbusTcpServer.RegisteredAddressMapping( new FunctionOperateExTwo<String, Byte,OperateResultExOne<String>>(){
                                @Override
                                public OperateResultExOne<String> Action(String address, Byte function) {
                                    return InovanceHelper.PraseInovanceH5UAddress( address, function );
                                }
                            } ); break;
                            case 5:  modbusTcpServer.RegisteredAddressMapping( new FunctionOperateExTwo<String, Byte,OperateResultExOne<String>>(){
                                @Override
                                public OperateResultExOne<String> Action(String address, Byte function) {
                                    return ModbusMappingAddress.Delta_AS( address, function );
                                }
                            }  ); break;
                            case 6:  modbusTcpServer.RegisteredAddressMapping( new FunctionOperateExTwo<String, Byte,OperateResultExOne<String>>(){
                                @Override
                                public OperateResultExOne<String> Action(String address, Byte function) {
                                    return DeltaDvpHelper.ParseDeltaDvpAddress( address, function );
                                }
                            } ); break;
                            case 7:  modbusTcpServer.RegisteredAddressMapping( new FunctionOperateExTwo<String, Byte,OperateResultExOne<String>>(){
                                @Override
                                public OperateResultExOne<String> Action(String address, Byte function) {
                                    return MegMeetHelper.PraseMegMeetAddress( address, function );
                                }
                            } ); break;
                            case 8:  modbusTcpServer.RegisteredAddressMapping( new FunctionOperateExTwo<String, Byte,OperateResultExOne<String>>(){
                                @Override
                                public OperateResultExOne<String> Action(String address, Byte function) {
                                    return XinJEHelper.PraseXinJEAddress( XinJESeries.XC, address, function );
                                }
                            }  ); break;
                            case 9:  modbusTcpServer.RegisteredAddressMapping( new FunctionOperateExTwo<String, Byte,OperateResultExOne<String>>(){
                                @Override
                                public OperateResultExOne<String> Action(String address, Byte function) {
                                    return XinJEHelper.PraseXinJEAddress( XinJESeries.XD, address, function );
                                }
                            } ); break;
                            case 10: modbusTcpServer.RegisteredAddressMapping( new FunctionOperateExTwo<String, Byte,OperateResultExOne<String>>(){
                                @Override
                                public OperateResultExOne<String> Action(String address, Byte function) {
                                    return ModbusMappingAddress.WeCon_Lx5v(address, function);
                                }
                            } ); break;
                            case 11: modbusTcpServer.RegisteredAddressMapping( new FunctionOperateExTwo<String, Byte,OperateResultExOne<String>>(){
                                @Override
                                public OperateResultExOne<String> Action(String address, Byte function) {
                                    return ModbusMappingAddress.Invt_Ts(address, function);
                                }
                            }); break;
                            default: break;
                        }
                    }

                    switch (comboBox_address_mapping.getSelectedIndex())
                    {
                        case 0:  addressExampleControl.SetAddressExample( HslCommunicationDemo.PLC.Modbus.DemoModbusHelper.GetModbusAddressExamples( ) ); break;
                        case 1:  addressExampleControl.SetAddressExample( HslCommunicationDemo.PLC.Inovance.InovanceHelper.GetInovanceAddress( ) ); break;
                        case 2:  addressExampleControl.SetAddressExample( HslCommunicationDemo.PLC.Inovance.InovanceHelper.GetInovanceAddress( ) ); break;
                        case 3:  addressExampleControl.SetAddressExample( HslCommunicationDemo.PLC.Inovance.InovanceHelper.GetInovanceAddress( ) ); break;
                        case 4:  addressExampleControl.SetAddressExample( HslCommunicationDemo.PLC.Inovance.InovanceHelper.GetInovanceAddress( ) ); break;
                        case 5:  addressExampleControl.SetAddressExample( HslCommunicationDemo.PLC.Delta.DeltaHelper.GetDeviceAddressExamples( ) ); break;
                        case 6:  addressExampleControl.SetAddressExample( HslCommunicationDemo.PLC.Delta.DeltaHelper.GetDeviceAddressExamples( ) ); break;
                        case 7:  addressExampleControl.SetAddressExample( HslCommunicationDemo.PLC.MegMeet.MegMeetHelper.GetMegMeetAddress( ) ); break;
                        case 8:  addressExampleControl.SetAddressExample( HslCommunicationDemo.PLC.XinJE.XinJEHelper.GetXinJEAddress( ) ); break;
                        case 9:  addressExampleControl.SetAddressExample( HslCommunicationDemo.PLC.XinJE.XinJEHelper.GetXinJEAddress( ) ); break;
                        case 10: addressExampleControl.SetAddressExample( HslCommunicationDemo.PLC.WeCon.WeConHelper.GetWeConLx5vAddress( ) ); break;
                        case 11: addressExampleControl.SetAddressExample( HslCommunicationDemo.PLC.Invt.InvtHelper.GetInvtAddress( ) ); break;
                        default: break;
                    }
                }
            }
        });

        JButton button2 = new JButton("Close");
        button2.setFocusPainted(false);
        button2.setBounds(850,11,121, 28);
        button_disconnect = button2;
        panelConnect.add(button2);

        JButton button1 = new JButton("Start");
        button1.setFocusPainted(false);
        button1.setBounds(752,11,91, 28);
        button_connect = button1;
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    modbusTcpServer.setDataFormat((DataFormat) comboBox1.getSelectedItem());
                    modbusTcpServer.UseModbusRtuOverTcp = checkBox1.isSelected();
                    modbusTcpServer.EnableWrite = checkBox2.isSelected();
                    modbusTcpServer.ServerStart(Integer.parseInt(textField1.getText()));
                    modbusTcpServer.setLogNet(new LogNetBase(){
                        /**
                         * 在日志存储到文件之前调用的方法，只需要重写本方法，就可以实现任意的信息输出<br />
                         * The method that is called before the log is stored in a file only needs to be overridden to achieve arbitrary information output
                         *
                         * @param message 等待记录的消息
                         */
                        @Override
                        public void BeforeSaveToFile(HslMessageItem message) {
                            super.BeforeSaveToFile(message);
                            userControlReadWriteDevice.getLogControl().WriteLog(message);
                        }
                    });
                    JOptionPane.showMessageDialog(
                            null,
                            "Start Success",
                            "Result",
                            JOptionPane.PLAIN_MESSAGE);

                    button2.setEnabled(true);
                    button1.setEnabled(false);
                    userControlReadWriteDevice.SetReadWriteNet(modbusTcpServer, defaultAddress, 10);
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
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!button2.isEnabled()) return;
                if(modbusTcpServer!=null){
                    modbusTcpServer.ServerClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }

}
