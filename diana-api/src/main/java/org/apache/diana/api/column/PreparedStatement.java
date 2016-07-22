package org.apache.diana.api.column;


import org.apache.diana.api.ExecuteAsyncQueryException;

import java.util.List;
import java.util.function.Consumer;

public interface PreparedStatement extends AutoCloseable {

    List<ColumnFamilyEntity> executeQuery();

    void executeQueryAsync(Consumer<List<ColumnFamilyEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    PreparedStatement bind(Object... values);
}
