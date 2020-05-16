package com.pgrela.neural.core.config;

public abstract class ExtendableBuilder<SELF extends ExtendableBuilder<SELF, T>, T> implements Supplier<T> {
    protected SELF myself;

    protected ExtendableBuilder(Class<SELF> myself) {
        this.myself = myself.cast(this);
    }
}
