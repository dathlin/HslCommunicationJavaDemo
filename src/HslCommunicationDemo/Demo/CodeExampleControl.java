package HslCommunicationDemo.Demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CodeExampleControl extends JPanel {

    public CodeExampleControl(){
        setLayout(null);

        textAreaLeft = new JTextArea();
        textAreaLeft.setLineWrap(true);
        Font defaultFont = new Font("宋体", Font.PLAIN, 14);
        textAreaLeft.setFont(defaultFont);
        JScrollPane jsp1 = new JScrollPane(textAreaLeft);
        jsp1.setBounds(5,5,500, 300);
        add(jsp1);


        textAreaRight = new JTextArea();
        textAreaRight.setLineWrap(true);
        JScrollPane jsp2 = new JScrollPane(textAreaRight);
        jsp2.setBounds(510,5,400, 300);
        add(jsp2);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                int left = getWidth() * 48 / 100;
                if (left < 100 ) left = 100;
                int right = getWidth() - left;
                if (right < 100 ) right = 100;


                jsp1.setBounds(5,5,left - 10, getHeight() - 8);
                jsp1.updateUI();

                jsp2.setBounds( left,5,right - 5, getHeight() - 8);

            }
        });

    }

    public void SetDeviceCode( String code ){
        textAreaLeft.setText(code);
    }

    public void SetReadWriteCode( String code ){
        textAreaRight.setText(code);
    }

    private JTextArea textAreaLeft = null;
    private JTextArea textAreaRight = null;


    /**
     * 获取标题信息
     * @return 标题信息
     */
    public static String GetTitle( ) {
        return "Code Example";
    }
}
