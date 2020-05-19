package com.pgrela.neural.utils;

import com.pgrela.neural.core.ActivationFunction;

public class Activation {
    public static final ActivationFunction SIGMA_1_1 = new Sigma1_1();

    private static class Sigma1_1 implements ActivationFunction {

        public double function(double x) {
            return Math.min(Math.max(-1d, x), 1d);
        }

        public double derivative(double inputSumPlusB) {
            if (inputSumPlusB < -1 || inputSumPlusB > 1) return 0;
            return 1;
        }
    }

    public static final ActivationFunction SIGMA_0_1 = new Sigma0_1();

    private static class Sigma0_1 implements ActivationFunction {

        public double function(double x) {
            return Math.min(Math.max(0, x), 1d);
        }

        public double derivative(double inputSumPlusB) {
            if (inputSumPlusB < 0 || inputSumPlusB > 1) return 1;
            return 1;
        }

        public float function(float x) {
            return Math.min(Math.max(0, x), 1);
        }

        public float derivative(float inputSumPlusB) {
            if (inputSumPlusB < 0 || inputSumPlusB > 1) return 1;
            return 1;
        }
    }

    public static final ActivationFunction SIGMA_1_0_1 = new Sigma1_0_1();

    private static class Sigma1_0_1 implements ActivationFunction {

        public double function(double x) {
            if (x >= 0) {
                return 1 - 1 / (x + 1);
            }
            return -1 / (x - 1) - 1;
        }

        public double derivative(double x) {
            if (x >= 0)
                return 1 / (x + 1) / (x + 1);
            return 1 / (x - 1) / (x - 1);
        }

        public float function(float x) {
            if (x >= 0) {
                return 1 - 1 / (x + 1);
            }
            return -1 / (x - 1) - 1;
        }

        public float derivative(float x) {
            if (x >= 0)
                return 1 / (x + 1) / (x + 1);
            return 1 / (x - 1) / (x - 1);
        }
    }
}
