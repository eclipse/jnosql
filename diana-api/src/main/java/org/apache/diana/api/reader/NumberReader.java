package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

/**
 * Class reader for {@link Number}, this converter first verify if the object is a Number instance,
 * if it will return itself, otherwise convert to String and then to {@link Double}
 *
 * @author Otávio Santana
 */
final class NumberReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Number.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {
        if (Number.class.isInstance(value)) {
            return (T) value;
        } else {
            return (T) Double.valueOf(value.toString());
        }
    }


}
