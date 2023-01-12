/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *
 */

package org.eclipse.jnosql.communication.column;

import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ColumnEntityTest {

    @Test
    public void shouldReturnErrorWhenNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> ColumnEntity.of(null));
    }

    @Test
    public void shouldReturnErrorWhenColumnsIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> ColumnEntity.of("entity", null));
    }

    @Test
    public void shouldReturnOneColumn() {
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
        assertNotSame(entity, copy);
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
    public void shouldReturnErrorWhenFindColumnIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Column column = Column.of("name", "name");
            ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
            entity.find(null);
        });
    }

    @Test
    public void shouldFindValue() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        Optional<String> name = entity.find("name", String.class);
        Assertions.assertNotNull(name);
        Assertions.assertTrue(name.isPresent());
        Assertions.assertEquals("name", name.orElse(""));
    }

    @Test
    public void shouldNotFindValue() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        Optional<String> name = entity.find("not_found", String.class);
        Assertions.assertNotNull(name);
        Assertions.assertFalse(name.isPresent());
    }

    @Test
    public void shouldFindTypeSupplier() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        List<String> names = entity.find("name", new TypeReference<List<String>>() {})
                .orElse(Collections.emptyList());
        Assertions.assertNotNull(names);
        Assertions.assertFalse(names.isEmpty());
        assertThat(names).contains("name");
    }

    @Test
    public void shouldNotFindTypeSupplier() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        List<String> names = entity.find("not_find", new TypeReference<List<String>>() {})
                .orElse(Collections.emptyList());
        Assertions.assertNotNull(names);
        Assertions.assertTrue(names.isEmpty());
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
    public void shouldConvertSubColumn() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(Column.of("sub", column)));
        Map<String, Object> result = entity.toMap();
        assertFalse(result.isEmpty());
        assertEquals(Integer.valueOf(1), Integer.valueOf(result.size()));
        Map<String, Object> map = (Map<String, Object>) result.get("sub");
        assertEquals("name", map.get("name"));
    }


    @Test
    public void shouldConvertSubColumnListToMap() {
        ColumnEntity entity = ColumnEntity.of("entity");
        entity.add(Column.of("_id", "id"));
        List<Column> columns = asList(Column.of("name", "Ada"), Column.of("type", "type"),
                Column.of("information", "ada@lovelace.com"));

        entity.add(Column.of("contacts", columns));
        Map<String, Object> result = entity.toMap();
        assertEquals("id", result.get("_id"));
        List<Map<String, Object>> contacts = (List<Map<String, Object>>) result.get("contacts");
        assertEquals(3, contacts.size());
        assertThat(contacts).contains(singletonMap("name", "Ada"), singletonMap("type", "type"),
                singletonMap("information", "ada@lovelace.com"));

    }

    @Test
    public void shouldConvertSubColumnListToMap2() {
        ColumnEntity entity = ColumnEntity.of("entity");
        entity.add(Column.of("_id", "id"));
        List<List<Column>> columns = new ArrayList<>();
        columns.add(asList(Column.of("name", "Ada"), Column.of("type", "type"),
                Column.of("information", "ada@lovelace.com")));

        entity.add(Column.of("contacts", columns));
        Map<String, Object> result = entity.toMap();
        assertEquals("id", result.get("_id"));
        List<List<Map<String, Object>>> contacts = (List<List<Map<String, Object>>>) result.get("contacts");
        assertEquals(1, contacts.size());
        List<Map<String, Object>> maps = contacts.get(0);
        assertEquals(3, maps.size());
        assertThat(maps).contains(singletonMap("name", "Ada"), singletonMap("type", "type"),
                singletonMap("information", "ada@lovelace.com"));

    }

    @Test
    public void shouldCreateANewInstance() {
        String name = "name";
        ColumnEntity entity = new ColumnEntity(name);
        assertEquals(name, entity.name());
    }

    @Test
    public void shouldCreateAnEmptyEntity() {
        ColumnEntity entity = new ColumnEntity("name");
        assertTrue(entity.isEmpty());
    }

    @Test
    public void shouldReturnAnErrorWhenAddANullColumn() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("name");
            entity.add(null);
        });
    }

    @Test
    public void shouldAddANewColumn() {
        ColumnEntity entity = new ColumnEntity("name");
        entity.add(Column.of("column", 12));
        assertFalse(entity.isEmpty());
        assertEquals(1, entity.size());
    }

    @Test
    public void shouldReturnErrorWhenAddAnNullIterable() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("name");
            entity.addAll(null);
        });
    }

    @Test
    public void shouldAddAllColumns() {
        ColumnEntity entity = new ColumnEntity("name");
        entity.addAll(Arrays.asList(Column.of("name", 12), Column.of("value", "value")));
        assertFalse(entity.isEmpty());
        assertEquals(2, entity.size());
    }


    @Test
    public void shouldNotFindColumn() {
        ColumnEntity entity = new ColumnEntity("name");
        Optional<Column> column = entity.find("name");
        assertFalse(column.isPresent());
    }

    @Test
    public void shouldRemoveByName() {
        ColumnEntity entity = new ColumnEntity("name");
        entity.add(Column.of("value", 32D));
        assertTrue(entity.remove("value"));
        assertTrue(entity.isEmpty());
    }

    @Test
    public void shouldReturnErrorWhenRemovedNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("name");
            entity.remove(null);
        });
    }

    @Test
    public void shouldNotRemoveByName() {
        ColumnEntity entity = new ColumnEntity("name");
        entity.add(Column.of("value", 32D));

        assertFalse(entity.remove("value1"));
        assertFalse(entity.isEmpty());
    }


    @Test
    public void shouldReturnErrorWhenRemoveByNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("name");
            entity.remove(null);
        });
    }


    @Test
    public void shouldAddColumnAsNameAndObject() {
        ColumnEntity entity = new ColumnEntity("columnFamily");
        entity.add("name", 10);
        assertEquals(1, entity.size());
        Optional<Column> name = entity.find("name");
        assertTrue(name.isPresent());
        assertEquals(10, name.get().get());
    }

    @Test
    public void shouldAddColumnAsNameAndValue() {
        ColumnEntity entity = new ColumnEntity("columnFamily");
        entity.add("name", Value.of(10));
        assertEquals(1, entity.size());
        Optional<Column> name = entity.find("name");
        assertTrue(name.isPresent());
        assertEquals(10, name.get().get());
    }

    @Test
    public void shouldReturnErrorWhenAddColumnsObjectWhenHasNullObject() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("columnFamily");
            entity.add("name", null);
        });
    }

    @Test
    public void shouldReturnErrorWhenAddColumnsObjectWhenHasNullColumnName() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("columnFamily");
            entity.add(null, 10);
        });
    }

    @Test
    public void shouldReturnErrorWhenAddColumnsValueWhenHasNullColumnName() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("columnFamily");
            entity.add(null, Value.of(12));
        });
    }


    @Test
    public void shouldAvoidDuplicatedColumn() {
        ColumnEntity entity = new ColumnEntity("columnFamily");
        entity.add("name", 10);
        entity.add("name", 13);
        assertEquals(1, entity.size());
        Optional<Column> column = entity.find("name");
        assertEquals(Column.of("name", 13), column.get());
    }

    @Test
    public void shouldAvoidDuplicatedColumnWhenAddList() {
        List<Column> columns = asList(Column.of("name", 10), Column.of("name", 13));
        ColumnEntity entity = new ColumnEntity("columnFamily");
        entity.addAll(columns);
        assertEquals(1, entity.size());
        assertEquals(1, ColumnEntity.of("columnFamily", columns).size());
    }

    @Test
    public void shouldReturnsTheColumnNames() {
        List<Column> columns = asList(Column.of("name", 10), Column.of("name2", 11),
                Column.of("name3", 12), Column.of("name4", 13),
                Column.of("name5", 14), Column.of("name5", 16));

        ColumnEntity columnFamily = ColumnEntity.of("columnFamily", columns);
        assertThat(columnFamily.columnNames())
                .hasSize(5)
                .contains("name", "name2", "name3", "name4", "name5");

    }

    @Test
    public void shouldReturnsTheColumnValues() {
        List<Column> columns = asList(Column.of("name", 10), Column.of("name2", 11),
                Column.of("name3", 12), Column.of("name4", 13),
                Column.of("name5", 14), Column.of("name5", 16));

        ColumnEntity columnFamily = ColumnEntity.of("columnFamily", columns);
        assertThat(columnFamily.values()).contains(Value.of(10), Value.of(11), Value.of(12),
                Value.of(13), Value.of(16));
    }

    @Test
    public void shouldReturnTrueWhenContainsElement() {
        List<Column> columns = asList(Column.of("name", 10), Column.of("name2", 11),
                Column.of("name3", 12), Column.of("name4", 13),
                Column.of("name5", 14), Column.of("name5", 16));

        ColumnEntity columnFamily = ColumnEntity.of("columnFamily", columns);

        assertTrue(columnFamily.contains("name"));
        assertTrue(columnFamily.contains("name2"));
        assertTrue(columnFamily.contains("name3"));
        assertTrue(columnFamily.contains("name4"));
        assertTrue(columnFamily.contains("name5"));
    }

    @Test
    public void shouldReturnFalseWhenDoesNotContainElement() {
        List<Column> columns = asList(Column.of("name", 10), Column.of("name2", 11),
                Column.of("name3", 12), Column.of("name4", 13),
                Column.of("name5", 14), Column.of("name5", 16));

        ColumnEntity columnFamily = ColumnEntity.of("columnFamily", columns);

        assertFalse(columnFamily.contains("name6"));
        assertFalse(columnFamily.contains("name7"));
        assertFalse(columnFamily.contains("name8"));
        assertFalse(columnFamily.contains("name9"));
        assertFalse(columnFamily.contains("name10"));
    }

    @Test
    public void shouldRemoveAllElementsWhenUseClearMethod() {
        List<Column> columns = asList(Column.of("name", 10), Column.of("name2", 11),
                Column.of("name3", 12), Column.of("name4", 13),
                Column.of("name5", 14), Column.of("name5", 16));

        ColumnEntity columnFamily = ColumnEntity.of("columnFamily", columns);


        assertFalse(columnFamily.isEmpty());
        columnFamily.clear();
        assertTrue(columnFamily.isEmpty());
    }

}