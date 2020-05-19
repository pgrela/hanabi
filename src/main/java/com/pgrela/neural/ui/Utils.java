package com.pgrela.neural.ui;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

public class Utils {
    public static void randomLookAndFeel() {
        try {
            LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
            UIManager.setLookAndFeel(installedLookAndFeels[Math.abs(Arrays.hashCode(installedLookAndFeels)) % installedLookAndFeels.length].getClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void dirDropdown(JComboBox dropdown, Path directory, String defaultFile){
        File[] files = new File(directory.toString()).listFiles(((dir, name) -> name.endsWith(".nn")));
        dropdown.removeAllItems();
        Arrays.stream(files).map(File::getName).forEach(dropdown::addItem);
        dropdown.setSelectedItem(defaultFile);
    }
}
