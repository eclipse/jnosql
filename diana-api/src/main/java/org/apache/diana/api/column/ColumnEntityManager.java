package org.apache.diana.api.column;


import org.apache.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;

public interface ColumnEntityManager extends AutoCloseable {

    void save(ColumnEntity entity);

    void saveAsync(ColumnEntity entity) throws ExecuteAsyncQueryException;

    void saveAsync(ColumnEntity entity, Consumer<Void> callBack) throws ExecuteAsyncQueryException;

    void update(ColumnEntity entity);

    void updateAsync(ColumnEntity entity) throws ExecuteAsyncQueryException;

    void updateAsync(ColumnEntity entity, Consumer<Void> callBack) throws ExecuteAsyncQueryException;

    void delete(ColumnEntity entity);

    void deleteAsync(ColumnEntity entity);

    void deleteAsync(ColumnEntity entity, Consumer<Void> callBack) throws ExecuteAsyncQueryException;

    List<ColumnEntity> find(ColumnEntity entity);

    void findAsync(ColumnEntity entity, Consumer<List<ColumnEntity>> callBack) throws ExecuteAsyncQueryException;

    List<ColumnEntity> nativeQuery(String query);

    void nativeQueryAsync(String query, Consumer<List<ColumnEntity>> callBack) throws ExecuteAsyncQueryException;

    PreparedStatement nativeQueryPrepare(String query);

}
