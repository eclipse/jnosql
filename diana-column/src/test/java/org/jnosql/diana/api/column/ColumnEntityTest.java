/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
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

package org.jnosql.diana.api.column;

import org.jnosql.diana.api.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    public void shouldReturnErroWhenFindCoumnIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Column column = Column.of("name", "name");
            ColumnEntity entity = ColumnEntity.of("entity", singletonList(column));
            entity.find(null);
        });
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
    public void shouldCreateANewInstance() {
        String name = "name";
        ColumnEntity entity = new DefaultColumnEntity(name);
        assertEquals(name, entity.getName());
    }

    @Test
    public void shouldCreateAnEmptyEntity() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        assertTrue(entity.isEmpty());
    }

    @Test
    public void shouldReturnAnErrorWhenAddANullColumn() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new DefaultColumnEntity("name");
            entity.add(null);
        });
    }

    @Test
    public void shouldAddANewColumn() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        entity.add(Column.of("column", 12));
        assertFalse(entity.isEmpty());
        assertEquals(1, entity.size());
    }

    @Test
    public void shouldReturnErrorWhenAddAnNullIterable() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new DefaultColumnEntity("name");
            entity.addAll(null);
        });
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

    @Test
    public void shouldReturnErrorWhenRemovedNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new DefaultColumnEntity("name");
            entity.remove(null);
        });
    }

    @Test
    public void shouldNotRemoveByName() {
        ColumnEntity entity = new DefaultColumnEntity("name");
        entity.add(Column.of("value", 32D));

        assertFalse(entity.remove("value1"));
        assertFalse(entity.isEmpty());
    }


    @Test
    public void shouldReturnErrorWhenRemoveByNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new DefaultColumnEntity("name");
            entity.remove(null);
        });
    }


    @Test
    public void shouldAddColumnAsNameAndObject() {
        ColumnEntity entity = new DefaultColumnEntity("columnFamily");
        entity.add("name", 10);
        assertEquals(1, entity.size());
        Optional<Column> name = entity.find("name");
        assertTrue(name.isPresent());
        assertEquals(10, name.get().get());
    }

    @Test
    public void shouldAddColumnAsNameAndValue() {
        ColumnEntity entity = new DefaultColumnEntity("columnFamily");
        entity.add("name", Value.of(10));
        assertEquals(1, entity.size());
        Optional<Column> name = entity.find("name");
        assertTrue(name.isPresent());
        assertEquals(10, name.get().get());
    }

    @Test
    public void shouldReturnErrorWhenAddColumnasObjectWhenHasNullObject() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new DefaultColumnEntity("columnFamily");
            entity.add("name", null);
        });
    }

    @Test
    public void shouldReturnErrorWhenAddColumnasObjectWhenHasNullColumnName() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new DefaultColumnEntity("columnFamily");
            entity.add(null, 10);
        });
    }

    @Test
    public void shouldReturnErrorWhenAddColumnasValueWhenHasNullColumnName() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ColumnEntity entity = new DefaultColumnEntity("columnFamily");
            entity.add(null, Value.of(12));
        });
    }


    @Test
    public void shouldAvoidDuplicatedColumn() {
        ColumnEntity entity = new DefaultColumnEntity("columnFamily");
        entity.add("name", 10);
        entity.add("name", 13);
        assertEquals(1, entity.size());
        Optional<Column> column = entity.find("name");
        assertEquals(Column.of("name", 13), column.get());
    }

    @Test
    public void shouldAvoidDuplicatedColumnWhenAddList() {
        List<Column> columns = asList(Column.of("name", 10), Column.of("name", 13));
        ColumnEntity entity = new DefaultColumnEntity("columnFamily");
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
        assertThat(columnFamily.getColumnNames(), containsInAnyOrder("name", "name2", "name3", "name4", "name5"));

    }

    @Test
    public void shouldReturnsTheColumnValues() {
        List<Column> columns = asList(Column.of("name", 10), Column.of("name2", 11),
                Column.of("name3", 12), Column.of("name4", 13),
                Column.of("name5", 14), Column.of("name5", 16));

        ColumnEntity columnFamily = ColumnEntity.of("columnFamily", columns);
        assertThat(columnFamily.getValues(), containsInAnyOrder(Value.of(10), Value.of(11), Value.of(12),
                Value.of(13), Value.of(16)));
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