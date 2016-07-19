package org.apache.diana.api.document;


import java.util.List;
import java.util.function.Consumer;

public interface DocumentCollectionManager extends AutoCloseable {

    void save(DocumentCollection entity);

    void saveAsync(DocumentCollection entity);

    void saveAsync(DocumentCollection entity, Consumer<Void> callBack);

    void update(DocumentCollection entity);

    void updateAsync(DocumentCollection entity);

    void updateAsync(DocumentCollection entity, Consumer<Void> callBack);

    void delete(Document key);

    void deleteAsync(Document key);

    void deleteAsync(Document key, Consumer<Void> callBack);

    DocumentCollection find(Document key);

    void findAsync(Document key, Consumer<DocumentCollection> callBack);

    List<DocumentCollection> nativeQuery(String query);

    PreparedStatement nativeQueryPrepare(String query);

}
