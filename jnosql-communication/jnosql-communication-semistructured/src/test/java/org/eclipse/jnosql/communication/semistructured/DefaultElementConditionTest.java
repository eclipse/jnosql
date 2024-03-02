/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
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

    private final CriteriaCondition lte = CriteriaCondition.lte(Element.of("salary", 10.32));

    @Test
    void shouldReturnErrorWhenColumnIsNull() {
        assertThrows(NullPointerException.class, () -> CriteriaCondition.of(null, Condition.AND));
    }

    @Test
    void shouldCreateAnInstance() {
        Element name = Element.of("name", "Otavio");
        CriteriaCondition condition = CriteriaCondition.of(name, Condition.EQUALS);
        assertNotNull(condition);
        assertEquals(name, condition.element());
        assertEquals(Condition.EQUALS, condition.condition());
    }

    @Test
    void shouldCreateNegationCondition() {
        Element age = Element.of("age", 26);
        CriteriaCondition condition = CriteriaCondition.of(age, Condition.GREATER_THAN);
        CriteriaCondition negate = condition.negate();
        Element negateElement = negate.element();
        assertEquals(Condition.NOT, negate.condition());
        assertEquals(Condition.NOT.getNameField(), negateElement.name());
        assertEquals(CriteriaCondition.of(age, Condition.GREATER_THAN), negateElement.value().get());
    }

    @Test
    void shouldReturnValidDoubleNegation() {
        Element age = Element.of("age", 26);
        CriteriaCondition condition = CriteriaCondition.of(age, Condition.GREATER_THAN);
        CriteriaCondition affirmative = condition.negate().negate();
        Assertions.assertEquals(condition, affirmative);
    }

    @Test
    void shouldCreateAndCondition() {
        Element age = Element.of("age", 26);
        Element name = Element.of("name", "Otavio");
        CriteriaCondition condition1 = CriteriaCondition.of(name, Condition.EQUALS);
        CriteriaCondition condition2 = CriteriaCondition.of(age, Condition.GREATER_THAN);

        CriteriaCondition and = condition1.and(condition2);
        Element andElement = and.element();
        assertEquals(Condition.AND, and.condition());
        assertEquals(Condition.AND.getNameField(), andElement.name());
        assertThat(andElement.value().get(new TypeReference<List<CriteriaCondition>>() {
                })).contains(condition1, condition2);

    }

    @Test
    void shouldCreateOrCondition() {
        Element age = Element.of("age", 26);
        Element name = Element.of("name", "Otavio");
        CriteriaCondition condition1 = CriteriaCondition.of(name, Condition.EQUALS);
        CriteriaCondition condition2 = CriteriaCondition.of(age, Condition.GREATER_THAN);

        CriteriaCondition and = condition1.or(condition2);
        Element andElement = and.element();
        assertEquals(Condition.OR, and.condition());
        assertEquals(Condition.OR.getNameField(), andElement.name());
        assertThat(andElement.value().get(new TypeReference<List<CriteriaCondition>>() {
                })).contains(condition1, condition2);

    }

    @Test
    void shouldReturnErrorWhenCreateAndWithNullValues() {
        assertThrows(NullPointerException.class, () -> CriteriaCondition.and((CriteriaCondition[]) null));
    }


    @Test
    void shouldReturnErrorWhenCreateOrWithNullValues() {
        assertThrows(NullPointerException.class, () -> CriteriaCondition.or((CriteriaCondition[]) null));
    }


    @Test
    void shouldAppendAnd() {
        CriteriaCondition eq = CriteriaCondition.eq(Element.of("name", "otavio"));
        CriteriaCondition gt = CriteriaCondition.gt(Element.of("age", 10));
        CriteriaCondition and = CriteriaCondition.and(eq, gt);
        assertEquals(Condition.AND, and.condition());
        List<CriteriaCondition> conditions = and.element().get(new TypeReference<>() {
        });
        assertThat(conditions).contains(eq, gt);
    }

    @Test
    void shouldAppendOr() {
        CriteriaCondition eq = CriteriaCondition.eq(Element.of("name", "otavio"));
        CriteriaCondition gt = CriteriaCondition.gt(Element.of("age", 10));
        CriteriaCondition and = CriteriaCondition.or(eq, gt);
        assertEquals(Condition.OR, and.condition());
        List<CriteriaCondition> conditions = and.element().get(new TypeReference<>() {
        });
        assertThat(conditions).contains(eq, gt);
    }

    @Test
    void shouldAnd() {
        CriteriaCondition eq = CriteriaCondition.eq(Element.of("name", "otavio"));
        CriteriaCondition gt = CriteriaCondition.gt(Element.of("age", 10));
        CriteriaCondition lte = CriteriaCondition.lte(Element.of("salary", 10_000.00));

        CriteriaCondition and = eq.and(gt);
        List<CriteriaCondition> conditions = and.element().get(new TypeReference<>() {
        });
        assertEquals(Condition.AND, and.condition());
        assertThat(conditions).contains(eq, gt);
        CriteriaCondition result = and.and(lte);

        assertEquals(Condition.AND, result.condition());
        assertThat(result.element().get(new TypeReference<List<CriteriaCondition>>() {
        })).contains(eq, gt, lte);

    }

    @Test
    void shouldOr() {
        CriteriaCondition eq = CriteriaCondition.eq(Element.of("name", "otavio"));
        CriteriaCondition gt = CriteriaCondition.gt(Element.of("age", 10));
        CriteriaCondition lte = CriteriaCondition.lte(Element.of("salary", 10_000.00));

        CriteriaCondition or = eq.or(gt);
        List<CriteriaCondition> conditions = or.element().get(new TypeReference<>() {
        });
        assertEquals(Condition.OR, or.condition());
        assertThat(conditions).contains(eq, gt);
        CriteriaCondition result = or.or(lte);

        assertEquals(Condition.OR, result.condition());
        assertThat(result.element().get(new TypeReference<List<CriteriaCondition>>() {
        })).contains(eq, gt, lte);

    }

    @Test
    void shouldNegate() {
        CriteriaCondition eq = CriteriaCondition.eq(Element.of("name", "otavio"));
        CriteriaCondition negate = eq.negate();
        assertEquals(Condition.NOT, negate.condition());
        CriteriaCondition condition = negate.element().get(CriteriaCondition.class);
        assertEquals(eq, condition);
    }

    @Test
    void shouldAffirmDoubleNegate() {
        CriteriaCondition eq = CriteriaCondition.eq(Element.of("name", "otavio"));
        CriteriaCondition affirm = eq.negate().negate();
        assertEquals(eq.condition(), affirm.condition());

    }

    @Test
    void shouldReturnErrorWhenBetweenIsNull() {
        assertThrows(NullPointerException.class, () -> CriteriaCondition.between(null));
    }

    @Test
    void shouldReturnErrorWhenBetweenIsNotIterable() {
        assertThrows(IllegalArgumentException.class, () -> {
            Element element = Element.of("age", 12);
            CriteriaCondition.between(element);
        });
    }

    @Test
    void shouldReturnErrorWhenIterableHasOneElement() {
        assertThrows(IllegalArgumentException.class, () -> {
            Element element = Element.of("age", Collections.singleton(12));
            CriteriaCondition.between(element);
        });
    }

    @Test
    void shouldReturnErrorWhenIterableHasMoreThanTwoElement2() {
        assertThrows(IllegalArgumentException.class, () -> {
            Element element = Element.of("age", Arrays.asList(12, 12, 12));
            CriteriaCondition.between(element);
        });
    }

    @Test
    void shouldReturnBetween() {
        Element element = Element.of("age", Arrays.asList(12, 13));
        CriteriaCondition between = CriteriaCondition.between(element);
        assertEquals(Condition.BETWEEN, between.condition());
        Iterable<Integer> integers = between.element().get(new TypeReference<>() {
        });
        assertThat(integers).contains(12, 13);
    }

    @Test
    void shouldReturnErrorWhenInConditionIsInvalid() {
        assertThrows(NullPointerException.class, () -> CriteriaCondition.in(null));
        assertThrows(IllegalArgumentException.class, () -> CriteriaCondition.in(Element.of("value", 10)));
    }

    @Test
    void shouldReturnInClause() {
        Element element = Element.of("age", Arrays.asList(12, 13));
        CriteriaCondition in = CriteriaCondition.in(element);
        assertEquals(Condition.IN, in.condition());
        Iterable<Integer> integers = in.element().get(new TypeReference<>() {
        });
        assertThat(integers).contains(12, 13);
    }

}