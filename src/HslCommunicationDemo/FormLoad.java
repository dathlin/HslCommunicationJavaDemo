package HslCommunicationDemo;

import HslCommunication.Authorization;
import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Net.NetHandle;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Enthernet.SimplifyNet.NetSimplifyClient;
import HslCommunication.Profinet.Siemens.SiemensPLCS;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Console;
import java.io.IOException;

public class FormLoad extends JDialog
{
    public FormLoad(){
        this.setTitle("HslCommunication Test Tool");
        this.setSize(996, 643);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        AddMenuBar();

        JPanel panel = new JPanel();
        panel.setLayout(null);

        AddMelsecGroup(panel);
        AddSiemensGroup(panel);
        AddModbusGroup(panel);
        AddGeGroup(panel);
        AddOmronGroup(panel);
        AddKeyenceGroup(panel);
        AddXinJEGroup(panel);
        AddHslGroup(panel);
        AddABGroup(panel);
        AddInovanceGroup(panel);
        AddMqttGroup(panel);
        AddFujiGroup(panel);
        AddFatekGroup(panel);
        AddDeltaGroup(panel);
        AddPanasonicGroup(panel);
        AddRobotGroup(panel);
        AddYokogawaGroup((panel));
        this.add(panel);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                NetSimplifyClient client = new NetSimplifyClient("118.24.36.220", 18467);
                OperateResultExOne<String> read = client.ReadFromServer( new NetHandle(200), SoftBasic.FrameworkVersion.toString( ) );
            }
        });
        thread.start();
    }

    private void AddMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        menuBar.setMargin(new Insets(8,8,8,8));
        menuBar.setBackground(new Color(32,178,170));
        JMenu menuAbout = new JMenu("About");
        menuAbout.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(
                        null,
                        "当前的功能没有实现！\r\nThe current function is not implemented",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuBar.add(menuAbout);

        JMenu menuChinese = new JMenu("简体中文");
        menuChinese.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(
                        null,
                        "当前的功能没有实现！\r\nThe current function is not implemented",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuBar.add(menuChinese);

        JMenu menuEnglish = new JMenu("English");
        menuEnglish.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(
                        null,
                        "当前的功能没有实现！\r\nThe current function is not implemented",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuBar.add(menuEnglish);

        JMenu menuChangelog = new JMenu("Changelog");
        menuChangelog.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(
                        null,
                        "当前的功能没有实现！\r\nThe current function is not implemented",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuBar.add(menuChangelog);

        JMenu menuPurchase = new JMenu("Purchase");
        menuPurchase.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://118.24.36.220");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        menuBar.add(menuPurchase);

        JMenu menuVersion = new JMenu("Version:"+ SoftBasic.FrameworkVersion.toString());
        menuBar.add(menuVersion);

        JMenu menuDisclaimer = new JMenu("Disclaimer" );
        menuBar.add(menuDisclaimer);

        menuBar.setVisible(true);
        this.setJMenuBar(menuBar);
    }


    private void AddMelsecGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(9,5,183, 279 + 45);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "Melsec [三菱PLC]"));
        int location_y = 24;

        JButton button1 = new JButton( "MC Binary");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormMelsecBinary form = new FormMelsecBinary();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        JButton button2 = new JButton( "MC Ascii");
        button2.setBounds(15,location_y,150, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormMelsecAscii form = new FormMelsecAscii();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;

        JButton button3 = new JButton( "MC Udp Binary");
        button3.setBounds(15,location_y,150, 32);
        button3.setFocusPainted(false);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormMelsecUdp form = new FormMelsecUdp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button3);
        location_y+=40;

        JButton button4 = new JButton( "MC Udp ASCII");
        button4.setBounds(15,location_y,150, 32);
        button4.setFocusPainted(false);
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormMelsecUdpAscii form = new FormMelsecUdpAscii();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button4);
        location_y+=40;

        JButton button5 = new JButton( "A-1E 二进制");
        button5.setBounds(15,location_y,150, 32);
        button5.setFocusPainted(false);
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormMelsecA1E form = new FormMelsecA1E();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button5);
        location_y+=40;

        JButton button6 = new JButton( "A-1E ASCII");
        button6.setBounds(15,location_y,150, 32);
        button6.setFocusPainted(false);
        button6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormMelsecA1EAsciiNet form = new FormMelsecA1EAsciiNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button6);
        location_y+=40;

        JButton button7 = new JButton( "FxSerialOverTcp");
        button7.setBounds(15,location_y,150, 32);
        button7.setFocusPainted(false);
        button7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormMelsecSerialOverTcp form = new FormMelsecSerialOverTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button7);
        location_y+=40;
        panel.add(buttonPanel);
    }

    private void AddSiemensGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(203,5,183, 279);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "Siemens [西门子PLC]"));
        int location_y = 24;

        JButton button1 = new JButton( "1200");
        button1.setBounds(15,location_y,70, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormSiemensS7 form = new FormSiemensS7(SiemensPLCS.S1200);
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);

        JButton button2 = new JButton( "1500");
        button2.setBounds(95,location_y,70, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormSiemensS7 form = new FormSiemensS7(SiemensPLCS.S1500);
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;

        JButton button3 = new JButton( "300");
        button3.setBounds(15,location_y,70, 32);
        button3.setFocusPainted(false);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormSiemensS7 form = new FormSiemensS7(SiemensPLCS.S300);
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button3);

        JButton button4 = new JButton( "400");
        button4.setBounds(95,location_y,70, 32);
        button4.setFocusPainted(false);
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormSiemensS7 form = new FormSiemensS7(SiemensPLCS.S400);
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button4);
        location_y+=40;

        JButton button5 = new JButton( "200 smart");
        button5.setBounds(15,location_y,150, 32);
        button5.setFocusPainted(false);
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormSiemensS7200 form = new FormSiemensS7200(SiemensPLCS.S200Smart);
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button5);
        location_y+=40;

        JButton button7 = new JButton( "200");
        button7.setBounds(15,location_y,150, 32);
        button7.setFocusPainted(false);
        button7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormSiemensS7200 form = new FormSiemensS7200(SiemensPLCS.S200);
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button7);
        location_y+=40;

        JButton button6 = new JButton( "Fetch/Write");
        button6.setBounds(15,location_y,150, 32);
        button6.setFocusPainted(false);
        button6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormSiemensFW form = new FormSiemensFW();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button6);
        location_y+=40;

        JButton button8 = new JButton( "PPI OverTcp");
        button8.setBounds(15,location_y,150, 32);
        button8.setFocusPainted(false);
        button8.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormSiemensPPIOverTcp form = new FormSiemensPPIOverTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button8);
        location_y+=40;

        panel.add(buttonPanel);
    }

    private void AddModbusGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(395,5,183, 160);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "Modbus"));
        int location_y = 24;

        JButton button1 = new JButton( "Modbus Tcp");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormModbusTcp form = new FormModbusTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        JButton button2 = new JButton( "Modbus RtuOverTcp");
        button2.setBounds(15,location_y,150, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormModbusRtuOverTcp form = new FormModbusRtuOverTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;

        JButton button3 = new JButton( "Modbus Udp");
        button3.setBounds(15, location_y,150, 32);
        button3.setFocusPainted(false);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormMelsecUdp form = new FormMelsecUdp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button3);
        location_y+=40;

        panel.add(buttonPanel);
    }

    private void AddGeGroup(JPanel panel){

        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(395,170,183, 114);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "GE"));
        int location_y = 24;

        JButton button1 = new JButton( "SRTP");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormGeSRTPNet form = new FormGeSRTPNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;


        panel.add(buttonPanel);
    }

    private void AddOmronGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(586,5,183, 279);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "Omron PLC [欧姆龙]"));
        int location_y = 24;

        JButton button1 = new JButton( "Fins-Tcp");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormOmronFins form = new FormOmronFins();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        JButton button2 = new JButton( "Fins-Udp");
        button2.setBounds(15,location_y,150, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormOmronFinsUdp form = new FormOmronFinsUdp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;

        JButton button3 = new JButton( "HostLinkOverTcp");
        button3.setBounds(15,location_y,150, 32);
        button3.setFocusPainted(false);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormOmronHostLinkOverTcp form = new FormOmronHostLinkOverTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button3);
        location_y+=40;

        JButton button4 = new JButton( "Cip");
        button4.setBounds(15,location_y,150, 32);
        button4.setFocusPainted(false);
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormOmronCipNet form = new FormOmronCipNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button4);
        location_y+=40;


        JButton button6 = new JButton( "Connected Cip");
        button6.setBounds(15,location_y,150, 32);
        button6.setFocusPainted(false);
        button6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormOmronConnectedCipNet form = new FormOmronConnectedCipNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button6);
        location_y+=40;

        JButton button5 = new JButton( "C-Mode OverTcp");
        button5.setBounds(15,location_y,150, 32);
        button5.setFocusPainted(false);
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormOmronHostLinkCModeOverTcp form = new FormOmronHostLinkCModeOverTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button5);
        location_y+=40;

        panel.add(buttonPanel);
    }

    private void AddKeyenceGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(777,5,183, 160);// 279
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "Keyence [基恩士]"));
        int location_y = 24;

        JButton button1 = new JButton( "Mc 二进制");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormKeyenceBinary form = new FormKeyenceBinary();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        JButton button2 = new JButton( "Mc Ascii");
        button2.setBounds(15,location_y,150, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormKeyenceAscii form = new FormKeyenceAscii();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;

        JButton button3 = new JButton( "Nano Over TCP");
        button3.setBounds(15,location_y,150, 32);
        button3.setFocusPainted(false);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormKeyenceNanoOverTcp form = new FormKeyenceNanoOverTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button3);
        location_y+=40;

        panel.add(buttonPanel);
    }

    private void AddXinJEGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(777,170,183, 114);// 279
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "XINJE [信捷]"));
        int location_y = 24;

        JButton button1 = new JButton( "XINJE TCP");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormXINJETcp form = new FormXINJETcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        JButton button2 = new JButton( "Serial OverTcp");
        button2.setBounds(15,location_y,150, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormXinJESerialOverTcp form = new FormXinJESerialOverTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;

        panel.add(buttonPanel);
    }

    private void AddHslGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(203,296,183, 160);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "Hsl"));
        int location_y = 24;

        JButton button1 = new JButton( "NetSimplify");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormSimplifyNet form = new FormSimplifyNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        JButton button2 = new JButton( "Net Push");
        button2.setBounds(15,location_y,150, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormPushNet form = new FormPushNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;

        JButton button3 = new JButton( "Net Udp");
        button3.setBounds(15,location_y,150, 32);
        button3.setFocusPainted(false);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormNetUdpClient form = new FormNetUdpClient();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button3);
        location_y+=40;

        panel.add(buttonPanel);
    }

    private void AddMqttGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(395,296,183, 160);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "Mqtt Sync Client"));
        int location_y = 24;

        JButton button1 = new JButton( "Mqtt Sync Client");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormMqttSyncClient form = new FormMqttSyncClient();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        panel.add(buttonPanel);
    }

    private void AddFujiGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(395,461,183, 114);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "Fuji PLC [富士]"));
        int location_y = 24;

        JButton button1 = new JButton( "SPB OverTcp");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormFujiSPBOverTcp form = new FormFujiSPBOverTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        JButton button2 = new JButton( "SPH");
        button2.setBounds(15,location_y,150, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormFujiSPHNet form = new FormFujiSPHNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;


        panel.add(buttonPanel);
    }

    private void AddABGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(9,296 + 45, 183, 279 - 45);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "AB plc [罗克韦尔]"));
        int location_y = 24;

        JButton button1 = new JButton( "Cip");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormABCip form = new FormABCip();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        JButton button2 = new JButton( "Micro Cip");
        button2.setBounds(15,location_y,150, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormAllenBradleyMicroCip form = new FormAllenBradleyMicroCip();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;

        JButton button3 = new JButton( "SLC Net");
        button3.setBounds(15,location_y,150, 32);
        button3.setFocusPainted(false);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormAllenBradleySLCNet form = new FormAllenBradleySLCNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button3);
        location_y+=40;

        JButton button4 = new JButton( "PCCC (1400)");
        button4.setBounds(15,location_y,150, 32);
        button4.setFocusPainted(false);
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormAllenBradleyPcccNet form = new FormAllenBradleyPcccNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button4);
        location_y+=40;

        panel.add(buttonPanel);
    }


    private void AddInovanceGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds( 203,461,183, 114);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "Inovance PLC [汇川]"));
        int location_y = 24;

        JButton button1 = new JButton( "Serial OverTcp");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormInovanceSerialOverTcp form = new FormInovanceSerialOverTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        JButton button2 = new JButton( "Tcp");
        button2.setBounds(15,location_y,150, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormInovanceTcpNet form = new FormInovanceTcpNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;


        panel.add(buttonPanel);
    }


    private void AddFatekGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(586,296,183, 75);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "Fatek PLC [永宏]"));
        int location_y = 24;

        JButton button1 = new JButton( "ProgramOverTcp");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormFatekProgramOverTcp form = new FormFatekProgramOverTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        panel.add(buttonPanel);
    }

    private void AddYokogawaGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(586,381,183, 75);
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Yokogawa PLC [横河]"));
        int location_y = 24;

        JButton button1 = new JButton( "Link Tcp");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormYokogawaLinkTcp form = new FormYokogawaLinkTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;


        panel.add(buttonPanel);
    }

    private void AddPanasonicGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(586,461,183, 114);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "Panasonic PLC [松下]"));
        int location_y = 24;

        JButton button1 = new JButton( "MC Binary");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormPanasonicMcNet form = new FormPanasonicMcNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        JButton button2 = new JButton( "MewtocolOverTcp");
        button2.setBounds(15,location_y,150, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormPanasonicMewtocolOverTcp form = new FormPanasonicMewtocolOverTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;


        panel.add(buttonPanel);
    }

    private void AddRobotGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(777,296,183, 160);
        buttonPanel.setBorder(BorderFactory.createTitledBorder( "Robot"));
        int location_y = 24;

        JButton button1 = new JButton( "Fanuc Robot");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormFanucInterfaceNet form = new FormFanucInterfaceNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;

        JButton button2 = new JButton( "Fanuc CNC");
        button2.setBounds(15,location_y,150, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormFanucCnc0i form = new FormFanucCnc0i();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;
        panel.add(buttonPanel);
    }


    private void AddDeltaGroup(JPanel panel){
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(777,461,183, 114);
        buttonPanel.setBorder(BorderFactory.createTitledBorder(  "Delta PLC [台达]"));
        int location_y = 24;


        JButton button1 = new JButton( "Tcp Net");
        button1.setBounds(15,location_y,150, 32);
        button1.setFocusPainted(false);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormDeltaDvpTcpNet form = new FormDeltaDvpTcpNet();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button1);
        location_y+=40;


        JButton button2 = new JButton( "Serial Over Tcp");
        button2.setBounds(15,location_y,150, 32);
        button2.setFocusPainted(false);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setVisible(false);
                FormDeltaSerialOverTcp form = new FormDeltaSerialOverTcp();
                form.setVisible(true);
                form.dispose();
                setVisible(true);
            }
        });
        buttonPanel.add(button2);
        location_y+=40;

        panel.add(buttonPanel);
    }
}

