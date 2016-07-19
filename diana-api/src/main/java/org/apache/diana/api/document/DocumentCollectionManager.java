package org.apache.diana.api.document;


import org.apache.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;

public interface DocumentCollectionManager extends AutoCloseable {

    void save(DocumentCollection collection);

    void saveAsync(DocumentCollection collection) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void saveAsync(DocumentCollection collection, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void update(DocumentCollection collection);

    void updateAsync(DocumentCollection collection) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void updateAsync(DocumentCollection collection, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void delete(DocumentQuery query);

    void deleteAsync(DocumentQuery query) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    void deleteAsync(DocumentQuery query, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    List<DocumentCollection> find(DocumentQuery query);

    void findAsync(DocumentQuery query, Consumer<List<DocumentCollection>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    List<DocumentCollection> nativeQuery(String query) throws UnsupportedOperationException;

    void nativeQueryAsync(String query, Consumer<List<DocumentCollection>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException;

}
