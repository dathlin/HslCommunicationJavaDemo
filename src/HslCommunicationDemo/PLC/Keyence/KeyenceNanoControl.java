package HslCommunicationDemo.PLC.Keyence;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Keyence.KeyenceNanoSerialOverTcp;
import HslCommunication.Profinet.Keyence.KeyencePLCS;
import HslCommunicationDemo.DemoUtils;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class KeyenceNanoControl extends JPanel {

    private KeyenceNanoSerialOverTcp keyence;

    public KeyenceNanoControl() {
        setLayout(null);

        JLabel label13 = new JLabel("Result:");
        label13.setBounds(3, 37, 44, 17);
        add(label13);

        JTextArea textArea_data = new JTextArea();
        textArea_data.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea_data);
        jsp.setBounds(60, 35, 500, 100);
        add(jsp);

        JTextArea textArea_code = new JTextArea();
        textArea_code.setLineWrap(true);
        JScrollPane jsp_code = new JScrollPane(textArea_code);
        jsp_code.setBounds(60, 35, 500, 100);
        add(jsp_code);

        JLabel label14 = new JLabel("Code:");
        label14.setBounds(3, 168, 44, 17);
        add(label14);

        JLabel label1 = new JLabel("Address:");
        label1.setBounds(3, 8, 52, 17);
        add(label1);


        JTextField textBox6 = new JTextField();
        textBox6.setText( "D100" );
        textBox6.setBounds( 60, 5, 215, 23 );
        add(textBox6);

        JButton button4 = new JButton( "注释" );
        button4.setBounds( 280, 2, 72, 28 );
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (keyence == null) return;;

                OperateResultExOne<String> read = keyence.ReadAddressAnnotation(textBox6.getText());
                if (read.IsSuccess){
                    textArea_data.setText( textBox6.getText() + " Annotation: " + read.Content );
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
                textArea_code.setText( "OperateResultExOne<String> read = keyence.ReadAddressAnnotation(\"" + textBox6.getText() + "\");" );
            }
        });
        add(button4);

        JButton button3 = new JButton( "PlcType" );
        button3.setBounds( 360, 2, 100, 28 );
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (keyence == null) return;;

                OperateResultExOne<KeyencePLCS> read = keyence.ReadPlcType();
                if (read.IsSuccess){
                    textArea_data.setText( "PLC type: " + read.Content.toString() );
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
                textArea_code.setText( "OperateResultExOne<KeyencePLCS> read = keyence.ReadPlcType();" );
            }
        });
        add(button3);

        JButton button_clear = new JButton( "Clear" );
        button_clear.setBounds( 470, 2, 100, 28 );
        button_clear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (keyence == null) return;;

                OperateResult result = keyence.ClearError();
                if (result.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Clear Success" ,
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Clear Failed:" + result.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
                textArea_code.setText( "OperateResult result = keyence.ClearError();" );
            }
        });
        add(button_clear);

        JButton button_mode = new JButton( "PlcMode" );
        button_mode.setBounds( 580, 2, 100, 28 );
        button_mode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (keyence == null) return;;

                OperateResultExOne<Integer> read = keyence.ReadPlcMode();
                if (read.IsSuccess){
                    textArea_data.setText( "PLC Mode: " + read.Content.toString() + "     // 如果是0，代表 PROG模式或者梯形图未登录，如果为1，代表RUN模式" );
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
                textArea_code.setText( "OperateResultExOne<Integer> read = keyence.ReadPlcMode();" );
            }
        });
        add(button_mode);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                jsp.setBounds(60, 35, getWidth() - 63, getHeight() - 35 - 5 - 50);
                label14.setBounds(3, getHeight() - 50, 44, 17);

                jsp_code.setBounds(60, getHeight() - 53, getWidth() - 63, 50);
            }
        });
    }

    public void SetPlc( KeyenceNanoSerialOverTcp keyence ){
        this.keyence = keyence;
    }

}
