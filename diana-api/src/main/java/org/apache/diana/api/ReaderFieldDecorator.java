package org.apache.diana.api;


import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Decorators of all {@link ReaderField} supported by Diana
 *
 * @author Otávio Santana
 * @see ReaderField
 */
final class ReaderFieldDecorator implements ReaderField {

    private static ReaderFieldDecorator INSTANCE = new ReaderFieldDecorator();

    public static ReaderFieldDecorator getInstance() {
        return INSTANCE;
    }

    private final List<ReaderField> readers = new ArrayList<>();

    {
        ServiceLoader.load(ReaderField.class).forEach(readers::add);
    }

    @Override
    public boolean isCompatible(Class clazz) {
        return readers.stream().anyMatch(r -> r.isCompatible(clazz));
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        ReaderField readerField = readers.stream().filter(r -> r.isCompatible(clazz)).findFirst().orElseThrow(
                () -> new UnsupportedOperationException("The type " + clazz + " is not supported yet"));
        return readerField.read(clazz, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReaderFieldDecorator{");
        sb.append("readers=").append(readers);
        sb.append('}');
        return sb.toString();
    }


}
