package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

import java.math.BigDecimal;

/**
 * Class to reads and converts to {@link BigDecimal}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#doubleValue()} otherwise convert to {@link String} and then {@link BigDecimal}
 *
 * @author Otávio Santana
 */
public final class BigDecimalReader implements ReaderField {


    @Override
    public boolean isCompatible(Class clazz) {
        return BigDecimal.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (BigDecimal.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) BigDecimal.valueOf(Double.valueOf(Number.class.cast(value).doubleValue()));
        } else {
            return (T) BigDecimal.valueOf(Double.valueOf(value.toString()));
        }
    }
}
