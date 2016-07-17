package org.apache.diana.api.column;


import java.util.List;
import java.util.function.Consumer;

public interface ColumnEntityManager extends AutoCloseable {

    void save(ColumnEntity entity);

    void saveAsync(ColumnEntity entity);

    void saveAsync(ColumnEntity entity, Consumer<Void> callBack);

    void update(ColumnEntity entity);

    void updateAsync(ColumnEntity entity);

    void updateAsync(ColumnEntity entity, Consumer<Void> callBack);

    void delete(ColumnEntity entity);

    void deleteAsync(ColumnEntity entity);

    void deleteAsync(ColumnEntity entity, Consumer<Void> callBack);

    List<ColumnEntity> find(ColumnEntity entity);

    void findAsync(Column key, Consumer<List<ColumnEntity>> callBack);

    List<ColumnEntity> nativeQuery(String query);

    PreparedStatement nativeQueryPrepare(String query);

}
