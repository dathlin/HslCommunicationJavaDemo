package HslCommunicationDemo.PLC.Siemens;

import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.LogNet.Core.HslMessageDegree;
import HslCommunication.LogNet.Core.ILogNet;
import HslCommunication.ModBus.ModbusTcpServer;
import HslCommunication.Profinet.Siemens.SiemensS7Server;
import HslCommunicationDemo.Demo.AddressExampleControl;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.UserControlReadWriteHead;
import HslCommunicationDemo.UserControlReadWriteOp;
import HslCommunicationDemo.UserControlReadWriteServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class FormSiemensS7Server extends JPanel {


    public FormSiemensS7Server(JTabbedPane tabbedPane){
        setLayout(null);

        add( new UserControlReadWriteHead("Siemens S7 Server", tabbedPane, this));
        AddConnectSegment(this);

        siemensS7Server = new SiemensS7Server();
        userControlReadWriteDevice = DemoUtils.CreateServerPanel(this);
        userControlReadWriteDevice.setEnabled(false);

        addressExampleControl = new AddressExampleControl(SiemensHelper.GetSiemensS7Address());
        userControlReadWriteDevice.AddSpecialFunctionTab(addressExampleControl, false, DeviceAddressExample.GetTitle());

        // 创建定时器
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userControlReadWriteDevice!= null)
                    userControlReadWriteDevice.getLogControl().SetOnlineText(siemensS7Server.GetOnlineCount());
            }
        });
        timer.start();

    }

    private AddressExampleControl addressExampleControl;
    private SiemensS7Server siemensS7Server = null;
    private String defaultAddress = "M100";
    private UserControlReadWriteServer userControlReadWriteDevice = null;

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Port：");
        label1.setBounds(8, 17,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,14,86, 23);
        textField1.setText("102");
        panelConnect.add(textField1);

        JCheckBox checkBox2 = new JCheckBox("Enable Write?");
        checkBox2.setBounds(200,16,120, 21);
        checkBox2.setSelected(true);
        panelConnect.add(checkBox2);

        JButton button2 = new JButton("Close");
        button2.setFocusPainted(false);
        button2.setBounds(600,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Start");
        button1.setFocusPainted(false);
        button1.setBounds(500,11,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    siemensS7Server.EnableWrite = checkBox2.isSelected();
                    siemensS7Server.ServerStart(Integer.parseInt(textField1.getText()));
                    siemensS7Server.setLogNet(new ILogNet() {
                        @Override
                        public int LogSaveMode() {
                            return 0;
                        }

                        @Override
                        public void RecordMessage(HslMessageDegree degree, String keyWord, String text) {

                        }

                        @Override
                        public void WriteDebug(String text) {
                        }

                        @Override
                        public void WriteDebug(String keyWord, String text) {
                            userControlReadWriteDevice.getLogControl().WriteDebug(keyWord, text);
                        }

                        @Override
                        public void WriteDescription(String description) {

                        }

                        @Override
                        public void WriteError(String text) {

                        }

                        @Override
                        public void WriteError(String keyWord, String text) {

                        }

                        @Override
                        public void WriteException(String keyWord, Exception ex) {

                        }

                        @Override
                        public void WriteException(String keyWord, String text, Exception ex) {

                        }

                        @Override
                        public void WriteFatal(String text) {

                        }

                        @Override
                        public void WriteFatal(String keyWord, String text) {

                        }

                        @Override
                        public void WriteInfo(String text) {

                        }

                        @Override
                        public void WriteInfo(String keyWord, String text) {

                        }

                        @Override
                        public void WriteNewLine() {

                        }

                        @Override
                        public void WriteWarn(String text) {

                        }

                        @Override
                        public void WriteWarn(String keyWord, String text) {

                        }

                        @Override
                        public void SetMessageDegree(HslMessageDegree degree) {

                        }

                        @Override
                        public void FiltrateKeyword(String keyword) {

                        }

                        @Override
                        public String[] GetExistLogFileNames() {
                            return new String[0];
                        }
                    });
                    JOptionPane.showMessageDialog(
                            null,
                            "Start Success",
                            "Result",
                            JOptionPane.PLAIN_MESSAGE);
                    button2.setEnabled(true);
                    button1.setEnabled(false);
                    userControlReadWriteDevice.SetReadWriteNet(siemensS7Server, defaultAddress, 10);
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
                if(siemensS7Server!=null){
                    siemensS7Server.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }

}
