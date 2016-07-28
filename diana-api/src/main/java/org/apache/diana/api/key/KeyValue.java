package org.apache.diana.api.key;


import org.apache.diana.api.Value;

import java.io.Serializable;

/**
 * A bucket unit, it's a tuple that contains key its respective value.
 *
 * @param <T> the key type
 * @author Otávio Santana
 */
public interface KeyValue<T> extends Serializable {


    /**
     * Creates a Key value instance
     *
     * @param key   the key
     * @param value the value
     * @param <T>   the key type
     * @return a {@link KeyValue} instance
     * @throws NullPointerException when either key or value are null
     */

    static <T> KeyValue<T> of(T key, Value value) throws NullPointerException {
        return new DefaultKeyValue(key, value);
    }

    /**
     * the key
     *
     * @return the value
     */
    T getKey();

    /**
     * The value
     *
     * @return the value
     * @see Value
     */
    Value getValue();

}
