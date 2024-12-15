package org.eclipse.jnosql.communication.reader;

import org.eclipse.jnosql.communication.ValueReader;

import java.util.UUID;

public class UUIDValueReader  implements ValueReader {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(Class<T> type, Object value) {
        if (value instanceof UUID) {
            return (T) value;
        }
        if (value instanceof CharSequence) {
            return (T) UUID.fromString((String) value);
        }
        return null;
    }

    @Override
    public boolean test(Class<?> type) {
        return UUID.class.equals(type);
    }
}
