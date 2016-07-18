package org.apache.diana.api.column;


import org.apache.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;

public interface PreparedStatement extends AutoCloseable {

    List<ColumnEntity> executeQuery();

    void executeQueryAsync(Consumer<List<ColumnEntity>> callBack) throws ExecuteAsyncQueryException;

    PreparedStatement bind(Object... values);
}
