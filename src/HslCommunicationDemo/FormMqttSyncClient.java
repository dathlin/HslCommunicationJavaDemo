package HslCommunicationDemo;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Net.NetHandle;
import HslCommunication.Core.Types.ActionOperateExTwo;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Core.Types.OperateResultExTwo;
import HslCommunication.Enthernet.SimplifyNet.NetSimplifyClient;
import HslCommunication.MQTT.MqttConnectionOptions;
import HslCommunication.MQTT.MqttCredential;
import HslCommunication.MQTT.MqttSyncClient;
import HslCommunication.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.UUID;

public class FormMqttSyncClient extends JDialog {

    public FormMqttSyncClient(){
        this.setTitle("Mqtt Sync Client Test Tool");
        this.setSize(1020, 684);
        this.setLocationRelativeTo(null);
        this.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        AddNormal(panel);
        AddConnectSegment(panel);
        AddContent(panel);

        this.add(panel);
    }

    private MqttSyncClient mqttSyncClient = null;
    private JPanel panelContent = null;


    public void AddNormal(JPanel panel){
        JLabel label1 = new JLabel("Blogs：");
        label1.setBounds(11, 9,68, 17);
        panel.add(label1);

        JLabel label5 = new JLabel("https://www.cnblogs.com/dathlin");
        label5.setBounds(80, 9,400, 17);
        panel.add(label5);

        JLabel label2 = new JLabel("Protocols");
        label2.setBounds(466, 9,68, 17);
        panel.add(label2);

        JLabel label3 = new JLabel("Mqtt");
        label3.setForeground(Color.RED);
        label3.setBounds(540, 9,160, 17);
        panel.add(label3);

        JLabel label4 = new JLabel("By Richard Hu");
        label4.setForeground(Color.ORANGE);
        label4.setBounds(887, 9,108, 17);
        panel.add(label4);
    }

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = new JPanel();
        panelConnect.setLayout(null);
        panelConnect.setBounds(14,32,978, 83);
        panelConnect.setBorder(BorderFactory.createTitledBorder( ""));

        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 17,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,14,106, 23);
        textField1.setText("127.0.0.1");
        panelConnect.add(textField1);

        JLabel label2 = new JLabel("Port：");
        label2.setBounds(184, 17,56, 17);
        panelConnect.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(238,14,61, 23);
        textField2.setText("1883");
        panelConnect.add(textField2);

        JLabel label6 = new JLabel("ClientId：");
        label6.setBounds(8, 54,80, 17);
        panelConnect.add(label6);

        JTextField textField3 = new JTextField();
        textField3.setBounds(94, 51,149, 23);
        textField3.setText("");
        panelConnect.add(textField3);

        JLabel label7 = new JLabel("Name：");
        label7.setBounds(249, 54,56, 17);
        panelConnect.add(label7);

        JTextField textField9 = new JTextField();
        textField9.setBounds(313, 51,91, 23);
        textField9.setText("");
        panelConnect.add(textField9);

        JLabel label8 = new JLabel("Pwd：");
        label8.setBounds(420, 54,44, 17);
        panelConnect.add(label8);

        JTextField textField10 = new JTextField();
        textField10.setBounds(484, 51,91, 23);
        textField10.setText("");
        panelConnect.add(textField10);




        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(584,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(477,11,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    if(mqttSyncClient != null) mqttSyncClient.ConnectClose();

                    MqttConnectionOptions options = new MqttConnectionOptions();
                    options.IpAddress = textField1.getText();
                    options.Port = Integer.parseInt(textField2.getText());
                    options.ClientId = textField3.getText();

                    if(Utilities.IsStringNullOrEmpty(textField9.getText())&& Utilities.IsStringNullOrEmpty(textField10.getText())){

                    }
                    else {
                        options.Credentials = new MqttCredential(textField9.getText(), textField10.getText());
                    }

                    mqttSyncClient = new MqttSyncClient(options);

                    OperateResult connect = mqttSyncClient.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        DemoUtils.SetPanelEnabled(panelContent,true);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
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
            }
        });
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (button2.isEnabled() == false) return;
                if(mqttSyncClient!=null){
                    mqttSyncClient.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    DemoUtils.SetPanelEnabled(panelContent,false);
                }
            }
        });


        panel.add(panelConnect);
    }

    public void AddContent(JPanel panel){
        JPanel panelContent = new JPanel();
        panelContent.setLayout(null);
        panelContent.setBounds(14,120,978, 507);
        panelContent.setBorder(BorderFactory.createTitledBorder( ""));

        JLabel label1 = new JLabel("Topic：");
        label1.setBounds(8, 11,60, 17);
        panelContent.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,7,181, 23);
        textField1.setText("A");
        panelContent.add(textField1);

        JLabel label3 = new JLabel("数据：");
        label3.setBounds(8, 39,60, 17);
        panelContent.add(label3);

        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea1);
        jsp.setBounds(62,36,892, 115);
        panelContent.add(jsp);

        JProgressBar sendProgressBar = new JProgressBar();
        sendProgressBar.setBounds(61, 156, 893, 18);
        panelContent.add(sendProgressBar);

        JButton button1 = new JButton("send");
        button1.setFocusPainted(false);
        button1.setBounds(62,180,91, 28);
        panelContent.add(button1);

        JButton button2 = new JButton("Clear");
        button2.setFocusPainted(false);
        button2.setBounds(863, 180,91, 28);
        panelContent.add(button2);

        JTextField textArea6 = new JTextField();
        textArea6.setBounds(62, 217,181, 23);
        panelContent.add(textArea6);

        JProgressBar receiveProgressBar = new JProgressBar();
        receiveProgressBar.setBounds(62, 246, 893, 18);
        panelContent.add(receiveProgressBar);

        JTextArea textArea2 = new JTextArea();
        textArea2.setLineWrap(true);
        JScrollPane jsp2 = new JScrollPane(textArea2);
        jsp2.setBounds(62, 272,892, 222);
        panelContent.add(jsp2);


        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false) return;
                super.mouseClicked(e);
                sendProgressBar.setValue(0);
                receiveProgressBar.setValue(0);

                button1.setEnabled(false);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OperateResultExTwo<String, String> read = mqttSyncClient.ReadString( textField1.getText(), textArea1.getText(),
                                new ActionOperateExTwo<Long, Long>(){
                                    @Override
                                    public void Action(Long m, Long n) {
                                        sendProgressBar.setMaximum(n.intValue());
                                        sendProgressBar.setValue(m.intValue());
                                    }
                                }, null,
                                new ActionOperateExTwo<Long, Long>(){
                                    @Override
                                    public void Action(Long m, Long n) {
                                        receiveProgressBar.setMaximum(n.intValue());
                                        receiveProgressBar.setValue(m.intValue());
                                    }
                                });
                        if (read.IsSuccess)
                        {
                            textArea6.setText(read.Content1);
                            textArea2.setText(read.Content2);
                            button1.setEnabled(true);
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Read Failed:" + read.ToMessageShowString(),
                                    "Result",
                                    JOptionPane.ERROR_MESSAGE);
                            button1.setEnabled(true);
                        }
                    }
                });
                thread.start();
            }
        });
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button2.isEnabled() == false) return;
                super.mouseClicked(e);

                textArea2.setText("");
            }
        });


        panel.add(panelContent);
        this.panelContent = panelContent;
        DemoUtils.SetPanelEnabled(this.panelContent,false);
    }
}
