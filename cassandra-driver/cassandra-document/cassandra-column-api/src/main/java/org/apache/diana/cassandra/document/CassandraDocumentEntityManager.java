package org.apache.diana.cassandra.document;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.Select;
import org.apache.diana.api.ExecuteAsyncQueryException;
import org.apache.diana.api.column.*;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class CassandraDocumentEntityManager implements ColumnFamilyManager {


    private final Session session;

    private final Executor executor;

    private final String keyspace;

    CassandraDocumentEntityManager(Session session, Executor executor, String keyspace) {
        this.session = session;
        this.executor = executor;
        this.keyspace = keyspace;
    }

    @Override
    public void save(ColumnFamilyEntity entity) {
        Insert insert = QueryUtils.insert(entity, keyspace);
        session.execute(insert);
    }

    @Override
    public void saveAsync(ColumnFamilyEntity entity) {
        Insert insert = QueryUtils.insert(entity, keyspace);
        session.executeAsync(insert);
    }

    @Override
    public void saveAsync(ColumnFamilyEntity entity, Consumer<Void> consumer) {
        Insert insert = QueryUtils.insert(entity, keyspace);
        ResultSetFuture resultSetFuture = session.executeAsync(insert);
        resultSetFuture.addListener(() -> consumer.accept(null), executor);
    }

    @Override
    public void update(ColumnFamilyEntity entity) {
        save(entity);
    }

    @Override
    public void updateAsync(ColumnFamilyEntity entity) {
        saveAsync(entity);
    }

    @Override
    public void updateAsync(ColumnFamilyEntity entity, Consumer<Void> consumer) {
        saveAsync(entity, consumer);
    }

    @Override
    public void delete(ColumnQuery query) {
        BuiltStatement delete = QueryUtils.delete(query, keyspace);
        session.execute(delete);

    }

    @Override
    public void deleteAsync(ColumnQuery query) {
        BuiltStatement delete = QueryUtils.delete(query, keyspace);
        session.executeAsync(delete);
    }

    @Override
    public void deleteAsync(ColumnQuery query, Consumer<Void> consumer) {
        BuiltStatement delete = QueryUtils.delete(query, keyspace);
        ResultSetFuture resultSetFuture = session.executeAsync(delete);
        resultSetFuture.addListener(() -> consumer.accept(null), executor);
    }

    @Override
    public List<ColumnFamilyEntity> find(ColumnQuery query) {
        BuiltStatement select = QueryUtils.add(query, keyspace);
        ResultSet resultSet = session.execute(select);
        return resultSet.all().stream().map(row -> CassandraConverter.toDocumentEntity(row)).collect(Collectors.toList());
    }



    @Override
    public void findAsync(ColumnQuery query, Consumer<List<ColumnFamilyEntity>> consumer) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        BuiltStatement select = QueryUtils.add(query, keyspace);
        ResultSetFuture resultSet = session.executeAsync(select);
        CassandraReturnQueryAsync executeAsync = new CassandraReturnQueryAsync(resultSet, consumer);
        resultSet.addListener(executeAsync, executor);
    }

    @Override
    public List<ColumnFamilyEntity> nativeQuery(String query) {
        ResultSet resultSet = session.execute(query);
        return resultSet.all().stream().map(row -> CassandraConverter.toDocumentEntity(row)).collect(Collectors.toList());
    }

    @Override
    public void nativeQueryAsync(String query, Consumer<List<ColumnFamilyEntity>> consumer) throws ExecuteAsyncQueryException {
        ResultSetFuture resultSet = session.executeAsync(query);
        CassandraReturnQueryAsync executeAsync = new CassandraReturnQueryAsync(resultSet, consumer);
        resultSet.addListener(executeAsync, executor);
    }

    @Override
    public PreparedStatement nativeQueryPrepare(String query) {
        com.datastax.driver.core.PreparedStatement prepare = session.prepare(query);
        return new CassandraPrepareStatment(prepare, executor, session);
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
