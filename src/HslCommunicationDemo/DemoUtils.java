package HslCommunicationDemo;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Net.IReadWriteNet;
import HslCommunication.Core.Types.Array;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunicationDemo.Demo.DeviceAddressExample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DemoUtils {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss] ");

    /**
     * 将结果信息显示出来，在界面里进行显示
     * @param result 等待显示的结果信息
     * @param address 读取的地址数据
     * @param textBox 显示结果的文本框
     * @param scrollPane 支持滚动条的控件
     * @param <T> 类型对象
     */
    public static <T> void ReadResultRender(OperateResultExOne<T> result, String address, JTextArea textBox, JScrollPane scrollPane ) {
        if (result.IsSuccess) {
            if (result.Content.getClass().isArray()) {
                textBox.append(dateFormat.format(new Date()) + "[" + address + "] " + SoftBasic.ArrayFormat(result.Content) + "\r\n");
                JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
                scrollBar.setValue(scrollBar.getMaximum());
            } else {
                textBox.append(dateFormat.format(new Date()) + "[" + address + "] " + result.Content + "\r\n");
                JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
                scrollBar.setValue(scrollBar.getMaximum());
            }
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    dateFormat.format(new Date()) + "[" + address + "] Read Failed\r\nReason:" + result.ToMessageShowString(),
                    "Result",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void WriteResultRender(OperateResult result, String address ) {
        if (result.IsSuccess) {
            JOptionPane.showMessageDialog(
                    null,
                    dateFormat.format(new Date()) + "[" + address + "] Write Success",
                    "Result",
                    JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    dateFormat.format(new Date()) + "[" + address + "] Write Failed\r\nReason:" + result.ToMessageShowString(),
                    "Result",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void OpResultRender(OperateResult result, String op ) {
        if (result.IsSuccess) {
            JOptionPane.showMessageDialog(
                    null,
                    dateFormat.format(new Date()) + "[" + op + "] Success",
                    "Result",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    dateFormat.format(new Date()) + "[" + op + "] Failed\r\nReason:" + result.ToMessageShowString(),
                    "Result",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void BulkReadRenderResult(IReadWriteNet readWrite, JTextField addTextBox, JTextField lengthTextBox, JTextArea resultTextBox ) {
        try {
            OperateResultExOne<byte[]> read = readWrite.Read(addTextBox.getText(), Short.parseShort(lengthTextBox.getText()));
            if (read.IsSuccess) {
                resultTextBox.setText("Result：" + HslCommunication.BasicFramework.SoftBasic.ByteToHexString(read.Content));
            } else {
                JOptionPane.showMessageDialog(null, "Read Failed：" + read.ToMessageShowString(), "Result", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Read Failed：" + ex.getMessage(), "Result", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void SetPanelEnabled(JPanel panel, boolean enable) {
        Component[] components = panel.getComponents();

        //System.out.println("Component Length:" + components.length);
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel1 = (JPanel) component;
                panel1.setEnabled(enable);
                SetPanelEnabled(panel1, enable);
            }
            else if (component instanceof JTabbedPane){
                JTabbedPane tabbedPane = (JTabbedPane)component;
                tabbedPane.setEnabled(enable);
            }
            else {
                component.setEnabled(enable);
            }
        }

    }

    public static JPanel CreateConnectPanel( JPanel parent ) {
        return CreateConnectPanel(parent, 59);
    }

    public static JPanel CreateConnectPanel( JPanel parent, int height )
    {
        JPanel panelConnect = new JPanel( );
        panelConnect.setLayout(null);
        panelConnect.setBounds(3,28,1000, height);
        panelConnect.setBorder(BorderFactory.createTitledBorder( ""));

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panelConnect.setBounds(3, 27, parent.getWidth() - 5, height);
            }
        });
        return panelConnect;
    }

    public static JPanel CreateContentPanel( JPanel parent )
    {
        return CreateContentPanel(parent, 90);
    }
    public static JPanel CreateContentPanel( JPanel parent, int y )
    {
        JPanel panelConnect = new JPanel( );
        panelConnect.setLayout(null);
        panelConnect.setBounds(3,y,1000, 580);
        panelConnect.setBorder(BorderFactory.createTitledBorder( ""));

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panelConnect.setBounds(3, y, parent.getWidth() - 5, parent.getHeight() - y - 2);
            }
        });
        return panelConnect;
    }

    public static UserControlReadWriteDevice CreateDevicePanel( JPanel parent )
    {
        return CreateDevicePanel(parent, 90);
    }

    public static UserControlReadWriteDevice CreateDevicePanel( JPanel parent, int y )
    {
        UserControlReadWriteDevice panelConnect = new UserControlReadWriteDevice( parent );
        panelConnect.setLayout(null);
        panelConnect.setBounds(3,y,1000, 580);
        panelConnect.setBorder(BorderFactory.createTitledBorder( ""));

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panelConnect.setBounds(3, y, parent.getWidth() - 5, parent.getHeight() - y - 2);
            }
        });
        parent.add(panelConnect);
        return panelConnect;
    }


    public static UserControlReadWriteServer CreateServerPanel( JPanel parent )
    {
        UserControlReadWriteServer panelConnect = new UserControlReadWriteServer( parent );
        panelConnect.setLayout(null);
        panelConnect.setBounds(3,90,1000, 580);
        panelConnect.setBorder(BorderFactory.createTitledBorder( ""));

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panelConnect.setBounds(3, 90, parent.getWidth() - 5, parent.getHeight() - 92);
            }
        });
        parent.add(panelConnect);
        return panelConnect;
    }

    public static DeviceAddressExample[] GetAddressExamples( DeviceAddressExample[] array1, DeviceAddressExample[] array2 ) {
        DeviceAddressExample[] array3 = new DeviceAddressExample[array1.length + array2.length];
        System.arraycopy(array1, 0, array3, 0, array1.length);
        System.arraycopy(array2, 0, array3, array1.length, array2.length);
        return array3;
    }
    public static DeviceAddressExample[] GetAddressExamples( DeviceAddressExample[] array1, DeviceAddressExample deviceAddressExample ){
        return GetAddressExamples(array1, new DeviceAddressExample[]{deviceAddressExample});
    }
    public static DeviceAddressExample[] GetAddressExamples( DeviceAddressExample[] array1, DeviceAddressExample deviceAddressExample1, DeviceAddressExample deviceAddressExample2 ) {
        return GetAddressExamples(array1, new DeviceAddressExample[]{deviceAddressExample1, deviceAddressExample2});
    }

    public static JTextField CreateIpAddressTextBox( JPanel panelConnect ) {
        JLabel label1 = new JLabel("Ip：");
        label1.setBounds(8, 17, 56, 17);
        panelConnect.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(60, 14, 200, 23);
        textField1.setText("127.0.0.1");
        panelConnect.add(textField1);

        return textField1;
    }

    public static JTextField CreateIpPortTextBox( JPanel panelConnect, String port ) {
        JLabel label1 = new JLabel("Port：");
        label1.setBounds(270, 17, 56, 17);
        panelConnect.add(label1);

        JTextField textField2 = new JTextField();
        textField2.setBounds(320, 14, 61, 23);
        textField2.setText(port);
        panelConnect.add(textField2);
        return textField2;
    }

    public static String IpAddressInputWrong = "IpAddress input wrong";
    public static String PortInputWrong = "Port input wrong";
    public static String SlotInputWrong = "Slot input wrong";
    public static String BaudRateInputWrong = "Baud rate input wrong";
    public static String DataBitsInputWrong = "Data bit input wrong";
    public static String StopBitInputWrong = "Stop bit input wrong";


    public static SimpleDateFormat FormatterDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat FormatterDate = new SimpleDateFormat("yyyy-MM-dd");
}
