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

import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ColumnEntityTest {

    @Test
    void shouldReturnErrorWhenNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> ColumnEntity.of(null));
    }

    @Test
    void shouldReturnErrorWhenColumnsIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> ColumnEntity.of("entity", null));
    }

    @Test
    void shouldReturnOneColumn() {
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
    void shouldDoCopy() {
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(Column.of("name", "name")));
        ColumnEntity copy = entity.copy();
        assertNotSame(entity, copy);
        assertEquals(entity, copy);

    }

    @Test
    void shouldFindColumn() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        Optional<Column> name = entity.find("name");
        Optional<Column> notfound = entity.find("not_found");
        assertTrue(name.isPresent());
        assertFalse(notfound.isPresent());
        assertEquals(column, name.get());
    }

    @Test
    void shouldReturnErrorWhenFindColumnIsNull() {
        Column column = Column.of("name", "name");
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
            entity.find(null);
        });
    }

    @Test
    void shouldFindValue() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        Optional<String> name = entity.find("name", String.class);
        Assertions.assertNotNull(name);
        Assertions.assertTrue(name.isPresent());
        Assertions.assertEquals("name", name.orElse(""));
    }

    @Test
    void shouldNotFindValue() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        Optional<String> name = entity.find("not_found", String.class);
        Assertions.assertNotNull(name);
        Assertions.assertFalse(name.isPresent());
    }

    @Test
    void shouldFindTypeSupplier() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        List<String> names = entity.find("name", new TypeReference<List<String>>() {})
                .orElse(Collections.emptyList());
        Assertions.assertNotNull(names);
        Assertions.assertFalse(names.isEmpty());
        assertThat(names).contains("name");
    }

    @Test
    void shouldNotFindTypeSupplier() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        List<String> names = entity.find("not_find", new TypeReference<List<String>>() {})
                .orElse(Collections.emptyList());
        Assertions.assertNotNull(names);
        Assertions.assertTrue(names.isEmpty());
    }

    @Test
    void shouldRemoveColumn() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        assertTrue(entity.remove("name"));
        assertTrue(entity.isEmpty());
    }

    @Test
    void shouldConvertToMap() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
        Map<String, Object> result = entity.toMap();
        assertFalse(result.isEmpty());
        assertEquals(Integer.valueOf(1), Integer.valueOf(result.size()));
        assertEquals(column.name(), result.keySet().stream().findAny().get());
    }

    @Test
    void shouldConvertSubColumn() {
        Column column = Column.of("name", "name");
        ColumnEntity entity = ColumnEntity.of("entity", singletonList(Column.of("sub", column)));
        Map<String, Object> result = entity.toMap();
        assertFalse(result.isEmpty());
        assertEquals(Integer.valueOf(1), Integer.valueOf(result.size()));
        Map<String, Object> map = (Map<String, Object>) result.get("sub");
        assertEquals("name", map.get("name"));
    }


    @Test
    void shouldConvertSubColumnListToMap() {
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
    void shouldConvertSubColumnListToMap2() {
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
    void shouldCreateANewInstance() {
        String name = "name";
        ColumnEntity entity = new ColumnEntity(name);
        assertEquals(name, entity.name());
    }

    @Test
    void shouldCreateAnEmptyEntity() {
        ColumnEntity entity = new ColumnEntity("name");
        assertTrue(entity.isEmpty());
    }

    @Test
    void shouldReturnAnErrorWhenAddANullColumn() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("name");
            entity.add(null);
        });
    }

    @Test
    void shouldAddANewColumn() {
        ColumnEntity entity = new ColumnEntity("name");
        entity.add(Column.of("column", 12));
        assertFalse(entity.isEmpty());
        assertEquals(1, entity.size());
    }

    @Test
    void shouldReturnErrorWhenAddAnNullIterable() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("name");
            entity.addAll(null);
        });
    }

    @Test
    void shouldAddAllColumns() {
        ColumnEntity entity = new ColumnEntity("name");
        entity.addAll(Arrays.asList(Column.of("name", 12), Column.of("value", "value")));
        assertFalse(entity.isEmpty());
        assertEquals(2, entity.size());
    }


    @Test
    void shouldNotFindColumn() {
        ColumnEntity entity = new ColumnEntity("name");
        Optional<Column> column = entity.find("name");
        assertFalse(column.isPresent());
    }

    @Test
    void shouldRemoveByName() {
        ColumnEntity entity = new ColumnEntity("name");
        entity.add(Column.of("value", 32D));
        assertTrue(entity.remove("value"));
        assertTrue(entity.isEmpty());
    }

    @Test
    void shouldReturnErrorWhenRemovedNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("name");
            entity.remove(null);
        });
    }

    @Test
    void shouldNotRemoveByName() {
        ColumnEntity entity = new ColumnEntity("name");
        entity.add(Column.of("value", 32D));

        assertFalse(entity.remove("value1"));
        assertFalse(entity.isEmpty());
    }


    @Test
    void shouldReturnErrorWhenRemoveByNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("name");
            entity.remove(null);
        });
    }


    @Test
    void shouldAddColumnAsNameAndObject() {
        ColumnEntity entity = new ColumnEntity("columnFamily");
        entity.add("name", 10);
        assertEquals(1, entity.size());
        Optional<Column> name = entity.find("name");
        assertTrue(name.isPresent());
        assertEquals(10, name.get().get());
    }

    @Test
    void shouldAddColumnAsNameAndValue() {
        ColumnEntity entity = new ColumnEntity("columnFamily");
        entity.add("name", Value.of(10));
        assertEquals(1, entity.size());
        Optional<Column> name = entity.find("name");
        assertTrue(name.isPresent());
        assertEquals(10, name.get().get());
    }

    @Test
    void shouldReturnErrorWhenAddColumnsObjectWhenHasNullObject() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("columnFamily");
            entity.add("name", null);
        });
    }

    @Test
    void shouldReturnErrorWhenAddColumnsObjectWhenHasNullColumnName() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("columnFamily");
            entity.add(null, 10);
        });
    }

    @Test
    void shouldReturnErrorWhenAddColumnsValueWhenHasNullColumnName() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new ColumnEntity("columnFamily");
            entity.add(null, Value.of(12));
        });
    }


    @Test
    void shouldAvoidDuplicatedColumn() {
        ColumnEntity entity = new ColumnEntity("columnFamily");
        entity.add("name", 10);
        entity.add("name", 13);
        assertEquals(1, entity.size());
        Optional<Column> column = entity.find("name");
        assertEquals(Column.of("name", 13), column.get());
    }

    @Test
    void shouldAvoidDuplicatedColumnWhenAddList() {
        List<Column> columns = asList(Column.of("name", 10), Column.of("name", 13));
        ColumnEntity entity = new ColumnEntity("columnFamily");
        entity.addAll(columns);
        assertEquals(1, entity.size());
        assertEquals(1, ColumnEntity.of("columnFamily", columns).size());
    }

    @Test
    void shouldReturnsTheColumnNames() {
        List<Column> columns = asList(Column.of("name", 10), Column.of("name2", 11),
                Column.of("name3", 12), Column.of("name4", 13),
                Column.of("name5", 14), Column.of("name5", 16));

        ColumnEntity columnFamily = ColumnEntity.of("columnFamily", columns);
        assertThat(columnFamily.columnNames())
                .hasSize(5)
                .contains("name", "name2", "name3", "name4", "name5");

    }

    @Test
    void shouldReturnsTheColumnValues() {
        List<Column> columns = asList(Column.of("name", 10), Column.of("name2", 11),
                Column.of("name3", 12), Column.of("name4", 13),
                Column.of("name5", 14), Column.of("name5", 16));

        ColumnEntity columnFamily = ColumnEntity.of("columnFamily", columns);
        assertThat(columnFamily.values()).contains(Value.of(10), Value.of(11), Value.of(12),
                Value.of(13), Value.of(16));
    }

    @Test
    void shouldReturnTrueWhenContainsElement() {
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
    void shouldReturnFalseWhenDoesNotContainElement() {
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
    void shouldRemoveAllElementsWhenUseClearMethod() {
        List<Column> columns = asList(Column.of("name", 10), Column.of("name2", 11),
                Column.of("name3", 12), Column.of("name4", 13),
                Column.of("name5", 14), Column.of("name5", 16));

        ColumnEntity columnFamily = ColumnEntity.of("columnFamily", columns);


        assertFalse(columnFamily.isEmpty());
        columnFamily.clear();
        assertTrue(columnFamily.isEmpty());
    }

}