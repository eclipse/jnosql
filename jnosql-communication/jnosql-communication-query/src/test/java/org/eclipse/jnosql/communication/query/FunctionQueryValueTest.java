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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FunctionQueryValueTest {

    private Function function;

    @BeforeEach
    public void setUp() {
        this.function = DefaultFunction.of("name", new Object[]{1, 2});
    }

    @Test
    public void shouldReturnFunctionType() {
        FunctionQueryValue queryValue = new FunctionQueryValue(function);
        assertThat(queryValue).isNotNull();
        assertThat(queryValue).extracting(FunctionQueryValue::type)
                .isEqualTo(ValueType.FUNCTION);
    }

    @Test
    public void shouldReturnValue() {
        FunctionQueryValue queryValue = new FunctionQueryValue(function);
        assertThat(queryValue).isNotNull();
        assertThat(queryValue).extracting(FunctionQueryValue::get)
                .isEqualTo(function);
    }

    @Test
    public void shouldEquals() {
        FunctionQueryValue queryValue = new FunctionQueryValue(function);
        assertEquals(queryValue, new FunctionQueryValue(function));
    }


    @Test
    public void shouldHashCode() {
        FunctionQueryValue queryValue = new FunctionQueryValue(function);
        assertEquals(queryValue.hashCode(), new FunctionQueryValue(function).hashCode());
    }
}