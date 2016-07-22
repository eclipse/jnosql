package org.apache.diana.api.writer;


import org.apache.diana.api.WriterField;

import java.util.Optional;

public final class OptionalWriter<T> implements WriterField<Optional<T>, T> {

    @Override
    public boolean isCompatible(Class clazz) {
        return Optional.class.equals(clazz);
    }


    @Override
    public T write(Optional<T> optional) {
        return optional.get();
    }
}
