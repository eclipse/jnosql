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
package org.eclipse.jnosql.communication.semistructured;

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


class DefaultElementConditionTest {

    private final ColumnCondition lte = ColumnCondition.lte(Element.of("salary", 10.32));

    @Test
    void shouldReturnErrorWhenColumnIsNull() {
        assertThrows(NullPointerException.class, () -> ColumnCondition.of(null, Condition.AND));
    }

    @Test
    void shouldCreateAnInstance() {
        Element name = Element.of("name", "Otavio");
        ColumnCondition condition = ColumnCondition.of(name, Condition.EQUALS);
        assertNotNull(condition);
        assertEquals(name, condition.column());
        assertEquals(Condition.EQUALS, condition.condition());
    }

    @Test
    void shouldCreateNegationCondition() {
        Element age = Element.of("age", 26);
        ColumnCondition condition = ColumnCondition.of(age, Condition.GREATER_THAN);
        ColumnCondition negate = condition.negate();
        Element negateElement = negate.column();
        assertEquals(Condition.NOT, negate.condition());
        assertEquals(Condition.NOT.getNameField(), negateElement.name());
        assertEquals(ColumnCondition.of(age, Condition.GREATER_THAN), negateElement.value().get());
    }

    @Test
    void shouldReturnValidDoubleNegation() {
        Element age = Element.of("age", 26);
        ColumnCondition condition = ColumnCondition.of(age, Condition.GREATER_THAN);
        ColumnCondition affirmative = condition.negate().negate();
        Assertions.assertEquals(condition, affirmative);
    }

    @Test
    void shouldCreateAndCondition() {
        Element age = Element.of("age", 26);
        Element name = Element.of("name", "Otavio");
        ColumnCondition condition1 = ColumnCondition.of(name, Condition.EQUALS);
        ColumnCondition condition2 = ColumnCondition.of(age, Condition.GREATER_THAN);

        ColumnCondition and = condition1.and(condition2);
        Element andElement = and.column();
        assertEquals(Condition.AND, and.condition());
        assertEquals(Condition.AND.getNameField(), andElement.name());
        assertThat(andElement.value().get(new TypeReference<List<ColumnCondition>>() {
                })).contains(condition1, condition2);

    }

    @Test
    void shouldCreateOrCondition() {
        Element age = Element.of("age", 26);
        Element name = Element.of("name", "Otavio");
        ColumnCondition condition1 = ColumnCondition.of(name, Condition.EQUALS);
        ColumnCondition condition2 = ColumnCondition.of(age, Condition.GREATER_THAN);

        ColumnCondition and = condition1.or(condition2);
        Element andElement = and.column();
        assertEquals(Condition.OR, and.condition());
        assertEquals(Condition.OR.getNameField(), andElement.name());
        assertThat(andElement.value().get(new TypeReference<List<ColumnCondition>>() {
                })).contains(condition1, condition2);

    }

    @Test
    void shouldReturnErrorWhenCreateAndWithNullValues() {
        assertThrows(NullPointerException.class, () -> ColumnCondition.and((ColumnCondition[]) null));
    }


    @Test
    void shouldReturnErrorWhenCreateOrWithNullValues() {
        assertThrows(NullPointerException.class, () -> ColumnCondition.or((ColumnCondition[]) null));
    }


    @Test
    void shouldAppendAnd() {
        ColumnCondition eq = ColumnCondition.eq(Element.of("name", "otavio"));
        ColumnCondition gt = ColumnCondition.gt(Element.of("age", 10));
        ColumnCondition and = ColumnCondition.and(eq, gt);
        assertEquals(Condition.AND, and.condition());
        List<ColumnCondition> conditions = and.column().get(new TypeReference<>() {
        });
        assertThat(conditions).contains(eq, gt);
    }

    @Test
    void shouldAppendOr() {
        ColumnCondition eq = ColumnCondition.eq(Element.of("name", "otavio"));
        ColumnCondition gt = ColumnCondition.gt(Element.of("age", 10));
        ColumnCondition and = ColumnCondition.or(eq, gt);
        assertEquals(Condition.OR, and.condition());
        List<ColumnCondition> conditions = and.column().get(new TypeReference<>() {
        });
        assertThat(conditions).contains(eq, gt);
    }

    @Test
    void shouldAnd() {
        ColumnCondition eq = ColumnCondition.eq(Element.of("name", "otavio"));
        ColumnCondition gt = ColumnCondition.gt(Element.of("age", 10));
        ColumnCondition lte = ColumnCondition.lte(Element.of("salary", 10_000.00));

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
    void shouldOr() {
        ColumnCondition eq = ColumnCondition.eq(Element.of("name", "otavio"));
        ColumnCondition gt = ColumnCondition.gt(Element.of("age", 10));
        ColumnCondition lte = ColumnCondition.lte(Element.of("salary", 10_000.00));

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
    void shouldNegate() {
        ColumnCondition eq = ColumnCondition.eq(Element.of("name", "otavio"));
        ColumnCondition negate = eq.negate();
        assertEquals(Condition.NOT, negate.condition());
        ColumnCondition condition = negate.column().get(ColumnCondition.class);
        assertEquals(eq, condition);
    }

    @Test
    void shouldAffirmDoubleNegate() {
        ColumnCondition eq = ColumnCondition.eq(Element.of("name", "otavio"));
        ColumnCondition affirm = eq.negate().negate();
        assertEquals(eq.condition(), affirm.condition());

    }

    @Test
    void shouldReturnErrorWhenBetweenIsNull() {
        assertThrows(NullPointerException.class, () -> ColumnCondition.between(null));
    }

    @Test
    void shouldReturnErrorWhenBetweenIsNotIterable() {
        assertThrows(IllegalArgumentException.class, () -> {
            Element element = Element.of("age", 12);
            ColumnCondition.between(element);
        });
    }

    @Test
    void shouldReturnErrorWhenIterableHasOneElement() {
        assertThrows(IllegalArgumentException.class, () -> {
            Element element = Element.of("age", Collections.singleton(12));
            ColumnCondition.between(element);
        });
    }

    @Test
    void shouldReturnErrorWhenIterableHasMoreThanTwoElement2() {
        assertThrows(IllegalArgumentException.class, () -> {
            Element element = Element.of("age", Arrays.asList(12, 12, 12));
            ColumnCondition.between(element);
        });
    }

    @Test
    void shouldReturnBetween() {
        Element element = Element.of("age", Arrays.asList(12, 13));
        ColumnCondition between = ColumnCondition.between(element);
        assertEquals(Condition.BETWEEN, between.condition());
        Iterable<Integer> integers = between.column().get(new TypeReference<>() {
        });
        assertThat(integers).contains(12, 13);
    }

    @Test
    void shouldReturnErrorWhenInConditionIsInvalid() {
        assertThrows(NullPointerException.class, () -> ColumnCondition.in(null));
        assertThrows(IllegalArgumentException.class, () -> ColumnCondition.in(Element.of("value", 10)));
    }

    @Test
    void shouldReturnInClause() {
        Element element = Element.of("age", Arrays.asList(12, 13));
        ColumnCondition in = ColumnCondition.in(element);
        assertEquals(Condition.IN, in.condition());
        Iterable<Integer> integers = in.column().get(new TypeReference<>() {
        });
        assertThat(integers).contains(12, 13);
    }

}