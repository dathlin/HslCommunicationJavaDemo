package HslCommunicationDemo.Instrument;

import HslCommunication.Core.Types.HslExtension;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Instrument.DLT.Helper.IDlt645;
import HslCommunication.Profinet.Melsec.Helper.IReadWriteMc;
import HslCommunication.Utilities;
import HslCommunicationDemo.DemoUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class Dlt645Control extends JPanel {
    public Dlt645Control(){
        setLayout(null);
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea);
        jsp.setBounds( 10, 88, 500, 92 );
        add(jsp);

        JLabel labelCode = new JLabel("Code: ");
        labelCode.setBounds(10, 180, 80, 17);
        add(labelCode);

        JTextArea textAreaCode = new JTextArea();
        textAreaCode.setLineWrap(true);
        JScrollPane jspCode = new JScrollPane(textAreaCode);
        jspCode.setBounds( 10, 177, 470, 40 );
        add(jspCode);

        JButton button1 = new JButton("唤醒接收");
        button1.setFocusPainted(false);
        button1.setBounds(10 ,10, 120, 28);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button1.isEnabled()) return;
                super.mouseClicked(e);
                if (dlt == null) return;

                OperateResult result = dlt.ActiveDevice();
                DemoUtils.OpResultRender(result, "ActiveDevice");
                textAreaCode.setText("OperateResult result = dlt.ActiveDevice();");
            }
        });
        add(button1);

        JButton button2 = new JButton("读取通信地址");
        button2.setFocusPainted(false);
        button2.setBounds(140 ,10, 120, 28);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button2.isEnabled()) return;
                super.mouseClicked(e);
                if (dlt == null) {
                    return;
                }

                OperateResultExOne<String> read = dlt.ReadAddress();
                if (read.IsSuccess){
                    textArea.setText("Address: " + read.Content);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Failed，Reason:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.WARNING_MESSAGE);
                }
                textAreaCode.setText("OperateResultExOne<String> read = dlt.ReadAddress();");
            }
        });
        add(button2);

        JButton button3 = new JButton("广播时间");
        button3.setFocusPainted(false);
        button3.setBounds(270 ,10, 120, 28);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button3.isEnabled()) return;
                super.mouseClicked(e);
                if (dlt == null) {
                    return;
                }

                OperateResult read = dlt.BroadcastTime( new Date() );
                if (read.IsSuccess){
                    textArea.setText(HslExtension.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss") + "BroadcastTime Success");
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Failed，Reason:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.WARNING_MESSAGE);
                }
                textAreaCode.setText("OperateResult read = dlt.BroadcastTime( new Date() );");
            }
        });
        add(button3);


        JLabel labelAddress = new JLabel("Address: ");
        labelAddress.setBounds(10, 55, 80, 17);
        add(labelAddress);

        JTextField textFieldAddress = new JTextField();
        textFieldAddress.setBounds(100, 52, 120, 23);
        add(textFieldAddress);


        JButton button5 = new JButton("写入通信地址");
        button5.setFocusPainted(false);
        button5.setBounds(400 ,10, 120, 28);
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button5.isEnabled()) return;
                super.mouseClicked(e);
                if (dlt == null) {
                    return;
                }

                OperateResult read = dlt.WriteAddress( textFieldAddress.getText() );
                if (read.IsSuccess){
                    textArea.setText(HslExtension.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss") + "Write Address Success");
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Failed，Reason:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.WARNING_MESSAGE);
                }
                textAreaCode.setText("OperateResult read = dlt.WriteAddress( \"" + textFieldAddress.getText() + "\" );");
            }
        });
        add(button5);

        JButton button4 = new JButton("读取原始字符串");
        button4.setFocusPainted(false);
        button4.setBounds(230 ,48, 150, 28);
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button4.isEnabled()) return;
                super.mouseClicked(e);
                if (dlt == null) {
                    return;
                }

                OperateResultExOne<String[]> read = dlt.ReadStringArray( textFieldAddress.getText() );
                if (read.IsSuccess){
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append( HslExtension.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss") );
                    stringBuilder.append( " Read Result:" );
                    stringBuilder.append( "\r\n" );
                    for( int i = 0; i < read.Content.length; i++ )
                    {
                        stringBuilder.append( read.Content[i] );
                        stringBuilder.append( "\r\n" );
                    }
                    textArea.setText(stringBuilder.toString() );
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Failed，Reason:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.WARNING_MESSAGE);
                }
                textAreaCode.setText("OperateResultExOne<String[]> read = dlt.ReadStringArray( \"" + textFieldAddress.getText() + "\" );");
            }
        });
        add(button4);



        JLabel label100 = new JLabel("有效时间: ");
        label100.setBounds(580, 12, 80, 17);
        add(label100);

        JTextField textFieldTime = new JTextField();
        textFieldTime.setBounds(680, 10, 150, 23);
        textFieldTime.setText(HslExtension.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        add(textFieldTime);

        JButton button7 = new JButton("跳闸");
        button7.setFocusPainted(false);
        button7.setBounds(580 ,40, 60, 28);
        button7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button5.isEnabled()) return;
                super.mouseClicked(e);
                if (dlt == null) {
                    return;
                }

                OperateResult read = dlt.Trip( HslExtension.StringToDate( textFieldTime.getText(), "yyyy-MM-dd HH:mm:ss" ) );
                if (read.IsSuccess){
                    textArea.setText(HslExtension.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss") + "Trip Success");
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Failed，Reason:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.WARNING_MESSAGE);
                }
                textAreaCode.setText("OperateResult read = dlt.Trip( HslExtension.StringToDate( \"" + textFieldTime.getText() + "\" , \"yyyy-MM-dd HH:mm:ss\" ) );");
            }
        });
        add(button7);


        JButton button8 = new JButton("允许合闸");
        button8.setFocusPainted(false);
        button8.setBounds(650 ,40, 120, 28);
        button8.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button5.isEnabled()) return;
                super.mouseClicked(e);
                if (dlt == null) {
                    return;
                }

                OperateResult read = dlt.SwitchingOn( HslExtension.StringToDate( textFieldTime.getText(), "yyyy-MM-dd HH:mm:ss" ) );
                if (read.IsSuccess){
                    textArea.setText(HslExtension.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss") + "SwitchingOn Success");
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Failed，Reason:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.WARNING_MESSAGE);
                }
                textAreaCode.setText("OperateResult read = dlt.SwitchingOn( HslExtension.StringToDate( \"" + textFieldTime.getText() + "\" , \"yyyy-MM-dd HH:mm:ss\" ) );");
            }
        });
        add(button8);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                jsp.setBounds( 10, 88, getWidth() - 20, getHeight() - 55 - 90);
                labelCode.setBounds(10, getHeight() - 47, 80, 17);
                jspCode.setBounds( 70, getHeight() - 50, getWidth() - 80, 45 );
            }
        });
    }



    /**
     * Sets whether or not this component is enabled.
     * A component that is enabled may respond to user input,
     * while a component that is not enabled cannot respond to
     * user input.  Some components may alter their visual
     * representation when they are disabled in order to
     * provide feedback to the user that they cannot take input.
     * <p>Note: Disabling a component does not disable its children.
     *
     * <p>Note: Disabling a lightweight component does not prevent it from
     * receiving MouseEvents.
     *
     * @param enabled true if this component should be enabled, false otherwise
     * @beaninfo preferred: true
     * bound: true
     * attribute: visualUpdate true
     * description: The enabled state of the component.
     * @see Component#isEnabled
     * @see Component#isLightweight
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        DemoUtils.SetPanelEnabled(this, enabled);
    }

    public void SetReadWritePlc(IDlt645 dlt){
        this.dlt = dlt;
    }

    private IDlt645 dlt;
}
