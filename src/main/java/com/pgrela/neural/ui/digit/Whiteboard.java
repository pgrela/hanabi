package com.pgrela.neural.ui.digit;

import com.pgrela.neural.core.Input;
import com.pgrela.neural.mnist.MnistData;
import com.pgrela.neural.training.TrainingSample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Whiteboard extends JPanel {

    public static final int SIZE = 28;
    private int[] horizontalLines = new int[SIZE + 1];
    private int[] verticalLines = new int[SIZE + 1];
    private int[][] grid = new int[SIZE][SIZE];
    private int brushSize = 10;
    private int brushSharpness = 0;

    public void clear() {
        for (int i = 0; i < grid.length; i++) {
            Arrays.fill(grid[i], 255);
        }
        gridUpdated();
    }

    public int[][] getGrid() {
        return grid;
    }

    public int loadLearningSample(Integer n) {
        TrainingSample trainingSample = new MnistData().learningSet(n).getSamples().get(n - 1);
        loadSample(trainingSample.getInput());
        return IntStream.range(0, 10).reduce(0, (a, b) -> trainingSample.getOutput().get(a) > trainingSample.getOutput().get(b) ? a : b);
    }

    public int loadTestingSample(Integer n) {
        TrainingSample trainingSample = new MnistData().testingSet(n).getSamples().get(n - 1);
        loadSample(trainingSample.getInput());
        return IntStream.range(0, 10).reduce(0, (a, b) -> trainingSample.getOutput().get(a) > trainingSample.getOutput().get(b) ? a : b);
    }

    private void loadSample(Input input) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = 255 - (int) (input.get(i * SIZE + j) * 255);
            }
        }
        gridUpdated();
    }

    public Whiteboard() {
        super();
        clear();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                spray(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                spray(e.getPoint());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resetSize(e.getComponent().getSize());
            }
        });
    }

    private void spray(Point point) {
        int firstYSector = 0;
        int firstXSector = 0;
        int brushTop = point.y - brushSize;
        int brushLeft = point.x - brushSize;
        int brushBottom = point.y + brushSize;
        int brushRight = point.x + brushSize;
        if (brushBottom < 0 || brushRight < 0) return;

        while (horizontalLines[firstYSector + 1] < brushTop) {
            ++firstYSector;
            if (firstYSector == SIZE) return;
        }
        while (verticalLines[firstXSector + 1] < brushLeft) {
            ++firstXSector;
            if (firstXSector == SIZE) return;
        }
        //spray(point, firstXSector, firstYSector);

        for (int i = firstYSector; i < SIZE; i++) {
            if (brushBottom < horizontalLines[i]) break;
            for (int j = firstXSector; j < SIZE; j++) {
                if (brushRight < verticalLines[j]) break;
                spray(point, j, i);
            }
        }

        gridUpdated();
    }

    private void gridUpdated() {
        firePropertyChange("grid", 0, 1);
        repaint();
    }

    private void spray(Point point, int sectorX, int sectorY) {
        //grid[sectorY][sectorX]=100;if(true)return;
        int centerX = (verticalLines[sectorX] + verticalLines[sectorX + 1]) / 2;
        int centerY = (horizontalLines[sectorY] + horizontalLines[sectorY + 1]) / 2;
        int d = Math.max(Math.abs(point.x - centerX), Math.abs(point.y - centerY));
        if (d > brushSize) return;
        d=Math.max(d-brushSharpness, 0);
        grid[sectorY][sectorX] = Math.min(d * 255 / brushSize, grid[sectorY][sectorX]);
    }


    private void resetSize(Dimension size) {
        int heightSpace = size.height;
        int widthSpace = size.width;
        horizontalLines = chop(heightSpace, SIZE);
        verticalLines = chop(widthSpace, SIZE);
    }

    private int[] chop(int space, int parts) {
        int[] ints = new int[parts + 1];
        ints[0] = 0;
        ints[parts] = space - 1;
        int spaceLeft = space - (parts + 1);
        for (int i = 1; i < parts; i++) {
            int chunk = spaceLeft / (parts - i + 1);
            ints[i] = ints[i - 1] + chunk + 1;
            spaceLeft -= chunk;
        }
        return ints;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        for (int verticalLine : verticalLines) {
            g.drawLine(verticalLine, 0, verticalLine, getHeight());
        }
        for (int horizontalLine : horizontalLines) {
            g.drawLine(0, horizontalLine, getWidth(), horizontalLine);
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

                g.setColor(new Color(grid[i][j], grid[i][j], grid[i][j]));

                g.fillRect(verticalLines[j] + 1,
                        horizontalLines[i] + 1,
                        verticalLines[j + 1] - verticalLines[j] - 1,
                        horizontalLines[i + 1] - horizontalLines[i] - 1);
            }
        }

    }

    public void setBrushSize(int value) {
        brushSize = value;
    }

    public void setBrushSharpness(int value) {
        brushSharpness = value;
    }
}
