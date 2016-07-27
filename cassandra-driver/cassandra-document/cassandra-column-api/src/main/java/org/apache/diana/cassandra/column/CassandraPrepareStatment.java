package org.apache.diana.cassandra.column;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import org.apache.diana.api.ExecuteAsyncQueryException;
import org.apache.diana.api.column.ColumnFamilyEntity;
import org.apache.diana.api.column.PreparedStatement;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class CassandraPrepareStatment implements PreparedStatement {

    private final com.datastax.driver.core.PreparedStatement prepare;

    private final Executor executor;

    private final Session session;

    private BoundStatement boundStatement;

    CassandraPrepareStatment(com.datastax.driver.core.PreparedStatement prepare, Executor executor, Session session) {
        this.prepare = prepare;
        this.executor = executor;
        this.session = session;
    }

    @Override
    public List<ColumnFamilyEntity> executeQuery() {
        loadBoundStatment();
        ResultSet resultSet = session.execute(boundStatement);
        return resultSet.all().stream().map(row -> CassandraConverter.toDocumentEntity(row)).collect(Collectors.toList());
    }



    @Override
    public void executeQueryAsync(Consumer<List<ColumnFamilyEntity>> consumer) throws ExecuteAsyncQueryException {
        loadBoundStatment();
        ResultSetFuture resultSet = session.executeAsync(boundStatement);
        CassandraReturnQueryAsync executeAsync = new CassandraReturnQueryAsync(resultSet, consumer);
        resultSet.addListener(executeAsync, executor);
    }

    @Override
    public PreparedStatement bind(Object... values) {
        boundStatement = prepare.bind(values);
        return this;
    }

    private void loadBoundStatment() {
        if (boundStatement == null) {
            boundStatement = prepare.bind();
        }
    }

    @Override
    public void close()  {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CassandraPrepareStatment{");
        sb.append("prepare=").append(prepare);
        sb.append(", executor=").append(executor);
        sb.append(", session=").append(session);
        sb.append(", boundStatement=").append(boundStatement);
        sb.append('}');
        return sb.toString();
    }
}
