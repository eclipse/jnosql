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
package org.eclipse.jnosql.artemis.column.query;

import jakarta.nosql.mapping.Database;
import jakarta.nosql.mapping.DatabaseType;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.PersonRepository;
import jakarta.nosql.tck.entities.PersonRepositoryAsync;
import org.eclipse.jnosql.artemis.test.CDIExtension;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@CDIExtension
public class ColumnRepositoryExtensionTest {

    @Inject
    @Database(value = DatabaseType.COLUMN)
    private PersonRepository repository;

    @Inject
    @Database(value = DatabaseType.COLUMN, provider = "columnRepositoryMock")
    private PersonRepository repositoryMock;

    @Inject
    @Database(value = DatabaseType.COLUMN)
    private PersonRepositoryAsync repositoryAsync;

    @Inject
    @Database(value = DatabaseType.COLUMN, provider = "columnRepositoryMock")
    private PersonRepositoryAsync repositoryMockAsync;

    @Test
    public void shouldInitiateAsync() {
        assertNotNull(repositoryAsync);
    }

    @Test
    public void shouldGetQualifierAsync() {
        assertNotNull(repositoryMockAsync);
    }

    @Test
    public void shouldInitiate() {
        assertNotNull(repository);
        Person person = repository.save(Person.builder().build());
        assertEquals("Default", person.getName());
    }

    @Test
    public void shouldUseInstantiation(){
        assertNotNull(repositoryMock);
        Person person = repositoryMock.save(Person.builder().build());
        assertEquals("columnRepositoryMock", person.getName());
    }
}