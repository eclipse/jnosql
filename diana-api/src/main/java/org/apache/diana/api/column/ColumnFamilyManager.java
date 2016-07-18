package org.apache.diana.api.column;


import org.apache.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;

public interface ColumnFamilyManager extends AutoCloseable {

    void save(ColumnFamily columnFamily);

    void saveAsync(ColumnFamily columnFamily) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void saveAsync(ColumnFamily columnFamily, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void update(ColumnFamily columnFamily);

    void updateAsync(ColumnFamily columnFamily) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void updateAsync(ColumnFamily columnFamily, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void delete(ColumnFamily columnFamily);

    void deleteAsync(ColumnFamily columnFamily) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void deleteAsync(ColumnFamily columnFamily, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    List<ColumnFamily> find(String columnFamily, ColumnCondition... conditions);

    void findAsync(ColumnFamily columnFamily, Consumer<List<ColumnFamily>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    List<ColumnFamily> nativeQuery(String query) throws UnsupportedOperationException;

    void nativeQueryAsync(String query, Consumer<List<ColumnFamily>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException;

}
