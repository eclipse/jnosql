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
 *   Elias Nogueira
 *
 */
package org.eclipse.jnosql.communication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ValueReaderDecoratorTest {

    private final ValueReaderDecorator serviceLoader = ValueReaderDecorator.getInstance();

    @Test
    @DisplayName("Should be able to convert")
    void shouldConvert() {
        Number convert = serviceLoader.read(Number.class, "10D");
        assertThat(convert).isEqualTo(10D);
    }

    @Test
    @DisplayName("Should be able to cast")
    void shouldCastObject() {
        Bean name = serviceLoader.read(Bean.class, new Bean());
        assertThat(name.getClass()).isEqualTo(Bean.class);
    }

    @Test
    @DisplayName("Should throw UnsupportedOperationException during read when class is not supported")
    void shouldReturnErrorWhenTypeIsNotSupported() {
        assertThatThrownBy(() -> serviceLoader.read(Bean.class, "name"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("The type class org.eclipse.jnosql.communication.ValueReaderDecoratorTest$Bean is not supported yet");
    }

    @Test
    @DisplayName("Should check for compatibility")
    void shouldReturnIfIsCompatible() {
        assertThat(serviceLoader.test(Integer.class)).isTrue();
    }

    @Test
    @DisplayName("Should check for incompatibility")
    void shouldReturnIfIsNotCompatible() {
        assertThat(serviceLoader.test(Bean.class)).isFalse();
        assertFalse(serviceLoader.test(Bean.class));
    }

    static class Bean {
        Bean() {
        }
    }
}
