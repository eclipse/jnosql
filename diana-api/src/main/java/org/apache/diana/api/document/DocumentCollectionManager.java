package org.apache.diana.api.document;


import org.apache.diana.api.ExecuteAsyncQueryException;
import org.apache.diana.api.column.ColumnFamilyEntity;

import java.util.List;
import java.util.function.Consumer;

/**
 * Interface used to interact with the persistence context to {@link DocumentCollectionEntity}
 * The DocumentCollectionManager API is used to create and remove persistent {@link DocumentCollectionEntity} instances, to find entities by their primary key, and to query over entities.
 */
public interface DocumentCollectionManager extends AutoCloseable {

    /**
     *
     * @param entity
     * @return
     */
    DocumentCollectionEntity save(DocumentCollectionEntity entity);

    /**
     *
     * @param entity
     * @throws ExecuteAsyncQueryException
     * @throws UnsupportedOperationException
     */
    void saveAsync(DocumentCollectionEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     *
     * @param entity
     * @param callBack
     * @throws ExecuteAsyncQueryException
     * @throws UnsupportedOperationException
     */
    void saveAsync(DocumentCollectionEntity entity, Consumer<DocumentCollectionEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     *
     * @param entity
     * @return
     */
    DocumentCollectionEntity update(DocumentCollectionEntity entity);

    /**
     *
     * @param entity
     * @throws ExecuteAsyncQueryException
     * @throws UnsupportedOperationException
     */
    void updateAsync(DocumentCollectionEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     *
     * @param entity
     * @param callBack
     * @throws ExecuteAsyncQueryException
     * @throws UnsupportedOperationException
     */
    void updateAsync(DocumentCollectionEntity entity, Consumer<DocumentCollectionEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     *
     * @param query
     */
    void delete(DocumentQuery query);

    /**
     *
     * @param query
     * @throws ExecuteAsyncQueryException
     * @throws UnsupportedOperationException
     */
    void deleteAsync(DocumentQuery query) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     *
     * @param query
     * @param callBack
     * @throws ExecuteAsyncQueryException
     * @throws UnsupportedOperationException
     */
    void deleteAsync(DocumentQuery query, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     *
     * @param query
     * @return
     */
    List<DocumentCollectionEntity> find(DocumentQuery query);

    /**
     *
     * @param query
     * @param callBack
     * @throws ExecuteAsyncQueryException
     * @throws UnsupportedOperationException
     */
    void findAsync(DocumentQuery query, Consumer<List<DocumentCollectionEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     *
     * @param query
     * @return
     * @throws UnsupportedOperationException
     */
    List<DocumentCollectionEntity> nativeQuery(String query) throws UnsupportedOperationException;

    /**
     *
     * @param query
     * @param callBack
     * @throws ExecuteAsyncQueryException
     * @throws UnsupportedOperationException
     */
    void nativeQueryAsync(String query, Consumer<List<DocumentCollectionEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     *
     * @param query
     * @return
     * @throws UnsupportedOperationException
     */
    PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException;

}
