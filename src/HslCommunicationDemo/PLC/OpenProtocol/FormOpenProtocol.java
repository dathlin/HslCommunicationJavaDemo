package HslCommunicationDemo.PLC.OpenProtocol;

import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.*;
import HslCommunication.Core.Types.List;
import HslCommunication.Profinet.Omron.OmronPlcType;
import HslCommunication.Profinet.OpenProtocol.OpenProtocolNet;
import HslCommunication.Utilities;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormOpenProtocol extends JPanel {
    public FormOpenProtocol(JTabbedPane tabbedPane){
        setLayout(null);
        add( new UserControlReadWriteHead("Open Protocol", tabbedPane, this));
        AddConnectSegment(this);
        openProtocolNet = new OpenProtocolNet(){
            /**
             * 当收到对方消息的时候触发的方法
             *
             * @param openProtocolNet OpenProtocol 对象信息
             * @param content         字符串内容
             */
            @Override
            public void OnReceivedOpenMessageMethod(OpenProtocolNet openProtocolNet, String content) {
                super.OnReceivedOpenMessageMethod(openProtocolNet, content);
                sub_tick++;
                label_sub.setText("Sub Tick:" + sub_tick);
                if (!checkBox_stop.isSelected()) {
                    if (checkBox_format.isSelected()) {
                        //SwingUtilities.invokeLater(new Runnable() {
                        //  @Override
                        // public void run() {
                        textBox_log.setText(DemoUtils.FormatterDateTime.format(new Date()) + " :               Length: " + content.length() + Environment.NewLine + Environment.NewLine + GetRenderOpenMessage(content));
                        // }
                        // });
                    } else {
                        textBox_log.append(DemoUtils.FormatterDateTime.format(new Date()) + " : " + content + Environment.NewLine);
                    }
                }
            }
        };

        JPanel panel = DemoUtils.CreateContentPanel(this);
        AddGroup(node);

        AddDeviceList(panel);
        add(panel);
    }

    private long read_tick = 0;
    private long sub_tick = 0;
    private DefaultMutableTreeNode node = new DefaultMutableTreeNode( "" );
    private JTextArea textBox_log;
    private JCheckBox checkBox_stop;
    private JCheckBox checkBox_format;
    private JLabel label_sub;
    private void AddDeviceList(JPanel panel){

        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);
        panelRead.setBounds(270,5,500, panel.getHeight() - 10);
        panelRead.setBorder(BorderFactory.createTitledBorder( "Read Single Test"));

        JLabel label1 = new JLabel("MID:");
        label1.setBounds(10, 25, 30, 23);
        panelRead.add(label1);

        JTextField textField_mid = new JTextField();
        textField_mid.setText("10");
        textField_mid.setBounds(45, 23, 40, 23);
        panelRead.add(textField_mid);

        JLabel label2 = new JLabel("Revison:");
        label2.setBounds(90, 25, 55, 17);
        panelRead.add(label2);

        JTextField textField_revison = new JTextField();
        textField_revison.setText("1");
        textField_revison.setBounds(150, 23, 40, 23);
        panelRead.add(textField_revison);

        JLabel label3 = new JLabel("stationID:");
        label3.setBounds(200, 25, 70, 17);
        panelRead.add(label3);

        JTextField textField_stationid = new JTextField();
        textField_stationid.setText("0");
        textField_stationid.setBounds(270, 23, 40, 23);
        panelRead.add(textField_stationid);

        JLabel label4 = new JLabel("spindleID:");
        label4.setBounds(320, 25, 70, 17);
        panelRead.add(label4);

        JTextField textField_spindleid = new JTextField();
        textField_spindleid.setText("0");
        textField_spindleid.setBounds(390, 23, 40, 23);
        panelRead.add(textField_spindleid);

        JButton button1 = new JButton("Read");
        button1.setBounds(450, 24, 80, 20);
        panelRead.add(button1);


        JLabel label5 = new JLabel("Parameers:");
        label5.setBounds(10, 47, 100, 17);
        panelRead.add(label5);
        JLabel label5_2 = new JLabel("多个参数换行");
        label5_2.setBounds(10, 66, 100, 17);
        panelRead.add(label5_2);

        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        JScrollPane jsp_parameters = new JScrollPane(textArea1);
        jsp_parameters.setBounds(110,50,400, 100);
        panelRead.add(jsp_parameters);

        JLabel label6 = new JLabel("Read Result:");
        label6.setBounds(10, 175, 100, 17);
        panelRead.add(label6);

        Font font = new Font("Consolas", Font.PLAIN, 12);

        JTextArea textBox_read_content = new JTextArea();
        textBox_read_content.setLineWrap(true);
        JScrollPane jsp_read_result = new JScrollPane(textBox_read_content);
        jsp_read_result.setBounds(110,175,400, 200);
        textBox_read_content.setFont(font);
        panelRead.add(jsp_read_result);

        JLabel label_read_time = new JLabel("Time: -");
        label_read_time.setBounds(110, 153, 150, 17);
        panelRead.add(label_read_time);

        JLabel label_read_cost = new JLabel("Cost: -");
        label_read_cost.setBounds(300, 153, 120, 17);
        panelRead.add(label_read_cost);

        JLabel label_read_tick = new JLabel("Tick: -");
        label_read_tick.setBounds(600, 153,100,17);
        panelRead.add(label_read_tick);

        JLabel label_read_byteLength = new JLabel("Length: -");
        label_read_byteLength.setBounds( 450, 153, 120, 17 );
        panelRead.add(label_read_byteLength);

        JLabel label7 = new JLabel("Subscribe:");
        label7.setBounds(10, 380, 100, 17);
        panelRead.add(label7);

        checkBox_stop = new JCheckBox("Stop");
        checkBox_stop.setBounds(10, 400, 100, 17);
        panelRead.add(checkBox_stop);

        checkBox_format = new JCheckBox("Format");
        checkBox_format.setBounds(10, 420, 100, 17);
        panelRead.add(checkBox_format);

        label_sub = new JLabel("Sub Tick: ");
        label_sub.setBounds( 5, 600, 105, 17 );
        panelRead.add(label_sub);

        JTextArea textArea3 = new JTextArea();
        textArea3.setLineWrap(true);
        JScrollPane jsp_subscribe = new JScrollPane(textArea3);
        jsp_subscribe.setBounds(110,380,400, 200);
        textArea3.setFont(font);
        panelRead.add(jsp_subscribe);
        textBox_log = textArea3;

        panel.add(panelRead);
        node.setUserObject( "Api List" );
        JTree jTree = new JTree(node);
        jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() > 1){
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                    if (node != null) {
                        if (node.getUserObject() != null && node.getUserObject().getClass() == OpenMessage.class ) {
                            OpenMessage message = (OpenMessage) node.getUserObject();
                            if (message != null) {
                                textField_mid.setText(String.valueOf(message.MID));
                                textField_revison.setText(String.valueOf(message.Revision));
                                textField_stationid.setText(String.valueOf(message.StationID));
                                textField_spindleid.setText(String.valueOf(message.SpindleID));
                                StringBuilder sb = new StringBuilder();
                                if (message.DataField != null){
                                    for(int i = 0; i < message.DataField.length; i ++){
                                        sb.append(message.DataField[i]);
                                        sb.append("\r\n");
                                    }
                                }
                                textArea1.setText(sb.toString());
                            }
                        }
                    }
                }
            }
        });


        JScrollPane jsp = new JScrollPane(jTree);
        panel.add(jsp);
        jsp.setBounds(5,5,260, panel.getHeight() - 10);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                jsp.setBounds(5,5,260, panel.getHeight() - 10);
                panelRead.setBounds(270,5,panel.getWidth() - 275, panel.getHeight() - 10);
                jsp_parameters.setBounds(110,50,panelRead.getWidth() - 115, 100);
                jsp_read_result.setBounds(110,175,panelRead.getWidth() - 115, 200);
                jsp_subscribe.setBounds(110,380,panelRead.getWidth() - 115, Math.max(panelRead.getHeight() - 385, 100));
                label_sub.setBounds( 5, panelRead.getHeight() - 23, 105, 17 );
            }
        });

        panel.add(jsp);


        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                try
                {
                    textBox_read_content.setText("");
                    textBox_read_content.updateUI( );

                    Date start = new Date();
                    OperateResultExOne<String> read = openProtocolNet.ReadCustomer( Integer.parseInt( textField_mid.getText() ),
                            Integer.parseInt( textField_revison.getText() ), Integer.parseInt( textField_stationid.getText() ),
                            Integer.parseInt( textField_spindleid.getText() ),  textArea1.getText().split("\\r\\n") );

                    long ts = new Date().getTime() - start.getTime();
                    if (read.IsSuccess)
                    {
                        label_read_time.setText( "Time: " + DemoUtils.FormatterDateTime.format(new Date()));
                        label_read_cost.setText("Cost: " +  ts + " ms");
                        read_tick++;
                        label_read_tick.setText( "Tick: " + String.valueOf(read_tick) );
                        label_read_byteLength.setText("Length: " + read.Content.length() );


                        textBox_read_content.setText( GetRenderOpenMessage( read.Content ));
                        //textBox_log.AppendText( DateTime.Now.ToString( ) + " : " + read.Content.Trim( '\0' ) + Environment.NewLine );
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(
                                null,
                                "Read Failed\r\nReason:"+ read.Message,
                                "Result",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed\r\nReason:"+ex.getMessage(),
                            "Socket Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }
    private void AddTreeNode( DefaultMutableTreeNode node, String key, OpenMessage message ){
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(key);
        message.Key = key;
        child.setUserObject(message);
        node.add(child);
    }

    private void AddGroup(DefaultMutableTreeNode treeNode){
        // 添加 Open protocol 消息
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode( "Parameter set messages" );
        AddTreeNode( node1, "MID 0010 Parameter set ID upload",               new OpenMessage(  10,  1,  -1,  -1,  null ) );
        AddTreeNode( node1, "MID 0012 Parameter set data upload",             new OpenMessage(  12,  1,  -1,  -1,  new String[] { "000" } ) );
        AddTreeNode( node1, "MID 0014 Parameter set selected subscribe",      new OpenMessage(  14,  1,  -1,  -1,  null ) );
        AddTreeNode( node1, "MID 0017 Parameter set selected unsubscribe",    new OpenMessage(  17,  1,  -1,  -1,  null ) );
        AddTreeNode( node1, "MID 0018 Select Parameter set",                  new OpenMessage(  18,  1,  -1,  -1,  new String[] { "000" } ) );
        AddTreeNode( node1, "MID 0019 Set Parameter set batch size",          new OpenMessage(  19,  1,  -1,  -1,  new String[] { "000", "00" } ) );
        AddTreeNode( node1, "MID 0020 Reset Parameter set batch counter",     new OpenMessage(  20,  1,  -1,  -1,  new String[] { "000" } ) );
        treeNode.add(node1);

        DefaultMutableTreeNode node2 = new DefaultMutableTreeNode( "Job message" );
        AddTreeNode( node2,  "MID 0030 Job ID upload" ,        new OpenMessage(  30,  1,  -1,  -1,  null ) );
        AddTreeNode( node2,  "MID 0032 Job data upload" ,      new OpenMessage(  32,  1,  -1,  -1,  new String[] { "01" } ) );
        AddTreeNode( node2,  "MID 0034 Job info subscribe" ,   new OpenMessage(  34,  1,  -1,  -1,  null ) );
        AddTreeNode( node2,  "MID 0037 Job info unsubscribe" , new OpenMessage(  37,  1,  -1,  -1,  null ) );
        AddTreeNode( node2,  "MID 0038 Select Job" ,           new OpenMessage(  38,  1,  -1,  -1,  new String[] { "01" } ) );
        AddTreeNode( node2,  "MID 0039 Job restart" ,          new OpenMessage(  39,  1,  -1,  -1,  new String[] { "00" } ) );
        treeNode.add(node2);

        DefaultMutableTreeNode node3 = new DefaultMutableTreeNode( "Tool messages" );
        AddTreeNode( node3,  "MID 0040 Tool data upload" ,      new OpenMessage(  40,  1,  -1,  -1,  null ) );
        AddTreeNode( node3,  "MID 0042 Disable tool" ,          new OpenMessage(  42,  1,  -1,  -1,  null ) );
        AddTreeNode( node3,  "MID 0043 Enable tool" ,           new OpenMessage(  43,  1,  -1,  -1,  null ) );
        AddTreeNode( node3,  "MID 0044 Disconnect tool" ,       new OpenMessage(  44,  1,  -1,  -1,  null ) );
        AddTreeNode( node3,  "MID 0045 Set calibration value" , new OpenMessage(  44,  1,  -1,  -1,  new String[] { "011", "02003550" } ) );
        treeNode.add(node3);

        DefaultMutableTreeNode node4 = new DefaultMutableTreeNode( "VIN Messages" );
        AddTreeNode( node4,  "MID 0050 Vehicle ID Number download" ,    new OpenMessage(  50,  1,  -1,  -1,  new String[] { "0000000000000000000000000" } ) );
        AddTreeNode( node4,  "MID 0051 Vehicle ID Number subscribe" ,   new OpenMessage(  51,  1,  -1,  -1, null ) );
        AddTreeNode( node4,  "MID 0054 Vehicle ID Number unsubscrib" ,  new OpenMessage(  54,  1,  -1,  -1,  null ) );
        treeNode.add(node4);

        DefaultMutableTreeNode node5 = new DefaultMutableTreeNode( "Tightening result messages" );
        AddTreeNode( node5,  "MID 0060 Last tightening result data subscribe" ,   new OpenMessage(  60,  1,  -1,  -1,  null ) );
        AddTreeNode( node5,  "MID 0063 Last tightening result data unsubscribe" , new OpenMessage(  63,  1,  -1,  -1,  null ) );
        AddTreeNode( node5,  "MID 0064 Old tightening result upload" ,            new OpenMessage(  64,  1,  -1,  -1,  new String[] { "0000000000" } ) );
        treeNode.add(node5);

        DefaultMutableTreeNode node6 = new DefaultMutableTreeNode( "Alarm messages" );
        AddTreeNode( node6,  "MID 0070 Alarm subscribe" ,                           new OpenMessage(  70,  1,  -1,  -1,  null ) );
        AddTreeNode( node6,  "MID 0073 Alarm unsubscribe" ,                         new OpenMessage(  73,  1,  -1,  -1,  null ) );
        AddTreeNode( node6,  "MID 0078 Acknowledge alarm remotely on controller" ,  new OpenMessage(  78,  1,  -1,  -1,  null ) );
        treeNode.add(node6);

        DefaultMutableTreeNode node7 = new DefaultMutableTreeNode( "Time messages" );
        AddTreeNode( node7,  "MID 0080 Read time upload" ,  new OpenMessage(  80,  1,  -1,  -1,  null ) );
        AddTreeNode( node7,  "MID 0082 Set Time" ,          new OpenMessage(  82,  1,  -1,  -1,  new String[] { new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date()) } ) );
        treeNode.add(node7);

        DefaultMutableTreeNode node8 = new DefaultMutableTreeNode( "Multi-spindle status messages" );
        AddTreeNode( node8,  "MID 0090 Multi-spindle status subscribe" ,    new OpenMessage(  90,  1,  -1,  -1,  null ) );
        AddTreeNode( node8,  "MID 0093 Multi-spindle status unsubscribe" ,  new OpenMessage(  93,  1,  -1,  -1,  null ) );
        treeNode.add(node8);

        DefaultMutableTreeNode node9 = new DefaultMutableTreeNode( "Multi-spindle result messages" );
        AddTreeNode( node9,  "MID 0100 Multi-spindle result subscribe" ,    new OpenMessage(  100,  1,  -1,  -1,  null ) );
        AddTreeNode( node9,  "MID 0103 Multi spindle result unsubscribe" ,  new OpenMessage(  103,  1,  -1,  -1,  null ) );
        treeNode.add(node9);


    }

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JTextField textField1 = DemoUtils.CreateIpAddressTextBox(panelConnect);
        JTextField textField2 = DemoUtils.CreateIpPortTextBox(panelConnect, "4545");

        JLabel label3 = new JLabel("RevisonOnConnect：");
        label3.setBounds(390, 17,120, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(510,14,40, 23);
        textField3.setText("1");
        panelConnect.add(textField3);

        JCheckBox checkBox1 = new JCheckBox("AutoAckControllerMessage?");
        checkBox1.setBounds(550,17,200, 17);
        panelConnect.add(checkBox1);

        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(860,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(762,11,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    openProtocolNet.setIpAddress(textField1.getText());
                    openProtocolNet.setPort(Integer.parseInt(textField2.getText()));
                    openProtocolNet.RevisonOnConnected = Integer.parseInt(textField3.getText());
                    openProtocolNet.AutoAckControllerMessage = checkBox1.isSelected();

                    OperateResult connect = openProtocolNet.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
                        //userControlReadWriteDevice.SetReadWriteNet(omronFinsNet, defaultAddress, 10);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Failed:" + connect.ToMessageShowString(),
                                "Result",
                                JOptionPane.WARNING_MESSAGE);
                    }
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
                if (!button2.isEnabled()) return;
                if(openProtocolNet!=null){
                    openProtocolNet.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    //userControlReadWriteDevice.setEnabled(false);
                }
            }
        });


        panel.add(panelConnect);
    }

    private OpenProtocolNet openProtocolNet;









    private String GetMIDText( int mid )
    {
        switch(mid)
        {
            case 1: return "Communication start";
            case 2: return "Communication start acknowledge";
            case 3: return "Communication stop";
            case 4: return "Command error";
            case 5: return "Command accepted";
            case 10: return "Parameter set ID upload request";
            case 11: return "Parameter set ID upload reply";
            case 12: return "Parameter set data upload request";
            case 13: return "Parameter set data upload reply";
            case 14: return "Parameter set selected subscribe";
            case 15: return "Parameter set selected";
            case 16: return "Parameter set selected acknowledge";
            case 17: return "Parameter set selected unsubscribe";
            case 18: return "Select Parameter set";
            case 19: return "Set Parameter set batch size";
            case 20: return "Reset Parameter set batch counter";
            case 30: return "Job ID upload request";
            case 31: return "Job ID upload reply";
            case 32: return "Job data upload request";
            case 33: return "Job data upload reply";
            case 34: return "Job info subscribe";
            case 35: return "Job info";
            case 36: return "Job info acknowledge";
            case 37: return "Job info unsubscribe";
            case 38: return "Select Job";
            case 39: return "Job restart";
            case 40: return "Tool data upload request";
            case 41: return "Tool data upload reply";
            case 42: return "Disable tool";
            case 43: return "Enable tool";
            case 44: return "Disconnect tool request";
            case 45: return "Set calibration value request";
            case 50: return "Vehicle ID Number download request";
            case 51: return "Vehicle ID Number subscribe";
            case 52: return "Vehicle ID Number";
            case 53: return "Vehicle ID Number acknowledge";
            case 54: return "Vehicle ID Number unsubscribe";
            case 60: return "Last tightening result data subscribe";
            case 61: return "Last tightening result data";
            case 62: return "Last tightening result data acknowledge";
            case 63: return "Last tightening result data unsubscribe";
            case 64: return "Old tightening result upload request";
            case 65: return "Old tightening result upload reply";
            case 70: return "Alarm subscribe";
            case 71: return "Alarm";
            case 72: return "Alarm acknowledge";
            case 73: return "Alarm unsubscribe";
            case 74: return "Alarm acknowledged on controller";
            case 75: return "Alarm acknowledged on controller acknowledge";
            case 76: return "Alarm status";
            case 77: return "Alarm status acknowledge";
            case 78: return "Acknowledge alarm remotely on controller";
            case 80: return "Read time upload request";
            case 81: return "Read time upload reply";
            case 82: return "Set Time";
            case 90: return "Multi-spindle status subscribe";
            case 91: return "Multi-spindle status";
            case 92: return "Multi-spindle status acknowledge";
            case 93: return "Multi-spindle status unsubscribe";
            case 100: return "Multi-spindle result subscribe";
            case 101: return "Multi-spindle result";
            case 102: return "Multi-spindle result acknowledge";
            case 103: return "Multi spindle result unsubscribe";
            default: return "Unknown";
        }
    }
    /**
     * 右左补齐
     */
    public static String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }
    /**
     * 左补齐
     */
    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
        for (int i = 0; i < diff; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    private void AddStringBuilder( StringBuilder sb, String content, boolean withIndex, String... paras )
    {
        sb.append( padRight("Name", 35, ' ' ) );
        sb.append( padRight("Index:Len", 15, ' ' ) );
        sb.append( padRight("Num", 10, ' ' ) );
        sb.append( "Value" );
        sb.append( Environment.NewLine );

        for (int i = 0; i < paras.length; i++)
        {
            int index = paras[i].indexOf( '[' );
            if (index < 0) return;

            String tail = "";
            if (!paras[i].endsWith("]"))
            {
                int endIndex = paras[i].lastIndexOf( "]" );
                if (endIndex > 0)
                {
                    tail = paras[i].substring( endIndex + 1 );
                    paras[i] = paras[i].substring( 0, endIndex + 1 );
                }
            }

            // 先添加名称信息
            String name = paras[i].substring( 0, index );

            // [21-25]   [21-N]  []
            Pattern pattern = Pattern.compile("[0-9]+" );
            Matcher matcher = pattern.matcher(paras[i].substring( index ));
            //matcher.find()
            List<String> mc = new List<>();
            while (matcher.find()){
                mc.add(matcher.group());
            }

            if (mc.size() != 2)
            {
                int index1 = Convert.ToInt32( mc.get(0) ) - 1;
                if (index1 >= content.length() - 1) break;
                sb.append( padRight(name, 35, ' ' ) );

                sb.append( padRight("[" + index1 + ":N]", 15, ' ' ) );

                if (mc.size() == 1 && paras[i].substring( index ).endsWith( "N]" ))
                {
                    if (withIndex)
                    {
                        sb.append( padRight(content.substring( index1, index1 + 2 ), 10, ' ' ) );
                        sb.append( content.substring( index1 + 2 ) );
                    }
                    else
                    {
                        sb.append( padRight(" ", 10, ' ' ) );
                        sb.append( content.substring( index1 ) );
                    }
                    if (sb.charAt(sb.length() - 1) == 0x00) sb.delete( sb.length() - 1, sb.length());
                }
                if (!Utilities.IsStringNullOrEmpty( tail )) sb.append( "  // " + tail );
            }
            else
            {
                int index1 = Convert.ToInt32( mc.get(0) ) - 1;
                int index2 = Convert.ToInt32( mc.get(1) ) - 1;
                int len = index2 - index1 + 1;

                if (index1 >= content.length() - 1) break;
                sb.append( padRight(name, 35, ' ' ) );

                sb.append( padRight("[" + index1 + ":" + len + "]", 15, ' ' ) );
                if (withIndex)
                {
                    sb.append( padRight(content.substring( index1, 2 + index1), 10, ' ' ) );
                    sb.append( content.substring( index1 + 2, len + index1 ) );
                }
                else
                {
                    sb.append( padRight(" ", 10, ' ' ) );
                    sb.append( content.substring( index1, len + index1 ) );
                }
                if (!Utilities.IsStringNullOrEmpty( tail )) sb.append( "  // " + tail );
            }
            sb.append( Environment.NewLine );
        }
    }



    private String GetRenderOpenMessage( String content )
    {
        int mid = Convert.ToInt32( content.substring( 4, 8 ) );
        int revision = Convert.ToInt32( content.substring( 8, 11 ) );

        StringBuilder sb = new StringBuilder( "MID " + content.substring( 4, 8 ) + " : " + GetMIDText( mid ) );
        sb.append( Environment.NewLine );
        sb.append( "Revision " + content.substring( 8, 11 ) + "                       Ack flag: " + content.substring( 11, 12 ) + " " + (content.charAt(11) == '0' ? "Ack needed" : content.charAt(11) == '1' ? "No ack needed" : "") );
        sb.append( Environment.NewLine );
        sb.append( Environment.NewLine );

        if (mid == 4)
        {
            sb.append( "Command MID: " + content.substring( 20, 24 ) + "   Error: " + content.substring( 24, 26 ) );
            sb.append( Environment.NewLine );
            sb.append( "Message: " + OpenProtocolNet.GetErrorText( Convert.ToInt32( content.substring( 24, 26 ) ) ) );
            sb.append( Environment.NewLine );
        }
        else if (mid == 5) AddStringBuilder( sb, content,  false, "MID number accepted[21-24]" );
			else if (mid == 11) AddStringBuilder( sb, content,  false, "The number of parameter sets[21-23]", "The ID of each parameter set[24-N]" );
			else if (mid == 13) AddStringBuilder( sb, content,  true, "Parameter set ID[21-25]", "Parameter set name[26-52]", "Rotation direction[53-55]1=CW;2=CCW", "Batch size[56-59]", "Torque min[60-67]",
            "Torque max[68-75]", "Torque final target[76-83]", "Angle min[84-90]", "Angle max[91-97]", "Final Angle Target[98-104]" );
			else if (mid == 15) AddStringBuilder( sb, content,  false, "Parameter set ID[21-23]", "Date of last change[21-42]" );
			else if (mid == 31 && revision == 1) { AddStringBuilder( sb, content,  false, "Number of Jobs[21-22]", "Job ID of each Job[23-N]" ); }
    else if (mid == 31 && revision == 2) { AddStringBuilder( sb, content,  false, "Number of Jobs[21-24]", "Job ID of each Job[25-N]" ); }
    else if (mid == 33) AddStringBuilder( sb, content,  true, "Job ID[21-24]", "Job name[25-51]", "Forced order[52-54]", "Max time for first tightening[55-60]", "Max time to complete Job[61-67]", "Job batch mode[68-70]", "Lock at Job done[71-73]",
            "Use line control[74-76]", "Repeat Job[77-79]", "Tool loosening[80-82]", "Reserved[83-85]", "Number of parameter sets[86-89]", "Job list[90-N]" );
			else if (mid == 35 && revision == 1) AddStringBuilder( sb, content,  true, "Job ID[21-24]", "Job status[25-27]", "Job batch mode[28-30]", "Job batch size[31-36]", "Job batch counter[37-42]", "Time stamp[43-63]" );
			else if (mid == 41) AddStringBuilder( sb, content,  true, "Tool serial number[21-36]", "Tool number of tightening[37-48]", "Last calibration date[49-69]", "Controller serial number[70-81]",
            "Calibration value[82-89]", "Last service date[90-110]", "Tightenings since service[111-122]", "Tool type[123-126]", "Motor size[127-130]", "Open end data[131-135]", "Controller software version[136-156]" );
			else if (mid == 52) AddStringBuilder( sb, content,  true, "VIN number[21-47]", "Identifier result part 2[48-74]", "Identifier result part 3[75-91]", "Identifier result part 4[92-128]" );
			else if (mid == 61 && revision == 1) AddStringBuilder( sb, content,  true, "Cell ID[21-26]", "Channel ID[27-30]", "Torque controller Name[31-57]", "VIN Number[58-84]", "Job ID[85-88]", "Parameter set ID[89-93]",
            "Batch size[94-99]", "Batch counter[100-105]", "Tightening Status[106-108]", "Torque status[109-111]", "Angle status[112-114]", "Torque Min limit[115-122]", "Torque Max limit[123-130]",
            "Torque final target[131-138]", "Torque[139-146]", "Angle Min[147-153]", "Angle Max[154-160]", "Final Angle Target[161-167]", "Angle[168-174]", "Time stamp[175-195]", "Date/time of last change[196-216]", "Batch status[217-219]", "Tightening ID[220-231]" );
			else if (mid == 61 && revision == 999) AddStringBuilder( sb, content,  false, "VIN Number[21-45]", "Job ID[46-47]", "Parameter set ID[48-50]", "Batch size[51-54]", "Batch counter[55-58]", "Batch status[59-59]",
            "Tightening status[60-60]", "Torque status[61-61]", "Angle status[62-62]", "Torque[63-68]", "Angle[69-73]", "Time stamp[74-92]", "Date/time of last change[93-111]", "Tightening ID[112-121]" );
			else if (mid == 65 && revision == 1) AddStringBuilder( sb, content,  true, "Tightening ID[21-32]", "VIN Number[33-59]", "Parameter set ID[60-64]", "Batch counter[65-70]", "Tightening Status[71-73]", "Torque status[74-76]",
            "Angle status[77-79]", "Torque[80-87]", "Angle[88-94]", "Time stamp[95-115]", "Batch status[116-118]" );
			else if (mid == 71) AddStringBuilder( sb, content,  true, "Error code[21-26]", "Controller ready status[27-29]", "Tool ready status[30-32]", "Time[33-53]" );
			else if (mid == 74) AddStringBuilder( sb, content,  false, "Error code[21-24]" );
			else if (mid == 76) AddStringBuilder( sb, content,  true, "Alarm status[21-23]", "Error code[24-29]", "Controller ready status[30-32]", "Tool ready status[33-35]", "Time[36-56]" );
			else if (mid == 81) AddStringBuilder( sb, content,  false, "Time[21-39]" );


        sb.append( Environment.NewLine );
        sb.append( Environment.NewLine );
        sb.append( "Source Content: ======================================================" );
        sb.append( Environment.NewLine );
        sb.append( content );
        return sb.toString( );
    }

}
