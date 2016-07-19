package org.apache.diana.api.document;


import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public interface PreparedStatement extends AutoCloseable {

    List<DocumentCollection> executeQuery();

    void executeQueryAsync(Consumer<List<DocumentCollection>> callBack);

    <T extends Serializable> PreparedStatement bind(T... values);
}
