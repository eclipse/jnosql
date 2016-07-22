package org.apache.diana.api.column;


import org.apache.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;


/**
 * Represents a prepared statement, a query with bound variables that has been
 * prepared (pre-parsed) by the database.
 *
 * @author Otávio Santana
 */
public interface PreparedStatement extends AutoCloseable {

    /**
     * Executes the query
     *
     * @return the result
     */
    List<ColumnFamilyEntity> executeQuery();

    /**
     * Executes the query asynchronously
     *
     * @param callBack the callback, when the process is finished will call this instance returning the result of query within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to run async.
     */
    void executeQueryAsync(Consumer<List<ColumnFamilyEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Bind the properties within query
     *
     * @param values the values to be put in the query
     * @return a  {@link PreparedStatement} binded with the parameters to execute a query
     */
    PreparedStatement bind(Object... values);
}
