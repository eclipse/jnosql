package org.apache.diana.cassandra.document;


import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import org.apache.diana.api.Value;
import org.apache.diana.api.column.Column;
import org.apache.diana.api.column.ColumnEntity;
import org.apache.diana.api.column.ColumnEntityManager;
import org.apache.diana.api.column.PreparedStatement;
import org.apache.diana.api.document.Document;
import org.apache.diana.api.document.Documents;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

class CassandraDocumentEntityManager implements ColumnEntityManager {


    private final Session session;

    private final Executor executor;

    private final String keyspace;

    CassandraDocumentEntityManager(Session session, Executor executor, String keyspace) {
        this.session = session;
        this.executor = executor;
        this.keyspace = keyspace;
    }

    @Override
    public void save(ColumnEntity columnEntity) {
        Insert insert = QueryUtils.insert(columnEntity, keyspace);
        session.execute(insert);
    }

    @Override
    public void saveAsync(ColumnEntity columnEntity) {
        Insert insert = QueryUtils.insert(columnEntity, keyspace);
        session.executeAsync(insert);
    }

    @Override
    public void saveAsync(ColumnEntity columnEntity, Consumer<Void> consumer) {
        Insert insert = QueryUtils.insert(columnEntity, keyspace);
        ResultSetFuture resultSetFuture = session.executeAsync(insert);
        resultSetFuture.addListener(() -> consumer.accept(null), executor);
    }

    @Override
    public void update(ColumnEntity columnEntity) {
        save(columnEntity);
    }

    @Override
    public void updateAsync(ColumnEntity columnEntity) {
        saveAsync(columnEntity);
    }

    @Override
    public void updateAsync(ColumnEntity columnEntity, Consumer<Void> consumer) {
        saveAsync(columnEntity, consumer);
    }

    @Override
    public void delete(ColumnEntity columnEntity) {

    }

    @Override
    public void deleteAsync(ColumnEntity columnEntity) {

    }

    @Override
    public void deleteAsync(ColumnEntity columnEntity, Consumer<Void> consumer) {

    }

    @Override
    public ColumnEntity find(ColumnEntity columnEntity) {
        Select.Where select = QueryUtils.select(columnEntity, keyspace);
        ResultSet execute = session.execute(select);
        List<Document> documents = new ArrayList<>();
        for (Row row : execute.all()) {
            for (ColumnDefinitions.Definition definition : row.getColumnDefinitions().asList()) {
                DataType type = definition.getType();
                Object value = CassandraConverter.get(definition, row);
//              Value value = CassandraValue.of(row, definition.getName());
                Document document = Documents.of(definition.getName(), value);
                documents.add(document);
            }
        }
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
