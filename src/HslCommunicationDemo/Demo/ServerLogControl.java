package HslCommunicationDemo.Demo;

import HslCommunicationDemo.DemoUtils;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Date;

public class ServerLogControl extends JPanel {
    public ServerLogControl(){
        setLayout(null);

        JLabel label1 = new JLabel("Online：");
        label1.setBounds(9, 7,70, 17);
        add(label1);

        textFieldOnline = new JTextField();
        textFieldOnline.setBounds(83,4,82, 23);
        textFieldOnline.setText("0");
        add(textFieldOnline);

        renderLogCheckBox = new JCheckBox();
        renderLogCheckBox.setBounds( 200, 4, 300, 23 );
        renderLogCheckBox.setText("Show Log?");
        renderLogCheckBox.setSelected(true);
        add(renderLogCheckBox);

        JLabel label3 = new JLabel("Result：");
        label3.setBounds(9, 35,70, 17);
        add(label3);

        textAreaLog = new JTextArea();
        textAreaLog.setLineWrap(true);
        scrollPaneLog = new JScrollPane(textAreaLog);
        scrollPaneLog.setBounds(83,33,870, 230);
        add(scrollPaneLog);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                scrollPaneLog.setBounds(83,33,getWidth() - 85, getHeight() - 35);
            }
        });
    }
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textFieldOnline.setEnabled(enabled);
        textAreaLog.setEnabled(enabled);
        renderLogCheckBox.setEnabled(enabled);
    }

    public void SetOnlineText( int count ) {
        textFieldOnline.setText(String.valueOf(count));
    }

    /**
     * 写日志信息
     * @param keyWord 关键字
     * @param text 消息信息
     */
    public void WriteDebug(String keyWord, String text) {
        if (renderLogCheckBox.isSelected()) {
            textAreaLog.append(DemoUtils.dateFormat.format(new Date()) + " " + keyWord + " " + text + "\r\n");
            JScrollBar scrollBar = scrollPaneLog.getVerticalScrollBar();
            scrollBar.setValue(scrollBar.getMaximum());
        }
    }


    private JTextField textFieldOnline;
    private JTextArea textAreaLog;
    private JScrollPane scrollPaneLog;
    private JCheckBox renderLogCheckBox;
}
