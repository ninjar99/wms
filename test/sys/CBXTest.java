package sys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CBXTest extends JFrame implements KeyListener {
    private JComboBox cbx;
    private JTextField jtf;
    @SuppressWarnings("unchecked")
    public CBXTest() {
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(null);
         
        cbx = new JComboBox(getItems());
        cbx.setEditable(true);
        cbx.setBounds(20, 20, 80, 20);
        jtf = (JTextField)cbx.getEditor().getEditorComponent();
        jtf.addKeyListener(this);
        c.add(cbx);

        setVisible(true);
    }

    public void keyPressed(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    @SuppressWarnings("unchecked")
    public void keyReleased(KeyEvent e) {
        Object obj = e.getSource();
        if (obj == jtf) {
            String key = jtf.getText();
            cbx.removeAllItems();
            for (Object item : getItems()) {
                if (((String)item).indexOf(key)>=0) { //这里是包含key的项目都筛选出来，
                	//可以把contains改成startsWith就是筛选以key开头的项目
                    cbx.addItem(item);
                }
            }
            jtf.setText(key);
        }
    }
    
    @SuppressWarnings("unchecked")
    public Object[] getItems() {
        return new Object[] {
            "abcd", "abcde", "abcdefg", "acef" 
        };
    }

    public static void main(String[] args) {
        new CBXTest();
    }
}
