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
package org.eclipse.jnosql.mapping.column.spi;

import jakarta.nosql.Database;
import jakarta.nosql.DatabaseType;
import jakarta.nosql.column.ColumnTemplate;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.PersonRepository;
import org.eclipse.jnosql.mapping.test.jupiter.CDIExtension;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
public class ColumnExtensionTest {

    @Inject
    @Database(value = DatabaseType.COLUMN, provider = "columnRepositoryMock")
    private ColumnTemplate templateMock;

    @Inject
    private ColumnTemplate template;

    @Inject
    @Database(value = DatabaseType.COLUMN)
    private PersonRepository repository;

    @Inject
    @Database(value = DatabaseType.COLUMN, provider = "columnRepositoryMock")
    private PersonRepository repositoryMock;

    @Test
    public void shouldInstance() {
        assertNotNull(template);
        assertNotNull(templateMock);
    }

    @Test
    public void shouldSave() {
        Person person = template.insert(Person.builder().build());
        Person personMock = templateMock.insert(Person.builder().build());

        assertEquals("Default", person.getName());
        assertEquals("columnRepositoryMock", personMock.getName());
    }

    @Test
    public void shouldInjectRepository() {
        assertNotNull(repository);
        assertNotNull(repositoryMock);
    }

    @Test
    public void shouldInjectTemplate() {
        assertNotNull(templateMock);
        assertNotNull(template);
    }
}