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

import org.eclipse.jnosql.communication.Condition;
import jakarta.data.repository.Sort;
import jakarta.data.repository.Direction;
import org.eclipse.jnosql.communication.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.communication.column.ColumnCondition.eq;
import static org.eclipse.jnosql.communication.column.ColumnQuery.builder;
import static org.junit.jupiter.api.Assertions.*;

class DefaultColumnQueryBuilderTest {

    @Test
    public void shouldReturnErrorWhenHasNullElementInSelect() {
        assertThrows(NullPointerException.class, () -> builder("document", "document'", null));
    }

    @Test
    public void shouldBuilder() {
        String documentCollection = "documentCollection";
        ColumnQuery query = builder().from(documentCollection).build();
        assertTrue(query.columns().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.columnFamily());
    }

    @Test
    public void shouldSelectDocument() {
        String documentCollection = "documentCollection";
        ColumnQuery query = builder("document", "document2").from(documentCollection).build();
        assertThat(query.columns()).contains("document", "document2");
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.columnFamily());
    }

    @Test
    public void shouldReturnErrorWhenFromIsNull() {
        assertThrows(NullPointerException.class, () -> builder().from(null));
    }


    @Test
    public void shouldSelectOrderAsc() {
        String documentCollection = "documentCollection";
        ColumnQuery query = builder().from(documentCollection).sort(Sort.asc("name")).build();
        assertTrue(query.columns().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.columnFamily());
        assertThat(query.sorts()).contains(Sort.asc("name"));
    }

    @Test
    public void shouldSelectOrderDesc() {
        String documentCollection = "documentCollection";
        ColumnQuery query = builder().from(documentCollection).sort(Sort.desc("name")).build();
        assertTrue(query.columns().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.columnFamily());
        assertThat(query.sorts()).contains(Sort.desc("name"));
    }


    @Test
    public void shouldReturnErrorSelectWhenOrderIsNull() {
        assertThrows(NullPointerException.class,() -> {
            String documentCollection = "documentCollection";
            builder().from(documentCollection).sort((Sort) null);
        });
    }

    @Test
    public void shouldSelectLimit() {
        String documentCollection = "documentCollection";
        ColumnQuery query = builder().from(documentCollection).limit(10).build();
        assertTrue(query.columns().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.columnFamily());
        assertEquals(10L, query.limit());
    }

    @Test
    public void shouldReturnErrorWhenLimitIsNegative() {
        String documentCollection = "documentCollection";
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder().from(documentCollection).limit(-1));
    }

    @Test
    public void shouldSelectSkip() {
        String documentCollection = "documentCollection";
        ColumnQuery query = builder().from(documentCollection).skip(10).build();
        assertTrue(query.columns().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.columnFamily());
        assertEquals(10L, query.skip());
    }

    @Test
    public void shouldReturnErrorWhenSkipIsNegative() {
        String documentCollection = "documentCollection";
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder().from(documentCollection).skip(-1));
    }

    @Test
    public void shouldSelectWhereNameEq() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        ColumnQuery query = builder().from(documentCollection)
                .where(eq("name", name))
                .build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(documentCollection, query.columnFamily());
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", column.name());
        assertEquals(name, column.get());

    }

    @Test
    public void shouldSelectWhereNameLike() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        ColumnQuery query = builder().from(documentCollection)
                .where(ColumnCondition.like("name", name))
                .build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(documentCollection, query.columnFamily());
        assertEquals(Condition.LIKE, condition.condition());
        assertEquals("name", column.name());
        assertEquals(name, column.get());
    }

    @Test
    public void shouldSelectWhereNameGt() {
        String documentCollection = "documentCollection";
        Number value = 10;

        ColumnQuery query = builder().from(documentCollection).where(ColumnCondition.gt("name", 10))
                .build();

        ColumnCondition condition = query.condition().get();

        Column column = condition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(documentCollection, query.columnFamily());
        assertEquals(Condition.GREATER_THAN, condition.condition());
        assertEquals("name", column.name());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameGte() {
        String documentCollection = "documentCollection";
        Number value = 10;
        ColumnCondition gteName = ColumnCondition.gte("name", value);
        ColumnQuery query = builder().from(documentCollection).where(gteName).build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(documentCollection, query.columnFamily());
        assertEquals(Condition.GREATER_EQUALS_THAN, condition.condition());
        assertEquals("name", column.name());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameLt() {
        String documentCollection = "documentCollection";
        Number value = 10;

        ColumnQuery query = builder().from(documentCollection).where(ColumnCondition.lt("name", value))
                .build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(documentCollection, query.columnFamily());
        assertEquals(Condition.LESSER_THAN, condition.condition());
        assertEquals("name", column.name());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameLte() {
        String documentCollection = "documentCollection";
        Number value = 10;
        ColumnQuery query = builder().from(documentCollection).where(ColumnCondition.lte("name", value))
                .build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(documentCollection, query.columnFamily());
        assertEquals(Condition.LESSER_EQUALS_THAN, condition.condition());
        assertEquals("name", column.name());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        String documentCollection = "documentCollection";
        Number valueA = 10;
        Number valueB = 20;

        ColumnQuery query = builder().from(documentCollection)
                .where(ColumnCondition.between("name", Arrays.asList(valueA, valueB)))
                .build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(documentCollection, query.columnFamily());
        assertEquals(Condition.BETWEEN, condition.condition());
        assertEquals("name", column.name());
        assertThat(column.get(new TypeReference<List<Number>>() {
        })).contains(10, 20);
    }

    @Test
    public void shouldSelectWhereNameNot() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        ColumnQuery query = builder().from(documentCollection).where(eq("name", name).negate())
                .build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();
        ColumnCondition negate = column.get(ColumnCondition.class);
        assertTrue(query.columns().isEmpty());
        assertEquals(documentCollection, query.columnFamily());
        assertEquals(Condition.NOT, condition.condition());
        assertEquals(Condition.EQUALS, negate.condition());
        assertEquals("name", negate.column().name());
        assertEquals(name, negate.column().get());
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        ColumnCondition nameEqualsAda = eq("name", name);
        ColumnCondition ageOlderTen = ColumnCondition.gt("age", 10);
        ColumnQuery query = builder().from(documentCollection)
                .where(ColumnCondition.and(nameEqualsAda, ageOlderTen))
                .build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });
        assertEquals(Condition.AND, condition.condition());
        org.assertj.core.api.Assertions.assertThat(conditions).contains(eq(Column.of("name", name)),
                ColumnCondition.gt(Column.of("age", 10)));
    }

    @Test
    public void shouldSelectWhereNameOr() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        ColumnCondition nameEqualsAda = eq("name", name);
        ColumnCondition ageOlderTen = ColumnCondition.gt("age", 10);
        ColumnQuery query = builder().from(documentCollection).where(ColumnCondition.or(nameEqualsAda, ageOlderTen))
                .build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });
        assertEquals(Condition.OR, condition.condition());
        org.assertj.core.api.Assertions.assertThat(conditions).contains(eq(Column.of("name", name)),
                ColumnCondition.gt(Column.of("age", 10)));
    }


    @Test
    public void shouldSelectNegate() {
        String columnFamily = "columnFamily";
        ColumnCondition nameNotEqualsLucas = eq("name", "Lucas").negate();
        ColumnQuery query = builder().from(columnFamily)
                .where(nameNotEqualsLucas).build();

        ColumnCondition condition = query.condition().orElseThrow(RuntimeException::new);
        assertEquals(columnFamily, query.columnFamily());
        Column column = condition.column();
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });

        assertEquals(Condition.NOT, condition.condition());
        org.assertj.core.api.Assertions.assertThat(conditions).contains(eq(Column.of("name", "Lucas")));

    }


    @Test
    public void shouldExecuteManager() {
        ColumnManager manager = Mockito.mock(ColumnManager.class);
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        String collection = "collection";
        Stream<ColumnEntity> entities = builder().from(collection).getResult(manager);
        Mockito.verify(manager).select(queryCaptor.capture());
        checkQuery(queryCaptor, collection);
    }

    @Test
    public void shouldExecuteSingleResultManager() {
        ColumnManager manager = Mockito.mock(ColumnManager.class);
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        String collection = "collection";
        Optional<ColumnEntity> entities = builder().from(collection).getSingleResult(manager);
        Mockito.verify(manager).singleResult(queryCaptor.capture());
        checkQuery(queryCaptor, collection);
    }

    private void checkQuery(ArgumentCaptor<ColumnQuery> queryCaptor, String collection) {
        ColumnQuery query = queryCaptor.getValue();
        assertTrue(query.columns().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(collection, query.columnFamily());
    }
}