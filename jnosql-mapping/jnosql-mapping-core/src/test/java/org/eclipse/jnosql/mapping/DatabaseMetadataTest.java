/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.eclipse.jnosql.mapping;

import jakarta.nosql.mapping.Database;
import jakarta.nosql.mapping.DatabaseType;
import org.eclipse.jnosql.mapping.DatabaseMetadata;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatabaseMetadataTest {

    @Test
    public void shouldReturnErrorWhenDatabaseIsNull() {
        assertThrows(NullPointerException.class, () -> DatabaseMetadata.of(null));
    }

    @Test
    public void shouldReturnMetadata() {
        Database database = Mockito.mock(Database.class);
        Mockito.when(database.value()).thenReturn(DatabaseType.COLUMN);
        Mockito.when(database.provider()).thenReturn("column");
        DatabaseMetadata metadata = DatabaseMetadata.of(database);
        assertEquals(DatabaseType.COLUMN, metadata.getType());
        assertEquals("column", metadata.getProvider());
    }

    @Test
    public void shouldReturnToString() {
        Database database = Mockito.mock(Database.class);
        Mockito.when(database.value()).thenReturn(DatabaseType.COLUMN);
        Mockito.when(database.provider()).thenReturn("column");
        DatabaseMetadata metadata = DatabaseMetadata.of(database);
        assertEquals("COLUMN@column", metadata.toString());
    }

    @Test
    public void shouldReturnToString2() {
        Database database = Mockito.mock(Database.class);
        Mockito.when(database.value()).thenReturn(DatabaseType.COLUMN);
        DatabaseMetadata metadata = DatabaseMetadata.of(database);
        assertEquals("COLUMN", metadata.toString());
    }
}