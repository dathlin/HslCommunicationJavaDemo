package HslCommunicationDemo;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Net.NetHandle;
import HslCommunication.Core.Types.FunctionOperate;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Enthernet.SimplifyNet.NetSimplifyClient;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunicationDemo.PLC.AllenBradley.FormABCip;
import HslCommunicationDemo.PLC.AllenBradley.FormAllenBradleyMicroCip;
import HslCommunicationDemo.PLC.AllenBradley.FormAllenBradleyPcccNet;
import HslCommunicationDemo.PLC.AllenBradley.FormAllenBradleySLCNet;
import HslCommunicationDemo.PLC.Beckhoff.FormBeckhoffAdsNet;
import HslCommunicationDemo.PLC.Delta.FormDeltaDvpTcpNet;
import HslCommunicationDemo.PLC.Delta.FormDeltaSerialOverTcp;
import HslCommunicationDemo.PLC.Fanuc.FormFanucCnc0i;
import HslCommunicationDemo.PLC.Fanuc.FormFanucInterfaceNet;
import HslCommunicationDemo.PLC.Fatek.FormFatekProgramOverTcp;
import HslCommunicationDemo.PLC.Fuji.FormFujiSPBOverTcp;
import HslCommunicationDemo.PLC.Fuji.FormFujiSPHNet;
import HslCommunicationDemo.PLC.Ge.FormGeSRTPNet;
import HslCommunicationDemo.PLC.Inovance.FormInovanceSerialOverTcp;
import HslCommunicationDemo.PLC.Inovance.FormInovanceTcpNet;
import HslCommunicationDemo.PLC.Keyence.FormKeyenceAscii;
import HslCommunicationDemo.PLC.Keyence.FormKeyenceBinary;
import HslCommunicationDemo.PLC.Keyence.FormKeyenceNanoOverTcp;
import HslCommunicationDemo.PLC.MegMeet.FormMegMeetSerialOverTcp;
import HslCommunicationDemo.PLC.MegMeet.FormMegMeetTcpNet;
import HslCommunicationDemo.PLC.Melsec.*;
import HslCommunicationDemo.PLC.Modbus.FormModbusRtuOverTcp;
import HslCommunicationDemo.PLC.Modbus.FormModbusServer;
import HslCommunicationDemo.PLC.Modbus.FormModbusTcp;
import HslCommunicationDemo.PLC.Modbus.FormModbusUdpNet;
import HslCommunicationDemo.PLC.Omron.*;
import HslCommunicationDemo.PLC.Panasonic.FormPanasonicMcNet;
import HslCommunicationDemo.PLC.Panasonic.FormPanasonicMewtocolOverTcp;
import HslCommunicationDemo.PLC.Siemens.*;
import HslCommunicationDemo.PLC.Toyota.FormToyoPuc;
import HslCommunicationDemo.PLC.XinJE.FormXINJETcp;
import HslCommunicationDemo.PLC.XinJE.FormXinJEInternalNet;
import HslCommunicationDemo.PLC.XinJE.FormXinJESerialOverTcp;
import HslCommunicationDemo.PLC.Yokogawa.FormYokogawaLinkTcp;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FormMain extends JDialog
{
    /**
     * 整个系统的语言信息，1表示中文，2表示英文
     */
    public static int Language = 2;
    /**
     * 整个系统的主题颜色信息
     */
    public static Color ThemeColor = new Color(92,108,124);

    public FormMain(){
        this.setTitle("HslCommunication Test Tool");
        this.setSize(1256, 755);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        AddMenuBar();

        JPanel panel = new JPanel();
        panel.setLayout(null);

        tabbedPane = new JTabbedPane( );
        tabbedPane.setBounds(222, 5, 1000, 680);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                tabbedPane.setBounds(222, 5, panel.getWidth() - 225, panel.getHeight() - 10);
            }
        });
        panel.add(tabbedPane);

        mapJPanel = new HashMap<>();
        AddMelsecGroup(node);
        AddSiemensGroup(node);
        AddModbusGroup(node);
        AddOmronGroup(node);
        AddKeyenceGroup(node);
        AddXinJEGroup(node);
        AddABGroup(node);
        AddBeckhoffGroup(node);
        AddInovanceGroup(node);
        AddMegmeetGroup(node);
        AddFatekGroup(node);
        AddYokogawaGroup(node);
        AddPanasonicGroup(node);
        AddFujiGroup(node);
        AddDeltaGroup(node);
        AddGeGroup(node);
        AddToyotaGroup(node);
        AddRobotGroup(node);
        AddMqttGroup(node);
        AddHslGroup(node);
        AddDeviceList(panel);
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
        menuBar.setBackground(ThemeColor);
        JMenu menuAbout = new JMenu("About");
        menuAbout.setForeground(new Color(233,233,233));
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
        menuChinese.setForeground(new Color(233,233,233));
        menuChinese.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                FormMain.Language = 1;
                JOptionPane.showMessageDialog(
                        null,
                        "已选择中文！\r\nChinese selected",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuBar.add(menuChinese);

        JMenu menuEnglish = new JMenu("English");
        menuEnglish.setForeground(new Color(233,233,233));
        menuEnglish.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                FormMain.Language = 2;
                JOptionPane.showMessageDialog(
                        null,
                        "已选择英文！\r\nEnglish selected",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuBar.add(menuEnglish);

        JMenu menuChangelog = new JMenu("Changelog");
        menuChangelog.setForeground(new Color(233,233,233));
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
        menuPurchase.setForeground(new Color(233,233,233));
        menuPurchase.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://www.hsltechnology.cn/Home/Licence?area=HslCommunication");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        menuBar.add(menuPurchase);

        JMenu menuVersion = new JMenu("Version:"+ SoftBasic.FrameworkVersion.toString());
        menuVersion.setForeground(new Color(233,233,233));
        menuBar.add(menuVersion);

        JMenu menuDisclaimer = new JMenu("Disclaimer" );
        menuDisclaimer.setForeground(new Color(233,233,233));
        menuBar.add(menuDisclaimer);

        menuBar.setVisible(true);
        this.setJMenuBar(menuBar);
    }
    private DefaultMutableTreeNode node = new DefaultMutableTreeNode( "" );
    private JTabbedPane tabbedPane;
    private Map<String, FunctionOperate<JPanel>> mapJPanel;


    private void AddDeviceList(JPanel panel){
        node.setUserObject( Language == 1 ? "设备列表" : "DeviceList" );

        JTree jTree = new JTree(node);
        jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() > 1){
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                    if (node != null){
                        String text = node.getUserObject().toString( );
                        FunctionOperate<JPanel> create = mapJPanel.get(node.getUserObject());
                        if (create!=null) {
                            JPanel form = create.Action();
                            tabbedPane.add(text, form);
                            tabbedPane.setSelectedComponent(form);
                        }
                    }
                }
            }
        });
        JScrollPane jsp = new JScrollPane(jTree);
        panel.add(jsp);
        jsp.setBounds(5,5,210, panel.getHeight() - 10);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                jsp.setBounds(5,5,210, panel.getHeight() - 10);
            }
        });

        panel.add(jsp);


    }

    private void AddTreeNode( DefaultMutableTreeNode node, String key, FunctionOperate create ){
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(key);
        node.add(child);
        mapJPanel.put(key, create);
    }
    private void AddMelsecGroup(DefaultMutableTreeNode treeNode){

        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Melsec [三菱PLC]");
        AddTreeNode( node, "MC Binary", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMelsecBinary(tabbedPane);
            }
        } );
        AddTreeNode( node, "MC Ascii", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMelsecAscii(tabbedPane);
            }
        } );
        AddTreeNode( node, "MC Udp Binary", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMelsecUdp(tabbedPane);
            }
        } );
        AddTreeNode( node, "MC Udp ASCII", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMelsecUdpAscii(tabbedPane);
            }
        } );
        AddTreeNode( node, "MC R Binary", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMelsecMcRNet(tabbedPane);
            }
        } );
        AddTreeNode( node, "MC Server", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMelsecMcServer(tabbedPane);
            }
        } );
        AddTreeNode( node, "A-1E 二进制", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMelsecA1E(tabbedPane);
            }
        } );
        AddTreeNode( node, "A-1E ASCII", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMelsecA1EAsciiNet(tabbedPane);
            }
        } );
        AddTreeNode( node, "FxSerialOverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMelsecSerialOverTcp(tabbedPane);
            }
        } );
        AddTreeNode( node, "FxLinksOverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMelsecFxLinksOverTcp(tabbedPane);
            }
        } );
        AddTreeNode( node, "A3CNetOverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMelsecA3CNetOverTcp(tabbedPane);
            }
        } );

        treeNode.add(node);
    }

    private void AddSiemensGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Siemens [西门子PLC]");
        AddTreeNode( node, "S7-1200", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormSiemensS7(tabbedPane,SiemensPLCS.S1200 );
            }
        } );
        AddTreeNode( node, "S7-1500", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormSiemensS7(tabbedPane,SiemensPLCS.S1500 );
            }
        } );
        AddTreeNode( node, "S7-300", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormSiemensS7(tabbedPane,SiemensPLCS.S300 );
            }
        } );
        AddTreeNode( node, "S7-400", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormSiemensS7(tabbedPane,SiemensPLCS.S400 );
            }
        } );
        AddTreeNode( node, "S7-200 smart", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormSiemensS7(tabbedPane,SiemensPLCS.S200Smart );
            }
        } );
        AddTreeNode( node, "S7-200", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormSiemensS7200(tabbedPane,SiemensPLCS.S200 );
            }
        } );
        AddTreeNode( node, "Fetch/Write", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormSiemensFW( tabbedPane );
            }
        } );
        AddTreeNode( node, "PPI OverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormSiemensPPIOverTcp( tabbedPane );
            }
        } );
        AddTreeNode( node, "S7 Server", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormSiemensS7Server( tabbedPane );
            }
        } );

        treeNode.add(node);
    }

    private void AddModbusGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Modbus [Modbus设备]");
        AddTreeNode( node, "Modbus Tcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormModbusTcp( tabbedPane );
            }
        } );
        AddTreeNode( node, "Modbus RtuOverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormModbusRtuOverTcp( tabbedPane );
            }
        } );
        AddTreeNode( node, "Modbus Udp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormModbusUdpNet( tabbedPane );
            }
        } );
        AddTreeNode( node, "Modbus Server", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormModbusServer( tabbedPane );
            }
        } );

        treeNode.add(node);
    }

    private void AddBeckhoffGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Beckhoff[倍福]");
        AddTreeNode( node, "Ads Net", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormBeckhoffAdsNet( tabbedPane );
            }
        } );

        treeNode.add(node);
    }

    private void AddGeGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Ge PLC[通用]");
        AddTreeNode( node, "Ge SRTP", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormGeSRTPNet( tabbedPane );
            }
        } );

        treeNode.add(node);
    }

    private void AddToyotaGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Toyota PLC[丰田]");
        AddTreeNode( node, "ToyoPuc Tcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormToyoPuc( tabbedPane );
            }
        } );

        treeNode.add(node);
    }


    private void AddOmronGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Omron PLC [欧姆龙]");
        AddTreeNode( node, "Fins-Tcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormOmronFins( tabbedPane );
            }
        } );
        AddTreeNode( node, "Fins-Udp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormOmronFinsUdp( tabbedPane );
            }
        } );
        AddTreeNode( node, "HostLinkOverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormOmronHostLinkOverTcp( tabbedPane );
            }
        } );
        AddTreeNode( node, "OmronCipNet", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormOmronCipNet( tabbedPane );
            }
        } );
        AddTreeNode( node, "OmronConnectedCip", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormOmronConnectedCipNet( tabbedPane );
            }
        } );
        AddTreeNode( node, "C-Mode OverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormOmronHostLinkCModeOverTcp( tabbedPane );
            }
        } );

        treeNode.add(node);

    }

    private void AddKeyenceGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Keyence [基恩士]");
        AddTreeNode( node, "Keyence Mc Binary", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormKeyenceBinary( tabbedPane );
            }
        } );
        AddTreeNode( node, "Keyence Mc Ascii", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormKeyenceAscii( tabbedPane );
            }
        } );
        AddTreeNode( node, "Nano Over TCP", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormKeyenceNanoOverTcp( tabbedPane );
            }
        } );

        treeNode.add(node);
    }

    private void AddXinJEGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "XINJE [信捷]");
        AddTreeNode( node, "XINJE TCP", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormXINJETcp( tabbedPane );
            }
        } );
        AddTreeNode( node, "XINJE Serial OverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormXinJESerialOverTcp( tabbedPane );
            }
        } );
        AddTreeNode( node, "XinJEInternalNet", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormXinJEInternalNet( tabbedPane );
            }
        } );

        treeNode.add(node);
    }

    private void AddABGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "AB plc [罗克韦尔]");
        AddTreeNode( node, "AllenBradly Cip", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormABCip( tabbedPane );
            }
        } );
        AddTreeNode( node, "AllenBradly Micro Cip", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormAllenBradleyMicroCip( tabbedPane );
            }
        } );
        AddTreeNode( node, "AllenBradly SLC Net", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormAllenBradleySLCNet( tabbedPane );
            }
        } );
        AddTreeNode( node, "AllenBradly PCCC(1400)", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormAllenBradleyPcccNet( tabbedPane );
            }
        } );

        treeNode.add(node);
    }

    private void AddFujiGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Fuji PLC [富士]");
        AddTreeNode( node, "SPBOverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormFujiSPBOverTcp( tabbedPane );
            }
        } );
        AddTreeNode( node, "SPH", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormFujiSPHNet( tabbedPane );
            }
        } );

        treeNode.add(node);
    }


    private void AddInovanceGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Inovance PLC [汇川]");
        AddTreeNode( node, "InovanceSerialOverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormInovanceSerialOverTcp( tabbedPane );
            }
        } );
        AddTreeNode( node, "InovanceTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormInovanceTcpNet( tabbedPane );
            }
        } );
        treeNode.add(node);
    }


    private void AddMegmeetGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Megmeet PLC [麦格米特]");
        AddTreeNode( node, "MegMeetSerialOverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMegMeetSerialOverTcp( tabbedPane );
            }
        } );
        AddTreeNode( node, "MegMeetTcpNet", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMegMeetTcpNet( tabbedPane );
            }
        } );
        treeNode.add(node);
    }


    private void AddFatekGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Fatek PLC [永宏]");
        AddTreeNode( node, "FatekProgramOverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormFatekProgramOverTcp( tabbedPane );
            }
        } );

        treeNode.add(node);
    }

    private void AddYokogawaGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Yokogawa PLC [横河]");
        AddTreeNode( node, "YokogawaLinkTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormYokogawaLinkTcp( tabbedPane );
            }
        } );

        treeNode.add(node);
    }

    private void AddPanasonicGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Panasonic PLC [松下]");
        AddTreeNode( node, "PanasonicMcBinary", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormPanasonicMcNet( tabbedPane );
            }
        } );
        AddTreeNode( node, "MewtocolOverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormPanasonicMewtocolOverTcp( tabbedPane );
            }
        } );

        treeNode.add(node);
    }


    private void AddDeltaGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Delta PLC [台达]");
        AddTreeNode( node, "DeltaTcpNet", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormDeltaDvpTcpNet( tabbedPane );
            }
        } );
        AddTreeNode( node, "DeltaSerialOverTcp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormDeltaSerialOverTcp( tabbedPane );
            }
        } );

        treeNode.add(node);
    }

    private void AddRobotGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Robot & CNC");
        AddTreeNode( node, "Fanuc Robot", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormFanucInterfaceNet( tabbedPane );
            }
        } );
        AddTreeNode( node, "Fanuc CNC", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormFanucCnc0i( tabbedPane );
            }
        } );

        treeNode.add(node);
    }

    private void AddMqttGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Mqtt");
        AddTreeNode( node, "MqttSyncClient", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormMqttSyncClient( tabbedPane );
            }
        } );

        treeNode.add(node);

    }
    private void AddHslGroup(DefaultMutableTreeNode treeNode){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode( "Hsl Protocol");
        AddTreeNode( node, "NetSimplify", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormSimplifyNet( tabbedPane );
            }
        } );
        AddTreeNode( node, "HslNetPush", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormPushNet( tabbedPane );
            }
        } );
        AddTreeNode( node, "HslNetUdp", new FunctionOperate<JPanel>(){
            @Override
            public JPanel Action() {
                return new FormNetUdpClient( tabbedPane );
            }
        } );

        treeNode.add(node);
    }


}
