package org.apache.diana.api.column;


import org.apache.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;

/**
 * Interface used to interact with the persistence context to {@link ColumnFamilyEntity}
 * The ColumnFamilyManager API is used to create and remove persistent {@link ColumnFamilyEntity} instances, to find entities by their primary key, and to query over entities.
 */
public interface ColumnFamilyManager extends AutoCloseable {

    /**
     * Saves a Column family entity
     * @param entity column family to be saved
     * @return the entity saved
     */
    ColumnFamilyEntity save(ColumnFamilyEntity entity);

    /**
     * Saves an entity asynchronously
     * @param entity entity to be saved
     * @throws ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    void saveAsync(ColumnFamilyEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Saves an entity asynchronously
     * @param entity entity to be saved
     * @param callBack the callback, when the process is finished will call this instance returning the saved entity within parameters
     * @throws ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    void saveAsync(ColumnFamilyEntity entity, Consumer<ColumnFamilyEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;
    /**
     * Updates a Column family entity
     * @param entity column family to be saved
     * @return the entity updated
     */
    ColumnFamilyEntity update(ColumnFamilyEntity entity);
    /**
     * Updates an entity asynchronously
     * @param entity entity to be saved
     * @throws ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to update asynchronous
     */
    void updateAsync(ColumnFamilyEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException;
    /**
     * Updates an entity asynchronously
     * @param entity entity to be saved
     * @param callBack the callback, when the process is finished will call this instance returning the updated entity within parameters
     * @throws ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to update asynchronous
     */
    void updateAsync(ColumnFamilyEntity entity, Consumer<ColumnFamilyEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Deletes an entity
     * @param query query to delete an entity
     */
    void delete(ColumnQuery query);

    /**
     * Deletes an entity asynchronously
     * @param query query to delete an entity
     * @throws ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to delete asynchronous
     */
    void deleteAsync(ColumnQuery query) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Deletes an entity asynchronously
     * @param query query to delete an entity
     * @param callBack the callback, when the process is finished will call this instance returning the null within parameters
     * @throws ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to delete asynchronous
     */
    void deleteAsync(ColumnQuery query, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Finds {@link ColumnFamilyEntity} from query
     * @param query - query to figure out entities
     * @return entities found by query
     */
    List<ColumnFamilyEntity> find(ColumnQuery query);

    /**
     * Finds {@link ColumnFamilyEntity} from query asynchronously
     * @param query query to find entities
     * @param callBack the callback, when the process is finished will call this instance returning the result of query within parameters
     * @throws ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    void findAsync(ColumnQuery query, Consumer<List<ColumnFamilyEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Executes a native query from database, this query may be difference between kind of database.
     * @param query query to be executed
     * @return the result of query
     * @throws UnsupportedOperationException when the database does not have support to run native query
     */
    List<ColumnFamilyEntity> nativeQuery(String query) throws UnsupportedOperationException;

    /**
     * Executes a native query from database, this query may be difference between kind of database and run it asynchronously.
     * @param query query to be executed
     * @param callBack the callback, when the process is finished will call this instance returning the result of query within parameters
     * @throws ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to run native query async.
     */
    void nativeQueryAsync(String query, Consumer<List<ColumnFamilyEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Creates a {@link PreparedStatement} from a native query
     * @param query a query to be executed
     * @return a {@link PreparedStatement}
     * @throws UnsupportedOperationException the there is not support to query
     */
    PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException;

}
