package org.apache.diana.cassandra.document;


import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.apache.diana.api.column.Column;
import org.apache.diana.api.column.ColumnEntity;
import org.apache.diana.api.column.ColumnEntityManager;
import org.apache.diana.api.column.PreparedStatement;

import java.util.List;
import java.util.function.Consumer;

class CassandraDocumentEntityManager implements ColumnEntityManager {


    private final Session session;

    private final String keyspace;

    CassandraDocumentEntityManager(Session session, String keyspace) {
        this.session = session;
        this.keyspace = keyspace;
    }

    @Override
    public void save(ColumnEntity columnEntity) {
        Insert insert = QueryBuilder.insertInto(keyspace, columnEntity.getColumnFamily());
        Column key = columnEntity.getKey();
        insert.value(key.getName(), key.getValue().get());
        columnEntity.getColumns().forEach(d -> insert.value(d.getName(), d.getValue().get()));
        session.execute(insert);
    }

    @Override
    public void saveAsync(ColumnEntity columnEntity) {

    }

    @Override
    public void saveAsync(ColumnEntity columnEntity, Consumer<Void> consumer) {

    }

    @Override
    public void update(ColumnEntity columnEntity) {

    }

    @Override
    public void updateAsync(ColumnEntity columnEntity) {

    }

    @Override
    public void updateAsync(ColumnEntity columnEntity, Consumer<Void> consumer) {

    }

    @Override
    public void delete(Column column) {

    }

    @Override
    public void deleteAsync(Column column) {

    }

    @Override
    public void deleteAsync(Column column, Consumer<Void> consumer) {

    }

    @Override
    public ColumnEntity find(Column column) {
        return null;
    }

    @Override
    public void findAsync(Column column, Consumer<ColumnEntity> consumer) {

    }

    @Override
    public List<ColumnEntity> nativeQuery(String s) {
        return null;
    }

    @Override
    public PreparedStatement nativeQueryPrepare(String s) {
        return null;
    }

    @Override
    public void close() throws Exception {
        session.close();
    }

    Session getSession() {
        return session;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CassandraDocumentEntityManager{");
        sb.append("session=").append(session);
        sb.append('}');
        return sb.toString();
    }
}
