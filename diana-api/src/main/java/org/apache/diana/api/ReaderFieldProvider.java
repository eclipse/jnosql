package org.apache.diana.api;


import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

final class ReaderFieldProvider {

    private static ReaderFieldProvider INSTANCE = new ReaderFieldProvider();

    public static ReaderFieldProvider getInstance() {
        return INSTANCE;
    }

    private final List<ReaderField> readers = new ArrayList<>();

    {
        ServiceLoader.load(ReaderField.class).forEach(readers::add);
    }


    <T> T convert(Class<T> clazz, Object value) {
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        ReaderField readerField = readers.stream().filter(r -> r.isCompatible(clazz)).findFirst().orElseThrow(
                () -> new UnsupportedOperationException("The type " + clazz + " is not supported yet"));
        return readerField.read(clazz, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReaderFieldProvider{");
        sb.append("readers=").append(readers);
        sb.append('}');
        return sb.toString();
    }
}
