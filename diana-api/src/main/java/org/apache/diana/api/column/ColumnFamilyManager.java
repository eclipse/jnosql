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
     * Save a Column family
     * @param columnFamilyEntity column family to be saved
     */
    void save(ColumnFamilyEntity columnFamilyEntity);

    void saveAsync(ColumnFamilyEntity columnFamilyEntity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void saveAsync(ColumnFamilyEntity columnFamilyEntity, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void update(ColumnFamilyEntity columnFamilyEntity);

    void updateAsync(ColumnFamilyEntity columnFamilyEntity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void updateAsync(ColumnFamilyEntity columnFamilyEntity, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void delete(ColumnQuery query);

    void deleteAsync(ColumnQuery query) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void deleteAsync(ColumnQuery query, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    List<ColumnFamilyEntity> find(ColumnQuery query);

    void findAsync(ColumnQuery query, Consumer<List<ColumnFamilyEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    List<ColumnFamilyEntity> nativeQuery(String query) throws UnsupportedOperationException;

    void nativeQueryAsync(String query, Consumer<List<ColumnFamilyEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException;

}
