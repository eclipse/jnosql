package org.apache.diana.api.column;


import org.apache.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;

/**
 * Interface used to interact with the persistence context to {@link ColumnFamily}
 * The ColumnFamilyManager API is used to create and remove persistent {@link ColumnFamily} instances, to find entities by their primary key, and to query over entities.
 */
public interface ColumnFamilyManager extends AutoCloseable {

    /**
     * Save a Column family
     * @param columnFamily column family to be saved
     */
    void save(ColumnFamily columnFamily);

    void saveAsync(ColumnFamily columnFamily) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void saveAsync(ColumnFamily columnFamily, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void update(ColumnFamily columnFamily);

    void updateAsync(ColumnFamily columnFamily) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void updateAsync(ColumnFamily columnFamily, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void delete(ColumnQuery query);

    void deleteAsync(ColumnQuery query) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void deleteAsync(ColumnQuery query, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    List<ColumnFamily> find(ColumnQuery query);

    void findAsync(ColumnQuery query, Consumer<List<ColumnFamily>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    List<ColumnFamily> nativeQuery(String query) throws UnsupportedOperationException;

    void nativeQueryAsync(String query, Consumer<List<ColumnFamily>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException;

}
