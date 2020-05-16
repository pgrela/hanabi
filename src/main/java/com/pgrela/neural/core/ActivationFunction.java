package com.pgrela.neural.core;

public interface ActivationFunction {
    double function(double x);

    double derivative(double x);

    default float function(float x) {
        return 0;
    }

    default float derivative(float x) {
        return 0;
    }
}
