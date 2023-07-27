package HslCommunicationDemo.PLC.Modbus;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Transfer.DataFormat;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.LogNet.Core.HslMessageDegree;
import HslCommunication.LogNet.Core.ILogNet;
import HslCommunication.ModBus.ModbusTcpNet;
import HslCommunication.ModBus.ModbusTcpServer;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.UserControlReadWriteHead;
import HslCommunicationDemo.UserControlReadWriteOp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class FormModbusServer extends JPanel {


    public FormModbusServer(JTabbedPane tabbedPane){
        setLayout(null);

        add( new UserControlReadWriteHead("Modbus Tcp Server", tabbedPane, this));
        AddConnectSegment(this);
        AddContent(this);

        modbusTcpServer = new ModbusTcpServer();

        // 创建定时器
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldOnline!= null)
                    textFieldOnline.setText(String.valueOf(modbusTcpServer.GetOnlineCount()));
            }
        });
        timer.start();

    }

    private ModbusTcpServer modbusTcpServer = null;
    private JPanel panelContent = null;
    private String defaultAddress = "100";
    private JTextArea textAreaLog = null;
    private JTextField textFieldOnline = null;
    private JScrollPane scrollPaneLog = null;
    private UserControlReadWriteOp userControlReadWriteOp1 = null;

    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = new JPanel();
        panelConnect.setLayout(null);
        panelConnect.setBounds(14,32,978, 54);
        panelConnect.setBorder(BorderFactory.createTitledBorder( ""));

        JLabel label1 = new JLabel("Port：");
        label1.setBounds(8, 17,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,14,86, 23);
        textField1.setText("502");
        panelConnect.add(textField1);

        JCheckBox checkBox1 = new JCheckBox("Use Modbus Rtu?");
        checkBox1.setBounds(160,16,140, 21);
        checkBox1.setSelected(false);
        panelConnect.add(checkBox1);


        JLabel label3 = new JLabel("Station：");
        label3.setBounds(298, 17,56, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(352,14,40, 23);
        textField3.setText("1");
        panelConnect.add(textField3);

        JCheckBox checkBox2 = new JCheckBox("Enable Write?");
        checkBox2.setBounds(420,16,120, 21);
        checkBox2.setSelected(true);
        panelConnect.add(checkBox2);

        JComboBox<DataFormat> comboBox1 = new JComboBox<>();
        comboBox1.setBounds(558,13,111, 25);
        comboBox1.addItem(DataFormat.ABCD);
        comboBox1.addItem(DataFormat.BADC);
        comboBox1.addItem(DataFormat.CDAB);
        comboBox1.addItem(DataFormat.DCBA);
        comboBox1.setSelectedItem(DataFormat.CDAB);
        panelConnect.add(comboBox1);

        JButton button2 = new JButton("Close");
        button2.setFocusPainted(false);
        button2.setBounds(850,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Start");
        button1.setFocusPainted(false);
        button1.setBounds(752,11,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    modbusTcpServer.setDataFormat((DataFormat) comboBox1.getSelectedItem());
                    modbusTcpServer.UseModbusRtuOverTcp = checkBox1.isSelected();
                    modbusTcpServer.EnableWrite = checkBox2.isSelected();
                    modbusTcpServer.ServerStart(Integer.parseInt(textField1.getText()));
                    modbusTcpServer.setLogNet(new ILogNet() {
                        @Override
                        public int LogSaveMode() {
                            return 0;
                        }

                        @Override
                        public void RecordMessage(HslMessageDegree degree, String keyWord, String text) {

                        }

                        @Override
                        public void WriteDebug(String text) {
                        }

                        @Override
                        public void WriteDebug(String keyWord, String text) {
                            textAreaLog.append( DemoUtils.dateFormat.format(new Date()) + " " + keyWord + " " + text + "\r\n");
                            JScrollBar scrollBar = scrollPaneLog.getVerticalScrollBar();
                            scrollBar.setValue(scrollBar.getMaximum());
                        }

                        @Override
                        public void WriteDescription(String description) {

                        }

                        @Override
                        public void WriteError(String text) {

                        }

                        @Override
                        public void WriteError(String keyWord, String text) {

                        }

                        @Override
                        public void WriteException(String keyWord, Exception ex) {

                        }

                        @Override
                        public void WriteException(String keyWord, String text, Exception ex) {

                        }

                        @Override
                        public void WriteFatal(String text) {

                        }

                        @Override
                        public void WriteFatal(String keyWord, String text) {

                        }

                        @Override
                        public void WriteInfo(String text) {

                        }

                        @Override
                        public void WriteInfo(String keyWord, String text) {

                        }

                        @Override
                        public void WriteNewLine() {

                        }

                        @Override
                        public void WriteWarn(String text) {

                        }

                        @Override
                        public void WriteWarn(String keyWord, String text) {

                        }

                        @Override
                        public void SetMessageDegree(HslMessageDegree degree) {

                        }

                        @Override
                        public void FiltrateKeyword(String keyword) {

                        }

                        @Override
                        public String[] GetExistLogFileNames() {
                            return new String[0];
                        }
                    });
                    JOptionPane.showMessageDialog(
                            null,
                            "Start Success",
                            "Result",
                            JOptionPane.PLAIN_MESSAGE);
                    DemoUtils.SetPanelEnabled(panelContent, true);
                    button2.setEnabled(true);
                    button1.setEnabled(false);
                    userControlReadWriteOp1.SetReadWriteNet(modbusTcpServer, defaultAddress, 10);
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
                if(modbusTcpServer!=null){
                    modbusTcpServer.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    DemoUtils.SetPanelEnabled(panelContent,false);
                }
            }
        });


        panel.add(panelConnect);
    }

    public void AddContent(JPanel panel){
        JPanel panelContent = new JPanel();
        panelContent.setLayout(null);
        panelContent.setBounds(14,95,978, 537);
        panelContent.setBorder(BorderFactory.createTitledBorder( ""));

        AddReadWrite(panelContent);
        AddReadBulk(panelContent);

        panel.add(panelContent);
        this.panelContent = panelContent;
        DemoUtils.SetPanelEnabled(this.panelContent,false);
    }

    public void AddReadWrite(JPanel panel){
        userControlReadWriteOp1 = new UserControlReadWriteOp();
        userControlReadWriteOp1.setLayout(null);
        userControlReadWriteOp1.setBounds(11,2,962, 235);
        panel.add(userControlReadWriteOp1);
    }

    public void AddReadBulk(JPanel panel){
        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);
        panelRead.setBounds(11,243,978, 400);
        panelRead.setBorder(BorderFactory.createTitledBorder( "Read byte by Length"));

        JLabel label1 = new JLabel("Online：");
        label1.setBounds(9, 30,70, 17);
        panelRead.add(label1);

        textFieldOnline = new JTextField();
        textFieldOnline.setBounds(83,27,82, 23);
        textFieldOnline.setText("0");
        panelRead.add(textFieldOnline);

//        JLabel label2 = new JLabel("Length：");
//        label2.setBounds(185, 30,60, 17);
//        panelRead.add(label2);
//
//        JTextField textField2 = new JTextField();
//        textField2.setBounds(234,27,102, 23);
//        textField2.setText("10");
//        panelRead.add(textField2);


        JLabel label3 = new JLabel("Result：");
        label3.setBounds(9, 58,70, 17);
        panelRead.add(label3);

        textAreaLog = new JTextArea();
        textAreaLog.setLineWrap(true);
        scrollPaneLog = new JScrollPane(textAreaLog);
        scrollPaneLog.setBounds(83,56,865, 230);
        panelRead.add(scrollPaneLog);


//        JButton button2 = new JButton("Read");
//        button2.setFocusPainted(false);
//        button2.setBounds(426,24,82, 28);
//        button2.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (button2.isEnabled() == false) return;
//                super.mouseClicked(e);
//                OperateResultExOne<byte[]> read = modbusTcpServer.Read(textField1.getText(),Short.parseShort(textField2.getText()));
//                if(read.IsSuccess){
//                    textAreaLog.setText(SoftBasic.ByteToHexString(read.Content));
//                }
//                else {
//                    JOptionPane.showMessageDialog(
//                            null,
//                            "Read Failed:" + read.ToMessageShowString(),
//                            "Result",
//                            JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//        panelRead.add(button2);

        panel.add(panelRead);
    }

}
