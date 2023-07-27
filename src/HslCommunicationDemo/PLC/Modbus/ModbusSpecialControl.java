package HslCommunicationDemo.PLC.Modbus;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.ModBus.IModbus;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModbusSpecialControl extends JPanel {

    public ModbusSpecialControl( )
    {
        this.setLayout(null);

        JLabel label1 = new JLabel("Read Address：");
        label1.setBounds(10, 26,93, 17);
        this.add(label1);

        textBox_readAddress = new JTextField();
        textBox_readAddress.setBounds(108, 23,150, 23);
        textBox_readAddress.setText("");
        this.add(textBox_readAddress);

        JLabel label2 = new JLabel("Read len：");
        label2.setBounds(280, 26,75, 17);
        this.add(label2);

        JTextField textBox_readLength = new JTextField();
        textBox_readLength.setBounds( 350, 23, 50, 23 );
        textBox_readLength.setText("10");
        this.add(textBox_readLength);

        JLabel label3 = new JLabel("Write Address");
        label3.setBounds( 10, 55, 93, 17 );
        this.add(label3);

        JTextField textBox_writeAddress = new JTextField();
        textBox_writeAddress.setBounds(108, 52,150, 23);
        textBox_writeAddress.setText("200");
        this.add(textBox_writeAddress);

        JLabel label4 = new JLabel("HexValue：");
        label4.setBounds(280, 55,75, 17);
        this.add(label4);


        JTextField textBox_writeValue = new JTextField();
        textBox_writeValue.setBounds( 350, 52, 350, 23 );
        textBox_writeValue.setText("11 22 33 44");
        this.add(textBox_writeValue);

        JLabel label5 = new JLabel("Result:");
        label5.setBounds( 10, 83, 93, 17 );
        this.add(label5);

        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(textArea1);
        jsp.setBounds(108,80,292, 175);
        this.add(jsp);


        JButton button = new JButton();
        button.setBounds(415, 23, 120, 23);
        button.setText("Read/Write");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                short readLength = Short.parseShort(textBox_readLength.getText());
                OperateResultExOne<byte[]> read =  modbus.ReadWrite( textBox_readAddress.getText(), readLength,
                        textBox_writeAddress.getText(), SoftBasic.HexStringToBytes(textBox_writeValue.getText() ) );

                if(read.IsSuccess){
                    textArea1.setText(SoftBasic.ByteToHexString(read.Content, ' '));
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
        this.add(button);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                textBox_writeValue.setBounds( 350, 52, getWidth() - 360, 23 );
                jsp.setBounds(108,80,getWidth() - 118, getHeight() - 85);
                jsp.updateUI();
            }
        });
    }

    public void SetReadWriteModbus( IModbus modbus, String defaultAddress ){
        this.modbus = modbus;
        textBox_readAddress.setText(defaultAddress);
    }

    private IModbus modbus;
    private JTextField textBox_readAddress;
}
