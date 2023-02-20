/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
 *   Maximillian Arruda
 *
 */

package org.eclipse.jnosql.communication;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DefaultValueTest extends ValueTest {

    @Override
    Value getValueOf(Supplier<?> supplier) {
        return Value.of(supplier.get());
    }

    @Test
    void shouldReturnErrorWhenElementIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> Value.of(null));
    }

    @Test
    void shouldBeEquals() {
        Value left = Value.of("12");
        Value right = Value.of("12");
        Assertions.assertTrue(left.equals(right));
        Assertions.assertTrue(left.equals(left));
    }

    @Test
    void shouldBeNotEquals() {
        Value left = Value.of("12");
        Value right = Value.of("13");
        Assertions.assertFalse(left.equals(right));
        Assertions.assertFalse(left.equals(null));
        Assertions.assertFalse(left.equals(new Object()));
    }

    @Test
    void testHashCode() {
        String wrappedValue = "12";
        Value targetValue = Value.of(wrappedValue);
        assertEquals(Objects.hashCode(wrappedValue), targetValue.hashCode(),
                "DefaultValue hash code should be conditioned to its value attribute's hashCode");
    }

    @Test
    void testToString() {
        String wrappedValue = "12";
        Value targetValue = Value.of(wrappedValue);
        assertEquals("DefaultValue{value=" + wrappedValue + "}", targetValue.toString());
    }

}