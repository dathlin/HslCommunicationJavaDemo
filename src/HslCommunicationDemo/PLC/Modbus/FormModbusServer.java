package HslCommunicationDemo.PLC.Modbus;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.LogNet.Core.HslMessageDegree;
import HslCommunication.LogNet.Core.HslMessageItem;
import HslCommunication.LogNet.Core.ILogNet;
import HslCommunication.LogNet.Core.LogNetBase;
import HslCommunication.ModBus.ModbusTcpNet;
import HslCommunication.ModBus.ModbusTcpServer;
import HslCommunicationDemo.*;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class FormModbusServer extends JPanel {


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

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Port：");
        label1.setBounds(8, 17,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,14,86, 23);
        textField1.setText("502");
        panelConnect.add(textField1);

        JCheckBox checkBox1 = new JCheckBox("Use Modbus Rtu?");
        checkBox1.setBounds(160,16,140, 21);
        checkBox1.setSelected(false);
        panelConnect.add(checkBox1);


        JLabel label3 = new JLabel("Station：");
        label3.setBounds(298, 17,56, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(352,14,40, 23);
        textField3.setText("1");
        panelConnect.add(textField3);

        JCheckBox checkBox2 = new JCheckBox("Enable Write?");
        checkBox2.setBounds(420,16,120, 21);
        checkBox2.setSelected(true);
        panelConnect.add(checkBox2);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(558,13,111, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedItem(DataFormat.CDAB);
        panelConnect.add(comboBox1);

        JButton button2 = new JButton("Close");
        button2.setFocusPainted(false);
        button2.setBounds(850,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Start");
        button1.setFocusPainted(false);
        button1.setBounds(752,11,91, 28);
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
