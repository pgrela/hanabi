package com.pgrela.neural.utils;

public class Utils {
    public static int maxIndex(double[] list) {
        int guess = -1;
        double max = -10;
        for (int j = 0; j < list.length; j++) {
            if (list[j] > max) {
                guess = j;
                max = list[j];
            }
        }
        return guess;
    }
}
