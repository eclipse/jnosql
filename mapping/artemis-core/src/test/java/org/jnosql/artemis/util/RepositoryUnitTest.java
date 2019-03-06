/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.artemis.util;

import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.jnosql.artemis.DatabaseType.COLUMN;
import static org.jnosql.artemis.DatabaseType.DOCUMENT;
import static org.jnosql.artemis.DatabaseType.GRAPH;
import static org.jnosql.artemis.DatabaseType.KEY_VALUE;
import static org.jnosql.artemis.DatabaseType.SHARED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepositoryUnitTest {

    @Mock
    private ConfigurationUnit unit;


    @Test
    public void shouldReturnNPEWhenParameterIsNull() {
        assertThrows(NullPointerException.class, () ->
            RepositoryUnit.of(null, unit));

        assertThrows(NullPointerException.class, () ->
                RepositoryUnit.of(UnitRepository.class, null));
    }

    @Test
    public void shouldReturnNPEWhenDatabaseIsNull() {
        when(unit.database()).thenReturn(null);
        RepositoryUnit repositoryUnit = RepositoryUnit.of(UnitRepository.class, this.unit);

        assertThrows(NullPointerException.class, () -> repositoryUnit.getDatabase());
    }

    @Test
    public void shouldReturnDatabase() {
        when(unit.database()).thenReturn("database");
        RepositoryUnit repositoryUnit = RepositoryUnit.of(UnitRepository.class, this.unit);
        assertEquals("database", repositoryUnit.getDatabase());
    }

    @Test
    public void shouldReturnGraph() {
        when(unit.repository()).thenReturn(GRAPH);
        RepositoryUnit repositoryUnit = RepositoryUnit.of(UnitRepository.class, this.unit);
        assertTrue(repositoryUnit.isGraph());
    }

    @Test
    public void shouldReturnKey() {
        when(unit.repository()).thenReturn(KEY_VALUE);
        RepositoryUnit repositoryUnit = RepositoryUnit.of(UnitRepository.class, this.unit);
        assertTrue(repositoryUnit.isKey());
    }

    @Test
    public void shouldReturnColumn() {
        when(unit.repository()).thenReturn(COLUMN);
        RepositoryUnit repositoryUnit = RepositoryUnit.of(UnitRepository.class, this.unit);
        assertTrue(repositoryUnit.isColumn());
    }

    @Test
    public void shouldReturnDocument() {
        when(unit.repository()).thenReturn(DOCUMENT);
        RepositoryUnit repositoryUnit = RepositoryUnit.of(UnitRepository.class, this.unit);
        assertTrue(repositoryUnit.isDocument());
    }



    @Test
    public void shouldReturnGraphWhenIsShared() {
        when(unit.repository()).thenReturn(SHARED);
        RepositoryUnit repositoryUnit = RepositoryUnit.of(UnitRepository.class, this.unit);
        assertTrue(repositoryUnit.isGraph());
    }

    @Test
    public void shouldReturnKeyIsShared() {
        when(unit.repository()).thenReturn(SHARED);
        RepositoryUnit repositoryUnit = RepositoryUnit.of(UnitRepository.class, this.unit);
        assertTrue(repositoryUnit.isKey());
    }

    @Test
    public void shouldReturnColumnIsShared() {
        when(unit.repository()).thenReturn(SHARED);
        RepositoryUnit repositoryUnit = RepositoryUnit.of(UnitRepository.class, this.unit);
        assertTrue(repositoryUnit.isColumn());
    }

    @Test
    public void shouldReturnDocumentIsShared() {
        when(unit.repository()).thenReturn(SHARED);
        RepositoryUnit repositoryUnit = RepositoryUnit.of(UnitRepository.class, this.unit);
        assertTrue(repositoryUnit.isDocument());
    }

    private interface UnitRepository extends Repository<String, String> {

    }
}