package cn.hslcommunication.HslCommunicationDemo.Demo;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Net.IReadWriteNet;
import HslCommunication.Core.Net.NetworkBase.NetworkDoubleBase;
import HslCommunication.Core.Net.NetworkBase.NetworkUdpBase;
import HslCommunication.Core.Types.OperateResultExOne;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class MessageReadControl extends JPanel {
    public MessageReadControl( ){
        setLayout(null);
        JLabel label1 = new JLabel("Message：");
        label1.setBounds(9, 10,70, 17);
        add(label1);

        textBox_message = new JTextArea();
        textBox_message.setLineWrap(true);
        textBox_message.setText(this.address);
        JScrollPane jsp1 = new JScrollPane(textBox_message);
        jsp1.setBounds(83,7,425, 83);
        add(jsp1);


        JLabel label3 = new JLabel("Result：");
        label3.setBounds(9, 38,70, 17);
        add(label3);

        textAreaResult = new JTextArea();
        textAreaResult.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textAreaResult);
        jsp.setBounds(83,96,425, 78);
        add(jsp);

        JPanel panelFormat = new JPanel(null);
        JRadioButton radioButton_binary = new JRadioButton("Binary");
        radioButton_binary.setSelected(true);
        radioButton_binary.setBounds( 0, 3, 70, 25 );
        panelFormat.add(radioButton_binary);
        JRadioButton radioButton_ascii = new JRadioButton("Ascii");
        radioButton_ascii.setBounds( 73, 3, 60, 25 );
        panelFormat.add(radioButton_ascii);
        panelFormat.setBounds( 83, 260, 150, 27 );
        add(panelFormat);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButton_binary);
        buttonGroup.add(radioButton_ascii);


        label_read_length = new JLabel("Length: ");
        label_read_length.setBounds(270, 261, 100, 27);
        add(label_read_length);

        label_read_time = new JLabel("Time: ");
        label_read_time.setBounds(370, 261, 100, 27);
        add(label_read_time);


        JLabel label_info = new JLabel("Info: Please enter the complete message data in the Message textBox.");
        label_info.setBounds(470, 261, 300, 27);
        label_info.setForeground(Color.lightGray);
        add(label_info);

        buttonRead = new JButton("Read");
        buttonRead.setFocusPainted(false);
        buttonRead.setBounds(436,4,72, 28);
        buttonRead.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (buttonRead.isEnabled() == false) return;
                super.mouseClicked(e);

                if (readWriteNet == null && readWriteUdp == null){
                    JOptionPane.showMessageDialog(
                            null,
                            "Communication Object is null",
                            "Result",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Date now = new Date();

                OperateResultExOne<byte[]> read = null;
                if (readWriteNet != null) {
                    read = readWriteNet.ReadFromCoreServer(
                            radioButton_binary.isSelected() ? SoftBasic.HexStringToBytes(textBox_message.getText()) : SoftBasic.GetFromAsciiStringRender(textBox_message.getText()), true, false);
                }
                else if (readWriteUdp!=null){
                    read = readWriteUdp.ReadFromCoreServer(
                            radioButton_binary.isSelected() ? SoftBasic.HexStringToBytes(textBox_message.getText()) : SoftBasic.GetFromAsciiStringRender(textBox_message.getText()), true, false);
                }
                SetTimeSpend(now);
                if(read.IsSuccess){
                    if (radioButton_binary.isSelected()){
                        textAreaResult.setText(SoftBasic.ByteToHexString(read.Content, ' '));
                    }
                    else {
                        textAreaResult.setText(SoftBasic.GetAsciiStringRender(read.Content));
                    }
                    label_read_length.setText("Length: " +read.Content.length );
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(buttonRead);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                buttonRead.setBounds(getWidth() - 80, 4, 72, 28);
                jsp.setBounds(83,96,getWidth() - 90 - 80, getHeight() - 120);
                jsp1.setBounds(83,7,getWidth() - 90 - 80, 83);

                panelFormat.setBounds( 83, getHeight() - 28, 130, 27 );
                jsp.updateUI();
                jsp1.updateUI();

                label_read_length.setBounds(270, getHeight() - 27, 100, 27);
                label_read_time.setBounds(370, getHeight() - 27, 100, 27);
                label_info.setBounds(470, getHeight() - 27, 400, 27);
            }
        });
    }

    private void SetTimeSpend(Date last)
    {
        long mill = new Date().getTime() - last.getTime();
        label_read_time.setText( "Time: " + mill + " ms");
    }

    public void SetReadWriteNet(NetworkDoubleBase readWrite, String message ){
        this.address = message;
        this.readWriteNet = readWrite;
        this.textBox_message.setText(message);
    }

    public void SetReadWriteNet(NetworkUdpBase readWrite, String message ){
        this.address = message;
        this.readWriteUdp = readWrite;
        this.textBox_message.setText(message);
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
        textBox_message.setEnabled(enabled);
        textAreaResult.setEnabled(enabled);
        buttonRead.setEnabled(enabled);
    }

    private JTextArea textBox_message;
    private String address = "";
    private NetworkDoubleBase readWriteNet;
    private NetworkUdpBase readWriteUdp;
    private JTextArea textAreaResult;
    private JLabel label_read_length;
    private JLabel label_read_time;
    private JButton buttonRead;
}
