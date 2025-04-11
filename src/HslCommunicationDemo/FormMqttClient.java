package HslCommunicationDemo;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.ActionOperateExTwo;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExTwo;
import HslCommunication.LogNet.Core.HslMessageDegree;
import HslCommunication.LogNet.Core.ILogNet;
import HslCommunication.LogNet.Core.LogNetBase;
import HslCommunication.MQTT.*;
import HslCommunication.Utilities;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class FormMqttClient extends JPanel {
    public FormMqttClient(JTabbedPane tabbedPane){
        setLayout(null);

        add( new UserControlReadWriteHead("Mqtt", tabbedPane, this));
        AddConnectSegment(this);
        AddContent(this);
    }

    private MqttClient mqttClient = null;
    private JTextArea textArea = null;
    private JPanel panelContent = null;
    private JScrollPane scrollPane = null;
    private JCheckBox checkBox_debug_show = null;
    private JTextArea textArea_code;
    private long receiveTick = 0;
    private JLabel label_receive = null;
    private JRadioButton optionHex = null;
    private boolean renderStop = false;

    private void SetText(String text, boolean append){
        if (textArea != null) {
            if (!renderStop) {
                textArea.append(Utilities.getStringDateShort(new Date(), "yyyy-MM-dd HH:mm:ss.SSS") + " " + text + "\r\n");
                JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
                scrollBar.setValue(scrollBar.getMaximum());
            }
        }
    }

    private void SetCodeText( String text ) {
        if (textArea_code != null) {
            textArea_code.setText(text);
        }
    }

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JTextField textField1 = DemoUtils.CreateIpAddressTextBox(panelConnect, 7);
        JTextField textField2 = DemoUtils.CreateIpPortTextBox(panelConnect, "1883", 7);

        JCheckBox checkBox1 = new JCheckBox("RSA?");
        checkBox1.setBounds(420,7,80,17);
        panelConnect.add(checkBox1);

        JLabel label6 = new JLabel("ClientId：");
        label6.setBounds(8, 34,80, 17);
        panelConnect.add(label6);

        JTextField textField3 = new JTextField();
        textField3.setBounds(90, 31,250, 23);
        textField3.setText("");
        panelConnect.add(textField3);

        JLabel label7 = new JLabel("Name：");
        label7.setBounds(350, 34,56, 17);
        panelConnect.add(label7);

        JTextField textField9 = new JTextField();
        textField9.setBounds(420, 31,100, 23);
        textField9.setText("");
        panelConnect.add(textField9);

        JLabel label8 = new JLabel("Pwd：");
        label8.setBounds(530, 34,50, 17);
        panelConnect.add(label8);

        JTextField textField10 = new JTextField();
        textField10.setBounds(590, 31,100, 23);
        textField10.setText("");
        panelConnect.add(textField10);

        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(620,2,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(517,2,100, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                //try {
                if(mqttClient != null) mqttClient.ConnectClose();
                MqttConnectionOptions options = new MqttConnectionOptions();
                options.IpAddress = textField1.getText();
                options.Port = Integer.parseInt(textField2.getText());
                options.ClientId = textField3.getText();
                options.UseRSAProvider = checkBox1.isSelected();

                if(Utilities.IsStringNullOrEmpty(textField9.getText())&& Utilities.IsStringNullOrEmpty(textField10.getText())){

                }
                else {
                    options.Credentials = new MqttCredential(textField9.getText(), textField10.getText());
                }

                mqttClient = new MqttClient(options);
                mqttClient.OnMqttMessageReceived = new ActionOperateExTwo<MqttClient, MqttApplicationMessage>(){
                    @Override
                    public void Action(MqttClient client, MqttApplicationMessage message) {
                        super.Action(client, message);
                        receiveTick++;
                        boolean hex = false;
                        if (optionHex != null && optionHex.isSelected()) {
                            hex = true;
                        }
                        SetText( "Topic[" + message.toString() + "]" + "\r\n" + ( hex ? SoftBasic.ByteToHexString(message.Payload, ' ') : new String(message.Payload, StandardCharsets.UTF_8) ), true );
                        label_receive.setText(String.valueOf(receiveTick));
                    }
                };
                mqttClient.LogNet = new LogNetBase(){
                    @Override
                    public void RecordMessage(HslMessageDegree degree, String keyWord, String text) {
                        if ( checkBox_debug_show != null && checkBox_debug_show.isSelected() ) {
                            SetText("[" + degree.toString() + "]" + " [" + keyWord + "] " + text, true);
                        }
                    }
                };
                OperateResult connect = mqttClient.ConnectServer();
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
                    mqttClient = null;
                    JOptionPane.showMessageDialog(
                            null,
                            "Connect Failed:" + connect.ToMessageShowString(),
                            "Result",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (button2.isEnabled() == false) return;
                if(mqttClient!=null){
                    mqttClient.ConnectClose();
                    mqttClient = null;
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    DemoUtils.SetPanelEnabled(panelContent,false);
                }
            }
        });


        panel.add(panelConnect);
    }

    public void AddContent(JPanel panel){
        JPanel panelContent = DemoUtils.CreateContentPanel(panel);

        JLabel label1 = new JLabel("Topic：");
        label1.setBounds(8, 11,60, 17);
        panelContent.add(label1);

        JTextField textField_topic = new JTextField();
        textField_topic.setBounds(62,7,240, 23);
        textField_topic.setText("A");
        panelContent.add(textField_topic);

        JButton button_sub = new JButton( "Subscribe" );
        button_sub.setFocusPainted(false);
        button_sub.setBounds(320,7,120,23);
        panelContent.add(button_sub);

        JButton button_unSub = new JButton( "UnSubscribe" );
        button_unSub.setFocusPainted(false);
        button_unSub.setBounds(450,7,120,23);
        panelContent.add(button_unSub);

        JLabel label3 = new JLabel("Payload:");
        label3.setBounds(8, 39,60, 17);
        panelContent.add(label3);


        JCheckBox checkBox_send_hex = new JCheckBox("Hex?");
        checkBox_send_hex.setBounds(3,58,58,17);
        panelContent.add(checkBox_send_hex);

        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea1);
        jsp.setBounds(62,36,892, 135);
        panelContent.add(jsp);

        JCheckBox checkBox_retain = new JCheckBox("Retain?");
        checkBox_retain.setBounds(60,183,80,17);
        panelContent.add(checkBox_retain);


        JComboBox<MqttQualityOfServiceLevel> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(150,181,120, 25);
        comboBox1.addItem(MqttQualityOfServiceLevel.AtMostOnce);
        comboBox1.addItem(MqttQualityOfServiceLevel.AtLeastOnce);
        comboBox1.addItem(MqttQualityOfServiceLevel.ExactlyOnce);
        comboBox1.addItem(MqttQualityOfServiceLevel.OnlyTransfer);
        comboBox1.setSelectedItem(MqttQualityOfServiceLevel.AtMostOnce);
        panelContent.add(comboBox1);

        JButton button_publish = new JButton("Publish");
        button_publish.setFocusPainted(false);
        button_publish.setBounds(280,180,120, 28);
        panelContent.add(button_publish);

        JButton button2 = new JButton("Clear");
        button2.setFocusPainted(false);
        button2.setBounds(863, 180,91, 28);
        panelContent.add(button2);

        checkBox_debug_show = new JCheckBox("Debug Log?");
        checkBox_debug_show.setBounds(60,248,120,17);
        panelContent.add(checkBox_debug_show);

        JLabel label10 = new JLabel("Received Tick：");
        label10.setBounds(180, 248,120, 17);
        panelContent.add(label10);

        label_receive = new JLabel("0");
        label_receive.setBounds(300, 248,100, 17);
        panelContent.add(label_receive);

        ButtonGroup buttonGroup = new ButtonGroup();
        // 创建两个 JRadioButton
        optionHex = new JRadioButton("HEX");
        optionHex.setBounds( 400, 248, 60, 17 );
        panelContent.add(optionHex);
        JRadioButton optionB = new JRadioButton("UTF8");
        optionB.setBounds( 460, 248, 60, 17 );
        optionB.setSelected(true);
        panelContent.add(optionB);
        // 将 JRadioButton 添加到 ButtonGroup 中
        buttonGroup.add(optionHex);
        buttonGroup.add(optionB);

        JButton button_stop = new JButton("Stop");
        button_stop.setFocusPainted(false);
        button_stop.setBounds(280,243,120, 28);
        panelContent.add(button_stop);

        JLabel label5 = new JLabel("Received：");
        label5.setBounds(3, 272,65, 17);
        panelContent.add(label5);

        JTextField textArea6 = new JTextField();
        textArea6.setBounds(62, 217,181, 23);
        panelContent.add(textArea6);


        JTextArea textArea2 = new JTextArea();
        textArea2.setLineWrap(true);
        JScrollPane jsp2 = new JScrollPane(textArea2);
        jsp2.setBounds(62, 272,892, 222);
        panelContent.add(jsp2);

        JLabel label_code = new JLabel("Code：");
        label_code.setBounds(8, 450,60, 17);
        panelContent.add(label_code);

        textArea_code = new JTextArea();
        textArea_code.setLineWrap(true);
        JScrollPane jsp_code = new JScrollPane(textArea_code);
        jsp_code.setBounds(62,450,892, 48);
        panelContent.add(jsp_code);


        textArea = textArea2;
        scrollPane = jsp2;


        button_sub.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button_sub.isEnabled() == false) return;
                super.mouseClicked(e);

                OperateResult result = mqttClient.SubscribeMessage(textField_topic.getText());
                if(result.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "SubscribeMessage Success!",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "SubscribeMessage Failed:" + result.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
                SetCodeText( "OperateResult result = mqttClient.SubscribeMessage(\"" + textField_topic.getText() + "\");" );
            }
        });
        button_unSub.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button_unSub.isEnabled() == false) return;
                super.mouseClicked(e);

                OperateResult result = mqttClient.UnSubscribeMessage(textField_topic.getText());
                if(result.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "UnSubscribeMessage Success!",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "UnSubscribeMessage Failed:" + result.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
                SetCodeText( "OperateResult result = mqttClient.UnSubscribeMessage(\"" + textField_topic.getText() + "\");" );
            }
        });

        button_publish.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button_publish.isEnabled() == false) return;
                super.mouseClicked(e);

                MqttApplicationMessage message = new MqttApplicationMessage();
                message.Retain = checkBox_retain.isSelected();
                message.Topic = textField_topic.getText();
                if (checkBox_send_hex.isSelected()){
                    message.Payload = SoftBasic.HexStringToBytes( textArea1.getText() );
                }
                else {
                    message.Payload = textArea1.getText().getBytes( StandardCharsets.UTF_8 );
                }
                message.QualityOfServiceLevel = (MqttQualityOfServiceLevel)comboBox1.getSelectedItem();
                OperateResult publish = mqttClient.PublishMessage(message);
                if(publish.IsSuccess){

                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Publish Failed:" + publish.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }

                SetCodeText( "OperateResult publish = mqttClient.PublishMessage(new MqttApplicationMessage( \"" + message.Topic +"\", \"" + textArea1.getText() + "\".getBytes( StandardCharsets.UTF_8 ), " + message.Retain + ", MqttQualityOfServiceLevel."+ message.QualityOfServiceLevel + " ));" );
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
        button_stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button_stop.isEnabled() == false) return;
                super.mouseClicked(e);

                if (renderStop){
                    renderStop = false;
                    button_stop.setText( "Stop" );
                }
                else {
                    renderStop = true;
                    button_stop.setText( "Start" );
                }
            }
        });


        panel.add(panelContent);
        this.panelContent = panelContent;
        DemoUtils.SetPanelEnabled(this.panelContent,false);

        panelContent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                jsp.setBounds(62,36,panelContent.getWidth() - 65, 135);
                jsp.updateUI();

                jsp2.setBounds(62, 272,panelContent.getWidth() - 65, panelContent.getHeight() - 275 - 50);
                jsp2.updateUI();

                button2.setBounds(panelContent.getWidth() - 94, 180,91, 28);
                button_stop.setBounds(panelContent.getWidth() - 94,240,91, 28);
                label_code.setBounds(8, panelContent.getHeight() - 48,60, 17);
                jsp_code.setBounds(62,panelContent.getHeight() - 50,panelContent.getWidth() - 65, 48);
            }
        });
    }
}
