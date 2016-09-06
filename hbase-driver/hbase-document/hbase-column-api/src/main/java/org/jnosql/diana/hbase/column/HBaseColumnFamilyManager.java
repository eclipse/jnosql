/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jnosql.diana.hbase.column;


import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.jnosql.diana.api.ExecuteAsyncQueryException;
import org.jnosql.diana.api.TTL;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.WriterField;
import org.jnosql.diana.api.column.*;
import org.jnosql.diana.api.writer.WriterFieldDecorator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.jnosql.diana.api.Condition.EQUALS;

public class HBaseColumnFamilyManager implements ColumnFamilyManager {

    private static final String KEY_REQUIRED_ERROR = "\"To save an entity is necessary to have an row, a Column that has a blank name. Documents.of(\\\"\\\", keyValue);\"";

    private final Connection connection;
    private final Table table;
    private final WriterField writerField = WriterFieldDecorator.getInstance();


    HBaseColumnFamilyManager(Connection connection, Table table) {
        this.connection = connection;
        this.table = table;
    }

    @Override
    public ColumnFamilyEntity save(ColumnFamilyEntity entity) {
        String family = entity.getName();
        List<Column> columns = entity.getColumns();
        if (columns.isEmpty()) {
            return entity;
        }
        Column columnID = entity.find(HBaseUtils.KEY_COLUMN).orElseThrow(() -> new DianaHBaseException(KEY_REQUIRED_ERROR));

        Put put = new Put(Bytes.toBytes(valueToString(columnID.getValue())));
        columns.stream().filter(Predicate.isEqual(columnID).negate()).forEach(column -> {
            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(column.getName()), Bytes
                    .toBytes(valueToString(column.getValue())));
        });
        try {
            table.put(put);
        } catch (IOException e) {
            throw new DianaHBaseException("An error happened when try to save an entity", e);
        }
        return entity;
    }

    @Override
    public ColumnFamilyEntity save(ColumnFamilyEntity entity, TTL ttl) throws NullPointerException {
        throw new UnsupportedOperationException("There is not support to save async");
    }


    @Override
    public void saveAsync(ColumnFamilyEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to save async");
    }

    @Override
    public void saveAsync(ColumnFamilyEntity entity, TTL ttl) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to save async");
    }

    @Override
    public void saveAsync(ColumnFamilyEntity entity, Consumer<ColumnFamilyEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to save async");
    }

    @Override
    public void saveAsync(ColumnFamilyEntity entity, TTL ttl, Consumer<ColumnFamilyEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to save async");
    }

    @Override
    public ColumnFamilyEntity update(ColumnFamilyEntity entity) {
        return save(entity);
    }

    @Override
    public void updateAsync(ColumnFamilyEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to update async");
    }

    @Override
    public void updateAsync(ColumnFamilyEntity entity, Consumer<ColumnFamilyEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to update async");
    }

    @Override
    public void delete(ColumnQuery query) {

        List<ColumnCondition> conditions = query.getConditions();
        if (isQuerySupported(conditions)) {

            List<Delete> deletes = conditions.stream().map(c -> c.getColumn().getValue()).
                    map(this::valueToString)
                    .map(String::getBytes).map(Delete::new).collect(toList());
            try {
                table.delete(deletes);
            } catch (IOException e) {
                throw new DianaHBaseException("An error when try to delete columns", e);
            }
        }

    }

    @Override
    public void deleteAsync(ColumnQuery query) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to find async");
    }

    @Override
    public void deleteAsync(ColumnQuery query, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to delete async");
    }

    @Override
    public List<ColumnFamilyEntity> find(ColumnQuery query) {

        List<ColumnCondition> conditions = query.getConditions();
        if (isQuerySupported(conditions)) {
            return Stream.of(findById(conditions)).map(EntityUnit::new).filter(EntityUnit::isNotEmpty).map(EntityUnit::toEntity).collect(toList());
        }

        throw new UnsupportedOperationException("There is not support to find more than one key");

    }

    @Override
    public void findAsync(ColumnQuery query, Consumer<List<ColumnFamilyEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to find async");
    }

    @Override
    public List<ColumnFamilyEntity> nativeQuery(String query) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to run nativeQuery");
    }

    @Override
    public void nativeQueryAsync(String query, Consumer<List<ColumnFamilyEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to run nativeQuery Async");
    }

    @Override
    public PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to PreparedStatement");
    }

    @Override
    public void close() {
        try {
            connection.close();
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String valueToString(Value value) {
        Object object = value.get();
        if (writerField.isCompatible(object.getClass())) {
            return writerField.write(object).toString();
        } else {
            return object.toString();
        }
    }

    private Result[] findById(List<ColumnCondition> conditions) {
        List<Get> gets = conditions.stream().map(c -> c.getColumn().getValue()).map(this::valueToString)
                .map(String::getBytes).map(Get::new).collect(toList());
        try {
            return table.get(gets);
        } catch (IOException e) {
            throw new DianaHBaseException("An error when try to find by id", e);
        }
    }

    private Map<String, List<Column>> toMap(Result result) {
        Map<String, List<Column>> columnsByKey = new HashMap<>();
        for (Cell cell : result.rawCells()) {
            String key = new String(CellUtil.cloneRow(cell));
            String name = new String(CellUtil.cloneQualifier(cell));
            String value = new String(CellUtil.cloneValue(cell));
            List<Column> columns = columnsByKey.getOrDefault(key, new ArrayList<>());
            columns.add(Column.of(name, value));
            columnsByKey.put(key, columns);
        }
        return columnsByKey;
    }


    private boolean isQuerySupported(List<ColumnCondition> conditions) {
        return conditions.stream().allMatch(columnCondition -> columnCondition.getCondition().equals(EQUALS));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HBaseColumnFamilyManager{");
        sb.append("connection=").append(connection);
        sb.append('}');
        return sb.toString();
    }


}
