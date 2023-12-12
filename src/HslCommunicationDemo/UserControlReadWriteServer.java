package HslCommunicationDemo;

import HslCommunication.Core.Net.IReadWriteNet;
import HslCommunication.Core.Net.NetworkBase.NetworkDoubleBase;
import HslCommunication.Core.Net.NetworkBase.NetworkUdpBase;
import HslCommunicationDemo.Demo.BatchReadControl;
import HslCommunicationDemo.Demo.MessageReadControl;
import HslCommunicationDemo.Demo.ServerLogControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class UserControlReadWriteServer extends JPanel {

    public UserControlReadWriteServer(JPanel parent) {
        userControlReadWriteOp = new UserControlReadWriteOp( this );
        add(userControlReadWriteOp);


        tabbedPane = new JTabbedPane( );
        tabbedPane.setBounds(3, 270, 1000, 680);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                tabbedPane.setBounds(3, 270, getWidth() - 5, getHeight() - 270 - 3);
            }
        });
        add(tabbedPane);


        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                setBounds(0, 0, parent.getWidth(), parent.getHeight());
            }
        });
        parent.add(this);

        logControl = new ServerLogControl();
        tabbedPane.add( "Log", logControl );

        batchReadControl = new BatchReadControl();
        tabbedPane.add("BatchRead", batchReadControl);
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
        userControlReadWriteOp.setEnabled(enabled);
        tabbedPane.setEnabled(enabled);
        batchReadControl.setEnabled(enabled);
        logControl.setEnabled(enabled);

        DemoUtils.SetPanelEnabled(userControlReadWriteOp, enabled);
        DemoUtils.SetPanelEnabled(batchReadControl, enabled);

    }

    /**
     * 设置当前的读写通信类对象
     * @param readWrite 通信对象
     * @param address 默认地址
     * @param strLength 长度信息
     */
    public void SetReadWriteNet(IReadWriteNet readWrite, String address, int strLength ) {
        this.userControlReadWriteOp.SetReadWriteNet(readWrite, address, strLength);
        this.batchReadControl.SetReadWriteNet(readWrite, address, strLength);
        setEnabled(true);
    }

    /**
     * 新增一个自定义的控件信息
     * @param control 自定义的控件实现
     * @param show 是否显示出来
     * @param title 标题名称
     */
    public void AddSpecialFunctionTab( JPanel control, boolean show, String title )
    {
        this.tabbedPane.add(title, control);
        if (show) this.tabbedPane.setSelectedComponent(control);
    }

    /**
     * 获取日志控件
     * @return 控件对象
     */
    public ServerLogControl getLogControl(){
        return this.logControl;
    }

    private ServerLogControl logControl;
    private BatchReadControl batchReadControl;
    private UserControlReadWriteOp userControlReadWriteOp;
    private JTabbedPane tabbedPane;

}
