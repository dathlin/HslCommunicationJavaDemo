package HslCommunicationDemo;

import HslCommunication.Core.Types.ActionOperateExTwo;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Enthernet.PushNet.NetPushClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.UUID;

public class FormPushNet extends JPanel {

    public FormPushNet(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Hsl Protocol", tabbedPane, this));
        AddConnectSegment(this);
        AddContent(this);
    }

    private NetPushClient netPushClient = null;
    private JPanel panelContent = null;
    private JLabel labelTime = null;
    private JLabel labelCount = null;
    private long receivedCount = 0;
    private JTextArea textArea = null;


    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 7,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,4,106, 23);
        textField1.setText("127.0.0.1");
        panelConnect.add(textField1);

        JLabel label2 = new JLabel("Port：");
        label2.setBounds(184, 7,56, 17);
        panelConnect.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(238,4,61, 23);
        textField2.setText("12345");
        panelConnect.add(textField2);


        JLabel label5 = new JLabel("Keyword：");
        label5.setBounds(310, 7,80, 17);
        panelConnect.add(label5);

        JTextField textField5 = new JTextField();
        textField5.setBounds(400,4,61, 23);
        textField5.setText("A");
        panelConnect.add(textField5);


        JLabel label3 = new JLabel("Token：");
        label3.setBounds(8, 32,56, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(62,29,384, 23);
        textField3.setText("00000000-0000-0000-0000-000000000000");
        panelConnect.add(textField3);


        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(584,2,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(477,2,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    netPushClient = new NetPushClient(textField1.getText(), Integer.parseInt(textField2.getText()),textField5.getText());
                    netPushClient.Token = UUID.fromString(textField3.getText());
                    OperateResult create = netPushClient.CreatePush(new ActionOperateExTwo<NetPushClient, String>(){
                        @Override
                        public void Action(NetPushClient content1, String content2) {
                            receivedCount++;
                            Date now = new Date();
                            labelTime.setText("Receive Time：" + now.toString());
                            labelCount.setText(String.valueOf(receivedCount));
                            textArea.setText(content2);
                        }
                    });

                    if(create.IsSuccess){
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
                                "Connect Failed:" + create.ToMessageShowString(),
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
                if(netPushClient!=null){
                    netPushClient.ClosePush();
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

        JLabel label1 = new JLabel("Receive Time：");
        label1.setBounds(8, 11,300, 17);
        panelContent.add(label1);

        JLabel label2 = new JLabel("Count：");
        label2.setBounds(8, 39,200, 17);
        panelContent.add(label2);

        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea1);
        jsp.setBounds(11,64,955, 450);
        panelContent.add(jsp);


        panelContent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                jsp.setBounds(8,64,panelContent.getWidth() - 10, panelContent.getHeight() - 69);
                jsp.updateUI();
            }
        });


        panel.add(panelContent);
        this.panelContent = panelContent;
        this.labelTime = label1;
        this.labelCount = label2;
        this.textArea = textArea1;
        DemoUtils.SetPanelEnabled(this.panelContent,false);
    }
}
