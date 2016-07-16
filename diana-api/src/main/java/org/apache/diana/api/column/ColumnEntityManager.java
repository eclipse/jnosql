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

    void delete(Column key);

    void deleteAsync(Column key);

    void deleteAsync(Column key, Consumer<Void> callBack);

    ColumnEntity find(Column key);

    void findAsync(Column key, Consumer<ColumnEntity> callBack);

    List<ColumnEntity> query(String query);

    PreparedStatement prepare(String query);

}
