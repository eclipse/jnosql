package org.apache.diana.api.column;


import org.apache.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;

public interface ColumnFamilyManager extends AutoCloseable {

    void save(ColumnFamily entity);

    void saveAsync(ColumnFamily entity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void saveAsync(ColumnFamily entity, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void update(ColumnFamily entity);

    void updateAsync(ColumnFamily entity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void updateAsync(ColumnFamily entity, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void delete(ColumnFamily entity);

    void deleteAsync(ColumnFamily entity)throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void deleteAsync(ColumnFamily entity, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    List<ColumnFamily> find(ColumnFamily entity);

    void findAsync(ColumnFamily entity, Consumer<List<ColumnFamily>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    List<ColumnFamily> nativeQuery(String query)throws UnsupportedOperationException;

    void nativeQueryAsync(String query, Consumer<List<ColumnFamily>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException;

}
