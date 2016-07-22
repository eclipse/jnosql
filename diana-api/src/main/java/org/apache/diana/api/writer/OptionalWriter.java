package org.apache.diana.api.writer;


import org.apache.diana.api.WriterField;

import java.util.Optional;

public final class OptionalWriter implements WriterField<Optional> {
    @Override
    public boolean isCompatible(Class clazz) {
        return Optional.class.equals(clazz);
    }

    @Override
    public Object write(Optional object) {
        return object.get();
    }
}
