/*
 * Copyright 2017 Otavio Santana and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package org.jnosql.diana.api.document;

import org.jnosql.diana.api.Condition;
import org.jnosql.diana.api.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.jnosql.diana.api.Condition.AND;
import static org.jnosql.diana.api.Condition.OR;
import static org.jnosql.diana.api.Condition.SUBQUERY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class DefaultDocumentConditionTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenDocumentIsNull() {
        DefaultDocumentCondition.of(null, AND);
    }

    @Test
    public void shouldCreateAnInstance() {
        Document name = Document.of("name", "Otavio");
        DocumentCondition condition = DefaultDocumentCondition.of(name, Condition.EQUALS);
        Assert.assertNotNull(condition);
        assertEquals(name, condition.getDocument());
        assertEquals(Condition.EQUALS, condition.getCondition());
    }

    @Test
    public void shouldCreateNegationConditon() {
        Document age = Document.of("age", 26);
        DocumentCondition condition = DefaultDocumentCondition.of(age, Condition.GREATER_THAN);
        DocumentCondition negate = condition.negate();
        Document negateDocument = negate.getDocument();
        assertEquals(Condition.NOT, negate.getCondition());
        assertEquals(Condition.NOT.getNameField(), negateDocument.getName());
        assertEquals(DefaultDocumentCondition.of(age, Condition.GREATER_THAN), negateDocument.getValue().get());
    }


    @Test
    public void shouldCreateAndCondition() {
        Document age = Document.of("age", 26);
        Document name = Document.of("name", "Otavio");
        DocumentCondition condition1 = DefaultDocumentCondition.of(name, Condition.EQUALS);
        DocumentCondition condition2 = DefaultDocumentCondition.of(age, Condition.GREATER_THAN);

        DocumentCondition and = condition1.and(condition2);
        Document andDocument = and.getDocument();
        assertEquals(AND, and.getCondition());
        assertEquals(AND.getNameField(), andDocument.getName());
        assertThat(andDocument.getValue().get(new TypeReference<List<DocumentCondition>>() {}),
                containsInAnyOrder(condition1, condition2));

    }

    @Test
    public void shouldCreateOrCondition() {
        Document age = Document.of("age", 26);
        Document name = Document.of("name", "Otavio");
        DocumentCondition condition1 = DefaultDocumentCondition.of(name, Condition.EQUALS);
        DocumentCondition condition2 = DefaultDocumentCondition.of(age, Condition.GREATER_THAN);

        DocumentCondition and = condition1.or(condition2);
        Document andDocument = and.getDocument();
        assertEquals(OR, and.getCondition());
        assertEquals(OR.getNameField(), andDocument.getName());
        assertThat(andDocument.getValue().get(new TypeReference<List<DocumentCondition>>() {}),
                containsInAnyOrder(condition1, condition2));

    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenCreateAndWithNullValues() {
        DefaultDocumentCondition.and((DocumentCondition[]) null);
    }


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenCreateOrWithNullValues() {
        DefaultDocumentCondition.or((DocumentCondition[])null);
    }

    //
    @Test
    public void shouldAppendAnd() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition gt = DocumentCondition.gt(Document.of("age", 10));
        DocumentCondition and = DocumentCondition.and(eq, gt);
        assertEquals(AND, and.getCondition());
        List<DocumentCondition> conditions = and.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        });
        assertThat(conditions, containsInAnyOrder(eq, gt));
    }

    @Test
    public void shouldAppendOr() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition gt = DocumentCondition.gt(Document.of("age", 10));
        DocumentCondition and = DocumentCondition.or(eq, gt);
        assertEquals(OR, and.getCondition());
        List<DocumentCondition> conditions = and.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        });
        assertThat(conditions, containsInAnyOrder(eq, gt));
    }

    @Test
    public void shouldAnd() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition gt = DocumentCondition.gt(Document.of("age", 10));
        DocumentCondition lte = DocumentCondition.lte(Document.of("salary", 10_000.00));

        DocumentCondition and = eq.and(gt);
        List<DocumentCondition> conditions = and.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(AND, and.getCondition());
        assertThat(conditions, containsInAnyOrder(eq, gt));
        DocumentCondition result = and.and(lte);

        assertEquals(AND, result.getCondition());
        assertThat(result.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        }), containsInAnyOrder(eq, gt, lte));

    }

    @Test
    public void shouldOr() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition gt = DocumentCondition.gt(Document.of("age", 10));
        DocumentCondition lte = DocumentCondition.lte(Document.of("salary", 10_000.00));

        DocumentCondition or = eq.or(gt);
        List<DocumentCondition> conditions = or.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(OR, or.getCondition());
        assertThat(conditions, containsInAnyOrder(eq, gt));
        DocumentCondition result = or.or(lte);

        assertEquals(OR, result.getCondition());
        assertThat(result.getDocument().get(new TypeReference<List<DocumentCondition>>() {
        }), containsInAnyOrder(eq, gt, lte));

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
    public void shouldAfirmeDoubleNegate() {
        DocumentCondition eq = DocumentCondition.eq(Document.of("name", "otavio"));
        DocumentCondition afirmative = eq.negate().negate();
        assertEquals(eq.getCondition(), afirmative.getCondition());

    }

    @Test
    public void shouldCreateSubQuery() {
        DocumentQuery query = DocumentQuery.of("query");
        DocumentCondition subquery = DocumentCondition.subquery(query);
        Document document = subquery.getDocument();
        assertEquals(Condition.SUBQUERY, subquery.getCondition());
        assertEquals(SUBQUERY.getNameField(), document.getName());
        assertEquals(query,document.get(DocumentQuery.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErroWhenBetweenIsNull() {
        DocumentCondition.between(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnErroWhenBetweenIsNotIterable() {
        Document document = Document.of("age", 12);
        DocumentCondition.between(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnErrorWhenIterableHasOneElement() {
        Document document = Document.of("age", Collections.singleton(12));
        DocumentCondition.between(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnErrorWhenIterableHasMoreThanTwoElement2() {
        Document document = Document.of("age", Arrays.asList(12, 12, 12));
        DocumentCondition.between(document);
    }

    @Test
    public void shouldReturnBetween() {
        Document document = Document.of("age", Arrays.asList(12, 13));
        DocumentCondition between = DocumentCondition.between(document);
        assertEquals(Condition.BETWEEN, between.getCondition());
        Iterable<Integer> integers = between.getDocument().get(new TypeReference<Iterable<Integer>>() {
        });
        Assert.assertThat(integers, contains(12, 13));
    }

}