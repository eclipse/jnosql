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
package org.jnosql.diana.api.column;

import org.hamcrest.Matchers;
import org.jnosql.diana.api.Condition;
import org.jnosql.diana.api.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.jnosql.diana.api.Condition.AND;
import static org.jnosql.diana.api.Condition.OR;
import static org.junit.Assert.*;


public class DefaultColumnConditionTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenColumnIsNull() {
        DefaultColumnCondition.of(null, AND);
    }

    @Test
    public void shouldCreateAnInstance() {
        Column name = Column.of("name", "Otavio");
        ColumnCondition condition = DefaultColumnCondition.of(name, Condition.EQUALS);
        Assert.assertNotNull(condition);
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
        assertEquals(AND, and.getCondition());
        assertEquals(AND.getNameField(), andColumn.getName());
        assertThat(andColumn.getValue().get(new TypeReference<List<ColumnCondition>>() {}),
                containsInAnyOrder(condition1, condition2));

    }

    @Test
    public void shouldCreateOrCondition() {
        Column age = Column.of("age", 26);
        Column name = Column.of("name", "Otavio");
        ColumnCondition condition1 = DefaultColumnCondition.of(name, Condition.EQUALS);
        ColumnCondition condition2 = DefaultColumnCondition.of(age, Condition.GREATER_THAN);

        ColumnCondition and = condition1.or(condition2);
        Column andColumn = and.getColumn();
        assertEquals(OR, and.getCondition());
        assertEquals(OR.getNameField(), andColumn.getName());
        assertThat(andColumn.getValue().get(new TypeReference<List<ColumnCondition>>() {}),
                containsInAnyOrder(condition1, condition2));

    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenCreateAndWithNullValues() {
        DefaultColumnCondition.and((ColumnCondition[]) null);
    }


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenCreateOrWithNullValues() {
        DefaultColumnCondition.or((ColumnCondition[])null);
    }

}