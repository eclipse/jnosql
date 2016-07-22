package org.apache.diana.api.document;


import org.apache.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;

public interface DocumentCollectionManager extends AutoCloseable {

    DocumentCollectionEntity save(DocumentCollectionEntity entity);

    void saveAsync(DocumentCollectionEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void saveAsync(DocumentCollectionEntity entity, Consumer<DocumentCollectionEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    DocumentCollectionEntity update(DocumentCollectionEntity entity);

    void updateAsync(DocumentCollectionEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void updateAsync(DocumentCollectionEntity entity, Consumer<DocumentCollectionEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void delete(DocumentQuery query);

    void deleteAsync(DocumentQuery query) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void deleteAsync(DocumentQuery query, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    List<DocumentCollectionEntity> find(DocumentQuery query);

    void findAsync(DocumentQuery query, Consumer<List<DocumentCollectionEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    List<DocumentCollectionEntity> nativeQuery(String query) throws UnsupportedOperationException;

    void nativeQueryAsync(String query, Consumer<List<DocumentCollectionEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException;

}
