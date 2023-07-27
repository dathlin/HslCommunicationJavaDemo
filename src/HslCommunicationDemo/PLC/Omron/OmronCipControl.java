package HslCommunicationDemo.PLC.Omron;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Core.Types.OperateResultExTwo;
import HslCommunication.Profinet.AllenBradley.IReadWriteCip;
import HslCommunicationDemo.DemoUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.util.Date;

public class OmronCipControl extends JPanel {
    public OmronCipControl(){
        setLayout(null);


        AddTimeRead(this);
        AddTypeReadWrite( this );
    }

    public void AddTimeRead( JPanel panel ){
        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);
        panelRead.setBounds(5, 5, 500, 240);
        panelRead.setBorder(BorderFactory.createTitledBorder("Date Time Read Write"));

        JLabel label1 = new JLabel("Address:");
        label1.setBounds(10, 30, 54, 17);
        panelRead.add(label1);

        JTextField textField_address = new JTextField();
        textField_address.setBounds(64, 27, 200, 23);
        textField_address.setText("A1");
        panelRead.add(textField_address);

        JLabel labe2 = new JLabel("Result:");
        labe2.setBounds(10, 55, 54, 17);
        panelRead.add(labe2);

        textAreaResult = new JTextArea();
        textAreaResult.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textAreaResult);
        jsp.setBounds(64,53,200, 100);
        panelRead.add(jsp);

        JButton button_read_date = new JButton("r-Date");
        button_read_date.setBounds( 280, 25, 100, 28 );
        button_read_date.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (readWriteCip == null) return;
                if (!button_read_date.isEnabled()) return;

                OperateResultExOne<Date> read = readWriteCip.ReadDate(textField_address.getText());
                if (read.IsSuccess){
                    textAreaResult.setText(DemoUtils.FormatterDateTime.format(read.Content));
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
        add(button_read_date);

        JButton button_write_date = new JButton("w-Date");
        button_write_date.setBounds( 390, 25, 100, 28 );
        button_write_date.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (readWriteCip == null) return;
                if (!button_write_date.isEnabled()) return;

                Date date = null;
                try {
                    date= DemoUtils.FormatterDate.parse(textAreaResult.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Date Parse Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                OperateResult write = readWriteCip.WriteDate(textField_address.getText(), date);
                if (write.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + write.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button_write_date);


        JButton button_write_dateAndTime = new JButton("w-DateAndTime");
        button_write_dateAndTime.setBounds( 280, 60, 210, 28 );
        button_write_dateAndTime.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (readWriteCip == null) return;
                if (!button_write_dateAndTime.isEnabled()) return;

                Date date = null;
                try {
                    date= DemoUtils.FormatterDateTime.parse(textAreaResult.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Date Parse Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                OperateResult write = readWriteCip.WriteTimeAndDate(textField_address.getText(), date);
                if (write.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + write.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button_write_dateAndTime);


        JButton button_read_time = new JButton("r-Time");
        button_read_time.setBounds( 280, 95, 100, 28 );
        button_read_time.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (readWriteCip == null) return;
                if (!button_read_time.isEnabled()) return;

                OperateResultExOne<Duration> read = readWriteCip.ReadTime(textField_address.getText());
                if (read.IsSuccess){
                    textAreaResult.setText(read.Content.toString());
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
        add(button_read_time);

        JButton button_write_time = new JButton("w-Time");
        button_write_time.setBounds( 390, 95, 100, 28 );
        button_write_time.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (readWriteCip == null) return;
                if (!button_write_time.isEnabled()) return;

                Duration time = null;
                try {
                    time= Duration.parse(textAreaResult.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Date Parse Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                OperateResult write = readWriteCip.WriteTime(textField_address.getText(), time);
                if (write.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + write.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button_write_time);


        JButton button_write_dateOfTime = new JButton("w-DateOfTime");
        button_write_dateOfTime.setBounds( 280, 128, 210, 28 );
        button_write_dateOfTime.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (readWriteCip == null) return;
                if (!button_write_dateOfTime.isEnabled()) return;

                Duration time = null;
                try {
                    time= Duration.parse(textAreaResult.getText());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(
                            null,
                            "Date Parse Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                OperateResult write = readWriteCip.WriteTimeOfDate(textField_address.getText(), time);
                if (write.IsSuccess){
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Success",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + write.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(button_write_dateOfTime);

        panel.add(panelRead);




        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panelRead.setBounds(5, 5, 500, panel.getHeight() - 10);
                jsp.setBounds(64,53,200, panelRead.getHeight() - 60);
                jsp.updateUI();
            }
        });
    }


    public void AddTypeReadWrite(JPanel panel) {
        JPanel panelRead = new JPanel();
        panelRead.setLayout(null);
        panelRead.setBounds(510, 5, 452, 147);
        panelRead.setBorder(BorderFactory.createTitledBorder("Read Write Type and byte[]"));

        JLabel label1 = new JLabel("Address:");
        label1.setBounds(10, 30, 54, 17);
        panelRead.add(label1);

        JTextField textField_address = new JTextField();
        textField_address.setBounds(64, 27, 239, 23);
        textField_address.setText("A1");
        panelRead.add(textField_address);

        JLabel label2 = new JLabel("Type:");
        label2.setBounds(309, 30, 44, 17);
        panelRead.add(label2);

        JTextField textField_type = new JTextField();
        textField_type.setText("C1");
        textField_type.setBounds(363, 27, 80, 23);
        panelRead.add(textField_type);

        JLabel label3 = new JLabel("Data:");
        label3.setBounds(10, 59, 44, 17);
        panelRead.add(label3);

        JTextArea textArea_data = new JTextArea();
        textArea_data.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea_data);
        jsp.setBounds(64, 56, 378, 52);
        panelRead.add(jsp);

        JLabel label4 = new JLabel("Len:");
        label4.setBounds(10, 119, 44, 17);
        panelRead.add(label4);

        JTextField textField_length = new JTextField();
        textField_length.setText("1");
        textField_length.setBounds(64, 116, 87, 23);
        panelRead.add(textField_length);

        JButton button_write = new JButton("Write");
        button_write.setBounds(157, 114, 86, 28);
        button_write.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!button_write.isEnabled()) return;
                try
                {
                    OperateResult write = readWriteCip.WriteTag(
                            textField_address.getText(),
                            Short.parseShort( textField_type.getText(), 16 ),
                            SoftBasic.HexStringToBytes(textArea_data.getText() ),
                            Integer.parseInt( textField_length.getText() ) );
                    DemoUtils.WriteResultRender( write, textField_address.getText() );
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(
                            null,
                            "Write Failed:" + ex.getMessage(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelRead.add(button_write);

        JButton button_read = new JButton("Read");
        button_read.setBounds(247, 114, 92, 28);
        button_read.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!button_read.isEnabled()) return;
                OperateResultExTwo<Short, byte[]> read = readWriteCip.ReadTag(textField_address.getText(), Integer.parseInt(textField_length.getText()));
                if (!read.IsSuccess) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Read Failed:" + read.ToMessageShowString(),
                            "Result",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    textField_type.setText(String.format("%X", read.Content1));
                    textArea_data.setText(SoftBasic.ByteToHexString(read.Content2, ' '));
                }
            }
        });
        panelRead.add(button_read);

        JButton button_read_type = new JButton("ReadType");
        button_read_type.setBounds(343, 114, 99, 28);
        button_read_type.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(!button_read_type.isEnabled()) return;
                OperateResultExOne<String> read = readWriteCip.ReadPlcType( );
                if (read.IsSuccess)
                {
                    textArea_data.setText(read.Content);
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
        });
        panelRead.add(button_read_type);

        panel.add(panelRead);

        panelRead.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                textField_address.setBounds(64, 27, panelRead.getWidth() - 210, 23);
                label2.setBounds(panelRead.getWidth() - 85 - 54, 30, 45, 17);
                textField_type.setBounds(panelRead.getWidth() - 85, 27, 80, 23);

                label4.setBounds(10, panelRead.getHeight() - 28, 44, 17);
                textField_length.setBounds(64, panelRead.getHeight() - 31, 87, 23);
                button_read.setBounds(247, panelRead.getHeight() - 33, 92, 28);
                button_write.setBounds(157, panelRead.getHeight() - 33, 86, 28);
                button_read_type.setBounds(343, panelRead.getHeight() - 33, 99, 28);

                jsp.setBounds(64, 56, panelRead.getWidth() - 69, panelRead.getHeight() - 93);
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panelRead.setBounds(510, 5, panel.getWidth() - 515, panel.getHeight() - 10);

            }
        });
    }



    public void SetReadWriteCip( IReadWriteCip cip ){
        this.readWriteCip = cip;
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
    private IReadWriteCip readWriteCip;
    private JTextArea textAreaResult;
}
