package com.pgrela.neural.ui.digit;

import com.pgrela.neural.fast.Main;
import com.pgrela.neural.fast.Network;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class NNDemo {
    private JPanel rootPanel;
    private JLabel label;
    private JPanel whiteboard;
    private JButton clearButton;
    private JComboBox<String> fileSelect;
    private JTable outputTable;
    private JSpinner trainingSampleNo;
    private JButton testingButton;
    private JButton learningButton;
    private JSlider brushSize;
    private JLabel digit;
    private JLabel expectedDigit;
    private JSlider brushSharpness;

    private Whiteboard whiteboardRef;
    private Network network;
    private CompletableFuture<double[]> completableFuture = CompletableFuture.completedFuture(null);

    public NNDemo() {
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                whiteboardRef.clear();
            }
        });
        fileSelect.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                loadNetwork(e.getItem().toString());
            }
        });
        whiteboard.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("grid")) {
                    updateScores();
                }
            }
        });
        learningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int expected = whiteboardRef.loadLearningSample(Integer.valueOf(trainingSampleNo.getValue().toString()));
                expectedDigit.setText(String.valueOf(expected));
            }
        });
        testingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int expected = whiteboardRef.loadTestingSample(Integer.valueOf(trainingSampleNo.getValue().toString()));
                expectedDigit.setText(String.valueOf(expected));
            }
        });
        brushSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                whiteboardRef.setBrushSize(brushSize.getValue());
            }
        });
        trainingSampleNo.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int expected = whiteboardRef.loadTestingSample(Integer.valueOf(trainingSampleNo.getValue().toString()));
                expectedDigit.setText(String.valueOf(expected));
            }
        });
        brushSharpness.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                whiteboardRef.setBrushSharpness(brushSharpness.getValue());
            }
        });
    }

    private void updateScores() {
        if (!completableFuture.isDone()) return;
        int[][] grid = whiteboardRef.getGrid();
        double[] input = new double[28 * 28];
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                input[i * 28 + j] = 1 - grid[i][j] / 255d;
            }
        }
        completableFuture = CompletableFuture.supplyAsync(() -> network.process(input));
        completableFuture.thenAccept(this::updateOutputTable);
    }

    private void updateOutputTable(double[] doubles) {
        for (int i = 0; i < doubles.length; i++) {
            outputTable.getModel().setValueAt(String.format("%.0f%%", 100 * doubles[i]), i, 1);
        }
        double max = Arrays.stream(doubles).max().orElseThrow();
        if (max > .5) {
            if (Arrays.stream(doubles).filter(i -> i > max * .9).count() == 1) {
                int d = IntStream.range(0, 10).reduce(0, (a, b) -> doubles[a] > doubles[b] ? a : b);
                digit.setText(String.valueOf(d));
                expectedDigit.setForeground(digit.getText().equals(expectedDigit.getText()) ? Color.GREEN : Color.RED);
                return;
            }
        }
        expectedDigit.setForeground(Color.WHITE);
        digit.setText("?");
    }

    private void loadNetwork(String filename) {
        network = Network.fromFile(new File(Main.SAVE_DIR.toFile(), filename));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        whiteboard = whiteboardRef = new Whiteboard();
        fillDropdown();
        outputTable = new JTable(new DefaultTableModel(IntStream.range(0, 10)
                .mapToObj(i -> new Object[]{i, 0d})
                .toArray(Object[][]::new), new String[]{"digit", "score"}));
    }

    private void fillDropdown() {
        File[] files = new File(Main.SAVE_DIR.toString()).listFiles(((dir, name) -> name.endsWith(".nn")));
        fileSelect = new JComboBox(Arrays.stream(files).map(File::getName).toArray(String[]::new));
        //loadNetwork(files[0].getName());
        loadNetwork("autosave_ls60000_e580_t95.nn");
        fileSelect.setSelectedItem("autosave_ls60000_e580_t95.nn");
    }
}
