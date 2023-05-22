package HslCommunicationDemo.PLC.Modbus;

import HslCommunication.BasicFramework.SoftBasic;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.ModBus.IModbus;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModbusSpecialControl extends JPanel {

    public ModbusSpecialControl( IModbus modbus, String defaultAddress)
    {
        this.modbus = modbus;
        this.setLayout(null);
        this.setBounds(546,245,419, 272);
        this.setBorder(BorderFactory.createTitledBorder( "0x17 Function Test"));

        JLabel label1 = new JLabel("Read Address：");
        label1.setBounds(10, 26,93, 17);
        this.add(label1);

        JTextField textBox_readAddress = new JTextField();
        textBox_readAddress.setBounds(108, 23,70, 23);
        textBox_readAddress.setText(defaultAddress);
        this.add(textBox_readAddress);


        JLabel label2 = new JLabel("Read len：");
        label2.setBounds(180, 26,75, 17);
        this.add(label2);

        JTextField textBox_readLength = new JTextField();
        textBox_readLength.setBounds( 250, 23, 50, 23 );
        textBox_readLength.setText("10");
        this.add(textBox_readLength);

        JLabel label3 = new JLabel("Write Address");
        label3.setBounds( 10, 55, 93, 17 );
        this.add(label3);

        JTextField textBox_writeAddress = new JTextField();
        textBox_writeAddress.setBounds(108, 52,70, 23);
        textBox_writeAddress.setText("200");
        this.add(textBox_writeAddress);

        JLabel label4 = new JLabel("HexValue：");
        label4.setBounds(180, 55,75, 17);
        this.add(label4);


        JTextField textBox_writeValue = new JTextField();
        textBox_writeValue.setBounds( 250, 52, 150, 23 );
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
        button.setBounds(305, 23, 100, 23);
        button.setText("Read/Write");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                short readLength = Short.parseShort(textBox_readLength.getText());
                OperateResultExOne<byte[]> read =  modbus.ReadWrite( textBox_readAddress.getText(), readLength,
                        textBox_writeAddress.getText(), SoftBasic.HexStringToBytes(textBox_writeValue.getText() ) );

                if(read.IsSuccess){
                    textArea1.setText(SoftBasic.ByteToHexString(read.Content));
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
    }


    private IModbus modbus;
}
