package HslCommunicationDemo;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Net.IReadWriteNet;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.HslExtension;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Core.Types.ValueLimit;
import HslCommunication.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class UserControlReadWriteOp extends JPanel {
    public UserControlReadWriteOp( ) {
        super();
        setLayout(null);
        AddRead(this);
        AddWrite(this);
    }

    public UserControlReadWriteOp( JPanel parent ) {
        super();
        setLayout(null);
        AddRead(this);
        AddWrite(this);

        setBounds(3,3,962, 265);
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                setBounds(3,3,parent.getWidth() - 5, 265);
            }
        });
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

    public void EnableRKC( )
    {
        button_read_bool.setEnabled(false);
        button_read_byte.setEnabled(false);
        button_read_short.setEnabled(false);
        button_read_ushort.setEnabled(false);
        button_read_int.setEnabled(false);
        button_read_uint.setEnabled(false);
        button_read_long.setEnabled(false);
        button_read_float.setEnabled(false);
        button_read_string.setEnabled(false);

        button_write_bool.setEnabled(false);
        button_write_byte.setEnabled(false);
        button_write_short.setEnabled(false);
        button_write_ushort.setEnabled(false);
        button_write_int.setEnabled(false);
        button_write_uint.setEnabled(false);
        button_write_long.setEnabled(false);
        button_write_float.setEnabled(false);
        button_write_hex.setEnabled(false);
        button_write_string.setEnabled(false);
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

    private JButton button_read_bool;
    private JButton button_read_short;
    private JButton button_read_ushort;
    private JButton button_read_int;
    private JButton button_read_uint;
    private JButton button_read_long;
    private JButton button_read_float;
    private JButton button_read_string;
    private JButton button_write_bool;
    private JButton button_write_byte;
    private JButton button_write_short;
    private JButton button_write_ushort;
    private JButton button_write_int;
    private JButton button_write_uint;
    private JButton button_write_long;
    private JButton button_write_float;
    private JButton button_write_hex;
    private JButton button_write_string;

    public void AddRead(JPanel panel){
        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);
        panelRead.setBounds(0,0,570, panel.getHeight() - 2);
        panelRead.setBorder(BorderFactory.createTitledBorder( "Read Single Test"));
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panelRead.setBounds(0,0,panel.getWidth() - 420, panel.getHeight() - 2);
            }
        });


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
        button1.setBounds( leftButtonX,19,85, 28);
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
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                } else {
                    OperateResultExOne<boolean[]> read = readWriteNet.ReadBool(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }

                if(checkBox_read_timer != null && checkBox_read_timer.isSelected()) button_read_timer = (JButton) e.getComponent();
            }
        });
        panelRead.add(button1);
        button_read_bool = button1;

        JButton button_read_byte = new JButton("r-byte");
        button_read_byte.setFocusPainted(false);
        button_read_byte.setBounds(leftButtonX + 90,19,85, 28);
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
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }

                if(checkBox_read_timer != null && checkBox_read_timer.isSelected()) button_read_timer = (JButton) e.getComponent();
            }
        });
        panelRead.add(button_read_byte);
        this.button_read_byte = button_read_byte;

        JButton button3 = new JButton("r-short");
        button3.setFocusPainted(false);
        button3.setBounds(leftButtonX,56,85, 28);
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
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }
                else {
                    OperateResultExOne<short[]> read = readWriteNet.ReadInt16(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected()) { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }

                if(checkBox_read_timer != null && checkBox_read_timer.isSelected()) button_read_timer = (JButton) e.getComponent();
            }
        });
        panelRead.add(button3);
        button_read_short = button3;

        JButton button4 = new JButton("r-ushort");
        button4.setFocusPainted(false);
        button4.setBounds(leftButtonX + 90,56,85, 28);
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
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }
                else {
                    OperateResultExOne<int[]> read = readWriteNet.ReadUInt16(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }

                if(checkBox_read_timer != null && checkBox_read_timer.isSelected()) button_read_timer = (JButton) e.getComponent();
            }
        });
        panelRead.add(button4);
        button_read_ushort = button4;

        JButton button5 = new JButton("r-int");
        button5.setFocusPainted(false);
        button5.setBounds(leftButtonX,90,85, 28);
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
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }
                else {
                    OperateResultExOne<int[]> read = readWriteNet.ReadInt32(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }

                if(checkBox_read_timer != null && checkBox_read_timer.isSelected()) button_read_timer = (JButton) e.getComponent();
            }
        });
        panelRead.add(button5);
        button_read_int = button5;

        JButton button6 = new JButton("r-uint");
        button6.setFocusPainted(false);
        button6.setBounds(leftButtonX + 90,90,85, 28);
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
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }
                else {
                    OperateResultExOne<long[]> read = readWriteNet.ReadUInt32(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }

                if(checkBox_read_timer != null && checkBox_read_timer.isSelected()) button_read_timer = (JButton) e.getComponent();
            }
        });
        panelRead.add(button6);
        button_read_uint = button6;

        JButton button7 = new JButton("r-long");
        button7.setFocusPainted(false);
        button7.setBounds(leftButtonX,124,85, 28);
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
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }
                else {
                    OperateResultExOne<long[]> read = readWriteNet.ReadInt64(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }

                if(checkBox_read_timer != null && checkBox_read_timer.isSelected()) button_read_timer = (JButton) e.getComponent();
            }
        });
        panelRead.add(button7);
        button_read_long = button7;


        JButton button9 = new JButton("r-float");
        button9.setFocusPainted(false);
        button9.setBounds(leftButtonX,158,85, 28);
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
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }
                else {
                    OperateResultExOne<float[]> read = readWriteNet.ReadFloat(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }

                if(checkBox_read_timer != null && checkBox_read_timer.isSelected()) button_read_timer = (JButton) e.getComponent();
            }
        });
        panelRead.add(button9);
        button_read_float = button9;

        JButton button10 = new JButton("r-double");
        button10.setFocusPainted(false);
        button10.setBounds(leftButtonX + 90,158,85, 28);
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
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }
                else {
                    OperateResultExOne<double[]> read = readWriteNet.ReadDouble(textField1.getText(), Short.parseShort(textField_ReadLength.getText()));
                    SetTimeSpend(now);
                    DemoUtils.ReadResultRender(read, textField1.getText(), textArea1, jsp);
                    if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}
                }

                if(checkBox_read_timer != null && checkBox_read_timer.isSelected()) button_read_timer = (JButton) e.getComponent();
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

        JLabel label9 = new JLabel("Encoding:");
        label9.setBounds(leftButtonX,232,80, 17);
        panelRead.add(label9);

        JComboBox<String> comboBox_encode = new JComboBox<>();
        comboBox_encode.setBounds(leftButtonX + 90,226,85, 28);
        comboBox_encode.addItem("ASCII");
        comboBox_encode.addItem("Unicode");
        comboBox_encode.addItem("UnicodeBE");
        comboBox_encode.addItem("UTF8");
        comboBox_encode.addItem("GB2312");
        comboBox_encode.setSelectedItem("ASCII");
        panelRead.add(comboBox_encode);

        JButton button11 = new JButton("r-string");
        button11.setFocusPainted(false);
        button11.setBounds(leftButtonX + 90,192,85, 28);
        button11.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button11.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                String encode = (String) comboBox_encode.getSelectedItem();
                Charset charset = StandardCharsets.US_ASCII;
                if (encode.equals("Unicode")) charset = StandardCharsets.UTF_16LE;
                else if (encode.equals("UnicodeBE")) charset = StandardCharsets.UTF_16BE;
                else if (encode.equals("UTF8")) charset = StandardCharsets.UTF_8;
                else if (encode.equals("GB2312")) charset = Charset.forName("GBK");

                OperateResultExOne<String> read = readWriteNet.ReadString(textField1.getText(), Short.parseShort(textField8.getText()), charset);
                SetTimeSpend(now);
                DemoUtils.ReadResultRender(read,textField1.getText(), textArea1, jsp );
                if (!read.IsSuccess&&checkBox_read_timer.isSelected())  { checkBox_read_timer.setSelected(false); button_read_timer = null;}


                if(checkBox_read_timer != null && checkBox_read_timer.isSelected()) button_read_timer = (JButton) e.getComponent();
            }
        });
        panelRead.add(button11);
        button_read_string = button11;


        panelRead.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                button1.setBounds(         panelRead.getWidth() - 185,19, 85, 28);
                button_read_byte.setBounds(panelRead.getWidth() - 95, 19, 85, 28);
                button3.setBounds(         panelRead.getWidth() - 185,56, 85, 28);
                button4.setBounds(         panelRead.getWidth() - 95, 56, 85, 28);
                button5.setBounds(         panelRead.getWidth() - 185,90, 85, 28);
                button6.setBounds(         panelRead.getWidth() - 95, 90, 85, 28);
                button7.setBounds(         panelRead.getWidth() - 185,124,85, 28);
                button9.setBounds(         panelRead.getWidth() - 185,158,85, 28);
                button10.setBounds(        panelRead.getWidth() - 95, 158,85, 28);
                label8.setBounds(          panelRead.getWidth() - 185,198,55, 17);
                textField8.setBounds(      panelRead.getWidth() - 185 + 43,195,41, 23);
                button11.setBounds(        panelRead.getWidth() - 95,192,85, 28);
                label9.setBounds(          panelRead.getWidth() - 185,232,80, 17);
                comboBox_encode.setBounds( panelRead.getWidth() - 95,226,85, 28);

                textField1.setBounds(83,27,panelRead.getWidth() - 335, 23);
                textField_ReadLength.setBounds(panelRead.getWidth() - 247,27,55, 23);
                jsp.setBounds(83,56,panelRead.getWidth() - 275, 165);
                jsp.updateUI();
            }
        });

        // 定时读取的部分实现
        JTextField textField_ReadTimer = new JTextField();
        textField_ReadTimer.setBounds(180,226,55, 23);
        textField_ReadTimer.setText("1000");
        panelRead.add(textField_ReadTimer);

        JLabel label40 = new JLabel("ms");
        label40.setBounds(238, 229,30, 17);
        panelRead.add(label40);

        JLabel label41 = new JLabel("Tick: 0");
        label41.setBounds(270, 229,100, 17);
        panelRead.add(label41);

        checkBox_read_timer = new JCheckBox("Read Timer");
        checkBox_read_timer.setBounds(80, 226, 100, 21);
        checkBox_read_timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkBox_read_timer.isSelected()){
                    // 选中
                    int timer_interval = 1000;
                    try{
                        timer_interval = Integer.parseInt(textField_ReadTimer.getText());
                    }
                    catch (Exception ex)
                    {
                        JOptionPane.showMessageDialog(
                            null,
                            "Read time interval input wrong! " + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (timer_interval <= 0)
                    {
                        JOptionPane.showMessageDialog(
                                null,
                                "Read time interval can not below 0!",
                                "Result",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (timer_read!=null) {
                        timer_read.stop();
                    }
                    timer_read = new Timer(timer_interval, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (button_read_timer!=null){

                                MouseEvent event = new MouseEvent(button_read_timer, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0,  0,0,1,true);
                                for (MouseListener listener : button_read_timer.getMouseListeners()) {
                                    listener.mouseClicked(event);
                                }
                                //button_read_timer.doClick();
                                read_tick_count++;
                                label41.setText("Tick: " + read_tick_count );
                            }
                        }
                    });
                    timer_read.start();
                    button_read_timer = null;
                    read_tick_count = 0;
                }
                else {
                    // 没有选中
                    if (timer_read!=null) {
                        timer_read.stop();
                    }
                    button_read_timer = null;
//                    JOptionPane.showMessageDialog(
//                            null,
//                            "No Selected",
//                            "Result",
//                            JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        panelRead.add(checkBox_read_timer);

        panel.add(panelRead);


    }

    private Timer timer_read;
    private JButton button_read_timer;
    private long read_tick_count = 0;
    private JCheckBox checkBox_read_timer;

    public void AddWrite(JPanel panel){
        JPanel panelWrite = new JPanel();
        panelWrite.setLayout(null);
        panelWrite.setBounds(572,0,420, panel.getHeight() - 2);
        panelWrite.setBorder(BorderFactory.createTitledBorder( "Write Single Test"));
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panelWrite.setBounds(panel.getWidth() - 420,0,420, panel.getHeight() - 2);
            }
        });

        JLabel label1 = new JLabel("Address：");
        label1.setBounds(9, 30,70, 17);
        panelWrite.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(83,27,147, 23);
        textField1.setText(address);
        panelWrite.add(textField1);

        textBox8 = textField1;

        JLabel label2 = new JLabel("Value：");
        label2.setBounds(9, 58,70, 17);
        panelWrite.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(83,56,147, 23);
        panelWrite.add(textField2);

        JLabel label100 = new JLabel("<html><span style=\"color:red\">Note: The value of the string needs to be converted<br />if bool: true,false,0,1<br />if array: [1,2,3]</span></html>");
        label100.setBounds(11, 82,200, 80);
        panelWrite.add(label100);

        JButton button1 = new JButton("w-bit");
        button1.setFocusPainted(false);
        button1.setBounds(236,24,82, 28);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button1.isEnabled()) return;
                super.mouseClicked(e);
                try {
                    String input = textField2.getText();
                    Date now = new Date();
                    if (input.startsWith("[") && input.endsWith("]")){
                        OperateResult write = readWriteNet.Write(textField1.getText(), HslExtension.StringToBoolArray(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
                    else {
                        boolean value = false;
                        if (input.equals("1")) value = true;
                        else if (input.equals("0")) value = false;
                        else value = Boolean.parseBoolean(input);
                        OperateResult write = readWriteNet.Write(textField1.getText(), value);
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
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
        button_write_bool = button1;

        JButton button2 = new JButton("w-byte");
        button2.setFocusPainted(false);
        button2.setBounds(323,24,86, 28);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button2.isEnabled()) return;
                super.mouseClicked(e);
                try {
                    String input = textField2.getText();
                    Date now = new Date();

                    if (input.startsWith("[") && input.endsWith("]")){
                        OperateResult write = readWriteNet.Write( textField1.getText(), HslExtension.StringToByteArray(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
                    else {
                        OperateResult write = (OperateResult)writeByteMethod.invoke(readWriteNet, textField1.getText(), Byte.parseByte(textField2.getText()));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
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
        button_write_byte = button2;

        JButton button3 = new JButton("w-short");
        button3.setFocusPainted(false);
        button3.setBounds(236,60,82, 28);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button3.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    String input = textField2.getText();
                    if (input.startsWith("[") && input.endsWith("]")) {
                        OperateResult write = readWriteNet.Write(textField1.getText(), HslExtension.StringToShortArray(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
                    else {
                        OperateResult write = readWriteNet.Write(textField1.getText(), Short.parseShort(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
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
        button_write_short = button3;

        JButton button4 = new JButton("w-ushort");
        button4.setFocusPainted(false);
        button4.setBounds(323,60,86, 28);
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button4.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    String input = textField2.getText();
                    if (input.startsWith("[") && input.endsWith("]")) {
                        OperateResult write = readWriteNet.Write(textField1.getText(), HslExtension.StringToUShortArray(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
                    else {
                        int value = Integer.parseInt(input);
                        if(value < 0 || value > 65535){
                            throw new Exception("Value must be between 0 - 65535");
                        }
                        OperateResult write = readWriteNet.Write(textField1.getText(), (short) value);
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
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
        button_write_ushort = button4;

        JButton button5 = new JButton("w-int");
        button5.setFocusPainted(false);
        button5.setBounds(236,93,82, 28);
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button5.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    String input = textField2.getText();
                    if (input.startsWith("[") && input.endsWith("]")) {
                        OperateResult write = readWriteNet.Write(textField1.getText(), HslExtension.StringToIntArray(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
                    else{
                        OperateResult write = readWriteNet.Write(textField1.getText(), Integer.parseInt(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
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
        button_write_int = button5;

        JButton button6 = new JButton("w-uint");
        button6.setFocusPainted(false);
        button6.setBounds(323,93,86, 28);
        button6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button6.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    String input = textField2.getText();
                    if (input.startsWith("[") && input.endsWith("]")) {
                        OperateResult write = readWriteNet.Write(textField1.getText(), HslExtension.StringToUIntArray(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
                    else {
                        long value = Long.parseLong(input);
                        if(value < 0L || value > 0xffffffffL){
                            throw new Exception("Value must be between 0 - 4294967295");
                        }
                        OperateResult write = readWriteNet.Write(textField1.getText(), (int)value);
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
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
        button_write_uint = button6;

        JButton button7 = new JButton("w-long");
        button7.setFocusPainted(false);
        button7.setBounds(236,126,82, 28);
        button7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button7.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    String input = textField2.getText();
                    if (input.startsWith("[") && input.endsWith("]")) {
                        OperateResult write = readWriteNet.Write(textField1.getText(), HslExtension.StringToLongArray(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
                    else {
                        OperateResult write = readWriteNet.Write(textField1.getText(), Long.parseLong(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
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
        button_write_long = button7;

        JButton button8 = new JButton("w-ulong");
        button8.setFocusPainted(false);
        button8.setVisible(false);
        button8.setBounds(323,126,86, 28);
        panelWrite.add(button8);

        JButton button9 = new JButton("w-float");
        button9.setFocusPainted(false);
        button9.setBounds(236,159,82, 28);
        button9.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button9.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    String input = textField2.getText();
                    if (input.startsWith("[") && input.endsWith("]")) {
                        OperateResult write = readWriteNet.Write(textField1.getText(), HslExtension.StringToFloatArray(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
                    else{
                        OperateResult write = readWriteNet.Write(textField1.getText(), Float.parseFloat(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
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
        button_write_float = button9;


        JButton button10 = new JButton("w-double");
        button10.setMargin(new Insets(0,0,0,0));
        button10.setFocusPainted(false);
        button10.setBounds(323,159,86, 28);
        button10.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button10.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    String input = textField2.getText();
                    if (input.startsWith("[") && input.endsWith("]")) {
                        OperateResult write = readWriteNet.Write(textField1.getText(), HslExtension.StringToDoubleArray(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
                    else{
                        OperateResult write = readWriteNet.Write(textField1.getText(), Double.parseDouble(input));
                        SetTimeSpend(now);
                        DemoUtils.WriteResultRender(write, textField1.getText());
                    }
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

        JLabel label9 = new JLabel("Encoding:");
        label9.setBounds(236,232,80, 17);
        panelWrite.add(label9);

        JComboBox<String> comboBox_encode = new JComboBox<>();
        comboBox_encode.setBounds(323,226,85, 28);
        comboBox_encode.addItem("ASCII");
        comboBox_encode.addItem("Unicode");
        comboBox_encode.addItem("UnicodeBE");
        comboBox_encode.addItem("UTF8");
        comboBox_encode.addItem("GB2312");
        comboBox_encode.setSelectedItem("ASCII");
        panelWrite.add(comboBox_encode);

        JButton button11 = new JButton("w-string");
        button11.setFocusPainted(false);
        button11.setBounds(323,192,86, 28);
        button11.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button11.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    String encode = (String) comboBox_encode.getSelectedItem();

                    Charset charset = StandardCharsets.US_ASCII;
                    if (encode.equals("Unicode")) charset = StandardCharsets.UTF_16LE;
                    else if (encode.equals("UnicodeBE")) charset = StandardCharsets.UTF_16BE;
                    else if (encode.equals("UTF8")) charset = StandardCharsets.UTF_8;
                    else if (encode.equals("GB2312"))  charset = Charset.forName("GBK");

                    OperateResult write = readWriteNet.Write(textField1.getText(), textField2.getText(), charset);
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
        button_write_string = button11;

        JButton button_write_hex = new JButton("w-hex");
        button_write_hex.setFocusPainted(false);
        button_write_hex.setBounds(236,192,82, 28);
        button_write_hex.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button_write_hex.isEnabled()) return;
                super.mouseClicked(e);
                Date now = new Date();
                try {
                    OperateResult write = readWriteNet.Write(textField1.getText(), SoftBasic.HexStringToBytes(textField2.getText()));
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
        panelWrite.add(button_write_hex);
        this.button_write_hex = button_write_hex;



        label_TimeCost = new JLabel(TimeCost);
        label_TimeCost.setBounds(9,172,150,21);
        panelWrite.add(label_TimeCost);

        label_TimeCount = new JLabel("<html><span style=\"color:gray\">Max:&nbsp;" + valueLimit.MaxValue + "&nbsp;&nbsp;&nbsp;&nbsp;Min: " + valueLimit.MinValue + "<br />Avg:&nbsp;" + valueLimit.Average + "&nbsp;&nbsp;&nbsp;&nbsp;Tick:&nbsp;" + valueLimit.Count + "</span></html>");
        label_TimeCount.setBounds(9, 193, 210, 42);
        panelWrite.add(label_TimeCount);

        panel.add(panelWrite);
    }

    private void SetTimeSpend(Date last)
    {
        long mill = new Date().getTime() - last.getTime();
        label_TimeCost.setText(TimeCost + mill + " ms");

        valueLimit.SetNewValue(mill);
        label_TimeCount.setText("<html><span style=\"color:gray\">Max:&nbsp;" + valueLimit.MaxValue + "&nbsp;&nbsp;&nbsp;&nbsp;Min: " + valueLimit.MinValue + "<br />Avg:&nbsp;" + ((int)valueLimit.Average) + "&nbsp;&nbsp;&nbsp;&nbsp;Tick:&nbsp;" + valueLimit.Count + "</span></html>");
    }

    private JLabel label_TimeCost;
    private JLabel label_TimeCount;
    private final String TimeCost = "Time-Cost: ";
    private ValueLimit valueLimit = new ValueLimit();
}
