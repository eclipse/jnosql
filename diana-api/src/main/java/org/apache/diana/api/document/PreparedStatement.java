package org.apache.diana.api.document;


import org.apache.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;

public interface PreparedStatement extends AutoCloseable {

    List<DocumentCollection> executeQuery();

    void executeQueryAsync(Consumer<List<DocumentCollection>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    PreparedStatement bind(Object... values);
}
