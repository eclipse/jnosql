package org.apache.diana.api.document;


import org.apache.diana.api.Value;

import java.io.Serializable;

/**
 * A Document Collection Entity unit, it is a tuple (pair) that consists of a key-value pair, where the key is mapped to a value.
 *
 * @author Otávio Santana
 */
public interface Document extends Serializable {

    /**
     * Creates a Document instance
     *
     * @param name  - column's name
     * @param value - column's value
     * @return a Document instance
     * @see Documents
     */
    static Document of(String name, Value value) throws NullPointerException {
        return new DefaultDocument(name, value);
    }

    /**
     * Creates a Document instance
     * @param name - column's name
     * @param value - column's value
     * @return a Document instance
     * @throws NullPointerException
     */
    static Document of(String name, Object value) throws NullPointerException {
        return new DefaultDocument(name, Value.of(value));
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
