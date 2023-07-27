package HslCommunicationDemo.Demo;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Net.IReadWriteNet;
import HslCommunication.Core.Types.OperateResultExOne;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class BatchReadControl extends JPanel {
    public BatchReadControl( ){
        setLayout(null);
        JLabel label1 = new JLabel("Address：");
        label1.setBounds(9, 10,70, 17);
        add(label1);

        textBox_address = new JTextField();
        textBox_address.setBounds(83,7,432, 23);
        textBox_address.setText(this.address);
        add(textBox_address);

        JLabel label2 = new JLabel("Length：");
        label2.setBounds(535, 10,60, 17);
        add(label2);

        textBox_length = new JTextField();
        textBox_length.setBounds(584,7,180, 23);
        textBox_length.setText("10");
        add(textBox_length);


        JLabel label3 = new JLabel("Result：");
        label3.setBounds(9, 38,70, 17);
        add(label3);

        textAreaResult = new JTextArea();
        textAreaResult.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textAreaResult);
        jsp.setBounds(83,36,425, 78);
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

        JButton button2 = new JButton("Read");
        button2.setFocusPainted(false);
        button2.setBounds(436,4,72, 28);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button2.isEnabled() == false) return;
                super.mouseClicked(e);
                Date now = new Date();
                OperateResultExOne<byte[]> read = readWriteNet.Read(textBox_address.getText(),Short.parseShort(textBox_length.getText()));
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
        add(button2);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                button2.setBounds(getWidth() - 80, 4, 72, 28);
                jsp.setBounds(83,36,getWidth() - 90, getHeight() - 60);

                panelFormat.setBounds( 83, getHeight() - 28, 130, 27 );
                jsp.updateUI();

                label_read_length.setBounds(270, getHeight() - 27, 100, 27);
                label_read_time.setBounds(370, getHeight() - 27, 100, 27);
            }
        });
    }

    private void SetTimeSpend(Date last)
    {
        long mill = new Date().getTime() - last.getTime();
        label_read_time.setText( "Time: " + mill + " ms");
    }

    public void SetReadWriteNet( IReadWriteNet readWrite, String address, int strLength ){
        this.address = address;
        this.readWriteNet = readWrite;
        this.textBox_address.setText(address);
        this.textBox_length.setText(String.valueOf(strLength));
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
        textBox_address.setEnabled(enabled);
        textBox_length.setEnabled(enabled);
        textAreaResult.setEnabled(enabled);
    }

    private JTextField textBox_address;
    private JTextField textBox_length;
    private String address = "";
    private IReadWriteNet readWriteNet;
    private JTextArea textAreaResult;
    private JLabel label_read_length;
    private JLabel label_read_time;

}
