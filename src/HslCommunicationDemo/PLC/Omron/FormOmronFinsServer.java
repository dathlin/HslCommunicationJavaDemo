package HslCommunicationDemo.PLC.Omron;

import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.LogNet.Core.HslMessageItem;
import HslCommunication.LogNet.Core.LogNetBase;
import HslCommunication.Profinet.Omron.OmronFinsServer;
import HslCommunication.Profinet.Siemens.SiemensS7Server;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.HslJPanel;
import HslCommunicationDemo.PLC.Siemens.SiemensHelper;
import HslCommunicationDemo.UserControlReadWriteHead;
import HslCommunicationDemo.UserControlReadWriteServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormOmronFinsServer extends HslJPanel {

    public FormOmronFinsServer(JTabbedPane tabbedPane){
        setLayout(null);

        add( new UserControlReadWriteHead("Omron FinsTCP Server", tabbedPane, this));
        AddConnectSegment(this);

        omronFinsServer = new OmronFinsServer();
        userControlReadWriteDevice = DemoUtils.CreateServerPanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(DemoOmronHelper.GetOmronAddressExamples());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());

        // 创建定时器
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userControlReadWriteDevice!= null)
                    userControlReadWriteDevice.getLogControl().SetOnlineText(omronFinsServer.GetOnlineCount());
            }
        });
        timer.start();

    }

    private AddressExampleControl addressExampleControl;
    private OmronFinsServer omronFinsServer = null;
    private String defaultAddress = "D100";
    private UserControlReadWriteServer userControlReadWriteDevice = null;
    private JButton button_connect;
    private JButton button_disconnect;

    @Override
    public void OnClose() {
        super.OnClose();
        if (button_connect == null || button_disconnect == null) return;
        if (button_disconnect.isEnabled()){
            omronFinsServer.ConnectClose();
        }
    }

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Port：");
        label1.setBounds(8, 17,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,14,86, 23);
        textField1.setText("9600");
        panelConnect.add(textField1);

        JCheckBox checkBox2 = new JCheckBox("Enable Write?");
        checkBox2.setBounds(180,16,120, 21);
        checkBox2.setSelected(true);
        panelConnect.add(checkBox2);

        JComboBox<DataFormat> comboBox_datatype = new JComboBox<>();
        comboBox_datatype.setBounds(310,14,80, 25);
        comboBox_datatype.addItem(DataFormat.ABCD);
        comboBox_datatype.addItem(DataFormat.BADC);
        comboBox_datatype.addItem(DataFormat.CDAB);
        comboBox_datatype.addItem(DataFormat.DCBA);
        comboBox_datatype.setSelectedIndex(2);
        panelConnect.add(comboBox_datatype);

        JCheckBox checkBox_string_reverse = new JCheckBox("String Reverse");
        checkBox_string_reverse.setBounds(400,16,120, 17);
        panelConnect.add(checkBox_string_reverse);

        JButton button2 = new JButton("Close");
        button2.setFocusPainted(false);
        button2.setBounds(650,11,121, 28);
        button_disconnect = button2;
        panelConnect.add(button2);

        JButton button1 = new JButton("Start");
        button1.setFocusPainted(false);
        button1.setBounds(550,11,91, 28);
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
                    omronFinsServer = new OmronFinsServer();
                    omronFinsServer.EnableWrite = checkBox2.isSelected();
                    omronFinsServer.setDataFormat((DataFormat) comboBox_datatype.getSelectedItem());
                    omronFinsServer.getByteTransform().setIsStringReverse(checkBox_string_reverse.isSelected());
                    omronFinsServer.ServerStart(Integer.parseInt(textField1.getText()));
                    omronFinsServer.setLogNet(new LogNetBase(){
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
                    userControlReadWriteDevice.SetReadWriteNet(omronFinsServer, defaultAddress, 10);
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
                if(omronFinsServer!=null){
                    omronFinsServer.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }
}

