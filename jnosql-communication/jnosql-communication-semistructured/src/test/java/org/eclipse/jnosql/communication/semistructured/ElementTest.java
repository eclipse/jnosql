/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;


import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ElementTest {

    private static final Value DEFAULT_VALUE = Value.of(12);

    @Test
    void shouldReturnNameWhenNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> Element.of(null, DEFAULT_VALUE));
    }

    @Test
    void shouldReturnNameWhenValueIsNull() {
        Element element = Element.of("Name", null);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(element.name()).isEqualTo("Name");
            softly.assertThat(element.value().isNull()).isTrue();
        });
    }

    @Test
    void shouldCreateAnColumnInstance() {
        String name = "name";
        Element element = Element.of(name, DEFAULT_VALUE);
        assertNotNull(element);
        assertEquals(name, element.name());
        assertEquals(DEFAULT_VALUE, element.value());
    }

    @Test
    void shouldBeEquals() {
        assertEquals(Element.of("name", DEFAULT_VALUE), Element.of("name", DEFAULT_VALUE));
    }

    @Test
    void shouldReturnGetObject() {
        Value value = Value.of("text");
        Element element = Element.of("name", value);
        assertEquals(value.get(), element.get());
    }

    @Test
    void shouldReturnGetClass() {
        Value value = Value.of("text");
        Element element = Element.of("name", value);
        assertEquals(value.get(String.class), element.get(String.class));
    }


    @Test
    void shouldReturnGetType() {
        Value value = Value.of("text");
        Element element = Element.of("name", value);
        TypeReference<List<String>> typeReference = new TypeReference<>() {
        };
        assertEquals(value.get(typeReference), element.get(typeReference));
    }
}
