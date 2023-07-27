package cn.hslcommunication.HslCommunicationDemo.PLC.Siemens;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import cn.hslcommunication.HslCommunicationDemo.DemoUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class SiemensS7Control extends JPanel {
    public SiemensS7Control(){
        setLayout(null);

        JLabel label1 = new JLabel("Address：");
        label1.setBounds(10, 10,100, 17);
        add(label1);

        JTextField textField1 = new JTextField();
        textField1.setBounds(78,7,300, 23);
        textField1.setText("M100");
        add(textField1);

        JLabel label2 = new JLabel("Value：");
        label2.setBounds(10, 40,100, 17);
        add(label2);

        JTextField textField2 = new JTextField();
        textField2.setBounds(78,37,300, 23);
        textField2.setText("");
        add(textField2);


        JButton button1 = new JButton("R-Time");
        button1.setFocusPainted(false);
        button1.setBounds(400,8,120, 28);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button1.isEnabled() == false) return;
                super.mouseClicked(e);
                OperateResultExOne<Date> read = siemensS7Net.ReadDateTime(textField1.getText());
                if(read.IsSuccess){
                    textField2.setText(DemoUtils.FormatterDateTime.format(read.Content));
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
        add(button1);

        JButton button2 = new JButton("W-Time");
        button2.setFocusPainted(false);
        button2.setBounds(530,8,120, 28);
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button2.isEnabled() == false) return;
                super.mouseClicked(e);

                Date date = null;
                try {
                    date=DemoUtils.FormatterDateTime.parse(textField2.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Date Parse Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                OperateResult read = siemensS7Net.Write(textField1.getText(), date);
                if(read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button2);

        JButton button3 = new JButton("r-wstring");
        button3.setFocusPainted(false);
        button3.setBounds(400,42,120, 28);
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button3.isEnabled() == false) return;
                super.mouseClicked(e);
                OperateResultExOne<String> read = siemensS7Net.ReadWString(textField1.getText());
                if(read.IsSuccess){
                    textField2.setText(read.Content);
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
        add(button3);

        JButton button4 = new JButton("w-wstring");
        button4.setFocusPainted(false);
        button4.setBounds(530,42,120, 28);
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button4.isEnabled() == false) return;
                super.mouseClicked(e);
                OperateResult write = siemensS7Net.WriteWString(textField1.getText(), textField2.getText());
                if(write.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + write.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button4);



        JButton button5 = new JButton("R-Date");
        button5.setFocusPainted(false);
        button5.setBounds(400,76,120, 28);
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button5.isEnabled() == false) return;
                super.mouseClicked(e);
                OperateResultExOne<Date> read = siemensS7Net.ReadDate(textField1.getText());
                if(read.IsSuccess){
                    textField2.setText(DemoUtils.FormatterDate.format(read.Content));
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
        add(button5);

        JButton button6 = new JButton("W-Date");
        button6.setFocusPainted(false);
        button6.setBounds(530,76,120, 28);
        button6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button6.isEnabled() == false) return;
                super.mouseClicked(e);

                Date date = null;
                try {
                    date= DemoUtils.FormatterDate.parse(textField2.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Date Parse Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                OperateResult read = siemensS7Net.WriteDate(textField1.getText(), date);
                if(read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button6);


        JButton button7 = new JButton("R-DTL Time");
        button7.setFocusPainted(false);
        button7.setBounds(400,110,120, 28);
        button7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button7.isEnabled() == false) return;
                super.mouseClicked(e);
                OperateResultExOne<Date> read = siemensS7Net.ReadDTLDataTime(textField1.getText());
                if(read.IsSuccess){
                    textField2.setText(DemoUtils.FormatterDateTime.format(read.Content));
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
        add(button7);

        JButton button8 = new JButton("W-DTL Time");
        button8.setFocusPainted(false);
        button8.setBounds(530,110,120, 28);
        button8.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button8.isEnabled() == false) return;
                super.mouseClicked(e);

                Date date = null;
                try {
                    date= DemoUtils.FormatterDateTime.parse(textField2.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Date Parse Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                OperateResult read = siemensS7Net.WriteDTLTime(textField1.getText(), date);
                if(read.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button8);
    }

    public void SetReadWriteS7( SiemensS7Net s7 ){
        this.siemensS7Net = s7;
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

    private SiemensS7Net siemensS7Net;
}
