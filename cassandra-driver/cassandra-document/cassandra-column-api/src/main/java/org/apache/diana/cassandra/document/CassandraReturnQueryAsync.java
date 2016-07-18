package org.apache.diana.cassandra.document;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import org.apache.diana.api.ExecuteAsyncQueryException;
import org.apache.diana.api.column.ColumnEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class CassandraReturnQueryAsync implements Runnable {

    private final ResultSetFuture resultSet;

    private final Consumer<List<ColumnEntity>> consumer;


    public CassandraReturnQueryAsync(ResultSetFuture resultSet, Consumer<List<ColumnEntity>> consumer) {
        this.resultSet = resultSet;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try {
            ResultSet resultSet = this.resultSet.get();
            List<ColumnEntity> entities = resultSet.all().stream().map(row -> CassandraConverter.toDocumentEntity(row)).collect(Collectors.toList());
            consumer.accept(entities);
        } catch (InterruptedException | ExecutionException e) {
            throw new ExecuteAsyncQueryException(e);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CassandraReturnQueryAsync{");
        sb.append("resultSet=").append(resultSet);
        sb.append(", consumer=").append(consumer);
        sb.append('}');
        return sb.toString();
    }
}
