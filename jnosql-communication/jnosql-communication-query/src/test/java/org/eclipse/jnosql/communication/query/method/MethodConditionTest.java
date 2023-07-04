/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query.method;

import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.query.BooleanQueryValue;
import org.eclipse.jnosql.communication.query.QueryCondition;
import org.eclipse.jnosql.communication.query.QueryValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodConditionTest {
    private QueryValue<Boolean> queryValue;

    @BeforeEach
    public void setUp() {
        this.queryValue = BooleanQueryValue.TRUE;
    }

    @Test
    public void shouldCreateCondition() {
        QueryCondition condition = new MethodCondition("active", Condition.EQUALS, queryValue);
        assertThat(condition).isNotNull();
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("active", condition.name());
        assertEquals(condition.value(), queryValue);
    }

    @Test
    public void shouldEquals() {
        QueryCondition condition = new MethodCondition("active", Condition.EQUALS, queryValue);
        QueryCondition conditionB = new MethodCondition("active", Condition.EQUALS, queryValue);
        assertEquals(condition,conditionB);
    }

    @Test
    public void shouldHashCode() {
        QueryCondition condition = new MethodCondition("active", Condition.EQUALS, queryValue);
        QueryCondition conditionB = new MethodCondition("active", Condition.EQUALS, queryValue);
        assertEquals(condition.hashCode(),conditionB.hashCode());
    }

    @Test
    public void shouldCreateWithQueryParam(){
        QueryCondition condition = new MethodCondition("active", Condition.EQUALS);
        assertThat(condition).isNotNull();
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("active", condition.name());
        Assertions.assertThat(condition.value()).isInstanceOf(MethodParamQueryValue.class);
    }
}
