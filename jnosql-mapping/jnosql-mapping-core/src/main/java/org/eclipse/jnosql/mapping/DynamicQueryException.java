package org.eclipse.jnosql.mapping;


import jakarta.nosql.NoSQLException;

/**
 * The root exception to dynamic query on {@link jakarta.data.repository.CrudRepository}
 */
public class DynamicQueryException extends NoSQLException {

    /**
     * Constructs a new runtime exception with the specified detail message.
     *
     * @param message the message
     */
    public DynamicQueryException(String message) {
        super(message);
    }
}
