package HslCommunicationDemo;

import HslCommunication.Core.Net.IReadWriteNet;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.*;
import java.util.Date;

public class UserControlReadWriteOp extends JPanel {
    public UserControlReadWriteOp() {
        super();

        AddRead(this);
        AddWrite(this);
    }

    public void SetReadWriteNet( IReadWriteNet readWrite, String address, int strLength ) {
        this.address = address;
        if (textBox3 != null) textBox3.setText(address);
        if (textBox8 != null) textBox8.setText(address);
        if (textBox1 != null) textBox1.setText(String.valueOf(strLength));

        readWriteNet = readWrite;
        try {
            readByteMethod = readWrite.getClass().getMethod("ReadByte", String.class);
        } catch (Exception ex) {

        }
        if (readByteMethod == null) button_read_byte.setEnabled(false);

        try {
            writeByteMethod = readWrite.getClass().getMethod("Write", String.class, byte.class);
            if (writeByteMethod == null) button23.setEnabled(false);
        } catch (Exception ex) {
            button23.setEnabled(false);
        }
    }

    private String address = "";
    private IReadWriteNet readWriteNet;
    private Method readByteMethod = null;
    private Method writeByteMethod = null;

    private JTextField textBox3;
    private JTextField textBox8;
    private JTextField textBox1;
    private JButton button_read_byte;
    private JButton button23;

    public void AddRead(JPanel panel){
        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);
        panelRead.setBounds(0,0,518, 234);
        panelRead.setBorder(BorderFactory.createTitledBorder( "Read Single Test"));

        JLabel label1 = new JLabel("Address：");
        label1.setBounds(9, 30,70, 17);
        panelRead.add(label1);


        JTextField textField1 = new JTextField();
        textField1.setBounds(83,27,190, 23);
        textField1.setText(address);
        panelRead.add(textField1);

        textBox3 = textField1;

        JTextField textField_ReadLength = new JTextField();
        textField_ReadLength.setBounds(280,27,42, 23);
        textField_ReadLength.setText("1");
        panelRead.add(textField_ReadLength);

        JLabel label2 = new JLabel("Result：");
        label2.setBounds(9, 58,70, 17);
        panelRead.add(label2);

        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea1);
        jsp.setBounds(83,56,238, 165);
        panelRead.add(jsp);

        int leftButtonX = 335;
        JButton button1 = new JButton("r-bit");
        button1.setFocusPainted(false);
        button1.setBounds( leftButtonX,19,82, 28);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button1.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                if (textField_ReadLength.getText().equals("1")) {
                    OperateResultExOne<Boolean> read = readWriteNet.ReadBool(textField1.getText());
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                } else {
                    OperateResultExOne<boolean[]> read = readWriteNet.ReadBool(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
            }
        });
        panelRead.add(button1);

        JButton button_read_byte = new JButton("r-byte");
        button_read_byte.setFocusPainted(false);
        button_read_byte.setBounds(leftButtonX + 90,19,82, 28);
        button_read_byte.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button_read_byte.isEnabled()) return;
                super.mouseClicked(e);
                try{
                    Date now = new Date();
                    OperateResultExOne<Byte> read = (OperateResultExOne<Byte>)readByteMethod.invoke(readWriteNet, textField1.getText());
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button_read_byte);
        this.button_read_byte = button_read_byte;

        JButton button3 = new JButton("r-short");
        button3.setFocusPainted(false);
        button3.setBounds(leftButtonX,56,82, 28);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button3.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                if (textField_ReadLength.getText().equals("1")) {
                    OperateResultExOne<Short> read = readWriteNet.ReadInt16(textField1.getText());
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
                else {
                    OperateResultExOne<short[]> read = readWriteNet.ReadInt16(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
            }
        });
        panelRead.add(button3);

        JButton button4 = new JButton("r-ushort");
        button4.setFocusPainted(false);
        button4.setBounds(leftButtonX + 90,56,82, 28);
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button4.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                if (textField_ReadLength.getText().equals("1")) {
                    OperateResultExOne<Integer> read = readWriteNet.ReadUInt16(textField1.getText());
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
                else {
                    OperateResultExOne<int[]> read = readWriteNet.ReadUInt16(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
            }
        });
        panelRead.add(button4);

        JButton button5 = new JButton("r-int");
        button5.setFocusPainted(false);
        button5.setBounds(leftButtonX,90,82, 28);
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button5.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                if (textField_ReadLength.getText().equals("1")) {
                    OperateResultExOne<Integer> read = readWriteNet.ReadInt32(textField1.getText());
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
                else {
                    OperateResultExOne<int[]> read = readWriteNet.ReadInt32(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
            }
        });
        panelRead.add(button5);

        JButton button6 = new JButton("r-uint");
        button6.setFocusPainted(false);
        button6.setBounds(leftButtonX + 90,90,82, 28);
        button6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button6.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                if (textField_ReadLength.getText().equals("1")) {
                    OperateResultExOne<Long> read = readWriteNet.ReadUInt32(textField1.getText());
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
                else {
                    OperateResultExOne<long[]> read = readWriteNet.ReadUInt32(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
            }
        });
        panelRead.add(button6);

        JButton button7 = new JButton("r-long");
        button7.setFocusPainted(false);
        button7.setBounds(leftButtonX,124,82, 28);
        button7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button7.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                if (textField_ReadLength.getText().equals("1")) {
                    OperateResultExOne<Long> read = readWriteNet.ReadInt64(textField1.getText());
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
                else {
                    OperateResultExOne<long[]> read = readWriteNet.ReadInt64(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
            }
        });
        panelRead.add(button7);

        JButton button8 = new JButton("r-ulong");
        button8.setFocusPainted(false);
        button8.setVisible(false);
        button8.setBounds(leftButtonX + 90,124,82, 28);
        panelRead.add(button8);

        JButton button9 = new JButton("r-float");
        button9.setFocusPainted(false);
        button9.setBounds(leftButtonX,158,82, 28);
        button9.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button9.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                if (textField_ReadLength.getText().equals("1")) {
                    OperateResultExOne<Float> read = readWriteNet.ReadFloat(textField1.getText());
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
                else {
                    OperateResultExOne<float[]> read = readWriteNet.ReadFloat(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
            }
        });
        panelRead.add(button9);

        JButton button10 = new JButton("r-double");
        button10.setFocusPainted(false);
        button10.setBounds(leftButtonX + 90,158,82, 28);
        button10.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button10.isEnabled() == false) return;
                super.mouseClicked(e);
                Date now = new Date();
                if (textField_ReadLength.getText().equals("1")) {
                    OperateResultExOne<Double> read = readWriteNet.ReadDouble(textField1.getText());
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
                else {
                    OperateResultExOne<double[]> read = readWriteNet.ReadDouble(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                }
            }
        });
        panelRead.add(button10);


        JLabel label8 = new JLabel("Length:");
        label8.setBounds(leftButtonX,198,55, 17);
        panelRead.add(label8);

        JTextField textField8 = new JTextField();
        textField8.setBounds(leftButtonX + 43,195,41, 23);
        textField8.setText("10");
        panelRead.add(textField8);

        textBox1 = textField8;

        JButton button11 = new JButton("r-string");
        button11.setFocusPainted(false);
        button11.setBounds(leftButtonX + 90,192,82, 28);
        button11.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button11.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                OperateResultExOne<String> read = readWriteNet.ReadString(textField1.getText(), Short.parseShort(textField8.getText()));
                SetTimeSpend(now);
                DemoUtils.ReadResultRender(read,textField1.getText(), textArea1, jsp );
            }
        });
        panelRead.add(button11);

        panel.add(panelRead);
    }

    public void AddWrite(JPanel panel){
        JPanel panelWrite = new JPanel();
        panelWrite.setLayout(null);
        panelWrite.setBounds(535,0,419, 234);
        panelWrite.setBorder(BorderFactory.createTitledBorder( "Write Single Test"));

        JLabel label1 = new JLabel("Address：");
        label1.setBounds(9, 30,70, 17);
        panelWrite.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(83,27,132, 23);
        textField1.setText(address);
        panelWrite.add(textField1);

        textBox8 = textField1;

        JLabel label2 = new JLabel("Value：");
        label2.setBounds(9, 58,70, 17);
        panelWrite.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(83,56,132, 23);
        panelWrite.add(textField2);

        JLabel label100 = new JLabel("<html>Note: The value of the string needs to be converted</html>");
        label100.setBounds(11, 82,200, 100);
        panelWrite.add(label100);

        JButton button1 = new JButton("w-bit");
        button1.setFocusPainted(false);
        button1.setBounds(226,24,82, 28);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button1.isEnabled()) return;
                super.mouseClicked(e);
                try {
                    Date now = new Date();
                    OperateResult write = readWriteNet.Write(textField1.getText(), Boolean.parseBoolean(textField2.getText()));
                    SetTimeSpend(now);
                    DemoUtils.WriteResultRender(write, textField1.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelWrite.add(button1);

        JButton button2 = new JButton("w-byte");
        button2.setFocusPainted(false);
        button2.setBounds(323,24,86, 28);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button2.isEnabled()) return;
                super.mouseClicked(e);
                try {
                    Date now = new Date();
                    OperateResult write = (OperateResult)writeByteMethod.invoke(readWriteNet, textField1.getText(), Byte.parseByte(textField2.getText()));
                    SetTimeSpend(now);
                    DemoUtils.WriteResultRender(write, textField1.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelWrite.add(button2);
        this.button23 = button2;

        JButton button3 = new JButton("w-short");
        button3.setFocusPainted(false);
        button3.setBounds(226,61,82, 28);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button3.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    OperateResult write = readWriteNet.Write(textField1.getText(), Short.parseShort(textField2.getText()));
                    SetTimeSpend(now);
                    DemoUtils.WriteResultRender(write, textField1.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelWrite.add(button3);

        JButton button4 = new JButton("w-ushort");
        button4.setFocusPainted(false);
        button4.setBounds(323,61,86, 28);
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button4.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    int value = Integer.parseInt(textField2.getText());
                    if(value < 0 || value > 65535){
                        throw new Exception("Value must be between 0 - 65535");
                    }
                    OperateResult write = readWriteNet.Write(textField1.getText(), (short) value);
                    SetTimeSpend(now);
                    DemoUtils.WriteResultRender(write, textField1.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelWrite.add(button4);

        JButton button5 = new JButton("w-int");
        button5.setFocusPainted(false);
        button5.setBounds(226,95,82, 28);
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button5.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    OperateResult write = readWriteNet.Write(textField1.getText(), Integer.parseInt(textField2.getText()));
                    SetTimeSpend(now);
                    DemoUtils.WriteResultRender(write, textField1.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelWrite.add(button5);

        JButton button6 = new JButton("w-uint");
        button6.setFocusPainted(false);
        button6.setBounds(323,95,86, 28);
        button6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button6.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    long value = Long.parseLong(textField2.getText());
                    if(value < 0L || value > 0xffffffffL){
                        throw new Exception("Value must be between 0 - 4294967295");
                    }
                    OperateResult write = readWriteNet.Write(textField1.getText(), (int)value);
                    SetTimeSpend(now);
                    DemoUtils.WriteResultRender(write, textField1.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelWrite.add(button6);

        JButton button7 = new JButton("w-long");
        button7.setFocusPainted(false);
        button7.setBounds(226,129,82, 28);
        button7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button7.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    OperateResult write = readWriteNet.Write(textField1.getText(), Long.parseLong(textField2.getText()));
                    SetTimeSpend(now);
                    DemoUtils.WriteResultRender(write, textField1.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelWrite.add(button7);

        JButton button8 = new JButton("w-ulong");
        button8.setFocusPainted(false);
        button8.setVisible(false);
        button8.setBounds(323,129,86, 28);
        panelWrite.add(button8);

        JButton button9 = new JButton("w-float");
        button9.setFocusPainted(false);
        button9.setBounds(226,163,82, 28);
        button9.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button9.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    OperateResult write = readWriteNet.Write(textField1.getText(), Float.parseFloat(textField2.getText()));
                    SetTimeSpend(now);
                    DemoUtils.WriteResultRender(write, textField1.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelWrite.add(button9);

        JButton button10 = new JButton("w-double");
        button10.setMargin(new Insets(0,0,0,0));
        button10.setFocusPainted(false);
        button10.setBounds(323,163,86, 28);
        button10.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button10.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    OperateResult write = readWriteNet.Write(textField1.getText(), Double.parseDouble(textField2.getText()));
                    SetTimeSpend(now);
                    DemoUtils.WriteResultRender(write, textField1.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelWrite.add(button10);

        JButton button11 = new JButton("w-string");
        button11.setFocusPainted(false);
        button11.setBounds(323,197,86, 28);
        button11.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button11.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    OperateResult write = readWriteNet.Write(textField1.getText(), textField2.getText());
                    SetTimeSpend(now);
                    DemoUtils.WriteResultRender(write, textField1.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelWrite.add(button11);
        label_TimeCost = new JLabel(TimeCost);
        label_TimeCost.setBounds(9,181,150,21);
        panelWrite.add(label_TimeCost);

        panel.add(panelWrite);
    }

    private void SetTimeSpend(Date last)
    {
        long mill = new Date().getTime() - last.getTime();
        label_TimeCost.setText(TimeCost + mill + " ms");
    }

    private JLabel label_TimeCost;
    private final String TimeCost = "Time-Cost:";
}
