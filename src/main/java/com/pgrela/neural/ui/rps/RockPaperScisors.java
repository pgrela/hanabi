package com.pgrela.neural.ui.rps;

import com.pgrela.neural.ui.Utils;

import javax.swing.*;
import java.awt.*;

public class RockPaperScisors extends JFrame {
    public RockPaperScisors() throws HeadlessException {
        super("Hello Digit!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Utils.randomLookAndFeel();
        setContentPane(new RockPaperScissorsUI().getRootPanel());
        pack();
        //setSize(900, 470);
        setLocation(400, 300);
        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(RockPaperScisors::new);
    }
}
