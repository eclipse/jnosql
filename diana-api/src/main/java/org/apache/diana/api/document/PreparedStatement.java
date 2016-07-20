package org.apache.diana.api.document;


import java.util.List;
import java.util.function.Consumer;

public interface PreparedStatement extends AutoCloseable {

    List<DocumentCollection> executeQuery();

    void executeQueryAsync(Consumer<List<DocumentCollection>> callBack);

    PreparedStatement bind(Object... values);
}
