package org.apache.diana.api.document;


import org.apache.diana.api.Condition;
import org.apache.diana.api.column.Column;
import org.apache.diana.api.column.ColumnFamilyManager;

import static org.apache.diana.api.Condition.*;

/**
 * An unit condition  to run a column family query
 *
 * @author Otávio Santana
 * @see ColumnFamilyManager#find(DocumentQuery)
 */
public interface DocumentCondition {

    /**
     * Gets the column to be used in the query
     *
     * @return a column instance
     */
    Column getColumn();

    /**
     * Gets the conditions to be used in the query
     *
     * @return a Condition instance
     * @see Condition
     */
    Condition getCondition();

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#EQUALS}, it means a query will scanning to a
     * column family that has the same name and equals value informed in this column.
     *
     * @param column a column instance
     * @return a {@link DocumentCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition eq(Column column) throws NullPointerException {
        return DefaultDocumentCondition.of(column, EQUALS);
    }

    static DocumentCondition gt(Column column) throws NullPointerException {
        return DefaultDocumentCondition.of(column, GREATER_THAN);
    }

    static DocumentCondition gte(Column column) throws NullPointerException {
        return DefaultDocumentCondition.of(column, GREATER_EQUALS_THAN);
    }

    static DocumentCondition lt(Column column) throws NullPointerException {
        return DefaultDocumentCondition.of(column, LESSER_THAN);
    }

    static DocumentCondition lte(Column column) throws NullPointerException {
        return DefaultDocumentCondition.of(column, LESSER_EQUALS_THAN);
    }

    static DocumentCondition in(Column column) throws NullPointerException {
        return DefaultDocumentCondition.of(column, IN);
    }

    static DocumentCondition like(Column column) throws NullPointerException {
        return DefaultDocumentCondition.of(column, LIKE);
    }

}
