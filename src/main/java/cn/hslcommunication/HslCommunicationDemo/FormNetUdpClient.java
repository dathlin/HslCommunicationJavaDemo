package cn.hslcommunication.HslCommunicationDemo;

import HslCommunication.Core.Net.NetHandle;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Enthernet.UdpNet.NetUdpClient;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;

public class FormNetUdpClient extends JPanel {

    public FormNetUdpClient(JTabbedPane tabbedPane){
        setLayout(null);

        add( new UserControlReadWriteHead("Hsl Protocol", tabbedPane, this));
        AddConnectSegment(this);
        AddContent(this);
    }

    private NetUdpClient simplifyClient = null;
    private JPanel panelContent = null;

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 7,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,4,106, 23);
        textField1.setText("192.168.0.10");
        panelConnect.add(textField1);

        JLabel label2 = new JLabel("Port：");
        label2.setBounds(184, 7,56, 17);
        panelConnect.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(238,4,61, 23);
        textField2.setText("12345");
        panelConnect.add(textField2);


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
                    simplifyClient = new NetUdpClient(textField1.getText(), Integer.parseInt(textField2.getText()));
                    simplifyClient.Token = UUID.fromString(textField3.getText());

                    DemoUtils.SetPanelEnabled(panelContent, true);
                    button2.setEnabled(true);
                    button1.setEnabled(false);
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
                if(simplifyClient!=null){
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    DemoUtils.SetPanelEnabled(panelContent,false);
                }
            }
        });


        panel.add(panelConnect);
    }

    public void AddContent(JPanel panel) {
        JPanel panelContent = DemoUtils.CreateContentPanel(panel);

        JLabel label1 = new JLabel("指令头：");
        label1.setBounds(8, 11, 60, 17);
        panelContent.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62, 7, 181, 23);
        textField1.setText("1");
        panelContent.add(textField1);

        JLabel label2 = new JLabel("举例：12345 或是 1.1.1");
        label2.setBounds(249, 11, 154, 17);
        panelContent.add(label1);

        JLabel label3 = new JLabel("数据：");
        label3.setBounds(8, 39, 60, 17);
        panelContent.add(label3);

        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea1);
        jsp.setBounds(62, 36, 892, 138);
        panelContent.add(jsp);

        JButton button1 = new JButton("send");
        button1.setFocusPainted(false);
        button1.setBounds(62, 180, 91, 28);
        panelContent.add(button1);

        JButton button2 = new JButton("Clear");
        button2.setFocusPainted(false);
        button2.setBounds(863, 180, 91, 28);
        panelContent.add(button2);


        JTextArea textArea2 = new JTextArea();
        textArea2.setLineWrap(true);
        JScrollPane jsp2 = new JScrollPane(textArea2);
        jsp2.setBounds(62, 214, 892, 292);
        panelContent.add(jsp2);


        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false) return;
                super.mouseClicked(e);

                NetHandle handle = new NetHandle(1, 1, 1);
                if (textField1.getText().indexOf('.') >= 0) {
                    String[] values = textField1.getText().split(".");
                    handle = new NetHandle(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                } else {
                    handle = new NetHandle(Integer.parseInt(textField1.getText()));
                }


                OperateResultExOne<String> read = simplifyClient.ReadFromServer(handle, textArea1.getText());
                if (read.IsSuccess) {
                    textArea2.append(read.Content + "\r\n");
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
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


        panelContent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                jsp.setBounds(62,36,panelContent.getWidth() - 65, 138);
                jsp.updateUI();

                jsp2.setBounds(62, 214,panelContent.getWidth() - 65, panelContent.getHeight() - 217);
                jsp2.updateUI();

                button2.setBounds(panelContent.getWidth() - 94, 180,91, 28);
            }
        });

        panel.add(panelContent);
        this.panelContent = panelContent;
        DemoUtils.SetPanelEnabled(this.panelContent, false);
    }
}
