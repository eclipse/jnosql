/*
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
 */
package org.eclipse.jnosql.mapping.graph;

import org.apache.tinkerpop.gremlin.structure.Property;
import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.communication.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class DefaultPropertyTest {


    @Test
    public void shouldReturnErrorWhenKeyINull() {
        assertThrows(NullPointerException.class, () -> DefaultProperty.of(null, 10L));
    }

    @Test
    public void shouldReturnErrorWhenValueINull() {
        assertThrows(NullPointerException.class, () -> DefaultProperty.of("key", null));
    }

    @Test
    public void shouldReturnKey() {
        Property element = DefaultProperty.of("key", 10L);
        assertEquals("key", element.key());
    }

    @Test
    public void shouldReturnValue() {
        Property element = DefaultProperty.of("key", 10L);
        assertEquals(10L, element.value());
    }

    @Test
    public void shouldReturnValueAsObject() {
        long value = 10L;
        Property element = DefaultProperty.of("key", value);
        assertEquals(value, element.value());
    }


    @Test
    public void shouldCreateInstanceValue() {
        Property element = DefaultProperty.of("key", Value.of(10L));
        assertEquals(Value.of(10L), element.value());
        assertEquals("key", element.key());
        assertEquals(Value.of(10L), element.value());
    }

    @Test
    public void shouldToString() {
        Property element = DefaultProperty.of("key", 10L);
        assertEquals("DefaultProperty{key='key', value=10}", element.toString());
    }

    @Test
    public void shouldUnsupportedOperationException() {
        Property element = DefaultProperty.of("key", 10L);
        assertThrows(UnsupportedOperationException.class, element::remove);
        assertThrows(UnsupportedOperationException.class, element::element);
    }

    @Test
    public void shouldEqualsHashCode() {
        Property element = DefaultProperty.of("key", 10L);
        assertEquals(element, element);
        assertEquals(element, DefaultProperty.of("key", 10L));
        assertEquals(element.hashCode(), DefaultProperty.of("key", 10L).hashCode());
    }

    @Test
    public void shouldIsPresent(){
        Property element = DefaultProperty.of("key", 10L);
        Assertions.assertThat(element.isPresent()).isTrue();
    }

}