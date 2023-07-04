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
package org.eclipse.jnosql.communication.query;

import org.eclipse.jnosql.communication.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultQueryConditionTest {
    private QueryValue<Boolean> queryValue;

    @BeforeEach
    public void setUp() {
        this.queryValue = BooleanQueryValue.TRUE;
    }

    @Test
    void shouldCreateCondition() {
        QueryCondition condition = new DefaultQueryCondition("active", Condition.EQUALS, queryValue);
        assertThat(condition).isNotNull();
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("active", condition.name());
        assertEquals(condition.value(), queryValue);
    }

    @Test
    void shouldEquals() {
        QueryCondition condition = new DefaultQueryCondition("active", Condition.EQUALS, queryValue);
        QueryCondition conditionB = new DefaultQueryCondition("active", Condition.EQUALS, queryValue);
        assertEquals(condition,conditionB);
    }

    @Test
    void shouldHashCode() {
        QueryCondition condition = new DefaultQueryCondition("active", Condition.EQUALS, queryValue);
        QueryCondition conditionB = new DefaultQueryCondition("active", Condition.EQUALS, queryValue);
        assertEquals(condition.hashCode(),conditionB.hashCode());
    }
}
