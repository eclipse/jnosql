package org.apache.diana.api.column;


import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public interface PreparedStatement extends AutoCloseable {

    List<ColumnEntity> executeQuery();

    void executeQueryAsync(Consumer<List<ColumnEntity>> callBack);

    <T extends Serializable> PreparedStatement bind(T... values);
}
