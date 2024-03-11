/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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
 */
package org.eclipse.jnosql.communication;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class ValueUtilTest {

    @Test
    void shouldConvert() {
        Value value = Value.of(10);
        assertEquals(10, ValueUtil.convert(value));
    }

    @Test
    void shouldConvert2() {
        Value value = Value.of(Arrays.asList(10, 20));
        assertEquals(Arrays.asList(10, 20), ValueUtil.convert(value));
    }

    @Test
    void shouldConvert3() {
        Value value = Value.of(Arrays.asList(Value.of(10), Value.of(20)));
        assertEquals(Arrays.asList(10, 20), ValueUtil.convert(value));
    }

    @Test
    void shouldConvertList() {
        Value value = Value.of(10);
        assertEquals(Collections.singletonList(10), ValueUtil.convertToList(value));
    }

    @Test
    void shouldConvertList2() {
        Value value = Value.of(Arrays.asList(10, 20));
        assertEquals(Arrays.asList(10, 20), ValueUtil.convertToList(value));
    }


    @Test
    void shouldConvertList3() {
        Value value = Value.of(Arrays.asList(Value.of(10), Value.of(20)));
        assertEquals(Arrays.asList(10, 20), ValueUtil.convertToList(value));
    }

    @Test
    void shouldConvertNull() {
        Value value = Value.of(null);
        assertNull(ValueUtil.convert(value));
    }

    @Test
    void shouldReturnNullWhenValueUtilHasNullValue() {
        Object result = ValueUtil.convert(Value.ofNull());
        Assertions.assertNull(result);
    }
}