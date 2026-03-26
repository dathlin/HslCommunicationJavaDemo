package HslCommunicationDemo.Demo;

import HslCommunication.Core.Types.Convert;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Delta.DeltaTcpNet;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.HslJPanel;
import HslCommunicationDemo.PLC.Delta.DeltaHelper;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class FormActive extends HslJPanel {

    public FormActive(){
        setLayout(null);

        setSize(480, 380);

        JLabel label2 = new JLabel( "MachineCode:" );
        label2.setBounds( 20, 7, 100, 20 );
        add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds( 120, 10, getWidth() - 130, 20 );
        textField2.setText( HslCommunication.BasicFramework.SoftAuthorize.GetInfo() );
        add(textField2);

        JLabel label1 = new JLabel( "ActiveCode:" );
        label1.setBounds( 20, 30, 100, 20 );
        add(label1);

        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        JScrollPane scrollPane1 = new JScrollPane(textArea1);
        scrollPane1.setBounds(20,50,getWidth() - 30, 140);
        add(scrollPane1);

        JTextArea textArea_code = new JTextArea();
        textArea_code.setLineWrap(true);
        JScrollPane scrollPane_code = new JScrollPane(textArea_code);
        scrollPane_code.setBounds(20,getHeight() - 130,getWidth() - 30, 90);
        add(scrollPane_code);

        JButton button1 = new JButton("Active");
        button1.setBounds(getWidth()/2 - 75, getHeight() - 80 - 100, 150, 40);
        add(button1);

        button1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                String code = textArea1.getText();
                if (code.length() < 100)
                {
                    if (HslCommunication.Authorization.SetAuthorizationCode(code)){
                        JOptionPane.showMessageDialog(
                                null,
                                "Active Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Active failed!",
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    textArea_code.setText( "if (HslCommunication.Authorization.SetAuthorizationCode(\"" + code + "\")){\r\n" +
                            "    System.out.println(\"Active success! [激活成功]\");\r\n" +
                            "} else {\r\n" +
                            "    System.out.println(\"Active failed, it can be use only 24 hours! [激活失败，当前的jar包只能连续使用24小时]\");\r\n" +
                            "}" );
                }
                else {
                    OperateResult active = HslCommunication.Authorization.SetHslCertificate(Convert.FromBase64String(code));
                    if (active.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Active Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Active failed: " + active.Message,
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    textArea_code.setText( "OperateResult active = HslCommunication.Authorization.SetHslCertificate(Convert.FromBase64String(\"" + code + "\"));\r\n" +
                            "if (active.IsSuccess){\r\n" +
                            "    System.out.println(\"Active success! [激活成功]\");\r\n" +
                            "} else {\r\n" +
                            "    System.out.println(\"Active failed: \" + active.Message);\r\n" +
                            "}" );
                }
            }
        });
    }

}
