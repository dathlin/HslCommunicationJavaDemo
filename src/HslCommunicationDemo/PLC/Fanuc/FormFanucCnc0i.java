package HslCommunicationDemo.PLC.Fanuc;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.CNC.Fanuc.*;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Core.Types.OperateResultExTwo;
import HslCommunication.Utilities;
import HslCommunicationDemo.DemoUtils;
import HslCommunicationDemo.UserControlReadWriteHead;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

public class FormFanucCnc0i extends JPanel {

    public FormFanucCnc0i(JTabbedPane tabbedPane){
        setLayout(null);

        add( new UserControlReadWriteHead("Fanuc Series 0i-MF", tabbedPane, this));
        AddConnectSegment(this);
        AddContent(this);
        fanuc = new FanucSeries0i("127.0.0.1", 8193);
    }

    private FanucSeries0i fanuc = null;
    private JPanel panelContent = null;


    public void AddConnectSegment(JPanel panel){
        JPanel panelConnect = DemoUtils.CreateConnectPanel(panel);

        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 17,56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(62,14,106, 23);
        textField1.setText("192.168.64.129");
        panelConnect.add(textField1);

        JLabel label2 = new JLabel("Port：");
        label2.setBounds(184, 17,56, 17);
        panelConnect.add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(238,14,61, 23);
        textField2.setText("8193");
        panelConnect.add(textField2);

        JLabel label3 = new JLabel("Timeout：");
        label3.setBounds(338, 17,70, 17);
        panelConnect.add(label3);

        JTextField textField3 = new JTextField();
        textField3.setBounds(422,14,40, 23);
        textField3.setText("5000");
        panelConnect.add(textField3);

        JButton button2 = new JButton("Disconnect");
        button2.setFocusPainted(false);
        button2.setBounds(684,11,121, 28);
        panelConnect.add(button2);

        JButton button1 = new JButton("Connect");
        button1.setFocusPainted(false);
        button1.setBounds(577,11,91, 28);
        panelConnect.add(button1);

        button2.setEnabled(false);
        button1.setEnabled(true);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false)return;
                super.mouseClicked(e);
                try {
                    fanuc.setIpAddress(textField1.getText());
                    fanuc.setPort(Integer.parseInt(textField2.getText()));
                    fanuc.setReceiveTimeOut( Integer.parseInt(textField3.getText()));

                    OperateResult connect = fanuc.ConnectServer();
                    if(connect.IsSuccess){
                        JOptionPane.showMessageDialog(
                                null,
                                "Connect Success",
                                "Result",
                                JOptionPane.PLAIN_MESSAGE);
                        DemoUtils.SetPanelEnabled(panelContent,true);
                        button2.setEnabled(true);
                        button1.setEnabled(false);
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
                if(fanuc !=null){
                    fanuc.ConnectClose();
                    button1.setEnabled(true);
                    button2.setEnabled(false);
                    DemoUtils.SetPanelEnabled(panelContent,false);
                }
            }
        });


        panel.add(panelConnect);
    }

    public void AddContent(JPanel panel){
        JPanel panelContent = DemoUtils.CreateContentPanel(panel);

        AddReadWrite(panelContent);

        panel.add(panelContent);
        this.panelContent = panelContent;
        DemoUtils.SetPanelEnabled(this.panelContent,false);
    }

    private String GetPathFromTree( DefaultMutableTreeNode treeNode )
    {
        if (treeNode.getParent() == null) return "//" + treeNode.toString() + "/";
        if (treeNode.getUserObject() != null )
        {
            FileDirInfo fileDirInfo = (FileDirInfo)treeNode.getUserObject();
            if (!fileDirInfo.IsDirectory)
            {
                return GetPathFromTree( (DefaultMutableTreeNode)treeNode.getParent() ) + fileDirInfo.Name;
            }
            else
            {
                return GetPathFromTree( (DefaultMutableTreeNode)treeNode.getParent() ) + fileDirInfo.Name + "/";
            }
        }
        return GetPathFromTree((DefaultMutableTreeNode) treeNode.getParent() ) + treeNode.toString() + "/";
    }

    private void BrowerFile( DefaultMutableTreeNode treeNode )
    {
        OperateResultExOne<FileDirInfo[]> read = fanuc.ReadAllDirectoryAndFile( GetPathFromTree( treeNode ) );
        if (read.IsSuccess)
        {
            for(int i = 0; i < read.Content.length; i++)
            {
                FileDirInfo fileDirInfo = read.Content[i];
                DefaultMutableTreeNode node = new DefaultMutableTreeNode( fileDirInfo );
                treeNode.add( node );

                if (fileDirInfo.IsDirectory)
                {
                    //node.ImageKey = "Class_489";
                    //node.SelectedImageKey = "Class_489";
                    BrowerFile( node );
                }
                else
                {
                    //node.ImageKey = "file";
                    //node.SelectedImageKey = "file";
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(
                    null,
                    "Read Failed:" + read.ToMessageShowString(),
                    "Result",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public void AddReadWrite(JPanel panel){
        textArea8 = new JTextArea();
        textArea8.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea8);
        jsp.setBounds(215,239,756, 308);
        panel.add(jsp);

        JLabel label14 = new JLabel("PATH:");
        label14.setBounds(602,130,51,17);
        panel.add(label14);

        JTextField texBox_path = new JTextField("//CNC_MEM/USER/PATH1/");
        texBox_path.setBounds(656,127,316,23);
        panel.add(texBox_path);

        JTextField texBox9 = new JTextField("O33");
        texBox9.setBounds(656,154,105,23);
        panel.add(texBox9);

        FileDirInfo root = new FileDirInfo();
        root.IsDirectory = true;
        root.Name = "CNC_MEM";
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( root );
        JTree jTree = new JTree(node);
        jTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                if (node != null){
                    FileDirInfo fileDirInfo = (FileDirInfo)node.getUserObject();
                    if (!fileDirInfo.IsDirectory)
                    {
                        textArea8.setText( fileDirInfo.toFileString( ) );
                        texBox9.setText( fileDirInfo.Name);
                        if (node.getParent() != null)
                        {
                            texBox_path.setText(GetPathFromTree(  (DefaultMutableTreeNode)node.getParent() ) ) ;
                        }
                    }
                    else
                    {
                        texBox_path.setText(GetPathFromTree( node ));

                        ArrayList<String> list = new ArrayList<String>( );

                        for(int i=0;i <node.getChildCount() ; i ++){
                            DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getChildAt(i);
                            FileDirInfo file = (FileDirInfo)child.getUserObject();
                            if (!file.IsDirectory)
                                list.add( file.toFileString( ) );
                        }
                        textArea8.setText("");
                        for (int i =0; i< list.size(); i++){
                            textArea8.append(list.get(i));
                            textArea8.append("\r\n");
                        }
                    }
                }
            }
        });
        JScrollPane jsp2 = new JScrollPane(jTree);
        jsp2.setBounds(5,239,204, 306);
        panel.add(jsp2);


        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                jsp.setBounds(215,239,panel.getWidth() - 220, panel.getHeight() - 242);
                jsp.updateUI();
                jsp2.setBounds(5,239,204, panel.getHeight() - 242);
                jsp2.updateUI();
            }
        });

        JButton button33 = new JButton("路径信息");
        button33.setFocusPainted(false);
        button33.setBounds(5,213,96, 23);
        button33.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                node.removeAllChildren();
                BrowerFile( node );
                jTree.updateUI();
            }
        });
        panel.add(button33);


        JButton button34 = new JButton("清除");
        button34.setFocusPainted(false);
        button34.setBounds(115,213,96, 23);
        button34.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                node.removeAllChildren();
                jTree.updateUI();
            }
        });
        panel.add(button34);

        JButton button3 = new JButton("系统状态");
        button3.setFocusPainted(false);
        button3.setBounds(11,7,96, 29);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<SysStatusInfo> read = fanuc.ReadSysStatusInfo();
                if (read.IsSuccess){
                    textArea8.setText(read.Content.toString());
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
        panel.add(button3);

        JButton button4 = new JButton("报警信息");
        button4.setFocusPainted(false);
        button4.setBounds(113,7,96, 29);
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<SysAlarm[]> read = fanuc.ReadSystemAlarm();
                if (read.IsSuccess){
                    textArea8.setText(SysAlarm.GetArrayString(read.Content));
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
        panel.add(button4);

        JButton button5 = new JButton("坐标数据");
        button5.setFocusPainted(false);
        button5.setBounds(215,7,96, 29);
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<SysAllCoors> read = fanuc.ReadSysAllCoors();
                if (read.IsSuccess){
                    textArea8.setText(read.Content.toString());
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
        panel.add(button5);

        JButton button6 = new JButton("程序列表");
        button6.setFocusPainted(false);
        button6.setBounds(317,7,96, 29);
        button6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<int[]> read = fanuc.ReadProgramList();
                if (read.IsSuccess){
                    textArea8.setText(SoftBasic.ArrayFormat(read.Content));
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
        panel.add(button6);


        JButton button7 = new JButton("程序名");
        button7.setFocusPainted(false);
        button7.setBounds(419,7,96, 29);
        button7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExTwo<String, Integer> read = fanuc.ReadSystemProgramCurrent();
                if (read.IsSuccess){
                    textArea8.setText("程序名：" + read.Content1 + "\r\n程序号：" + read.Content2);
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
        panel.add(button7);

        JButton button8 = new JButton("主轴转速");
        button8.setFocusPainted(false);
        button8.setBounds(521,7,96, 29);
        button8.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExTwo<Double,Double> read = fanuc.ReadSpindleSpeedAndFeedRate();
                if (read.IsSuccess){
                    textArea8.setText(read.Content1.toString() + "\r\n" + read.Content2.toString());
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
        panel.add(button8);

        JButton button9 = new JButton("伺服负载");
        button9.setFocusPainted(false);
        button9.setBounds(623,7,96, 29);
        button9.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExTwo<Double,Double> read = fanuc.ReadSpindleSpeedAndFeedRate();
                if (read.IsSuccess){
                    textArea8.setText(read.Content1.toString() + "\r\n" + read.Content2.toString());
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
        panel.add(button9);

        JButton button10 = new JButton("刀具补偿");
        button10.setFocusPainted(false);
        button10.setBounds(725,7,96, 29);
        button10.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<CutterInfo[]> read = fanuc.ReadCutterInfos(24);
                if (read.IsSuccess){
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i = 0; i < read.Content.length; i++){
                        stringBuilder.append(read.Content[i].toString());
                        stringBuilder.append("\r\n");
                    }
                    textArea8.setText(stringBuilder.toString());
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
        panel.add(button10);


        JButton button11 = new JButton("程序路径");
        button11.setFocusPainted(false);
        button11.setBounds(827,7,96, 29);
        button11.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<String> read = fanuc.ReadCurrentForegroundDir();
                if (read.IsSuccess){
                    textArea8.setText(read.Content );
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
        panel.add(button11);

        JButton button12 = new JButton("工件尺寸");
        button12.setFocusPainted(false);
        button12.setBounds(11,42,96, 29);
        button12.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<double[]> read = fanuc.ReadDeviceWorkPiecesSize();
                if (read.IsSuccess){
                    textArea8.setText(SoftBasic.ArrayFormat(read.Content ));
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
        panel.add(button12);

        JButton button13 = new JButton("报警代码");
        button13.setFocusPainted(false);
        button13.setBounds(113,42,96, 29);
        button13.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<Integer> read = fanuc.ReadAlarmStatus();
                if (read.IsSuccess){
                    textArea8.setText(read.Content.toString());
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
        panel.add(button13);


        JButton button19 = new JButton("机床时间");
        button19.setFocusPainted(false);
        button19.setBounds(215,42,96, 29);
        button19.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<Date> read = fanuc.ReadCurrentDateTime();
                if (read.IsSuccess){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
                    textArea8.setText(sdf.format(read.Content));
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
        panel.add(button19);


        JButton button20 = new JButton("已加工数");
        button20.setFocusPainted(false);
        button20.setBounds(317,42,96, 29);
        button20.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<Integer> read = fanuc.ReadCurrentProduceCount();
                if (read.IsSuccess){
                    textArea8.setText(read.Content.toString());
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
        panel.add(button20);


        JButton button21 = new JButton("总加工数");
        button21.setFocusPainted(false);
        button21.setBounds(419,42,96, 29);
        button21.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<Integer> read = fanuc.ReadExpectProduceCount();
                if (read.IsSuccess){
                    textArea8.setText(read.Content.toString());
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
        panel.add(button21);


        JButton button22 = new JButton("系统语言");
        button22.setFocusPainted(false);
        button22.setBounds(521,42,96, 29);
        button22.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<Short> read = fanuc.ReadLanguage();
                if (read.IsSuccess){
                    textArea8.setText(read.Content.toString() + "\r\n此处举几个常用值 0: 英语 1: 日语 2: 德语 3: 法语 4: 中文繁体 6: 韩语 15: 中文简体 16: 俄语 17: 土耳其语");
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
        panel.add(button22);


        JButton button24 = new JButton("当前程序");
        button24.setFocusPainted(false);
        button24.setBounds(623,42,96, 29);
        button24.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<String> read = fanuc.ReadCurrentProgram();
                if (read.IsSuccess){
                    textArea8.setText(read.Content);
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
        panel.add(button24);


        JButton button26 = new JButton("启动加工");
        button26.setFocusPainted(false);
        button26.setBounds(725,42,96, 29);
        button26.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResult read = fanuc.StartProcessing();
                if (read.IsSuccess){
                    textArea8.setText("Start success");
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
        panel.add(button26);


        JButton button32 = new JButton("系统信息");
        button32.setFocusPainted(false);
        button32.setBounds(827,42,96, 29);
        button32.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<FanucSysInfo> read = fanuc.ReadSysInfo();
                if (read.IsSuccess){
                    textArea8.setText(read.Content.toString());
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
        panel.add(button32);

        JButton button14 = new JButton("开机时间");
        button14.setFocusPainted(false);
        button14.setBounds(11,77,96, 29);
        button14.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                ReadTimeData(0);
            }
        });
        panel.add(button14);

        JButton button15 = new JButton("运行时间");
        button15.setFocusPainted(false);
        button15.setBounds(113,77,96, 29);
        button15.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ReadTimeData(1);
            }
        });
        panel.add(button15);


        JButton button16 = new JButton("切削时间");
        button16.setFocusPainted(false);
        button16.setBounds(215,77,96, 29);
        button16.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ReadTimeData(2);
            }
        });
        panel.add(button16);


        JButton button17 = new JButton("循环时间");
        button17.setFocusPainted(false);
        button17.setBounds(317,77,96, 29);
        button17.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ReadTimeData(3);
            }
        });
        panel.add(button17);


        JButton button30 = new JButton("当前刀号");
        button30.setFocusPainted(false);
        button30.setBounds(419,77,96, 29);
        button30.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                OperateResultExOne<Integer> read = fanuc.ReadCutterNumber();
                if (read.IsSuccess){
                    textArea8.setText("刀号：" + read.Content.toString());
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
        panel.add(button30);


        JButton button40 = new JButton("操作信息");
        button40.setFocusPainted(false);
        button40.setBounds(521,77,96, 29);
        button40.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                OperateResultExOne<FanucOperatorMessage[]> read = fanuc.ReadOperatorMessage();
                if (read.IsSuccess){
                    if (read.Content != null){
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < read.Content.length; i ++){
                            sb.append("Number: " + read.Content[i].Number + " " + read.Content[i].Data);
                            sb.append("\r\n");
                        }
                        textArea8.setText(sb.toString());
                    }
                    else {
                        textArea8.setText("Null");
                    }
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
        panel.add(button40);


        JLabel label2 = new JLabel("宏变量");
        label2.setBounds(8,123,56,17);
        panel.add(label2);

        JTextField texBox3 = new JTextField("4320");
        texBox3.setBounds(62,120,105,23);
        panel.add(texBox3);


        JButton button18 = new JButton("读宏变量");
        button18.setFocusPainted(false);
        button18.setBounds(185,117,96, 29);
        button18.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                OperateResultExOne<Double> read = fanuc.ReadSystemMacroValue(Integer.parseInt(texBox3.getText()));
                if (read.IsSuccess){
                    textArea8.setText("刀号：" + read.Content.toString());
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
        panel.add(button18);


        JLabel label100 = new JLabel("路径：");
        label100.setBounds(308,123,56,17);
        panel.add(label100);

        JTextField texBox100 = new JTextField( "1" );
        texBox100.setBounds(362,120,50,23);
        panel.add(texBox100);

        JButton button100 = new JButton("设置路径");
        button100.setFocusPainted(false);
        button100.setBounds(435,117,96, 29);
        button100.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                fanuc.setOperatePath(Short.parseShort(texBox100.getText()));
                JOptionPane.showMessageDialog(
                        null,
                        "Set Success",
                        "Result",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panel.add(button100);

        JLabel label4 = new JLabel("起始");
        label4.setBounds(8,156,44,17);
        panel.add(label4);

        JTextField texBox4 = new JTextField("1200");
        texBox4.setBounds(62,153,105,23);
        panel.add(texBox4);


        JLabel label6 = new JLabel("结束");
        label6.setBounds(182,156,44,17);
        panel.add(label6);

        JTextField texBox5 = new JTextField("1270");
        texBox5.setBounds(236,153,105,23);
        panel.add(texBox5);


        JButton button23 = new JButton("读R数据");
        button23.setFocusPainted(false);
        button23.setBounds(358,150,96, 29);
        button23.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                OperateResultExOne<byte[]> read = fanuc.ReadRData(Integer.parseInt(texBox4.getText()),Integer.parseInt(texBox5.getText()));
                if (read.IsSuccess){
                    textArea8.setText( SoftBasic.ByteToHexString(read.Content, ' '));
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
        panel.add(button23);


        JLabel label7 = new JLabel("程序号");
        label7.setBounds(8,186,56,17);
        panel.add(label7);

        JTextField texBox6 = new JTextField("15");
        texBox6.setBounds(62,183,105,23);
        panel.add(texBox6);


        JButton button25 = new JButton("设置主程序");
        button25.setFocusPainted(false);
        button25.setBounds(185,180,110, 29);
        button25.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                OperateResult read = fanuc.SetCurrentProgram(Short.parseShort(texBox6.getText()));
                if (read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Set success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
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
        panel.add(button25);


        JLabel label9 = new JLabel("程序号");
        label9.setBounds(602,157,56,17);
        panel.add(label9);

        JButton button29 = new JButton("删除程序");
        button29.setFocusPainted(false);
        button29.setBounds(774,151,96, 29);
        button29.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String path = texBox_path.getText();
                if (!path.endsWith( "/" )) path = path + "/";

                OperateResult read = fanuc.DeleteFile( path + texBox9.getText() );
                if (read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "delete success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "delete Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(button29);


        JButton button28 = new JButton("读取程序");
        button28.setFocusPainted(false);
        button28.setBounds(876,151,96, 29);
        button28.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                OperateResultExOne<String> read = fanuc.ReadProgram(texBox9.getText(), texBox_path.getText());
                if (read.IsSuccess){
                    textArea8.setText(read.Content);
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
        panel.add(button28);



        JLabel label8 = new JLabel("程序文件");
        label8.setBounds(302,186,68,17);
        panel.add(label8);

        JTextField texBox7 = new JTextField("O6.txt");
        texBox7.setBounds(376,183,494,23);
        panel.add(texBox7);


        JButton button27 = new JButton("下传程序");
        button27.setFocusPainted(false);
        button27.setBounds(876,180,96, 29);
        button27.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                String content = "";
                String path = texBox7.getText();
                if (Utilities.IsStringNullOrEmpty(path) ){
                    content = textArea8.getText();
                }
                else {
                    File file = new File(path);
                    if (!file.exists()){
                        JOptionPane.showMessageDialog(
                                null,
                                "file not exists:",
                                "Result",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.US_ASCII);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                OperateResult read = fanuc.WriteProgramContent(
                        content, 512, texBox_path.getText());
                if (read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "download success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "download Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(button27);
    }

    private JTextArea textArea8;
    private void ReadTimeData( int type )
    {
        OperateResultExOne<Long> read = fanuc.ReadTimeData( type );
        if (read.IsSuccess)
        {
            textArea8.setText( (read.Content / 3600)+ " H " + (read.Content % 3600 / 60) + " M " + (read.Content % 3600 % 60) + " S");
        }
        else
        {
            JOptionPane.showMessageDialog(
                    null,
                    "Read Failed:" + read.ToMessageShowString(),
                    "Result",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
