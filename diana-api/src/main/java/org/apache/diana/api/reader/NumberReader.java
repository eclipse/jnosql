package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;


final class NumberReader implements ReaderField{
    @Override
    public boolean isCompatible(Class clazz) {
        return Number.class.isAssignableFrom(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {
        return null;
    }
}
