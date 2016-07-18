package org.apache.diana.cassandra.document;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.Select;
import org.apache.diana.api.ExecuteAsyncQueryException;
import org.apache.diana.api.column.ColumnEntity;
import org.apache.diana.api.column.ColumnEntityManager;
import org.apache.diana.api.column.PreparedStatement;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
        Delete.Where delete = QueryUtils.delete(columnEntity, keyspace);
        session.execute(delete);

    }

    @Override
    public void deleteAsync(ColumnEntity columnEntity) {
        Delete.Where delete = QueryUtils.delete(columnEntity, keyspace);
        session.executeAsync(delete);
    }

    @Override
    public void deleteAsync(ColumnEntity columnEntity, Consumer<Void> consumer) {
        Delete.Where delete = QueryUtils.delete(columnEntity, keyspace);
        ResultSetFuture resultSetFuture = session.executeAsync(delete);
        resultSetFuture.addListener(()-> consumer.accept(null), executor);
    }

    @Override
    public List<ColumnEntity> find(ColumnEntity columnEntity) {
        Select.Where select = QueryUtils.select(columnEntity, keyspace);
        ResultSet resultSet = session.execute(select);
        return resultSet.all().stream().map(row -> CassandraConverter.toDocumentEntity(row)).collect(Collectors.toList());
    }

    @Override
    public void findAsync(ColumnEntity columnEntity, Consumer<List<ColumnEntity>> consumer) {
        Select.Where select = QueryUtils.select(columnEntity, keyspace);
        ResultSetFuture resultSet = session.executeAsync(select);
        CassandraReturnQueryAsync executeAsync = new CassandraReturnQueryAsync(resultSet, consumer);
        resultSet.addListener(executeAsync, executor);
    }

    @Override
    public List<ColumnEntity> nativeQuery(String query) {
        ResultSet resultSet = session.execute(query);
        return resultSet.all().stream().map(row -> CassandraConverter.toDocumentEntity(row)).collect(Collectors.toList());
    }

    @Override
    public void nativeQueryAsync(String query, Consumer<List<ColumnEntity>> consumer) throws ExecuteAsyncQueryException {
        ResultSetFuture resultSet = session.executeAsync(query);
        CassandraReturnQueryAsync executeAsync = new CassandraReturnQueryAsync(resultSet, consumer);
        resultSet.addListener(executeAsync, executor);
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
