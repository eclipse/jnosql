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
    public void save(ColumnFamily columnFamily) {
        Insert insert = QueryUtils.insert(columnFamily, keyspace);
        session.execute(insert);
    }

    @Override
    public void saveAsync(ColumnFamily columnFamily) {
        Insert insert = QueryUtils.insert(columnFamily, keyspace);
        session.executeAsync(insert);
    }

    @Override
    public void saveAsync(ColumnFamily columnFamily, Consumer<Void> consumer) {
        Insert insert = QueryUtils.insert(columnFamily, keyspace);
        ResultSetFuture resultSetFuture = session.executeAsync(insert);
        resultSetFuture.addListener(() -> consumer.accept(null), executor);
    }

    @Override
    public void update(ColumnFamily columnFamily) {
        save(columnFamily);
    }

    @Override
    public void updateAsync(ColumnFamily columnFamily) {
        saveAsync(columnFamily);
    }

    @Override
    public void updateAsync(ColumnFamily columnFamily, Consumer<Void> consumer) {
        saveAsync(columnFamily, consumer);
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
    public List<ColumnFamily> find(ColumnQuery query) {
        BuiltStatement select = QueryUtils.add(query, keyspace);
        ResultSet resultSet = session.execute(select);
        return resultSet.all().stream().map(row -> CassandraConverter.toDocumentEntity(row)).collect(Collectors.toList());
    }



    @Override
    public void findAsync(ColumnQuery query, Consumer<List<ColumnFamily>> consumer) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        BuiltStatement select = QueryUtils.add(query, keyspace);
        ResultSetFuture resultSet = session.executeAsync(select);
        CassandraReturnQueryAsync executeAsync = new CassandraReturnQueryAsync(resultSet, consumer);
        resultSet.addListener(executeAsync, executor);
    }

    @Override
    public List<ColumnFamily> nativeQuery(String query) {
        ResultSet resultSet = session.execute(query);
        return resultSet.all().stream().map(row -> CassandraConverter.toDocumentEntity(row)).collect(Collectors.toList());
    }

    @Override
    public void nativeQueryAsync(String query, Consumer<List<ColumnFamily>> consumer) throws ExecuteAsyncQueryException {
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
