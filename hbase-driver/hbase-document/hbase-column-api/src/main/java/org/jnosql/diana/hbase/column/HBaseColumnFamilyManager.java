package org.jnosql.diana.hbase.column;


import org.apache.hadoop.hbase.client.Connection;
import org.jnosql.diana.api.ExecuteAsyncQueryException;
import org.jnosql.diana.api.column.ColumnFamilyEntity;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnQuery;
import org.jnosql.diana.api.column.PreparedStatement;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

class HBaseColumnFamilyManager implements ColumnFamilyManager {

    private final Connection connection;

    HBaseColumnFamilyManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ColumnFamilyEntity save(ColumnFamilyEntity entity) {
        return null;
    }

    @Override
    public void saveAsync(ColumnFamilyEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public void saveAsync(ColumnFamilyEntity entity, Consumer<ColumnFamilyEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public ColumnFamilyEntity update(ColumnFamilyEntity entity) {
        return null;
    }

    @Override
    public void updateAsync(ColumnFamilyEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public void updateAsync(ColumnFamilyEntity entity, Consumer<ColumnFamilyEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public void delete(ColumnQuery query) {

    }

    @Override
    public void deleteAsync(ColumnQuery query) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public void deleteAsync(ColumnQuery query, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public List<ColumnFamilyEntity> find(ColumnQuery query) {
        return null;
    }

    @Override
    public void findAsync(ColumnQuery query, Consumer<List<ColumnFamilyEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public List<ColumnFamilyEntity> nativeQuery(String query) throws UnsupportedOperationException {
        return null;
    }

    @Override
    public void nativeQueryAsync(String query, Consumer<List<ColumnFamilyEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException {
        return null;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
