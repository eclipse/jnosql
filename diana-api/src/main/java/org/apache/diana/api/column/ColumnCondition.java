package org.apache.diana.api.column;


import org.apache.diana.api.Condition;

import static org.apache.diana.api.Condition.*;

/**
 * An unit condition  to run a column family query
 *
 * @author Otávio Santana
 * @see ColumnFamilyManager#find(String, ColumnCondition...)
 */
public interface ColumnCondition {

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
     * Creates a {@link ColumnCondition} that has a {@link Condition#EQUALS}, it means a query will scanning to a
     * column family that has the same name and equals value informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition eq(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, EQUALS);
    }

    public static ColumnCondition gt(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, GREATER_THAN);
    }

    public static ColumnCondition gte(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, GREATER_EQUALS_THAN);
    }

    public static ColumnCondition lt(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, LESSER_THAN);
    }

    public static ColumnCondition lte(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, LESSER_EQUALS_THAN);
    }

    public static ColumnCondition in(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, IN);
    }

}
