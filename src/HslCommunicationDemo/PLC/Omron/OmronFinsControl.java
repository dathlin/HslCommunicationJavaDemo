package HslCommunicationDemo.PLC.Omron;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Omron.IOmronFins;
import HslCommunication.Profinet.Omron.OmronCpuUnitData;
import HslCommunication.Profinet.Omron.OmronCpuUnitStatus;
import HslCommunication.Utilities;
import HslCommunicationDemo.DemoUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class OmronFinsControl extends JPanel {
    public OmronFinsControl() {
        setLayout(null);

        JLabel label2 = new JLabel("<html><span style=\"color:red\">Run Stop 请谨慎操作，确认安全为前提</span></html>");
        label2.setBounds(10, 45, 300, 17);
        add(label2);

        JTextArea textArea_data = new JTextArea();
        textArea_data.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea_data);
        jsp.setBounds(10, 70, 500, 100);
        add(jsp);


        JButton button1 = new JButton("Run");
        button1.setFocusPainted(false);
        button1.setBounds(10, 8, 100, 28);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false) return;
                super.mouseClicked(e);
                OperateResult read = omron.Run();
                if (read.IsSuccess) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Run Success:",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Run Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button1);

        JButton button2 = new JButton("Stop");
        button2.setFocusPainted(false);
        button2.setBounds(120, 8, 100, 28);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button2.isEnabled() == false) return;
                super.mouseClicked(e);
                OperateResult stop = omron.Stop();
                if (stop.IsSuccess) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Stop Success:",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Stop Failed:" + stop.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button2);

        JButton button3 = new JButton("Cpu Data");
        button3.setFocusPainted(false);
        button3.setBounds( 230, 8, 100, 28 );
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button3.isEnabled() == false) return;
                super.mouseClicked(e);
                OperateResultExOne<OmronCpuUnitData> read = omron.ReadCpuUnitData();
                if (read.IsSuccess) {
                    textArea_data.setText(read.Content.toString());
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button3);

        JButton button4 = new JButton("Cpu Status");
        button4.setFocusPainted(false);
        button4.setBounds( 340, 8, 100, 28 );
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button4.isEnabled() == false) return;
                super.mouseClicked(e);
                OperateResultExOne<OmronCpuUnitStatus> read = omron.ReadCpuUnitStatus();
                if (read.IsSuccess) {
                    textArea_data.setText(read.Content.toString());
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button4);

        JButton button5 = new JButton("Cpu Time");
        button5.setFocusPainted(false);
        button5.setBounds( 450, 8, 100, 28 );
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button5.isEnabled() == false) return;
                super.mouseClicked(e);
                OperateResultExOne<Date> read = omron.ReadCpuTime();
                if (read.IsSuccess) {
                    textArea_data.setText(DemoUtils.FormatterDateTime.format(read.Content));
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button5);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                jsp.setBounds(10, 70, getWidth() - 20, getHeight() - 80);

            }
        });
    }

    public void SetOmronFins( IOmronFins omronFins ){
        this.omron = omronFins;
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

    private IOmronFins omron;
}
