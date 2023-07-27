package cn.hslcommunication.HslCommunicationDemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserControlReadWriteHead extends JPanel
{
    public UserControlReadWriteHead( String protocol, JTabbedPane tabbedPane, JPanel panel ){
        setLayout(null);
        setBackground(FormMain.ThemeColor);

        JLabel label1 = new JLabel("Home：");
        label1.setForeground(new Color(233,233,233));
        label1.setBounds(5, 3,68, 17);
        add(label1);

        JLabel label5 = new JLabel("http://www.hsltechnology.cn/");
        label5.setForeground(new Color(233,233,233));
        label5.setBounds(75, 3,400, 17);

        add(label5);

        JLabel label2 = new JLabel( (FormMain.Language == 1 ? "使用协议:" : "Protocols:"));
        label2.setForeground(new Color(233,233,233));
        label2.setBounds(416, 3,68, 17);
        add(label2);

        JLabel label3 = new JLabel(protocol);
        label3.setForeground(new Color(233,233,233));
        label3.setBounds(490, 3,200, 17);
        add(label3);

        JLabel label4 = new JLabel((FormMain.Language == 1 ? "关闭窗体" :"Close") );
        label4.setForeground(new Color(233,233,233));
        label4.setBounds(887, 3,108, 17);
        label4.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                tabbedPane.remove(panel);
            }
        });
        add(label4);

        setBounds(0, 0, 1200, 25);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                setBounds(0, 0, panel.getWidth(), 25);
                label4.setLocation( panel.getWidth() - 55, 3 );
            }
        });

    }

}
