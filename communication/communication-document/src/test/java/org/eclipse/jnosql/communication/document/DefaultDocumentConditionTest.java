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

package org.eclipse.jnosql.communication.document;

import jakarta.nosql.Condition;
import jakarta.nosql.TypeReference;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCondition;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class DefaultDocumentConditionTest {


    @Test
    public void shouldReturnErrorWhenDocumentIsNull() {
        assertThrows(NullPointerException.class, () -> DefaultDocumentCondition.of(null, Condition.AND));
    }

    @Test
    public void shouldCreateAnInstance() {
        Document name = Document.of("name", "Otavio");
        DocumentCondition condition = DefaultDocumentCondition.of(name, Condition.EQUALS);
        assertNotNull(condition);
        assertEquals(name, condition.getDocument());
        assertEquals(Condition.EQUALS, condition.getCondition());
    }

    @Test
    public void shouldCreateNegationCondition() {
        Document age = Document.of("age", 26);
        DocumentCondition condition = DefaultDocumentCondition.of(age, Condition.GREATER_THAN);
        DocumentCondition negate = condition.negate();
        Document negateDocument = negate.getDocument();
        assertEquals(Condition.NOT, negate.getCondition());
        assertEquals(Condition.NOT.getNameField(), negateDocument.getName());
        assertEquals(DefaultDocumentCondition.of(age, Condition.GREATER_THAN), negateDocument.getValue().get());
    }

    @Test
    public void shouldReturnValidDoubleNegation() {
        Document age = Document.of("age", 26);
        DocumentCondition condition = DefaultDocumentCondition.of(age, Condition.GREATER_THAN);
        DocumentCondition affirmative = condition.negate().negate();
        Assertions.assertEquals(condition, affirmative);
    }

    @Test
    public void shouldCreateAndCondition() {
        Document age = Document.of("age", 26);
        Document name = Document.of("name", "Otavio");
        DocumentCondition condition1 = DefaultDocumentCondition.of(name, Condition.EQUALS);
        DocumentCondition condition2 = DefaultDocumentCondition.of(age, Condition.GREATER_THAN);

        DocumentCondition and = condition1.and(condition2);
        Document andDocument = and.getDocument();
        assertEquals(Condition.AND, and.getCondition());
        assertEquals(Condition.AND.getNameField(), andDocument.getName());
        assertThat(andDocument.getValue().get(new TypeReference<List<DocumentCondition>>() {
                }),
                Matchers.containsInAnyOrder(condition1, condition2));

    }

    @Test
    public void shouldCreateOrCondition() {
        Document age = Document.of("age", 26);
        Document name = Document.of("name", "Otavio");
        DocumentCondition condition1 = DefaultDocumentCondition.of(name, Condition.EQUALS);
        DocumentCondition condition2 = DefaultDocumentCondition.of(age, Condition.GREATER_THAN);

        DocumentCondition and = condition1.or(condition2);
        Document andDocument = and.getDocument();
        assertEquals(Condition.OR, and.getCondition());
        assertEquals(Condition.OR.getNameField(), andDocument.getName());
        assertThat(andDocument.getValue().get(new TypeReference<List<DocumentCondition>>() {
                }),
                Matchers.containsInAnyOrder(condition1, condition2));

    }

    @Test
    public void shouldReturnErrorWhenCreateAndWithNullValues() {
        assertThrows(NullPointerException.class, () -> DefaultDocumentCondition.and((DocumentCondition[]) null));
    }


    @Test
    public void shouldReturnErrorWhenCreateOrWithNullValues() {
        assertThrows(NullPointerException.class, () -> DefaultDocumentCondition.or((DocumentCondition[]) null));
    }


    @Test
    public void shouldAppendAnd() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition gt = DocumentCondition.gt(Document.of("age", 10));
        DocumentCondition and = DocumentCondition.and(eq, gt);
        assertEquals(Condition.AND, and.getCondition());
        List<DocumentCondition> conditions = and.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        });
        assertThat(conditions, Matchers.containsInAnyOrder(eq, gt));
    }

    @Test
    public void shouldAppendOr() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition gt = DocumentCondition.gt(Document.of("age", 10));
        DocumentCondition and = DocumentCondition.or(eq, gt);
        assertEquals(Condition.OR, and.getCondition());
        List<DocumentCondition> conditions = and.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        });
        assertThat(conditions, Matchers.containsInAnyOrder(eq, gt));
    }

    @Test
    public void shouldAnd() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition gt = DocumentCondition.gt(Document.of("age", 10));
        DocumentCondition lte = DocumentCondition.lte(Document.of("salary", 10_000.00));

        DocumentCondition and = eq.and(gt);
        List<DocumentCondition> conditions = and.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.AND, and.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(eq, gt));
        DocumentCondition result = and.and(lte);

        assertEquals(Condition.AND, result.getCondition());
        assertThat(result.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        }), Matchers.containsInAnyOrder(eq, gt, lte));

    }

    @Test
    public void shouldOr() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition gt = DocumentCondition.gt(Document.of("age", 10));
        DocumentCondition lte = DocumentCondition.lte(Document.of("salary", 10_000.00));

        DocumentCondition or = eq.or(gt);
        List<DocumentCondition> conditions = or.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.OR, or.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(eq, gt));
        DocumentCondition result = or.or(lte);

        assertEquals(Condition.OR, result.getCondition());
        assertThat(result.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        }), Matchers.containsInAnyOrder(eq, gt, lte));

    }

    @Test
    public void shouldNegate() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition negate = eq.negate();
        assertEquals(Condition.NOT, negate.getCondition());
        DocumentCondition condition = negate.getDocument().get(DocumentCondition.class);
        assertEquals(eq, condition);
    }

    @Test
    public void shouldAffirmDoubleNegate() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition affirm = eq.negate().negate();
        assertEquals(eq.getCondition(), affirm.getCondition());

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
        assertEquals(Condition.BETWEEN, between.getCondition());
        Iterable<Integer> integers = between.getDocument().get(new TypeReference<Iterable<Integer>>() {
        });
        assertThat(integers, contains(12, 13));
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
        assertEquals(Condition.IN, in.getCondition());
        Iterable<Integer> integers = in.getDocument().get(new TypeReference<Iterable<Integer>>() {
        });
        assertThat(integers, contains(12, 13));
    }
}