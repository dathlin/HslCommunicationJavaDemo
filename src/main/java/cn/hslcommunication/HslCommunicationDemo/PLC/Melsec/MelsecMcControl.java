package cn.hslcommunication.HslCommunicationDemo.PLC.Melsec;

import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Melsec.Helper.IReadWriteMc;
import cn.hslcommunication.HslCommunicationDemo.DemoUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MelsecMcControl extends JPanel {
    public MelsecMcControl(){
        setLayout(null);

        JButton button1 = new JButton("RemoteReset");
        button1.setFocusPainted(false);
        button1.setBounds(10 ,10, 120, 28);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button1.isEnabled()) return;
                super.mouseClicked(e);
                if (plc == null) {
                    return;
                }
                DemoUtils.OpResultRender(plc.RemoteReset(), "RemoteReset");
            }
        });
        add(button1);

        JButton button2 = new JButton("ErrorReset");
        button2.setFocusPainted(false);
        button2.setBounds(140 ,10, 120, 28);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button2.isEnabled()) return;
                super.mouseClicked(e);
                if (plc == null) {
                    return;
                }
                DemoUtils.OpResultRender(plc.ErrorStateReset(), "ErrorStateReset");
            }
        });
        add(button2);


        JButton button3 = new JButton("RemoteRun");
        button3.setFocusPainted(false);
        button3.setBounds(270 ,10, 120, 28);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button3.isEnabled()) return;
                super.mouseClicked(e);
                if (plc == null) {
                    return;
                }
                DemoUtils.OpResultRender(plc.RemoteRun(), "RemoteRun");
            }
        });
        add(button3);

        JButton button4 = new JButton("RemoteStop");
        button4.setFocusPainted(false);
        button4.setBounds(400 ,10, 120, 28);
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button4.isEnabled()) return;
                super.mouseClicked(e);
                if (plc == null) {
                    return;
                }
                DemoUtils.OpResultRender(plc.RemoteStop(), "RemoteStop");
            }
        });
        add(button4);


        JButton button5 = new JButton("PLC type");
        button5.setFocusPainted(false);
        button5.setBounds(530 ,10, 120, 28);
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!button5.isEnabled()) return;
                super.mouseClicked(e);
                if (plc == null) {
                    return;
                }
                OperateResultExOne<String> result = plc.ReadPlcType();
                if (result.IsSuccess) {
                    JOptionPane.showMessageDialog(
                            null,
                            "PLC Type: " + result.Content,
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Failed，Reason:" + result.ToMessageShowString(),
                            "Result",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        add(button5);
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
        DemoUtils.SetPanelEnabled(this, enabled);
    }

    public void SetReadWritePlc(IReadWriteMc plc){
        this.plc = plc;
    }

    private IReadWriteMc plc;
}
