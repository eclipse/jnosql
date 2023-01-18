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
import org.eclipse.jnosql.communication.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class DefaultColumnConditionTest {

    private final ColumnCondition lte = ColumnCondition.lte(Column.of("salary", 10.32));

    @Test
    public void shouldReturnErrorWhenColumnIsNull() {
        assertThrows(NullPointerException.class, () -> ColumnCondition.of(null, Condition.AND));
    }

    @Test
    public void shouldCreateAnInstance() {
        Column name = Column.of("name", "Otavio");
        ColumnCondition condition = ColumnCondition.of(name, Condition.EQUALS);
        assertNotNull(condition);
        assertEquals(name, condition.column());
        assertEquals(Condition.EQUALS, condition.condition());
    }

    @Test
    public void shouldCreateNegationCondition() {
        Column age = Column.of("age", 26);
        ColumnCondition condition = ColumnCondition.of(age, Condition.GREATER_THAN);
        ColumnCondition negate = condition.negate();
        Column negateColumn = negate.column();
        assertEquals(Condition.NOT, negate.condition());
        assertEquals(Condition.NOT.getNameField(), negateColumn.name());
        assertEquals(ColumnCondition.of(age, Condition.GREATER_THAN), negateColumn.value().get());
    }

    @Test
    public void shouldReturnValidDoubleNegation() {
        Column age = Column.of("age", 26);
        ColumnCondition condition = ColumnCondition.of(age, Condition.GREATER_THAN);
        ColumnCondition affirmative = condition.negate().negate();
        Assertions.assertEquals(condition, affirmative);
    }

    @Test
    public void shouldCreateAndCondition() {
        Column age = Column.of("age", 26);
        Column name = Column.of("name", "Otavio");
        ColumnCondition condition1 = ColumnCondition.of(name, Condition.EQUALS);
        ColumnCondition condition2 = ColumnCondition.of(age, Condition.GREATER_THAN);

        ColumnCondition and = condition1.and(condition2);
        Column andColumn = and.column();
        assertEquals(Condition.AND, and.condition());
        assertEquals(Condition.AND.getNameField(), andColumn.name());
        assertThat(andColumn.value().get(new TypeReference<List<ColumnCondition>>() {
                })).contains(condition1, condition2);

    }

    @Test
    public void shouldCreateOrCondition() {
        Column age = Column.of("age", 26);
        Column name = Column.of("name", "Otavio");
        ColumnCondition condition1 = ColumnCondition.of(name, Condition.EQUALS);
        ColumnCondition condition2 = ColumnCondition.of(age, Condition.GREATER_THAN);

        ColumnCondition and = condition1.or(condition2);
        Column andColumn = and.column();
        assertEquals(Condition.OR, and.condition());
        assertEquals(Condition.OR.getNameField(), andColumn.name());
        assertThat(andColumn.value().get(new TypeReference<List<ColumnCondition>>() {
                })).contains(condition1, condition2);

    }

    @Test
    public void shouldReturnErrorWhenCreateAndWithNullValues() {
        assertThrows(NullPointerException.class, () -> ColumnCondition.and((ColumnCondition[]) null));
    }


    @Test
    public void shouldReturnErrorWhenCreateOrWithNullValues() {
        assertThrows(NullPointerException.class, () -> ColumnCondition.or((ColumnCondition[]) null));
    }


    @Test
    public void shouldAppendAnd() {
        ColumnCondition eq = ColumnCondition.eq(Column.of("name", "otavio"));
        ColumnCondition gt = ColumnCondition.gt(Column.of("age", 10));
        ColumnCondition and = ColumnCondition.and(eq, gt);
        assertEquals(Condition.AND, and.condition());
        List<ColumnCondition> conditions = and.column().get(new TypeReference<>() {
        });
        assertThat(conditions).contains(eq, gt);
    }

    @Test
    public void shouldAppendOr() {
        ColumnCondition eq = ColumnCondition.eq(Column.of("name", "otavio"));
        ColumnCondition gt = ColumnCondition.gt(Column.of("age", 10));
        ColumnCondition and = ColumnCondition.or(eq, gt);
        assertEquals(Condition.OR, and.condition());
        List<ColumnCondition> conditions = and.column().get(new TypeReference<>() {
        });
        assertThat(conditions).contains(eq, gt);
    }

    @Test
    public void shouldAnd() {
        ColumnCondition eq = ColumnCondition.eq(Column.of("name", "otavio"));
        ColumnCondition gt = ColumnCondition.gt(Column.of("age", 10));
        ColumnCondition lte = ColumnCondition.lte(Column.of("salary", 10_000.00));

        ColumnCondition and = eq.and(gt);
        List<ColumnCondition> conditions = and.column().get(new TypeReference<>() {
        });
        assertEquals(Condition.AND, and.condition());
        assertThat(conditions).contains(eq, gt);
        ColumnCondition result = and.and(lte);

        assertEquals(Condition.AND, result.condition());
        assertThat(result.column().get(new TypeReference<List<ColumnCondition>>() {
        })).contains(eq, gt, lte);

    }

    @Test
    public void shouldOr() {
        ColumnCondition eq = ColumnCondition.eq(Column.of("name", "otavio"));
        ColumnCondition gt = ColumnCondition.gt(Column.of("age", 10));
        ColumnCondition lte = ColumnCondition.lte(Column.of("salary", 10_000.00));

        ColumnCondition or = eq.or(gt);
        List<ColumnCondition> conditions = or.column().get(new TypeReference<>() {
        });
        assertEquals(Condition.OR, or.condition());
        assertThat(conditions).contains(eq, gt);
        ColumnCondition result = or.or(lte);

        assertEquals(Condition.OR, result.condition());
        assertThat(result.column().get(new TypeReference<List<ColumnCondition>>() {
        })).contains(eq, gt, lte);

    }

    @Test
    public void shouldNegate() {
        ColumnCondition eq = ColumnCondition.eq(Column.of("name", "otavio"));
        ColumnCondition negate = eq.negate();
        assertEquals(Condition.NOT, negate.condition());
        ColumnCondition condition = negate.column().get(ColumnCondition.class);
        assertEquals(eq, condition);
    }

    @Test
    public void shouldAffirmDoubleNegate() {
        ColumnCondition eq = ColumnCondition.eq(Column.of("name", "otavio"));
        ColumnCondition affirm = eq.negate().negate();
        assertEquals(eq.condition(), affirm.condition());

    }

    @Test
    public void shouldReturnErrorWhenBetweenIsNull() {
        assertThrows(NullPointerException.class, () -> ColumnCondition.between(null));
    }

    @Test
    public void shouldReturnErrorWhenBetweenIsNotIterable() {
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
        assertEquals(Condition.BETWEEN, between.condition());
        Iterable<Integer> integers = between.column().get(new TypeReference<>() {
        });
        assertThat(integers).contains(12, 13);
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
        assertEquals(Condition.IN, in.condition());
        Iterable<Integer> integers = in.column().get(new TypeReference<>() {
        });
        assertThat(integers).contains(12, 13);
    }

}