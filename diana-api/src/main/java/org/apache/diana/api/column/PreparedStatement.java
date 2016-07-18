package org.apache.diana.api.column;


import org.apache.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;

public interface PreparedStatement extends AutoCloseable {

    List<ColumnFamily> executeQuery();

    void executeQueryAsync(Consumer<List<ColumnFamily>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    PreparedStatement bind(Object... values);
}
