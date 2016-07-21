package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

import static java.lang.Character.MIN_VALUE;

/**
 * Class reader for {@link Character}
 *
 * @author Otávio Santana
 */
public final class CharacterReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Character.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {
        if (Character.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) Character.valueOf((char) Number.class.cast(value).intValue());
        }

        if (value.toString().isEmpty()) {
            return (T) Character.valueOf(MIN_VALUE);
        }
        return (T) Character.valueOf(value.toString().charAt(0));
    }


}
