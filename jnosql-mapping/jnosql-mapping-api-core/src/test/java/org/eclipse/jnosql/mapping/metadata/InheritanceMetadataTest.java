/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.metadata;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class InheritanceMetadataTest {

    @Test
    void shouldConstructorAndGetters() {
        String discriminatorValue = "testValue";
        String discriminatorColumn = "testColumn";
        Class<?> parentClass = Object.class;
        Class<?> entityClass = String.class;

        InheritanceMetadata metadata = new InheritanceMetadata(discriminatorValue, discriminatorColumn, parentClass, entityClass);

        assertEquals(discriminatorValue, metadata.discriminatorValue());
        assertEquals(discriminatorColumn, metadata.discriminatorColumn());
        assertEquals(parentClass, metadata.parent());
        assertEquals(entityClass, metadata.entity());
    }

    @Test
    void shouldEqualsAndHashCode() {
        InheritanceMetadata metadata1 = new InheritanceMetadata("value1", "column1", String.class, Integer.class);
        InheritanceMetadata metadata2 = new InheritanceMetadata("value1", "column1", String.class, Integer.class);
        InheritanceMetadata metadata3 = new InheritanceMetadata("value2", "column1", String.class, Integer.class);
        InheritanceMetadata metadata4 = new InheritanceMetadata("value1", "column2", String.class, Integer.class);
        InheritanceMetadata metadata5 = new InheritanceMetadata("value1", "column1", Integer.class, Integer.class);
        InheritanceMetadata metadata6 = new InheritanceMetadata("value1", "column1", String.class, String.class);

        // Reflexive
        assertEquals(metadata1, metadata1);
        // Symmetric
        assertEquals(metadata1, metadata2);
        assertEquals(metadata2, metadata1);
        assertEquals(metadata1, metadata2);

        assertNotEquals(metadata1, metadata4);
        assertNotEquals(metadata1, metadata5);

        assertNotEquals(metadata1, null);
        assertEquals(metadata1.hashCode(), metadata2.hashCode());
    }

    @Test
    void shouldIsParent() {
        Class<?> parentClass = String.class;
        InheritanceMetadata metadata = new InheritanceMetadata("value", "column", parentClass, Integer.class);

        assertTrue(metadata.isParent(parentClass));
        assertFalse(metadata.isParent(Integer.class));
    }

    @Test
    void shouldIsParentWithNull() {
        InheritanceMetadata metadata = new InheritanceMetadata("value", "column", String.class, Integer.class);
        assertThrows(NullPointerException.class, () -> metadata.isParent(null));
    }

    @Test
    void shouldToString() {
        InheritanceMetadata metadata = new InheritanceMetadata("testValue", "testColumn", String.class, Integer.class);
        String expected = "InheritanceMetadata{discriminatorValue='testValue', discriminatorColumn='testColumn', parent=class java.lang.String}";
        assertEquals(expected, metadata.toString());
    }
}