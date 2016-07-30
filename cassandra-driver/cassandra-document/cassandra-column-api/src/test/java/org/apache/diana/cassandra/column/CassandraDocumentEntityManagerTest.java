/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.diana.cassandra.column;

import com.datastax.driver.core.Session;
import org.apache.diana.api.Sort;
import org.apache.diana.api.Value;
import org.apache.diana.api.column.*;
import org.apache.diana.api.document.Document;
import org.apache.diana.api.document.Documents;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.apache.diana.cassandra.column.Constants.COLUMN_FAMILY;
import static org.apache.diana.cassandra.column.Constants.KEY_SPACE;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;


public class CassandraDocumentEntityManagerTest {

    private ColumnFamilyManager columnEntityManager;

    @Before
    public void setUp() {
        CassandraConfiguration cassandraConfiguration = new CassandraConfiguration();
        ColumnFamilyManagerFactory entityManagerFactory = cassandraConfiguration.getManagerFactory();
        columnEntityManager = entityManagerFactory.getColumnEntityManager(KEY_SPACE);
    }

    @Test
    public void shouldClose() throws Exception {
        columnEntityManager.close();
        CassandraDocumentEntityManager cassandraDocumentEntityManager = CassandraDocumentEntityManager.class.cast(columnEntityManager);
        Session session = cassandraDocumentEntityManager.getSession();
        assertTrue(session.isClosed());
    }

    @Test
    public void shouldInsertJustKey() {
        Column key = Columns.of("id", 10L);
        ColumnFamilyEntity columnEntity = ColumnFamilyEntity.of(COLUMN_FAMILY);
        columnEntity.add(key);
        columnEntityManager.save(columnEntity);
    }

    @Test
    public void shouldInsertColumns() {
        ColumnFamilyEntity columnEntity = getColumnFamily();
        columnEntityManager.save(columnEntity);
    }


    @Test
    public void shouldFindById() {

        columnEntityManager.save(getColumnFamily());
        ColumnQuery query = ColumnQuery.of(COLUMN_FAMILY).addCondition(ColumnCondition.eq(Columns.of("id", 10L)));
        List<ColumnFamilyEntity> columnEntity = columnEntityManager.find(query);
        assertFalse(columnEntity.isEmpty());
        List<Column> columns = columnEntity.get(0).getColumns();
        assertThat(columns.stream().map(Column::getName).collect(toList()), containsInAnyOrder("name", "version", "options", "id"));
        assertThat(columns.stream().map(Column::getValue).map(Value::get).collect(toList()), containsInAnyOrder("Cassandra", 3.2, asList(1, 2, 3), 10L));

    }

    @Test
    public void shouldRunNativeQuery() {
        columnEntityManager.save(getColumnFamily());
        List<ColumnFamilyEntity> entities = columnEntityManager.nativeQuery("select * from newKeySpace.newColumnFamily where id=10;");
        assertFalse(entities.isEmpty());
        List<Column> columns = entities.get(0).getColumns();
        assertThat(columns.stream().map(Column::getName).collect(toList()), containsInAnyOrder("name", "version", "options", "id"));
        assertThat(columns.stream().map(Column::getValue).map(Value::get).collect(toList()), containsInAnyOrder("Cassandra", 3.2, asList(1, 2, 3), 10L));
    }

    @Test
    public void shouldPrepareStatment() {
        columnEntityManager.save(getColumnFamily());
        PreparedStatement preparedStatement = columnEntityManager.nativeQueryPrepare("select * from newKeySpace.newColumnFamily where id=?");
        preparedStatement.bind(10L);
        List<ColumnFamilyEntity> entities = preparedStatement.executeQuery();
        List<Column> columns = entities.get(0).getColumns();
        assertThat(columns.stream().map(Column::getName).collect(toList()), containsInAnyOrder("name", "version", "options", "id"));
        assertThat(columns.stream().map(Column::getValue).map(Value::get).collect(toList()), containsInAnyOrder("Cassandra", 3.2, asList(1, 2, 3), 10L));
    }

    @Test
    public void shouldDeleteColumnFamiliy() {
        columnEntityManager.save(getColumnFamily());
        ColumnFamilyEntity.of(COLUMN_FAMILY, singletonList(Columns.of("id", 10L)));
        ColumnQuery query = ColumnQuery.of(COLUMN_FAMILY).addCondition(ColumnCondition.eq(Columns.of("id", 10L)));
        columnEntityManager.delete(query);
        List<ColumnFamilyEntity> entities = columnEntityManager.nativeQuery("select * from newKeySpace.newColumnFamily where id=10;");
        Assert.assertTrue(entities.isEmpty());
    }

    @Test
    public void shouldLimitResult() {
        getEntities().forEach(columnEntityManager::save);
        ColumnQuery query = ColumnQuery.of(COLUMN_FAMILY);
        query.addCondition(ColumnCondition.in(Column.of("id", asList(1L, 2L, 3L))));
        query.setLimit(2L);
        List<ColumnFamilyEntity> columnFamilyEntities = columnEntityManager.find(query);
        assertEquals(Integer.valueOf(2), Integer.valueOf(columnFamilyEntities.size()));
    }
    
    private List<ColumnFamilyEntity> getEntities() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Cassandra");
        fields.put("version", 3.2);
        fields.put("options", asList(1, 2, 3));
        List<Column> columns = Columns.of(fields);
        ColumnFamilyEntity entity = ColumnFamilyEntity.of(COLUMN_FAMILY, singletonList(Columns.of("id", 1L)));
        ColumnFamilyEntity entity1 = ColumnFamilyEntity.of(COLUMN_FAMILY, singletonList(Columns.of("id", 2L)));
        ColumnFamilyEntity entity2 = ColumnFamilyEntity.of(COLUMN_FAMILY, singletonList(Columns.of("id", 3L)));
        columns.forEach(entity::add);
        columns.forEach(entity1::add);
        columns.forEach(entity2::add);
        return asList(entity, entity1, entity2);
    }

    private ColumnFamilyEntity getColumnFamily() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Cassandra");
        fields.put("version", 3.2);
        fields.put("options", asList(1, 2, 3));
        List<Column> columns = Columns.of(fields);
        ColumnFamilyEntity columnFamily = ColumnFamilyEntity.of(COLUMN_FAMILY, singletonList(Columns.of("id", 10L)));
        columns.forEach(columnFamily::add);
        return columnFamily;
    }

}