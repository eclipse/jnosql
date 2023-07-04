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

import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.communication.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WhereTest {
    private QueryCondition condition;

    private QueryValue<Boolean> queryValue;

    @BeforeEach
    public void setUp() {
        this.queryValue = BooleanQueryValue.TRUE;
        condition = new DefaultQueryCondition("active", Condition.EQUALS, queryValue);
    }

    @Test
    void shouldCreateInstance() {
        Where where = Where.of(condition);
        Assertions.assertThat(where).isNotNull()
                .extracting(Where::condition)
                .isEqualTo(condition);
    }

    @Test
    void shouldEquals() {
        Where where = Where.of(condition);
        assertEquals(where, where, "should be equals to yourself");
        assertEquals(where, Where.of(condition));
        assertNotEquals(where, new Object(), "should be not equal to an instance of any other type");
        assertNotEquals(null, where, "should be not equal to null reference");
    }


    @Test
    void shouldHashCode() {
        Where where = Where.of(condition);
        assertEquals(where.hashCode(), Where.of(condition).hashCode());
    }

    @Test
    void shouldToString() {
        Where where = Where.of(condition);
        String actual = where.toString();
        assertNotNull(actual);
        assertTrue(actual.startsWith("where "));
    }
}
