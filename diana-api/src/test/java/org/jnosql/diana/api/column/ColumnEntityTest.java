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

package org.jnosql.diana.api.column;

import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.document.Document;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;


public class ColumnEntityTest {

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenNameIsNull() {
        ColumnEntity.of(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenColumnsIsNull() {
        ColumnEntity.of("entity", null);
    }

    @Test
    public void shouldReturnOneColunm() {
        ColumnEntity entity = ColumnEntity.of("entity");
        assertEquals(Integer.valueOf(0), Integer.valueOf(entity.size()));
        assertTrue(entity.isEmpty());

        entity.add(Column.of("name", "name"));
        entity.add(Column.of("name2", Value.of("name2")));
        assertFalse(entity.isEmpty());
        assertEquals(Integer.valueOf(2), Integer.valueOf(entity.size()));
        assertFalse(ColumnEntity.of("entity", singletonList(Column.of("name", "name"))).isEmpty());
    }

    @Test
    public void shouldDoCopy() {
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(Column.of("name", "name")));
        ColumnEntity copy = entity.copy();
        assertFalse(entity == copy);
        assertEquals(entity, copy);

    }

    @Test
    public void shouldFindColumn() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        Optional<Column> name = entity.find("name");
        Optional<Column> notfound = entity.find("not_found");
        assertTrue(name.isPresent());
        assertFalse(notfound.isPresent());
        assertEquals(column, name.get());
    }

    @Test
    public void shouldRemoveColumn() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        assertTrue(entity.remove("name"));
        assertTrue(entity.isEmpty());
    }

    @Test
    public void shouldConvertToMap() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        Map<String, Object> result = entity.toMap();
        assertFalse(result.isEmpty());
        assertEquals(Integer.valueOf(1), Integer.valueOf(result.size()));
        assertEquals(column.getName(), result.keySet().stream().findAny().get());

    }

    @Test
    public void shouldShouldCreateANewInsntace() {
        String name = "name";
        ColumnEntity entity = new DefaultColumnEntity(name);
        assertEquals(name, entity.getName());
    }

    @Test
    public void shouldCreateAnEmptyEntity() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        assertTrue(entity.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnAnErrorWhenAddANullColumn() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        entity.add(null);
    }

    @Test
    public void shouldAddANewColumn() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        entity.add(Column.of("column", 12));
        assertFalse(entity.isEmpty());
        assertEquals(1, entity.size());
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenAddAnNullIterable() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        entity.addAll(null);
    }

    @Test
    public void shouldAddAllColumns() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        entity.addAll(Arrays.asList(Column.of("name", 12), Column.of("value", "value")));
        assertFalse(entity.isEmpty());
        assertEquals(2, entity.size());
    }


    @Test
    public void shouldNotFindColumn() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        Optional<Column> column = entity.find("name");
        assertFalse(column.isPresent());
    }

    @Test
    public void shouldRemoveByName() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        entity.add(Column.of("value", 32D));
        assertTrue(entity.remove("value"));
        assertTrue(entity.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenRemovedNameIsNull() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        entity.remove((String) null);
    }

    @Test
    public void shouldNotRemoveByName() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        entity.add(Column.of("value", 32D));

        assertFalse(entity.remove("value1"));
        assertFalse(entity.isEmpty());
    }

    @Test
    public void shouldRemoveByColumn() {
        Column column = Column.of("value", 32D);
        ColumnEntity entity = new DefaultColumnEntity("name");
        entity.add(column);
        assertTrue(entity.remove(column));
        assertTrue(entity.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenRemovedColumnIsNull() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        entity.remove((Column) null);
    }

    @Test
    public void shouldDontRemoveColumn() {
        Column column = Column.of("value", 32D);
        ColumnEntity entity = new DefaultColumnEntity("name");
        entity.add(Column.of("value", 31D));
        assertFalse(entity.remove(column));
        assertFalse(entity.isEmpty());
    }

}