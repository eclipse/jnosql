package org.apache.diana.api.document;


import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public interface PreparedStatement extends AutoCloseable {

    List<DocumentEntity> executeQuery();

    void executeQueryAsync(Consumer<List<DocumentEntity>> callBack);

    <T extends Serializable> PreparedStatement bind(T... values);
}
