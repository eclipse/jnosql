package org.apache.diana.api.column;


import org.apache.diana.api.Condition;

import static org.apache.diana.api.Condition.*;

/**
 * An unit condition  to run a column family query
 *
 * @author Otávio Santana
 * @see ColumnFamilyManager#find(ColumnQuery)
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
    static ColumnCondition eq(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, EQUALS);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#GREATER_THAN}, it means a query will scanning to a
     * column family that has the same name and the value  greater than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition gt(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, GREATER_THAN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#GREATER_EQUALS_THAN}, it means a query will scanning to a
     * column family that has the same name and the value  greater or equals than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition gte(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, GREATER_EQUALS_THAN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LESSER_THAN}, it means a query will scanning to a
     * column family that has the same name and the value  lesser than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition lt(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, LESSER_THAN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LESSER_EQUALS_THAN}, it means a query will scanning to a
     * column family that has the same name and the value  lesser or equals than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition lte(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, LESSER_EQUALS_THAN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#IN}, it means a query will scanning to a
     * column family that has the same name and the value is within informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#IN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition in(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, IN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LIKE}, it means a query will scanning to a
     * column family that has the same name and the value  is like than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition like(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, LIKE);
    }

}
