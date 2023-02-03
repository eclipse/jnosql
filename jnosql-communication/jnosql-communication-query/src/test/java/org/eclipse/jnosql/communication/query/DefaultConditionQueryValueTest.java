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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultConditionQueryValueTest {

    private QueryCondition condition;

    private QueryValue<Boolean> queryValue;

    @BeforeEach
    public void setUp() {
        this.queryValue = BooleanQueryValue.TRUE;
        this.condition = new DefaultQueryCondition("active", Condition.EQUALS, queryValue);
    }

    @Test
    public void shouldReturnType() {
        ConditionQueryValue conditionQueryValue = DefaultConditionQueryValue.of(List.of(condition));
        Assertions.assertThat(conditionQueryValue).isNotNull()
                .extracting(ConditionQueryValue::type)
                .isEqualTo(ValueType.CONDITION);
    }

    @Test
    public void shouldGet() {
        ConditionQueryValue conditionQueryValue = DefaultConditionQueryValue.of(List.of(condition));
        Assertions.assertThat(conditionQueryValue).isNotNull()
                .extracting(ConditionQueryValue::get)
                .isEqualTo(List.of(condition));
    }

    @Test
    public void shouldEquals(){
        ConditionQueryValue conditionQueryValue = DefaultConditionQueryValue.of(List.of(condition));
        assertEquals(conditionQueryValue,
                DefaultConditionQueryValue.of(List.of(condition)));
    }

    @Test
    public void shouldHashCode(){
        ConditionQueryValue conditionQueryValue = DefaultConditionQueryValue.of(List.of(condition));
        assertEquals(conditionQueryValue.hashCode(),
                DefaultConditionQueryValue.of(List.of(condition)).hashCode());
    }
}