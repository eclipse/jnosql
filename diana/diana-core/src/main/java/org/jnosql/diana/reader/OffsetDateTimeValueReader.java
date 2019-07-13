package org.jnosql.diana.reader;

import jakarta.nosql.ValueReader;

import java.time.OffsetDateTime;

public class OffsetDateTimeValueReader implements ValueReader {

    @Override
    public <T> boolean isCompatible(Class<T> aClass) {
        return OffsetDateTime.class.equals(aClass);
    }

    @Override
    public <T> T read(Class<T> aClass, Object o) {
        return null;
    }
}
