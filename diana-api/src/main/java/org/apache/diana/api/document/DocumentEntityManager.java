package org.apache.diana.api.document;


import java.util.List;
import java.util.function.Consumer;

public interface DocumentEntityManager extends AutoCloseable {

    void save(DocumentEntity entity);

    void saveAsync(DocumentEntity entity);

    void saveAsync(DocumentEntity entity, Consumer<Void> callBack);

    void update(DocumentEntity entity);

    void updateAsync(DocumentEntity entity);

    void updateAsync(DocumentEntity entity, Consumer<Void> callBack);

    void delete(Document key);

    void deleteAsync(Document key);

    void deleteAsync(Document key, Consumer<Void> callBack);

    DocumentEntity find(Document key);

    void findAsync(Document key, Consumer<DocumentEntity> callBack);

    List<DocumentEntity> nativeQuery(String query);

    PreparedStatement nativeQueryPrepare(String query);

}
