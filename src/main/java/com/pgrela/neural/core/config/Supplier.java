package com.pgrela.neural.core.config;

@FunctionalInterface
public interface Supplier<T> {
    T build();
}
