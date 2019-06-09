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
package org.jnosql.diana.column;

import org.hamcrest.Matchers;
import org.jnosql.diana.Condition;
import org.jnosql.diana.TypeReference;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class DefaultColumnConditionTest {


    private final ColumnCondition lte = ColumnCondition.lte(Column.of("salary", 10.32));

    @Test
    public void shouldReturnErrorWhenColumnIsNull() {
        assertThrows(NullPointerException.class, () -> DefaultColumnCondition.of(null, Condition.AND));
    }

    @Test
    public void shouldCreateAnInstance() {
        Column name = Column.of("name", "Otavio");
        ColumnCondition condition = DefaultColumnCondition.of(name, Condition.EQUALS);
        assertNotNull(condition);
        assertEquals(name, condition.getColumn());
        assertEquals(Condition.EQUALS, condition.getCondition());
    }

    @Test
    public void shouldCreateNegationConditon() {
        Column age = Column.of("age", 26);
        ColumnCondition condition = DefaultColumnCondition.of(age, Condition.GREATER_THAN);
        ColumnCondition negate = condition.negate();
        Column negateColumn = negate.getColumn();
        assertEquals(Condition.NOT, negate.getCondition());
        assertEquals(Condition.NOT.getNameField(), negateColumn.getName());
        assertEquals(DefaultColumnCondition.of(age, Condition.GREATER_THAN), negateColumn.getValue().get());
    }


    @Test
    public void shouldCreateAndCondition() {
        Column age = Column.of("age", 26);
        Column name = Column.of("name", "Otavio");
        ColumnCondition condition1 = DefaultColumnCondition.of(name, Condition.EQUALS);
        ColumnCondition condition2 = DefaultColumnCondition.of(age, Condition.GREATER_THAN);

        ColumnCondition and = condition1.and(condition2);
        Column andColumn = and.getColumn();
        assertEquals(Condition.AND, and.getCondition());
        assertEquals(Condition.AND.getNameField(), andColumn.getName());
        assertThat(andColumn.getValue().get(new TypeReference<List<ColumnCondition>>() {
                }),
                Matchers.containsInAnyOrder(condition1, condition2));

    }

    @Test
    public void shouldCreateOrCondition() {
        Column age = Column.of("age", 26);
        Column name = Column.of("name", "Otavio");
        ColumnCondition condition1 = DefaultColumnCondition.of(name, Condition.EQUALS);
        ColumnCondition condition2 = DefaultColumnCondition.of(age, Condition.GREATER_THAN);

        ColumnCondition and = condition1.or(condition2);
        Column andColumn = and.getColumn();
        assertEquals(Condition.OR, and.getCondition());
        assertEquals(Condition.OR.getNameField(), andColumn.getName());
        assertThat(andColumn.getValue().get(new TypeReference<List<ColumnCondition>>() {
                }),
                Matchers.containsInAnyOrder(condition1, condition2));

    }

    @Test
    public void shouldReturnErrorWhenCreateAndWithNullValues() {
        assertThrows(NullPointerException.class, () -> DefaultColumnCondition.and((ColumnCondition[]) null));
    }


    @Test
    public void shouldReturnErrorWhenCreateOrWithNullValues() {
        assertThrows(NullPointerException.class, () -> DefaultColumnCondition.or((ColumnCondition[]) null));
    }


    @Test
    public void shouldAppendAnd() {
        ColumnCondition eq = ColumnCondition.eq(Column.of("name", "otavio"));
        ColumnCondition gt = ColumnCondition.gt(Column.of("age", 10));
        ColumnCondition and = ColumnCondition.and(eq, gt);
        assertEquals(Condition.AND, and.getCondition());
        List<ColumnCondition> conditions = and.getColumn().get(new TypeReference<List<ColumnCondition>>() {
        });
        assertThat(conditions, Matchers.containsInAnyOrder(eq, gt));
    }

    @Test
    public void shouldAppendOr() {
        ColumnCondition eq = ColumnCondition.eq(Column.of("name", "otavio"));
        ColumnCondition gt = ColumnCondition.gt(Column.of("age", 10));
        ColumnCondition and = ColumnCondition.or(eq, gt);
        assertEquals(Condition.OR, and.getCondition());
        List<ColumnCondition> conditions = and.getColumn().get(new TypeReference<List<ColumnCondition>>() {
        });
        assertThat(conditions, Matchers.containsInAnyOrder(eq, gt));
    }

    @Test
    public void shouldAnd() {
        ColumnCondition eq = ColumnCondition.eq(Column.of("name", "otavio"));
        ColumnCondition gt = ColumnCondition.gt(Column.of("age", 10));
        ColumnCondition lte = ColumnCondition.lte(Column.of("salary", 10_000.00));

        ColumnCondition and = eq.and(gt);
        List<ColumnCondition> conditions = and.getColumn().get(new TypeReference<List<ColumnCondition>>() {
        });
        assertEquals(Condition.AND, and.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(eq, gt));
        ColumnCondition result = and.and(lte);

        assertEquals(Condition.AND, result.getCondition());
        assertThat(result.getColumn().get(new TypeReference<List<ColumnCondition>>() {
        }), Matchers.containsInAnyOrder(eq, gt, lte));

    }

    @Test
    public void shouldOr() {
        ColumnCondition eq = ColumnCondition.eq(Column.of("name", "otavio"));
        ColumnCondition gt = ColumnCondition.gt(Column.of("age", 10));
        ColumnCondition lte = ColumnCondition.lte(Column.of("salary", 10_000.00));

        ColumnCondition or = eq.or(gt);
        List<ColumnCondition> conditions = or.getColumn().get(new TypeReference<List<ColumnCondition>>() {
        });
        assertEquals(Condition.OR, or.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(eq, gt));
        ColumnCondition result = or.or(lte);

        assertEquals(Condition.OR, result.getCondition());
        assertThat(result.getColumn().get(new TypeReference<List<ColumnCondition>>() {
        }), Matchers.containsInAnyOrder(eq, gt, lte));

    }

    @Test
    public void shouldNegate() {
        ColumnCondition eq = ColumnCondition.eq(Column.of("name", "otavio"));
        ColumnCondition negate = eq.negate();
        assertEquals(Condition.NOT, negate.getCondition());
        ColumnCondition condition = negate.getColumn().get(ColumnCondition.class);
        assertEquals(eq, condition);
    }

    @Test
    public void shouldAfirmeDoubleNegate() {
        ColumnCondition eq = ColumnCondition.eq(Column.of("name", "otavio"));
        ColumnCondition afirmative = eq.negate().negate();
        assertEquals(eq.getCondition(), afirmative.getCondition());

    }

    @Test
    public void shouldReturnErroWhenBetweenIsNull() {
        assertThrows(NullPointerException.class, () -> ColumnCondition.between(null));
    }

    @Test
    public void shouldReturnErroWhenBetweenIsNotIterable() {
        assertThrows(IllegalArgumentException.class, () -> {
            Column column = Column.of("age", 12);
            ColumnCondition.between(column);
        });
    }

    @Test
    public void shouldReturnErrorWhenIterableHasOneElement() {
        assertThrows(IllegalArgumentException.class, () -> {
            Column column = Column.of("age", Collections.singleton(12));
            ColumnCondition.between(column);
        });
    }

    @Test
    public void shouldReturnErrorWhenIterableHasMoreThanTwoElement2() {
        assertThrows(IllegalArgumentException.class, () -> {
            Column column = Column.of("age", Arrays.asList(12, 12, 12));
            ColumnCondition.between(column);
        });
    }

    @Test
    public void shouldReturnBetween() {
        Column column = Column.of("age", Arrays.asList(12, 13));
        ColumnCondition between = ColumnCondition.between(column);
        assertEquals(Condition.BETWEEN, between.getCondition());
        Iterable<Integer> integers = between.getColumn().get(new TypeReference<Iterable<Integer>>() {
        });
        assertThat(integers, contains(12, 13));
    }

    @Test
    public void shouldReturnErrorWhenInConditionIsInvalid() {
        assertThrows(NullPointerException.class, () -> ColumnCondition.in(null));
        assertThrows(IllegalArgumentException.class, () -> ColumnCondition.in(Column.of("value", 10)));
    }

    @Test
    public void shouldReturnInClause() {
        Column column = Column.of("age", Arrays.asList(12, 13));
        ColumnCondition in = ColumnCondition.in(column);
        assertEquals(Condition.IN, in.getCondition());
        Iterable<Integer> integers = in.getColumn().get(new TypeReference<Iterable<Integer>>() {
        });
        assertThat(integers, contains(12, 13));
    }

}