package org.eclipse.jnosql.communication;

/**
 * An enum implementing the {@link Value} interface to represent a null value.
 * This enum provides a standardized way to handle null values within the context of {@link Value}.
 * It is often used in scenarios where the absence of a meaningful value needs to be explicitly represented.
 * The methods in this enum always return null or false, depending on the context.
 */
enum NullValue implements Value {
    INSTANCE;

    @Override
    public Object get() {
        return null;
    }

    @Override
    public <T> T get(Class<T> type) {
        return null;
    }

    @Override
    public <T> T get(TypeSupplier<T> supplier) {
        return null;
    }

    @Override
    public boolean isInstanceOf(Class<?> typeClass) {
        return false;
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
