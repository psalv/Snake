package Tests;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class KeyBindingsTester extends JFrame{

    private static final String HIT_ME = "hit me";

    public KeyBindingsTester(){
        super("Key Bindings");
        JPanel p = new JPanel();

        p.getInputMap().put(KeyStroke.getKeyStroke("UP"), HIT_ME);
        p.getActionMap().put(HIT_ME, new PrintAction());

        add(p);
        remove(p);
        setSize(200, 100);
        setVisible(true);
    }

    private class PrintAction extends AbstractAction{

        public void actionPerformed(ActionEvent e){
            System.out.println("i printed mf");
        }
    }

    public static void main(String[] args) {
        KeyBindingsTester x = new KeyBindingsTester();
    }
}
