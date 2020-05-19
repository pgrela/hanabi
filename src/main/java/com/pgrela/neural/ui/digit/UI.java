package com.pgrela.neural.ui.digit;

import com.pgrela.neural.ui.Utils;

import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {
    public UI() throws HeadlessException {
        super("Hello Digit!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Utils.randomLookAndFeel();
        setContentPane(new NNDemo().getRootPanel());
        pack();
        setSize(900, 470);
        setLocation(400, 300);
        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(UI::new);
    }
}
