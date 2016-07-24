package org.apache.diana.api.column;


import org.apache.diana.api.Value;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Column Family entity unit, it is a tuple (pair) that consists of a key-value pair, where the key is mapped to a value.
 *
 * @author Otávio Santana
 */
public interface Column extends Serializable {


    /**
     * Creates a column instance
     *
     * @param name  - column's name
     * @param value - column's value
     * @return a column instance
     * @see Columns
     */
    public static Column of(String name, Value value) {
        return new DefaultColumn(name, value);
    }

    /**
     * The column's name
     *
     * @return name
     */
    String getName();

    /**
     * the column's value
     *
     * @return {@link Value}
     */
    Value getValue();
}
