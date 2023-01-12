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

package org.eclipse.jnosql.communication.document;

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


public class DocumentConditionTest {


    @Test
    public void shouldReturnErrorWhenDocumentIsNull() {
        assertThrows(NullPointerException.class, () -> DocumentCondition.of(null, Condition.AND));
    }

    @Test
    public void shouldCreateAnInstance() {
        Document name = Document.of("name", "Otavio");
        DocumentCondition condition = DocumentCondition.of(name, Condition.EQUALS);
        assertNotNull(condition);
        assertEquals(name, condition.document());
        assertEquals(Condition.EQUALS, condition.condition());
    }

    @Test
    public void shouldCreateNegationCondition() {
        Document age = Document.of("age", 26);
        DocumentCondition condition = DocumentCondition.of(age, Condition.GREATER_THAN);
        DocumentCondition negate = condition.negate();
        Document negateDocument = negate.document();
        assertEquals(Condition.NOT, negate.condition());
        assertEquals(Condition.NOT.getNameField(), negateDocument.name());
        assertEquals(DocumentCondition.of(age, Condition.GREATER_THAN), negateDocument.value().get());
    }

    @Test
    public void shouldReturnValidDoubleNegation() {
        Document age = Document.of("age", 26);
        DocumentCondition condition = DocumentCondition.of(age, Condition.GREATER_THAN);
        DocumentCondition affirmative = condition.negate().negate();
        Assertions.assertEquals(condition, affirmative);
    }

    @Test
    public void shouldCreateAndCondition() {
        Document age = Document.of("age", 26);
        Document name = Document.of("name", "Otavio");
        DocumentCondition condition1 = DocumentCondition.of(name, Condition.EQUALS);
        DocumentCondition condition2 = DocumentCondition.of(age, Condition.GREATER_THAN);

        DocumentCondition and = condition1.and(condition2);
        Document andDocument = and.document();
        assertEquals(Condition.AND, and.condition());
        assertEquals(Condition.AND.getNameField(), andDocument.name());
        assertThat(andDocument.value().get(new TypeReference<List<DocumentCondition>>() {
                })).contains(condition1, condition2);

    }

    @Test
    public void shouldCreateOrCondition() {
        Document age = Document.of("age", 26);
        Document name = Document.of("name", "Otavio");
        DocumentCondition condition1 = DocumentCondition.of(name, Condition.EQUALS);
        DocumentCondition condition2 = DocumentCondition.of(age, Condition.GREATER_THAN);

        DocumentCondition and = condition1.or(condition2);
        Document andDocument = and.document();
        assertEquals(Condition.OR, and.condition());
        assertEquals(Condition.OR.getNameField(), andDocument.name());
        assertThat(andDocument.value().get(new TypeReference<List<DocumentCondition>>() {
                })).contains(condition1, condition2);

    }

    @Test
    public void shouldReturnErrorWhenCreateAndWithNullValues() {
        assertThrows(NullPointerException.class, () -> DocumentCondition.and((DocumentCondition[]) null));
    }


    @Test
    public void shouldReturnErrorWhenCreateOrWithNullValues() {
        assertThrows(NullPointerException.class, () -> DocumentCondition.or((DocumentCondition[]) null));
    }


    @Test
    public void shouldAppendAnd() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition gt = DocumentCondition.gt(Document.of("age", 10));
        DocumentCondition and = DocumentCondition.and(eq, gt);
        assertEquals(Condition.AND, and.condition());
        List<DocumentCondition> conditions = and.document().get(new TypeReference<>() {
        });
        assertThat(conditions).contains(eq, gt);
    }

    @Test
    public void shouldAppendOr() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition gt = DocumentCondition.gt(Document.of("age", 10));
        DocumentCondition and = DocumentCondition.or(eq, gt);
        assertEquals(Condition.OR, and.condition());
        List<DocumentCondition> conditions = and.document().get(new TypeReference<>() {
        });
        assertThat(conditions).contains(eq, gt);
    }

    @Test
    public void shouldAnd() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition gt = DocumentCondition.gt(Document.of("age", 10));
        DocumentCondition lte = DocumentCondition.lte(Document.of("salary", 10_000.00));

        DocumentCondition and = eq.and(gt);
        List<DocumentCondition> conditions = and.document().get(new TypeReference<>() {
        });
        assertEquals(Condition.AND, and.condition());
        assertThat(conditions).contains(eq, gt);
        DocumentCondition result = and.and(lte);

        assertEquals(Condition.AND, result.condition());
        assertThat(result.document().get(new TypeReference<List<DocumentCondition>>() {
        })).contains(eq, gt, lte);

    }

    @Test
    public void shouldOr() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition gt = DocumentCondition.gt(Document.of("age", 10));
        DocumentCondition lte = DocumentCondition.lte(Document.of("salary", 10_000.00));

        DocumentCondition or = eq.or(gt);
        List<DocumentCondition> conditions = or.document().get(new TypeReference<>() {
        });
        assertEquals(Condition.OR, or.condition());
        assertThat(conditions).contains(eq, gt);
        DocumentCondition result = or.or(lte);

        assertEquals(Condition.OR, result.condition());
        assertThat(result.document().get(new TypeReference<List<DocumentCondition>>() {
        })).contains(eq, gt, lte);

    }

    @Test
    public void shouldNegate() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition negate = eq.negate();
        assertEquals(Condition.NOT, negate.condition());
        DocumentCondition condition = negate.document().get(DocumentCondition.class);
        assertEquals(eq, condition);
    }

    @Test
    public void shouldAffirmDoubleNegate() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition affirm = eq.negate().negate();
        assertEquals(eq.condition(), affirm.condition());

    }

    @Test
    public void shouldReturnErrorWhenBetweenIsNull() {
        assertThrows(NullPointerException.class, () -> DocumentCondition.between(null));
    }

    @Test
    public void shouldReturnErrorWhenBetweenIsNotIterable() {
        assertThrows(IllegalArgumentException.class, () -> {
            Document document = Document.of("age", 12);
            DocumentCondition.between(document);
        });
    }

    @Test
    public void shouldReturnErrorWhenIterableHasOneElement() {
        assertThrows(IllegalArgumentException.class, () -> {
            Document document = Document.of("age", Collections.singleton(12));
            DocumentCondition.between(document);
        });
    }

    @Test
    public void shouldReturnErrorWhenIterableHasMoreThanTwoElement2() {
        assertThrows(IllegalArgumentException.class, () -> {
            Document document = Document.of("age", Arrays.asList(12, 12, 12));
            DocumentCondition.between(document);
        });
    }

    @Test
    public void shouldReturnBetween() {
        Document document = Document.of("age", Arrays.asList(12, 13));
        DocumentCondition between = DocumentCondition.between(document);
        assertEquals(Condition.BETWEEN, between.condition());
        Iterable<Integer> integers = between.document().get(new TypeReference<>() {
        });
        assertThat(integers).contains(12, 13);
    }

    @Test
    public void shouldReturnErrorWhenInConditionIsInvalid() {
        assertThrows(NullPointerException.class, () -> DocumentCondition.in(null));
        assertThrows(IllegalArgumentException.class, () -> DocumentCondition.in(Document.of("value", 10)));
    }

    @Test
    public void shouldReturnInClause() {
        Document column = Document.of("age", Arrays.asList(12, 13));
        DocumentCondition in = DocumentCondition.in(column);
        assertEquals(Condition.IN, in.condition());
        Iterable<Integer> integers = in.document().get(new TypeReference<>() {
        });
        assertThat(integers).contains(12, 13);
    }
}