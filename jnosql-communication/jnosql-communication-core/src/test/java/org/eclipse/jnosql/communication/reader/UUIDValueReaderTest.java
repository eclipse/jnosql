/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.reader;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UUIDValueReaderTest {
    private final UUIDValueReader valueReader = new UUIDValueReader();

    @Test
    void shouldReadUUIDFromUUIDInstance() {
        UUID uuid = UUID.randomUUID();
        UUID result = valueReader.read(UUID.class, uuid);
        assertEquals(uuid, result);
    }

    @Test
    void shouldReadUUIDFromString() {
        UUID uuid = UUID.randomUUID();
        UUID result = valueReader.read(UUID.class, uuid.toString());
        assertEquals(uuid, result);
    }

    @Test
    void shouldReturnNullForInvalidString() {
        String invalidUUID = "invalid-uuid";
        assertThrows(IllegalArgumentException.class, () -> valueReader.read(UUID.class, invalidUUID));
    }

    @Test
    void shouldReturnNullForUnsupportedType() {
        Integer unsupportedValue = 42;
        UUID result = valueReader.read(UUID.class, unsupportedValue);
        assertNull(result);
    }

    @Test
    void shouldTestUUIDType() {
        assertTrue(valueReader.test(UUID.class));
    }

    @Test
    void shouldNotTestNonUUIDType() {
        assertFalse(valueReader.test(String.class));
    }
}
