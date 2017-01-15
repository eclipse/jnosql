/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.api.document;

import org.jnosql.diana.api.Condition;
import org.jnosql.diana.api.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.jnosql.diana.api.Condition.AND;
import static org.jnosql.diana.api.Condition.OR;
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

}